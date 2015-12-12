/*********************************************************************************************
 * Copyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.
 *                         http://www.mbse.gatech.edu/
 *                  http://www.mbsec.gatech.edu/research/oslc
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
 *	   Axel Reichwein, Koneksys (axel.reichwein@koneksys.com)		
 *******************************************************************************************/
package edu.gatech.mbsec.adapter.magicdraw.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.lyo.oslc4j.core.annotation.OslcDescription;
import org.eclipse.lyo.oslc4j.core.annotation.OslcName;
import org.eclipse.lyo.oslc4j.core.annotation.OslcOccurs;
import org.eclipse.lyo.oslc4j.core.annotation.OslcNamespace;
import org.eclipse.lyo.oslc4j.core.annotation.OslcReadOnly;
import org.eclipse.lyo.oslc4j.core.annotation.OslcPropertyDefinition;
import org.eclipse.lyo.oslc4j.core.annotation.OslcRange;
import org.eclipse.lyo.oslc4j.core.annotation.OslcRepresentation;
import org.eclipse.lyo.oslc4j.core.annotation.OslcResourceShape;
import org.eclipse.lyo.oslc4j.core.annotation.OslcTitle;
import org.eclipse.lyo.oslc4j.core.annotation.OslcValueType;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.Occurs;
import org.eclipse.lyo.oslc4j.core.model.Representation;
import org.eclipse.lyo.oslc4j.core.model.ValueType;
import org.eclipse.lyo.oslc4j.core.model.Link;

@OslcNamespace(Constants.SYSML_NAMESPACE)
@OslcName("ConnectorEnd")
@OslcResourceShape(title = "ConnectorEnd Resource Shape", describes = Constants.TYPE_SYSML_CONNECTOREND)
public class SysMLConnectorEnd extends AbstractResource{

	public SysMLConnectorEnd() throws URISyntaxException {
		super();
	}
	public SysMLConnectorEnd(URI about) throws URISyntaxException {
		super(about);
	}

	private String lower;

	public void setLower(String lower) {
		this.lower = lower;
	}

	@OslcDescription("Description of MultiplicityElement::lower TBD")
	@OslcName("lower")
	@OslcOccurs(Occurs.ZeroOrOne)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#MultiplicityElement_lower")
	@OslcTitle("lower")
	@OslcValueType(ValueType.XMLLiteral)
	public String getLower() {
		 return lower;
	}
	private String upper;

	public void setUpper(String upper) {
		this.upper = upper;
	}

	@OslcDescription("Description of MultiplicityElement::upper TBD")
	@OslcName("upper")
	@OslcOccurs(Occurs.ZeroOrOne)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#MultiplicityElement_upper")
	@OslcTitle("upper")
	@OslcValueType(ValueType.XMLLiteral)
	public String getUpper() {
		 return upper;
	}
	// ********* owner *********
	private URI owner;

	public void setOwner(final URI owner) {
		this.owner = owner;
	}

	@OslcDescription("Description of OwnedElement::owner TBD")
	@OslcName("owner")
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#OwnedElement_owner")
	@OslcTitle("owner")
	@OslcRange("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#NamedElement")
	public URI  getOwner() {
		 return owner;
	}

	// ********* role *********
	private URI role;

	public void setRole(final URI role) {
		this.role = role;
	}

	@OslcDescription("Description of ConnectorEnd::role TBD")
	@OslcName("role")
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#ConnectorEnd_role")
	@OslcTitle("role")
	@OslcRange("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Property")
	public URI  getRole() {
		 return role;
	}

	// ********* definingEnd *********
	private URI definingEnd;

	public void setDefiningEnd(final URI definingEnd) {
		this.definingEnd = definingEnd;
	}

	@OslcDescription("Description of ConnectorEnd::definingEnd TBD")
	@OslcName("definingEnd")
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#ConnectorEnd_definingEnd")
	@OslcTitle("definingEnd")
	@OslcRange("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Property")
	public URI  getDefiningEnd() {
		 return definingEnd;
	}

	// ********* partWithPort *********
	private URI partWithPort;

	public void setPartWithPort(final URI partWithPort) {
		this.partWithPort = partWithPort;
	}

	@OslcDescription("Description of ConnectorEnd::partWithPort TBD")
	@OslcName("partWithPort")
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#ConnectorEnd_partWithPort")
	@OslcTitle("partWithPort")
	@OslcRange("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Property")
	public URI  getPartWithPort() {
		 return partWithPort;
	}

	private URI      serviceProvider;

	public void setServiceProvider(final URI serviceProvider)
	{		this.serviceProvider = serviceProvider;
	}

	@OslcDescription("The scope of a resource is a URI for the resource's OSLC Service Provider.")
	@OslcPropertyDefinition(OslcConstants.OSLC_CORE_NAMESPACE + "serviceProvider")
	@OslcRange(OslcConstants.TYPE_SERVICE_PROVIDER)
	@OslcTitle("Service Provider")	
	public URI getServiceProvider()
	{
		return serviceProvider;
	}

	// ********* rdfType *********
	private URI[] rdfTypes = new URI[3];

	public void setRdfTypes(final URI[] rdfTypes) {
		this.rdfTypes = rdfTypes;
	}

	@OslcDescription("Additional resource type URIs ")
	@OslcName("type")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition(OslcConstants.RDF_NAMESPACE + "type")
	public URI[]  getRdfTypes() {
		 URI[] rdfTypes = {URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#MultiplicityElement"), URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#OwnedElement")};
		 return rdfTypes;
	}

}