package net.svcret.core.invoker.soap;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

/**
 * SOAP Condtants
 */
public class Constants {

	public static final String CONTENT_TYPE_XML = "text/xml";
	public static final String NS_SOAPENV11 = "http://schemas.xmlsoap.org/soap/envelope/";
	public static final String NS_SOAPENV12 = "http://www.w3.org/2003/05/soap-envelope";
	public static final Set<String> NS_SOAPENV12_ALTERNATES;
	public static final String NS_WSDL = "http://schemas.xmlsoap.org/wsdl/";
	public static final String NS_WSDLSOAP = "http://schemas.xmlsoap.org/wsdl/soap/";
	public static final String NS_WSSEC_SECEXT = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	public static final String NS_WSSEC_UTIL = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
	public static final String NS_XSD = "http://www.w3.org/2001/XMLSchema";
	public static final QName SOAPENV11_BODY_QNAME = new QName(NS_SOAPENV11, "Body");
	public static final QName SOAPENV11_ENVELOPE_QNAME = new QName(NS_SOAPENV11, "Envelope");
	public static final QName SOAPENV11_FAULT_QNAME = new QName(NS_SOAPENV11, "Fault");
	public static final QName SOAPENV11_HEADER_QNAME = new QName(NS_SOAPENV11, "Header");
	public static final QName SOAPENV12_ENVELOPE_QNAME = new QName(NS_SOAPENV12, "Envelope");
	public static final Set<QName> SOAPENV12_ENVELOPE_QNAME_ALTERNATES;
	public static final String TAG_SOAPENV11_FAULTCODE = "faultcode";
	public static final String TAG_SOAPENV11_FAULTSTRING = "faultstring";
	public static final String VALUE_WSSE_PASSWORD_TYPE_TEXT = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";
	public static final QName WSSE_PASSWORD_QNAME = new QName(NS_WSSEC_SECEXT, "Password");
	public static final QName WSSE_SECURITY_QNAME = new QName(NS_WSSEC_SECEXT, "Security");
	public static final QName WSSE_TYPE_QNAME = new QName(NS_WSSEC_SECEXT, "Type");
	public static final QName WSSE_USERNAME_QNAME = new QName(NS_WSSEC_SECEXT, "Username");
	public static final QName WSSE_USERNAME_TOKEN_QNAME = new QName(NS_WSSEC_SECEXT, "UsernameToken");
	public static final QName WSSU_ID_QNAME = new QName(NS_WSSEC_UTIL, "Id");

	static {
		HashSet<String> soapenvAlts = new HashSet<String>();
		soapenvAlts.add("http://www.w3.org/2001/12/soap-envelope");
		soapenvAlts.add("http://www.w3.org/2002/12/soap-envelope");
		NS_SOAPENV12_ALTERNATES = Collections.unmodifiableSet(soapenvAlts);
		
		HashSet<QName> soapAlts = new HashSet<QName>();
		for (String nextNs : NS_SOAPENV12_ALTERNATES) {
			soapAlts.add(new QName(nextNs, "Envelope"));
		}
		SOAPENV12_ENVELOPE_QNAME_ALTERNATES=Collections.unmodifiableSet(soapAlts);
	}

	public static QName getSoapenvHeaderQname(String theXmlPrefix) {
		return new QName(SOAPENV11_HEADER_QNAME.getNamespaceURI(), SOAPENV11_HEADER_QNAME.getLocalPart(), theXmlPrefix);
	}

	public static QName getWssePasswordQname(String theXmlPrefix) {
		return new QName(WSSE_PASSWORD_QNAME.getNamespaceURI(), WSSE_PASSWORD_QNAME.getLocalPart(), theXmlPrefix);
	}

	public static QName getWsseSecurityQname(String theXmlPrefix) {
		return new QName(WSSE_SECURITY_QNAME.getNamespaceURI(), WSSE_SECURITY_QNAME.getLocalPart(), theXmlPrefix);
	}

	public static QName getWsseUsernameQname(String theXmlPrefix) {
		return new QName(WSSE_USERNAME_QNAME.getNamespaceURI(), WSSE_USERNAME_QNAME.getLocalPart(), theXmlPrefix);
	}

	public static QName getWsseUsernameTokenQname(String theXmlPrefix) {
		return new QName(WSSE_USERNAME_TOKEN_QNAME.getNamespaceURI(), WSSE_USERNAME_TOKEN_QNAME.getLocalPart(), theXmlPrefix);
	}

	public static QName getWssuIdQname(String theXmlPrefix) {
		return new QName(WSSU_ID_QNAME.getNamespaceURI(), WSSU_ID_QNAME.getLocalPart(), theXmlPrefix);
	}

}
