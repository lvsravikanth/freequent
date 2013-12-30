package com.scalar.freequent.service;

import com.scalar.core.ScalarServiceException;
import com.scalar.freequent.common.InvoiceLineItemData;

import java.util.List;

/**
 * User: Sujan Kumar Suppala
 * Date: Dec 30, 2013
 * Time: 7:23:02 PM
 */
public interface InvoiceLineItemDataService {
	List<InvoiceLineItemData> findAll(String invoiceId) throws ScalarServiceException;
	InvoiceLineItemData findByLineNumber(String invoiceId, int lineNumber) throws ScalarServiceException;
	InvoiceLineItemData findById(String id) throws ScalarServiceException;
	public boolean exists (String invoiceId, int lineNumber) throws ScalarServiceException;
	boolean insertOrUpdate(InvoiceLineItemData orderData) throws ScalarServiceException;
	boolean removeByInvoiceId(String invoiceId) throws ScalarServiceException;
	boolean removeByLineNumber(String invoiceId, int lineNumber) throws ScalarServiceException;
	boolean remove(String id) throws ScalarServiceException;
}