
package com.scalar.freequent.util;

import java.text.Bidi;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.web.util.HtmlUtils;
import com.scalar.core.ScalarException;

/**
 * The <code>StringUtil</code> class provides utility functions for working with strings.
 *
 * @author .sujan.
 * @version $Revision: #1 $ $Date: 2011/11/08 $
 */
public final class StringUtil {

	/** The ellipsis string, used when truncating long strings. */
	public static final String ELLIPSIS = "...";

	/** The escaped new line regex pattern. */
	public static final Pattern ESCAPED_NEW_LINE_PATTERN = Pattern.compile("\\\\[r|n]");

	/** The apostrophe entity, which is replaced.*/
	public static final String APOSTROPHE = "&#39;";

	/** The apostrophe character, which is to be replaced.*/
	public static final String APOSTROPHE_CHAR = "'";

	/** The regex pattern for escaping an apostrophe. */
	public static final Pattern APOSTROPHE_ESCAPE_PATTERN = Pattern.compile(APOSTROPHE_CHAR);

	/** The regex pattern for unescaping an apostrophe. */
	public static final Pattern APOSTROPHE_UNESCAPE_PATTERN = Pattern.compile(APOSTROPHE);

	/**
	 * The regex pattern for Unicode escape characters (ex. \\uHHHH where HHHH
	 * is a hexadecimal index of the character in the Unicode character set.
	 */
	public static final Pattern UNICODE_ESCAPE_PATTERN = Pattern.compile("\\\\u(\\p{XDigit}\\p{XDigit}\\p{XDigit}\\p{XDigit})");

	/**
	 * The replacement string representing an HTML hexadecimal numerical
	 * entity (ex. &#xHHHH;).
	 */
	public static final String HTML_HEX_NUM_ENTITY_REPLACEMENT = "&#x$1;";

	/**
	 * The regex pattern for HTML hexadecimal numerical entities (ex. &#xHHHH;
	 * where HHHH is a hexadecimal index of the character in the Unicode
	 * character set.
	 */
	public static final Pattern HTML_HEX_NUM_ENTITY_PATTERN = Pattern.compile("&#x(\\p{XDigit}\\p{XDigit}\\p{XDigit}\\p{XDigit});");

	/**
	 * The replacement string representing a Unicode escape character
	 * (ex. \\uHHHH).
	 */
	public static final String UNICODE_ESCAPE_REPLACEMENT = "\\\\u$1";

	/** Default maximum number of label characters. */
	public static final int DEFAULT_LABEL_MAX = 20;

	/**
	 * Constructs a new <code>StringUtil</code>. This is the default constructor and is unavailable to others.
	 */
	private StringUtil() {
	}

	/**
	 * Converts a string into a string array with the given delimiters.
	 *
	 * @param values the concatenated values
	 * @param delimiters the delimiters
	 * @return a string array
	 */
	public static String[] toArray(String values, String delimiters) {
		StringTokenizer tokenizer = new StringTokenizer(values, delimiters);

		int index = 0;
		int count = tokenizer.countTokens();

		String[] valuesArray = new String[count];
		while ( tokenizer.hasMoreTokens() ) {
			valuesArray[index++] = tokenizer.nextToken().trim();
		}

		return valuesArray;
	}

	/**
	 * Converts a string into a string list with the given delimiters.
	 *
	 * @param values the concatenated values
	 * @param delimiters the delimiters
	 * @return a <code>List</code>
	 */
	public static List<String> toList(String values, String delimiters) {
		StringTokenizer tokenizer = new StringTokenizer(values, delimiters);

		List<String> list = new ArrayList<String>();
		while ( tokenizer.hasMoreTokens() ) {
			list.add(tokenizer.nextToken().trim());
		}

		return list;
	}

	/**
	 * Converts a string array into a string with the given delimiter.
	 *
	 * @param values the string array
	 * @param delimiter the delimiter
	 * @return a concatenated string of the values
	 */
	public static String toString(String[] values, char delimiter) {
		StringBuilder value = new StringBuilder();
		for ( int i = 0; i < values.length; ++i ) {
			if ( i > 0 ) {
				value.append(delimiter);
			}
			value.append(values[i]);
		}

		return value.toString();
	}

