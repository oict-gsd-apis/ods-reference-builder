package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.concurrent.BlockingQueue;

import org.un.dm.oict.gsd.odsreferencebuilder.OutputDatabaseMSSQL.InfoType;

import au.com.bytecode.opencsv.CSVReader;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 19-May-2015
 * @description This class is used to call the tesseract procedure to acquire a new body for
 * a known problematic file
 * @version 1.0
 * @codeReviewer
 */
public class BodyProducer implements Runnable {
	// Create two BlockingQueue variables to store the documents for processing
	// by the Consumer class
	BlockingQueue<SolrDocument> processedBodySolrDocuments;
	int solrFilesProduced = 0;
	int referenceFilesProduced = 0;

	// Constructor accepting blocking queues
	public BodyProducer(BlockingQueue<SolrDocument> processedBodySolrDocuments) {
		// Set the local variables with the ones passed into during instantiation
		this.processedBodySolrDocuments = processedBodySolrDocuments;
	}

	@Override
	/**
	 * Method called on running the thread
	 */
	public void run() {
		try {
			// Start the process
			System.out.println("INFO: Body Producer Started");
			iterateFiles(AppProp.rootFileDirectory);	
			Helper.logMessage(InfoType.Info, "Body Producer finished");
		} catch (Exception e) {
			Helper.logMessage(InfoType.Error, "Overall Error in Producer - " + e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param csvFilename
	 */
	private void iterateFiles(String csvFilename) {
		String currXmlFilename = "";

		CSVReader filenames = Helper.readCSV(csvFilename);
		String [] nextLine;
	    try {
	    	int count = 0;
			while ((nextLine = filenames.readNext()) != null) {
				if (count > 0) { // Header
					try {
						currXmlFilename = nextLine[0];
						// Instantiate a mew SolrDocument and map the data onto it
						SolrDocument currentSolrDocument = null; 
						currentSolrDocument = TextExtractorFile.readXmlDocument(currXmlFilename);

						System.out.println("INFO: Beginning to process - " + currXmlFilename);

						processSolrDocuments(currentSolrDocument);
					} catch (Exception e) {
						Helper.logMessage(InfoType.Error, e.getMessage());
					}
				}
				count++;
			}
	    } catch (Exception e) {
	    	System.out.println("ERROR: " + e.getMessage());
	    }
	}
	
	/**
	 * 
	 * @param currentSolrDocument
	 */
	private void processSolrDocuments(SolrDocument currentSolrDocument) {
		// Obtain a new body using tesseract
		SolrDocument newSolrDocument = FileProcessor.processBodyTextErroneous(currentSolrDocument);
		processedBodySolrDocuments.add(newSolrDocument);
	}
}
