package org.un.dm.oict.gsd.odsreferencebuilder;

public class Builder {
	
	public static void main(String args[]) {
		// Read each XML File
		// Extract the data from the existing XML File
		// Cleanse the data
			// Remove ? from particular fields
			// Empty titles
			// Dates in the future
			// Check body contains relevant text
				// If bodies text is insufficient call Tika/OCR server and obtain new body
			// Check field size is integer
				// If its not an integer set to zero
		// Extract references for all Symbol variations - to obtain that list
		// Write out new XML Solr File
		// Write out new XML Reference File
		// Store in Database
		// Optional Func - Write the document to Solr
	}
}