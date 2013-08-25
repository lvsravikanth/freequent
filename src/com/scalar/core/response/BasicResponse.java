package com.scalar.core.response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 24, 2013
 * Time: 5:02:47 PM
 */
public class BasicResponse implements Response {
    protected static final Log logger = LogFactory.getLog(BasicResponse.class);
    private Object wrappedObject;

    public BasicResponse() {
        //Default constuctor
    }

    public void setWrappedObject(Object wrappedObject) {
        this.wrappedObject = wrappedObject;
    }

    public Object getWrappedObject() {
        return wrappedObject;
    }
}
