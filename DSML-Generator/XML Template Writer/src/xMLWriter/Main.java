package xMLWriter;


import java.io.FileWriter;
import java.io.IOException;

public class Main {
	
	
			

	public static void main(String[] args) {
		
		XMLTranslator translator = new XMLTranslator("pss.xml");
		translator.startTranslating();
		//generating xcode
		System.out.println(translator.generatedcode_Xcore);
		createFile("GeneratedCode.xcore", translator.generatedcode_Xcore);
		//generating other code
		System.out.println(translator.generatedcode_MPS);
		createFile("GeneratedStructure.MPS", translator.generatedcode_MPS);
	
		}
	
	public static void createFile(String fileNameString, String textToWriteString) {
	    try {
	        FileWriter myWriter = new FileWriter(fileNameString,false);
	        myWriter.write(textToWriteString);
	        myWriter.close();
	        System.out.println("Successfully wrote to the file.");
	      
	    	} catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	  }	

}
