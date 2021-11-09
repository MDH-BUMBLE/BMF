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
	
	public String generateXCoreFromModel(MPackage mPackage)
	{
		String tempTextString, finalTextString;
		finalTextString = "";
		for (MClass mClass : mPackage.MClasses) {
			tempTextString = "";
			//adding abstract keyword 
			tempTextString = (mClass.isAbstract) ? "class " : "abstract class ";
			//adding name of class
			tempTextString += mClass.name + " ";
			
			//adding inheritances
			if (mClass.inheritances.size() > 0) {
				tempTextString += "extends ";
				for (Inheritance inheritance : mClass.inheritances) {
					tempTextString += inheritance.name + ", ";
					
				}
				tempTextString = tempTextString.substring(0,tempTextString.length() - 2); // Removing last comma
			}
			
			//adding bracket of class
			tempTextString += "{\n";
			
			//adding association
			for (Association association : mClass.associations) {
				tempTextString += "refers " + association.type + association.cardinality.getSymbol()  +  " " + association.name + "\n";
				
			}
			
			//adding containment
			for (Containment containment : mClass.containments) {
				tempTextString += "contains " + containment.type + containment.cardinality.getSymbol()  + " " + containment.name + "\n";
				Cardinality cardinality;
				
			}
			
			//adding attributes
			for (Attribute attribute : mClass.attributes) {
				tempTextString += attribute.type + " " + attribute.name + "\n";
				
			}
			
			//adding operations
			for (Operation operation : mClass.operations) {
				tempTextString += operation.type + " " + operation.name + "()" + "\n";
				
			}
			//closing bracket
			tempTextString += "}\n\n";
			
			//finally concatenating in resultant string
			finalTextString += tempTextString;
			
			
		}
		return finalTextString;
	}
	public String generateOtherFromXML(MPackage mPackage)
	{
		String tempTextString, finalTextString;
		finalTextString = "";
		for (MClass mClass : mPackage.MClasses) {
			tempTextString = "";
			String instaceCanBeRootString;
			//adding abstract keyword 
			if (mClass.isAbstract == true)
				{
				tempTextString += "abstract concept ";
				instaceCanBeRootString = "instance can be root: true";
				}
			else 
			{
				tempTextString += "concept ";
				instaceCanBeRootString = "instance can be root: false";
			}
			//adding name of class
			tempTextString += mClass.name + " ";
			
			//adding inheritances
			if (mClass.inheritances.size() > 0) {
				tempTextString += "extends ";
				for (Inheritance inheritance : mClass.inheritances) {
					tempTextString += inheritance.name + ", ";
					
				}
				tempTextString = tempTextString.substring(0,tempTextString.length() - 2) + " "; // Removing last comma
				tempTextString += "implements <none>\n";
				tempTextString += instaceCanBeRootString + "\nalias: <no alias>\nshort description: <no short description>\n";
			}
			else {
				tempTextString += "extends BaseConcept  implements <none>\n";
				tempTextString += instaceCanBeRootString + "\nalias: <no alias>\nshort description: <no short description>\n";
			}
			
			
			
			//adding association
			tempTextString += "\nreferences: \n";
			if (mClass.associations.size() == 0) tempTextString +=  "<< ... >> \n";
			for (Association association : mClass.associations) {
				tempTextString += association.name  +  " : " + association.type + association.cardinality.getMPSSymbol() + "\n";
				
			}
			
			//adding containment
			tempTextString += "\nchildren: \n";
			if (mClass.containments.size() == 0) tempTextString +=  "<< ... >> \n";
			for (Containment containment : mClass.containments) {
				tempTextString += containment.name  + " : " + containment.type + containment.cardinality.getMPSSymbol() + "\n";
				
			}
			
			//adding attributes
			tempTextString += "\nproperties: \n";
			if (mClass.attributes.size() == 0) tempTextString +=  "<< ... >> \n";
			for (Attribute attribute : mClass.attributes) {
				tempTextString += attribute.name + " : " + attribute.type + "\n";
				
			}
			
			//adding operations
			//tempTextString += "\noperations: \n";
			//if (mClass.operations.size() == 0) tempTextString +=  "<< ... >> \n";
			//for (Operation operation : mClass.operations) {
			//	tempTextString += operation.name + "() :  " + operation.type + "\n";
				
			//}
			
			//finally concatenating in resultant string
			finalTextString += tempTextString + "\n\n";
			
			
		}
		return finalTextString;
		
	}
	
	

}
