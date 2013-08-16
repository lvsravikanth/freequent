package com.scalar.freequent.web.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

/**
 * The <code>GZIPOutputStream</code> class extends <code>ServletOutputStream</code> to provide GZIP compressed
 * output.
 *
 * @author .sujan.
 */
public class GZIPOutputStream extends ServletOutputStream {
	/**
	 * The output stream for the GZIP'd output.
	 */
	protected ByteArrayOutputStream byteStream;

	/**
	 * The GZIP output stream.
	 */
	protected java.util.zip.GZIPOutputStream gzipStream;

	/**
	 * The output bytes.
	 */
	protected byte[] bytes;

	/**
	 * Constructs a new <code>GZIPOutputStream</code>. This is the default constructor.
	 *
	 * @throws IOException if there is a problem creating the <code>GZIPOutputStream</code>
	 */
	public GZIPOutputStream() throws IOException {
		byteStream = new ByteArrayOutputStream();
		gzipStream = new java.util.zip.GZIPOutputStream(byteStream);
	}

	/**
	 * Returns the output bytes. Only useful after close() has been called.
	 *
	 * @return the output bytes
	 */
	public byte[] getBytes() {
		return bytes;
	}

	/**
	 * Returns <code>true</code> if the stream is closed, otherwise <code>false</code>.
	 *
	 * @return <code>true</code> if the stream is closed, otherwise <code>false</code>
	 */
	public boolean isClosed() {
		return null == gzipStream;
	}

	@Override
	public void close() throws IOException {
		if ( null == gzipStream ) {
			throw new IOException("close() has already been called.");
		}

		// We're done
		gzipStream.finish();

		bytes = byteStream.toByteArray();

		gzipStream = null;
		byteStream = null;
	}

	@Override
	public void flush() throws IOException {
		if ( null == gzipStream ) {
			throw new IOException("close() has already been called.");
		}

		gzipStream.flush();
	}

	@Override
	public void write(byte[] writeBytes) throws IOException {
		write(writeBytes, 0, writeBytes.length);
	}

	@Override
	public void write(byte[] writeBytes, int offset, int length) throws IOException {
		if ( null == gzipStream ) {
			throw new IOException("close() has already been called.");
		}

		gzipStream.write(writeBytes, offset, length);
	}

	@Override
	public void write(int i) throws IOException {
		if ( null == gzipStream ) {
			throw new IOException("close() has already been called.");
		}

		gzipStream.write(i);
	}
}
