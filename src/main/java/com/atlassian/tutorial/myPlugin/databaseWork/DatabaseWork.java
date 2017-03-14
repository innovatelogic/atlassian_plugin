package com.atlassian.tutorial.myPlugin.databaseWork;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.atlassian.tutorial.myPlugin.entity.CommentEntity;
import com.atlassian.tutorial.myPlugin.entity.MessageEntity;
import com.atlassian.tutorial.myPlugin.entity.LikeCommentEntity;
import com.atlassian.tutorial.myPlugin.entity.LikeMessageEntity;
import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;

/**
 * This class implements all management functions for message and comment objects. 
 * This Java-class is responsible for the persisting of entities to database.
 * 
 * 
 * */
@Scanned
public class DatabaseWork implements IDatabaseWork {
	
	private ActiveObjects activeObjects;
	
	public DatabaseWork(ActiveObjects activeObjects) {
		this.activeObjects = activeObjects;
	}

	/*
	 * This method applies a 'like' functionality to the comment.
	 * Adds a new 'like' to like-counter, on the like-button click.
	 * One more click on the like-button, causes that a 'like'-object will be
	 * deleted from the like-counter. It creates a new 'like'-entity class,
	 * sets some of its values and persists this entity to the database.
	 * 
	 * @param commentEntity
	 * @param author
	 * 
	 * */
	public void addLikeCom(final CommentEntity messageEntity, final String author) {

		boolean flag = false;
		for(LikeCommentEntity like : messageEntity.getLikes()){
			if(author.equals(like.getAuthor())){
				activeObjects.delete(like);
				flag = true;
			}
		}
		if(!flag){
			LikeCommentEntity likeEntity = activeObjects.create(LikeCommentEntity.class);
			likeEntity.setPost(messageEntity);
			likeEntity.setAuthor(author);
			likeEntity.setLike(likeEntity.getLike()+1);
			likeEntity.save();
		}					
	}
			
	/*
	 * This method applies a 'like' functionality to the message.
	 * Adds a new 'like' to like-counter, on the like-button click.
	 * One more click on the like-button, causes that a 'like'-object will be
	 * deleted from the like-counter. It creates a new 'like'-entity class,
	 * sets some of its values and persists this entity to the database.
	 * 
	 * @param messageEntity
	 * @param subject
	 * @param author
	 * 
	 * */
	public void addLikeMsg(final MessageEntity messageEntity, final String subject,
			final String author) {
		try{
			LikeMessageEntity messageEntityLike = activeObjects.get(
					LikeMessageEntity.class, getLike(subject, author).getID());
			activeObjects.delete(messageEntityLike);
		}catch(Exception ex){
			LikeMessageEntity likeEntity = activeObjects.create(LikeMessageEntity.class);
			likeEntity.setPost(messageEntity);
			likeEntity.setAuthor(author);
			likeEntity.setLike(likeEntity.getLike()+1);
			likeEntity.save();
		}		
	}
	
	/*
	 * This method gets a 'like'. It calls getMessage() method and returns
	 * commentEntity object. Finds a 'like' by parameter. 
	 * 
	 * @param subject
	 * @param author
	 * 
	 * */
	public LikeMessageEntity getLike(String subject, String author) {
		MessageEntity msg = getMessage(subject);
		for (LikeMessageEntity commentEntity : msg.getLikes()) {
			if (author.equals(commentEntity.getAuthor())) {
				return commentEntity;
			}
		}
		return null;
	}
	
