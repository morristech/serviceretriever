package net.svcret.ejb.model.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import net.svcret.admin.shared.model.BaseGKeepsRecentMessages;
import net.svcret.ejb.api.ResponseTypeEnum;

@MappedSuperclass
public abstract class BasePersKeepsRecentTransactions extends BasePersObject {

	private static final long serialVersionUID = 1L;

	@Column(name = "KEEP_RCNT_NUM_FAIL", nullable = true)
	private Integer myKeepNumRecentTransactionsFail;

	@Column(name = "KEEP_RCNT_NUM_FAULT", nullable = true)
	private Integer myKeepNumRecentTransactionsFault;

	@Column(name = "KEEP_RCNT_NUM_SECFAIL", nullable = true)
	private Integer myKeepNumRecentTransactionsSecurityFail;

	@Column(name = "KEEP_RCNT_NUM_SUC", nullable = true)
	private Integer myKeepNumRecentTransactionsSuccess;

	public Integer determineKeepNumRecentTransactions(ResponseTypeEnum theResultType) {
		switch (theResultType) {
		case FAIL:
			return myKeepNumRecentTransactionsFail;
		case FAULT:
			return myKeepNumRecentTransactionsFault;
		case SECURITY_FAIL:
			return myKeepNumRecentTransactionsSecurityFail;
		case SUCCESS:
			return myKeepNumRecentTransactionsSuccess;
		}

		throw new IllegalStateException("Unknown type: " + theResultType);
	}

	/**
	 * @return the keepNumRecentTransactionsFail
	 */
	public Integer getKeepNumRecentTransactionsFail() {
		return myKeepNumRecentTransactionsFail;
	}

	/**
	 * @return the keepNumRecentTransactionsFault
	 */
	public Integer getKeepNumRecentTransactionsFault() {
		return myKeepNumRecentTransactionsFault;
	}

	/**
	 * @return the keepNumRecentTransactionsSecurityFail
	 */
	public Integer getKeepNumRecentTransactionsSecurityFail() {
		return myKeepNumRecentTransactionsSecurityFail;
	}

	/**
	 * @return the keepNumRecentTransactionsSuccess
	 */
	public Integer getKeepNumRecentTransactionsSuccess() {
		return myKeepNumRecentTransactionsSuccess;
	}

	/**
	 * @param theKeepNumRecentTransactionsFail
	 *            the keepNumRecentTransactionsFail to set
	 */
	public void setKeepNumRecentTransactionsFail(Integer theKeepNumRecentTransactionsFail) {
		myKeepNumRecentTransactionsFail = theKeepNumRecentTransactionsFail;
	}

	/**
	 * @param theKeepNumRecentTransactionsFault
	 *            the keepNumRecentTransactionsFault to set
	 */
	public void setKeepNumRecentTransactionsFault(Integer theKeepNumRecentTransactionsFault) {
		myKeepNumRecentTransactionsFault = theKeepNumRecentTransactionsFault;
	}

	/**
	 * @param theKeepNumRecentTransactionsSecurityFail
	 *            the keepNumRecentTransactionsSecurityFail to set
	 */
	public void setKeepNumRecentTransactionsSecurityFail(Integer theKeepNumRecentTransactionsSecurityFail) {
		myKeepNumRecentTransactionsSecurityFail = theKeepNumRecentTransactionsSecurityFail;
	}

	/**
	 * @param theKeepNumRecentTransactionsSuccess
	 *            the keepNumRecentTransactionsSuccess to set
	 */
	public void setKeepNumRecentTransactionsSuccess(Integer theKeepNumRecentTransactionsSuccess) {
		myKeepNumRecentTransactionsSuccess = theKeepNumRecentTransactionsSuccess;
	}

	public void populateKeepRecentTransactionsToDto(BaseGKeepsRecentMessages<?> theDto) {
		theDto.setKeepNumRecentTransactionsFail(this.getKeepNumRecentTransactionsFail());
		theDto.setKeepNumRecentTransactionsSecurityFail(this.getKeepNumRecentTransactionsSecurityFail());
		theDto.setKeepNumRecentTransactionsFault(this.getKeepNumRecentTransactionsFault());
		theDto.setKeepNumRecentTransactionsSuccess(this.getKeepNumRecentTransactionsSuccess());
	}

	public void populateKeepRecentTransactionsFromDto(BaseGKeepsRecentMessages<?> theDto) {
		setKeepNumRecentTransactionsFail(theDto.getKeepNumRecentTransactionsFail());
		setKeepNumRecentTransactionsSecurityFail(theDto.getKeepNumRecentTransactionsSecurityFail());
		setKeepNumRecentTransactionsFault(theDto.getKeepNumRecentTransactionsFault());
		setKeepNumRecentTransactionsSuccess(theDto.getKeepNumRecentTransactionsSuccess());
	}

}
