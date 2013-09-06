package net.svcret.ejb.ejb.log;

import java.util.Date;

import net.svcret.admin.shared.model.AuthorizationOutcomeEnum;
import net.svcret.ejb.api.HttpRequestBean;
import net.svcret.ejb.api.HttpResponseBean;
import net.svcret.ejb.api.InvocationResponseResultsBean;
import net.svcret.ejb.ejb.TransactionLoggerBean;
import net.svcret.ejb.model.entity.BasePersServiceVersion;
import net.svcret.ejb.model.entity.PersServiceVersionMethod;
import net.svcret.ejb.model.entity.PersServiceVersionRecentMessage;
import net.svcret.ejb.model.entity.PersServiceVersionUrl;
import net.svcret.ejb.model.entity.PersUser;
import net.svcret.ejb.util.Validate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class UnflushedServiceVersionRecentMessages extends BaseUnflushed<PersServiceVersionRecentMessage> {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UnflushedServiceVersionRecentMessages.class);

	private PersServiceVersionMethod myMethod;

	public UnflushedServiceVersionRecentMessages(PersServiceVersionMethod theMethod) {
		myMethod = theMethod;
	}

	public synchronized void recordTransaction(Date theTransactionTime, PersServiceVersionMethod theMethod, PersUser theUser, String theRequestBody,
			InvocationResponseResultsBean theInvocationResponse, HttpRequestBean theRequest, PersServiceVersionUrl theImplementationUrl, HttpResponseBean theHttpResponse,
			AuthorizationOutcomeEnum theAuthorizationOutcome, String theResponseBody) {
		Validate.notNull(theInvocationResponse);
		Validate.notNull(theMethod);
		Validate.notNull(theTransactionTime);

		initIfNeeded();

		BasePersServiceVersion svcVer = theMethod.getServiceVersion();
		Integer keepRecent = svcVer.determineKeepNumRecentTransactions(theInvocationResponse.getResponseType());

		ourLog.debug("Keeping {} recent SvcVer transactions for response type {}", keepRecent, theInvocationResponse.getResponseType());

		if (keepRecent != null && keepRecent > 0) {

			PersServiceVersionRecentMessage message = new PersServiceVersionRecentMessage();
			message.populate(theTransactionTime, theRequest, theImplementationUrl, theRequestBody, theInvocationResponse, theResponseBody);
			message.setServiceVersion(svcVer);
			message.setMethod(theMethod);
			message.setUser(theUser);
			message.setTransactionTime(theTransactionTime);
			message.setAuthorizationOutcome(theAuthorizationOutcome);

			long responseTime = theHttpResponse != null ? theHttpResponse.getResponseTime() : 0;
			message.setTransactionMillis(responseTime);

			switch (theInvocationResponse.getResponseType()) {
			case FAIL:
				getFail().add(message);
				TransactionLoggerBean.trimOldest(getFail(), keepRecent);
				break;
			case FAULT:
				getFault().add(message);
				TransactionLoggerBean.trimOldest(getFault(), keepRecent);
				break;
			case SECURITY_FAIL:
				getSecurityFail().add(message);
				TransactionLoggerBean.trimOldest(getSecurityFail(), keepRecent);
				break;
			case SUCCESS:
				getSuccess().add(message);
				TransactionLoggerBean.trimOldest(getSuccess(), keepRecent);
				break;
			case THROTTLE_REJ:
				throw new UnsupportedOperationException();
			}

		}

	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("svcVer", myMethod.getPid());
		b.append("fail", getFail().size());
		b.append("secfail", getSecurityFail().size());
		b.append("fault", getFault().size());
		b.append("success", getSuccess().size());
		return b.build();
	}

}