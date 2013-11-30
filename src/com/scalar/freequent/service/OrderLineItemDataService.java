package com.scalar.freequent.service;

import com.scalar.core.ScalarServiceException;
import com.scalar.freequent.common.OrderLineItemData;

import java.util.List;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 14, 2013
 * Time: 11:23:02 PM
 */
public interface OrderLineItemDataService {
	List<OrderLineItemData> findAll(String orderId) throws ScalarServiceException;
	OrderLineItemData findByLineNumber(String orderId, int lineNumber) throws ScalarServiceException;
	OrderLineItemData findById(String id) throws ScalarServiceException;
	public boolean exists (String orderId, int lineNumber) throws ScalarServiceException;
	boolean insertOrUpdate(OrderLineItemData orderData) throws ScalarServiceException;
	boolean removeByOrderId(String orderId) throws ScalarServiceException;
	boolean removeByLineNumber(String orderId, int lineNumber) throws ScalarServiceException;
	boolean remove(String id) throws ScalarServiceException;
}