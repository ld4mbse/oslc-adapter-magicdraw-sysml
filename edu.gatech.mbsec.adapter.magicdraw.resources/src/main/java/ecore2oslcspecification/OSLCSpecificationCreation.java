package ecore2oslcspecification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFWriter;

public class OSLCSpecificationCreation {

	static String specificEcoreFileLocation = "../org.eclipse.lyo.adapter.magicdraw.ecore/model/sysml4MBSE.ecore";
	static String genericEcoreFileLocation = "../org.eclipse.mbse.common.vocabulary/Ecore Metamodels/CommonMBSEVocabulary.ecore";
	static String specificEclipseProjectName = "org.eclipse.lyo.adapter.magicdraw";
	static String genericEclipseProjectName = "org.eclipse.mbse.common.vocabulary";

	static String namespaceURI;
	static String namespacePrefix;
	static EPackage ecorePackage;

	static Set<String> metaClasses = new HashSet<String>();
	static Set<String> metaPropertyURIs = new HashSet<String>();
	static StringBuffer rdfVocabularyBuffer = new StringBuffer();

	static ResourceSet resourceSet = new ResourceSetImpl();

	public static void main(String[] args) {
		loadEcoreMetaModels();
		prepareRDFVocabularyFile();
		convertEcoreMetamodelIntoRDFandOSLCResources();
		closeRDFVocabularyFile();

		System.out.println("Created " + metaClasses.size() + " OSLC Resource Shapes");
	}

