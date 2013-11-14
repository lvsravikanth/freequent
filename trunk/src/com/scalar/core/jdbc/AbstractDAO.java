package com.scalar.core.jdbc;

import com.scalar.core.request.Request;
import com.scalar.core.util.GUID;
import com.scalar.core.ScalarException;
import com.scalar.freequent.dao.RecordDataDAO;
import com.scalar.freequent.dao.RecordDataRow;
import com.scalar.freequent.common.ObjectType;
import com.scalar.freequent.auth.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 4:38:24 PM
 */
public class AbstractDAO extends JdbcDaoSupport implements DAO {
    protected static final Log logger = LogFactory.getLog(AbstractDAO.class);

    /**
     * The <code>Request</code> that is currently being handled. This is useful for utilizing the built-in cache.
     *
     * @see #setRequest(com.scalar.core.request.Request)
     * @see #getRequest()
     * @see com.scalar.core.request.Request#putCachedData(Object, Object)
     * @see com.scalar.core.request.Request#getCachedData(Object)
     */
    private Request request;

    /**
     * This is the default constructor for <code>AbstractService</code>.
     */
    public AbstractDAO() {
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

	protected int insertRecord (GUID objectId, String objectType) throws ScalarException {
		RecordDataDAO recordDataDAO = DAOFactory.getDAO(RecordDataDAO.class, getRequest());
		RecordDataRow row = new RecordDataRow();
		row.setRecordId(GUID.generate(ObjectType.TYPE_CODE_RECORD));
		row.setObjectId(objectId);
		row.setObjectType(objectType);
		row.setCreatedBy(User.getActiveUser().getUserId());
		return recordDataDAO.insert(row);
	}

	protected int updateRecord (GUID objectId) throws ScalarException {
		RecordDataDAO recordDataDAO = DAOFactory.getDAO(RecordDataDAO.class, getRequest());
		RecordDataRow row = new RecordDataRow();
		row.setObjectId(objectId);
		row.setModifiedBy(User.getActiveUser().getUserId());
		return recordDataDAO.updateByObjectId(row);
	}
}