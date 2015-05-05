package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.un.dm.oict.gsd.odsreferencebuilder.Consumer.ConsumerRunType;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This class is the entry point to the application.
 * @version 1.0
 * @codeReviewer
 */
public class Service {
	
	// Two blocking queues for storing the processed files and consuming them
	static BlockingQueue<SolrDocument> processedSolrDocuments;
	static BlockingQueue<ReferenceDocument> processedReferenceDocuments;
	static int solrFilesConsumed = 0;
	static int referenceFilesConsumed = 0;
	static int solrFilesProduced = 0;
	static int referenceFilesProduced = 0;
	
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
		initializeVariables();
		
		try { 	
			// Start the Producer and Consumers on individual threads, therefore 3 threads
			Producer producer = new Producer(processedSolrDocuments, processedReferenceDocuments, solrFilesProduced, referenceFilesProduced);
	        new Thread(producer).start();
	        Consumer solrConsumer = new Consumer(ConsumerRunType.Solr, processedSolrDocuments, processedReferenceDocuments, solrFilesConsumed, referenceFilesConsumed);
	        new Thread(solrConsumer).start();
	        Consumer referenceConsumer = new Consumer(ConsumerRunType.Reference, processedSolrDocuments, processedReferenceDocuments, solrFilesConsumed, referenceFilesConsumed);
	        new Thread(referenceConsumer).start();
		} catch (Exception e) {
			// TODO Kevin log/error generic method
			System.out.println("ERROR: " + e.getMessage());
		}
		//AppProp.tempTesseractImgOutputDir = "dir";
		//TextExtractorOCR.performCompleteOCR("pdfLoc" , "ar");
	}
	
	/**
	 * This method is used to initialize the variables and logger
	 */
	private static void initializeVariables() {
		// Initialize the config file, log file and set the variables
		Helper.initialiseConfigFile();
		
		AppProp.log = Logger.getLogger(Service.class);

		AppProp.invalidChars = new char[] {'\uFFFD','\uF02A'};
		AppProp.debug = Boolean.parseBoolean(Helper.getProperty("debug"));
		AppProp.tempTesseractImgOutputDir = Helper.getProperty("tempTesseractImgOutputDir");
		AppProp.rootFileDirectory = Helper.getProperty("rootFileDirectory");
		AppProp.referenceOutputFolder = Helper.getProperty("referenceOutputFolder");
		AppProp.documentOutputFolder = Helper.getProperty("documentOutputFolder");
		AppProp.solrInstance = Helper.getProperty("solrInstance");
		AppProp.solrCollection = Helper.getProperty("solrCollection");
		AppProp.solrUsername = Helper.getProperty("solrUsername");
		AppProp.solrPassword = Helper.getProperty("solrPassword");
		AppProp.databaseLocation = Helper.getProperty("databaseLocation");
		AppProp.databaseName = Helper.getProperty("databaseName");
		AppProp.databaseUsername = Helper.getProperty("databaseUsername");
		AppProp.databasePassword = Helper.getProperty("databasePassword");
		AppProp.databasePort = Helper.getProperty("databasePort");
		AppProp.databaseDocumentTable = Helper.getProperty("databaseDocumentTable");
		AppProp.databaseReferenceTable = Helper.getProperty("databaseReferenceTable");
		AppProp.databaseWarningTable = Helper.getProperty("databaseWarningTable");
		AppProp.databaseErrorTable = Helper.getProperty("databaseErrorTable");
		AppProp.tikaTesseractServer = Helper.getProperty("tikaTesseractServer");
		AppProp.referenceRegex = Helper.getProperty("referenceRegex");
		AppProp.pollDuration = Integer.parseInt(Helper.getProperty("pollDuration"));
		AppProp.writeSolrDocumentToSolr = Boolean.parseBoolean(Helper.getProperty("writeSolrDocumentToSolr"));
		AppProp.database = new OutputDatabaseMSSQL();
		
		//Establish connection with SQL DB
		AppProp.database.establishConnection();
	}
}