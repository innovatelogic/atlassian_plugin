package com.atlassian.tutorial.myPlugin.databaseWork;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.atlassian.activeobjects.tx.Transactional;

import com.atlassian.tutorial.myPlugin.entity.CommentEntity;
import com.atlassian.tutorial.myPlugin.entity.LikeMessageEntity;
import com.atlassian.tutorial.myPlugin.entity.MessageEntity;

/**
 * This interface represents management functions for message and comment objects.
 * 
 * 
 * */
@Transactional
public interface IDatabaseWork {
	void addLikeCom(final CommentEntity messageEntity,
			final String author);
	void addLikeMsg(final MessageEntity messageEntity, final String subject,
			final String author);
	LikeMessageEntity getLike(String subject, String author);
	void deleteComment(final String subject, final String comment);
	void deleteMessage(final String subject);
	void deleteAllLike(MessageEntity msg);
	void deleteAllComments(MessageEntity messageEntity);
	void deleteAllCommentLike(CommentEntity commentEntity);
	CommentEntity getComment(String subject, String comment);
	void upgradeMessage(final MessageEntity messageEntity,
			final String currentMessage, final String newMessage);
	void upgradeComment(final CommentEntity[] commentEntities,
			final String currentComment, final String newComment);
	CommentEntity addComment(final MessageEntity messageEntity,
			final String author, final String comment, final String authorAvatar, String date);
	void addNewMessage(final String subject, final String content,
			final Date date, final String author, final String authorAvatar,final String image);
	ArrayList<MessageEntity> getMessageForJSON(String subject);
	 MessageEntity getMessage(String subject);
	 List<MessageEntity> getMessages();
}

