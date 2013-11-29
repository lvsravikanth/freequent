package com.scalar.freequent.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 5, 2013
 * Time: 9:20:53 PM
 */
public class Capability {
    protected static final Log logger = LogFactory.getLog(Capability.class);
    private String name = null;
    private boolean supportsRead;
    private boolean supportsWrite;
    private boolean supportsDelete;

    public static final String READ_TYPE = "read";
    public static final String WRITE_TYPE = "write";
    public static final String DELETE_TYPE = "delete";

	public static final String USER_CAPABILITY = "USER";
	public static final Capability USER_READ = new Capability(USER_CAPABILITY, true, false, false);
	public static final Capability USER_WRITE = new Capability(USER_CAPABILITY, false, true, false);
	public static final Capability USER_DELETE = new Capability(USER_CAPABILITY, false, false, true);

	public static final String ITEM_CAPABILITY = "ITEM";
	public static final Capability ITEM_READ = new Capability(ITEM_CAPABILITY, true, false, false);
	public static final Capability ITEM_WRITE = new Capability(ITEM_CAPABILITY, false, true, false);
	public static final Capability ITEM_DELETE = new Capability(ITEM_CAPABILITY, false, false, true);

	public Capability() {
    }

    public Capability(String name) {
        this.name = name;
    }

    public Capability(String name, boolean supportsRead, boolean supportsWrite, boolean supportsDelete) {
        this.name = name;
        this.supportsRead = supportsRead;
        this.supportsWrite = supportsWrite;
        this.supportsDelete = supportsDelete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSupportsRead() {
        return supportsRead;
    }

    public void setSupportsRead(boolean supportsRead) {
        this.supportsRead = supportsRead;
    }

    public boolean isSupportsWrite() {
        return supportsWrite;
    }

    public void setSupportsWrite(boolean supportsWrite) {
        this.supportsWrite = supportsWrite;
    }

    public boolean isSupportsDelete() {
        return supportsDelete;
    }

    public void setSupportsDelete(boolean supportsDelete) {
        this.supportsDelete = supportsDelete;
    }
}
