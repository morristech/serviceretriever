package net.svcret.admin.client.rpc;

import net.svcret.admin.client.rpc.ModelUpdateService.UserAndAuthHost;
import net.svcret.admin.shared.model.AddServiceVersionResponse;
import net.svcret.admin.shared.model.GAuthenticationHostList;
import net.svcret.admin.shared.model.GDomain;
import net.svcret.admin.shared.model.GHttpClientConfig;
import net.svcret.admin.shared.model.GHttpClientConfigList;
import net.svcret.admin.shared.model.GLocalDatabaseAuthHost;
import net.svcret.admin.shared.model.GPartialUserList;
import net.svcret.admin.shared.model.GService;
import net.svcret.admin.shared.model.GSoap11ServiceVersion;
import net.svcret.admin.shared.model.GUser;
import net.svcret.admin.shared.model.ModelUpdateRequest;
import net.svcret.admin.shared.model.ModelUpdateResponse;
import net.svcret.admin.shared.model.PartialUserListRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ModelUpdateServiceAsync {

	void loadModelUpdate(ModelUpdateRequest theRequest, AsyncCallback<ModelUpdateResponse> callback);

	void addDomain(String theId, String theName, AsyncCallback<GDomain> callback);

	void saveDomain(long thePid, String theId, String theName, AsyncCallback<GDomain> callback);

	void addService(long theDomainPid, String theId, String theName, boolean theActive, AsyncCallback<GService> theCallback);

	void loadWsdl(GSoap11ServiceVersion theService, String theWsdlUrl, AsyncCallback<GSoap11ServiceVersion> callback);

	void saveServiceVersionToSession(GSoap11ServiceVersion theServiceVersion, AsyncCallback<Void> theCallback);

	void createNewSoap11ServiceVersion(Long theUncommittedId, AsyncCallback<GSoap11ServiceVersion> theCallback);

	void reportClientError(String theMessage, Throwable theException, AsyncCallback<Void> callback);

	void addServiceVersion(Long theExistingDomainPid, String theCreateDomainId, Long theExistingServicePid, String theCreateServiceId, GSoap11ServiceVersion theVersion, AsyncCallback<AddServiceVersionResponse> theCallback);

	void saveHttpClientConfig(boolean theCreate, GHttpClientConfig theConfig, AsyncCallback<GHttpClientConfig> theAsyncCallback);

	void deleteHttpClientConfig(long thePid, AsyncCallback<GHttpClientConfigList> theCallback);

	void saveAuthenticationHost(GLocalDatabaseAuthHost theAuthHost, AsyncCallback<GAuthenticationHostList> theCallback);

	void removeAuthenticationHost(long thePid, AsyncCallback<GAuthenticationHostList> theAsyncCallback);

	void loadUsers(PartialUserListRequest theRequest, AsyncCallback<GPartialUserList> callback);

	void loadUser(long theUserPid, AsyncCallback<UserAndAuthHost> callback);

	void saveUser(GUser theUser, AsyncCallback<Void> theAsyncCallback);

}
