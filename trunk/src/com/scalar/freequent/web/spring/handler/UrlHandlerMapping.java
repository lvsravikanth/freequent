package com.scalar.freequent.web.spring.handler;

import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.beans.BeansException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 17, 2013
 * Time: 4:48:36 PM
 */
public class UrlHandlerMapping extends AbstractHandlerMapping {
    protected static final Log logger = LogFactory.getLog(UrlHandlerMapping.class);
    private final Set<String> handlerMappings = new HashSet<String>();
    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Override
    protected void initApplicationContext() throws BeansException {
        super.initApplicationContext();
        detectHandlers();

    }

    /**
     * Look up a handler for the URL path of the given request.
     * http://localhost:8080/freequent/billing/newbill.htm
     *
     * @param request the current http request
     * @return the handler instance, or <code>null</code> if none found
     * @throws Exception
     */
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        String urlPath = this.urlPathHelper.getLookupPathForRequest(request);
        if (logger.isDebugEnabled()) {
            logger.debug("Looking up handler for urlPath [" + urlPath + "]");
        }
        int startIdx = urlPath.indexOf("/");
        int lastIdx = urlPath.lastIndexOf("/");
        if (lastIdx == startIdx) {
            lastIdx = urlPath.length()-1;
        }
        String handlerPath = urlPath.substring(startIdx + 1, lastIdx);
        if (logger.isDebugEnabled()) {
            logger.debug("Looking up handler for handlerPath [" + handlerPath + "]");
        }
        return lookupHandler(handlerPath);
    }

    protected void detectHandlers() throws BeansException {
        if (logger.isDebugEnabled()) {
            logger.debug("Looking for mappings in application context: " + getApplicationContext());
        }
        String[] beanNames = getApplicationContext().getBeanNamesForType(Controller.class);
        handlerMappings.addAll(Arrays.asList(beanNames));
    }

    /**
     * Look up a handler instance for the given URL path.
     *
     * @param handlerPath URL the bean is mapped to
     * @return the associated handler instance, or <code>null</code> if not found
     */
    protected Object lookupHandler(String handlerPath) {
        if (handlerMappings.contains(handlerPath)) {
            return handlerPath;
        }

        return null;
    }
}
