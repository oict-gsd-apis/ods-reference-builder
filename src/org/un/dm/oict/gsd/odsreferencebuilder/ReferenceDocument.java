package org.un.dm.oict.gsd.odsreferencebuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	@SuppressWarnings("unchecked")
	/**
	 * This overridden method is used to construct the Solr XML for the document
	 * used to populate the Solr index
	 */
	public String toString() {
		try {
			String xml = "<add>\n<doc>\n";
			for (Field field : this.getClass().getDeclaredFields()) {
				if (field.get(this) != null) {
					if (field.getType().isAssignableFrom(Map.class)) {
						Map<String, String> vals = ((Map<String, String>) field.get(this));
						for (Map.Entry<String, String> kv : vals.entrySet()) {
							if (!kv.getValue().isEmpty())
								xml += "<field name=\"" + kv.getKey() + "\">"
										+ Helper.makeXMLTextSafeField(kv.getValue()) + "</field>\n";
						}
					}else if (field.getType().isAssignableFrom(List.class)) {
						List<String> vals = ((ArrayList<String>) field.get(this));
						for (String kv : vals) {
							if (!kv.isEmpty())
								xml += "<field name=\"" + field.getName() + "\">"
										+ Helper.makeXMLTextSafeField(kv) + "</field>\n";
						}
					} else {
						if (!field.get(this).equals("") && !field.get(this).equals("<![CDATA[]]>")) {
							xml += "<field name=\"" + field.getName() + "\">"
								+ field.get(this) + "</field>\n";
						}
					}
				}
			}
			xml += "</doc>\n</add>\n";
			return xml;
		} catch (Exception e) {
			return "";
		}
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
