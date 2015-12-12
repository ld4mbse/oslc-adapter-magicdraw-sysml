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
package edu.gatech.mbsec.adapter.magicdraw.clients;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.apache.wink.client.handlers.BasicAuthSecurityHandler;
import org.apache.wink.client.handlers.ClientHandler;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLBlock;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLModel;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLPackage;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLPartProperty;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLReferenceProperty;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLRequirement;
import org.eclipse.lyo.oslc4j.client.OslcRestClient;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
//import org.eclipse.lyo.oslc4j.provider.json4j.Json4JProvidersRegistry;

import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;





/**
 * The main() method of TestOSLCClientWithApacheWink sends an 
 * HTTP request to receive an RDF/XML representation of the ServiceProviderCatalog resource
 * as provided by the OSLC MagicDraw SysML adapter. The HTTP response is transformed into a POJO using OSLC4J. 
 * The client then uses the Java API of the ServiceProviderCatalog resource to retrieve
 * the ServicerProvider resources, the query services and their query base, and then perform multiple queries. 
 * The query results are then printed to the console.
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
public class TestOSLCClientWithApacheWink {

	private static final Set<Class<?>> PROVIDERS = new HashSet<Class<?>>();

	static {
		PROVIDERS.addAll(JenaProvidersRegistry.getProviders());
		// PROVIDERS.addAll(Json4JProvidersRegistry.getProviders());
	}

	public static void main(String[] args) {

		// URI of the HTTP request
		String serviceProviderCatalogURI = MagicDrawManager.baseHTTPURI + "/services/catalog/singleton";

		// expected mediatype
		String mediaType = "application/rdf+xml";
		
		// readTimeout specifies how long the RestClient object waits (in
		// milliseconds) for a response before timing out
		int readTimeout = 120000;

		// set up the credentials for the basic authentication
		BasicAuthSecurityHandler basicAuthHandler = new BasicAuthSecurityHandler();
		basicAuthHandler.setUserName("foo");
		basicAuthHandler.setPassword("bar");

		// set up the HTTP connection
		final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS,
				serviceProviderCatalogURI, mediaType, readTimeout,
				basicAuthHandler);

		// retrieve the serviceProviderCatalog as POJO
		final ServiceProviderCatalog serviceProviderCatalog = oslcRestClient
				.getOslcResource(ServiceProviderCatalog.class);
		System.out.println(serviceProviderCatalog.getTitle());

		// retrieve queryCapabilities of the serviceProviderCatalog
		for (ServiceProvider serviceProvider : serviceProviderCatalog
				.getServiceProviders()) {
			
			if(!serviceProvider.getTitle().equals("TestProject3")){
				continue;
			}
			System.out.println(serviceProvider.getTitle());
			for (Service service : serviceProvider.getServices()) {
				for (QueryCapability queryCapability : service
						.getQueryCapabilities()) {					
					System.out.println(queryCapability.getQueryBase());
					String queryCapabilityURI = queryCapability.getQueryBase()
							.toString();
					final OslcRestClient oslcRestClient2 = new OslcRestClient(
							PROVIDERS, queryCapabilityURI, mediaType, 120000,
							basicAuthHandler);
					String resourceShapeURI = queryCapability.getResourceShape().toString();
					
					// retrieve all SysML Blocks 
					if(resourceShapeURI.endsWith("block")){
						final SysMLBlock[] sysmlBlocks = oslcRestClient2
								.getOslcResource(SysMLBlock[].class);
						for (SysMLBlock sysMLBlock : sysmlBlocks) {							
							System.out.println(sysMLBlock.getName());
						}
					}
					
					// retrieve SysML Model
					else if(resourceShapeURI.endsWith("model")){
						final SysMLModel[] sysMLModels = oslcRestClient2
								.getOslcResource(SysMLModel[].class);
						for (SysMLModel sysMLModel : sysMLModels) {
							System.out.println(sysMLModel.getName());
						}
					}
					
					// retrieve SysML Packages
					else if(resourceShapeURI.endsWith("package")){
						final SysMLPackage[] sysMLPackages = oslcRestClient2
								.getOslcResource(SysMLPackage[].class);
						for (SysMLPackage sysMLPackage : sysMLPackages) {
							System.out.println(sysMLPackage.getName());
						}
					}
					
					// retrieve SysML Requirements
					else if(resourceShapeURI.endsWith("requirement")){
						final SysMLRequirement[] sysMLRequirements = oslcRestClient2
								.getOslcResource(SysMLRequirement[].class);
						for (SysMLRequirement sysMLRequirement : sysMLRequirements) {
							System.out.println(sysMLRequirement.getIdentifier());
						}
					}
					
					// retrieve SysML PartProperties
					else if(resourceShapeURI.endsWith("partProperty")){
						final SysMLPartProperty[] sysMLPartProperties = oslcRestClient2
								.getOslcResource(SysMLPartProperty[].class);
						for (SysMLPartProperty sysMLPartProperty : sysMLPartProperties) {
							System.out.println(sysMLPartProperty.getType().toString() + "::" + sysMLPartProperty.getName());
						}
					}
					
					// retrieve SysML ReferenceProperties
					else if(resourceShapeURI.endsWith("referenceProperty")){
						final SysMLReferenceProperty[] sysMLReferenceProperties = oslcRestClient2
								.getOslcResource(SysMLReferenceProperty[].class);
						for (SysMLReferenceProperty sysMLReferenceProperty : sysMLReferenceProperties) {
							System.out.println(sysMLReferenceProperty.getType().toString() + "::" + sysMLReferenceProperty.getName());
						}
					}
				}
			}
		}

	}

}
