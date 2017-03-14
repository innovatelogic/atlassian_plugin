package com.atlassian.tutorial.myPlugin.servlet;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.tutorial.myPlugin.entity.MessageEntity;
import com.atlassian.tutorial.myPlugin.rest.MessageModel;
import com.atlassian.tutorial.myPlugin.databaseWork.DatabaseWork;

/**
 * This servlet class creates a new message map and 
 * renders the template details.vm in Confluence page.
 * 
 * 
 * */
@Scanned
@Named("DetailsServlet")
public class DetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@ComponentImport
	private TemplateRenderer templateRenderer;
	@ComponentImport
	static UserManager userManager;
	@ComponentImport
	private ActiveObjects activeObjects;

	static DatabaseWork workWithDataBase;
	private MessageEntity messageEntity1;
	private MessageModel messageEntity;
	static String subject, user;
	private Map<String, Object> messageMap;

	/*
	 * A constructor inject the servlet with the ActiveObjects service,
	 * TemplateRenderer and UserManager service
	 * and creates a new databaseWork object.
	 * 
	 * */
	@Inject
	public DetailsServlet(ActiveObjects activeObjects,
			TemplateRenderer templateRenderer, UserManager userManager) {
		this.templateRenderer = checkNotNull(templateRenderer);
		this.userManager = checkNotNull(userManager);
		this.workWithDataBase = new DatabaseWork(activeObjects);
	}

	/*
	 * This method makes a client request with parameter id and creates
	 * a new message map. The response from server sends character data, 
	 * by using the PrintWriter object and renders the template details.vm
	 * in Confluence page.
	 * 
	 * @param req
	 * @param resp
	 * 
	 * */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		subject = req.getParameter("id");

		createMessageMap();

		templateRenderer.render("details.vm", messageMap, resp.getWriter());
	}

	/*
	 * This method creates the new message map
	 * and puts the message, the username, all comments and
	 * 'likes' to the message inside this map.
	 * 
	 * */
	protected void createMessageMap() {
		messageMap = new LinkedHashMap<String, Object>();

		getMessageBySubject();
		checkUser();
		
		messageMap.put("messages", messageEntity);
		messageMap.put("comments", messageEntity1.getComments());
		messageMap.put("like", messageEntity1.getLikes().length);
		messageMap.put("user", user);
	}
	
	/*
	 * This method works with DatabaseWork.class 
	 * and MessageModel.class and gets a message by subject.
	 * It creates a new message model with parameters.
	 * 
	 * */
	protected void getMessageBySubject() {
		messageEntity1 = workWithDataBase.getMessage(subject);
		messageEntity = new MessageModel(messageEntity1.getAuthorAvatar(), messageEntity1.getAuthor(),
				messageEntity1.getContent(), messageEntity1.getSubject(), messageEntity1.getDate(),
				String.valueOf(messageEntity1.getID()), messageEntity1.getImage(), messageEntity1.getLikes().length);	
	}
    
	/*
	* This method gets the author username, if the author not null.
	*/
	protected void checkUser() {
		try {
			user = userManager.getRemoteUsername();
		} catch (NullPointerException ex) {
			user = "anonymous";
		}
	}
}
