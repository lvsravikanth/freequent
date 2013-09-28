/**
 * Set up the transport section and our requirements
 */
fui.provide("fui.transport");
fui.provide("fui.transport.Transport");

fui.transport = {
	// Tags
	REQUEST_TAG: "fuiRequest",
	ERROR_TAG: "fuiError",
	MESSAGE_TAG: "fuiMessage",
	RESPONSE_TAG: "fuiResponse",
	SEARCH_TAG: "<fui",

	// Attributes
	REQUEST_ID_ATTRIBUTE: "id",
	ACTION_KEY_ATTRIBUTE: "action.key",
	METHOD_ATTRIBUTE: "method",
	REQUEST_DATA_FORMAT_ATTRIBUTE: "data.format",
	RESPONSE_DATA_FORMAT_ATTRIBUTE: "response.data.format",
	MODIFIER_KEY_ATTRIBUTE: "modifier.key",
	MESSAGE_ATTRIBUTE: "message",
	CONTENT_ATTRIBUTE: "content",

	// Keys
	REQUEST_KEY: "fui.request",
	REQUEST_TRANSPORT_KEY: "fui.request.transport",
	RESPONSE_TRANSPORT_KEY: "fui.response.transport",

	get: function(type) {
		var creator = fui.transport.internal.creators[type];
		if ( !creator ) {
			if ( fui.log.isError() ) { fui.log.error("Unable to find transport creator for type: " + type); }
			return null;
		}

		return creator();
	},

	register: function(type, creator) {
		// Check creator cache
		if ( fui.transport.internal.creators[type] ) {
			if ( fui.log.isError() ) { fui.log.error("Attempt to register same transport type again: " + type); }
			return;
		}

		// Register the creator
		fui.transport.internal.creators[type] = creator;
	}
};

fui.transport.Transport = function() {
	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}
};

fui.extend(fui.transport.Transport, {
	getContent: function(requests, requestTransport, responseTransport) {
		var request = this.getRequestPrefix();

		for (var i = 0 ; i < requests.length ; ++i) {
			request += this.getRequest(requests[i]) + this.getRequestSeparator();
		}

		request += this.getRequestSuffix();

		// Prep content
		var content = {};
		content[fui.transport.REQUEST_TRANSPORT_KEY] = requestTransport;
		if ( responseTransport ) {
			content[fui.transport.RESPONSE_TRANSPORT_KEY] = responseTransport;
		}
		content[fui.transport.REQUEST_KEY] = request;

		return content;
	},

	getRequestPrefix: function() {
		return "";
	},

	getRequestSuffix: function() {
		return "";
	},

	getRequestSeparator: function() {
		return "";
	},

	getRequest: function(request) {
		return "";
	},

	parse: function(data) {
		return {};
	}
});

fui.transport.RESPONSE_ATTRIBUTES = [
		fui.transport.REQUEST_ID_ATTRIBUTE, fui.transport.ACTION_KEY_ATTRIBUTE,
		fui.transport.METHOD_ATTRIBUTE, fui.transport.RESPONSE_DATA_FORMAT_ATTRIBUTE,
		fui.transport.MODIFIER_KEY_ATTRIBUTE, fui.transport.MESSAGE_ATTRIBUTE
];

fui.provide("fui.transport.internal");

fui.transport.internal = {
	// Creators
	creators: {}
};
