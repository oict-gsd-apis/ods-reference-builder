package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This is a consumer class.
 * @version 1.0
 * @codeReviewer
 */
public class Consumer implements Runnable {

	// Create two BlockingQueue variables to store the documents for processing
	// by the Consumer class
	BlockingQueue<SolrDocument> processedSolrDocuments;
	BlockingQueue<ReferenceDocument> processedReferenceDocuments;
	int solrFilesConsumed = 0;
	int referenceFilesConsumed = 0;

	// Constructor accepting blocking queues
	public Consumer(BlockingQueue<SolrDocument> processedSolrDocuments, BlockingQueue<ReferenceDocument> processedReferenceDocuments, int solrFilesConsumed, int referenceFilesConsumed) {
		// Set the local variables with the ones passed into during instantiation
		this.processedSolrDocuments = processedSolrDocuments;
		this.processedReferenceDocuments = processedReferenceDocuments;
		this.solrFilesConsumed = solrFilesConsumed;
		this.referenceFilesConsumed = referenceFilesConsumed;
	}

	@Override
	/**
	 * Method called on running the thread
	 */
	public void run() {
		try {
			// Sleep 30 seconds, allowing enough time for both
			// blocking queues to have an initial document
			Thread.sleep(3 * 1000);
		} catch (InterruptedException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		 
		// Process Solr Documents
    	if (processedSolrDocuments.size() > 0) {
    		processSolrDocuments();
    	} 
    	
    	// Process Reference Documents
    	if (processedReferenceDocuments.size() > 0) {
    		processReferenceDocuments();
    	} 
	}
	
	/**
	 * 
	 */
	private void processSolrDocuments() {
		SolrDocument currentSolrDocument = null;
    	try {
			while ((currentSolrDocument = processedSolrDocuments.poll(AppProp.pollDuration, TimeUnit.MINUTES)) != null) {
				// Write out newly created Solr Document
				OutputFile of = new OutputFile();
				of.writeSolrDocument(currentSolrDocument.buildNewFilename(), currentSolrDocument.toString());
				solrFilesConsumed++;
				System.out.println("(" + solrFilesConsumed + ") Solr Document Consumed : " + currentSolrDocument.getId());
				
				// Store in Database
				AppProp.database.insertSolrDocument(currentSolrDocument);
				// Optional Function - Write the document to Solr
				if (AppProp.writeSolrDocumentToSolr) {
					OutputSolr.writeDocumentToSolr();
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}	
	}
	
	/**
	 * 
	 */
	private void processReferenceDocuments() {
    	ReferenceDocument currentReferenceDocument = null;
    	try {
			while ((currentReferenceDocument = processedReferenceDocuments.poll(AppProp.pollDuration, TimeUnit.MINUTES)) != null) {
				// Write out newly created Reference Document
				OutputFile of = new OutputFile();
				of.writeReferenceDocument(currentReferenceDocument.buildNewFilename(), currentReferenceDocument.toString());
				referenceFilesConsumed++;
				System.out.println("(" + referenceFilesConsumed + ") Reference Document Consumed : " + currentReferenceDocument.getId());
			}
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}		
	}
}
