/**
 * Set up the resource section and our requirements
 */
fui.provide("fui.resource");

fui.resource = {
	DEFAULT_TRANSPORT: "json",

	ACTION_KEY: "resource",

	/**
	 * method name for getting the catalog.
	 */
	GET_CATALOG: "getcatalog",

	BASENAME_ATTRIBUTE: "basename",
	
	/**
	 * Stores a map of messages under the basename for later retrieval.
	 * 
	 * @param basename the basename for the messages
	 * @param messageMap the map of messages
	 */
	setMessages: function(basename, messageMap) {
		fui.resource.internal.catalog[basename] = messageMap;
	},
	
	/**
	 * Returns an HTML-escaped message for the provided basename and key.
	 * 
	 * @param basename the basename for the message
	 * @param key the key for the message
	 */
	getMessage: function(basename, key) {
		var map = fui.resource.internal.catalog[basename];
		if ( !map ) {
			//return basename + '.' + key;
			map = fui.resource.internal.getCatalog(basename);
		}
		if (!map) {
			return basename + '.' + key;
		}
		var message = map[key];
		if ( !message ) {
			return basename + '.' + key;
		}
		
		return message;
	}
};

/**
 * Set up the private resource section and our requirements
 */
fui.provide("fui.resource.internal");

fui.resource.internal = {
	catalog: {},
	getCatalog: function(basename) {
		var handler = function(messageMap) {

			fui.resource.setMessages(basename, fui.secure_eval(messageMap));
			return messageMap;
		};

		var requestData = requestData || {};
		requestData.content = requestData.content || {};
		requestData.content[fui.resource.BASENAME_ATTRIBUTE] = basename;

		requestData.actionKey = fui.resource.ACTION_KEY;
		requestData.method = fui.resource.GET_CATALOG;
		requestData.handler = handler;
		requestData.sync = true;

		var request = fui.request.build(requestData);
		fui.io.api(request);
	}
};
