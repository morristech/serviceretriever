package net.svcret.admin.server.rpc;

import java.io.IOException;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.svcret.admin.api.AdminServiceProvider;
import net.svcret.admin.api.IChartingServiceBean;
import net.svcret.admin.api.UnexpectedFailureException;
import net.svcret.admin.shared.model.TimeRange;
import net.svcret.admin.shared.util.ChartParams;
import net.svcret.admin.shared.util.ChartTypeEnum;

import org.apache.commons.io.IOUtils;

import com.tractionsoftware.gwt.user.server.UTCDateTimeUtils;

public class GraphServlet extends HttpServlet {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GraphServlet.class);

	private static final long serialVersionUID = 1L;

	private IChartingServiceBean myChartSvc;

	@Override
	public void init() throws ServletException {
		super.init();
		
		myChartSvc = AdminServiceProvider.getInstance().getChartingService();
	}

	
	private byte[] renderLatency(HttpServletRequest theReq, TimeRange theRange, boolean theIndividualMethod) throws IOException, ServletException {
		long pid = getPid(theReq);

		ourLog.info("Rendering latency graph for service version {}", pid);

		byte[] graph;
		try {
			graph = myChartSvc.renderSvcVerLatencyMethodGraph(pid, theRange, theIndividualMethod);
		} catch (UnexpectedFailureException e) {
			throw new ServletException(e);
		}

		return graph;
	}

	private long getPid(HttpServletRequest theReq) {
		String pidString = theReq.getParameter(ChartParams.PID);
		long pid = Long.parseLong(pidString);
		return pid;
	}

	@Override
	protected void doGet(HttpServletRequest theReq, HttpServletResponse theResp) throws ServletException, IOException {
		String chartTypeString = theReq.getParameter(ChartParams.CHART_TYPE);
		ChartTypeEnum chartType = ChartTypeEnum.valueOf(chartTypeString);

		String rangeString = theReq.getParameter(ChartParams.RANGE);
		TimeRange range = new TimeRange();
		range.fromUrlValue(rangeString);

		if (range.getWithPresetRange() == null) {
			range.setNoPresetFrom(UTCDateTimeUtils.getDateValue(TimeZone.getDefault(), range.getNoPresetFromDate(), range.getNoPresetFromTime()));
			range.setNoPresetTo(UTCDateTimeUtils.getDateValue(TimeZone.getDefault(), range.getNoPresetToDate(), range.getNoPresetToTime()));
		}

		byte[] bytes = null;
		if (BaseRpcServlet.isMockMode()) {
			bytes = IOUtils.toByteArray(GraphServlet.class.getResourceAsStream("/net/svcret/images/icon_library_16.png"));
		} else {
			switch (chartType) {
			case LATENCY:
				boolean individualMethod = ChartParams.VALUE_TRUE.equals(theReq.getParameter(ChartParams.BY_METHOD));
				bytes = renderLatency(theReq, range, individualMethod);
				break;
			case USAGE:
				bytes = renderUsage(theReq, range);
				break;
			case PAYLOADSIZE:
				bytes = renderPayloadSize(theReq, range);
				break;
			case THROTTLING:
				bytes = renderThrottling(theReq, range);
				break;
			case USERMETHODS:
				bytes = renderUserMethod(theReq, range);
			}
		}

		if (bytes == null) {
			throw new ServletException("Unknown chart type: " + chartTypeString);
		}

		theResp.setContentType("image/png");
		theResp.setContentLength(bytes.length);
		theResp.getOutputStream().write(bytes);
		theResp.getOutputStream().close();

	}

	private byte[] renderUserMethod(HttpServletRequest theReq, TimeRange theRange) throws ServletException, IOException {
		long pid = getPid(theReq);

		ourLog.info("Rendering user method stats graph for user PID {}", pid);

		byte[] graph;
		try {
			graph = myChartSvc.renderUserMethodGraphForUser(pid, theRange);
		} catch (UnexpectedFailureException e) {
			throw new ServletException(e);
		}

		return graph;
	}

	private byte[] renderPayloadSize(HttpServletRequest theReq, TimeRange theRange) throws IOException, ServletException {
		long pid = getPid(theReq);

		ourLog.info("Rendering payload size graph for service version {}", pid);

		byte[] graph;
		try {
			graph = myChartSvc.renderSvcVerPayloadSizeGraph(pid, theRange);
		} catch (UnexpectedFailureException e) {
			throw new ServletException(e);
		}

		return graph;
	}

	private byte[] renderThrottling(HttpServletRequest theReq, TimeRange theRange) throws IOException, ServletException {
		long pid = getPid(theReq);

		ourLog.info("Rendering throttling graph for service version {}", pid);

		byte[] graph;
		try {
			graph = myChartSvc.renderSvcVerThrottlingGraph(pid, theRange);
		} catch (UnexpectedFailureException e) {
			throw new ServletException(e);
		}

		return graph;
	}

	private byte[] renderUsage(HttpServletRequest theReq, TimeRange theRange) throws IOException, ServletException {
		long pid = getPid(theReq);

		ourLog.info("Rendering usage graph for service version {}", pid);

		byte[] graph;
		try {
			graph = myChartSvc.renderSvcVerUsageGraph(pid, theRange);
		} catch (UnexpectedFailureException e) {
			throw new ServletException(e);
		}

		return graph;
	}

}
