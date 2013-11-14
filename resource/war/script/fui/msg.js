fui.provide("fui.msg");

/**
 * Convenient Ext.MessageBox "overridden" implementations
 */
fui.msg = {

	/**
	 * @constant
	 * @desc a list of CSS classes to style the buttons
	 * @public
	 */
	BUTTON_CLASS: 'fui-button',

	TYPE_CONFIRM: "confirm",
	TYPE_ALERT: "alert",
	TYPE_CONFIRM_OK: "confirmOK",
	TYPE_CONFIRM_SAVE: "confirmSave",
	TYPE_PROMPT: "prompt",

	/**
	 *  Replaces Ext.Msg.confirm()
	 */
	confirm: function(title, msg, fn, scope) {
		fui.msg.internal.show(this.TYPE_CONFIRM, title, msg, fn, scope);
		/*return fui.ext.Msg.show({
			title: title,
			msg: msg,
			fn: fn,
			scope: scope,
			width: 400,
			buttons: {yes: fui.ui.getMessage('yes'), no: fui.ui.getMessage('no')},
			closable: false,
			cls: "fui-dialog",
			icon: fui.ext.Msg.QUESTION
		});*/
	},

	/**
	 *  Replaces fui.ext.Msg.alert()
	 */
	alert: function(title, msg, fn, scope) {
		fui.msg.internal.show(this.TYPE_ALERT, title, msg, fn, scope);
	},

	/**
	 *  Confirm with ok/cancel buttons
	 */
	confirmOk: function(title, msg, fn, scope) {
		/*return fui.ext.Msg.show({
			title: title,
			msg: msg,
			fn: fn,
			scope: scope,
			width: 400,
			buttons: {ok: fui.ui.getMessage('ok'), cancel: fui.ui.getMessage('cancel')},
			closable: false,
			cls: "fui-dialog",
			icon: fui.ext.Msg.QUESTION
		});*/
		fui.msg.internal.show(this.TYPE_CONFIRM_OK, title, msg, fn, scope);
	},

	/**
	 *  Confirm with yes/no/cancel buttons
	 */
	confirmSave: function(title, msg, fn, scope, buttons) {
		/*return fui.ext.Msg.show({
			title: title,
			msg: msg,
			fn: fn,
			scope: scope,
			width: 400,
			buttons: (buttons) ? buttons : {yes: fui.ui.getMessage('yes'), no: fui.ui.getMessage('no'), cancel: fui.ui.getMessage('cancel')},
			closable: false,
			cls: "fui-dialog",
			icon: fui.ext.Msg.QUESTION
		});*/
		fui.msg.internal.show(this.TYPE_CONFIRM_SAVE, title, msg, fn, scope, buttons);
	},

	/**
	 * Prompt the user to enter some text with ok and cancel buttons
	 */
	prompt: function(title, msg, value, fn, scope, multiline){
		multiline = multiline || false;
		/*return fui.ext.Msg.show({
			title: title,
			msg: msg,
			fn: fn,
			scope: scope,
			width: 400,
			buttons: {ok: fui.ui.getMessage('ok'), cancel: fui.ui.getMessage('cancel')},
			closable: false,
			cls: "fui-dialog",
			prompt: true,
			multiline: multiline,
			value: value
		});	*/
		fui.msg.internal.show(this.TYPE_PROMPT, title, msg, fn, scope);
	}
};

fui.msg.internal = {


	show: function(type, title, msg, fn, scope, buttons) {
		if (!buttons) {
			buttons = [];
			if (type === fui.msg.TYPE_CONFIRM) {
				buttons.push({
					text:fui.workspace.getMessage("yes"),
					click: this.buildButtonHandler("yes",fn)
				});
				buttons.push({
					text:fui.workspace.getMessage("no"),
					click:this.buildButtonHandler("no",fn)
				});
			} else if (type === fui.msg.TYPE_CONFIRM_OK) {
				buttons.push({
					text:fui.workspace.getMessage("ok"),
					click: this.buildButtonHandler("ok",fn)
				});
				buttons.push({
					text:fui.workspace.getMessage("cancel"),
					click:this.buildButtonHandler("cancel",fn)
				});
			} else if (type === fui.msg.TYPE_CONFIRM_SAVE) {
				buttons.push({
					text:fui.workspace.getMessage("yes"),
					click: this.buildButtonHandler("yes",fn)
				});
				buttons.push({
					text:fui.workspace.getMessage("no"),
					click: this.buildButtonHandler("no",fn)
				});
				buttons.push({
					text:fui.workspace.getMessage("cancel"),
					click:this.buildButtonHandler("cancel",fn)
				});
			} else if (type == fui.msg.TYPE_PROMPT) {
				// todo
			} else {
				buttons.push({
					text:fui.workspace.getMessage("ok"),
					click:this.buildButtonHandler("ok",fn)
				});
			}
		}
		var dialogCls = 'fui-dialog fui-dialog-' + type +' fui-editor-no-native-close';
		var config = {
			modal: true,
			autoOpen: true,
			dialogClass: dialogCls,
			buttons: buttons,
			title: title,
			close: function(event, ui) {
				fui.query(this).dialog('destroy').remove();
			}
		}

		fui.query("<div>"+msg+"</div>").dialog(config);
	},
	buildButtonHandler : function(button, handler) {
		return function() {
			fui.query( this ).dialog( "close" );
			if (handler)
				handler(button);
		};
	}
};