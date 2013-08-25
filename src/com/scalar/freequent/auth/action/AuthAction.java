package com.scalar.freequent.auth.action;

import com.scalar.core.request.Request;
import com.scalar.core.response.Response;
import com.scalar.core.service.ServiceFactory;
import com.scalar.freequent.auth.service.AuthService;
import com.scalar.freequent.web.spring.controller.AbstractActionController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 18, 2013
 * Time: 3:20:47 PM
 */
public class AuthAction extends AbstractActionController {
    protected static final Log logger = LogFactory.getLog(AuthAction.class);

    public ModelAndView defaultProcess(Request request, Response response, Object command, BindException errors) throws Exception {
        ModelAndView modelAndView = new ModelAndView("auth/login");
        AuthService authService = ServiceFactory.getService(AuthService.class, request);
        authService.dbTransactionTest();
        authService.noDbTransactionTest();
        return modelAndView;
    }
}
