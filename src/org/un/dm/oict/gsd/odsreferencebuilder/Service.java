package org.un.dm.oict.gsd.odsreferencebuilder;

import java.io.File;

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