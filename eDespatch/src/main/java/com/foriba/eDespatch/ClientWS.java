package com.foriba.eDespatch;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Properties;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;

import org.apache.commons.codec.binary.Base64;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocument;

import com.foriba.eDespatch.ParamServices.Direction;
import com.foriba.eDespatch.ParamServices.UBLDesDocumentType;
import com.foriba.eDespatch.ParamServices.UBLDocumentType;
import com.foriba.eDespatch.ParamServices.UserRole;
import com.foriba.eDespatch.ParamServices.ViewDocType;
import com.foriba.eDespatch.test.Application;
import com.foriba.edespatch.ClientEDespatchServices;
import com.foriba.edespatch.ClientEDespatchServicesPort;
import com.foriba.edespatch.GetDesEnvelopeStatusFaultMessage;
import com.foriba.edespatch.GetDesEnvelopeStatusRequest;
import com.foriba.edespatch.GetDesEnvelopeStatusResponse;
import com.foriba.edespatch.GetDesEnvelopeStatusResponseType;
import com.foriba.edespatch.GetDesReceiptsFaultMessage;
import com.foriba.edespatch.GetDesReceiptsRequest;
import com.foriba.edespatch.GetDesReceiptsResponse;
import com.foriba.edespatch.GetDesReceiptsResponseReceiptsType;
import com.foriba.edespatch.GetDesReceiptsResponseType;
import com.foriba.edespatch.GetDesUBLFaultMessage;
import com.foriba.edespatch.GetDesUBLListFaultMessage;
import com.foriba.edespatch.GetDesUBLListRequest;
import com.foriba.edespatch.GetDesUBLListResponse;
import com.foriba.edespatch.GetDesUBLRequest;
import com.foriba.edespatch.GetDesUBLResponse;
import com.foriba.edespatch.GetDesUserListFaultMessage;
import com.foriba.edespatch.GetDesUserListRequest;
import com.foriba.edespatch.GetDesUserListResponse;
import com.foriba.edespatch.GetDesViewFaultMessage;
import com.foriba.edespatch.GetDesViewRequest;
import com.foriba.edespatch.GetDesViewRequestType;
import com.foriba.edespatch.GetDesViewResponse;
import com.foriba.edespatch.GetDesViewResponseType;
import com.foriba.edespatch.SendDesUBLFaultMessage;
import com.foriba.edespatch.SendDesUBLRequest;
import com.foriba.edespatch.SendDesUBLResponse;
import com.foriba.edespatch.SendDesUBLResponseType;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.UUIDType;
import oasis.names.specification.ubl.schema.xsd.despatchadvice_2.DespatchAdviceType;
import oasis.names.specification.ubl.schema.xsd.receiptadvice_2.ReceiptAdviceType;

public class ClientWS {
	private static int count = 1;

	private static int childCount = 1;

	private static boolean ID_AUTO_GENERATE = true;

	private static String userNameProp;
	private static String passwordProp;
	private static String endPointProp;

	static {
		try {
			FileReader reader = new FileReader("src\\main\\resources\\user.properties");
			Properties p = new Properties();
			p.load(reader);
			userNameProp = p.getProperty("username");
			passwordProp = p.getProperty("password");
			endPointProp = p.getProperty("endPoint");
		} catch (Exception e) {
		}
	}

