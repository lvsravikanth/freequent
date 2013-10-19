package com.scalar.freequent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 7, 2013
 * Time: 12:29:35 PM
 *
 *  Holds the data from a single row of the FTUSERS table.
 *
 */
public class UserDataRow {
    protected static final Log logger = LogFactory.getLog(UserDataRow.class);

    /**
     *  UserId
     */
    private String mUserId;

    /**
     *  <CODE>true</CODE> if the UserId column has been modified.
     */
    private boolean mModUserlId = false;

    /**
     *  Replaces the value of the UserId column.
     *
     *  @param x The new value for the column.
     */
    public void setUserId(String x){ mUserId = x; mModUserlId = true; }

    /**
     *  Determine whether the UserId column has been modified.
     *
     *  @return <CODE>true</CODE> if the UserId column has been modified.
     */
    public boolean modUserId(){ return mModUserlId; }

    /**
     *  Obtain the value of the UserId column.
     *
     *  @return The value of the column.
     */
    public String getUserId(){ return mUserId; }

    private String mFirstName;
    private boolean mModFirstName = false;

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String x) {
        this.mFirstName = x; mModFirstName = true;
    }

    public boolean modFirstName() {
        return mModFirstName;
    }

    private String mMiddleName;
    private boolean mModMiddleName = false;

    public String getMiddleName () {
        return mMiddleName;
    }

    public void setMiddleName (String x) {
        mMiddleName = x; mModMiddleName = true;
    }

    public boolean modMiddleName () {
        return mModMiddleName;
    }

    private String mLastName;
    private boolean mModLastName;

    public String getLastName () {
        return mLastName;
    }

    public void setLastName (String x) {
        mLastName = x; mModLastName = true;
    }

    public boolean modLastName () {
        return mModLastName;
    }

    private String mPassword;
    private boolean mModPassword = false;

    public String getPassword () {
        return mPassword;
    }

    public void setPassword (String x) {
        mPassword = x; mModPassword = true;
    }

    public boolean modPassword () {
        return mModPassword;
    }

	private int mDisabled;
	private boolean mModDisabled = false;

	public int getDisabled() {
		return mDisabled;
	}

	public void setDisabled(int x) {
		mDisabled = x; mModDisabled = true;
	}

	public boolean modDisabled() {
		return mModDisabled;
	}

	private Date mExpiresOn;
	private boolean mModExpiresOn = false;

	public Date getExpiresOn() {
		return mExpiresOn;
	}

	public void setExpiresOn(Date x) {
		mExpiresOn = x; mModExpiresOn = true;
	}

	public boolean modExpiresOn() {
		return mModExpiresOn;
	}

	private String mCreatedBy;
	public String getCreatedBy() {
		return mCreatedBy;
	}

	public void setCreatedBy(String x) {
		mCreatedBy = x;
	}

	private String mModifiedBy;
	private boolean mModModifiedBy = false;
	public String getModifiedBy() {
		return mModifiedBy;
	}

	public void setModifiedBy(String x) {
		mModifiedBy = x; mModModifiedBy = true;
	}

	public boolean modModifiedBy() {
		return mModModifiedBy;
	}

	private Date mCreatedOn;

	public Date getCreatedOn() {
		return mCreatedOn;
	}

	public void setCreatedOn(Date x) {
		mCreatedOn = x;
	}

	private Date mModifiedOn;

	public Date getModifiedOn() {
		return mModifiedOn;
	}

	public void setModifiedOn(Date x) {
		mModifiedOn = x;
	}

	/**
     *  Determine whether any column of the row has been modified.
     *
     *  @return <CODE>true</CODE> if any column has been modified.
     */
    public boolean modified() {
        boolean result = false;
        if(
            mModUserlId ||
            mModFirstName ||
            mModMiddleName ||
			mModLastName ||
            mModPassword ||
			mModDisabled ||
			mModExpiresOn ||
			mModModifiedBy ||
			false ){ result = true; }
        return result;
    }

    /**
     *  Set all of the column modified flags to false.
     */
    public void clean() {
        mModUserlId = false;
        mModFirstName = false;
        mModMiddleName = false;
        mModLastName = false;
        mModPassword = false;
		mModDisabled = false;
		mModExpiresOn  = false;
		mModModifiedBy = false;
	}
}
