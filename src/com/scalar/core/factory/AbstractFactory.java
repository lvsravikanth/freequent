package com.scalar.core.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import com.scalar.core.ContextUtil;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 5:28:44 PM
 */
public abstract class AbstractFactory implements Factory {
    protected static final Log logger = LogFactory.getLog(AbstractFactory.class);

    /**
	 * Returns a <code>BeanFactory</code> for the given config path.
	 *
	 * @return a <code>BeanFactory</code>
	 */
	protected static BeanFactory getFactory() {

		return ContextUtil.getApplicationContext();
	}

}
