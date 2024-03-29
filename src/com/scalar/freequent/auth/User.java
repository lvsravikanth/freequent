package com.scalar.freequent.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.util.Global;
import com.scalar.freequent.util.Constants;
import com.scalar.freequent.l10n.FrameworkResource;
import com.scalar.freequent.common.HasRecord;
import com.scalar.freequent.common.Record;
import com.scalar.core.ScalarAuthException;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.MsgObjectUtil;


/**
 * User: Sujan Kumar Suppala
 * Date: Sep 5, 2013
 * Time: 9:18:25 PM
 */
public class User implements HasRecord {
    protected static final Log logger = LogFactory.getLog(User.class);
    private static final ThreadLocal userThreadLocal = new ThreadLocal(){
        @Override
		protected Object initialValue() {
			return null;
		}
    };

	public static final String USER_ID = "userid";
	public static final String FIRST_NAME = "firstname";
	public static final String LAST_NAME = "lastname";
	public static final String MIDDLE_NAME = "middlename";
	public static final String DISABLED = "disabled";
	public static final String EXPIRESON = "expireson";

	private String userId;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date createdOn;
    private Date modifiedOn;
    private Date expiresOn;
    private boolean disabled;
	private String createdBy;
	private String modifiedBy;
	private List<UserCapability> userCapabilities = null;
	private Record record;

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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
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

    public List<UserCapability> getUserCapabilities() {
        return userCapabilities;
    }

    public void setUserCapabilities(List<UserCapability> userCapabilities) {
        this.userCapabilities = userCapabilities;
    }

	private Map<String, UserCapability> userCapabilitiesMap = null;
	public Map<String, UserCapability> getUserCapabilitiesMap() {
		if (userCapabilitiesMap == null) {
			userCapabilitiesMap = new HashMap<String, UserCapability>();
			if (userCapabilities != null) {
				for (UserCapability userCapability: userCapabilities) {
					userCapabilitiesMap.put (userCapability.getCapabilityName(), userCapability);
				}
			}
		}

		return userCapabilitiesMap;
	}

    public void checkCapabilities (Capability[] capabilities) throws ScalarAuthException {
        if (StringUtil.isEmpty(capabilities)) {
            return ;
        } else {
            for (Capability capability: capabilities) {
                checkCapability(capability);
            }
        }
    }

    public void checkCapability ( Capability capability ) throws ScalarAuthException {
        if (capability == null || StringUtil.isEmpty(capability.getName())) {
            return;
        }

        // admin user has all the capabilities
        if (Global.getAdminUserId().equals(getUserId())) {
            return;
        }

        boolean isAuthorized =  false;
        Map<String, UserCapability> capabilitiesMap = getUserCapabilitiesMap();
        if ( capabilitiesMap != null) {
                UserCapability cap = capabilitiesMap.get( capability.getName() );
                if ( cap != null ) {//USER HAS THIS FUNCTIONALITY
                    if (capability.isSupportsRead() ) {
                        isAuthorized = cap.isHasRead();
                    } else if (capability.isSupportsWrite()){
                        isAuthorized = cap.isHasWrite();
                    } else if (capability.isSupportsDelete()){
                        isAuthorized = cap.isHasDelete();
                    }
                }
        }
        if (!isAuthorized) {
            String args[] = new String[2];
            args[0] = capability.getName();
            if (capability.isSupportsRead())
                args[1] = Capability.READ_TYPE;
            else if (capability.isSupportsWrite())
                args[1] = Capability.WRITE_TYPE;
            else if (capability.isSupportsDelete())
                args[1] = Capability.DELETE_TYPE;
            else
                args[1] = "UNKNOWN";
            MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.USER_DOES_NOT_HAVE_CAPABILITY, args);
            throw ScalarAuthException.create (msgObject);
        }
    }

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public Map<String, Object> toMap() {
		HashMap<String, Object> userMap = new HashMap<String,Object>();
		userMap.put(User.USER_ID, getUserId());
		userMap.put(User.FIRST_NAME, getFirstName());
		userMap.put(User.MIDDLE_NAME, getMiddleName());
		userMap.put(User.LAST_NAME, getLastName());
		userMap.put(User.DISABLED, isDisabled());
		userMap.put(User.EXPIRESON, getExpiresOn());
		userMap.put(Constants.ITEM_NAME_ATTRIBUTE, getFirstName());
		userMap.put (ATTR_RECORD, getRecord().toMap());
		return userMap;
	}
}
