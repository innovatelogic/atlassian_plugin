package com.atlassian.tutorial.myPlugin.servlet;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.user.UserManager;

/**
 * This servlet class helps user to add a comment to database.
 * It uses HTTP GET method to get the parameters id and comment. 
 * It uses UserManager to get the Confluence user and user avatar.
 * 
 * 
 * */
@Scanned
@Named("CommentServlet")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@ComponentImport
	private UserManager userManager;

	private String author, comment, subject, authorAvatar;
	private DatabaseWork workWithDataBase;
	private MessageEntity messageEntity;

	/*
	 * A constructor inject the servlet with the 
	 * ActiveObjects service and UserManager service.
	 * and creates a new databaseWork object.
	 * 
	 * */
	@Inject
	public CommentServlet(ActiveObjects activeObjects, UserManager userManager) {
		this.userManager = checkNotNull(userManager);
		this.workWithDataBase = new DatabaseWork(activeObjects);
	}

	/*
	 * This method makes a client request with parameters id and comment.
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

		comment = req.getParameter("comment");
		subject = req.getParameter("id");

		addCommentIfFieldsNotEmpty();

		resp.sendRedirect("/confluence/plugins/servlet/details?id=" + subject);
	}

	/*
	 * This method gets a message by id, checks user for the username,
	 * adds a user avatar and creates a date. It works with DatabaseWork.class 
	 * and uses addComment() method that adds a new comment to database, if
	 * comment not empty.
	 *  
	 * */
	protected void addCommentIfFieldsNotEmpty() {
		if (!comment.isEmpty()) {
			getMessageBySubject(subject);
			checkUser();

			try{
				authorAvatar = userManager.getUserProfile(userManager.getRemoteUsername())
						.getProfilePictureUri().getPath();
			}catch(Exception ex){
				authorAvatar = "";
			}
			
			SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			formatDate.format(new Date());
			
			workWithDataBase.addComment(messageEntity, author, comment, authorAvatar, formatDate.format(new Date()));
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

	/*
	* This method gets the author username, if the author not null.
	* 
	*/
	protected void checkUser() {
		try {
			author = userManager.getRemoteUsername();
		} catch (NullPointerException ex) {
			author = "anonymous";
		}
	}
}
