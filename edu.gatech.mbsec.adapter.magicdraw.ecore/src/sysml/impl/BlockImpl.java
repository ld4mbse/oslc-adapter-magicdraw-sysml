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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import sysml.Block;
import sysml.BoundReference;
import sysml.Connector;
import sysml.FlowProperty;
import sysml.FullPort;
import sysml.InternalBlockDiagram;
import sysml.NamedElement;
import sysml.OwnedElement;
import sysml.PartProperty;
import sysml.Port;
import sysml.ProxyPort;
import sysml.ReferenceProperty;
import sysml.Requirement;
import sysml.SysmlPackage;
import sysml.Type;
import sysml.ValueProperty;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Block</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link sysml.impl.BlockImpl#getOwner <em>Owner</em>}</li>
 *   <li>{@link sysml.impl.BlockImpl#getPartProperty <em>Part Property</em>}</li>
 *   <li>{@link sysml.impl.BlockImpl#getReferenceProperty <em>Reference Property</em>}</li>
 *   <li>{@link sysml.impl.BlockImpl#getFlowProperty <em>Flow Property</em>}</li>
 *   <li>{@link sysml.impl.BlockImpl#getValueProperty <em>Value Property</em>}</li>
 *   <li>{@link sysml.impl.BlockImpl#getBoundReference <em>Bound Reference</em>}</li>
 *   <li>{@link sysml.impl.BlockImpl#getNestedBlock <em>Nested Block</em>}</li>
 *   <li>{@link sysml.impl.BlockImpl#getInheritedBlock <em>Inherited Block</em>}</li>
 *   <li>{@link sysml.impl.BlockImpl#getSatisfy <em>Satisfy</em>}</li>
 *   <li>{@link sysml.impl.BlockImpl#getConnector <em>Connector</em>}</li>
 *   <li>{@link sysml.impl.BlockImpl#getInternalBlockDiagram <em>Internal Block Diagram</em>}</li>
 *   <li>{@link sysml.impl.BlockImpl#getPort <em>Port</em>}</li>
 *   <li>{@link sysml.impl.BlockImpl#getProxyPort <em>Proxy Port</em>}</li>
 *   <li>{@link sysml.impl.BlockImpl#getFullPort <em>Full Port</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BlockImpl extends NamedElementImpl implements Block {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "*********************************************************************************************\r\nCopyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.\r\n                                 http://www.mbse.gatech.edu/\r\n                      http://www.mbsec.gatech.edu/research/oslc\r\n\r\n  All rights reserved. This program and the accompanying materials\r\n  are made available under the terms of the Eclipse Public License v1.0\r\n  and Eclipse Distribution License v. 1.0 which accompanies this distribution.\r\n  \r\n  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html\r\n  and the Eclipse Distribution License is available at\r\n  http://www.eclipse.org/org/documents/edl-v10.php.\r\n  \r\n  Contributors:\r\n  \r\n       Axel Reichwein, Koneksys (axel.reichwein@koneksys.com)        \r\n*******************************************************************************************";

	/**
	 * The cached value of the '{@link #getOwner() <em>Owner</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwner()
	 * @generated
	 * @ordered
	 */
	protected NamedElement owner;

	/**
	 * The cached value of the '{@link #getPartProperty() <em>Part Property</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPartProperty()
	 * @generated
	 * @ordered
	 */
	protected EList<PartProperty> partProperty;

	/**
	 * The cached value of the '{@link #getReferenceProperty() <em>Reference Property</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenceProperty()
	 * @generated
	 * @ordered
	 */
	protected EList<ReferenceProperty> referenceProperty;

	/**
	 * The cached value of the '{@link #getFlowProperty() <em>Flow Property</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlowProperty()
	 * @generated
	 * @ordered
	 */
	protected EList<FlowProperty> flowProperty;

	/**
	 * The cached value of the '{@link #getValueProperty() <em>Value Property</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValueProperty()
	 * @generated
	 * @ordered
	 */
	protected EList<ValueProperty> valueProperty;

	/**
	 * The cached value of the '{@link #getBoundReference() <em>Bound Reference</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBoundReference()
	 * @generated
	 * @ordered
	 */
	protected EList<BoundReference> boundReference;

	/**
	 * The cached value of the '{@link #getNestedBlock() <em>Nested Block</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNestedBlock()
	 * @generated
	 * @ordered
	 */
	protected EList<Block> nestedBlock;

	/**
	 * The cached value of the '{@link #getInheritedBlock() <em>Inherited Block</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInheritedBlock()
	 * @generated
	 * @ordered
	 */
	protected EList<Block> inheritedBlock;

	/**
	 * The cached value of the '{@link #getSatisfy() <em>Satisfy</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSatisfy()
	 * @generated
	 * @ordered
	 */
	protected EList<Requirement> satisfy;

	/**
	 * The cached value of the '{@link #getConnector() <em>Connector</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnector()
	 * @generated
	 * @ordered
	 */
	protected EList<Connector> connector;

	/**
	 * The cached value of the '{@link #getInternalBlockDiagram() <em>Internal Block Diagram</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInternalBlockDiagram()
	 * @generated
	 * @ordered
	 */
	protected EList<InternalBlockDiagram> internalBlockDiagram;

	/**
	 * The cached value of the '{@link #getPort() <em>Port</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPort()
	 * @generated
	 * @ordered
	 */
	protected EList<Port> port;

	/**
	 * The cached value of the '{@link #getProxyPort() <em>Proxy Port</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProxyPort()
	 * @generated
	 * @ordered
	 */
	protected EList<ProxyPort> proxyPort;

	/**
	 * The cached value of the '{@link #getFullPort() <em>Full Port</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFullPort()
	 * @generated
	 * @ordered
	 */
	protected EList<FullPort> fullPort;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BlockImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SysmlPackage.Literals.BLOCK;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NamedElement getOwner() {
		if (owner != null && owner.eIsProxy()) {
			InternalEObject oldOwner = (InternalEObject)owner;
			owner = (NamedElement)eResolveProxy(oldOwner);
			if (owner != oldOwner) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.BLOCK__OWNER, oldOwner, owner));
			}
		}
		return owner;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NamedElement basicGetOwner() {
		return owner;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOwner(NamedElement newOwner) {
		NamedElement oldOwner = owner;
		owner = newOwner;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.BLOCK__OWNER, oldOwner, owner));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PartProperty> getPartProperty() {
		if (partProperty == null) {
			partProperty = new EObjectResolvingEList<PartProperty>(PartProperty.class, this, SysmlPackage.BLOCK__PART_PROPERTY);
		}
		return partProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ReferenceProperty> getReferenceProperty() {
		if (referenceProperty == null) {
			referenceProperty = new EObjectResolvingEList<ReferenceProperty>(ReferenceProperty.class, this, SysmlPackage.BLOCK__REFERENCE_PROPERTY);
		}
		return referenceProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FlowProperty> getFlowProperty() {
		if (flowProperty == null) {
			flowProperty = new EObjectResolvingEList<FlowProperty>(FlowProperty.class, this, SysmlPackage.BLOCK__FLOW_PROPERTY);
		}
		return flowProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ValueProperty> getValueProperty() {
		if (valueProperty == null) {
			valueProperty = new EObjectResolvingEList<ValueProperty>(ValueProperty.class, this, SysmlPackage.BLOCK__VALUE_PROPERTY);
		}
		return valueProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BoundReference> getBoundReference() {
		if (boundReference == null) {
			boundReference = new EObjectResolvingEList<BoundReference>(BoundReference.class, this, SysmlPackage.BLOCK__BOUND_REFERENCE);
		}
		return boundReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Block> getNestedBlock() {
		if (nestedBlock == null) {
			nestedBlock = new EObjectResolvingEList<Block>(Block.class, this, SysmlPackage.BLOCK__NESTED_BLOCK);
		}
		return nestedBlock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Block> getInheritedBlock() {
		if (inheritedBlock == null) {
			inheritedBlock = new EObjectResolvingEList<Block>(Block.class, this, SysmlPackage.BLOCK__INHERITED_BLOCK);
		}
		return inheritedBlock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Requirement> getSatisfy() {
		if (satisfy == null) {
			satisfy = new EObjectResolvingEList<Requirement>(Requirement.class, this, SysmlPackage.BLOCK__SATISFY);
		}
		return satisfy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Connector> getConnector() {
		if (connector == null) {
			connector = new EObjectResolvingEList<Connector>(Connector.class, this, SysmlPackage.BLOCK__CONNECTOR);
		}
		return connector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<InternalBlockDiagram> getInternalBlockDiagram() {
		if (internalBlockDiagram == null) {
			internalBlockDiagram = new EObjectResolvingEList<InternalBlockDiagram>(InternalBlockDiagram.class, this, SysmlPackage.BLOCK__INTERNAL_BLOCK_DIAGRAM);
		}
		return internalBlockDiagram;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Port> getPort() {
		if (port == null) {
			port = new EObjectResolvingEList<Port>(Port.class, this, SysmlPackage.BLOCK__PORT);
		}
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ProxyPort> getProxyPort() {
		if (proxyPort == null) {
			proxyPort = new EObjectResolvingEList<ProxyPort>(ProxyPort.class, this, SysmlPackage.BLOCK__PROXY_PORT);
		}
		return proxyPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FullPort> getFullPort() {
		if (fullPort == null) {
			fullPort = new EObjectResolvingEList<FullPort>(FullPort.class, this, SysmlPackage.BLOCK__FULL_PORT);
		}
		return fullPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SysmlPackage.BLOCK__OWNER:
				if (resolve) return getOwner();
				return basicGetOwner();
			case SysmlPackage.BLOCK__PART_PROPERTY:
				return getPartProperty();
			case SysmlPackage.BLOCK__REFERENCE_PROPERTY:
				return getReferenceProperty();
			case SysmlPackage.BLOCK__FLOW_PROPERTY:
				return getFlowProperty();
			case SysmlPackage.BLOCK__VALUE_PROPERTY:
				return getValueProperty();
			case SysmlPackage.BLOCK__BOUND_REFERENCE:
				return getBoundReference();
			case SysmlPackage.BLOCK__NESTED_BLOCK:
				return getNestedBlock();
			case SysmlPackage.BLOCK__INHERITED_BLOCK:
				return getInheritedBlock();
			case SysmlPackage.BLOCK__SATISFY:
				return getSatisfy();
			case SysmlPackage.BLOCK__CONNECTOR:
				return getConnector();
			case SysmlPackage.BLOCK__INTERNAL_BLOCK_DIAGRAM:
				return getInternalBlockDiagram();
			case SysmlPackage.BLOCK__PORT:
				return getPort();
			case SysmlPackage.BLOCK__PROXY_PORT:
				return getProxyPort();
			case SysmlPackage.BLOCK__FULL_PORT:
				return getFullPort();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SysmlPackage.BLOCK__OWNER:
				setOwner((NamedElement)newValue);
				return;
			case SysmlPackage.BLOCK__PART_PROPERTY:
				getPartProperty().clear();
				getPartProperty().addAll((Collection<? extends PartProperty>)newValue);
				return;
			case SysmlPackage.BLOCK__REFERENCE_PROPERTY:
				getReferenceProperty().clear();
				getReferenceProperty().addAll((Collection<? extends ReferenceProperty>)newValue);
				return;
			case SysmlPackage.BLOCK__FLOW_PROPERTY:
				getFlowProperty().clear();
				getFlowProperty().addAll((Collection<? extends FlowProperty>)newValue);
				return;
			case SysmlPackage.BLOCK__VALUE_PROPERTY:
				getValueProperty().clear();
				getValueProperty().addAll((Collection<? extends ValueProperty>)newValue);
				return;
			case SysmlPackage.BLOCK__BOUND_REFERENCE:
				getBoundReference().clear();
				getBoundReference().addAll((Collection<? extends BoundReference>)newValue);
				return;
			case SysmlPackage.BLOCK__NESTED_BLOCK:
				getNestedBlock().clear();
				getNestedBlock().addAll((Collection<? extends Block>)newValue);
				return;
			case SysmlPackage.BLOCK__INHERITED_BLOCK:
				getInheritedBlock().clear();
				getInheritedBlock().addAll((Collection<? extends Block>)newValue);
				return;
			case SysmlPackage.BLOCK__SATISFY:
				getSatisfy().clear();
				getSatisfy().addAll((Collection<? extends Requirement>)newValue);
				return;
			case SysmlPackage.BLOCK__CONNECTOR:
				getConnector().clear();
				getConnector().addAll((Collection<? extends Connector>)newValue);
				return;
			case SysmlPackage.BLOCK__INTERNAL_BLOCK_DIAGRAM:
				getInternalBlockDiagram().clear();
				getInternalBlockDiagram().addAll((Collection<? extends InternalBlockDiagram>)newValue);
				return;
			case SysmlPackage.BLOCK__PORT:
				getPort().clear();
				getPort().addAll((Collection<? extends Port>)newValue);
				return;
			case SysmlPackage.BLOCK__PROXY_PORT:
				getProxyPort().clear();
				getProxyPort().addAll((Collection<? extends ProxyPort>)newValue);
				return;
			case SysmlPackage.BLOCK__FULL_PORT:
				getFullPort().clear();
				getFullPort().addAll((Collection<? extends FullPort>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case SysmlPackage.BLOCK__OWNER:
				setOwner((NamedElement)null);
				return;
			case SysmlPackage.BLOCK__PART_PROPERTY:
				getPartProperty().clear();
				return;
			case SysmlPackage.BLOCK__REFERENCE_PROPERTY:
				getReferenceProperty().clear();
				return;
			case SysmlPackage.BLOCK__FLOW_PROPERTY:
				getFlowProperty().clear();
				return;
			case SysmlPackage.BLOCK__VALUE_PROPERTY:
				getValueProperty().clear();
				return;
			case SysmlPackage.BLOCK__BOUND_REFERENCE:
				getBoundReference().clear();
				return;
			case SysmlPackage.BLOCK__NESTED_BLOCK:
				getNestedBlock().clear();
				return;
			case SysmlPackage.BLOCK__INHERITED_BLOCK:
				getInheritedBlock().clear();
				return;
			case SysmlPackage.BLOCK__SATISFY:
				getSatisfy().clear();
				return;
			case SysmlPackage.BLOCK__CONNECTOR:
				getConnector().clear();
				return;
			case SysmlPackage.BLOCK__INTERNAL_BLOCK_DIAGRAM:
				getInternalBlockDiagram().clear();
				return;
			case SysmlPackage.BLOCK__PORT:
				getPort().clear();
				return;
			case SysmlPackage.BLOCK__PROXY_PORT:
				getProxyPort().clear();
				return;
			case SysmlPackage.BLOCK__FULL_PORT:
				getFullPort().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case SysmlPackage.BLOCK__OWNER:
				return owner != null;
			case SysmlPackage.BLOCK__PART_PROPERTY:
				return partProperty != null && !partProperty.isEmpty();
			case SysmlPackage.BLOCK__REFERENCE_PROPERTY:
				return referenceProperty != null && !referenceProperty.isEmpty();
			case SysmlPackage.BLOCK__FLOW_PROPERTY:
				return flowProperty != null && !flowProperty.isEmpty();
			case SysmlPackage.BLOCK__VALUE_PROPERTY:
				return valueProperty != null && !valueProperty.isEmpty();
			case SysmlPackage.BLOCK__BOUND_REFERENCE:
				return boundReference != null && !boundReference.isEmpty();
			case SysmlPackage.BLOCK__NESTED_BLOCK:
				return nestedBlock != null && !nestedBlock.isEmpty();
			case SysmlPackage.BLOCK__INHERITED_BLOCK:
				return inheritedBlock != null && !inheritedBlock.isEmpty();
			case SysmlPackage.BLOCK__SATISFY:
				return satisfy != null && !satisfy.isEmpty();
			case SysmlPackage.BLOCK__CONNECTOR:
				return connector != null && !connector.isEmpty();
			case SysmlPackage.BLOCK__INTERNAL_BLOCK_DIAGRAM:
				return internalBlockDiagram != null && !internalBlockDiagram.isEmpty();
			case SysmlPackage.BLOCK__PORT:
				return port != null && !port.isEmpty();
			case SysmlPackage.BLOCK__PROXY_PORT:
				return proxyPort != null && !proxyPort.isEmpty();
			case SysmlPackage.BLOCK__FULL_PORT:
				return fullPort != null && !fullPort.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == Type.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == OwnedElement.class) {
			switch (derivedFeatureID) {
				case SysmlPackage.BLOCK__OWNER: return SysmlPackage.OWNED_ELEMENT__OWNER;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == Type.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == OwnedElement.class) {
			switch (baseFeatureID) {
				case SysmlPackage.OWNED_ELEMENT__OWNER: return SysmlPackage.BLOCK__OWNER;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} //BlockImpl
