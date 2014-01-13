package net.svcret.ejb.propcap;

import java.io.IOException;
import java.io.StringReader;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.svcret.ejb.api.SrBeanIncomingRequest;
import net.svcret.ejb.api.SrBeanProcessedRequest;
import net.svcret.ejb.ex.InvocationFailedDueToInternalErrorException;
import net.svcret.ejb.ex.InvocationRequestFailedException;
import net.svcret.ejb.invoker.soap.InvocationFailedException;
import net.svcret.ejb.model.entity.BasePersServiceVersion;
import net.svcret.ejb.model.entity.PersPropertyCapture;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class PropertyCaptureBean implements IPropertyCaptureService {

	private DocumentBuilderFactory myDocumentBuilderFactory;
	private XPathFactory myXpathFactory;

	public PropertyCaptureBean() {
		myDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
		myDocumentBuilderFactory.setNamespaceAware(true);

		myXpathFactory = XPathFactory.newInstance();
	}

	@Override
	public void captureRequestProperties(BasePersServiceVersion theServiceVersion, SrBeanIncomingRequest theRequest, SrBeanProcessedRequest theInvocationResult) throws InvocationFailedException {
		for (PersPropertyCapture next : theServiceVersion.getPropertyCaptures()) {
			captureRequestProperty(next, theRequest, theInvocationResult);
		}
	}

	private void captureRequestProperty(PersPropertyCapture theCapture, SrBeanIncomingRequest theRequest, SrBeanProcessedRequest theInvocationResult) throws InvocationFailedException {
		if (StringUtils.isNotBlank(theCapture.getXpathExpression())) {
			captureRequestPropertyXpath(theCapture, theRequest, theInvocationResult);
		}
	}

	private void captureRequestPropertyXpath(PersPropertyCapture theCapture, SrBeanIncomingRequest theRequest, SrBeanProcessedRequest theInvocationResult) throws InvocationFailedException {

		XPath xpath = myXpathFactory.newXPath();
		XPathExpression expr = theCapture.getCompiledXpathExpression();
		if (expr == null) {
			try {
				expr = xpath.compile(theCapture.getXpathExpression());
				theCapture.setCompiledXpathExpression(expr);
			} catch (XPathExpressionException e1) {
				throw new InvocationRequestFailedException(e1, "Failed to compile property capture XPath: " + theCapture.getXpathExpression());
			}
		}

		DocumentBuilder builder;
		try {
			builder = myDocumentBuilderFactory.newDocumentBuilder();
			String requestBody = theRequest.getRequestBody();
			Document doc = builder.parse(new InputSource(new StringReader(requestBody)));
			String result = (String) expr.evaluate(doc, XPathConstants.STRING);

			theInvocationResult.addPropertyCapture(theCapture.getPk().getPropertyName(), result);

		} catch (ParserConfigurationException e) {
			throw new InvocationFailedDueToInternalErrorException(e, "Failed to initialize DOM parser");
		} catch (SAXException e) {
			throw new InvocationRequestFailedException(e);
		} catch (IOException e) {
			throw new InvocationRequestFailedException(e);
		} catch (XPathExpressionException e) {
			throw new InvocationRequestFailedException(e);
		}

	}

}