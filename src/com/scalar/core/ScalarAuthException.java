package com.scalar.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.util.MsgObject;

import java.util.Locale;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 8, 2013
 * Time: 3:33:40 PM
 */
public class ScalarAuthException extends ScalarLoggedException {
    protected static final Log logger = LogFactory.getLog(ScalarAuthException.class);

    protected ScalarAuthException(MsgObject msgObject, Locale locale, Throwable throwable) {
        super(msgObject, locale, throwable);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public static ScalarAuthException create (MsgObject msgObject, Locale locale, Throwable throwable) {
        return new ScalarAuthException (msgObject, locale, throwable);
    }

    public static ScalarAuthException create (MsgObject msgObject, Throwable throwable) {
        return new ScalarAuthException (msgObject, null, throwable);
    }

    public static ScalarAuthException create (MsgObject msgObject) {
        return new ScalarAuthException (msgObject, null, null);
    }
}
