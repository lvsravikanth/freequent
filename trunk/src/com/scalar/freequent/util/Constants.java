package com.scalar.freequent.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Sujan Kumar Suppala
 * Date: Oct 24, 2013
 * Time: 2:33:43 PM
 */
public final class Constants {
	protected static final Log logger = LogFactory.getLog(Constants.class);

	/**
	 * Constant used to identify the editor id attribute.
	 */
	public static final String EDITOR_ID_ATTRIBUTE = ".fui.editor.id";

	public static final String NEW_EDITOR_ID_VALUE = "fui-editor-object-new-";

	public static final String FORM = "form";

	/**
	 * consttant used to identify the item name.
	 */
	public static final String ITEM_NAME_ATTRIBUTE = "name";
	public static final String CAPABILITIES_ATTRIBUTE = "capabilities";

	private Constants() {}
}
