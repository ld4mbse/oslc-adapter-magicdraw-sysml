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
import javax.swing.JOptionPane;
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

import edu.gatech.mbsec.adapter.magicdraw.resources.Constants;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLReferenceProperty;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLRequirement;
import org.eclipse.lyo.oslc4j.core.annotation.OslcCreationFactory;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;

import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;

/**
 * This servlet contains the implementation of OSLC RESTful web services for SysMLRequirement resources.
 * 
 * The servlet contains web services for: <ul margin-top: 0;>
 * <li> returning specific SysMLRequirement resources in HTML and other formats </li>
 * <li> returning all SysMLRequirement resources within a specific MagicDraw project
 *  in HTML and other formats </li>
 *  <li> adding new SysMLRequirement resources to a specific MagicDraw project</li>
 *  </ul>
 *  
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
@OslcService(Constants.SYSML_REQUIREMENT_DOMAIN)
@Path("{projectId}/requirements")
public class SysMLRequirementService extends HttpServlet {

	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;

	@OslcQueryCapability
	(
			title = "SysML Requirement Query Capability",
	        label = "SysML Requirement Catalog Query",
	        resourceShape = OslcConstants.PATH_RESOURCE_SHAPES + "/" + Constants.PATH_SYSML_REQUIREMENT,
	        resourceTypes = {Constants.TYPE_SYSML_REQUIREMENT},
	        usages = {OslcConstants.OSLC_USAGE_DEFAULT}
	)
	
	@GET
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public List<edu.gatech.mbsec.adapter.magicdraw.resources.SysMLRequirement> getRequirements(
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
		return MagicDrawManager.getRequirements();
	}
	
	@GET
	@Path("{requirementId}")
	@Produces({ OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_JSON })
	public SysMLRequirement getRequirement(@PathParam("projectId") final String projectId, 
			@PathParam("requirementId") final String requirementId)
			throws URISyntaxException {		 
		MagicDrawManager.loadSysMLProjects();
		SysMLRequirement sysMLRequirement = MagicDrawManager.getRequirementByID(projectId + "/requirements/" + requirementId);
		return sysMLRequirement;
	}

	@GET
    @Produces(MediaType.TEXT_HTML)
    public void getHtmlRequirements(@PathParam("projectId") final String projectId)
    {				
		MagicDrawManager.loadSysMLProjects();
		List<SysMLRequirement> sysmlRequirements = MagicDrawManager.getRequirements();		
		String requestURL = httpServletRequest.getRequestURL().toString();
    	if (sysmlRequirements !=null )
    	{        
	        httpServletRequest.setAttribute("elements", sysmlRequirements);	        
	        httpServletRequest.setAttribute("requestURL",requestURL);
	        httpServletRequest.setAttribute("projectId",projectId);	        
	        RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/sysml/sysml_requirements_html.jsp");
    		try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {				
				e.printStackTrace();
				throw new WebApplicationException(e);
			} 
    	}
    }
	
	@GET
	@Path("{reqID}")
	@Produces(MediaType.TEXT_HTML)
	public void getHtmlRequirement(
			@PathParam("projectId") final String projectId,
			@PathParam("reqID") final String reqID,
			@QueryParam("oslc.properties") final String propertiesString,
			@QueryParam("oslc.prefix") final String prefix)
			throws URISyntaxException, IOException {
		MagicDrawManager.loadSysMLProjects();
		edu.gatech.mbsec.adapter.magicdraw.resources.SysMLRequirement sysmlRequirement = MagicDrawManager.getRequirementByID(projectId + "/requirements/" + reqID);
	
		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlRequirement !=null )
    	{
	        
	        httpServletRequest.setAttribute("requirement", sysmlRequirement);
	        httpServletRequest.setAttribute("requestURL",requestURL);
	        RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/sysml/sysml_requirement_html.jsp");
    		try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {				
				e.printStackTrace();
				throw new WebApplicationException(e);
			} 
    	}	
	}
	
	@OslcCreationFactory(title = "SysML Requirement Creation Factory", label = "SysML Requirement Creation", resourceShapes = { OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_REQUIREMENT }, resourceTypes = { Constants.TYPE_SYSML_REQUIREMENT }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	@POST
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public SysMLRequirement addRequirement(@PathParam("projectId") final String projectId)
			throws IOException, ServletException {		
//		MagicDrawManager.loadSysMLProjects();
		MagicDrawManager.makeSysMLProjectActive(projectId);
		// get element name from HTTP header, header = "NewElementName", Value =
		// "MyNewPackage"
		String newElementName = httpServletRequest.getHeader("NewElementName");
		if (newElementName == null) {
			newElementName = "NewElement";
		}

		// get owner name from HTTP header, header = "OwnerName", Value =
		// "MyNewPackage"
		String ownerName = httpServletRequest.getHeader("OwnerName");
		if (ownerName == null) {
			return null;
		}

		final SysMLRequirement newSysMLRequirement = MagicDrawManager
				.createSysMLRequirement(newElementName, ownerName, projectId);
		return newSysMLRequirement;
	}
}