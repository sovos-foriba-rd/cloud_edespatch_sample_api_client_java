package com.foriba.eDespatch;


public class ConstantsUBL {

	public static final String	UBL_ENV_SCHEMA_LOCATION = "http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader PackageProxy_1_2.xsd";
	public static final String	UBL_INV_SCHEMA_LOCATION = "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2 ..\\xsdrt\\maindoc\\UBL-Invoice-2.1.xsd";
	public static final String	UBL_AR_SCHEMA_LOCATION = "urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2 ..\\xsdrt\\maindoc\\UBL-ApplicationResponse-2.1.xsd";
	public static final String	UBL_CN_SCHEMA_LOCATION = "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2 ..\\xsdrt\\maindoc\\UBL-CreditNote-2.1.xsd";
	public static final String	UBL_DA_SCHEMA_LOCATION = "urn:oasis:names:specification:ubl:schema:xsd:DespatchAdvice-2 ..\\xsdrt\\maindoc\\UBL-DespatchAdvice-2.1.xsd";
	public static final String	UBL_RA_SCHEMA_LOCATION = "urn:oasis:names:specification:ubl:schema:xsd:ReceiptAdvice-2 ..\\xsdrt\\maindoc\\UBL-ReceiptAdvice-2.1.xsd";
	
	public static final String	SEND_DOC_SUCCESS_MSG = "Doküman başarıyla işlendi";
	public static final int 	DOCUMENT_FILENAME_LENGTH = 40;
	public static final int 	DOCUMENT_HASH_LENGTH = 32;
	public static final String 	DOCUMENT_FILENAME_POSTFIX = ".zip";
	
	public static final int 	ENVELOPE_UUID_LENGTH = 36;
	public static final String	ENVELOPE_HEADER_VERSION = "1.2";
	public static final String 	ENVELOPE_IDENTIFIER_DEFAULT_SENDER = "defaultgb";
	public static final String 	ENVELOPE_IDENTIFIER_DEFAULT_RECEIVER = "defaultpk";
	public static final String 	ENVELOPE_PARTNER_CTI_VKNTCKN = "VKN_TCKN";
	
	public static final String	ENVELOPE_HEADER_DI_STANDARD = "UBL-TR";
	public static final String	ENVELOPE_HEADER_DI_TYPE_VERSION = "1.2";
	public static final String	UBL_VERSION_ID = "2.1";
	public static final String	UBL_CUSTOMIZATION_ID = "TR1.2";
	
	public static final String	SR_PROFILE_ID = "UBL-TR-PROFILE-1";
	public static final String	SR_RESPONSE_CODE = "S_APR";
	public static final String	SR_RESPONSE_DESC = "SystemApplicationResponse";
	
	//XSLT
	public static final String	XSLT_ID = "1";
	public static final String	XSLT_DOCTYPE = "XSLT";
	public static final String	XSLT_CHARSET = "UTF-8";
	public static final String	XSLT_ENCODING = "Base64";
	public static final String	XSLT_FILENAME_POSTFIX = ".xslt";
	public static final String	XSL_FILENAME_POSTFIX = ".xsl";
	public static final String	XSLT_MIME = "application/xml";
	
	//XML
	public static final String	XML_FILENAME_POSTFIX = ".XML";
	
	//VKN-TCKN-IDENTIFIER
	public static final int VKN_LENGTH = 10;
	public static final int TCKN_LENGTH = 11;
	public static final int ALIAS_MAX_LENGTH = 160;
	
	
	public enum ENVELOPE_TYPES {
		SENDERENVELOPE,
		POSTBOXENVELOPE,
		SYSTEMENVELOPE,
		USERENVELOPE
	}
	
	public enum ENVELOPE_ELEMENT_TYPES {
		INVOICE,
		APPLICATIONRESPONSE,
		CREDITNOTE,
		DESPATCHADVICE,
		RECEIPTADVICE,
		PROCESSUSERACCOUNT,
		CANCELUSERACCOUNT
	}
	
	public enum PIT_VKNTCKN {
		VKN,
		TCKN
	}
	
