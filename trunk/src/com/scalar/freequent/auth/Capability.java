package com.scalar.freequent.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 5, 2013
 * Time: 9:20:53 PM
 */
public class Capability {
    protected static final Log logger = LogFactory.getLog(Capability.class);
    private String name = null;
    private boolean read;
    private boolean write;
    private boolean delete;

    public static final String READ_TYPE = "read";
    public static final String WRITE_TYPE = "write";
    public static final String DELETE_TYPE = "delete";

    public Capability(String name) {
        this.name = name;
    }

    public Capability(String name, boolean read, boolean write, boolean delete) {
        this.name = name;
        this.read = read;
        this.write = write;
        this.delete = delete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
