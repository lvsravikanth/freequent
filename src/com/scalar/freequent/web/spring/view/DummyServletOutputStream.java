package com.scalar.freequent.web.spring.view;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

/**
 * User: ssuppala
 * Date: Sep 27, 2013
 * Time: 2:11:10 PM
 */
public class DummyServletOutputStream extends ServletOutputStream {
	ByteArrayOutputStream writer = new ByteArrayOutputStream();

	public void write(int i) throws IOException {
		writer.write(i);
	}

	public String getBuffer(String characterEncoding) {
		return new String (writer.toByteArray(), Charset.forName(characterEncoding));
	}

	public String getBuffer() {
		return getBuffer("UTF-8");
	}

	public void close() {
		try { writer.close(); } catch (IOException ignore) {}
	}
}