	/**
	 * Truncates 'str' after 'len' characters and append an ellipsis (...)
	 * as necessary.  This assumes 'str' is not already HTML-escaped.
	 *
	 * @param str The string to summarize.
	 * @param len The maximum length before truncating (and adding an ellipsis).
	 * @return The possibly truncated string.
	 */
	public static String summary(String str, int len) {
		return summary(str, len, false);
	}

	/**
	 * Truncates 'str' after 'len' characters and append an ellipsis (...)
	 * as necessary.
	 *
	 * @param str The string to summarize.
	 * @param len The maximum length before truncating (and adding an ellipsis).
	 * @param html Set to <code>true</code> to indicate the string is already
	 *    HTML-escaped.
	 * @return The possibly truncated string.
	 */
	public static String summary(String str, int len, boolean html) {
		if ( (null == str) || (str.length() <= len) ) {
			return str;
		} else {
			if (html) { str = htmlUnescape(str); }

			String subStr = str.substring(0, Math.min(len, str.length()));
			if ( new Bidi(subStr, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).isLeftToRight() ) {
				subStr += ELLIPSIS; // for LTR or mixed strings
			} else {
				subStr = ELLIPSIS + subStr; // for RTL strings or mixed strings
			}

			return (html ? htmlEscape(subStr) : subStr);
		}
	}

	/**
	 * Truncates 'str' after a default number of characters.  This assumes
	 * 'str' is not already HTML-escaped.
	 *
	 * @param str The string to truncate.
	 * @return The possibly truncated string.
	 */
	public static String getLabel(String str) {
		return getLabel(str, DEFAULT_LABEL_MAX, false);
	}

	/**
	 * Truncates 'str' after 20 characters.
	 *
	 * @param str The string to truncate.
	 * @param html Set to <code>true</code> to indicate the string is already
	 *    HTML-escaped.
	 * @return The possibly truncated string.
	 */
	public static String getLabel(String str, boolean html) {
		return getLabel(str, DEFAULT_LABEL_MAX, html);
	}

	/**
	 * Truncates 'str' after 'max' characters.
	 *
	 * @param str The string to truncate.
	 * @param max The maximum length before truncating.
	 * @param html Set to <code>true</code> to indicate the string is already
	 *    HTML-escaped.
	 * @return The possibly truncated string.
	 */
	public static String getLabel(String str, int max, boolean html) {
		if ( (null == str) || (str.length() <= max) ) {
			return str;
		} else {
			if (html) {
				str = htmlUnescape(str);
			}
			String subStr = str;
			if(max < str.length()){
				subStr = str.substring(0, max);
			}

			return (html ? htmlEscape(subStr) : subStr);
		}
	}


	/**
	 * Returns the appropriate tooltip string.  This will return the tooltip if
	 * specified, otherwise it will return the label only if it would normally
	 * be truncated.
	 *
	 * @param label The alternate string.
	 * @param tooltip An optional tooltip.
	 * @return The possibly truncated string.
	 */
	public static String getTooltip(String tooltip, String label) {
		return getTooltip(tooltip, label, DEFAULT_LABEL_MAX);
	}

	/**
	 * Returns the appropriate tooltip string.  This will return the tooltip if
	 * specified, otherwise it will return the label only if it would normally
	 * be truncated.
	 *
	 * @param label The alternate string.
	 * @param tooltip An optional tooltip.
	 * @param max The maximum label length being used.
	 * @return The possibly truncated string.
	 */
	public static String getTooltip(String tooltip, String label, int max) {
		if (!isEmpty(tooltip)) {
			return tooltip;
		} else {
			if (!isEmpty(label) && (label.length() > max)) {
				return label;
			} else {
				return "";
			}
		}
	}

