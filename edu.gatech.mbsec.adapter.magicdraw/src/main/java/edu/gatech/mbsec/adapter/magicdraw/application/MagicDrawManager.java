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
 *	   Sebastian Herzig (sebastian.herzig@me.gatech.edu) - support for loading multiple MagicDraw models at the same time      
 *******************************************************************************************/

package edu.gatech.mbsec.adapter.magicdraw.application;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLAssociationBlock;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLBlock;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLBlockDiagram;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLConnector;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLConnectorEnd;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLFlowDirection;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLFlowProperty;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLFullPort;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLInterfaceBlock;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLInternalBlockDiagram;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLItemFlow;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLModel;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPackage;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPartProperty;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLPort;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLProxyPort;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLReferenceProperty;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLRequirement;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLValueProperty;
import edu.gatech.mbsec.adapter.magicdraw.resources.SysMLValueType;
import org.eclipse.lyo.oslc4j.core.model.Link;

import com.nomagic.magicdraw.commandline.CommandLine;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.core.project.ProjectDescriptor;
import com.nomagic.magicdraw.core.project.ProjectDescriptorsFactory;
import com.nomagic.magicdraw.core.project.ProjectsManager;
import com.nomagic.magicdraw.export.image.ImageExporter;
import com.nomagic.magicdraw.openapi.uml.ModelElementsManager;
import com.nomagic.magicdraw.openapi.uml.ReadOnlyElementException;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.magicdraw.sysml.util.SysMLConstants;
import com.nomagic.magicdraw.uml.BaseElement;
import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.nomagic.runtime.ApplicationExitedException;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdmodels.Model;
import com.nomagic.uml2.ext.magicdraw.classes.mdassociationclasses.AssociationClass;
import com.nomagic.uml2.ext.magicdraw.classes.mddependencies.Abstraction;
import com.nomagic.uml2.ext.magicdraw.classes.mddependencies.Dependency;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.AggregationKindEnum;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Association;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Classifier;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.DataType;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.DirectedRelationship;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Generalization;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.InstanceSpecification;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Namespace;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.PackageableElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ParameterDirectionKindEnum;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Type;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.ConnectableElement;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.ConnectorEnd;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;
import com.nomagic.uml2.impl.ElementsFactory;

import edu.gatech.mbsec.adapter.magicdraw.services.OSLC4JMagicDrawApplication;
import edu.gatech.mbsec.adapter.magicdraw.stereotypes.MDModelLibException;
import edu.gatech.mbsec.adapter.magicdraw.stereotypes.MDSysMLModelHandler;

/**
 * MagicDrawManager is responsible for all the communication between the
 * MagicDraw application and the OSLC MagicDraw adapter. It is used to load
 * MagicDraw SysML projects, retrieve SysML elements from projects, and map
 * SysML elements to OSLC resources described as POJOs
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 * @author Sebastian Herzig (sebastian.herzig@me.gatech.edu)
 */
public class MagicDrawManager {

	public static String magicdrawModelsDirectory = OSLC4JMagicDrawApplication.magicdrawModelsDirectory;
	public static boolean areSysMLProjectsLoaded;

	static int sessionID = 1;
	static Collection<String> predefinedMagicDrawSysMLPackageNames = new HashSet<String>();

	public static Collection<Class> mdSysmlRequirements = new ArrayList<Class>();
	public static Map<String, Collection<Class>> projectIdMDSysmlRequirementsMap = new HashMap<String, Collection<Class>>();

	public static Collection<Class> mdSysmlBlocks = new ArrayList<Class>(); // list
																			// of
																			// MagicDraw
																			// SysML
																			// blocks
																			// for
																			// a
																			// specific
																			// project
	// list gets overwritten for each new project - this is ok when loading each
	// individual model separately - each creation web service in the past would
	// first reload the model and the list would be ok
	// now, models are loaded at launch of adapter, and at refresh times. each
	// creation web service no longer reload the model
	// when trying to retrieve MagicDraw SysML blocks of a specific project,
	// this list only provides the blocks of the last loaded model
	// what is needed is a list of mdSysmlBlocks for each MagicDraw model,
	// populated when loading a specific model, and used in the creation web
	// services
	public static Map<String, Collection<Class>> projectIdMDSysmlBlocksMap = new HashMap<String, Collection<Class>>();

	static Collection<Class> mdSysmlInterfaceBlocks = new ArrayList<Class>();
	public static Map<String, Collection<Class>> projectIdMDSysmlInterfaceBlocksMap = new HashMap<String, Collection<Class>>();

	static Collection<InformationFlow> mdSysmlItemFlows = new ArrayList<InformationFlow>();
	public static Map<String, Collection<InformationFlow>> projectIdMDSysmlItemFlowsMap = new HashMap<String, Collection<InformationFlow>>();

	public static Collection<DataType> mdSysmlValueTypes = new ArrayList<DataType>();
	public static Map<String, Collection<DataType>> projectIdMDSysmlValueTypesMap = new HashMap<String, Collection<DataType>>();

	public static Collection<Property> mdSysmlPartProperties = new ArrayList<Property>();
	public static Map<String, Collection<Property>> projectIdMDSysmlPartPropertiesMap = new HashMap<String, Collection<Property>>();

	public static Collection<Connector> mdSysmlConnectors = new ArrayList<Connector>();
	public static Map<String, Collection<Connector>> projectIdMDSysmlConnectorsMap = new HashMap<String, Collection<Connector>>();

	public static Collection<Port> mdSysmlPorts = new ArrayList<Port>();
	public static Map<String, Collection<Port>> projectIdMDSysmlPortsMap = new HashMap<String, Collection<Port>>();

	public static Collection<Property> mdSysmlValueProperties = new ArrayList<Property>();
	public static Map<String, Collection<Property>> projectIdMDSysmlValuePropertiesMap = new HashMap<String, Collection<Property>>();

	public static Collection<Property> mdSysmlFlowProperties = new ArrayList<Property>();
	public static Map<String, Collection<Property>> projectIdMDSysmlFlowPropertiesMap = new HashMap<String, Collection<Property>>();

	public static Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package> mdSysmlPackages = new ArrayList<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package>();
	public static Map<String, Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package>> projectIdMDSysmlPackagesMap = new HashMap<String, Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package>>();

	static Collection<DiagramPresentationElement> mdSysmlBlockDiagrams = new ArrayList<DiagramPresentationElement>();
	static Collection<DiagramPresentationElement> mdSysmlInternalBlockDiagrams = new ArrayList<DiagramPresentationElement>();
	static Collection<com.nomagic.uml2.ext.magicdraw.classes.mdassociationclasses.AssociationClass> mdSysmlAssociationBlocks = new ArrayList<com.nomagic.uml2.ext.magicdraw.classes.mdassociationclasses.AssociationClass>();

	static Collection<SysMLRequirement> oslcSysmlRequirements = new ArrayList<SysMLRequirement>();
	static Collection<SysMLBlock> oslcSysmlBlocks = new ArrayList<SysMLBlock>();

	static Map<String, Class> idMdSysmlRequirementMap = new HashMap<String, Class>();
	static Map<String, SysMLRequirement> idOslcSysmlRequirementMap = new HashMap<String, SysMLRequirement>();

	// static Map<String, Class> qNameMdSysmlBlockMap = new HashMap<String,
	// Class>();
	static Map<String, SysMLBlock> qNameOslcSysmlBlockMap = new HashMap<String, SysMLBlock>();

	static Map<String, SysMLPartProperty> qNameOslcSysmlPartPropertyMap = new HashMap<String, SysMLPartProperty>();
	static Map<String, SysMLReferenceProperty> qNameOslcSysmlReferencePropertyMap = new HashMap<String, SysMLReferenceProperty>();

	static Map<String, SysMLModel> oslcSysmlModelMap = new HashMap<String, SysMLModel>();
	static Map<String, SysMLPackage> qNameOslcSysmlPackageMap = new HashMap<String, SysMLPackage>();

	static Map<String, SysMLAssociationBlock> qNameOslcSysmlAssociationBlockMap = new HashMap<String, SysMLAssociationBlock>();
	static Map<String, SysMLConnector> qNameOslcSysmlConnectorMap = new HashMap<String, SysMLConnector>();
	static Map<String, SysMLConnectorEnd> qNameOslcSysmlConnectorEndMap = new HashMap<String, SysMLConnectorEnd>();
	static Map<String, SysMLPort> qNameOslcSysmlPortMap = new HashMap<String, SysMLPort>();
	static Map<String, SysMLProxyPort> qNameOslcSysmlProxyPortMap = new HashMap<String, SysMLProxyPort>();
	static Map<String, SysMLFullPort> qNameOslcSysmlFullPortMap = new HashMap<String, SysMLFullPort>();

	static Map<String, SysMLInterfaceBlock> qNameOslcSysmlInterfaceBlockMap = new HashMap<String, SysMLInterfaceBlock>();
	static Map<String, SysMLFlowProperty> qNameOslcSysmlFlowPropertyMap = new HashMap<String, SysMLFlowProperty>();

	static Map<String, SysMLItemFlow> qNameOslcSysmlItemFlowMap = new HashMap<String, SysMLItemFlow>();
	static Map<String, SysMLValueProperty> qNameOslcSysmlValuePropertyMap = new HashMap<String, SysMLValueProperty>();
	static Map<String, SysMLValueType> qNameOslcSysmlValueTypeMap = new HashMap<String, SysMLValueType>();

	static Map<String, SysMLBlockDiagram> qNameOslcSysmlBlockDiagramMap = new HashMap<String, SysMLBlockDiagram>();
	static Map<String, SysMLInternalBlockDiagram> qNameOslcSysmlInternalBlockDiagramMap = new HashMap<String, SysMLInternalBlockDiagram>();

	static StringBuffer buffer;

	public static String baseHTTPURI = "http://localhost:" + OSLC4JMagicDrawApplication.portNumber + "/oslc4jmagicdraw";
	static String projectId;

	static Application magicdrawApplication;
	static Model model;
	public static Project project;
	public static ProjectsManager projectsManager;
	public static Map<String, Project> loadedProjects = new HashMap<String, Project>();

	static String magicDrawFileName;

	public static void main(String[] args) throws MDModelLibException {
		loadSysMLProject("TestProject2");
	}

	/**
	 * This method retrieves SysML elements from a specific MagicDraw project
	 * (mdzip file).
	 * 
	 * This method is invoked by most or all web services of the OSLC MagicDraw
	 * adapter.
	 * 
	 * @param projectId
	 *            the name of the MagicDraw mdzip file (not the name of the
	 *            SysML model contained in the mdzip file!)
	 * 
	 */
	public static synchronized void loadSysMLProject(String projectId) {

		if (loadedProjects.keySet().contains(projectId)) {
			return;
		}

		initializeCollections();

		MagicDrawManager.projectId = projectId;

		magicDrawFileName = projectId;

		if (project == null) {
			// if (magicdrawApplication == null) { this does not support
			// reloading of models
			// launch MagicDraw in batch mode
			magicdrawApplication = Application.getInstance();
			try {
				magicdrawApplication.start(false, true, false, new String[0], null);
			} catch (ApplicationExitedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println(e.toString());
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.toString());
			}
		}

		projectsManager = magicdrawApplication.getProjectsManager();
		if (!loadedProjects.keySet().contains(projectId)) {

			final File sysmlfile;
			if (OSLC4JMagicDrawApplication.useIndividualSubversionFiles) {
				String[] filePathSegments = projectId.split("\\\\");
				String fileDirName = filePathSegments[filePathSegments.length - 1];
				String[] fileDirNameSegments = fileDirName.split("---");
				String fileName = fileDirNameSegments[1];
				String dirName = fileDirNameSegments[0];
				sysmlfile = new File(magicdrawModelsDirectory + dirName + "/" + fileName + ".mdzip");
			} else {
				sysmlfile = new File(magicdrawModelsDirectory + projectId + ".mdzip");
			}

			// final File sysmlfile = new File(magicdrawModelsDirectory +
			// projectId + ".mdzip");
			ProjectDescriptor projectDescriptor = ProjectDescriptorsFactory.createProjectDescriptor(sysmlfile.toURI());
			projectsManager.loadProject(projectDescriptor, true);

			if (!SessionManager.getInstance().isSessionCreated()) {
				SessionManager.getInstance()
						.createSession("MagicDraw OSLC Session for projectId" + projectId + sessionID);
				sessionID++;
			}
			project = projectsManager.getActiveProject();
			loadedProjects.put(projectId, project);
		} else {
			projectsManager.setActiveProject(loadedProjects.get(projectId));
			project = projectsManager.getActiveProject();
		}

		// List of packages not to load
		predefinedMagicDrawSysMLPackageNames.add("SysML");
		predefinedMagicDrawSysMLPackageNames.add("Matrix Templates Profile");
		predefinedMagicDrawSysMLPackageNames.add("UML Standard Profile");
		predefinedMagicDrawSysMLPackageNames.add("QUDV Library");
		predefinedMagicDrawSysMLPackageNames.add("PrimitiveValueTypes");
		predefinedMagicDrawSysMLPackageNames.add("MD Customization for SysML");

		// logging info saved in buffer and file
		buffer = new StringBuffer();

		try {

			// mapping MagicDraw SysML model
			model = mapSysMLModel(project);

			// collecting all MagicDraw SysML blocks and requirements
			mdSysmlBlocks = getAllSysMLBlocks(model);
			Collection<Class> blocks = new ArrayList<Class>();
			blocks.addAll(mdSysmlBlocks);
			projectIdMDSysmlBlocksMap.put(projectId, blocks);

			mdSysmlRequirements = getAllSysMLRequirements(model);
			Collection<Class> reqs = new ArrayList<Class>();
			reqs.addAll(mdSysmlRequirements);
			projectIdMDSysmlRequirementsMap.put(projectId, reqs);

			mdSysmlPackages = getAllSysMLPackages(model);
			Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package> packages = new ArrayList<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package>();
			packages.addAll(mdSysmlPackages);
			projectIdMDSysmlPackagesMap.put(projectId, packages);

			mdSysmlAssociationBlocks = getAllSysMLAssociationBlocks(model);

			mdSysmlInterfaceBlocks = getAllSysMLInterfaceBlocks(model);
			Collection<Class> infblocks = new ArrayList<Class>();
			infblocks.addAll(mdSysmlInterfaceBlocks);
			projectIdMDSysmlInterfaceBlocksMap.put(projectId, infblocks);

			mdSysmlItemFlows = getAllSysMLItemFlows(model);
			Collection<InformationFlow> itemFlows = new ArrayList<InformationFlow>();
			itemFlows.addAll(mdSysmlItemFlows);
			projectIdMDSysmlItemFlowsMap.put(projectId, itemFlows);

			predefinedMagicDrawSysMLPackageNames.remove("QUDV Library");

			mdSysmlValueTypes = getAllSysMLValueTypes(model);
			Collection<DataType> valuetypes = new ArrayList<DataType>();
			valuetypes.addAll(mdSysmlValueTypes);
			projectIdMDSysmlValueTypesMap.put(projectId, valuetypes);

			predefinedMagicDrawSysMLPackageNames.add("QUDV Library");

			getAllSysMLDiagrams();

			// closing MagicDraw
			// magicdrawApplication.exit();
			//

			// mapping MagicDraw SysML packages into OSLC packages
			mapSysMLPackages();

			// mapping MagicDraw SysML requirements into OSLC requirements
			mapSysMLRequirements();

			// mapping MagicDraw SysML blocks into OSLC blocks
			mapSysMLBlocks();
			initializeMap();

			// mapping MagicDraw SysML interface blocks into OSLC interface
			// blocks
			mapSysMLInterfaceBlocks();

			// mapping MagicDraw SysML association blocks into OSLC association
			// blocks
			mapSysMLAssociationBlocks();

			// mapping MagicDraw SysML value types into OSLC value types
			mapSysMLValueTypes();

			// mapping MagicDraw SysML item flows into OSLC item flows
			mapSysMLItemFlows();

			// mapping MagicDraw SysML package relationships
			mapSysMLPackageRelationships();

			// mapping MagicDraw SysML requirements relationships
			mapSysMLRequirementRelationships();

			// mapping MagicDraw SysML block relationships
			mapSysMLBlockRelationships();

			// map SysML block diagrams
			mapSysMLBlockDiagrams();

			// map SysML internal block diagrams

		} catch (MDModelLibException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println(e.toString());
		}

		// close MagicDraw , Problem: project = null when reloaded
		// Application.getInstance().getProjectsManager().closeProject();

		// printing logging info to a file
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter("magicdraw-log");
			fileWriter.append(buffer);
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void initializeMap() {
		Collection<Property> parts = new ArrayList<Property>();
		parts.addAll(mdSysmlPartProperties);
		projectIdMDSysmlPartPropertiesMap.put(projectId, parts);

		Collection<Property> values = new ArrayList<Property>();
		values.addAll(mdSysmlValueProperties);
		projectIdMDSysmlValuePropertiesMap.put(projectId, values);

		Collection<Property> flows = new ArrayList<Property>();
		flows.addAll(mdSysmlFlowProperties);
		projectIdMDSysmlFlowPropertiesMap.put(projectId, flows);

		Collection<Connector> connectors = new ArrayList<Connector>();
		connectors.addAll(mdSysmlConnectors);
		projectIdMDSysmlConnectorsMap.put(projectId, connectors);

		Collection<Port> ports = new ArrayList<Port>();
		ports.addAll(mdSysmlPorts);
		projectIdMDSysmlPortsMap.put(projectId, ports);

	}

	private static void initializeCollections() {

		magicDrawFileName = null;
		// model = null;

		mdSysmlBlocks.clear();
		mdSysmlRequirements.clear();
		mdSysmlPackages.clear();
		mdSysmlAssociationBlocks.clear();
		mdSysmlInterfaceBlocks.clear();
		mdSysmlItemFlows.clear();
		mdSysmlValueTypes.clear();
		mdSysmlBlockDiagrams.clear();
		mdSysmlInternalBlockDiagrams.clear();
		mdSysmlPartProperties.clear();
		mdSysmlConnectors.clear();
		mdSysmlPorts.clear();
		mdSysmlValueProperties.clear();
		mdSysmlFlowProperties.clear();

		oslcSysmlRequirements.clear();
		oslcSysmlBlocks.clear();

		idMdSysmlRequirementMap.clear();
//		idOslcSysmlRequirementMap.clear();

		oslcSysmlModelMap.clear();
	}

