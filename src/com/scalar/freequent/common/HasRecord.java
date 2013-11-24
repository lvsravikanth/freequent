package com.scalar.freequent.common;

import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 7, 2013
 * Time: 8:05:34 PM
 */
public interface HasRecord {
	public static final String ATTR_RECORD = "record";
	Record getRecord();
	void setRecord(Record record);
	Map<String, Object> toMap();
}
