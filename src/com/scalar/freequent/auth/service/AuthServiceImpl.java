package com.scalar.freequent.auth.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;

import javax.sql.DataSource;

import com.scalar.freequent.util.DebugUtil;
import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.dao.UserDataDAO;
import com.scalar.freequent.dao.UserDataRow;
import com.scalar.freequent.auth.User;
import com.scalar.freequent.l10n.ServiceResource;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.ScalarException;
import com.scalar.core.util.MsgObject;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.service.AbstractService;
import com.scalar.core.jdbc.DAOFactory;

import java.sql.Connection;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 10:46:14 AM
 */
public class AuthServiceImpl extends AbstractService implements ApplicationContextAware, AuthService {
    protected static final Log logger = LogFactory.getLog(AuthServiceImpl.class);
    private ApplicationContext applicationContext = null;
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Transactional
    public void dbTransactionTest() throws Exception {
        DataSource datasource = (DataSource)applicationContext.getBean("dataSource");
        DebugUtil.transactionRequired("dbTransactionTest");
        Connection conn = null;
        try {
            conn = DataSourceUtils.getConnection(datasource);
            System.out.println("conn.getAutoCommit = " + conn.getAutoCommit());
        } finally {
            DataSourceUtils.releaseConnection(conn, datasource);
        }
    }

    public void noDbTransactionTest() throws Exception {
        DataSource datasource = (DataSource)applicationContext.getBean("dataSource");
        Connection conn = null;
        try {
            conn = DataSourceUtils.getConnection(datasource);
            System.out.println("conn.getAutoCommit = " + conn.getAutoCommit());
        } finally {
            DataSourceUtils.releaseConnection(conn, datasource);
        }
    }

    public boolean checkCredentials(String username, String password) throws ScalarServiceException {
        UserDataDAO userDataDAO = DAOFactory.getDAO(UserDataDAO.class, getRequest());
        try {
        return userDataDAO.checkUserCredentials(username, StringUtil.encrypt(password));
        } catch (ScalarException e) {
            MsgObject msgObject = MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.CHECK_CREDENTIALS_FAILED);
            throw ScalarServiceException.create(msgObject, e);
        }
    }

    public User getUser(String username) throws ScalarServiceException {
        UserDataDAO userDataDAO = DAOFactory.getDAO(UserDataDAO.class, getRequest());
        UserDataRow row = userDataDAO.findByPrimaryKey(username);
        User user = UserDataDAO.rowToData(row);

        return user;
    }
}
