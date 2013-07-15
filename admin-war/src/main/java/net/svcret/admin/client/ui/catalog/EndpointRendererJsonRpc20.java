package net.svcret.admin.client.ui.catalog;

import net.svcret.admin.shared.model.GConfig;
import net.svcret.admin.shared.model.GServiceVersionJsonRpc20;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class EndpointRendererJsonRpc20 extends BaseEndpointRenderer<GServiceVersionJsonRpc20> {

	EndpointRendererJsonRpc20(GConfig theConfig) {
		super(theConfig);
	}
	
	@Override
	public Widget render(GServiceVersionJsonRpc20 theObject) {
		FlowPanel retVal=new FlowPanel();
		
		String url = getUrlBase() + theObject.getProxyPath();
		Anchor endpoint = new Anchor("Endpoint");
		endpoint.setHref(url);
		retVal.add(endpoint);
		
		return retVal;
	}


}
