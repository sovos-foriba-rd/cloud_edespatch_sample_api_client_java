package com.foriba.eDespatch;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



public class UBLDigestValueParser {

	private final Map<String, String> uuidDigestMap = new HashMap<String, String>();

	private static SAXParserFactory factory = null;
	static {
		initializeSAXParser();
	}

	private static void initializeSAXParser() {
		if(factory == null) {
			factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
		}
	}

	private UBLDigestValueParser() {}

	public static UBLDigestValueParser newInstance(byte[] binaryData) throws Exception {

		UBLDigestValueParser parser = new UBLDigestValueParser();

		if(binaryData == null) {
			throw new IllegalArgumentException("Binary data null gönderilemez");
		}
		parser.calculateDigestValue(binaryData);

		return parser;
	}

	public String getDigestValueByUUID(String invUUID) {
		String hash = uuidDigestMap.get(invUUID);
		return hash;
	}

	public Map<String, String> getDigestValueMap() {
		return uuidDigestMap;
	}

	private void calculateDigestValue(byte[] binaryData) throws Exception {

		initializeSAXParser();
		SAXParser saxParser = factory.newSAXParser();

		DefaultHandler handler = new DefaultHandler() {

			boolean invUUID = false;
			boolean invDigest = false;
			String uriFlag = null;
			String nodeFlag = null;

			public List<String> listUUID;
			public List<String> listDIGEST;

			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

				if(localName.equalsIgnoreCase("Invoice")) {
					nodeFlag = localName;
					listUUID = new ArrayList<String>();
					listDIGEST = new ArrayList<String>();
				}
				else if(nodeFlag != null && nodeFlag.equalsIgnoreCase("Invoice") && localName.equalsIgnoreCase("UUID")) {
					invUUID = true;
					nodeFlag = null; // Fatura içerisinde alınan son deger UUID, alınan son degerden sonra null ataması yapılır.
				}
				else if(nodeFlag != null && nodeFlag.equalsIgnoreCase("Invoice") && localName.equalsIgnoreCase("Reference")) {
					uriFlag = attributes.getValue("URI");
				}
				else if(nodeFlag != null && uriFlag != null && uriFlag.equalsIgnoreCase("") && localName.equalsIgnoreCase("DigestValue")) {
					invDigest = true;
					uriFlag = null;
				}
			}

			@Override
			public void endElement(String uri, String localName, String qName) throws SAXException {

				if(localName.equalsIgnoreCase("Invoice")) {
					if(!(listUUID.isEmpty() || listDIGEST.isEmpty())) {
						uuidDigestMap.put(listUUID.get(0), listDIGEST.get(0));
					}
				}
			}

			@Override
			public void characters(char ch[], int start, int length) throws SAXException {

				if(invUUID) {
					listUUID.add(new String(ch, start, length).trim());
					invUUID = false;
				}
				if(invDigest) {
					listDIGEST.add(new String(ch, start, length).trim());
					invDigest = false;
				}
			}
		};

		InputStream in = null;
		Reader reader = null;
		try {
			in = new ByteArrayInputStream(binaryData);
			reader = new InputStreamReader(in, "UTF-8");

			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");

			saxParser.parse(is, handler);
		}
		finally {
			UtilsIO.closeStream(reader, in);
		}
	}
}
