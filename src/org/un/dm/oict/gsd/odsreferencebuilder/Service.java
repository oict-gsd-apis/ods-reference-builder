package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.un.dm.oict.gsd.odsreferencebuilder.Consumer.ConsumerRunType;
import org.un.dm.oict.gsd.odsreferencebuilder.OutputDatabaseMSSQL.InfoType;

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
	static int solrFilesProduced = 0;
	static int referenceFilesProduced = 0;
	/**
	 * Entry point to application, sets variables, initializes the logs
	 * and starts Producer and Consumer classes on threads
	 * @param args
	 */
	public static void main(String args[]) {
		// Initialize the two blocking queues
		processedSolrDocuments = new ArrayBlockingQueue<>(1500000);
		processedReferenceDocuments = new ArrayBlockingQueue<>(1500000);
	
		// Initialize the config file, log file and set the variables
		System.out.println("INFO: Variables initialized");
		initializeVariables();
		
		try { 	
			if (AppProp.standardService) {
				// Start the Producer and Consumers on individual threads, therefore 3 threads
				Producer producer = new Producer(processedSolrDocuments, processedReferenceDocuments, solrFilesProduced, referenceFilesProduced);
		        new Thread(producer).start();
		        Consumer solrConsumer = new Consumer(ConsumerRunType.Solr, processedSolrDocuments, processedReferenceDocuments);
		        new Thread(solrConsumer).start();
		        Consumer referenceConsumer = new Consumer(ConsumerRunType.Reference, processedSolrDocuments, processedReferenceDocuments);
		        new Thread(referenceConsumer).start();
			} else {
				BodyProducer producer = new BodyProducer(processedSolrDocuments);
		        new Thread(producer).start();
		        Consumer solrConsumer = new Consumer(ConsumerRunType.Body, processedSolrDocuments, processedReferenceDocuments);
		        new Thread(solrConsumer).start();
			}
		} catch (Exception e) {
			Helper.logMessage(InfoType.Error, e.getMessage());
		}
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
		AppProp.problematicBodyFilenames = Helper.getProperty("problematicBodyFilenames");
		AppProp.bodyOutputDirectory = Helper.getProperty("bodyOutputDirectory");
		AppProp.standardService = Boolean.parseBoolean(Helper.getProperty("standardService"));
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