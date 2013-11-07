package com.scalar.freequent.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 6, 2013
 * Time: 9:25:38 PM
 */
public class Record {
	protected static final Log logger = LogFactory.getLog(Record.class);

	protected String recordId;
	protected String objectId;
	protected String objectType;
	protected String createdBy;
	protected Date createdOn;
	protected String modifiedBy;
	protected Date modifiedOn;

	public static final String RECORD_ID = "recordId";
	public static final String OBJECT_ID = "objectId";
	public static final String OBJECT_Type = "objectType";
	public static final String CREATED_BY = "createdBy";
	public static final String CREATED_ON = "createdOn";
	public static final String MODIFIED_BY = "modifiedBy";
	public static final String MODIFIED_ON = "modifiedOn";

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Map<String, Object> convertToMap () {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put (RECORD_ID, getRecordId());
		map.put (OBJECT_ID, getObjectId());
		map.put (OBJECT_Type, getObjectType());
		map.put (CREATED_ON, getCreatedOn());
		map.put (CREATED_BY, getCreatedBy());
		map.put (MODIFIED_ON, getModifiedOn());
		map.put (MODIFIED_BY, getModifiedBy());

		return map;
	}
}
