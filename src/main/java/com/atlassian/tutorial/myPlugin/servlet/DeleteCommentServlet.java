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
 * This servlet class helps user to delete a comment from database.
 * It uses HTTP method GET to get the parameters id and comment. 
 * It works with method deleteComment() from DatabaseWork.class.
 * 
 * 
 * */
@Scanned
@Named("DeleteCommentServlet")
public class DeleteCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DatabaseWork workWithDataBase;

	/*
	 * A constructor inject the servlet with the ActiveObjects 
	 * service and creates a new databaseWork object.
	 * 
	 * */
	@Inject
	public DeleteCommentServlet(ActiveObjects activeObjects) {
		this.workWithDataBase = new DatabaseWork(activeObjects);
	}

	/*
	 * This method makes a client request with parameters id and comment 
	 * and uses the method deleteComment() from DatabaseWork.class.
	 * It becomes a response from server, that redirects the user to the 
	 * page with message found by id with all comments.
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

		workWithDataBase.deleteComment(subject, comment);

		resp.sendRedirect("/confluence/plugins/servlet/details?id=" + subject);
	}
}
