package net.svcret.admin.shared.util;


public class StringUtil {

	public static String defaultString(String theString) {
		if (theString == null) {
			theString = "";
		}
		return theString;
	}

	public static String defaultString(String theString, String theValue) {
		if (theString == null) {
			theString = theValue;
		}
		return theString;
	}

	public static boolean isBlank(String theString) {
		if (theString == null || theString.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static String obscure(String theString) {
		StringBuilder b = new StringBuilder();
		if (isBlank(theString)) {
			b.append("<none>");
		} else {
			for (int i = 0; i < theString.length(); i++) {
				b.append('*');
			}
		}
		return b.toString();
	}

	public static boolean isNotBlank(String theString) {
		return !isBlank(theString);
	}

	public static boolean positiveInt(String theString) {
		if (theString==null || theString.length()==0) {
			return false;
		}
		for (int i = 0; i < theString.length(); i++) {
			char next = theString.charAt(i);
			if (next < '0' || next > '9') {
				return false;
			}
		}
		return true;
	}

	public static int compare(String theName, String theName2) {
		return defaultString(theName).compareTo(defaultString(theName2));
	}

	public static String convertPlaintextToHtml(String theDescription) {
		return theDescription.replace("<", "&lt;").replaceAll("(http(s?):\\S+)(\\s| |$)", "<a href=\"$1\">$1</a>$3").replace("\n", "<br/>");
	}

	public static boolean equals(String theValue, String theValue2) {
		if (theValue==null && theValue2==null) {
			return true;
		}
		if (theValue==null || theValue2==null) {
			return false;
		}
		return theValue.equals(theValue2);
	}
	
}
