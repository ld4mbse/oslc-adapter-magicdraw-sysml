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
import java.util.Map;

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

import org.eclipse.lyo.adapter.magicdraw.resources.Constants;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLBlock;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLInterfaceBlock;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLModel;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLPackage;
import org.eclipse.lyo.oslc4j.core.annotation.OslcCreationFactory;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;

import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;

/**
 * This servlet contains the implementation of OSLC RESTful web services for SysMLInterfaceBlock resources.
 * 
 * The servlet contains web services for: <ul margin-top: 0;>
 * <li> returning specific SysMLInterfaceBlock resources in HTML and other formats </li>
 * <li> returning all SysMLInterfaceBlock resources within a specific MagicDraw project
 *  in HTML and other formats </li>
 *  <li> adding new SysMLInterfaceBlock resources to a specific MagicDraw project</li>
 *  </ul>
 *  
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
@OslcService(Constants.SYSML_INTERFACEBLOCK_DOMAIN)
@Path("{projectId}/interfaceblocks")
public class SysMLInterfaceBlockService extends HttpServlet {

	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;

	@OslcQueryCapability(title = "SysML Interface Block Query Capability", label = "SysML Interface Block Catalog Query", resourceShape = OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_INTERFACEBLOCK, resourceTypes = { Constants.TYPE_SYSML_INTERFACEBLOCK }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	@GET
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public List<org.eclipse.lyo.adapter.magicdraw.resources.SysMLInterfaceBlock> getInterfaceBlocks(
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
		return MagicDrawManager.getInterfaceBlocks(projectId);
	}

	@GET
	@Path("{blockQualifiedName}")
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_JSON })
	public org.eclipse.lyo.adapter.magicdraw.resources.SysMLInterfaceBlock getInterfaceBlock(
			@PathParam("projectId") final String projectId,
			@PathParam("blockQualifiedName") final String blockQualifiedName)
			throws URISyntaxException {
		MagicDrawManager.loadSysMLProjects();
		org.eclipse.lyo.adapter.magicdraw.resources.SysMLInterfaceBlock sysmlInterfaceBlock = MagicDrawManager
				.getInterfaceBlockByQualifiedName(projectId + "/interfaceblocks/" + blockQualifiedName);
		return sysmlInterfaceBlock;
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public void getHtmlInterfaceBlocks(@PathParam("projectId") final String projectId) {
		MagicDrawManager.loadSysMLProjects();
		List<SysMLInterfaceBlock> sysmlInterfaceBlocks = MagicDrawManager.getInterfaceBlocks(projectId);
		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlInterfaceBlocks != null) {			
			httpServletRequest.setAttribute("elements", sysmlInterfaceBlocks);
			httpServletRequest.setAttribute("requestURL", requestURL);
			httpServletRequest.setAttribute("projectId", projectId);
			RequestDispatcher rd = httpServletRequest
					.getRequestDispatcher("/sysml/sysml_interfaceblocks_html.jsp");
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
	public void getHtmlInterfaceBlock(@PathParam("projectId") final String projectId,
			@PathParam("blockQualifiedName") final String blockQualifiedName,
			@QueryParam("oslc.properties") final String propertiesString,
			@QueryParam("oslc.prefix") final String prefix)
			throws URISyntaxException, IOException {
		MagicDrawManager.loadSysMLProjects();
		org.eclipse.lyo.adapter.magicdraw.resources.SysMLInterfaceBlock sysmlBlock = MagicDrawManager
				.getInterfaceBlockByQualifiedName(projectId + "/interfaceblocks/" + blockQualifiedName);

		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlBlock != null) {
			
			httpServletRequest.setAttribute("interfaceblock", sysmlBlock);
			httpServletRequest.setAttribute("requestURL", requestURL);
			RequestDispatcher rd = httpServletRequest
					.getRequestDispatcher("/sysml/sysml_interfaceblock_html.jsp");
			try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {
				e.printStackTrace();
				throw new WebApplicationException(e);
			}
		}
	}

	@OslcCreationFactory(title = "SysML Interface Block Creation Factory", label = "SysML Interface Block Creation", resourceShapes = { OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_INTERFACEBLOCK }, resourceTypes = { Constants.TYPE_SYSML_INTERFACEBLOCK }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	@POST
	@Consumes({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public Response addInterfaceBlock(@PathParam("projectId") final String projectId,
			final SysMLInterfaceBlock sysmlInterfaceBlock) throws IOException, ServletException {		
//		MagicDrawManager.loadSysMLProjects();
		MagicDrawManager.makeSysMLProjectActive(projectId);
		MagicDrawManager.createSysMLInterfaceBlock(sysmlInterfaceBlock, projectId);
		URI about = sysmlInterfaceBlock.getAbout();
		return Response.created(about).entity(sysmlInterfaceBlock).build();
	}
}
