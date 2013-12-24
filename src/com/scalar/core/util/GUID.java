package com.scalar.core.util;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.io.Serializable;

import com.scalar.core.ScalarException;
import com.scalar.core.ScalarRuntimeException;
import com.scalar.freequent.l10n.FrameworkResource;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 7, 2013
 * Time: 6:14:55 PM
 */

/**
 * Provides an ID that is guaranteed to be globally unique. This ID is guaranteed based on
 * the following assumptions:
 * <ul>
 * <li>The machine takes more than one second to reboot
 * <li>The machine's clock is never set backward,
 * <li>The host ip address is unique, and
 * <li>The system property that specifies a 'unique' JVM ID is
 * truly unique (on this machine), or, if the property is not
 * set, the static initializer for this class is not
 * executed within the same millisecond by multiple JVM instances.
 * </ul>
 * The GUID is a 40 character <code>String</code> that is created by concatenating
 * the following five fields:
 * <ul>
 * <li>Counter value - In <code>String</code> format, this value is the
 * 4 hex digits of a Java <code>short int</code>, in reverse order.
 * For example, if the counter value is 266 (base 10), then
 * the hex value would be '<code>010a</code>'.  This would appear as the first
 * four characters in the GUID <code>String</code> as '<code>a010</code>'.
 * <li>Timestamp - The low-order 12 hex digits of a system
 * time value (a Java <code>long</code> type), also recorded in reverse
 * order.
 * <li>Unique JVM ID - A <code>String</code> value that may be set by
 * a system property (see PROPERTY_JVMID). The maximum length of
 * this property is 12 characters. If the value is longer, then it
 * is truncated.
 * <br>If it is less than 12 characters, it is
 * padded to 12 with '0' (an ASCII zero) characters.  Any whitespace
 * characters are also replaced by the '0' character.
 * <br>
 * If the system property is not specified, then this ID is
 * the low-order 12 hex digits of the system time when the class
 * static initializer is executed, again in reverse order.
 * <p/>
 * <li>Host primary IP address - An 8 character hex <code>String</code>, in reverse
 * order. This address is the value that is returned by the
 * <code>java.net.InetAddress.getLocalHost</code> method.
 * <p/>
 * <li>4 character type ID code (Not reversed.)
 * </ul>
 */
