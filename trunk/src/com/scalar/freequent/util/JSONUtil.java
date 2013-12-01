package com.scalar.freequent.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import java.util.*;
import java.io.Writer;
import java.io.FilterWriter;
import java.io.IOException;

import com.scalar.freequent.common.HasRecord;

/**
 * The <code>JSONUtil</code> class provides utility functions for working with JSON objects.
 *
 * User: Sujan Kumar Suppala
 * Date: Sep 22, 2013
 * Time: 11:58:40 AM
 */
public final class JSONUtil {

    protected static final Log logger = LogFactory.getLog(JSONUtil.class);

	/**
	 * Constant used to prefix script output for JS cross-site hacking prevention.
	 */
	public static final String SCRIPT_SECURITY_PREFIX = "/*";

	/**
	 * Constant used to suffic script output for JS cross-site hacking prevention.
	 */
	public static final String SCRIPT_SECURITY_SUFFIX = "*/";

	/**
	 * Constant used to identify a ".
	 */
	private static final char QUOTE_TOKEN = '"';

	/**
	 * Constant used to identify a '.
	 */
	private static final char APOS_TOKEN = '\'';

	/**
	 * Constant used to identify a \n.
	 */
	private static final char NEWLINE_TOKEN = '\n';

	/**
	 * Constant used to identify a \r.
	 */
	private static final char RETURN_TOKEN = '\r';

	/**
	 * Constant used to identify a " entity.
	 */
	private static final String QUOTE_ENTITY = "\\\"";

	/**
	 * Constant used to identify a ' entity.
	 */
	private static final String APOS_ENTITY = "\\'";

	/**
	 * Constant used to identify a \n entity.
	 */
	private static final String NEWLINE_ENTITY = "\\n";

	/**
	 * Constant used to identify a \r entity.
	 */
	private static final String RETURN_ENTITY = "\\r";

	/**
	 * Constructs a new <code>JSONUtil</code>. This is the default constructor and is unavailable to others.
	 */
	private JSONUtil() {
	}

	/**
	 * Builds a <code>JSONObject</code> from a <code>Map</code> of objects.
	 *
	 * @param map the <code>Map</code> of objects
	 * @param locale the <code>Locale</code>; can be <code>null</code>
	 * @param timeZone the <code>TimeZone</code>; can be <code>null</code>
	 * @return a <code>JSONObject</code>
	 * @throws JSONException if there is a problem building the <code>JSONObject</code>
	 */
	public static JSONObject buildJSONObjectFromMap(Map<?, ?> map, Locale locale, TimeZone timeZone) throws JSONException {
		JSONObject json = new JSONObject();

		if(map == null){
			return json;
		}

		for ( Map.Entry<?, ?> entry : map.entrySet() ) {
			String key = (String)entry.getKey();
			Object value = entry.getValue();

			if ( Map.class.isInstance(value) ) {
				json.put(key, buildJSONObjectFromMap((Map<?, ?>)value, locale, timeZone));
			} else if ( Collection.class.isInstance(value) ) {
				json.put(key, buildJSONArrayFromCollection((Collection<?>)value, locale, timeZone));
			} else if (NormalizedDate.class.isInstance(value)) {
				json.put(key, value.toString());
			} else if (HasRecord.class.isInstance(value)) {
				json.put(key, buildJSONObjectFromMap(((HasRecord)value).toMap(), locale, timeZone));
			} else {
				if ( (null != locale) && Date.class.isInstance(value) ) {
					value = DateTimeUtil.getLocalizedValue(value, locale, timeZone);
				}
				json.put(key, value);
			}
		}

		return json;
	}

	/**
	 * Builds a <code>JSONArray</code> from a <code>Collection</code>.
	 *
	 * @param collection the <code>Collection</code>
	 * @param locale the <code>Locale</code>; can be <code>null</code>
	 * @param timeZone the <code>TimeZone</code>; can be <code>null</code>
	 * @return a <code>JSONArray</code>
	 * @throws JSONException if there is a problem building the <code>JSONArray</code>
	 */
	public static JSONArray buildJSONArrayFromCollection(Collection<?> collection, Locale locale, TimeZone timeZone) throws JSONException {
		JSONArray json = new JSONArray();

		for ( Object value : collection ) {
			if ( Map.class.isInstance(value) ) {
				json.put(buildJSONObjectFromMap((Map<?, ?>)value, locale, timeZone));
			} else if ( Collection.class.isInstance(value) ) {
				json.put(buildJSONArrayFromCollection((Collection<?>)value, locale, timeZone));
			} else if (NormalizedDate.class.isInstance(value)) {
				json.put(value.toString());
			} else if (HasRecord.class.isInstance(value)) {
				json.put(buildJSONObjectFromMap(((HasRecord)value).toMap(), locale, timeZone));
			} else {
				if ( (null != locale) && Date.class.isInstance(value) ) {
					value = DateTimeUtil.getLocalizedValue(value, locale, timeZone);
				}
				json.put(value);
			}
		}

		return json;
	}