	public enum PIT_IHRACAT {
		GTB_REFNO,
		GTB_GCB_TESCILNO,
		GTB_FIILI_IHRACAT_TARIHI
	}
	
	public enum INV_PROFILE_ID {
		TEMELFATURA,
		TICARIFATURA,
		IHRACAT,
		YOLCUBERABERFATURA
	}
	
	public enum INV_TYPES {
		SATIS,
		IADE,
		TEVKIFAT,
		ISTISNA,
		ARACTESCIL,
		OZELMATRAH,
		IHRACKAYITLI
	}
	
	public enum DES_PROFILE_ID {
		TEMELIRSALIYE,
		TICARIIRSALIYE
	}
	
	public enum DES_TYPES {
		SEVK
	}
	
	public enum AR_RESPONSE_TYPES {
		
		KABUL(1, "Fatura kabul edildi"),
		RED(2, "Fatura reddedildi"),
		IADE(3, "Fatura iade edildi"),
		GUMRUKONAY(4, "Fatura iade edildi");
		
		private int code;
		private String description;
		
		private AR_RESPONSE_TYPES(int code, String description) {
			this.code = code;
			this.setDescription(description);
		}
		
		public static AR_RESPONSE_TYPES getARResponseTypeByCode(int code) {
			switch(code) {
				case 1: return KABUL;
				case 2: return RED;
				case 3: return IADE;
			}
			throw new IllegalArgumentException("Kod bulunamadı");
		}
		
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}

		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}	
	}
	
	public enum USER_ROLE_TYPES {
		GB("Gönderici Birim"),
		PK("Posta Kutusu");
		private String description;
		private USER_ROLE_TYPES(String description) {
			this.setDescription(description);
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
	}
	
	public enum USER_OPTION_CODES {
		
		KAMU("1", 1),
		ÖZEL("2", 2),
		KAMU_SAKLAMA("11", 11), //eFatura Saklama Kamu
		ÖZEL_SAKLAMA("12", 12), //eFatura Saklama Özel
		
		A_KAMU("A1", 21), //eArşiv Kamu
		A_ÖZEL("A2", 22), //eArşiv Özel
		
		T_KAMU("T1", 41), //eBilet Kamu
		T_ÖZEL("T2", 42), //eBilet Özel
		
		D_KAMU("D1", 51), //eİrsaliye Kamu
		D_ÖZEL("D2", 52), //eİrsaliye Özel
		
		S_KAMU("S1", 71), //eSMM Kamu
		S_ÖZEL("S2", 72), //eSMM Özel
		
		RO_KAMU("R1", 111), //e-Mali Rapor Eski Kamu
		RO_ÖZEL("R2", 112), //e-Mali Rapor Eski Özel
		
		RN_KAMU("R5", 121), //e-Mali Rapor Yeni Kamu
		RN_ÖZEL("R6", 122); //e-Mali Rapor Yeni Özel
		
		private String code;
		private int ublCode;
		private USER_OPTION_CODES(String code, int ublCode) {
			this.setCode(code);
			this.setUBLCode(ublCode);
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getCode() {
			return code;
		}
		public void setUBLCode(int ublCode) {
			this.ublCode = ublCode;
		}
		public int getUBLCode() {
			return ublCode;
		}
		public static USER_OPTION_CODES getUserOptionByCode(String code) {
			USER_OPTION_CODES optCode = null;
			for(USER_OPTION_CODES currCode : USER_OPTION_CODES.values()) {
				if(currCode.getCode().equals(code)) {
					optCode = currCode;
				}
			}
			if(optCode == null) {
				throw new IllegalArgumentException("Kod bulunamadı: " + code);
			}
			return optCode;
		}
		public static USER_OPTION_CODES getUserOptionByUBLCode(int ublCode) {
			USER_OPTION_CODES optCode = null;
			for(USER_OPTION_CODES currCode : USER_OPTION_CODES.values()) {
				if(currCode.getUBLCode() ==  ublCode) {
					optCode = currCode;
				}
			}
			if(optCode == null) {
				throw new IllegalArgumentException("UBL kod bulunamadı: " + ublCode);
			}
			return optCode;
		}
	}
}
