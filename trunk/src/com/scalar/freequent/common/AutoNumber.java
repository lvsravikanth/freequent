package com.scalar.freequent.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.atomic.AtomicLong;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import com.scalar.freequent.service.OrderDataService;
import com.scalar.freequent.util.StringUtil;
import com.scalar.core.service.ServiceFactory;
import com.scalar.core.request.Request;
import com.scalar.core.ScalarException;

/**
 * User: Sujan Kumar Suppala
 * Date: Dec 18, 2013
 * Time: 7:42:08 PM
 */
public class AutoNumber {
	protected static final Log logger = LogFactory.getLog(AutoNumber.class);
	protected static AtomicLong orderSequence = null;
	protected static AtomicLong invoiceSequence = null;
	public static int MAX_SEQUENCE_LENGTH = 3;

	protected static long getNextOrderSequence(Request request) throws ScalarException {
		if (orderSequence == null) {
			OrderDataService orderDataService = ServiceFactory.getService(OrderDataService.class, request);
			orderSequence = new AtomicLong(orderDataService.getOrdersCount());
		}

		if (Long.toString(orderSequence.get()+1).length() > MAX_SEQUENCE_LENGTH) {
			orderSequence.set(0);
		}

		return orderSequence.incrementAndGet();
	}

	public static String generateOrderNumber(Request request) throws ScalarException  {
		long seq = getNextOrderSequence(request);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("MMddyy");
		String prefix = format.format(cal.getTime());
		String suffix = StringUtil.formatToLength(seq+"", "0", null, MAX_SEQUENCE_LENGTH);
		return "O"+prefix+suffix;
	}


}
