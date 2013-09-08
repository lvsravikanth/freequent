package com.scalar.freequent.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 7, 2013
 * Time: 7:56:41 PM
 */
public class UserCapability {
    protected static final Log logger = LogFactory.getLog(UserCapability.class);

    private String capabilityName;
    private String userId;
    private boolean hasRead;
    private boolean hasWrite;
    private boolean hasDelete;

    public String getCapabilityName() {
        return capabilityName;
    }

    public void setCapabilityName(String capabilityName) {
        this.capabilityName = capabilityName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public boolean isHasWrite() {
        return hasWrite;
    }

    public void setHasWrite(boolean hasWrite) {
        this.hasWrite = hasWrite;
    }

    public boolean isHasDelete() {
        return hasDelete;
    }

    public void setHasDelete(boolean hasDelete) {
        this.hasDelete = hasDelete;
    }
}
