package xMLWriter;


import java.io.FileWriter;
import java.io.IOException;

public class Main {
	
	
			

	public static void main(String[] args) {
		
		XMLTranslator translator = new XMLTranslator("pss1.xml");
		translator.startTranslating();
		System.out.println(translator.generatedcode_Xcode + "\n----------\n");
		System.out.println(translator.generatedcode_Other);
		
		//creating files
		createFile("GeneratedCode.xcore", translator.generatedcode_Xcode);
		
		createFile("GeneratedCodeOther.txt", translator.generatedcode_Other);
	
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
