package com.foriba.eDespatch.test;

import java.io.IOException;

import com.foriba.eDespatch.ClientWS;
import com.foriba.eDespatch.ParamServices;
import com.foriba.eDespatch.ParamServices.Direction;
import com.foriba.eDespatch.ParamServices.UBLDesDocumentType;
import com.foriba.eDespatch.ParamServices.UserRole;
import com.foriba.eDespatch.ParamServices.ViewDocType;

public class Application {

	//İlgili parametreler doldurulmalıdır
	private static String vkntckn = "1234567890";
	private static String id="DA72018000000004";
	private static String uuid ="00aaccd4-1a6d-41b9-8e30-172651ffd23e";
	private static String docRefUUID="a42345e1-364b-483b-9fc2-6ad42373c1f";
	private static String custDesID = "TST2019024698675";
	private static String identifier = "urn:mail:testgb@test.com";
	private static String receiveridentifier = "urn:mail:testk@test.com";
	private static String fromDate = "2019-07-01T02:31:24.000+02:00";
	private static String toDate = "2019-07-01T12:31:24.000+02:00";


	private static Direction type = ParamServices.Direction.OUTBOUND;
	private static ViewDocType viewType = ParamServices.ViewDocType.HTML;
	private static UBLDesDocumentType  doctype = ParamServices.UBLDesDocumentType.ENVELOPE;
	private static UserRole role = ParamServices.UserRole.GB;
	

	
	public static void main(String[] args) throws IOException {

			
		//2 farklı parametre verilebilir.(vkntckn,uuid,identifier,doctype,type)||(identifier,vkntckn,doctype,type,fromDate,toDate)
//		ClientWS.getDesUBLList(vkntckn,uuid,identifier,doctype,type);
		
//		ClientWS.getDesEnvelopeStatus(vkntckn,uuid,identifier);
		
//		ClientWS.getDesReceipts(identifier,vkntckn,docRefUUID,type);
		
//		ClientWS.getDesUserList(vkntckn,identifier,role);	
		
//		ClientWS.getDesUBL(identifier,vkntckn,uuid,doctype,type);
		
//		ClientWS.sendDesUBL(vkntckn,doctype,identifier,receiveridentifier);
		
//		ClientWS.sendRAUBL(vkntckn,doctype);
		
		//3 farklı parametre verilebilir.(identifier,vkntckn,uuid,type,doctype,viewType)||(identifier,vkntckn,type,doctype,viewType,id)||(identifier,vkntckn,type,doctype,custDesID,viewType)
//		ClientWS.getDesView(identifier,vkntckn,uuid,type,doctype,viewType);	
		

	}
}