	/**
	 * Determines if the specified name of a file has a file extension that is recognized by the system as an image
	 * file, and can therefore be previewed by this assumption.
	 *
	 * @param str The file name.
	 * @return <tt>true</tt>if the file name has a recognized "image" file extension, or <tt>false</tt> otherwise
	 */
	public static boolean hasImageExtension(String str) {
		String lcName = (null != str ? str.toLowerCase() : "");
		// in order from most common to rare
		return lcName.endsWith(".jpg") || lcName.endsWith(".jpe") || lcName.endsWith(".jpeg") || lcName.endsWith(".png") || lcName.endsWith(".gif") || lcName.endsWith(".bmp")
		|| lcName.endsWith(".jif") || lcName.endsWith(".jfif");
	}

	/**
	 * Determines if the given <code>String</code> is <code>null</code> or
	 * empty.  Will also work for any object, checking if its
	 * <code>String</code> representation is <code>null</code> or an empty
	 * string.
	 *
	 * @param obj Any object.  Most likely a <code>String</code> or
	 *    <code>StringBuilder</code>, but could be any object.
	 * @return <code>true</code> if the object's String representation is
	 *    <code>null</code> or an empty string; <code>false</code> otherwise.
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof String) {
			return (((String)obj).length() == 0);
		} else if(obj instanceof byte[]) {
			return ( ((byte[])obj).length == 0);
		} else {
			return (obj.toString().length() == 0);
		}
	}

	/**
	 * Replaces special characters in a given <code>String</code> with
	 * HTML entity codes. If the string is <code>null</code>
	 * or empty returns an empty <code>String</code>.
	 *
	 * @param value <code>String</code>.
	 *
	 * @return <code>value</code>. If the string is <code>null</code>
	 * or empty returns an empty <code>String</code>.
	 */
	public static String htmlEscape(String value) {
		if (value == null) {
			return "";
		}
		return APOSTROPHE_ESCAPE_PATTERN.matcher(HtmlUtils.htmlEscape(value)).replaceAll(APOSTROPHE);
	}

	/**
	 * Replaces HTML entity codes in a given <code>String</code> with
	 * special characters. If the string is <code>null</code>
	 * or empty returns an empty <code>String</code>.
	 *
	 * @param value <code>String</code>.
	 *
	 * @return <code>value</code>. If the string is <code>null</code>
	 * or empty returns an empty <code>String</code>.
	 */
	public static String htmlUnescape(String value) {
		if (value == null) {
			return "";
		}
		return APOSTROPHE_UNESCAPE_PATTERN.matcher(HtmlUtils.htmlUnescape(value)).replaceAll(APOSTROPHE_CHAR);
	}

	/**
	 * Replaces Unicode escape characters in a given <code>String</code> with
	 * their HTML hexadecimal numerical entity representations.
	 *
	 * @param value The <code>String</code> to process.
	 * @return The processed <code>String</code>.
	 * @see #unicodeUnescape
	 */
	public static String unicodeEscape(String value) {
		if (isEmpty(value)) {
			return "";
		}
		return UNICODE_ESCAPE_PATTERN.matcher(value).replaceAll(HTML_HEX_NUM_ENTITY_REPLACEMENT);
	}

	/**
	 * Replaces HTML hexadecimal numerical entities in a given
	 * <code>String</code> with their Unicode escape character representations.
	 *
	 * @param value The <code>String</code> to process.
	 * @return The processed <code>String</code>.
	 * @see #unicodeEscape
	 */
	public static String unicodeUnescape(String value) {
		if (isEmpty(value)) {
			return "";
		}
		return HTML_HEX_NUM_ENTITY_PATTERN.matcher(value).replaceAll(UNICODE_ESCAPE_REPLACEMENT);
	}


	/**
	 * HTML Unescapes the given string and then escapes special HTML characters only.
	 *
	 * @param value The string to process.
	 * @return The processed string.
	 */
	public static String flashEscape(String value) {
		return basicHtmlEscape(htmlUnescape(value));
	}

