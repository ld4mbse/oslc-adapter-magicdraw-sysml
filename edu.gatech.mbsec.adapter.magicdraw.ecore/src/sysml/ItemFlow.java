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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Item Flow</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link sysml.ItemFlow#getItemProperty <em>Item Property</em>}</li>
 *   <li>{@link sysml.ItemFlow#getInformationTarget <em>Information Target</em>}</li>
 *   <li>{@link sysml.ItemFlow#getInformationSource <em>Information Source</em>}</li>
 *   <li>{@link sysml.ItemFlow#getRealizingConnector <em>Realizing Connector</em>}</li>
 *   <li>{@link sysml.ItemFlow#getConveyedBlock <em>Conveyed Block</em>}</li>
 * </ul>
 * </p>
 *
 * @see sysml.SysmlPackage#getItemFlow()
 * @model
 * @generated
 */
public interface ItemFlow extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "*********************************************************************************************\r\nCopyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.\r\n                                 http://www.mbse.gatech.edu/\r\n                      http://www.mbsec.gatech.edu/research/oslc\r\n\r\n  All rights reserved. This program and the accompanying materials\r\n  are made available under the terms of the Eclipse Public License v1.0\r\n  and Eclipse Distribution License v. 1.0 which accompanies this distribution.\r\n  \r\n  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html\r\n  and the Eclipse Distribution License is available at\r\n  http://www.eclipse.org/org/documents/edl-v10.php.\r\n  \r\n  Contributors:\r\n  \r\n       Axel Reichwein, Koneksys (axel.reichwein@koneksys.com)        \r\n*******************************************************************************************";

	/**
	 * Returns the value of the '<em><b>Item Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Item Property</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Item Property</em>' reference.
	 * @see #setItemProperty(Property)
	 * @see sysml.SysmlPackage#getItemFlow_ItemProperty()
	 * @model
	 * @generated
	 */
	Property getItemProperty();

	/**
	 * Sets the value of the '{@link sysml.ItemFlow#getItemProperty <em>Item Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Item Property</em>' reference.
	 * @see #getItemProperty()
	 * @generated
	 */
	void setItemProperty(Property value);

	/**
	 * Returns the value of the '<em><b>Information Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Information Target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Information Target</em>' reference.
	 * @see #setInformationTarget(Property)
	 * @see sysml.SysmlPackage#getItemFlow_InformationTarget()
	 * @model required="true"
	 * @generated
	 */
	Property getInformationTarget();

	/**
	 * Sets the value of the '{@link sysml.ItemFlow#getInformationTarget <em>Information Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Information Target</em>' reference.
	 * @see #getInformationTarget()
	 * @generated
	 */
	void setInformationTarget(Property value);

	/**
	 * Returns the value of the '<em><b>Information Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Information Source</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Information Source</em>' reference.
	 * @see #setInformationSource(Property)
	 * @see sysml.SysmlPackage#getItemFlow_InformationSource()
	 * @model required="true"
	 * @generated
	 */
	Property getInformationSource();

	/**
	 * Sets the value of the '{@link sysml.ItemFlow#getInformationSource <em>Information Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Information Source</em>' reference.
	 * @see #getInformationSource()
	 * @generated
	 */
	void setInformationSource(Property value);

	/**
	 * Returns the value of the '<em><b>Realizing Connector</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Realizing Connector</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Realizing Connector</em>' reference.
	 * @see #setRealizingConnector(Connector)
	 * @see sysml.SysmlPackage#getItemFlow_RealizingConnector()
	 * @model required="true"
	 * @generated
	 */
	Connector getRealizingConnector();

	/**
	 * Sets the value of the '{@link sysml.ItemFlow#getRealizingConnector <em>Realizing Connector</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Realizing Connector</em>' reference.
	 * @see #getRealizingConnector()
	 * @generated
	 */
	void setRealizingConnector(Connector value);

	/**
	 * Returns the value of the '<em><b>Conveyed Block</b></em>' reference list.
	 * The list contents are of type {@link sysml.Block}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Conveyed Block</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Conveyed Block</em>' reference list.
	 * @see sysml.SysmlPackage#getItemFlow_ConveyedBlock()
	 * @model
	 * @generated
	 */
	EList<Block> getConveyedBlock();

} // ItemFlow
