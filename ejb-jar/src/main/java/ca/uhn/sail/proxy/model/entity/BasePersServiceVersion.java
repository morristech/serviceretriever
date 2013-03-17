package ca.uhn.sail.proxy.model.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import ca.uhn.sail.proxy.api.ServiceProtocolEnum;
import ca.uhn.sail.proxy.util.Validate;

@Entity
@Table(name = "PX_SVC_VER", uniqueConstraints = { @UniqueConstraint(columnNames = { "SERVICE_PID", "VERSION_ID" }) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "AUTH_TYPE", length = 20, discriminatorType = DiscriminatorType.STRING)
public abstract class BasePersServiceVersion extends BasePersObject {
	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BasePersServiceVersion.class);

	@Column(name = "ISACTIVE")
	private boolean myActive;

	@Transient
	private transient boolean myAssociationsLoaded;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "SVC_VERSION_PID", referencedColumnName = "PID")
	@OrderColumn(name = "CAUTH_ORDER")
	private List<PersBaseClientAuth<?>> myClientAuths;

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "HTTP_CONFIG_PID", referencedColumnName = "PID", nullable=false)
	private PersHttpClientConfig myHttpClientConfig;

	@Transient
	private transient Map<String, PersServiceVersionUrl> myIdToUrl;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "SVC_VERSION_PID", referencedColumnName = "PID")
	@OrderColumn(name = "METHOD_ORDER")
	private List<PersServiceVersionMethod> myMethods;

	@Transient
	private transient Map<String, PersServiceVersionMethod> myNameToMethod;

	@Version()
	@Column(name = "OPTLOCK")
	private int myOptLock;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PID")
	private Long myPid;

	@Transient
	private transient Map<Long, PersServiceVersionUrl> myPidToUrl;

	@OneToMany(cascade = CascadeType.ALL)
	@OrderColumn(name = "SAUTH_ORDER")
	private List<PersBaseServerAuth<?,?>> myServerAuths;

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICE_PID", referencedColumnName = "PID")
	private PersService myService;

	@OneToOne(cascade = {}, fetch = FetchType.LAZY, mappedBy = "myServiceVersion", orphanRemoval = true)
	private PersServiceVersionStatus myStatus;

	@OneToMany(cascade = CascadeType.ALL)
	@MapKey(name = "myResourceUrl")
	private Map<String, PersServiceVersionResource> myUriToResource;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
	@OrderColumn(name = "URL_ORDER")
	private List<PersServiceVersionUrl> myUrls;

	@Column(name = "VERSION_ID", length = 200, nullable = false)
	private String myVersionId;

	@Transient
	private transient Map<String, PersServiceVersionUrl> myUrlToUrl;

	public void addClientAuth(int theIndex, PersBaseClientAuth<?> theAuth) {
		theAuth.setServiceVersion(this);
		getClientAuths();
		myClientAuths.add(theIndex, theAuth);
	}

	public void addClientAuth(PersBaseClientAuth<?> theAuth) {
		theAuth.setServiceVersion(this);
		getClientAuths();
		myClientAuths.add(theAuth);
	}

	public void addResource(String theUrl, String theText) {
		PersServiceVersionResource res = new PersServiceVersionResource();
		res.setResourceText(theText);
		res.setResourceUrl(theUrl);
		res.setServiceVersion(this);
		getUriToResource().put(theUrl, res);
	}

	public void addServerAuth(PersBaseServerAuth<?,?> theAuth) {
		theAuth.setServiceVersion(this);
		getServerAuths();
		myServerAuths.add(theAuth);
	}

	/**
	 * @return the clientAuths
	 */
	public List<PersBaseClientAuth<?>> getClientAuths() {
		if (myClientAuths == null) {
			myClientAuths = new ArrayList<PersBaseClientAuth<?>>();
		}
		return Collections.unmodifiableList(myClientAuths);
	}

	/**
	 * @return the httpClientConfig
	 */
	public PersHttpClientConfig getHttpClientConfig() {
		return myHttpClientConfig;
	}

	public PersServiceVersionMethod getMethod(String theName) {
		/*
		 * We avoid synchronization here at the expense of the small chance
		 * we'll create the nameToMethod map more than once..
		 */

		if (myNameToMethod == null) {
			HashMap<String, PersServiceVersionMethod> nameToMethod = new HashMap<String, PersServiceVersionMethod>();
			for (PersServiceVersionMethod next : myMethods) {
				nameToMethod.put(next.getName(), next);
			}
			myNameToMethod = nameToMethod;
			return nameToMethod.get(theName);
		}

		return myNameToMethod.get(theName);
	}

	/**
	 * @return the methods
	 */
	public List<PersServiceVersionMethod> getMethods() {
		if (myMethods == null) {
			myMethods = new ArrayList<PersServiceVersionMethod>();
		}
		return Collections.unmodifiableList(myMethods);
	}

	/**
	 * @return the versionNum
	 */
	public int getOptLock() {
		return myOptLock;
	}

	public PersServiceVersionMethod getOrCreateAndAddMethodWithName(String theName) {
		for (PersServiceVersionMethod next : getMethods()) {
			if (next.getName().equals(theName)) {
				return next;
			}
		}

		PersServiceVersionMethod method = new PersServiceVersionMethod();
		method.setName(theName);
		method.setServiceVersion(this);

		getMethods();
		myMethods.add(method);
		
		myNameToMethod = null;

		return method;
	}

	/**
	 * @return the id
	 */
	public Long getPid() {
		return myPid;
	}

	public abstract ServiceProtocolEnum getProtocol();

	public String getProxyPath() {
		PersService service = getService();
		PersDomain domain = service.getDomain();
		return "/" + domain.getDomainId() + "/" + service.getServiceId() + "/v" + getVersionId();
	}

	public PersServiceVersionResource getResourceForUri(String theUri) {
		PersServiceVersionResource res = getUriToResource().get(theUri);
		if (res != null) {
			return res;
		}
		return null;
	}

	public String getResourceTextForUri(String theUri) {
		PersServiceVersionResource res = getUriToResource().get(theUri);
		if (res != null) {
			return res.getResourceText();
		}
		return null;
	}

	public PersServiceVersionResource getResourceWithPid(long theXsdNum) {
		for (PersServiceVersionResource next : getUriToResource().values()) {
			if (next.getPid().equals(theXsdNum)) {
				return next;
			}
		}
		return null;
	}

	/**
	 * @return the serverAuths
	 */
	public List<PersBaseServerAuth<?,?>> getServerAuths() {
		if (myServerAuths == null) {
			myServerAuths = new ArrayList<PersBaseServerAuth<?,?>>();
		}
		return Collections.unmodifiableList(myServerAuths);
	}

	/**
	 * @return the service
	 */
	public PersService getService() {
		return myService;
	}

	/**
	 * Returns (and creates if neccesary) the status
	 */
	public PersServiceVersionStatus getStatus() {
		if (myStatus == null) {
			myStatus = new PersServiceVersionStatus();
		}
		return myStatus;
	}

	/**
	 * @return the uriToResource
	 */
	public Map<String, PersServiceVersionResource> getUriToResource() {
		if (myUriToResource == null) {
			myUriToResource = new HashMap<String, PersServiceVersionResource>();
		}
		return myUriToResource;
	}

	/**
	 * @return the urls
	 */
	public List<PersServiceVersionUrl> getUrls() {
		if (myUrls == null) {
			myUrls = new ArrayList<PersServiceVersionUrl>();
		}
		return myUrls;
	}

	public PersServiceVersionUrl getUrlWithId(String theString) {
		Map<String, PersServiceVersionUrl> map = myIdToUrl;
		if (map == null) {
			map = new HashMap<String, PersServiceVersionUrl>();
			for (PersServiceVersionUrl next : myUrls) {
				map.put(next.getUrlId(), next);
			}
			myIdToUrl = map;
		}
		return map.get(theString);
	}

	public PersServiceVersionUrl getUrlWithPid(long thePid) {
		Map<Long, PersServiceVersionUrl> map = myPidToUrl;
		if (map == null) {
			map = new HashMap<Long, PersServiceVersionUrl>();
			for (PersServiceVersionUrl next : myUrls) {
				map.put(next.getPid(), next);
			}
			myPidToUrl = map;
		}
		return map.get(thePid);
	}

	public PersServiceVersionUrl getUrlWithUrl(String theUrl) {
		Map<String, PersServiceVersionUrl> map = myUrlToUrl;
		if (map == null) {
			map = new HashMap<String, PersServiceVersionUrl>();
			for (PersServiceVersionUrl next : myUrls) {
				map.put(next.getUrl(), next);
			}
			myUrlToUrl = map;
		}
		return map.get(theUrl);
	}

	/**
	 * @return the versionId
	 */
	public String getVersionId() {
		return myVersionId;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return myActive;
	}

	public void loadAllAssociations() {
		if (myAssociationsLoaded) {
			return;
		}
		myAssociationsLoaded = true;

		for (PersBaseClientAuth<?> next : myClientAuths) {
			next.loadAllAssociations();
		}

		for (PersServiceVersionMethod next : myMethods) {
			next.loadAllAssociations();
		}

		for (PersServiceVersionResource next : myUriToResource.values()) {
			next.loadAllAssociations();
		}

		for (PersServiceVersionUrl next : myUrls) {
			next.loadAllAssociations();
		}
	}

	public void putMethodAtIndex(PersServiceVersionMethod theMethod, int theIndex) {
		getMethods();

		if (!myMethods.contains(theMethod)) {
			throw new IllegalArgumentException("Method[" + theMethod.getName() + "] is not in version");
		}

		if (!myMethods.get(theIndex).equals(theMethod)) {
			myMethods.remove(theIndex);
			myMethods.add(theIndex, theMethod);
		}
	}

	public void removeClientAuth(PersBaseClientAuth<?> theClientAuth) {
		theClientAuth.setServiceVersion(null);
		getClientAuths();
		myClientAuths.remove(theClientAuth);
	}

	/**
	 * Remove any URLs whose ID doesn't appear in the given IDs
	 */
	public void retainOnlyMethodsWithNames(Collection<String> theIds) {
		HashSet<String> ids = new HashSet<String>(theIds);
		for (Iterator<PersServiceVersionMethod> iter = myMethods.iterator(); iter.hasNext();) {
			PersServiceVersionMethod next = iter.next();
			if (!ids.contains(next.getName())) {
				ourLog.info("Removing Method with ID[{}] and NAME[{}] from Service Version with ID[{}/{}]", new Object[] { next.getPid(), next.getName(), getPid(), getVersionId() });
				iter.remove();
			}
		}
	}

	/**
	 * Remove any URLs whose ID doesn't appear in the given IDs
	 */
	public void retainOnlyMethodsWithNames(String... theUrlIds) {
		retainOnlyMethodsWithNames(Arrays.asList(theUrlIds));
	}

	/**
	 * Remove any URLs whose ID doesn't appear in the given IDs
	 */
	public void retainOnlyUrlsWithIds(Collection<String> theIds) {
		HashSet<String> ids = new HashSet<String>(theIds);
		for (Iterator<PersServiceVersionUrl> iter = myUrls.iterator(); iter.hasNext();) {
			PersServiceVersionUrl next = iter.next();
			if (!ids.contains(next.getUrlId())) {
				ourLog.info("Removing URL with ID[{}/{}] from Service Version with ID[{}/{}]", new Object[] { next.getPid(), next.getUrlId(), getPid(), getVersionId() });
				iter.remove();
			}
		}
		urlsChanged();
	}

	/**
	 * Remove any URLs whose ID doesn't appear in the given IDs
	 */
	public void retainOnlyUrlsWithIds(String... theUrlIds) {
		retainOnlyUrlsWithIds(Arrays.asList(theUrlIds));
	}

	/**
	 * @param theActive
	 *            the active to set
	 */
	public void setActive(boolean theActive) {
		myActive = theActive;
	}

	/**
	 * @param theHttpClientConfig the httpClientConfig to set
	 */
	public void setHttpClientConfig(PersHttpClientConfig theHttpClientConfig) {
		myHttpClientConfig = theHttpClientConfig;
	}

	/**
	 * @param theOptLock
	 *            the versionNum to set
	 */
	public void setOptLock(int theOptLock) {
		myOptLock = theOptLock;
	}

	/**
	 * @param thePid
	 *            the id to set
	 */
	public void setPid(Long thePid) {
		myPid = thePid;
	}

	/**
	 * @param theService
	 *            the service to set
	 */
	public void setService(PersService theService) {
		myService = theService;
	}

	public void setStatus(PersServiceVersionStatus theStatus) {
		Validate.throwIllegalArgumentExceptionIfNull("Status", theStatus);
		myStatus = theStatus;
	}

	/**
	 * @param theVersionId
	 *            the versionId to set
	 */
	public void setVersionId(String theVersionId) {
		myVersionId = theVersionId;
	}

	private void urlsChanged() {
		myIdToUrl = null;
		myPidToUrl = null;
		myUrlToUrl = null;
	}

}