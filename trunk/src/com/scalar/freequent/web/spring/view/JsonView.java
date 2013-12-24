package com.scalar.freequent.web.spring.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.View;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.OrderComparator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.ServletOutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.io.Writer;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;

import com.scalar.core.response.Response;
import com.scalar.core.response.ErrorResponse;
import com.scalar.core.ScalarActionException;
import com.scalar.core.request.Request;
import com.scalar.core.request.RequestUtil;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.util.MsgObject;
import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.util.JSONUtil;
import com.scalar.freequent.util.IOUtil;
import com.scalar.freequent.l10n.FrameworkResource;
import com.scalar.freequent.auth.User;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 21, 2013
 * Time: 8:08:42 PM
 */
public class JsonView extends JstlView {
    protected static final Log logger = LogFactory.getLog(JsonView.class);

    /**
	 * Constant used to identify the root name.
	 */
	private static final String ROOT_NAME = "xapi";

	/**
	 * Constant used to identify the metadata name.
	 */
	private static final String METADATA_NAME = "metadata";

	/**
	 * Constant used to identify the container name.
	 */
	private static final String CONTAINER_NAME = "container";

	/**
	 * Constant used to identify the container type.
	 */
	private static final String CONTAINER_TYPE = "fui";

    /**
	 * Constant used to format metadata time values.
	 */
	protected static final Format metaDataTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    /**
	 * Constant used to set the content type on the response.
	 */
	protected static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";

    /**
	 * Constant used to set the content type on the response.
	 */
	protected static final String TEXT_PLAIN_CONTENT_TYPE = "text/plain;charset=UTF-8";

	/**
	 * Constant used to identify the request content in a service request.
	 */
	protected static final String CONTENT = "content";

	/**
	 * Constant used to identify the response type in the output.
	 */
	protected static final String TYPE = "type";

	@Override
    public boolean isUrlRequired() {
        return false;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        // Check for committed response
		if ( httpResponse.isCommitted() ) {
			if ( logger.isDebugEnabled() ) {
				logger.debug("Response already committed");
			}

			return;
		}

		long startMillis = System.currentTimeMillis();

        Response response = (Response)model.get (Response.RESPONSE_ATTRIBUTE);
        Writer writer = null;
		try {
			// Set the response content type based on the transport
			setContentType(response);
            writer = IOUtil.getResponseWriter(response);
            startOutput(response, writer);
		    createOutput(response, writer);
            finishOutput(response, writer);
        } catch ( ScalarActionException e ) {
			if ( logger.isErrorEnabled() ) {
				logger.error("Unable to create output", e);
			}

			throw e;
		} catch ( IOException e ) {
			if ( logger.isErrorEnabled() ) {
				logger.error("Unable to get writer", e);
			}

			throw e;
		} catch ( Exception e ) {
			// Catching just exception on purpose
			if ( logger.isErrorEnabled() ) {
				logger.error("Unexpected error during output", e);
			}

			throw e;
		} finally {
			try {
				if ( null != writer ) {
					// Ensure output
					writer.flush();
				}
			} catch ( IOException e ) {
				if ( logger.isErrorEnabled() ) {
					logger.error("Unable to flush output", e);
				}
			}

			if ( logger.isDebugEnabled() ) {
				logger.debug("End of creating UI response: " + httpRequest.getRequestURI() + " Total flight time: " + (System.currentTimeMillis() - startMillis));
			}
		}
    }

