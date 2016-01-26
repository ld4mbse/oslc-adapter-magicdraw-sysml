/*******************************************************************************
 * Copyright (c) 2012, 2014 IBM Corporation.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *  
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *
 *     Michael Fiedler     - initial API and implementation for Bugzilla adapter
 *     
 * Modifications performed by:    
 *     Axel Reichwein		- implementation for MagicDraw adapter
 *     (axel.reichwein@koneksys.com)
 *     Sebastian Herzig (sebastian.herzig@me.gatech.edu) - support for publishing OSLC resource shapes     
 *******************************************************************************/
package edu.gatech.mbsec.adapter.magicdraw.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.lyo.adapter.magicdraw.resources.Constants;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLAssociationBlock;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLBlock;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLBlockDiagram;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLConnector;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLConnectorEnd;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLFlowProperty;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLFullPort;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLInterfaceBlock;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLInternalBlockDiagram;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLItemFlow;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLModel;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLPackage;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLPartProperty;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLPort;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLProxyPort;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLReferenceProperty;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLRequirement;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLValueProperty;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLValueType;
import edu.gatech.mbsec.adapter.subversion.SubversionFile;
import edu.gatech.mbsec.adapter.subversion.SubversionFileService;
import edu.gatech.mbsec.adapter.subversion.SubversionManager;
import org.eclipse.lyo.oslc4j.application.OslcResourceShapeResource;
import org.eclipse.lyo.oslc4j.application.OslcWinkApplication;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.model.AllowedValues;
import org.eclipse.lyo.oslc4j.core.model.Compact;
import org.eclipse.lyo.oslc4j.core.model.CreationFactory;
import org.eclipse.lyo.oslc4j.core.model.Dialog;
import org.eclipse.lyo.oslc4j.core.model.Error;
import org.eclipse.lyo.oslc4j.core.model.ExtendedError;
import org.eclipse.lyo.oslc4j.core.model.Link;
import org.eclipse.lyo.oslc4j.core.model.OAuthConfiguration;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.PrefixDefinition;
import org.eclipse.lyo.oslc4j.core.model.Preview;
import org.eclipse.lyo.oslc4j.core.model.Property;
import org.eclipse.lyo.oslc4j.core.model.Publisher;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.ResourceShape;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
import org.eclipse.lyo.oslc4j.provider.json4j.Json4JProvidersRegistry;

import com.opencsv.CSVReader;

import clients.SubversionClient;
import clients.SubversionFileClient;
import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;
import util.FileMetadata;

//import com.nomagic.magicdraw.commandline.CommandLine;

/**
 * OSLC4JMagicDrawApplication registers all entity providers for converting
 * POJOs into RDF/XML, JSON and other formats. OSLC4JMagicDrawApplication
 * registers also registers each servlet class containing the implementation of
 * OSLC RESTful web services.
 * 
 * OSLC4JMagicDrawApplication also reads the user-defined configuration file
 * with loadPropertiesFile(). This is done at the initialization of the web
 * application, for example when the first resource or service of the OSLC
 * MagicDraw adapter is requested.
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 * @author Sebastian Herzig (sebastian.herzig@me.gatech.edu)
 */
public class OSLC4JMagicDrawApplication extends OslcWinkApplication {

	private static final Set<Class<?>> RESOURCE_CLASSES = new HashSet<Class<?>>();
	public static final Map<String, Class<?>> RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP = new HashMap<String, Class<?>>();

	public static String sysmlEcoreLocation = null;
	public static String magicdrawModelsDirectory = null;
	public static String portNumber = null;
	public static boolean syncWithSvnRepo = false;
	public static String svnurl = null;
	
	public static boolean useIndividualSubversionFiles = false;

	public static String warConfigFilePath = "../oslc4jmagicdraw configuration/config.properties";
	public static String localConfigFilePath = "oslc4jmagicdraw configuration/config.properties";
	public static String configFilePath = null;
	public static int delayInSecondsBetweenDataRefresh = 100000;
	public static SubversionManager subversionManager = null;
	public static SubversionFileClient subversionFileClient;
	public static String svnUserName;
	public static String svnPassword;
	
	public static String warSVNURLsFilePath = "../oslc4jmagicdraw configuration/subversionfiles.csv";
	public static String localSVNURLsFilePath = "oslc4jmagicdraw configuration/subversionfiles.csv";
	public static String svnURLsFilePath = null;
	public static ArrayList<String> subversionFileURLs;

	// public static String configFilePath = "configuration/config.properties";

