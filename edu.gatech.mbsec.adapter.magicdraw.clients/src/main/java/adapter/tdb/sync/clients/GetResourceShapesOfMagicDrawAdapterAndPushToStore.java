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
package adapter.tdb.sync.clients;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;


import edu.gatech.mbsec.adapter.subversion.SubversionFile;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.ResourceShape;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.provider.jena.JenaModelHelper;
import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
import org.glassfish.jersey.client.ClientConfig;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;

import tdb.clients.DeleteAllTriplesInTriplestore;

import util.TriplestoreUtil;

public class GetResourceShapesOfMagicDrawAdapterAndPushToStore {

	public static void main(String[] args) {
		// start with clean empty triplestore
//		DeleteAllTriplesInTriplestore.main(null);

		Thread thread = new Thread() {
			public void start() {
				// create TDB dataset
				String directory = TriplestoreUtil.getTriplestoreLocation();
				Dataset dataset = TDBFactory.createDataset(directory);

				String baseHTTPURI = "http://localhost:" + "8080" + "/oslc4jmagicdraw";

				String oslcServiceProviderCatalogURI = baseHTTPURI + "/services/catalog/singleton";

				ClientConfig clientConfig = new ClientConfig();
				for (Class providerClass : JenaProvidersRegistry.getProviders()) {
					clientConfig.register(providerClass);
				}
				Client rdfclient = ClientBuilder.newClient(clientConfig);

//				System.out.println(oslcServiceProviderCatalogURI);
				Response response = rdfclient.target(oslcServiceProviderCatalogURI).request("application/rdf+xml")
						.get();
//				System.out.println(response.getStatus());
				ServiceProviderCatalog serviceProviderCatalog = response.readEntity(ServiceProviderCatalog.class);

				// list to collect all resources
				ArrayList<AbstractResource> oslcResourcesArrayList = new ArrayList<AbstractResource>();
				
				for (ServiceProvider serviceProvider : serviceProviderCatalog.getServiceProviders()) {
//					System.out.println("serviceProvider " + serviceProvider.getAbout());

					for (Service service : serviceProvider.getServices()) {

						for (QueryCapability queryCapability : service.getQueryCapabilities()) {
													
//							System.out.println(queryCapability.getResourceShape());
							Response queryCapabilityResponse = rdfclient.target(queryCapability.getResourceShape())
									.request("application/rdf+xml").get();							
							ResourceShape resourceShape = queryCapabilityResponse.readEntity(ResourceShape.class);							
//							System.out.println(queryCapabilityResponse.getStatus());
							if(resourceShape != null){
								oslcResourcesArrayList.addAll(Arrays.asList(resourceShape));
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
		};
		thread.start();
		try {
			thread.join();
			System.out.println("All resource shapes of OSLC MagicDraw SysML adapter added to triplestore");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