    /**
	 * Starts the output of the JSON transport.
	 */
	public void startOutput(Response response, Writer writer) throws ScalarActionException {
		try {
			// We send back a serialized object rather than an array to avoid JS hijacking
			// and prefix for JS cross-site hacking prevention
			writer.write("/*{" + getRootName() + ":{");

			// Output metatdata
			writer.write(getMetadataName());
			writer.write(":{");

			boolean comma = false;
			Map<String, String> data = getMetaData(response);
			for ( Map.Entry<String, String> entry  : data.entrySet() ) {
				addOutputAttribute(entry.getKey(), entry.getValue(), writer, comma);
				comma = true;
			}

			writer.write("},");

			// Start container
			writer.write(getContainerName());
			writer.write(":{");

			comma = false;
			data = getContainerAttributes(response);
			for ( Map.Entry<String, String> entry  : data.entrySet() ) {
				addOutputAttribute(entry.getKey(), entry.getValue(), writer, comma);
				comma = true;
			}

			if ( comma ) {
				writer.write(',');
			}

			writer.write("data:[");
		} catch ( IOException e ) {
            MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.WRITE_OUTPUT_FAILURE);
			throw ScalarActionException.create(msgObject, e);
		}
	}

    /**
	 * Finishs the output of the JSON transport.
	 */
	public void finishOutput(Response response, Writer writer) throws ScalarActionException {
		try {
			// Finish container
			writer.write("]}");

			// We send back a serialized object rather than an array to avoid JS hijacking
			// and suffix for JS cross-site hacking prevention
			writer.write("}}*/");
		} catch ( IOException e ) {
            MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.WRITE_OUTPUT_FAILURE);
			throw ScalarActionException.create(msgObject, e);
		}

		response.cleanup();
	}

	/**
	 * Starts the output process for the <code>Response</code> by wrapping the output with JSON and adding the
	 * attributes.
	 */
	protected void startResponseOutput(Response response, Writer writer) throws ScalarActionException {
        // No wrapping if there is no tag
		String tag = response.getTag();
		if ( (null == tag) || (tag.length() == 0) ) {
			return;
		}

		try {
			writer.write('{');
			addOutputAttribute(TYPE, response.getTag(), writer, false);

			Map<String, String> data = response.getOutputAttributes();
			for ( Map.Entry<String, String> entry  : data.entrySet() ) {
				String key = entry.getKey();
				String value = entry.getValue();
				if(value != null && tag.equals(ErrorResponse.FUI_ERROR) &&
						( key.equals(ErrorResponse.MESSAGE_ATTRIBUTE) || key.equals(ErrorResponse.ROOT_MESSAGE_ATTRIBUTE) )){
					value = value.replace('\n', ' ');
					value = value.replace('\r', ' ');
					value = value.replace('\t', ' ');
				}
				addOutputAttribute(key, value, writer, true);
			}


			writer.write(",\"" + CONTENT + "\": \"");
		} catch ( IOException e ) {
			MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.WRITE_OUTPUT_FAILURE);
			throw ScalarActionException.create(msgObject, e);
		}
    }

    /**
	 * Finishes the output process for the <code>Response</code> by ending the wrapping with JSON.
	 */
	protected void finishResponseOutput(Response response, Writer writer) throws ScalarActionException {
		// No wrapping if there is no tag
		String tag = response.getTag();
		if ( (null == tag) || (tag.length() == 0) ) {
			return;
		}

		try {
			writer.write("\", \"end\": \"---end-marker---\"}");
		} catch ( IOException e ) {
            MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.WRITE_OUTPUT_FAILURE);
			throw ScalarActionException.create(msgObject, e);
		}
	}

    /**
	 * Returns the root name for output.
	 *
	 * @return the root name.
	 */
	protected String getRootName() {
		return ROOT_NAME;
	}

    /**
	 * Returns the metadata name for output.
	 *
	 * @return the metadat name.
	 */
	protected String getMetadataName() {
		return METADATA_NAME;
	}

	/**
	 * Returns the container name for output.
	 *
	 * @return the container name.
	 */
	protected String getContainerName() {
		return CONTAINER_NAME;
	}

	/**
	 * Returns the container type.
	 *
	 * @return the container type.
	 */
	protected String getContainerType() {
		return CONTAINER_TYPE;
	}

	/**
	 * Returns the container id.
	 *
	 * @param response the <code>Response</code>
	 * @return the container id
	 */
	protected String getContainerId(Response response) {
		return response.getRequest().getId();
	}

    /**
	 * Returns the metadata for the output.
	 *
	 * @param response the current <code>Response</code>
	 * @return the metadata for the output
	 */
	protected Map<String, String> getMetaData(Response response) {
		Map<String, String> metadata = new HashMap<String, String>();
        Request request = response.getRequest();

        User authUser = request.getActiveUser();
        String user = "?";
        if (authUser != null) {
            user = authUser.getUserId();
        }

		metadata.put("user", encodeValue(user));
		metadata.put("request", encodeValue(RequestUtil.getRequestURL(request).toString()));
		metadata.put("status", encodeValue("ok"));


		if ( null != request ) {
			metadata.put("startTime", metaDataTimeFormat.format(request.getCreateTime()));
			metadata.put("endTime", metaDataTimeFormat.format(System.currentTimeMillis()));
		}

		return metadata;
	}

    /**
	 * Returns a JSON encoded value. If the value is <code>null</code>, an empty <code>String</code> will be
	 * returned.
	 */
	public String encodeValue(String value) {
		return (null == value) ? "" : JSONUtil.encodeString(value);
	}

    /**
	 * Adds an attribute name and value to the output in JSON format.
	 *
	 * @param attribute the attribute name
	 * @param value the attribute value
	 * @param writer the <code>Writer</code> for output
	 * @param comma the comma flag, if <code>true</code>, will prefix a comma
	 * @throws ScalarActionException if there is a problem adding the attribute to the output
	 */
	protected void addOutputAttribute(String attribute, String value, Writer writer, boolean comma) throws ScalarActionException {
		if ( null == value ) {
			return;
		}

		try {
			if ( comma ) {
				writer.write(',');
			}
			writer.write("\"");
			writer.write(attribute);
			writer.write("\": \"");
			writer.write(encodeValue(value));
			writer.write("\"");
		} catch ( IOException e ) {
            MsgObject msgObject = MsgObjectUtil.getMsgObject(FrameworkResource.BASE_NAME, FrameworkResource.WRITE_OUTPUT_FAILURE);
			throw ScalarActionException.create(msgObject, e);
		}
	}

    /**
	 * Returns the container attributes.
	 *
	 * @param response the response
	 * @return the container attributes for the response
	 */
	protected Map<String, String> getContainerAttributes(Response response) {
		Map<String, String> attributes = new HashMap<String, String>();

		attributes.put("type", encodeValue(getContainerType()));
		attributes.put("id", encodeValue(getContainerId(response)));

		return attributes;
	}

    /**
	 * Sets the <code>Service</code> response content type to {@link #JSON_CONTENT_TYPE}.
	 */
	public void setContentType(Response response) {
		// If this is a multipart request (aka file upload) we need to set content type to text otherwise
		// user will see a download dialog
		/*if ( null != response.getRequest().getFileNames() ) {
            ((HttpServletResponse)response.getWrappedObject()).setContentType(TEXT_PLAIN_CONTENT_TYPE);
		} else*/ {
			// Set our preferred content type
            ((HttpServletResponse)response.getWrappedObject()).setContentType(JSON_CONTENT_TYPE);
		}
	}

    /**
	 * Creates the output for the <code>Response</code> using the <code>Writer</code>. The subclasses are given an
	 * opportunity to provide context around the <code>Response</code> output.
	 */
	public void createOutput(Response response, Writer writer) throws ScalarActionException {
		// Create our output
		startResponseOutput(response, writer);

		response.createOutput(writer);

		finishResponseOutput(response, writer);

		/*// Restore the previous values
		if ( null != previousRequest ) {
			requestService.setAttribute(Request.REQUEST_ATTRIBUTE, previousRequest);
		}

		if ( null != previousContext ) {
			requestService.setAttribute(Context.CONTEXT_ATTRIBUTE, previousContext);
		}*/
	}
}
