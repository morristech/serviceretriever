package net.svcret.admin.shared.model;

import java.util.ArrayList;
import java.util.List;

public class GUserServiceVersionPermission extends BaseGObject<GUserServiceVersionPermission> {

	private static final long serialVersionUID = 1L;

	private boolean myAllowAllServiceVersionMethods;
	private List<GUserServiceVersionMethodPermission> myServiceVersionMethodPermissions;
	private long myServiceVersionPid;

	public GUserServiceVersionMethodPermission getOrCreateServiceVersionMethodPermission(long theServiceVersionMethodPid) {
		for (GUserServiceVersionMethodPermission next : getServiceVersionMethodPermissions()) {
			if (next.getServiceVersionMethodPid() == theServiceVersionMethodPid) {
				return next;
			}
		}
		GUserServiceVersionMethodPermission permission = new GUserServiceVersionMethodPermission();
		permission.setServiceVersionMethodPid(theServiceVersionMethodPid);
		myServiceVersionMethodPermissions.add(permission);
		return permission;
	}

	/**
	 * @return the ServiceVersionPermissions
	 */
	public List<GUserServiceVersionMethodPermission> getServiceVersionMethodPermissions() {
		if (myServiceVersionMethodPermissions == null) {
			myServiceVersionMethodPermissions = new ArrayList<GUserServiceVersionMethodPermission>();
		}
		return myServiceVersionMethodPermissions;
	}

	/**
	 * @return the ServiceVersionPid
	 */
	public long getServiceVersionPid() {
		return myServiceVersionPid;
	}

	/**
	 * @return the allowAllServiceVersions
	 */
	public boolean isAllowAllServiceVersionMethods() {
		return myAllowAllServiceVersionMethods;
	}

	@Override
	public void merge(GUserServiceVersionPermission theObject) {
		setPid(theObject.getPid());
		setAllowAllServiceVersionMethods(theObject.isAllowAllServiceVersionMethods());
		setServiceVersionPid(theObject.getServiceVersionPid());
		setServiceVersionMethodPermissions(theObject.getServiceVersionMethodPermissions());
	}

	/**
	 * @param theAllowAllServiceVersionMethods
	 *            the allowAllServiceVersions to set
	 */
	public void setAllowAllServiceVersionMethods(boolean theAllowAllServiceVersionMethods) {
		myAllowAllServiceVersionMethods = theAllowAllServiceVersionMethods;
	}

	/**
	 * @param theServiceVersionMethodPermissions
	 *            the ServiceVersionPermissions to set
	 */
	public void setServiceVersionMethodPermissions(List<GUserServiceVersionMethodPermission> theServiceVersionMethodPermissions) {
		myServiceVersionMethodPermissions = theServiceVersionMethodPermissions;
	}

	/**
	 * @param theServiceVersionPid
	 *            the ServiceVersionPid to set
	 */
	public void setServiceVersionPid(long theServiceVersionPid) {
		myServiceVersionPid = theServiceVersionPid;
	}

}
