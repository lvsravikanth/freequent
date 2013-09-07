package com.scalar.freequent.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.Map;

import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.util.Global;


/**
 * User: Sujan Kumar Suppala
 * Date: Sep 5, 2013
 * Time: 9:18:25 PM
 */
public class User {
    protected static final Log logger = LogFactory.getLog(User.class);
    private static final ThreadLocal userThreadLocal = new ThreadLocal(){
        @Override
		protected Object initialValue() {
			return null;
		}
    };

    private String userId;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date createdOn;
    private Date modifiedOn;
    private Date expiresOn;
    private boolean disabled;
    private Map<String, Capability> capabilitiesMap = null;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Map<String, Capability> getCapabilitiesMap() {
        return capabilitiesMap;
    }

    public void setCapabilitiesMap(Map<String, Capability> capabilitiesMap) {
        this.capabilitiesMap = capabilitiesMap;
    }

    public static void setActiveUser(User user) {
        userThreadLocal.set(user);
    }

    public static void unset() {
        userThreadLocal.remove();
    }

    public static User getActiveUser() {
        return (User)userThreadLocal.get();
    }

    public boolean checkCapability ( Capability capability )
    {
        boolean isAuthenticated =  false;
        // admin user has all the capabilities
        if (Global.getAdminUserId().equals(getUserId())) {
            return true;
        }
        if ( capability != null && capabilitiesMap != null)
        {
            if ( !StringUtil.isEmpty( capability.getName() ) )
            {
                Capability cap = capabilitiesMap.get( capability.getName() );
                if ( cap != null ) //USER HAS THIS FUNCTIONALITY
                {
                    if ( capability.isRead() )
                    {
                        isAuthenticated = cap.isRead();
                    }
                    if ( capability.isWrite() )
                    {
                        isAuthenticated = cap.isWrite();
                    }
                    if ( capability.isDelete() )
                    {
                        isAuthenticated = cap.isDelete();
                    }
                }
            }
            else
            {
                isAuthenticated = true;
            }
        }
        else
        {
            isAuthenticated = true;
        }
        return isAuthenticated;
    }
}
