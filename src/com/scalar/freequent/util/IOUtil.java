
package com.scalar.freequent.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.log4j.Level;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.core.response.Response;

import javax.servlet.http.HttpServletResponse;

/**
 * The <code>IOUtil</code> class provides utility functions for working with I/O.
 *
 * @author .r.
 * @version $Revision: #1 $ $Date: 2011/11/08 $
 */
public final class IOUtil {
    protected static final Log logger = LogFactory.getLog(IOUtil.class);
	/**
	 * Constant used to for character encoding/decoding.
	 */
	public static final String UTF8 = "UTF-8";

	/**
	 * Constant used for buffer sizes.
	 */
	private static final int BUFFER_SIZE = 8192;

	/**
	 * Constructs a new <code>IOUtil</code>. This is the default constructor and is unavailable to others.
	 */
	private IOUtil() {
	}

	/**
	 * Checks to see if a file exists.
	 *
	 * @param name name of the file to check
	 * @return a <code>File</code> if the file exists; otherwise <code>null</code>
	 */
	public static File fileExists(String name) {
		File file = new File(name);
		if ( file.exists() ) {
			return file;
		}

		return null;
	}

	/**
	 * Copies all the data from the <code>Reader</code> to the <code>Writer</code>.
	 *
	 * @param reader the data source
	 * @param writer the data destination
	 * @throws IOException if this is a failure while reading or writing
	 */
	public static void readAndWrite(Reader reader, Writer writer) throws IOException {
		char[] buffer = new char[BUFFER_SIZE];

		while ( true ) {
			int size = reader.read(buffer);

			if ( size > 0 ) {
				writer.write(buffer, 0, size);
			}

			if ( size < BUFFER_SIZE ) {
				break;
			}
		}
	}

	/**
	 * Returns a <code>Writer</code> from the <code>ResponseService</code>. The content type should already have
	 * been set on the <code>ResponseService</code> before calling this method.
	 *
	 * @param response the <code>Response</code>
	 * @return a <code>Writer</code>
	 * @throws IOException if there is a problem getting the <code>Writer</code>
	 */
	public static Writer getResponseWriter(Response response) throws IOException {
		// Check global setting
		boolean useOutputStream = Global.getBoolean("core.use.output.stream");
		if ( useOutputStream ) {
			// We need to be able to spew output
			OutputStream outputStream = null;
			try {
				outputStream = ((HttpServletResponse)response.getWrappedObject()).getOutputStream();
			} catch ( IOException e ) {
				// Can't create output :(
				if ( null == outputStream ) {
					if ( logger.isErrorEnabled() ) {
						logger.error("Unable to get output stream from service", e);
					}

					return null;
				}
			}

			// Create the writer, matching the preferred request character
			// encoding below (UTF-8)
			return new OutputStreamWriter(outputStream, IOUtil.UTF8);
		} else {
			return ((HttpServletResponse)response.getWrappedObject()).getWriter();
		}
	}
}
