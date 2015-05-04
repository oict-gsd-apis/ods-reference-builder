package org.un.dm.oict.gsd.odsreferencebuilder;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This class is used to output to a file, either Solr or Reference document for ODS
 * @version 1.0
 * @codeReviewer
 */
public class OutputFile {
	/**
	 * 
	 * @param filename
	 * @param xml
	 */
	void writeSolrDocument(String filename, String xml) {
		writeDocument(filename, xml);
	}
	
	/**
	 * 
	 * @param filename
	 * @param xml
	 */
	void writeReferenceDocument(String filename, String xml) {
		writeDocument(filename, xml);
	}
	
	/**
	 * 
	 * @param filename
	 * @param xml
	 */
	private void writeDocument(String filename, String xml) {
		synchronized(this){
			BufferedWriter outputStream = null;
			FileOutputStream fileOutputStream = null;
			try {
				try {					
					fileOutputStream = new FileOutputStream(filename, false);
					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
					outputStream = new BufferedWriter(outputStreamWriter);
				} catch (FileNotFoundException ef) {
					System.out.println("ERROR: " + ef.getMessage());
				} catch (Exception e) {
					System.out.println("ERROR: " + e.getMessage());
				}
				outputStream.write(xml);				 
			} catch (Exception e) {

			} finally {
				try {
					outputStream.close();
					fileOutputStream.close();
				} catch (IOException e) {
					System.out.println("ERROR: " + e.getMessage());
				}
			}
	    }
	}
}
