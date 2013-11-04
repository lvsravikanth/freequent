package com.scalar.freequent.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 4, 2013
 * Time: 7:18:17 PM
 */
public class EditorUtils {
	protected static final Log logger = LogFactory.getLog(EditorUtils.class);


	/**
	 * Constant used to identify the editor id attribute.
	 */
	public static final String EDITOR_ID_ATTRIBUTE = ".fui.editor.id";
	public static final String NEW_EDITOR_ID_VALUE = "fui-editor-object-new-";

	public static boolean isNewEditorId(String editorId) {
		return editorId != null && editorId.startsWith(NEW_EDITOR_ID_VALUE);
	}
}
