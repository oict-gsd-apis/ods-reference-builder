package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.ArrayList;
import java.util.List;

/* 
* Author: Kevin Thomas Bradley
* Date: 1-May-2015
* Description: This class represents a ODS Reference Explorer Document
* Version: 1.0
* Code Reviewer:
*/
public class ReferenceDocument {

	protected String id;
	protected String symbol;
	protected String title;
	protected String publicationDate;
	protected String url;
	protected String languageCode;
	protected String dateCreated;
	protected List<String> references;
	
	public ReferenceDocument() {
		this.references = new ArrayList<String>();
	}
	
	@Override
	public String toString() {
		return "ReferenceDocument [id=" + id + ", symbol=" + symbol
				+ ", title=" + title + ", publicationDate=" + publicationDate
				+ ", url=" + url + ", languageCode=" + languageCode
				+ ", dateCreated=" + dateCreated + "]";
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