public class GUID
        implements Serializable {
    protected static final Log logger = LogFactory.getLog(GUID.class);
    // hard coding required as of 7.3.1.1
    // see vicket 143620 (GUID serialVersionUID has changed in 7311) for more information
    static final long serialVersionUID = -2272938812893514738L;

/*
 *
 *    Note:  This class originally used the java.rmi.server.UID to provide the
 *    the value for the first three fields.  With the move to clustered
 *    appservers, there was a risk that two instances of a clustered server
 *    running on the same physical machine would generate duplicated GUIDs,
 *    because the UID class used an arbitrary object's hashcode to generate the
 *    'unique' component of the UID.  A new package-scope class, named GUIDGen,
 *    is now used to generate the first three fields.  This provides
 *    essentially the same algorithm as the UID, but the 'unique' component can
 *    be specified as a system propery, or set explicitly by calling
 *    setJvmId().
 */

    // Must be 40 characters in length, otherwise DB will pad with spaces.
    /**
     * The constant representing the invalid GUID.
     */
    public static final String INVALID_ID =
            "------------------------------------NULL";

    /**
     * The constant representing the type code for an object of unknown type.
     */
    public static final String UNKNOWN_TYPE_CODE =
            "____";

    /**
     * The constant representing the type code for logging GUIDs.
     */
    public static final String LOG_TYPE_CODE =
            "_LOG";

    /**
     * The constant representing the required length of an ID, including the type code.
     */
    public static final int ID_LENGTH =
            INVALID_ID.length();

    /**
     * The constant representing the required length of a type code.
     */
    public static final int TYPE_CODE_LENGTH =
            UNKNOWN_TYPE_CODE.length();

    /**
     * This is where the type code starts within the id.
     */
    private static final int TYPE_CODE_POSITION =
            ID_LENGTH - TYPE_CODE_LENGTH;

    /**
     * The constant representing the name of the system property that may be used to
     * set the JVM ID for this GUID generator. See the class
     * description for a discussion on how this is used.
     */
    public static final String PROPERTY_JVMID = GUIDGen.PROPERTY_JVMID;

    /**
     * Lazily cached host identifier used for GUID generation
     */
    private static String localHostInfo = null;


    /**
     * Sets the JVM ID.
     *
     * @param id <code>String</code> of the JVM ID. If <code>null</code> or zero-length,
     *           then this ID is set to the low-order 12 hex
     *           digits of the current system time (in reverse order).
     */
    public static void setJvmId(String id) {
        GUIDGen.setJvmId(id);
    }

    /**
     * Generates a new, valid GUID with an unknown type.
     *
     * @return <code>GUID</code> with an unknown type.
     * @throws ScalarException thrown if errors occur generating the GUID.
     * @see #generate(String)
     * @see #generateString
     */
    public static GUID generate()
            throws ScalarException {
        return new GUID(generateString());
    }

    /**
     * Generates a new, valid GUID with a type code.
     *
     * @param typeCode 4-character type code to be appended to the GUID.
     * @return <code>GUID</code> with a type code.
     * @throws ScalarException thrown if errors occur generating the GUID.
     * @see #generate
     * @see #generateString(String)
     */
    public static GUID generate(String typeCode)
            throws ScalarException {
        return new GUID(generateString(typeCode));
    }

    /**
     * Generates a new, valid <code>String</code> GUID with unknown type.
     *
     * @return <code>String</code> GUID with unknown type.
     * @throws ScalarException thrown if errors occur generating the GUID.
     */
    public static String generateString()
            throws ScalarException {
        return generateString(UNKNOWN_TYPE_CODE);
    }

    /**
     * Generates a new, valid <code>String</code> GUID with a type of <code>_REQ</code>.
     *
     * @return <code>String</code> GUID with a type of <code>_REQ</code>.
     */
    public static String genReqId() {
        try {

            // The algorithm used comes from the java.rmi.server.UID class:
            //
            // Create a pure identifier that is unique with respect to the host
            // on which it is generated. This UID is unique under the following
            // conditions: a) the machine takes more than one second to reboot,
            // and b) the machine's clock is never set backward. In order to
            // construct a UID that is globally unique, simply pair a UID with
            // an InetAddress.
            StringBuilder buf = new StringBuilder(36);

            // Append the time based identifier
            GUIDGen.createId(buf);

            // if localHostInfo cache is empty, set it
            if (localHostInfo == null) {
                java.net.InetAddress laddr = java.net.InetAddress.getLocalHost();
                byte[] bytes = laddr.getAddress();
                StringBuilder cache_tmp = new StringBuilder(36);
                bytesToHexString(bytes, cache_tmp);
                localHostInfo = cache_tmp.toString();
            }

            // Append the ip address
            buf.append(localHostInfo);

            // Tack on the type code
            buf.append(LOG_TYPE_CODE);

            return buf.toString();
            // Minimize checking and don't throw exceptions so log calls can be
            // as cheap as possible.
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Generates a new, valid <code>String</code> GUID with a type code.
     *
     * @param typeCode 4-character type code to be appended to the GUID.
     * @return <code>String</code> GUID with a type code.
     * @throws ScalarException thrown if errors occur generating the GUID.
     */
    public static String generateString(String typeCode)
            throws ScalarException {
        if (typeCode == null) {
            typeCode = UNKNOWN_TYPE_CODE;
        } else if (typeCode.length() != TYPE_CODE_LENGTH) {
            throw ScalarRuntimeException.create(
                    MsgObjectUtil.getMsgObject(FrameworkResource.ILLEGAL_TYPE_CODE_LENGTH,
                            typeCode, String.valueOf(TYPE_CODE_LENGTH)));
        }

        // Make sure typeCode does not contain whitespace
        char[] chars = typeCode.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isWhitespace(chars[i])) {
                throw ScalarRuntimeException.create(
                        MsgObjectUtil.getMsgObject(FrameworkResource.WHITESPACE_NOT_ALLOWED_IN_TYPE_CODE));
            }
        }

        try {

            // The algorithm used comes from the java.rmi.server.UID class:
            //
            // Create a pure identifier that is unique with respect to the host
            // on which it is generated. This UID is unique under the following
            // conditions: a) the machine takes more than one second to reboot,
            // and b) the machine's clock is never set backward. In order to
            // construct a UID that is globally unique, simply pair a UID with
            // an InetAddress.
            StringBuilder buf = new StringBuilder(36);

            // Append the time based identifier
            GUIDGen.createId(buf);

            //if localHostInfo cache is empty, set it
            if (localHostInfo == null) {
                java.net.InetAddress laddr = java.net.InetAddress.getLocalHost();
                byte[] bytes = laddr.getAddress();
                StringBuilder cache_tmp = new StringBuilder(36);
                bytesToHexString(bytes, cache_tmp);
                localHostInfo = cache_tmp.toString();
            }

            // Append the ip address
            buf.append(localHostInfo);

            // Tack on the type code
            buf.append(typeCode);

            return buf.toString();

        } catch (Exception ex) {
            throw new ScalarException(MsgObjectUtil.getMsgObject(FrameworkResource.UNABLE_TO_GENERATE_GUID), null, ex);
        }

    } // End generateString

    /**
     * Store an array of bytes as a hex string.
     *
     * @param bytes The array of bytes to be stored.
     * @param buf   The buffer into which the hex string is to be stored.
     */
    private static void bytesToHexString(byte[] bytes, StringBuilder buf) {

        // Loop through the bytes in reverse order so that we can get the
        // differences near the beginning of the string.  This helps to speed
        // up DB searches.
        for (int i = bytes.length - 1; i >= 0; i--) {

            int x = ((int) bytes[i]) & 0xff;
            // append leading zero if necessary
            if (x < 16) buf.append("0");
            buf.append(Integer.toHexString(x));
        }
    }

    /**
     * The value of the GUID.
     *
     * @vgnInternal
     */
    protected String id;

    /**
     * Constructor
     */
    public GUID() {
        id = INVALID_ID;
    }

    /**
     * Constructor given a <code>String</code> ID.  This method may be helpful
     * when de-serializing data.
     *
     * @param id the ID for the new GUID.
     */
    public GUID(String id) {
        this(id, true);
    }

    /**
     * Constructor given a <code>String</code> ID.  This method may be helpful
     * when de-serializing data.
     *
     * @param id      the ID for the new GUID.
     * @param checkId whether to validate id
     * @vgnInternal
     */
    public GUID(String id, boolean checkId) {
        // Validate the id if the caller says to do so
        if (checkId) {
            checkId(id);
        }

        this.id = id;
    }


    /**
     * Constructor that creates a GUId as a copy of another GUID. The IDs have the same
     * underlying value.
     *
     * @param that GUID to be copied.
     */
    public GUID(GUID that) {
        this.id = that.id;
    }

    /**
     * Determines if this GUID is valid.
     *
     * @return <code>boolean</code> indicating if this GUID is valid.
     */
    public boolean isValid() {
        return (id != null && !id.equals(INVALID_ID));
    }

    /**
     * Invalidates this GUID.
     */
    public void invalidate() {
        id = INVALID_ID;
    }

    /**
     * Determines if another object is a GUID and has the same
     * underlying value as this one.
     *
     * @param obj the object to be compared.
     * @return <code>boolean</code> indicating if the other object is a GUID and has the same
     *         underlying value as this one.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof GUID)) return false;

        GUID that = (GUID) obj;
        if (this == that) return true;
        if (that.id == null) return (this.id == null);
        return id.equals(that.id);
    }

    /**
     * Compares two GUIDs.
     *
     * @param that the GUID to be compared to this one.
     * @return <code>int</code> of the comparison's result. If it is a negative number,
     *         this GUID is sorted before the passed
     *         GUID. If it s a positive number, this GUID is sorted after the
     *         passed GUID.Returns zero if the GUIDs are equal.
     * @see java.lang.String#compareTo(String)
     */
    public int compareTo(GUID that) {
        if (this == that) return 0;
        if (this.id == null) {
            return (that.id == null ? 0 : -1);
        } else if (that.id == null) {
            return 1;
        }

        return id.compareTo(that.id);
    }

    /**
     * Sets the string value of this GUID.
     * <p/>
     * param id The string value to be used for this GUID.
     *
     * @vgnInternal
     */
    protected void setId(String id) {
        checkId(id);
        this.id = id;
    }

    /**
     * Gets the GUID as a <code>String</code>.
     *
     * @return <code>id</code> - <code>String</code> of this GUID.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets a <code>String</code> representation of this object.
     *
     * @return <code>id</code> - <code>String</code> representation of this object.
     */
    public String toString() {
        return id;
    }

    /**
     * Gets the embedded type code.  This is the last 4 characters of the
     * ID.
     *
     * @return <code>String</code> of the 4 character embedded type code.
     */
    public String getTypeCode() {
        return id.substring(TYPE_CODE_POSITION);
    }

    /**
     * Returns the hash code for this object.
     *
     * @return <code>int</code> of the hash code for this object.
     */
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Returns a string that can be used in SQL statement.
     *
     * @return A hash code.
     * @vgnInternal
     */
    public String toSQLString() {
        return "'" + id + "'";
    }

    /**
     * Validates the id length and characters.
     */
    protected static final void checkId(String id) {

        if (id.length() != ID_LENGTH) {
            throw ScalarRuntimeException.create(
                    MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.ILLEGAL_ID_LENGTH,
                            id, String.valueOf(ID_LENGTH)));
        }

        // Validate the characters.  Whitespace is not allowed.
        char[] chars = id.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isWhitespace(chars[i])) {
                throw ScalarRuntimeException.create(
                        MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.WHITESPACE_NOT_ALLOWED_IN_ID));
            }
        }

    } // End checkId

    /**
     * Determine if id is syntactically valid.
     *
     * @param id a String
     * @return <code>true</code> iff id is syntactically valid.
     */
	public static boolean isValid(String id) {
		try {
			checkId(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

} // End GUID

