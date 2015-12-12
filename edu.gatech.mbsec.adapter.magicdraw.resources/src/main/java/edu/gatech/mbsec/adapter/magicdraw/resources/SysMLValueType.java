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
@OslcName("ValueType")
@OslcResourceShape(title = "ValueType Resource Shape", describes = Constants.TYPE_SYSML_VALUETYPE)
public class SysMLValueType extends AbstractResource{

	public SysMLValueType() throws URISyntaxException {
		super();
	}
	public SysMLValueType(URI about) throws URISyntaxException {
		super(about);
	}

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	@OslcDescription("Description of NamedElement::name TBD")
	@OslcName("name")
	@OslcOccurs(Occurs.ZeroOrOne)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#NamedElement_name")
	@OslcTitle("name")
	@OslcValueType(ValueType.XMLLiteral)
	public String getName() {
		 return name;
	}
	// ********* unit *********
	private URI unit;

	public void setUnit(final URI unit) {
		this.unit = unit;
	}

	@OslcDescription("Description of ValueType::unit TBD")
	@OslcName("unit")
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#ValueType_unit")
	@OslcTitle("unit")
	@OslcRange("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Unit")
	public URI  getUnit() {
		 return unit;
	}

	// ********* quantityKind *********
	private URI quantityKind;

	public void setQuantityKind(final URI quantityKind) {
		this.quantityKind = quantityKind;
	}

	@OslcDescription("Description of ValueType::quantityKind TBD")
	@OslcName("quantityKind")
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#ValueType_quantityKind")
	@OslcTitle("quantityKind")
	@OslcRange("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#QuantityKind")
	public URI  getQuantityKind() {
		 return quantityKind;
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
		 URI[] rdfTypes = {URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Type"), URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#NamedElement")};
		 return rdfTypes;
	}

}