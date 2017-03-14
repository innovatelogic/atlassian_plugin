package com.atlassian.tutorial.myPlugin.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.tutorial.myPlugin.entity.*;
import com.atlassian.tutorial.myPlugin.databaseWork.DatabaseWork;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;

/**
 * This servlet class helps user to edit a message from database.
 * It uses HTTP method GET to get the parameters id, currentComment and comment. 
 * It works with method upgradeMessage() from DatabaseWork.class.
 * 
 * 
 * */
@Scanned
@Named("EditMessageServlet")
public class EditMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DatabaseWork workWithDataBase;
	private MessageEntity messageEntity;
	private String currentMessage, newMessage, subject;

	/*
	 * A constructor inject the servlet with the 
	 * ActiveObjects and creates a new databaseWork object.
	 * 
	 * */
	@Inject
	public EditMessageServlet(ActiveObjects activeObjects) {
		this.workWithDataBase = new DatabaseWork(activeObjects);
	}

	/*
	 * This method makes a client request with parameters id, currentComment and newComment.
	 * It looks if all parameters for the message was given and becomes 
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

		currentMessage = req.getParameter("currentComment");
		newMessage = req.getParameter("comment");
		subject = req.getParameter("id");

		upgradeMessageIfFieldsNotEmpty();

		resp.sendRedirect("/confluence/plugins/servlet/details?id=" + subject);
	}

	/*
	 * This method updates the message, if it is not empty.
	 * It works with DatabaseWork.class and uses upgradeMessage() method that 
	 * upgrades message from the database, if message not empty.
	 *  
	 * */
	protected void upgradeMessageIfFieldsNotEmpty() {
		if (!currentMessage.isEmpty() && !currentMessage.equals(newMessage)
				&& !newMessage.isEmpty()) {
			getMessageBySubject(subject);
			workWithDataBase.upgradeMessage(messageEntity, currentMessage,
					newMessage);
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
