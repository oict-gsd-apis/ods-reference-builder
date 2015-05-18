package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
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
		//TODO DANIEL commented this line due to performance issues when opening the file 
		//newSolrDocument.setBody(Helper.minimizeWhitespace(newSolrDocument.getBody()));
			
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
		newSolrDocument.setSession1( newSolrDocument.getSession1().contains( replaceChar ) ? newSolrDocument.getSession1().replace( replaceChar , "") : newSolrDocument.getSession1() );
		newSolrDocument.setSession2( newSolrDocument.getSession2().contains( replaceChar ) ? newSolrDocument.getSession2().replace( replaceChar , "") : newSolrDocument.getSession2() );
		newSolrDocument.setSession3( newSolrDocument.getSession3().contains( replaceChar ) ? newSolrDocument.getSession3().replace( replaceChar , "") : newSolrDocument.getSession3() );
		newSolrDocument.setAgenda1( newSolrDocument.getAgenda1().contains( replaceChar ) ? newSolrDocument.getAgenda1().replace( replaceChar , "") : newSolrDocument.getAgenda1() );
		newSolrDocument.setAgenda2( newSolrDocument.getAgenda2().contains( replaceChar ) ? newSolrDocument.getAgenda2().replace( replaceChar , "") : newSolrDocument.getAgenda2() );
		newSolrDocument.setAgenda3( newSolrDocument.getAgenda3().contains( replaceChar ) ? newSolrDocument.getAgenda3().replace( replaceChar , "") : newSolrDocument.getAgenda3() );
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
			String newUrl = url.substring(0, url.indexOf("DS=")+3) + symbol + url.substring(url.indexOf("DS=")+3, url.length());
			newSolrDocument.setUrl(newUrl);
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
		if (newSolrDocument.getTitle().isEmpty())
			AppProp.database.insertWarning(newSolrDocument.getId(), newSolrDocument.getFilename() ,"Empty Title");
		return newSolrDocument;
	}
	
	/**
	 * 
	 * @param newSolrDocument
	 * @return
	 */
	private static SolrDocument processFutureDates(SolrDocument newSolrDocument) { 
		// Log these values to a particular DB Table
		Date d = new Date();
		if ( d.before( Helper.getTimestamp(newSolrDocument.getPublicationDate()) ) )
			Helper.logMessage(InfoType.Warning, newSolrDocument, "Future Date (" + newSolrDocument.getPublicationDate() + ")");
		return newSolrDocument;
	}
	
	/**
	 * 
	 * @param newSolrDocument
	 * @return
	 */
	private static SolrDocument processBodyText(SolrDocument newSolrDocument) { 
		//String body = newSolrDocument.getBody();
		// If bodies text is insufficient call Tika/OCR server and obtain new body
		//if (newSolrDocument.getRequiresNewBody() || body.isEmpty() || body.length() < 1) {
		if (newSolrDocument.getRequiresNewBody()) {
			Helper.logMessage(InfoType.Warning, newSolrDocument, "newBody Required");
//			String newBody = TextExtractorOCR.obtainText(newSolrDocument);
//			if (!newBody.isEmpty()) 
//				newSolrDocument.setBody( newBody );
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
		String regex = AppProp.referenceRegex;;
	    Map<String, Integer> refs = new HashMap<String, Integer>();
	    Pattern pattern = Pattern.compile(regex);//Pattern.compile("([A-Z]+[\\-\\./\\(\\)]([A-Z]+|[0-9]+)\\w+[\\-\\./\\(\\)]\\w+[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*)");
	    Matcher matcher = pattern.matcher(body);
	    //int count = 0;
		while (matcher.find()) {
			String potentialMatch = matcher.group();
			if (potentialMatch.indexOf("/") > 0) {
				if (potentialMatch.endsWith(".")) {
					potentialMatch = potentialMatch.substring(0, potentialMatch.length()-1);
				}
				int leftCharCount = Helper.getCountChar(potentialMatch, "(");
				int rightCharCount = Helper.getCountChar(potentialMatch, ")");
				if (leftCharCount != rightCharCount) {
					String leftPart = "";
					String rightPart = "";
					if (leftCharCount > rightCharCount) {
						leftPart = potentialMatch.substring(0, potentialMatch.lastIndexOf("("));
						rightPart = potentialMatch.substring(potentialMatch.lastIndexOf("(")+1, potentialMatch.length());
					} else {
						leftPart = potentialMatch.substring(0, potentialMatch.lastIndexOf(")"));
						rightPart = potentialMatch.substring(potentialMatch.lastIndexOf(")")+1, potentialMatch.length());
					}
					potentialMatch = leftPart + rightPart;
				}
				String pm = potentialMatch.trim();
				if (!refs.containsKey(pm)) {
					refs.put(pm, 1);
				} else{
					refs.put(pm, refs.get(pm)+1);	
				}
				//count++;
			}
		}
		
	    CountComparator cc =  new CountComparator(refs);
	    TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(cc);
	    sorted_map.putAll(refs);
		 
		newReferenceDocument.setId( newSolrDocument.getId() );
		newReferenceDocument.setSymbol( newSolrDocument.getSymbol() );
		newReferenceDocument.setTitle( newSolrDocument.getTitle() );
		
		newReferenceDocument.setPublicationDate( newSolrDocument.getPublicationDate() );
		newReferenceDocument.setTitle( newSolrDocument.getTitle() );
		newReferenceDocument.setTitle( newSolrDocument.getTitle() );
		newReferenceDocument.setUrl( newSolrDocument.getUrl() );
		newReferenceDocument.setLanguageCode( newSolrDocument.getLanguageCode() );
		newReferenceDocument.setDateCreated( newSolrDocument.getDateCreated() );
		newReferenceDocument.setReferences( sorted_map );		
		
		return newReferenceDocument;
	}
}
