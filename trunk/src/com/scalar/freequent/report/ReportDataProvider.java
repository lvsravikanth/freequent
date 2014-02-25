package com.scalar.freequent.report;

import com.scalar.core.request.Request;
import com.scalar.core.ScalarValidationException;

import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Jan 12, 2014
 * Time: 8:58:34 PM
 */
public interface ReportDataProvider {
	/**
	 * Constant representing the key name for the report data.
	 */
	public static final String REPORT_DATA_KEY = "reportdata";

	/**
	 * Implementation of this method should prepare and populate the data.
	 *
	 * @param request request object.
	 * @param reportName report name for which the data needs to be prepared.
	 * @param data should be populated with the report data.
	 */
	void prepareData(Request request, String reportName, Map<String, Object> data) throws ScalarValidationException;
}
