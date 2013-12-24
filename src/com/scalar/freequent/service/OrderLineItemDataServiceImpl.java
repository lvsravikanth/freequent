package com.scalar.freequent.service;

import com.scalar.core.ScalarException;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.jdbc.DAOFactory;
import com.scalar.core.service.AbstractService;
import com.scalar.core.service.ServiceFactory;
import com.scalar.core.util.GUID;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.freequent.common.OrderLineItemData;
import com.scalar.freequent.common.Item;
import com.scalar.freequent.dao.OrderLineItemDataDAO;
import com.scalar.freequent.dao.OrderLineItemDataRow;
import com.scalar.freequent.l10n.ServiceResource;
import com.scalar.freequent.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 30, 2013
 * Time: 11:25:54 AM
 */
public class OrderLineItemDataServiceImpl extends AbstractService implements OrderLineItemDataService {
	protected static final Log logger = LogFactory.getLog(OrderLineItemDataServiceImpl.class);

	public List<OrderLineItemData> findAll(String orderId) {
		OrderLineItemDataDAO orderDataDAO = DAOFactory.getDAO(OrderLineItemDataDAO.class, getRequest());
		List<OrderLineItemDataRow> rows = orderDataDAO.findAll(orderId);
		List<OrderLineItemData> orderLineItemDatas = new ArrayList<OrderLineItemData>(rows.size());
		for (OrderLineItemDataRow row : rows) {
			OrderLineItemData orderLineItemData = OrderLineItemDataDAO.rowToData(row);
			setRelations(orderLineItemData);
			orderLineItemDatas.add(orderLineItemData);
		}
		return orderLineItemDatas;
	}

	public OrderLineItemData findByLineNumber(String orderId, int lineNumber) throws ScalarServiceException {
		OrderLineItemDataDAO orderLineItemDataDAO = DAOFactory.getDAO(OrderLineItemDataDAO.class, getRequest());
		OrderLineItemDataRow orderLineItemDataRow = orderLineItemDataDAO.findByLineNumber(orderId, lineNumber);
		OrderLineItemData orderLineItemData = null;
		if (orderLineItemDataRow != null) {
			orderLineItemData = OrderLineItemDataDAO.rowToData(orderLineItemDataRow);
			setRelations(orderLineItemData);
		}

		return orderLineItemData;
	}

	public boolean exists(String orderId, int lineNumber) throws ScalarServiceException {
		OrderLineItemDataDAO orderLineItemDataDAO = DAOFactory.getDAO(OrderLineItemDataDAO.class, getRequest());
		return orderLineItemDataDAO.existsByLineNumber(orderId, lineNumber);
	}

	@Transactional
	public boolean removeByOrderId(String orderId) throws ScalarServiceException {
		OrderLineItemDataDAO orderLineItemDataDAO = DAOFactory.getDAO(OrderLineItemDataDAO.class, getRequest());
		return orderLineItemDataDAO.removeByOrderId(orderId) != 0;
	}

	@Transactional
	public boolean removeByLineNumber(String orderId, int lineNumber) throws ScalarServiceException {
		OrderLineItemDataDAO orderLineItemDataDAO = DAOFactory.getDAO(OrderLineItemDataDAO.class, getRequest());
		return orderLineItemDataDAO.removeByLineNumber(orderId, lineNumber) != 0;
	}


	public OrderLineItemData findById(String id) throws ScalarServiceException {
		OrderLineItemDataDAO orderLineItemDataDAO = DAOFactory.getDAO(OrderLineItemDataDAO.class, getRequest());
		OrderLineItemData orderLineItemData = null;
		OrderLineItemDataRow orderLineItemDataRow = orderLineItemDataDAO.findByPrimaryKey(id);
		orderLineItemData = OrderLineItemDataDAO.rowToData(orderLineItemDataRow);
		setRelations(orderLineItemData);

		return orderLineItemData;
	}

	private void setRelations(OrderLineItemData orderLineItemData) {
		// nothing for now
	}

	@Transactional
	public boolean insertOrUpdate(OrderLineItemData orderLineItemData) throws ScalarServiceException {
		boolean isNew = StringUtil.isEmpty(orderLineItemData.getId());
		OrderLineItemDataDAO orderLineItemDataDAO = DAOFactory.getDAO(OrderLineItemDataDAO.class, getRequest());
		ItemDataService itemDataService = ServiceFactory.getService(ItemDataService.class, getRequest());
		Item itemData = itemDataService.findById(orderLineItemData.getItemId());
		if (isNew) {
			// insert
			try {
				orderLineItemData.setId(GUID.generateString());
			} catch (ScalarException e) {
				throw ScalarServiceException.create(MsgObjectUtil.getMsgObject(e.getMessage()), e);
			}
			OrderLineItemDataRow row = OrderLineItemDataDAO.dataToRow(orderLineItemData);
			if (row.getPrice() == 0) {
				// set item price
				row.setPrice(itemData.getPrice());
			}
			row.setTaxable(itemData.getTaxable() ? 1 : 0);
			try {
				orderLineItemDataDAO.insert(row);
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_CREATE_ORDER_LINEITEM, orderLineItemData.getLineNumber());
				throw ScalarServiceException.create(msgObject, ex);
			}
		} else {
			// update
			//OrderLineItemDataRow row = OrderLineItemDataDAO.dataToRow(orderLineItemData);
			OrderLineItemDataRow row = new OrderLineItemDataRow();
			row.setId(new GUID(orderLineItemData.getId()));
			row.setOrderId(new GUID(orderLineItemData.getOrderId()));
			row.setLineNumber(orderLineItemData.getLineNumber());
			row.setItemId(new GUID(orderLineItemData.getItemId()));
			row.setQty(orderLineItemData.getQty());
			row.setPrice(itemData.getPrice());
			row.setTaxable(itemData.getTaxable() ? 1 : 0);
			try {
				orderLineItemDataDAO.update(row);
			} catch (ScalarException ex) {
				MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_UPDATE_ORDER_LINEITEM, orderLineItemData.getLineNumber());
				throw ScalarServiceException.create(msgObject, ex);
			}
		}

		return false;
	}

	@Transactional
	public boolean remove(String id) {
		OrderLineItemDataDAO orderLineItemDataDAO = DAOFactory.getDAO(OrderLineItemDataDAO.class, getRequest());
		orderLineItemDataDAO.removeById(id);
		return true;
	}
}