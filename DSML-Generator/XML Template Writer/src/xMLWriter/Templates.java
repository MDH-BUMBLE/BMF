package xMLWriter;

public class Templates {
	
	public String generateClass (String txtAbstract, String txtClassName, String txtExtends, String txtBody ) {
		
		String responseTxt = "";
		responseTxt += txtAbstract + " class " + txtClassName + " " + txtExtends + " {\n";
		responseTxt += txtBody +"\n";
		responseTxt += "}\n";		
		return responseTxt;		
	}
	
	public String generateOperation() {
		
		
		return " Operation -> generateOperation is not implemented in templates Class";
	}
	
	public String generateAttribute(String type, String name) {
		String responseTxt = "";
		responseTxt += type + " " + name + ";";
		return responseTxt;
	}
	
	public String generateSpecialAttribute(String txtAttributeName, String txtType, String txtMultiplicity, String txtAttributeValue) {
		
		return txtAttributeName + " " + txtType + "[" + txtMultiplicity + "]" + txtAttributeValue;
	}
	public String generateOtherGeneration(String ConceptName, String propertiesText, String childrenText, String referencesText, String extendText) {
		String returnText = "concept " + ConceptName;
		if (extendText.trim() == "") returnText += "  extends BaseConcept  implements <none>\n";
		else returnText += extendText + " implements <none>\n";
		returnText += "instance can be root: false\n";
		returnText += "alias: <no alias>\n"; 
		returnText += "short description: <no short description>\n";
		 // properties contains variable and attribute name 
		returnText += "properties:\n";
		returnText += propertiesText;
		 // all contained concepts in main concept should be here with cardinality. For one to many syntax should be [0..n]
		returnText += "children:\n";
		returnText += childrenText;
		//here I will add containment

		  // all association concepts in main concept should be here with cardinality. For one to many syntax should be [0..n]

		returnText += "references:\n ";
		returnText += referencesText;
		//here I will add association
		return returnText;
	}
	public String generateTextualLanguage(String conceptName, String txtExtends, String containmentName, String xyz)
	{
		return "";
	}
	
	

}
