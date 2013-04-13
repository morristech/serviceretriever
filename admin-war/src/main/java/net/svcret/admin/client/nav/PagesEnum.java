package net.svcret.admin.client.nav;
import static net.svcret.admin.client.AdminPortal.MSGS;

public enum PagesEnum {

	/** Add domain step 2 */
	AD2(MSGS.addDomainStep2_Breadcrumb()),

	/** Add domain */
	ADD(MSGS.addDomain_Breadcrumb()),

	/** Authentication Host List */
	AHL(MSGS.authenticationHostsPanel_Breadcrumb()),

	/** Add service */
	ASE(MSGS.addService_Breadcrumb()),

	/** Add service version */
	ASV(MSGS.addServiceVersion_Breadcrumb()),

	/** Add service version step 2 */
	AV2(MSGS.addServiceVersionStep2_Breadcrumb()),

	/** Service dashboard */
	DSH(MSGS.serviceDashboard_Breadcrumb()),

	/** Edit domain */
	EDO(MSGS.editDomain_Breadcrumb()),

	/** Edit user */
	EDU(MSGS.editUser_Dashboard()),
	
	/** Edit User List */
	EUL(MSGS.editUsersPanel_Dashboard()),

	/** Edit HTTP client configs */
	HCC(MSGS.httpClientConfigsPanel_Dashboard())

	;
	
	private String myBreadcrumb;

	private PagesEnum(String theBreadcrumb) {
		myBreadcrumb = theBreadcrumb;
	}

	/**
	 * @return the breadcrumb
	 */
	public String getBreadcrumb() {
		return myBreadcrumb;
	}
	
}