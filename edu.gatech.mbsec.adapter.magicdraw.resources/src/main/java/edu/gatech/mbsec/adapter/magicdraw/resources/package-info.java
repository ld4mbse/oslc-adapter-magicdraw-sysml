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
 * 
 * Modifications performed by:    
 *     Axel Reichwein		- implementation for MagicDraw adapter
 *     (axel.reichwein@koneksys.com)
 *******************************************************************************/
@OslcSchema ({
    @OslcNamespaceDefinition(prefix = OslcConstants.DCTERMS_NAMESPACE_PREFIX,             namespaceURI = OslcConstants.DCTERMS_NAMESPACE),
    @OslcNamespaceDefinition(prefix = OslcConstants.OSLC_CORE_NAMESPACE_PREFIX,           namespaceURI = OslcConstants.OSLC_CORE_NAMESPACE),
    @OslcNamespaceDefinition(prefix = OslcConstants.OSLC_DATA_NAMESPACE_PREFIX,           namespaceURI = OslcConstants.OSLC_DATA_NAMESPACE),
    @OslcNamespaceDefinition(prefix = OslcConstants.RDF_NAMESPACE_PREFIX,                 namespaceURI = OslcConstants.RDF_NAMESPACE),
    @OslcNamespaceDefinition(prefix = OslcConstants.RDFS_NAMESPACE_PREFIX,                namespaceURI = OslcConstants.RDFS_NAMESPACE),
//    @OslcNamespaceDefinition(prefix = Constants.CHANGE_MANAGEMENT_NAMESPACE_PREFIX,       namespaceURI = Constants.CHANGE_MANAGEMENT_NAMESPACE),   
    @OslcNamespaceDefinition(prefix = Constants.FOAF_NAMESPACE_PREFIX,                    namespaceURI = Constants.FOAF_NAMESPACE),
//    @OslcNamespaceDefinition(prefix = Constants.QUALITY_MANAGEMENT_PREFIX,                namespaceURI = Constants.QUALITY_MANAGEMENT_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.REQUIREMENTS_MANAGEMENT_PREFIX,           namespaceURI = Constants.REQUIREMENTS_MANAGEMENT_NAMESPACE),
//    @OslcNamespaceDefinition(prefix = Constants.SOFTWARE_CONFIGURATION_MANAGEMENT_PREFIX, namespaceURI = Constants.SOFTWARE_CONFIGURATION_MANAGEMENT_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.MBSE_PREFIX, namespaceURI = Constants.MBSE_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_ASSOCIATION_PREFIX, namespaceURI = Constants.SYSML_ASSOCIATION_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_ASSOCIATIONBLOCK_PREFIX, namespaceURI = Constants.SYSML_ASSOCIATIONBLOCK_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_BLOCK_PREFIX, namespaceURI = Constants.SYSML_BLOCK_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_INTERFACEBLOCK_PREFIX, namespaceURI = Constants.SYSML_INTERFACEBLOCK_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_MODEL_PREFIX, namespaceURI = Constants.SYSML_MODEL_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_PACKAGE_PREFIX, namespaceURI = Constants.SYSML_PACKAGE_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_PARTPROPERTY_PREFIX, namespaceURI = Constants.SYSML_PARTPROPERTY_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_FLOWPROPERTY_PREFIX, namespaceURI = Constants.SYSML_FLOWPROPERTY_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_REFERENCEPROPERTY_PREFIX, namespaceURI = Constants.SYSML_REFERENCEPROPERTY_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_REQUIREMENT_PREFIX, namespaceURI = Constants.SYSML_REQUIREMENT_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_NAMEDELEMENT_PREFIX, namespaceURI = Constants.SYSML_NAMEDELEMENT_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_MULTIPLICITYELEMENT_PREFIX, namespaceURI = Constants.SYSML_MULTIPLICITYELEMENT_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_PREFIX, namespaceURI = Constants.SYSML_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_VALUEPROPERTY_PREFIX, namespaceURI = Constants.SYSML_VALUEPROPERTY_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_PORT_PREFIX, namespaceURI = Constants.SYSML_PORT_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_PROXYPORT_PREFIX, namespaceURI = Constants.SYSML_PROXYPORT_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_FULLPORT_PREFIX, namespaceURI = Constants.SYSML_FULLPORT_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_BLOCKDIAGRAM_PREFIX, namespaceURI = Constants.SYSML_BLOCKDIAGRAM_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_INTERNALBLOCKDIAGRAM_PREFIX, namespaceURI = Constants.SYSML_INTERNALBLOCKDIAGRAM_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_CONNECTOR_PREFIX, namespaceURI = Constants.SYSML_CONNECTOR_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_CONNECTOREND_PREFIX, namespaceURI = Constants.SYSML_CONNECTOREND_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_ITEMFLOW_PREFIX, namespaceURI = Constants.SYSML_ITEMFLOW_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_OWNEDELEMENT_PREFIX, namespaceURI = Constants.SYSML_OWNEDELEMENT_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_QUANTITYKIND_PREFIX, namespaceURI = Constants.SYSML_QUANTITYKIND_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_TYPE_PREFIX, namespaceURI = Constants.SYSML_TYPE_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_UNIT_PREFIX, namespaceURI = Constants.SYSML_UNIT_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_VALUETYPE_PREFIX, namespaceURI = Constants.SYSML_VALUETYPE_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SYSML_PROPERTY_PREFIX, namespaceURI = Constants.SYSML_PROPERTY_NAMESPACE)
    
})
package edu.gatech.mbsec.adapter.magicdraw.resources;

import org.eclipse.lyo.oslc4j.core.annotation.OslcNamespaceDefinition;
import org.eclipse.lyo.oslc4j.core.annotation.OslcSchema;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;

import edu.gatech.mbsec.adapter.magicdraw.resources.Constants;

