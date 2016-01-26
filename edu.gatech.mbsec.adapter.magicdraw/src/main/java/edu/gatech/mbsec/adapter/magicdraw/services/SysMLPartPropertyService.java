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
import java.net.URI;
import java.net.URISyntaxException;
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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.emf.ecore.EClass;
import edu.gatech.mbsec.adapter.magicdraw.resources.Constants;
import edu.gatech.mbsec.adapter.magicdraw.resources.OSLCJavaClassesGenerator;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLBlock;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLModel;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPackage;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPartProperty;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLRequirement;
import org.eclipse.lyo.oslc4j.core.annotation.OslcCreationFactory;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;

import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;

/**
 * This servlet contains the implementation of OSLC RESTful web services for SysMLPartProperty resources.
 * 
 * The servlet contains web services for: <ul margin-top: 0;>
 * <li> returning specific SysMLPartProperty resources in HTML and other formats </li>
 * <li> returning all SysMLPartProperty resources within a specific MagicDraw project
 *  in HTML and other formats </li>
 *  <li> adding new SysMLPartProperty resources to a specific MagicDraw project</li>
 *  </ul>
 *  <br>
 *  The servlet also contains RESTful web services for: 
 *  <li> returning an HTML page with forms that a user can fill out and submit for 
 *  adding new SysMLPartProperty resources to a specific MagicDraw project</li>  
 *  <li> adding new SysMLPartProperty resources to a specific MagicDraw project based on
 *  submitted HTML forms</li> 
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
@OslcService(Constants.SYSML_PARTPROPERTY_DOMAIN)
@Path("{projectId}/partproperties")
public class SysMLPartPropertyService extends HttpServlet {

	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;

	@OslcQueryCapability(title = "SysML Part Property Query Capability", label = "SysML PartProperty Catalog Query", resourceShape = OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_PARTPROPERTY, resourceTypes = { Constants.TYPE_SYSML_PARTPROPERTY }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	@GET
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public List<edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPartProperty> getPartProperties(
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
		return MagicDrawManager.getPartProperties(projectId);
	}

	@GET
	@Path("{propertyQualifiedName}")
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_JSON })
	public edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPartProperty getPartProperty(
			@PathParam("projectId") final String projectId,
			@PathParam("propertyQualifiedName") final String propertyQualifiedName)
			throws URISyntaxException {
		MagicDrawManager.loadSysMLProjects();
		SysMLPartProperty sysMLPartProperty = MagicDrawManager
				.getPartPropertyByQualifiedName(projectId + "/partproperties/" + propertyQualifiedName);
		return sysMLPartProperty;
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public void getHtmlPartProperties(
			@PathParam("projectId") final String projectId) {
		MagicDrawManager.loadSysMLProjects();
		List<SysMLPartProperty> sysmlPartProperties = MagicDrawManager
				.getPartProperties(projectId);
		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlPartProperties != null) {			
			httpServletRequest.setAttribute("elements",
					sysmlPartProperties);
			httpServletRequest.setAttribute("requestURL", requestURL);
			httpServletRequest.setAttribute("projectId", projectId);
			RequestDispatcher rd = httpServletRequest
					.getRequestDispatcher("/sysml/sysml_partproperties_html.jsp");
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
	public void getHtmlPartProperty(
			@PathParam("projectId") final String projectId,
			@PathParam("qualifiedName") final String qualifiedName,
			@QueryParam("oslc.properties") final String propertiesString,
			@QueryParam("oslc.prefix") final String prefix)
			throws URISyntaxException, IOException {
		MagicDrawManager.loadSysMLProjects();
		edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPartProperty sysmlPartProperty = MagicDrawManager
				.getPartPropertyByQualifiedName(projectId + "/partproperties/" + qualifiedName);

		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlPartProperty != null) {
			
			httpServletRequest.setAttribute("partProperty", sysmlPartProperty);
			httpServletRequest.setAttribute("requestURL", requestURL);
			RequestDispatcher rd = httpServletRequest
					.getRequestDispatcher("/sysml/sysml_partproperty_html.jsp");
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
	public void createHtmlPart(@PathParam("projectId") final String projectId)
			throws URISyntaxException, IOException {

		MagicDrawManager.loadSysMLProjects();		

		List<String> possibleBlocks = new ArrayList<String>();
		possibleBlocks.add("NONE");		
		for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class mdBlock : MagicDrawManager.mdSysmlBlocks) {
			possibleBlocks.add("BLOCK___"
					+ mdBlock.getQualifiedName());
		}

		EClass eClass = ServiceUtil.getEClass("PartProperty");
		
		httpServletRequest.setAttribute("creatorUri",
				MagicDrawManager.baseHTTPURI + "/services/" + projectId
						+ "/partproperties");
		httpServletRequest.setAttribute("eclass", eClass);
		httpServletRequest.setAttribute("possibleBlocks", possibleBlocks);


		RequestDispatcher rd = httpServletRequest
				.getRequestDispatcher("/sysml/sysml_partproperty_creator.jsp");
		try {
			rd.forward(httpServletRequest, httpServletResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		}

	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	public void addPartFromHtmlForm(
			@PathParam("projectId") final String projectId,
			@FormParam("name") final String elementName,
			@FormParam("lower") final String lower,
			@FormParam("upper") final String upper,
			@FormParam("type") final String type,		
			@FormParam("ownerElement") final String ownerElement)
			throws IOException, ServletException {

		MagicDrawManager.loadSysMLProjects();
		
		SysMLPartProperty newSysMLPart;
		try {
			newSysMLPart = new SysMLPartProperty();
			newSysMLPart.setName(elementName);
			newSysMLPart.setLower(lower);
			newSysMLPart.setUpper(upper);
			
			// owner
			// Unparse owner element string
			String[] ownerElementStrings = ownerElement.split("_");
			String ownerName = ownerElementStrings[ownerElementStrings.length-1];
			ownerName = ownerName.replaceAll("\\n", "-").replaceAll(" ", "_");
			URI ownerURI = URI
					.create(MagicDrawManager.baseHTTPURI + "/services/"
							+ projectId + "/blocks/" + ownerName);
			newSysMLPart.setOwner(ownerURI);			
			
			// URI
			URI elementURI = URI
					.create(MagicDrawManager.baseHTTPURI + "/services/"
							+ projectId + "/parts/" + ownerName + "::" + elementName);
			newSysMLPart.setAbout(elementURI);	
			
			// type
			String[] typeElementStrings = type.split("_");
			String typeQualifiedName = typeElementStrings[typeElementStrings.length-1];
			URI typeURI = MagicDrawManager.getURIFromQualifiedName(typeQualifiedName);
			newSysMLPart.setType(typeURI);
			
			MagicDrawManager.createSysMLPartProperty(newSysMLPart, projectId);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {		
			
			httpServletRequest.setAttribute("elementType", "PartProperty");
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
	
	
	@OslcCreationFactory(title = "SysML PartProperty Creation Factory", label = "SysML PartProperty Creation", resourceShapes = { OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_PARTPROPERTY }, resourceTypes = { Constants.TYPE_SYSML_PARTPROPERTY }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	@POST
	@Consumes({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public Response addPartProperty(
			@PathParam("projectId") final String projectId,
			final SysMLPartProperty sysmlPart) throws IOException,
			ServletException {
//		MagicDrawManager.loadSysMLProjects();
		MagicDrawManager.makeSysMLProjectActive(projectId);
		// creating the element in MagicDraw
		MagicDrawManager.createSysMLPartProperty(sysmlPart, projectId);
		URI about = sysmlPart.getAbout();
		return Response.created(about).entity(sysmlPart).build();
	}
}
