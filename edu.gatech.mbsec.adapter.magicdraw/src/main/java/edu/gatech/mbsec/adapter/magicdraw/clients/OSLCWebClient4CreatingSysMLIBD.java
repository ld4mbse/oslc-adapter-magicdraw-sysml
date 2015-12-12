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
 *******************************************************************************************/
package edu.gatech.mbsec.adapter.magicdraw.clients;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.apache.wink.client.handlers.BasicAuthSecurityHandler;
import org.apache.wink.client.handlers.ClientHandler;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLBlock;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLConnector;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLConnectorEnd;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLModel;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLPackage;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLPartProperty;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLPort;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLReferenceProperty;
import org.eclipse.lyo.adapter.magicdraw.resources.SysMLRequirement;
import org.eclipse.lyo.oslc4j.client.OslcRestClient;
import org.eclipse.lyo.oslc4j.core.model.Link;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
//import org.eclipse.lyo.oslc4j.provider.json4j.Json4JProvidersRegistry;

import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;





/**
 * The main() method of OSLCWebClient4CreatingSysMLIBD creates several new SysML elements 
 * in a MagicDraw SysML project using the OSLC MagicDraw SysML adapter. First, the SysML elements 
 * to be added to the project are created as POJOs. Second, the POJOs are transformed 
 * into RDF/XML by OSLC4J, and then sent as the body of HTTP requests to the creationfactory services
 * of the OSLC MagicDraw SysML adapter. OSLCWebClient4CreatingSysMLIBD 
 * adds SysML elements to a SysML project which can be viewed in MagicDraw in
 * a SysML internal block diagram.
 *  
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
public class OSLCWebClient4CreatingSysMLIBD {

	private static final Set<Class<?>> PROVIDERS = new HashSet<Class<?>>();

	static {
		PROVIDERS.addAll(JenaProvidersRegistry.getProviders());
		// PROVIDERS.addAll(Json4JProvidersRegistry.getProviders());
	}

	public static void main(String[] args) {

		String baseHTTPURI = MagicDrawManager.baseHTTPURI;
		String projectId = "TestProject3";

		// URI of the HTTP request
		String sysmlBlockCreationFactoryURI = baseHTTPURI + "/services/"
				+ projectId + "/blocks";
		String sysmlPartCreationFactoryURI = baseHTTPURI + "/services/"
				+ projectId + "/partproperties";
		String sysmlConnectorEndCreationFactoryURI = baseHTTPURI + "/services/"
				+ projectId + "/connectorends";
		String sysmlConnectorCreationFactoryURI = baseHTTPURI + "/services/"
				+ projectId + "/connectors";
		String sysmlPortCreationFactoryURI = baseHTTPURI + "/services/"
				+ projectId + "/ports";

		// expected mediatype
		String mediaType = "application/rdf+xml";

		// readTimeout specifies how long the RestClient object waits (in
		// milliseconds) for a response before timing out
		int readTimeout = 2400000;

		// set up the credentials for the basic authentication
		BasicAuthSecurityHandler basicAuthHandler = new BasicAuthSecurityHandler();
		basicAuthHandler.setUserName("foo");
		basicAuthHandler.setPassword("bar");

		// creating the OSLC REST clients
		final OslcRestClient oslcSysMLBlockCreationRestClient = new OslcRestClient(
				PROVIDERS, sysmlBlockCreationFactoryURI, mediaType,
				readTimeout, basicAuthHandler);

		final OslcRestClient oslcSysMLPartCreationRestClient = new OslcRestClient(
				PROVIDERS, sysmlPartCreationFactoryURI, mediaType, readTimeout,
				basicAuthHandler);

		final OslcRestClient oslcSysMLConnectorEndCreationRestClient = new OslcRestClient(
				PROVIDERS, sysmlConnectorEndCreationFactoryURI, mediaType,
				readTimeout, basicAuthHandler);

		final OslcRestClient oslcSysMLConnectorCreationRestClient = new OslcRestClient(
				PROVIDERS, sysmlConnectorCreationFactoryURI, mediaType,
				readTimeout, basicAuthHandler);

		final OslcRestClient oslcSysMLPortCreationRestClient = new OslcRestClient(
				PROVIDERS, sysmlPortCreationFactoryURI, mediaType, readTimeout,
				basicAuthHandler);

		try {

			// *****************************
			// *** Creating SysML Blocks ***

			// create the HybridSUV sysml block
			SysMLBlock HybridSUVSysMLBlock = new SysMLBlock();
			HybridSUVSysMLBlock.setName("HybridSUV");
			URI HybridSUVSysMLBlockURI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/blocks/" + "HybridSUV");
			HybridSUVSysMLBlock.setAbout(HybridSUVSysMLBlockURI);
			HybridSUVSysMLBlock.setOwner(URI.create(baseHTTPURI + "/services/"
					+ projectId + "/model/" + "Data"));

			// create the BodySubsystem sysml block
			SysMLBlock BodySubsystemSysMLBlock = new SysMLBlock();
			BodySubsystemSysMLBlock.setName("BodySubsystem");
			URI BodySubsystemSysMLBlockURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/blocks/" + "BodySubsystem");
			BodySubsystemSysMLBlock.setAbout(BodySubsystemSysMLBlockURI);
			BodySubsystemSysMLBlock.setOwner(URI.create(baseHTTPURI
					+ "/services/" + projectId + "/model/" + "Data"));

			// create the ChassisSubsystem sysml block
			SysMLBlock ChassisSubsystemSysMLBlock = new SysMLBlock();
			ChassisSubsystemSysMLBlock.setName("ChassisSubsystem");
			URI ChassisSubsystemSysMLBlockURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/blocks/"
					+ "ChassisSubsystem");
			ChassisSubsystemSysMLBlock.setAbout(ChassisSubsystemSysMLBlockURI);
			ChassisSubsystemSysMLBlock.setOwner(URI.create(baseHTTPURI
					+ "/services/" + projectId + "/model/" + "Data"));

			// create the PowerSubsystem sysml block
			SysMLBlock PowerSubsystemSysMLBlock = new SysMLBlock();
			PowerSubsystemSysMLBlock.setName("PowerSubsystem");
			URI PowerSubsystemSysMLBlockURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/blocks/" + "PowerSubsystem");
			PowerSubsystemSysMLBlock.setAbout(PowerSubsystemSysMLBlockURI);
			PowerSubsystemSysMLBlock.setOwner(URI.create(baseHTTPURI
					+ "/services/" + projectId + "/model/" + "Data"));

			// create the BrakeSubsystem sysml block
			SysMLBlock BrakeSubsystemSysMLBlock = new SysMLBlock();
			BrakeSubsystemSysMLBlock.setName("BrakeSubsystem");
			URI BrakeSubsystemSysMLBlockURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/blocks/" + "BrakeSubsystem");
			BrakeSubsystemSysMLBlock.setAbout(BrakeSubsystemSysMLBlockURI);
			BrakeSubsystemSysMLBlock.setOwner(URI.create(baseHTTPURI
					+ "/services/" + projectId + "/model/" + "Data"));

			// create the InteriorSubsystem sysml block
			SysMLBlock InteriorSubsystemSysMLBlock = new SysMLBlock();
			InteriorSubsystemSysMLBlock.setName("InteriorSubsystem");
			URI InteriorSubsystemSysMLBlockURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/blocks/"
					+ "InteriorSubsystem");
			InteriorSubsystemSysMLBlock
					.setAbout(InteriorSubsystemSysMLBlockURI);
			InteriorSubsystemSysMLBlock.setOwner(URI.create(baseHTTPURI
					+ "/services/" + projectId + "/model/" + "Data"));

			// create the LightingSubsystem sysml block
			SysMLBlock LightingSubsystemSysMLBlock = new SysMLBlock();
			LightingSubsystemSysMLBlock.setName("LightingSubsystem");
			URI LightingSubsystemSysMLBlockURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/blocks/"
					+ "LightingSubsystem");
			LightingSubsystemSysMLBlock
					.setAbout(LightingSubsystemSysMLBlockURI);
			LightingSubsystemSysMLBlock.setOwner(URI.create(baseHTTPURI
					+ "/services/" + projectId + "/model/" + "Data"));

			// ****************************
			// *** Creating SysML Parts ***

			// create the BodySubsystem sysml part
			SysMLPartProperty bodySubsystemSysMLPart = new SysMLPartProperty();
			bodySubsystemSysMLPart.setName("b");
			bodySubsystemSysMLPart.setLower("1");
			bodySubsystemSysMLPart.setUpper("1");
			URI bodySubsystemSysMLPartURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/parts/" + "HybridSUV::b");
			bodySubsystemSysMLPart.setAbout(bodySubsystemSysMLPartURI);
			bodySubsystemSysMLPart.setOwner(HybridSUVSysMLBlockURI);
			bodySubsystemSysMLPart.setType(BodySubsystemSysMLBlockURI);

			// create the ChassisSubsystem sysml part
			SysMLPartProperty chassisSubsystemSysMLPart = new SysMLPartProperty();
			chassisSubsystemSysMLPart.setName("c");
			chassisSubsystemSysMLPart.setLower("1");
			chassisSubsystemSysMLPart.setUpper("1");
			URI chassisSubsystemSysMLPartURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/parts/" + "HybridSUV::c");
			chassisSubsystemSysMLPart.setAbout(chassisSubsystemSysMLPartURI);
			chassisSubsystemSysMLPart.setOwner(HybridSUVSysMLBlockURI);
			chassisSubsystemSysMLPart.setType(ChassisSubsystemSysMLBlockURI);

			// create the PowerSubsystem sysml part
			SysMLPartProperty powerSubsystemSysMLPart = new SysMLPartProperty();
			powerSubsystemSysMLPart.setName("p");
			powerSubsystemSysMLPart.setLower("1");
			powerSubsystemSysMLPart.setUpper("1");
			URI powerSubsystemSysMLPartURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/parts/" + "HybridSUV::p");
			powerSubsystemSysMLPart.setAbout(powerSubsystemSysMLPartURI);
			powerSubsystemSysMLPart.setOwner(HybridSUVSysMLBlockURI);
			powerSubsystemSysMLPart.setType(PowerSubsystemSysMLBlockURI);

			// create the BrakeSubsystem sysml part
			SysMLPartProperty brakeSubsystemSysMLPart = new SysMLPartProperty();
			brakeSubsystemSysMLPart.setName("br");
			brakeSubsystemSysMLPart.setLower("1");
			brakeSubsystemSysMLPart.setUpper("1");
			URI brakeSubsystemSysMLPartURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/parts/" + "HybridSUV::br");
			brakeSubsystemSysMLPart.setAbout(brakeSubsystemSysMLPartURI);
			brakeSubsystemSysMLPart.setOwner(HybridSUVSysMLBlockURI);
			brakeSubsystemSysMLPart.setType(BrakeSubsystemSysMLBlockURI);

			// create the InteriorSubsystem sysml part
			SysMLPartProperty interiorSubsystemSysMLPart = new SysMLPartProperty();
			interiorSubsystemSysMLPart.setName("i");
			interiorSubsystemSysMLPart.setLower("1");
			interiorSubsystemSysMLPart.setUpper("1");
			URI interiorSubsystemSysMLPartURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/parts/" + "HybridSUV::i");
			interiorSubsystemSysMLPart.setAbout(interiorSubsystemSysMLPartURI);
			interiorSubsystemSysMLPart.setOwner(HybridSUVSysMLBlockURI);
			interiorSubsystemSysMLPart.setType(InteriorSubsystemSysMLBlockURI);

			// create the LightingSubsystem sysml part
			SysMLPartProperty lightingSubsystemSysMLPart = new SysMLPartProperty();
			lightingSubsystemSysMLPart.setName("l");
			lightingSubsystemSysMLPart.setLower("1");
			lightingSubsystemSysMLPart.setUpper("1");
			URI lightingSubsystemSysMLPartURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/parts/" + "HybridSUV::l");
			lightingSubsystemSysMLPart.setAbout(lightingSubsystemSysMLPartURI);
			lightingSubsystemSysMLPart.setOwner(HybridSUVSysMLBlockURI);
			lightingSubsystemSysMLPart.setType(LightingSubsystemSysMLBlockURI);

			// ****************************************************
			// *** Creating SysML Connectors and Connector Ends ***

			// create the chassis-body sysml connector
			SysMLConnector bodyChassisConnector = new SysMLConnector();
			bodyChassisConnector.setName("b-c");
			URI bodyChassisConnectorURI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/connectors/" + "HybridSUV::b-c");
			bodyChassisConnector.setAbout(bodyChassisConnectorURI);
			// create the chassis-body chassis sysml connector end
			SysMLConnectorEnd bodyChassisChassisConnectorEnd = new SysMLConnectorEnd();
			bodyChassisChassisConnectorEnd
					.setRole(chassisSubsystemSysMLPartURI);
			URI bodyChassisChassisConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::bodyChassisChassisConnectorEnd");
			bodyChassisChassisConnectorEnd.setOwner(bodyChassisConnectorURI);
			bodyChassisChassisConnectorEnd
					.setAbout(bodyChassisChassisConnectorEndURI);
			// create the chassis-body body sysml connector end
			SysMLConnectorEnd bodyChassisBodyConnectorEnd = new SysMLConnectorEnd();
			bodyChassisBodyConnectorEnd.setRole(bodySubsystemSysMLPartURI);
			URI bodyChassisBodyConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::bodyChassisBodyConnectorEnd");
			bodyChassisBodyConnectorEnd.setOwner(bodyChassisConnectorURI);
			bodyChassisBodyConnectorEnd
					.setAbout(bodyChassisBodyConnectorEndURI);
			Link[] bodyChassisConnectorEnds = new Link[2];
			bodyChassisConnectorEnds[0] = new Link(
					bodyChassisBodyConnectorEndURI);
			bodyChassisConnectorEnds[1] = new Link(
					bodyChassisChassisConnectorEndURI);
			bodyChassisConnector.setEnds(bodyChassisConnectorEnds);
			bodyChassisConnector.setOwner(HybridSUVSysMLBlockURI);

			// create the power-chassis sysml connector
			SysMLConnector powerChassisConnector = new SysMLConnector();
			powerChassisConnector.setName("p-c");
			URI powerChassisConnectorURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectors/"
					+ "HybridSUV::p-cb");
			powerChassisConnector.setAbout(powerChassisConnectorURI);
			// create the power-chassis power sysml connector end
			SysMLConnectorEnd powerChassisPowerConnectorEnd = new SysMLConnectorEnd();
			powerChassisPowerConnectorEnd.setRole(powerSubsystemSysMLPartURI);
			URI powerChassisPowerConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::powerChassisPowerConnectorEnd");
			powerChassisPowerConnectorEnd.setOwner(powerChassisConnectorURI);
			powerChassisPowerConnectorEnd
					.setAbout(powerChassisPowerConnectorEndURI);
			// create the power-chassis chassis sysml connector end
			SysMLConnectorEnd powerChassisChassisConnectorEnd = new SysMLConnectorEnd();
			powerChassisChassisConnectorEnd
					.setRole(chassisSubsystemSysMLPartURI);
			URI powerChassisChassisConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::powerChassisChassisConnectorEnd");
			powerChassisChassisConnectorEnd.setOwner(powerChassisConnectorURI);
			powerChassisChassisConnectorEnd
					.setAbout(powerChassisChassisConnectorEndURI);
			Link[] powerChassisConnectorEnds = new Link[2];
			powerChassisConnectorEnds[0] = new Link(
					powerChassisPowerConnectorEndURI);
			powerChassisConnectorEnds[1] = new Link(
					powerChassisChassisConnectorEndURI);
			powerChassisConnector.setEnds(powerChassisConnectorEnds);
			powerChassisConnector.setOwner(HybridSUVSysMLBlockURI);

			// create the chassis-brake sysml connector
			SysMLConnector chassisBrakeConnector = new SysMLConnector();
			chassisBrakeConnector.setName("c-bk");
			URI chassisBrakeConnectorURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectors/"
					+ "HybridSUV::c-bk");
			chassisBrakeConnector.setAbout(chassisBrakeConnectorURI);
			// create the chassis-brake chassis sysml connector end
			SysMLConnectorEnd chassisBrakeChassisConnectorEnd = new SysMLConnectorEnd();
			chassisBrakeChassisConnectorEnd
					.setRole(chassisSubsystemSysMLPartURI);
			URI chassisBrakeChassisConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::chassisBrakeChassisConnectorEnd");
			chassisBrakeChassisConnectorEnd.setOwner(chassisBrakeConnectorURI);
			chassisBrakeChassisConnectorEnd
					.setAbout(chassisBrakeChassisConnectorEndURI);
			// create the chassis-brake brake sysml connector end
			SysMLConnectorEnd chassisBrakeBrakeConnectorEnd = new SysMLConnectorEnd();
			chassisBrakeBrakeConnectorEnd.setRole(brakeSubsystemSysMLPartURI);
			URI chassisBrakeBrakeConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::chassisBrakeBrakeConnectorEnd");
			chassisBrakeBrakeConnectorEnd.setOwner(chassisBrakeConnectorURI);
			chassisBrakeBrakeConnectorEnd
					.setAbout(chassisBrakeBrakeConnectorEndURI);
			Link[] chassisBrakeConnectorEnds = new Link[2];
			chassisBrakeConnectorEnds[0] = new Link(
					chassisBrakeChassisConnectorEndURI);
			chassisBrakeConnectorEnds[1] = new Link(
					chassisBrakeBrakeConnectorEndURI);
			chassisBrakeConnector.setEnds(chassisBrakeConnectorEnds);
			chassisBrakeConnector.setOwner(HybridSUVSysMLBlockURI);

			// create the power-brake sysml connector
			SysMLConnector powerBrakeConnector = new SysMLConnector();
			powerBrakeConnector.setName("p-bk");
			URI powerBrakeConnectorURI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/connectors/" + "HybridSUV::p-bk");
			powerBrakeConnector.setAbout(powerBrakeConnectorURI);
			// create the power-brake power sysml connector end
			SysMLConnectorEnd powerBrakePowerConnectorEnd = new SysMLConnectorEnd();
			powerBrakePowerConnectorEnd.setRole(powerSubsystemSysMLPartURI);
			URI powerBrakePowerConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::powerBrakePowerConnectorEnd");
			powerBrakePowerConnectorEnd.setOwner(powerBrakeConnectorURI);
			powerBrakePowerConnectorEnd
					.setAbout(powerBrakePowerConnectorEndURI);
			// create the power-brake brake sysml connector end
			SysMLConnectorEnd powerBrakeBrakeConnectorEnd = new SysMLConnectorEnd();
			powerBrakeBrakeConnectorEnd.setRole(brakeSubsystemSysMLPartURI);
			URI powerBrakeBrakeConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::powerBrakeBrakeConnectorEnd");
			powerBrakeBrakeConnectorEnd.setOwner(powerBrakeConnectorURI);
			powerBrakeBrakeConnectorEnd
					.setAbout(powerBrakeBrakeConnectorEndURI);
			Link[] powerBrakeConnectorEnds = new Link[2];
			powerBrakeConnectorEnds[0] = new Link(
					powerBrakePowerConnectorEndURI);
			powerBrakeConnectorEnds[1] = new Link(
					powerBrakeBrakeConnectorEndURI);
			powerBrakeConnector.setEnds(powerBrakeConnectorEnds);
			powerBrakeConnector.setOwner(HybridSUVSysMLBlockURI);

			// create the brake-lighting sysml connector
			SysMLConnector brakeLightingConnector = new SysMLConnector();
			brakeLightingConnector.setName("bk-l");
			URI brakeLightingConnectorURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectors/"
					+ "HybridSUV::bk-l");
			brakeLightingConnector.setAbout(brakeLightingConnectorURI);
			// create the brake-lighting brake sysml connector end
			SysMLConnectorEnd brakeLightingBrakeConnectorEnd = new SysMLConnectorEnd();
			brakeLightingBrakeConnectorEnd.setRole(brakeSubsystemSysMLPartURI);
			URI brakeLightingBrakeConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::brakeLightingBrakeConnectorEnd");
			brakeLightingBrakeConnectorEnd.setOwner(brakeLightingConnectorURI);
			brakeLightingBrakeConnectorEnd
					.setAbout(brakeLightingBrakeConnectorEndURI);
			// create the brake-lighting lighting sysml connector end
			SysMLConnectorEnd brakeLightingLightingConnectorEnd = new SysMLConnectorEnd();
			brakeLightingLightingConnectorEnd
					.setRole(lightingSubsystemSysMLPartURI);
			URI brakeLightingLightingConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::brakeLightingLightingConnectorEnd");
			brakeLightingLightingConnectorEnd
					.setOwner(brakeLightingConnectorURI);
			brakeLightingLightingConnectorEnd
					.setAbout(brakeLightingLightingConnectorEndURI);
			Link[] brakeLightingConnectorEnds = new Link[2];
			brakeLightingConnectorEnds[0] = new Link(
					brakeLightingBrakeConnectorEndURI);
			brakeLightingConnectorEnds[1] = new Link(
					brakeLightingLightingConnectorEndURI);
			brakeLightingConnector.setEnds(brakeLightingConnectorEnds);
			brakeLightingConnector.setOwner(HybridSUVSysMLBlockURI);

			// create the interior-lighting sysml connector
			SysMLConnector interiorLightingConnector = new SysMLConnector();
			interiorLightingConnector.setName("i-l");
			URI interiorLightingConnectorURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectors/"
					+ "HybridSUV::i-l");
			interiorLightingConnector.setAbout(interiorLightingConnectorURI);
			// create the interior-lighting brake sysml connector end
			SysMLConnectorEnd interiorLightingInteriorConnectorEnd = new SysMLConnectorEnd();
			interiorLightingInteriorConnectorEnd
					.setRole(interiorSubsystemSysMLPartURI);
			URI interiorLightingInteriorConnectorEndURI = URI
					.create(baseHTTPURI + "/services/" + projectId
							+ "/connectorends/"
							+ "HybridSUV::interiorLightingInteriorConnectorEnd");
			interiorLightingInteriorConnectorEnd
					.setOwner(interiorLightingConnectorURI);
			interiorLightingInteriorConnectorEnd
					.setAbout(interiorLightingInteriorConnectorEndURI);
			// create the interior-lighting lighting sysml connector end
			SysMLConnectorEnd interiorLightingLightingConnectorEnd = new SysMLConnectorEnd();
			interiorLightingLightingConnectorEnd
					.setRole(lightingSubsystemSysMLPartURI);
			URI interiorLightingLightingConnectorEndURI = URI
					.create(baseHTTPURI + "/services/" + projectId
							+ "/connectorends/"
							+ "HybridSUV::interiorLightingLightingConnectorEnd");
			interiorLightingLightingConnectorEnd
					.setOwner(interiorLightingConnectorURI);
			interiorLightingLightingConnectorEnd
					.setAbout(interiorLightingLightingConnectorEndURI);
			Link[] interiorLightingConnectorEnds = new Link[2];
			interiorLightingConnectorEnds[0] = new Link(
					interiorLightingInteriorConnectorEndURI);
			interiorLightingConnectorEnds[1] = new Link(
					interiorLightingLightingConnectorEndURI);
			interiorLightingConnector.setEnds(interiorLightingConnectorEnds);
			interiorLightingConnector.setOwner(HybridSUVSysMLBlockURI);

			// create the body-lighting sysml connector
			SysMLConnector bodyLightingConnector = new SysMLConnector();
			bodyLightingConnector.setName("b-l");
			URI bodyLightingConnectorURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectors/"
					+ "HybridSUV::b-l");
			bodyLightingConnector.setAbout(bodyLightingConnectorURI);
			// create the body-lighting body sysml connector end
			SysMLConnectorEnd bodyrLightingBodyConnectorEnd = new SysMLConnectorEnd();
			bodyrLightingBodyConnectorEnd.setRole(bodySubsystemSysMLPartURI);
			URI bodyLightingBodyConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::bodyrLightingBodyConnectorEnd");
			bodyrLightingBodyConnectorEnd.setOwner(bodyLightingConnectorURI);
			bodyrLightingBodyConnectorEnd
					.setAbout(bodyLightingBodyConnectorEndURI);
			// create the body-lighting lighting sysml connector end
			SysMLConnectorEnd bodyLightingLightingConnectorEnd = new SysMLConnectorEnd();
			bodyLightingLightingConnectorEnd
					.setRole(lightingSubsystemSysMLPartURI);
			URI bodyLightingLightingConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::bodyLightingLightingConnectorEnd");
			bodyLightingLightingConnectorEnd.setOwner(bodyLightingConnectorURI);
			bodyLightingLightingConnectorEnd
					.setAbout(bodyLightingLightingConnectorEndURI);
			Link[] bodyLightingConnectorEnds = new Link[2];
			bodyLightingConnectorEnds[0] = new Link(
					bodyLightingBodyConnectorEndURI);
			bodyLightingConnectorEnds[1] = new Link(
					bodyLightingLightingConnectorEndURI);
			bodyLightingConnector.setEnds(bodyLightingConnectorEnds);
			bodyLightingConnector.setOwner(HybridSUVSysMLBlockURI);

			// create the body-interior sysml connector
			SysMLConnector bodyInteriorConnector = new SysMLConnector();
			bodyInteriorConnector.setName("b-i");
			URI bodyInteriorConnectorURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectors/"
					+ "HybridSUV::b-i");
			bodyInteriorConnector.setAbout(bodyInteriorConnectorURI);

			// create the body-interior body sysml connector end
			SysMLConnectorEnd bodyInteriorBodyConnectorEnd = new SysMLConnectorEnd();
			bodyInteriorBodyConnectorEnd.setRole(bodySubsystemSysMLPartURI);
			URI bodyInteriorBodyConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::bodyInteriorBodyConnectorEnd");
			bodyInteriorBodyConnectorEnd.setOwner(bodyInteriorConnectorURI);
			bodyInteriorBodyConnectorEnd
					.setAbout(bodyInteriorBodyConnectorEndURI);
			// create the body-interior interior sysml connector end
			SysMLConnectorEnd bodyInteriorInteriorConnectorEnd = new SysMLConnectorEnd();
			bodyInteriorInteriorConnectorEnd
					.setRole(interiorSubsystemSysMLPartURI);
			URI bodyInteriorInteriorConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::bodyInteriorInteriorConnectorEnd");
			bodyInteriorInteriorConnectorEnd.setOwner(bodyInteriorConnectorURI);
			bodyInteriorInteriorConnectorEnd
					.setAbout(bodyInteriorInteriorConnectorEndURI);
			Link[] bodyInteriorConnectorEnds = new Link[2];
			bodyInteriorConnectorEnds[0] = new Link(
					bodyInteriorBodyConnectorEndURI);
			bodyInteriorConnectorEnds[1] = new Link(
					bodyInteriorInteriorConnectorEndURI);
			bodyInteriorConnector.setEnds(bodyInteriorConnectorEnds);
			bodyInteriorConnector.setOwner(HybridSUVSysMLBlockURI);

			// create additional elements for port-based connection

			// create the SimDouble sysml block
			SysMLBlock SimDoubleSysMLBlock = new SysMLBlock();
			SimDoubleSysMLBlock.setName("SimDouble");
			URI SimDoubleSysMLBlockURI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/blocks/" + "SimDouble");
			SimDoubleSysMLBlock.setAbout(SimDoubleSysMLBlockURI);
			SimDoubleSysMLBlock.setOwner(URI.create(baseHTTPURI + "/services/"
					+ projectId + "/model/" + "Data"));

			// create ports
			// create the p1 sysml port
			SysMLPort p1SysMLPort = new SysMLPort();
			p1SysMLPort.setName("p1");
			p1SysMLPort.setLower("1");
			p1SysMLPort.setUpper("1");
			URI p1SysMLPortURI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/ports/" + "BodySubsystem::p1");
			p1SysMLPort.setAbout(p1SysMLPortURI);
			p1SysMLPort.setOwner(BodySubsystemSysMLBlockURI);
			p1SysMLPort.setType(SimDoubleSysMLBlockURI);

			// create the p2 sysml port
			SysMLPort p2SysMLPort = new SysMLPort();
			p2SysMLPort.setName("p2");
			p2SysMLPort.setLower("1");
			p2SysMLPort.setUpper("1");
			URI p2SysMLPortURI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/ports/" + "BrakeSubsystem::p2");
			p2SysMLPort.setAbout(p2SysMLPortURI);
			p2SysMLPort.setOwner(BrakeSubsystemSysMLBlockURI);
			p2SysMLPort.setType(SimDoubleSysMLBlockURI);

			// create the body-brake sysml connector
			SysMLConnector bodyBrakeConnector = new SysMLConnector();
			bodyBrakeConnector.setName("b-br");
			URI bodyBrakeConnectorURI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/connectors/" + "HybridSUV::b-br");
			bodyBrakeConnector.setAbout(bodyBrakeConnectorURI);
			// create the body-interior body sysml connector end
			SysMLConnectorEnd bodyBrakeBodyConnectorEnd = new SysMLConnectorEnd();
			bodyBrakeBodyConnectorEnd.setRole(p1SysMLPortURI);
			bodyBrakeBodyConnectorEnd
					.setPartWithPort(bodySubsystemSysMLPartURI);
			URI bodyBrakeBodyConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::bodyBrakeBodyConnectorEnd");
			bodyBrakeBodyConnectorEnd.setOwner(bodyBrakeConnectorURI);
			bodyBrakeBodyConnectorEnd.setAbout(bodyBrakeBodyConnectorEndURI);
			// create the body-interior interior sysml connector end
			SysMLConnectorEnd bodyBrakeBrakeConnectorEnd = new SysMLConnectorEnd();
			bodyBrakeBrakeConnectorEnd.setRole(p2SysMLPortURI);
			bodyBrakeBrakeConnectorEnd
					.setPartWithPort(brakeSubsystemSysMLPartURI);
			URI bodyBrakeBrakeConnectorEndURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/connectorends/"
					+ "HybridSUV::bodyBrakeBrakeConnectorEnd");
			bodyBrakeBrakeConnectorEnd.setOwner(bodyBrakeConnectorURI);
			bodyBrakeBrakeConnectorEnd.setAbout(bodyBrakeBrakeConnectorEndURI);
			Link[] bodyBrakeConnectorEnds = new Link[2];
			bodyBrakeConnectorEnds[0] = new Link(bodyBrakeBodyConnectorEndURI);
			bodyBrakeConnectorEnds[1] = new Link(bodyBrakeBrakeConnectorEndURI);
			bodyBrakeConnector.setEnds(bodyBrakeConnectorEnds);
			bodyBrakeConnector.setOwner(HybridSUVSysMLBlockURI);

			// ****************************************************
			// *** Invoking the OSLC adapter creation factories ***

			// creating MagicDraw elements corresponding to new OSLC resources
			oslcSysMLBlockCreationRestClient
					.addOslcResource(HybridSUVSysMLBlock);
			oslcSysMLBlockCreationRestClient
					.addOslcResource(ChassisSubsystemSysMLBlock);
			oslcSysMLBlockCreationRestClient
					.addOslcResource(PowerSubsystemSysMLBlock);
			oslcSysMLBlockCreationRestClient
					.addOslcResource(BrakeSubsystemSysMLBlock);
			oslcSysMLBlockCreationRestClient
					.addOslcResource(BodySubsystemSysMLBlock);
			oslcSysMLBlockCreationRestClient
					.addOslcResource(InteriorSubsystemSysMLBlock);
			oslcSysMLBlockCreationRestClient
					.addOslcResource(LightingSubsystemSysMLBlock);

			oslcSysMLPartCreationRestClient
					.addOslcResource(chassisSubsystemSysMLPart);
			oslcSysMLPartCreationRestClient
					.addOslcResource(powerSubsystemSysMLPart);
			oslcSysMLPartCreationRestClient
					.addOslcResource(brakeSubsystemSysMLPart);
			oslcSysMLPartCreationRestClient
					.addOslcResource(bodySubsystemSysMLPart);
			oslcSysMLPartCreationRestClient
					.addOslcResource(interiorSubsystemSysMLPart);
			oslcSysMLPartCreationRestClient
					.addOslcResource(lightingSubsystemSysMLPart);

			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(bodyChassisBodyConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(bodyChassisChassisConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(powerChassisPowerConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(powerChassisChassisConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(chassisBrakeChassisConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(chassisBrakeBrakeConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(powerBrakePowerConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(powerBrakeBrakeConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(brakeLightingBrakeConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(brakeLightingLightingConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(interiorLightingInteriorConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(interiorLightingLightingConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(bodyrLightingBodyConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(bodyLightingLightingConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(bodyInteriorBodyConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(bodyInteriorInteriorConnectorEnd);

			oslcSysMLConnectorCreationRestClient
					.addOslcResource(bodyChassisConnector);
			oslcSysMLConnectorCreationRestClient
					.addOslcResource(bodyInteriorConnector);
			oslcSysMLConnectorCreationRestClient
					.addOslcResource(bodyLightingConnector);
			oslcSysMLConnectorCreationRestClient
					.addOslcResource(powerBrakeConnector);
			oslcSysMLConnectorCreationRestClient
					.addOslcResource(powerChassisConnector);
			oslcSysMLConnectorCreationRestClient
					.addOslcResource(brakeLightingConnector);
			oslcSysMLConnectorCreationRestClient
					.addOslcResource(chassisBrakeConnector);
			oslcSysMLConnectorCreationRestClient
					.addOslcResource(interiorLightingConnector);

			oslcSysMLBlockCreationRestClient
					.addOslcResource(SimDoubleSysMLBlock);

			oslcSysMLPortCreationRestClient.addOslcResource(p1SysMLPort);
			oslcSysMLPortCreationRestClient.addOslcResource(p2SysMLPort);

			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(bodyBrakeBodyConnectorEnd);
			oslcSysMLConnectorEndCreationRestClient
					.addOslcResource(bodyBrakeBrakeConnectorEnd);

			oslcSysMLConnectorCreationRestClient
					.addOslcResource(bodyBrakeConnector);

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
