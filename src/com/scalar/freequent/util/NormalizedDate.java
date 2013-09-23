package com.scalar.freequent.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

/**
 * Extends {@link java.util.Date} for formatting purposes.
 *
 * User: Sujan Kumar Suppala
 * Date: Sep 22, 2013
 * Time: 12:03:39 PM
 */
public class NormalizedDate extends Date {

    protected static final Log logger = LogFactory.getLog(NormalizedDate.class);

	/**
	 * Serialization UID.
	 */
	private static final long serialVersionUID = 8624036975348229175L;

	private static final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss Z");

	/**
	 * Allocates a <code>NormalizedDate</code> object and initializes it so that
	 * it represents the time at which it was allocated, measured to the
	 * nearest millisecond.
	 *
	 */
	public NormalizedDate() {
		super();
	}

	/**
	 * Allocates a <code>NormalizedDate</code> object and initializes it to
	 * represent the specified number of milliseconds since the
	 * standard base time known as "the epoch", namely January 1,
	 * 1970, 00:00:00 GMT.
	 *
	 * @param   date   the milliseconds since January 1, 1970, 00:00:00 GMT.
	 */
	public NormalizedDate(long date) {
		super(date);
	}

	/**
	 * Extends toString() to ensure a complete date AND time normalized format.
	 *
	 * @return a MM/dd/yyyy HH:mm:ss.SSS Z date
	 */
	@Override
	public String toString() {
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		return format.format(this);
	}
}

