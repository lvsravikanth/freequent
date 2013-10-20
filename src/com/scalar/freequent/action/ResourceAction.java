package com.scalar.freequent.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.freequent.web.spring.controller.AbstractActionController;
import com.scalar.freequent.util.ResourceUtil;
import com.scalar.core.request.Request;
import com.scalar.core.ScalarActionException;
import com.scalar.core.util.MsgObjectUtil;

import java.util.Map;
import java.util.Locale;
import java.lang.reflect.Field;


/**
 * User: Sujan Kumar Suppala
 * Date: Oct 20, 2013
 * Time: 11:26:55 AM
 */
public class ResourceAction extends AbstractActionController {
	protected static final Log logger = LogFactory.getLog(ResourceAction.class);

	/**
	 * Constant used to identify the keys field.
	 */
	protected static final String KEYS_FIELD = "keys";

	protected static final String BASENAME_ATTRIBUTE = "basename";

	public void defaultProcess(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		// nothing to do
	}

	public void getcatalog(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		String basename = request.getParameter(BASENAME_ATTRIBUTE);
		try {
			Class<?> clazz = Class.forName(basename);
			Field field = clazz.getField(KEYS_FIELD);
			String[] keys = (String[])field.get(null);
			Locale locale = request.getLocale();
			for ( String key : keys ) {
				String message = ResourceUtil.getMessage(basename, locale, key);
				// Replace things that bite
				message = message.replaceAll("\n", "\\n").replaceAll("\r", "\\r");

				data.put (key, message);
			}
		} catch (Exception e) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(e.getMessage()), e);
		}
	}
}
