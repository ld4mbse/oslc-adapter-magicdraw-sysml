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


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.swing.JOptionPane;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;










/**
 * OSLC4MBSESpecificationService contains the implementation of the web service
 * returning an HTML representation of the SysML concepts supported by the OSLC
 * MagicDraw adapter. The HTML representation is similar to the HTML representation
 * of OSLC specifications. 
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
@Path("specification")
public class OSLC4MBSESpecificationService
{
	@Context private HttpServletRequest httpServletRequest;
	@Context private HttpServletResponse httpServletResponse;
	@Context private UriInfo uriInfo;
   
    
    
   

  
    
    /**
     * Return the catalog singleton as HTML
     * 
     * Forwards to serviceprovidercatalog_html.jsp to build the html
     * 
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public void getHtmlServiceProvider()
    {
    	// load sysml.ecore model
    			Resource ecoreResource = loadEcoreModel(org.eclipse.emf.common.util.URI.createFileURI(new File(
    					OSLC4JMagicDrawApplication.sysmlEcoreLocation).getAbsolutePath()));

    			EPackage sysmlPackage = (EPackage) EcoreUtil.getObjectByType(
    					ecoreResource.getContents(),
    					EcorePackage.eINSTANCE.getEPackage());
    			
	        httpServletRequest.setAttribute("sysmlPackage", sysmlPackage);
//	        httpServletRequest.setAttribute("catalog",catalog);

	        RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/oslc4mbse specification/specification_html.jsp");
    		try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {				
				e.printStackTrace();
				throw new WebApplicationException(e);
			} 
    	
    }
    
    private static Resource loadEcoreModel(org.eclipse.emf.common.util.URI fileURI) {
		// Create a resource set.
		ResourceSet resourceSet = new ResourceSetImpl();

		// Register the default resource factory -- only needed for stand-alone!
		resourceSet
				.getResourceFactoryRegistry()
				.getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());

		// Register the package -- only needed for stand-alone!
		EcorePackage ecorePackage = EcorePackage.eINSTANCE;

		// Demand load the resource for this file.
		Resource resource = resourceSet.getResource(fileURI, true);
		return resource;
	}
}