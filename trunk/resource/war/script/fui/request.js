fui.provide("fui.request");
fui.provide("fui.Request");

fui.require("fui.io");
fui.require("fui.lang");
fui.require("fui.handler");
fui.require("fui.Handler");

fui.request = {
	build: function(requestData, handler, context) {
		handler = handler || fui.handler.build(requestData);
		requestData.handler = handler;

		context = context || fui.context.build(requestData);
		requestData.context = context;

		var request = new fui.Request();
		fui.lang.combine(request, requestData, { precedent: "right", combineAs: "intersection", clone: false });

		return request;
	}
};

/**
 * Request object for send
 */
fui.Request = function() {
	this.id = Math.random();
	this.handler = new fui.Handler();

	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}
};

fui.extend(fui.Request, {
	// Request parameters
	id: 0,
	actionKey: "",
	method: "",
	content: {},

	// Data formats and modifier
	requestDataFormat: "",
	responseDataFormat: "",
	modifierKey: "",

	// Content building from nodes
	formNode: {},

	// Response handler
	handler: null,

	// Context
	context: null,

	// Timeout
	timeoutSeconds: 0,

	// Sync
	sync: false,

	// Ajax method, GET or POST
	ajaxMethod: null,
	
	// formId for multipart requests
	formId: null,

	/**
	 * Returns the content for this request.
	 */
	getContent: function() {
		// Make sure content isn't null
		var content = this.content || {};

		// Check for form node
		if ( this.formNode && this.formNode.tagName ) {
			// Encode it
			var query = fui.io.encodeForm(this.formNode);
			if(query.length){
				// Merge it into content
				var parameters = query.split("&");
				for ( var i = 0; i < parameters.length; ++i ) {
					if ( parameters[i].length ) {
						var parameter = parameters[i].split("=");
						content[parameter[0]] = parameter[1];
					}
				}
			}
		}

		return content;
	},

	/**
	 * Returns the context for this request.
	 */
	getContext: function() {
		return this.context ? fui.context.getRequestContext(this.context) : {};
	}
});
