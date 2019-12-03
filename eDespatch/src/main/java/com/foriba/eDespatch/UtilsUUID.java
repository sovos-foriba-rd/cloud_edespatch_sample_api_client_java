package com.foriba.eDespatch;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

public class UtilsUUID {
	
	public static String generateRandomUUID() {
		return java.util.UUID.randomUUID().toString();
	}
	
	//10 milyon tekil GIBYYYYMMxxxxxxx formatında ID üretebilir
	public static String generateRandomID(String prefix) {
		Calendar cal = Calendar.getInstance();
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumIntegerDigits(2);
		StringBuffer sb = new StringBuffer();
		sb.append(prefix);
		sb.append(cal.get(Calendar.YEAR));
		sb.append(nf.format(cal.get(Calendar.MONTH) + 1));
		nf.setMinimumIntegerDigits(7);
		nf.setMaximumIntegerDigits(7);
		nf.setMaximumFractionDigits(0);
		nf.setGroupingUsed(false);
		sb.append(nf.format(Math.random() * 10000000));
		return sb.toString();
	}
	
	
	public static boolean isValidUUID(String UUID) {
		return Pattern.matches("\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}", UUID);
	}
	
	public static boolean isValidInvID(String ID) {
		return Pattern.matches("\\w{3}\\d{4}\\d{9}", ID);
	}
	
	public static void main(String[] args) {
		System.out.println(UtilsUUID.generateRandomID("GIB"));
	}

}
