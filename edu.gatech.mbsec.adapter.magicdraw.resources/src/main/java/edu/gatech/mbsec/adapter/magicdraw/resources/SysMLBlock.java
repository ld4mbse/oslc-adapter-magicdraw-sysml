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
@OslcName("Block")
@OslcResourceShape(title = "Block Resource Shape", describes = Constants.TYPE_SYSML_BLOCK)
public class SysMLBlock extends AbstractResource{

	public SysMLBlock() throws URISyntaxException {
		super();
	}
	public SysMLBlock(URI about) throws URISyntaxException {
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

	// ********* partProperty *********
	private final Set<Link> partProperties = new HashSet<Link>();

	public void setPartProperties(final Link[] partProperties) {
		this.partProperties.clear();
		if (partProperties != null)
		{
			this.partProperties.addAll(Arrays.asList(partProperties));
		}
	}

	@OslcDescription("Description of Block::partProperty TBD")
	@OslcName("partProperty")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Block_partProperty")
	@OslcTitle("partProperty")
	@OslcReadOnly(false)
	public Link[]  getPartProperties() {
		 return partProperties.toArray(new Link[partProperties.size()]);
	}

	// ********* referenceProperty *********
	private final Set<Link> referenceProperties = new HashSet<Link>();

	public void setReferenceProperties(final Link[] referenceProperties) {
		this.referenceProperties.clear();
		if (referenceProperties != null)
		{
			this.referenceProperties.addAll(Arrays.asList(referenceProperties));
		}
	}

	@OslcDescription("Description of Block::referenceProperty TBD")
	@OslcName("referenceProperty")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Block_referenceProperty")
	@OslcTitle("referenceProperty")
	@OslcReadOnly(false)
	public Link[]  getReferenceProperties() {
		 return referenceProperties.toArray(new Link[referenceProperties.size()]);
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

	@OslcDescription("Description of Block::flowProperty TBD")
	@OslcName("flowProperty")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Block_flowProperty")
	@OslcTitle("flowProperty")
	@OslcReadOnly(false)
	public Link[]  getFlowProperties() {
		 return flowProperties.toArray(new Link[flowProperties.size()]);
	}

	// ********* valueProperty *********
	private final Set<Link> valueProperties = new HashSet<Link>();

	public void setValueProperties(final Link[] valueProperties) {
		this.valueProperties.clear();
		if (valueProperties != null)
		{
			this.valueProperties.addAll(Arrays.asList(valueProperties));
		}
	}

	@OslcDescription("Description of Block::valueProperty TBD")
	@OslcName("valueProperty")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Block_valueProperty")
	@OslcTitle("valueProperty")
	@OslcReadOnly(false)
	public Link[]  getValueProperties() {
		 return valueProperties.toArray(new Link[valueProperties.size()]);
	}

	// ********* boundReference *********
	private final Set<Link> boundReferences = new HashSet<Link>();

	public void setBoundReferences(final Link[] boundReferences) {
		this.boundReferences.clear();
		if (boundReferences != null)
		{
			this.boundReferences.addAll(Arrays.asList(boundReferences));
		}
	}

	@OslcDescription("Description of Block::boundReference TBD")
	@OslcName("boundReference")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Block_boundReference")
	@OslcTitle("boundReference")
	@OslcReadOnly(false)
	public Link[]  getBoundReferences() {
		 return boundReferences.toArray(new Link[boundReferences.size()]);
	}

	// ********* nestedBlock *********
	private final Set<Link> nestedBlocks = new HashSet<Link>();

	public void setNestedBlocks(final Link[] nestedBlocks) {
		this.nestedBlocks.clear();
		if (nestedBlocks != null)
		{
			this.nestedBlocks.addAll(Arrays.asList(nestedBlocks));
		}
	}

	@OslcDescription("Description of Block::nestedBlock TBD")
	@OslcName("nestedBlock")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Block_nestedBlock")
	@OslcTitle("nestedBlock")
	@OslcReadOnly(false)
	public Link[]  getNestedBlocks() {
		 return nestedBlocks.toArray(new Link[nestedBlocks.size()]);
	}

	// ********* inheritedBlock *********
	private final Set<Link> inheritedBlocks = new HashSet<Link>();

	public void setInheritedBlocks(final Link[] inheritedBlocks) {
		this.inheritedBlocks.clear();
		if (inheritedBlocks != null)
		{
			this.inheritedBlocks.addAll(Arrays.asList(inheritedBlocks));
		}
	}

	@OslcDescription("Description of Block::inheritedBlock TBD")
	@OslcName("inheritedBlock")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Block_inheritedBlock")
	@OslcTitle("inheritedBlock")
	@OslcReadOnly(false)
	public Link[]  getInheritedBlocks() {
		 return inheritedBlocks.toArray(new Link[inheritedBlocks.size()]);
	}

	// ********* satisfy *********
	private final Set<Link> satisfies = new HashSet<Link>();

	public void setSatisfies(final Link[] satisfies) {
		this.satisfies.clear();
		if (satisfies != null)
		{
			this.satisfies.addAll(Arrays.asList(satisfies));
		}
	}

	@OslcDescription("Description of Block::satisfy TBD")
	@OslcName("satisfy")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Block_satisfy")
	@OslcTitle("satisfy")
	@OslcReadOnly(false)
	public Link[]  getSatisfies() {
		 return satisfies.toArray(new Link[satisfies.size()]);
	}

	// ********* connector *********
	private final Set<Link> connectors = new HashSet<Link>();

	public void setConnectors(final Link[] connectors) {
		this.connectors.clear();
		if (connectors != null)
		{
			this.connectors.addAll(Arrays.asList(connectors));
		}
	}

	@OslcDescription("Description of Block::connector TBD")
	@OslcName("connector")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Block_connector")
	@OslcTitle("connector")
	@OslcReadOnly(false)
	public Link[]  getConnectors() {
		 return connectors.toArray(new Link[connectors.size()]);
	}

	// ********* internalBlockDiagram *********
	private final Set<Link> internalBlockDiagrams = new HashSet<Link>();

	public void setInternalBlockDiagrams(final Link[] internalBlockDiagrams) {
		this.internalBlockDiagrams.clear();
		if (internalBlockDiagrams != null)
		{
			this.internalBlockDiagrams.addAll(Arrays.asList(internalBlockDiagrams));
		}
	}

	@OslcDescription("Description of Block::internalBlockDiagram TBD")
	@OslcName("internalBlockDiagram")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Block_internalBlockDiagram")
	@OslcTitle("internalBlockDiagram")
	@OslcReadOnly(false)
	public Link[]  getInternalBlockDiagrams() {
		 return internalBlockDiagrams.toArray(new Link[internalBlockDiagrams.size()]);
	}

	// ********* port *********
	private final Set<Link> ports = new HashSet<Link>();

	public void setPorts(final Link[] ports) {
		this.ports.clear();
		if (ports != null)
		{
			this.ports.addAll(Arrays.asList(ports));
		}
	}

	@OslcDescription("Description of Block::port TBD")
	@OslcName("port")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Block_port")
	@OslcTitle("port")
	@OslcReadOnly(false)
	public Link[]  getPorts() {
		 return ports.toArray(new Link[ports.size()]);
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

	@OslcDescription("Description of Block::proxyPort TBD")
	@OslcName("proxyPort")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Block_proxyPort")
	@OslcTitle("proxyPort")
	@OslcReadOnly(false)
	public Link[]  getProxyPorts() {
		 return proxyPorts.toArray(new Link[proxyPorts.size()]);
	}

	// ********* fullPort *********
	private final Set<Link> fullPorts = new HashSet<Link>();

	public void setFullPorts(final Link[] fullPorts) {
		this.fullPorts.clear();
		if (fullPorts != null)
		{
			this.fullPorts.addAll(Arrays.asList(fullPorts));
		}
	}

	@OslcDescription("Description of Block::fullPort TBD")
	@OslcName("fullPort")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Block_fullPort")
	@OslcTitle("fullPort")
	@OslcReadOnly(false)
	public Link[]  getFullPorts() {
		 return fullPorts.toArray(new Link[fullPorts.size()]);
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
	private URI[] rdfTypes = new URI[5];

	public void setRdfTypes(final URI[] rdfTypes) {
		this.rdfTypes = rdfTypes;
	}

	@OslcDescription("Additional resource type URIs ")
	@OslcName("type")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition(OslcConstants.RDF_NAMESPACE + "type")
	public URI[]  getRdfTypes() {
		 URI[] rdfTypes = {URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#NamedElement"), URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Type"), URI.create("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#OwnedElement"), URI.create("http://eclipse.org/MBSE/Block")};
		 return rdfTypes;
	}

}