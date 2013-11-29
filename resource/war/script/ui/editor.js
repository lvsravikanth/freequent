/**
 * editor helper class for the ui.
 */
fui.provide("fui.ui.editor");

fui.require("fui.editor");

fui.ui.editor = {

	TYPE_UNKNOWN: "UNKNOWN",

	DEFAULT_ID_ATTRIBUTE: "id",

	edit: function(editConfig, requestData) {
		var id = editConfig.id;
        editConfig.type = editConfig.type || this.TYPE_UNKNOWN;
        editConfig.idAttribute = editConfig.idAttribute || this.DEFAULT_ID_ATTRIBUTE;
        var saveHandlers = editConfig.saveHandlers || [];
        if (fui.query.isFunction(saveHandlers)) {
            saveHandlers = [ saveHandlers ];
        }
		var isClone=editConfig.clone;
		requestData = requestData || {};
		requestData.content = requestData.content || {};
		var cloneId=requestData.content[fui.editor.CLONE_ATTRIBUTE];
		if(cloneId){
			isClone=true;
			id=cloneId;
		}
		var rootEditConfig = {
			idAttribute: editConfig.idAttribute,
			type: editConfig.type,
			id: id,
			fullscreen: editConfig.fullscreen,
			checkers: editConfig.checkers,
			collectors: editConfig.collectors,
			saveHandlers: saveHandlers,
			closeHandlers: editConfig.closeHandlers,
			saveCallback: editConfig.saveCallback || fui.ui.workspace.editorSaveCallback,
			closeCallback: editConfig.closeCallback,
			ignoreFoldersWsPath: editConfig.ignoreFoldersWsPath,
			initialData: editConfig.initialData,
			helpHandler: editConfig.helpHandler,
			errorHandler: editConfig.errorHandler,
			readOnly: editConfig.readOnly,
			clone: isClone,
			skipToolbar: editConfig.skipToolbar,
			skipType: editConfig.skipType,
			//extraSetup: customizeIcon,
			ACTION_KEY: editConfig.ACTION_KEY,
			validate: editConfig.validate || this.validate
		};

		fui.editor.edit(rootEditConfig, requestData);
    },

	/**
	 * validates the user edit form.
	 *
	 * @param editor
	 * @param data
	 * @param requestData
	 */
	validate: function(editor, data, requestData) {
		var isValid = fui.query('#'+editor.formId).valid();
		return isValid;
	}
};