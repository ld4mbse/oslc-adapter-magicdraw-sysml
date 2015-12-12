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
@OslcName("Package")
@OslcResourceShape(title = "Package Resource Shape", describes = Constants.TYPE_SYSML_PACKAGE)
public class SysMLPackage extends AbstractResource{

	public SysMLPackage() throws URISyntaxException {
		super();
	}
	public SysMLPackage(URI about) throws URISyntaxException {
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

	// ********* block *********
	private final Set<Link> blocks = new HashSet<Link>();

	public void setBlocks(final Link[] blocks) {
		this.blocks.clear();
		if (blocks != null)
		{
			this.blocks.addAll(Arrays.asList(blocks));
		}
	}

	@OslcDescription("Description of Package::block TBD")
	@OslcName("block")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Package_block")
	@OslcTitle("block")
	@OslcReadOnly(false)
	public Link[]  getBlocks() {
		 return blocks.toArray(new Link[blocks.size()]);
	}

	// ********* requirement *********
	private final Set<Link> requirements = new HashSet<Link>();

	public void setRequirements(final Link[] requirements) {
		this.requirements.clear();
		if (requirements != null)
		{
			this.requirements.addAll(Arrays.asList(requirements));
		}
	}

	@OslcDescription("Description of Package::requirement TBD")
	@OslcName("requirement")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Package_requirement")
	@OslcTitle("requirement")
	@OslcReadOnly(false)
	public Link[]  getRequirements() {
		 return requirements.toArray(new Link[requirements.size()]);
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
		 URI[] rdfTypes = {URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#NamedElement"), URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#OwnedElement")};
		 return rdfTypes;
	}

}