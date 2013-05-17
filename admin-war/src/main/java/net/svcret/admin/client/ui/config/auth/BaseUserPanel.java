package net.svcret.admin.client.ui.config.auth;

import static net.svcret.admin.client.AdminPortal.*;
import net.svcret.admin.client.AdminPortal;
import net.svcret.admin.client.ui.components.CssConstants;
import net.svcret.admin.client.ui.components.LoadingSpinner;
import net.svcret.admin.client.ui.components.PButton;
import net.svcret.admin.client.ui.components.TwoColumnGrid;
import net.svcret.admin.shared.Model;
import net.svcret.admin.shared.model.BaseGAuthHost;
import net.svcret.admin.shared.model.GUser;
import net.svcret.admin.shared.util.StringUtil;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public abstract class BaseUserPanel extends FlowPanel {

	private FlowPanel myContentPanel;
	private LoadingSpinner myLoadingSpinner;
	private CheckBox myPasswordCheckbox;
	private TextBox myPasswordTextbox;
	private PermissionsPanel myPermissionsPanel;
	private GUser myUser;
	private TwoColumnGrid myUsernamePasswordGrid;
	private TextBox myUsernameTextBox;
	private TextArea myNotesTextBox;

	public BaseUserPanel() {
		FlowPanel listPanel = new FlowPanel();
		listPanel.setStylePrimaryName(CssConstants.MAIN_PANEL);
		add(listPanel);

		Label titleLabel = new Label(getPanelTitle());
		titleLabel.setStyleName(CssConstants.MAIN_PANEL_TITLE);
		listPanel.add(titleLabel);

		myContentPanel = new FlowPanel();
		myContentPanel.addStyleName(CssConstants.CONTENT_INNER_PANEL);
		listPanel.add(myContentPanel);

		myLoadingSpinner = new LoadingSpinner();
		myContentPanel.add(myLoadingSpinner);

		myLoadingSpinner.show();

	}

	public void setUser(final GUser theResult, BaseGAuthHost theAuthHost) {
		myLoadingSpinner.hideCompletely();
		myUser = theResult;
		myPermissionsPanel.setPermissions(theResult);

		myUsernamePasswordGrid.clear();

		myNotesTextBox.setText(theResult.getContactNotes());

		myUsernameTextBox = new TextBox();
		myUsernameTextBox.setValue(myUser.getUsername());
		myUsernameTextBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> theEvent) {
				theResult.setUsername(theEvent.getValue());
			}
		});
		myUsernamePasswordGrid.addRow(MSGS.editUser_Username(), myUsernameTextBox);

		if (theAuthHost.isSupportsPasswordChange()) {
			myPasswordCheckbox = new CheckBox(MSGS.editUser_Password());
			myPasswordCheckbox.setStyleName(CssConstants.FORM_LABEL);
			myPasswordCheckbox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> theEvent) {
					if (myPasswordCheckbox.getValue()) {
						myUser.setChangePassword(myPasswordTextbox.getValue());
					} else {
						myUser.setChangePassword(null);
					}
				}
			});
			myPasswordTextbox = new TextBox();
			myPasswordTextbox.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> theEvent) {
					myPasswordCheckbox.setValue(StringUtil.isNotBlank(myPasswordTextbox.getValue()));
					if (myPasswordCheckbox.getValue()) {
						myUser.setChangePassword(myPasswordTextbox.getValue());
					} else {
						myUser.setChangePassword(null);
					}
				}
			});
			myUsernamePasswordGrid.addRow(myPasswordCheckbox, myPasswordTextbox);
		}

	}

	protected abstract String getPanelTitle();

	protected void initContents() {
		myUsernamePasswordGrid = new TwoColumnGrid();
		myContentPanel.add(myUsernamePasswordGrid);

		PButton saveButton = new PButton(AdminPortal.IMAGES.iconSave(), AdminPortal.MSGS.actions_Save(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent theEvent) {
				save();
			}
		});
		myContentPanel.add(saveButton);
		myContentPanel.add(myLoadingSpinner);

		/*
		 * Contcat
		 */

		FlowPanel contactPanel = new FlowPanel();
		contactPanel.setStylePrimaryName(CssConstants.MAIN_PANEL);
		add(contactPanel);

		Label contactTitleLabel = new Label(MSGS.editUser_ContactTitle());
		contactTitleLabel.setStyleName(CssConstants.MAIN_PANEL_TITLE);
		contactPanel.add(contactTitleLabel);

		TwoColumnGrid contactGrid = new TwoColumnGrid();
		contactPanel.add(contactGrid);
		
		myNotesTextBox = new TextArea();
		myNotesTextBox.getElement().getStyle().setWidth(100, Unit.PX);
		myNotesTextBox.setVisibleLines(8);
		myNotesTextBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> theEvent) {
				myUser.setContactNotes(myNotesTextBox.getText());
			}
		});
		contactGrid.addRow(MSGS.editUser_ContactNotes(), myNotesTextBox);
		
		/*
		 * Permissions
		 */

		FlowPanel permsPanel = new FlowPanel();
		permsPanel.setStylePrimaryName(CssConstants.MAIN_PANEL);
		add(permsPanel);

		Label titleLabel = new Label(MSGS.editUser_PermissionsTitle());
		titleLabel.setStyleName(CssConstants.MAIN_PANEL_TITLE);
		permsPanel.add(titleLabel);

		myPermissionsPanel = new PermissionsPanel();
		permsPanel.add(myPermissionsPanel);

	}

	protected void save() {

		myLoadingSpinner.show();
		AdminPortal.MODEL_SVC.saveUser(myUser, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable theCaught) {
				Model.handleFailure(theCaught);
			}

			@Override
			public void onSuccess(Void theResult) {
				myLoadingSpinner.showMessage(MSGS.editUser_DoneSaving(), false);
			}
		});

	}
}