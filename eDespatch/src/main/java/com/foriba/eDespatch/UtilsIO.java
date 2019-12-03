package com.foriba.eDespatch;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;



@SuppressWarnings("unused")
public class UtilsIO {

	private static final int BUFFER_SIZE = 1024;

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public static String readStringFromInputStream(InputStream is) throws IOException {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(is, ParamRuntime.UTF8_CHARSET);
			br = new BufferedReader(isr);
			String line = br.readLine();
			while(line != null) {
				sb.append(line);
				line = br.readLine();
				if(line != null) {
					sb.append(LINE_SEPARATOR);
				}
			}
		}
		finally {
			UtilsIO.closeStream(br);
		}
		return sb.toString();
	}

	public static byte[] readBytesFromInputStream(InputStream is) throws IOException {
		BufferedInputStream bis = null;
		byte[] data = null;
		try {
			bis = new BufferedInputStream(is);
			data = new byte[is.available()];
			bis.read(data);
		}
		finally {
			UtilsIO.closeStream(bis);
		}
		return data;
	}
	
	public static class ZipFile {
		public String name;
		public byte[] data;
	}

	public static byte[] zip(ZipFile zipFile) throws IOException {

		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		ZipOutputStream zos = null;
		try {
			bais = new ByteArrayInputStream(zipFile.data);
			baos = new ByteArrayOutputStream();
			zos = new ZipOutputStream(baos);

			zos.putNextEntry(new ZipEntry(zipFile.name));
			byte[] buffer = new byte[BUFFER_SIZE];
			int length = 0;
			while((length = bais.read(buffer)) > 0) {
				zos.write(buffer, 0, length);
			}
			zos.closeEntry();
			zos.finish();

			return baos.toByteArray();
		}
		finally {
			UtilsIO.closeStream(bais, zos, baos);
		}
	}

	public static List<ZipFile> unzip(byte[] data) throws Exception {

		ZipInputStream zis = null;
		ByteArrayOutputStream baos = null;
		try {
			zis = new ZipInputStream(new ByteArrayInputStream(data));
			List<ZipFile> zipFiles = new ArrayList<ZipFile>();
			ZipEntry entry = zis.getNextEntry();

			while(entry != null) {
				try {
					byte[] buffer = new byte[BUFFER_SIZE];
					int length = 0;
					baos = new ByteArrayOutputStream();
					while((length = zis.read(buffer)) > 0) {
						baos.write(buffer, 0, length);
					}
					baos.flush();
					ZipFile zipFile = new ZipFile();
					zipFile.name = entry.getName();
					if(baos.toByteArray() != null) {
						zipFile.data = baos.toByteArray();
					}
					zipFiles.add(zipFile);

					entry = zis.getNextEntry();
				}
				finally {
					UtilsIO.closeStream(baos);
				}
			}
			return zipFiles;
		}
		finally {
			UtilsIO.closeStream(zis);
		}
	}
	
	public static byte[] zipSingleXML(byte[] data, String uuid) throws Exception {
		byte[] binaryData;
		
		ZipFile zipFile = new ZipFile();
		zipFile.data = data;
		zipFile.name = uuid + ".xml";
		
		binaryData = UtilsIO.zip(zipFile);
		return binaryData;
	}
	
	public static byte[] unzipSingleXML(byte[] data) throws Exception {
		List<ZipFile> zipFileList = null;
		
		zipFileList = unzip(data);
		if (zipFileList.size() != 1) {
			throw new Exception("Zip içerisinde bir adet xml dosya bulunmalıdır");
		}
		return zipFileList.get(0).data;
	}

	public static String calculateMD5Checksum(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(data, 0, data.length);
		BigInteger bi = new BigInteger(1, md.digest());
		return String.format("%1$032X", bi);
	}

	public static String calculateSHA256(byte[] value) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(value);
		byte byteData[] = md.digest();
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	public static void writeToFile(String fileName, byte[] data) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			fos.write(data);
			fos.flush();
		}
		finally {
			UtilsIO.closeStream(fos);
		}
	}

	// http://en.wikipedia.org/wiki/Byte_order_mark
	public static byte[] escapeUTF8BOM(byte[] data) {
		if(hasBOMBytes1(data)) {
			return Arrays.copyOfRange(data, 3, data.length);
		}
		return data;
	}

	private static boolean hasBOMBytes1(byte[] data) {
		if(data.length >= 3 && (
			(data[0] & 0xFF) == 0xEF &&
			(data[1] & 0xFF) == 0xBB &&
			(data[2] & 0xFF) == 0xBF)
			) {
			return true;
		}
		return false;
	}

	private static boolean hasBOMBytes2(byte[] data) {
		return data.length >= 3 && (data[0] == -17 && data[1] == -69 && data[2] == -65);
	}

	public static boolean isValidUTF8(byte[] input) {
		CharsetDecoder cs = Charset.forName("UTF-8").newDecoder();
		try {
			cs.decode(ByteBuffer.wrap(input));
			return true;
		}
		catch (CharacterCodingException e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static String formatStackTrace(Class c, Exception e) {

		StringWriter sw = null;
		PrintWriter pw = null;

		try {
			sw = new StringWriter();
			sw.append("FIT eInvoice Core Engine Exception in class " + c.getName());
			sw.append("\n\nStack Trace:\n");

			pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			int i = 0;
			Throwable t = e.getCause();
			while(t != null) {
				sw.append("\n\nCause Stack Trace[" + i + "]:\n");
				t.printStackTrace(pw);
				t = t.getCause();
				i++;
			}

			return sw.toString();
		}
		finally {
			UtilsIO.closeStream(sw, pw);
		}
	}

	public static void closeStream(Closeable... c) {
		for(Closeable c1 : c) {
			try {
				if(c1 != null) {
					c1.close();
				}
			} 
			catch (Exception e) {
				UtilsIO.handleTrivialException(UtilsIO.class, e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void handleTrivialException(Class c, Exception e) {
		// do nothin'
		return;
	}

}
