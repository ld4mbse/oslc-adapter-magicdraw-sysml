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
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import edu.gatech.mbsec.adapter.magicdraw.resources.Constants;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLBlock;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLBlockDiagram;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLModel;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPackage;
import org.eclipse.lyo.oslc4j.core.annotation.OslcCreationFactory;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;

import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;

/**
 * This servlet contains the implementation of OSLC RESTful web services for 
 * <li> returning specific SysMLBlockDiagram resources in HTML and other formats </li>
 * <li> returning all SysMLBlockDiagram resources within a specific MagicDraw project
 *  in HTML and other formats </li>
 *
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
@OslcService(Constants.SYSML_BLOCKDIAGRAM_DOMAIN)
@Path("{projectId}/blockdiagrams")
public class SysMLBlockDiagramService extends HttpServlet {

	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;

	
//	@GET
//	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
//			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
//	public List<edu.gatech.mbsec.adapter.magicdraw.resources.SysMLBlock> getBlockDiagrams(
//			@PathParam("projectId") final String projectId,
//			@QueryParam("oslc.where") final String where,
//			@QueryParam("oslc.select") final String select,
//			@QueryParam("oslc.prefix") final String prefix,
//			@QueryParam("page") final String pageString,
//			@QueryParam("oslc.orderBy") final String orderBy,
//			@QueryParam("oslc.searchTerms") final String searchTerms,
//			@QueryParam("oslc.paging") final String paging,
//			@QueryParam("oslc.pageSize") final String pageSize)
//			throws IOException, ServletException {
//		MagicDrawManager.loadSysMLProject(projectId);
//		return MagicDrawManager.getBlocks();
//	}

//	@GET
//	@Path("{blockQualifiedName}")
//	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
//			OslcMediaType.APPLICATION_JSON })
//	public edu.gatech.mbsec.adapter.magicdraw.resources.SysMLBlock getBlockDiagram(
//			@PathParam("projectId") final String projectId,
//			@PathParam("blockQualifiedName") final String blockQualifiedName)
//			throws URISyntaxException {
//		MagicDrawManager.loadSysMLProject(projectId);
//		edu.gatech.mbsec.adapter.magicdraw.resources.SysMLBlock sysmlBlock = MagicDrawManager
//				.getBlockByQualifiedName(blockQualifiedName);
//		return sysmlBlock;
//	}

	@OslcQueryCapability(title = "SysML Block Diagram Query Capability", label = "SysML Block Diagram Catalog Query", resourceShape = OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_BLOCKDIAGRAM, resourceTypes = { Constants.TYPE_SYSML_BLOCKDIAGRAM }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	@GET
	@Produces(MediaType.TEXT_HTML)
	public void getHtmlBlockDiagrams(@PathParam("projectId") final String projectId) {
		MagicDrawManager.loadSysMLProjects();
		List<SysMLBlockDiagram> sysmlBlockDiagrams = MagicDrawManager.getBlockDiagrams(projectId);
		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlBlockDiagrams != null) {			
			httpServletRequest.setAttribute("elements", sysmlBlockDiagrams);
			httpServletRequest.setAttribute("requestURL", requestURL);
			httpServletRequest.setAttribute("projectId", projectId);
			RequestDispatcher rd = httpServletRequest
					.getRequestDispatcher("/sysml/sysml_blockdiagrams_html.jsp");
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
	public void getHtmlBlockDiagram(@PathParam("projectId") final String projectId,
			@PathParam("blockQualifiedName") final String blockQualifiedName,
			@QueryParam("oslc.properties") final String propertiesString,
			@QueryParam("oslc.prefix") final String prefix)
			throws URISyntaxException, IOException {
		MagicDrawManager.loadSysMLProjects();
		edu.gatech.mbsec.adapter.magicdraw.resources.SysMLBlockDiagram sysmlBlockDiagram = MagicDrawManager
				.getBlockDiagramByQualifiedName(projectId + "/blockdiagrams/" + blockQualifiedName);

		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlBlockDiagram != null) {			
			httpServletRequest.setAttribute("blockdiagram", sysmlBlockDiagram);
			httpServletRequest.setAttribute("requestURL", requestURL);
			RequestDispatcher rd = httpServletRequest
					.getRequestDispatcher("/sysml/sysml_blockdiagram_html.jsp");
			try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {
				e.printStackTrace();
				throw new WebApplicationException(e);
			}
		}
	}

	
}
