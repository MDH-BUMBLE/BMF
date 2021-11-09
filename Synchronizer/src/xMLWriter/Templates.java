package xMLWriter;

import model.MPackage;
import model.Operation;
import model.Association;
import model.Attribute;
import model.Cardinality;
import model.Containment;
import model.Inheritance;
import model.MClass;

public class Templates {
	
	public String generateTextFromXML(MPackage mPackage)
	{
		String tempTextString, finalTextString;
		finalTextString = "";
		for (MClass mClass : mPackage.MClasses) {
			tempTextString = "";
			String itKeywordOrNameOfClassString = mClass.name;
			//adding abstract keyword 
			if (mClass.isAbstract == true)
				{
				tempTextString += itKeywordOrNameOfClassString + " is abstract concept. ";
				itKeywordOrNameOfClassString = "It";
				}
			
			//adding inheritances
			for (Inheritance inheritance : mClass.inheritances) {
					tempTextString += itKeywordOrNameOfClassString + " inherits " +inheritance.name + ". ";
					itKeywordOrNameOfClassString = "It";
					
			}
			
			//adding association
			
			for (Association association : mClass.associations) {
				tempTextString += itKeywordOrNameOfClassString +  " associates " + association.type + " concept where cardinality is " + association.cardinality.getText() + ". ";
				itKeywordOrNameOfClassString = "It";
				
			}
			
			//adding containment

			for (Containment containment : mClass.containments) {
				tempTextString += itKeywordOrNameOfClassString  + " contains " + containment.type + " concept where cardinality is " + containment.cardinality.getText() + ". ";
				itKeywordOrNameOfClassString = "It";
				
			}
			
			//adding attributes

			for (Attribute attribute : mClass.attributes) {
				tempTextString += itKeywordOrNameOfClassString + " has " + attribute.name + " attribute of type " + attribute.type + ". ";
				itKeywordOrNameOfClassString = "It";
				
			}

			for (Operation operation : mClass.operations) {
				tempTextString += itKeywordOrNameOfClassString + " has " + operation.name + " operation of type  " + operation.type + ". ";
				itKeywordOrNameOfClassString = "It";
				
			}
			
			//finally concatenating in resultant string
			finalTextString += tempTextString + "\n\n";
			
			
		}
		return finalTextString;
		
	}
	
	

}
