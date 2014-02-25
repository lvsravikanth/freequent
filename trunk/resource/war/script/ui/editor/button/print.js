/**
 * Set up the print section and our requirements
 */
fui.provide("fui.ui.editor.button.print");

fui.require("fui.editor.button");
fui.require("fui.ui.type");

fui.ui.editor.button.print = {
	PRINT_BUTTON_ID : 'fui-editor-toolbar-print',
	PRINT_BUTTON_CLASS : 'fui-btn-action',
	PRINT_ICON_CLASS : 'fui-editor-toolbar-print',
	PRINT_DEFAULT_INDEX : 1,
	CREATE_INVOICE_COLLECTOR_ID: 'fui-create-invoice-collector',

	/**
	 * Return the Print button.
	 *
	 */
	getPrint: function() {
		return new fui.editor.Button({
			id : this.PRINT_BUTTON_ID,
			index : this.PRINT_DEFAULT_INDEX,
			supports: this.supports,
			clickHandler: fui.scope(this, this.savePrintClickHandler),
			getTitle : function(editor, obj) {
				if (!editor.isReadOnly()) {
					return fui.editor.getMessage('toolbar.saveprint.title');
				} else {
					return fui.editor.getMessage('toolbar.print.title');
				}
			},
			getTooltip : function(editor, obj) {
				if (!editor.isReadOnly()) {
					return fui.editor.getMessage('toolbar.saveprint.tooltip');
				} else {
					return fui.editor.getMessage('toolbar.print.tooltip');
				}
			},
			getIconClass : function(editor, obj) { return fui.editor.button.save.PRINT_ICON_CLASS; },
			getButtonClass : function(editor, obj) { return fui.editor.button.save.PRINT_BUTTON_CLASS; },
			setupButton: this.setupButton
		});
	},

	/**
	 * Returns true if this button supports the type.
	 *
	 * @param type the type to check
	 */
	supports: function(type) {
		return (type === fui.ui.type.ORDER);
	},

	/**
	 * handles disabling button, as it will be decided at runtime.
	 *
	 * @param type
	 * @param editor
	 * @param tbButton
	 */
	setupButton: function(type, editor, tbButton) {
		var isNew = fui.editor.isNewEditorId(editor.id);
		if (isNew && editor.isReadOnly()) {
			tbButton.disabled = true;
		} else {
			// set callback
			editor.setSaveCallback(fui.ui.editor.button.print.getSaveCallbackHandler(editor.saveCallback));
		}
	},

	/**
	 * Print click handler.
	 *
	 * @param type the object type
	 * @param id the object id
	 * @param button the UI button
	 */
	printHandler: function(id) {
		fui.ui.manageorders.print(id);
	},

	getSaveCallbackHandler: function(orig) {
		return function(callbackData) {
			if(orig) {
				orig(callbackData);
			}
			if (callbackData.configCallbackData && callbackData.configCallbackData.print) {
				fui.ui.editor.button.print.printHandler(callbackData.id);
			}
		};
	},

	/**
	 * Save & Print click handler.
	 *
	 * @param type the object type
	 * @param id the object id
	 * @param button the UI button
	 */
	savePrintClickHandler: function(type, id, button) {
		var printFunc = function(btn) {
			if (btn == 'yes') {
				fui.ui.editor.button.print.clickHandler(type, id, button);
			}
		};
		var e = fui.editor;
		var editor = e.find(id);
		var reloadTitle = fui.editor.getMessage("print.order.confirm.title");
		var reloadText = editor.isReadOnly() ? fui.editor.getMessage("print.order.confirm.text") : fui.editor.getMessage("save.print.order.confirm.text");
		fui.msg.confirm(reloadTitle, reloadText, printFunc);
	},

	/**
	 * Click Handler.
	 *
	 * @param type the object type
	 * @param id the object id
	 * @param button the UI button
	 * @param action the action indicator (0 = close, 1 = save, 2 = save/close)
	 */
	clickHandler: function(type, id, button) {
		var e = fui.editor;

		var editor = e.find(id);
		if ( !editor ) {
			return;
		}
		var readOnly = editor.isReadOnly();

		// action > 0 means it's either a save or save/close
		var isNew = fui.editor.isNewEditorId(id);
		if (!readOnly) {
			// save the editor
			var config = {
				close: undefined,
				reload: true,
				callbackData: {print: true}
			};
			config.isNew = isNew;
			// add collector to set the createinvoice flag
			editor.addCollector(function (editor, data, requestData) {
				data = data || {};
				// remove the flag
				delete data['createinvoice'];
				if (requestData && requestData.addCreateInvoiceFlag) {
					fui.combine(data, {createinvoice: true});
				}
				return data;
			}, this.CREATE_INVOICE_COLLECTOR_ID);

			var requestData = {addCreateInvoiceFlag:true};
			editor.save(config, requestData);

			return; // print will be handled in the save callback handler.
		}

		// print the order
		fui.ui.editor.button.print.printHandler(id);
	}
};

// register this button
fui.editor.button.register(fui.ui.editor.button.print.getPrint());