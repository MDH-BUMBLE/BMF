package xMLWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import model.Association;
import model.Attribute;
import model.Cardinality;
import model.Containment;
import model.Inheritance;
import model.MClass;
import model.MPackage;
import model.Operation;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class XMLTranslator {

	// creating a constructor of file class and parsing an XML file
	File file;
	// an instance of factory that gives a document builder
	DocumentBuilderFactory dbf;
	// an instance of builder to parse the specified xml file
	DocumentBuilder db;
	Document doc;

	Node node;
	Templates templates = new Templates();

	enum codeGenerationTypes {
		xcode, other
	};
	
	public String generatedcode_Text;
	private MPackage mPackage;

	public XMLTranslator(String pathToXMLFile) {
		try {
			// creating a constructor of file class and parsing an XML file
			file = new File(pathToXMLFile);
			// an instance of factory that gives a document builder
			dbf = DocumentBuilderFactory.newInstance();
			// an instance of builder to parse the specified xml file
			db = dbf.newDocumentBuilder();
			doc = db.parse(file);
			doc.getDocumentElement().normalize();
			// My Code
			node = doc.getDocumentElement(); // getting the document node
		} catch (Exception e) {
			e.printStackTrace();

		}

	}
	public void startTranslating() {

		
		NodeList conceptsList = node.getChildNodes();
		mPackage = new MPackage();
		mPackage.MClasses = new LinkedList<MClass>();
		
		

		// iterating through all concepts one by one.
		for (int i = 0; i < conceptsList.getLength(); i++) {
			
			//creating an object of MClass
			MClass mClass = new MClass();

			// conceptNode will be a single Node containing a concept
			Node conceptNode = conceptsList.item(i);

			// type checking of node, it should be Element Node
			if (conceptNode.getNodeType() == Node.ELEMENT_NODE) {
				// converting node to element type
				Element conceptElement = (Element) conceptNode;

				// getting values from conceptElement and saving to appropriate variables
				// getting class name
				mClass.name = conceptElement.getElementsByTagName("name").item(0).getTextContent().trim();
				mClass.isAbstract = GetTextContentsOfElement("isabstract", conceptElement).trim().toLowerCase().equals("yes");
				
				//Getting Inheritance
				mClass.inheritances = (List<Inheritance>)GetInnerConcepts("inheritance", conceptElement);
				
				//getting Associations
				mClass.associations = (List<Association>)GetInnerConcepts("association", conceptElement); 
				
				//getting Containment
				mClass.containments = (List<Containment>)GetInnerConcepts("containment", conceptElement);
				
				//getting attributes
				mClass.attributes = (List<Attribute>)GetInnerConcepts("attribute", conceptElement);
				
				//getting operations
				mClass.operations = (List<Operation>)GetInnerConcepts("operation", conceptElement);
				
				//storing in the list
				mPackage.MClasses.add(mClass);

			}
		}

		//generating text from model
		generatedcode_Text = templates.generateTextFromXML(mPackage);
		

	}
	public Object GetInnerConcepts(String nameOfConcept, Element conceptElement) {
		//New code for MClass
		List<Object> returnObject = new ArrayList<Object>();
		NodeList innerList = conceptElement.getElementsByTagName(nameOfConcept);
			for (int i = 0; i < innerList.getLength(); i++) {
				Node innerNode = innerList.item(i);
				if (innerNode.getNodeType() == Node.ELEMENT_NODE) {
					Element innerElement = (Element) innerNode;
					String innerElementName = GetTextContentsOfElement("name", innerElement).trim();
					String innerElementType = GetTextContentsOfElement("type", innerElement).trim();
					String innerElementCardinality = GetTextContentsOfElement("cardinality", innerElement).trim().toLowerCase();
					//calculating cardinality//
					Cardinality cardinality;
					switch (innerElementCardinality) {
					case "zero to many":
						cardinality = Cardinality.zeroToMany;
						break;
					case "one to many":
						cardinality = Cardinality.oneToMany;
						break;
					
					default: 
						cardinality = null;
					
					}
					//end of calculating cardinality
					if (!innerElementName.equals(""))
					
					{
						switch (nameOfConcept) {
						case "inheritance":
							Inheritance inheritance = new Inheritance();
							inheritance.name = innerElementName;
							returnObject.add(inheritance);
							break;
						
						case "association":
							Association association = new Association();
							association.name = decapitalizeFirstLetterOfString(innerElementName); //in XML, the name association is actually the type. so we just lowered first letter to generate name
							association.type = innerElementName; //In Input XML File, the name of the association is actually the type of containment
							association.cardinality = cardinality;
							returnObject.add(association);
							break;
						
						case "containment":
							Containment containment = new Containment();
							containment.name = decapitalizeFirstLetterOfString(innerElementName); //in XML, the name containment is actually the type. so we just lowered first letter to generate name
							containment.type = innerElementName; //In Input XML File, the name of the containment is actually the type of containment
							containment.cardinality = cardinality;
							returnObject.add(containment);
							break;
						
						case "operation":
							Operation operation = new Operation();
							operation.name = innerElementName;
							operation.type = innerElementType;
							returnObject.add(operation);
							break;
							
						case "attribute": 
							Attribute attribute = new Attribute();
							attribute.name = innerElementName;
							attribute.type = innerElementType;
							returnObject.add(attribute);
							break;
						default:
							throw new IllegalArgumentException("Unexpected value: " + nameOfConcept);
						}
					}
				}
				
			}
			return returnObject;
		
	}
	private String GetTextContentsOfElement(String name, Element element)
	{
		Element e = (Element)element.getElementsByTagName(name).item(0);
		if (e != null) {
			return e.getTextContent();
		}
		else return "";
		
	}
	private  String decapitalizeFirstLetterOfString(String string) {
	//    return string == null || string.isEmpty() ? "" : Character.toLowerCase(string.charAt(0)) + string.substring(1);
		 return string == null || string.isEmpty() ? "" : string.toLowerCase();
	}

	public String getFirstLevelTextContent(Node node) {
		NodeList list = node.getChildNodes();
		StringBuilder textContent = new StringBuilder();
		for (int i = 0; i < list.getLength(); ++i) {
			Node child = list.item(i);
			if (child.getNodeType() == Node.TEXT_NODE)
				textContent.append(child.getTextContent());
		}
		return textContent.toString();
	}

}
