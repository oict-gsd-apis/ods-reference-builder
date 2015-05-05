package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.ArrayList;
import java.util.List;

import org.un.dm.oict.gsd.odsreferencebuilder.OutputDatabaseMSSQL.InfoType;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This class represents a ODS Reference Explorer Document
 * @version 1.0
 * @codeReviewer
 */
public class ReferenceDocument {

	private String id;
	private String symbol;
	private String title;
	private String publicationDate;
	private String url;
	private String languageCode;
	private String dateCreated;
	// Multi-valued Solr Field
	private List<String> references;
	
	private String filename;
	
	/**
	 * 
	 */
	public ReferenceDocument() {
		this.references = new ArrayList<String>();
	}
	
	@Override
	/**
	 * This overridden method is used to construct the Solr XML for the document
	 * used to populate the Solr index
	 */
	public String toString() {
		String xml = "";
		xml += "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n";
		xml += "<add>\n";
			xml += "<doc>\n";
				xml += "<field name=\"id\">" + this.id + "</field>\n";
				xml += "<field name=\"symbol\">" + this.symbol + "</field>\n";
				for(String ref : this.references) {
					xml += "<field name=\"reference\">" + ref + "</field>\n";
				}
			xml += "</add>\n";
		xml += "</doc>";
		return xml;
	}
	
	public String buildNewFilename() {
		try {
			String tf = this.filename.substring(this.filename.lastIndexOf("/")+1, this.filename.length());
			return AppProp.referenceOutputFolder  + tf;
		} catch (Exception e) {
			Helper.logMessage(InfoType.Error, e.getMessage());
			return "";
		}
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public List<String> getReferences() {
		return references;
	}
	public void setReferences(List<String> references) {
		this.references = references;
	}
	public void addReference(String reference) {
		this.references.add(reference);
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
