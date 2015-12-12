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

import sysml.FlowProperty;
import sysml.InterfaceBlock;
import sysml.NamedElement;
import sysml.OwnedElement;
import sysml.ProxyPort;
import sysml.SysmlPackage;
import sysml.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Interface Block</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link sysml.impl.InterfaceBlockImpl#getOwner <em>Owner</em>}</li>
 *   <li>{@link sysml.impl.InterfaceBlockImpl#getFlowProperty <em>Flow Property</em>}</li>
 *   <li>{@link sysml.impl.InterfaceBlockImpl#getProxyPort <em>Proxy Port</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InterfaceBlockImpl extends NamedElementImpl implements InterfaceBlock {
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
	 * The cached value of the '{@link #getFlowProperty() <em>Flow Property</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlowProperty()
	 * @generated
	 * @ordered
	 */
	protected EList<FlowProperty> flowProperty;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InterfaceBlockImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SysmlPackage.Literals.INTERFACE_BLOCK;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.INTERFACE_BLOCK__OWNER, oldOwner, owner));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.INTERFACE_BLOCK__OWNER, oldOwner, owner));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FlowProperty> getFlowProperty() {
		if (flowProperty == null) {
			flowProperty = new EObjectResolvingEList<FlowProperty>(FlowProperty.class, this, SysmlPackage.INTERFACE_BLOCK__FLOW_PROPERTY);
		}
		return flowProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ProxyPort> getProxyPort() {
		if (proxyPort == null) {
			proxyPort = new EObjectResolvingEList<ProxyPort>(ProxyPort.class, this, SysmlPackage.INTERFACE_BLOCK__PROXY_PORT);
		}
		return proxyPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SysmlPackage.INTERFACE_BLOCK__OWNER:
				if (resolve) return getOwner();
				return basicGetOwner();
			case SysmlPackage.INTERFACE_BLOCK__FLOW_PROPERTY:
				return getFlowProperty();
			case SysmlPackage.INTERFACE_BLOCK__PROXY_PORT:
				return getProxyPort();
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
			case SysmlPackage.INTERFACE_BLOCK__OWNER:
				setOwner((NamedElement)newValue);
				return;
			case SysmlPackage.INTERFACE_BLOCK__FLOW_PROPERTY:
				getFlowProperty().clear();
				getFlowProperty().addAll((Collection<? extends FlowProperty>)newValue);
				return;
			case SysmlPackage.INTERFACE_BLOCK__PROXY_PORT:
				getProxyPort().clear();
				getProxyPort().addAll((Collection<? extends ProxyPort>)newValue);
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
			case SysmlPackage.INTERFACE_BLOCK__OWNER:
				setOwner((NamedElement)null);
				return;
			case SysmlPackage.INTERFACE_BLOCK__FLOW_PROPERTY:
				getFlowProperty().clear();
				return;
			case SysmlPackage.INTERFACE_BLOCK__PROXY_PORT:
				getProxyPort().clear();
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
			case SysmlPackage.INTERFACE_BLOCK__OWNER:
				return owner != null;
			case SysmlPackage.INTERFACE_BLOCK__FLOW_PROPERTY:
				return flowProperty != null && !flowProperty.isEmpty();
			case SysmlPackage.INTERFACE_BLOCK__PROXY_PORT:
				return proxyPort != null && !proxyPort.isEmpty();
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
		if (baseClass == OwnedElement.class) {
			switch (derivedFeatureID) {
				case SysmlPackage.INTERFACE_BLOCK__OWNER: return SysmlPackage.OWNED_ELEMENT__OWNER;
				default: return -1;
			}
		}
		if (baseClass == Type.class) {
			switch (derivedFeatureID) {
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
		if (baseClass == OwnedElement.class) {
			switch (baseFeatureID) {
				case SysmlPackage.OWNED_ELEMENT__OWNER: return SysmlPackage.INTERFACE_BLOCK__OWNER;
				default: return -1;
			}
		}
		if (baseClass == Type.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} //InterfaceBlockImpl