	static {
		RESOURCE_CLASSES.addAll(JenaProvidersRegistry.getProviders());
		RESOURCE_CLASSES.addAll(Json4JProvidersRegistry.getProviders());

		// RESOURCE_CLASSES.add(CommandLine.class);
		RESOURCE_CLASSES.add(MagicDrawManager.class);

		RESOURCE_CLASSES.add(Link.class);

		RESOURCE_CLASSES.add(ServiceProviderCatalogService.class);
		RESOURCE_CLASSES.add(ServiceProviderService.class);

		RESOURCE_CLASSES.add(SysMLRequirementService.class);
		RESOURCE_CLASSES.add(SysMLBlockService.class);
		RESOURCE_CLASSES.add(SysMLPartPropertyService.class);
		RESOURCE_CLASSES.add(SysMLReferencePropertyService.class);
		RESOURCE_CLASSES.add(SysMLModelService.class);
		RESOURCE_CLASSES.add(SysMLPackageService.class);
		RESOURCE_CLASSES.add(SysMLAssociationBlockService.class);
		RESOURCE_CLASSES.add(SysMLConnectorService.class);
		RESOURCE_CLASSES.add(SysMLConnectorEndService.class);
		RESOURCE_CLASSES.add(SysMLPortService.class);
		RESOURCE_CLASSES.add(SysMLProxyPortService.class);
		RESOURCE_CLASSES.add(SysMLFullPortService.class);
		RESOURCE_CLASSES.add(SysMLInterfaceBlockService.class);
		RESOURCE_CLASSES.add(SysMLFlowPropertyService.class);
		RESOURCE_CLASSES.add(SysMLItemFlowService.class);
		RESOURCE_CLASSES.add(SysMLValuePropertyService.class);
		RESOURCE_CLASSES.add(SysMLValueTypeService.class);
		RESOURCE_CLASSES.add(SysMLBlockDiagramService.class);
		RESOURCE_CLASSES.add(SysMLInternalBlockDiagramService.class);

		RESOURCE_CLASSES.add(SubversionFile.class);
		RESOURCE_CLASSES.add(SubversionFileService.class);
		RESOURCE_CLASSES.add(SysMLSVNFileURLService.class);

		RESOURCE_CLASSES.add(OSLC4MBSESpecificationService.class);
		
		RESOURCE_CLASSES.add(OslcResourceShapeResource.class);
		RESOURCE_CLASSES.add(ResourceShapeService.class);
		
		RESOURCE_CLASSES.add(RDFVocabularyService.class);

		
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_ASSOCIATIONBLOCK, SysMLAssociationBlock.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_BLOCKDIAGRAM, SysMLBlockDiagram.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_BLOCK, SysMLBlock.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_CONNECTOREND, SysMLConnectorEnd.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_CONNECTOR, SysMLConnector.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_FLOWPROPERTY, SysMLFlowProperty.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_FULLPORT, SysMLFullPort.class);		
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_INTERFACEBLOCK, SysMLInterfaceBlock.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_INTERNALBLOCKDIAGRAM, SysMLInternalBlockDiagram.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_ITEMFLOW, SysMLItemFlow.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_MODEL, SysMLModel.class);		
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_PACKAGE, SysMLPackage.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_PARTPROPERTY, SysMLPartProperty.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_PORT, SysMLPort.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_PROXYPORT, SysMLProxyPort.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_REFERENCEPROPERTY, SysMLReferenceProperty.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_REQUIREMENT, SysMLRequirement.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_VALUEPROPERTY, SysMLValueProperty.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SYSML_VALUETYPE, SysMLValueType.class);
			
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_ALLOWED_VALUES, AllowedValues.class);
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_COMPACT, Compact.class);
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_CREATION_FACTORY, CreationFactory.class);
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_DIALOG, Dialog.class);
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_ERROR, Error.class);
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_EXTENDED_ERROR, ExtendedError.class);
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_OAUTH_CONFIGURATION, OAuthConfiguration.class);
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_PREFIX_DEFINITION, PrefixDefinition.class);
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_PREVIEW, Preview.class);
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_PROPERTY, Property.class);
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_PUBLISHER, Publisher.class);
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_QUERY_CAPABILITY, QueryCapability.class);
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_RESOURCE_SHAPE, ResourceShape.class);
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_SERVICE, Service.class);
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_SERVICE_PROVIDER, ServiceProvider.class);
//		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_SERVICE_PROVIDER_CATALOG,
//				ServiceProviderCatalog.class);

		loadPropertiesFile();

		readDataFirstTime();

		readDataPeriodically();

	}

	public OSLC4JMagicDrawApplication() throws OslcCoreApplicationException, URISyntaxException {
		super(RESOURCE_CLASSES, OslcConstants.PATH_RESOURCE_SHAPES, RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP);
	}

	// private static void loadPropertiesFile() {
	// Properties prop = new Properties();
	// InputStream input = null;
	//
	// try {
	// // loading properties file
	//// input = new FileInputStream("./configuration/config.properties");
	// input = new FileInputStream(configFilePath); //config for war
	//
	// // load property file content and convert backslashes into forward
	// slashes
	//// String str = readFile("./configuration/config.properties",
	// Charset.defaultCharset());
	// String str = readFile(configFilePath, Charset.defaultCharset()); //config
	// for war
	// prop.load(new StringReader(str.replace("\\","/")));
	//
	// // get the property value
	// String sysmlEcoreLocationFromUser =
	// prop.getProperty("sysmlEcoreLocation");
	// String magicdrawModelsDirectoryFromUser =
	// prop.getProperty("magicdrawModelsDirectory");
	//
	// // add trailing slash if missing
	// if(!magicdrawModelsDirectoryFromUser.endsWith("/")){
	// magicdrawModelsDirectoryFromUser = magicdrawModelsDirectoryFromUser +
	// "/";
	// }
	// magicdrawModelsDirectory = magicdrawModelsDirectoryFromUser;
	// sysmlEcoreLocation = sysmlEcoreLocationFromUser;
	// portNumber = prop.getProperty("portNumber");
	//
	// } catch (IOException ex) {
	// ex.printStackTrace();
	// } finally {
	// if (input != null) {
	// try {
	// input.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// }

	private static void loadPropertiesFile() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			// loading properties file
			// input = new FileInputStream("./configuration/config.properties");
			input = new FileInputStream(warConfigFilePath); // for war file
			configFilePath = warConfigFilePath;
		} catch (FileNotFoundException e) {
			try {
				input = new FileInputStream(localConfigFilePath);
				configFilePath = localConfigFilePath;
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // for war file
		}

		// load property file content and convert backslashes into forward
		// slashes
		String str;
		if (input != null) {
			try {
				str = readFile(configFilePath, Charset.defaultCharset());
				prop.load(new StringReader(str.replace("\\", "/")));

				// get the property value
				String magicdrawEcoreLocationFromUser = prop.getProperty("sysmlEcoreLocation");
				String magicdrawModelsDirectoryFromUser = prop.getProperty("magicdrawModelsDirectory");
				String syncWithSvnRepoFromUser = prop.getProperty("syncWithSvnRepo");
				String delayInSecondsBetweenDataRefreshFromUser = prop.getProperty("delayInSecondsBetweenDataRefresh");
				String useIndividualSubversionFilesFromUser = prop.getProperty("useIndividualSubversionFiles");

				// add trailing slash if missing
				if (!magicdrawModelsDirectoryFromUser.endsWith("/")) {
					magicdrawModelsDirectoryFromUser = magicdrawModelsDirectoryFromUser + "/";
				}
				magicdrawModelsDirectory = magicdrawModelsDirectoryFromUser;
				sysmlEcoreLocation = magicdrawEcoreLocationFromUser;
				portNumber = prop.getProperty("portNumber");
				svnUserName = prop.getProperty("svnUserName");
				svnPassword = prop.getProperty("svnPassword");
				try {
					if (Boolean.parseBoolean(syncWithSvnRepoFromUser)) {
						syncWithSvnRepo = true;
					}
				} catch (Exception e) {

				}
				
				
				try {
					if (Boolean.parseBoolean(useIndividualSubversionFilesFromUser)) {
						useIndividualSubversionFiles = true;
						// delete all existing models in directory as they will be populated with new Subversion files
						Path directory = Paths.get(OSLC4JMagicDrawApplication.magicdrawModelsDirectory);
						   Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
							   @Override
							   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
								   Files.delete(file);
								   return FileVisitResult.CONTINUE;
							   }

							   @Override
							   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
								   Files.delete(dir);
								   return FileVisitResult.CONTINUE;
							   }

						   });
					}
				} catch (Exception e) {

				}
				
				
				
				
				svnurl = prop.getProperty("svnurl");
				try {
					delayInSecondsBetweenDataRefresh = Integer.parseInt(delayInSecondsBetweenDataRefreshFromUser);
				} catch (Exception e) {

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}

	public static void checkoutOrUpdateSVNWorkingCopy() {
		Thread thread = new Thread() {
			public void start() {
				ArrayList<FileMetadata> fileMetaDatas = SubversionClient.syncWorkingCopy(svnurl,
						magicdrawModelsDirectory);
				// convert fileMetaDatas into OSLC POJOs
				subversionManager.convertFileMetaDataIntoRDFSubversionFileResources(fileMetaDatas);
			}
		};
		thread.start();
		try {
			thread.join();
			System.out.println(
					"Connection with Subversion repository and creation of OSLC Subversion file resources finished.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public static void readDataFirstTime() {
		Thread thread = new Thread() {
			public void start() {
				subversionManager = new SubversionManager(MagicDrawManager.baseHTTPURI);
				reloadModels();
			}

		};
		thread.start();
		try {
			thread.join();
			System.out.println("MagicDraw files read. Initialization of OSLC MagicDraw adapter finished.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void readDataPeriodically() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				reloadModels();								
			}
		}, delayInSecondsBetweenDataRefresh * 1000, delayInSecondsBetweenDataRefresh * 1000);
	}

	protected static void reloadModels() {
		if (useIndividualSubversionFiles) {					
			checkoutOrUpdateSVNFiles();
		} else if (syncWithSvnRepo) {
			checkoutOrUpdateSVNWorkingCopy();
		}			
		MagicDrawManager.areSysMLProjectsLoaded = false; // to reload MagicDraw	models
		MagicDrawManager.loadSysMLProjects();	
		
	}
	
	
	
	
	

	private static void loadSVNURLsFile() {
		Properties prop = new Properties();
		InputStream input = null;
	
		try {
			// loading properties file
			// input = new FileInputStream("./configuration/config.properties");
			input = new FileInputStream(warSVNURLsFilePath); // for war file
			svnURLsFilePath = warSVNURLsFilePath;
		} catch (FileNotFoundException e) {
			try {
				input = new FileInputStream(localSVNURLsFilePath);
				svnURLsFilePath = localSVNURLsFilePath;
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // for war file
		}
	
		// load property file content and convert backslashes into forward
		// slashes
		String str;
		if (input != null) {
			try {				
				CSVReader reader2 = new CSVReader(new FileReader(svnURLsFilePath));
				List<String[]> allElements = reader2.readAll();								
				subversionFileURLs = readSVNFileURLs(allElements);									
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
	
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	
			}
		}
	
	}

	public static ArrayList<String> readSVNFileURLs(List<String[]> allElements) {
			List<String> svnurls = new ArrayList<String>();		
			
			for (String[] element : allElements) {
				if ((element.length == 1)) {
					svnurls.add(element[0]);
				}
			}
			
			ArrayList<String> subversionFileURLs = new ArrayList<String>();
	//		for (String subversionFileURL : SubversionFileURLsFromUserArray) {
			for (String subversionFileURL : svnurls) {
				// make sure to delete possible space character
				if (subversionFileURL.startsWith(" ")) {
					subversionFileURL = subversionFileURL.substring(1, subversionFileURL.length());
				}
				if (subversionFileURL.endsWith(" ")) {
					subversionFileURL = subversionFileURL.substring(0, subversionFileURL.length()-1);
				}
				
				try {
					// make sure that URL is valid
					new URL(subversionFileURL);
						
					// make sure that url is not a duplicate							
						if(!subversionFileURLs.contains(subversionFileURL)){
							subversionFileURLs.add(subversionFileURL);
						}
						
					
				} catch (Exception e) {
	
				}
			}
			return subversionFileURLs;
		}

	public static void checkoutOrUpdateSVNFiles() {
		Thread thread = new Thread() {
			public void start() {
				// read all subversion file urls
				loadSVNURLsFile();
	
				// initialize global list for FileMetaDatas
				ArrayList<FileMetadata> fileMetaDatas = new ArrayList<FileMetadata>();
				
				// for each subversion file url, perform checkout or update
				for (String subversionFileURLString : subversionFileURLs) {
					// create local directory to save subversionFile
					// files belonging to the same subversion repo directory will share the same local directory
					// name directory based on subversionFileURL
					// get repository directory
					String[] repParts = subversionFileURLString.split("/");
					String subversionFileName = repParts[repParts.length - 1];
					
					String subversionFileDirURL = subversionFileURLString.replace(subversionFileName, "");															
					
					String localSubversionFileDir = subversionFileDirURL.replace(":", "");
					localSubversionFileDir = localSubversionFileDir.replace("/", "");
					
					// create a new directory for the working copy of that file
					if(!new File(magicdrawModelsDirectory + localSubversionFileDir).exists()){
						new File(magicdrawModelsDirectory + localSubversionFileDir).mkdirs();
					}
					
					// perform checkout or update on Subversion file					
					if(subversionFileClient == null){
						subversionFileClient = new SubversionFileClient();
					}
					FileMetadata specificFileMetaData = subversionFileClient.syncFile(subversionFileURLString,
							magicdrawModelsDirectory + localSubversionFileDir, svnUserName, svnPassword);
					
					// save Subversion file metadata
					if(specificFileMetaData != null){
						fileMetaDatas.add(specificFileMetaData);
					}
					
				}
				
				
				// convert fileMetaDatas into OSLC POJOs
				subversionManager.convertFileMetaDataIntoRDFSubversionFileResources(fileMetaDatas);
			}
		};
		thread.start();
		try {
			thread.join();
			System.out.println(
					"Connection with Subversion repository and creation of OSLC Subversion file resources finished.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
