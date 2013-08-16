
package com.scalar.freequent.web.lifecycle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * The <code>BasicServletContextListener</code> class is the implementation of the <code>ServletContextListener</code>
 * interface and provides startup and shutdown services for the framework.
 *
 * @author .sujan.
 * @version $Revision: #1 $ $Date: 2011/11/08 $
 */
public class BasicServletContextListener implements ServletContextListener, HttpSessionListener {
	/**
	 * Constructs a new <code>BasicServletContextListener</code>. This is the default constructor.
	 */
	public BasicServletContextListener() {
	}

	/**
	 * Initializes the environment.
	 *
	 * @param servletContextEvent the <code>ServletContextEvent</code>
	 */
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			//do it
		} catch ( Throwable t ) {
			// Catching just throwable on purpose
			outputError("Failed to initialize context", t);
		}
	}

	/**
	 * Destroys the environment.
	 *
	 * @param servletContextEvent the <code>ServletContextEvent</code>
	 */
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

		try {
			//do it
		} catch ( Throwable t ) {
			// Catching just throwable on purpose
			outputError("Failed to destroy context", t);
		}
	}


	public void sessionCreated(HttpSessionEvent event) {
		// Nothing to do here
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		// Preferences need cleanup
		//cleanup user cache
	}

	/**
	 * Output error to <code>System.err</code> and <code>System.out</code>.
	 *
	 * @param message the error message
	 * @param t the throwable
	 */
	protected void outputError(String message, Throwable t) {
		System.err.println(message);

		System.out.println(message);

		if ( null != t ) {
			System.err.println(t.getLocalizedMessage());
			t.printStackTrace(System.err);

			System.out.println(t.getLocalizedMessage());
			t.printStackTrace(System.out);
		}
	}
}
