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
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLConnector;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLConnectorEnd;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPartProperty;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPort;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLReferenceProperty;
import org.eclipse.lyo.oslc4j.core.annotation.OslcCreationFactory;
import org.eclipse.lyo.oslc4j.core.annotation.OslcName;
import org.eclipse.lyo.oslc4j.core.annotation.OslcNamespace;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcResourceShape;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.Link;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port;

import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;

/**
 * This servlet contains the implementation of OSLC RESTful web services for SysMLConnector resources.
 * 
 * The servlet contains web services for: <ul>
 * <li> returning specific SysMLConnector resources in HTML and other formats </li>
 * <li> returning all SysMLBlock resources within a specific MagicDraw project
 *  in HTML and other formats </li>
 *  <li> adding new SysMLConnector resources to a specific MagicDraw project</li>
 *  </ul>
 *  <br>
 *  The servlet also contains RESTful web services for: 
 *  <li> returning an HTML page with forms that a user can fill out and submit for 
 *  adding new SysMLConnector resources to a specific MagicDraw project</li>  
 *  <li> adding new SysMLConnector resources to a specific MagicDraw project based on
 *  submitted HTML forms</li> 
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
@OslcService(Constants.SYSML_CONNECTOR_DOMAIN)
@Path("{projectId}/connectors")
public class SysMLConnectorService extends HttpServlet {

	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;

	static int connectorEndID = 0;

	@OslcQueryCapability(title = "SysML Connector Query Capability", label = "SysML Connector Catalog Query", resourceShape = OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_CONNECTOR, resourceTypes = { Constants.TYPE_SYSML_CONNECTOR }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	@GET
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public List<edu.gatech.mbsec.adapter.magicdraw.resources.SysMLConnector> getConnectors(
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
		return MagicDrawManager.getConnectors(projectId);
	}

	@GET
	@Path("{propertyQualifiedName}")
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_JSON })
	public edu.gatech.mbsec.adapter.magicdraw.resources.SysMLConnector getConnector(
			@PathParam("projectId") final String projectId,
			@PathParam("propertyQualifiedName") final String propertyQualifiedName)
			throws URISyntaxException {
		MagicDrawManager.loadSysMLProjects();
		SysMLConnector sysMLConnector = MagicDrawManager
				.getConnectorByQualifiedName(projectId + "/connectors/" + propertyQualifiedName);
		return sysMLConnector;
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public void getHtmlConnectors(@PathParam("projectId") final String projectId) {
		MagicDrawManager.loadSysMLProjects();
		List<SysMLConnector> sysmlConnectors = MagicDrawManager.getConnectors(projectId);
		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlConnectors != null) {			
			httpServletRequest.setAttribute("elements", sysmlConnectors);
			httpServletRequest.setAttribute("requestURL", requestURL);
			httpServletRequest.setAttribute("projectId", projectId);
			RequestDispatcher rd = httpServletRequest
					.getRequestDispatcher("/sysml/sysml_connectors_html.jsp");
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
	public void getHtmlConnector(
			@PathParam("projectId") final String projectId,
			@PathParam("qualifiedName") final String qualifiedName,
			@QueryParam("oslc.properties") final String propertiesString,
			@QueryParam("oslc.prefix") final String prefix)
			throws URISyntaxException, IOException {
		MagicDrawManager.loadSysMLProjects();
		edu.gatech.mbsec.adapter.magicdraw.resources.SysMLConnector sysmlConnector = MagicDrawManager
				.getConnectorByQualifiedName(projectId + "/connectors/" + qualifiedName);

		String requestURL = httpServletRequest.getRequestURL().toString();
		if (sysmlConnector != null) {			
			httpServletRequest.setAttribute("connector", sysmlConnector);
			httpServletRequest.setAttribute("requestURL", requestURL);
			RequestDispatcher rd = httpServletRequest
					.getRequestDispatcher("/sysml/sysml_connector_html.jsp");
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
	public void createHtmlConnector(
			@PathParam("projectId") final String projectId)
			throws URISyntaxException, IOException {

		MagicDrawManager.loadSysMLProjects();
		
		List<String> possibleBlocks = new ArrayList<String>();
		possibleBlocks.add("NONE");
		for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class mdBlock : MagicDrawManager.mdSysmlBlocks) {
			possibleBlocks.add("BLOCK___"
					+ mdBlock.getQualifiedName());
		}
		
		List<String> possibleParts = new ArrayList<String>();
		possibleParts.add("NONE");
		for (Property mdPart : MagicDrawManager.mdSysmlPartProperties) {
			possibleParts.add("PART___"
					+ mdPart.getQualifiedName());
		}

		List<String> possiblePorts = new ArrayList<String>();
		possiblePorts.add("NONE");
		for (Port mdPort : MagicDrawManager.mdSysmlPorts) {
			possibleParts.add("PORT___"
					+ mdPort.getQualifiedName());
		}

		List<String> possibleRoles = new ArrayList<String>();
		possibleRoles.addAll(possibleParts);
		possibleRoles.addAll(possiblePorts);

		EClass eClass = ServiceUtil.getEClass("Connector");
		
		httpServletRequest.setAttribute("creatorUri",
				MagicDrawManager.baseHTTPURI + "/services/" + projectId
						+ "/connectors");
		httpServletRequest.setAttribute("eclass", eClass);
		httpServletRequest.setAttribute("possibleBlocks", possibleBlocks);
		httpServletRequest.setAttribute("possibleParts", possibleParts);
		httpServletRequest.setAttribute("possibleRoles", possibleRoles);

		RequestDispatcher rd = httpServletRequest
				.getRequestDispatcher("/sysml/sysml_connector_creator.jsp");
		try {
			rd.forward(httpServletRequest, httpServletResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		}

	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	public void addConnectorFromHtmlForm(
			@PathParam("projectId") final String projectId,
			@FormParam("name") final String elementName,
			@FormParam("role1") final String role1,
			@FormParam("partWithPort1") final String partWithPort1,
			@FormParam("role2") final String role2,
			@FormParam("partWithPort2") final String partWithPort2,
			@FormParam("ownerElement") final String ownerElement)
			throws IOException, ServletException {

//		MagicDrawManager.loadSysMLProjects();
		MagicDrawManager.makeSysMLProjectActive(projectId);

		// Unparse owner element string
		String[] ownerElementStrings = ownerElement.split("_");
		// String ownerType = ownerElementStrings[0];
		String ownerName = ownerElementStrings[ownerElementStrings.length - 1];
		ownerName = ownerName.replaceAll("\\n", "-").replaceAll(" ", "_");
		
		SysMLConnector newSysMLConnector;
		try {
			newSysMLConnector = new SysMLConnector();
			newSysMLConnector.setName(elementName);

			// owner
			URI ownerURI = MagicDrawManager
					.getURIFromQualifiedName(ownerElement);
			newSysMLConnector.setOwner(ownerURI);

			// URI
			URI elementURI = URI
					.create(MagicDrawManager.baseHTTPURI + "/services/"
							+ projectId + "/connectors/" + ownerName + "::"
							+ elementName);
			newSysMLConnector.setAbout(elementURI);

			// connector ends
			SysMLConnectorEnd end1 = new SysMLConnectorEnd();
			URI end1URI = URI
					.create(MagicDrawManager.baseHTTPURI + "/services/"
							+ projectId + "/connectorends/"
							+ getConnectorEndID());
			end1.setAbout(end1URI);
			SysMLConnectorEnd end2 = new SysMLConnectorEnd();
			URI end2URI = URI
					.create(MagicDrawManager.baseHTTPURI + "/services/"
							+ projectId + "/connectorends/"
							+ getConnectorEndID());
			end2.setAbout(end2URI);
			MagicDrawManager.createSysMLConnectorEnd(end1, projectId);
			MagicDrawManager.createSysMLConnectorEnd(end2, projectId);

			// end 1
			URI role1URI = MagicDrawManager.getURIFromQualifiedName(role1);
			end1.setRole(role1URI);
			if (partWithPort1 != null) {
				URI partWithPort1URI = MagicDrawManager
						.getURIFromQualifiedName(partWithPort1);
				end1.setPartWithPort(partWithPort1URI);
			}

			// end 2
			URI role2URI = MagicDrawManager.getURIFromQualifiedName(role2);
			end2.setRole(role2URI);
			if (partWithPort2 != null) {
				URI partWithPort2URI = MagicDrawManager
						.getURIFromQualifiedName(partWithPort2);
				end2.setPartWithPort(partWithPort2URI);
			}

			Link[] connectorEndsLinks = new Link[2];
			connectorEndsLinks[0] = new Link(end1URI);
			connectorEndsLinks[1] = new Link(end2URI);
			newSysMLConnector.setEnds(connectorEndsLinks);

			MagicDrawManager.createSysMLConnector(newSysMLConnector, projectId);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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

	private int getConnectorEndID() {
		connectorEndID++;
		return connectorEndID;
	}

	@OslcCreationFactory(title = "SysML Connector Creation Factory", label = "SysML Connector Creation", resourceShapes = { OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SYSML_CONNECTOR }, resourceTypes = { Constants.TYPE_SYSML_CONNECTOR }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	@POST
	@Consumes({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public Response addConnector(
			@PathParam("projectId") final String projectId,
			final SysMLConnector sysmlConnector) throws IOException,
			ServletException {

		// load all elements from MagicDraw project if not yet performed
//		MagicDrawManager.loadSysMLProjects();
		MagicDrawManager.makeSysMLProjectActive(projectId);
		
		// creating the element in MagicDraw
		MagicDrawManager.createSysMLConnector(sysmlConnector, projectId);
		URI about = sysmlConnector.getAbout();
		return Response.created(about).entity(sysmlConnector).build();
	}
}
