package org.un.dm.oict.gsd.odsreferencebuilder;

import java.io.File;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This class is the entry point to the application, it loops
 * through each .xml file within a directory and process this using other classes
 * in the solution.
 * @version 1.0
 * @codeReviewer
 */
public class Service {
	
	static String xmlFileDirectory = "/Users/kevinbradley/Desktop/testFiles";
	static String solrInstance = "";
	static String solrCollection = "";
	static String solrUsername = "";
	static String solrPassword = "";
	static String databaseLocation = "";
	static String databaseName = "";
	static String databaseUsername = "";
	static String databasePassword = "";
	static String tikaTesseractServer = "http://frankie:9998/tika";
	
	/**
	 * Entry point to application
	 * @param args
	 */
	public static void main(String args[]) {
		performTask(xmlFileDirectory);
	}
	
	/**
	 * Method which performs the looping of a given directory
	 * to obtain the relevant xml files, from this it processes each
	 * and writes the output to an individual file
	 * @param xmlFilesDirectory
	 */
	static void performTask(String xmlFilesDirectory) {
		
		// Set file directory for XML files
		File dir = new File(xmlFilesDirectory);
		File[] directoryListing = dir.listFiles();
		// Verify not null
		if (directoryListing != null) {
			// Loop each file within the directory
			for (File child : directoryListing) {
				// Get current filename
				String currXmlFilename = child.getAbsolutePath();
				// Verify its an XML file, that is all we are interested in
				if (currXmlFilename.endsWith(".xml")) {
					
					// Instantiate two new SolrDocuments and one Reference Document
					SolrDocument currentSolrDocument = TextExtractorFile.readXmlDocument(currXmlFilename);
					SolrDocument newSolrDocument = new SolrDocument();
					ReferenceDocument newReferenceDocument = new ReferenceDocument();

					// Process the Solr Document - Cleanse & apply business logic
					// If required obtain a new body
					newSolrDocument = FileProcessor.processFile(currentSolrDocument, newSolrDocument);
	
					// Extract references for all Symbol variations
					// TODO Test references, there seems to be a lost of noise, code below shows output
					newReferenceDocument = FileProcessor.extractReferences(newSolrDocument, newReferenceDocument);
					/*String refs = "[";
					for(String s : newReferenceDocument.getReferences()) {
						refs += "{" + s + "},";
					}
					refs += "]";
					System.out.println("Id : " + newReferenceDocument.getId() + " refs: " + refs);*/
	
					// Write out newly created Solr Document
					/*OutputFile.writeSolrDocument("filename", newSolrDocument.toString());
					// Write out newly created Reference Document
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