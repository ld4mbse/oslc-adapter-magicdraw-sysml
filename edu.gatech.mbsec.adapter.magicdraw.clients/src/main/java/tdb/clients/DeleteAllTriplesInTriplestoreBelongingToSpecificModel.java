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
package tdb.clients;

import java.io.InputStream;

import util.TriplestoreUtil;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.GraphStore;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.util.FileManager;

public class DeleteAllTriplesInTriplestoreBelongingToSpecificModel {

	public static void main(String[] args) {
		Thread thread = new Thread() {
			public void start() {
				// load model from triplestore
				String directory = TriplestoreUtil.getTriplestoreLocation();
				Dataset dataset = TDBFactory.createDataset(directory);
				
				String fileName = "model11";

				// delete all triples where the resource, or predicate, or object is owned by a specific model
//				String queryString = 
//					"" +
//					"" +
//					"DELETE{?s  ?p ?o .}" +
//					"WHERE {" +
//					"    ?s  ?p ?o . "
//					+ "FILTER ( regex(str(?s), \"/services/model11/\") || regex(str(?p), \"/services/model11/\") || regex(str(?o), \"/services/model11/\")) "
//					+ "}";
				
				String queryString = 
						"" +
						"" +
						"DELETE{?s  ?p ?o .}" +
						"WHERE {" +
						"    ?s  ?p ?o . "
						+ "FILTER ( regex(str(?s), \"/services/" + fileName + "/\") || regex(str(?p), \"/services/" + fileName + "/\") || regex(str(?o), \"/services/" + fileName + "/\")) "
						+ "}";
				
				UpdateRequest query = UpdateFactory.create(queryString);

				// Execute the query and obtain results
				UpdateProcessor qe = UpdateExecutionFactory.create(query, (GraphStore) dataset.asDatasetGraph());
				qe.execute();
				
				
				dataset.close();
			}
		};
		thread.start();
		try {
			thread.join();
			System.out.println("All triples deleted in triplestore");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
