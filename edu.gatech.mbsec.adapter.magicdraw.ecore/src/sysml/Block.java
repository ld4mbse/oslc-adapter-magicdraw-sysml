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

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Block</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link sysml.Block#getPartProperty <em>Part Property</em>}</li>
 *   <li>{@link sysml.Block#getReferenceProperty <em>Reference Property</em>}</li>
 *   <li>{@link sysml.Block#getFlowProperty <em>Flow Property</em>}</li>
 *   <li>{@link sysml.Block#getValueProperty <em>Value Property</em>}</li>
 *   <li>{@link sysml.Block#getBoundReference <em>Bound Reference</em>}</li>
 *   <li>{@link sysml.Block#getNestedBlock <em>Nested Block</em>}</li>
 *   <li>{@link sysml.Block#getInheritedBlock <em>Inherited Block</em>}</li>
 *   <li>{@link sysml.Block#getSatisfy <em>Satisfy</em>}</li>
 *   <li>{@link sysml.Block#getConnector <em>Connector</em>}</li>
 *   <li>{@link sysml.Block#getInternalBlockDiagram <em>Internal Block Diagram</em>}</li>
 *   <li>{@link sysml.Block#getPort <em>Port</em>}</li>
 *   <li>{@link sysml.Block#getProxyPort <em>Proxy Port</em>}</li>
 *   <li>{@link sysml.Block#getFullPort <em>Full Port</em>}</li>
 * </ul>
 * </p>
 *
 * @see sysml.SysmlPackage#getBlock()
 * @model
 * @generated
 */
public interface Block extends NamedElement, Type, OwnedElement {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "*********************************************************************************************\r\nCopyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.\r\n                                 http://www.mbse.gatech.edu/\r\n                      http://www.mbsec.gatech.edu/research/oslc\r\n\r\n  All rights reserved. This program and the accompanying materials\r\n  are made available under the terms of the Eclipse Public License v1.0\r\n  and Eclipse Distribution License v. 1.0 which accompanies this distribution.\r\n  \r\n  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html\r\n  and the Eclipse Distribution License is available at\r\n  http://www.eclipse.org/org/documents/edl-v10.php.\r\n  \r\n  Contributors:\r\n  \r\n       Axel Reichwein, Koneksys (axel.reichwein@koneksys.com)        \r\n*******************************************************************************************";

	/**
	 * Returns the value of the '<em><b>Part Property</b></em>' reference list.
	 * The list contents are of type {@link sysml.PartProperty}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Part Property</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Part Property</em>' reference list.
	 * @see sysml.SysmlPackage#getBlock_PartProperty()
	 * @model
	 * @generated
	 */
	EList<PartProperty> getPartProperty();

	/**
	 * Returns the value of the '<em><b>Reference Property</b></em>' reference list.
	 * The list contents are of type {@link sysml.ReferenceProperty}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference Property</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference Property</em>' reference list.
	 * @see sysml.SysmlPackage#getBlock_ReferenceProperty()
	 * @model
	 * @generated
	 */
	EList<ReferenceProperty> getReferenceProperty();

	/**
	 * Returns the value of the '<em><b>Flow Property</b></em>' reference list.
	 * The list contents are of type {@link sysml.FlowProperty}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Flow Property</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Flow Property</em>' reference list.
	 * @see sysml.SysmlPackage#getBlock_FlowProperty()
	 * @model
	 * @generated
	 */
	EList<FlowProperty> getFlowProperty();

	/**
	 * Returns the value of the '<em><b>Value Property</b></em>' reference list.
	 * The list contents are of type {@link sysml.ValueProperty}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value Property</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value Property</em>' reference list.
	 * @see sysml.SysmlPackage#getBlock_ValueProperty()
	 * @model
	 * @generated
	 */
	EList<ValueProperty> getValueProperty();

	/**
	 * Returns the value of the '<em><b>Bound Reference</b></em>' reference list.
	 * The list contents are of type {@link sysml.BoundReference}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bound Reference</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bound Reference</em>' reference list.
	 * @see sysml.SysmlPackage#getBlock_BoundReference()
	 * @model
	 * @generated
	 */
	EList<BoundReference> getBoundReference();

	/**
	 * Returns the value of the '<em><b>Nested Block</b></em>' reference list.
	 * The list contents are of type {@link sysml.Block}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nested Block</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nested Block</em>' reference list.
	 * @see sysml.SysmlPackage#getBlock_NestedBlock()
	 * @model
	 * @generated
	 */
	EList<Block> getNestedBlock();

	/**
	 * Returns the value of the '<em><b>Inherited Block</b></em>' reference list.
	 * The list contents are of type {@link sysml.Block}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inherited Block</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inherited Block</em>' reference list.
	 * @see sysml.SysmlPackage#getBlock_InheritedBlock()
	 * @model
	 * @generated
	 */
	EList<Block> getInheritedBlock();

	/**
	 * Returns the value of the '<em><b>Satisfy</b></em>' reference list.
	 * The list contents are of type {@link sysml.Requirement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Satisfy</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Satisfy</em>' reference list.
	 * @see sysml.SysmlPackage#getBlock_Satisfy()
	 * @model
	 * @generated
	 */
	EList<Requirement> getSatisfy();

	/**
	 * Returns the value of the '<em><b>Connector</b></em>' reference list.
	 * The list contents are of type {@link sysml.Connector}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connector</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connector</em>' reference list.
	 * @see sysml.SysmlPackage#getBlock_Connector()
	 * @model
	 * @generated
	 */
	EList<Connector> getConnector();

	/**
	 * Returns the value of the '<em><b>Internal Block Diagram</b></em>' reference list.
	 * The list contents are of type {@link sysml.InternalBlockDiagram}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Internal Block Diagram</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Internal Block Diagram</em>' reference list.
	 * @see sysml.SysmlPackage#getBlock_InternalBlockDiagram()
	 * @model
	 * @generated
	 */
	EList<InternalBlockDiagram> getInternalBlockDiagram();

	/**
	 * Returns the value of the '<em><b>Port</b></em>' reference list.
	 * The list contents are of type {@link sysml.Port}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port</em>' reference list.
	 * @see sysml.SysmlPackage#getBlock_Port()
	 * @model
	 * @generated
	 */
	EList<Port> getPort();

	/**
	 * Returns the value of the '<em><b>Proxy Port</b></em>' reference list.
	 * The list contents are of type {@link sysml.ProxyPort}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Proxy Port</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Proxy Port</em>' reference list.
	 * @see sysml.SysmlPackage#getBlock_ProxyPort()
	 * @model
	 * @generated
	 */
	EList<ProxyPort> getProxyPort();

	/**
	 * Returns the value of the '<em><b>Full Port</b></em>' reference list.
	 * The list contents are of type {@link sysml.FullPort}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Full Port</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Full Port</em>' reference list.
	 * @see sysml.SysmlPackage#getBlock_FullPort()
	 * @model
	 * @generated
	 */
	EList<FullPort> getFullPort();

} // Block
