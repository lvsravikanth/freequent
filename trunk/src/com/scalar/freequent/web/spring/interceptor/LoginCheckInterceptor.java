package com.scalar.freequent.web.spring.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpSession;

import com.scalar.freequent.web.session.SessionParameters;
import com.scalar.freequent.web.util.ErrorInfoUtil;

/**
 * User: ssuppala
 * Date: Mar 25, 2012
 * Time: 2:38:56 PM
 */
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {
	protected final Log logger = LogFactory.getLog(getClass());

	public boolean preHandle(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse, Object bject) throws Exception {
		HttpSession session = httpServletRequest.getSession();
		logger.info("Checking for User existance");
		System.out.println("========IN INTERCEPTOR============");
		if (session == null || session.getAttribute(SessionParameters.ATTRIBUTE_USER) == null) {
			logger.info("User does not exists forwarding to the login page:");

			String url = httpServletRequest.getServletPath();
			System.out.println("================URL:" + url);
			String query = httpServletRequest.getQueryString();
			//ErrorInfoUtil.addError(httpServletRequest, "session.expired", null); //todo: property key should be fetched form resouce class
			ModelAndView modelAndView = new ModelAndView("loginPageController");

			if (query != null) {
				modelAndView.addObject("signonForwardAction", url + "?" + query);
			} else {
				modelAndView.addObject("signonForwardAction", url);
			}
			throw new ModelAndViewDefiningException(modelAndView);
		}
		logger.info("User exists");
		return true;
	}
}
