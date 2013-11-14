package com.scalar.freequent.service;

import com.scalar.freequent.common.UnitData;
import com.scalar.core.ScalarServiceException;

import java.util.List;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 14, 2013
 * Time: 8:24:49 PM
 */
public interface UnitService {
	List<UnitData> findAll();
	UnitData findByName(String name);
	boolean insertOrUpdate(UnitData unitData) throws ScalarServiceException;
	boolean removeByName(String name);
	boolean remove(String id);
}