	/**
	 * Escapes special HTML characters only.  This only handles the
	 * common special characters of >, <, &, ', and ".  This is not
	 * as robust as the support provided by {@link #htmlEscape(String)}.
	 *
	 * @param value The string to process.
	 * @return The processed string.
	 */
	public static String basicHtmlEscape(String value) {
		if (value == null) {
			return "";
		}

		StringBuilder buffer = new StringBuilder();

		StringTokenizer tokenizer = new StringTokenizer(value, "&<>\"'", true);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token.length() == 1) {
				switch (token.charAt(0)) {
					case '&':
						buffer.append("&amp;");
						break;

					case '<':
						buffer.append("&lt;");
						break;

					case '>':
						buffer.append("&gt;");
						break;

					case '"':
						buffer.append("&quot;");
						break;

					case '\'':
						buffer.append("&apos;");
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
	 * Escapes special HTML characters twice, to get around using
	 * JSP text in JS markup, like in an onclick.  This only handles the
	 * common special characters of >, <, &, ', and ".  This is not
	 * as robust as the support provided by {@link #htmlEscape(String)}.
	 *
	 * @param value The string to process.
	 * @return The processed string.
	 */
	public static String doubleHtmlEscape(String value) {
		if (value == null) {
			return "";
		}

		StringBuilder buffer = new StringBuilder();

		StringTokenizer tokenizer = new StringTokenizer(value, "&<>\"'", true);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token.length() == 1) {
				switch (token.charAt(0)) {
					case '&':
						buffer.append("&amp;");
						break;

					case '<':
						buffer.append("\\&lt;");
						break;

					case '>':
						buffer.append("\\&gt;");
						break;

					case '"':
						buffer.append("\\&quot;");
						break;

					case '\'':
						buffer.append("\\&#39;");
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
	 * Escapes strings to be used as a Javascript string, changing
	 * ' to \', " to \", and \ to \\.  A common usage would be in a JSP page,
	 * where Java sets a String variable to be used in Javascript.
	 *
	 * @param value The string to process.
	 * @return The processed string.
	 */
	public static String jsEscape(String value) {
		if (value == null) {
			return "";
		}

		StringBuilder buffer = new StringBuilder();

		StringTokenizer tokenizer = new StringTokenizer(value, "\\\"'\r\n", true);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token.length() == 1) {
				switch (token.charAt(0)) {
					case '\\':
						buffer.append("\\\\");
						break;

					case '"':
						buffer.append("\\\"");
						break;

					case '\'':
						buffer.append("\\'");
						break;

					case '\r':
						buffer.append("\\r");
						break;

					case '\n':
						buffer.append("\\n");
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
	 * Replacements for line breaks in return String.
	 *
	 * @param value The string to process.
	 * @param replaceStr replacement String for line breaks
	 * @return The processed string.
	 */
	public static String replaceEscapedBreaks(String value, String replaceStr) {
		return (null==value) ? "" : ESCAPED_NEW_LINE_PATTERN.matcher(value).replaceAll(replaceStr);
	}

	/**
	 * Returns a non-null <code>String</code> for the provided object.
	 *
	 * @param object the object
	 * @return a non-null <code>String</code>
	 */
	public static String nonNull(Object object) {
		return (null == object) ? "" : object.toString();
	}

	/**
	 * Determines if any strings in the <code>Set</code> are also found
	 * in the comma-separated-values <code>String</code>.
	 *
	 * @param items A <code>Set</code> of <code>Strings</code>s.
	 * @param csvString A <code>String</code> of comma-separated-values.
	 * @return <code>true</code> if any strings in the <code>Set</code>
	 *    are also found in the <code>String</code> of comma-separated-values.
	 */
	public static boolean inCSVString(Set<String> items, String csvString) {
		if ((items != null) && (csvString != null) &&
				(items.size() > 0) && (csvString.length() > 0)) {
			String[] tokens = csvString.split(",");
			for (String token : tokens) {
				if (items.contains(token.trim())) {
					return true;
				}
			}
		}
		return false;
	}

    public static String concatBytes(byte[] byteArr, String delim) {
        if ((byteArr == null) || (delim == null)) {
            return null;
        }

        if (byteArr.length == 0) {
            return "";
        }

        if (delim == null) {
            delim = ";";
        }

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < byteArr.length; i++) {
            buf.append(byteArr[i] + delim);
        }

        return buf.toString();
    }

    public static String encrypt (String string) throws ScalarException {
         return StringUtil.concatBytes( MessageDigestUtils.getMAC (string), "," );
    }
}
