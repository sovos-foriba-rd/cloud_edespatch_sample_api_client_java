package com.foriba.eDespatch;

public class ParamServices {
	
	public static final boolean WS_CL_SOAP_OUTPUT = false;

	
	public enum Direction {
		OUTBOUND,
		INBOUND
	}
	
	public enum UserRole {
		GB,
		PK,
		ANY
		//BOTH
	}

	
	public enum ViewDocType {
		XSLT,
		HTML,
		PDF,
		HTML_DEFAULT,
		PDF_DEFAULT
	}
	
	public enum UBLDocumentType {
		ENVELOPE,
		INVOICE,
		APP_RESP,
		SYS_RESP,
		CREDITNOTE
	}
	
	public enum UBLDesDocumentType {
		ENVELOPE,
		DESPATCH,
		RECEIPT,
		SYS_RESP
	}
	
	public enum Parameters {
		ZIP,
		DOC_DATA,
	}
	
	public enum UploadInvoiceListDocType {
		XML
	}
	
	public enum UploadDocumentDocType {
		UBL,
		UBL_INV
	}
	
}
