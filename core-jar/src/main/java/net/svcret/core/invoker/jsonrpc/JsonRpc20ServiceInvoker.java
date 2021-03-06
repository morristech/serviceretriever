package net.svcret.core.invoker.jsonrpc;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Set;

import net.svcret.admin.api.ProcessingException;
import net.svcret.admin.shared.enm.ResponseTypeEnum;
import net.svcret.core.api.IResponseValidator;
import net.svcret.core.api.RequestType;
import net.svcret.core.api.SrBeanIncomingRequest;
import net.svcret.core.api.SrBeanIncomingResponse;
import net.svcret.core.api.SrBeanProcessedRequest;
import net.svcret.core.api.SrBeanProcessedResponse;
import net.svcret.core.ex.InvalidRequestException;
import net.svcret.core.ex.InvocationFailedDueToInternalErrorException;
import net.svcret.core.ex.InvocationRequestFailedException;
import net.svcret.core.ex.InvocationResponseFailedException;
import net.svcret.core.ex.InvalidRequestException.IssueEnum;
import net.svcret.core.invoker.BaseServiceInvoker;
import net.svcret.core.model.entity.BasePersServiceVersion;
import net.svcret.core.model.entity.PersBaseClientAuth;
import net.svcret.core.model.entity.PersBaseServerAuth;
import net.svcret.core.model.entity.PersMethod;
import net.svcret.core.model.entity.jsonrpc.NamedParameterJsonRpcClientAuth;
import net.svcret.core.model.entity.jsonrpc.NamedParameterJsonRpcCredentialGrabber;
import net.svcret.core.model.entity.jsonrpc.NamedParameterJsonRpcServerAuth;
import net.svcret.core.model.entity.jsonrpc.PersServiceVersionJsonRpc20;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

@Service
public class JsonRpc20ServiceInvoker extends BaseServiceInvoker implements IServiceInvokerJsonRpc20 {

