fui.provide("fui.handler");
fui.provide("fui.Handler");

fui.handler = {
	build: function(requestData, response) {
		var handler = new fui.Handler();

		handler.response = response || requestData.handler;
		handler.error = requestData.errorHandler || handler.error;
		handler.message = requestData.messageHandler || handler.message;
		handler.authorization = requestData.authorizationHandler || handler.authorization;

		return handler;
	}
};

/**
 * Handler object that is used with requests
 */
fui.Handler = function() {
	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}
};

fui.extend(fui.Handler, {
	/**
	 * Handles a good response.
	 *
	 * @param response the response
	 */
	response: function(response) {
	},

	/**
	 * Handles an error response.
	 *
	 * @param message the error message
	 * @param rootMessage the root error message
	 */
	error: function(message, rootMessage) {
		if ( fui.log.isError() ) {
			fui.log.error(message);
			if ( rootMessage ) {
				fui.log.error(rootMessage);
			}
		}
		return false;
	},

	/**
	 * Handles a message response.
	 *
	 * @param message the message
	 */
	message: function(message) {
		if ( fui.log.isInfo() ) { fui.log.info(message); }
	},

	/**
	 * Handles an authorization response.
	 *
	 * @param response the response
	 */
	authorization: function(response) {
		if ( fui.log.isError() ) { fui.log.error(response); }
		return false;
	}
});
