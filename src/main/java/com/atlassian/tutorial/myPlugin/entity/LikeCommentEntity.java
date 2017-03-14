package com.atlassian.tutorial.myPlugin.entity;

import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.AutoIncrement;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.PrimaryKey;
import net.java.ao.schema.Table;

/**
 * This interface creates the entity for 'likes' in comments.
 * The annotation @Preload tells Active Objects to load all the fields of the entity eagerly. 
 * 
 * 
 * */
@Preload
@Table("LikesComments")
public interface LikeCommentEntity extends Entity {
	@AutoIncrement
	@NotNull
	@PrimaryKey("ID")
	public int getID();
	
	/**
	* @return the post of the comment entity
	*/
	public CommentEntity getPost();

	/**
	* @param post to set
	*/
	public void setPost(CommentEntity post);
	
	/**
	* @return the like of the object
	*/
	public int getLike();
	
	/**
	* @param like to set
	*/
	public void setLike(int like);
	
	/**
	* @return the author of the object
	*/
	public String getAuthor();
	
	/**
	* @param author to set
	*/
	public void setAuthor(String author);
}
