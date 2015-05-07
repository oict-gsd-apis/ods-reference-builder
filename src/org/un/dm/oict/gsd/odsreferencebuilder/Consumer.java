package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.un.dm.oict.gsd.odsreferencebuilder.OutputDatabaseMSSQL.InfoType;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This is a consumer class.
 * @version 1.0
 * @codeReviewer
 */
public class Consumer implements Runnable {
	
	protected enum ConsumerRunType {
		Solr,
		Reference
	}

	// Create two BlockingQueue variables to store the documents for processing
	// by the Consumer class
	BlockingQueue<SolrDocument> processedSolrDocuments;
	BlockingQueue<ReferenceDocument> processedReferenceDocuments;
	int solrFilesConsumed = 0;
	int referenceFilesConsumed = 0;
	ConsumerRunType runType;

	// Constructor accepting blocking queues
	public Consumer(ConsumerRunType runType, BlockingQueue<SolrDocument> processedSolrDocuments, BlockingQueue<ReferenceDocument> processedReferenceDocuments, int solrFilesConsumed, int referenceFilesConsumed) {
		// Set the local variables with the ones passed into during instantiation
		this.processedSolrDocuments = processedSolrDocuments;
		this.processedReferenceDocuments = processedReferenceDocuments;
		this.solrFilesConsumed = solrFilesConsumed;
		this.referenceFilesConsumed = referenceFilesConsumed;
		this.runType = runType;
	}

	@Override
	/**
	 * Method called on running the thread
	 */
	public void run() {
		try {
			// Sleep X seconds, allowing enough time for both
			// blocking queues to have an initial document
			System.out.println("INFO: Consumer (" + runType + ") Waiting");
			Thread.sleep(30 * 1000);
			
		} catch (InterruptedException e) {
			Helper.logMessage(InfoType.Error, e.getMessage());
		}
		
		if (runType.equals(ConsumerRunType.Solr)) { 
			// Process Solr Documents
	    	if (processedSolrDocuments.size() > 0) {
	    		System.out.println("INFO: Consumer (" + runType + ") Started");
	    		processSolrDocuments();
	    	} 
		}
    	
		if (runType.equals(ConsumerRunType.Reference)) {
	    	// Process Reference Documents
	    	if (processedReferenceDocuments.size() > 0) {
	    		System.out.println("INFO: Consumer (" + runType + ") Started");
	    		processReferenceDocuments();
	    	} 
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
			Helper.logMessage(InfoType.Error, e.getMessage());
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
			Helper.logMessage(InfoType.Error, e.getMessage());
		}		
	}
}
