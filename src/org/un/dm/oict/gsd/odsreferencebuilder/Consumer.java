package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.Date;
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
		Reference,
		Body
	}

	// Create two BlockingQueue variables to store the documents for processing
	// by the Consumer class
	BlockingQueue<SolrDocument> processedSolrDocuments;
	BlockingQueue<ReferenceDocument> processedReferenceDocuments;
	ConsumerRunType runType;

	// Constructor accepting blocking queues
	public Consumer(ConsumerRunType runType, BlockingQueue<SolrDocument> processedSolrDocuments, BlockingQueue<ReferenceDocument> processedReferenceDocuments) {
		// Set the local variables with the ones passed into during instantiation
		this.processedSolrDocuments = processedSolrDocuments;
		this.processedReferenceDocuments = processedReferenceDocuments;
		this.runType = runType;
	}

	@Override
	/**
	 * Method called on running the thread
	 */
	public void run() {
		try {
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
			
			if (runType.equals(ConsumerRunType.Body)) {
				System.out.println("INFO: Consumer (" + runType + ") Waiting");
				Thread.sleep(120 * 1000);
				// Process Solr Documents
		    	if (processedSolrDocuments.size() > 0) {
		    		System.out.println("INFO: Consumer (" + runType + ") Started");
		    		processSolrBodyDocuments();
		    	}
			}
		} catch (Exception e) {
			Helper.logMessage(InfoType.Error, "Overall Error in Consumer - " + e.getMessage());
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
				AppProp.solrFilesConsumed++;
				System.out.println(new Date().toString() + "(" + AppProp.solrFilesConsumed + ") Solr Document Consumed : " + currentSolrDocument.getId());
				Helper.logMessage(InfoType.Info, "(" + AppProp.solrFilesConsumed + ") (BlockingQueue: " + processedSolrDocuments.size() + ") Solr Document Consumed : " + currentSolrDocument.getId());
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
    	Helper.logMessage(InfoType.Info, "Consumer finished for Solr Documents");
	}
	
	private void processSolrBodyDocuments() {
		SolrDocument currentSolrDocument = null;
    	try {
			while ((currentSolrDocument = processedSolrDocuments.poll(AppProp.pollDuration, TimeUnit.MINUTES)) != null) {
				// Write out newly created Solr Document
				OutputFile of = new OutputFile();
				of.writeSolrDocument(currentSolrDocument.buildNewBodyFilename(), currentSolrDocument.toString());
				AppProp.solrFilesConsumed++;
				System.out.println(new Date().toString() + "(" + AppProp.solrFilesConsumed + ") Solr Body Document Consumed : " + currentSolrDocument.getId());
				Helper.logMessage(InfoType.Info, "(" + AppProp.solrFilesConsumed + ") (BlockingQueue: " + processedSolrDocuments.size() + ") Solr Body Document Consumed : " + currentSolrDocument.getId());
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
    	Helper.logMessage(InfoType.Info, "Consumer finished for Solr Body Documents");
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
				AppProp.referenceFilesConsumed++;
				System.out.println(new Date().toString() + "(" + AppProp.referenceFilesConsumed + ") Reference Document Consumed : " + currentReferenceDocument.getId());
				Helper.logMessage(InfoType.Info, "(" + AppProp.referenceFilesConsumed + ") (BlockingQueue: " + processedReferenceDocuments.size() + ") Reference Document Consumed : " + currentReferenceDocument.getId());
				// Store in Database
				AppProp.database.insertReferenceDocument(currentReferenceDocument);
			}
		} catch (Exception e) {
			Helper.logMessage(InfoType.Error, e.getMessage());
		}
    	Helper.logMessage(InfoType.Info, "Consumer finished for Reference Documents");
	}
}
