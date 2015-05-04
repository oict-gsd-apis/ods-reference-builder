package org.un.dm.oict.gsd.odsreferencebuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This class is used to connect to our Tika/Tesseract Instance (Check One Pager)
 * @version 1.0
 * @codeReviewer
 */
public class TextExtractorOCR {

	/**
	 * This method simply obtains the jobUrl from the Solr Document
	 * calls the performCURLCommand method to extract a new body text
	 * using tika/tesseract, verifies that the body is not blank
	 * and returns this
	 * @param newSolrDocument
	 * @return
	 */
	static String obtainText(SolrDocument newSolrDocument) { 
		// Obtain the jobUrl of the Solr Document
		String pdfUrl = newSolrDocument.getUrlJob();
		
		// Call method and store the returning parsed body
		String newBody = performCURLCommand(pdfUrl);
		// Verify that the new body is not empty
		if (newBody.length() > 0)
			return newBody;
		else
			return "";
	}
	
	/**
	 * This method is used to make a cURL command to the dev
	 * server which has tika/tesseract installed on it, passing the name
	 * of the pdf and obtaining the text return
	 * @param pdfUrl
	 * @return
	 */
	static String performCURLCommand(String pdfUrl) {
		// Construct the cURL command
		String[] command = { "curl",
	            			"-T",
	            			pdfUrl.contains("/mnt/y_drive/")? pdfUrl: "/mnt/y_drive/" + pdfUrl,
	            			"http://frankie:9998/tika",
	            			"--header",
	            			"\"Accept: text/plain\""};
		// Create a new ProcessBuilder which will run the command
        ProcessBuilder probuilder = new ProcessBuilder( command );
        
        Process process = null;
		try {
			// Start the local process
			process = probuilder.start();
		} catch (IOException e) {
			return "";
		}
        
		// Prepare to receive the output from the process
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String body = "";
        String line = "";
        try {
        	// Obtain the output from the process and store this in the new body
			while ((line = br.readLine()) != null) {
				// Remove excess white space
				if (line.length() > 0)
					body += line.replaceAll("[\n\r]","") + "\n";
			}
		} catch (IOException e) {
			return "";
		}
        
        try {
        	// Wait for this process to finish
            process.waitFor();
        } catch (InterruptedException e) {
        	return "";
        }
        return body;
	}
}
