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
 *     Russell Boykin       - initial API and implementation
 *     Alberto Giammaria    - initial API and implementation
 *     Chris Peters         - initial API and implementation
 *     Gianluca Bernardini  - initial API and implementation
 *     Michael Fiedler      - Bugzilla adapter implementation
 *     
 * Modifications performed by:    
 *     Axel Reichwein		- implementation for MagicDraw adapter
 *     (axel.reichwein@koneksys.com) 
 *******************************************************************************/
package edu.gatech.mbsec.adapter.magicdraw.serviceproviders;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.eclipse.lyo.adapter.magicdraw.resources.Constants;
import org.eclipse.lyo.oslc4j.client.ServiceProviderRegistryURIs;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.PrefixDefinition;
import org.eclipse.lyo.oslc4j.core.model.Publisher;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderFactory;

import edu.gatech.mbsec.adapter.magicdraw.services.SysMLAssociationBlockService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLBlockDiagramService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLBlockService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLConnectorEndService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLConnectorService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLFlowPropertyService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLFullPortService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLInterfaceBlockService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLInternalBlockDiagramService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLItemFlowService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLModelService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLPackageService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLPartPropertyService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLPortService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLProxyPortService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLReferencePropertyService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLRequirementService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLValuePropertyService;
import edu.gatech.mbsec.adapter.magicdraw.services.SysMLValueTypeService;

/**
 * MagicDrawServiceProviderFactory registers all OSLC Services of each 
 * OSLC MagicDraw Service Provider. There is a OSLC MagicDraw Service Provider
 * for each MagicDraw project. An OSLC Service Provider refers to all 
 * available query and creation factory Services.
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
public class MagicDrawServiceProviderFactory {
	private static Class<?>[] RESOURCE_CLASSES = {
			SysMLAssociationBlockService.class, SysMLBlockService.class,
			SysMLModelService.class, SysMLPackageService.class,
			SysMLPartPropertyService.class,
			SysMLReferencePropertyService.class, SysMLRequirementService.class,
			SysMLItemFlowService.class, SysMLValueTypeService.class,
			SysMLConnectorService.class, SysMLConnectorEndService.class,
			SysMLFlowPropertyService.class, SysMLValuePropertyService.class, SysMLPortService.class,
			SysMLFullPortService.class, SysMLProxyPortService.class, SysMLInterfaceBlockService.class,
			SysMLBlockDiagramService.class, SysMLInternalBlockDiagramService.class};

	private MagicDrawServiceProviderFactory() {
		super();
	}

	/**
	 * Create a new MagicDraw OSLC service provider.
	 * 
	 * @param baseURI
	 * @param product
	 * @param parameterValueMap
	 *            - a map containing the path replacement value for {productId}.
	 *            See ServiceProviderCatalogSingleton.
	 *            initServiceProvidersFromProducts()
	 * 
	 * @throws OslcCoreApplicationException
	 * @throws URISyntaxException
	 */
	public static ServiceProvider createServiceProvider(final String baseURI,
			final String product, final Map<String, Object> parameterValueMap)
			throws OslcCoreApplicationException, URISyntaxException {
		final ServiceProvider serviceProvider = ServiceProviderFactory
				.createServiceProvider(baseURI, ServiceProviderRegistryURIs
						.getUIURI(), product,
						"Service provider for MagicDraw project: " + product,
						new Publisher("Georgia Institute of Technology OSLC Project",
								"urn:oslc:ServiceProvider"), RESOURCE_CLASSES,
						parameterValueMap);
		URI detailsURIs[] = { new URI(baseURI + "/details") };
		serviceProvider.setDetails(detailsURIs);

		final PrefixDefinition[] prefixDefinitions = {
				new PrefixDefinition(OslcConstants.DCTERMS_NAMESPACE_PREFIX,
						new URI(OslcConstants.DCTERMS_NAMESPACE)),
				new PrefixDefinition(OslcConstants.OSLC_CORE_NAMESPACE_PREFIX,
						new URI(OslcConstants.OSLC_CORE_NAMESPACE)),
				new PrefixDefinition(OslcConstants.OSLC_DATA_NAMESPACE_PREFIX,
						new URI(OslcConstants.OSLC_DATA_NAMESPACE)),
				new PrefixDefinition(OslcConstants.RDF_NAMESPACE_PREFIX,
						new URI(OslcConstants.RDF_NAMESPACE)),
				new PrefixDefinition(OslcConstants.RDFS_NAMESPACE_PREFIX,
						new URI(OslcConstants.RDFS_NAMESPACE)),
				new PrefixDefinition(
						Constants.CHANGE_MANAGEMENT_NAMESPACE_PREFIX, new URI(
								Constants.CHANGE_MANAGEMENT_NAMESPACE)),				
				new PrefixDefinition(Constants.FOAF_NAMESPACE_PREFIX, new URI(
						Constants.FOAF_NAMESPACE)),
				new PrefixDefinition(Constants.QUALITY_MANAGEMENT_PREFIX,
						new URI(Constants.QUALITY_MANAGEMENT_NAMESPACE)),
				new PrefixDefinition(Constants.REQUIREMENTS_MANAGEMENT_PREFIX,
						new URI(Constants.REQUIREMENTS_MANAGEMENT_NAMESPACE)),
				new PrefixDefinition(
						Constants.SOFTWARE_CONFIGURATION_MANAGEMENT_PREFIX,
						new URI(
								Constants.SOFTWARE_CONFIGURATION_MANAGEMENT_NAMESPACE)),
				new PrefixDefinition(Constants.SYSML_PREFIX, new URI(
						Constants.SYSML_NAMESPACE)) };
		serviceProvider.setPrefixDefinitions(prefixDefinitions);
		return serviceProvider;
	}
}
