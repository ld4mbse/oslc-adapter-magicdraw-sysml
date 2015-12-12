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

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import sysml.QuantityKind;
import sysml.SysmlPackage;
import sysml.Unit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Unit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link sysml.impl.UnitImpl#getSymbol <em>Symbol</em>}</li>
 *   <li>{@link sysml.impl.UnitImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link sysml.impl.UnitImpl#getDefinitionURI <em>Definition URI</em>}</li>
 *   <li>{@link sysml.impl.UnitImpl#getQuantityKind <em>Quantity Kind</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UnitImpl extends NamedElementImpl implements Unit {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "*********************************************************************************************\r\nCopyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.\r\n                                 http://www.mbse.gatech.edu/\r\n                      http://www.mbsec.gatech.edu/research/oslc\r\n\r\n  All rights reserved. This program and the accompanying materials\r\n  are made available under the terms of the Eclipse Public License v1.0\r\n  and Eclipse Distribution License v. 1.0 which accompanies this distribution.\r\n  \r\n  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html\r\n  and the Eclipse Distribution License is available at\r\n  http://www.eclipse.org/org/documents/edl-v10.php.\r\n  \r\n  Contributors:\r\n  \r\n       Axel Reichwein, Koneksys (axel.reichwein@koneksys.com)        \r\n*******************************************************************************************";

	/**
	 * The default value of the '{@link #getSymbol() <em>Symbol</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSymbol()
	 * @generated
	 * @ordered
	 */
	protected static final String SYMBOL_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getSymbol() <em>Symbol</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSymbol()
	 * @generated
	 * @ordered
	 */
	protected String symbol = SYMBOL_EDEFAULT;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefinitionURI() <em>Definition URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefinitionURI()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFINITION_URI_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getDefinitionURI() <em>Definition URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefinitionURI()
	 * @generated
	 * @ordered
	 */
	protected String definitionURI = DEFINITION_URI_EDEFAULT;

	/**
	 * The cached value of the '{@link #getQuantityKind() <em>Quantity Kind</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuantityKind()
	 * @generated
	 * @ordered
	 */
	protected QuantityKind quantityKind;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UnitImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SysmlPackage.Literals.UNIT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSymbol(String newSymbol) {
		String oldSymbol = symbol;
		symbol = newSymbol;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.UNIT__SYMBOL, oldSymbol, symbol));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.UNIT__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefinitionURI() {
		return definitionURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefinitionURI(String newDefinitionURI) {
		String oldDefinitionURI = definitionURI;
		definitionURI = newDefinitionURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.UNIT__DEFINITION_URI, oldDefinitionURI, definitionURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QuantityKind getQuantityKind() {
		if (quantityKind != null && quantityKind.eIsProxy()) {
			InternalEObject oldQuantityKind = (InternalEObject)quantityKind;
			quantityKind = (QuantityKind)eResolveProxy(oldQuantityKind);
			if (quantityKind != oldQuantityKind) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.UNIT__QUANTITY_KIND, oldQuantityKind, quantityKind));
			}
		}
		return quantityKind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QuantityKind basicGetQuantityKind() {
		return quantityKind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setQuantityKind(QuantityKind newQuantityKind) {
		QuantityKind oldQuantityKind = quantityKind;
		quantityKind = newQuantityKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.UNIT__QUANTITY_KIND, oldQuantityKind, quantityKind));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SysmlPackage.UNIT__SYMBOL:
				return getSymbol();
			case SysmlPackage.UNIT__DESCRIPTION:
				return getDescription();
			case SysmlPackage.UNIT__DEFINITION_URI:
				return getDefinitionURI();
			case SysmlPackage.UNIT__QUANTITY_KIND:
				if (resolve) return getQuantityKind();
				return basicGetQuantityKind();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SysmlPackage.UNIT__SYMBOL:
				setSymbol((String)newValue);
				return;
			case SysmlPackage.UNIT__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case SysmlPackage.UNIT__DEFINITION_URI:
				setDefinitionURI((String)newValue);
				return;
			case SysmlPackage.UNIT__QUANTITY_KIND:
				setQuantityKind((QuantityKind)newValue);
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
			case SysmlPackage.UNIT__SYMBOL:
				setSymbol(SYMBOL_EDEFAULT);
				return;
			case SysmlPackage.UNIT__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case SysmlPackage.UNIT__DEFINITION_URI:
				setDefinitionURI(DEFINITION_URI_EDEFAULT);
				return;
			case SysmlPackage.UNIT__QUANTITY_KIND:
				setQuantityKind((QuantityKind)null);
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
			case SysmlPackage.UNIT__SYMBOL:
				return SYMBOL_EDEFAULT == null ? symbol != null : !SYMBOL_EDEFAULT.equals(symbol);
			case SysmlPackage.UNIT__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case SysmlPackage.UNIT__DEFINITION_URI:
				return DEFINITION_URI_EDEFAULT == null ? definitionURI != null : !DEFINITION_URI_EDEFAULT.equals(definitionURI);
			case SysmlPackage.UNIT__QUANTITY_KIND:
				return quantityKind != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (symbol: ");
		result.append(symbol);
		result.append(", description: ");
		result.append(description);
		result.append(", definitionURI: ");
		result.append(definitionURI);
		result.append(')');
		return result.toString();
	}

} //UnitImpl
