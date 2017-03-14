package com.atlassian.tutorial.myPlugin.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;

/**
 * This servlet class reads the image from the mentioned directory and 
 * writes the content in the response object using ServletOutputStream 
 * and BufferedOutputStream classes.
 * 
 * http://www.javatpoint.com/example-to-display-image-using-servlet
 * 
 * @author 
 *
 */
@Scanned
@Named("ImageServlet")
public class ImageServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
    public ImageServlet() {
        super();
    }
 
    /* 
     * This Method reads an image with FileInputStream and
     * writes this image content as a response with ServletOutputStream class.
     * BufferedInputStream and BufferedOutputStream classes make the performance faster.
     * 
     * */
    public void doGet(HttpServletRequest req, HttpServletResponse response)  throws IOException {  
       //set the MIME-Type for Browser interpretation
	   response.setContentType("image/jpeg");  
	   ServletOutputStream out;
	   //writes this image content as a response
	   out = response.getOutputStream(); 
	   
	   System.out.println("ImageServlet ########");
	   System.out.println("image name" + req.getParameter("imageName"));
	   
	   //reads image
	   FileInputStream fin = new FileInputStream(req.getParameter("imageName"));  
	   System.out.println("FileInputStream");  
	   //make the performance faster
	   BufferedInputStream bin = new BufferedInputStream(fin);  
	   BufferedOutputStream bout = new BufferedOutputStream(out);  
	   int ch =0; ;  
	   while((ch=bin.read())!=-1)  
	   {  
		   bout.write(ch);
		   System.out.println("write");
	   }  
	     
	   bin.close();  
	   fin.close();  
	   bout.close();  
	   out.close();  
   }  

}