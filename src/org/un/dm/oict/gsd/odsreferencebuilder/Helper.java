package org.un.dm.oict.gsd.odsreferencebuilder;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

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
		return checkBodyContainsInvalidChars(body, invalidChars);
	}
	
	/**
	 * 
	 * @param body
	 * @param invalidChars
	 * @return
	 */
	static boolean checkBodyContainsInvalidChars(String body, char[] invalidChars) {
		boolean found = false;
		for(char c : invalidChars) {
			if (body.contains(Character.toString(c))){
				found =  true;
				break; //once one of the characters is found breaks the loop 
			}
		}
		return found;
	}
}