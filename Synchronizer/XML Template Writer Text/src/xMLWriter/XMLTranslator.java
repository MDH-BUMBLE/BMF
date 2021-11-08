package xMLWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class XMLTranslator {

	// creating a constructor of file class and parsing an XML file
	File file;
	// an instance of factory that gives a document builder
	DocumentBuilderFactory dbf;
	// an instance of builder to parse the specified xml file
	DocumentBuilder db;
	Document doc;

	Node node;
	public String generatedcode_Textual;

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
		String className, textualGenerationString ;
		NodeList conceptsList = node.getChildNodes();
		textualGenerationString = "";
		
		//iterating through all concepts one by one.
		for (int i = 0; i < conceptsList.getLength(); i++) {
			
			//variables for output
			className = textualGenerationString = "";
			
			//conceptNode will be a single Node containing a concept
			Node conceptNode = conceptsList.item(i);
			
			// type checking of node, it should be Element Node
			if (conceptNode.getNodeType() == Node.ELEMENT_NODE) {
				// converting node to element type
				Element conceptElement = (Element) conceptNode;
				
				// getting values from conceptElement and saving to appropriate variables
				//getting class name
				className = conceptElement.getElementsByTagName("name").item(0).getTextContent().trim();
				
				
				
				//getting inheritance and saving in variable extendNode
				Node extendNode = conceptElement.getElementsByTagName("inheritance").item(0);
				if (extendNode != null && extendNode.getTextContent().trim() != "") {
					String extendsString = conceptElement.getElementsByTagName("inheritance").item(0).getTextContent().trim();
					
					//generating for textual generation
					textualGenerationString += className + " inherits " + extendsString + " concept. It ";
				}
				else { textualGenerationString += className + " "; }
				
				
				//getting containing and generating textual generation
				textualGenerationString += getAssocAndCardinality("containment", conceptElement);
				
				//getting association and generating textual generation
				textualGenerationString += getAssocAndCardinality("association", conceptElement);
				
				//getting attributes
				NodeList attributesList = conceptElement.getElementsByTagName("attribute");
				//getting attributes and generating for textual generation
				textualGenerationString += GetAttributesText(attributesList);
				
				
				//generating output for textual
				//removing last "It String" 
				if (textualGenerationString.substring(textualGenerationString.length() - 4, textualGenerationString.length()).equals(" It "))
					{
						textualGenerationString = textualGenerationString.substring(0,textualGenerationString.length() - 4);
	
					}
				generatedcode_Textual += textualGenerationString +"\n";
			}
		}

	}
	public String GetAttributesText(NodeList attributeList) {
		String returnTextString = "";
		for (int i = 0; i < attributeList.getLength(); i++) {
			Node attributeNode = attributeList.item(i);
			if (attributeNode.getNodeType() == Node.ELEMENT_NODE) {
				// converting
				Element attribute = (Element) attributeNode;
				if (attribute.getElementsByTagName("attributename").item(0) == null) continue ; 
				if (attribute.getElementsByTagName("attributetype").item(0) == null) continue ; 
				String nameString = attribute.getElementsByTagName("attributename").item(0).getTextContent();
				String typeString = attribute.getElementsByTagName("attributetype").item(0).getTextContent();
				returnTextString = "has " + nameString + " attribute of type " + typeString + ". It ";
				
			}
			
		}
		return returnTextString;
	}
	public String GetOperationssText(NodeList operationsList) {
		String returnTextString = "";
		for (int i = 0; i < operationsList.getLength(); i++) {
			Node operationNode = operationsList.item(i);
			if (operationNode.getNodeType() == Node.ELEMENT_NODE) {
				// converting
				Element operation = (Element) operationNode;
				if (operation.getElementsByTagName("operationname").item(0) == null) continue;
				if (operation.getElementsByTagName("operationtype").item(0) == null) continue;
				String nameString = operation.getElementsByTagName("operationname").item(0).getTextContent();
				String typeString = operation.getElementsByTagName("operationtype").item(0).getTextContent();
				if (nameString.trim() == "") continue;
				if (typeString.trim() == "") continue;
				returnTextString += typeString + " " + nameString + "()\n";
				
			}
			
		}
		return returnTextString;
	}
	public String getAssocAndCardinality(String keyword, Element conceptElement) {
		String specialAttributeName = "";
		String cardinality = "";
		String cardinalityString = "";
		if (keyword == "containment") {
			specialAttributeName =getFirstLevelTextContent(conceptElement.getElementsByTagName("containment").item(0)).trim();
			if (specialAttributeName != "") {
				cardinality = ((Element) conceptElement).getElementsByTagName("cardinality").item(0).getTextContent().trim().toLowerCase();
				cardinalityString = cardinality;
				if (cardinality.equals("zero to many")) cardinality = "[]";
				else if (cardinality.equals("zero to one")) cardinality = "[1]";
				else cardinality = "[" + cardinality + "]";
				
				specialAttributeName = "contains " + specialAttributeName + " concept where cardinality is " + cardinalityString + ". It ";
				
				}
		}
		else if (keyword == "association") {
			specialAttributeName =getFirstLevelTextContent(conceptElement.getElementsByTagName("association").item(0)).trim();
			if (specialAttributeName != "") {
				cardinality = ((Element) conceptElement).getElementsByTagName("cardinality").item(0).getTextContent().trim().toLowerCase();
				cardinalityString = cardinality;
				if (cardinality.equals("zero to many")) cardinality = "[]";
				else if (cardinality.equals("zero to one")) cardinality = "[1]";
				else cardinality = "[" + cardinality + "]";

				specialAttributeName = "associates " + specialAttributeName + " concept where cardinality is " + cardinalityString + ". It ";
				}
			
		}
		return specialAttributeName;
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
