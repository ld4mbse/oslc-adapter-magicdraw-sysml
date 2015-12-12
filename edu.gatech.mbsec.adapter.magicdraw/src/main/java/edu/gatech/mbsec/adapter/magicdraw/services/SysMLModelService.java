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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.eclipse.lyo.adapter.magicdraw.resources.Constants;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLModel;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLReferenceProperty;
import org.eclipse.lyo.oslc4j.core.OSLC4JConstants;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;

import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;
import edu.gatech.mbsec.adapter.magicdraw.serviceproviders.ServiceProviderCatalogSingleton;











/**
 * This servlet contains the implementation of OSLC RESTful web services for SysMLModel resources.
 * 
 * The servlet contains web services for returning specific SysMLModel resources 
 * in HTML and other formats.
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
@OslcService(Constants.SYSML_MODEL_DOMAIN)
@Path("{projectId}/model")
public class SysMLModelService extends HttpServlet {

	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;

	@OslcQueryCapability
	(
			title = "SysML Model Query Capability",
	        label = "SysML Model Catalog Query",
	        resourceShape = OslcConstants.PATH_RESOURCE_SHAPES + "/" + Constants.PATH_SYSML_MODEL,
	        resourceTypes = {Constants.TYPE_SYSML_MODEL},
	        usages = {OslcConstants.OSLC_USAGE_DEFAULT}
	)
	
	@GET
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
	public List<org.eclipse.lyo.adapter.magicdraw.resources.SysMLModel> getModels(
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
		return MagicDrawManager.getModels();
	}
	
	@GET
	@Path("{modelName}")
	@Produces({ OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_JSON })
	public org.eclipse.lyo.adapter.magicdraw.resources.SysMLModel getModel(
			@PathParam("projectId") final String projectId,
			@PathParam("modelName") final String modelName,
			@QueryParam("oslc.properties") final String propertiesString,
			@QueryParam("oslc.prefix") final String prefix)
			throws URISyntaxException, IOException {
//		Map<String, String> prefixMap;
//		try {
//			prefixMap = QueryUtils.parsePrefixes(prefix);
//		} catch (ParseException e) {
//			throw new IOException(e);
//		}
//		addDefaultPrefixes(prefixMap);

		MagicDrawManager.loadSysMLProjects();
		SysMLModel sysMLModel = MagicDrawManager.getModelByName(modelName);
//		setETagHeader(getETagFromChangeRequest(sysMLModel), httpServletResponse);

//		sysMLModel.setServiceProvider(ServiceProviderCatalogSingleton.getServiceProvider(httpServletRequest, projectId).getAbout());
//		sysMLModel.setAbout(ServiceUtils.getAboutURI(projectId + "/model/" + sysMLModel.getName()));
//        httpServletRequest.setAttribute(OSLC4JConstants.OSLC4J_SELECTED_PROPERTIES,
//                                        QueryUtils.invertSelectedProperties(properties));
		return sysMLModel;
	}


	@GET
    @Produces(MediaType.TEXT_HTML)
    public void getHtmlModel(@PathParam("projectId") final String projectId)
    {
		ServiceProviderCatalog catalog = ServiceProviderCatalogSingleton.getServiceProviderCatalog(httpServletRequest);
		
		MagicDrawManager.loadSysMLProjects();
		List<SysMLModel> sysmlModels = MagicDrawManager.getModels();
		SysMLModel sysMLModel = sysmlModels.get(0);

		String requestURL = httpServletRequest.getRequestURL().toString();


		
		
    	if (sysMLModel !=null )
    	{
	        
	        httpServletRequest.setAttribute("model", sysMLModel);
	        httpServletRequest.setAttribute("catalog",catalog);
	        httpServletRequest.setAttribute("requestURL",requestURL);

	        RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/sysml/sysml_model_html.jsp");
    		try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {				
				e.printStackTrace();
				throw new WebApplicationException(e);
			} 
    	}
    }
	
	
	

}
