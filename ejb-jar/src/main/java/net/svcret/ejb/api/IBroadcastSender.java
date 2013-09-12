package net.svcret.ejb.api;

import javax.ejb.Local;

import net.svcret.ejb.ex.ProcessingException;
import net.svcret.ejb.model.entity.PersStickySessionUrlBinding;

@Local
public interface IBroadcastSender {

	void notifyUserCatalogChanged() throws ProcessingException;
	
	void notifyServiceCatalogChanged() throws ProcessingException;

	void notifyConfigChanged() throws ProcessingException;

	void monitorRulesChanged() throws ProcessingException;

	void notifyUrlStatusChanged(Long thePid) throws ProcessingException;

	void notifyNewStickySession(PersStickySessionUrlBinding theExisting) throws ProcessingException;
	
}
