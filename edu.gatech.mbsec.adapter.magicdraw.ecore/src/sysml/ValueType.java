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
package sysml;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link sysml.ValueType#getUnit <em>Unit</em>}</li>
 *   <li>{@link sysml.ValueType#getQuantityKind <em>Quantity Kind</em>}</li>
 * </ul>
 * </p>
 *
 * @see sysml.SysmlPackage#getValueType()
 * @model
 * @generated
 */
public interface ValueType extends Type, NamedElement {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "*********************************************************************************************\r\nCopyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.\r\n                                 http://www.mbse.gatech.edu/\r\n                      http://www.mbsec.gatech.edu/research/oslc\r\n\r\n  All rights reserved. This program and the accompanying materials\r\n  are made available under the terms of the Eclipse Public License v1.0\r\n  and Eclipse Distribution License v. 1.0 which accompanies this distribution.\r\n  \r\n  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html\r\n  and the Eclipse Distribution License is available at\r\n  http://www.eclipse.org/org/documents/edl-v10.php.\r\n  \r\n  Contributors:\r\n  \r\n       Axel Reichwein, Koneksys (axel.reichwein@koneksys.com)        \r\n*******************************************************************************************";

	/**
	 * Returns the value of the '<em><b>Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unit</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unit</em>' reference.
	 * @see #setUnit(Unit)
	 * @see sysml.SysmlPackage#getValueType_Unit()
	 * @model
	 * @generated
	 */
	Unit getUnit();

	/**
	 * Sets the value of the '{@link sysml.ValueType#getUnit <em>Unit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit</em>' reference.
	 * @see #getUnit()
	 * @generated
	 */
	void setUnit(Unit value);

	/**
	 * Returns the value of the '<em><b>Quantity Kind</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Quantity Kind</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Quantity Kind</em>' reference.
	 * @see #setQuantityKind(QuantityKind)
	 * @see sysml.SysmlPackage#getValueType_QuantityKind()
	 * @model
	 * @generated
	 */
	QuantityKind getQuantityKind();

	/**
	 * Sets the value of the '{@link sysml.ValueType#getQuantityKind <em>Quantity Kind</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Quantity Kind</em>' reference.
	 * @see #getQuantityKind()
	 * @generated
	 */
	void setQuantityKind(QuantityKind value);

} // ValueType
