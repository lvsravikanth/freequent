package com.scalar.freequent.common;

import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 7, 2013
 * Time: 8:05:34 PM
 */
public interface HasRecord {
	Record getRecord();
	Map<String, Object> toMap();
}
