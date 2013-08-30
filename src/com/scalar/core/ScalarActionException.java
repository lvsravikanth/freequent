package com.scalar.core;

import com.scalar.core.util.MsgObject;

import java.util.Locale;

/**
 * User: ssuppala
 * Date: Aug 30, 2013
 * Time: 1:44:14 PM
 */
public class ScalarActionException extends ScalarLoggedException {

	protected ScalarActionException(MsgObject msgObject, Locale locale, Throwable throwable) {
		super(msgObject, locale, throwable);
	}

	protected ScalarActionException(MsgObject msgObject, Throwable throwable) {
		super(msgObject, null, throwable);
	}

	public static ScalarActionException create (MsgObject msgObject, Throwable throwable) {
		return new ScalarActionException (msgObject, throwable);
	}
}
