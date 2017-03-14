package com.atlassian.tutorial.myPlugin.rest;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.user.UserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.atlassian.tutorial.myPlugin.entity.MessageEntity;
import com.atlassian.tutorial.myPlugin.databaseWork.*;

/**
 * A REST Service class.
 * It based on JAX-RS annotations. Specifically, it uses Jersey, 
 * which is the JAX-RS reference implementation.
 * 
 *  
 * */
@Path("message")
public class MyRestResource {

	private DatabaseWork withDataBase;

	private final UserManager userManager;

	@Inject
	public MyRestResource(ActiveObjects activeObjects, UserManager userManager) {
		this.withDataBase = new DatabaseWork(activeObjects);
		this.userManager = checkNotNull(userManager);
	}

	Gson gson;

	/*
	 * Default method
	 * 
	 * @QueryParam key
	 * 
	 * */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getMessage(@QueryParam("key") String key) {
		if (key != null)
			return Response.ok(
					new MyRestResourceModel(key, getMessageFromKey(key)))
					.build();
		else
			return Response.ok(
					new MyRestResourceModel("default", "Hello World")).build();
	}

	/*
	 * This method retrieves the resources with parameters in XML/JSON 
	 * from database by parameter subject.
	 * To get the messages by subject use this URL-format:
	 * http://host:port/confluence/rest/shoutbox/1.0/message/subjectMessage
	 * 
	 * */
	@GET
	@Produces({ MediaType.APPLICATION_JSON})
	@Path("/{subjectMessage}")
	public Response getMessageFromPath(@PathParam("subjectMessage") String subjectMessage) {
		final ArrayList<MessageEntity> messageEntitys = withDataBase.getMessageForJSON(subjectMessage);
		gson = new GsonBuilder().create();
		String json = "";

		for(MessageEntity messageEntity : messageEntitys){
			json += gson.toJson(new MessageModel(messageEntity.getAuthor(), 
											messageEntity.getAuthorAvatar(), 
											messageEntity.getSubject(),
											messageEntity.getContent(), 
											messageEntity.getDate()));
		}	
		return Response.ok(json).build();
	}

	/*
	 * This method retrieves all resources with parameters in XML/JSON from database
	 * To get all messages use this URL-format:
	 * http://host:port/confluence/rest/shoutbox/1.0/message/all
	 * 
	 * */
	@GET
	@Produces({ MediaType.APPLICATION_JSON})
	@Path("/all")
	public Response getMessages() {
		gson = new GsonBuilder().create();
		String json = "";
		for (MessageEntity messageEntity : withDataBase.getMessages()) {
			json += gson.toJson(new MessageModel(messageEntity.getAuthor(),
											messageEntity.getAuthorAvatar(),
											messageEntity.getSubject(), 
											messageEntity.getContent(), 
											messageEntity.getDate()));
		}
		return Response.ok(json).build();
	}
	
	/*
	 * This method adds a new resource to database. HTTP Method POST
	 * To add a new message use this URL-format:
	 * http://host:port/confluence/rest/shoutbox/1.0/message/add?subject=testSubject&content=testContent
	 * 
	 * @QueryParam subject
	 * @QueryParam content
	 * 
	 * */	
//	@RolesAllowed("user")
	@POST
	@Produces({ MediaType.APPLICATION_JSON})
	@Path("/add")
	public Response setMessageReq(@QueryParam("subject") String subject,
								  @QueryParam("content") String content) {
		String authorAvatar;
		try{
			authorAvatar = userManager.getUserProfile(userManager.getRemoteUsername())
					.getProfilePictureUri().getPath();
		}catch(NullPointerException ex){
			authorAvatar = "";
		}
		withDataBase.addNewMessage(subject, content, getDate(), getAuthor(), authorAvatar, "");

		return Response.ok(new MessageModel(getAuthor(), authorAvatar, subject, content, getDate())).build();
	}	

	/*
	 * This method adds a new resource to database. HTTP Method GET
	 * To add a new message use this URL-format:
	 * http://host:port/confluence/rest/shoutbox/1.0/message/add?subject=testSubject&content=testContent
	 * 
	 * @PathParam subject
	 * @PathParam content
	 * 
	 * */
	@GET
	@Produces({ MediaType.APPLICATION_JSON})
	@Path("/{subject}/{content}")
	public Response setMessage(@PathParam("subject") String subject,
							   @PathParam("content") String content) {
		String authorAvatar;
		try{
			authorAvatar = userManager.getUserProfile(userManager.getRemoteUsername())
					.getProfilePictureUri().getPath();
		}catch(NullPointerException ex){
			authorAvatar = "";
		}
		withDataBase.addNewMessage(subject, content, getDate(), getAuthor(), authorAvatar, "");

		return Response.ok(new MessageModel(getAuthor(), authorAvatar, subject, content, getDate())).build();
	}

	/*
	* @return the date of the object
	*/
	protected Date getDate() {
		return new Date();
	}

	/*
	* @return the author of the object
	*/
	protected String getAuthor() {
		return (null != userManager.getRemoteUsername()) ? userManager
				.getRemoteUsername() : "anonymous";
	}

	/*
	 * Default method
	 * 
	 * @QueryParam key
	 * @return json
	 * 
	 * */
	private String getMessageFromKey(String key) {
		String json = gson.toJson(withDataBase.getMessage(key));
		return json;
	}
}
