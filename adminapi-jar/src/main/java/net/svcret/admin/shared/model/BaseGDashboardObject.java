package net.svcret.admin.shared.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public abstract class BaseGDashboardObject extends BaseDtoKeepsRecentMessages {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="runtime_AvgFailLatencyOverLast60Min")
	private double myAverageFailTransactionsPerMin60min;
	
	@XmlElement(name="runtime_AvgFaultLatencyOverLast60Min")
	private double myAverageFaultTransactionsPerMin60min;

	@XmlElement(name="runtime_AvgSuccessLatencyOverLast60Min")
	private int myAverageLatency60min;
	
	@XmlElement(name="runtime_AvgSecurityFailLatencyOverLast60Min")
	private double myAverageSecurityFailTransactionsPerMin60min;
	
	@XmlElement(name="runtime_AvgTransactionsPerMinuteOverLast60Min")
	private double myAverageTransactionsPerMin60min;
	
	@XmlTransient
	private boolean myExpandedOnDashboard;

	@XmlElement(name="config_Id")
	private String myId;
	
	@XmlElement(name="runtime_LastServerSecurityFailure")
	private Date myLastServerSecurityFailure;

	@XmlElement(name="runtime_LastSuccessfulInvocation")
	private Date myLastSuccessfulInvocation;
	
	@XmlElement(name="runtime_LatencyInMillisOver60Mins")
	private DtoIntArray myLatency60mins;
	
	@XmlElement(name="runtime_MaxFailLatencyOverLast60Min")
	private long myMaxFailTransactionsPerMin60min;

	@XmlElement(name="runtime_MaxFaultLatencyOverLast60Min")
	private long myMaxFaultTransactionsPerMin60min;

	@XmlElement(name="runtime_MaxLatencyInMillisOver60Mins")
	private int myMaxLatency60min;

	@XmlElement(name="runtime_MaxSecurityFailLatencyOverLast60Min")
	private long myMaxSecurityFailTransactionsPerMin60min;

	@XmlElement(name="runtime_MaxTransactionsPerMinOver60Mins")
	private double myMaxTransactionsPerMin60min;

	@XmlElement(name="config_Name")
	private String myName;

	@XmlElement(name="runtime_StatsInitialized")
	private Date myStatsInitialized;

	@XmlElement(name="runtime_Status")
	private StatusEnum myStatus;

	@XmlElement(name="runtime_TransactionSuccessCountOver60Mins")
	private DtoIntArray myTransactions60mins;

	@XmlElement(name="runtime_TransactionFailCountOver60Mins")
	private DtoIntArray myTransactionsFail60mins;

	@XmlElement(name="runtime_TransactionFaultCountOver60Mins")
	private DtoIntArray myTransactionsFault60mins;

	@XmlElement(name="runtime_TransactionSecurityFailCountOver60Mins")
	private DtoIntArray myTransactionsSecurityFail60mins;

	public void flushStats() {
		myStatsInitialized=null;
	}

	public double getAverageFailTransactionsPerMin60min() {
		return myAverageFailTransactionsPerMin60min;
	}

	public double getAverageFaultTransactionsPerMin60min() {
		return myAverageFaultTransactionsPerMin60min;
	}

	/**
	 * @return the averageLatency
	 */
	public int getAverageLatency60min() {
		return myAverageLatency60min;
	}

	public double getAverageSecurityFailTransactionsPerMin60min() {
		return myAverageSecurityFailTransactionsPerMin60min;
	}

	/**
	 * @return the averageTransactionsPerMin
	 */
	public double getAverageTransactionsPerMin60min() {
		return myAverageTransactionsPerMin60min;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return myId;
	}

	/**
	 * @return the lastServerSecurityFailure
	 */
	public Date getLastServerSecurityFailure() {
		return myLastServerSecurityFailure;
	}

	/**
	 * @return the lastSuccessfulInvocation
	 */
	public Date getLastSuccessfulInvocation() {
		return myLastSuccessfulInvocation;
	}

	/**
	 * @return the latency60mins
	 */
	public int[] getLatency60mins() {
		return DtoIntArray.from(myLatency60mins);
	}

	public long getMaxFailTransactionsPerMin60min() {
		return myMaxFailTransactionsPerMin60min;
	}

	public long getMaxFaultTransactionsPerMin60min() {
		return myMaxFaultTransactionsPerMin60min;
	}

	/**
	 * @return the averageLatency
	 */
	public int getMaxLatency60min() {
		return myMaxLatency60min;
	}

	public long getMaxSecurityFailTransactionsPerMin60min() {
		return myMaxSecurityFailTransactionsPerMin60min;
	}

	/**
	 * @return the averageTransactionsPerMin
	 */
	public double getMaxTransactionsPerMin60min() {
		return myMaxTransactionsPerMin60min;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return myName;
	}

	/**
	 * @return the statsInitialized
	 */
	public Date getStatsInitialized() {
		return myStatsInitialized;
	}

	/**
	 * @return the status
	 */
	public StatusEnum getStatus() {
		return myStatus;
	}

	/**
	 * @return the transactions60mins
	 */
	public int[] getTransactions60mins() {
		return DtoIntArray.from(myTransactions60mins);
	}

	public int[] getTransactionsFail60mins() {
		return DtoIntArray.from(myTransactionsFail60mins);
	}
	
	
	public int[] getTransactionsFault60mins() {
		return DtoIntArray.from(myTransactionsFault60mins);
	}

	public int[] getTransactionsSecurityFail60mins() {
		return DtoIntArray.from(myTransactionsSecurityFail60mins);
	}

	/**
	 * Do we need this? Maybe make it configurable
	 */
	public boolean hideDashboardRowWhenExpanded() {
		return true;
	}

	/**
	 * @return the expandedOnDashboard
	 */
	public boolean isExpandedOnDashboard() {
		return myExpandedOnDashboard;
	}

	/**
	 * @return the statsInitialized
	 */
	public boolean isStatsInitialized() {
		return myStatsInitialized!=null;
	}

	@Override
	public void merge(BaseGObject theObject) {
		super.merge(theObject);
		
		BaseGDashboardObject obj = (BaseGDashboardObject)theObject;
		setId(obj.getId());
		setName(obj.getName());
		
		if (obj.isStatsInitialized()) {
			setStatsInitialized(obj.getStatsInitialized());
			setStatus(obj.getStatus());
			setTransactions60mins(obj.getTransactions60mins());
			setLatency60mins(obj.getLatency60mins());
			setLastServerSecurityFailure(obj.getLastServerSecurityFailure());
			setLastSuccessfulInvocation(obj.getLastSuccessfulInvocation());
		}
	}

	/**
	 * @param theExpandedOnDashboard
	 *            the expandedOnDashboard to set
	 */
	public void setExpandedOnDashboard(boolean theExpandedOnDashboard) {
		myExpandedOnDashboard = theExpandedOnDashboard;
	}

	/**
	 * @param theId
	 *            the id to set
	 */
	public void setId(String theId) {
		myId = theId;
	}

	/**
	 * @param theLastServerSecurityFailure
	 *            the lastServerSecurityFailure to set
	 */
	public void setLastServerSecurityFailure(Date theLastServerSecurityFailure) {
		myLastServerSecurityFailure = theLastServerSecurityFailure;
	}

	/**
	 * @param theLastSuccessfulInvocation
	 *            the lastSuccessfulInvocation to set
	 */
	public void setLastSuccessfulInvocation(Date theLastSuccessfulInvocation) {
		myLastSuccessfulInvocation = theLastSuccessfulInvocation;
	}

	/**
	 * @param theLatency60mins
	 *            the latency60mins to set
	 */
	public void setLatency60mins(int[] theLatency60mins) {
		myLatency60mins = DtoIntArray.to(theLatency60mins);

		int count = 0;
		int total = 0;
		int max = 0;
		for (int i : theLatency60mins) {
			if (i > 0) {
				total++;
				count += i;
			}
			max = Math.max(max, i);
		}
		if (total > 0) {
			myAverageLatency60min = count / total;
		}
		myMaxLatency60min = max;
	}

	/**
	 * @param theName
	 *            the name to set
	 */
	public void setName(String theName) {
		myName = theName;
	}

	/**
	 * @param theStatsInitialized
	 *            the statsInitialized to set
	 */
	public void setStatsInitialized(Date theStatsInitialized) {
		myStatsInitialized = theStatsInitialized;
	}

	/**
	 * @param theStatus
	 *            the status to set
	 */
	public void setStatus(StatusEnum theStatus) {
		if (theStatus == null) {
			throw new NullPointerException("Status can not be null");
		}
		myStatus = theStatus;
	}

	/**
	 * @param theTransactions60mins
	 *            the transactions60mins to set
	 */
	public void setTransactions60mins(int[] theTransactions60mins) {
		myTransactions60mins = DtoIntArray.to(theTransactions60mins);
		long total = 0;
		long max = 0;
		for (int i : theTransactions60mins) {
			total += i;
			max = Math.max(max, i);
		}
		if (theTransactions60mins.length > 0) {
			myAverageTransactionsPerMin60min = ((double)total / (double)theTransactions60mins.length);
			myMaxTransactionsPerMin60min = max;
		}
	}

	/**
	 * @param theTransactionsFail60mins
	 *            the transactions60mins to set
	 */
	public void setTransactionsFail60mins(int[] theTransactionsFail60mins) {
		myTransactionsFail60mins = DtoIntArray.to(theTransactionsFail60mins);
		long total = 0;
		long max = 0;
		for (int i : theTransactionsFail60mins) {
			total += i;
			max = Math.max(max, i);
		}
		if (theTransactionsFail60mins.length > 0) {
			myAverageFailTransactionsPerMin60min = ((double)total / (double)theTransactionsFail60mins.length);
			myMaxFailTransactionsPerMin60min = max;
		}
	}

	/**
	 * @param theTransactionsFault60mins
	 *            the transactions60mins to set
	 */
	public void setTransactionsFault60mins(int[] theTransactionsFault60mins) {
		myTransactionsFault60mins = DtoIntArray.to(theTransactionsFault60mins);
		long total = 0;
		long max = 0;
		for (int i : theTransactionsFault60mins) {
			total += i;
			max = Math.max(max, i);
		}
		if (theTransactionsFault60mins.length > 0) {
			myAverageFaultTransactionsPerMin60min = ((double)total / (double)theTransactionsFault60mins.length);
			myMaxFaultTransactionsPerMin60min = max;
		}
	}

	/**
	 * @param theTransactionsSecurityFail60mins
	 *            the transactions60mins to set
	 */
	public void setTransactionsSecurityFail60mins(int[] theTransactionsSecurityFail60mins) {
		myTransactionsSecurityFail60mins = DtoIntArray.to(theTransactionsSecurityFail60mins);
		long total = 0;
		long max = 0;
		for (int i : theTransactionsSecurityFail60mins) {
			total += i;
			max = Math.max(max, i);
		}
		if (theTransactionsSecurityFail60mins.length > 0) {
			myAverageSecurityFailTransactionsPerMin60min = ((double)total / (double)theTransactionsSecurityFail60mins.length);
			myMaxSecurityFailTransactionsPerMin60min = max;
		}
	}

}
