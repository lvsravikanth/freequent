package com.scalar.freequent.web.spring.view;

import org.springframework.web.servlet.view.tiles2.TilesView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.Attribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 29, 2013
 * Time: 3:48:47 PM
 */
public class CustomTilesView extends TilesView {
	protected static final Log logger = LogFactory.getLog(CustomTilesView.class);
	protected static final String baseDefinitionName = "base.definition";
	protected static final String baseDefinitionAttribute_BODY = "body";

	public String getUrl() {
		return baseDefinitionName;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ServletContext servletContext = getServletContext();
		TilesContainer container = ServletUtil.getContainer(servletContext);

		if (container == null) {
			throw new ServletException("Tiles container is not initialized. " +
					"Have you added a TilesConfigurer to your web application context?");
		}

		AttributeContext attributeContext = container.getAttributeContext(request, response);

		attributeContext.putAttribute(baseDefinitionAttribute_BODY, Attribute.createTemplateAttribute(super.getUrl()));

		super.renderMergedOutputModel(model, request, response);
	}
}
