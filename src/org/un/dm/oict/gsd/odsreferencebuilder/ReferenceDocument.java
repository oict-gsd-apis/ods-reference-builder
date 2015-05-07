package org.un.dm.oict.gsd.odsreferencebuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
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
	private Map<String,Integer> references;
	
	private String filename;
	
	/**
	 * 
	 */
	public ReferenceDocument() {
		this.references = new HashMap<String, Integer>();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	/**
	 * This overridden method is used to construct the Solr XML for the document
	 * used to populate the Solr index
	 */
	public String toString() {
		try {
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><add>\n<doc>\n";
			for (Field field : this.getClass().getDeclaredFields()) {
				if (field.get(this) != null) {
					if (field.getType().isAssignableFrom(Map.class)) {
						Map<String, Integer> vals = ((Map<String, Integer>) field.get(this));
						for (Map.Entry<String, Integer> kv : vals.entrySet()) {
							if (kv.getValue() != 0)
								xml += "<field name=\"reference\">"// + kv.getKey() + "\">"
										+ Helper.makeXMLTextSafeField(  kv.getKey() + " [" + kv.getValue().toString() + "]" ) + "</field>\n";
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
							if (field.getName().toLowerCase().equals("url")) {
								xml += "<field name=\"" + field.getName() + "\">"
								+ Helper.makeXMLTextSafeUrl(field.get(this).toString()) + "</field>\n";
							} else {
								xml += "<field name=\"" + field.getName() + "\">"
								+ Helper.makeXMLTextSafeField(field.get(this).toString()) + "</field>\n";
							}
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
			String cP = this.filename.substring(0,this.filename.lastIndexOf("/"));
			String fn = this.filename.substring( cP.lastIndexOf("/")+1, cP.length());
			return AppProp.referenceOutputFolder + "/" + fn + "/" + tf; 
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
	public Map<String, Integer> getReferences() {
		return references;
	}
	public void setReferences(Map<String, Integer> references) {
		this.references = references;
	}
	public void addReference(String reference, int count) {
		this.references.put(reference, count);
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
