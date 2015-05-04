package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.concurrent.BlockingQueue;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This is a consumer class.
 * @version 1.0
 * @codeReviewer
 */
public class Consumer implements Runnable {

	BlockingQueue<SolrDocument> processedSolrDocuments;
	BlockingQueue<ReferenceDocument> processedReferenceDocuments;

	public Consumer(BlockingQueue<SolrDocument> processedSolrDocuments, BlockingQueue<ReferenceDocument> processedReferenceDocuments) {
		
		this.processedSolrDocuments = processedSolrDocuments;
		this.processedReferenceDocuments = processedReferenceDocuments;
	}

	@Override
	public void run() {
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
