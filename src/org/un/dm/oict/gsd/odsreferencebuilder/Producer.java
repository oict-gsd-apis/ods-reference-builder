package org.un.dm.oict.gsd.odsreferencebuilder;

import java.io.File;
import java.util.concurrent.BlockingQueue;

import org.un.dm.oict.gsd.odsreferencebuilder.OutputDatabaseMSSQL.InfoType;

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
	int solrFilesProduced = 0;
	int referenceFilesProduced = 0;

	// Constructor accepting blocking queues
	public Producer(BlockingQueue<SolrDocument> processedSolrDocuments, BlockingQueue<ReferenceDocument> processedReferenceDocuments, int solrFilesProduced, int referenceFilesProduced) {
		// Set the local variables with the ones passed into during instantiation
		this.processedSolrDocuments = processedSolrDocuments;
		this.processedReferenceDocuments = processedReferenceDocuments;
		this.solrFilesProduced = solrFilesProduced;
		this.referenceFilesProduced = referenceFilesProduced;
	}

	@Override
	/**
	 * Method called on running the thread
	 */
	public void run() {
		// Start the process
		System.out.println("INFO: Producer Started");
		iterateFiles(AppProp.rootFileDirectory);	
	}
	
	/**
	 * Method which performs the looping of a given directory
	 * to obtain the relevant xml files, from this it processes each
	 * and writes the output to an individual file
	 * @param xmlFilesDirectory
	 */
	private void iterateFiles(String rootFileDirectory) {
		// Set file directory for XML files
		File dir = new File(rootFileDirectory);
		File[] directoryListing = dir.listFiles();
		// Verify not null
		if (directoryListing != null) {
			// Loop each file within the directory
			for (File child : directoryListing) {
				// Recurse through this directory
				if (child.isDirectory()) {
					iterateFiles(child.getAbsolutePath());
				} else {
					// Get current filename
					String currXmlFilename = child.getAbsolutePath();
					// Verify its an XML file, that is all we are interested in
					if (currXmlFilename.endsWith(".xml")) {
						try {
							// Instantiate two new SolrDocuments and one Reference Document
							SolrDocument currentSolrDocument = TextExtractorFile.readXmlDocument(currXmlFilename);
							SolrDocument newSolrDocument = new SolrDocument();
							ReferenceDocument newReferenceDocument = new ReferenceDocument();
							
							// Set both the currentSolrDocument and newSolrDocuments filename to be the same
							currentSolrDocument.setFilename(currXmlFilename);
							newSolrDocument.setFilename(currXmlFilename);
							newReferenceDocument.setFilename(currXmlFilename);
		
							System.out.println("INFO: Beginning to process - " + currXmlFilename);
							//Create the Folder
							 Helper.createOutputFolders( child.getParent().substring(child.getParent().lastIndexOf("/") + 1, child.getParent().length()));
	
							processSolrDocuments(currentSolrDocument, newSolrDocument);
							processReferenceDocuments(newSolrDocument, newReferenceDocument);
						} catch (Exception e) {
							Helper.logMessage(InfoType.Error, e.getMessage());
						}
					}
				}
			}
		}		
	}
	
	/**
	 * 
	 * @param currentSolrDocument
	 * @param newSolrDocument
	 */
	private void processSolrDocuments(SolrDocument currentSolrDocument, SolrDocument newSolrDocument) {
		// Process the Solr Document - Cleanse & apply business logic
		// If required obtain a new body
		newSolrDocument = FileProcessor.processFile(currentSolrDocument, newSolrDocument);
		processedSolrDocuments.add(newSolrDocument);	
		solrFilesProduced++;
		System.out.println("("+ solrFilesProduced +") Solr Document Produced : " + newSolrDocument.getId());
	}
	
	/**
	 * 
	 * @param newSolrDocument
	 * @param newReferenceDocument
	 */
	private void processReferenceDocuments(SolrDocument newSolrDocument, ReferenceDocument newReferenceDocument) {
		// Extract references for all Symbol variations
		// TODO Daniel Test references, there seems to be a lost of noise, code below shows output
		newReferenceDocument = FileProcessor.extractReferences(newSolrDocument, newReferenceDocument);
		processedReferenceDocuments.add(newReferenceDocument);
		referenceFilesProduced++;
		System.out.println("(" + referenceFilesProduced + ")Reference Document Produced : " + newReferenceDocument.getId());
	}
}
