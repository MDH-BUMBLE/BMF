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
	Templates templates = new Templates();

	enum codeGenerationTypes {
		xcode, other
	};

	public String generatedcode_Xcore;
	public String generatedcode_MPS;

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

		// System.out.println("Root element:" + node.getNodeName());

		String classBodyText;
		String className, abstractName, specialAttributeName, extendName;
		String propertiesText, childrenText, referencesText;
		NodeList conceptsList = node.getChildNodes();
		generatedcode_MPS = generatedcode_Xcore = "";

		// iterating through all concepts one by one.
		for (int i = 0; i < conceptsList.getLength(); i++) {

			// variables for output
			className = abstractName = specialAttributeName = extendName = classBodyText = "";
			propertiesText = childrenText = referencesText = "";

			// conceptNode will be a single Node containing a concept
			Node conceptNode = conceptsList.item(i);

			// type checking of node, it should be Element Node
			if (conceptNode.getNodeType() == Node.ELEMENT_NODE) {
				// converting node to element type
				Element conceptElement = (Element) conceptNode;

				// getting values from conceptElement and saving to appropriate variables
				// getting class name
				className = conceptElement.getElementsByTagName("name").item(0).getTextContent().trim();

				// getting inheritance and saving in variable extendNode
				Node extendNode = conceptElement.getElementsByTagName("inheritance").item(0);
				if (extendNode != null && extendNode.getTextContent().trim() != "") {
					String extendsString = conceptElement.getElementsByTagName("inheritance").item(0).getTextContent()
							.trim();

					extendName = " extends " + extendsString;
				}

				// getting containing and appending in classBodyText variable
				specialAttributeName = getAssocAndCardinality("containment", conceptElement, codeGenerationTypes.xcode);
				if (specialAttributeName.trim() != "")
					classBodyText += specialAttributeName + "\n";
				// getting containing and generating for children
				childrenText = getAssocAndCardinality("containment", conceptElement, codeGenerationTypes.other);

				// getting association and appending in classBodyText variable
				specialAttributeName = getAssocAndCardinality("association", conceptElement, codeGenerationTypes.xcode);
				if (specialAttributeName.trim() != "")
					classBodyText += specialAttributeName + "\n";
				// getting association and generating for references
				referencesText = getAssocAndCardinality("association", conceptElement, codeGenerationTypes.other);

				// getting attributes and appending in class body text
				NodeList attributesList = conceptElement.getElementsByTagName("attribute");
				classBodyText += GetAttributesText(attributesList, codeGenerationTypes.xcode);
				// getting attributes and generating for properties
				propertiesText = GetAttributesText(attributesList, codeGenerationTypes.other);

				// getting operations and appending in class body text
				NodeList operationsList = conceptElement.getElementsByTagName("operation");
				classBodyText += GetOperationssText(operationsList, codeGenerationTypes.xcode);
				// no need for generating operations for other generations

				// generating output for xcode
				generatedcode_Xcore += templates.generateClass(abstractName, className, extendName, classBodyText)
						+ "\n";

				// generating output for other
				generatedcode_MPS += templates.generateOtherGeneration(className, propertiesText, childrenText,
						referencesText, extendName) + "\n";
			}
		}

	}

	public String GetAttributesText(NodeList attributeList, codeGenerationTypes codeGenerationType) {
		String returnTextString = "";
		for (int i = 0; i < attributeList.getLength(); i++) {
			Node attributeNode = attributeList.item(i);
			if (attributeNode.getNodeType() == Node.ELEMENT_NODE) {
				// converting
				Element attribute = (Element) attributeNode;
				if (attribute.getElementsByTagName("attributename").item(0) == null)
					continue;
				if (attribute.getElementsByTagName("attributetype").item(0) == null)
					continue;
				String nameString = attribute.getElementsByTagName("attributename").item(0).getTextContent();
				String typeString = attribute.getElementsByTagName("attributetype").item(0).getTextContent();
				if (codeGenerationType == codeGenerationTypes.xcode)
					returnTextString += typeString + " " + nameString + "\n";
				else if (codeGenerationType == codeGenerationTypes.other)
					returnTextString += nameString + " : " + typeString + "\n";

			}

		}
		return returnTextString;
	}

	public String GetOperationssText(NodeList operationsList, codeGenerationTypes codeGenerationType) {
		String returnTextString = "";
		for (int i = 0; i < operationsList.getLength(); i++) {
			Node operationNode = operationsList.item(i);
			if (operationNode.getNodeType() == Node.ELEMENT_NODE) {
				// converting
				Element operation = (Element) operationNode;
				if (operation.getElementsByTagName("operationname").item(0) == null)
					continue;
				if (operation.getElementsByTagName("operationtype").item(0) == null)
					continue;
				String nameString = operation.getElementsByTagName("operationname").item(0).getTextContent();
				String typeString = operation.getElementsByTagName("operationtype").item(0).getTextContent();
				if (nameString.trim() == "")
					continue;
				if (typeString.trim() == "")
					continue;
				returnTextString += typeString + " " + nameString + "()\n";

			}

		}
		return returnTextString;
	}

	public String getAssocAndCardinality(String keyword, Element conceptElement,
			codeGenerationTypes codeGenerationType) {
		String specialAttributeName = "";
		String cardinality = "";
		String cardinalityString = "";
		if (keyword == "containment") {
			specialAttributeName = getFirstLevelTextContent(conceptElement.getElementsByTagName("containment").item(0))
					.trim();
			if (specialAttributeName != "") {
				cardinality = ((Element) conceptElement).getElementsByTagName("cardinality").item(0).getTextContent()
						.trim().toLowerCase();
				cardinalityString = cardinality;
				if (cardinality.equals("zero to many"))
					cardinality = "[]";
				else if (cardinality.equals("zero to one"))
					cardinality = "[1]";
				else
					cardinality = "[" + cardinality + "]";

				if (codeGenerationType == codeGenerationTypes.xcode) {
					specialAttributeName += cardinality + " " + specialAttributeName.toLowerCase();
					specialAttributeName = "contains " + specialAttributeName;
				}

				else if (codeGenerationType == codeGenerationTypes.other) {
					specialAttributeName = specialAttributeName.toLowerCase() + " : " + specialAttributeName
							+ cardinality;
				}
			}
		} else if (keyword == "association") {
			specialAttributeName = getFirstLevelTextContent(conceptElement.getElementsByTagName("association").item(0))
					.trim();
			if (specialAttributeName != "") {
				cardinality = ((Element) conceptElement).getElementsByTagName("cardinality").item(0).getTextContent()
						.trim().toLowerCase();
				cardinalityString = cardinality;
				if (cardinality.equals("zero to many"))
					cardinality = "[]";
				else if (cardinality.equals("zero to one"))
					cardinality = "[1]";
				else
					cardinality = "[" + cardinality + "]";

				if (codeGenerationType == codeGenerationTypes.xcode) {
					specialAttributeName += cardinality + " " + specialAttributeName.toLowerCase();
					specialAttributeName = "refers " + specialAttributeName;
				} else if (codeGenerationType == codeGenerationType.other) {
					specialAttributeName = specialAttributeName.toLowerCase() + " : " + specialAttributeName
							+ cardinality;

				}
			}

		}
		return specialAttributeName;
	}

	public void saveToFile(String fileName) {

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
