/*********************************************************************************************
 * Copyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.
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
 *	   Axel Reichwein (axel.reichwein@koneksys.com)		- initial implementation       
 *******************************************************************************************/
package edu.gatech.mbsec.adapter.magicdraw.resources;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.lyo.oslc4j.core.annotation.OslcDescription;
import org.eclipse.lyo.oslc4j.core.annotation.OslcName;
import org.eclipse.lyo.oslc4j.core.annotation.OslcNamespace;
import org.eclipse.lyo.oslc4j.core.annotation.OslcOccurs;
import org.eclipse.lyo.oslc4j.core.annotation.OslcPropertyDefinition;
import org.eclipse.lyo.oslc4j.core.annotation.OslcRange;
import org.eclipse.lyo.oslc4j.core.annotation.OslcReadOnly;
import org.eclipse.lyo.oslc4j.core.annotation.OslcResourceShape;
import org.eclipse.lyo.oslc4j.core.annotation.OslcTitle;
import org.eclipse.lyo.oslc4j.core.annotation.OslcValueType;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.Link;
import org.eclipse.lyo.oslc4j.core.model.Occurs;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.ValueType;

public class OSLCJavaClassesGenerator {

	static EPackage sysmlPackage;
	static EClass eclass;
	
	public static ResourceSet resourceSet = new ResourceSetImpl();
	
	// WARNING: 3 User-defined hard-coded paths
		static String magicdrawEcoreGitRepositoryLocation = "C:/Users/Axel/git/org.eclipse.lyo.adapter.magicdraw.oslc/";		// necessary to know the entire file path to resolve URIs		
		static String commonMBSEEcoreGitRepositoryLocation = "C:/Users/Axel/git/org.eclipse.mbse.common.vocabulary/";	// where is the org.eclipse.mbse.common.vocabulary project (git repository) located?
//		static String javaSrcPackageLocation = "C:/Users/Axel/git/org.eclipse.lyo.adapter.magicdraw.oslc/edu.gatech.mbsec.adapter.magicdraw.resources/src/main/java/org/eclipse/lyo/adapter/magicdraw/resources/";
		static String javaSrcPackageLocation = "C:/Users/Axel/git/org.eclipse.lyo.adapter.magicdraw.oslc/edu.gatech.mbsec.adapter.magicdraw.resources/src/main/java/org/eclipse/lyo/adapter/magicdraw/resources/";
		static String magicdrawEcoreLocation = magicdrawEcoreGitRepositoryLocation + "org.eclipse.lyo.adapter.magicdraw.ecore/model/sysml4MBSE.ecore";	
		static String commonMBSEEcoreLocation = commonMBSEEcoreGitRepositoryLocation + "org.eclipse.mbse.common.vocabulary/Ecore Metamodels/CommonMBSEVocabulary.ecore";

