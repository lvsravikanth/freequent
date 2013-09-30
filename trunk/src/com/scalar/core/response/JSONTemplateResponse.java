package com.scalar.core.response;

import com.scalar.core.ScalarActionException;
import com.scalar.core.ContextUtil;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.freequent.web.spring.view.DummyServletOutputStream;
import com.scalar.freequent.web.spring.view.SwallowingHttpServletResponse;
import com.scalar.freequent.util.IOUtil;
import com.scalar.freequent.l10n.FrameworkResource;

import java.io.Writer;
import java.io.StringReader;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.OrderComparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletRequest;

/**
 * User: ssuppala
 * Date: Sep 30, 2013
 * Time: 3:43:30 PM
 */
public class JSONTemplateResponse extends JSONResponse {
	protected static final Log logger = LogFactory.getLog(JSONTemplateResponse.class);

	/** List of ViewResolvers used by this servlet */
	private static List<ViewResolver> viewResolvers;

	/** Detect all ViewResolvers or just expect "viewResolver" bean? */
	private boolean detectAllViewResolvers = true;


	public void createOutput(Writer writer) throws ScalarActionException {
		if (logger.isDebugEnabled()) {
			logger.debug("Creating template output for template: " + getTemplateName());
		}

		DummyServletOutputStream dummyStream = new DummyServletOutputStream();
		try {
			SwallowingHttpServletResponse proxyHttpResponse = new SwallowingHttpServletResponse((HttpServletResponse)getWrappedObject(), dummyStream);
			View view = resolveViewName(getTemplateName(), null, getRequest().getLocale(), (HttpServletRequest)getRequest().getWrappedObject() );
			if (view != null) {
				view.render(getActionData(), (HttpServletRequest)getRequest().getWrappedObject(), proxyHttpResponse);
			}
		} catch (Exception e) {
			MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.WRITE_OUTPUT_FAILURE);
			throw ScalarActionException.create(msgObject, e);
		}

		String templateOutput = dummyStream.getBuffer();
		dummyStream.close();
		dummyStream = null;
		StringReader sr = new StringReader (templateOutput);
		try {
			IOUtil.readAndWrite(sr, writer);
		} catch (IOException e) {
			MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.WRITE_OUTPUT_FAILURE);
			throw ScalarActionException.create(msgObject, e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Writing template output is completed.");
		}
	}

	/**
	 * Initialize the ViewResolvers used by this class.
	 * <p>If no ViewResolver beans are defined in the BeanFactory for this
	 * namespace, we default to InternalResourceViewResolver.
	 */
	private void initViewResolvers(ApplicationContext context) {
		viewResolvers = null;

		if (this.detectAllViewResolvers) {
			// Find all ViewResolvers in the ApplicationContext, including ancestor contexts.
			Map<String, ViewResolver> matchingBeans =
					BeanFactoryUtils.beansOfTypeIncludingAncestors(context, ViewResolver.class, true, false);
			if (!matchingBeans.isEmpty()) {
				viewResolvers = new ArrayList<ViewResolver>(matchingBeans.values());
				// We keep ViewResolvers in sorted order.
				OrderComparator.sort(viewResolvers);
			}
		}
		else {
			try {
				ViewResolver vr = context.getBean(DispatcherServlet.VIEW_RESOLVER_BEAN_NAME, ViewResolver.class);
				this.viewResolvers = Collections.singletonList(vr);
			}
			catch (NoSuchBeanDefinitionException ex) {
				// Ignore, we'll add a default ViewResolver later.
			}
		}

		// Ensure we have at least one ViewResolver, by registering
		// a default ViewResolver if no other resolvers are found.
		/*if (this.viewResolvers == null) {
			this.viewResolvers = getDefaultStrategies(context, ViewResolver.class);
			if (logger.isDebugEnabled()) {
				logger.debug("No ViewResolvers found in servlet '" + getServletName() + "': using default");
			}
		}*/
	}

	protected List<ViewResolver> getViewResolvers () {
		if (viewResolvers == null) {
			initViewResolvers(RequestContextUtils.getWebApplicationContext((ServletRequest)getRequest().getWrappedObject()));
		}

		return viewResolvers;
	}

	/**
	 * Resolve the given view name into a View object (to be rendered).
	 * <p>The default implementations asks all ViewResolvers of this dispatcher.
	 * Can be overridden for custom resolution strategies, potentially based on
	 * specific model attributes or request parameters.
	 * @param viewName the name of the view to resolve
	 * @param model the model to be passed to the view
	 * @param locale the current locale
	 * @param request current HTTP servlet request
	 * @return the View object, or <code>null</code> if none found
	 * @throws Exception if the view cannot be resolved
	 * (typically in case of problems creating an actual View object)
	 * @see ViewResolver#resolveViewName
	 */
	protected View resolveViewName(String viewName, Map<String, Object> model, Locale locale,
			HttpServletRequest request) throws Exception {
		List<ViewResolver> resolvers = getViewResolvers();
		for (ViewResolver viewResolver : resolvers) {
			View view = viewResolver.resolveViewName(viewName, locale);
			if (view != null) {
				return view;
			}
		}
		return null;
	}
}