	/**
	 * @param identifier İrsaliyenin göndericisine/alıcısına ait ait Gönderici
	 *                   Birim(GB) ya da Posta Kutusu(PK) etiketi
	 * @param vkntckn    İrsaliyenin göndericisine/alıcısına ait VKN/TCKN
	 * @param docRefUUID İrsaliye ETTN
	 * @param type       Gelen/Gönderilen Belge (OUTBOUND, INBOUND)
	 * @throws IOException
	 */
	public static void getDesReceipts(String identifier, String vkntckn, String docRefUUID, Direction type)
			throws IOException {

		try {
			ClientEDespatchServices clientWS = new ClientEDespatchServices();
			ClientEDespatchServicesPort port = clientWS.getClientEDespatchServicesPort();
			BindingProvider provider = (BindingProvider) port;
			Map<String, Object> reqContext = provider.getRequestContext();
			reqContext.put(BindingProvider.USERNAME_PROPERTY, userNameProp);
			reqContext.put(BindingProvider.PASSWORD_PROPERTY, passwordProp);
			reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointProp);

			long avg = 0;
			for (int i = 1; i <= count; i++) {

				GetDesReceiptsRequest req = new GetDesReceiptsRequest();
				req.setIdentifier(identifier);
				req.setVKNTCKN(vkntckn);
				req.getUUID().add(docRefUUID);
				req.setType(type.name());

				long duration = 0;
				try {
					long start = System.currentTimeMillis();
					GetDesReceiptsResponse resp = port.getDesReceipts(req);
					for (GetDesReceiptsResponseType iterable_element : resp.getResponse()) {
						for (GetDesReceiptsResponseReceiptsType iterable_element2 : iterable_element.getReceipts()) {
							iterable_element2.getEnvUUID();
							String docData=new String(Base64.encodeBase64(iterable_element2.getDocData()));
							System.out.println("RAENVUUID:"+iterable_element2.getEnvUUID()+"\nRAUUID:"+iterable_element2.getUUID()+"\nID:"+iterable_element2.getID()+"\nInsertDateTime"+iterable_element2.getInsertDateTime()+"\nIssueDate:"+iterable_element2.getIssueDate()+"\nDocData:"+docData);
						}
					}
					duration = (System.currentTimeMillis() - start);
					avg = ((avg * (i - 1)) + duration) / i;
					System.out.println("--------------------------------------------------");
					String srSBD = new String("DespatchUUID:" + resp.getResponse().get(0).getDespatchUUID());
					System.out.println(srSBD);
					System.out.println(i + " - Duration: " + duration + ", Avg: " + avg);
				} catch (GetDesReceiptsFaultMessage fm) {
					System.out.println("FM: " + fm.getFaultInfo().getCode() + " - " + fm.getFaultInfo().getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");

	}


	

	/**
	 * @param uuid       Belge ETTN
	 * @param identifier Belgenin gönderici ya da alıcısına ait Gönderici Birim(GB)
	 *                   ya da Posta Kutusu(PK) etiketi
	 * @param vkntckn    Belgenin gönderici ya da alıcısına ait VKN/TCKN
	 * @param type       Gelen/Gönderilen Belge (OUTBOUND, INBOUND)
	 * @param doctype    Belge Türü (DESPATCH/RECEIPT)
	 * @param viewType   Doküman Türü (HTML, PDF, HTML_DEFAULT, PDF_DEFAULT)
	 */
	public static void getDesView(String identifier,String vkntckn, String uuid, Direction type,
			UBLDesDocumentType doctype, ViewDocType viewType) throws IOException {

		try {
			ClientEDespatchServices clientWS = new ClientEDespatchServices();
			ClientEDespatchServicesPort port = clientWS.getClientEDespatchServicesPort();
			BindingProvider provider = (BindingProvider) port;
			Map<String, Object> reqContext = provider.getRequestContext();
			reqContext.put(BindingProvider.USERNAME_PROPERTY, userNameProp);
			reqContext.put(BindingProvider.PASSWORD_PROPERTY, passwordProp);
			reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointProp);

			long avg = 0;
			for (int i = 1; i <= count; i++) {
				GetDesViewRequest request = new GetDesViewRequest();
				GetDesViewRequestType request1= new GetDesViewRequestType();
				request.setIdentifier(identifier);
				request.setVKNTCKN(vkntckn);
				request1.setUUID(uuid);
				request1.setType(type.name());
				request1.setDocType(doctype.name());
				request1.setViewType(viewType.name());
				request.getDocDetails().add(request1);

				long duration = 0;
				try {
					long start = System.currentTimeMillis();
					GetDesViewResponse resp = port.getDesView(request);
					duration = (System.currentTimeMillis() - start);
					avg = ((avg * (i - 1)) + duration) / i;
					for (GetDesViewResponseType iterable_element : resp.getResponse()) {
						String docData = new String(Base64.encodeBase64String(iterable_element.getDocData()));
						String str = new String("UUID:"+ iterable_element.getUUID()+"\nType:"+ iterable_element.getType()+
								"\nDocType:"+iterable_element.getDocType() + "\nViewType:"+iterable_element.getViewType()+
								"\nDocData:"+docData + "\nResult:"+ iterable_element.getResult()+
								"\nResultDescription:"+ iterable_element.getResultDescription());

						System.out.println(str);
				}
					System.out.println(i + " - Duration: " + duration + ", Avg: " + avg);
				} catch (GetDesViewFaultMessage fm) {

					System.out.println("FM: " + fm.getFaultInfo().getCode() + " - " + fm.getFaultInfo().getMessage());
				}
				
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");

	}
	
	/**
	 * @param id         Belge ID
	 * @param identifier Belgenin gönderici ya da alıcısına ait Gönderici Birim(GB)
	 *                   ya da Posta Kutusu(PK) etiketi
	 * @param vkntckn    Belgenin gönderici ya da alıcısına ait VKN/TCKN
	 * @param type       Gelen/Gönderilen Belge (OUTBOUND, INBOUND)
	 * @param doctype    Belge Türü (DESPATCH/RECEIPT)
	 * @param viewType   Doküman Türü (HTML, PDF, HTML_DEFAULT, PDF_DEFAULT)
	 */
	public static void getDesView(String identifier,String vkntckn,  Direction type,
			UBLDesDocumentType doctype, ViewDocType viewType,String id) throws IOException {

		try {
			ClientEDespatchServices clientWS = new ClientEDespatchServices();
			ClientEDespatchServicesPort port = clientWS.getClientEDespatchServicesPort();
			BindingProvider provider = (BindingProvider) port;
			Map<String, Object> reqContext = provider.getRequestContext();
			reqContext.put(BindingProvider.USERNAME_PROPERTY, userNameProp);
			reqContext.put(BindingProvider.PASSWORD_PROPERTY, passwordProp);
			reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointProp);

			long avg = 0;
			for (int i = 1; i <= count; i++) {
				GetDesViewRequest request = new GetDesViewRequest();
				GetDesViewRequestType request1= new GetDesViewRequestType();
				request.setIdentifier(identifier);
				request.setVKNTCKN(vkntckn);
				request1.setID(id);
				request1.setType(type.name());
				request1.setDocType(doctype.name());
				request1.setViewType(viewType.name());
				request.getDocDetails().add(request1);

				long duration = 0;
				try {
					long start = System.currentTimeMillis();
					GetDesViewResponse resp = port.getDesView(request);
					duration = (System.currentTimeMillis() - start);
					avg = ((avg * (i - 1)) + duration) / i;

					for (GetDesViewResponseType iterable_element : resp.getResponse()) {
						String docData = new String(Base64.encodeBase64String(iterable_element.getDocData()));
						String str = new String("UUID:"+ iterable_element.getUUID()+"\nType:"+ iterable_element.getType()+
								"\nDocType:"+iterable_element.getDocType() + "\nViewType:"+iterable_element.getViewType()+
								"\nDocData:"+docData + "\nResult:"+ iterable_element.getResult()+
								"\nResultDescription:"+ iterable_element.getResultDescription());

						System.out.println(str);
				}
					System.out.println(i + " - Duration: " + duration + ", Avg: " + avg);
				} catch (GetDesViewFaultMessage fm) {

					System.out.println("FM: " + fm.getFaultInfo().getCode() + " - " + fm.getFaultInfo().getMessage());
				}
				
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");

	}
	
	/**
	 * @param custDesID  Müşteri İrsaliye ID
	 * @param identifier Belgenin gönderici ya da alıcısına ait Gönderici Birim(GB)
	 *                   ya da Posta Kutusu(PK) etiketi
	 * @param vkntckn    Belgenin gönderici ya da alıcısına ait VKN/TCKN
	 * @param type       Gelen/Gönderilen Belge (OUTBOUND, INBOUND)
	 * @param doctype    Belge Türü (DESPATCH/RECEIPT)
	 * @param viewType   Doküman Türü (HTML, PDF, HTML_DEFAULT, PDF_DEFAULT)
	 */
	public static void getDesView(String identifier,String vkntckn,Direction type,String custDesID,
			UBLDesDocumentType doctype, ViewDocType viewType) throws IOException {

		try {
			ClientEDespatchServices clientWS = new ClientEDespatchServices();
			ClientEDespatchServicesPort port = clientWS.getClientEDespatchServicesPort();
			BindingProvider provider = (BindingProvider) port;
			Map<String, Object> reqContext = provider.getRequestContext();
			reqContext.put(BindingProvider.USERNAME_PROPERTY, userNameProp);
			reqContext.put(BindingProvider.PASSWORD_PROPERTY, passwordProp);
			reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointProp);

			long avg = 0;
			for (int i = 1; i <= count; i++) {
				GetDesViewRequest request = new GetDesViewRequest();
				GetDesViewRequestType request1= new GetDesViewRequestType();
				request.setIdentifier(identifier);
				request.setVKNTCKN(vkntckn);
				request1.setCustDesID(custDesID);
				request1.setType(type.name());
				request1.setDocType(doctype.name());
				request1.setViewType(viewType.name());
				request.getDocDetails().add(request1);

				long duration = 0;
				try {
					long start = System.currentTimeMillis();
					GetDesViewResponse resp = port.getDesView(request);
					duration = (System.currentTimeMillis() - start);
					avg = ((avg * (i - 1)) + duration) / i;
					for (GetDesViewResponseType iterable_element : resp.getResponse()) {
						String docData = new String(Base64.encodeBase64String(iterable_element.getDocData()));
						String str = new String("UUID:"+ iterable_element.getUUID()+"\nType:"+ iterable_element.getType()+
								"\nDocType:"+iterable_element.getDocType() + "\nViewType:"+iterable_element.getViewType()+
								"\nDocData:"+docData + "\nResult:"+ iterable_element.getResult()+
								"\nResultDescription:"+ iterable_element.getResultDescription());
						System.out.println(str);
				}
					System.out.println(i + " - Duration: " + duration + ", Avg: " + avg);
				} catch (GetDesViewFaultMessage fm) {

					System.out.println("FM: " + fm.getFaultInfo().getCode() + " - " + fm.getFaultInfo().getMessage());
				}
				
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");

	}

	/**
	 * @param identifier Gönderici Birim(GB) ya da Posta Kutusu(PK) etiketi
	 * @param vkntckn    VKN/TCKN bilgisi
	 * @param role       Kullanıcı Tipi (GB/PK)
	 * @throws IOException
	 */

	public static void getDesUserList(String vkntckn, String identifier, UserRole role) throws IOException {

		try {
			ClientEDespatchServices clientWS = new ClientEDespatchServices();
			ClientEDespatchServicesPort port = clientWS.getClientEDespatchServicesPort();
			BindingProvider provider = (BindingProvider) port;
			Map<String, Object> reqContext = provider.getRequestContext();
			reqContext.put(BindingProvider.USERNAME_PROPERTY, userNameProp);
			reqContext.put(BindingProvider.PASSWORD_PROPERTY, passwordProp);
			reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointProp);

			long avg = 0;
			for (int i = 1; i <= count; i++) {

				GetDesUserListRequest req = new GetDesUserListRequest();
				req.setIdentifier(identifier);
				req.setVKNTCKN(vkntckn);
				req.setRole(role.name());

				long duration = 0;
				try {
					long start = System.currentTimeMillis();
					GetDesUserListResponse resp = port.getDesUserList(req);
					duration = (System.currentTimeMillis() - start);
					avg = ((avg * (i - 1)) + duration) / i;

					String docData = new String("DocData: " + Base64.encodeBase64String(resp.getDocData()));
					System.out.println(docData);

					System.out.println(i + " - Duration: " + duration + ", Avg: " + avg);
				} catch (GetDesUserListFaultMessage fm) {
					System.out.println("FM: " + fm.getFaultInfo().getCode() + " - " + fm.getFaultInfo().getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}

	/**
	 * @param identifier Zarfın göndericisine ait Gönderici Birim(GB) ya da Posta
	 *                   Kutusu(PK) etiketi
	 * @param uuid       Zarf ETTN
	 * @param vkntckn    Zarfın göndericisine ait VKN/TCKN
	 * @throws IOException
	 */
	public static void getDesEnvelopeStatus(String vkntckn, String uuid, String identifier) throws IOException {

		try {
			ClientEDespatchServices clientWS = new ClientEDespatchServices();
			ClientEDespatchServicesPort port = clientWS.getClientEDespatchServicesPort();
			BindingProvider provider = (BindingProvider) port;
			Map<String, Object> reqContext = provider.getRequestContext();
			reqContext.put(BindingProvider.USERNAME_PROPERTY, userNameProp);
			reqContext.put(BindingProvider.PASSWORD_PROPERTY, passwordProp);
			reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointProp);

			long avg = 0;
			for (int i = 1; i <= count; i++) {

				GetDesEnvelopeStatusRequest req = new GetDesEnvelopeStatusRequest();
				req.getUUID().add(uuid);
				req.setIdentifier(identifier);
				req.setVKNTCKN(vkntckn);
				req.getParameters().add("DOC_DATA");

				long duration = 0;
				try {
					long start = System.currentTimeMillis();
					GetDesEnvelopeStatusResponse resp = port.getDesEnvelopeStatus(req);
					duration = (System.currentTimeMillis() - start);
					avg = ((avg * (i - 1)) + duration) / i;

					for (GetDesEnvelopeStatusResponseType respt : resp.getResponse()) {
						System.out.println("UUID:" + respt.getUUID() + "\nIssueDate:" + respt.getIssueDate()
								+ "\nVDocumentTypeCode :" + respt.getDocumentType() + "\nDocumentType :"
								+ respt.getDocumentType() + "\nResponseCode :" + respt.getResponseCode()
								+ "\nDescription :" + respt.getDescription());
						System.out.println(i + " - Duration: " + duration + ", Avg: " + avg);

					}
				} catch (GetDesEnvelopeStatusFaultMessage fm) {
					System.out.println("FM: " + fm.getFaultInfo().getCode() + " - " + fm.getFaultInfo().getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}

	/**
	 * @param identifier Belgenin gönderici ya da alıcısına ait Gönderici Birim(GB)
	 *                   ya da Posta Kutusu(PK) etiketi
	 * @param vkntckn    Belgenin gönderici ya da alıcısına ait VKN/TCK
	 * @param uuid       Belge ETTN
	 * @param type       Gelen/Gönderilen Belge (OUTBOUND, INBOUND)
	 * @param doctype    Belge Türü (ENVELOPE, DESPATCH, RECEIPT, SYS_RESP)
	 * @throws IOException
	 */
	public static void getDesUBL(String identifier, String vkntckn, String uuid, UBLDesDocumentType doctype,
			Direction type) throws IOException {

		try {
			ClientEDespatchServices clientWS = new ClientEDespatchServices();
			ClientEDespatchServicesPort port = clientWS.getClientEDespatchServicesPort();
			BindingProvider provider = (BindingProvider) port;
			Map<String, Object> reqContext = provider.getRequestContext();
			reqContext.put(BindingProvider.USERNAME_PROPERTY, userNameProp);
			reqContext.put(BindingProvider.PASSWORD_PROPERTY, passwordProp);
			reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointProp);

			long avg = 0;
			for (int i = 1; i <= count; i++) {

				GetDesUBLRequest req = new GetDesUBLRequest();
				req.setIdentifier(identifier);
				req.setVKNTCKN(vkntckn);
				req.getUUID().add(uuid);
				req.setDocType(doctype.name());
				req.setType(type.name());

				long duration = 0;
				try {
					long start = System.currentTimeMillis();
					GetDesUBLResponse resp = port.getDesUBL(req);
					duration = (System.currentTimeMillis() - start);
					avg = ((avg * (i - 1)) + duration) / i;

					String getDesUBLResponse = new String("UUID:" + resp.getResponse().get(0).getUUID() + "\nDocData: "
							+ Base64.encodeBase64String(resp.getResponse().get(0).getDocData()));
					System.out.println(getDesUBLResponse);

					System.out.println(i + " - Duration: " + duration + ", Avg: " + avg);
				} catch (GetDesUBLFaultMessage fm) {
					System.out.println("FM: " + fm.getFaultInfo().getCode() + " - " + fm.getFaultInfo().getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}

	/**
	 * @param identifier Belgenin gönderici ya da alıcısına ait Gönderici Birim(GB)
	 *                   ya da Posta Kutusu(PK) etiketi
	 * @param vkntckn    Belgenin gönderici ya da alıcısına ait VKN/TCKN
	 * @param uuid       Belge UUID listesi
	 * @param docType    Belge Türü (ENVELOPE, DESPATCH, RECEIPT, SYS_RESP)
	 * @param type       Gelen/Gönderilen Belge (OUTBOUND, INBOUND)
	 * @throws IOException
	 */
	public static void getDesUBLList(String vkntckn, String uuid, String identifier, UBLDesDocumentType docType,
			Direction type) throws IOException {

		try {
			ClientEDespatchServices clientWS = new ClientEDespatchServices();
			ClientEDespatchServicesPort port = clientWS.getClientEDespatchServicesPort();
			BindingProvider provider = (BindingProvider) port;
			Map<String, Object> reqContext = provider.getRequestContext();
			reqContext.put(BindingProvider.USERNAME_PROPERTY, userNameProp);
			reqContext.put(BindingProvider.PASSWORD_PROPERTY, passwordProp);
			reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointProp);

			long avg = 0;
			for (int i = 1; i <= count; i++) {

				GetDesUBLListRequest req = new GetDesUBLListRequest();
				req.setIdentifier(identifier);
				req.setVKNTCKN(vkntckn);
				req.getUUID().add(uuid);
				req.setDocType(docType.name());
				req.setType(type.name());

				long duration = 0;
				try {
					long start = System.currentTimeMillis();
					GetDesUBLListResponse resp = port.getDesUBLList(req);
					duration = (System.currentTimeMillis() - start);
					avg = ((avg * (i - 1)) + duration) / i;
					String getDesUBLListResponse = new String("UUID:" + resp.getResponse().get(0).getUUID() + "\nidentifier:"
							+ resp.getResponse().get(0).getIdentifier() + "\nVKN_TCKN:"
							+ resp.getResponse().get(0).getVKNTCKN() + "\nEnvUUID:"
							+ resp.getResponse().get(0).getEnvUUID() + "\nID:" + resp.getResponse().get(0).getID()
							+ "\nCustDesID:" + resp.getResponse().get(0).getCustDesID() + "\nInsertDateTime:"
							+ resp.getResponse().get(0).getInsertDateTime());

					System.out.println(getDesUBLListResponse);
					System.out.println(i + " - Duration: " + duration + ", Avg: " + avg);
				} catch (GetDesUBLListFaultMessage fm) {
					System.out.println("FM: " + fm.getFaultInfo().getCode() + " - " + fm.getFaultInfo().getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");

	}

	/**
	 * @param identifier Belgenin gönderici ya da alıcısına ait Gönderici Birim(GB)
	 *                   ya da Posta Kutusu(PK) etiketi
	 * @param vkntckn    Belgenin gönderici ya da alıcısına ait VKN/TCKN
	 * @param docType    Belge Türü (ENVELOPE, DESPATCH, RECEIPT, SYS_RESP)
	 * @param type       Gelen/Gönderilen Belge (OUTBOUND, INBOUND)
	 * @param FromDate   Başlangıç tarihi ve zamanı
	 * @param ToDate     Bitiş tarihi ve zamanı
	 * @throws IOException
	 */
	public static void getDesUBLList(String identifier, String vkntckn, UBLDocumentType docType, Direction type,
			String FromDate, String ToDate) throws IOException {

		try {
			ClientEDespatchServices clientWS = new ClientEDespatchServices();
			ClientEDespatchServicesPort port = clientWS.getClientEDespatchServicesPort();

			BindingProvider provider = (BindingProvider) port;
			Map<String, Object> reqContext = provider.getRequestContext();
			reqContext.put(BindingProvider.USERNAME_PROPERTY, userNameProp);
			reqContext.put(BindingProvider.PASSWORD_PROPERTY, passwordProp);
			reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointProp);

			XMLGregorianCalendar fromDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(FromDate);
			XMLGregorianCalendar toDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(ToDate);

			long avg = 0;
			for (int i = 1; i <= count; i++) {

				GetDesUBLListRequest req = new GetDesUBLListRequest();
				req.setIdentifier(identifier);
				req.setVKNTCKN(vkntckn);
				req.setDocType(docType.name());
				req.setType(type.name());
				req.setFromDate(fromDate);
				req.setToDate(toDate);

				long duration = 0;
				try {
					long start = System.currentTimeMillis();
					GetDesUBLListResponse resp = port.getDesUBLList(req);
					duration = (System.currentTimeMillis() - start);
					avg = ((avg * (i - 1)) + duration) / i;
					String getDesUBLListResponse = new String("UUID:" + resp.getResponse().get(0).getUUID() + "\nidentifier:"
							+ resp.getResponse().get(0).getIdentifier() + "\nVKN_TCKN:"
							+ resp.getResponse().get(0).getVKNTCKN() + "\nEnvUUID:"
							+ resp.getResponse().get(0).getEnvUUID() + "\nID:" + resp.getResponse().get(0).getID()
							+ "\nCustInvID:" + resp.getResponse().get(0).getCustDesID() + "\nInsertDateTime:"
							+ resp.getResponse().get(0).getInsertDateTime());

					System.out.println(getDesUBLListResponse);
					System.out.println(i + " - Duration: " + duration + ", Avg: " + avg);
				} catch (GetDesUBLListFaultMessage fm) {
					System.out.println("FM: " + fm.getFaultInfo().getCode() + " - " + fm.getFaultInfo().getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");

	}

	/**
	 * @param vkntckn            Gönderici VKN/TCKN
	 * @param doctype            Belge Türü (ENVELOPE, DESPATCH, RECEIPT)
	 * @param identifier         Gönderici Etiketi (yalnızca zarfsız gönderimlerde)
	 * @param receiverIdentifier Alıcı Etiketi (yalnızca zarfsız gönderimlerde)
	 * @throws IOException
	 */
	public static void sendDesUBL(String vkntckn, UBLDesDocumentType doctype, String identifier,
			String receiverIdentifier) throws IOException {
		try {
			ClientEDespatchServices clientWS = new ClientEDespatchServices();
			ClientEDespatchServicesPort port = clientWS.getClientEDespatchServicesPort();

			BindingProvider provider = (BindingProvider) port;
			Map<String, Object> reqContext = provider.getRequestContext();
			reqContext.put(BindingProvider.USERNAME_PROPERTY, userNameProp);
			reqContext.put(BindingProvider.PASSWORD_PROPERTY, passwordProp);
			reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointProp);
			String ublXML = UtilsIO.readStringFromInputStream(Application.class.getResourceAsStream("envDesUBL.xml"));
			StandardBusinessDocument sbd = UBLMarshallUnmarshall.unmarshallSBD(ublXML);

			long avg = 0;
			for (int i = 1; i <= count; i++) {
				String envUUID = UtilsUUID.generateRandomUUID();
				sbd.getStandardBusinessDocumentHeader().getDocumentIdentification().setInstanceIdentifier(envUUID);
				DespatchAdviceType dat = (DespatchAdviceType) sbd.getPackage().getElements().get(0).getElementList().getAny().get(0);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(dat);
				sbd.getPackage().getElements().get(0).getElementList().getAny().clear();
				for (int j = 1; j <= childCount; j++) {
					ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
					ObjectInputStream ois = new ObjectInputStream(bais);
					dat = (DespatchAdviceType) ois.readObject();
					UUIDType ut = new UUIDType();
					ut.setValue(UtilsUUID.generateRandomUUID());
					dat.setUUID(ut);
					IDType desIDType = new IDType();
					desIDType.setValue(UtilsUUID.generateRandomID("TST"));
					if (!ID_AUTO_GENERATE) {
						dat.setID(desIDType);
					} else {
						dat.getAdditionalDocumentReference().get(0).setID(desIDType);
					}
					sbd.getPackage().getElements().get(0).getElementList().getAny().add(dat);
				}
				sbd.getPackage().getElements().get(0).setElementCount(childCount);
				String ubl = UBLMarshallUnmarshall.marshallSBD(sbd);
				byte[] zipData = UBLPackageManager.packageSBDXMLasBinaryData(envUUID, ubl.getBytes("UTF-8"));
				SendDesUBLRequest req = new SendDesUBLRequest();
				req.setVKNTCKN(vkntckn);
				req.setDocType(doctype.name());
				req.setSenderIdentifier(identifier);
				req.setReceiverIdentifier(receiverIdentifier);
				req.setDocData(zipData);

				long duration = 0;
				try {
					long start = System.currentTimeMillis();
					SendDesUBLResponse resp = port.sendDesUBL(req);
					duration = (System.currentTimeMillis() - start);
					avg = ((avg * (i - 1)) + duration) / i;
					for (SendDesUBLResponseType respt : resp.getResponse()) {
						System.out.println("EnvUUID: " + respt.getEnvUUID() + " UUID: " + respt.getUUID() + " ID: " + respt.getID() + " - " + respt.getCustDesID());
					}
					System.out.println(i + " - Duration: " + duration + ", Avg: " + avg);
				} catch (SendDesUBLFaultMessage fm) {
					System.out.println("FM: " + fm.getFaultInfo().getCode() + " - " + fm.getFaultInfo().getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}

	
	/**
	 * @param vkntckn Gönderici VKN/TCKN
	 * @param doctype Belge Türü (ENVELOPE, DESPATCH, RECEIPT)
	 * @throws IOException
	 */

	public static void sendRAUBL(String vkntckn, UBLDesDocumentType doctype) throws IOException {
		try {
			ClientEDespatchServices clientWS = new ClientEDespatchServices();
			ClientEDespatchServicesPort port = clientWS.getClientEDespatchServicesPort();

			BindingProvider provider = (BindingProvider) port;
			Map<String, Object> reqContext = provider.getRequestContext();
			reqContext.put(BindingProvider.USERNAME_PROPERTY, userNameProp);
			reqContext.put(BindingProvider.PASSWORD_PROPERTY, passwordProp);
			reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointProp);
			String ublXML = UtilsIO.readStringFromInputStream(Application.class.getResourceAsStream("envRAUBL.xml"));
			StandardBusinessDocument sbd = UBLMarshallUnmarshall.unmarshallSBD(ublXML);

			long avg = 0;
			for (int i = 1; i <= count; i++) {
				String envUUID = UtilsUUID.generateRandomUUID();
				sbd.getStandardBusinessDocumentHeader().getDocumentIdentification().setInstanceIdentifier(envUUID);
				ReceiptAdviceType rat = (ReceiptAdviceType) sbd.getPackage().getElements().get(0).getElementList()
						.getAny().get(0);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(rat);
				sbd.getPackage().getElements().get(0).getElementList().getAny().clear();
				for (int j = 1; j <= childCount; j++) {
					ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
					ObjectInputStream ois = new ObjectInputStream(bais);
					rat = (ReceiptAdviceType) ois.readObject();
					UUIDType ut = new UUIDType();
					ut.setValue(UtilsUUID.generateRandomUUID());
					rat.setUUID(ut);
					IDType invIDType = new IDType();
					invIDType.setValue(UtilsUUID.generateRandomUUID());
					sbd.getPackage().getElements().get(0).getElementList().getAny().add(rat);
				}
				sbd.getPackage().getElements().get(0).setElementCount(childCount);
				String ubl = UBLMarshallUnmarshall.marshallSBD(sbd);
				byte[] zipData = UBLPackageManager.packageSBDXMLasBinaryData(envUUID, ubl.getBytes("UTF-8"));
				SendDesUBLRequest req = new SendDesUBLRequest();
				req.setVKNTCKN(vkntckn);
				req.setDocType(doctype.name());
				req.setDocData(zipData);
				long duration = 0;
				try {
					long start = System.currentTimeMillis();
					SendDesUBLResponse resp = port.sendDesUBL(req);
					duration = (System.currentTimeMillis() - start);
					avg = ((avg * (i - 1)) + duration) / i;
					for (SendDesUBLResponseType respt : resp.getResponse()) {
						System.out.println("RAEnvUUID: " + respt.getEnvUUID() + " RAUUID: " + respt.getUUID() + " RAID: "
								+ respt.getID());
					}
					System.out.println("Irsaliye yanıtı gönderildi.");
					System.out.println(i + " - Duration: " + duration + ", Avg: " + avg);
				} catch (SendDesUBLFaultMessage fm) {
					System.out.println("FM: " + fm.getFaultInfo().getCode() + " - " + fm.getFaultInfo().getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}

}
