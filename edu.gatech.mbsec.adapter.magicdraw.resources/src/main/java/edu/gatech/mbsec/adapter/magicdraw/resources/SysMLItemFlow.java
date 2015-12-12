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
@OslcName("ItemFlow")
@OslcResourceShape(title = "ItemFlow Resource Shape", describes = Constants.TYPE_SYSML_ITEMFLOW)
public class SysMLItemFlow extends AbstractResource{

	public SysMLItemFlow() throws URISyntaxException {
		super();
	}
	public SysMLItemFlow(URI about) throws URISyntaxException {
		super(about);
	}

	// ********* itemProperty *********
	private URI itemProperty;

	public void setItemProperty(final URI itemProperty) {
		this.itemProperty = itemProperty;
	}

	@OslcDescription("Description of ItemFlow::itemProperty TBD")
	@OslcName("itemProperty")
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#ItemFlow_itemProperty")
	@OslcTitle("itemProperty")
	@OslcRange("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Property")
	public URI  getItemProperty() {
		 return itemProperty;
	}

	// ********* informationTarget *********
	private URI informationTarget;

	public void setInformationTarget(final URI informationTarget) {
		this.informationTarget = informationTarget;
	}

	@OslcDescription("Description of ItemFlow::informationTarget TBD")
	@OslcName("informationTarget")
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#ItemFlow_informationTarget")
	@OslcTitle("informationTarget")
	@OslcRange("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Property")
	public URI  getInformationTarget() {
		 return informationTarget;
	}

	// ********* informationSource *********
	private URI informationSource;

	public void setInformationSource(final URI informationSource) {
		this.informationSource = informationSource;
	}

	@OslcDescription("Description of ItemFlow::informationSource TBD")
	@OslcName("informationSource")
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#ItemFlow_informationSource")
	@OslcTitle("informationSource")
	@OslcRange("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Property")
	public URI  getInformationSource() {
		 return informationSource;
	}

	// ********* realizingConnector *********
	private URI realizingConnector;

	public void setRealizingConnector(final URI realizingConnector) {
		this.realizingConnector = realizingConnector;
	}

	@OslcDescription("Description of ItemFlow::realizingConnector TBD")
	@OslcName("realizingConnector")
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#ItemFlow_realizingConnector")
	@OslcTitle("realizingConnector")
	@OslcRange("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#Connector")
	public URI  getRealizingConnector() {
		 return realizingConnector;
	}

	// ********* conveyedBlock *********
	private final Set<Link> conveyedBlocks = new HashSet<Link>();

	public void setConveyedBlocks(final Link[] conveyedBlocks) {
		this.conveyedBlocks.clear();
		if (conveyedBlocks != null)
		{
			this.conveyedBlocks.addAll(Arrays.asList(conveyedBlocks));
		}
	}

	@OslcDescription("Description of ItemFlow::conveyedBlock TBD")
	@OslcName("conveyedBlock")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8080/oslc4jmagicdraw/services/sysml-rdfvocabulary#ItemFlow_conveyedBlock")
	@OslcTitle("conveyedBlock")
	@OslcReadOnly(false)
	public Link[]  getConveyedBlocks() {
		 return conveyedBlocks.toArray(new Link[conveyedBlocks.size()]);
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

}