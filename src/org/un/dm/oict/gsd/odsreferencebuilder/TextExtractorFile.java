package org.un.dm.oict.gsd.odsreferencebuilder;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This class is used to extract data from a file and map this
 * @version 1.0
 * @codeReviewer
 */
public class TextExtractorFile {
	
	/**
	 * This method reads an XML document then calls the mapper function
	 * @param currXmlFilename
	 * @return
	 */
	protected static SolrDocument readXmlDocument(String currXmlFilename) { 
		SolrDocument currentSolrDocument = new SolrDocument();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    Document doc = builder.parse( new File(currXmlFilename) ); 
		    
		    // Map Schema onto SolrDocument Object
		    currentSolrDocument = mapXmlSolrDocument(currentSolrDocument, doc);
		    		    
		} catch (Exception e) {
			
		}
		return currentSolrDocument;
	}
	
	// TODO Daniel Expansion of business logic required
	/**
	 * 
	 * @param currentSolrDocument
	 * @param doc
	 * @return
	 */
	private static SolrDocument mapXmlSolrDocument(SolrDocument currentSolrDocument, Document doc) {
		
	    currentSolrDocument.setId( getXmlElement(doc, "id") );
	    currentSolrDocument.setSymbol( getXmlElement(doc, "symbol") );
	    currentSolrDocument.setLanguageCode( getXmlElement(doc, "languageCode") );
	    currentSolrDocument.setSymbol2( getXmlElement(doc, "symbol2") );
	    currentSolrDocument.setSymbol3( getXmlElement(doc, "symbol3") );
		currentSolrDocument.setAgenda1( getXmlElement(doc, "agenda1") );
		currentSolrDocument.setAgenda2( getXmlElement(doc, "agenda2") );
		currentSolrDocument.setAgenda3( getXmlElement(doc, "agenda3") );
		currentSolrDocument.setSession1( getXmlElement(doc, "session1") );
		currentSolrDocument.setSession2( getXmlElement(doc, "session2") );
		currentSolrDocument.setSession3( getXmlElement(doc, "session3") );
		currentSolrDocument.setAlternativeSymbols( getXmlElement(doc, "alternativeSymbols") );
		currentSolrDocument.setTitle( getXmlElement(doc, "title") );
		currentSolrDocument.setDocType( getXmlElement(doc, "docType") );
		currentSolrDocument.setSize( getXmlElement(doc, "size") );
		currentSolrDocument.setUrlJob( getXmlElement(doc, "urlJob") );
		currentSolrDocument.setSubjects( getXmlElement(doc, "subjects") );
		currentSolrDocument.setPublicationDate( getXmlElement(doc, "publicationDate") );
		currentSolrDocument.setUrl( getXmlElement(doc, "url") );
		currentSolrDocument.setPdfContentLength( getXmlElement(doc, "pdfContentLength") );
		currentSolrDocument.setPdfContentType( getXmlElement(doc, "pdfContentType") );
		currentSolrDocument.setPdfCreationDate( getXmlElement(doc, "pdfCreationDate") );
		currentSolrDocument.setPdfLastModified( getXmlElement(doc, "pdfLastModified") );
		currentSolrDocument.setPdfLastSaveDate( getXmlElement(doc, "pdfLastSaveDate") );
		currentSolrDocument.setPdfCreated( getXmlElement(doc, "pdfCreated") );
		currentSolrDocument.setPdfDate( getXmlElement(doc, "pdfDate") );
		currentSolrDocument.setPdfDCTermsCreated( getXmlElement(doc, "pdfDCTermsCreated") );
		currentSolrDocument.setPdfDCTermsModified( getXmlElement(doc, "pdfDCTermsModified") );
		currentSolrDocument.setPdfMetaCreationDate( getXmlElement(doc, "pdfMetaCreationDate") );
		currentSolrDocument.setPdfMetaSaveDate( getXmlElement(doc, "pdfMetaSaveDate") );
		currentSolrDocument.setPdfModified( getXmlElement(doc, "pdfModified") );
		currentSolrDocument.setPdfProducer( getXmlElement(doc, "pdfProducer") );
		currentSolrDocument.setPdfXMPCreatorTool( getXmlElement(doc, "pdfXMPCreatorTool") );
		currentSolrDocument.setPdfXMPTpgNPages( getXmlElement(doc, "pdfXMPTpgNPages") );
		currentSolrDocument.setDateCreated( getXmlElement(doc, "dateCreated") );
	
		//protected Map<String, String> sessions;
		//protected Map<String, String> agendas;
	    
	    if (currentSolrDocument.getLanguageCode().equals("en"))
	    	currentSolrDocument.setBody( getXmlElement(doc, "body_en") );
	    else if (currentSolrDocument.getLanguageCode().equals("fr"))
	    	currentSolrDocument.setBody( getXmlElement(doc, "body_fr") );
	    else if (currentSolrDocument.getLanguageCode().equals("es"))
	    	currentSolrDocument.setBody( getXmlElement(doc, "body_es") );
	    else if (currentSolrDocument.getLanguageCode().equals("ar"))
	    	currentSolrDocument.setBody( getXmlElement(doc, "body_ar") );
	    else if (currentSolrDocument.getLanguageCode().equals("ru"))
	    	currentSolrDocument.setBody( getXmlElement(doc, "body_ru") );
	    else if (currentSolrDocument.getLanguageCode().equals("zh-cn"))
	    	currentSolrDocument.setBody( getXmlElement(doc, "body_zh-cn") );
	    
	    return currentSolrDocument;
	    
	}
	
	/**
	 * This method gets a specific xml element based on a Solr Schema
	 * @param doc
	 * @param fieldName
	 * @return
	 */
	private static String getXmlElement(Document doc, String fieldName) {
		String field = "";
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		try {
			field = xpath.evaluate("/add/doc/field[@name='"+ fieldName + "']/text()", doc);
		} catch (Exception e) {
			System.out.println("ERROR GENERAL: " + e.getMessage());
		}
		return field;
	}

	/**
	 * 
	 */
	static void obtainText() {}
}