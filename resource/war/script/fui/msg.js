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
		fui.msg.internal.show(this.TYPE_CONFIRM_OK, title, msg, fn, scope);
	},

	/**
	 *  Confirm with yes/no/cancel buttons
	 */
	confirmSave: function(title, msg, fn, scope, buttons) {
		fui.msg.internal.show(this.TYPE_CONFIRM_SAVE, title, msg, fn, scope, buttons);
	},

	/**
	 * Prompt the user to enter some text with ok and cancel buttons
	 */
	prompt: function(title, msg, value, fn, scope, multiline){
		multiline = multiline || false;
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
		var dialogCls = 'fui-alert fui-alert-' + type +' fui-editor-no-native-close';
		var config = {
			modal: true,
			autoOpen: true,
			dialogClass: dialogCls,
			buttons: buttons,
			title: title,
			width: 400,
			resizable: false,
			close: function(event, ui) {
				fui.query(this).dialog('destroy').remove();
			}
		}

		fui.query('<div/>').html(msg).dialog(config);
	},
	buildButtonHandler : function(button, handler) {
		return function() {
			fui.query( this ).dialog( "close" );
			if (handler)
				handler(button);
		};
	}
};