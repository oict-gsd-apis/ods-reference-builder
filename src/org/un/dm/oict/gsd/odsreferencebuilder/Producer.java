package org.un.dm.oict.gsd.odsreferencebuilder;

import java.io.File;
import java.util.concurrent.BlockingQueue;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This is the producer class, which loops through the root file directory
 * ASDAS
 * @version 1.0
 * @codeReviewer
 */
public class Producer implements Runnable {
	
	// Create two BlockingQueue variables to store the documents for processing
	// by the Consumer class
	BlockingQueue<SolrDocument> processedSolrDocuments;
	BlockingQueue<ReferenceDocument> processedReferenceDocuments;

	// Constructor accepting blocking queues
	public Producer(BlockingQueue<SolrDocument> processedSolrDocuments, BlockingQueue<ReferenceDocument> processedReferenceDocuments) {
		// Set the local variables with the ones passed into during instantiation
		this.processedSolrDocuments = processedSolrDocuments;
		this.processedReferenceDocuments = processedReferenceDocuments;
	}

	@Override
	public void run() {
		// Start the process
		performTask(AppProp.rootFileDirectory);	
	}
	
	/**
	 * Method which performs the looping of a given directory
	 * to obtain the relevant xml files, from this it processes each
	 * and writes the output to an individual file
	 * @param xmlFilesDirectory
	 */
	void performTask(String rootFileDirectory) {
		// Set file directory for XML files
		File dir = new File(rootFileDirectory);
		File[] directoryListing = dir.listFiles();
		// Verify not null
		if (directoryListing != null) {
			// Loop each file within the directory
			for (File child : directoryListing) {
				// Recurse through this directory
				if (child.isDirectory()) {
					performTask(child.getAbsolutePath());
				} else {
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
						processedSolrDocuments.add(newSolrDocument);
		
						// Extract references for all Symbol variations
						// TODO Test references, there seems to be a lost of noise, code below shows output
						newReferenceDocument = FileProcessor.extractReferences(newSolrDocument, newReferenceDocument);
						processedReferenceDocuments.add(newReferenceDocument);
						
						String refs = "[";
						for(String s : newReferenceDocument.getReferences()) {
							refs += "{" + s + "},";
						}
						refs += "]";
						System.out.println("Id : " + newReferenceDocument.getId() + " refs: " + refs);
					}
				}
			}
		}		
	}
}
