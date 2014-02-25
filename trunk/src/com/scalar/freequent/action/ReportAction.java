package com.scalar.freequent.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.request.Request;
import com.scalar.core.ScalarActionException;
import com.scalar.core.ScalarValidationException;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.response.Response;
import com.scalar.freequent.web.spring.controller.AbstractActionController;
import com.scalar.freequent.report.ReportDataFactory;
import com.scalar.freequent.report.ReportDataProvider;
import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.l10n.ActionResource;

import java.util.Map;
import java.util.Collections;

/**
 * User: Sujan Kumar Suppala
 * Date: Jan 4, 2014
 * Time: 4:55:28 PM
 */
public class ReportAction extends AbstractActionController {
	protected static final Log logger = LogFactory.getLog(ReportAction.class);
	public static final String FORMAT = "reportformat";
	public static final String FORMAT_PDF = "pdf";
	public static final String FORMAT_HTML = "html";
	public static final String FORMAT_CSV = "csv";
	public static final String FORMAT_XLS = "xls";
	public static final String DEFAULT_FORMAT = FORMAT_PDF;

	public void defaultProcess(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		// do nothing
	}

	public void getreport(Request request, Object command, Map<String, Object> data) throws ScalarActionException, ScalarValidationException {
		String reportName = request.getParameter(Request.PARAM_REPORT_NAME);
		if (StringUtil.isEmpty(reportName)) {
			MsgObject msgObject = MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.PARAM_REQUIRED, Request.PARAM_REPORT_NAME);
			throw ScalarActionException.create(msgObject, null);
		}
		data.put (Request.PARAM_REPORT_NAME, reportName);
		String format = request.getParameter(FORMAT);
		if (StringUtil.isEmpty(format) || !validFormat(format)) {
			format = DEFAULT_FORMAT;
		}
		data.put (FORMAT, format);
		ReportDataProvider dataProvider = ReportDataFactory.getDataProvider(reportName);
		if (dataProvider != null) {
			dataProvider.prepareData(request, reportName, data);
		} else {
			// set empty list as the data provider.
			data.put (ReportDataProvider.REPORT_DATA_KEY, Collections.emptyList());
		}
		data.put(Response.TEMPLATE_ATTRIBUTE, "report/getreport");
	}

	/**
	 * Action to view a content item. Sets the html fragment in the data to be set in the UI.
	 *
	 * @throws ScalarActionException
	 * @throws ScalarValidationException
	 */
	public void view(Request request, Object command, Map<String, Object> data) throws ScalarActionException, ScalarValidationException {
		data.put(Response.TEMPLATE_ATTRIBUTE, "report/view");
	}

	private boolean validFormat(String format) {
		return FORMAT_CSV.equals(format) || FORMAT_PDF.equals(format) ||
				FORMAT_XLS.equals(format) || FORMAT_HTML.equals(format);
	}
}
