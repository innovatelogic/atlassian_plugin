package com.atlassian.tutorial.myPlugin.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.tutorial.myPlugin.databaseWork.*;
import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import static com.google.common.base.Preconditions.checkNotNull;

import com.atlassian.sal.api.user.UserManager;

/**
 * This servlet class helps user to add a 'like' to the message and save it in the database.
 * It uses HTTP GET method to get the parameter id from message. 
 * It uses UserManager to get the Confluence user, that likes a message.
 * 
 * 
 * */
@Scanned
@Named("MessageLikeServlet")
public class MessageLikeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DatabaseWork workWithDataBase;
	
	@ComponentImport
	private final UserManager userManager;
	private String author;
	
	/*
	 * A constructor inject the servlet with the 
	 * ActiveObjects service and UserManager service
	 * and creates a new databaseWork object.
	 * 
	 * */
	@Inject
	public MessageLikeServlet(ActiveObjects activeObjects, UserManager userManager) {
		this.workWithDataBase = new DatabaseWork(activeObjects);
		this.userManager = checkNotNull(userManager);
	}

	/*
	 * This method makes a client request with parameters id from message.
	 * It gets the username from user that created a message. It uses a
	 * method addLikeMsg() from DatabaseWork.class that gets a message and 
	 * adds a 'like' to this message. 
	 * The client becomes a response from server, that redirects the user 
	 * to the page with message found by id with all comments.
	 * 
	 * @param req
	 * @param resp
	 * 
	 * */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String subject = req.getParameter("id");
		
		getAuthor();
		
		workWithDataBase.addLikeMsg(workWithDataBase.getMessage(subject), subject, author);
		
		resp.sendRedirect("/confluence/plugins/servlet/details?id=" + subject);
	}
	
	/*
	* This method gets the username, if the user not null.
	*/
	protected void getAuthor() {
		try {
			author = userManager.getRemoteUsername();
		} catch (NullPointerException ex) {
			author = "anonymous";
		}
	}
}
