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
	List<Item> findAll() throws ScalarServiceException;
	List<Item> search(Map<String, String> searchParams) throws ScalarServiceException;
	Item findByName(String name) throws ScalarServiceException;
	Item findById(String id) throws ScalarServiceException;
	public boolean exists (String itemName) throws ScalarServiceException;
	boolean insertOrUpdate(Item item) throws ScalarServiceException;
	boolean removeByName(String name) throws ScalarServiceException;
	boolean remove(String id) throws ScalarServiceException;
}
