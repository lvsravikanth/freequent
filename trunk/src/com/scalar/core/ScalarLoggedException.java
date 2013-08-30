package com.scalar.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Locale;

import com.scalar.core.util.MsgObject;

/**
 * Implements an exception which is automatically logged.
 * <p>
 *
 * Methods should throw <code>VgnLoggedException</code> or some subclass thereof
 * User: ssuppala
 * Date: Aug 30, 2013
 * Time: 12:23:02 PM
 */
public class ScalarLoggedException extends ScalarException {
	 protected static final Log logger = LogFactory.getLog(ScalarLoggedException.class);

	protected ScalarLoggedException(MsgObject msgObject, Locale locale, Throwable throwable) {
		super(msgObject, locale, throwable);

		logger.error(getMessage(), this);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
