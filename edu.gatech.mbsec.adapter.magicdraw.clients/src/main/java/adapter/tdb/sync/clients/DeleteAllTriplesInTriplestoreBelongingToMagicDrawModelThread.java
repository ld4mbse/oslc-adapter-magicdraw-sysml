package adapter.tdb.sync.clients;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

import util.TriplestoreUtil;

public class DeleteAllTriplesInTriplestoreBelongingToMagicDrawModelThread extends Thread{

	String fileName;
	
	public DeleteAllTriplesInTriplestoreBelongingToMagicDrawModelThread(String fileName){
		this.fileName = fileName;
	}
	
	public static void main(String[] args){
		String fileName = "SUV_Example";
		Thread thread = new DeleteAllTriplesInTriplestoreBelongingToMagicDrawModelThread(fileName);
		thread.start();
		try {
			thread.join();
			System.out.println("All triples of file " + fileName + " deleted in triplestore");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void start() {
		// load model from triplestore
		String directory = TriplestoreUtil.getTriplestoreLocation();
		Dataset dataset = TDBFactory.createDataset(directory);

		// delete all triples where the resource, or predicate, or object is owned by a specific model
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

}