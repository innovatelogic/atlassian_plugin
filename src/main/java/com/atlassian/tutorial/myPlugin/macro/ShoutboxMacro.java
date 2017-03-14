package com.atlassian.tutorial.myPlugin.macro;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.confluence.xhtml.api.XhtmlContent;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.tutorial.myPlugin.databaseWork.DatabaseWork;
import com.atlassian.tutorial.myPlugin.entity.*;
import com.atlassian.tutorial.myPlugin.rest.*;
import com.atlassian.tutorial.myPlugin.servlet.MainPageServlet;

/**
 * This class is using the Confluence Macro interface.
 * The created macro are invoked through the execute() 
 * method of this class with the following parameters: 
 * parameters, bodyContent, conversionContext.
 * It renders velocity templates mainpage.vm and 
 * mobile-mainpage.vm to Confluence.
 * 
 * 
 * */
@Scanned
@Named("ShoutboxMacro")
public class ShoutboxMacro implements Macro{
	
	@ComponentImport
	private final XhtmlContent xhtmlUtils;
	@ComponentImport
	private ActiveObjects activeObjects;
	@ComponentImport
	private final WebResourceManager webResourceManager;
	
	private List<MessageEntity> messageList1;
	private List<MessageModel> messageList;
	private Map<String, Object> messageMap;
	
	/*
	 * Constructor for ShoutboxMacro class
	 * 
	 * @param xhtmlUtils
	 * @param webResourceManager
	 * 
	 * */
	@Inject
	public ShoutboxMacro(XhtmlContent xhtmlUtils, WebResourceManager webResourceManager) {
		this.xhtmlUtils = xhtmlUtils;
		this.webResourceManager = webResourceManager;
	}

	/*
	 * This method controls what this macro actually does.
	 * With arguments of this method the created macro can examine the body 
	 * of a given Confluence page, convert the data into a String 
	 * and return the storage format of the macros so that the macro 
	 * can display this information.
	 * This method makes the selection of different web resources and 
	 * a different template for Confluence Mobile. The web resources are selected 
	 * by context which is recommended if you have multiple web resources in your macro.
	 * 
	 * @param parameters
	 * @param bodyContent
	 * @param conversionContext
	 * @return VelocityUtils.getRenderedTemplate(template, messageMap)
	 * 
	 * */
	@Override
	public String execute(Map<String, String> parameters, String bodyContent,
			ConversionContext conversionContext) throws MacroExecutionException {
		
		messageMap = MacroUtils.defaultVelocityContext();
		createMessageMap();
		
		String template = "mainpage.vm";

		if("mobile".equals(conversionContext.getOutputDeviceType())) {
				webResourceManager.requireResourcesForContext("${atlassian.plugin.key}:shout-mobile");
				template = "mobile-mainpage.vm";
			}
			else {
				webResourceManager.requireResourcesForContext("${atlassian.plugin.key}:shout");
				template = "mainpage.vm";
			}	
		return VelocityUtils.getRenderedTemplate(template, messageMap);
	}

	/*
	 * This method creates a new message model as a LinkedList,
	 * sorts all message entities by date and makes a message map 
	 * of all messages currently stored in the database.
	 * 
	 * */
	protected void createMessageMap() {
		messageList = new LinkedList<MessageModel>();
		messageList1 =  MainPageServlet.workWithDataBase.getMessages();
		
		sortList();
		
		for(int i = 0; i<messageList1.size(); i++){
			MessageEntity entity = messageList1.get(i);
			messageList.add(new MessageModel(entity.getAuthorAvatar(), entity.getAuthor(),
											 entity.getContent(), entity.getSubject(),
											 entity.getDate(), String.valueOf(entity.getID()), 
											 entity.getImage(), entity.getLikes().length));		
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
	
	/*
	 * This method defines that your macro's body type is NONE.
	 * 
	 * @return BodyType.NONE
	 * 
	 * */
	@Override
	public BodyType getBodyType() {
		return BodyType.NONE;
	}

	/*
	 * This method defines that your macro's output type is BLOCK.
	 * 
	 * @return OutputType.BLOCK
	 * 
	 * */
	@Override
	public OutputType getOutputType() {
		return OutputType.BLOCK;
	}
}
