package com.atlassian.tutorial.myPlugin.macro;

import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.XhtmlException;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.confluence.xhtml.api.MacroDefinition;
import com.atlassian.confluence.xhtml.api.MacroDefinitionHandler;
import com.atlassian.confluence.xhtml.api.XhtmlContent;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.tutorial.myPlugin.entity.MessageEntity;
import com.atlassian.tutorial.myPlugin.rest.MessageModel;
import com.atlassian.tutorial.myPlugin.servlet.DetailsServlet;
import com.atlassian.tutorial.myPlugin.databaseWork.DatabaseWork;

@Scanned
@Named("DetailsMacro")
public class DetailsMacro implements Macro{
	
	@ComponentImport
	private final XhtmlContent xhtmlUtils;
	@ComponentImport
	private ActiveObjects activeObjects;
	@ComponentImport
	private final WebResourceManager webResourceManager;
	static String subject, user;
	private MessageEntity messageEntity1;
	private MessageModel messageEntity;
	private Map<String, Object> messageMap;
	
	@Inject
	public DetailsMacro(XhtmlContent xhtmlUtils, WebResourceManager webResourceManager) {
		this.xhtmlUtils = xhtmlUtils;
		this.webResourceManager = webResourceManager;
	}

	@Override
	public String execute(Map<String, String> parameters, String bodyContent,
			ConversionContext conversionContext) throws MacroExecutionException {
		
		messageMap = MacroUtils.defaultVelocityContext();
		
		//subject = DetailsServlet.subject;

		createMessageMap();
		
			String template = "details.vm";

		 if("mobile".equals(conversionContext.getOutputDeviceType())) {
				webResourceManager.requireResourcesForContext("${atlassian.plugin.key}:shout-mobile");
				template = "mobile-details.vm";
			}
			else {
				webResourceManager.requireResourcesForContext("${atlassian.plugin.key}:shout");
				template = "details.vm";
			}
		 
		
		 return VelocityUtils.getRenderedTemplate(template, messageMap);
	}

	protected void createMessageMap() {

		messageMap = new LinkedHashMap<String, Object>();

		//getMessageBySubject();
		//checkUser();
		
		messageMap.put("messages", messageEntity);
		messageMap.put("comments", messageEntity1.getComments());
		messageMap.put("like", messageEntity1.getLikes().length);
		messageMap.put("user", user);
	}
/*
	protected void getMessageBySubject() {
		messageEntity1 = DetailsServlet.databaseWork.getMessage(subject);
		messageEntity = new MessageModel(messageEntity1.getAuthor(),messageEntity1.getAuthorAvatar(),
				messageEntity1.getSubject(),messageEntity1.getContent(),messageEntity1.getDate(),String.valueOf(messageEntity1.getID()));
		
	}


	protected void checkUser() {
		try {
			user = DetailsServlet.userManager.getRemoteUsername();
		} catch (NullPointerException ex) {
			user = "anonymous";
		}
	}
*/	
	@Override
	public BodyType getBodyType() {
		return BodyType.NONE;
	}

	@Override
	public OutputType getOutputType() {
		return OutputType.BLOCK;
	}

}



