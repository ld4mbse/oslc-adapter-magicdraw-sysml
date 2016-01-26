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
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLBlock;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLFullPort;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLModel;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPackage;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPartProperty;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLReferenceProperty;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLRequirement;
import org.eclipse.lyo.oslc4j.client.OslcRestClient;
import org.eclipse.lyo.oslc4j.core.model.Link;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
//import org.eclipse.lyo.oslc4j.provider.json4j.Json4JProvidersRegistry;

import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;





/**
 * The main() method of OSLCClient4GettingSysMLBlock sends an 
 * HTTP request to receive an RDF/XML representation of a SysML Block as provided by the 
 * OSLC MagicDraw SysML adapter. The HTTP response is transformed into a POJO using OSLC4J. 
 * The ports of the SysML Block are retrieved through the Java API of the SysML Block.
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
public class OSLCClient4GettingSysMLBlock {

	private static final Set<Class<?>> PROVIDERS = new HashSet<Class<?>>();

	static {
		PROVIDERS.addAll(JenaProvidersRegistry.getProviders());
		// PROVIDERS.addAll(Json4JProvidersRegistry.getProviders());
	}

	public static void main(String[] args) {

		// URI of the HTTP request
		String resourceURI = MagicDrawManager.baseHTTPURI  
				+ "/services/Wired_Camera_Example/blocks/Blocks::Wired_Camera";

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
				resourceURI, mediaType, readTimeout,
				basicAuthHandler);

		// retrieve the SysML block as POJO
		final SysMLBlock sysMLBlock = oslcRestClient
				.getOslcResource(SysMLBlock.class);
		System.out.println("SysML Block name: " + sysMLBlock.getName());
		System.out.println("SysML Block full ports:");
		for (Link sysMLFullPortLink : sysMLBlock.getFullPorts()) {
			System.out.println(sysMLFullPortLink.getValue());
		}
		System.out.println("SysML Block proxy ports:");
		for (Link sysMLProxyPortLink : sysMLBlock.getProxyPorts()) {
			System.out.println(sysMLProxyPortLink.getValue());
		}
	}

}
