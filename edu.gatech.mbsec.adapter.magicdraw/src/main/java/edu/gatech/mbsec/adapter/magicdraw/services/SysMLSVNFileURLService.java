/*********************************************************************************************
 * Copyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.
 *                         http://www.mbse.gatech.edu/
 *                  http://www.mbsec.gatech.edu/research/oslc
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *
 *  Contributors:
 *
 *	   Axel Reichwein, Koneksys (axel.reichwein@koneksys.com)		
 *******************************************************************************************/
package edu.gatech.mbsec.adapter.magicdraw.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.eclipse.lyo.adapter.subversion.SubversionManager;
import org.eclipse.lyo.oslc4j.core.annotation.OslcCreationFactory;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import clients.SubversionFileClient;
import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;
import util.FileMetadata;

/**
 * This servlet contains the implementation of web services for selecting
 *  files to be published by the adapter basedon on Subversion
 * file URLs.
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */

@Path("svnfilepublisher")
public class SysMLSVNFileURLService {

	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;

	private static List<String> svnurls;
	// private static final String ADDRESS_FILE = "oslc4jsimulink
	// configuration/subversionfiles.csv";

	@GET
	@Produces(MediaType.TEXT_HTML)
	public void get() {

		CSVReader reader2;
		try {
			reader2 = new CSVReader(new FileReader(OSLC4JMagicDrawApplication.svnURLsFilePath));
			List<String[]> allElements = reader2.readAll();
			if (svnurls == null) {
				if (allElements.size() > 0) {
					svnurls = OSLC4JMagicDrawApplication.readSVNFileURLs(allElements);
				}
			}
			httpServletRequest.setAttribute("svnurls", svnurls);

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// httpServletRequest.setAttribute("modelName", modelName);
		// RequestDispatcher rd = httpServletRequest
		// .getRequestDispatcher("/subversion/svnfilepublisher.jsp");

		httpServletRequest.setAttribute("creatorUri", MagicDrawManager.baseHTTPURI + "/services/svnfilepublisher");

		RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/subversion/svnfilelist.jsp");

		try {
			rd.forward(httpServletRequest, httpServletResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		}

	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	public Response add(@FormParam("svnurl") final String svnurl) throws IOException, ServletException {

		try {
			// make sure that URL is valid
			new URL(svnurl);
				
			if (svnurls == null) {
				svnurls = new ArrayList<String>();
			}
			
			// make sure that url is not a duplicate							
			if (!svnurls.contains(svnurl)) {
				System.out.println(svnurl);
				svnurls.add(svnurl);

				// write to file
				CSVWriter writer2 = new CSVWriter(new FileWriter(OSLC4JMagicDrawApplication.svnURLsFilePath), '\n');
				String[] entries = new String[svnurls.size()];
				int j = 0;
				for (String string : svnurls) {
					entries[j] = string;
					j++;
				}
				writer2.writeNext(entries, false);
				writer2.close();
			}
							
		} catch (Exception e) {

		}
		
		

		httpServletRequest.setAttribute("svnurls", svnurls);
		httpServletRequest.setAttribute("creatorUri", MagicDrawManager.baseHTTPURI + "/services/svnfilepublisher");
		try {
			String svnfileSelectorURI = uriInfo.getAbsolutePath().toString();
			httpServletResponse.sendRedirect(svnfileSelectorURI);
			return Response.seeOther(new URI(svnfileSelectorURI)).build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		}
	}

	@POST
	@Path("delete")
	@Produces(MediaType.TEXT_HTML)
	public Response delete(@FormParam("svnurl") final String svnurl) throws IOException, ServletException {

		// delete specific file in local working copy
		String[] repParts = svnurl.split("/");
		String subversionFileName = repParts[repParts.length - 1];
		String subversionFileDirURL = svnurl.replace(subversionFileName, "");
		String localSubversionFileDir = subversionFileDirURL.replace(":", "");
		localSubversionFileDir = localSubversionFileDir.replace("/", "");
		new File(OSLC4JMagicDrawApplication.magicdrawModelsDirectory + localSubversionFileDir + "/" + subversionFileName)
				.delete();

		svnurls.remove(svnurl);
		// write to file
		CSVWriter writer2 = new CSVWriter(new FileWriter(OSLC4JMagicDrawApplication.svnURLsFilePath), '\n');
		String[] entries = new String[svnurls.size()];
		int j = 0;
		for (String string : svnurls) {
			entries[j] = string;
			j++;
		}
		writer2.writeNext(entries, false);
		writer2.close();

		httpServletRequest.setAttribute("svnurls", svnurls);
		httpServletRequest.setAttribute("creatorUri", MagicDrawManager.baseHTTPURI + "/services/svnfilepublisher");
		try {
			String svnfileSelectorURI = uriInfo.getAbsolutePath().toString().replace("/delete", "");
			httpServletResponse.sendRedirect(svnfileSelectorURI);
			return Response.seeOther(new URI(svnfileSelectorURI)).build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		}
	}

	@POST
	@Path("deleteall")
	@Produces(MediaType.TEXT_HTML)
	public Response deleteAll() throws IOException, ServletException {

		// delete all files in local working copy
		for (String subversionFileURLString : svnurls) {
			// delete specific file
			String[] repParts = subversionFileURLString.split("/");
			String subversionFileName = repParts[repParts.length - 1];
			String subversionFileDirURL = subversionFileURLString.replace(subversionFileName, "");
			String localSubversionFileDir = subversionFileDirURL.replace(":", "");
			localSubversionFileDir = localSubversionFileDir.replace("/", "");
			new File(OSLC4JMagicDrawApplication.magicdrawModelsDirectory + localSubversionFileDir + "/"
					+ subversionFileName).delete();
		}

		svnurls.clear();
		// rewrite svn file list csv file
		CSVWriter writer2 = new CSVWriter(new FileWriter(OSLC4JMagicDrawApplication.svnURLsFilePath), '\n');
		writer2.close();

		httpServletRequest.setAttribute("svnurls", svnurls);
		httpServletRequest.setAttribute("creatorUri", MagicDrawManager.baseHTTPURI + "/services/svnfilepublisher");
		try {
			String svnfileSelectorURI = uriInfo.getAbsolutePath().toString().replace("/deleteall", "");
			httpServletResponse.sendRedirect(svnfileSelectorURI);
			return Response.seeOther(new URI(svnfileSelectorURI)).build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		}
	}

	@POST
	@Path("publish")
	@Produces(MediaType.TEXT_HTML)
	public Response publish() throws IOException, ServletException {
		OSLC4JMagicDrawApplication.reloadModels();
		httpServletRequest.setAttribute("svnurls", svnurls);
		httpServletRequest.setAttribute("creatorUri", MagicDrawManager.baseHTTPURI + "/services/svnfilepublisher");
		try {
			String svnfileSelectorURI = uriInfo.getAbsolutePath().toString().replace("/publish", "");
			httpServletResponse.sendRedirect(svnfileSelectorURI);
			return Response.seeOther(new URI(svnfileSelectorURI)).build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		}
	}
	
	@HEAD	
	@Path("republish")
	public void getEventFromRepository(String svnRepoURL) {
		System.out.println("subversion commit event");
		System.out.println(svnRepoURL);
		
		
		OSLC4JMagicDrawApplication.reloadModels();
		
//		// check out svnfiles which are in svnrepo
//		List<String> svnurlsToUpdate = new ArrayList<String>();
//		for (String svnurl : svnurls) {
//			if(svnurl.contains(svnRepoURL)){
//				svnurlsToUpdate.add(svnurl);
//			}
//		}
//		// get subversionFileURLString and localSubversionFileDir
//		
//		
//		
//		
//		// initialize global list for FileMetaDatas
//		ArrayList<FileMetadata> fileMetaDatas = new ArrayList<FileMetadata>();
//		
//		// perform revert and update on these files
//		// perform checkout or update on Subversion file
//		for (String svnurlToUpdate : svnurlsToUpdate) {
//			
//			String[] repParts = svnurlToUpdate.split("/");
//			String subversionFileName = repParts[repParts.length - 1];			
//			String subversionFileDirURL = svnurlToUpdate.replace(subversionFileName, "");																		
//			String localSubversionFileDir = subversionFileDirURL.replace(":", "");
//			localSubversionFileDir = localSubversionFileDir.replace("/", "");
//						
//			FileMetadata specificFileMetaData = SubversionFileClient.syncFile(svnurlToUpdate,
//					OSLC4JMagicDrawApplication.magicdrawModelsDirectory + localSubversionFileDir);
//		}
//		
//		
//		// convert fileMetaDatas into OSLC POJOs
//		subversionManager.convertSpecificFileMetaDataIntoRDFSubversionFileResources(fileMetaDatas);
	}

}
