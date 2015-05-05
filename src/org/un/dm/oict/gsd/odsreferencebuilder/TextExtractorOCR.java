package org.un.dm.oict.gsd.odsreferencebuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This class is used to connect to our Tika/Tesseract Instance (Check One Pager)
 * @version 1.0
 * @codeReviewer
 * 
 * Tesseract & Ghostscript should ideally be installed:
 * sudo apt-get install ghostscript
 * sudo apt-get install tesseract-ocr tesseract-ocr-eng tesseract-ocr-fre tesseract-ocr-spa tesseract-ocr-ara tesseract-ocr-rus tesseract-ocr-chi_sim
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
		String body = performProcess(command);
		if (Helper.checkBodyContainsInvalidChars(body, AppProp.invalidChars)) {
			// TODO log to process later as Tika was unable to obtain a good version
			// could be automated to do performCompleteOCR
		}
			
		return body;
	}
	
	/**
	 * This is a generic method used to perform a process using
	 * a ProcessBuilder object
	 * @param command
	 * @return
	 */
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
				if (AppProp.debug)
					System.out.println(line);
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
	
	/**
	 * This method is used to remove some of the whitespace in the body
	 * @param val
	 * @return
	 */
	static String minimizeWhitespace(String val) {
		return val.replaceAll("[\n\r]","") + "\n";
	}
	
	/**
	 * This method is used to run a complete OCR process:
	 * 	Ghostscript
	 *  Tesseract
	 *  Text Extraction
	 * @param tempOutputDir
	 * @param pdfUrl
	 * @param languageCode
	 * @return
	 */
	static String performCompleteOCR(String pdfUrl, String languageCode) {
		String tempId = UUID.randomUUID().toString();
		String imgFile = AppProp.tempTesseractImgOutputDir + "" + tempId + ".tif";
		String txtFile = AppProp.tempTesseractImgOutputDir + "" + tempId + "";
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
	
	/**
	 * This method is used to read the contents of a file
	 * @param path
	 * @param encoding
	 * @return
	 */
	static String readFile(String path, Charset encoding) {
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		return new String(encoded, encoding);
	}

	/**
	 * This method is used to map solr language code i.e. en
	 * onto tesseract language codes i.e. eng
	 * @param languageCode
	 * @return
	 */
	static String mapLanguageCode(String languageCode) {
		if (languageCode.equals("en"))
			return "eng";
		else if (languageCode.equals("fr"))
			return "fre";
		else if (languageCode.equals("es"))
			return "spa";
		else if (languageCode.equals("ar"))
			return "ara";
		else if (languageCode.equals("ru"))
			return "rus";
		else if (languageCode.equals("zh-cn"))
			return "chi_sim";
		else
			return "eng";
	}
}
