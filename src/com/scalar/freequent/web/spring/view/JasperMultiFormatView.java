package com.scalar.freequent.web.spring.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import com.scalar.core.request.Request;
import net.sf.jasperreports.engine.JasperReport;

/**
 * User: Sujan Kumar Suppala
 * Date: Jan 13, 2014
 * Time: 7:12:55 PM
 */
public class JasperMultiFormatView extends JasperReportsMultiFormatView {
	protected static final Log logger = LogFactory.getLog(JasperMultiFormatView.class);
	protected static final String URL_PREFIX = "/WEB-INF/reports/";
	protected static final String URL_SUFFIX = ".jasper";

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String reportNamae = (String)model.get(Request.PARAM_REPORT_NAME);
		setUrl(URL_PREFIX + reportNamae + URL_SUFFIX);
		super.render(model, request, response);
	}

	@Override
	protected JasperReport getReport() {
		return loadReport();
	}
}
