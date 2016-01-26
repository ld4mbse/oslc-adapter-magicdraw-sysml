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
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
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
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPartProperty;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPort;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLReferenceProperty;
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
 * This servlet contains the implementation of OSLC RESTful web services for SysMLPort resources.
 * 
 * The servlet contains web services for: <ul margin-top: 0;>
 * <li> returning specific SysMLPort resources in HTML and other formats </li>
 * <li> returning all SysMLPort resources within a specific MagicDraw project
 *  in HTML and other formats </li>
 *  <li> adding new SysMLPort resources to a specific MagicDraw project</li>
 *  </ul>
 *  
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
@OslcService(Constants.SYSML_PORT_DOMAIN)
@Path("{projectId}/ports")
public class SysMLPortService extends HttpServlet{
	
	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;
	
	@OslcQueryCapability
	(
			title = "SysML Port Query Capability",
	        label = "SysML Port Catalog Query",
	        resourceShape = OslcConstants.PATH_RESOURCE_SHAPES + "/" + Constants.PATH_SYSML_PORT,
	        resourceTypes = {Constants.TYPE_SYSML_PORT},
	        usages = {OslcConstants.OSLC_USAGE_DEFAULT}
	)
	
	@GET
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public List<edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPort> getPorts(
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
		return MagicDrawManager.getPorts(projectId);
	}
	
	@GET
	@Path("{propertyQualifiedName}")
	@Produces({ OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_JSON})
	public edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPort getPort(@PathParam("projectId") final String projectId, @PathParam("propertyQualifiedName") final String propertyQualifiedName) throws URISyntaxException  {
		MagicDrawManager.loadSysMLProjects();
		SysMLPort sysMLPort = MagicDrawManager.getPortByQualifiedName(projectId + "/ports/" + propertyQualifiedName);
		return sysMLPort;
	}

	@GET
    @Produces(MediaType.TEXT_HTML)
    public void getHtmlPorts(@PathParam("projectId") final String projectId)
    {				
		MagicDrawManager.loadSysMLProjects();
		List<SysMLPort> sysmlPorts = MagicDrawManager.getPorts(projectId);		
		String requestURL = httpServletRequest.getRequestURL().toString();
    	if (sysmlPorts !=null )
    	{	        
	        httpServletRequest.setAttribute("elements", sysmlPorts);	        
	        httpServletRequest.setAttribute("requestURL",requestURL);
	        httpServletRequest.setAttribute("projectId",projectId);	        
	        RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/sysml/sysml_ports_html.jsp");
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
	public void getHtmlPort(
			@PathParam("projectId") final String projectId,
			@PathParam("qualifiedName") final String qualifiedName,
			@QueryParam("oslc.properties") final String propertiesString,
			@QueryParam("oslc.prefix") final String prefix)
			throws URISyntaxException, IOException {
		MagicDrawManager.loadSysMLProjects();
		edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPort sysmlPort = MagicDrawManager.getPortByQualifiedName(projectId + "/ports/" + qualifiedName);
	
		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlPort !=null )
    	{
	        
	        httpServletRequest.setAttribute("port", sysmlPort);
	        httpServletRequest.setAttribute("requestURL",requestURL);
	        RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/sysml/sysml_port_html.jsp");
    		try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {				
				e.printStackTrace();
				throw new WebApplicationException(e);
			} 
    	}	
	}
	
	@OslcCreationFactory(title = "SysML Port Creation Factory", label = "SysML Port Creation", resourceShapes = { OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_PORT }, resourceTypes = { Constants.TYPE_SYSML_PORT }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	@POST
	@Consumes({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public Response addPort(
			@PathParam("projectId") final String projectId,
			final SysMLPort sysmlPort) throws IOException,
			ServletException {
//		MagicDrawManager.loadSysMLProjects();
		MagicDrawManager.makeSysMLProjectActive(projectId);
		// creating the element in MagicDraw
		MagicDrawManager.createSysMLPort(sysmlPort, projectId);
		URI about = sysmlPort.getAbout();
		return Response.created(about).entity(sysmlPort).build();
	}
}
