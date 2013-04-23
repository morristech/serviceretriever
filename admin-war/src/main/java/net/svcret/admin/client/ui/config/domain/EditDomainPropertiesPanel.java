package net.svcret.admin.client.ui.config.domain;

import net.svcret.admin.client.AdminPortal;
import net.svcret.admin.shared.Model;
import net.svcret.admin.shared.model.GDomain;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class EditDomainPropertiesPanel extends FlowPanel {

	private GDomain myDomain;
	private EditDomainBasicPropertiesPanel myEditDomainBasicPropertiesPanel;

	public EditDomainPropertiesPanel(GDomain theDomain) {
		myDomain = new GDomain();
		myDomain.mergeSimple(theDomain);

		setStylePrimaryName("mainPanel");

		Label titleLabel = new Label("Edit Domain");
		titleLabel.setStyleName("mainPanelTitle");
		add(titleLabel);

		FlowPanel contentPanel = new FlowPanel();
		contentPanel.addStyleName("contentInnerPanel");
		add(contentPanel);

		contentPanel.add(new Label(AddDomainPanel.DOMAIN_DESC));

		myEditDomainBasicPropertiesPanel = new EditDomainBasicPropertiesPanel(myDomain, "Save", new MySaveButtonHandler(), AdminPortal.IMAGES.iconSave());
		contentPanel.add(myEditDomainBasicPropertiesPanel);

	}

	

	public class MySaveButtonHandler implements ClickHandler, AsyncCallback<GDomain> {

		@Override
		public void onClick(ClickEvent theEvent) {
			if (myEditDomainBasicPropertiesPanel.validateValues()) {
				myEditDomainBasicPropertiesPanel.showMessage("Saving Domain...", true);
				AdminPortal.MODEL_SVC.saveDomain(myDomain, this);
			}
		}

		@Override
		public void onFailure(Throwable theCaught) {
			myEditDomainBasicPropertiesPanel.hideSpinner();
			myEditDomainBasicPropertiesPanel.showError(theCaught.getMessage());
		}

		@Override
		public void onSuccess(GDomain theResult) {
			Model.getInstance().addDomain(theResult);
			myEditDomainBasicPropertiesPanel.showMessage("Domain has been saved", false);
		}

	}

}