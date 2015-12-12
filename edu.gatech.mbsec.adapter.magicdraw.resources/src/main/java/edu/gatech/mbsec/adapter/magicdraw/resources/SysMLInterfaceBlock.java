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
@OslcName("InterfaceBlock")
@OslcResourceShape(title = "InterfaceBlock Resource Shape", describes = Constants.TYPE_SYSML_INTERFACEBLOCK)
public class SysMLInterfaceBlock extends AbstractResource{

	public SysMLInterfaceBlock() throws URISyntaxException {
		super();
	}
	public SysMLInterfaceBlock(URI about) throws URISyntaxException {
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

	// ********* flowProperty *********
	private final Set<Link> flowProperties = new HashSet<Link>();

	public void setFlowProperties(final Link[] flowProperties) {
		this.flowProperties.clear();
		if (flowProperties != null)
		{
			this.flowProperties.addAll(Arrays.asList(flowProperties));
		}
	}

	@OslcDescription("Description of InterfaceBlock::flowProperty TBD")
	@OslcName("flowProperty")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#InterfaceBlock_flowProperty")
	@OslcTitle("flowProperty")
	@OslcReadOnly(false)
	public Link[]  getFlowProperties() {
		 return flowProperties.toArray(new Link[flowProperties.size()]);
	}

	// ********* proxyPort *********
	private final Set<Link> proxyPorts = new HashSet<Link>();

	public void setProxyPorts(final Link[] proxyPorts) {
		this.proxyPorts.clear();
		if (proxyPorts != null)
		{
			this.proxyPorts.addAll(Arrays.asList(proxyPorts));
		}
	}

	@OslcDescription("Description of InterfaceBlock::proxyPort TBD")
	@OslcName("proxyPort")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#InterfaceBlock_proxyPort")
	@OslcTitle("proxyPort")
	@OslcReadOnly(false)
	public Link[]  getProxyPorts() {
		 return proxyPorts.toArray(new Link[proxyPorts.size()]);
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
	private URI[] rdfTypes = new URI[4];

	public void setRdfTypes(final URI[] rdfTypes) {
		this.rdfTypes = rdfTypes;
	}

	@OslcDescription("Additional resource type URIs ")
	@OslcName("type")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition(OslcConstants.RDF_NAMESPACE + "type")
	public URI[]  getRdfTypes() {
		 URI[] rdfTypes = {URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#NamedElement"), URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#OwnedElement"), URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Type")};
		 return rdfTypes;
	}

}