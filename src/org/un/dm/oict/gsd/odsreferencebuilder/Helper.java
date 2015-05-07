package org.un.dm.oict.gsd.odsreferencebuilder;

//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.un.dm.oict.gsd.odsreferencebuilder.OutputDatabaseMSSQL.InfoType;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This is a helper class, used to generic methods used globally within the 
 * application.
 * @version 1.0
 * @codeReviewer
 */
public class Helper {
	
	/**
	 * This method obtains a particular property from the config.properties file
	 * @param name
	 * @return
	 */
	static String getProperty(String name){
		return AppProp.configFile.getProperty(name);
	}
	
	/**
	 * This method initializes the config file storing this value in the AppProp
	 */
	static void initialiseConfigFile(){
		try {
			AppProp.configFile = Helper.getProperties(Service.class.getClassLoader());
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}
	
	/**
	 * This method is used to load the actual config file based on its name
	 * @param loader
	 * @return
	 * @throws Exception
	 */
    static Properties getProperties(ClassLoader loader) throws Exception {
    	Properties configFile = new Properties();
    	try {
    		configFile.load(loader.getResourceAsStream("config.properties"));
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());		
		}
    	return configFile;
    }
    
    /**
     * This method is used to obtain a Unix timestamp for a given date format
     * @param sdate
     * @return
     */
	static Timestamp getTimestamp(String sdate){
		if (sdate == null || sdate.equals(""))
			sdate = "1900-01-01T00:00:00Z";
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(sdate);
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());		
		}
		long time = date.getTime();
		return new Timestamp(time);
	}
	
	/**
	 * This method is used to attempt to identify invalid bodies
	 * @param newSolrDocument
	 * @param invalidChars
	 * @return
	 */
	static boolean checkBodyContainsInvalidChars(SolrDocument newSolrDocument, char[] invalidChars) {
		String body = newSolrDocument.getBody();
		if (body.isEmpty() || body.length() < 1) {
			return true;
		}
		return checkBodyContainsInvalidChars(body, invalidChars);
	}
	
	/**
	 * 
	 * @param body
	 * @return
	 */
	//TODO check how to pass more than one UNICODE value and how to implement them on regex
	static boolean checkBodyContainsInvalidChars(String body, char[] invalidChars) {
		boolean found = false;
	    Pattern pattern = Pattern.compile("\uFFFD");
	    Matcher matcher = pattern.matcher(body);
		while (matcher.find()) {
			System.out.println("Invalid character found");
			found = true;
			break;
		}		
		return found;
	}	
	
	/**
	 * This method is used to remove some of the whitespace in the body
	 * @param val
	 * @return
	 */
	protected static String minimizeWhitespace(String val) {
		return val.replaceAll("[\n\r]","") + "\n";
	}
	
	/**
	 * 
	 * @param type
	 * @param message
	 * @return
	 */
	protected static boolean logMessage(InfoType type, String message) {
		return logMessage(type, null, message);
	}
	
	/**
	 * 
	 * @param type
	 * @param newSolrDocument
	 * @param message
	 * @return
	 */
	protected static boolean logMessage(InfoType type, SolrDocument newSolrDocument, String message) {
		String msg = "";
		if (type == InfoType.Info) {
			msg += "INFO:";
			msg += " Id (" + ((newSolrDocument != null) ? newSolrDocument.getId() : "NOT SET") + ") " + message;
		} else if (type == InfoType.Warning) {
			msg += "WARNING:";
			msg += " Id (" + ((newSolrDocument != null) ? newSolrDocument.getId() : "NOT SET") + ") " + message;
			AppProp.database.insertWarning(newSolrDocument != null ? newSolrDocument.getId() : "NOT SET", msg);
		} else if (type == InfoType.Error) {
			msg += "ERROR:";
			msg += " Id (" + ((newSolrDocument != null) ? newSolrDocument.getId() : "NOT SET") + ") " + message;
			AppProp.database.insertError(newSolrDocument != null ? newSolrDocument.getId() : "NOT SET", msg);
		}
		System.out.println(msg);
		return true;
	}

	/**
	 * 
	 * @param fullText
	 * @param pattern
	 * @param keywords
	 * @return
	 */
    static String getSingleValue(String fullText, String pattern, String[] keywords) {
    	Pattern p = Pattern.compile(pattern);
    	Matcher m = p.matcher(fullText.toLowerCase());
    	String vals = "";
    	while (m.find()){
    		vals = m.group();
    	}
    	for(String s : keywords) {
    		vals = vals.replace(s, "");
    	}
    	return vals.trim();
    }

    /**
     * 
     * @param fullText
     * @param startTextPattern
     * @param endTextPattern
     * @param splitter
     * @param keywords
     * @return
     */
    static String[] getMultiValue(String fullText, String startTextPattern, String endTextPattern, String splitter, String[] keywords) {
    	String lowerFullText = fullText.toLowerCase();
    	int startIndex = getIndexOf(lowerFullText, startTextPattern, -1); 
    	int endIndex = getIndexOf(lowerFullText, endTextPattern, startIndex);
    	int startTextLength = startTextPattern.length();
    	String vals;
    	if (startIndex < 0 || endIndex < 0)
    		return new String[] { "" };
    	else
    		vals = lowerFullText.substring(startIndex+startTextLength, endIndex);
    	for(String s : keywords) {
    		vals = vals.replace(s, "");
    	}
    	String cleaned = vals.trim();
    	return cleaned.split(splitter);
    }
	    
    /**
     * 
     * @param unformattedDate
     * @param format
     * @return
     */
    static String getFormattedDate(String unformattedDate, String format) {
    	String formattedDate = "";
    	DateFormat df = null;
    	DateFormat sf = null;
    	Date udate = null;
    	
    	if (unformattedDate == null || unformattedDate.equals("")) {
    		unformattedDate = "01 January 1900";
    	}

	    df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	    //sf = new SimpleDateFormat("dd/MM/yyyy");
	    sf = new SimpleDateFormat(format);
	    
		try {
			udate = sf.parse(unformattedDate);
			formattedDate = df.format(udate);
		} catch (Exception e) {
			Helper.logMessage(InfoType.Error, "Formatting Date Time(" + unformattedDate + ") - " + e.getMessage());
			return "1900-01-01T00:00:01Z";
		}
		
	    return formattedDate;
	}
	    
    /**
     * 
     * @param init
     * @return
     */
    static String toCamelCase(final String init) {
        if (init==null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length()==init.length()))
                ret.append(" ");
        }
        return ret.toString();
    }
	    
    /**
     * 
     * @param text
     * @param regex
     * @param startIndex
     * @return
     */
    static int getIndexOf(String text, String regex, int startIndex) {
    	int index = -1;
    	Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        // Check all occurrences
        if (startIndex < 1) {
	        while (matcher.find()) {
	        	index = matcher.start();
	        	break;
	        }
        }
        else {
	        while (matcher.find(startIndex)) {
	        	index = matcher.start();
	        	break;
	        } 
        }
        return index;
    }
    
    /**
     * 
     * @param str
     * @param findChar
     * @return
     */
    static int getCountChar(String str, String findChar) {
    	int lastIndex = 0;
    	int count  = 0;
    	while(lastIndex != -1) {
    		lastIndex = str.indexOf(findChar, lastIndex);
    		if (lastIndex != -1) {
    			count++;
    			lastIndex += findChar.length();
    		}
    	}
    	return count;
    }
    
    /**
     * 
     * @param folderName
     */
    static void createOutputFolders(String folderName)
    {
    	//Verify if the directory exists 
		 File theDocDir = new File(AppProp.documentOutputFolder + "/" + folderName);
		 File theRefDir = new File(AppProp.referenceOutputFolder + "/" + folderName);
		 createDirectory(theDocDir);
		 createDirectory(theRefDir);
    }
    
    /**
     * 
     * @param folderPath
     */
    static void createDirectory(File folderPath){
    	if (!folderPath.exists()) {
		    try{
		    	folderPath.mkdir();
		    	System.out.println("Directory created succesfully");
		     } catch(SecurityException se){
		        //handle it
		    	 System.out.println("An error occurred while creating the directory " + se.getMessage());
		     }		    	 
		  }
    }
