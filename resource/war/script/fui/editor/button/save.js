/**
 * Set up the save section and our requirements
 */
fui.provide("fui.editor.button.save");

fui.editor.button.save = {
	SAVE_BUTTON_ID : 'fui-editor-toolbar-save',
	SAVE_BUTTON_CLASS : 'fui-editor-toolbar-button-save',
	//SAVE_ICON_CLASS : 'fui-editor-toolbar-save',
	SAVE_ICON_CLASS : '',
	READONLY_ICON_CLASS : 'fui-editor-toolbar-readonly',
	SAVE_DEFAULT_INDEX : 1,

	CLOSE_BUTTON_ID : 'fui-editor-toolbar-close',
	CLOSE_BUTTON_CLASS : 'fui-btn-action',
	//CLOSE_ICON_CLASS : 'fui-editor-toolbar-close',
	CLOSE_ICON_CLASS : '',
	CLOSE_DEFAULT_INDEX : 1,

	OK_BUTTON_ID : 'fui-editor-toolbar-ok',
	OK_BUTTON_CLASS : 'fui-btn-action fui-editor-toolbar-button-ok',
	OK_ICON_CLASS : '',
	OK_DEFAULT_INDEX : 1,

	CANCEL_BUTTON_ID : 'fui-editor-toolbar-cancel',
	CANCEL_BUTTON_CLASS : 'fui-editor-toolbar-button-cancel',
	CANCEL_ICON_CLASS : '',
	CANCEL_DEFAULT_INDEX : 1,

	/**
	 * Return the save button.
	 * @param readOnly true to get the read-only version of this button
	 */
	getSave: function(readOnly) {
		return new fui.editor.Button({
			id : this.SAVE_BUTTON_ID,
			index : this.SAVE_DEFAULT_INDEX,
			supports: this.supports,
			clickHandler:  fui.scope(this, this.saveClickHandler),
			getTitle : function(editor, obj) {
				if (!readOnly) {
					return fui.editor.getMessage('toolbar.save.title');
				} else {
					if ( obj.lockOwner ) {
						return fui.editor.getMessage('toolbar.locked.title');
					} else {
						return fui.editor.getMessage('toolbar.readonly.title');
					}
				}
			},
			getTooltip : function(editor, obj) {
				if (!readOnly) {
					return fui.editor.getMessage('toolbar.save.tooltip');
				} else {
					if ( obj.lockOwner ) {
						return fui.editor.getMessage('toolbar.locked.tooltip');
					} else {
						return fui.editor.getMessage('toolbar.readonly.tooltip');
					}
				}
			},
			getIconClass : function(editor, obj) {
				if (!readOnly) {
					return fui.editor.button.save.SAVE_ICON_CLASS;
				} else {
					return fui.editor.button.save.READONLY_ICON_CLASS;
				}
			},
			getButtonClass : function(editor, obj) {
				return fui.editor.button.save.SAVE_BUTTON_CLASS;
			},
			disabled: readOnly
		});
	},

	/**
	 * Return the ok button.
	 * @param readOnly true to get the read-only version of this button
	 */
	getOk: function(readOnly) {
		return new fui.editor.Button({
			id : this.OK_BUTTON_ID,
			index : this.OK_DEFAULT_INDEX,
			supports: this.supports,
			clickHandler:  fui.scope(this, this.saveCloseClickHandler),
			getTitle : function(editor, obj) {
				if (!readOnly) {
					return fui.editor.getMessage('toolbar.ok.title');
				} else {
					if ( obj.lockOwner ) {
						return fui.editor.getMessage('toolbar.locked.title');
					} else {
						return fui.editor.getMessage('toolbar.readonly.title');
					}
				}
			},
			getTooltip : function(editor, obj) {
				if (!readOnly) {
					return fui.editor.getMessage('toolbar.ok.title');
				} else {
					if ( obj.lockOwner ) {
						return fui.editor.getMessage('toolbar.locked.tooltip');
					} else {
						return fui.editor.getMessage('toolbar.readonly.tooltip');
					}
				}
			},
			getIconClass : function(editor, obj) {
				if (!readOnly) {
					return fui.editor.button.save.OK_ICON_CLASS;
				} else {
					return fui.editor.button.save.READONLY_ICON_CLASS;
				}
			},
			getButtonClass : function(editor, obj) {
				return fui.editor.button.save.OK_BUTTON_CLASS;
			},
			disabled: readOnly
		});
	},

	/**
	 * Return the close button.
	 * @param readOnly true to get the read-only version of this button
	 */
	getClose: function(readOnly) {
		return new fui.editor.Button({
			id : this.CLOSE_BUTTON_ID,
			index : this.CLOSE_DEFAULT_INDEX,
			supports: this.supports,
			clickHandler: fui.scope(this, !readOnly ? this.saveCloseClickHandler : this.closeClickHandler),
			getTitle : function(editor, obj) {
				if (!readOnly) {
					return fui.editor.getMessage('toolbar.saveclose.title');
				} else {
					return fui.editor.getMessage('toolbar.close.title');
				}
			},
			getTooltip : function(editor, obj) {
				if (!readOnly) {
					return fui.editor.getMessage('toolbar.saveclose.tooltip');
				} else {
					return fui.editor.getMessage('toolbar.close.tooltip');
				}
			},
			getIconClass : function(editor, obj) { return fui.editor.button.save.CLOSE_ICON_CLASS; },
			getButtonClass : function(editor, obj) { return fui.editor.button.save.CLOSE_BUTTON_CLASS; }
		});
	},

	/**
	 * Return the cancel button.
	 * @param editorId the id of the editor where the button is displayed
	 */
	getCancel: function() {
		return new fui.editor.Button({
			id : this.CANCEL_BUTTON_ID,
			index : this.CANCEL_DEFAULT_INDEX,
			supports: this.supports,
			clickHandler: fui.scope(this, this.closeClickHandler),
			getTitle : function(editor, obj) {
				return fui.editor.getMessage('toolbar.cancel.title');
			},
			getIconClass : function(editor, obj) { return fui.editor.button.save.CANCEL_ICON_CLASS; },
			getButtonClass : function(editor, obj) { return fui.editor.button.save.CANCEL_BUTTON_CLASS; }
		});
	},

	/**
	 * Returns true if this button supports the type.
	 *
	 * @param type the type to check
	 */
	supports: function(type) {
		return true;
	},

	/**
	 * Close click handler.
	 *
	 * @param type the object type
	 * @param id the object id
	 * @param button the UI button
	 */
	closeClickHandler: function(type, id, button) {
		this.clickHandler(type, id, button, 0);
	},

	/**
	 * Save click handler.
	 *
	 * @param type the object type
	 * @param id the object id
	 * @param button the UI button
	 */
	saveClickHandler: function(type, id, button) {
		this.clickHandler(type, id, button, 1);
	},

	/**
	 * Save & Close click handler.
	 *
	 * @param type the object type
	 * @param id the object id
	 * @param button the UI button
	 */
	saveCloseClickHandler: function(type, id, button) {
		this.clickHandler(type, id, button, 2);
	},

	/**
	 * Click Handler.
	 *
	 * @param type the object type
	 * @param id the object id
	 * @param button the UI button
	 * @param action the action indicator (0 = close, 1 = save, 2 = save/close)
	 */
	clickHandler: function(type, id, button, action) {
		var e = fui.editor;

		var editor = e.find(id);
		if ( !editor ) {
			return;
		}

		// in read-only mode, the save/close button will simply be close
		if (action === 0) {
			editor.deactivate();
			editor.closeHandler(editor.ui);
			return;
		}

		// action > 0 means it's either a save or save/close
		var isNew = fui.editor.isNewEditorId(id);
		if ( !isNew && !editor.hasChanged() ) {
			if ( action === 2 ) {
				editor.close();
			}
			return;
		}

		var config = {
			close: action === 2 ? true : undefined,
			reload: action === 1 ? true : undefined
		};

		//set the isNew value with the isNewEditorId(id) value
		// useful in case the asset is not having creation/modified times (Ex: category)
		config.isNew = isNew;

		editor.save(config);
	}
};
