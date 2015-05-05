package org.un.dm.oict.gsd.odsreferencebuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
	            			pdfUrl.contains("/mnt/y_drive/")? pdfUrl: "/mnt/y_drive/DATA/" + pdfUrl,
	            			"http://frankie:9998/tika",
	            			"--header",
	            			"\"Accept: text/plain\""};
		return performProcess(command);
	}
	
	static String performProcess(String[] command) {
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
					body += minimizeWhitespace(line);
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
	
	static String minimizeWhitespace(String val) {
		return val.replaceAll("[\n\r]","") + "\n";
	}
	
	static String performCompleteOCR(String tempOutputDir, String pdfUrl, String languageCode) {
		String tempId = UUID.randomUUID().toString();
		String imgFile = tempOutputDir + "" + tempId + ".tif";
		String txtFile = tempOutputDir + "" + tempId + "";
		String lang = mapLanguageCode(languageCode);
		// TODO Copy PDF locally
		// Convert pdf to images in a specific folder
		String[] command = { "gs",
        			"-dNOPAUSE",
        			"-sDEVICE=tiffg4",
        			"-r600x600",
        			"-dBATCH",
        			"-sPAPERSIZE=a4",
        			"-sOutputFile=" + imgFile,
        			pdfUrl};
		performProcess(command);
		// Use Tesseract to convert these images to text based on the languageCode
		String[] command2 = { "tesseract",
				imgFile,
				txtFile,
    			"-l",
    			lang};
		performProcess(command2);
		
		// Read the contents of this file and store in a variable
		String body = readFile(txtFile + ".txt", StandardCharsets.UTF_8);
		
		try {
			Files.delete(Paths.get(imgFile)); 
			Files.delete(Paths.get(txtFile));
		} catch (IOException e) {

		} 
		return body;
	}
	
	static String readFile(String path, Charset encoding) {
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			
		}
		return new String(encoded, encoding);
	}

	static String mapLanguageCode(String languageCode) {
		// en-> eng
		if (languageCode.equals("en"))
			return "eng";
		else if (languageCode.equals("ar"))
			return "ara";
		else
			return "eng";
	}
}
