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
import com.atlassian.tutorial.myPlugin.entity.MessageEntity;
import com.atlassian.tutorial.myPlugin.databaseWork.DatabaseWork;

/**
 * This servlet class helps user to edit a comment from database.
 * It uses HTTP method GET to get the parameters id, currentComment and comment. 
 * It works with method upgradeComment() from DatabaseWork.class.
 * `````
 * 
 * */
@Scanned
@Named("EditCommentServlet")
public class EditCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DatabaseWork workWithDataBase;
	private String currentComment, newComment, subject;
	private MessageEntity messageEntity;

	/*
	 * A constructor inject the servlet with the 
	 * ActiveObjects and creates a new databaseWork object.
	 * 
	 * */
	@Inject
	public EditCommentServlet(ActiveObjects activeObjects) {
		this.workWithDataBase = new DatabaseWork(activeObjects);
	}

	/*
	 * This method makes a client request with parameters id, currentComment and comment.
	 * It looks if all parameters for the comment was given and becomes 
	 * a response from server, that redirects the user to the page with 
	 * message found by id with all comments.
	 * 
	 * @param req
	 * @param resp
	 * 
	 * */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		currentComment = req.getParameter("currentComment");
		newComment = req.getParameter("comment");
		subject = req.getParameter("id");

		upgradeCommentIfFieldsNotEmpty();

		resp.sendRedirect("/confluence/plugins/servlet/details?id=" + subject);
	}

	/*
	 * This method updates the comment, if it is not empty.
	 * It works with DatabaseWork.class and uses upgradeComment() method that 
	 * upgrades comment from the database, if comment not empty.
	 *  
	 * */
	protected void upgradeCommentIfFieldsNotEmpty() {
		if (!currentComment.isEmpty() && !currentComment.equals(newComment)
				&& !newComment.isEmpty()) {

			getMessageBySubject(subject);

			workWithDataBase.upgradeComment(messageEntity.getComments(),
					currentComment, newComment);
		}
	}

	/*
	 * This method works with DatabaseWork.class 
	 * and gets a message by subject.
	 * 
	 * @param subject
	 * 
	 * */
	protected void getMessageBySubject(String subject) {
		messageEntity = workWithDataBase.getMessage(subject);
	}
}
