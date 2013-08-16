package com.scalar.freequent.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The <code>GZIPFilter</code> class implements <code>Filter</code> to provide GZIP-ing of output from the servlet.
 *
 * @author .sujan.
 */
public class GZIPFilter implements Filter {
	/**
	 * Constant used to identify the accept encoding header.
	 */
	protected static final String ACCEPT_ENCODING_HEADER = "Accept-Encoding";

	/**
	 * Constant used to identify the content encoding header.
	 */
	protected static final String CONTENT_ENCODING_HEADER = "Content-Encoding";

	/**
	 * Constant used to identify the GZIP encoding.
	 */
	protected static final String GZIP_ENCODING = "gzip";

	/**
	 * Constant used to idenitfy the encoding init parameter.
	 */
	protected static final String ENCODING_PARAMETER = "encoding";

	/**
	 * Request character encoding.
	 */
	protected String encoding = null;

	public void init(FilterConfig filterConfig) throws ServletException {
		setEncoding(filterConfig.getInitParameter(ENCODING_PARAMETER));
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		// Handle request character encoding
		String encode = getEncoding();
		if ( (null != encode) && (null == servletRequest.getCharacterEncoding()) ) {
			servletRequest.setCharacterEncoding(encode);
		}

		// Default behavior
		boolean doChain = true;

		// HTTP and not already filtering?
		while ( (HttpServletRequest.class.isInstance(servletRequest)) && !GZIPResponseWrapper.class.isInstance(servletResponse) ) {
			HttpServletRequest httpServletRequest = HttpServletRequest.class.cast(servletRequest);
			HttpServletResponse httpServletResponse = HttpServletResponse.class.cast(servletResponse);

			// Supports gzip ?
			String acceptEncoding = httpServletRequest.getHeader(ACCEPT_ENCODING_HEADER);
			if ( (null == acceptEncoding) || (acceptEncoding.indexOf(GZIP_ENCODING) == -1) ) {
				break;
			}

			// Wrap it
			GZIPResponseWrapper gzipResponseWrapper = new GZIPResponseWrapper(httpServletResponse);

			// Do it
			filterChain.doFilter(servletRequest, gzipResponseWrapper);

			// Get the output
			byte[] bytes = gzipResponseWrapper.finish();

			if ( null != bytes ) {
				// Set the headers
				httpServletResponse.addHeader(CONTENT_ENCODING_HEADER, GZIP_ENCODING);

				// Send the output
				ServletOutputStream stream = httpServletResponse.getOutputStream();
				stream.write(bytes);
				stream.flush();
			}

			doChain = false;

			break;
		}

		// Need default behavior?
		if ( doChain ) {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	public void destroy() {
		// Nothing for us to do
	}

	/**
	 * Sets the request character encoding value.
	 *
	 * @param encoding the request character encoding value.
	 * @see #getEncoding()
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Returns the request character encoding.
	 *
	 * @return the request character encoding.
	 * @see #setEncoding(String)
	 */
	public String getEncoding() {
		return encoding;
	}
}