	/**
	 * Crafts a JSON String from a {@link Map}.  If the {@link Map} is ordered
	 * (ex. {@link java.util.LinkedHashMap}), this will preserve the ordering.
	 * This assumes basic types for the Map's <code>Object</code>, recognizing
	 * {@link Integer}, {@link Number}, and {@link Date}; all other object
	 * types will be serialized via its <code>toString()</code> method.
	 *
	 * @param map The <code>Map</code> to serialize.
 	 * @param locale The <code>Locale</code>; can be <code>null</code>.
	 * @param timeZone The <code>TimeZone</code>; can be <code>null</code>.
	 * @return A serialized JSON String.  If the map is empty or
	 *    <code>null</code>, it will return "{}";
	 */
	public static String serializeMapToJSON(Map<String, Object> map,
											Locale locale,
											TimeZone timeZone) {
		if (map == null) {
			return "{}";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("{");

		boolean first = true;
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (!first) {
				sb.append(",");
			} else {
				first = false;
			}
			sb.append("\"").append(encodeString(entry.getKey())).append("\"");
			sb.append(":");

			Object value = entry.getValue();
			if (Number.class.isInstance(value)) {
				sb.append(value);
			} else if (NormalizedDate.class.isInstance(value)) {
				sb.append(value.toString());
			} else {
				if ((locale != null) && (Date.class.isInstance(value))) {
					value = DateTimeUtil.getLocalizedValue(value, locale, timeZone);
				}
				sb.append("\"").append(encodeString(value.toString())).append("\"");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	/**
	 * Returns a <code>Properties</code> version of the JSON object. This is shallow and only does <code>String</code>
	 * objects.
	 *
	 * @param json the <code>JSONObject</code> to turn into <code>Properties</code>
	 * @return a <code>Properties</code> object
	 */
	public static Properties getProperties(JSONObject json) {
		Properties properties = new Properties();

		for ( Iterator<?> it = json.keys(); it.hasNext(); ) {
			String key = (String)it.next();

			// Skip nulls
			if ( json.isNull(key) ) {
				continue;
			}

			properties.setProperty(key, json.opt(key).toString());
		}

		return properties;
	}

	/**
	 * Returns a <code>Map</code> version of the JSON object. This is shallow and only does <code>String</code>
	 * objects.
	 *
	 * @param json the <code>JSONObject</code> to turn into <code>Map</code>
	 * @return a <code>Map</code> object
	 * @throws JSONException if there is a failure eveluating json content
	 */
	public static Map<String, Object> getMap(JSONObject json) throws JSONException {
		Map<String, Object> ret = new HashMap<String, Object>();
		for ( Iterator<?> it = json.keys(); it.hasNext(); ) {
			String key = (String)it.next();
			Object obj = json.opt(key);
			if(obj == null){
				continue;
			}
			if(JSONArray.class.isInstance(obj)){
				ret.put(key, getList((JSONArray)obj));
			} else if(JSONObject.class.isInstance(obj)){
				ret.put(key, getMap((JSONObject)obj));
			} else {
				ret.put(key, obj);
			}
		}
		return ret;
	}

	/**
	 * Returns a <code>List</code> version of the JSON array. This is shallow and only does <code>String</code>
	 * objects.
	 *
	 * @param jsonArray the <code>JSONArray</code> to turn into <code>List</code>
	 * @return a <code>List</code> object
	 * @throws JSONException if there is a failure eveluating json content
	 */
	public static List<Object> getList(JSONArray jsonArray) throws JSONException{
		List<Object> list = new ArrayList<Object>();
		if(jsonArray != null){
			for(int i = 0; i<jsonArray.length(); i++){
				Object obj = jsonArray.get(i);
				if(obj == null){
					continue;
				}
				if(JSONArray.class.isInstance(obj)){
					list.add(getList((JSONArray)obj));
				} else if(JSONObject.class.isInstance(obj)){
					list.add(getMap((JSONObject)obj));
				} else {
					list.add(obj);
				}
			}
		}
		return list;
	}

	/**
	 * Encodes JSON entities in a string.
	 *
	 * @param value the string to encode
	 * @return the encoded string
	 */
	public static String encodeString(String value) {
		StringBuilder buffer = new StringBuilder();

		StringTokenizer tokenizer = new StringTokenizer(value, "\"'\n\r", true);
		while ( tokenizer.hasMoreTokens() ) {
			String token = tokenizer.nextToken();
			if ( token.length() == 1 ) {
				switch ( token.charAt(0) ) {
					case QUOTE_TOKEN:
						buffer.append(QUOTE_ENTITY);
						break;

					case APOS_TOKEN:
						buffer.append(APOS_ENTITY);
						break;

					case NEWLINE_TOKEN:
						buffer.append(NEWLINE_ENTITY);
						break;

					case RETURN_TOKEN:
						buffer.append(RETURN_ENTITY);
						break;

					default:
						buffer.append(token);
					break;
				}
			} else {
				buffer.append(token);
			}
		}

		return buffer.toString();
	}

	/**
	 * Returns a JSON <code>Writer</code> that encodes output.
	 *
	 * @param writer the <code>Writer</code> to wrap with the JSON encoding
	 * @return a JSON <code>Writer</code>
	 */
	public static Writer getJSONWriter(Writer writer) {
		return new JSONFilterWriter(writer);
	}

	/**
	 * The <code>JSONFilterWriter</code> extends <code>FilterWriter</code> to implement JSON data encoding for the
	 * output.
	 */
	private static class JSONFilterWriter extends FilterWriter {
		/**
		 * Constructs a new <code>JSONFilterWriter</code> that wraps the <code>Writer</code>.
		 *
		 * @param writer the <code>Writer</code> to wrap
		 */
		public JSONFilterWriter(Writer writer) {
			super(writer);
		}

		/**
		 * Writes a single character and encodes it as needed.
		 *
		 * @param c the character to write
		 * @throws java.io.IOException if there is an I/O error
		 */
		@Override
		public void write(int c) throws IOException {
			switch ( c ) {
				case QUOTE_TOKEN:
					out.write(QUOTE_ENTITY);
					break;

				case NEWLINE_TOKEN:
					out.write(NEWLINE_ENTITY);
					break;

				case RETURN_TOKEN:
					out.write(RETURN_ENTITY);
					break;

				default:
					out.write(c);
				break;
			}
		}

		/**
		 * Writes a portion of an array of characters and encodes them as needed.
		 *
		 * @param cbuf the buffer of characters to be written
		 * @param off the offset from which to start reading characters
		 * @param len the number of characters to be written
		 * @throws IOException if there is an I/O error
		 */
		@Override
		public void write(char cbuf[], int off, int len) throws IOException {
			// Nothing to do?
			if ( len < 1 ) {
				return;
			}

			char[] temp = new char[len];

			String entity = null;
			int tempIndex = 0;

			for ( int i = 0; i < len; ++i ) {
				switch ( cbuf[off + i] ) {
					case QUOTE_TOKEN:
						entity = QUOTE_ENTITY;
						break;

					case NEWLINE_TOKEN:
						entity = NEWLINE_ENTITY;
						break;

					case RETURN_TOKEN:
						entity = RETURN_ENTITY;
						break;

					default:
						temp[tempIndex++] = cbuf[off + i];
					break;
				}

				if ( null != entity ) {
					out.write(temp, 0, tempIndex);
					out.write(entity);

					tempIndex = 0;
					entity = null;
				}
			}

			// write out anything remaining
			if ( tempIndex > 0 ) {
				out.write(temp, 0, tempIndex);
			}
		}

		/**
		 * Writes a portion of a string and encodes it as needed.
		 *
		 * @param str the string to be written
		 * @param off the offset from which to start reading characters
		 * @param len the number of characters to be written
		 * @throws IOException if there is an I/O error
		 */
		@Override
		public void write(String str, int off, int len) throws IOException {
			char[] chars = new char[len];
			str.getChars(off, off + len, chars, 0);

			write(chars, 0, len);
		}
	}
}

