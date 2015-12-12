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
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.lyo.adapter.magicdraw.resources.Constants;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLBlock;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLModel;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLPackage;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLRequirement;
import org.eclipse.lyo.oslc4j.core.annotation.OslcCreationFactory;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.Link;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;

import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;

/**
 * This servlet contains the implementation of OSLC RESTful web services for SysMLBlock resources.
 * 
 * The servlet contains web services for: <ul margin-top: 0;>
 * <li> returning specific SysMLBlock resources in HTML and other formats </li>
 * <li> returning all SysMLBlock resources within a specific MagicDraw project
 *  in HTML and other formats </li>
 *  <li> adding new SysMLBlock resources to a specific MagicDraw project</li>
 *  </ul>
 *  <br>
 *  The servlet also contains RESTful web services for: 
 *  <li> returning an HTML page with forms that a user can fill out and submit for 
 *  adding new SysMLBlock resources to a specific MagicDraw project</li>  
 *  <li> adding new SysMLBlock resources to a specific MagicDraw project based on
 *  submitted HTML forms</li> 
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
@SuppressWarnings("serial")
@OslcService(Constants.SYSML_BLOCK_DOMAIN)
@Path("{projectId}/blocks")
public class SysMLBlockService extends HttpServlet {

	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;

	@OslcQueryCapability(title = "SysML Block Query Capability", label = "SysML Block Catalog Query", resourceShape = OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_BLOCK, resourceTypes = { Constants.TYPE_SYSML_BLOCK }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	@GET
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public List<org.eclipse.lyo.adapter.magicdraw.resources.SysMLBlock> getBlocks(
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
		return MagicDrawManager.getBlocks(projectId);
	}

	@GET
	@Path("{blockQualifiedName}")
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_JSON })
	public org.eclipse.lyo.adapter.magicdraw.resources.SysMLBlock getBlock(
			@PathParam("projectId") final String projectId,
			@PathParam("blockQualifiedName") final String blockQualifiedName)
			throws URISyntaxException {
		MagicDrawManager.loadSysMLProjects();
		org.eclipse.lyo.adapter.magicdraw.resources.SysMLBlock sysmlBlock = MagicDrawManager
				.getBlockByQualifiedName(projectId + "/blocks/" + blockQualifiedName);
		return sysmlBlock;
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public void getHtmlBlocks(@PathParam("projectId") final String projectId) {
		MagicDrawManager.loadSysMLProjects();
		List<SysMLBlock> sysmlBlocks = MagicDrawManager.getBlocks(projectId);
		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlBlocks != null) {			
			httpServletRequest.setAttribute("elements", sysmlBlocks);
			httpServletRequest.setAttribute("requestURL", requestURL);
			httpServletRequest.setAttribute("projectId", projectId);
			RequestDispatcher rd = httpServletRequest
					.getRequestDispatcher("/sysml/sysml_blocks_html.jsp");
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
	public void getHtmlBlock(@PathParam("projectId") final String projectId,
			@PathParam("blockQualifiedName") final String blockQualifiedName,
			@QueryParam("oslc.properties") final String propertiesString,
			@QueryParam("oslc.prefix") final String prefix)
			throws URISyntaxException, IOException {
		MagicDrawManager.loadSysMLProjects();
		org.eclipse.lyo.adapter.magicdraw.resources.SysMLBlock sysmlBlock = MagicDrawManager
				.getBlockByQualifiedName(projectId + "/blocks/" + blockQualifiedName);

		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlBlock != null) {			
			httpServletRequest.setAttribute("block", sysmlBlock);
			httpServletRequest.setAttribute("requestURL", requestURL);
			RequestDispatcher rd = httpServletRequest
					.getRequestDispatcher("/sysml/sysml_block_html.jsp");
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
	public void createHtmlBlock(@PathParam("projectId") final String projectId)
			throws URISyntaxException, IOException {

		MagicDrawManager.loadSysMLProjects();
			
		List<SysMLModel> sysmlModels = MagicDrawManager.getModels();		
		List<String> possibleOwnerElements = new ArrayList<String>();
		List<String> possibleBlocks = new ArrayList<String>();
		possibleBlocks.add("NONE");
		List<String> possibleRequirements = new ArrayList<String>();
		possibleRequirements.add("NONE");
		for (SysMLModel sysMLModel : sysmlModels) {
			possibleOwnerElements.add("MODEL___"
					+ MagicDrawManager.getQualifiedNameFromURI(sysMLModel
							.getAbout()));
		}
		for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package mdPackage : MagicDrawManager.mdSysmlPackages) {
			possibleOwnerElements.add("PACKAGE___"
					+ mdPackage.getQualifiedName());
		}
		for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class mdBlock : MagicDrawManager.mdSysmlBlocks) {
			possibleBlocks.add("BLOCK___"
					+ mdBlock.getQualifiedName());
		}
		for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class mdRequirement : MagicDrawManager.mdSysmlRequirements) {
			possibleRequirements.add("REQUIREMENT___"
					+mdRequirement.getQualifiedName());
		}

		EClass eClass = ServiceUtil.getEClass("Block");

		
		httpServletRequest.setAttribute("creatorUri",
				MagicDrawManager.baseHTTPURI + "/services/" + projectId
						+ "/blocks");
		httpServletRequest.setAttribute("eclass", eClass);
		httpServletRequest.setAttribute("possibleOwnerElements",
				possibleOwnerElements);
		httpServletRequest.setAttribute("possibleBlocks", possibleBlocks);
		httpServletRequest.setAttribute("possibleRequirements",
				possibleRequirements);

		RequestDispatcher rd = httpServletRequest
				.getRequestDispatcher("/sysml/sysml_block_creator.jsp");
		try {
			rd.forward(httpServletRequest, httpServletResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		}

	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	public void addBlockFromHtmlForm(
			@PathParam("projectId") final String projectId,
			@FormParam("name") final String elementName,
			@FormParam("inheritedBlock") final String inheritedBlock,
			@FormParam("satisfies") final String satisfies,
			@FormParam("ownerElement") final String ownerElement)
			throws IOException, ServletException {

//		MagicDrawManager.loadSysMLProjects();
		MagicDrawManager.makeSysMLProjectActive(projectId);

		// Unparse owner element string
		String[] ownerElementStrings = ownerElement.split("_");
		String ownerQualifiedName = ownerElementStrings[ownerElementStrings.length - 1];
		ownerQualifiedName = ownerQualifiedName.replaceAll("\\n", "-").replaceAll(" ", "_");

		// create new SysMLBlock OSLC resource
		SysMLBlock newSysMLBlock;
		try {
			newSysMLBlock = new SysMLBlock();

			// name
			newSysMLBlock.setName(elementName);

			// owner
			URI ownerURI = MagicDrawManager
					.getURIFromQualifiedName(ownerElement);
			newSysMLBlock.setOwner(ownerURI);

//			// inherited block
//			String[] inheritedBlockStrings = inheritedBlock.split("_");
//			String inheritedBlockQualifiedName = inheritedBlockStrings[inheritedBlockStrings.length - 1];
//			URI inheritedBlockURI = MagicDrawManager
//					.getURIFromQualifiedName(inheritedBlockQualifiedName);
//			Link inheritedBlockLink = new Link(inheritedBlockURI);
//			Link[] inheritedBlockLinks = new Link[1];
//			inheritedBlockLinks[0] = inheritedBlockLink;
//			newSysMLBlock.setInheritedBlocks(inheritedBlockLinks);

			// satisfies requirement
			String[] satisfiedRequirementStrings = satisfies.split("_");
			String satisfiedRequirementQualifiedName = satisfiedRequirementStrings[satisfiedRequirementStrings.length - 1];
			URI satisfiedRequirementURI = MagicDrawManager
					.getURIFromQualifiedName(satisfies);
			Link satisfiedRequirementLink = new Link(satisfiedRequirementURI);
			Link[] satisfiedRequirementLinks = new Link[1];
			satisfiedRequirementLinks[0] = satisfiedRequirementLink;
			newSysMLBlock.setSatisfies(satisfiedRequirementLinks);

			// URI
			URI elementURI = null;
			if (ownerElement.startsWith("MODEL")) {
				elementURI = URI
						.create(MagicDrawManager.baseHTTPURI + "/services/"
								+ projectId + "/blocks/" + elementName);
			} else {
				elementURI = URI
						.create(MagicDrawManager.baseHTTPURI + "/services/"
								+ projectId
								+ "/blocks/"
								+ ownerQualifiedName
								+ "::" + elementName);
			}
			newSysMLBlock.setAbout(elementURI);

			// create SysML Block in MagicDraw
			MagicDrawManager.createSysMLBlock(newSysMLBlock, projectId);

		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// send creation confirmation
		try {
			
			httpServletRequest.setAttribute("elementType", "Block");
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

	@OslcCreationFactory(title = "SysML Block Creation Factory", label = "SysML Block Creation", resourceShapes = { OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_BLOCK }, resourceTypes = { Constants.TYPE_SYSML_BLOCK }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	@POST
	@Consumes({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public Response addBlock(@PathParam("projectId") final String projectId,
			final SysMLBlock sysmlBlock) throws IOException, ServletException {
		System.out.println(sysmlBlock.getName());
//		MagicDrawManager.loadSysMLProjects();
//		MagicDrawManager.loadSysMLProject(projectId);
		MagicDrawManager.makeSysMLProjectActive(projectId);
		
		// make sure that the correct MagicDraw project is set as active and is loaded
		// global project variable of MagicDrawManager points to the last loaded project
		
		
		MagicDrawManager.createSysMLBlock(sysmlBlock, projectId);
		URI about = sysmlBlock.getAbout();
		return Response.created(about).entity(sysmlBlock).build();
	}

}
