package com.scalar.freequent.service;

import com.scalar.core.ScalarServiceException;
import com.scalar.freequent.common.InvoiceData;
import com.scalar.freequent.common.OrderData;

import java.util.List;
import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Dec 30, 2013
 * Time: 8:23:02 PM
 */
public interface InvoiceDataService {
	List<InvoiceData> findAll() throws ScalarServiceException;
	List<InvoiceData> search(Map<String, Object> searchParams) throws ScalarServiceException;
	InvoiceData findByInvoiceNumber(String invoiceNumber) throws ScalarServiceException;
	InvoiceData findById(String id) throws ScalarServiceException;
	boolean exists (String invoiceNumber) throws ScalarServiceException;
	boolean insertOrUpdate(InvoiceData invoiceData) throws ScalarServiceException;
	boolean removeByInvoiceNumber(String invoiceNumber) throws ScalarServiceException;
	boolean remove(String id) throws ScalarServiceException;
	long getInvoicesCount() throws ScalarServiceException;
	InvoiceData createInvoice(OrderData orderData) throws ScalarServiceException;
}