	/*
	 * This method deletes a comment object from database. 
	 * Removal is done by activeObjects.delete, which consists an instance messageEntity.
	 * 
	 * @param subject
	 * @param comment
	 * 
	 * */
	public void deleteComment(final String subject, final String comment) {
		CommentEntity messageEntity = activeObjects.get(
				CommentEntity.class, getComment(subject, comment).getID());
		try{
			deleteAllCommentLike(messageEntity);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		activeObjects.delete(messageEntity);
	}

	/*
	 * This method deletes a message object from database. 
	 * Removal is done by activeObjects.delete, which consists an instance messageEntity.
	 * When a message has comment objects, the method deleteAllComments() will be called.
	 * When a message has 'like'-objects, the method deleteAllLike() will be called.
	 * 
	 * @param subject
	 * @param comment
	 * 
	 * */
	public void deleteMessage(final String subject) {
		MessageEntity messageEntity = activeObjects.get(
				MessageEntity.class, getMessage(subject).getID());
		deleteAllComments(messageEntity);
		deleteAllLike(messageEntity);
		activeObjects.delete(messageEntity);
	}

	/*
	 * This method removes all activeObjects instances 'like' from messages.
	 * 
	 * @param msg
	 * 
	 * */
	public void deleteAllLike(MessageEntity msg) {
		LikeMessageEntity[] likes = msg.getLikes();
		for(final LikeMessageEntity like : likes){
					activeObjects.delete(like);
		}
	}
	
	/*
	 * This method removes all comment objects from a message entity with a special ID.
	 * At first it gets all comment entities, removes all 'likes' from comment entities 
	 * and then removes all comments.
	 * 
	 * @param messageEntity
	 * 
	 * */
	public void deleteAllComments(MessageEntity messageEntity) {
		CommentEntity[] commentEntities = messageEntity.getComments();
		for (CommentEntity commentEntity : commentEntities) {
			deleteAllCommentLike(commentEntity);
			deleteComment(String.valueOf(messageEntity.getID()),
					commentEntity.getComment());
		}
	}
	
	/*
	 * This method removes all 'like' objects from a comment entity.
	 * At first it gets all 'likes' from comment entities 
	 * and then removes all 'likes'.
	 * 
	 * @param commentEntity
	 * 
	 * */
	public void deleteAllCommentLike(CommentEntity commentEntity){
		LikeCommentEntity[] likes = commentEntity.getLikes();
		for(final LikeCommentEntity like : likes){
					activeObjects.delete(like);
		}
	}

	/*
	 * This method searches for a comment. It calls getMessage()-method,
	 * that gives back a message model, that belongs to this comment. 
	 * 
	 * @param subject
	 * @param comment
	 * @return commentEntity
	 * 
	 * */
	public CommentEntity getComment(String subject, String comment) {
		MessageEntity msg = getMessage(subject);
		for (CommentEntity commentEntity : msg.getComments()) {
			if (comment.equals(commentEntity.getComment())) {
				return commentEntity;
			}
		}
		return null;
	}

	/*
	 * The method updates a message entity. It changes and sets new some of its values
	 * and saves this changes to the database.
	 * 
	 * @param messageEntity
	 * @param currentMessage
	 * @param newMessage
	 * 
	 * */
	public void upgradeMessage(final MessageEntity messageEntity,
			final String currentMessage, final String newMessage) {
		messageEntity.setAuthor(messageEntity.getAuthor());
		messageEntity.setDate(messageEntity.getDate());
		messageEntity.setSubject(messageEntity.getSubject());
		messageEntity.setContent(newMessage);
		messageEntity.save();
	}

	/*
	 * The method updates a comment entity. It changes and sets new some of its values 
	 * and saves these changes to the database.
	 * 
	 * @param messageEntity
	 * @param currentMessage
	 * @param newMessage
	 * 
	 * */
	public void upgradeComment(final CommentEntity[] commentEntities,
			final String currentComment, final String newComment) {
		for (CommentEntity commentEntity : commentEntities) {
			if (currentComment.equals(commentEntity.getComment())
					&& currentComment.toCharArray().length < 500) {
				commentEntity.setPost(commentEntity.getPost());
				commentEntity.setAuthor(commentEntity.getAuthor());
				commentEntity.setComment(newComment);
				commentEntity.save();
			}
		}
	}

	/*
	 * The method used for creating a new comment object.
	 * It creates an ActiveObjects exemplar and fills the fields of this object.
	 * The method returns the created commentEntity object. At first it creates a new  comment entity,
	 * sets some of its values and persists this entity to the database.
	 * 
	 * @param messageEntity
	 * @param author
	 * @param date
	 * @param comment
	 * @param authorAvatar
	 * @return commentEntity
	 * 
	 * */
	public CommentEntity addComment(final MessageEntity messageEntity, final String author, 
			final String comment, final String authorAvatar, final String date) {
		final CommentEntity commentEntity = activeObjects.create(CommentEntity.class);

		if (comment.toCharArray().length < 500) {
			commentEntity.setPost(messageEntity);
			commentEntity.setAuthor(author);
			commentEntity.setDate(date);
			commentEntity.setComment(comment);
			commentEntity.setAuthorAvatar(authorAvatar);
			commentEntity.save();
		}
		return commentEntity;
	}

	/*
	 * The method used for creating a new message object.
	 * It creates an ActiveObjects exemplar and fills the fields of this object.
	 * The method returns the created messageEntity object. At first it creates a new  message entity,
	 * sets some of its values and persists this entity to the database.
	 * 
	 * @param subject
	 * @param content
	 * @param date
	 * @param author
	 * @param authorAvatar
	 * 
	 * */
	public void addNewMessage(final String subject, final String content, final Date date, 
			final String author, final String authorAvatar,final String image) {
		final MessageEntity messageEntity = activeObjects.create(MessageEntity.class);
		
		if(content.toCharArray().length < 2000) {
			messageEntity.setSubject(subject);
			messageEntity.setContent(content);
			messageEntity.setDate(date);
			messageEntity.setAuthor(author);
			messageEntity.setAuthorAvatar(authorAvatar);
			messageEntity.setImage(image);
			messageEntity.save();
		}
	}
	
	/* 
	 * This method gets a new list of messages for the REST-functionality.
	 * 
	 * @param subject
	 * @return listMsg
	 * */
	public ArrayList<MessageEntity> getMessageForJSON(String subject) {
		ArrayList<MessageEntity> listMsg = new ArrayList<MessageEntity>();
		for (MessageEntity msg : getMessages()) {
			if (subject.equals(msg.getSubject())) {
				listMsg.add(msg);
			}
		}
		return listMsg;
	}

	/* 
	 * This method gets a new message. It calls getMessages()-method, 
	 * that returns a message object. It searches for a message, 
	 * that has the same subject as a subject from method parameter. 
	 * 
	 * @param subject
	 * @return 
	 * 
	 * */
	public MessageEntity getMessage(String subject) {
		for (MessageEntity msg : getMessages()) {
			if (subject.equals(String.valueOf(msg.getID()))) {
				return msg;
			}
		}
		return null;
	}

	/*
	 * This method returns all messages from the database. 
	 *  
	 * @return ArrayList<MessageEntity>
	 * 
	 * */
	public List<MessageEntity> getMessages() {
		return new ArrayList<MessageEntity>(
				Arrays.asList(activeObjects.find(MessageEntity.class)));
	}
}
