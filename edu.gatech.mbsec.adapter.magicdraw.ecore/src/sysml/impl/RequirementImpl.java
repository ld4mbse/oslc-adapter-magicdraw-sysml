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

import sysml.NamedElement;
import sysml.Requirement;
import sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Requirement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link sysml.impl.RequirementImpl#getSatisfiedBy <em>Satisfied By</em>}</li>
 *   <li>{@link sysml.impl.RequirementImpl#getMaster <em>Master</em>}</li>
 *   <li>{@link sysml.impl.RequirementImpl#getRefines <em>Refines</em>}</li>
 *   <li>{@link sysml.impl.RequirementImpl#getId <em>Id</em>}</li>
 *   <li>{@link sysml.impl.RequirementImpl#getText <em>Text</em>}</li>
 *   <li>{@link sysml.impl.RequirementImpl#getDerived <em>Derived</em>}</li>
 *   <li>{@link sysml.impl.RequirementImpl#getSubRequirement <em>Sub Requirement</em>}</li>
 *   <li>{@link sysml.impl.RequirementImpl#getName <em>Name</em>}</li>
 *   <li>{@link sysml.impl.RequirementImpl#getDerivedFrom <em>Derived From</em>}</li>
 *   <li>{@link sysml.impl.RequirementImpl#getHyperlink <em>Hyperlink</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RequirementImpl extends MinimalEObjectImpl.Container implements Requirement {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "*********************************************************************************************\r\nCopyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.\r\n                                 http://www.mbse.gatech.edu/\r\n                      http://www.mbsec.gatech.edu/research/oslc\r\n\r\n  All rights reserved. This program and the accompanying materials\r\n  are made available under the terms of the Eclipse Public License v1.0\r\n  and Eclipse Distribution License v. 1.0 which accompanies this distribution.\r\n  \r\n  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html\r\n  and the Eclipse Distribution License is available at\r\n  http://www.eclipse.org/org/documents/edl-v10.php.\r\n  \r\n  Contributors:\r\n  \r\n       Axel Reichwein, Koneksys (axel.reichwein@koneksys.com)        \r\n*******************************************************************************************";

	/**
	 * The cached value of the '{@link #getSatisfiedBy() <em>Satisfied By</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSatisfiedBy()
	 * @generated
	 * @ordered
	 */
	protected EList<NamedElement> satisfiedBy;

	/**
	 * The cached value of the '{@link #getMaster() <em>Master</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaster()
	 * @generated
	 * @ordered
	 */
	protected Requirement master;

	/**
	 * The cached value of the '{@link #getRefines() <em>Refines</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRefines()
	 * @generated
	 * @ordered
	 */
	protected EList<NamedElement> refines;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected static final String TEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected String text = TEXT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDerived() <em>Derived</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDerived()
	 * @generated
	 * @ordered
	 */
	protected EList<Requirement> derived;

	/**
	 * The cached value of the '{@link #getSubRequirement() <em>Sub Requirement</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubRequirement()
	 * @generated
	 * @ordered
	 */
	protected EList<Requirement> subRequirement;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDerivedFrom() <em>Derived From</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDerivedFrom()
	 * @generated
	 * @ordered
	 */
	protected EList<Requirement> derivedFrom;

	/**
	 * The default value of the '{@link #getHyperlink() <em>Hyperlink</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHyperlink()
	 * @generated
	 * @ordered
	 */
	protected static final String HYPERLINK_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHyperlink() <em>Hyperlink</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHyperlink()
	 * @generated
	 * @ordered
	 */
	protected String hyperlink = HYPERLINK_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RequirementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SysmlPackage.Literals.REQUIREMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NamedElement> getSatisfiedBy() {
		if (satisfiedBy == null) {
			satisfiedBy = new EObjectResolvingEList<NamedElement>(NamedElement.class, this, SysmlPackage.REQUIREMENT__SATISFIED_BY);
		}
		return satisfiedBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Requirement getMaster() {
		if (master != null && master.eIsProxy()) {
			InternalEObject oldMaster = (InternalEObject)master;
			master = (Requirement)eResolveProxy(oldMaster);
			if (master != oldMaster) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.REQUIREMENT__MASTER, oldMaster, master));
			}
		}
		return master;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Requirement basicGetMaster() {
		return master;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaster(Requirement newMaster) {
		Requirement oldMaster = master;
		master = newMaster;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.REQUIREMENT__MASTER, oldMaster, master));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NamedElement> getRefines() {
		if (refines == null) {
			refines = new EObjectResolvingEList<NamedElement>(NamedElement.class, this, SysmlPackage.REQUIREMENT__REFINES);
		}
		return refines;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.REQUIREMENT__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getText() {
		return text;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setText(String newText) {
		String oldText = text;
		text = newText;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.REQUIREMENT__TEXT, oldText, text));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Requirement> getDerived() {
		if (derived == null) {
			derived = new EObjectResolvingEList<Requirement>(Requirement.class, this, SysmlPackage.REQUIREMENT__DERIVED);
		}
		return derived;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Requirement> getSubRequirement() {
		if (subRequirement == null) {
			subRequirement = new EObjectResolvingEList<Requirement>(Requirement.class, this, SysmlPackage.REQUIREMENT__SUB_REQUIREMENT);
		}
		return subRequirement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.REQUIREMENT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Requirement> getDerivedFrom() {
		if (derivedFrom == null) {
			derivedFrom = new EObjectResolvingEList<Requirement>(Requirement.class, this, SysmlPackage.REQUIREMENT__DERIVED_FROM);
		}
		return derivedFrom;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getHyperlink() {
		return hyperlink;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHyperlink(String newHyperlink) {
		String oldHyperlink = hyperlink;
		hyperlink = newHyperlink;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.REQUIREMENT__HYPERLINK, oldHyperlink, hyperlink));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SysmlPackage.REQUIREMENT__SATISFIED_BY:
				return getSatisfiedBy();
			case SysmlPackage.REQUIREMENT__MASTER:
				if (resolve) return getMaster();
				return basicGetMaster();
			case SysmlPackage.REQUIREMENT__REFINES:
				return getRefines();
			case SysmlPackage.REQUIREMENT__ID:
				return getId();
			case SysmlPackage.REQUIREMENT__TEXT:
				return getText();
			case SysmlPackage.REQUIREMENT__DERIVED:
				return getDerived();
			case SysmlPackage.REQUIREMENT__SUB_REQUIREMENT:
				return getSubRequirement();
			case SysmlPackage.REQUIREMENT__NAME:
				return getName();
			case SysmlPackage.REQUIREMENT__DERIVED_FROM:
				return getDerivedFrom();
			case SysmlPackage.REQUIREMENT__HYPERLINK:
				return getHyperlink();
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
			case SysmlPackage.REQUIREMENT__SATISFIED_BY:
				getSatisfiedBy().clear();
				getSatisfiedBy().addAll((Collection<? extends NamedElement>)newValue);
				return;
			case SysmlPackage.REQUIREMENT__MASTER:
				setMaster((Requirement)newValue);
				return;
			case SysmlPackage.REQUIREMENT__REFINES:
				getRefines().clear();
				getRefines().addAll((Collection<? extends NamedElement>)newValue);
				return;
			case SysmlPackage.REQUIREMENT__ID:
				setId((String)newValue);
				return;
			case SysmlPackage.REQUIREMENT__TEXT:
				setText((String)newValue);
				return;
			case SysmlPackage.REQUIREMENT__DERIVED:
				getDerived().clear();
				getDerived().addAll((Collection<? extends Requirement>)newValue);
				return;
			case SysmlPackage.REQUIREMENT__SUB_REQUIREMENT:
				getSubRequirement().clear();
				getSubRequirement().addAll((Collection<? extends Requirement>)newValue);
				return;
			case SysmlPackage.REQUIREMENT__NAME:
				setName((String)newValue);
				return;
			case SysmlPackage.REQUIREMENT__DERIVED_FROM:
				getDerivedFrom().clear();
				getDerivedFrom().addAll((Collection<? extends Requirement>)newValue);
				return;
			case SysmlPackage.REQUIREMENT__HYPERLINK:
				setHyperlink((String)newValue);
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
			case SysmlPackage.REQUIREMENT__SATISFIED_BY:
				getSatisfiedBy().clear();
				return;
			case SysmlPackage.REQUIREMENT__MASTER:
				setMaster((Requirement)null);
				return;
			case SysmlPackage.REQUIREMENT__REFINES:
				getRefines().clear();
				return;
			case SysmlPackage.REQUIREMENT__ID:
				setId(ID_EDEFAULT);
				return;
			case SysmlPackage.REQUIREMENT__TEXT:
				setText(TEXT_EDEFAULT);
				return;
			case SysmlPackage.REQUIREMENT__DERIVED:
				getDerived().clear();
				return;
			case SysmlPackage.REQUIREMENT__SUB_REQUIREMENT:
				getSubRequirement().clear();
				return;
			case SysmlPackage.REQUIREMENT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SysmlPackage.REQUIREMENT__DERIVED_FROM:
				getDerivedFrom().clear();
				return;
			case SysmlPackage.REQUIREMENT__HYPERLINK:
				setHyperlink(HYPERLINK_EDEFAULT);
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
			case SysmlPackage.REQUIREMENT__SATISFIED_BY:
				return satisfiedBy != null && !satisfiedBy.isEmpty();
			case SysmlPackage.REQUIREMENT__MASTER:
				return master != null;
			case SysmlPackage.REQUIREMENT__REFINES:
				return refines != null && !refines.isEmpty();
			case SysmlPackage.REQUIREMENT__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case SysmlPackage.REQUIREMENT__TEXT:
				return TEXT_EDEFAULT == null ? text != null : !TEXT_EDEFAULT.equals(text);
			case SysmlPackage.REQUIREMENT__DERIVED:
				return derived != null && !derived.isEmpty();
			case SysmlPackage.REQUIREMENT__SUB_REQUIREMENT:
				return subRequirement != null && !subRequirement.isEmpty();
			case SysmlPackage.REQUIREMENT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SysmlPackage.REQUIREMENT__DERIVED_FROM:
				return derivedFrom != null && !derivedFrom.isEmpty();
			case SysmlPackage.REQUIREMENT__HYPERLINK:
				return HYPERLINK_EDEFAULT == null ? hyperlink != null : !HYPERLINK_EDEFAULT.equals(hyperlink);
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
		result.append(" (id: ");
		result.append(id);
		result.append(", text: ");
		result.append(text);
		result.append(", name: ");
		result.append(name);
		result.append(", hyperlink: ");
		result.append(hyperlink);
		result.append(')');
		return result.toString();
	}

} //RequirementImpl
