package com.atlassian.tutorial.myPlugin.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.tutorial.myPlugin.databaseWork.DatabaseWork;

/**
 * This servlet class helps user to delete a message from database.
 * It uses HTTP method GET to get the parameter subject. 
 * It works with method deleteMessage() from DatabaseWork.class.
 * 
 * 
 * */
@Scanned
@Named("DeleteMessageServlet")
public class DeleteMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DatabaseWork workWithDataBase;

	/*
	 * A constructor inject the servlet with the ActiveObjects 
	 * service and creates a new databaseWork object.
	 * 
	 * */
	@Inject
	public DeleteMessageServlet(ActiveObjects activeObjects) {
		this.workWithDataBase = new DatabaseWork(activeObjects);
	}

	/*
	 * This method makes a client request with parameter subject 
	 * and uses the method deleteMessage() from DatabaseWork.class.
	 * It becomes a response from server, that redirects the user 
	 * to the mainpage of this plugin.
	 * 
	 * @param req
	 * @param resp
	 * 
	 * */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String subject = req.getParameter("subject");
		
		workWithDataBase.deleteMessage(subject);
		//resp.sendRedirect("/confluence/plugins/servlet/main");
		resp.sendRedirect("/confluence/display/SHOUT/shoutbox+Home");
		
	}
}