//   
//	    
//	/**
//	 *   
//	 * @param fullText
//	 * @return
//	 */
//    static String getLanguageCode(String fullText) {
//    	LanguageIdentifier li = new LanguageIdentifier(fullText);
//    	return li.getLanguage();
//    }
//	    
//    /**
//     * 
//     * @param filename
//     * @return
//     */
//    static Map<String, Object> extractDataViaShare(String filename) { 
//    	Map<String, Object> data = null;
//		try {		
//		    File file = new File(filename);
//		    data = extractFileData(file);
//		} catch (Exception e) {
//			Helper.logMessage(InfoType.Error, e.getMessage());
//		}
//		return data;
//    }
//	 
//    /**
//     * 
//     * @param url
//     * @return
//     */
//    static Map<String, Object> extractDataViaHTTP(String url) { 
//    	Map<String, Object> data = null;
//		try {	
//			String cleanedUrl = url.trim().replace(" ", "%20");
//			URL webAddr = new URL(cleanedUrl);
//		    File file = new File(AppProp.tempTikaFTPFilename);
//		    FileUtils.copyURLToFile(webAddr, file);
//		    data = extractFileData(file);
//		} catch (Exception e) {
//			Helper.logMessage(InfoType.Error, e.getMessage());
//		}
//		return data;
//    }
//	
//    /**
//     * 
//     * @param filename
//     * @return
//     */
//    static Map<String, Object> extractDataViaFTP(String filename) { 
//    	Map<String, Object> data = null;
//		try {
//			String localFilename = AppProp.outputFileDir + AppProp.tempTikaFTPFilename;
//
//			AppProp.ftp.setFileType(FTP.BINARY_FILE_TYPE);
//			AppProp.ftp.enterLocalPassiveMode();
//			AppProp.ftp.setAutodetectUTF8(true);
//
//		    //Create an InputStream to the File Data and use FileOutputStream to write it
//		    InputStream inputStream = AppProp.ftp.retrieveFileStream(filename);
//		    FileOutputStream fileOutputStream = new FileOutputStream(localFilename);
//		    IOUtils.copy(inputStream, fileOutputStream);
//		    fileOutputStream.flush();
//		    boolean commandOK = AppProp.ftp.completePendingCommand();
//		    
//		    File file = null;
//			if (commandOK) {
//				file = new File(localFilename);
//				data = extractFileData(file);
//			}
//		} catch (Exception e) {
//			Helper.logMessage(InfoType.Error, e.getMessage());
//		}
//		return data;
//    }
//	    
//    /**
//     * 
//     * @param file
//     * @return
//     */
//    private static Map<String, Object> extractFileData(File file) { 
//		try {
//			ExtractorTika parser = new ExtractorTika();
//			Map<String, Object> allPDFData = new HashMap<String, Object>();
//			
//			allPDFData = parser.extraction(file);
//				
//			Helper.displayInfo("INFO: Parsed data successfully for file" + file.getAbsolutePath());
//					
//			return allPDFData;	
//			
//		} catch (Exception e) {
//			Helper.logMessage(InfoType.Error, e.getMessage());
//		}
//		return null;
//    }
//	  
//    /**
//     * 
//     * @param jar
//     * @param properties
//     * @param params
//     * @return
//     */
//    static String callExternalJar(String jar, String properties, String params) {
//		 try {
//			String query = "java " + properties + " -jar " + jar + " " + params;
//			System.out.println("Running command: " + query);
//		    Process ps = Runtime.getRuntime().exec(query);
//		    ps.waitFor();
//		    InputStream is = ps.getInputStream();
//		    byte b[] = new byte[is.available()];
//		    is.read(b,0,b.length);
//		    return new String(b);
//		} catch (Exception e) {
//			Helper.logMessage(InfoType.Error, e.getMessage());
//			return null;
//		}
//	};
//
//	/**
//	 * 
//	 * @param outputFilename
//	 * @return
//	 */
//    static boolean postDataToSolr(String outputFilename) {
//		 try {
//		        System.out.println("Posting Output File to Solr: " + AppProp.solrCollection);
//		    // Call the post.jar in the solr directory to push the output xml file to SOLR
//		    String output = callExternalJar(AppProp.solrJarFile, "-Dcommit=true -Durl=" + AppProp.solrCollection, AppProp.outputFileDir + AppProp.currentDate + '/' + outputFilename);
//		    System.out.println("Output from post.jar: " + output);
//		    System.out.println("Called solr post.jar file");
//		    return true;
//		} catch (Exception e) {
//			Helper.logMessage(InfoType.Error, e.getMessage());
//			return true;
//		}
//	}
//		
//    /**
//     * 
//     */
//    static void postAllDataToSolr(){
//    	try{
//	        System.out.println("Posting Output File to Solr: " + AppProp.solrCollection);
//	         //Call the post.jar in the solr directory to push the output xml file to SOLR
//	        String output = callExternalJar(AppProp.solrJarFile, "-Dauto -Drecursive -Dcommit=true -Durl=" + AppProp.solrCollection, AppProp.outputFileDir + AppProp.currentDate );
//
//	        Helper.displayInfo("Output from post.jar: " + output);
//	        Helper.displayInfo("Called solr post.jar file");    		
//    	}catch (Exception e){
//    		System.out.println(e.getMessage());
//    		//return true;
//    	}
//    }
//	    
//    /**
//     * 
//     * @return
//     */
//	static boolean closeFTPConnection() {
//        try {
//        	if (AppProp.ftp.isConnected()) {
//	    		Helper.displayInfo("INFO: Logging out of FTP Server");
//				AppProp.ftp.logout();
//		        Helper.displayInfo("INFO: Disconnecting from FTP Server");
//		        AppProp.ftp.disconnect();
//        	}
//		} catch (IOException e) {
//			Helper.logMessage(InfoType.Error, e.getMessage());
//			return false;
//		}
//        return true;
//	}
//		
//	/**
//	 * 
//	 * @return
//	 */
//	static boolean getFTPConnection() {
//		try {
//        	// Connect to the FTP Server
//        	Helper.displayInfo("INFO: Connecting to FTP Server [" + AppProp.ftpServerAddress + "]");
//        	AppProp.ftp.connect(AppProp.ftpServerAddress);
//			Helper.displayInfo("INFO: Connected to FTP Server [" + AppProp.ftpServerAddress + "]");
//			Helper.displayInfo("INFO: Logging into to FTP Server with [Username:"  + AppProp.ftpUserId + "] and [Password:" + AppProp.ftpPassword + "]");
//
//			if(AppProp.ftp.login(AppProp.ftpUserId, AppProp.ftpPassword))
//	        {
//				// Obtain the reply code and ensure its connected successfully
//	        	int reply = AppProp.ftp.getReplyCode();
//	            if (FTPReply.isPositiveCompletion(reply))
//	            {
//	            	Helper.displayInfo("INFO: Entering FTP passive mode");
//	            	AppProp.ftp.enterLocalPassiveMode();
//	            	Helper.displayInfo("INFO: Remote system is" + AppProp.ftp.getSystemType());
//	            	return true;
//	            }
//	            else {
//	            	return false;
//	            }
//	        } else
//	        	return false;
//		} catch (Exception e) {
//			Helper.logMessage(InfoType.Error, e.getMessage());
//			return false;
//		}
//	}
//		
//	/**
//	 * 
//	 * @return
//	 */
//	static Timestamp getCurrentTimestamp() {
//		long time = System.currentTimeMillis();
//		java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
//		return timestamp;
//	}
//		
//	/**
//	 * 
//	 * @param parentDir
//	 * @param currentDir
//	 * @param level
//	 * @param businessLogicFunction
//	 * @throws IOException
//	 */
//	static void iterateFTPFiles(String parentDir, String currentDir, int level, Callable<Boolean> businessLogicFunction) throws IOException {    
//		String dirToList = parentDir;
//        if (!currentDir.equals("")) {
//            dirToList += "/" + currentDir;
//        } 
//        Helper.displayInfo("INFO: Current Parent Directory: " + dirToList);
//        Helper.displayInfo("INFO: Getting Files for Directory");
//        // Get the files for the current directory
//        FTPFile[] subFiles = AppProp.ftp.listFiles(dirToList);
//        // Ensure there are files
//        if (subFiles != null && subFiles.length > 0) {
//        	Helper.displayInfo("INFO: Looping Collected Files");
//        	// Loop each file
//            for (FTPFile aFile : subFiles) {
//                AppProp.currentlyProcessingFilename = dirToList + "/" + aFile.getName();   
//                Helper.displayInfo("INFO: Current File" + AppProp.currentlyProcessingFilename);
//                // If Directory recurse - ensuring business logic of particular folder types
//                if (aFile.isDirectory()) {
//                	iterateFTPFiles(dirToList, aFile.getName(), level + 1, businessLogicFunction);
//                } else {
//                	try {
//						businessLogicFunction.call();
//					} catch (Exception e) {
//						
//					}
//                }
//            }
//        }
//	}
//		
//	/**
//	 * 
//	 * @param filename
//	 * @return
//	 */
//    static File extractCSVViaFTP(String filename) { 
//    	File tmpFile = null;
//		try {
//			String localFilename = AppProp.outputFileDir + AppProp.tempCSVFTPFilename;
//
//			AppProp.ftp.setFileType(FTP.BINARY_FILE_TYPE);
//			AppProp.ftp.enterLocalPassiveMode();
//			AppProp.ftp.setAutodetectUTF8(true);
//
//		    //Create an InputStream to the File Data and use FileOutputStream to write it
//		    InputStream inputStream = AppProp.ftp.retrieveFileStream(filename);
//		    FileOutputStream fileOutputStream = new FileOutputStream(localFilename);
//		    IOUtils.copy(inputStream, fileOutputStream);
//		    fileOutputStream.flush();
//		    boolean commandOK = AppProp.ftp.completePendingCommand();
//		     
//			if (commandOK) {
//				tmpFile = new File(localFilename); 
//			}
//		} catch (Exception e) {
//			Helper.recordError("Error extracting csv file via FTP - " + e.getMessage());
//		}
//		return tmpFile;
//    }	
//		
//    /**
//     * 
//     * @param metaData
//     * @param val
//     * @return
//     */
//	static String getMetaDataValue(Map<String, String> metaData, String val) {
//		return metaData.get(val) != null ? metaData.get(val) : "";
//	}
//	
	/**
	 * 
	 * @param text
	 * @return
	 */
	static String makeXMLTextSafe(String text) {
		return "<![CDATA[" + text.replace("<", "").replace(">", "").replace("&", "") + "]]>";
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	static String makeXMLIdSafe(String text) {
		return "<![CDATA[" + text.replace("<", "%3C").replace(">", "%3E").replace("&", "%26") + "]]>";
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	static String makeXMLTextSafeField(String text) {
		return "<![CDATA[" + text.replace("<", "").replace(">", "").replace("&", "").replace("\\n", "").replace("\\r", "") + "]]>";
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	static String makeXMLTextSafeUrl(String text) {
		return "<![CDATA[" + text.replace("<", "").replace(">", "").replace("\\n", "").replace("\\r", "") + "]]>";
	}
	
}

class CountComparator implements Comparator<String> {

    Map<String, Integer> base;
    public CountComparator(Map<String, Integer> base) {
        this.base = base;
    }
  
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        }
    }
}