	private static void mapSysMLBlockDiagrams() {
		for (DiagramPresentationElement diagramPresentationElement : mdSysmlBlockDiagrams) {
			String diagramName = diagramPresentationElement.getDiagram().getName();
			// BaseElement baseElement =
			// diagramPresentationElement.getObjectParent();
			// Element owner =
			// diagramPresentationElement.getDiagram().getOwner();

			if (!diagramPresentationElement.isLoaded()) {
				diagramPresentationElement.ensureLoaded();
				System.out.println("diagram not loaded");
			}
			if (!diagramPresentationElement.isLoaded()) {
				System.out.println("diagram still not loaded");
				diagramPresentationElement.open();
			}
			Namespace namespace = diagramPresentationElement.getDiagram().getOwnerOfDiagram();
			String filePathName = "src/main/webapp/images/sysml block diagrams/" + diagramName + ".png";
			// String filePathName =
			// "C:/Users/Axel/git/oslc4jmagicdraw/oslc4jmagicdraw/src/main/webapp/images/sysml
			// block diagrams/"
			// + diagramName + ".png";
			File diagramFile = new File(filePathName);
			// File diagramFile = new File(mDestinationDir,
			// diagram.getHumanName() + diagram.getID() + ".png");
			try {
				ImageExporter.export(diagramPresentationElement, ImageExporter.PNG, diagramFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static void getAllSysMLDiagrams() {
		for (DiagramPresentationElement diagramPresentationElement : magicdrawApplication.getProject().getDiagrams()) {
			String diagramType = diagramPresentationElement.getDiagramType().getType();
			String diagramName = diagramPresentationElement.getDiagram().getName();
			String qfOwner = getQualifiedNameOrID(diagramPresentationElement.getDiagram().getOwner());
			String diagramID = qfOwner + "::" + diagramName.replaceAll("\\n", "-").replaceAll(" ", "_");
			if (diagramPresentationElement.isLoaded()) {
				System.out.println("test");
			}
			if (diagramType.equals("SysML Block Definition Diagram")) {
				mdSysmlBlockDiagrams.add(diagramPresentationElement);

				SysMLBlockDiagram sysMLBlockDiagram;
				try {
					sysMLBlockDiagram = new SysMLBlockDiagram();
					qNameOslcSysmlBlockDiagramMap.put(magicDrawFileName + "/blockdiagrams/" + diagramID,
							sysMLBlockDiagram);
					sysMLBlockDiagram.setAbout(
							URI.create(baseHTTPURI + "/services/" + projectId + "/blockdiagrams/" + diagramID));
					sysMLBlockDiagram.setName(diagramName.replaceAll(" ", "_"));
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (diagramType.equals("SysML Internal Block Diagram")) {
				mdSysmlInternalBlockDiagrams.add(diagramPresentationElement);

				SysMLInternalBlockDiagram sysMLInternalBlockDiagram;
				try {
					sysMLInternalBlockDiagram = new SysMLInternalBlockDiagram();
					qNameOslcSysmlInternalBlockDiagramMap.put(magicDrawFileName + "/internalblockdiagrams/" + diagramID,
							sysMLInternalBlockDiagram);
					sysMLInternalBlockDiagram.setAbout(
							URI.create(baseHTTPURI + "/services/" + projectId + "/internalblockdiagrams/" + diagramID));
					sysMLInternalBlockDiagram.setName(diagramName.replaceAll(" ", "_"));
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			String filePathName = "src/main/webapp/images/sysml diagrams/" + diagramName + ".png";
			File diagramFile = new File(filePathName);
			try {
				ImageExporter.export(diagramPresentationElement, ImageExporter.PNG, diagramFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private static void mapSysMLValueTypes() {
		for (DataType mdSysMLValueType : mdSysmlValueTypes) {
			String qName = mdSysMLValueType.getQualifiedName();

			SysMLValueType sysMLValueType;
			try {
				sysMLValueType = new SysMLValueType();
				qNameOslcSysmlValueTypeMap.put(
						magicDrawFileName + "/valuetypes/" + qName.replaceAll("\\n", "-").replaceAll(" ", "_"),
						sysMLValueType);

				// name attribute
				String name = mdSysMLValueType.getName();

				// URI
				if (name != null) {
					sysMLValueType.setName(name);
					buffer.append("\r\nSysML Block with Name: " + sysMLValueType.getName());
					sysMLValueType.setAbout(URI.create(baseHTTPURI + "/services/" + projectId + "/valuetypes/"
							+ getQualifiedNameOrID(mdSysMLValueType)));
				}

				// unit attribute
				String humanName = mdSysMLValueType.getHumanName();
				Element unit = (Element) StereotypesHelper.getStereotypePropertyFirst(mdSysMLValueType,
						StereotypesHelper.getFirstVisibleStereotype(mdSysMLValueType), "unit");

				Stereotype valueTypeStereotype = StereotypesHelper.getStereotype(Application.getInstance().getProject(),
						SysMLConstants.VALUE_TYPE_STEREOTYPE, SysMLConstants.SYSML_PROFILE);
				if (valueTypeStereotype != null) {
					unit = ((InstanceSpecification) StereotypesHelper.getStereotypePropertyFirst(mdSysMLValueType,
							valueTypeStereotype, SysMLConstants.VALUE_TYPE_UNIT_TAG));
				}

				if (unit != null) {
					sysMLValueType.setUnit(URI
							.create(baseHTTPURI + "/services/" + projectId + "/units/" + getQualifiedNameOrID(unit)));
				}

				// quantity kind attribute
				Element quantityKind = (Element) StereotypesHelper.getStereotypePropertyFirst(mdSysMLValueType,
						StereotypesHelper.getFirstVisibleStereotype(mdSysMLValueType), "quantityKind");
				if (quantityKind != null) {
					sysMLValueType.setUnit(URI.create(baseHTTPURI + "/services/" + projectId + "/quantitykinds/"
							+ getQualifiedNameOrID(quantityKind)));
				}

			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static Collection<DataType> getAllSysMLValueTypes(
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package packageableElement) throws MDModelLibException {
		Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.DataType> sysmlValueTypes = new ArrayList<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.DataType>();
		if (packageableElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) {
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package package_ = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) packageableElement;
			for (PackageableElement nestedPackageableElement : package_.getPackagedElement()) {
				if (MDSysMLModelHandler.isSysMLElement(nestedPackageableElement, "ValueType")) {
					DataType magicDrawSysMLValueType = (DataType) nestedPackageableElement;
					sysmlValueTypes.add(magicDrawSysMLValueType);
				} else
					if (nestedPackageableElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) {
					com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package nestedPackage = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) nestedPackageableElement;
					if (!predefinedMagicDrawSysMLPackageNames.contains(nestedPackage.getName())) {
						sysmlValueTypes.addAll(getAllSysMLValueTypes(nestedPackage));
					}
				}
			}
		}

		return sysmlValueTypes;
	}

	private static void mapSysMLItemFlows() throws MDModelLibException {
		for (com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow mdSysMLItemFlow : mdSysmlItemFlows) {
			String itemFlowID = mdSysMLItemFlow.getID();
			// qNameMdSysmlAssociationBlockMap.put(
			// qName.replaceAll("\\n", "-").replaceAll(" ", "_"),
			// mdSysMLAssociationBlock);
			SysMLItemFlow sysMLItemFlow;
			try {
				sysMLItemFlow = new SysMLItemFlow();
				qNameOslcSysmlItemFlowMap.put(getQualifiedNameOrID(mdSysMLItemFlow), sysMLItemFlow);
				sysMLItemFlow.setAbout(URI.create(baseHTTPURI + "/services/" + projectId + "/itemflows/"
						+ getQualifiedNameOrID(mdSysMLItemFlow)));

				// information source
				NamedElement informationSource = (NamedElement) mdSysMLItemFlow.getInformationSource().toArray()[0];
				URI linkedInformationSourceURI = null;
				if (MDSysMLModelHandler.isSysMLElement(informationSource, "PartProperty")) {
					linkedInformationSourceURI = new URI(baseHTTPURI + "/services/" + projectId + "/partproperties/"
							+ getQualifiedNameOrID(informationSource));
				} else if (MDSysMLModelHandler.isSysMLElement(informationSource, "ProxyPort")) {
					linkedInformationSourceURI = new URI(baseHTTPURI + "/services/" + projectId + "/proxyports/"
							+ getQualifiedNameOrID(informationSource));
				} else if (MDSysMLModelHandler.isSysMLElement(informationSource, "FullPort")) {
					linkedInformationSourceURI = new URI(baseHTTPURI + "/services/" + projectId + "/fullports/"
							+ getQualifiedNameOrID(informationSource));
				} else
					if (informationSource instanceof com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port) {
					linkedInformationSourceURI = new URI(baseHTTPURI + "/services/" + projectId + "/ports/"
							+ getQualifiedNameOrID(informationSource));
				}
				sysMLItemFlow.setInformationSource(linkedInformationSourceURI);

				// information target
				NamedElement informationTarget = (NamedElement) mdSysMLItemFlow.getInformationTarget().toArray()[0];
				URI linkedInformationTargetURI = null;
				if (MDSysMLModelHandler.isSysMLElement(informationTarget, "PartProperty")) {
					linkedInformationTargetURI = new URI(baseHTTPURI + "/services/" + projectId + "/partproperties/"
							+ getQualifiedNameOrID(informationTarget));

				} else if (MDSysMLModelHandler.isSysMLElement(informationTarget, "ProxyPort")) {
					linkedInformationTargetURI = new URI(baseHTTPURI + "/services/" + projectId + "/proxyports/"
							+ getQualifiedNameOrID(informationTarget));
				} else if (MDSysMLModelHandler.isSysMLElement(informationTarget, "FullPort")) {
					linkedInformationTargetURI = new URI(baseHTTPURI + "/services/" + projectId + "/fullports/"
							+ getQualifiedNameOrID(informationTarget));
				} else
					if (informationTarget instanceof com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port) {
					linkedInformationTargetURI = new URI(baseHTTPURI + "/services/" + projectId + "/ports/"
							+ getQualifiedNameOrID(informationTarget));
				}
				sysMLItemFlow.setInformationTarget(linkedInformationTargetURI);

				// realizingConnector
				if (mdSysMLItemFlow.getRealizingConnector().size() > 0) {
					Connector connector = (Connector) mdSysMLItemFlow.getRealizingConnector().toArray()[0];
					URI realizingConnectorURI = new URI(
							baseHTTPURI + "/services/" + projectId + "/connectors/" + getQualifiedNameOrID(connector));
					sysMLItemFlow.setRealizingConnector(realizingConnectorURI);
				}

				// itemProperty
				Property itemProperty = (Property) StereotypesHelper.getStereotypePropertyFirst(mdSysMLItemFlow,
						StereotypesHelper.getFirstVisibleStereotype(mdSysMLItemFlow), "itemProperty");
				URI itemPropertyURI = null;
				if (itemProperty != null) {
					if (MDSysMLModelHandler.isSysMLElement(itemProperty, "FlowProperty")) {
						itemPropertyURI = new URI(baseHTTPURI + "/services/" + projectId + "/flowproperties/"
								+ getQualifiedNameOrID(itemProperty));
					}
					sysMLItemFlow.setItemProperty(itemPropertyURI);
				}

			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static void mapSysMLAssociationBlocks() {
		for (com.nomagic.uml2.ext.magicdraw.classes.mdassociationclasses.AssociationClass mdSysMLAssociationBlock : mdSysmlAssociationBlocks) {
			String qName = mdSysMLAssociationBlock.getQualifiedName();
			// qNameMdSysmlAssociationBlockMap.put(
			// qName.replaceAll("\\n", "-").replaceAll(" ", "_"),
			// mdSysMLAssociationBlock);
			SysMLAssociationBlock sysMLAssociationBlock;
			try {
				sysMLAssociationBlock = new SysMLAssociationBlock();
				qNameOslcSysmlAssociationBlockMap.put(
						magicDrawFileName + "/associationblocks/" + qName.replaceAll("\\n", "-").replaceAll(" ", "_"),
						sysMLAssociationBlock);

				// SysML association block Name attribute
				String name = mdSysMLAssociationBlock.getName();
				if (name != null) {
					sysMLAssociationBlock.setName(name);
					buffer.append("\r\nSysML Block with Name: " + sysMLAssociationBlock.getName());
					sysMLAssociationBlock.setAbout(URI.create(baseHTTPURI + "/services/" + projectId
							+ "/associationblocks/" + qName.replaceAll("\\n", "-").replaceAll(" ", "_")));
				}

				// SysML association block memberEnd attribute
				Association mdSysMAssociation = (Association) mdSysMLAssociationBlock;
				Link[] linksArray = new Link[2];

				int linksArrayIndex = 0;
				for (Property memberEnd : mdSysMAssociation.getMemberEnd()) {
					URI linkedElementURI = null;
					linkedElementURI = new URI(baseHTTPURI + "/services/" + projectId + "/referenceproperties/"
							+ memberEnd.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_"));
					Link link = new Link(linkedElementURI);
					linksArray[linksArrayIndex] = link;
					linksArrayIndex++;
				}
				if (linksArrayIndex > 0) {
					sysMLAssociationBlock.setMemberEnds(linksArray);
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static void mapSysMLInterfaceBlocks() throws MDModelLibException {
		for (Class mdSysMLBlock : mdSysmlInterfaceBlocks) {
			String qName = mdSysMLBlock.getQualifiedName();
			// qNameMdSysmlInterfaceBlockMap.put(
			// qName.replaceAll("\\n", "-").replaceAll(" ", "_"),
			// mdSysMLBlock);
			SysMLInterfaceBlock sysMLInterfaceBlock;
			try {
				sysMLInterfaceBlock = new SysMLInterfaceBlock();
				qNameOslcSysmlInterfaceBlockMap.put(
						magicDrawFileName + "/interfaceblocks/" + qName.replaceAll("\\n", "-").replaceAll(" ", "_"),
						sysMLInterfaceBlock);

				// SysML Block Name attribute
				String name = mdSysMLBlock.getName();
				if (name != null) {
					sysMLInterfaceBlock.setName(name);
					buffer.append("\r\nSysML Interface Block with Name: " + sysMLInterfaceBlock.getName());
					sysMLInterfaceBlock.setAbout(URI.create(baseHTTPURI + "/services/" + projectId + "/interfaceblocks/"
							+ qName.replaceAll("\\n", "-").replaceAll(" ", "_")));
				}

				// SysML Block Flow Properties
				mapSysMLFlowProperties(mdSysMLBlock, sysMLInterfaceBlock);

				// SysML Proxy Ports
				mapSysMLProxyPorts(mdSysMLBlock, sysMLInterfaceBlock);

			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static void mapSysMLFlowProperties(Class mdSysMLBlock, SysMLInterfaceBlock sysMLInterfaceBlock) {
		Link[] flowPropertiesLinksArray = getLinkedStereotypedSysMLElements(mdSysMLBlock.getOwnedAttribute(),
				"FlowProperty", baseHTTPURI + "/services/" + projectId + "/flowproperties/");

		if (flowPropertiesLinksArray != null) {
			sysMLInterfaceBlock.setFlowProperties(flowPropertiesLinksArray);

			buffer.append("\r\n " + sysMLInterfaceBlock.getName());
			buffer.append("\r\n\tblock reference properties: " + flowPropertiesLinksArray.length);
			buffer.append("\r\n " + sysMLInterfaceBlock.getName());
			buffer.append("\r\n\tblock reference properties: ");
			for (Link link : flowPropertiesLinksArray) {
				buffer.append("\r\n\t\t " + link.getValue());
			}
		}

		for (Property property : mdSysMLBlock.getOwnedAttribute()) {

			if (property.getAppliedStereotypeInstance() != null) {
				InstanceSpecification stereotypeInstance = property.getAppliedStereotypeInstance();
				if (stereotypeInstance.getClassifier().get(0).getName().contains("FlowProperty")) {
					SysMLFlowProperty sysmlFlowProperty;
					try {
						sysmlFlowProperty = new SysMLFlowProperty();
						qNameOslcSysmlFlowPropertyMap.put(
								magicDrawFileName + "/flowproperties/"
										+ property.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_"),
								sysmlFlowProperty);

						// referenceProperty name
						sysmlFlowProperty.setName(property.getName());

						String qName = property.getQualifiedName();
						sysmlFlowProperty.setAbout(URI.create(baseHTTPURI + "/services/" + projectId
								+ "/flowproperties/" + qName.replaceAll("\\n", "-").replaceAll(" ", "_")));

						// referenceProperty type
						if (property.getType() != null) {
							sysmlFlowProperty
									.setType(new URI(baseHTTPURI + "/services/" + projectId + "/blocks/" + property
											.getType().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
						}

						// referenceProperty multiplicity
						// String lowerMultiplicity = Integer.toString(property
						// .getLower());
						// String upperMultiplicity = Integer.toString(property
						// .getUpper());
						// sysmlFlowProperty.setLower(lowerMultiplicity);
						// sysmlFlowProperty.setUpper(upperMultiplicity);

						// direction
						Object directionObject = StereotypesHelper.getStereotypePropertyFirst(property,
								(Stereotype) property.getAppliedStereotypeInstance().getClassifier().get(0),
								"direction");
						if (directionObject instanceof EnumerationLiteral) {
							EnumerationLiteral enumLit = (EnumerationLiteral) directionObject;
							String enumLitName = enumLit.getName();
							if (enumLitName.equals("in")) {
								// sysmlFlowProperty
								// .setDirection(SysMLFlowDirection.IN);
								sysmlFlowProperty.setDirection("in");
							} else if (enumLitName.equals("out")) {
								// sysmlFlowProperty
								// .setDirection(SysMLFlowDirection.OUT);
								sysmlFlowProperty.setDirection("out");
							}

						}

					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

	}

	private static Collection<com.nomagic.uml2.ext.magicdraw.classes.mdassociationclasses.AssociationClass> getAllSysMLAssociationBlocks(
			Model model) {
		Collection<com.nomagic.uml2.ext.magicdraw.classes.mdassociationclasses.AssociationClass> sysmlAssociationBlocks = new ArrayList<com.nomagic.uml2.ext.magicdraw.classes.mdassociationclasses.AssociationClass>();
		java.lang.Class[] classArray = new java.lang.Class[1];
		classArray[0] = com.nomagic.uml2.ext.magicdraw.classes.mdassociationclasses.AssociationClass.class;
		Collection<? extends Element> elementsOfType = ModelHelper.getElementsOfType(model, classArray, true, true);
		sysmlAssociationBlocks = (Collection<AssociationClass>) elementsOfType;
		buffer.append("**** Elements of type AssociationClass ****");
		for (Element element : elementsOfType) {
			buffer.append("\r\n" + element.getHumanName());
		}
		return sysmlAssociationBlocks;
	}

	// static Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class>
	// getAllSysMLAssociationBlocks(
	// com.nomagic.uml2.ext.magicdraw.classes.mdkernel.PackageableElement
	// packageableElement) throws MDModelLibException {
	// Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class>
	// sysmlBlocks = new
	// ArrayList<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class>();
	// if (packageableElement instanceof
	// com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) {
	// com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package package_ =
	// (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package)
	// packageableElement;
	// for (PackageableElement nestedPackageableElement : package_
	// .getPackagedElement()) {
	// if (MDSysMLModelHandler.isSysMLElement(nestedPackageableElement,
	// "AssociationBlock")) {
	// Class magicDrawSysMLBlock = (Class) nestedPackageableElement;
	// sysmlBlocks.add(magicDrawSysMLBlock);
	// sysmlBlocks
	// .addAll(getAllSysMLAssociationBlocks(magicDrawSysMLBlock));
	// } else if (nestedPackageableElement instanceof
	// com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) {
	// com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package nestedPackage =
	// (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package)
	// nestedPackageableElement;
	// if (!predefinedMagicDrawSysMLPackageNames
	// .contains(nestedPackage.getName())) {
	// sysmlBlocks
	// .addAll(getAllSysMLAssociationBlocks(nestedPackage));
	// }
	// }
	// }
	// } else if (packageableElement instanceof
	// com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
	// com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class class_ =
	// (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class)
	// packageableElement;
	// for (Classifier nestedClassifier : class_.getNestedClassifier()) {
	// if (MDSysMLModelHandler.isSysMLElement(nestedClassifier,
	// "AssociationBlock")) {
	// sysmlBlocks
	// .add((com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class)
	// nestedClassifier);
	// }
	// sysmlBlocks.addAll(getAllSysMLAssociationBlocks(nestedClassifier));
	// }
	// }
	// return sysmlBlocks;
	// }

	private static Collection<com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow> getAllSysMLItemFlows(
			Model model) throws MDModelLibException {
		Collection<com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow> sysmlInformationFlows = new ArrayList<com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow>();
		java.lang.Class[] classArray = new java.lang.Class[1];
		classArray[0] = com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow.class;
		Collection<? extends Element> elementsOfType = ModelHelper.getElementsOfType(model, classArray, true, true);
		sysmlInformationFlows = (Collection<com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow>) elementsOfType;
		Collection<com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow> itemFlows = new ArrayList<com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow>();
		for (InformationFlow sysmlInformationFlow : sysmlInformationFlows) {
			if (MDSysMLModelHandler.isSysMLElement(sysmlInformationFlow, "ItemFlow")) {
				itemFlows.add(sysmlInformationFlow);
			}
		}

		// buffer.append("**** Elements of type InformationFlow ****");
		// for (Element element : elementsOfType) {
		// buffer.append("\r\n" + element.getHumanName());
		// }
		return itemFlows;
	}

	private static void mapSysMLPackageRelationships() {
		for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package mdSysMLPackage : mdSysmlPackages) {

			SysMLPackage sysMLPackage = qNameOslcSysmlPackageMap.get(magicDrawFileName + "/packages/"
					+ mdSysMLPackage.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_"));

			// get nested blocks
			Link[] packageBlocksLinksArray = getLinkedStereotypedSysMLElements(mdSysMLPackage.getOwnedType(), "Block",
					baseHTTPURI + "/services/" + projectId + "/blocks/");
			if (packageBlocksLinksArray != null) {
				sysMLPackage.setBlocks(packageBlocksLinksArray);
				buffer.append("\r\n " + sysMLPackage.getName());
				buffer.append("\r\n\tpackageBlocks: " + packageBlocksLinksArray.length);
				buffer.append("\r\n " + sysMLPackage.getName());
				buffer.append("\r\n\tpackageBlocks: ");
				for (Link link : packageBlocksLinksArray) {
					buffer.append("\r\n\t\t " + link.getValue());
				}
			}

			// get nested requirements
			Link[] packageRequirementsLinksArray = getLinkedStereotypedSysMLElements(mdSysMLPackage.getOwnedType(),
					"Requirement", baseHTTPURI + "/services/" + projectId + "/requirements/");
			if (packageRequirementsLinksArray != null) {
				sysMLPackage.setRequirements(packageRequirementsLinksArray);
				buffer.append("\r\n " + sysMLPackage.getName());
				buffer.append("\r\n\tpackageRequirements: " + packageRequirementsLinksArray.length);
				buffer.append("\r\n " + sysMLPackage.getName());
				buffer.append("\r\n\tpackageRequirements: ");
				for (Link link : packageRequirementsLinksArray) {
					buffer.append("\r\n\t\t " + link.getValue());
				}
			}
		}

	}

	private static void mapSysMLPackages() {
		for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package mdSysMLPackage : mdSysmlPackages) {
			String qName = mdSysMLPackage.getQualifiedName();
			// qNameMdSysmlPackageMap
			// .put(qName.replaceAll("\\n", "-"), mdSysMLPackage);
			SysMLPackage sysMLPackage;
			try {
				sysMLPackage = new SysMLPackage();
				qNameOslcSysmlPackageMap.put(
						magicDrawFileName + "/packages/" + qName.replaceAll("\\n", "-").replaceAll(" ", "_"),
						sysMLPackage);

				// SysML Package Name attribute
				String name = mdSysMLPackage.getName();
				if (name != null) {
					sysMLPackage.setName(name);
					buffer.append("\r\nSysML Package with Name: " + sysMLPackage.getName());
					sysMLPackage.setAbout(URI.create(baseHTTPURI + "/services/" + projectId + "/packages/"
							+ qName.replaceAll("\\n", "-").replaceAll(" ", "_")));
				}

			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static Model mapSysMLModel(Project project) {
		Model model = project.getModel();
		SysMLModel sysMLModel;
		try {
			sysMLModel = new SysMLModel();
			if (model.getName() == null) {
				sysMLModel.setName("Data");
			} else {
				sysMLModel.setName(model.getName());
			}

			sysMLModel.setAbout(URI.create(baseHTTPURI + "/services/" + projectId + "/model/" + model.getName()));

			oslcSysmlModelMap.put(model.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_"), sysMLModel);

			Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package> modelPackages = new ArrayList<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package>();
			for (PackageableElement packageableElement : model.getPackagedElement()) {
				if (packageableElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) {
					if (!predefinedMagicDrawSysMLPackageNames.contains(packageableElement.getName())) {
						modelPackages.add((com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) packageableElement);
					}

				}
			}

			if (modelPackages.size() > 0) {
				Link[] linksArray = new Link[modelPackages.size()];

				int linksArrayIndex = 0;
				for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package package1 : modelPackages) {
					if (!predefinedMagicDrawSysMLPackageNames.contains(package1.getName())) {
						URI linkedElementURI = null;
						linkedElementURI = new URI(baseHTTPURI + "/services/" + projectId + "/packages/"
								+ package1.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_"));
						Link link = new Link(linkedElementURI);
						linksArray[linksArrayIndex] = link;
						linksArrayIndex++;
					}
				}

				sysMLModel.setPackages(linksArray);
				buffer.append("\r\n " + sysMLModel.getName());
				buffer.append("\r\n\tmodel packages: " + linksArray.length);
				buffer.append("\r\n " + sysMLModel.getName());
				buffer.append("\r\n\tmodel packages: ");
				for (Link link2 : linksArray) {
					buffer.append("\r\n\t\t " + link2.getValue());
				}
			}

			// map SysML packages
			// mapSysMLPackages(modelPackages);

		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return model;

	}

	private static void mapSysMLBlockRelationships() throws MDModelLibException {
		for (Class mdSysmlBlock : mdSysmlBlocks) {
			SysMLBlock sysmlBlock = qNameOslcSysmlBlockMap.get(magicDrawFileName + "/blocks/"
					+ mdSysmlBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_"));

			// SysML Block generalization
			Collection<Classifier> inheritedClassifiers = mdSysmlBlock.getGeneral();
			Link[] inheritedBlocksLinkArray = new Link[inheritedClassifiers.size()];

			int inheritedBlocksLinkArrayIndex = 0;
			buffer.append("\r\n " + sysmlBlock.getName());
			buffer.append("\r\n\tinheritedBlocks: " + inheritedBlocksLinkArray.length);
			for (Classifier inheritedclassifier : inheritedClassifiers) {
				String qNameInheritedclassifier = inheritedclassifier.getQualifiedName();
				SysMLBlock inheritedBlock = qNameOslcSysmlBlockMap.get(magicDrawFileName + "/blocks/"
						+ qNameInheritedclassifier.replaceAll("\\n", "-").replaceAll(" ", "_"));

				URI inheritedBlockURI = null;
				try {
					inheritedBlockURI = new URI(baseHTTPURI + "/services/" + projectId + "/blocks/"
							+ qNameInheritedclassifier.replaceAll("\\n", "-").replaceAll(" ", "_"));
					Link inheritedBlockLink = new Link(inheritedBlockURI);
					inheritedBlocksLinkArray[inheritedBlocksLinkArrayIndex] = inheritedBlockLink;
					inheritedBlocksLinkArrayIndex++;
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (inheritedBlocksLinkArray.length > 0) {
				sysmlBlock.setInheritedBlocks(inheritedBlocksLinkArray);
				buffer.append("\r\n " + sysmlBlock.getName());
				buffer.append("\r\n\tinheritedBlocks: ");
				for (Link link : inheritedBlocksLinkArray) {
					buffer.append("\r\n\t\t " + link.getValue());
				}
			}

			// SysML Block nesting
			Collection<Classifier> nestedClassifiers = mdSysmlBlock.getNestedClassifier();
			Link[] nestedBlocksLinkArray = new Link[nestedClassifiers.size()];
			int nestedBlocksLinkArrayIndex = 0;
			buffer.append("\r\n " + sysmlBlock.getName());
			buffer.append("\r\n\tnestedBlocks: " + nestedBlocksLinkArray.length);
			for (Classifier nestedClassifier : nestedClassifiers) {
				String qNameNestedclassifier = nestedClassifier.getQualifiedName();
				SysMLBlock nestedBlock = qNameOslcSysmlBlockMap.get(magicDrawFileName + "/blocks/"
						+ qNameNestedclassifier.replaceAll("\\n", "-").replaceAll(" ", "_"));
				URI nestedBlockURI = null;
				try {
					nestedBlockURI = new URI(
							baseHTTPURI + "/services/" + projectId + "/blocks/" + nestedBlock.getName());
					Link nestedBlockLink = new Link(nestedBlockURI);
					nestedBlocksLinkArray[nestedBlocksLinkArrayIndex] = nestedBlockLink;
					nestedBlocksLinkArrayIndex++;
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (nestedBlocksLinkArray.length > 0) {
				sysmlBlock.setNestedBlocks(nestedBlocksLinkArray);
				buffer.append("\r\n " + sysmlBlock.getName());
				buffer.append("\r\n\tnestedBlocks: ");
				for (Link link : nestedBlocksLinkArray) {
					buffer.append("\r\n\t\t " + link.getValue());
				}
			}

			// satisfies relationships (Block satisfies Requirement)
			Link[] satisfiesLinks = getDirectedLinksOfSysMLElement(true, mdSysmlBlock, "Satisfy");
			if (satisfiesLinks != null) {
				sysmlBlock.setSatisfies(satisfiesLinks);
				buffer.append("\r\n " + sysmlBlock.getName());
				buffer.append("\r\n\tSatisfies: ");
				for (Link link : satisfiesLinks) {
					buffer.append("\r\n\t\t " + link.getValue());
				}
			}

		}

	}

	private static void mapSysMLRequirementRelationships() throws MDModelLibException {
		for (Class mdSysMLRequirement : mdSysmlRequirements) {

			// String sourceReqQualifiedName = mdSysMLRequirement
			// .getQualifiedName().replaceAll("\\n", "-")
			// .replaceAll(" ", "_");
			// SysMLRequirement sysMLRequirement = idOslcSysmlRequirementMap
			// .get(sourceReqQualifiedName);

//			String id = (String) StereotypesHelper.getStereotypePropertyFirst(mdSysMLRequirement,
//					StereotypesHelper.getFirstVisibleStereotype(mdSysMLRequirement), "Id");
			String id = getQualifiedNameOrID(mdSysMLRequirement);
			SysMLRequirement sysMLRequirement = idOslcSysmlRequirementMap.get(projectId + "/requirements/" + id);

			// subRequirements
			Collection<Class> subRequirements = new ArrayList<Class>();
			for (Classifier nestedClassifier : mdSysMLRequirement.getNestedClassifier()) {
				if (MDSysMLModelHandler.isSysMLElement(nestedClassifier, "Requirement")) {
					subRequirements.add((Class) nestedClassifier);
				}
			}
			if (subRequirements.size() > 0) {
				Link[] subRequirementsLinksArray = new Link[subRequirements.size()];
				int linksArrayIndex = 0;
				for (NamedElement namedElement : subRequirements) {
					try {
						URI linkedElementURI = null;

						if (namedElement instanceof Class) {
							Class mdSysMLClass = (Class) namedElement;
//							String linkedRequirementID = (String) StereotypesHelper.getStereotypePropertyFirst(
//									mdSysMLClass, StereotypesHelper.getFirstVisibleStereotype(mdSysMLRequirement),
//									"Id");
							String linkedRequirementID = getQualifiedNameOrID(mdSysMLRequirement);
							if (linkedRequirementID != null) {
								linkedElementURI = new URI(baseHTTPURI + "/services/" + projectId + "/requirements/"
										+ linkedRequirementID);
								Link link = new Link(linkedElementURI);
								subRequirementsLinksArray[linksArrayIndex] = link;
								linksArrayIndex++;
							}
						}

					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				sysMLRequirement.setSubRequirements(subRequirementsLinksArray);
			}

			// master relationship
			URI masterURI = getDirectedLinkSysMLElement(true, mdSysMLRequirement, "Copy");
			if (masterURI != null) {
				sysMLRequirement.setMaster(masterURI);
			}

			// derivedFrom relationships (Requirement derivedFrom Requirements)
			Link[] derivedFromLinks = getDirectedLinksOfSysMLElement(true, mdSysMLRequirement, "DeriveReqt");
			if (derivedFromLinks != null) {
				sysMLRequirement.setDerivedFromElements(derivedFromLinks);
				buffer.append("\r\n " + sysMLRequirement.getIdentifier());
				buffer.append("\r\n\tDerivedFrom: ");
				for (Link link : derivedFromLinks) {
					buffer.append("\r\n\t\t " + link.getValue());
				}
			}

			// derived relationships (Requirement has derived Requirements)
			Link[] derivedLinks = getDirectedLinksOfSysMLElement(false, mdSysMLRequirement, "DeriveReqt");
			if (derivedLinks != null) {
				sysMLRequirement.setDerivedElements(derivedLinks);
				buffer.append("\r\n " + sysMLRequirement.getIdentifier());
				buffer.append("\r\n\tDerived: ");
				for (Link link : derivedLinks) {
					buffer.append("\r\n\t\t " + link.getValue());
				}
			}

			// satisfiedBy relationships (Requirement satisfied By X)
			Link[] satisfiedByLinks = getDirectedLinksOfSysMLElement(false, mdSysMLRequirement, "Satisfy");
			if (satisfiedByLinks != null) {
				sysMLRequirement.setSatisfiedBy(satisfiedByLinks);
				buffer.append("\r\n " + sysMLRequirement.getIdentifier());
				buffer.append("\r\n\tSatisfiedBy: ");
				for (Link link : satisfiedByLinks) {
					buffer.append("\r\n\t\t " + link.getValue());
				}
			}

			// refinedBy relationships (Requirement refined By X)
			Link[] refinedByLinks = getDirectedLinksOfSysMLElement(false, mdSysMLRequirement, "Refine");
			if (refinedByLinks != null) {
				sysMLRequirement.setElaboratedBy(refinedByLinks);
				buffer.append("\r\n " + sysMLRequirement.getIdentifier());
				buffer.append("\r\n\tRefinedBy: ");
				for (Link link : refinedByLinks) {
					buffer.append("\r\n\t\t " + link.getValue());
				}
			}

		}
	}

	private static void mapSysMLBlocks() throws MDModelLibException {

		for (Class mdSysMLBlock : mdSysmlBlocks) {
			String qName = mdSysMLBlock.getQualifiedName();
			// qNameMdSysmlBlockMap.put(
			// qName.replaceAll("\\n", "-").replaceAll(" ", "_"),
			// mdSysMLBlock);
			SysMLBlock sysMLBlock;
			try {
				sysMLBlock = new SysMLBlock();
				qNameOslcSysmlBlockMap.put(
						magicDrawFileName + "/blocks/" + qName.replaceAll("\\n", "-").replaceAll(" ", "_"), sysMLBlock);

				// SysML Block Name attribute
				String name = mdSysMLBlock.getName();
				if (name != null) {
					sysMLBlock.setName(name);
					buffer.append("\r\nSysML Block with Name: " + sysMLBlock.getName());
					sysMLBlock.setAbout(URI.create(baseHTTPURI + "/services/" + projectId + "/blocks/"
							+ qName.replaceAll("\\n", "-").replaceAll(" ", "_")));
				}

				// SysML Block Parts
				mapSysMLPartProperties(mdSysMLBlock, sysMLBlock);

				// SysML Block References
				mapSysMLReferenceProperties(mdSysMLBlock, sysMLBlock);

				// SysML Block Value Properties
				mapSysMLValueProperties(mdSysMLBlock, sysMLBlock);

				// SysML Block Flow Properties
				mapSysMLFlowProperties(mdSysMLBlock, sysMLBlock);

				// SysML Block Connectors
				mapSysMLConnectors(mdSysMLBlock, sysMLBlock);

				// SysML Block Ports
				mapSysMLPorts(mdSysMLBlock, sysMLBlock);

			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void mapSysMLValueProperties(Class mdSysMLBlock, SysMLBlock sysMLBlock) throws MDModelLibException {
		Link[] valuePropertiesLinksArray = getLinkedStereotypedSysMLElements(mdSysMLBlock.getOwnedAttribute(),
				"ValueProperty", baseHTTPURI + "/services/" + projectId + "/valueproperties/");

		if (valuePropertiesLinksArray != null) {
			sysMLBlock.setValueProperties(valuePropertiesLinksArray);

			buffer.append("\r\n " + sysMLBlock.getName());
			buffer.append("\r\n\tblock reference properties: " + valuePropertiesLinksArray.length);
			buffer.append("\r\n " + sysMLBlock.getName());
			buffer.append("\r\n\tblock reference properties: ");
			for (Link link : valuePropertiesLinksArray) {
				buffer.append("\r\n\t\t " + link.getValue());
			}
		}

		for (Property property : mdSysMLBlock.getOwnedAttribute()) {

			if (property.getAppliedStereotypeInstance() != null) {
				InstanceSpecification stereotypeInstance = property.getAppliedStereotypeInstance();
				if (stereotypeInstance.getClassifier().get(0).getName().contains("ValueProperty")) {
					SysMLValueProperty sysmlValueProperty;
					try {
						sysmlValueProperty = new SysMLValueProperty();
						qNameOslcSysmlValuePropertyMap.put(
								magicDrawFileName + "/valueproperties/" + getQualifiedNameOrID(property),
								sysmlValueProperty);
						mdSysmlValueProperties.add(property);

						// valueProperty name
						sysmlValueProperty.setName(property.getName());
						sysmlValueProperty.setAbout(URI.create(baseHTTPURI + "/services/" + projectId
								+ "/valueproperties/" + getQualifiedNameOrID(property)));

						// valueProperty type
						if (property.getType() != null) {
							if (MDSysMLModelHandler.isSysMLElement(property.getType(), "Block")) {
								sysmlValueProperty.setType(new URI(baseHTTPURI + "/services/" + projectId + "/blocks/"
										+ getQualifiedNameOrID(property.getType())));
							} else if (MDSysMLModelHandler.isSysMLElement(property.getType(), "ValueType")) {
								sysmlValueProperty.setType(new URI(baseHTTPURI + "/services/" + projectId
										+ "/valuetypes/" + getQualifiedNameOrID(property.getType())));
							}
						}

						// referenceProperty multiplicity
						String lowerMultiplicity = Integer.toString(property.getLower());
						String upperMultiplicity = Integer.toString(property.getUpper());
						sysmlValueProperty.setLower(lowerMultiplicity);
						sysmlValueProperty.setUpper(upperMultiplicity);

						// defaultValue
						ValueSpecification valueSpecification = property.getDefaultValue();
						if (valueSpecification instanceof LiteralString) {
							LiteralString literalString = (LiteralString) valueSpecification;
							sysmlValueProperty.setDefaultValue(literalString.getValue());
						}

					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

	}

	private static void mapSysMLPorts(Class mdSysMLBlock, SysMLBlock sysMLBlock) throws MDModelLibException {
		ArrayList<Port> proxyPortsList = new ArrayList<Port>();
		ArrayList<Port> fullPortsList = new ArrayList<Port>();
		ArrayList<Port> portsList = new ArrayList<Port>();

		for (Port port : mdSysMLBlock.getOwnedPort()) {
			if (MDSysMLModelHandler.isSysMLElement(port, "ProxyPort")) {
				proxyPortsList.add(port);
			} else if (MDSysMLModelHandler.isSysMLElement(port, "FullPort")) {
				fullPortsList.add(port);
			}
			// else if (MDSysMLModelHandler.isSysMLElement(port, "FlowPort")){
			// flowPortsList.add(port);
			// }
			else if (port instanceof com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port) {
				// standard port
				portsList.add(port);
			}
		}

		Link[] proxyPortsLinksArray;
		Link[] fullPortsLinksArray;
		Link[] portsLinksArray;

		if (proxyPortsList.size() > 0) {
			proxyPortsLinksArray = new Link[proxyPortsList.size()];
			String proxyPortBaseURI = baseHTTPURI + "/services/" + projectId + "/proxyports/";
			int proxyPortsLinksArrayIndex = 0;
			for (Port port : proxyPortsList) {
				URI linkedElementURI = null;
				try {
					linkedElementURI = new URI(proxyPortBaseURI + getQualifiedNameOrID(port));
					Link link = new Link(linkedElementURI);
					proxyPortsLinksArray[proxyPortsLinksArrayIndex] = link;
					proxyPortsLinksArrayIndex++;

					SysMLProxyPort sysMLProxyPort = new SysMLProxyPort();
					qNameOslcSysmlProxyPortMap.put(magicDrawFileName + "/proxyports/" + getQualifiedNameOrID(port),
							sysMLProxyPort);

					// port name
					sysMLProxyPort.setName(port.getName());

					// port URI
					String qName = port.getQualifiedName();
					sysMLProxyPort.setAbout(URI.create(
							baseHTTPURI + "/services/" + projectId + "/proxyports/" + getQualifiedNameOrID(port)));

					// port type
					if (port.getType() != null) {
						if (MDSysMLModelHandler.isSysMLElement(port.getType(), "Block")) {
							sysMLProxyPort.setType(new URI(baseHTTPURI + "/services/" + projectId + "/blocks/"
									+ port.getType().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
						} else if (MDSysMLModelHandler.isSysMLElement(port.getType(), "InterfaceBlock")) {
							sysMLProxyPort.setType(new URI(baseHTTPURI + "/services/" + projectId + "/interfaceblocks/"
									+ port.getType().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
						}
					}

					// isService
					sysMLProxyPort.setIsService(port.isService());

					// isBehavior
					sysMLProxyPort.setIsBehavior(port.isBehavior());

					// isConjugated
					sysMLProxyPort.setIsConjugated(port.isConjugated());

					// port multiplicity
					String lowerMultiplicity = Integer.toString(port.getLower());
					String upperMultiplicity = Integer.toString(port.getUpper());
					sysMLProxyPort.setLower(lowerMultiplicity);
					sysMLProxyPort.setUpper(upperMultiplicity);

				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sysMLBlock.setProxyPorts(proxyPortsLinksArray);
		}

		if (fullPortsList.size() > 0) {
			fullPortsLinksArray = new Link[fullPortsList.size()];
			String fullPortBaseURI = baseHTTPURI + "/services/" + projectId + "/fullports/";
			int fullPortsLinksArrayIndex = 0;
			for (Port port : fullPortsList) {
				URI linkedElementURI = null;
				try {
					linkedElementURI = new URI(fullPortBaseURI + getQualifiedNameOrID(port));
					Link link = new Link(linkedElementURI);
					fullPortsLinksArray[fullPortsLinksArrayIndex] = link;
					fullPortsLinksArrayIndex++;

					SysMLFullPort sysMLFullPort = new SysMLFullPort();
					qNameOslcSysmlFullPortMap.put(magicDrawFileName + "/fullports/" + getQualifiedNameOrID(port),
							sysMLFullPort);

					// port name
					sysMLFullPort.setName(port.getName());

					// port URI
					sysMLFullPort.setAbout(URI.create(
							baseHTTPURI + "/services/" + projectId + "/fullports/" + getQualifiedNameOrID(port)));

					// port type
					if (port.getType() != null) {
						if (MDSysMLModelHandler.isSysMLElement(port.getType(), "Block")) {
							sysMLFullPort.setType(new URI(baseHTTPURI + "/services/" + projectId + "/blocks/"
									+ port.getType().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
						} else if (MDSysMLModelHandler.isSysMLElement(port.getType(), "InterfaceBlock")) {
							sysMLFullPort.setType(new URI(baseHTTPURI + "/services/" + projectId + "/interfaceblocks/"
									+ port.getType().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
						}
					}

					// isService
					sysMLFullPort.setIsService(port.isService());

					// isBehavior
					sysMLFullPort.setIsBehavior(port.isBehavior());

					// isConjugated
					sysMLFullPort.setIsConjugated(port.isConjugated());

					// port multiplicity
					String lowerMultiplicity = Integer.toString(port.getLower());
					String upperMultiplicity = Integer.toString(port.getUpper());
					sysMLFullPort.setLower(lowerMultiplicity);
					sysMLFullPort.setUpper(upperMultiplicity);

				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sysMLBlock.setFullPorts(fullPortsLinksArray);
		}

		if (portsList.size() > 0) {
			portsLinksArray = new Link[portsList.size()];
			String fullPortBaseURI = baseHTTPURI + "/services/" + projectId + "/ports/";
			int portsLinksArrayIndex = 0;
			for (Port port : portsList) {
				URI linkedElementURI = null;
				try {
					linkedElementURI = new URI(fullPortBaseURI + getQualifiedNameOrID(port));
					Link link = new Link(linkedElementURI);
					portsLinksArray[portsLinksArrayIndex] = link;
					portsLinksArrayIndex++;

					SysMLPort sysMLPort = new SysMLPort();
					qNameOslcSysmlPortMap.put(magicDrawFileName + "/ports/" + getQualifiedNameOrID(port), sysMLPort);
					mdSysmlPorts.add(port);

					// port name
					sysMLPort.setName(port.getName());

					// port URI
					String qName = port.getQualifiedName();
					sysMLPort.setAbout(URI
							.create(baseHTTPURI + "/services/" + projectId + "/ports/" + getQualifiedNameOrID(port)));

					// port type
					if (port.getType() != null) {
						if (MDSysMLModelHandler.isSysMLElement(port.getType(), "Block")) {
							sysMLPort.setType(new URI(baseHTTPURI + "/services/" + projectId + "/blocks/"
									+ port.getType().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
						} else if (MDSysMLModelHandler.isSysMLElement(port.getType(), "InterfaceBlock")) {
							sysMLPort.setType(new URI(baseHTTPURI + "/services/" + projectId + "/interfaceblocks/"
									+ port.getType().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
						}
					}

					// port owner
					if (port.getOwner() != null) {
						NamedElement portOwnerNamedElement = (NamedElement) port.getOwner();
						sysMLPort.setOwner(
								new URI(baseHTTPURI + "/services/" + projectId + "/blocks/" + portOwnerNamedElement
										.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
					}

					// isService
					sysMLPort.setIsService(port.isService());

					// isBehavior
					sysMLPort.setIsBehavior(port.isBehavior());

					// isConjugated
					sysMLPort.setIsConjugated(port.isConjugated());

					// port multiplicity
					String lowerMultiplicity = Integer.toString(port.getLower());
					String upperMultiplicity = Integer.toString(port.getUpper());
					sysMLPort.setLower(lowerMultiplicity);
					sysMLPort.setUpper(upperMultiplicity);

				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			sysMLBlock.setPorts(portsLinksArray);
		}

	}

	private static void mapSysMLProxyPorts(Class mdSysMLBlock, SysMLInterfaceBlock sysMLInterfaceBlock)
			throws MDModelLibException {
		ArrayList<Port> proxyPortsList = new ArrayList<Port>();

		for (Port port : mdSysMLBlock.getOwnedPort()) {
			if (MDSysMLModelHandler.isSysMLElement(port, "ProxyPort")) {
				proxyPortsList.add(port);
			} else {
				// flow port
			}
		}

		Link[] proxyPortsLinksArray;

		if (proxyPortsList.size() > 0) {
			proxyPortsLinksArray = new Link[proxyPortsList.size()];
			String proxyPortBaseURI = baseHTTPURI + "/services/" + projectId + "/proxyports/";
			int proxyPortsLinksArrayIndex = 0;
			for (Port port : proxyPortsList) {
				URI linkedElementURI = null;
				try {
					linkedElementURI = new URI(proxyPortBaseURI + getQualifiedNameOrID(port));
					Link link = new Link(linkedElementURI);
					proxyPortsLinksArray[proxyPortsLinksArrayIndex] = link;
					proxyPortsLinksArrayIndex++;

					SysMLProxyPort sysMLProxyPort = new SysMLProxyPort();
					qNameOslcSysmlProxyPortMap.put(magicDrawFileName + "/proxyports/" + getQualifiedNameOrID(port),
							sysMLProxyPort);

					// port name
					sysMLProxyPort.setName(port.getName());

					// port URI
					sysMLProxyPort.setAbout(URI.create(
							baseHTTPURI + "/services/" + projectId + "/proxyports/" + getQualifiedNameOrID(port)));

					// port type
					if (port.getType() != null) {
						if (MDSysMLModelHandler.isSysMLElement(port.getType(), "Block")) {
							sysMLProxyPort.setType(new URI(baseHTTPURI + "/services/" + projectId + "/blocks/"
									+ port.getType().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
						} else if (MDSysMLModelHandler.isSysMLElement(port.getType(), "InterfaceBlock")) {
							sysMLProxyPort.setType(new URI(baseHTTPURI + "/services/" + projectId + "/interfaceblocks/"
									+ port.getType().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
						}
					}

					// isService
					sysMLProxyPort.setIsService(port.isService());

					// isBehavior
					sysMLProxyPort.setIsBehavior(port.isBehavior());

					// isConjugated
					sysMLProxyPort.setIsConjugated(port.isConjugated());

				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sysMLInterfaceBlock.setProxyPorts(proxyPortsLinksArray);
		}
	}

	private static void mapSysMLConnectors(Class mdSysMLBlock, SysMLBlock sysMLBlock) throws MDModelLibException {
		Link[] connectorsLinksArray = getLinkedSysMLElements(mdSysMLBlock.getOwnedConnector(),
				baseHTTPURI + "/services/" + projectId + "/connectors/");

		if (connectorsLinksArray != null) {
			sysMLBlock.setConnectors(connectorsLinksArray);
			buffer.append("\r\n " + sysMLBlock.getName());
			buffer.append("\r\n\tblock connectors: " + connectorsLinksArray.length);
			buffer.append("\r\n " + sysMLBlock.getName());
			buffer.append("\r\n\tblock connectors: ");
			for (Link link : connectorsLinksArray) {
				buffer.append("\r\n\t\t " + link.getValue());
			}
		}

		for (Connector connector : mdSysMLBlock.getOwnedConnector()) {
			SysMLConnector sysMLConnector;
			try {
				sysMLConnector = new SysMLConnector();

				qNameOslcSysmlConnectorMap.put(magicDrawFileName + "/connectors/" + getQualifiedNameOrID(connector),
						sysMLConnector);
				mdSysmlConnectors.add(connector);
				if (!connector.getName().equals("")) {
					// connector name
					sysMLConnector.setName(connector.getName());
				}
				sysMLConnector.setAbout(URI.create(
						baseHTTPURI + "/services/" + projectId + "/connectors/" + getQualifiedNameOrID(connector)));

				// connector ends
				Link[] connectorsEndsLinksArray = getLinkedSysMLElements(connector.getEnd(),
						baseHTTPURI + "/services/" + projectId + "/connectorends/");
				sysMLConnector.setEnds(connectorsEndsLinksArray);

				// connector type
				if (connector.getType() != null) {

					// connector type is an association block
					if (MDSysMLModelHandler.isSysMLElement(connector.getType(), "Block")) {
						sysMLConnector.setType(new URI(baseHTTPURI + "/services/" + projectId + "/associationblocks/"
								+ connector.getType().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
					} else if (connector
							.getType() instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Association) {
						sysMLConnector.setType(new URI(baseHTTPURI + "/services/" + projectId + "/associations/"
								+ connector.getType().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
					}
				}

				// connector owner
				if (connector.getOwner() != null) {
					if (connector.getOwner() instanceof NamedElement) {
						NamedElement namedElement = (NamedElement) connector.getOwner();
						sysMLConnector.setOwner(new URI(baseHTTPURI + "/services/" + projectId + "/blocks/"
								+ namedElement.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
					}
				}

				// map connector ends
				mapSysMLConnectorEnds(connector, sysMLConnector);

			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void mapSysMLConnectorEnds(Connector connector, SysMLConnector sysMLConnector)
			throws MDModelLibException {

		for (ConnectorEnd connectorEnd : connector.getEnd()) {

			try {
				SysMLConnectorEnd sysMLConnectorEnd = new SysMLConnectorEnd();
				sysMLConnectorEnd.setAbout(
						URI.create(baseHTTPURI + "/services/" + projectId + "/connectorends/" + connectorEnd.getID()));
				qNameOslcSysmlConnectorEndMap.put(magicDrawFileName + "/connectorends/" + connectorEnd.getID(),
						sysMLConnectorEnd);

				ConnectableElement role = connectorEnd.getRole();

				Property definingEnd = connectorEnd.getDefiningEnd();
				Property partWithPort = connectorEnd.getPartWithPort();

				// role
				if (MDSysMLModelHandler.isSysMLElement(role, "PartProperty")) {
					sysMLConnectorEnd.setRole(new URI(baseHTTPURI + "/services/" + projectId + "/partproperties/"
							+ connectorEnd.getRole().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
				} else if (MDSysMLModelHandler.isSysMLElement(role, "ProxyPort")) {
					sysMLConnectorEnd.setRole(new URI(baseHTTPURI + "/services/" + projectId + "/proxyports/"
							+ connectorEnd.getRole().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
				} else if (MDSysMLModelHandler.isSysMLElement(role, "FullPort")) {
					sysMLConnectorEnd.setRole(new URI(baseHTTPURI + "/services/" + projectId + "/fullports/"
							+ connectorEnd.getRole().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
				} else if (role instanceof com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port) {
					sysMLConnectorEnd.setRole(new URI(baseHTTPURI + "/services/" + projectId + "/ports/"
							+ connectorEnd.getRole().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
				}

				// definingEnd
				if (definingEnd != null) {
					sysMLConnectorEnd.setDefiningEnd(
							new URI(baseHTTPURI + "/services/" + projectId + "/partproperties/" + connectorEnd
									.getDefiningEnd().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
				}

				// partWithPort
				if (partWithPort != null) {
					sysMLConnectorEnd.setPartWithPort(
							new URI(baseHTTPURI + "/services/" + projectId + "/partproperties/" + connectorEnd
									.getPartWithPort().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));
				}

			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private static void mapSysMLReferenceProperties(Class mdSysmlBlock, SysMLBlock sysMLBlock)
			throws MDModelLibException {

		Link[] blockReferencesLinksArray = getLinkedStereotypedSysMLElements(mdSysmlBlock.getOwnedAttribute(),
				"ReferenceProperty", baseHTTPURI + "/services/" + projectId + "/referenceproperties/");

		if (blockReferencesLinksArray != null) {
			sysMLBlock.setReferenceProperties(blockReferencesLinksArray);

			buffer.append("\r\n " + sysMLBlock.getName());
			buffer.append("\r\n\tblock reference properties: " + blockReferencesLinksArray.length);
			buffer.append("\r\n " + sysMLBlock.getName());
			buffer.append("\r\n\tblock reference properties: ");
			for (Link link : blockReferencesLinksArray) {
				buffer.append("\r\n\t\t " + link.getValue());
			}
		}

		for (Property property : mdSysmlBlock.getOwnedAttribute()) {

			if (property.getAppliedStereotypeInstance() != null) {
				InstanceSpecification stereotypeInstance = property.getAppliedStereotypeInstance();
				if (stereotypeInstance.getClassifier().get(0).getName().contains("ReferenceProperty")) {
					SysMLReferenceProperty sysmlReferenceProperty;
					try {
						sysmlReferenceProperty = new SysMLReferenceProperty();
						qNameOslcSysmlReferencePropertyMap.put(
								magicDrawFileName + "/referenceproperties/"
										+ property.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_"),
								sysmlReferenceProperty);

						// referenceProperty name
						sysmlReferenceProperty.setName(property.getName());

						String qName = property.getQualifiedName();
						sysmlReferenceProperty.setAbout(URI.create(baseHTTPURI + "/services/" + projectId
								+ "/referenceproperties/" + qName.replaceAll("\\n", "-").replaceAll(" ", "_")));

						// referenceProperty type
						sysmlReferenceProperty.setType(new URI(baseHTTPURI + "/services/" + projectId + "/blocks/"
								+ property.getType().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));

						// referenceProperty multiplicity
						String lowerMultiplicity = Integer.toString(property.getLower());
						String upperMultiplicity = Integer.toString(property.getUpper());
						sysmlReferenceProperty.setLower(lowerMultiplicity);
						sysmlReferenceProperty.setUpper(upperMultiplicity);

						// referenceProperty association attribute
						if (property.getAssociation() != null) {
							Association mdSysMAssociation = property.getAssociation();
							URI linkedElementURI = null;
							String baseURI = baseHTTPURI + "/services/" + projectId + "/unknown/";
							// check if property has an associationBlock as
							// association

							if (MDSysMLModelHandler.isSysMLElement(property.getAssociation(), "Block")) {
								// if (!mdSysmlAssociationBlocks
								// .contains((com.nomagic.uml2.ext.magicdraw.classes.mdassociationclasses.AssociationClass)
								// property.getAssociation())) {
								// mdSysmlAssociationBlocks.add((com.nomagic.uml2.ext.magicdraw.classes.mdassociationclasses.AssociationClass)
								// property
								// .getAssociation());
								// }
								baseURI = baseHTTPURI + "/services/" + projectId + "/associationblocks/";
							} else if (property
									.getAssociation() instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Association) {
								baseURI = baseHTTPURI + "/services/" + projectId + "/associations/";
							}
							linkedElementURI = new URI(baseURI
									+ mdSysMAssociation.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_"));
							sysmlReferenceProperty.setAssociation(linkedElementURI);
						}

					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

	}

	private static void mapSysMLFlowProperties(Class mdSysmlBlock, SysMLBlock sysMLBlock) {

		Link[] flowPropertiesLinksArray = getLinkedStereotypedSysMLElements(mdSysmlBlock.getOwnedAttribute(),
				"FlowProperty", baseHTTPURI + "/services/" + projectId + "/flowproperties/");

		if (flowPropertiesLinksArray != null) {
			sysMLBlock.setFlowProperties(flowPropertiesLinksArray);

			buffer.append("\r\n " + sysMLBlock.getName());
			buffer.append("\r\n\tblock reference properties: " + flowPropertiesLinksArray.length);
			buffer.append("\r\n " + sysMLBlock.getName());
			buffer.append("\r\n\tblock reference properties: ");
			for (Link link : flowPropertiesLinksArray) {
				buffer.append("\r\n\t\t " + link.getValue());
			}
		}

		for (Property property : mdSysmlBlock.getOwnedAttribute()) {

			if (property.getAppliedStereotypeInstance() != null) {
				InstanceSpecification stereotypeInstance = property.getAppliedStereotypeInstance();
				if (stereotypeInstance.getClassifier().get(0).getName().contains("FlowProperty")) {
					SysMLFlowProperty sysmlFlowProperty;
					try {
						sysmlFlowProperty = new SysMLFlowProperty();
						qNameOslcSysmlFlowPropertyMap.put(
								magicDrawFileName + "/flowproperties/"
										+ property.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_"),
								sysmlFlowProperty);

						// referenceProperty name
						sysmlFlowProperty.setName(property.getName());

						String qName = property.getQualifiedName();
						sysmlFlowProperty.setAbout(URI.create(baseHTTPURI + "/services/" + projectId
								+ "/flowproperties/" + qName.replaceAll("\\n", "-").replaceAll(" ", "_")));

						// referenceProperty type
						sysmlFlowProperty.setType(new URI(baseHTTPURI + "/services/" + projectId + "/blocks/"
								+ property.getType().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));

						// referenceProperty multiplicity
						String lowerMultiplicity = Integer.toString(property.getLower());
						String upperMultiplicity = Integer.toString(property.getUpper());
						sysmlFlowProperty.setLower(lowerMultiplicity);
						sysmlFlowProperty.setUpper(upperMultiplicity);

						// direction
						Object directionObject = StereotypesHelper.getStereotypePropertyFirst(property,
								(Stereotype) property.getAppliedStereotypeInstance().getClassifier().get(0),
								"direction");
						if (directionObject instanceof EnumerationLiteral) {
							EnumerationLiteral enumLit = (EnumerationLiteral) directionObject;
							String enumLitName = enumLit.getName();
							if (enumLitName.equals("in")) {
								// sysmlFlowProperty
								// .setDirection(SysMLFlowDirection.IN);
								sysmlFlowProperty.setDirection("in");
							} else if (enumLitName.equals("out")) {
								// sysmlFlowProperty
								// .setDirection(SysMLFlowDirection.OUT);
								sysmlFlowProperty.setDirection("out");
							}

						}

					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

	}

	private static Link[] getLinkedSysMLElements(Collection<? extends Element> elementCollection,
			String linkedElementBaseURI) {

		// counting the number of links
		int linksCount = elementCollection.size();

		// creating linksArray
		Link[] linksArray = null;
		if (linksCount > 0) {
			linksArray = new Link[linksCount];
		}

		// populating linksArray
		int linksArrayIndex = 0;
		for (Element element : elementCollection) {
			try {
				URI linkedElementURI = null;
				linkedElementURI = new URI(linkedElementBaseURI + getQualifiedNameOrID(element));
				Link link = new Link(linkedElementURI);
				linksArray[linksArrayIndex] = link;
				linksArrayIndex++;
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return linksArray;
	}

	private static Link[] getLinkedStereotypedSysMLElements(Collection<? extends NamedElement> namedElementCollection,
			String stereotypeName, String linkedElementBaseURI) {

		// counting the number of links
		int linksCount = 0;
		for (NamedElement namedElement : namedElementCollection) {
			if (namedElement.getAppliedStereotypeInstance() != null) {
				InstanceSpecification stereotypeInstance = namedElement.getAppliedStereotypeInstance();
				if (stereotypeInstance.getClassifier().get(0).getName().equals(stereotypeName)) {
					linksCount++;
				}
			}
		}

		// creating linksArray
		Link[] linksArray = null;
		if (linksCount > 0) {
			linksArray = new Link[linksCount];
		}

		// populating linksArray
		int linksArrayIndex = 0;
		for (NamedElement namedElement : namedElementCollection) {
			if (namedElement.getAppliedStereotypeInstance() != null) {
				InstanceSpecification stereotypeInstance = namedElement.getAppliedStereotypeInstance();
				if (stereotypeInstance.getClassifier().get(0).getName().equals(stereotypeName)) {
					try {
						URI linkedElementURI = null;
						if (stereotypeName.equals("Requirement") & namedElement instanceof Class) {
							String linkedElementID = (String) StereotypesHelper.getStereotypePropertyFirst(namedElement,
									StereotypesHelper.getFirstVisibleStereotype(namedElement), "Id");
							linkedElementURI = new URI(linkedElementBaseURI + linkedElementID);
						} else {
							linkedElementURI = new URI(linkedElementBaseURI + getQualifiedNameOrID(namedElement));
						}

						Link link = new Link(linkedElementURI);
						linksArray[linksArrayIndex] = link;
						linksArrayIndex++;
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return linksArray;

	}

	private static URI getDirectedLinkSysMLElement(boolean isElementSource, Element element, String relationshipType)
			throws MDModelLibException {

		Collection<DirectedRelationship> directedRelationships;
		if (isElementSource) {
			directedRelationships = element.get_directedRelationshipOfSource();
		} else {
			directedRelationships = element.get_directedRelationshipOfTarget();
		}

		// getting relationships of that specific type
		Collection<DirectedRelationship> relationshipsOfType = new ArrayList<DirectedRelationship>();
		for (DirectedRelationship directedRelationship : directedRelationships) {
			try {
				String directedRelationshipType = directedRelationship.getAppliedStereotypeInstance().getClassifier()
						.get(0).getName();
				if (directedRelationshipType.equals(relationshipType)) {
					relationshipsOfType.add(directedRelationship);
				}
			} catch (Exception e) {
			}
		}

		URI linkedElementURI = null;
		for (DirectedRelationship directedRelationship : relationshipsOfType) {
			Collection<Element> linkedElements;
			if (isElementSource) {
				linkedElements = directedRelationship.getTarget();
			} else {
				linkedElements = directedRelationship.getSource();
			}
			for (Element linkedElement : linkedElements) {
				if (linkedElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement) {
					NamedElement linkedNamedElement = (NamedElement) linkedElement;
					String linkedElementBaseURI = baseHTTPURI + "/services/" + projectId + "/unknown/";
					String linkedElementID = null;
					boolean isLinkedElementRequirement = false;
					if (MDSysMLModelHandler.isSysMLElement(linkedNamedElement, "Block")) {
						linkedElementBaseURI = baseHTTPURI + "/services/" + projectId + "/blocks/";
					} else if (MDSysMLModelHandler.isSysMLElement(linkedNamedElement, "Requirement")) {
						linkedElementID = (String) StereotypesHelper.getStereotypePropertyFirst(linkedNamedElement,
								StereotypesHelper.getFirstVisibleStereotype(linkedNamedElement), "Id");
						isLinkedElementRequirement = true;
						linkedElementBaseURI = baseHTTPURI + "/services/" + projectId + "/requirements/";
					} else if (linkedNamedElement instanceof com.nomagic.uml2.ext.magicdraw.mdusecases.UseCase) {
						linkedElementBaseURI = baseHTTPURI + "/services/" + projectId + "/usecases/";
					}
					String linkedElementQName = null;
					if (!isLinkedElementRequirement) {
						linkedElementQName = linkedNamedElement.getQualifiedName().replaceAll("\\n", "-")
								.replaceAll(" ", "_");
					} else {
						linkedElementQName = linkedElementID;
					}

					try {
						linkedElementURI = new URI(linkedElementBaseURI + linkedElementQName);
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return linkedElementURI;
	}

	private static Link[] getDirectedLinksOfSysMLElement(boolean isElementSource, Element element,
			String relationshipType) throws MDModelLibException {

		Collection<DirectedRelationship> directedRelationships;
		if (isElementSource) {
			directedRelationships = element.get_directedRelationshipOfSource();
		} else {
			directedRelationships = element.get_directedRelationshipOfTarget();
		}

		// counting the number of relationships of that specific type
		Collection<DirectedRelationship> relationshipsOfType = new ArrayList<DirectedRelationship>();
		for (DirectedRelationship directedRelationship : directedRelationships) {
			try {
				String directedRelationshipType = directedRelationship.getAppliedStereotypeInstance().getClassifier()
						.get(0).getName();
				if (directedRelationshipType.equals(relationshipType)) {
					relationshipsOfType.add(directedRelationship);
				}
			} catch (Exception e) {
			}
		}

		// creating the links array
		Link[] linksArray = null;
		if (relationshipsOfType.size() > 0) {
			linksArray = new Link[relationshipsOfType.size()];
		}

		// populating the links array
		int linksArrayIndex = 0;
		for (DirectedRelationship directedRelationship : relationshipsOfType) {
			Collection<Element> linkedElements;
			if (isElementSource) {
				linkedElements = directedRelationship.getTarget();
			} else {
				linkedElements = directedRelationship.getSource();
			}
			for (Element linkedElement : linkedElements) {
				if (linkedElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement) {
					NamedElement linkedNamedElement = (NamedElement) linkedElement;
					String linkedElementBaseURI = baseHTTPURI + "/services/" + projectId + "/unknown/";
					String linkedElementID = null;
					boolean isLinkedElementRequirement = false;
					if (MDSysMLModelHandler.isSysMLElement(linkedNamedElement, "Block")) {
						linkedElementBaseURI = baseHTTPURI + "/services/" + projectId + "/blocks/";
					} else if (MDSysMLModelHandler.isSysMLElement(linkedNamedElement, "Requirement")) {
						linkedElementID = (String) StereotypesHelper.getStereotypePropertyFirst(linkedNamedElement,
								StereotypesHelper.getFirstVisibleStereotype(linkedNamedElement), "Id");
						isLinkedElementRequirement = true;
						linkedElementBaseURI = baseHTTPURI + "/services/" + projectId + "/requirements/";
					} else if (linkedNamedElement instanceof com.nomagic.uml2.ext.magicdraw.mdusecases.UseCase) {
						linkedElementBaseURI = baseHTTPURI + "/services/" + projectId + "/usecases/";
					}
					String linkedElementQName = null;
//					if (!isLinkedElementRequirement) {
//						linkedElementQName = linkedNamedElement.getQualifiedName().replaceAll("\\n", "-")
//								.replaceAll(" ", "_");
//					} else {
//						linkedElementQName = linkedElementID;
//					}
					
					
						linkedElementQName = linkedNamedElement.getQualifiedName().replaceAll("\\n", "-")
								.replaceAll(" ", "_");
					
					
					URI linkedElementURI = null;
					try {
						linkedElementURI = new URI(linkedElementBaseURI + linkedElementQName);
						Link link = new Link(linkedElementURI);
						linksArray[linksArrayIndex] = link;
						linksArrayIndex++;
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return linksArray;
	}

	private static void mapSysMLPartProperties(Class mdSysmlBlock, SysMLBlock sysMLBlock) {

		Link[] blockPartsLinksArray = getLinkedStereotypedSysMLElements(mdSysmlBlock.getOwnedAttribute(),
				"PartProperty", baseHTTPURI + "/services/" + projectId + "/partproperties/");

		if (blockPartsLinksArray != null) {
			sysMLBlock.setPartProperties(blockPartsLinksArray);

			buffer.append("\r\n " + sysMLBlock.getName());
			buffer.append("\r\n\tblockParts: " + blockPartsLinksArray.length);
			buffer.append("\r\n " + sysMLBlock.getName());
			buffer.append("\r\n\tblockParts: ");
			for (Link link : blockPartsLinksArray) {
				buffer.append("\r\n\t\t " + link.getValue());
			}
		}

		for (Property property : mdSysmlBlock.getOwnedAttribute()) {

			if (property.getAppliedStereotypeInstance() != null) {
				InstanceSpecification stereotypeInstance = property.getAppliedStereotypeInstance();
				if (stereotypeInstance.getClassifier().get(0).getName().contains("PartProperty")) {
					SysMLPartProperty sysmlPartProperty;
					try {
						sysmlPartProperty = new SysMLPartProperty();
						qNameOslcSysmlPartPropertyMap.put(
								magicDrawFileName + "/partproperties/" + getQualifiedNameOrID(property),
								sysmlPartProperty);
						mdSysmlPartProperties.add(property);

						// partProperty name
						sysmlPartProperty.setName(property.getName());

						String qName = property.getQualifiedName();
						sysmlPartProperty.setAbout(URI.create(baseHTTPURI + "/services/" + projectId
								+ "/partproperties/" + getQualifiedNameOrID(property)));

						// partProperty type
						sysmlPartProperty.setType(new URI(baseHTTPURI + "/services/" + projectId + "/blocks/"
								+ property.getType().getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));

						// partProperty owner
						NamedElement partPropertyOwnerNamedElement = (NamedElement) property.getOwner();
						sysmlPartProperty.setOwner(new URI(
								baseHTTPURI + "/services/" + projectId + "/blocks/" + partPropertyOwnerNamedElement
										.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")));

						// partProperty multiplicity
						String lowerMultiplicity = Integer.toString(property.getLower());
						String upperMultiplicity = Integer.toString(property.getUpper());
						sysmlPartProperty.setLower(lowerMultiplicity);
						sysmlPartProperty.setUpper(upperMultiplicity);

					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

	}

	private static void mapSysMLRequirements() {
		for (Class mdSysMLRequirement : mdSysmlRequirements) {
//			String id = (String) StereotypesHelper.getStereotypePropertyFirst(mdSysMLRequirement,
//					StereotypesHelper.getFirstVisibleStereotype(mdSysMLRequirement), "Id");
			
			// using qualified name as id
			String id = getQualifiedNameOrID(mdSysMLRequirement);
			
			
			if (id != null) {
				idMdSysmlRequirementMap.put(id, mdSysMLRequirement);
				// qNameMdSysmlRequirementMap.put(
				// mdSysMLRequirement.getQualifiedName()
				// .replaceAll("\\n", "-").replaceAll(" ", "_"),
				// mdSysMLRequirement);

				SysMLRequirement sysMLRequirement;
				try {
					sysMLRequirement = new SysMLRequirement();

					// SysML Requirement id attribute
					sysMLRequirement.setIdentifier(id);
					idOslcSysmlRequirementMap.put(magicDrawFileName + "/requirements/" + id, sysMLRequirement);

					sysMLRequirement
							.setAbout(URI.create(baseHTTPURI + "/services/" + projectId + "/requirements/" + id));

					// qNameOslcSysmlRequirementMap.put(mdSysMLRequirement
					// .getQualifiedName().replaceAll("\\n", "-")
					// .replaceAll(" ", "_"), sysMLRequirement);
					buffer.append("\r\nSysML Requirement with ID: " + sysMLRequirement.getIdentifier());

					// SysML Requirement Name attribute
					String name = mdSysMLRequirement.getName();
					if (name != null) {
						sysMLRequirement.setTitle(name);
						buffer.append("\r\n\tTitle: " + sysMLRequirement.getTitle());
					}

					// SysML Requirement Text attribute
					String text = (String) StereotypesHelper.getStereotypePropertyFirst(mdSysMLRequirement,
							StereotypesHelper.getFirstVisibleStereotype(mdSysMLRequirement), "Text");
					if (text != null) {
						sysMLRequirement.setDescription(text);
						buffer.append("\r\n\tDescription: " + sysMLRequirement.getDescription());
					}

					// SysML Requirement hyperlink attribute
					Stereotype hyperlinkOwnerStereotype = StereotypesHelper
							.getStereotype(Project.getProject(mdSysMLRequirement), "HyperlinkOwner");
					List textValues = StereotypesHelper.getStereotypePropertyValue(mdSysMLRequirement,
							hyperlinkOwnerStereotype, "hyperlinkText");
					if (textValues.size() > 0) {
						String hyperlinkText = (String) textValues.get(0);
						sysMLRequirement.setHyperlink(hyperlinkText);
					}
					// for (int i = textValues.size() - 1; i >= 0; --i) {
					// String link = (String) textValues.get(i);
					// }

				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public static SysMLBlockDiagram getBlockDiagramByQualifiedName(String qualifiedName) throws URISyntaxException {
		SysMLBlockDiagram sysMLBlockDiagram = qNameOslcSysmlBlockDiagramMap.get(qualifiedName);
		return sysMLBlockDiagram;
	}

	public static List<SysMLBlockDiagram> getBlockDiagrams(String projectName) {
		List<SysMLBlockDiagram> sysMLBlocks = new ArrayList<SysMLBlockDiagram>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlBlockDiagramMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/blockdiagrams/")) {
				sysMLBlocks.add(qNameOslcSysmlBlockDiagramMap.get(qNameOslcSysmlElement));
			}
		}
		return sysMLBlocks;
	}

	public static SysMLInternalBlockDiagram getInternalBlockDiagramByQualifiedName(String qualifiedName)
			throws URISyntaxException {
		SysMLInternalBlockDiagram sysMLInternalBlockDiagram = qNameOslcSysmlInternalBlockDiagramMap.get(qualifiedName);
		return sysMLInternalBlockDiagram;
	}

	public static List<SysMLInternalBlockDiagram> getInternalBlockDiagrams(String projectName) {
		List<SysMLInternalBlockDiagram> sysMLBlocks = new ArrayList<SysMLInternalBlockDiagram>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlInternalBlockDiagramMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/internalblockdiagrams/")) {
				sysMLBlocks.add(qNameOslcSysmlInternalBlockDiagramMap.get(qNameOslcSysmlElement));
			}
		}
		return sysMLBlocks;
	}

	public static SysMLRequirement getRequirementByID(String qualifiedName) throws URISyntaxException {
		SysMLRequirement sysMLRequirement = idOslcSysmlRequirementMap.get(qualifiedName);

		return sysMLRequirement;
	}

	public static List<SysMLRequirement> getRequirements(String projectName) {
		List<SysMLRequirement> sysMLRequirements = new ArrayList<SysMLRequirement>();
		for (String id : idOslcSysmlRequirementMap.keySet()) {
			if(id.startsWith(projectName + "/requirements/")){
				sysMLRequirements.add(idOslcSysmlRequirementMap.get(id));
			}		
		}
		return sysMLRequirements;
	}

	public static edu.gatech.mbsec.adapter.magicdraw.resources.SysMLBlock getBlockByQualifiedName(String qualifiedName) {
		SysMLBlock sysMLBlock = qNameOslcSysmlBlockMap.get(qualifiedName);
		return sysMLBlock;
	}

	public static List<SysMLBlock> getBlocks(String projectName) {
		List<SysMLBlock> sysMLBlocks = new ArrayList<SysMLBlock>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlBlockMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/blocks/")) {
				sysMLBlocks.add(qNameOslcSysmlBlockMap.get(qNameOslcSysmlElement));
			}
		}
		return sysMLBlocks;
	}

	static Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package> getAllSysMLPackages(
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.PackageableElement packageableElement) {
		Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package> sysmlPackages = new ArrayList<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package>();

		if (packageableElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) {
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package package_ = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) packageableElement;
			if (!predefinedMagicDrawSysMLPackageNames.contains(package_.getName())) {
				if (!(packageableElement instanceof Model)) {
					sysmlPackages.add(package_);
				}
				// add nested packages
				for (PackageableElement nestedPackageableElement : package_.getPackagedElement()) {
					if (nestedPackageableElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) {
						com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package nestedPackage = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) nestedPackageableElement;
						if (!predefinedMagicDrawSysMLPackageNames.contains(nestedPackage.getName())) {
							sysmlPackages.addAll(getAllSysMLPackages(nestedPackage));
						}
					}
				}
			}
		}

		return sysmlPackages;
	}

	static Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class> getAllSysMLBlocks(
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.PackageableElement packageableElement)
					throws MDModelLibException {
		Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class> sysmlBlocks = new ArrayList<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class>();
		if (packageableElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) {
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package package_ = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) packageableElement;
			for (PackageableElement nestedPackageableElement : package_.getPackagedElement()) {
				if (MDSysMLModelHandler.isSysMLElement(nestedPackageableElement, "Block")
						| MDSysMLModelHandler.isSysMLElement(nestedPackageableElement, "System")) {
					Class magicDrawSysMLBlock = (Class) nestedPackageableElement;
					sysmlBlocks.add(magicDrawSysMLBlock);
					sysmlBlocks.addAll(getAllSysMLBlocks(magicDrawSysMLBlock));
				} else
					if (nestedPackageableElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) {
					com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package nestedPackage = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) nestedPackageableElement;
					if (!predefinedMagicDrawSysMLPackageNames.contains(nestedPackage.getName())) {
						sysmlBlocks.addAll(getAllSysMLBlocks(nestedPackage));
					}
				}
			}
		} else if (packageableElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class class_ = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) packageableElement;
			for (Classifier nestedClassifier : class_.getNestedClassifier()) {
				if (MDSysMLModelHandler.isSysMLElement(nestedClassifier, "Block")
						| MDSysMLModelHandler.isSysMLElement(nestedClassifier, "System")) {
					sysmlBlocks.add((com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) nestedClassifier);
				}
				sysmlBlocks.addAll(getAllSysMLBlocks(nestedClassifier));
			}
		}
		return sysmlBlocks;
	}

	static Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class> getAllSysMLInterfaceBlocks(
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.PackageableElement packageableElement)
					throws MDModelLibException {
		Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class> sysmlBlocks = new ArrayList<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class>();
		if (packageableElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) {
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package package_ = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) packageableElement;
			for (PackageableElement nestedPackageableElement : package_.getPackagedElement()) {
				if (MDSysMLModelHandler.isSysMLElement(nestedPackageableElement, "InterfaceBlock")) {
					Class magicDrawSysMLBlock = (Class) nestedPackageableElement;
					sysmlBlocks.add(magicDrawSysMLBlock);
					sysmlBlocks.addAll(getAllSysMLInterfaceBlocks(magicDrawSysMLBlock));
				} else
					if (nestedPackageableElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) {
					com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package nestedPackage = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) nestedPackageableElement;
					if (!predefinedMagicDrawSysMLPackageNames.contains(nestedPackage.getName())) {
						sysmlBlocks.addAll(getAllSysMLInterfaceBlocks(nestedPackage));
					}
				}
			}
		} else if (packageableElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class class_ = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) packageableElement;
			for (Classifier nestedClassifier : class_.getNestedClassifier()) {
				if (MDSysMLModelHandler.isSysMLElement(nestedClassifier, "InterfaceBlock")) {
					sysmlBlocks.add((com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) nestedClassifier);
				}
				sysmlBlocks.addAll(getAllSysMLInterfaceBlocks(nestedClassifier));
			}
		}
		return sysmlBlocks;
	}

	static Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class> getAllSysMLRequirements(
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.PackageableElement packageableElement)
					throws MDModelLibException {
		Collection<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class> sysmlRequirements = new ArrayList<com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class>();
		if (packageableElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) {
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package package_ = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) packageableElement;
			for (PackageableElement nestedPackageableElement : package_.getPackagedElement()) {
				if (MDSysMLModelHandler.isSysMLElement(nestedPackageableElement, "Requirement")) {
					com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class sysMLRequirement = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) nestedPackageableElement;
					sysmlRequirements.add(sysMLRequirement);
					sysmlRequirements.addAll(getAllSysMLRequirements(sysMLRequirement));
				} else
					if (nestedPackageableElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) {
					com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package nestedPackage = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package) nestedPackageableElement;
					if (!predefinedMagicDrawSysMLPackageNames.contains(nestedPackage.getName())) {
						sysmlRequirements.addAll(getAllSysMLRequirements(nestedPackage));
					}
				}
			}
		} else if (packageableElement instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class class_ = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) packageableElement;
			for (Classifier nestedClassifier : class_.getNestedClassifier()) {
				if (MDSysMLModelHandler.isSysMLElement(nestedClassifier, "Requirement")) {
					sysmlRequirements.add((com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) nestedClassifier);
				}
				sysmlRequirements.addAll(getAllSysMLRequirements(nestedClassifier));
			}
		}
		return sysmlRequirements;
	}

	public static SysMLPartProperty getPartPropertyByQualifiedName(String propertyQualifiedName) {
		SysMLPartProperty sysMLPartProperty = qNameOslcSysmlPartPropertyMap.get(propertyQualifiedName);
		return sysMLPartProperty;
	}

	public static List<SysMLPartProperty> getPartProperties(String projectName) {
		List<SysMLPartProperty> elements = new ArrayList<SysMLPartProperty>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlPartPropertyMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/partproperties/")) {
				elements.add(qNameOslcSysmlPartPropertyMap.get(qNameOslcSysmlElement));
			}
		}
		return elements;
	}

	public static SysMLReferenceProperty getReferencePropertyByQualifiedName(String propertyQualifiedName) {
		SysMLReferenceProperty sysMLReferenceProperty = qNameOslcSysmlReferencePropertyMap.get(propertyQualifiedName);
		return sysMLReferenceProperty;
	}

	public static List<SysMLReferenceProperty> getReferenceProperties(String projectName) {
		List<SysMLReferenceProperty> elements = new ArrayList<SysMLReferenceProperty>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlReferencePropertyMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/referenceproperties/")) {
				elements.add(qNameOslcSysmlReferencePropertyMap.get(qNameOslcSysmlElement));
			}
		}
		return elements;
	}

	public static SysMLValueProperty getValuePropertyByQualifiedName(String propertyQualifiedName) {
		SysMLValueProperty sysMLValueProperty = qNameOslcSysmlValuePropertyMap.get(propertyQualifiedName);
		return sysMLValueProperty;
	}

	public static List<SysMLValueProperty> getValueProperties(String projectName) {
		List<SysMLValueProperty> elements = new ArrayList<SysMLValueProperty>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlValuePropertyMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/valueproperties/")) {
				elements.add(qNameOslcSysmlValuePropertyMap.get(qNameOslcSysmlElement));
			}
		}
		return elements;
	}

	public static SysMLValueType getValueTypeByQualifiedName(String propertyQualifiedName) {
		SysMLValueType sysMLValueType = qNameOslcSysmlValueTypeMap.get(propertyQualifiedName);
		return sysMLValueType;
	}

	public static List<SysMLValueType> getValueTypes(String projectName) {
		List<SysMLValueType> elements = new ArrayList<SysMLValueType>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlValueTypeMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/valuetypes/")) {
				elements.add(qNameOslcSysmlValueTypeMap.get(qNameOslcSysmlElement));
			}
		}
		return elements;
	}

	public static SysMLFlowProperty getFlowPropertyByQualifiedName(String propertyQualifiedName) {
		SysMLFlowProperty sysMLFlowProperty = qNameOslcSysmlFlowPropertyMap.get(propertyQualifiedName);
		return sysMLFlowProperty;
	}

	public static List<SysMLFlowProperty> getFlowProperties(String projectName) {
		List<SysMLFlowProperty> elements = new ArrayList<SysMLFlowProperty>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlFlowPropertyMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/flowproperties/")) {
				elements.add(qNameOslcSysmlFlowPropertyMap.get(qNameOslcSysmlElement));
			}
		}
		return elements;
	}

	public static SysMLInterfaceBlock getInterfaceBlockByQualifiedName(String propertyQualifiedName) {
		SysMLInterfaceBlock sysMLInterfaceBlock = qNameOslcSysmlInterfaceBlockMap.get(propertyQualifiedName);
		return sysMLInterfaceBlock;
	}

	public static List<SysMLInterfaceBlock> getInterfaceBlocks(String projectName) {
		List<SysMLInterfaceBlock> elements = new ArrayList<SysMLInterfaceBlock>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlInterfaceBlockMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/interfaceblocks/")) {
				elements.add(qNameOslcSysmlInterfaceBlockMap.get(qNameOslcSysmlElement));
			}
		}
		return elements;
	}

	public static SysMLItemFlow getItemFlowByQualifiedName(String propertyQualifiedName) {
		SysMLItemFlow sysMLItemFlow = qNameOslcSysmlItemFlowMap.get(propertyQualifiedName);
		return sysMLItemFlow;
	}

	public static List<SysMLItemFlow> getItemFlows() {
		List<SysMLItemFlow> sysMLItemFlows = new ArrayList<SysMLItemFlow>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlItemFlowMap.keySet()) {
			sysMLItemFlows.add(qNameOslcSysmlItemFlowMap.get(qNameOslcSysmlElement));
		}
		return sysMLItemFlows;
	}

	public static SysMLPort getPortByQualifiedName(String propertyQualifiedName) {
		SysMLPort sysMLPort = qNameOslcSysmlPortMap.get(propertyQualifiedName);
		return sysMLPort;
	}

	public static List<SysMLPort> getPorts(String projectName) {
		List<SysMLPort> elements = new ArrayList<SysMLPort>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlPortMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/ports/")) {
				elements.add(qNameOslcSysmlPortMap.get(qNameOslcSysmlElement));
			}
		}
		return elements;
	}

	public static SysMLProxyPort getProxyPortByQualifiedName(String propertyQualifiedName) {
		SysMLProxyPort sysMLProxyPort = qNameOslcSysmlProxyPortMap.get(propertyQualifiedName);
		return sysMLProxyPort;
	}

	public static List<SysMLProxyPort> getProxyPorts(String projectName) {
		List<SysMLProxyPort> elements = new ArrayList<SysMLProxyPort>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlProxyPortMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/proxyports/")) {
				elements.add(qNameOslcSysmlProxyPortMap.get(qNameOslcSysmlElement));
			}
		}
		return elements;
	}

	public static SysMLFullPort getFullPortByQualifiedName(String propertyQualifiedName) {
		SysMLFullPort sysMLFullPort = qNameOslcSysmlFullPortMap.get(propertyQualifiedName);
		return sysMLFullPort;
	}

	public static List<SysMLFullPort> getFullPorts(String projectName) {
		List<SysMLFullPort> elements = new ArrayList<SysMLFullPort>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlFullPortMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/fullports/")) {
				elements.add(qNameOslcSysmlFullPortMap.get(qNameOslcSysmlElement));
			}
		}
		return elements;
	}

	public static SysMLConnector getConnectorByQualifiedName(String propertyQualifiedName) {
		SysMLConnector sysMLConnector = qNameOslcSysmlConnectorMap.get(propertyQualifiedName);
		return sysMLConnector;
	}

	public static List<SysMLConnector> getConnectors(String projectName) {
		List<SysMLConnector> elements = new ArrayList<SysMLConnector>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlConnectorMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/connectors/")) {
				elements.add(qNameOslcSysmlConnectorMap.get(qNameOslcSysmlElement));
			}
		}
		return elements;
	}

	public static SysMLConnectorEnd getConnectorEndByQualifiedName(String propertyQualifiedName) {
		// if(qNameOslcSysmlConnectorEndMap.keySet().size() > 0){
		// String key = (String)
		// qNameOslcSysmlConnectorEndMap.keySet().toArray()[0];
		// if(key.startsWith("/oslc4jmagicdraw/services/")){
		// propertyQualifiedName = "/oslc4jmagicdraw/services/" + projectId +
		// "/connectorends/" + propertyQualifiedName;
		// }
		// }
		SysMLConnectorEnd sysMLConnectorEnd = qNameOslcSysmlConnectorEndMap.get(propertyQualifiedName);
		return sysMLConnectorEnd;
	}

	public static List<SysMLConnectorEnd> getConnectorEnds(String projectName) {
		List<SysMLConnectorEnd> elements = new ArrayList<SysMLConnectorEnd>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlConnectorEndMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/connectorends/")) {
				elements.add(qNameOslcSysmlConnectorEndMap.get(qNameOslcSysmlElement));
			}
		}
		return elements;
	}

	public static SysMLModel getModelByName(String modelName) {
		SysMLModel sysMLModel = oslcSysmlModelMap.get(modelName);
		return sysMLModel;
	}

	public static List<SysMLModel> getModels() {
		List<SysMLModel> sysMLModels = new ArrayList<SysMLModel>();
		for (String id : oslcSysmlModelMap.keySet()) {
			sysMLModels.add(oslcSysmlModelMap.get(id));
		}
		return sysMLModels;
	}

	public static SysMLPackage getPackageByQualifiedName(String qualifiedName) {
		SysMLPackage sysMLPackage = qNameOslcSysmlPackageMap.get(qualifiedName);
		return sysMLPackage;
	}

	public static List<SysMLPackage> getPackages(String projectName) {
		List<SysMLPackage> elements = new ArrayList<SysMLPackage>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlPackageMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/packages/")) {
				elements.add(qNameOslcSysmlPackageMap.get(qNameOslcSysmlElement));
			}
		}
		return elements;
	}

	public static SysMLAssociationBlock getAssociationBlockByQualifiedName(String blockQualifiedName) {
		SysMLAssociationBlock sysMLAssociationBlock = qNameOslcSysmlAssociationBlockMap.get(blockQualifiedName);
		return sysMLAssociationBlock;
	}

	public static List<SysMLAssociationBlock> getAssociationBlocks(String projectName) {
		List<SysMLAssociationBlock> elements = new ArrayList<SysMLAssociationBlock>();
		for (String qNameOslcSysmlElement : qNameOslcSysmlAssociationBlockMap.keySet()) {
			if (qNameOslcSysmlElement.startsWith(projectName + "/associationblocks/")) {
				elements.add(qNameOslcSysmlAssociationBlockMap.get(qNameOslcSysmlElement));
			}
		}
		return elements;
	}

	public static void createSysMLPackage(SysMLPackage sysmlPackage, String projectId2) {
		ProjectsManager projectsManager = magicdrawApplication.getProjectsManager();
		final File sysmlfile = new File(magicdrawModelsDirectory + projectId2 + ".mdzip");
		ProjectDescriptor projectDescriptor = ProjectDescriptorsFactory.createProjectDescriptor(sysmlfile.toURI());
		projectsManager.loadProject(projectDescriptor, true);
		if (!SessionManager.getInstance().isSessionCreated()) {
			SessionManager.getInstance().createSession("MagicDraw OSLC Session for projectId" + projectId + sessionID);
			sessionID++;
		}
		Project project = magicdrawApplication.getProject();
		ElementsFactory elementsFactory = magicdrawApplication.getProject().getElementsFactory();

		String newElementQualifiedName = getQualifiedNameFromURI(sysmlPackage.getAbout());
		boolean elementExists = false;
		for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package mdPackage : projectIdMDSysmlPackagesMap
				.get(projectId2)) {
			if (mdPackage.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
					.equals(newElementQualifiedName)) {
				elementExists = true;
				break;
			}
		}
		// if(MagicDrawManager
		// .getBlockByQualifiedName(projectId2 + "/packages/" +
		// newElementQualifiedName) != null){
		// elementExists = true;
		// }

		if (!elementExists) {
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package newMDPackage = elementsFactory
					.createPackageInstance();
			newMDPackage.setName(sysmlPackage.getName());

			// get owner/parent element
			Model model = project.getModel();

			// set owner
			URI packageOwnerURI = sysmlPackage.getOwner();
			String ownerURIString = packageOwnerURI.getRawPath();
			ownerURIString = ownerURIString.replace("/oslc4jmagicdraw/services/", "");
			String[] ownerElementStrings = ownerURIString.split("/");
			String ownerType = ownerElementStrings[1];
			String ownerQualifiedName = null;
			if (ownerElementStrings.length > 2) {
				ownerQualifiedName = ownerElementStrings[2];
			}

			if (ownerType.equals("model")) {
				// owner of package is a model
				newMDPackage.setOwner(model);
			} else if (ownerType.equals("packages")) {
				for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package mdPackage : projectIdMDSysmlPackagesMap
						.get(projectId2)) {
					if (mdPackage.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
							.equals(ownerQualifiedName)) {
						newMDPackage.setOwner(mdPackage);
						break;
					}
				}
			}
			// adding the new package to the list of MagicDraw SysML packages
			projectIdMDSysmlPackagesMap.get(projectId2).add(newMDPackage);
			qNameOslcSysmlPackageMap.put(projectId2 + "/packages/" + getQualifiedNameOrID(newMDPackage), sysmlPackage);

			// close session
			SessionManager.getInstance().closeSession();

			// saves project (need to take new project descriptor)
			projectDescriptor = ProjectDescriptorsFactory.getDescriptorForProject(project);
			projectsManager.saveProject(projectDescriptor, true);
		}
	}

	public static void createSysMLItemFlow(SysMLItemFlow sysMLItemFlow, String projectId2) {
		ProjectsManager projectsManager = magicdrawApplication.getProjectsManager();
		final File sysmlfile = new File(magicdrawModelsDirectory + projectId2 + ".mdzip");
		ProjectDescriptor projectDescriptor = ProjectDescriptorsFactory.createProjectDescriptor(sysmlfile.toURI());
		projectsManager.loadProject(projectDescriptor, true);
		if (!SessionManager.getInstance().isSessionCreated()) {
			SessionManager.getInstance().createSession("MagicDraw OSLC Session for projectId" + projectId + sessionID);
			sessionID++;
		}
		Project project = magicdrawApplication.getProject();
		ElementsFactory elementsFactory = magicdrawApplication.getProject().getElementsFactory();
		InformationFlow informationFlow = elementsFactory.createInformationFlowInstance();
		Stereotype itemFlowStereotype = StereotypesHelper.getStereotype(project, "ItemFlow");
		StereotypesHelper.addStereotype(informationFlow, itemFlowStereotype);

		// information flow name
		String newElementQualifiedName = getQualifiedNameFromURI(sysMLItemFlow.getAbout());
		// String newElementName =
		// getNameFromQualifiedName(newElementQualifiedName);
		informationFlow.setName(newElementQualifiedName);

		// information flow source
		String sourcePortQualifiedName = getQualifiedNameFromURI(sysMLItemFlow.getInformationSource());
		for (Port mdSysmlPort : projectIdMDSysmlPortsMap.get(projectId2)) {
			if (mdSysmlPort.getQualifiedName().equals(sourcePortQualifiedName)) {
				informationFlow.getInformationSource().add(mdSysmlPort);
				break;
			}
		}

		// information flow target
		String targetPortQualifiedName = getQualifiedNameFromURI(sysMLItemFlow.getInformationTarget());
		for (Port mdSysmlPort : projectIdMDSysmlPortsMap.get(projectId2)) {
			if (mdSysmlPort.getQualifiedName().equals(targetPortQualifiedName)) {
				informationFlow.getInformationTarget().add(mdSysmlPort);
				break;
			}
		}

		// // information flow conveyed type
		// for (Class mdSysmlBlock : mdSysmlBlocks) {
		// if (mdSysmlBlock.getQualifiedName().equals("SimDouble")) {
		// informationFlow.getConveyed().add(mdSysmlBlock);
		// break;
		// }
		// }

		// get owner/parent element
		Model model = project.getModel();

		// set owner
		informationFlow.setOwner(model);

		// set the realizing connector
		String connectorQualifiedName = getQualifiedNameFromURI(sysMLItemFlow.getRealizingConnector());
		for (Connector mdSysmlConnector : projectIdMDSysmlConnectorsMap.get(projectId2)) {
			if (mdSysmlConnector.getQualifiedName().contains(connectorQualifiedName)) {
				mdSysmlConnector.get_informationFlowOfRealizingConnector().add(informationFlow);
				break;
			}
		}

		// adding the new block to the list of MagicDraw SysML blocks
		projectIdMDSysmlItemFlowsMap.get(projectId2).add(informationFlow);
		qNameOslcSysmlItemFlowMap.put(getQualifiedNameOrID(informationFlow), sysMLItemFlow);

		// close session
		SessionManager.getInstance().closeSession();

		// saves project (need to take new project descriptor)
		projectDescriptor = ProjectDescriptorsFactory.getDescriptorForProject(project);
		projectsManager.saveProject(projectDescriptor, true);

	}

	public static void createSysMLRequirement2(SysMLRequirement sysmlRequirement, String projectId2) {

		// get element name
		String newElementQualifiedName = getQualifiedNameFromURI(sysmlRequirement.getAbout());
		String newElementName = getNameFromQualifiedName(newElementQualifiedName);

		// only create element if it doesn't yet exist		
		if(!idOslcSysmlRequirementMap.keySet().contains(projectId2 + "/requirements/" + sysmlRequirement.getIdentifier())){
			
			
			ProjectsManager projectsManager = magicdrawApplication.getProjectsManager();
			final File sysmlfile = new File(magicdrawModelsDirectory + projectId2 + ".mdzip");
			ProjectDescriptor projectDescriptor = ProjectDescriptorsFactory.createProjectDescriptor(sysmlfile.toURI());
			if (project == null) {
				projectsManager.loadProject(projectDescriptor, true);

				project = magicdrawApplication.getProject();
			}
			if (!SessionManager.getInstance().isSessionCreated()) {
				SessionManager.getInstance().createSession("MagicDraw OSLC Session for projectId" + projectId + sessionID);
				sessionID++;
			}
			
			ElementsFactory elementsFactory = project.getElementsFactory();
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class newMDBlock = elementsFactory.createClassInstance();
			
			Stereotype requirementStereotype = StereotypesHelper.getStereotype(Application.getInstance().getProject(),
					SysMLConstants.REQUIREMENT_STEREOTYPE, SysMLConstants.SYSML_PROFILE);
			StereotypesHelper.addStereotype(newMDBlock, requirementStereotype);
			newMDBlock.setName(newElementName);
			//newMDBlock.setID(newElementName);
			// only numeric values are allowed for the id!
			//StereotypesHelper.setStereotypePropertyValue(newMDBlock, requirementStereotype, "Id", newElementName);
			
			// TODO: currently, requirements can only be owned by the top level project (not by packages)
			Model model = project.getModel();
			newMDBlock.setOwner(model);
			
			// adding derivedFrom relationship if existing
			if(sysmlRequirement.getDerivedFromElements().length > 0 ){
				Stereotype deriveReqtStereotype = StereotypesHelper.getStereotype(Application.getInstance().getProject(),
						SysMLConstants.DERIVE_REQT_STEREOTYPE, SysMLConstants.SYSML_PROFILE);
//				Stereotype deriveReqtStereotype = StereotypesHelper.getStereotype(project, "DeriveReqt");
				Link[] derivedFromElements = sysmlRequirement.getDerivedFromElements();
				for (Link link : derivedFromElements) {
					// get md requirement corresponding to link
					// get target requirement
					URI targetRequirementURI = link.getValue(); 
					String targetRequirementQualifiedName = getQualifiedNameFromURI(targetRequirementURI);
					
					for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class mdRequirement : projectIdMDSysmlRequirementsMap
							.get(projectId2)) {
						if (mdRequirement.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
								.equals(targetRequirementQualifiedName)) {
							
							// create abstraction element with stereotype
							// for now only for DeriveReqt relationship
							// TODO: add support for more relationship types							
							Abstraction abstraction = elementsFactory.createAbstractionInstance();
							abstraction.getClient().add(newMDBlock);
							abstraction.getSupplier().add(mdRequirement);
							StereotypesHelper.addStereotype(abstraction, deriveReqtStereotype);
							abstraction.setOwner(model);							
							break;
						}
					}					
				}

			}
			
	
			// adding the new requirement to the list of MagicDraw SysML requirements
			projectIdMDSysmlRequirementsMap.get(projectId2).add(newMDBlock);
			//qNameOslcSysmlRequirementMap.put(projectId2 + "/requirements/" + getQualifiedNameOrID(newMDBlock), sysmlRequirement);
			idOslcSysmlRequirementMap.put(projectId2 + "/requirements/" + sysmlRequirement.getIdentifier(), sysmlRequirement);
			
			// close session
			SessionManager.getInstance().closeSession();

			// saves project (need to take new project descriptor)
			projectDescriptor = ProjectDescriptorsFactory.getDescriptorForProject(project);
			projectsManager.saveProject(projectDescriptor, true);

		}
	}
	
	
	public static void createSysMLBlock(SysMLBlock sysmlBlock, String projectId2) {

		ProjectsManager projectsManager = magicdrawApplication.getProjectsManager();
		final File sysmlfile = new File(magicdrawModelsDirectory + projectId2 + ".mdzip");
		ProjectDescriptor projectDescriptor = ProjectDescriptorsFactory.createProjectDescriptor(sysmlfile.toURI());
		if (project == null) {
			projectsManager.loadProject(projectDescriptor, true);

			project = magicdrawApplication.getProject();
		}
		if (!SessionManager.getInstance().isSessionCreated()) {
			SessionManager.getInstance().createSession("MagicDraw OSLC Session for projectId" + projectId + sessionID);
			sessionID++;
		}

		// get element name
		String newElementQualifiedName = getQualifiedNameFromURI(sysmlBlock.getAbout());
		String newElementName = getNameFromQualifiedName(newElementQualifiedName);

		// only create element if it doesn't yet exist
		boolean elementExists = false;
		for (Class mdBlock : projectIdMDSysmlBlocksMap.get(projectId2)) {
			if (mdBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
					.equals(newElementQualifiedName)) {
				elementExists = true;
				break;
			}
		}
		// if(MagicDrawManager
		// .getBlockByQualifiedName(projectId2 + "/blocks/" +
		// newElementQualifiedName) != null){
		// elementExists = true;
		// }

		if (!elementExists) {
			ElementsFactory elementsFactory = project.getElementsFactory();
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class newMDBlock = elementsFactory.createClassInstance();
			Stereotype blockStereotype = StereotypesHelper.getStereotype(Application.getInstance().getProject(),
					SysMLConstants.BLOCK_STEREOTYPE, SysMLConstants.SYSML_PROFILE);
			StereotypesHelper.addStereotype(newMDBlock, blockStereotype);
			newMDBlock.setName(newElementName);

			// set owner
			URI ownerURI = sysmlBlock.getOwner();
			String ownerURIString = ownerURI.getRawPath();
			ownerURIString = ownerURIString.replace("/oslc4jmagicdraw/services/", "");
			String[] ownerElementStrings = ownerURIString.split("/");
			String ownerType = ownerElementStrings[1];
			String ownerQualifiedName = ownerElementStrings[2];

			if (ownerType.equals("model")) {
				Model model = project.getModel();
				newMDBlock.setOwner(model);
			} else if (ownerType.equals("packages")) {
				for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package mdPackage : projectIdMDSysmlPackagesMap
						.get(projectId2)) {
					if (mdPackage.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
							.equals(ownerQualifiedName)) {
						newMDBlock.setOwner(mdPackage);
						break;
					}
				}
			} else if (ownerType.equals("blocks")) {
				for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class mdBlock : projectIdMDSysmlBlocksMap
						.get(projectId2)) {
					if (mdBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
							.equals(ownerQualifiedName)) {
						newMDBlock.setOwner(mdBlock);
						break;
					}
				}
			}

			// set inherited block
			if (sysmlBlock.getInheritedBlocks().length > 0) {
				URI inheritedBlockURi = sysmlBlock.getInheritedBlocks()[0].getValue();
				String inheritedBlockQualifiedName = getQualifiedNameFromURI(inheritedBlockURi);
				for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class mdBlock : projectIdMDSysmlBlocksMap
						.get(projectId2)) {
					if (mdBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
							.equals(inheritedBlockQualifiedName)) {
						Generalization generalization = elementsFactory.createGeneralizationInstance();
						generalization.setGeneral(mdBlock);
						generalization.setSpecific(newMDBlock);
						newMDBlock.getGeneralization().add(generalization);
						break;
					}
				}
			}

			// set satisfies Requirement
			if (sysmlBlock.getSatisfies().length > 0) {
				URI satisfiesRequirementURI = sysmlBlock.getSatisfies()[0].getValue();
				String satisfiesRequirementQualifiedName = getQualifiedNameFromURI(satisfiesRequirementURI);
				for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class mdRequirement : projectIdMDSysmlRequirementsMap
						.get(projectId2)) {
					if (mdRequirement.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
							.equals(satisfiesRequirementQualifiedName)) {
						Dependency satisfiesDependency = elementsFactory.createDependencyInstance();
						satisfiesDependency.getClient().add(newMDBlock);
						satisfiesDependency.getSupplier().add(newMDBlock);
						Stereotype satisfyStereotype = StereotypesHelper.getStereotype(project, "Satisfy");
						StereotypesHelper.addStereotype(satisfiesDependency, satisfyStereotype);
						satisfiesDependency.setOwner(newMDBlock.getOwner());
						break;
					}
				}
			}

			// adding the new block to the list of MagicDraw SysML blocks
			projectIdMDSysmlBlocksMap.get(projectId2).add(newMDBlock);
			qNameOslcSysmlBlockMap.put(projectId2 + "/blocks/" + getQualifiedNameOrID(newMDBlock), sysmlBlock);

			// close session
			SessionManager.getInstance().closeSession();

			// saves project (need to take new project descriptor)
			projectDescriptor = ProjectDescriptorsFactory.getDescriptorForProject(project);
			projectsManager.saveProject(projectDescriptor, true);

		}
	}

	public static SysMLReferenceProperty createSysMLReferenceProperty(String newElementName, String ownerName,
			String projectId2) {

		SysMLReferenceProperty newSysMLReferenceProperty = null;
		ProjectsManager projectsManager = magicdrawApplication.getProjectsManager();
		final File sysmlfile = new File(magicdrawModelsDirectory + projectId2 + ".mdzip");
		ProjectDescriptor projectDescriptor = ProjectDescriptorsFactory.createProjectDescriptor(sysmlfile.toURI());
		projectsManager.loadProject(projectDescriptor, true);
		if (!SessionManager.getInstance().isSessionCreated()) {
			SessionManager.getInstance().createSession("MagicDraw OSLC Session for projectId" + projectId + sessionID);
			sessionID++;
		}
		Project project = magicdrawApplication.getProject();
		ElementsFactory elementsFactory = magicdrawApplication.getProject().getElementsFactory();

		com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property newMDProperty = elementsFactory
				.createPropertyInstance();
		// TODO: add stereotype
		// StereotypesHelper.addStereotype(newMDBlock, arg1);
		newMDProperty.setName(newElementName);

		// get owner/parent element
		Classifier owner = null;
		for (Classifier classifier : projectIdMDSysmlBlocksMap.get(projectId2)) {
			if (classifier.getName().equals(ownerName)) {
				owner = classifier;
			}
		}
		if (owner == null) {
			return null;
		}

		// set owner
		newMDProperty.setOwner(owner);
		// ModelElementsHelper.addElement(Element element, Element parent);
		// model.getPackagedElement().add(newMDPackage);
		// ModelElementsManager.getInstance().addElement(newMDPackage, model);

		// close session
		SessionManager.getInstance().closeSession();

		// saves project (need to take new project descriptor)
		projectDescriptor = ProjectDescriptorsFactory.getDescriptorForProject(project);
		projectsManager.saveProject(projectDescriptor, true);

		// Creating corresponding OSLC SysML resource
		if (newMDProperty != null) {
			String qName = newMDProperty.getQualifiedName();
			try {
				newSysMLReferenceProperty = new SysMLReferenceProperty();
				newSysMLReferenceProperty.setName(newElementName);
				newSysMLReferenceProperty.setAbout(URI.create(baseHTTPURI + "/services/" + projectId2
						+ "/referenceproperties/" + qName.replaceAll("\\n", "-").replaceAll(" ", "_")));

			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return newSysMLReferenceProperty;
	}

	public static SysMLRequirement createSysMLRequirement(String newElementName, String ownerName, String projectId2) {

		SysMLRequirement newSysMLRequirement = null;
		ProjectsManager projectsManager = magicdrawApplication.getProjectsManager();
		final File sysmlfile = new File(magicdrawModelsDirectory + projectId2 + ".mdzip");
		ProjectDescriptor projectDescriptor = ProjectDescriptorsFactory.createProjectDescriptor(sysmlfile.toURI());
		projectsManager.loadProject(projectDescriptor, true);
		if (!SessionManager.getInstance().isSessionCreated()) {
			SessionManager.getInstance().createSession("MagicDraw OSLC Session for projectId" + projectId + sessionID);
			sessionID++;
		}
		Project project = magicdrawApplication.getProject();
		ElementsFactory elementsFactory = magicdrawApplication.getProject().getElementsFactory();

		com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class newMDClass = elementsFactory.createClassInstance();
		// TODO: add stereotype
		// StereotypesHelper.addStereotype(newMDBlock, arg1);
		newMDClass.setName(newElementName);

		// get owner/parent element
		Model model = project.getModel();
		Classifier owner = null;
		// for (Classifier classifier : mdSysmlBlocks) {
		// if(classifier.getName().equals(ownerName)){
		// owner = classifier;
		// }
		// }
		// if(owner == null){
		// return null;
		// }

		// set owner
		newMDClass.setOwner(model);
		// ModelElementsHelper.addElement(Element element, Element parent);
		// model.getPackagedElement().add(newMDPackage);
		// ModelElementsManager.getInstance().addElement(newMDPackage, model);

		// close session
		SessionManager.getInstance().closeSession();

		// saves project (need to take new project descriptor)
		projectDescriptor = ProjectDescriptorsFactory.getDescriptorForProject(project);
		projectsManager.saveProject(projectDescriptor, true);

		// Creating corresponding OSLC SysML resource
		if (newMDClass != null) {
			String qName = newMDClass.getQualifiedName();
			try {
				newSysMLRequirement = new SysMLRequirement();
				newSysMLRequirement.setIdentifier(newElementName);
				newSysMLRequirement.setAbout(URI.create(baseHTTPURI + "/services/" + projectId2 + "/requirements/"
						+ qName.replaceAll("\\n", "-").replaceAll(" ", "_")));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return newSysMLRequirement;
	}

	public static String getQualifiedNameOrID(Element element) {
		String qfOrID = null;
		if (element instanceof NamedElement) {
			NamedElement namedElement = (NamedElement) element;
			if (namedElement.getName().equals("")) {
				qfOrID = element.getID();
			} else {
				qfOrID = ((NamedElement) element).getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_");
			}
		} else {
			qfOrID = element.getID();
		}
		return qfOrID;
	}

	public static void createSysMLPartProperty(SysMLPartProperty sysMLPart, String projectId2) {

		ProjectsManager projectsManager = magicdrawApplication.getProjectsManager();
		final File sysmlfile = new File(magicdrawModelsDirectory + projectId2 + ".mdzip");
		ProjectDescriptor projectDescriptor = ProjectDescriptorsFactory.createProjectDescriptor(sysmlfile.toURI());
		if (project == null) {
			projectsManager.loadProject(projectDescriptor, true);
			project = magicdrawApplication.getProject();
		}
		if (!SessionManager.getInstance().isSessionCreated()) {
			SessionManager.getInstance().createSession("MagicDraw OSLC Session for projectId" + projectId + sessionID);
			sessionID++;
		}

		// Unparse element URI
		String newElementQualifiedName = getQualifiedNameFromURI(sysMLPart.getAbout());

		// only create element if it doesn't yet exist
		boolean elementExists = false;
		for (Property mdPartProperty : projectIdMDSysmlPartPropertiesMap.get(projectId2)) {
			if (mdPartProperty.getQualifiedName().equals(newElementQualifiedName)) {
				elementExists = true;
				break;
			}
		}
		// if(MagicDrawManager
		// .getBlockByQualifiedName(projectId2 + "/partproperties/" +
		// newElementQualifiedName) != null){
		// elementExists = true;
		// }

		if (!elementExists) {
			ElementsFactory elementsFactory = project.getElementsFactory();
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property newMDPartProperty = elementsFactory
					.createPropertyInstance();
			newMDPartProperty.setAggregation(AggregationKindEnum.COMPOSITE);
			Stereotype partPropertyStereotype = StereotypesHelper.getStereotype(project, "PartProperty");
			StereotypesHelper.addStereotype(newMDPartProperty, partPropertyStereotype);

			// get the new element name from the new element qualified name
			String newElementName = getNameFromQualifiedName(newElementQualifiedName);
			newMDPartProperty.setName(newElementName);

			// Unparse owner element URI
			String ownerQualifiedName = getQualifiedNameFromURI(sysMLPart.getOwner());

			// owner of a part property is a block
			for (Class mdSysmlBlock : projectIdMDSysmlBlocksMap.get(projectId2)) {
				if (mdSysmlBlock.getQualifiedName().equals(ownerQualifiedName)) {
					// set owner
					// newMDPartProperty.setOwner(mdSysmlBlock);
					mdSysmlBlock.getAttribute().add(newMDPartProperty);

					// get the OSLC SysML block and add the OSLC SysML part to
					// it
					SysMLBlock sysmlBlock = qNameOslcSysmlBlockMap.get(projectId2 + "/blocks/"
							+ mdSysmlBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_"));
					Link[] blockParts = sysmlBlock.getPartProperties();
					Link[] newblockParts = new Link[blockParts.length + 1];
					for (int i = 0; i < newblockParts.length - 1; i++) {
						newblockParts[i] = blockParts[i];
					}
					newblockParts[blockParts.length] = new Link(sysMLPart.getAbout());
					sysmlBlock.setPartProperties(newblockParts);
					break;
				}
			}

			// part multiplicity
			ModelHelper.setMultiplicity(Integer.valueOf(sysMLPart.getLower()), Integer.valueOf(sysMLPart.getUpper()),
					newMDPartProperty);

			// part type
			// Unparse type URI
			String typeQualifiedName = getQualifiedNameFromURI(sysMLPart.getType());
			for (Class mdSysmlBlock : projectIdMDSysmlBlocksMap.get(projectId2)) {
				if (mdSysmlBlock.getQualifiedName().equals(typeQualifiedName)) {
					// set owner
					newMDPartProperty.setType(mdSysmlBlock);
					break;
				} else if (mdSysmlBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
						.equals(typeQualifiedName)) {
					// set owner
					newMDPartProperty.setType(mdSysmlBlock);
					break;
				}
			}

			// adding the new part to the list of MagicDraw SysML parts
			projectIdMDSysmlPartPropertiesMap.get(projectId2).add(newMDPartProperty);

			// close session
			SessionManager.getInstance().closeSession();

			// saves project (need to take new project descriptor)
			projectDescriptor = ProjectDescriptorsFactory.getDescriptorForProject(project);
			projectsManager.saveProject(projectDescriptor, true);

			// adding the new part to the list of OSLC SysML parts
			qNameOslcSysmlPartPropertyMap.put(projectId2 + "/partproperties/" + getQualifiedNameOrID(newMDPartProperty),
					sysMLPart);

		}

	}

	public static void createSysMLConnector(SysMLConnector sysmlConnector, String projectId2) {
		ProjectsManager projectsManager = magicdrawApplication.getProjectsManager();
		final File sysmlfile = new File(magicdrawModelsDirectory + projectId2 + ".mdzip");
		ProjectDescriptor projectDescriptor = ProjectDescriptorsFactory.createProjectDescriptor(sysmlfile.toURI());
		if (project == null) {
			projectsManager.loadProject(projectDescriptor, true);
			project = magicdrawApplication.getProject();
		}
		if (!SessionManager.getInstance().isSessionCreated()) {
			SessionManager.getInstance().createSession("MagicDraw OSLC Session for projectId" + projectId + sessionID);
			sessionID++;
		}

		// Unparse element URI
		String newElementQualifiedName = getQualifiedNameFromURI(sysmlConnector.getAbout());

		// Unparse owner element URI
		String ownerQualifiedName = getQualifiedNameFromURI(sysmlConnector.getOwner());

		// only create element if it doesn't yet exist
		boolean elementExists = false;
		for (Connector mdConnector : projectIdMDSysmlConnectorsMap.get(projectId2)) {
			if (getQualifiedNameOrID(mdConnector).equals(newElementQualifiedName)) {
				elementExists = true;
				break;
			}
		}
		// if(MagicDrawManager
		// .getBlockByQualifiedName(projectId2 + "/connectors/" +
		// newElementQualifiedName) != null){
		// elementExists = true;
		// }

		if (!elementExists) {
			ElementsFactory elementsFactory = project.getElementsFactory();
			com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector newMDConnector = elementsFactory
					.createConnectorInstance();

			// get the new element name from the new element qualified name

			// String newElementName =
			// getNameFromQualifiedName(newElementQualifiedName);
			String newElementName = newElementQualifiedName;

			if (newElementName != null) {
				if (newElementName.length() > 0) {
					newMDConnector.setName(newElementName);
				}
			}

			// owner of a connector is a block
			for (Class mdSysmlBlock : projectIdMDSysmlBlocksMap.get(projectId2)) {
				if (mdSysmlBlock.getQualifiedName().equals(ownerQualifiedName)) {
					// set owner
					newMDConnector.setOwner(mdSysmlBlock);
					break;
				} else if (mdSysmlBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
						.equals(ownerQualifiedName)) {
					// set owner
					newMDConnector.setOwner(mdSysmlBlock);
					break;
				}
			}

			// connector ends
			ConnectorEnd connectorEnd1 = newMDConnector.getEnd().get(0);
			ConnectorEnd connectorEnd2 = newMDConnector.getEnd().get(1);

			Link[] sysmlConnectorEnds = sysmlConnector.getEnds();
			URI connectorEnd1URI = sysmlConnectorEnds[0].getValue();
			URI connectorEnd2URI = sysmlConnectorEnds[1].getValue();

			String sysmlConnectorEndID1 = connectorEnd1URI.getRawPath()
					.replace("/oslc4jmagicdraw/services/" + projectId2 + "/connectorends/", "");
			String sysmlConnectorEndID2 = connectorEnd2URI.getRawPath()
					.replace("/oslc4jmagicdraw/services/" + projectId2 + "/connectorends/", "");

			// SysMLConnectorEnd sysmlConnectorEnd1 =
			// qNameOslcSysmlConnectorEndMap
			// .get(projectId2 + "/connectorends/" +
			// connectorEnd1URI.getRawPath());
			SysMLConnectorEnd sysmlConnectorEnd1 = qNameOslcSysmlConnectorEndMap
					.get(projectId2 + "/connectorends/" + sysmlConnectorEndID1);
			// SysMLConnectorEnd sysmlConnectorEnd2 =
			// qNameOslcSysmlConnectorEndMap
			// .get(projectId2 + "/connectorends/" +
			// connectorEnd2URI.getRawPath());
			SysMLConnectorEnd sysmlConnectorEnd2 = qNameOslcSysmlConnectorEndMap
					.get(projectId2 + "/connectorends/" + sysmlConnectorEndID2);

			// qNameOslcSysmlConnectorEndMap.put(magicDrawFileName +
			// "/connectorends/" + connectorEnd.getID(),
			// sysMLConnectorEnd);

			// nestedConnectorEnd stereotype needs to be applied other
			// MagicDraw will not draw the connector in the IBD
			Stereotype nestedConnectorEndStereotype = StereotypesHelper.getStereotype(project, "NestedConnectorEnd");
			if (sysmlConnectorEnd1 != null & sysmlConnectorEnd2 != null) {

				if (sysmlConnectorEnd1.getPartWithPort() != null) {
					StereotypesHelper.addStereotype(connectorEnd1, nestedConnectorEndStereotype);
					String partWithPort1URIString = sysmlConnectorEnd1.getPartWithPort().getRawPath();
					partWithPort1URIString = partWithPort1URIString.replace("/oslc4jmagicdraw/services/", "");
					String[] partWithPort1Strings = partWithPort1URIString.split("/");
					String partWithPort1QualifiedName = partWithPort1Strings[2];

					for (Property mdSysmlPartProperty : projectIdMDSysmlPartPropertiesMap.get(projectId2)) {
						if (mdSysmlPartProperty.getQualifiedName().equals(partWithPort1QualifiedName)) {
							// set owner
							connectorEnd1.setPartWithPort(mdSysmlPartProperty);
							StereotypesHelper.setStereotypePropertyValue(connectorEnd1, nestedConnectorEndStereotype,
									"propertyPath", mdSysmlPartProperty);
							break;
						}
					}
				}
				if (sysmlConnectorEnd2.getPartWithPort() != null) {
					StereotypesHelper.addStereotype(connectorEnd2, nestedConnectorEndStereotype);
					String partWithPort2URIString = sysmlConnectorEnd2.getPartWithPort().getRawPath();
					partWithPort2URIString = partWithPort2URIString.replace("/oslc4jmagicdraw/services/", "");
					String[] partWithPort2Strings = partWithPort2URIString.split("/");
					String partWithPort2QualifiedName = partWithPort2Strings[2];
					for (Property mdSysmlPartProperty : projectIdMDSysmlPartPropertiesMap.get(projectId2)) {
						if (mdSysmlPartProperty.getQualifiedName().equals(partWithPort2QualifiedName)) {
							// set owner
							connectorEnd2.setPartWithPort(mdSysmlPartProperty);
							StereotypesHelper.setStereotypePropertyValue(connectorEnd2, nestedConnectorEndStereotype,
									"propertyPath", mdSysmlPartProperty);
							break;
						}
					}

				}

				String role1URIString = sysmlConnectorEnd1.getRole().getRawPath();
				String role2URIString = sysmlConnectorEnd2.getRole().getRawPath();

				role1URIString = role1URIString.replace("/oslc4jmagicdraw/services/", "");
				String[] role1Strings = role1URIString.split("/");
				String role1Type = role1Strings[1];
				String role1QualifiedName = role1Strings[2];
				// role can be a part or a port
				if (role1Type.equals("parts")) {
					for (Property mdSysmlPartProperty : projectIdMDSysmlPartPropertiesMap.get(projectId2)) {
						if (mdSysmlPartProperty.getQualifiedName().equals(role1QualifiedName)) {
							// set owner
							connectorEnd1.setRole(mdSysmlPartProperty);
							break;
						}
					}
				} else if (role1Type.equals("ports")) {
					for (Port mdSysmlPort : projectIdMDSysmlPortsMap.get(projectId2)) {
						if (mdSysmlPort.getQualifiedName().equals(role1QualifiedName)) {
							// set owner
							connectorEnd1.setRole(mdSysmlPort);
							break;
						}
					}
				}

				role2URIString = role2URIString.replace("/oslc4jmagicdraw/services/", "");
				String[] role2Strings = role2URIString.split("/");
				String role2Type = role1Strings[1];
				String role2QualifiedName = role2Strings[2];
				// role can be a part or a port
				if (role2Type.equals("parts")) {
					for (Property mdSysmlPartProperty : projectIdMDSysmlPartPropertiesMap.get(projectId2)) {
						if (mdSysmlPartProperty.getQualifiedName().equals(role2QualifiedName)) {
							// set owner
							connectorEnd2.setRole(mdSysmlPartProperty);
							break;
						}
					}
				} else if (role2Type.equals("ports")) {
					for (Port mdSysmlPort : projectIdMDSysmlPortsMap.get(projectId2)) {
						if (mdSysmlPort.getQualifiedName().equals(role2QualifiedName)) {
							// set owner
							connectorEnd2.setRole(mdSysmlPort);
							break;
						}
					}
				}
			}

			// adding the new connector to the list of MagicDraw SysML
			// connectors
			projectIdMDSysmlConnectorsMap.get(projectId2).add(newMDConnector);

			// close session
			SessionManager.getInstance().closeSession();

			// saves project (need to take new project descriptor)
			projectDescriptor = ProjectDescriptorsFactory.getDescriptorForProject(project);
			projectsManager.saveProject(projectDescriptor, true);

			// adding the new connector to the list of OSLC SysML connectors
			qNameOslcSysmlConnectorMap.put(projectId2 + "/connectors/" + getQualifiedNameOrID(newMDConnector),
					sysmlConnector);

		}

	}

	public static void createSysMLConnectorEnd(SysMLConnectorEnd sysmlConnectorEnd, String projectId2) {
		// adding the new connectorend to the list of OSLC SysML connector ends

		String rawPath = sysmlConnectorEnd.getAbout().getRawPath();
		String sysmlConnectorEndID = rawPath.replace("/oslc4jmagicdraw/services/" + projectId2 + "/connectorends/", "");
		// qNameOslcSysmlConnectorEndMap.put(sysmlConnectorEndID,
		// sysmlConnectorEnd);

		qNameOslcSysmlConnectorEndMap.put(projectId2 + "/connectorends/" + sysmlConnectorEndID, sysmlConnectorEnd);

	}

	public static void createSysMLPort(SysMLPort sysmlPort, String projectId2) {
		ProjectsManager projectsManager = magicdrawApplication.getProjectsManager();
		final File sysmlfile = new File(magicdrawModelsDirectory + projectId2 + ".mdzip");
		ProjectDescriptor projectDescriptor = ProjectDescriptorsFactory.createProjectDescriptor(sysmlfile.toURI());
		if (project == null) {
			projectsManager.loadProject(projectDescriptor, true);
			project = magicdrawApplication.getProject();
		}
		if (!SessionManager.getInstance().isSessionCreated()) {
			SessionManager.getInstance().createSession("MagicDraw OSLC Session for projectId" + projectId + sessionID);
			sessionID++;
		}

		// Unparse element URI
		URI elementURI = sysmlPort.getAbout();
		String elementURIString = elementURI.getRawPath();
		elementURIString = elementURIString.replace("/oslc4jmagicdraw/services/", "");
		String[] elementStrings = elementURIString.split("/");
		String newElementQualifiedName = elementStrings[2];

		// Unparse owner element URI
		URI ownerURI = sysmlPort.getOwner();
		String ownerURIString = ownerURI.getRawPath();
		ownerURIString = ownerURIString.replace("/oslc4jmagicdraw/services/", "");
		String[] ownerElementStrings = ownerURIString.split("/");
		String ownerType = ownerElementStrings[1];
		String ownerQualifiedName = ownerElementStrings[2];

		// only create element if it doesn't yet exist
		boolean elementExists = false;
		for (Port mdPort : projectIdMDSysmlPortsMap.get(projectId2)) {
			if (mdPort.getQualifiedName().equals(newElementQualifiedName)) {
				elementExists = true;
				break;
			}
		}
		// if(MagicDrawManager
		// .getBlockByQualifiedName(projectId2 + "/ports/" +
		// newElementQualifiedName) != null){
		// elementExists = true;
		// }

		if (!elementExists) {
			ElementsFactory elementsFactory = project.getElementsFactory();
			com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port newMDPort = elementsFactory
					.createPortInstance();

			// get the new element name from the new element qualified name
			String[] newElementQNameParts = newElementQualifiedName.split("::");
			int newElementQNamePartsSize = newElementQNameParts.length;
			String newElementName = newElementQNameParts[newElementQNamePartsSize - 1];
			newMDPort.setName(newElementName);

			// it is assumed for now that the owner of a port is a
			// block!
			for (Class mdSysmlBlock : projectIdMDSysmlBlocksMap.get(projectId2)) {
				if (mdSysmlBlock.getQualifiedName().equals(ownerQualifiedName)) {
					// set owner
					mdSysmlBlock.getAttribute().add(newMDPort);
					// get the OSLC SysML block and add the OSLC SysML port to
					// it
					SysMLBlock sysmlBlock = qNameOslcSysmlBlockMap.get(projectId2 + "/blocks/"
							+ mdSysmlBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_"));
					Link[] blockPorts = sysmlBlock.getPorts();
					Link[] newblockPorts = new Link[blockPorts.length + 1];
					for (int i = 0; i < newblockPorts.length - 1; i++) {
						newblockPorts[i] = blockPorts[i];
					}
					newblockPorts[blockPorts.length] = new Link(sysmlPort.getAbout());
					sysmlBlock.setPorts(newblockPorts);
					break;
				} else if (mdSysmlBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
						.equals(ownerQualifiedName)) {
					// set owner
					mdSysmlBlock.getAttribute().add(newMDPort);

					// get the OSLC SysML block and add the OSLC SysML port to
					// it
					SysMLBlock sysmlBlock = qNameOslcSysmlBlockMap.get(projectId2 + "/blocks/"
							+ mdSysmlBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_"));
					Link[] blockPorts = sysmlBlock.getPorts();
					Link[] newblockPorts = new Link[blockPorts.length + 1];
					for (int i = 0; i < newblockPorts.length - 1; i++) {
						newblockPorts[i] = blockPorts[i];
					}
					newblockPorts[blockPorts.length] = new Link(sysmlPort.getAbout());
					sysmlBlock.setPorts(newblockPorts);
					break;
				}
			}

			// part multiplicity
			if (sysmlPort.getLower() != null & sysmlPort.getUpper() != null) {
				ModelHelper.setMultiplicity(Integer.valueOf(sysmlPort.getLower()),
						Integer.valueOf(sysmlPort.getUpper()), newMDPort);
			}

			// part type
			// Unparse type URI
			if (sysmlPort.getType() != null) {
				URI typeURI = sysmlPort.getType();
				String typeURIString = typeURI.getRawPath();
				typeURIString = typeURIString.replace("/oslc4jmagicdraw/services/", "");
				String[] typeStrings = typeURIString.split("/");
				String typeQualifiedName = typeStrings[2];
				Collection<Class> allBlocks = new ArrayList<Class>();
				allBlocks.addAll(projectIdMDSysmlBlocksMap.get(projectId2));
				allBlocks.addAll(projectIdMDSysmlInterfaceBlocksMap.get(projectId2));
				for (Class mdSysmlBlock : allBlocks) {
					if (mdSysmlBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
							.equals(typeQualifiedName)) {
						// set owner
						newMDPort.setType(mdSysmlBlock);
						break;
					}
				}
			}

			// isConjugated
			newMDPort.setConjugated(sysmlPort.getIsConjugated());

			// adding the new block to the list of MagicDraw SysML blocks
			projectIdMDSysmlPortsMap.get(projectId2).add(newMDPort);

			// close session
			SessionManager.getInstance().closeSession();

			// saves project (need to take new project descriptor)
			projectDescriptor = ProjectDescriptorsFactory.getDescriptorForProject(project);
			projectsManager.saveProject(projectDescriptor, true);

			// adding the new block to the list of OSLC SysML blocks
			qNameOslcSysmlPortMap.put(projectId2 + "/ports/" + getQualifiedNameOrID(newMDPort), sysmlPort);

		}

	}

	public static String getQualifiedNameFromURI(URI uri) {
		String uriString = uri.getRawPath();
		uriString = uriString.replace("/oslc4jmagicdraw/services/", "");
		String[] uriStrings = uriString.split("/");
		String qualifiedName = uriStrings[2];
		// qualifiedName = qualifiedName.replaceAll("\\n", "-").replaceAll(" ",
		// "_");
		return qualifiedName;
	}

	public static String getNameFromQualifiedName(String qualifiedName) {
		String[] elementQNameParts = qualifiedName.split("::");
		int newElementQNamePartsSize = elementQNameParts.length;
		String elementName = elementQNameParts[newElementQNamePartsSize - 1];
		return elementName;
	}

	public static URI getURIFromQualifiedName(String typeAndQualifiedName) {
		String[] typeAndQualifiedNameStrings = typeAndQualifiedName.split("_");
		String type = typeAndQualifiedNameStrings[0];
		String qualifiedName = typeAndQualifiedNameStrings[typeAndQualifiedNameStrings.length - 1];

		// URI cannot contain empty characters
		qualifiedName = qualifiedName.replaceAll("\\n", "-").replaceAll(" ", "_");

		String elementType = null;
		if (type.equals("MODEL")) {
			elementType = "model";
		} else if (type.equals("PACKAGE")) {
			elementType = "packages";
		} else if (type.equals("BLOCK")) {
			elementType = "blocks";
		} else if (type.equals("REQUIREMENT")) {
			elementType = "requirements";
		} else if (type.equals("PART")) {
			elementType = "parts";
		} else if (type.equals("PORT")) {
			elementType = "ports";
		}

		URI elementURI = URI.create(baseHTTPURI + "/services/" + projectId + "/" + elementType + "/" + qualifiedName);

		return elementURI;
	}

	public static void createSysMLValueProperty(SysMLValueProperty sysMLValueProperty, String projectId2) {
		ProjectsManager projectsManager = magicdrawApplication.getProjectsManager();
		final File sysmlfile = new File(magicdrawModelsDirectory + projectId2 + ".mdzip");
		ProjectDescriptor projectDescriptor = ProjectDescriptorsFactory.createProjectDescriptor(sysmlfile.toURI());
		if (project == null) {
			projectsManager.loadProject(projectDescriptor, true);
			project = magicdrawApplication.getProject();
		}
		if (!SessionManager.getInstance().isSessionCreated()) {
			SessionManager.getInstance().createSession("MagicDraw OSLC Session for projectId" + projectId + sessionID);
			sessionID++;
		}

		// Unparse element URI
		String newElementQualifiedName = getQualifiedNameFromURI(sysMLValueProperty.getAbout());

		// Unparse owner element URI
		String ownerQualifiedName = getQualifiedNameFromURI(sysMLValueProperty.getOwner());

		// only create element if it doesn't yet exist
		boolean elementExists = false;
		for (Property mdValueProperty : projectIdMDSysmlValuePropertiesMap.get(projectId2)) {
			if (mdValueProperty.getQualifiedName().equals(newElementQualifiedName)) {
				elementExists = true;
				break;
			}
		}
		// if(MagicDrawManager
		// .getBlockByQualifiedName(projectId2 + "/valueproperties/" +
		// newElementQualifiedName) != null){
		// elementExists = true;
		// }

		if (!elementExists) {
			ElementsFactory elementsFactory = project.getElementsFactory();
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property newMDValueProperty = elementsFactory
					.createPropertyInstance();

			Stereotype valuePropertyStereotype = StereotypesHelper.getStereotype(project, "ValueProperty");
			StereotypesHelper.addStereotype(newMDValueProperty, valuePropertyStereotype);

			// set the new element name from the new element qualified name
			String newElementName = getNameFromQualifiedName(newElementQualifiedName);
			newMDValueProperty.setName(newElementName);

			// set the value property default value
			LiteralString literalString = elementsFactory.createLiteralStringInstance();
			literalString.setValue(sysMLValueProperty.getDefaultValue());
			newMDValueProperty.setDefaultValue(literalString);

			// owner of a value property is a block
			for (Class mdSysmlBlock : projectIdMDSysmlBlocksMap.get(projectId2)) {
				if (mdSysmlBlock.getQualifiedName().equals(ownerQualifiedName)) {
					// set owner
					mdSysmlBlock.getAttribute().add(newMDValueProperty);
					// get the OSLC SysML block and add the OSLC SysML value
					// property to it
					SysMLBlock sysmlBlock = qNameOslcSysmlBlockMap.get(projectId2 + "/blocks/"
							+ mdSysmlBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_"));
					Link[] blockValueProperties = sysmlBlock.getValueProperties();
					Link[] newblockValueProperties = new Link[blockValueProperties.length + 1];
					for (int i = 0; i < newblockValueProperties.length - 1; i++) {
						newblockValueProperties[i] = blockValueProperties[i];
					}
					newblockValueProperties[blockValueProperties.length] = new Link(sysMLValueProperty.getAbout());
					sysmlBlock.setValueProperties(newblockValueProperties);
					break;
				} else if (mdSysmlBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
						.equals(ownerQualifiedName)) {
					// set owner
					mdSysmlBlock.getAttribute().add(newMDValueProperty);
					// get the OSLC SysML block and add the OSLC SysML value
					// property to it
					SysMLBlock sysmlBlock = qNameOslcSysmlBlockMap.get(projectId2 + "/blocks/"
							+ mdSysmlBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_"));
					Link[] blockValueProperties = sysmlBlock.getValueProperties();
					Link[] newblockValueProperties = new Link[blockValueProperties.length + 1];
					for (int i = 0; i < newblockValueProperties.length - 1; i++) {
						newblockValueProperties[i] = blockValueProperties[i];
					}
					newblockValueProperties[blockValueProperties.length] = new Link(sysMLValueProperty.getAbout());
					sysmlBlock.setValueProperties(newblockValueProperties);
					break;
				}
			}

			// part multiplicity
			if (sysMLValueProperty.getLower() != null & sysMLValueProperty.getUpper() != null) {
				ModelHelper.setMultiplicity(Integer.valueOf(sysMLValueProperty.getLower()),
						Integer.valueOf(sysMLValueProperty.getUpper()), newMDValueProperty);
			}

			// part type
			// Unparse type URI
			if (sysMLValueProperty.getType() != null) {
				String typeQualifiedName = getQualifiedNameFromURI(sysMLValueProperty.getType());

				for (DataType mdSysmlValueType : projectIdMDSysmlValueTypesMap.get(projectId2)) {
					if (mdSysmlValueType.getQualifiedName().equals(typeQualifiedName)) {
						// set owner
						newMDValueProperty.setType(mdSysmlValueType);
						break;
					} else if (mdSysmlValueType.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
							.equals(typeQualifiedName)) {
						// set owner
						newMDValueProperty.setType(mdSysmlValueType);
						break;
					}
				}
			}

			// adding the new part to the list of MagicDraw SysML parts
			projectIdMDSysmlValuePropertiesMap.get(projectId2).add(newMDValueProperty);

			// close session
			SessionManager.getInstance().closeSession();

			// saves project (need to take new project descriptor)
			projectDescriptor = ProjectDescriptorsFactory.getDescriptorForProject(project);
			projectsManager.saveProject(projectDescriptor, true);

			// adding the new value property to the list of OSLC SysML value
			// properties
			qNameOslcSysmlValuePropertyMap.put(
					projectId2 + "/valueproperties/" + getQualifiedNameOrID(newMDValueProperty), sysMLValueProperty);

		}

	}

	public static void updateSysMLValueProperty(SysMLValueProperty sysMLValueProperty, String projectId2) {
		ProjectsManager projectsManager = magicdrawApplication.getProjectsManager();
		final File sysmlfile = new File(magicdrawModelsDirectory + projectId2 + ".mdzip");
		ProjectDescriptor projectDescriptor = ProjectDescriptorsFactory.createProjectDescriptor(sysmlfile.toURI());
		if (project == null) {
			projectsManager.loadProject(projectDescriptor, true);
			project = magicdrawApplication.getProject();
		}
		if (!SessionManager.getInstance().isSessionCreated()) {
			SessionManager.getInstance().createSession("MagicDraw OSLC Session for projectId" + projectId + sessionID);
			sessionID++;
		}

		// Unparse element URI
		String newElementQualifiedName = getQualifiedNameFromURI(sysMLValueProperty.getAbout());

		// get element that already exists
		Property mdValuePropertyToUpdate = null;
		
		
		Collection<Property> projectSpecificMdValueProperties = projectIdMDSysmlValuePropertiesMap.get(projectId2);
		
		//for (Property mdValueProperty : mdSysmlValueProperties) {
		for (Property mdValueProperty : projectSpecificMdValueProperties) {
			if (mdValueProperty.getQualifiedName().equals(newElementQualifiedName)) {
				mdValuePropertyToUpdate = mdValueProperty;
				break;
			}
		}
		if (mdValuePropertyToUpdate != null) {
			ElementsFactory elementsFactory = project.getElementsFactory();

			// set the new element name from the new element qualified name
			String newElementName = getNameFromQualifiedName(newElementQualifiedName);
			mdValuePropertyToUpdate.setName(newElementName);

			// set the value property default value
			LiteralString literalString = elementsFactory.createLiteralStringInstance();
			literalString.setValue(sysMLValueProperty.getDefaultValue());
			mdValuePropertyToUpdate.setDefaultValue(literalString);

			// part multiplicity
			if (sysMLValueProperty.getLower() != null & sysMLValueProperty.getUpper() != null) {
				ModelHelper.setMultiplicity(Integer.valueOf(sysMLValueProperty.getLower()),
						Integer.valueOf(sysMLValueProperty.getUpper()), mdValuePropertyToUpdate);
			}

			// part type
			// Unparse type URI
			if (sysMLValueProperty.getType() != null) {
				String typeQualifiedName = getQualifiedNameFromURI(sysMLValueProperty.getType());

				for (DataType mdSysmlValueType : mdSysmlValueTypes) {
					if (mdSysmlValueType.getQualifiedName().equals(typeQualifiedName)) {
						// set owner
						mdValuePropertyToUpdate.setType(mdSysmlValueType);
						break;
					} else if (mdSysmlValueType.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
							.equals(typeQualifiedName)) {
						// set owner
						mdValuePropertyToUpdate.setType(mdSysmlValueType);
						break;
					}
				}
			}

			// close session
			SessionManager.getInstance().closeSession();

			// saves project (need to take new project descriptor)
			projectDescriptor = ProjectDescriptorsFactory.getDescriptorForProject(project);
			projectsManager.saveProject(projectDescriptor, true);

			// update the value property stored in the map
			qNameOslcSysmlValuePropertyMap.put(
					projectId2 + "/valueproperties/" + getQualifiedNameOrID(mdValuePropertyToUpdate),
					sysMLValueProperty);
		}

	}

	public static void createSysMLInterfaceBlock(SysMLInterfaceBlock sysmlInterfaceBlock, String projectId2) {
		ProjectsManager projectsManager = magicdrawApplication.getProjectsManager();
		final File sysmlfile = new File(magicdrawModelsDirectory + projectId2 + ".mdzip");
		ProjectDescriptor projectDescriptor = ProjectDescriptorsFactory.createProjectDescriptor(sysmlfile.toURI());
		if (project == null) {
			projectsManager.loadProject(projectDescriptor, true);

			project = magicdrawApplication.getProject();
		}
		if (!SessionManager.getInstance().isSessionCreated()) {
			SessionManager.getInstance().createSession("MagicDraw OSLC Session for projectId" + projectId + sessionID);
			sessionID++;
		}

		// get element name
		String newElementQualifiedName = getQualifiedNameFromURI(sysmlInterfaceBlock.getAbout());
		String newElementName = getNameFromQualifiedName(newElementQualifiedName);

		// only create element if it doesn't yet exist
		boolean elementExists = false;
		for (Class mdBlock : projectIdMDSysmlInterfaceBlocksMap.get(projectId2)) {
			if (mdBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
					.equals(newElementQualifiedName)) {
				elementExists = true;
				break;
			}
		}
		// if(MagicDrawManager
		// .getBlockByQualifiedName(projectId2 + "/interfaceblocks/" +
		// newElementQualifiedName) != null){
		// elementExists = true;
		// }

		if (!elementExists) {
			ElementsFactory elementsFactory = project.getElementsFactory();
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class newMDInterfaceBlock = elementsFactory
					.createClassInstance();
			Stereotype interfaceBlockStereotype = StereotypesHelper.getStereotype(
					Application.getInstance().getProject(), "InterfaceBlock", SysMLConstants.SYSML_PROFILE);
			StereotypesHelper.addStereotype(newMDInterfaceBlock, interfaceBlockStereotype);
			newMDInterfaceBlock.setName(newElementName);

			// set owner
			URI ownerURI = sysmlInterfaceBlock.getOwner();
			String ownerURIString = ownerURI.getRawPath();
			ownerURIString = ownerURIString.replace("/oslc4jmagicdraw/services/", "");
			String[] ownerElementStrings = ownerURIString.split("/");
			String ownerType = ownerElementStrings[1];
			String ownerQualifiedName = ownerElementStrings[2];

			if (ownerType.equals("model")) {
				Model model = project.getModel();
				newMDInterfaceBlock.setOwner(model);
			} else if (ownerType.equals("packages")) {
				for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package mdPackage : projectIdMDSysmlPackagesMap
						.get(projectId2)) {
					if (mdPackage.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
							.equals(ownerQualifiedName)) {
						newMDInterfaceBlock.setOwner(mdPackage);
						break;
					}
				}
			} else if (ownerType.equals("blocks")) {
				for (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class mdBlock : projectIdMDSysmlBlocksMap
						.get(projectId2)) {
					if (mdBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
							.equals(ownerQualifiedName)) {
						newMDInterfaceBlock.setOwner(mdBlock);
						break;
					}
				}
			}

			// adding the new block to the list of MagicDraw SysML blocks
			projectIdMDSysmlInterfaceBlocksMap.get(projectId2).add(newMDInterfaceBlock);
			qNameOslcSysmlInterfaceBlockMap.put(
					projectId2 + "/interfaceblocks/" + getQualifiedNameOrID(newMDInterfaceBlock), sysmlInterfaceBlock);

			// close session
			SessionManager.getInstance().closeSession();

			// saves project (need to take new project descriptor)
			projectDescriptor = ProjectDescriptorsFactory.getDescriptorForProject(project);
			projectsManager.saveProject(projectDescriptor, true);
		}

	}

	public static void createSysMLFlowProperty(SysMLFlowProperty sysmlFlowProperty, String projectId2) {
		ProjectsManager projectsManager = magicdrawApplication.getProjectsManager();
		final File sysmlfile = new File(magicdrawModelsDirectory + projectId2 + ".mdzip");
		ProjectDescriptor projectDescriptor = ProjectDescriptorsFactory.createProjectDescriptor(sysmlfile.toURI());
		if (project == null) {
			projectsManager.loadProject(projectDescriptor, true);
			project = magicdrawApplication.getProject();
		}
		if (!SessionManager.getInstance().isSessionCreated()) {
			SessionManager.getInstance().createSession("MagicDraw OSLC Session for projectId" + projectId + sessionID);
			sessionID++;
		}

		// Unparse element URI
		String newElementQualifiedName = getQualifiedNameFromURI(sysmlFlowProperty.getAbout());

		// Unparse owner element URI
		String ownerQualifiedName = getQualifiedNameFromURI(sysmlFlowProperty.getOwner());

		// only create element if it doesn't yet exist
		boolean elementExists = false;
		for (Property mdValueProperty : projectIdMDSysmlFlowPropertiesMap.get(projectId2)) {
			if (mdValueProperty.getQualifiedName().equals(newElementQualifiedName)) {
				elementExists = true;
				break;
			}
		}
		// if(MagicDrawManager
		// .getBlockByQualifiedName(projectId2 + "/flowproperties/" +
		// newElementQualifiedName) != null){
		// elementExists = true;
		// }

		if (!elementExists) {
			ElementsFactory elementsFactory = project.getElementsFactory();
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property newMDFlowProperty = elementsFactory
					.createPropertyInstance();

			Stereotype flowPropertyStereotype = StereotypesHelper.getStereotype(project, "FlowProperty");
			StereotypesHelper.addStereotype(newMDFlowProperty, flowPropertyStereotype);

			// set the new element name from the new element qualified name
			String newElementName = getNameFromQualifiedName(newElementQualifiedName);
			newMDFlowProperty.setName(newElementName);

			// set the flow property direction
			// SysMLFlowDirection sysMLFlowDirection =
			// sysmlFlowProperty.getDirection();
			String sysMLFlowDirection = sysmlFlowProperty.getDirection();
			if (sysMLFlowDirection != null) {
				// if(sysMLFlowDirection == SysMLFlowDirection.IN){
				if (sysMLFlowDirection.equals("in")) {
					StereotypesHelper.setStereotypePropertyValue(newMDFlowProperty, flowPropertyStereotype, "direction",
							ParameterDirectionKindEnum.IN);
				}
				// else if(sysMLFlowDirection == SysMLFlowDirection.OUT){
				else if (sysMLFlowDirection.equals("out")) {
					StereotypesHelper.setStereotypePropertyValue(newMDFlowProperty, flowPropertyStereotype, "direction",
							ParameterDirectionKindEnum.OUT);
					// StereotypesHelper.setStereotypePropertyValue(
					// newMDFlowProperty,
					// flowPropertyStereotype,
					// "direction", SysMLConstants.FLOW_PROPERTY_DIRECTION_IN);
				}
				// StereotypesHelper.getStereotypePropertyFirst(newMDFlowProperty,
				// StereotypesHelper
				// .getFirstVisibleStereotype(newMDFlowProperty), "direction");
			}

			// owner of a value property is a block
			Collection<Class> allBlocks = new ArrayList<Class>();
			allBlocks.addAll(projectIdMDSysmlBlocksMap.get(projectId2));
			allBlocks.addAll(projectIdMDSysmlInterfaceBlocksMap.get(projectId2));

			for (Class mdSysmlBlock : allBlocks) {
				if (mdSysmlBlock.getQualifiedName().equals(ownerQualifiedName)) {
					// set owner
					mdSysmlBlock.getAttribute().add(newMDFlowProperty);
					break;
				} else if (mdSysmlBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
						.equals(ownerQualifiedName)) {
					// set owner
					mdSysmlBlock.getAttribute().add(newMDFlowProperty);
					break;
				}
			}

			// property multiplicity
			if (sysmlFlowProperty.getLower() != null & sysmlFlowProperty.getUpper() != null) {
				ModelHelper.setMultiplicity(Integer.valueOf(sysmlFlowProperty.getLower()),
						Integer.valueOf(sysmlFlowProperty.getUpper()), newMDFlowProperty);
			}

			// property type
			// Unparse type URI
			if (sysmlFlowProperty.getType() != null) {
				String typeQualifiedName = getQualifiedNameFromURI(sysmlFlowProperty.getType());
				for (Class mdSysmlBlock : projectIdMDSysmlBlocksMap.get(projectId2)) {
					if (mdSysmlBlock.getQualifiedName().equals(typeQualifiedName)) {
						// set owner
						newMDFlowProperty.setType(mdSysmlBlock);
						break;
					} else if (mdSysmlBlock.getQualifiedName().replaceAll("\\n", "-").replaceAll(" ", "_")
							.equals(typeQualifiedName)) {
						// set owner
						newMDFlowProperty.setType(mdSysmlBlock);
						break;
					}
				}
			}

			// adding the new part to the list of MagicDraw SysML parts
			projectIdMDSysmlFlowPropertiesMap.get(projectId2).add(newMDFlowProperty);

			// close session
			SessionManager.getInstance().closeSession();

			// saves project (need to take new project descriptor)
			projectDescriptor = ProjectDescriptorsFactory.getDescriptorForProject(project);
			projectsManager.saveProject(projectDescriptor, true);

			// adding the new value property to the list of OSLC SysML value
			// properties
			qNameOslcSysmlFlowPropertyMap.put(projectId2 + "/flowproperties/" + getQualifiedNameOrID(newMDFlowProperty),
					sysmlFlowProperty);
		}

	}

	// http://javarevisited.blogspot.com/2013/03/generate-md5-hash-in-java-string-byte-array-example-tutorial.html
	// http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
	public static String md5Java(Object object) {
		String digest = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(object);
			byte[] objectBytes = bos.toByteArray();
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(objectBytes);
			StringBuilder sb = new StringBuilder(2 * hash.length);
			for (byte b : hash) {
				sb.append(String.format("%02x", b & 0xff));
			}
			digest = sb.toString();
		} catch (UnsupportedEncodingException ex) {
		} catch (NoSuchAlgorithmException ex) {
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return digest;
	}

	public static synchronized void loadSysMLProjects() {
		if (areSysMLProjectsLoaded) {
			return;
		}
		if (projectsManager != null) {
			while (!projectsManager.getProjects().isEmpty()) {
				projectsManager.closeProject();
			}
		}

		loadedProjects.clear(); // to reload MagicDraw
		Thread thread = new Thread() {
			public void start() {
				initializeMapsAcrossAllProjects();
				// project = null;
				// magicdrawApplication = null; //new magicdrawApplication, new
				// projectmanager which can load updated MD model!? no this does
				// not work

				ArrayList<File> files = getMagicDrawModels(OSLC4JMagicDrawApplication.magicdrawModelsDirectory,
						new ArrayList<File>());
				for (File file : files) {
					if (file.getPath().endsWith("mdzip")) {
						String[] filePathSegments = file.getPath().split("\\\\");
						String fileName = filePathSegments[filePathSegments.length - 1];
						fileName = fileName.replaceAll(".mdzip", "");

						// project id can depend on svn repo url
						if (OSLC4JMagicDrawApplication.useIndividualSubversionFiles) {
							String fileDirName = filePathSegments[filePathSegments.length - 2];
							fileName = fileDirName + "---" + fileName;
						}

						loadSysMLProject(fileName);
					}
				}
			}
		};
		thread.start();
		try {
			thread.join();
			System.out.println("Data read from " + OSLC4JMagicDrawApplication.magicdrawModelsDirectory
					+ " and converted into OSLC resources at " + new Date().toString());
			areSysMLProjectsLoaded = true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected static void initializeMapsAcrossAllProjects() {
		predefinedMagicDrawSysMLPackageNames.clear();

		qNameOslcSysmlBlockMap.clear();
		qNameOslcSysmlPartPropertyMap.clear();
		qNameOslcSysmlReferencePropertyMap.clear();
		qNameOslcSysmlPackageMap.clear();
		qNameOslcSysmlAssociationBlockMap.clear();
		qNameOslcSysmlConnectorMap.clear();
		qNameOslcSysmlConnectorEndMap.clear();
		qNameOslcSysmlPortMap.clear();
		qNameOslcSysmlProxyPortMap.clear();
		qNameOslcSysmlFullPortMap.clear();
		qNameOslcSysmlInterfaceBlockMap.clear();
		qNameOslcSysmlFlowPropertyMap.clear();
		qNameOslcSysmlItemFlowMap.clear();
		qNameOslcSysmlValuePropertyMap.clear();
		qNameOslcSysmlValueTypeMap.clear();
		qNameOslcSysmlBlockDiagramMap.clear();
		qNameOslcSysmlInternalBlockDiagramMap.clear();

		projectIdMDSysmlRequirementsMap.clear();
		projectIdMDSysmlBlocksMap.clear();
		projectIdMDSysmlInterfaceBlocksMap.clear();
		projectIdMDSysmlItemFlowsMap.clear();
		projectIdMDSysmlPackagesMap.clear();
		projectIdMDSysmlValueTypesMap.clear();
		projectIdMDSysmlPartPropertiesMap.clear();
		projectIdMDSysmlValuePropertiesMap.clear();
		projectIdMDSysmlFlowPropertiesMap.clear();
		projectIdMDSysmlPortsMap.clear();
		projectIdMDSysmlConnectorsMap.clear();

	}

	public static void makeSysMLProjectActive(String projectId2) {
		projectsManager.setActiveProject(loadedProjects.get(projectId2));
		project = projectsManager.getActiveProject();

	}

	public static ArrayList<File> getMagicDrawModels(String directoryName, ArrayList<File> files) {
		File directory = new File(directoryName);
		// get all the files from a directory		
		if(directory.listFiles() != null){
			File[] fList = directory.listFiles();
			for (File file : fList) {
				if (file.isFile()) {
					files.add(file);
				} else if (file.isDirectory()) {
					getMagicDrawModels(file.getAbsolutePath(), files);
				}
			}
		}
		
		return files;
	}
}
