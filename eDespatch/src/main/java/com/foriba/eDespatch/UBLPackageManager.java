package com.foriba.eDespatch;


import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.foriba.eDespatch.UtilsIO.ZipFile;



public class UBLPackageManager {
	
	public static String extractSBDXMLFromDocument(InputStream is) throws Exception {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(is);
		
		NodeList nl = doc.getElementsByTagName("binaryData");
		Node n = nl.item(0);
		String base64Content = n.getTextContent();
		
		byte[] zippedBinaryData = new Base64().decode(base64Content);
		List<ZipFile> zipFiles = UtilsIO.unzip(zippedBinaryData);
		byte[] data = zipFiles.get(0).data;
		
		return new String(data);

	}
	
	public static byte[] packageSBDXMLasBinaryData(String envUUID, byte[] ublSBDData) throws Exception {
		//Zip data
		byte[] zippedData = null;
		try {
			UtilsIO.ZipFile zFile = new UtilsIO.ZipFile();
			zFile.name = envUUID + ".xml";
			zFile.data = ublSBDData;
			zippedData = UtilsIO.zip(zFile);
		}
		catch(Exception e) {
			throw new IllegalArgumentException("Zip oluşturulamadı: " + e.getMessage());
		}
		return zippedData;
		
	}
	
	

}
