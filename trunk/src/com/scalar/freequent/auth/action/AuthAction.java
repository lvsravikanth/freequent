package com.scalar.freequent.auth.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import com.scalar.freequent.web.spring.controller.AbstractActionFormController;
import com.scalar.freequent.web.spring.controller.AbstractActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Sujan Kumar Suppala
 * Date: Aug 18, 2013
 * Time: 3:20:47 PM
 */
public class AuthAction extends AbstractActionController {
    protected static final Log logger = LogFactory.getLog(AuthAction.class);

    public ModelAndView defaultProcess(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ModelAndView modelAndView = new ModelAndView("auth/login");
        return modelAndView;
    }
}
