package com.scalar.freequent.service;

import com.scalar.freequent.common.UnitData;
import com.scalar.core.ScalarServiceException;

import java.util.List;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 14, 2013
 * Time: 8:24:49 PM
 */
public interface UnitDataService {
	List<UnitData> findAll();
	UnitData findByName(String name);
    UnitData findById(String id);
    public boolean exists (String id) throws ScalarServiceException;
    List<UnitData> search(Map<String, Object> searchParams) throws ScalarServiceException;
	boolean insertOrUpdate(UnitData unitData) throws ScalarServiceException;
	boolean removeByName(String name);
	boolean remove(String id);
}
