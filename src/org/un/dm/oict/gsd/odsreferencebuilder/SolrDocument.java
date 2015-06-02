package org.un.dm.oict.gsd.odsreferencebuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.un.dm.oict.gsd.odsreferencebuilder.OutputDatabaseMSSQL.InfoType;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This class represents a Solr Document
 * @version 1.0
 * @codeReviewer
 */
public class SolrDocument {
	
	private String id;
	private String symbol;
	private String agenda1;
	private String agenda2;
	private String agenda3;
	private String session1;
	private String session2;
	private String session3;
	private String alternativeSymbols;
	private String title;
	private String docType;
	private String size;
	private String urlJob;
	private String subjects;
	private String publicationDate;
	private String url;
	private String languageCode;
	private String body;
	private String pdfContentLength;
	private String pdfContentType;
	private String pdfCreationDate;
	private String pdfLastModified;
	private String pdfLastSaveDate;
	private String pdfCreated;
	private String pdfDate;
	private String pdfDCTermsCreated;
	private String pdfDCTermsModified;
	private String pdfMetaCreationDate;
	private String pdfMetaSaveDate;
	private String pdfModified;
	private String pdfProducer;
	private String pdfXMPCreatorTool;
	private String pdfXMPTpgNPages;
	private String dateCreated;
	
	private String filename;
	
	private boolean requiresNewBody;
	
	
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
							if (field.getName().toLowerCase().equals("url")) {
								xml += "<field name=\"" + field.getName() + "\">"
								+ Helper.makeXMLTextSafeUrl(field.get(this).toString()) + "</field>\n";
							} else if (field.getName().toLowerCase().contains("body")) {
								xml += "<field name=\"" + field.getName() + "\">" 
								+ Helper.makeXMLTextSafe(field.get(this).toString()) + "</field>\n";
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
			return AppProp.documentOutputFolder + "/" + fn + "/" + tf; 
		} catch (Exception e) {
			Helper.logMessage(InfoType.Error, e.getMessage());
			return "";
		}
	}
	
	public String buildNewBodyFilename() {
		try {
			String tf = this.filename.substring(this.filename.lastIndexOf("/")+1, this.filename.length());
			String newfilename = tf.substring(0, tf.indexOf(".xml")) + "_modified.xml";
			return AppProp.bodyOutputDirectory + newfilename;
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

	public String getAgenda1() {
		return agenda1;
	}

	public void setAgenda1(String agenda1) {
		this.agenda1 = agenda1;
	}

	public String getAgenda2() {
		return agenda2;
	}

	public void setAgenda2(String agenda2) {
		this.agenda2 = agenda2;
	}

	public String getAgenda3() {
		return agenda3;
	}

	public void setAgenda3(String agenda3) {
		this.agenda3 = agenda3;
	}

	public String getSession1() {
		return session1;
	}

	public void setSession1(String session1) {
		this.session1 = session1;
	}

	public String getSession2() {
		return session2;
	}

	public void setSession2(String session2) {
		this.session2 = session2;
	}

	public String getSession3() {
		return session3;
	}

	public void setSession3(String session3) {
		this.session3 = session3;
	}

	public String getAlternativeSymbols() {
		return alternativeSymbols;
	}

	public void setAlternativeSymbols(String alternativeSymbols) {
		this.alternativeSymbols = alternativeSymbols;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUrlJob() {
		return urlJob;
	}

	public void setUrlJob(String urlJob) {
		this.urlJob = urlJob;
	}

	public String getSubjects() {
		return subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getPdfContentLength() {
		return pdfContentLength;
	}

	public void setPdfContentLength(String pdfContentLength) {
		this.pdfContentLength = pdfContentLength;
	}

	public String getPdfContentType() {
		return pdfContentType;
	}

	public void setPdfContentType(String pdfContentType) {
		this.pdfContentType = pdfContentType;
	}

	public String getPdfCreationDate() {
		return pdfCreationDate;
	}

	public void setPdfCreationDate(String pdfCreationDate) {
		this.pdfCreationDate = pdfCreationDate;
	}

	public String getPdfLastModified() {
		return pdfLastModified;
	}

	public void setPdfLastModified(String pdfLastModified) {
		this.pdfLastModified = pdfLastModified;
	}

	public String getPdfLastSaveDate() {
		return pdfLastSaveDate;
	}

	public void setPdfLastSaveDate(String pdfLastSaveDate) {
		this.pdfLastSaveDate = pdfLastSaveDate;
	}

	public String getPdfCreated() {
		return pdfCreated;
	}

	public void setPdfCreated(String pdfCreated) {
		this.pdfCreated = pdfCreated;
	}

	public String getPdfDate() {
		return pdfDate;
	}

	public void setPdfDate(String pdfDate) {
		this.pdfDate = pdfDate;
	}

	public String getPdfDCTermsCreated() {
		return pdfDCTermsCreated;
	}

	public void setPdfDCTermsCreated(String pdfDCTermsCreated) {
		this.pdfDCTermsCreated = pdfDCTermsCreated;
	}

	public String getPdfDCTermsModified() {
		return pdfDCTermsModified;
	}

	public void setPdfDCTermsModified(String pdfDCTermsModified) {
		this.pdfDCTermsModified = pdfDCTermsModified;
	}

	public String getPdfMetaCreationDate() {
		return pdfMetaCreationDate;
	}

	public void setPdfMetaCreationDate(String pdfMetaCreationDate) {
		this.pdfMetaCreationDate = pdfMetaCreationDate;
	}

	public String getPdfMetaSaveDate() {
		return pdfMetaSaveDate;
	}

	public void setPdfMetaSaveDate(String pdfMetaSaveDate) {
		this.pdfMetaSaveDate = pdfMetaSaveDate;
	}

	public String getPdfModified() {
		return pdfModified;
	}

	public void setPdfModified(String pdfModified) {
		this.pdfModified = pdfModified;
	}

	public String getPdfProducer() {
		return pdfProducer;
	}

	public void setPdfProducer(String pdfProducer) {
		this.pdfProducer = pdfProducer;
	}

	public String getPdfXMPCreatorTool() {
		return pdfXMPCreatorTool;
	}

	public void setPdfXMPCreatorTool(String pdfXMPCreatorTool) {
		this.pdfXMPCreatorTool = pdfXMPCreatorTool;
	}

	public String getPdfXMPTpgNPages() {
		return pdfXMPTpgNPages;
	}

	public void setPdfXMPTpgNPages(String pdfXMPTpgNPages) {
		this.pdfXMPTpgNPages = pdfXMPTpgNPages;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public boolean getRequiresNewBody() {
		return requiresNewBody;
	}

	public void setRequiresNewBody(boolean requiresNewBody) {
		this.requiresNewBody = requiresNewBody;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
