package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 4-May-2015
 * @description This class is used to store app variables used globally
 * @version 1.0
 * @codeReviewer
 */
public class AppProp {
	
	protected static String rootFileDirectory = "";
	
	protected static String solrInstance = "";
	protected static String solrCollection = "";
	protected static String solrUsername = "";
	protected static String solrPassword = "";
	protected static boolean writeSolrDocumentToSolr = false;
	
	protected static OutputDatabase database;
	protected static String databaseLocation = "";
	protected static String databaseName = "";
	protected static String databasePort = "";
	protected static String databaseUsername = "";
	protected static String databasePassword = "";
	protected static String databaseDocumentTable = "";
	protected static String databaseReferenceTable = "";
	protected static String databaseWarningTable = "";
	protected static String databaseErrorTable = "";
	
	protected static String referenceOutputFolder = "";
	protected static String documentOutputFolder = "";
	
	protected static String tikaTesseractServer = "";
	protected static String tempTesseractImgOutputDir = "";
	protected static String referenceRegex = "";
	protected static int pollDuration = 0;
	protected static char[] invalidChars;
	
	protected static Logger log;
	protected static Properties configFile;
	
	protected static boolean debug = false;
	
	protected static int solrFilesConsumed = 0;
	protected static int referenceFilesConsumed = 0;
	
}