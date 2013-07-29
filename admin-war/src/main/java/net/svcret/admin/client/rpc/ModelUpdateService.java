package net.svcret.admin.client.rpc;

import java.io.Serializable;
import java.util.List;

import net.svcret.admin.shared.ServiceFailureException;
import net.svcret.admin.shared.model.AddServiceVersionResponse;
import net.svcret.admin.shared.model.BaseGAuthHost;
import net.svcret.admin.shared.model.BaseGServiceVersion;
import net.svcret.admin.shared.model.GAuthenticationHostList;
import net.svcret.admin.shared.model.GConfig;
import net.svcret.admin.shared.model.GDomain;
import net.svcret.admin.shared.model.GDomainList;
import net.svcret.admin.shared.model.GHttpClientConfig;
import net.svcret.admin.shared.model.GHttpClientConfigList;
import net.svcret.admin.shared.model.GMonitorRule;
import net.svcret.admin.shared.model.GMonitorRuleFiring;
import net.svcret.admin.shared.model.GMonitorRuleList;
import net.svcret.admin.shared.model.GPartialUserList;
import net.svcret.admin.shared.model.GRecentMessage;
import net.svcret.admin.shared.model.GRecentMessageLists;
import net.svcret.admin.shared.model.GService;
import net.svcret.admin.shared.model.GServiceVersionDetailedStats;
import net.svcret.admin.shared.model.GServiceVersionSingleFireResponse;
import net.svcret.admin.shared.model.GSoap11ServiceVersion;
import net.svcret.admin.shared.model.GUrlStatus;
import net.svcret.admin.shared.model.GUser;
import net.svcret.admin.shared.model.ModelUpdateRequest;
import net.svcret.admin.shared.model.ModelUpdateResponse;
import net.svcret.admin.shared.model.PartialUserListRequest;
import net.svcret.admin.shared.model.ServiceProtocolEnum;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("modelupdate")
public interface ModelUpdateService extends RemoteService {

	GDomain addDomain(GDomain theDomain) throws ServiceFailureException;

	GService addService(long theDomainPid, String theId, String theName, boolean theActive) throws ServiceFailureException;

	AddServiceVersionResponse addServiceVersion(Long theExistingDomainPid, String theCreateDomainId, Long theExistingServicePid, String theCreateServiceId, BaseGServiceVersion theVersion) throws ServiceFailureException;

	BaseGServiceVersion createNewServiceVersion(ServiceProtocolEnum theProtocol, Long theDomainPid, Long theServicePid, Long theUncommittedId);

	GHttpClientConfigList deleteHttpClientConfig(long thePid) throws ServiceFailureException;

	GConfig loadConfig() throws ServiceFailureException;

	ModelUpdateResponse loadModelUpdate(ModelUpdateRequest theRequest) throws ServiceFailureException;

	GMonitorRuleList loadMonitorRuleList() throws ServiceFailureException;

	GRecentMessage loadRecentMessageForServiceVersion(long thePid);

	GRecentMessage loadRecentMessageForUser(long thePid);

	GRecentMessageLists loadRecentTransactionListForServiceVersion(long theServiceVersionPid);

	GRecentMessageLists loadRecentTransactionListForuser(long thePid);

	GServiceVersionDetailedStats loadServiceVersionDetailedStats(long theVersionPid) throws ServiceFailureException;

	BaseGServiceVersion loadServiceVersionIntoSession(long theServiceVersionPid) throws ServiceFailureException;

	List<GUrlStatus> loadServiceVersionUrlStatuses(long theServiceVersionPid);

	UserAndAuthHost loadUser(long theUserPid, boolean theLoadStats) throws ServiceFailureException;

	GPartialUserList loadUsers(PartialUserListRequest theRequest) throws ServiceFailureException;

	GSoap11ServiceVersion loadWsdl(GSoap11ServiceVersion theService, String theWsdlUrl) throws ServiceFailureException;

	GAuthenticationHostList removeAuthenticationHost(long thePid) throws ServiceFailureException;

	GDomainList removeDomain(long thePid) throws ServiceFailureException;

	GDomainList removeService(long theDomainPid, long theServicePid) throws ServiceFailureException;

	GDomainList removeServiceVersion(long thePid) throws ServiceFailureException;

	void reportClientError(String theMessage, Throwable theException);

	GAuthenticationHostList saveAuthenticationHost(BaseGAuthHost theAuthHost) throws ServiceFailureException;

	void saveConfig(GConfig theConfig) throws ServiceFailureException;

	GDomainList saveDomain(GDomain theDomain) throws ServiceFailureException;

	GHttpClientConfig saveHttpClientConfig(boolean theCreate, GHttpClientConfig theConfig) throws ServiceFailureException;

	GMonitorRuleList saveMonitorRule(GMonitorRule theRule) throws ServiceFailureException;

	GDomainList saveService(GService theService) throws ServiceFailureException;

	void saveServiceVersionToSession(BaseGServiceVersion theServiceVersion);

	void saveUser(GUser theUser) throws ServiceFailureException;

	GServiceVersionSingleFireResponse testServiceVersionWithSingleMessage(String theMessageText, long thePid) throws ServiceFailureException;

	public static class UserAndAuthHost implements Serializable {
		private static final long serialVersionUID = 1L;

		private BaseGAuthHost myAuthHost;
		private GUser myUser;

		/**
		 * Constructor
		 */
		public UserAndAuthHost() {
			super();
		}

		/**
		 * Constructor
		 */
		public UserAndAuthHost(GUser theUser, BaseGAuthHost theAuthHost) {
			super();
			myUser = theUser;
			myAuthHost = theAuthHost;
		}

		/**
		 * @return the authHost
		 */
		public BaseGAuthHost getAuthHost() {
			return myAuthHost;
		}

		/**
		 * @return the user
		 */
		public GUser getUser() {
			return myUser;
		}

		/**
		 * @param theAuthHost
		 *            the authHost to set
		 */
		public void setAuthHost(BaseGAuthHost theAuthHost) {
			myAuthHost = theAuthHost;
		}

		/**
		 * @param theUser
		 *            the user to set
		 */
		public void setUser(GUser theUser) {
			myUser = theUser;
		}
	}

	List<GMonitorRuleFiring> loadMonitorRuleFirings(Long theDomainPid, Long theServicePid, Long theServiceVersionPid, int theStart);

}
