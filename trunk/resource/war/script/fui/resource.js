/**
 * Set up the resource section and our requirements
 */
vui.provide("fui.resource");

fui.resource = {
	DEFAULT_TRANSPORT: "json",
	
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
	catalog: {}
};
