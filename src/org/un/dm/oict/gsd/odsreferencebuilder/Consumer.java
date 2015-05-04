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

	// Constructor accepting blocking queues
	public Consumer(BlockingQueue<SolrDocument> processedSolrDocuments, BlockingQueue<ReferenceDocument> processedReferenceDocuments) {
		// Set the local variables with the ones passed into during instantiation
		this.processedSolrDocuments = processedSolrDocuments;
		this.processedReferenceDocuments = processedReferenceDocuments;
	}

	@Override
	public void run() {
		try {
			// Sleep 30 seconds, allowing enough time for both
			// blocking queues to have an initial document
			Thread.sleep(300 * 1000);
		} catch (InterruptedException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		 
		// Process Solr Documents
    	if (processedSolrDocuments.size() > 0) {
	    	SolrDocument currentSolrDocument = null;
	    	try {
				while ((currentSolrDocument = processedSolrDocuments.poll(AppProp.pollDuration, TimeUnit.MINUTES)) != null) {
					// Write out newly created Solr Document
					OutputFile.writeSolrDocument(currentSolrDocument.buildNewFilename(), currentSolrDocument.toString());
					// Store in Database
					//OutputDatabase.logData();
					// Optional Function - Write the document to Solr
					if (AppProp.writeSolrDocumentToSolr) {
						OutputSolr.writeDocumentToSolr();
					}
				}
			} catch (Exception e) {
				System.out.println("ERROR: " + e.getMessage());
			}
 		
    	} 
    	
    	// Process Reference Documents
    	if (processedReferenceDocuments.size() > 0) {
	    	ReferenceDocument currentReferenceDocument = null;
	    	try {
				while ((currentReferenceDocument = processedReferenceDocuments.poll(AppProp.pollDuration, TimeUnit.MINUTES)) != null) {
					// Write out newly created Reference Document
					OutputFile.writeReferenceDocument("filename", currentReferenceDocument.toString());
				}
			} catch (Exception e) {
				System.out.println("ERROR: " + e.getMessage());
			}
 		
    	} 
	}
}
