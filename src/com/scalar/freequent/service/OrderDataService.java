package com.scalar.freequent.service;

import com.scalar.core.ScalarServiceException;
import com.scalar.freequent.common.Item;
import com.scalar.freequent.common.OrderData;

import java.util.List;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 14, 2013
 * Time: 11:23:02 PM
 */
public interface OrderDataService {
	List<OrderData> findAll() throws ScalarServiceException;
	List<OrderData> search(Map<String, Object> searchParams) throws ScalarServiceException;
	OrderData findByOrderNumber(String orderNumber) throws ScalarServiceException;
	OrderData findById(String id) throws ScalarServiceException;
	public boolean exists (String orderNumber) throws ScalarServiceException;
	boolean insertOrUpdate(OrderData orderData, boolean createInvoice) throws ScalarServiceException;
	boolean removeByOrderNumber(String orderNumber) throws ScalarServiceException;
	boolean remove(String id) throws ScalarServiceException;
	long getOrdersCount() throws ScalarServiceException;
}