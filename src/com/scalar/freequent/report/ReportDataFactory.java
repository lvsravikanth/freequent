package com.scalar.freequent.report;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.HashMap;

/**
 * User: Sujan Kumar Suppala
 * Date: Jan 12, 2014
 * Time: 9:02:49 PM
 */
public final class ReportDataFactory {
	protected static final Log logger = LogFactory.getLog(ReportDataFactory.class);
	private Map<String, ReportDataProvider> dataProviders = new HashMap<String, ReportDataProvider>();

	public static ReportDataProvider getDataProvider(String reportName) {
		return createInstance().dataProviders.get(reportName);
	}

	private static ReportDataFactory reportDataFactory = null;
	public static ReportDataFactory createInstance() {
		if (reportDataFactory == null) {
			reportDataFactory = new ReportDataFactory();
		}
		return reportDataFactory;
	}

	public Map<String, ReportDataProvider> getDataProviders() {
		return dataProviders;
	}

	public void setDataProviders(Map<String, ReportDataProvider> dataProviders) {
		this.dataProviders = dataProviders;
	}

	public static ReportDataFactory getReportDataFactory() {
		return reportDataFactory;
	}

	public static void setReportDataFactory(ReportDataFactory reportDataFactory) {
		ReportDataFactory.reportDataFactory = reportDataFactory;
	}
}
