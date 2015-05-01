package org.un.dm.oict.gsd.odsreferencebuilder;

import java.io.File;

/* 
* Author: Kevin Thomas Bradley
* Date: 1-May-2015
* Description: This class is the entry point to the application, it loops
* through each .xml file within a directory and process this using other classes
* in the solution
* Version: 1.0
* Code Reviewer:
*/
public class Service {
	
	public static void main(String args[]) {
		performTask("/Users/kevinbradley/Desktop/testFiles");
	}
	
	static void performTask(String xmlFilesDirectory) {
		
		File dir = new File(xmlFilesDirectory);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				String currXmlFilename = child.getAbsolutePath();
				if (currXmlFilename.endsWith(".xml")) {
					SolrDocument currentSolrDocument = TextExtractorFile.readXmlDocument(currXmlFilename);
					SolrDocument newSolrDocument = new SolrDocument();
					ReferenceDocument newReferenceDocument = new ReferenceDocument();

					// Process the Solr Document - Cleanse & apply business logic
					newSolrDocument = FileProcessor.processFile(currentSolrDocument, newSolrDocument);
	
					// Extract references for all Symbol variations - to obtain that list
					// TODO Further refinement of the regex and output
					newReferenceDocument = FileProcessor.extractReferences(newSolrDocument, newReferenceDocument);
					/*String refs = "[";
					for(String s : newReferenceDocument.getReferences()) {
						refs += "{" + s + "},";
					}
					refs += "]";
					System.out.println("Id : " + newReferenceDocument.getId() + " refs: " + refs);*/
	
					// Write out new XML Solr File
					/*OutputFile.writeSolrDocument("filename", newSolrDocument.toString());
					// Write out new XML Reference File
					OutputFile.writeReferenceDocument("filename", newReferenceDocument.toString());
					// Store in Database
					OutputDatabase.logData();
					// Optional Func - Write the document to Solr
					OutputSolr.writeDocumentToSolr();*/
				}
			}
		} 	
	}
}