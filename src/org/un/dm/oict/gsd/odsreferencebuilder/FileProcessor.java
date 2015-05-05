package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.un.dm.oict.gsd.odsreferencebuilder.OutputDatabaseMSSQL.InfoType;

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
	 * This method is used to process the logic for each file
	 * @param currentSolrDocument
	 * @param newSolrDocument
	 * @return
	 */
	protected static SolrDocument processFile(SolrDocument currentSolrDocument, SolrDocument newSolrDocument) {
		newSolrDocument = extractMapData(currentSolrDocument, newSolrDocument);
		newSolrDocument = cleanseData(newSolrDocument);
		return newSolrDocument;
	}
	
	/**
	 * This method is used to map the existing solr document with the new one, which will
	 * be transformed
	 * @param currentSolrDocument
	 * @param newSolrDocument
	 * @return
	 */
	private static SolrDocument extractMapData(SolrDocument currentSolrDocument, SolrDocument newSolrDocument) { 
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
	
		// TODO Daniel set these values, code similar in old version
		//protected Map<String, String> sessions;
		//protected Map<String, String> agendas;
	    
		newSolrDocument.setBody( currentSolrDocument.getBody() );
		newSolrDocument.setRequiresNewBody( checkBodyValidity(newSolrDocument) );

		return newSolrDocument;
	}
	
	/**
	 * This method is used as a wrapper for the Helper method checkBodyContainsInvalidChars
	 * which checks a text body for particular erroneous characters
	 * @param newSolrDocument
	 * @return
	 */
	protected static boolean checkBodyValidity(SolrDocument newSolrDocument) {
		return Helper.checkBodyContainsInvalidChars(newSolrDocument, AppProp.invalidChars);
	}

	/**
	 * This method is used as a wrapper to cleanse the data
	 * @param newSolrDocument
	 * @return
	 */
	protected static SolrDocument cleanseData(SolrDocument newSolrDocument) { 
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
		// Remove whitespace from body
		newSolrDocument.setBody(Helper.minimizeWhitespace(newSolrDocument.getBody()));
			
		return newSolrDocument; 
	}
	
	/**
	 * This method removes ? from particular fields
	 * @param newSolrDocument
	 * @return
	 */
	private static SolrDocument processQMarks(SolrDocument newSolrDocument) { 
		String replaceChar = "?";
		newSolrDocument.setId( newSolrDocument.getId().contains( replaceChar ) ? newSolrDocument.getId().replace( replaceChar , "") : newSolrDocument.getId() );
		newSolrDocument.setSymbol( newSolrDocument.getSymbol().contains( replaceChar ) ? newSolrDocument.getSymbol().replace( replaceChar , "") : newSolrDocument.getSymbol() );
		newSolrDocument.setAlternativeSymbols( newSolrDocument.getAlternativeSymbols().contains( replaceChar ) ? newSolrDocument.getAlternativeSymbols().replace( replaceChar , "") : newSolrDocument.getAlternativeSymbols() );
		newSolrDocument.setDocType( newSolrDocument.getDocType().contains( replaceChar ) ? newSolrDocument.getDocType().replace( replaceChar , "") : newSolrDocument.getDocType() );
		newSolrDocument.setSize( newSolrDocument.getSize().contains( replaceChar ) ? newSolrDocument.getSize().replace( replaceChar , "") : newSolrDocument.getSize() );
		//TODO Daniel Further session and agendas Daniel Extend functionality
		newSolrDocument.setSession1( newSolrDocument.getSession1().contains( replaceChar ) ? newSolrDocument.getSession1().replace( replaceChar , "") : newSolrDocument.getSession1() );
		newSolrDocument.setAgenda1( newSolrDocument.getAgenda1().contains( replaceChar ) ? newSolrDocument.getAgenda1().replace( replaceChar , "") : newSolrDocument.getAgenda1() );
		newSolrDocument.setUrlJob( newSolrDocument.getUrlJob().contains( replaceChar ) ? newSolrDocument.getUrlJob().replace( replaceChar , "") : newSolrDocument.getUrlJob() );
		newSolrDocument.setSubjects( newSolrDocument.getSubjects().contains( replaceChar ) ? newSolrDocument.getSubjects().replace( replaceChar , "") : newSolrDocument.getSubjects() );
		newSolrDocument.setPublicationDate( newSolrDocument.getPublicationDate().contains( replaceChar ) ? newSolrDocument.getPublicationDate().replace( replaceChar , "") : newSolrDocument.getPublicationDate() );
		newSolrDocument.setLanguageCode( newSolrDocument.getLanguageCode().contains( replaceChar ) ? newSolrDocument.getLanguageCode().replace( replaceChar , "") : newSolrDocument.getLanguageCode() );
		newSolrDocument.setDateCreated( newSolrDocument.getDateCreated().contains( replaceChar ) ? newSolrDocument.getDateCreated().replace( replaceChar , "") : newSolrDocument.getDateCreated() );

		return newSolrDocument;
	}
	
	/**
	 * 
	 * @param newSolrDocument
	 * @return
	 */
	private static SolrDocument ensureSymbolUrl(SolrDocument newSolrDocument) {
		String url = newSolrDocument.getUrl().toUpperCase();
		String symbol = newSolrDocument.getSymbol().toUpperCase();
		if (!url.contains(symbol)) {
			Helper.logMessage(InfoType.Error, newSolrDocument, "Symbol not in URL");
		}
		return newSolrDocument;
	}
	
	/**
	 * 
	 * @param newSolrDocument
	 * @return
	 */
	private static SolrDocument processEmptyTitles(SolrDocument newSolrDocument) { 
		// Log these values to a particular DB Table
		AppProp.database.insertWarning(newSolrDocument.getId(), "Empty Title");
		return newSolrDocument;
	}
	
	/**
	 * 
	 * @param newSolrDocument
	 * @return
	 */
	private static SolrDocument processFutureDates(SolrDocument newSolrDocument) { 
		// Log these values to a particular DB Table
		Helper.logMessage(InfoType.Warning, newSolrDocument, "Future Date (" + newSolrDocument.getPublicationDate() + ")");
		return newSolrDocument;
	}
	
	/**
	 * 
	 * @param newSolrDocument
	 * @return
	 */
	private static SolrDocument processBodyText(SolrDocument newSolrDocument) { 
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
	private static SolrDocument processFieldSize(SolrDocument newSolrDocument) {
		String size = newSolrDocument.getSize();
		try {
			Integer.parseInt(size);
		} catch (NumberFormatException e) {
			size = "0";
			Helper.logMessage(InfoType.Warning, newSolrDocument, "Size not number (" + newSolrDocument.getSize() + ")");
		}
		newSolrDocument.setSize( size );
		return newSolrDocument;
	}
	
	/**
	 * 
	 * @param newSolrDocument
	 * @param newReferenceDocument
	 * @return
	 */
	protected static ReferenceDocument extractReferences(SolrDocument newSolrDocument, ReferenceDocument newReferenceDocument) { 
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
		newReferenceDocument.setSymbol( newSolrDocument.getSymbol() );
		newReferenceDocument.setTitle( newSolrDocument.getTitle() );
		
		newReferenceDocument.setPublicationDate( newSolrDocument.getPublicationDate() );
		newReferenceDocument.setTitle( newSolrDocument.getTitle() );
		newReferenceDocument.setTitle( newSolrDocument.getTitle() );
		newReferenceDocument.setUrl( newSolrDocument.getUrl() );
		newReferenceDocument.setLanguageCode( newSolrDocument.getLanguageCode() );
		newReferenceDocument.setDateCreated( newSolrDocument.getDateCreated() );
		newReferenceDocument.setReferences( refs );		
		
		return newReferenceDocument;
	}
}
