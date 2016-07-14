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


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;


import edu.gatech.mbsec.adapter.subversion.SubversionFile;
import org.eclipse.lyo.oslc4j.core.annotation.OslcPropertyDefinition;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.Link;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.provider.jena.ErrorHandler;
import org.eclipse.lyo.oslc4j.provider.jena.JenaModelHelper;
import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
import org.glassfish.jersey.client.ClientConfig;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

import adapter.tdb.sync.clients.DeleteAllTriplesInTriplestoreBelongingToMagicDrawModelThread;
import tdb.clients.DeleteAllTriplesInTriplestore;


import util.TriplestoreUtil;

public class MagicDrawAdapterAndTDBSubversionSyncClientWithRevision {
	
	static int delayInSecondsBetweenChangeChecks = 30;
	static String port = "8080";
	public static String oslcServiceProviderCatalogURI = "http://localhost:" + port + "/oslc4jmagicdraw" + "/services/catalog/singleton";
	static String triplestoreDatasetName = "mydataset";				
	static boolean isFirstComparison = true;
	static Map<String, String> newFilePathRevisionMap;			
	
	

	public static void main(String[] args) {
		
		// start with clean empty triplestore
		DeleteAllTriplesInTriplestore.main(null);
		
		// populate triplestore based on latest version of resources of adapter
		GETAllMagicDrawResourcesAndPopulateTriplestoreWithRevision.main(null);
		
		// periodically check subversion files to see if they have changed
		periodicallyCheckSubversionFiles();
		
	}

	private static void periodicallyCheckSubversionFiles() {				
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				checkSubversionFiles();				
			}
		}, 0, delayInSecondsBetweenChangeChecks * 1000);

	}

	public static void checkSubversionFiles() {
				
		// every time period, check if committeddate of subversion files has changed
		Map<String, String> oldFilePathRevisionMap;
		if(isFirstComparison){
			oldFilePathRevisionMap = GETAllMagicDrawResourcesAndPopulateTriplestoreWithRevision.filePathRevisionMap;
			isFirstComparison = false;
		}
		else{
			oldFilePathRevisionMap = newFilePathRevisionMap;
		}
		 
		
				
		// check new subversion file resources and get new map 
		newFilePathRevisionMap = getFilePathRevisionMap();

		//compare old and new maps
		for (String newFilePath : newFilePathRevisionMap.keySet()) {
			String oldRevision = oldFilePathRevisionMap.get(newFilePath);
			String newRevision = newFilePathRevisionMap.get(newFilePath);
			if(oldRevision == null){
				// new File -> add all resources from that file in triplestore
				String fileName = newFilePath.replace(".mdzip", "");
				addFileResourcesToTriplestore(fileName, newRevision);
			}
			else if(oldRevision.equals(newRevision)){
				// no change
			}
			else{
				// there was a change to the file  
				// -> delete all resources from that file
				String fileName = newFilePath.replace(".mdzip", "");
//				deleteFileResourcesInTriplestore(fileName);
				// -> add all resources from that file in triplestore
				addFileResourcesToTriplestore(fileName, newRevision);
			}
		}
		
//		for (String oldFilePath : oldFilePathRevisionMap.keySet()) {
//			String newCommittedDate = newFilePathRevisionMap.get(oldFilePath);
//			if(newCommittedDate == null){
//				// old file was deleted -> delete all resources from that file
//				String fileName = oldFilePath.replace(".mdzip", "");
//				deleteFileResourcesInTriplestore(fileName);
//			}
//			
//		}
		
		System.out.println("Data synced between OSLC MagicDraw adapter " +  " and triplestore based on Subversion files at " + new Date().toString());
		
		
		
		

		

	}

	

	private static void addFileResourcesToTriplestore(String fileName, String revision) {
		Thread thread = new AddAllTriplesToTriplestoreWithRevisionBelongingToMagicDrawModelThread(fileName, revision);		
		thread.start();
		try {
			thread.join();
			System.out.println("All triples of file " + fileName +  " added to triplestore");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static Map<String, String> getFilePathRevisionMap() {
		

		// get requirements as POJOs
		ClientConfig clientConfig = new ClientConfig();
		for (Class providerClass : JenaProvidersRegistry.getProviders()) {
			clientConfig.register(providerClass);
		}
		Client rdfclient = ClientBuilder.newClient(clientConfig);
		System.out.println(oslcServiceProviderCatalogURI);
		Response response = rdfclient.target(oslcServiceProviderCatalogURI).request("application/rdf+xml").get();
		System.out.println(response.getStatus());
		ServiceProviderCatalog serviceProviderCatalog = response.readEntity(ServiceProviderCatalog.class);			
		
		// list to collect all resources
		ArrayList<AbstractResource> oslcResourcesArrayList = new ArrayList<AbstractResource>();
		
		// map to track changes to files
		Map<String, String> newFilePathRevisionMap = new HashMap<String, String>();
		for (ServiceProvider serviceProvider : serviceProviderCatalog.getServiceProviders()) {
			System.out.println("serviceProvider " + serviceProvider.getAbout());
			if(serviceProvider.getAbout().toString().endsWith("subversionfiles")){
				for (Service service : serviceProvider.getServices()) {
					for (QueryCapability queryCapability : service.getQueryCapabilities()) {					
						System.out.println(queryCapability.getQueryBase());
						Response queryCapabilityResponse = rdfclient.target(queryCapability.getQueryBase()).request("application/rdf+xml").get();
						System.out.println(queryCapabilityResponse.getStatus());					
						
							SubversionFile[] oslcResources = queryCapabilityResponse.readEntity(SubversionFile[].class);
							for (SubversionFile subversionFile : oslcResources) {
								newFilePathRevisionMap.put(subversionFile.getPath(), subversionFile.getRevision());
							}						
						
					}
				}
			}
			
		}
		return newFilePathRevisionMap;
	}

	

	

	
	

	

}
