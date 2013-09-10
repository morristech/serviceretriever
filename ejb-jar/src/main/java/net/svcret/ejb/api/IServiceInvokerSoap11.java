package net.svcret.ejb.api;

import net.svcret.ejb.ex.ProcessingException;
import net.svcret.ejb.model.entity.soap.PersServiceVersionSoap11;

public interface IServiceInvokerSoap11 extends IServiceInvoker {

	byte[] createWsdlBundle(PersServiceVersionSoap11 theSvcVer) throws ProcessingException;

}
