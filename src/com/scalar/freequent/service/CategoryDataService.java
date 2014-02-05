package com.scalar.freequent.service;

import com.scalar.core.ScalarServiceException;
import com.scalar.freequent.common.CategoryData;

import java.util.List;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 14, 2013
 * Time: 8:24:49 PM
 */
public interface CategoryDataService {
	List<CategoryData> findAll();
	CategoryData findByName(String name);
    CategoryData findById(String id);
    public boolean exists (String id) throws ScalarServiceException;
    List<CategoryData> search(Map<String, Object> searchParams) throws ScalarServiceException;
	boolean insertOrUpdate(CategoryData categoryData) throws ScalarServiceException;
	boolean removeByName(String name);
	boolean remove(String id);
}