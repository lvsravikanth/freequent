package com.scalar.freequent.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.HashMap;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 6, 2013
 * Time: 9:15:27 PM
 */
public class GroupData implements HasRecord  {
	protected static final Log logger = LogFactory.getLog(GroupData.class);

	private String id;
	private String name;
	private String description;
	private Record record;

	public static final String ATTR_ID = "id";
	public static final String ATTR_NAME = "name";
	public static final String ATTR_DESCRIPTION = "description";
	public static final String ATTR_RECORD = "record";

    public static final String PARAM_ID = "id";
    public static final String PARAM_NAME = "name";
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Record getRecord() {
		return record;
	}
    public Map<String, Object> toMap() {
		return toMap(this);
	}
    
	public Map<String, Object> toMap(GroupData groupData) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put (ATTR_ID, id);
		map.put (ATTR_NAME, name);
		map.put (ATTR_DESCRIPTION, description);
		map.put (ATTR_RECORD, groupData.getRecord().toMap());
		return map;
	}

	public void setRecord(Record record) {
		this.record = record;
	}
}
