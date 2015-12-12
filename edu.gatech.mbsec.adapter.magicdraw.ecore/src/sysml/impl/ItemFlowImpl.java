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
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import sysml.Block;
import sysml.Connector;
import sysml.ItemFlow;
import sysml.Property;
import sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Item Flow</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link sysml.impl.ItemFlowImpl#getItemProperty <em>Item Property</em>}</li>
 *   <li>{@link sysml.impl.ItemFlowImpl#getInformationTarget <em>Information Target</em>}</li>
 *   <li>{@link sysml.impl.ItemFlowImpl#getInformationSource <em>Information Source</em>}</li>
 *   <li>{@link sysml.impl.ItemFlowImpl#getRealizingConnector <em>Realizing Connector</em>}</li>
 *   <li>{@link sysml.impl.ItemFlowImpl#getConveyedBlock <em>Conveyed Block</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ItemFlowImpl extends MinimalEObjectImpl.Container implements ItemFlow {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "*********************************************************************************************\r\nCopyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.\r\n                                 http://www.mbse.gatech.edu/\r\n                      http://www.mbsec.gatech.edu/research/oslc\r\n\r\n  All rights reserved. This program and the accompanying materials\r\n  are made available under the terms of the Eclipse Public License v1.0\r\n  and Eclipse Distribution License v. 1.0 which accompanies this distribution.\r\n  \r\n  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html\r\n  and the Eclipse Distribution License is available at\r\n  http://www.eclipse.org/org/documents/edl-v10.php.\r\n  \r\n  Contributors:\r\n  \r\n       Axel Reichwein, Koneksys (axel.reichwein@koneksys.com)        \r\n*******************************************************************************************";

	/**
	 * The cached value of the '{@link #getItemProperty() <em>Item Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getItemProperty()
	 * @generated
	 * @ordered
	 */
	protected Property itemProperty;

	/**
	 * The cached value of the '{@link #getInformationTarget() <em>Information Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInformationTarget()
	 * @generated
	 * @ordered
	 */
	protected Property informationTarget;

	/**
	 * The cached value of the '{@link #getInformationSource() <em>Information Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInformationSource()
	 * @generated
	 * @ordered
	 */
	protected Property informationSource;

	/**
	 * The cached value of the '{@link #getRealizingConnector() <em>Realizing Connector</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRealizingConnector()
	 * @generated
	 * @ordered
	 */
	protected Connector realizingConnector;

	/**
	 * The cached value of the '{@link #getConveyedBlock() <em>Conveyed Block</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConveyedBlock()
	 * @generated
	 * @ordered
	 */
	protected EList<Block> conveyedBlock;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ItemFlowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SysmlPackage.Literals.ITEM_FLOW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Property getItemProperty() {
		if (itemProperty != null && itemProperty.eIsProxy()) {
			InternalEObject oldItemProperty = (InternalEObject)itemProperty;
			itemProperty = (Property)eResolveProxy(oldItemProperty);
			if (itemProperty != oldItemProperty) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.ITEM_FLOW__ITEM_PROPERTY, oldItemProperty, itemProperty));
			}
		}
		return itemProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Property basicGetItemProperty() {
		return itemProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setItemProperty(Property newItemProperty) {
		Property oldItemProperty = itemProperty;
		itemProperty = newItemProperty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ITEM_FLOW__ITEM_PROPERTY, oldItemProperty, itemProperty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Property getInformationTarget() {
		if (informationTarget != null && informationTarget.eIsProxy()) {
			InternalEObject oldInformationTarget = (InternalEObject)informationTarget;
			informationTarget = (Property)eResolveProxy(oldInformationTarget);
			if (informationTarget != oldInformationTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.ITEM_FLOW__INFORMATION_TARGET, oldInformationTarget, informationTarget));
			}
		}
		return informationTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Property basicGetInformationTarget() {
		return informationTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInformationTarget(Property newInformationTarget) {
		Property oldInformationTarget = informationTarget;
		informationTarget = newInformationTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ITEM_FLOW__INFORMATION_TARGET, oldInformationTarget, informationTarget));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Property getInformationSource() {
		if (informationSource != null && informationSource.eIsProxy()) {
			InternalEObject oldInformationSource = (InternalEObject)informationSource;
			informationSource = (Property)eResolveProxy(oldInformationSource);
			if (informationSource != oldInformationSource) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.ITEM_FLOW__INFORMATION_SOURCE, oldInformationSource, informationSource));
			}
		}
		return informationSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Property basicGetInformationSource() {
		return informationSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInformationSource(Property newInformationSource) {
		Property oldInformationSource = informationSource;
		informationSource = newInformationSource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ITEM_FLOW__INFORMATION_SOURCE, oldInformationSource, informationSource));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Connector getRealizingConnector() {
		if (realizingConnector != null && realizingConnector.eIsProxy()) {
			InternalEObject oldRealizingConnector = (InternalEObject)realizingConnector;
			realizingConnector = (Connector)eResolveProxy(oldRealizingConnector);
			if (realizingConnector != oldRealizingConnector) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.ITEM_FLOW__REALIZING_CONNECTOR, oldRealizingConnector, realizingConnector));
			}
		}
		return realizingConnector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Connector basicGetRealizingConnector() {
		return realizingConnector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRealizingConnector(Connector newRealizingConnector) {
		Connector oldRealizingConnector = realizingConnector;
		realizingConnector = newRealizingConnector;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ITEM_FLOW__REALIZING_CONNECTOR, oldRealizingConnector, realizingConnector));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Block> getConveyedBlock() {
		if (conveyedBlock == null) {
			conveyedBlock = new EObjectResolvingEList<Block>(Block.class, this, SysmlPackage.ITEM_FLOW__CONVEYED_BLOCK);
		}
		return conveyedBlock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SysmlPackage.ITEM_FLOW__ITEM_PROPERTY:
				if (resolve) return getItemProperty();
				return basicGetItemProperty();
			case SysmlPackage.ITEM_FLOW__INFORMATION_TARGET:
				if (resolve) return getInformationTarget();
				return basicGetInformationTarget();
			case SysmlPackage.ITEM_FLOW__INFORMATION_SOURCE:
				if (resolve) return getInformationSource();
				return basicGetInformationSource();
			case SysmlPackage.ITEM_FLOW__REALIZING_CONNECTOR:
				if (resolve) return getRealizingConnector();
				return basicGetRealizingConnector();
			case SysmlPackage.ITEM_FLOW__CONVEYED_BLOCK:
				return getConveyedBlock();
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
			case SysmlPackage.ITEM_FLOW__ITEM_PROPERTY:
				setItemProperty((Property)newValue);
				return;
			case SysmlPackage.ITEM_FLOW__INFORMATION_TARGET:
				setInformationTarget((Property)newValue);
				return;
			case SysmlPackage.ITEM_FLOW__INFORMATION_SOURCE:
				setInformationSource((Property)newValue);
				return;
			case SysmlPackage.ITEM_FLOW__REALIZING_CONNECTOR:
				setRealizingConnector((Connector)newValue);
				return;
			case SysmlPackage.ITEM_FLOW__CONVEYED_BLOCK:
				getConveyedBlock().clear();
				getConveyedBlock().addAll((Collection<? extends Block>)newValue);
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
			case SysmlPackage.ITEM_FLOW__ITEM_PROPERTY:
				setItemProperty((Property)null);
				return;
			case SysmlPackage.ITEM_FLOW__INFORMATION_TARGET:
				setInformationTarget((Property)null);
				return;
			case SysmlPackage.ITEM_FLOW__INFORMATION_SOURCE:
				setInformationSource((Property)null);
				return;
			case SysmlPackage.ITEM_FLOW__REALIZING_CONNECTOR:
				setRealizingConnector((Connector)null);
				return;
			case SysmlPackage.ITEM_FLOW__CONVEYED_BLOCK:
				getConveyedBlock().clear();
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
			case SysmlPackage.ITEM_FLOW__ITEM_PROPERTY:
				return itemProperty != null;
			case SysmlPackage.ITEM_FLOW__INFORMATION_TARGET:
				return informationTarget != null;
			case SysmlPackage.ITEM_FLOW__INFORMATION_SOURCE:
				return informationSource != null;
			case SysmlPackage.ITEM_FLOW__REALIZING_CONNECTOR:
				return realizingConnector != null;
			case SysmlPackage.ITEM_FLOW__CONVEYED_BLOCK:
				return conveyedBlock != null && !conveyedBlock.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ItemFlowImpl
