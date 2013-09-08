package com.scalar.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 7, 2013
 * Time: 6:08:32 PM
 */

/**
 * The class generates an ID that is guaranteed to be unique on this
 * machine, based on the following assumptions:
 *
 *   a) the machine takes more than one second to reboot,
 *   b) the machine's clock is never set backward,
 *   c) the system property that specifies a 'unique' jvmid is
 *      truly unique (on this machine) or, if the property is not
 *      set, then the static initializer for this class is not
 *      executed within the same millisecond by multiple jvm instances.
 *
 * The GUID is a 28 character String that is created by concatenating
 * the following three fields:
 *
 *    1) A counter value - In string format, this value is the
 *       4 hex digits of a Java 'short' integer, in reverse order.
 *       For example, if the counter value is 266 (base 10), then
 *       the hex value would be '010a'.  This would appear as the first
 *       four characters in the GUID string as 'a010'.
 *    2) A timestamp - This is the low-order 12 hex digits of a system
 *       time value (a Java 'long' type), also recorded in reverse
 *       order.
 *    3) The unique JVM id.  This is a string value that may be set by
 *       a system property (see PROPERTY_JVMID).  The maximum length of
 *       this property is 12 characters.  If the value is longer, then it
 *       will be truncated.  If it is less than 12 characters, it will be
 *       padded to 12 with '0' (an ASCII zero) characters.  Any whitespace
 *       characters will also be replaced by the '0' character.
 *
 *       If the system property is not specified, then this id will be
 *       the low-order 12 hex digits of the system time when the class
 *       static initializer is executed, again in reverse order.
 *
 *
 */
class GUIDGen {
     protected static final Log logger = LogFactory.getLog(GUIDGen.class);
    /**
     * The name of the system property that may be used to
     * set the jvmid for this GUID generator.  (See the class
     * description for a discussion on how this is used.)
     */
    static final String PROPERTY_JVMID = "com.scalar.jvmid";

    /** The id counter */
    private static short mCounter = Short.MIN_VALUE;

    /** A sync object */
    private static Object mSyncObj = new Object();

    /** The system time for the current interval */
    private static long mSysTime;
    /** The previous system time for the current interval */
    private static long mLastSysTime = System.currentTimeMillis();

    private static long  ONE_SECOND = 1000; // in milliseconds
    private static int  MAX_TS_LEN = 12; // Timestamps are the low-order 12 hex chars in
                                          // the 16 character hex time string

    private static int  MAX_JVMID_LEN = 12;

    /**
     * The jvm unique id.  This may be set by specifying a value for the
     * PROPERTY_JVMID property in the system properties.  If this
     * system property is not set, then the system time (during class static initialization)
     * will be used.  (The purpose of this value is to uniquely identify the jvm
     * server instance.)
     */
    private static String mJvmId;

    /**
     * Set the jvmId.
     *
     * @param id The new value of the jvmId.  If null or zero-length,
     *              then this id will be set to the low-order 12 hex
     *              digits of the current system time (in reverse order).
     */
    static void setJvmId(String id)
    {
        String value = null;
        if (id == null || id.trim().length()==0 ) {
            value = Long.toHexString(System.currentTimeMillis());
        }
        else {
            char[] buf = new char[MAX_JVMID_LEN];
            int j=MAX_JVMID_LEN-1;
            for (int i=0; i<MAX_JVMID_LEN; i++) {
                if (i <id.length()) {
                    char c = id.charAt(i);
                    if (Character.isWhitespace(c))
                        buf[j] = '0';
                    else
                        buf[j] = c;
                }
                else
                    buf[j] = '0';
                j--;
            }
            value = new String(buf);
        }
        synchronized(mSyncObj) {
            mJvmId = value;
        }
    }

    /** Set the initial value of the jvmId */
    static {
        String id = System.getProperty(PROPERTY_JVMID);
        setJvmId(id);
    }

    /**
     * Create a unique identifier.  This is essentially the
     * same algorithm as the rmi UID, but we don't want the
     * unique number to be based on an arbitrary object's hashcode.
     */
    static void createId(StringBuffer buf)
	{
		StringBuilder strBld = new StringBuilder(36);
		createId(strBld);
		buf.append(strBld.toString());
	}


    /**
     * Create a unique identifier.  This is essentially the
     * same algorithm as the rmi UID, but we don't want the
     * unique number to be based on an arbitrary object's hashcode.
     */
    static void createId(StringBuilder buf)
    {
        short counter;
        long time;
        String jvmId;

        synchronized (mSyncObj) {
            time = mLastSysTime;
            if (mCounter == Short.MAX_VALUE) {
                boolean done = false;
                while (!done) {
                    time = System.currentTimeMillis();
                    if (time < mLastSysTime+ONE_SECOND) {
                        // pause for a second to wait for time to change
                        try {
                            Thread.currentThread().sleep(ONE_SECOND);
                        }
                        catch (java.lang.InterruptedException e) {
                        }	// ignore exception
                        continue;
                    }
                    else {
                        mLastSysTime = time;
                        mCounter = Short.MIN_VALUE;
                        done = true;
                    }
                }
            }
            jvmId = mJvmId;
            counter = mCounter++;
            // now, let someone else in...
        }

        // Create the string
        fillBuffer( buf, Integer.toHexString(counter), 4 );
        fillBuffer( buf, Long.toHexString(time), 12 );
        fillBuffer( buf, jvmId, 12 );
    }



    /**
     *  Append the last 'count' characters from the input String to
     *  the StringBuilder, starting with the last character in
     *  the input String (and moving backwards).  If count is
     *  greater than the String length, then fill with '0'.
     *
     *  @param buf The buffer that will receive the characters.
     *  @param inStr The source input string.
     *  @param count The number of character to be appended to buf.
     *
     */
    private static void fillBuffer(StringBuilder buf, String inStr, int count)
    {
        int pos = inStr.length()-1;
        for (int i=0; i<count; i++) {
            if (pos>=0)
                buf.append(inStr.charAt(pos));
            else
                buf.append("0");
            pos--;
        }
    }
} // End class GUIDGen

