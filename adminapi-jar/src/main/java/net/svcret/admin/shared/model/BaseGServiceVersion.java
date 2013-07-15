package net.svcret.admin.shared.model;

import java.util.Date;

public abstract class BaseGServiceVersion extends BaseGDashboardObjectWithUrls<BaseGServiceVersion> implements IProvidesUrlCount {

	private static final long serialVersionUID = 7886801527330335503L;

	private boolean myActive;
	private BaseGClientSecurityList myClientSecurityList;
	private GServiceVersionDetailedStats myDetailedStats;
	private String myExplicitProxyPath;
	private long myHttpClientConfigPid;
	private Date myLastAccess;
	private String myParentServiceName;
	private long myParentServicePid;
	private String myProxyPath;
	private GServiceVersionResourcePointerList myResourcePointerList;
	private BaseGServerSecurityList myServerSecurityList;
	private GServiceMethodList myServiceMethodList;
	private GServiceVersionUrlList myServiceUrlList;

	public BaseGServiceVersion() {
		myServiceMethodList = new GServiceMethodList();
		myServiceUrlList = new GServiceVersionUrlList();
		myServerSecurityList = new BaseGServerSecurityList();
		myClientSecurityList = new BaseGClientSecurityList();
		myResourcePointerList = new GServiceVersionResourcePointerList();
	}

	/**
	 * @return the clientSecurityList
	 */
	public BaseGClientSecurityList getClientSecurityList() {
		return myClientSecurityList;
	}

	/**
	 * @return the detailedStats
	 */
	public GServiceVersionDetailedStats getDetailedStats() {
		return myDetailedStats;
	}

	public String getExplicitProxyPath() {
		return myExplicitProxyPath;
	}

	/**
	 * @return the httpClientConfigPid
	 */
	public long getHttpClientConfigPid() {
		return myHttpClientConfigPid;
	}

	/**
	 * @return the lastAccess
	 */
	public Date getLastAccess() {
		return myLastAccess;
	}

	/**
	 * @return the serviceUrlList
	 */
	public GServiceMethodList getMethodList() {
		return myServiceMethodList;
	}

	public String getParentServiceName() {
		return myParentServiceName;
	}

	public long getParentServicePid() {
		return myParentServicePid;
	}

	public abstract ServiceProtocolEnum getProtocol();

	/**
	 * @return the proxyPath
	 */
	public String getProxyPath() {
		return myProxyPath;
	}

	/**
	 * @return the resourcePointerList
	 */
	public GServiceVersionResourcePointerList getResourcePointerList() {
		return myResourcePointerList;
	}

	/**
	 * @return the serverSecurityList
	 */
	public BaseGServerSecurityList getServerSecurityList() {
		return myServerSecurityList;
	}

	public GServiceVersionUrlList getUrlList() {
		return myServiceUrlList;
	}

	public boolean hasMethodWithName(String theName) {
		for (GServiceMethod next : myServiceMethodList) {
			if (next.getName().equals(theName)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasUrlWithName(String theUrl) {
		for (GServiceVersionUrl next : myServiceUrlList) {
			if (next.getUrl().equals(theUrl)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hideDashboardRowWhenExpanded() {
		return false;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return myActive;
	}

	@Override
	public void merge(BaseGServiceVersion theObject) {
		super.merge((BaseGDashboardObject<BaseGServiceVersion>) theObject);

		myActive = theObject.myActive;
		myLastAccess = theObject.myLastAccess;

		if (theObject.getMethodList() != null) {
			getMethodList().mergeResults(theObject.getMethodList());
		}

		if (theObject.getUrlList() != null) {
			getUrlList().mergeResults(theObject.getUrlList());
		}

		if (theObject.getServerSecurityList() != null) {
			getServerSecurityList().mergeResults(theObject.getServerSecurityList());
		}

		if (theObject.getClientSecurityList() != null) {
			getClientSecurityList().mergeResults(theObject.getClientSecurityList());
		}

		if (theObject.getResourcePointerList() != null) {
			getResourcePointerList().mergeResults(theObject.getResourcePointerList());
		}

	}

	/**
	 * @param theActive
	 *            the active to set
	 */
	public void setActive(boolean theActive) {
		myActive = theActive;
	}

	public void setDetailedStats(GServiceVersionDetailedStats theResult) {
		myDetailedStats = theResult;
	}

	public void setExplicitProxyPath(String theExplicitProxyPath) {
		myExplicitProxyPath = theExplicitProxyPath;
	}

	/**
	 * @param theHttpClientConfigPid
	 *            the httpClientConfigPid to set
	 */
	public void setHttpClientConfigPid(long theHttpClientConfigPid) {
		myHttpClientConfigPid = theHttpClientConfigPid;
	}

	/**
	 * @param theLastAccess
	 *            the lastAccess to set
	 */
	public void setLastAccess(Date theLastAccess) {
		myLastAccess = theLastAccess;
	}

	public void setParentServiceName(String theParentServiceName) {
		myParentServiceName = theParentServiceName;
	}

	public void setParentServicePid(long theParentServicePid) {
		myParentServicePid = theParentServicePid;
	}

	public void setProxyPath(String theProxyPath) {
		myProxyPath = theProxyPath;
	}

	/**
	 * @param theResourcePointerList
	 *            the resourcePointerList to set
	 */
	public void setResourcePointerList(GServiceVersionResourcePointerList theResourcePointerList) {
		myResourcePointerList = theResourcePointerList;
	}

}
