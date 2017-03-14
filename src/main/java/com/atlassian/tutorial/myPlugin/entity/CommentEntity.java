package com.atlassian.tutorial.myPlugin.entity;

import java.util.Date;

import net.java.ao.Entity;
import net.java.ao.OneToMany;
import net.java.ao.Preload;
import net.java.ao.schema.AutoIncrement;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.PrimaryKey;
import net.java.ao.schema.Table;

/**
 * This interface creates a comment entity.
 * The annotation @Preload tells Active Objects to load all the fields of the entity eagerly. 
 * 
 * 
 * */
@Preload
@Table("Comments")
public interface CommentEntity extends Entity {
	@AutoIncrement
	@NotNull
	@PrimaryKey("ID")
	public int getID();
	
	/**
	* @return the author avatar of the object
	*/
	String getAuthorAvatar();

	/**
	* @param author avatar to set
	*/
	void setAuthorAvatar(String authorAvatar);
	
	/**
	* @return the message entity post of the object
	*/
	public MessageEntity getPost();
	
	/**
	* @param post to set
	*/
	public void setPost(MessageEntity post);

	/**
	* @param author to set
	*/
	public void setAuthor(String author);

	/**
	* @return the author of the object
	*/
	public String getAuthor();
	
	/**
	* @return the date of the object
	*/
	String getDate();
	
	/**
	* @param date to set
	*/
	void setDate(String date);

	/**
	* @param comment to set
	*/
	public void setComment(String comment);

	/**
	* @return the comment of the object
	*/
	public String getComment();
	
	/**
	* @return 'likes' of the comment entity 'like' object
	*/
	@OneToMany
	public LikeCommentEntity[] getLikes();
}
