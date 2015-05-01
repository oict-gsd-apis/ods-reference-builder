package org.un.dm.oict.gsd.odsreferencebuilder;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

public class TextExtractorFile {
	
	static SolrDocument readXmlDocument(String currXmlFilename) { 
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
	
	// TODO Expansion of business logic required
	static SolrDocument mapXmlSolrDocument(SolrDocument currentSolrDocument, Document doc) {
		
	    currentSolrDocument.setId( getXmlElement(doc, "id") );
	    currentSolrDocument.setSymbol( getXmlElement(doc, "symbol") );
	    currentSolrDocument.setLanguageCode( getXmlElement(doc, "languageCode") );
	    
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
	
	static String getXmlElement(Document doc, String fieldName) {
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

	
	static void obtainText() {}
}
