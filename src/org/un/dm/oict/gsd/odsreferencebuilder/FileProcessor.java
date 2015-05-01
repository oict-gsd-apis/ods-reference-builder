package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This class is used process an existing Solr Document, copying the values
 * and applying business logic to the new file to remove unwanted content
 * @version 1.0
 * @codeReviewer
 */
public class FileProcessor {
	
	/**
	 * 
	 * @param currentSolrDocument
	 * @param newSolrDocument
	 * @return
	 */
	static SolrDocument processFile(SolrDocument currentSolrDocument, SolrDocument newSolrDocument) {
		
		newSolrDocument = extractMapData(currentSolrDocument, newSolrDocument);

		newSolrDocument = cleanseData(newSolrDocument);
		
		return newSolrDocument;
	}
	
	// TODO Expansion of business logic required
	/**
	 * 
	 * @param currentSolrDocument
	 * @param newSolrDocument
	 * @return
	 */
	static SolrDocument extractMapData(SolrDocument currentSolrDocument, SolrDocument newSolrDocument) { 
		newSolrDocument.setId( currentSolrDocument.getId() );
		newSolrDocument.setBody( currentSolrDocument.getBody() );
		return newSolrDocument;
	}

	/**
	 * 
	 * @param newSolrDocument
	 * @return
	 */
	static SolrDocument cleanseData(SolrDocument newSolrDocument) { 
		// Remove ? from particular fields
		newSolrDocument = processQMarks(newSolrDocument);
		// Empty titles
		newSolrDocument = processEmptyTitles(newSolrDocument);
		// Dates in the future
		newSolrDocument = processFutureDates(newSolrDocument);
		// Check field size is integer If its not an integer set to zero
		newSolrDocument = processFieldSize(newSolrDocument);
		// Check body contains relevant text
		newSolrDocument = processBodyText(newSolrDocument);
			
		return newSolrDocument; 
	}
	
	/**
	 * 
	 * @param newSolrDocument
	 * @return
	 */
	static SolrDocument processQMarks(SolrDocument newSolrDocument) { 
		String replaceChar = "?";
		newSolrDocument.setId( newSolrDocument.getId().contains( replaceChar ) ? newSolrDocument.getId().replace( replaceChar , "") : newSolrDocument.getId() );
		return newSolrDocument;
	}
	
	// TODO Implement logic
	/**
	 * 
	 * @param newSolrDocument
	 * @return
	 */
	static SolrDocument processEmptyTitles(SolrDocument newSolrDocument) { 
		// Log these values to a particular DB Table
		return newSolrDocument;
	}
	
	// TODO Implement logic
	/**
	 * 
	 * @param newSolrDocument
	 * @return
	 */
	static SolrDocument processFutureDates(SolrDocument newSolrDocument) { 
		// Log these values to a particular DB Table
		return newSolrDocument;
	}
	
	// TODO Identify problematic characters
	/**
	 * 
	 * @param newSolrDocument
	 * @return
	 */
	static SolrDocument processBodyText(SolrDocument newSolrDocument) { 
		String body = newSolrDocument.getBody();
		// If bodies text is insufficient call Tika/OCR server and obtain new body
		if (body.contains("&#$@%@#$%@#$") || body.isEmpty() || body.length() < 1) {
			String newBody = TextExtractorOCR.obtainText(newSolrDocument);
			if (!newBody.equals("")) 
				newSolrDocument.setBody( newBody );
		}
		return newSolrDocument;
	}
	
	/**
	 * 
	 * @param newSolrDocument
	 * @return
	 */
	static SolrDocument processFieldSize(SolrDocument newSolrDocument) {
		String size = newSolrDocument.getSize();
		try {
			Integer.parseInt(size);
		} catch (NumberFormatException e) {
			size = "0";
		}
		newSolrDocument.setSize( size );
		return newSolrDocument;
	}
	
	// TODO Expansion of business logic required
	/**
	 * 
	 * @param newSolrDocument
	 * @param newReferenceDocument
	 * @return
	 */
	static ReferenceDocument extractReferences(SolrDocument newSolrDocument, ReferenceDocument newReferenceDocument) { 
	    String body = newSolrDocument.getBody();
	    List<String> refs = new ArrayList<String>();
		Pattern pattern = Pattern.compile("(\\w+[\\-\\./\\(\\)]\\w+[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*)");
	    Matcher matcher = pattern.matcher(body);
		while (matcher.find()) {
			String potentialMatch = matcher.group();
			if (potentialMatch.indexOf("/") > 0) {
				if (!refs.contains(potentialMatch))
					refs.add(potentialMatch);
			}
		}
		newReferenceDocument.setId( newSolrDocument.getId() );
		newReferenceDocument.setLanguageCode( newSolrDocument.getLanguageCode() );
		newReferenceDocument.setReferences( refs );
		
		return newReferenceDocument;
	}
	
}
