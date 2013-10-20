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

	/**
	 *  Replaces Ext.Msg.confirm()
	 */
	confirm: function(title, msg, fn, scope) {
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
		this.alert(title, msg, fn, scope);
	},

	/**
	 *  Replaces fui.ext.Msg.alert()
	 */
	alert: function(title, msg, fn, scope) {
		alert("TODO: "+msg); //todo
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
		this.alert(title, msg, fn, scope);
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
		this.alert(title, msg, fn, scope);
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
		this.alert(title, msg, fn, scope);
	}
};