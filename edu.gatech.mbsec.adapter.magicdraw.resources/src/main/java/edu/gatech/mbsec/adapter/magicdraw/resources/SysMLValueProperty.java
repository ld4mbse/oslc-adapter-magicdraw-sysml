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
@OslcName("ValueProperty")
@OslcResourceShape(title = "ValueProperty Resource Shape", describes = Constants.TYPE_SYSML_VALUEPROPERTY)
public class SysMLValueProperty extends AbstractResource{

	public SysMLValueProperty() throws URISyntaxException {
		super();
	}
	public SysMLValueProperty(URI about) throws URISyntaxException {
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
	private String defaultValue;

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@OslcDescription("Description of ValueProperty::defaultValue TBD")
	@OslcName("defaultValue")
	@OslcOccurs(Occurs.ZeroOrOne)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#ValueProperty_defaultValue")
	@OslcTitle("defaultValue")
	@OslcValueType(ValueType.XMLLiteral)
	public String getDefaultValue() {
		 return defaultValue;
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

	// ********* type *********
	private URI type;

	public void setType(final URI type) {
		this.type = type;
	}

	@OslcDescription("Description of Property::type TBD")
	@OslcName("type")
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Property_type")
	@OslcTitle("type")
	@OslcRange("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Type")
	public URI  getType() {
		 return type;
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
		 URI[] rdfTypes = {URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#NamedElement"), URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#MultiplicityElement"), URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#OwnedElement"), URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Property"), URI.create("http://eclipse.org/MBSE/Parameter")};
		 return rdfTypes;
	}

}