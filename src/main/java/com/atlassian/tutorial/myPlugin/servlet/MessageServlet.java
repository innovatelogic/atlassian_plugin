package com.atlassian.tutorial.myPlugin.servlet;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.tutorial.myPlugin.databaseWork.DatabaseWork;

import static com.google.common.base.Preconditions.*;

/**
 * This servlet class helps user to add a new message to database.
 * It uses HTTP GET method to get the parameters subject, content and imageName. 
 * It uses HTTP POST method to render the template message.vm with 
 * TemplateRenderer service to Confluence. It uses UserManager to get the 
 * Confluence user and user avatar.
 * 
 * 
 * */
@Scanned
@Named("MessageServlet")
public class MessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TemplateRenderer templateRenderer;
    private final UserManager userManager;

	static DatabaseWork workWithDataBase;
	private String subject, content, author, authorAvatar, image;
	private Date date;

	/*
	 * A constructor inject the servlet with the ActiveObjects service,
	 * TemplateRenderer and UserManager service
	 * and creates a new databaseWork object.
	 * 
	 * */
	@Inject
	public MessageServlet(ActiveObjects activeObjects,
			TemplateRenderer templateRenderer, UserManager userManager) {
		this.templateRenderer = checkNotNull(templateRenderer);
		this.userManager = checkNotNull(userManager);
		this.workWithDataBase = new DatabaseWork(activeObjects);	
	}

	/*
	 * HTTP POST method to render the template message.vm with 
     * TemplateRenderer service to Confluence.
     * 
	 * @param req
	 * @param resp
	 * 
	 * */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		templateRenderer.render("message.vm", resp.getWriter());
	}

	/*
	 * HTTP GET method makes a client request with parameters subject, content and imageName.
	 * It looks if subject and content parameters for the message was given and becomes 
	 * a response from server, that redirects the user to the mainpage of this plugin.
	 * 
	 * @param req
	 * @param resp
	 * 
	 * */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		subject = req.getParameter("subject");
		content = req.getParameter("content");
		image = req.getParameter("imageName");
		System.out.println(image);	
		
		addNewMessageIfFieldsNotEmpty();

		//resp.sendRedirect("/confluence/plugins/servlet/main");
		resp.sendRedirect("/confluence/display/SHOUT/shoutbox+Home");
	}

	/*
	 * This method looks if subject and content parameters are not empty.
	 * It adds a username, user avatar and a date parameters to the message. 
	 * It works with DatabaseWork.class and uses addNewMessage() method that adds 
	 * a new message to database, if subject and content parameters was given.
	 *  
	 * */
	protected void addNewMessageIfFieldsNotEmpty() {
		if (!subject.isEmpty() && !content.isEmpty()) {
			getDate();
			getAuthor();
			
			try{
				authorAvatar = userManager.getUserProfile(userManager.getRemoteUsername())
						.getProfilePictureUri().getPath();
			}catch(NullPointerException ex){
				authorAvatar = "";
			}
			
			workWithDataBase.addNewMessage(subject, content, date, author, authorAvatar, image);
		}
	}

	/*
	* This method gets the author username, if the author not null.
	* 
	*/
	protected void getAuthor() {
		try {
			author = userManager.getRemoteUsername();
		} catch (NullPointerException ex) {
			author = "anonymous";
		}
	}

	/*
	 * This method gets the date for the created message.
	 * 
	 * */
	protected void getDate() {
		date = new Date();
	}
}
