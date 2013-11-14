package com.scalar.freequent.service;

import com.scalar.core.ScalarServiceException;
import com.scalar.freequent.common.Item;

import java.util.List;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 14, 2013
 * Time: 11:23:02 PM
 */
public interface ItemDataService {
	List<Item> findAll();
	List<Item> search(Map<String, String> searchParams);
	Item findByName(String name);
	Item findById(String id);
	boolean insertOrUpdate(Item item) throws ScalarServiceException;
	boolean removeByName(String name);
	boolean remove(String id);
}
