package com.scalar.freequent.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 23, 2013
 * Time: 8:10:36 PM
 */
public class DebugUtil {
    protected static final Log logger = LogFactory.getLog(DebugUtil.class);

	private static final boolean transactionDebugging = true;
	private static final boolean verboseTransactionDebugging = true;

	public static void showTransactionStatus(String message) {
		System.out.println(((transactionActive()) ? "[+] " : "[-] ") + message);
	}

	// Some guidance from: http://java.dzone.com/articles/monitoring-declarative-transac?page=0,1
	public static boolean transactionActive() {
		try {
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			Class tsmClass = contextClassLoader.loadClass("org.springframework.transaction.support.TransactionSynchronizationManager");
			Boolean isActive = (Boolean) tsmClass.getMethod("isActualTransactionActive", null).invoke(null, null);

			return isActive;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		// If we got here it means there was an exception
		throw new IllegalStateException("ServerUtils.transactionActive was unable to complete properly");
	}

	public static void transactionRequired(String message) {
		// Are we debugging transactions?
		if (!transactionDebugging) {
			// No, just return
			return;
		}

		// Are we doing verbose transaction debugging?
		if (verboseTransactionDebugging) {
			// Yes, show the status before we get to the possibility of throwing an exception
			showTransactionStatus(message);
		}

		// Is there a transaction active?
		if (!transactionActive()) {
			// No, throw an exception
			throw new IllegalStateException("Transaction required but not active [" + message + "]");
		}
	}

    /**
	 * Logs the data in the <code>Map</code>.
	 *
	 * @param log the log for output
	 * @param data the data to log
	 * @param message the message to log prior to the data. Can be <code>null</code>.
	 */
	public static void logMap(Log log, Map<?, ?> data, String message) {
		if ( null != message ) {
			log.debug(message);
		}

		if ( null == data ) {
			return;
		}

		for ( Object name : data.entrySet() ) {
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>)name;
			log.debug(entry.getKey() + " : " + entry.getValue());
		}
	}

}
