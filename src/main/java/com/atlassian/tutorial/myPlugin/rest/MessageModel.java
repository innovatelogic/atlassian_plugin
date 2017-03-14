package com.atlassian.tutorial.myPlugin.rest;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class creates MessageModel object with JAXB Annotations.
 * 
 * 
 * */
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageModel {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "authorAvatar")
	String authorAvatar;
	@XmlAttribute
	String author;
	@XmlElement(name = "content")
	String content;
	@XmlElement(name = "subject")
	String subject;
	@XmlElement(name = "date")
	String date;	
	@XmlElement(name = "image")
	String image;
	@XmlElement(name = "like")
	int like;

	String id;
	
	/*
	 * This private constructor isn't used by any code, but JAXB requires any
     * representation class to have a no-args constructor.
     * 
	 * */
	public MessageModel() {
		
	}
	
	/*
	 * Creates a new message model with the given parameters.
	 * 
	 * @param author
	 * @param authorAvatar
	 * @param subject
	 * @param content
	 * @param date
	 * @param id
	 * @param image
	 * 
	 */
	public MessageModel(String authorAvatar, String author, String content, String subject, Date date, String id, String image, int like) {
		super();
		this.authorAvatar = authorAvatar;
		this.author = author;
		this.content = content;
		this.subject = subject;
		SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		this.date = formatDate.format(date);
		this.id = id;
		this.image = image;
		this.like = like;
	}
	
	/*
	 * Creates a new message model with the given parameters.
	 * 
	 * @param author
	 * @param authorAvatar
	 * @param subject
	 * @param content
	 * @param date
	 * @param id
	 * 
	 */
	public MessageModel(String authorAvatar, String author, String content, String subject, Date date, String id) {
		super();
		this.authorAvatar = authorAvatar;
		this.author = author;
		this.content = content;
		this.subject = subject;
		SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		this.date = formatDate.format(date);
		this.id = id;
	}

   /*
    * Creates a new message model with the given parameters.
    * 
	* @param author
	* @param authorAvatar
	* @param subject
	* @param content
	* @param date
	* 
    */
	public MessageModel(String author, String authorAvatar, String subject, String content, Date date) {
		this.authorAvatar = authorAvatar;
		this.author = author;
		this.content = content;
		this.subject = subject;
		SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		this.date = formatDate.format(date);
	}

	/**
	* @return the image of the object
	*/
	public String getImage() {
		return image;
	}

	/**
	* @param image to set
	*/
	public void setImage(String image) {
		this.image = image;
	}
	
	/**
	* @return the id of the object
	*/
	public String getID() {
		return id;
	}

	/**
	* @param id to set
	*/
	public void setId(String id) {
		this.id = id;
	}

	/**
	* @return the author avatar of the object
	*/
	public String getAuthorAvatar() {
		return authorAvatar;
	}

	/**
	* @param author avatar to set
	*/
	public void setAuthorAvatar(String authorAvatar) {
		this.authorAvatar = authorAvatar;
	}

	/**
	* @return the author of the object
	*/
	public String getAuthor() {
		return author;
	}

	/**
	* @param author to set
	*/
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	* @return the content of the object
	*/
	public String getContent() {
		return content;
	}

	/**
	* @param content to set
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/**
	* @return the subject of the object
	*/
	public String getSubject() {
		return subject;
	}

	/**
	* @param subject to set
	*/
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	* @return the date of the object
	*/
	public String getDate() {
		return date;
	}

	/**
	* @param date to set
	*/
	public void setDate(Date date) {
		SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");		
		this.date = formatDate.format(date);
	}
	
	/**
	* @return the like of the object
	*/
	public int getLike() {
		return like;
	}

	/**
	* @param like to set
	*/
	public void setLike(int like) {
		this.like = like;
	}
}
