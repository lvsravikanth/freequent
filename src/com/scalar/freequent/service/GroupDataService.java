package com.scalar.freequent.service;

import com.scalar.freequent.common.UnitData;
import com.scalar.freequent.common.GroupData;
import com.scalar.core.ScalarServiceException;

import java.util.List;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 14, 2013
 * Time: 8:24:49 PM
 */
public interface GroupDataService {
	List<GroupData> findAll();
	GroupData findByName(String name);
	boolean insertOrUpdate(GroupData groupData) throws ScalarServiceException;
	boolean removeByName(String name);
	boolean remove(String id);
}