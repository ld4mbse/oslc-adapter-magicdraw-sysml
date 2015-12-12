/*********************************************************************************************
 * Copyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.
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
 *	   Axel Reichwein (axel.reichwein@koneksys.com)		- initial implementation       
 *******************************************************************************************/
package edu.gatech.mbsec.adapter.magicdraw.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.lyo.adapter.magicdraw.resources.Constants;
import org.eclipse.lyo.adapter.magicdraw.resources.OSLCJavaClassesGenerator;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLFlowProperty;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLPartProperty;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLPort;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLProxyPort;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLReferenceProperty;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLValueProperty;
import org.eclipse.lyo.oslc4j.core.annotation.OslcCreationFactory;
import org.eclipse.lyo.oslc4j.core.annotation.OslcName;
import org.eclipse.lyo.oslc4j.core.annotation.OslcNamespace;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcResourceShape;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;

import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;

/**
 * This servlet contains the implementation of OSLC RESTful web services for SysMLValueProperty resources.
 * 
 * The servlet contains web services for: <ul margin-top: 0;>
 * <li> returning specific SysMLValueProperty resources in HTML and other formats </li>
 * <li> returning all SysMLValueProperty resources within a specific MagicDraw project
 *  in HTML and other formats </li>
 *  <li> adding new SysMLValueProperty resources to a specific MagicDraw project</li>
 *  <li> updating a specific SysMLValueProperty</li>
 *  <li> getting the ETag of a specific SysMLValueProperty resource</li>
 *  </ul>
 *  <br>
 *  The servlet also contains RESTful web services for: 
 *  <li> returning an HTML page with forms that a user can fill out and submit for 
 *  adding new SysMLValueProperty resources to a specific MagicDraw project</li>  
 *  <li> adding new SysMLValueProperty resources to a specific MagicDraw project based on
 *  submitted HTML forms</li> 
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
@OslcService(Constants.SYSML_VALUEPROPERTY_DOMAIN)
@Path("{projectId}/valueproperties")
public class SysMLValuePropertyService extends HttpServlet {

	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;
	

	@OslcQueryCapability(title = "SysML Value Property Query Capability", label = "SysML Value Property Catalog Query", resourceShape = OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_VALUEPROPERTY, resourceTypes = { Constants.TYPE_SYSML_VALUEPROPERTY }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	@GET
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public List<org.eclipse.lyo.adapter.magicdraw.resources.SysMLValueProperty> getValueProperties(
			@PathParam("projectId") final String projectId,
			@QueryParam("oslc.where") final String where,
			@QueryParam("oslc.select") final String select,
			@QueryParam("oslc.prefix") final String prefix,
			@QueryParam("page") final String pageString,
			@QueryParam("oslc.orderBy") final String orderBy,
			@QueryParam("oslc.searchTerms") final String searchTerms,
			@QueryParam("oslc.paging") final String paging,
			@QueryParam("oslc.pageSize") final String pageSize)
			throws IOException, ServletException {
		MagicDrawManager.loadSysMLProjects();
		return MagicDrawManager.getValueProperties(projectId);
	}

	@GET
	@Path("{propertyQualifiedName}")
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_JSON })
	public synchronized Response getValueProperty(
			@PathParam("projectId") final String projectId,
			@PathParam("propertyQualifiedName") final String propertyQualifiedName,
			@Context Request request)
			throws URISyntaxException {
		MagicDrawManager.loadSysMLProjects();
		SysMLValueProperty sysMLValueProperty = MagicDrawManager
				.getValuePropertyByQualifiedName(projectId + "/valueproperties/" + propertyQualifiedName);
		if(sysMLValueProperty == null){
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
		}
		EntityTag eTag = new EntityTag(md5Java(sysMLValueProperty));
		String requestETag = httpServletRequest.getHeader("If-None-Match");
		if(requestETag != null){
			// Conditional GET
			ResponseBuilder builder = request.evaluatePreconditions(eTag);
			
			// If rb is null then either it is first time request; or resource is
			// modified
			// Get the updated representation and return with Etag attached to it
			if (builder == null) {
				builder = Response.ok(sysMLValueProperty);
				builder.tag(eTag);
			} 
			return builder.build();
		}
		else{
			// Regular GET
			return Response.ok(sysMLValueProperty).tag(eTag).build();
		}
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public void getHtmlValueProperties(
			@PathParam("projectId") final String projectId) {
		MagicDrawManager.loadSysMLProjects();
		List<SysMLValueProperty> sysmlValueProperties = MagicDrawManager
				.getValueProperties(projectId);
		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlValueProperties != null) {			
			httpServletRequest.setAttribute("elements",
					sysmlValueProperties);
			httpServletRequest.setAttribute("requestURL", requestURL);
			httpServletRequest.setAttribute("projectId", projectId);
			RequestDispatcher rd = httpServletRequest
					.getRequestDispatcher("/sysml/sysml_valueproperties_html.jsp");
			try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {
				e.printStackTrace();
				throw new WebApplicationException(e);
			}
		}
	}

	@GET
	@Path("{qualifiedName}")
	@Produces(MediaType.TEXT_HTML)
	public void getHtmlValueProperty(
			@PathParam("projectId") final String projectId,
			@PathParam("qualifiedName") final String qualifiedName,
			@QueryParam("oslc.properties") final String propertiesString,
			@QueryParam("oslc.prefix") final String prefix)
			throws URISyntaxException, IOException {
		MagicDrawManager.loadSysMLProjects();
		org.eclipse.lyo.adapter.magicdraw.resources.SysMLValueProperty sysmlValueProperty = MagicDrawManager
				.getValuePropertyByQualifiedName(projectId + "/valueproperties/" + qualifiedName);

		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlValueProperty != null) {
			
			httpServletRequest
					.setAttribute("valueProperty", sysmlValueProperty);
			httpServletRequest.setAttribute("requestURL", requestURL);
			RequestDispatcher rd = httpServletRequest
					.getRequestDispatcher("/sysml/sysml_valueproperty_html.jsp");
			try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {
				e.printStackTrace();
				throw new WebApplicationException(e);
			}
		}
	}

	@GET
	@Path("creator")
	@Produces(MediaType.TEXT_HTML)
	public void createHtmlValueProperty(
			@PathParam("projectId") final String projectId)
			throws URISyntaxException, IOException {

		MagicDrawManager.loadSysMLProjects();

		List<String> possibleValueTypes = new ArrayList<String>();
		possibleValueTypes.add("NONE");
		for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.DataType mdValueType : MagicDrawManager.mdSysmlValueTypes) {
			possibleValueTypes.add("VALUETYPE___"
					+ mdValueType.getQualifiedName());
		}

		List<String> possibleBlocks = new ArrayList<String>();
		possibleValueTypes.add("NONE");
		for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class mdBlock : MagicDrawManager.mdSysmlBlocks) {
			possibleBlocks.add("BLOCK___" + mdBlock.getQualifiedName());
		}

		EClass eClass = ServiceUtil.getEClass("ValueProperty");
		
		httpServletRequest.setAttribute("creatorUri",
				MagicDrawManager.baseHTTPURI + "/services/" + projectId
						+ "/valueproperties");
		httpServletRequest.setAttribute("eclass", eClass);
		httpServletRequest.setAttribute("possibleBlocks", possibleBlocks);
		httpServletRequest.setAttribute("possibleValueTypes",
				possibleValueTypes);

		RequestDispatcher rd = httpServletRequest
				.getRequestDispatcher("/sysml/sysml_valueproperty_creator.jsp");
		try {
			rd.forward(httpServletRequest, httpServletResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		}

	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	public void addValuePropertyFromHtmlForm(
			@PathParam("projectId") final String projectId,
			@FormParam("name") final String elementName,
			@FormParam("defaultValue") final String defaultValue,
			@FormParam("lower") final String lower,
			@FormParam("upper") final String upper,
			@FormParam("type") final String type,
			@FormParam("ownerElement") final String ownerElement)
			throws IOException, ServletException {

//		MagicDrawManager.loadSysMLProjects();
		MagicDrawManager.makeSysMLProjectActive(projectId);

		SysMLValueProperty newSysMLValueProperty;
		try {
			newSysMLValueProperty = new SysMLValueProperty();
			newSysMLValueProperty.setName(elementName);
			newSysMLValueProperty.setLower(lower);
			newSysMLValueProperty.setUpper(upper);
			newSysMLValueProperty.setDefaultValue(defaultValue);

			// owner
			// Unparse owner element string
			String[] ownerElementStrings = ownerElement.split("_");
			String ownerName = ownerElementStrings[ownerElementStrings.length - 1];
			ownerName = ownerName.replaceAll("\\n", "-").replaceAll(" ", "_");
			URI ownerURI = URI
					.create(MagicDrawManager.baseHTTPURI + "/services/"
							+ projectId + "/blocks/" + ownerName);
			newSysMLValueProperty.setOwner(ownerURI);

			// URI
			URI elementURI = URI
					.create(MagicDrawManager.baseHTTPURI + "/services/"
							+ projectId + "/valueproperties/" + ownerName
							+ "::" + elementName);
			newSysMLValueProperty.setAbout(elementURI);

			// type
			String[] typeElementStrings = type.split("_");
			String typeQualifiedName = typeElementStrings[typeElementStrings.length - 1];
			URI typeURI = MagicDrawManager
					.getURIFromQualifiedName(typeQualifiedName);
			newSysMLValueProperty.setType(typeURI);

			MagicDrawManager.createSysMLValueProperty(newSysMLValueProperty,
					projectId);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			
			httpServletRequest.setAttribute("elementType", "ValueProperty");
			httpServletRequest.setAttribute("createdElement", elementName);
			httpServletRequest.setAttribute("projectId", projectId);
			httpServletRequest.setAttribute("portNumber", OSLC4JMagicDrawApplication.portNumber);
			RequestDispatcher rd = httpServletRequest
					.getRequestDispatcher("/sysml/sysml_creationConfirmation.jsp");
			rd.forward(httpServletRequest, httpServletResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		}
	}

	@OslcCreationFactory(title = "SysML ValueProperty Creation Factory", label = "SysML ValueProperty Creation", resourceShapes = { OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_VALUEPROPERTY }, resourceTypes = { Constants.TYPE_SYSML_VALUEPROPERTY }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	@POST
	@Consumes({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public Response addValueProperty(
			@PathParam("projectId") final String projectId,
			final SysMLValueProperty sysmlValueProperty) throws IOException,
			ServletException {
//		MagicDrawManager.loadSysMLProjects();
		MagicDrawManager.makeSysMLProjectActive(projectId);
		// creating the element in MagicDraw
		MagicDrawManager
				.createSysMLValueProperty(sysmlValueProperty, projectId);
		URI about = sysmlValueProperty.getAbout();
		return Response.created(about).entity(sysmlValueProperty).build();
	}

	@PUT
	@Path("{propertyQualifiedName}")
	@Consumes({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public synchronized Response updateValueproperty(
			@PathParam("projectId") final String projectId,
			final SysMLValueProperty sysMLValueProperty, @Context Request request)
			throws IOException, ServletException {
		MagicDrawManager.loadSysMLProjects();
		String incomingValuePropertyURI = sysMLValueProperty.getAbout().toString();
		incomingValuePropertyURI = incomingValuePropertyURI.replace(MagicDrawManager.baseHTTPURI + "/services/" + projectId + "/valueproperties/", "");
		SysMLValueProperty sysMLValuePropertyToUpdate = MagicDrawManager
				.getValuePropertyByQualifiedName(projectId + "/valueproperties/" + incomingValuePropertyURI);
		EntityTag eTag = new EntityTag(md5Java(sysMLValuePropertyToUpdate));
		
		// just for debug/checking purposes
		String requestETag = httpServletRequest.getHeader("If-Match");
		
		// check if ETag of Request matches with local ETag
		ResponseBuilder builder = request.evaluatePreconditions(eTag);

		// client is not up to date (send back 412, Precondition failed)
		if (builder != null) {
			return builder.build();		
		}

		// update SysML ValueProperty
		sysMLValuePropertyToUpdate.setDefaultValue(sysMLValueProperty.getDefaultValue());
		MagicDrawManager
				.updateSysMLValueProperty(sysMLValuePropertyToUpdate, projectId);		
		builder = Response.ok();
		EntityTag updatedETag = new EntityTag(md5Java(sysMLValuePropertyToUpdate));
		return builder.tag(updatedETag).build();				
	}
	
	@HEAD
	@Path("{propertyQualifiedName}")
	public synchronized Response headValueProperty(
			@PathParam("projectId") final String projectId,
			@PathParam("propertyQualifiedName") final String propertyQualifiedName,
			@Context Request request) throws URISyntaxException {
//		MagicDrawManager.loadSysMLProjects();
		MagicDrawManager.makeSysMLProjectActive(projectId);
		
		SysMLValueProperty sysMLValueProperty = MagicDrawManager
				.getValuePropertyByQualifiedName(projectId + "/valueproperties/" + propertyQualifiedName);
		EntityTag eTag = new EntityTag(
				md5Java(sysMLValueProperty));
		String requestETag = httpServletRequest.getHeader("If-None-Match");
		if (requestETag != null) {
			ResponseBuilder builder = request.evaluatePreconditions(eTag);
			// If rb is null then either it is first time request; or resource
			// is
			// modified, return status 200 with Etag attached to it
			// else, just return status 304, not modified with Etag
			if (builder == null) {
				builder = Response.ok();
			} else {
				builder = Response.status(HttpServletResponse.SC_NOT_MODIFIED);
			}
			return builder.tag(eTag).build();
		} else {
			// Regular HEAD
			return Response.ok().tag(eTag).build();
		}
	}

	public static String md5Java(SysMLValueProperty sysMLValueProperty) {
		String digest = null;
		String message = sysMLValueProperty.getAbout().toASCIIString() + sysMLValueProperty.getName() + sysMLValueProperty.getDefaultValue();
		try {			
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(message.getBytes("UTF-8"));
			 //converting byte array to Hexadecimal String
			StringBuilder sb = new StringBuilder(2*hash.length);
			for (byte b : hash) {
				sb.append(String.format("%02x", b & 0xff));
			}
			digest = sb.toString();
		} catch (UnsupportedEncodingException ex) {
		} catch (NoSuchAlgorithmException ex) {
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return digest;
	}
	
}
