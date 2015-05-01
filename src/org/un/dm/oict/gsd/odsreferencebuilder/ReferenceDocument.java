package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This class represents a ODS Reference Explorer Document
 * @version 1.0
 * @codeReviewer
 */
public class ReferenceDocument {

	protected String id;
	protected String symbol;
	protected String title;
	protected String publicationDate;
	protected String url;
	protected String languageCode;
	protected String dateCreated;
	// Multi-valued Solr Field
	protected List<String> references;
	
	/**
	 * 
	 */
	public ReferenceDocument() {
		this.references = new ArrayList<String>();
	}
	
	@Override
	/**
	 * 
	 */
	public String toString() {
		String xml = "";
		xml += "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
		xml += "<add>";
			xml += "<doc>";
				xml += "<field name=\"id\">" + this.id + "</field>";
				xml += "<field name=\"symbol\">" + this.symbol + "</field>";
				for(String ref : this.references) {
					xml += "<field name=\"reference\">" + ref + "</field>";
				}
			xml += "</add>";
		xml += "</doc>";
		return xml;
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
}
