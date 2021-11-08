package pssnlp;


///////////////////////////// LIBRARIES///////////////////////////////////////
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
// Regular Expression libraries
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// XML LIBRARY DOM Parser
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
// Library for POS Tagging
import edu.stanford.nlp.tagger.maxent.*;


public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//********************************************** XML  *************************************************************** 
		//      Some XML variales globally declared 
		Element mainElement = null;
	    Text concept_name=null;
		Text attriname = null;
		Text attritype = null;
		Text containmentname = null;
		Text inheritancename=null;
		Text associationname =null;
		Element rootElement=null;
		Element conceptname=null;
		Text cardinalty=null;
		Text opername= null;
		Text opertype= null;
		Text constraintname= null;
		
		//      Addressing towards XML file 
		File xmlfile =new File("pss.xml");
		if(xmlfile.exists())
		{
			//DocumentBUILDERfACTORY
			DocumentBuilderFactory docFactory= DocumentBuilderFactory.newInstance();
			try {
			//DocumentBuilder
				DocumentBuilder docBuilder= docFactory.newDocumentBuilder();
				Document xmlDoc= docBuilder.newDocument();
				//create rootnode
				rootElement =xmlDoc.createElement("concepts");
			
		//********************************************** XML  *************************************************************** 
		
		//*******************************************************************************************************************
		
          ///////////////////////  string ///////////////////////////////////////
			/*	String trial = "Action  contains activity concept where cardinality is zero  to many. It contains object concept where cardinality is zero  to many. It contains block concept where cardinality is one to many. It has actionidentifier attribute of string type. # \r\n" + 
		 		"Object inherits DataFlowObject concept and DataObject concept. It has Objectidentifier attribute of string type. #\r\n" + 
		 		"DataFlowObject inherits Stream concept and buffer concept. #\r\n" + 
		 		"Stream has input and output attributes of Boolean type. #\r\n" + 
		 		"Buffer has input and output attributes of Boolean type. #\r\n" + 
		 		"DataFlowObject inherits Resource concept. #\r\n" + 
		 		"Resource inherits Pool concept. # \r\n" + 
		 		"Pool associates with resource concept where cardinality is zero to one. # \r\n"
		 		+ "Activity concept can only be instantiated after Action concept. ";       */
				
				String trial = " PssModel contains AbstractComponent concept where cardinality is zero to many. # " +
 
"AbstractComponent inherits Component concept and RootComponent concept. It contains Action concept where cardinality is one to many. It has componentidentifier attribute of string type. # "+ 
 
"Component contains DataObject concept where cardinality is zero to many. It inherits ComponentInvocation concept# "+ 
 
"RootComponent associates with ComponentInvocation concept where cardinality is zero to one. It associates with ActionInvocation concept where cardinality is zero to one. # "+ 
 
"ComponentInvocation associates with Component concept where cardinality is zero to many. # " +
 
 
"ActionInvocation associates with Action concept where cardinality is zero to many. # " +
 
 
"Action inherits ActionInvocation concept. It contains Activity concept where cardinality is zero to many. It contains Object concept where cardinality is zero to many. It has actionidentifier attribute of string type. # "+
 
 
"Activity contains ComponentInvocation concept where cardinality is zero to many. # "+
 
 
"Object inherits DataFlowObject and DataObject concept. It has Objectidentifier attribute of string type. # "+
 
"DataFlowObject inherits Stream concept and buffer concept. # "+
 
"Stream has input and output attributes of Boolean type. # "+
 
"Buffer has input and output attributes of Boolean type. # "+
 
"DataFlowObject inherits Resource concept. # "+
 
"Resource inherits Pool concept. # " +
 
"Pool associates with resource concept where cardinality is zero to one. " ;
	    
		 /////////////////////// Passing string for POS TAGGING ///////////////////////////////////////
	        MaxentTagger tagger =  new MaxentTagger("stanford-tagger-4.2.0/stanford-postagger-full-2020-11-17/models/english-left3words-distsim.tagger.");
	    /////////////////////// Splitting  string on # sign (Every concept will be placed into each index of this string) ///////////////////////////////////////
	        String[] taged = tagger.tagString(trial).split("\\#+"); 

	   
	    //   LOOP JUST FOR PRINTING AND CHECKING TAGGING
	        for (String token : taged) {
	          System.out.println(token);  
	       } 
	        
	  
	  //********************************************** *************************************************************** 
	  /////////////////////////// 1.  PATTERN to check concept//////////////////////////////
	 	   
	   String patternn = "\\w+NN|NNP|PRP\\b.*(?:\\W+\\W+){0,0}?\\w+VBZ\\b.*\\W+(?:\\w+\\W+){0,0}?\\w+NN|NNP\\b.*\\W+(?:\\w+\\W+){0,0}?\\w+WRB\\b.*\\W+(?:\\w+\\W+){0,0}?\\w+NN|JJ\\b";
	      // Create a Pattern object
	      Pattern r = Pattern.compile(patternn);
	
	      // Loop to traverse the whole string and extract all concepts from it with the help of pattern matching
	      
	     for(int i=0; i< taged.length; i++)
	     {
	    	 
	    	 // Making attribute's slots with their corresponding name in XML file
	    	 conceptname =xmlDoc.createElement("name");
			 Element attributes =xmlDoc.createElement("attribute");
			 Element attributename =xmlDoc.createElement("attributename");
			 Element attributtype =xmlDoc.createElement("attributtype");		
			 Element containment = xmlDoc.createElement("containment");
		//	 Element containname = xmlDoc.createElement("containment name");
            Element association = xmlDoc.createElement("association");
       //    Element associoname = xmlDoc.createElement("association name");
			 Element inheritance = xmlDoc.createElement("inheritance");
		// Element inheritename = xmlDoc.createElement("inheritance name");
			 Element operation =xmlDoc.createElement("operation");
			 Element operationname =xmlDoc.createElement("operationname");
			 Element operationtype =xmlDoc.createElement("operationtype");
			 Element constraint =xmlDoc.createElement("constraint");
			 Element cardinality =xmlDoc.createElement("cardinality");
					
	    	 
	    	 
	       // Taking string which will split on every fullstop (every line) of every PSS concept that is written in paragrpahs text.
			 // In order to traverse each line which will fed into patterns.
	       String[] trio= taged[i].toString().split("\\.+");
	       
	       // This if else In order to get the name of every concept from text.
	       if(i==0)
	      {
	    	   // Printing concept name in console output
	       System.out.println("***************************************************************");
	       System.out.println("**************         CONCEPT"+i+"             **********************");
	       System.out.println("**************         "+trio[0].substring(0, 15).replaceAll("_\\S+", "")+"             *******************");
	       		// Printing concept name in XML File
	       mainElement =xmlDoc.createElement("CONCEPT"+i);
			concept_name= xmlDoc.createTextNode(trio[0].substring(0, 15).replaceAll("_\\S+", ""));
			conceptname.appendChild(concept_name); 
	          
	       System.out.println("***************************************************************");
	      }
	       else
	       {
	    	// Printing concept name in console output
	    	   System.out.println("***************************************************************");
		       System.out.println("**************         CONCEPT"+i+"             **********************");
		       System.out.println("**************         "+trio[0].substring(4, 15).replaceAll("_\\S+", "")+"             *******************");
		    // Printing concept name in XML File
		       mainElement =xmlDoc.createElement("CONCEPT"+i);
			   concept_name= xmlDoc.createTextNode(trio[0].substring(4, 15).replaceAll("_\\S+", ""));
		       conceptname.appendChild(concept_name);
		       System.out.println("***************************************************************"); 
	       }
	    
	       int temp=0;
	       String trioindex1[]= new String[10];
	       // Inner for loop to traverse TRio string traversing ( traversing each line one by one of a concept.
	      for (int k=0; k< trio.length; k++)
	   {
	    // pattern object 'r' is now matching lines from string trio, 
	      Matcher m = r.matcher(trio[k]);  
	       
	           if (m.find( )) {      // if string matches
	             if(trio[k].contains("associates_VBZ")) // then find associates word from it
	             {  
	    //    when found then, extracting that associated concept for print purpose
	            	 
	             
	               String pattero = " \\w+NN\\b.(?:\\w+\\W+){0,0}?\\w+(NN)\\b.*?(?=\\w+WRB?\\b)";
	              //  String pattero = " \\w+NN\\b.\\w+NN\\b. | \\w+NNP\\b.\\w+NN\\b.";
	                Pattern rero = Pattern.compile(pattero);
	                 Matcher m0 = rero.matcher(trio[k]);  
	                if (m0.find( )) 
	                {
	                	// Printing in console output
	                	
	            
	                System.out.println("It is the association relationship between   "+trio[0].substring(4, 20).replaceAll("_\\S+", "")+" and   "+m0.group().replaceAll("_\\S+", ""));   
	                    // Storing in XML file
	                
	                 associationname= xmlDoc.createTextNode(m0.group().replaceAll("_\\S+", ""));
	         		 association.appendChild(associationname);
	          		
	             		String cardinalitypattern ="(?<=\\w+WRB\\b.\\w+NN\\b.\\w+VBZ\\b).*"; // after these three words string
	             		
		                Pattern cp = Pattern.compile(cardinalitypattern);
		                Matcher mc = cp.matcher(trio[k]);  
		                if (mc.find( )) 
		             {
		                	// Printing in console output
		                
		                	 System.out.println(" "+ mc.group().replaceAll("_\\S+", ""));  
		                	// Storing in XML file
		                	 cardinality =xmlDoc.createElement("cardinality");
		                	 cardinalty= xmlDoc.createTextNode(mc.group().replaceAll("_\\S+", ""));
		                	 cardinality.appendChild(cardinalty);
		                	 association.appendChild(cardinality);
		             }
	                }
	             }
	             // if assciates not found, check for contains word 
	            else  if(trio[k].contains("contains_VBZ")) 
	             {
	            	
	               String pattero = " \\w+NN\\b.\\w+NN\\b.?(?=\\w+WRB\\b)|\\w+NNP\\b.\\w+NN\\b.?(?=\\w+WRB\\b)";
	               //| \\w+NNP\\b.\\w+NN\\b.*?(?=\\w+WRB?\\b)";
	                Pattern rero = Pattern.compile(pattero);
	                Matcher m0 = rero.matcher(trio[k]);  
	                if (m0.find( )) 
	             {	
	                	// Printing in console output
	               
	               System.out.println("It is the containment relationship between   "+trio[0].substring(0, 15).replaceAll("_\\S+", "")+" and   "+ m0.group().replaceAll("_\\S+", ""));   
	             // Storing in XML file
	               
	                containmentname= xmlDoc.createTextNode(m0.group().replaceAll("_\\S+", "")+"       ");
	              
	    			containment.appendChild(containmentname);
	    	 		
             		String cardinalitypattern ="(?<=\\w+WRB\\b.\\w+NN\\b.\\w+VBZ\\b).*";
             			
	                Pattern cp = Pattern.compile(cardinalitypattern);
	                Matcher mc = cp.matcher(trio[k]);  
	                if (mc.find( )) 
	             {
	                	// Printing in console output
	                
	              
	                	 System.out.println("Cardnilaity is : "+ mc.group().replaceAll("_\\S+", ""));  
	                	// Storing in XML file
	                	 cardinality =xmlDoc.createElement("cardinality");
	                	 cardinalty= xmlDoc.createTextNode(mc.group().replaceAll("_\\S+", ""));
	                	 cardinality.appendChild(cardinalty);
	                	 containment.appendChild(cardinality);
	             }
	             }
	             } 
	          // if contains not found, check for inherits word 
	            else  if(trio[k].contains("inherits_VBZ"))
	             {
	            	
	            	String pattero = " \\w+NN\\b.\\w+NN\\b.*|\\w+NNP\\b.\\w+NN\\b.*";
	            			//" \\w+NNP\\b.(?:\\w+\\W+){0,0}?\\w+(NN)\\b.* |  \\w+NN\\b.(?:\\w+\\W+){0,0}?\\w+(NN)\\b.* ";
	            
	                Pattern rero = Pattern.compile(pattero);
	                Matcher m0 = rero.matcher(trio[k]);  
	                if (m0.find( )) 
	             {
	                	// Printing in console output
	               
	                	 System.out.println("It is the inheritance relationship between   "+trio[0].substring(4, 20).replaceAll("_\\S+", "")+" and   "+ m0.group().replaceAll("_\\S+", ""));  
	                	// Storing in XML file
	                	 
	        			  inheritance =xmlDoc.createElement("inheritance");
	                	 inheritancename= xmlDoc.createTextNode(m0.group().replaceAll("_\\S+", ""));
	             		inheritance.appendChild(inheritancename);
	             }
	             } 
	             // NOw for attribute concept checking
	            else if (trio[k].contains("has_VBZ"))
	      {
	            	// pattern to get attribute wali line
	            	String pattern = "\\w+NN|PRP\\b.(?:\\W+\\W+){0,0}?\\w+VBZ\\b."
	                         + "\\W+(?:\\w+\\W+){0,0}?\\w+NN|VBN\\b.";
	                 
	                 Pattern r2 = Pattern.compile(pattern);
	                 Matcher mop = r2.matcher(trio[k]);  
	                 if (mop.find( )) {   
	                
	                // pattern to get attributes name only for output purpose
	                String pattero = "(?<=\\w+VBZ\\b).*?(?=\\w+IN\\b)"; // pattern to extract part of string Between VBZ and IN 
	                 Pattern rero = Pattern.compile(pattero);
	                 Matcher m0 = rero.matcher(trio[k]);  
	                 if (m0.find( )) {
	                	 
	                	 // printing in console output
	                     System.out.println("Attributes are : "+m0.group().replaceAll("_\\S+", ""));
	                     // printing in XML file 
	                     attriname= xmlDoc.createTextNode(m0.group().replaceAll("_\\S+", ""));
	                      attributename.appendChild(attriname);
	                     
	                   // pattern to get attributes type name only for output purpose
	                     String patterntype = "(?<=\\w+IN\\b).*"; // IN sentence mn jahan b milay us k bad ki sari string uthane ka pattern
	                 
		                 Pattern rtype = Pattern.compile(patterntype);
		                 Matcher t0 = rtype.matcher(trio[k]);  
		                 if (t0.find( )) {
		                	// printing in console output
		                 System.out.println("TYPE : "+t0.group().replaceAll("_\\S+", ""));
		              // printing in XML file 
	                     attritype = xmlDoc.createTextNode(t0.group().replaceAll("_\\S+", ""));
	                     attributtype.appendChild(attritype);
		                 }  
	           }             
	                 }         
	      }  
	           else
	             { ///// Constraint pattern//////////
	                 String pattern = "\\w+NN\\b.*(?:\\W+\\W+){0,0}?\\w+MD\\b.*\\W+(?:\\w+\\W+){0,0}?\\w+RB\\b";
	                 Pattern r2 = Pattern.compile(pattern);
	                 Matcher m2 = r2.matcher(trio[k]);  
	                 if (m2.find( ))
	                 {
	                  String pattero = ".*";
	                 Pattern rero = Pattern.compile(pattero);
	                 Matcher m0 = rero.matcher(trio[k]);  
	                 if (m0.find( )) {
	                     System.out.println("Constraint:::"+m0.group().replaceAll("_\\S+", ""));
	                     }
	                 }
	             }
	           }
		      // XML 
	           mainElement.appendChild(conceptname);
	           mainElement.appendChild(containment);
	           mainElement.appendChild(association);
	           mainElement.appendChild(inheritance);
	           mainElement.appendChild(attributes);
	           attributes.appendChild(attributename);
	           attributes.appendChild(attributtype);
	           mainElement.appendChild(operation);
	 		   operation.appendChild(operationname);
	 		   operation.appendChild(operationtype);
	 		   mainElement.appendChild(constraint);
	   }
	 	       rootElement.appendChild(mainElement); 
	 }
		      xmlDoc.appendChild(rootElement);
			  DOMSource source= new DOMSource(xmlDoc);
			  Result result= new StreamResult(xmlfile);
			  FileOutputStream oustream = new FileOutputStream(xmlfile);
	          TransformerFactory transformerFactory= TransformerFactory.newInstance();
			  Transformer transformer;
			try {
				transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
				transformer.setOutputProperty(OutputKeys.INDENT, "no");
				transformer.transform(source, result);
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	      
	   }
	     catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	     catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	     
	}
}
	
}

