package com.atlassian.tutorial.myPlugin.rest;

import javax.xml.bind.annotation.*;

/**
 * Default class
 * 
 * 
 * */
public class MyRestResourceModel {
	
  	@XmlAttribute
    private String key;

    @XmlElement(name = "value")
    private String message;
    
    /*
	 * Default constructor
	 * */
    public MyRestResourceModel() {
    }

    /*
	 * Constructor
	 * 
	 * @param key
	 * @param message
	 * 
	 * */
    public MyRestResourceModel(String key, String message) {
        this.key = key;
        this.message = message;
    }
 
	/*
	* @return the key of the object
	*/
    public String getKey() {
        return key;
    }
 
	/*
	* @param key to set
	*/
    public void setKey(String key) {
        this.key = key;
    }

	/*
	* @return the message of the object
	*/
    public String getMessage() {
        return message;
    }

	/*
	* @param message to set
	*/
    public void setMessage(String message) {
        this.message = message;
    }
}