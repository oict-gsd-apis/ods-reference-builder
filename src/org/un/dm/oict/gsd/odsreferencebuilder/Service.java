package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This class is the entry point to the application.
 * @version 1.0
 * @codeReviewer
 */
public class Service {
	
	static char[] invalidChars = {'\uFFFD','\uF02A'};
	
	// Two blocking queues for storing the processed files and consuming them
	static BlockingQueue<SolrDocument> processedSolrDocuments;
	static BlockingQueue<ReferenceDocument> processedReferenceDocuments;
	
	/**
	 * Entry point to application, sets variables, initializes the logs
	 * and starts Producer and Consumer classes on threads
	 * @param args
	 */
	public static void main(String args[]) {
		
		// Initialize the two blocking queues
		processedSolrDocuments = new ArrayBlockingQueue<>(1000000);
		processedReferenceDocuments = new ArrayBlockingQueue<>(1000000);
		
		// Initialize the config file, log file and set the variables
		Helper.initialiseConfigFile();
		AppProp.log = Logger.getLogger(Service.class);
		AppProp.rootFileDirectory = Helper.getProperty("rootFileDirectory");
		AppProp.solrInstance = Helper.getProperty("solrInstance");
		AppProp.solrCollection = Helper.getProperty("solrCollection");
		AppProp.solrUsername = Helper.getProperty("solrUsername");
		AppProp.solrPassword = Helper.getProperty("solrPassword");
		AppProp.databaseLocation = Helper.getProperty("databaseLocation");
		AppProp.databaseName = Helper.getProperty("databaseName");
		AppProp.databaseUsername = Helper.getProperty("databaseUsername");
		AppProp.databasePassword = Helper.getProperty("databasePassword");
		AppProp.tikaTesseractServer = Helper.getProperty("tikaTesseractServer");
		AppProp.referenceRegex = Helper.getProperty("referenceRegex");
		
		try { 			
			// Start the Producer and Consumer on individual threads
			Producer producer = new Producer(processedSolrDocuments, processedReferenceDocuments);
	        new Thread(producer).start();
	        Consumer consumer = new Consumer(processedSolrDocuments, processedReferenceDocuments);
	        new Thread(consumer).start();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}
}