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
 * This servlet class helps user to add a 'like' to the comment and save it in the database.
 * It uses HTTP method GET to get the parameters id and comment. 
 * It uses UserManager to get the Confluence user, that likes a comment.
 * 
 * 
 * */
@Scanned
@Named("CommentLikeServlet")
public class CommentLikeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DatabaseWork workWithDataBase;
	private String author;
	
	@ComponentImport
	private final UserManager userManager;

	/*
	 * A constructor inject the servlet with the 
	 * ActiveObjects service and UserManager service.
	 * and creates a new databaseWork object.
	 * 
	 * */
	@Inject
	public CommentLikeServlet(ActiveObjects activeObjects, UserManager userManager) {
		this.workWithDataBase = new DatabaseWork(activeObjects);
		this.userManager = checkNotNull(userManager);
	}
 
	/*
	 * This method makes a client request with parameters id and comment.
	 * It gets an author, looks for a comment and becomes a response 
	 * from server, that redirects the user to the page with 
	 * message found by id with all comments.
	 * 
	 * @param req
	 * @param resp
	 * 
	 * */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String subject = req.getParameter("id");
		String comment = req.getParameter("comment");
		
		getAuthor();
		
		workWithDataBase.addLikeCom(workWithDataBase.getComment(subject, comment), author);

		resp.sendRedirect("/confluence/plugins/servlet/details?id=" + subject);
	}
	
	/*
	* This method gets the username, if the user not null.
	*/
	protected void getAuthor() {
		author = (null != userManager.getRemoteUsername()) ? userManager
				.getRemoteUsername() : "anonymous";
	}
}
