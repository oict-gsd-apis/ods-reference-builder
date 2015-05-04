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
		newSolrDocument.setSymbol( currentSolrDocument.getSymbol() );
		newSolrDocument.setLanguageCode( currentSolrDocument.getLanguageCode() );
		newSolrDocument.setSymbol2( currentSolrDocument.getSymbol2() );
		newSolrDocument.setSymbol3( currentSolrDocument.getSymbol3() );
		newSolrDocument.setAgenda1( currentSolrDocument.getAgenda1() );
		newSolrDocument.setAgenda2( currentSolrDocument.getAgenda2() );
		newSolrDocument.setAgenda3( currentSolrDocument.getAgenda3() );
		newSolrDocument.setSession1( currentSolrDocument.getSession1() );
		newSolrDocument.setSession2( currentSolrDocument.getSession2() );
		newSolrDocument.setSession3( currentSolrDocument.getSession3() );
		newSolrDocument.setAlternativeSymbols( currentSolrDocument.getAlternativeSymbols() );
		newSolrDocument.setTitle( currentSolrDocument.getTitle() );
		newSolrDocument.setDocType( currentSolrDocument.getDocType() );
		newSolrDocument.setSize( currentSolrDocument.getSize() );
		newSolrDocument.setUrlJob( currentSolrDocument.getUrlJob() );
		newSolrDocument.setSubjects( currentSolrDocument.getSubjects() );
		newSolrDocument.setPublicationDate( currentSolrDocument.getPublicationDate() );
		newSolrDocument.setUrl( currentSolrDocument.getUrl() );
		newSolrDocument.setPdfContentLength( currentSolrDocument.getPdfContentLength() );
		newSolrDocument.setPdfContentType( currentSolrDocument.getPdfContentType() );
		newSolrDocument.setPdfCreationDate( currentSolrDocument.getPdfCreationDate() );
		newSolrDocument.setPdfLastModified( currentSolrDocument.getPdfLastModified() );
		newSolrDocument.setPdfLastSaveDate( currentSolrDocument.getPdfLastSaveDate() );
		newSolrDocument.setPdfCreated( currentSolrDocument.getPdfCreated() );
		newSolrDocument.setPdfDate( currentSolrDocument.getPdfDate() );
		newSolrDocument.setPdfDCTermsCreated( currentSolrDocument.getPdfDCTermsCreated() );
		newSolrDocument.setPdfDCTermsModified( currentSolrDocument.getPdfDCTermsModified() );
		newSolrDocument.setPdfMetaCreationDate( currentSolrDocument.getPdfMetaCreationDate() );
		newSolrDocument.setPdfMetaSaveDate( currentSolrDocument.getPdfMetaSaveDate() );
		newSolrDocument.setPdfModified( currentSolrDocument.getPdfModified() );
		newSolrDocument.setPdfProducer( currentSolrDocument.getPdfProducer() );
		newSolrDocument.setPdfXMPCreatorTool( currentSolrDocument.getPdfXMPCreatorTool() );
		newSolrDocument.setPdfXMPTpgNPages( currentSolrDocument.getPdfXMPTpgNPages() );
		newSolrDocument.setDateCreated( currentSolrDocument.getDateCreated() );
	
		//protected Map<String, String> sessions;
		//protected Map<String, String> agendas;
	    
		newSolrDocument.setBody( currentSolrDocument.getBody() );
		newSolrDocument.setRequiresNewBody( checkBodyValidity(newSolrDocument) );

		return newSolrDocument;
	}
	
	/**
	 * 
	 * @param newSolrDocument
	 * @return
	 */
	static boolean checkBodyValidity(SolrDocument newSolrDocument) {
		return checkBodyContainsInvalidChars(newSolrDocument, new char[] {'\uFFFD','\uF02A'});
	}

	// TODO add removal of breaklines regardless of whether a new body is extracted
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
		// Ensure that the URL contains the Symbol
		newSolrDocument = ensureSymbolUrl(newSolrDocument);
		// Check body contains relevant text
		newSolrDocument = processBodyText(newSolrDocument);
			
		return newSolrDocument; 
	}
	
	// TODO Extend functionality
	/**
	 * 
	 * @param newSolrDocument
	 * @return
	 */
	static SolrDocument processQMarks(SolrDocument newSolrDocument) { 
		String replaceChar = "?";
		newSolrDocument.setId( newSolrDocument.getId().contains( replaceChar ) ? newSolrDocument.getId().replace( replaceChar , "") : newSolrDocument.getId() );
		//newSolrDocument.setSymbol( newSolrDocument.getSymbol().contains( replaceChar ) ? newSolrDocument.getSymbol().replace( replaceChar , "") : newSolrDocument.getSymbol() );
		/*alternativeSymbols
		docType
		size
		session1
		agenda1
		urlJob
		subjects
		publicationDate
		languageCode
		dateCreated*/
		return newSolrDocument;
	}
	
	static SolrDocument ensureSymbolUrl(SolrDocument newSolrDocument) {
		/*String url = newSolrDocument.getUrl().toUpperCase();
		String symbol = newSolrDocument.getSymbol().toUpperCase();
		if (!url.contains(symbol)) {
			// Log to database
		}*/
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
		if (newSolrDocument.getRequiresNewBody() || body.isEmpty() || body.length() < 1) {
			String newBody = TextExtractorOCR.obtainText(newSolrDocument);
			if (!newBody.isEmpty()) 
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
	// TODO Counts per reference
	/**
	 * 
	 * @param newSolrDocument
	 * @param newReferenceDocument
	 * @return
	 */
	static ReferenceDocument extractReferences(SolrDocument newSolrDocument, ReferenceDocument newReferenceDocument) { 
	    String body = newSolrDocument.getBody();
	    String regex = AppProp.referenceRegex;
	    List<String> refs = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex);
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
	
	/**
	 * 
	 * @param newSolrDocument
	 * @param invalidChars
	 * @return
	 */
	static boolean checkBodyContainsInvalidChars(SolrDocument newSolrDocument, char[] invalidChars) {
		String body = newSolrDocument.getBody();
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
