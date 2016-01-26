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
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import edu.gatech.mbsec.adapter.magicdraw.resources.Constants;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLAssociationBlock;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLRequirement;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLValueType;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;

import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;

/**
 * This servlet contains the implementation of OSLC RESTful web services for SysMLValueType resources.
 * 
 * The servlet contains web services for: <ul margin-top: 0;>
 * <li> returning specific SysMLValueType resources in HTML and other formats </li>
 * <li> returning all SysMLValueType resources within a specific MagicDraw project
 *  in HTML and other formats </li>
 *  </ul>
 *  
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
@OslcService(Constants.SYSML_VALUETYPE_DOMAIN)
@Path("{projectId}/valuetypes")
public class SysMLValueTypeService {

	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;

	@OslcQueryCapability(title = "SysML Value Type Query Capability", label = "SysML ValueType Catalog Query", resourceShape = OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_VALUETYPE, resourceTypes = { Constants.TYPE_SYSML_VALUETYPE }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	
	@GET
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public List<edu.gatech.mbsec.adapter.magicdraw.resources.SysMLValueType> getValueTypes(
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
		return MagicDrawManager.getValueTypes(projectId);
	}

	@GET
	@Path("{blockQualifiedName}")
	@Produces({ OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_JSON })
	public edu.gatech.mbsec.adapter.magicdraw.resources.SysMLValueType getValueType(
			@PathParam("projectId") final String projectId,
			@PathParam("blockQualifiedName") final String blockQualifiedName)
			throws URISyntaxException {
		MagicDrawManager.loadSysMLProjects();
		edu.gatech.mbsec.adapter.magicdraw.resources.SysMLValueType sysmlValueType = MagicDrawManager
				.getValueTypeByQualifiedName(projectId + "/valuetypes/" + blockQualifiedName);
		return sysmlValueType;
	}
	
	@GET
    @Produces(MediaType.TEXT_HTML)
    public void getHtmlValueTypes(@PathParam("projectId") final String projectId)
    {				
		MagicDrawManager.loadSysMLProjects();
		List<SysMLValueType> sysmlValueTypes = MagicDrawManager.getValueTypes(projectId);		
		String requestURL = httpServletRequest.getRequestURL().toString();
    	if (sysmlValueTypes !=null )
    	{        
	        httpServletRequest.setAttribute("elements", sysmlValueTypes);	        
	        httpServletRequest.setAttribute("requestURL",requestURL);
	        httpServletRequest.setAttribute("projectId",projectId);	        
	        RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/sysml/sysml_valuetypes_html.jsp");
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
	public void getHtmlValueType(
			@PathParam("projectId") final String projectId,
			@PathParam("blockQualifiedName") final String blockQualifiedName,
			@QueryParam("oslc.properties") final String propertiesString,
			@QueryParam("oslc.prefix") final String prefix)
			throws URISyntaxException, IOException {
		MagicDrawManager.loadSysMLProjects();
		edu.gatech.mbsec.adapter.magicdraw.resources.SysMLValueType sysmlValueType = 
				MagicDrawManager.getValueTypeByQualifiedName(projectId + "/valuetypes/" + blockQualifiedName);
	
		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlValueType !=null )
    	{	        
	        httpServletRequest.setAttribute("valuetype", sysmlValueType);
	        httpServletRequest.setAttribute("requestURL",requestURL);
	        RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/sysml/sysml_valuetype_html.jsp");
    		try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {				
				e.printStackTrace();
				throw new WebApplicationException(e);
			} 
    	}	
	}
}
