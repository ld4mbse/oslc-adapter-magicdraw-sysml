package tdb.clients;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import util.TriplestoreUtil;

import com.hp.hpl.jena.assembler.assemblers.FileModelAssembler;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;

public class CreateTriplestore {

	public static void main(String[] args) {
		Thread thread = new Thread() {
			public void start() {
				// create TDB dataset
//				String directory = TriplestoreUtil.getTriplestoreLocation();
				String directory = "C:\\Users\\Axel\\git\\edu.gatech.mbsec.adapter.tdb\\exampletriplestore";
				Dataset dataset = TDBFactory.createDataset(directory);		
				dataset.close();
			}
		};
		thread.start();
		try {
			thread.join();
			System.out.println("Triplestore created");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
