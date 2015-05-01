package org.un.dm.oict.gsd.odsreferencebuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextExtractorOCR {

	static String obtainText(SolrDocument newSolrDocument) { 
		String pdfUrl = newSolrDocument.getUrlJob();
		
		String newBody = performCURLCommand(pdfUrl);
		if (newBody.length() > 0)
			return newBody;
		else
			return "";
	}
	
	static String performCURLCommand(String pdfUrl) {
		String[] command = { "curl",
	            			"-T",
	            			pdfUrl,
	            			"http://frankie:9998/tika",
	            			"--header",
	            			"\"Accept: text/plain\""};
        ProcessBuilder probuilder = new ProcessBuilder( command );
        
        Process process = null;
		try {
			process = probuilder.start();
		} catch (IOException e) {
			return "";
		}
        
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String body = "";
        String line = "";
        try {
			while ((line = br.readLine()) != null) {
				body += line;
			}
		} catch (IOException e) {
			return "";
		}
        
        try {
            process.waitFor();
        } catch (InterruptedException e) {
        	return "";
        }
        return body;
	}
}
