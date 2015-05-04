package org.un.dm.oict.gsd.odsreferencebuilder;

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
	 * 
	 * @param name
	 * @return
	 */
	static String getProperty(String name){
		return AppProp.configFile.getProperty(name);
	}
	
	/**
	 * 
	 */
	static void initialiseConfigFile(){
		try {
			AppProp.configFile = Helper.getProperties(Service.class.getClassLoader());
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}
	
	/**
	 * 
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
}