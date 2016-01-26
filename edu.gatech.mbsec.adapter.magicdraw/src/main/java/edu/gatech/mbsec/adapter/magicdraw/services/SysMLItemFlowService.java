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
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLAssociationBlock;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLBlock;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLItemFlow;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLRequirement;
import org.eclipse.lyo.oslc4j.core.annotation.OslcCreationFactory;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;

import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;

/**
 * This servlet contains the implementation of OSLC RESTful web services for SysMLItemFlow resources.
 * 
 * The servlet contains web services for: <ul margin-top: 0;>
 * <li> returning specific SysMLItemFlow resources in HTML and other formats </li>
 * <li> returning all SysMLItemFlow resources within a specific MagicDraw project
 *  in HTML and other formats </li>
 *  <li> adding new SysMLItemFlow resources to a specific MagicDraw project</li>
 *  </ul>
 *  
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
@OslcService(Constants.SYSML_ITEMFLOW_DOMAIN)
@Path("{projectId}/itemflows")
public class SysMLItemFlowService {

	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;

	@OslcQueryCapability(title = "SysML Item Flow Query Capability", label = "SysML Item Flow Catalog Query", resourceShape = OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_ITEMFLOW, resourceTypes = { Constants.TYPE_SYSML_ITEMFLOW }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	
	@GET
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public List<edu.gatech.mbsec.adapter.magicdraw.resources.SysMLItemFlow> getItemFlows(
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
		return MagicDrawManager.getItemFlows();
	}

	@GET
	@Path("{blockQualifiedName}")
	@Produces({ OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_JSON })
	public edu.gatech.mbsec.adapter.magicdraw.resources.SysMLItemFlow getItemFlow(
			@PathParam("projectId") final String projectId,
			@PathParam("blockQualifiedName") final String blockQualifiedName)
			throws URISyntaxException {
		MagicDrawManager.loadSysMLProjects();
		edu.gatech.mbsec.adapter.magicdraw.resources.SysMLItemFlow sysmlAssociationBlock = MagicDrawManager
				.getItemFlowByQualifiedName(blockQualifiedName);
		return sysmlAssociationBlock;
	}
	
	@GET
    @Produces(MediaType.TEXT_HTML)
    public void getHtmlItemFlows(@PathParam("projectId") final String projectId)
    {				
		MagicDrawManager.loadSysMLProjects();
		List<SysMLItemFlow> sysmlItemFlowslocks = MagicDrawManager.getItemFlows();		
		String requestURL = httpServletRequest.getRequestURL().toString();
    	if (sysmlItemFlowslocks !=null )
    	{	        
	        httpServletRequest.setAttribute("elements", sysmlItemFlowslocks);	        
	        httpServletRequest.setAttribute("requestURL",requestURL);
	        httpServletRequest.setAttribute("projectId",projectId);	        
	        RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/sysml/sysml_itemflows_html.jsp");
    		try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {				
				e.printStackTrace();
				throw new WebApplicationException(e);
			} 
    	}
    }

	
	@GET
	@Path("{blockQualifiedName}")
	@Produces(MediaType.TEXT_HTML)
	public void getHtmlItemFlow(
			@PathParam("projectId") final String projectId,
			@PathParam("blockQualifiedName") final String blockQualifiedName,
			@QueryParam("oslc.properties") final String propertiesString,
			@QueryParam("oslc.prefix") final String prefix)
			throws URISyntaxException, IOException {
		MagicDrawManager.loadSysMLProjects();
		edu.gatech.mbsec.adapter.magicdraw.resources.SysMLItemFlow sysmlItemFlow = MagicDrawManager.getItemFlowByQualifiedName(blockQualifiedName);
	
		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlItemFlow !=null )
    	{
	        
	        httpServletRequest.setAttribute("itemflow", sysmlItemFlow);
	        httpServletRequest.setAttribute("requestURL",requestURL);
	        RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/sysml/sysml_itemflow_html.jsp");
    		try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {				
				e.printStackTrace();
				throw new WebApplicationException(e);
			} 
    	}	
	}
	
	@OslcCreationFactory(title = "SysML ItemFlow Creation Factory", label = "SysML ItemFlow Creation", resourceShapes = { OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_ITEMFLOW }, resourceTypes = { Constants.TYPE_SYSML_ITEMFLOW }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	@POST
	@Consumes({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public Response addItemFlow(@PathParam("projectId") final String projectId,
			final SysMLItemFlow sysmlItemFlow) throws IOException, ServletException {		
//		MagicDrawManager.loadSysMLProjects();
		MagicDrawManager.makeSysMLProjectActive(projectId);
		MagicDrawManager.createSysMLItemFlow(sysmlItemFlow, projectId);
		URI about = sysmlItemFlow.getAbout();
		return Response.created(about).entity(sysmlItemFlow).build();
	}
}
