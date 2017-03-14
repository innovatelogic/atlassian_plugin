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
 * This interface creates a message entity.
 * The annotation @Preload tells Active Objects to load all the fields of the entity eagerly. 
 * 
 * 
 * */
@Preload
@Table("Messages")
public interface MessageEntity extends Entity {
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
	* @return the author of the object
	*/
	String getAuthor();

	/**
	* @param author to set
	*/
	void setAuthor(String author);

	/**
	* @return the content of the object
	*/
	String getContent();

	/**
	* @param content to set
	*/
	void setContent(String content);

	/**
	* @return the subject of the object
	*/
	String getSubject();

	/**
	* @param subject to set
	*/
	void setSubject(String subject);
	
	/**
	* @return the image of the object
	*/
	String getImage();

	/**
	* @param image to set
	*/
	void setImage(String image);

	/**
	* @return the date of the object
	*/
	Date getDate();
	
	/**
	* @param date to set
	*/
	void setDate(Date date);
		
	/**
	* @return likes of the object
	*/
	@OneToMany
	public LikeMessageEntity[] getLikes();
	
	/**
	* @return the comments of the object
	*/
	@OneToMany
	public CommentEntity[] getComments();
	
}
