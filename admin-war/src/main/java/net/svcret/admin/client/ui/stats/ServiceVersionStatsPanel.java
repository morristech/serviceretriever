package net.svcret.admin.client.ui.stats;

import static net.svcret.admin.client.AdminPortal.*;

import java.util.Date;
import java.util.List;

import net.svcret.admin.client.AdminPortal;
import net.svcret.admin.client.ui.components.CssConstants;
import net.svcret.admin.client.ui.components.HtmlH1;
import net.svcret.admin.client.ui.components.LoadingSpinner;
import net.svcret.admin.client.ui.dash.model.BaseDashModel;
import net.svcret.admin.shared.IAsyncLoadCallback;
import net.svcret.admin.shared.Model;
import net.svcret.admin.shared.model.BaseGServiceVersion;
import net.svcret.admin.shared.model.GUrlStatus;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class ServiceVersionStatsPanel extends FlowPanel {

	private static DateTimeFormat ourDateTimeFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM);
	private LoadingSpinner myLoadingSpinner;
	private long myServiceVersionPid;
	private Label myTitleLabel;
	private FlowPanel myTopPanel;

	public ServiceVersionStatsPanel(long theDomainPid, long theServicePid, long theVersionPid) {
		myServiceVersionPid = theVersionPid;

		myTopPanel = new FlowPanel();
		add(myTopPanel);

		myTopPanel.setStylePrimaryName(CssConstants.MAIN_PANEL);

		myTitleLabel = new Label(MSGS.serviceVersionStats_Title(""));
		myTitleLabel.setStyleName(CssConstants.MAIN_PANEL_TITLE);
		myTopPanel.add(myTitleLabel);

		myLoadingSpinner = new LoadingSpinner();
		myLoadingSpinner.show();
		myTopPanel.add(myLoadingSpinner);

		Model.getInstance().loadServiceVersion(theDomainPid, theServicePid, theVersionPid, new IAsyncLoadCallback<BaseGServiceVersion>() {
			@Override
			public void onSuccess(BaseGServiceVersion theResult) {
				set01ServiceVersion(theResult);
			}
		});

	}

	private String renderDate(Date theDate) {
		if (theDate == null) {
			return null;
		}
		return ourDateTimeFormat.format(theDate);
	}

	private void set01ServiceVersion(BaseGServiceVersion theResult) {
		myTitleLabel.setText(MSGS.serviceVersionStats_Title(theResult.getName()));

		HtmlH1 urlsLabel = new HtmlH1(MSGS.serviceVersionStats_UrlsTitle());
		myTopPanel.add(urlsLabel);

		AdminPortal.MODEL_SVC.loadServiceVersionUrlStatuses(myServiceVersionPid, new AsyncCallback<List<GUrlStatus>>() {

			@Override
			public void onFailure(Throwable theCaught) {
				Model.handleFailure(theCaught);
			}

			@Override
			public void onSuccess(List<GUrlStatus> theUrlStatuses) {
				set02UrlStatuses(theUrlStatuses);
			}
		});

	}

	private void set02UrlStatuses(List<GUrlStatus> theUrlStatuses) {

		Grid urlGrid = new Grid(theUrlStatuses.size() + 1, 8);
		myTopPanel.add(urlGrid);
		urlGrid.addStyleName(CssConstants.PROPERTY_TABLE);

		int URLTBL_COL_URL = 0;
		int URLTBL_COL_STATUS = 1;
		int URLTBL_COL_LAST_SUCCESS = 2;
		int URLTBL_COL_LAST_SUCCESS_MSG = 3;
		int URLTBL_COL_LAST_FAULT = 4;
		int URLTBL_COL_LAST_FAULT_MSG = 5;
		int URLTBL_COL_LAST_FAILURE = 6;
		int URLTBL_COL_LAST_FAILURE_MSG = 7;

		urlGrid.setText(0, URLTBL_COL_URL, "URL");
		urlGrid.setText(0, URLTBL_COL_STATUS, "Status");
		urlGrid.setText(0, URLTBL_COL_LAST_SUCCESS, "Last Success");
		urlGrid.setText(0, URLTBL_COL_LAST_SUCCESS_MSG, "Message");
		urlGrid.setText(0, URLTBL_COL_LAST_FAULT, "Last Fault");
		urlGrid.setText(0, URLTBL_COL_LAST_FAULT_MSG, "Message");
		urlGrid.setText(0, URLTBL_COL_LAST_FAILURE, "Last Failure");
		urlGrid.setText(0, URLTBL_COL_LAST_FAILURE_MSG, "Message");

		for (int i = 0; i < theUrlStatuses.size(); i++) {
			GUrlStatus status = theUrlStatuses.get(i);

			urlGrid.setText(i + 1, URLTBL_COL_URL, status.getUrl());

			HorizontalPanel statusPanel = new HorizontalPanel();
			statusPanel.setStyleName(CssConstants.UNSTYLED_TABLE);
			statusPanel.add(BaseDashModel.returnImageForStatus(status.getStatus()));
			switch (status.getStatus()) {
			case ACTIVE:
				statusPanel.add(new Label("Ok"));
				break;
			case DOWN:
				statusPanel.add(new Label("Down"));
				break;
			case UNKNOWN:
				statusPanel.add(new Label("Unknown (no requests)"));
				break;
			}
			urlGrid.setWidget(i + 1, URLTBL_COL_STATUS, statusPanel);

			urlGrid.setText(i + 1, URLTBL_COL_LAST_SUCCESS, renderDate(status.getLastSuccess()));
			urlGrid.setText(i + 1, URLTBL_COL_LAST_SUCCESS_MSG, status.getLastSuccessMessage());
			urlGrid.setText(i + 1, URLTBL_COL_LAST_FAULT, renderDate(status.getLastFault()));
			urlGrid.setText(i + 1, URLTBL_COL_LAST_FAULT_MSG, status.getLastFaultMessage());
			urlGrid.setText(i + 1, URLTBL_COL_LAST_FAILURE, renderDate(status.getLastFailure()));
			urlGrid.setText(i + 1, URLTBL_COL_LAST_FAILURE_MSG, status.getLastFailureMessage());
		}

		set03Usage();
		
	}

	private void set03Usage() {
		
		myTopPanel.add(new HtmlH1(MSGS.serviceVersionStats_UsageTitle()));
		Image img = new Image("graph.png?ct=USAGE&pid=" + myServiceVersionPid);
		img.addStyleName(CssConstants.STATS_IMAGE);
		myTopPanel.add(img);

		myTopPanel.add(new HtmlH1(MSGS.serviceVersionStats_LatencyTitle()));
		img = new Image("graph.png?ct=LATENCY&pid=" + myServiceVersionPid);
		img.addStyleName(CssConstants.STATS_IMAGE);
		myTopPanel.add(img);
		
		myTopPanel.add(new HtmlH1(MSGS.serviceVersionStats_MessageSizeTitle()));
		img = new Image("graph.png?ct=PAYLOADSIZE&pid=" + myServiceVersionPid);
		img.addStyleName(CssConstants.STATS_IMAGE);
		myTopPanel.add(img);

		myLoadingSpinner.hideCompletely();
		
	}

}