	static final String TOKEN_ID = "id";
	static final String TOKEN_PARAMS = "params";
	static final String TOKEN_METHOD = "method";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonRpc20ServiceInvoker.class);

	static final String TOKEN_JSONRPC = "jsonrpc";
	static final String TOKEN_RESULT = "result";
	static final String TOKEN_ERROR = "error";
	private boolean myPrettyPrintModeForUnitTest;

	public static void consumeEqually(IJsonReader theJsonReader, IJsonWriter theJsonWriter) throws IOException, ProcessingException {
		int objectDepth = 0;
		int arrayDepth = 0;

		JsonToken next = theJsonReader.peek();
		switch (next) {
		case NULL:
			theJsonReader.nextNull();
			theJsonWriter.nullValue();
			return;
		case BEGIN_OBJECT:
			theJsonReader.beginObject();
			theJsonWriter.beginObject();
			objectDepth++;
			break;
		case BEGIN_ARRAY:
			theJsonReader.beginArray();
			theJsonWriter.beginArray();
			arrayDepth++;
			break;
		case BOOLEAN:
			theJsonWriter.value(theJsonReader.nextBoolean());
			return;
		case NUMBER:
			consumeNumberEqually(theJsonReader, theJsonWriter);
			return;
		case STRING:
			theJsonWriter.value(theJsonReader.nextString());
			break;
		case NAME:
		case END_ARRAY:
		case END_DOCUMENT:
		case END_OBJECT:
		default:
			throw new ProcessingException("Found token of " + next + " in an unexpected place");
		}

		do {
			next = theJsonReader.peek();
			switch (next) {
			case NULL:
				theJsonReader.nextNull();
				theJsonWriter.nullValue();
				break;
			case BEGIN_ARRAY:
				theJsonReader.beginArray();
				theJsonWriter.beginArray();
				arrayDepth++;
				break;
			case BEGIN_OBJECT:
				theJsonReader.beginObject();
				theJsonWriter.beginObject();
				objectDepth++;
				break;
			case BOOLEAN:
				theJsonWriter.value(theJsonReader.nextBoolean());
				break;
			case END_ARRAY:
				theJsonReader.endArray();
				theJsonWriter.endArray();
				arrayDepth--;
				break;
			case END_DOCUMENT:
				break;
			case END_OBJECT:
				theJsonReader.endObject();
				theJsonWriter.endObject();
				objectDepth--;
				break;
			case NAME:
				String nextName = theJsonReader.nextName();
				theJsonWriter.name(nextName);
				break;
			case NUMBER:
				consumeNumberEqually(theJsonReader, theJsonWriter);
				break;
			case STRING:
				String nextString = theJsonReader.nextString();
				theJsonWriter.value(nextString);
				break;
			}
		} while ((objectDepth > 0 || arrayDepth > 0));

	}

	private static void consumeNumberEqually(IJsonReader theJsonReader, IJsonWriter theJsonWriter) throws IOException, ProcessingException {
		String value = theJsonReader.nextString();
		if (value.contains(".")) {
			try {
				theJsonWriter.value(Double.parseDouble(value));
			} catch (NumberFormatException e) {
				throw new ProcessingException("Invalid double value found in JSON: " + value);
			}
		} else {
			try {
				theJsonWriter.value(Long.parseLong(value));
			} catch (NumberFormatException e) {
				throw new ProcessingException("Invalid long value found in JSON: " + value);
			}
		}
	}

	private static void consumeNumberEqually(JsonReader theJsonReader, JsonWriter theJsonWriter) throws IOException, ProcessingException {
		String value = theJsonReader.nextString();
		if (value.contains(".")) {
			try {
				theJsonWriter.value(Double.parseDouble(value));
			} catch (NumberFormatException e) {
				throw new ProcessingException("Invalid double value found in JSON: " + value);
			}
		} else {
			try {
				theJsonWriter.value(Long.parseLong(value));
			} catch (NumberFormatException e) {
				throw new ProcessingException("Invalid long value found in JSON: " + value);
			}
		}
	}

	public static void consumeEqually(JsonReader theJsonReader) throws IOException {
		int objectDepth = 0;
		int arrayDepth = 0;

		JsonToken next = theJsonReader.peek();
		switch (next) {
		case BEGIN_OBJECT:
			theJsonReader.beginObject();
			objectDepth++;
			break;
		case BEGIN_ARRAY:
			theJsonReader.beginArray();
			arrayDepth++;
			break;
		case NULL:
			theJsonReader.nextNull();
			return;
		case BOOLEAN:
			theJsonReader.nextBoolean();
			return;
		case END_ARRAY:
		case END_DOCUMENT:
		case END_OBJECT:
		case NAME:
			throw new IllegalStateException("Not expected here");
		case NUMBER:
			theJsonReader.nextString(); // avoid long/double problems by just using string
			return;
		case STRING:
			theJsonReader.nextString();
			return;
		}

		do {
			next = theJsonReader.peek();
			switch (next) {
			case NULL:
				theJsonReader.nextNull();
				break;
			case BEGIN_ARRAY:
				theJsonReader.beginArray();
				arrayDepth++;
				break;
			case BEGIN_OBJECT:
				theJsonReader.beginObject();
				objectDepth++;
				break;
			case BOOLEAN:
				theJsonReader.nextBoolean();
				break;
			case END_ARRAY:
				theJsonReader.endArray();
				arrayDepth--;
				break;
			case END_DOCUMENT:
				break;
			case END_OBJECT:
				theJsonReader.endObject();
				objectDepth--;
				break;
			case NAME:
				theJsonReader.nextName();
				break;
			case NUMBER:
				theJsonReader.nextString(); // avoid long/double problems by just using string
				break;
			case STRING:
				theJsonReader.nextString();
				break;
			}
		} while ((objectDepth > 0 || arrayDepth > 0));

	}

	@Override
	public SrBeanProcessedRequest processInvocation(SrBeanIncomingRequest theRequest, BasePersServiceVersion theServiceDefinition) throws InvalidRequestException, InvocationRequestFailedException {
		if (theRequest.getRequestType() != RequestType.POST) {
			throw new InvalidRequestException(IssueEnum.UNSUPPORTED_ACTION, theRequest.getRequestType().name(), "Requests to JSON-RPC 2.0 services must use HTTP POST.");
		}

		SrBeanProcessedRequest retVal;
		try {
			retVal = doProcessInvocation((PersServiceVersionJsonRpc20) theServiceDefinition, theRequest.getInputReader());
		} catch (IOException e) {
			throw new InvocationRequestFailedException(e);
		}

		return retVal;
	}

	@SuppressWarnings("resource")
	private static SrBeanProcessedRequest doProcessInvocation(PersServiceVersionJsonRpc20 theServiceDefinition, Reader theReader) throws IOException, InvocationRequestFailedException {
		SrBeanProcessedRequest retVal = new SrBeanProcessedRequest();

		// Reader
		String inputMessage = IOUtils.toString(theReader);
		ourLog.trace("Input message:\n{}", inputMessage);
		try (StringReader inputReader = new StringReader(inputMessage)) {

			// Writer
			try (StringWriter stringWriter = new StringWriter()) {
				IJsonWriter jsonWriter = new MyJsonWriter(stringWriter);
				IJsonReader jsonReader = new MyJsonReader(inputReader);
				try {
					/*
					 * Create security pipeline if needed
					 */
					for (PersBaseServerAuth<?, ?> next : theServiceDefinition.getServerAuths()) {
						if (next instanceof NamedParameterJsonRpcServerAuth) {
							NamedParameterJsonRpcCredentialGrabber grabber = ((NamedParameterJsonRpcServerAuth) next).newCredentialGrabber(jsonReader, jsonWriter);
							jsonWriter = grabber.getWrappedWriter();
							jsonReader = grabber.getWrappedReader();
							retVal.addCredentials(next, grabber);
						} else {
							jsonWriter.close();
							jsonReader.close();
							throw new InvocationRequestFailedException("Don't know how to handle server auth of type: " + next);
						}
					}

					/*
					 * client security
					 */
					for (PersBaseClientAuth<?> next : theServiceDefinition.getClientAuths()) {
						if (next instanceof NamedParameterJsonRpcClientAuth) {
							jsonWriter = ((NamedParameterJsonRpcClientAuth) next).createWrappedWriter(jsonWriter);
						}
					}

					jsonWriter.setLenient(true);
					jsonWriter.setSerializeNulls(true);
					jsonWriter.setIndent("  ");

					jsonReader.setLenient(true);

					String method = null;

					jsonReader.beginObject();
					jsonWriter.beginObject();

					while (jsonReader.hasNext()) {

						String nextName = jsonReader.nextName();
						jsonWriter.name(nextName);

						if (TOKEN_JSONRPC.equals(nextName)) {

							String rpcVal = jsonReader.nextString();
							jsonWriter.value(rpcVal);
							ourLog.debug("JsonRpc version in request: {}", rpcVal);

						} else if (TOKEN_METHOD.equals(nextName)) {

							method = jsonReader.nextString();
							jsonWriter.value(method);
							ourLog.debug("JsonRpc method name: {}", method);

						} else if (TOKEN_PARAMS.equals(nextName)) {

							jsonReader.beginJsonRpcParams();
							try {
								consumeEqually(jsonReader, jsonWriter);
							} catch (ProcessingException e) {
								throw new InvocationRequestFailedException(e);
							}
							jsonReader.endJsonRpcParams();

						} else if (TOKEN_ID.equals(nextName)) {

							String requestId = jsonReader.nextString();
							jsonWriter.value(requestId);
							ourLog.debug("JsonRpc request ID: {}", requestId);

						}

					}

					jsonReader.endObject();
					jsonWriter.endObject();

					String requestBody = stringWriter.toString();
					String contentType = "application/json";
					PersMethod methodDef = theServiceDefinition.getMethod(method);
					if (methodDef == null) {
						throw new InvocationRequestFailedException("Unknown method \"" + method + "\" for Service \"" + theServiceDefinition.getService().getServiceName() + "\"");
					}

					retVal.setResultMethod(methodDef, requestBody, contentType);
					return retVal;
				} finally {
					jsonWriter.close();
					jsonReader.close();
				}
			}
		}

	}

	@SuppressWarnings("resource")
	@Override
	public SrBeanProcessedResponse processInvocationResponse(BasePersServiceVersion theServiceDefinition, SrBeanIncomingRequest theRequest, SrBeanIncomingResponse theResponse) throws InvocationResponseFailedException {
		SrBeanProcessedResponse retVal = new SrBeanProcessedResponse();
		retVal.setResponseHeaders(theResponse.getHeaders());

		String body = theResponse.getBody();
		StringReader reader = new StringReader(body);

		ourLog.trace("JSON Response: {}", body);

		JsonReader jsonReader = new JsonReader(reader);
		try {
			jsonReader.beginObject();

			while (jsonReader.hasNext()) {

				String nextName = jsonReader.nextName();

				if (JsonRpc20ServiceInvoker.TOKEN_JSONRPC.equals(nextName)) {

					String rpcVal = jsonReader.nextString();
					ourLog.debug("JSON-RPC version in response: {}", rpcVal);

				} else if (JsonRpc20ServiceInvoker.TOKEN_RESULT.equals(nextName)) {

					JsonRpc20ServiceInvoker.consumeEqually(jsonReader);

				} else if (JsonRpc20ServiceInvoker.TOKEN_ERROR.equals(nextName)) {

					ourLog.debug("Response is fault");

					Gson gson = new GsonBuilder().create();
					JsonErrorType error = gson.fromJson(jsonReader, JsonErrorType.class);
					assert error != null;

					retVal.setResponseType(ResponseTypeEnum.FAULT);
					retVal.setResponseFaultCode(Integer.toString(error.getCode()));
					retVal.setResponseFaultDescription(error.getMessage());

				} else if (JsonRpc20ServiceInvoker.TOKEN_ID.equals(nextName)) {

					String requestId = jsonReader.nextString();
					ourLog.debug("JsonRpc request ID: {}", requestId);

				}

			}

		} catch (IOException e) {
			throw new InvocationResponseFailedException(e, "IO Error while processing response", theResponse);
		} finally {
			IOUtils.closeQuietly(jsonReader);
		}

		retVal.setResponseBody(body);
		retVal.setResponseContentType("application/json");

		if (retVal.getResponseType() == null) {
			retVal.setResponseType(ResponseTypeEnum.SUCCESS);
		}

		return retVal;
	}

	@Override
	public IResponseValidator provideInvocationResponseValidator(BasePersServiceVersion theServiceDefinition) {
		return new JsonRpc20ResponseValidator();
	}

	@SuppressWarnings("resource")
	@Override
	public String obscureMessageForLogs(BasePersServiceVersion theServiceDefinition, String theMessage, Set<String> theElementNamesToRedact) throws InvocationFailedDueToInternalErrorException {
		if (theElementNamesToRedact == null || theElementNamesToRedact.isEmpty()) {
			return theMessage;
		}

		StringReader reader = new StringReader(theMessage);

		ourLog.trace("JSON Response: {}", theMessage);

		StringWriter buf = new StringWriter();
		JsonWriter jsonWriter = new JsonWriter(buf);
		if (myPrettyPrintModeForUnitTest) {
			jsonWriter.setIndent("  ");
		}

		JsonReader jsonReader = new JsonReader(reader);
		try {
			jsonReader.beginObject();
			jsonWriter.beginObject();

			LOOP_JSON: while (true) {

				switch (jsonReader.peek()) {
				case BEGIN_ARRAY:
					jsonWriter.beginArray();
					jsonReader.beginArray();
					break;
				case BEGIN_OBJECT:
					jsonReader.beginObject();
					jsonWriter.beginObject();
					break;
				case BOOLEAN:
					jsonWriter.value(jsonReader.nextBoolean());
					break;
				case END_ARRAY:
					jsonReader.endArray();
					jsonWriter.endArray();
					break;
				case END_DOCUMENT:
					break LOOP_JSON;
				case END_OBJECT:
					jsonReader.endObject();
					jsonWriter.endObject();
					break;
				case NAME:
					String nextName = jsonReader.nextName();
					jsonWriter.name(nextName);
					if (theElementNamesToRedact.contains(nextName)) {
						consumeEqually(jsonReader);
						jsonWriter.value("**REDACTED**");
					}
					break;
				case NULL:
					jsonReader.nextNull();
					jsonWriter.nullValue();
					break;
				case NUMBER:
					consumeNumberEqually(jsonReader, jsonWriter);
					break;
				case STRING:
					jsonWriter.value(jsonReader.nextString());
					break;
				}

			}

		} catch (IOException e) {
			throw new InvocationFailedDueToInternalErrorException(e);
		} catch (ProcessingException e) {
			throw new InvocationFailedDueToInternalErrorException(e);
		} finally {
			IOUtils.closeQuietly(jsonReader);
			IOUtils.closeQuietly(jsonWriter);
		}

		return buf.toString();
	}

	void setPrettyPrintModeForUnitTest(boolean theB) {
		myPrettyPrintModeForUnitTest = theB;
	}

}
