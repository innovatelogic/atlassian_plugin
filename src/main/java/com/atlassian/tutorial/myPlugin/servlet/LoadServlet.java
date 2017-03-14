package com.atlassian.tutorial.myPlugin.servlet;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * This class allows to upload files to the server. 
 * It can handle multiple file uploading at a time.
 * 
 * http://www.tutorialspoint.com/servlets/servlets-file-uploading.htm
 * 
 * @author 
 *
 */
@Named("LoadServlet")
public class LoadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private boolean isMultipart;
	private int maxFileSize = 300 * 1024;
	private int maxMemSize = 100 * 1024;
	private File file;

	/*
	 * HTTP POST method.
	 * */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		System.out.println(request.toString());
		// Check a file upload request
		isMultipart = ServletFileUpload.isMultipartContent(request);
		response.setContentType("text/html");
		java.io.PrintWriter out = response.getWriter();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File("/"));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax(maxFileSize);

		try {
			// Parse the request to get file items.
			List fileItems = upload.parseRequest(request);
			// Process the uploaded file items
			Iterator i = fileItems.iterator();

			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				if (!fi.isFormField()) {
					// Get the uploaded file parameter
					String fileName = fi.getName();
					// Write the file
					if (fileName.lastIndexOf("\\") >= 0) {
						file = new File(fileName.substring(fileName.lastIndexOf("\\")));
					} else {
						file = new File(fileName.substring(fileName.lastIndexOf("\\") + 1));
					}
					fi.write(file);
					System.out.println(file.getAbsolutePath());
					out.println(fileName);
				}
			}		
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		 throw new ServletException("GET method used with " +
	                getClass( ).getName( )+": POST method required.");
	}
}
