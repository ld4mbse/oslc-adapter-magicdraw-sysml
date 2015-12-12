/**
 * *********************************************************************************************
 * Copyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.
 *                                  http://www.mbse.gatech.edu/
 *                       http://www.mbsec.gatech.edu/research/oslc
 * 
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *   
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Eclipse Distribution License is available at
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *   Contributors:
 *   
 *        Axel Reichwein, Koneksys (axel.reichwein@koneksys.com)        
 * *******************************************************************************************
 */
package sysml.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import sysml.Association;
import sysml.AssociationBlock;
import sysml.Block;
import sysml.BlockDiagram;
import sysml.BoundReference;
import sysml.Connector;
import sysml.ConnectorEnd;
import sysml.FlowDirection;
import sysml.FlowProperty;
import sysml.FullPort;
import sysml.InterfaceBlock;
import sysml.InternalBlockDiagram;
import sysml.ItemFlow;
import sysml.Model;
import sysml.OwnedElement;
import sysml.PartProperty;
import sysml.Port;
import sysml.ProxyPort;
import sysml.QuantityKind;
import sysml.ReferenceProperty;
import sysml.Requirement;
import sysml.SysmlFactory;
import sysml.SysmlPackage;
import sysml.Type;
import sysml.Unit;
import sysml.ValueProperty;
import sysml.ValueType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SysmlFactoryImpl extends EFactoryImpl implements SysmlFactory {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "*********************************************************************************************\r\nCopyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.\r\n                                 http://www.mbse.gatech.edu/\r\n                      http://www.mbsec.gatech.edu/research/oslc\r\n\r\n  All rights reserved. This program and the accompanying materials\r\n  are made available under the terms of the Eclipse Public License v1.0\r\n  and Eclipse Distribution License v. 1.0 which accompanies this distribution.\r\n  \r\n  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html\r\n  and the Eclipse Distribution License is available at\r\n  http://www.eclipse.org/org/documents/edl-v10.php.\r\n  \r\n  Contributors:\r\n  \r\n       Axel Reichwein, Koneksys (axel.reichwein@koneksys.com)        \r\n*******************************************************************************************";

	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SysmlFactory init() {
		try {
			SysmlFactory theSysmlFactory = (SysmlFactory)EPackage.Registry.INSTANCE.getEFactory(SysmlPackage.eNS_URI);
			if (theSysmlFactory != null) {
				return theSysmlFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new SysmlFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SysmlFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case SysmlPackage.MODEL: return createModel();
			case SysmlPackage.PACKAGE: return createPackage();
			case SysmlPackage.OWNED_ELEMENT: return createOwnedElement();
			case SysmlPackage.REQUIREMENT: return createRequirement();
			case SysmlPackage.BLOCK: return createBlock();
			case SysmlPackage.INTERFACE_BLOCK: return createInterfaceBlock();
			case SysmlPackage.ASSOCIATION_BLOCK: return createAssociationBlock();
			case SysmlPackage.PART_PROPERTY: return createPartProperty();
			case SysmlPackage.REFERENCE_PROPERTY: return createReferenceProperty();
			case SysmlPackage.FLOW_PROPERTY: return createFlowProperty();
			case SysmlPackage.VALUE_PROPERTY: return createValueProperty();
			case SysmlPackage.ASSOCIATION: return createAssociation();
			case SysmlPackage.PORT: return createPort();
			case SysmlPackage.PROXY_PORT: return createProxyPort();
			case SysmlPackage.FULL_PORT: return createFullPort();
			case SysmlPackage.CONNECTOR: return createConnector();
			case SysmlPackage.CONNECTOR_END: return createConnectorEnd();
			case SysmlPackage.ITEM_FLOW: return createItemFlow();
			case SysmlPackage.VALUE_TYPE: return createValueType();
			case SysmlPackage.UNIT: return createUnit();
			case SysmlPackage.QUANTITY_KIND: return createQuantityKind();
			case SysmlPackage.TYPE: return createType();
			case SysmlPackage.BLOCK_DIAGRAM: return createBlockDiagram();
			case SysmlPackage.INTERNAL_BLOCK_DIAGRAM: return createInternalBlockDiagram();
			case SysmlPackage.BOUND_REFERENCE: return createBoundReference();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case SysmlPackage.FLOW_DIRECTION:
				return createFlowDirectionFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case SysmlPackage.FLOW_DIRECTION:
				return convertFlowDirectionToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Model createModel() {
		ModelImpl model = new ModelImpl();
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public sysml.Package createPackage() {
		PackageImpl package_ = new PackageImpl();
		return package_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OwnedElement createOwnedElement() {
		OwnedElementImpl ownedElement = new OwnedElementImpl();
		return ownedElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Requirement createRequirement() {
		RequirementImpl requirement = new RequirementImpl();
		return requirement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Block createBlock() {
		BlockImpl block = new BlockImpl();
		return block;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InterfaceBlock createInterfaceBlock() {
		InterfaceBlockImpl interfaceBlock = new InterfaceBlockImpl();
		return interfaceBlock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssociationBlock createAssociationBlock() {
		AssociationBlockImpl associationBlock = new AssociationBlockImpl();
		return associationBlock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PartProperty createPartProperty() {
		PartPropertyImpl partProperty = new PartPropertyImpl();
		return partProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReferenceProperty createReferenceProperty() {
		ReferencePropertyImpl referenceProperty = new ReferencePropertyImpl();
		return referenceProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FlowProperty createFlowProperty() {
		FlowPropertyImpl flowProperty = new FlowPropertyImpl();
		return flowProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ValueProperty createValueProperty() {
		ValuePropertyImpl valueProperty = new ValuePropertyImpl();
		return valueProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Association createAssociation() {
		AssociationImpl association = new AssociationImpl();
		return association;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port createPort() {
		PortImpl port = new PortImpl();
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProxyPort createProxyPort() {
		ProxyPortImpl proxyPort = new ProxyPortImpl();
		return proxyPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FullPort createFullPort() {
		FullPortImpl fullPort = new FullPortImpl();
		return fullPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Connector createConnector() {
		ConnectorImpl connector = new ConnectorImpl();
		return connector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConnectorEnd createConnectorEnd() {
		ConnectorEndImpl connectorEnd = new ConnectorEndImpl();
		return connectorEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ItemFlow createItemFlow() {
		ItemFlowImpl itemFlow = new ItemFlowImpl();
		return itemFlow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ValueType createValueType() {
		ValueTypeImpl valueType = new ValueTypeImpl();
		return valueType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Unit createUnit() {
		UnitImpl unit = new UnitImpl();
		return unit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QuantityKind createQuantityKind() {
		QuantityKindImpl quantityKind = new QuantityKindImpl();
		return quantityKind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Type createType() {
		TypeImpl type = new TypeImpl();
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BlockDiagram createBlockDiagram() {
		BlockDiagramImpl blockDiagram = new BlockDiagramImpl();
		return blockDiagram;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InternalBlockDiagram createInternalBlockDiagram() {
		InternalBlockDiagramImpl internalBlockDiagram = new InternalBlockDiagramImpl();
		return internalBlockDiagram;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BoundReference createBoundReference() {
		BoundReferenceImpl boundReference = new BoundReferenceImpl();
		return boundReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FlowDirection createFlowDirectionFromString(EDataType eDataType, String initialValue) {
		FlowDirection result = FlowDirection.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFlowDirectionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SysmlPackage getSysmlPackage() {
		return (SysmlPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SysmlPackage getPackage() {
		return SysmlPackage.eINSTANCE;
	}

} //SysmlFactoryImpl
