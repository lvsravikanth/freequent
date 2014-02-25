package com.scalar.freequent.report;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.request.Request;
import com.scalar.core.ScalarValidationException;

import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Jan 12, 2014
 * Time: 10:04:57 PM
 */
public class InvoiceDataProvider implements ReportDataProvider {
	protected static final Log logger = LogFactory.getLog(InvoiceDataProvider.class);

	public void prepareData(Request request, String reportName, Map<String, Object> data) throws ScalarValidationException {
		String invoiceId = request.getParameter(Request.ID);
	}
}