	private static void closeRDFVocabularyFile() {
		rdfVocabularyBuffer.append("</rdf:RDF>");
		FileWriter rdfsClassFileWriter;
		try {
			rdfsClassFileWriter = new FileWriter("RDF Vocabulary/" + namespacePrefix + "RDFVocabulary.rdf");
			rdfsClassFileWriter.append(rdfVocabularyBuffer);
			rdfsClassFileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void convertEcoreMetamodelIntoRDFandOSLCResources() {
		// Convert metaclasses into RDF vocabulary and
		// OSLC resource shapes
		ArrayList<EClassifier> languageConcepts = getSysMLConceptsToMap();
		mapConceptsToOSLCSpecification("", languageConcepts);
	}

	private static void prepareRDFVocabularyFile() {
		// Create RDF Vocabulary
		rdfVocabularyBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		rdfVocabularyBuffer.append("\r\n");
		rdfVocabularyBuffer.append("<rdf:RDF");
		rdfVocabularyBuffer.append("\r\n");
		rdfVocabularyBuffer.append("\txmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"");
		rdfVocabularyBuffer.append("\r\n");
		rdfVocabularyBuffer.append("\txmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"");
		rdfVocabularyBuffer.append("\r\n");
		rdfVocabularyBuffer.append("\txmlns:dcterms=\"http://purl.org/dc/terms/\"");
		rdfVocabularyBuffer.append("\r\n");
		rdfVocabularyBuffer.append("\txmlns:" + namespacePrefix + "=\"" + namespaceURI + "\">");
		rdfVocabularyBuffer.append("\r\n");

	}

	private static void mapConceptsToOSLCSpecification(String prefix, ArrayList<EClassifier> eClassifiers) {
		for (EClassifier eClassifier : eClassifiers) {
			System.out.println("\t\t" + eClassifier.getName());

			if (metaClasses.contains(eClassifier.getName())) {
				System.err.println(eClassifier.getName() + " ALREADY DEFINED!");
				continue;
			} else {
				metaClasses.add(eClassifier.getName());
			}

			if (eClassifier instanceof EClass) {
				EClass metaClass = (EClass) eClassifier;

				// Create RDFS Class
				rdfVocabularyBuffer.append("\t<rdfs:Class");
				rdfVocabularyBuffer.append(" rdf:about=\"" + namespacePrefix + ":" + eClassifier.getName() + "\">");
				rdfVocabularyBuffer.append("\r\n");

				// rdfs:label
				rdfVocabularyBuffer.append("\t\t<rdfs:label xml:lang=\"en-GB\">" + eClassifier.getName()
						+ "</rdfs:label>");
				rdfVocabularyBuffer.append("\r\n");

				// dcterms:description
				if (metaClass.getEAnnotations().size() > 0) {
					for (EAnnotation eAnnotation : metaClass.getEAnnotations()) {
						if (eAnnotation.getSource().equals("http://www.eclipse.org/emf/2002/GenModel")) {
							String documentation = eAnnotation.getDetails().get("documentation");
							// convert string into UTF8 encoding
							try {
								byte[] bytes = documentation.getBytes("ISO-8859-1");
								String documentationUTF8 = new String(bytes, "UTF-8");
								if (documentationUTF8 != null) {
									rdfVocabularyBuffer.append("\t\t<dcterms:description xml:lang=\"en-GB\">"
											+ documentationUTF8 + "</dcterms:description>");
									rdfVocabularyBuffer.append("\r\n");
								}
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						}
					}
				}

				// rdfs:isDefinedBy
				rdfVocabularyBuffer.append("\t\t<rdfs:isDefinedBy rdf:resource=\"" + namespaceURI + "\"/>");
				rdfVocabularyBuffer.append("\r\n");

				// dcterms:issued
				rdfVocabularyBuffer.append("\t\t<dcterms:issued>2014-01-05</dcterms:issued>");
				rdfVocabularyBuffer.append("\r\n");

				// rdfs:subClassOf
				if (metaClass.getEGenericSuperTypes().size() > 0) {
					for (EGenericType genericType : metaClass.getEAllGenericSuperTypes()) {
						EClassifier superClass = genericType.getEClassifier();
						if (superClass.eIsProxy()) {
							URI unresolvedURI = EcoreUtil.getURI(superClass);
							String unresolvedURIString = unresolvedURI.toString();
							String resolvedURIString = unresolvedURIString.replace(
									specificEclipseProjectName, genericEclipseProjectName);
							EObject newSuperClass = resourceSet
									.getEObject(URI.createURI(resolvedURIString, true), true);
							superClass = (EClassifier) newSuperClass;
						}

						rdfVocabularyBuffer.append("\t\t<rdfs:subClassOf rdf:resource=\""
								+ superClass.getEPackage().getNsURI() + superClass.getName() + "\"/>");
						rdfVocabularyBuffer.append("\r\n");

					}
				}
				rdfVocabularyBuffer.append("\t</rdfs:Class>");
				rdfVocabularyBuffer.append("\r\n");

				// Create OSLC Resource Shape
				StringBuffer resourceShapeBuffer = new StringBuffer();
				resourceShapeBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				resourceShapeBuffer.append("\r\n");
				resourceShapeBuffer.append("<rdf:RDF");
				resourceShapeBuffer.append("\r\n");
				resourceShapeBuffer.append("\txmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"");
				resourceShapeBuffer.append("\r\n");
				resourceShapeBuffer.append("\txmlns:oslc=\"http://open-services.net/ns/core#\"");
				resourceShapeBuffer.append("\r\n");
				resourceShapeBuffer.append("\txmlns:dcterms=\"http://purl.org/dc/terms/\"");
				resourceShapeBuffer.append("\r\n");
				resourceShapeBuffer.append("\txmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"");
				resourceShapeBuffer.append("\r\n");
				resourceShapeBuffer.append("\txmlns:" + namespacePrefix + "=\"" + namespaceURI + "\">");

				// oslc:ResourceShape
				resourceShapeBuffer.append("\r\n");
				resourceShapeBuffer.append("\t<oslc:ResourceShape rdf:about=\"http://myOSLCServiceProvider.com/"
						+ namespacePrefix + "/" + metaClass.getName() + "ResourceShape\">");

				// oslc:describes
				resourceShapeBuffer.append("\r\n");
				resourceShapeBuffer.append("\t\t<oslc:describes rdf:resource=\"" + namespacePrefix + ":"
						+ metaClass.getName() + "\"/>");

				// dcterms:title
				resourceShapeBuffer.append("\r\n");
				resourceShapeBuffer
						.append("\t\t<dcterms:title rdf:datatype=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral\">"
								+ metaClass.getName() + " Resource Shape</dcterms:title>");

				Set<EStructuralFeature> eStructuralFeatures = getAllEStructuralFeatures(metaClass,
						new LinkedHashSet<EStructuralFeature>());

				Set<EEnum> enumerations = new LinkedHashSet<EEnum>();
				for (EStructuralFeature eStructuralFeature : eStructuralFeatures) {
					if (eStructuralFeature.isDerived()) {
						continue;
					}

					String propertyID = eStructuralFeature.getEContainingClass().getName() + "_"
							+ eStructuralFeature.getName();

					// Create RDF Property
					rdfVocabularyBuffer.append("\t<rdf:Property");
					rdfVocabularyBuffer.append(" rdf:about=\"" + namespacePrefix + ":" + propertyID + "\">");
					rdfVocabularyBuffer.append("\r\n");

					// rdfs:label
					rdfVocabularyBuffer.append("\t\t<rdfs:label xml:lang=\"en-GB\">" + eStructuralFeature.getName()
							+ "</rdfs:label>");
					rdfVocabularyBuffer.append("\r\n");

					// dcterms:description
					if (eStructuralFeature.getEAnnotations().size() > 0) {
						for (EAnnotation eAnnotation : eStructuralFeature.getEAnnotations()) {
							if (eAnnotation.getSource().equals("http://www.eclipse.org/emf/2002/GenModel")) {
								String documentation = eAnnotation.getDetails().get("documentation");
								// convert string into UTF8 encoding
								try {
									byte[] bytes = documentation.getBytes("ISO-8859-1");
									String documentationUTF8 = new String(bytes, "UTF-8");
									if (documentationUTF8 != null) {
										rdfVocabularyBuffer.append("\t\t<dcterms:description xml:lang=\"en-GB\">"
												+ documentationUTF8 + "</dcterms:description>");
										rdfVocabularyBuffer.append("\r\n");
									}
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							}
						}
					}

					// rdfs:isDefinedBy
					rdfVocabularyBuffer.append("\t\t<rdfs:isDefinedBy rdf:resource=\"" + namespaceURI + "\"/>");
					rdfVocabularyBuffer.append("\r\n");

					// rdfs:subPropertyOf
					if (eStructuralFeature.getEAnnotations().size() > 0) {
						for (EAnnotation eAnnotation : eStructuralFeature.getEAnnotations()) {
							if (eAnnotation.getSource().equals("subsets")) {
								if (eAnnotation.getReferences().size() > 0) {
									for (EObject eObject : eAnnotation.getReferences()) {
										if (eObject.eIsProxy()) {
											URI unresolvedURI = EcoreUtil.getURI(eObject);
											String unresolvedURIString = unresolvedURI.toString();
											String resolvedURIString = unresolvedURIString.replace(
													specificEclipseProjectName, genericEclipseProjectName);
											eObject = resourceSet
													.getEObject(URI.createURI(resolvedURIString, true), true);											
										}
										if (eObject instanceof EStructuralFeature) {
											EStructuralFeature eStructuralFeature2 = (EStructuralFeature) eObject;
											String propertyID2 = eStructuralFeature2.getEContainingClass().getName()
													+ "_" + eStructuralFeature2.getName();
											rdfVocabularyBuffer.append("\t\t<rdfs:subPropertyOf rdf:resource=\""
													+ eStructuralFeature2.getEContainingClass().getEPackage().getNsURI() + propertyID2 + "\"/>");
											rdfVocabularyBuffer.append("\r\n");
											break;
										}
									}
								}
								break;
							}
						}
					}

					// dcterms:issued
					rdfVocabularyBuffer.append("\t\t<dcterms:issued>2014-01-05</dcterms:issued>");
					rdfVocabularyBuffer.append("\r\n");
					rdfVocabularyBuffer.append("\t</rdf:Property>");
					rdfVocabularyBuffer.append("\r\n");

					// if (metaPropertyURIs.contains(propertyID)) {
					// // System.err.println(propertyID + " ALREADY DEFINED!");
					// continue;
					// }else{
					// metaPropertyURIs.add(propertyID);
					// }

					// oslc:property
					resourceShapeBuffer.append("\r\n");
					resourceShapeBuffer.append("\t\t<oslc:property>");

					// oslc:Property
					resourceShapeBuffer.append("\r\n");
					resourceShapeBuffer.append("\t\t\t<oslc:Property>");

					// oslc:name
					System.out.println("\t\t\t" + eStructuralFeature.getName());
					resourceShapeBuffer.append("\r\n");
					resourceShapeBuffer.append("\t\t\t\t<oslc:name>" + eStructuralFeature.getName() + "</oslc:name>");

					// oslc:propertyDefinition
					resourceShapeBuffer.append("\r\n");
					resourceShapeBuffer.append("\t\t\t\t<oslc:propertyDefinition rdf:resource=\"" + namespacePrefix
							+ ":" + propertyID + "\"/>");

					// oslc:valueType or oslc:range
					System.out.println("\t\t\t\t" + eStructuralFeature.getEType().getName());
					if (eStructuralFeature instanceof EAttribute) {
						if (eStructuralFeature.getEType().getName().equals("String")) {
							resourceShapeBuffer.append("\r\n");
							resourceShapeBuffer
									.append("\t\t\t\t<oslc:valueType rdf:resource=\"http://www.w3.org/2001/XMLSchema#string\"/>");
						} else if (eStructuralFeature.getEType().getName().equals("Integer")) {
							resourceShapeBuffer.append("\r\n");
							resourceShapeBuffer
									.append("\t\t\t\t<oslc:valueType rdf:resource=\"http://www.w3.org/2001/XMLSchema#integer\"/>");
						} else if (eStructuralFeature.getEType().getName().equals("Boolean")) {
							resourceShapeBuffer.append("\r\n");
							resourceShapeBuffer
									.append("\t\t\t\t<oslc:valueType rdf:resource=\"http://www.w3.org/2001/XMLSchema#boolean\"/>");
						}

						if (eStructuralFeature.getEType() instanceof EEnum) {
							EEnum eEnum = (EEnum) eStructuralFeature.getEType();
							enumerations.add(eEnum);

							// oslc:allowedValues
							resourceShapeBuffer.append("\r\n");
							resourceShapeBuffer.append("\t\t\t\t<oslc:allowedValues rdf:resource=\"" + namespacePrefix
									+ ":" + eEnum.getName() + "\"/>");
						}
					} else if (eStructuralFeature instanceof EReference) {
						// oslc:range
						resourceShapeBuffer.append("\r\n");
						resourceShapeBuffer.append("\t\t\t\t<oslc:range rdf:resource=\"" + namespacePrefix + ":"
								+ eStructuralFeature.getEType().getName() + "\"/>");

						// oslc:valueType
						resourceShapeBuffer.append("\r\n");
						resourceShapeBuffer
								.append("\t\t\t\t<oslc:valueType rdf:resource=\"http://open-services.net/ns/core#Resource\"/>");
					}

					// oslc:occurs
					System.out.println("\t\t\t\t" + eStructuralFeature.getLowerBound());
					System.out.println("\t\t\t\t" + eStructuralFeature.getUpperBound());
					resourceShapeBuffer.append("\r\n");
					int lowerBound = eStructuralFeature.getLowerBound();
					int upperBound = eStructuralFeature.getUpperBound();
					// occurs EXACTLY ONE
					if (lowerBound == 1 & upperBound == 1) {
						resourceShapeBuffer
								.append("\t\t\t\t<oslc:occurs rdf:resource=\"http://open-service.net/ns/core#Exactly-one\"/>");
					}
					// zero-or-one
					else if (lowerBound == 0 & upperBound == 1) {
						resourceShapeBuffer
								.append("\t\t\t\t<oslc:occurs rdf:resource=\"http://open-services.net/ns/core#Zero-or-one\"/>");
					}
					// one or many
					else if (lowerBound == 1 & upperBound == -1) {
						resourceShapeBuffer
								.append("\t\t\t\t<oslc:occurs rdf:resource=\"http://open-services.net/ns/core#One-or-many\"/>");
					}
					// Zero-or-many
					else if (lowerBound == 0 & upperBound == -1) {
						resourceShapeBuffer
								.append("\t\t\t\t<oslc:occurs rdf:resource=\"http://open-service.net/ns/core#Zero-or-many\"/>");
					} else {
						resourceShapeBuffer
								.append("\t\t\t\t<oslc:occurs rdf:resource=\"http://open-service.net/ns/core#Zero-or-many\"/>");
					}

					resourceShapeBuffer.append("\r\n");
					resourceShapeBuffer.append("\t\t\t</oslc:Property>");

					resourceShapeBuffer.append("\r\n");
					resourceShapeBuffer.append("\t\t</oslc:property>");
				}
				resourceShapeBuffer.append("\r\n");
				resourceShapeBuffer.append("\t</oslc:ResourceShape>");

				// enumerations
				for (EEnum enumration : enumerations) {
					resourceShapeBuffer.append("\r\n");
					resourceShapeBuffer.append("\t<oslc:AllowedValues rdf:about=\"" + namespacePrefix + ":"
							+ enumration.getName() + "\">");

					// dcterms:title
					resourceShapeBuffer.append("\r\n");
					resourceShapeBuffer.append("\t\t<dcterms:title rdf:parseType=\"Literal\">" + enumration.getName()
							+ "</dcterms:title>");

					for (EEnumLiteral eEnumLiteral : enumration.getELiterals()) {
						resourceShapeBuffer.append("\r\n");
						resourceShapeBuffer.append("\t\t<oslc:allowedValue>" + eEnumLiteral.getLiteral()
								+ "</oslc:allowedValue>");
					}

					resourceShapeBuffer.append("\r\n");
					resourceShapeBuffer.append("\t</oslc:AllowedValues>");

				}

				resourceShapeBuffer.append("\r\n");
				resourceShapeBuffer.append("</rdf:RDF>");

				FileWriter resourceShapeFileWriter;
				try {
					resourceShapeFileWriter = new FileWriter("Resource Shapes/" + prefix + metaClass.getName() + ".rdf");
					resourceShapeFileWriter.append(resourceShapeBuffer);
					resourceShapeFileWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private static void loadEcoreMetaModels() {
		// load .ecore model
		Resource ecoreResource = loadEcoreModel(URI
				.createFileURI(new File(specificEcoreFileLocation).getAbsolutePath()));
		ecorePackage = (EPackage) EcoreUtil.getObjectByType(ecoreResource.getContents(),
				EcorePackage.eINSTANCE.getEPackage());
		namespacePrefix = ecorePackage.getNsPrefix();
		namespaceURI = ecorePackage.getNsURI();

	}

	static private ArrayList<EClassifier> getSysMLConceptsToMap() {
		ArrayList<EClassifier> languageConceptsToMap = new ArrayList<EClassifier>();
		languageConceptsToMap.addAll(ecorePackage.getEClassifiers());
		for (EPackage nestedPackage : ecorePackage.getESubpackages()) {
			System.out.println("\t" + nestedPackage.getName());
			for (EClassifier eClassifier : nestedPackage.getEClassifiers()) {
				languageConceptsToMap.add(eClassifier);
			}
		}
		return languageConceptsToMap;
	}

	private static Set<EEnum> getAllEEnumerations(EClass metaClass, LinkedHashSet<EEnum> linkedHashSet) {
		for (EAttribute eAttribute : metaClass.getEAllAttributes()) {
			if (eAttribute.getEType() instanceof EEnum) {
				EEnum eEnum = (EEnum) eAttribute.getEType();
				linkedHashSet.add(eEnum);
			}

		}
		for (EReference eReference : metaClass.getEAllReferences()) {
			// System.out.println("\t\t\t" + eReference.getName());
			// if (eReference.getName().startsWith("base")) {
			// System.out
			// .println("\t\t\t\t" + eReference.getEType().getName());
			EClassifier umlClassifier = eReference.getEType();
			if (umlClassifier instanceof EClass) {
				EClass umlType = (EClass) umlClassifier;

				for (EAttribute umlAttribute : umlType.getEAllAttributes()) {
					if (umlAttribute.getName().equals("eAnnotations")) {
						continue;
					}
					if (umlAttribute.getEType() instanceof EEnum) {
						EEnum eEnum = (EEnum) umlAttribute.getEType();
						linkedHashSet.add(eEnum);
					}
				}
				for (EClass eSuperType : umlType.getESuperTypes()) {
					getAllEEnumerations(eSuperType, linkedHashSet);
				}
			}

			// }
		}
		return linkedHashSet;
	}

	private static LinkedHashSet<EStructuralFeature> getAllEStructuralFeatures(EClass eClass,
			LinkedHashSet<EStructuralFeature> eStructuralFeatures) {
		for (EAttribute eAttribute : eClass.getEAllAttributes()) {
			eStructuralFeatures.add(eAttribute);
		}
		for (EReference eReference : eClass.getEAllReferences()) {
			eStructuralFeatures.add(eReference);
		}
		return eStructuralFeatures;
	}

	private static Resource loadEcoreModel(URI fileURI) {
		// Create a resource set.

		// Register the default resource factory -- only needed for stand-alone!
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		// Register the package -- only needed for stand-alone!
		EcorePackage ecorePackage = EcorePackage.eINSTANCE;

		// Demand load the resource for this file.
		Resource resource = resourceSet.getResource(fileURI, true);
		return resource;
	}
}
