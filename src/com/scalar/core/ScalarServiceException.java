package com.scalar.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.util.MsgObject;

import java.util.Locale;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 31, 2013
 * Time: 1:05:30 PM
 */
public class ScalarServiceException extends ScalarLoggedException {
    protected static final Log logger = LogFactory.getLog(ScalarServiceException.class);

    protected ScalarServiceException(MsgObject msgObject, Locale locale, Throwable throwable) {
        super(msgObject, locale, throwable);
    }

    public static ScalarServiceException create (MsgObject msgObject, Throwable throwable) {
		return new ScalarServiceException (msgObject, null, throwable);
	}
}
