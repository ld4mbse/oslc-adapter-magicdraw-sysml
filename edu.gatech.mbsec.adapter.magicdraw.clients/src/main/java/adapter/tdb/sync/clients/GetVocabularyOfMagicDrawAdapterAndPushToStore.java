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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.provider.jena.JenaModelHelper;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;

import util.TriplestoreUtil;

public class GetVocabularyOfMagicDrawAdapterAndPushToStore {

	public static void main(String[] args) {
		
		Thread thread = new Thread() {
			public void start() {
				
				URI vocabURI = URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary"); 
				
				
				
				
				// create an empty RDF model
				Model model = ModelFactory.createDefaultModel();

				// use FileManager to read OSLC Resource Shape in RDF
//				String inputFileName = "file:C:/Users/Axel/git/EcoreMetamodel2OSLCSpecification/EcoreMetamodel2OSLCSpecification/Resource Shapes/Block.rdf";
//				String inputFileName = "file:C:/Users/Axel/git/ecore2oslc/EcoreMetamodel2OSLCSpecification/RDF Vocabulary/sysmlRDFVocabulary of OMG.rdf";
//				InputStream in = FileManager.get().open(inputFileName);
//				if (in == null) {
//					throw new IllegalArgumentException("File: " + inputFileName
//							+ " not found");
//				}

				
				
				// create TDB dataset
				String directory = TriplestoreUtil.getTriplestoreLocation();
				Dataset dataset = TDBFactory.createDataset(directory);
				Model tdbModel = dataset.getDefaultModel();
				try {
					
					HttpGet httpget = new HttpGet(vocabURI);
					httpget.addHeader("accept", "application/rdf+xml");
					CloseableHttpClient httpClient = HttpClients.createDefault();
		            HttpResponse response = httpClient.execute(httpget);
		            HttpEntity entity = response.getEntity();
		            if (entity != null) {		                
		                InputStream inputStream = entity.getContent();
		                model.read(inputStream, null);
		                tdbModel.add(model);
		            }

				}  catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
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
			System.out.println("Vocabulary of OSLC MagicDraw SysML adapter added to triplestore");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
				

	}

}
