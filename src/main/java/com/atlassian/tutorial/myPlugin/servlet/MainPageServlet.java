package com.atlassian.tutorial.myPlugin.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.templaterenderer.TemplateRenderer;

import static com.google.common.base.Preconditions.checkNotNull;

import com.atlassian.tutorial.myPlugin.rest.*;
import com.atlassian.tutorial.myPlugin.entity.*;
import com.atlassian.tutorial.myPlugin.databaseWork.DatabaseWork;

/**
 * This servlet class creates a new message map and 
 * renders the template mainpage.vm in Confluence page.
 * 
 * 
 * */
@Scanned
@Named("MainPageServlet")
public class MainPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@ComponentImport
	private TemplateRenderer templateRenderer;

	private List<MessageEntity> messageList1;
	private List<MessageModel> messageList;

	public static DatabaseWork workWithDataBase;
    
	private static Map<String, Object> messageMap;
	String image; 

	/*
	 * A constructor inject the servlet with the ActiveObjects service,
	 * TemplateRenderer service and creates a new databaseWork object.
	 * 
	 * */
	@Inject
	public MainPageServlet(ActiveObjects activeObjects,
			TemplateRenderer templateRenderer) {
		this.templateRenderer = checkNotNull(templateRenderer);
		this.workWithDataBase = new DatabaseWork(activeObjects);
	}

	/*
	 * This method creates a new message map and 
	 * renders the template mainpage.vm with 
	 * TemplateRenderer service in Confluence page.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//String subject = req.getParameter("id");
		//String image = req.getParameter("imageName");
		
		createMessageMap();
		
		//workWithDataBase.addLikeMsg(workWithDataBase.getMessage(subject), subject, author);

		templateRenderer.render("mainpage.vm", messageMap, resp.getWriter());
	}

	/*
	 * This method creates the new message map
	 * and puts all the parameters of message inside this map.
	 * 
	 * */
	protected void createMessageMap() {
		messageMap = new LinkedHashMap<String, Object>();
		messageList = new LinkedList<MessageModel>();
		messageList1 = workWithDataBase.getMessages();

		sortList();
		
		for(int i = 0; i<messageList1.size(); i++){
			MessageEntity entity = messageList1.get(i);
			messageList.add(new MessageModel(entity.getAuthorAvatar(), entity.getAuthor(),
					entity.getContent(), entity.getSubject(), entity.getDate(), 
					String.valueOf(entity.getID()), entity.getImage(), entity.getLikes().length));			
		}	
		messageMap.put("messages", messageList);
	}
	
	/*
	 * This method sorts all message entities from messageList1 by date.
	 * 
	 *  @return o2.getDate().compareTo(o1.getDate())
	 * 
	 * */
	protected void sortList() {
		Collections.sort(messageList1, new Comparator<MessageEntity>() {
			@Override
			public int compare(MessageEntity o1, MessageEntity o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});
	}
}
