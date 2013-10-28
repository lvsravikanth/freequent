package com.scalar.core;

import com.scalar.core.util.MsgObject;

import java.util.Locale;

/**
 * User: ssuppala
 * Date: Oct 28, 2013
 * Time: 1:44:14 PM
 */
public class ScalarValidationException extends ScalarLoggedException {

	protected ScalarValidationException(MsgObject msgObject, Locale locale, Throwable throwable) {
		super(msgObject, locale, throwable);
	}

	protected ScalarValidationException(MsgObject msgObject, Throwable throwable) {
		super(msgObject, null, throwable);
	}

	public static ScalarValidationException create (MsgObject msgObject, Throwable throwable) {
		return new ScalarValidationException(msgObject, throwable);
	}
}