	public static void main(String[] args) {

		// load sysml.ecore model
		Resource ecoreResource = loadEcoreModel(URI.createFileURI(new File(
				magicdrawEcoreLocation).getAbsolutePath()));

		sysmlPackage = (EPackage) EcoreUtil.getObjectByType(
				ecoreResource.getContents(),
				EcorePackage.eINSTANCE.getEPackage());
		System.out.println(sysmlPackage.getName());

		for (EClassifier eclassifier : sysmlPackage.getEClassifiers()) {
			if (eclassifier instanceof EClass) {
				eclass = (EClass) eclassifier;
				if (!eclass.isAbstract()) {
					// create annotated Java class
					StringBuffer buffer = new StringBuffer();

					// print license clause
					printLicenseClause(buffer);
					
					// print package declaration
					printPackageDeclaration(buffer);

					// print import statements
					printImportStatements(buffer);

					// print Java class OSLC annotations
					printJavaClassOSLCAnnotations(buffer);

					// print Java class declaration
					printJavaClassDeclaration(buffer);

					// print Java class constructors
					printJavaClassConstructors(buffer);

					// print Java class attributes
					printJavaClassAttributes(buffer);

					// print Java class references
					printJavaClassReferences(buffer);
					
					// print references describing additional resource types
					printJavaTypeReferences(buffer);

					buffer.append("}");

					FileWriter fileWriter;
					try {
						fileWriter = new FileWriter(
								javaSrcPackageLocation
										+ "SysML" + eclass.getName() + ".java");
						fileWriter.append(buffer);
						fileWriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			} else if (eclassifier instanceof EEnum) {
				EEnum eEnum = (EEnum) eclassifier;

				// create annotated Java enumenration
				StringBuffer buffer = new StringBuffer();

				// print package declaration
				printPackageDeclaration(buffer);

				// print enum
				printJavaEnum(buffer, eEnum);

				FileWriter fileWriter;
				try {
					fileWriter = new FileWriter(
							"C:/Users/Axel/git/org.eclipse.lyo.adapter.magicdraw/edu.gatech.mbsec.adapter.magicdraw.resources/src/main/java/org/eclipse/lyo/adapter/magicdraw/resources/"
									+ "SysML" + eEnum.getName() + ".java");
					fileWriter.append(buffer);
					fileWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	private static void printJavaClassReferences(StringBuffer buffer) {
		for (EReference ereference : eclass.getEAllReferences()) {
			if (ereference.isDerived()) {
				continue;
			}

			int lowerBound = ereference.getLowerBound();
			int upperBound = ereference.getUpperBound();

			// find reference name
			String referenceName = ereference.getName();

			// find reference name with first char in uppercase
			String referenceNameFirstLetter = referenceName.subSequence(0, 1)
					.toString().toUpperCase();
			String referenceNameRest = referenceName.subSequence(1,
					referenceName.length()).toString();
			String referenceNameUpperCase = referenceNameFirstLetter
					+ referenceNameRest;

			// find name of class containing reference
			String referenceContainingClassName = ereference
					.getEContainingClass().getName();

			// find name of reference type
			String referenceTypeName = null;
			EClassifier etype = ereference.getEType();
			if (etype.getName() != null) {
				if (etype.getName().equals("EString")) {
					referenceTypeName = "String";
				} else {
					referenceTypeName = "SysML" + etype.getName();
				}
			}

			// do not generate code for the reference if its type is not
			// resolved
			if (referenceTypeName == null) {
				continue;
			}

			// if reference is multiple-valued
			if (lowerBound >= 0 & upperBound == -1 | lowerBound >= 0
					& upperBound >= 2) {

				// add comments to mark section
				buffer.append("\t// ********* " + referenceName + " *********");
				buffer.append("\r\n");

				String originalReferenceName = referenceName;
				if (referenceName.endsWith("y")) {
					referenceName = referenceName.substring(0,
							referenceName.length() - 1)
							+ "ie";
					referenceNameUpperCase = referenceNameUpperCase.substring(
							0, referenceNameUpperCase.length() - 1) + "ie";
				} else if (referenceName.endsWith("ed")) {
					referenceName = referenceName + "Element";
					referenceNameUpperCase = referenceNameUpperCase + "Element";
				} else if (referenceName.endsWith("om")) {
					referenceName = referenceName + "Element";
					referenceNameUpperCase = referenceNameUpperCase + "Element";
				}

				// print reference declaration
				buffer.append("\tprivate final Set<Link> " + referenceName
						+ "s = new HashSet<Link>();");
				buffer.append("\r\n");
				buffer.append("\r\n");

				// print reference setter method
				buffer.append("\tpublic void set" + referenceNameUpperCase
						+ "s(final Link[] " + referenceName + "s) {");
				buffer.append("\r\n");
				buffer.append("\t\tthis." + referenceName + "s.clear();");
				buffer.append("\r\n");
				buffer.append("\t\tif (" + referenceName + "s != null)");
				buffer.append("\r\n");
				buffer.append("\t\t{");
				buffer.append("\r\n");
				buffer.append("\t\t\tthis." + referenceName
						+ "s.addAll(Arrays.asList(" + referenceName + "s));");
				buffer.append("\r\n");
				buffer.append("\t\t}");
				buffer.append("\r\n");
				buffer.append("\t}");
				buffer.append("\r\n");
				buffer.append("\r\n");

				// print reference getter method annotations

				// @OslcDescription
				buffer.append("\t@OslcDescription(\"Description of "
						+ referenceContainingClassName + "::"
						+ originalReferenceName + " TBD\")");
				buffer.append("\r\n");

				// @OslcName
				buffer.append("\t@OslcName(\"" + originalReferenceName + "\")");
				buffer.append("\r\n");

				// @OslcOccurs
				if (lowerBound == 0 & upperBound == -1) {
					buffer.append("\t@OslcOccurs(Occurs.ZeroOrMany)");
					buffer.append("\r\n");
				} else if (lowerBound == 1 & upperBound == -1) {
					buffer.append("\t@OslcOccurs(Occurs.OneOrMany)");
					buffer.append("\r\n");
				}

				// @OslcPropertyDefinition
				buffer.append("\t@OslcPropertyDefinition(\""
						+ Constants.SYSML_NAMESPACE
						+ referenceContainingClassName + "_"
						+ originalReferenceName + "\")");
				buffer.append("\r\n");

				// @OslcTitle
				buffer.append("\t@OslcTitle(\"" + originalReferenceName + "\")");
				buffer.append("\r\n");

				// @OslcReadOnly
				buffer.append("\t@OslcReadOnly(false)");
				buffer.append("\r\n");

				// // @OslcValueType
				// if(referenceTypeName.equals("String")){
				// buffer.append("\t@OslcValueType(ValueType.XMLLiteral)");
				// buffer.append("\r\n");
				// }

				// print reference getter method
				buffer.append("\tpublic Link[] " + " get"
						+ referenceNameUpperCase + "s() {");
				buffer.append("\r\n");
				buffer.append("\t\t return " + referenceName
						+ "s.toArray(new Link[" + referenceName + "s.size()]);");
				buffer.append("\r\n");
				buffer.append("\t}");
				buffer.append("\r\n");
				buffer.append("\r\n");

			} else if (lowerBound >= 0 & upperBound == 1) {

				// add comments to mark section
				buffer.append("\t// ********* " + referenceName + " *********");
				buffer.append("\r\n");

				// print reference declaration
				buffer.append("\tprivate URI " + referenceName + ";");
				buffer.append("\r\n");
				buffer.append("\r\n");

				// print reference setter method
				buffer.append("\tpublic void set" + referenceNameUpperCase
						+ "(final URI " + referenceName + ") {");
				buffer.append("\r\n");
				buffer.append("\t\tthis." + referenceName + " = "
						+ referenceName + ";");
				buffer.append("\r\n");
				buffer.append("\t}");
				buffer.append("\r\n");
				buffer.append("\r\n");

				// print reference getter method annotations

				// @OslcDescription
				buffer.append("\t@OslcDescription(\"Description of "
						+ referenceContainingClassName + "::" + referenceName
						+ " TBD\")");
				buffer.append("\r\n");

				// @OslcName
				buffer.append("\t@OslcName(\"" + referenceName + "\")");
				buffer.append("\r\n");

				// @OslcOccurs
				if (lowerBound == 0 & upperBound == -1) {
					buffer.append("\t@OslcOccurs(Occurs.ZeroOrMany)");
					buffer.append("\r\n");
				} else if (lowerBound == 1 & upperBound == -1) {
					buffer.append("\t@OslcOccurs(Occurs.OneOrMany)");
					buffer.append("\r\n");
				}

				// @OslcPropertyDefinition
				buffer.append("\t@OslcPropertyDefinition(\""
						+ Constants.SYSML_NAMESPACE
						+ referenceContainingClassName + "_" + referenceName
						+ "\")");
				buffer.append("\r\n");

				// @OslcTitle
				buffer.append("\t@OslcTitle(\"" + referenceName + "\")");
				buffer.append("\r\n");

				// @OslcRange
				// buffer.append("\t@OslcRange(\"http://open-services.net/ns/sysml/"
				// + etype.getName() + "\")");
				buffer.append("\t@OslcRange(\"" + Constants.SYSML_NAMESPACE
						+ etype.getName() + "\")");
				buffer.append("\r\n");

				// print reference getter method
				buffer.append("\tpublic URI " + " get" + referenceNameUpperCase
						+ "() {");
				buffer.append("\r\n");
				buffer.append("\t\t return " + referenceName + ";");
				buffer.append("\r\n");
				buffer.append("\t}");
				buffer.append("\r\n");
				buffer.append("\r\n");

			}

		}

		// add serviceprovider reference
		buffer.append("\tprivate URI      serviceProvider;");
		buffer.append("\r\n");
		buffer.append("\r\n");
		buffer.append("\tpublic void setServiceProvider(final URI serviceProvider)");
		buffer.append("\r\n");
		buffer.append("\t{");
		buffer.append("\t\tthis.serviceProvider = serviceProvider;");
		buffer.append("\r\n");
		buffer.append("\t}");
		buffer.append("\r\n");
		buffer.append("\r\n");

		buffer.append("\t@OslcDescription(\"The scope of a resource is a URI for the resource's OSLC Service Provider.\")");
		buffer.append("\r\n");
		buffer.append("\t@OslcPropertyDefinition(OslcConstants.OSLC_CORE_NAMESPACE + \"serviceProvider\")");
		buffer.append("\r\n");
		buffer.append("\t@OslcRange(OslcConstants.TYPE_SERVICE_PROVIDER)");
		buffer.append("\r\n");
		buffer.append("\t@OslcTitle(\"Service Provider\")");
		buffer.append("\t\r\n");
		buffer.append("\tpublic URI getServiceProvider()");
		buffer.append("\r\n");
		buffer.append("\t{");
		buffer.append("\r\n");
		buffer.append("\t\treturn serviceProvider;");
		buffer.append("\r\n");
		buffer.append("\t}");
		buffer.append("\r\n");
		buffer.append("\r\n");

	}

	private static void printJavaClassAttributes(StringBuffer buffer) {
		for (EAttribute eattribute : eclass.getEAllAttributes()) {
			if (eattribute.isDerived()) {
				continue;
			}

			int lowerBound = eattribute.getLowerBound();
			int upperBound = eattribute.getUpperBound();

			// find attribute name
			String attributeName = eattribute.getName();

			// find attribute name with first char in uppercase
			String attributeNameFirstLetter = attributeName.subSequence(0, 1)
					.toString().toUpperCase();
			String attributeNameRest = attributeName.subSequence(1,
					attributeName.length()).toString();
			String attributeNameUpperCase = attributeNameFirstLetter
					+ attributeNameRest;

			// find name of class containing attribute
			String attributeContainingClassName = eattribute
					.getEContainingClass().getName();

			// find name of attribute type
			String attributeTypeName = null;
			EClassifier etype = eattribute.getEType();
			if (etype.getName() != null) {
				if (etype.getName().equals("EString")) {
					attributeTypeName = "String";
				} else if (etype.getName().equals("EBoolean")) {
					attributeTypeName = "boolean";
				} else {
					attributeTypeName = "SysML" + etype.getName();
				}
			}

			// do not generate code for the attribute if its type is not
			// resolved
			if (attributeTypeName == null) {
				continue;
			}

			// if attribute is single-valued
			if (lowerBound >= 0 & upperBound <= 1) {

				// print attribute declaration
				buffer.append("\tprivate " + attributeTypeName + " "
						+ attributeName + ";");
				buffer.append("\r\n");
				buffer.append("\r\n");

				// print attribute setter method
				buffer.append("\tpublic void set" + attributeNameUpperCase
						+ "(" + attributeTypeName + " " + attributeName + ") {");
				buffer.append("\r\n");
				buffer.append("\t\tthis." + attributeName + " = "
						+ attributeName + ";");
				buffer.append("\r\n");
				buffer.append("\t}");
				buffer.append("\r\n");
				buffer.append("\r\n");

				// print attribute getter method annotations

				// @OslcDescription
				buffer.append("\t@OslcDescription(\"Description of "
						+ attributeContainingClassName + "::" + attributeName
						+ " TBD\")");
				buffer.append("\r\n");

				// @OslcName
				buffer.append("\t@OslcName(\"" + attributeName + "\")");
				buffer.append("\r\n");

				// @OslcOccurs
				if (lowerBound == 1 & upperBound == 1) {
					buffer.append("\t@OslcOccurs(Occurs.ExactlyOne)");
					buffer.append("\r\n");
				} else if (lowerBound == 0 & upperBound == 1) {
					buffer.append("\t@OslcOccurs(Occurs.ZeroOrOne)");
					buffer.append("\r\n");
				}

				// @OslcPropertyDefinition
				buffer.append("\t@OslcPropertyDefinition(\""
						+ Constants.SYSML_NAMESPACE
						+ attributeContainingClassName + "_" + attributeName
						+ "\")");
				buffer.append("\r\n");

				// @OslcTitle
				buffer.append("\t@OslcTitle(\"" + attributeName + "\")");
				buffer.append("\r\n");

				// @OslcValueType
				if (attributeTypeName.equals("String")) {
					buffer.append("\t@OslcValueType(ValueType.XMLLiteral)");
					buffer.append("\r\n");
				}

				// print attribute getter method
				buffer.append("\tpublic " + attributeTypeName + " get"
						+ attributeNameUpperCase + "() {");
				buffer.append("\r\n");
				buffer.append("\t\t return " + attributeName + ";");
				buffer.append("\r\n");
				buffer.append("\t}");
				buffer.append("\r\n");

			}
		}
	}

	private static void printJavaClassConstructors(StringBuffer buffer) {
		buffer.append("\tpublic SysML" + eclass.getName()
				+ "() throws URISyntaxException {");
		buffer.append("\r\n");
		buffer.append("\t\tsuper();");
		buffer.append("\r\n");
		buffer.append("\t}");
		buffer.append("\r\n");

		buffer.append("\tpublic SysML" + eclass.getName()
				+ "(URI about) throws URISyntaxException {");
		buffer.append("\r\n");
		buffer.append("\t\tsuper(about);");
		buffer.append("\r\n");
		buffer.append("\t}");
		buffer.append("\r\n");
		buffer.append("\r\n");

	}

	private static void printJavaClassDeclaration(StringBuffer buffer) {
		if (eclass.getName().equals("Requirement")) {
			buffer.append("public class SysML" + eclass.getName()
					+ " extends Requirement{");
			buffer.append("\r\n");
			buffer.append("\r\n");
		} else {
			buffer.append("public class SysML" + eclass.getName()
					+ " extends AbstractResource{");
			buffer.append("\r\n");
			buffer.append("\r\n");
		}

	}

	private static void printJavaEnum(StringBuffer buffer, EEnum eEnum) {

		buffer.append("public enum SysML" + eEnum.getName() + " {");
		buffer.append("\r\n");

		for (Iterator iterator = eEnum.getELiterals().iterator(); iterator
				.hasNext();) {
			EEnumLiteral eEnumLiteral = (EEnumLiteral) iterator.next();
			buffer.append("\t");
			buffer.append(eEnumLiteral.getName().toUpperCase());
			if (iterator.hasNext()) {
				buffer.append(",");
			}
			buffer.append("\r\n");
		}
		buffer.append("}");

	}

	private static void printJavaClassOSLCAnnotations(StringBuffer buffer) {
		// buffer.append("@OslcNamespace(\"" + sysmlPackage.getNsURI() + "\")");
		// buffer.append("@OslcNamespace(Constants.SYSML_" +
		// eclass.getName().toUpperCase() + "_NAMESPACE)");
		buffer.append("@OslcNamespace(Constants.SYSML_NAMESPACE)");
		buffer.append("\r\n");
		buffer.append("@OslcName(\"" + eclass.getName() + "\")");
		buffer.append("\r\n");
		// buffer.append("@OslcResourceShape(title = \"" + eclass.getName()
		// + " Resource Shape\", describes = \"" + sysmlPackage.getNsURI()
		// + eclass.getName().toLowerCase() + "/rdf#\")");
		// buffer.append("@OslcResourceShape(title = \"" + eclass.getName()
		// + " Resource Shape\", describes = \"" + sysmlPackage.getNsURI() +
		// "rdf#"
		// + eclass.getName() + "\")");

		// buffer.append("@OslcResourceShape(title = \"" + eclass.getName()
		// + " Resource Shape\", describes = \"" + sysmlPackage.getNsURI() +
		// "rdf#"
		// + eclass.getName() + "\")");

		buffer.append("@OslcResourceShape(title = \"" + eclass.getName()
				+ " Resource Shape\", describes = " + "Constants.TYPE_SYSML_"
				+ eclass.getName().toUpperCase() + ")");
		buffer.append("\r\n");

	}

	private static void printImportStatements(StringBuffer buffer) {
		buffer.append("import java.net.URI;");
		buffer.append("\r\n");
		buffer.append("import java.net.URISyntaxException;");
		buffer.append("\r\n");
		buffer.append("import java.util.ArrayList;");
		buffer.append("\r\n");
		buffer.append("import java.util.Arrays;");
		buffer.append("\r\n");
		buffer.append("import java.util.List;");
		buffer.append("\r\n");
		buffer.append("import java.util.HashSet;");
		buffer.append("\r\n");
		buffer.append("import java.util.Set;");
		buffer.append("\r\n");

		buffer.append("import org.eclipse.lyo.oslc4j.core.annotation.OslcDescription;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.annotation.OslcName;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.annotation.OslcOccurs;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.annotation.OslcNamespace;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.annotation.OslcReadOnly;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.annotation.OslcPropertyDefinition;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.annotation.OslcRange;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.annotation.OslcRepresentation;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.annotation.OslcResourceShape;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.annotation.OslcTitle;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.annotation.OslcValueType;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.model.AbstractResource;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.model.OslcConstants;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.model.Occurs;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.model.Representation;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.model.ValueType;");
		buffer.append("\r\n");
		buffer.append("import org.eclipse.lyo.oslc4j.core.model.Link;");
		buffer.append("\r\n");

		buffer.append("\r\n");

	}

	private static void printPackageDeclaration(StringBuffer buffer) {
		buffer.append("package edu.gatech.mbsec.adapter.magicdraw.resources;");
		buffer.append("\r\n");
		buffer.append("\r\n");
	}
	
	private static void printJavaTypeReferences(StringBuffer buffer) {

		int superTypesCount = eclass.getESuperTypes().size();

		if(superTypesCount > 0){
		
			// find reference name with first char in uppercase
			String referenceName = "rdfType";
			String referenceNameFirstLetter = referenceName.subSequence(0, 1).toString().toUpperCase();
			String referenceNameRest = referenceName.subSequence(1, referenceName.length()).toString();
			String referenceNameUpperCase = referenceNameFirstLetter + referenceNameRest;

			// find name of class containing reference
			String referenceContainingClassName = eclass.getName();

			// reference is multiple-valued

			// add comments to mark section
			buffer.append("\t// ********* " + referenceName + " *********");
			buffer.append("\r\n");

			String originalReferenceName = referenceName;
			

			// print reference declaration
			buffer.append("\tprivate URI[] " + referenceName + "s = new URI[" + (superTypesCount + 1) + "];");
			buffer.append("\r\n");
			buffer.append("\r\n");

			// print reference setter method
			buffer.append("\tpublic void set" + referenceNameUpperCase + "s(final URI[] " + referenceName + "s) {");
			buffer.append("\r\n");
			buffer.append("\t\tthis." + referenceName + "s = " + referenceName + "s;");
			buffer.append("\r\n");			
			buffer.append("\t}");
			buffer.append("\r\n");
			buffer.append("\r\n");

			// print reference getter method annotations

			// @OslcDescription
			buffer.append("\t@OslcDescription(\"Additional resource type URIs" + " \")");
			buffer.append("\r\n");

			// @OslcName
			buffer.append("\t@OslcName(\"type\")");
			buffer.append("\r\n");

			// @OslcOccurs ZeroOrMany

			buffer.append("\t@OslcOccurs(Occurs.ZeroOrMany)");
			buffer.append("\r\n");

			// @OslcPropertyDefinition
			buffer.append("\t@OslcPropertyDefinition(OslcConstants.RDF_NAMESPACE + \"type\")");
			buffer.append("\r\n");

			// print reference getter method
			buffer.append("\tpublic URI[] " + " get" + referenceNameUpperCase + "s() {");
			buffer.append("\r\n");
			buffer.append("\t\t URI[] " + referenceName + "s = {");
			int i = 0;
			
			for (EClass superClass : eclass.getEAllSuperTypes()) {
				if(superClass.eIsProxy()){
					URI unresolvedURI = EcoreUtil.getURI(superClass);
					String unresolvedURIString = unresolvedURI.toString();
					String resolvedURIString = unresolvedURIString.replace(magicdrawEcoreGitRepositoryLocation, commonMBSEEcoreGitRepositoryLocation);
					EObject newSuperClass = resourceSet.getEObject(URI.createURI(resolvedURIString, true), true);
					superClass = (EClass) newSuperClass;
				}
				if(i > 0){
					buffer.append(", ");	
				}
				
				buffer.append("URI.create(\"" + superClass.getEPackage().getNsURI() + superClass .getName() + "\")");	
				i++;
			}
			buffer.append("};");
			buffer.append("\r\n");
			buffer.append("\t\t return " + referenceName + "s;");
			buffer.append("\r\n");
			buffer.append("\t}");
			buffer.append("\r\n");
			buffer.append("\r\n");

		}

	}

	private static Resource loadEcoreModel(URI fileURI) {
		// Create a resource set.
//		ResourceSet resourceSet = new ResourceSetImpl();

		// Register the default resource factory -- only needed for stand-alone!
		resourceSet
				.getResourceFactoryRegistry()
				.getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());

		// Register the package -- only needed for stand-alone!
		EcorePackage ecorePackage = EcorePackage.eINSTANCE;

		// Demand load the resource for this file.
		Resource resource = resourceSet.getResource(fileURI, true);
		return resource;
	}

	public static EClass getEClass(String eClassName) {
		if (sysmlPackage == null) {
			// load sysml.ecore model
			Resource ecoreResource = loadEcoreModel(URI.createFileURI(new File(
					"sysml.ecore").getAbsolutePath()));

			sysmlPackage = (EPackage) EcoreUtil.getObjectByType(
					ecoreResource.getContents(),
					EcorePackage.eINSTANCE.getEPackage());
		}

		for (EClassifier eclassifier : sysmlPackage.getEClassifiers()) {
			if (eclassifier instanceof EClass) {
				EClass eClass = (EClass) eclassifier;
				if (eClass.getName().equals(eClassName)) {
					return eClass;
				}
			}
		}
		return null;
	}

	private static void printLicenseClause(StringBuffer buffer) {
		buffer.append("/*********************************************************************************************\r\n");		
		buffer.append(" * Copyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.\r\n");		
		buffer.append(" *                         http://www.mbse.gatech.edu/\r\n");
		buffer.append(" *                  http://www.mbsec.gatech.edu/research/oslc\r\n");
		buffer.append(" *\r\n");
		buffer.append(" *  All rights reserved. This program and the accompanying materials\r\n");
		buffer.append(" *  are made available under the terms of the Eclipse Public License v1.0\r\n");
		buffer.append(" *  and Eclipse Distribution License v. 1.0 which accompanies this distribution.\r\n");
		buffer.append(" *\r\n");
		buffer.append(" *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html\r\n");
		buffer.append(" *  and the Eclipse Distribution License is available at\r\n");
		buffer.append(" *  http://www.eclipse.org/org/documents/edl-v10.php.\r\n");
		buffer.append(" *\r\n");
		buffer.append(" *  Contributors:\r\n");
		buffer.append(" *\r\n");
		buffer.append(" *	   Axel Reichwein, Koneksys (axel.reichwein@koneksys.com)		\r\n");
		buffer.append(" *******************************************************************************************/\r\n");
	}
}
