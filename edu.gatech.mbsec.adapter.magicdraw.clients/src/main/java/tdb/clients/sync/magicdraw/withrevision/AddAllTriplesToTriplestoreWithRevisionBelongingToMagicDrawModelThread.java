/*********************************************************************************************
 * Copyright (c) 2015 Model-Based Systems Engineering Center, Georgia Institute of Technology.
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
package tdb.clients.sync.magicdraw.withrevision;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;

import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLBlock;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLValueProperty;
import edu.gatech.mbsec.adapter.subversion.SubversionFile;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.provider.jena.JenaModelHelper;
import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
import org.glassfish.jersey.client.ClientConfig;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.GraphStore;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;


import util.TriplestoreUtil;

public class AddAllTriplesToTriplestoreWithRevisionBelongingToMagicDrawModelThread extends Thread{

	String fileName;
	String revision;
	
	public AddAllTriplesToTriplestoreWithRevisionBelongingToMagicDrawModelThread(String fileName, String revision){
		this.fileName = fileName;
		this.revision = revision;
	}
	
	public void start() {
		String directory = TriplestoreUtil.getTriplestoreLocation();
		Dataset dataset = TDBFactory.createDataset(directory);
		
		ClientConfig clientConfig = new ClientConfig();
		for (Class providerClass : JenaProvidersRegistry.getProviders()) {
			clientConfig.register(providerClass);
		}
		Client rdfclient = ClientBuilder.newClient(clientConfig);
		System.out.println(MagicDrawAdapterAndTDBSubversionSyncClientWithRevision.oslcServiceProviderCatalogURI);
		Response response = rdfclient.target(MagicDrawAdapterAndTDBSubversionSyncClientWithRevision.oslcServiceProviderCatalogURI).request("application/rdf+xml").get();
		System.out.println(response.getStatus());
		ServiceProviderCatalog serviceProviderCatalog = response.readEntity(ServiceProviderCatalog.class);			
		
		
		
		
		
		// list to collect all resources
		ArrayList<AbstractResource> oslcResourcesArrayList = new ArrayList<AbstractResource>();
		
		for (ServiceProvider serviceProvider : serviceProviderCatalog.getServiceProviders()) {
			System.out.println("serviceProvider " + serviceProvider.getAbout());
			if(serviceProvider.getAbout().toString().endsWith("/serviceProviders/" + fileName)){
								
				
				
				
				for (Service service : serviceProvider.getServices()) {
					for (QueryCapability queryCapability : service.getQueryCapabilities()) {					
						System.out.println(queryCapability.getQueryBase());
						Response queryCapabilityResponse = rdfclient.target(queryCapability.getQueryBase()).request("application/rdf+xml").get();
						System.out.println(queryCapabilityResponse.getStatus());
						if(queryCapability.getQueryBase().toString().endsWith("blocks")){
							SysMLBlock[] oslcResources = queryCapabilityResponse.readEntity(SysMLBlock[].class);
							oslcResourcesArrayList.addAll(Arrays.asList(getResourcesWithVersion(oslcResources, revision)));
						}
						if(queryCapability.getQueryBase().toString().endsWith("valueproperties")){
							SysMLValueProperty[] oslcResources = queryCapabilityResponse.readEntity(SysMLValueProperty[].class);
							oslcResourcesArrayList.addAll(Arrays.asList(getResourcesWithVersion(oslcResources, revision)));
						}
						
					}
				}
			}
			
		}
		
		
		
		Object[] objects = oslcResourcesArrayList.toArray();

		Model model;
		Model tdbModel = dataset.getDefaultModel();
		try {
			model = JenaModelHelper.createJenaModel(objects);
			tdbModel.add(model);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OslcCoreApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tdbModel.close();
		dataset.close();
	}
	
	protected static AbstractResource[] getResourcesWithVersion(AbstractResource[] oslcResources, String revision) {
		AbstractResource[] oslcResourcesWithVersion = new AbstractResource[oslcResources.length];
		int i = 0;
		for (AbstractResource resource : oslcResources) {
			resource.setAbout(URI.create(resource.getAbout().toString() + "---revision" + revision));
			oslcResourcesWithVersion[i] = resource;
			i++;
		}
		return oslcResourcesWithVersion;
	}

	protected static AbstractResource getResourceWithVersion(AbstractResource oslcResource, String revision) {
		AbstractResource oslcResourceWithVersion = oslcResource;
		oslcResourceWithVersion.setAbout(URI.create(oslcResource.getAbout().toString() + "---revision" + revision));		
		return oslcResourceWithVersion;
	}
}