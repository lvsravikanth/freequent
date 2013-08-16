package com.scalar.freequent.web.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.scalar.freequent.util.IOUtil;

/**
 * The <code>GZIPResponseWrapper</code> class extends <code>HttpServletResponse</code> to provide GZIP compressed
 * output.
 *
 * @author .sujan.
 */
@SuppressWarnings("deprecation")
public class GZIPResponseWrapper extends HttpServletResponseWrapper {
	/**
	 * The <code>PrintWriter</code> for output.
	 */
	protected PrintWriter writer = null;

	/**
	 * The <code>GZIPFilterStream</code> for output.
	 */
	protected GZIPOutputStream stream = null;

	/**
	 * Constructs a new <code>GZIPResponseWrapper</code>.
	 *
	 * @param httpServletResponse the <code>HttpServletResponse</code> to wrap
	 */
	public GZIPResponseWrapper(HttpServletResponse httpServletResponse) {
		super(httpServletResponse);
	}

	/**
	 * Closes the output stream and returns the output bytes.
	 *
	 * @return the output bytes.
	 * @throws IOException if there is a problem finishing.
	 */
	public byte[] finish() throws IOException {
		byte[] bytes = null;

		if ( null != stream ) {
			if ( !stream.isClosed() ) {
				stream.close();
			}
			bytes = stream.getBytes();
		}

		return bytes;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if ( null != stream ) {
			return stream;
		}

		stream = new GZIPOutputStream();

		return stream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if ( null != writer ) {
			return writer;
		}

		// This will set stream
		getOutputStream();

		writer = new PrintWriter(new OutputStreamWriter(stream, IOUtil.UTF8));

		return writer;
	}

	@Override
	public void flushBuffer() throws IOException {
		if ( null != writer ) {
			writer.flush();
		}

		if ( null != stream ) {
			stream.flush();
		}
	}

	@Override
	public void setContentLength(int i) {
		// Nothing to do
	}
}
