package com.foriba.eDespatch;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class UBLNamespacePrefixMapper extends NamespacePrefixMapper {

	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
		// System.out.println("PREFIX: " + namespaceUri + " SUGGEST: " + suggestion + " required: " + requirePrefix);
		if (namespaceUri.equals("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2")) {
			return "";
		} else if (namespaceUri.equals("urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2")) {
			return "";
		} else if (namespaceUri.equals("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2")) {
			return "";
		} else if (namespaceUri.equals("urn:oasis:names:specification:ubl:schema:xsd:DespatchAdvice-2")) {
			return "";
		} else if (namespaceUri.equals("urn:oasis:names:specification:ubl:schema:xsd:ReceiptAdvice-2")) {
			return "";
		} else if (namespaceUri.equals("http://www.hr-xml.org/3")) {
			return "";
		}
		return suggestion;
	}
}
