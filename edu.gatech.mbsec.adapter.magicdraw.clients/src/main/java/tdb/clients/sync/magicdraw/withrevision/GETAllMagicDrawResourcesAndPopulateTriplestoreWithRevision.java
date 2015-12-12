package tdb.clients.sync.magicdraw.withrevision;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;

import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLBlock;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLValueProperty;
import org.eclipse.lyo.adapter.subversion.SubversionFile;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.provider.jena.ErrorHandler;
import org.eclipse.lyo.oslc4j.provider.jena.JenaModelHelper;
import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
import org.eclipse.lyo.oslc4j.provider.jena.OslcRdfXmlCollectionProvider;
import org.eclipse.lyo.oslc4j.provider.jena.OslcRdfXmlProvider;
import org.glassfish.jersey.client.ClientConfig;

import util.TriplestoreUtil;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;

public class GETAllMagicDrawResourcesAndPopulateTriplestoreWithRevision {

	public static Map<String, String> filePathRevisionMap;
	
	
	public static void main(String[] args) {
		
		Thread thread = new Thread() {
			public void start() {
				// create TDB dataset
				String directory = TriplestoreUtil.getTriplestoreLocation();
				Dataset dataset = TDBFactory.createDataset(directory);

				
				String baseHTTPURI = "http://localhost:" + MagicDrawAdapterAndTDBSubversionSyncClientWithRevision.port + "/oslc4jmagicdraw";
				
				
				String oslcServiceProviderCatalogURI = baseHTTPURI + "/services/catalog/singleton";

				
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
				filePathRevisionMap = new HashMap<String, String>();
				for (ServiceProvider serviceProvider : serviceProviderCatalog.getServiceProviders()) {
					System.out.println("serviceProvider " + serviceProvider.getAbout());					
					
					
					
					// get model version
					String revision = "0";
					if(serviceProvider.getAbout().toString().split("---").length > 1){
						String projectName = serviceProvider.getAbout().toString().split("/")[serviceProvider.getAbout().toString().split("/").length - 1];
						String subversionFileURI = baseHTTPURI + "/services/subversionfiles/" + projectName + ".mdzip";
						Response subversionFileResponse = rdfclient.target(subversionFileURI).request("application/rdf+xml").get();
						SubversionFile subversionFileResource = subversionFileResponse.readEntity(SubversionFile.class);
						revision = subversionFileResource.getRevision();
						System.out.println("revision: " + revision);
					}
					
					
					
					
					if(serviceProvider.getAbout().toString().endsWith("subversionfiles")){
						for (Service service : serviceProvider.getServices()) {
							for (QueryCapability queryCapability : service.getQueryCapabilities()) {					
								System.out.println(queryCapability.getQueryBase());
								Response queryCapabilityResponse = rdfclient.target(queryCapability.getQueryBase()).request("application/rdf+xml").get();
								System.out.println(queryCapabilityResponse.getStatus());					
								
									SubversionFile[] oslcResources = queryCapabilityResponse.readEntity(SubversionFile[].class);
									for (SubversionFile subversionFile : oslcResources) {
										filePathRevisionMap.put(subversionFile.getPath(), subversionFile.getRevision());
									}														
							}
						}
					}
					
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
			System.out.println("All resources of OSLC adapter added to triplestore");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
}
