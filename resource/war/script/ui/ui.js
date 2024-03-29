fui.provide("fui.ui");

fui.require("fui.handler");
fui.require("fui.html");
fui.require("fui.auth");

fui.ui = {
	/**
	 * @function
	 * @desc UI error handler.
	 *
	 * @param message the error message
	 * @param rootMessage the root error message
	 * @public
	 */
	errorHandler: function(message, rootMessage) {
		if ( fui.log.isError() ) {
			fui.log.error(message);
			if ( rootMessage ) {
				fui.log.error(rootMessage);
			}
		}

		// If the message is just error, swallow it for now
		if ( message && (message.toLowerCase && message.toLowerCase() === "error") ) {
			return true;
		}

		if (message) {
			//message = fui.html.escape(message);
			if (rootMessage) {
				//rootMessage = fui.html.escape(rootMessage);
				message += '<br/>' + rootMessage;
			}

			//fui.vext.msg.alert(fui.ui.workspace.getMessage('error'), message);
			fui.msg.alert(fui.workspace.getMessage("error"), message);
		}

		return true;
	},

	/**
	 * @function
	 * @desc UI message handler.
	 *
	 * @param message the message
	 * @public
	 */
	messageHandler: function(message) {
		if ( fui.log.isInfo() ) { fui.log.info(message); }
		if ( message ) {
			//fui.ui.notification.message(message);
			fui.msg.alert(fui.workspace.getMessage("message"), message);
		}
		return true;
	},

	/**
	 * @function
	 * @desc UI authorization handler.
	 *
	 * @param response the authorization response
	 * @public
	 */
	authorizationHandler: function(response) {
		//if ( fui.log.isError() ) { fui.log.error(response); }
		fui.auth.logout(fui.auth.AUTHORIZATION_LOGOUT); //todo

		return true;
	}
};

// UI Overrides
(function override() {
	// Handler
	var org = fui.handler.build;

	fui.handler.build = function(requestData, response) {
		requestData.errorHandler = requestData.errorHandler || fui.ui.errorHandler;
		requestData.messageHandler = requestData.messageHandler || fui.ui.messageHandler;
		requestData.authorizationHandler = requestData.authorizationHandler || fui.ui.authorizationHandler;

		return org(requestData, response);
	};

	// Editor resources
	fui.editor.BASENAME = 'com.scalar.freequent.l10n.EditorResource';

	// Workspace resources
	fui.workspace.BASENAME = 'com.scalar.freequent.l10n.WorkspaceResource';
})();