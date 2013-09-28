/**
 * Set up the io section and our requirements
 */
fui.provide("fui.io");

fui.require("fui.lang");
fui.require("fui.transport");

fui.io = {
	// The default transport for send
	DEFAULT_TRANSPORT: "json",

	// The default ajax max length, based on which it is determined, whether it should be a POST.
	// If this is not found in the vuiConfig.maxAjaxGetLength, then it uses a length of 1200 as the default.
	MAX_AJAX_GET_LENGTH: (fuiConfig && fuiConfig.maxAjaxGetLength) ? fuiConfig.maxAjaxGetLength : 1200,

	// Data format types
	JSON_FORMAT: "json",
	XML_FORMAT: "xml",

	TRANSPORT_ERROR_MESSAGE_PREFIX: "XMLHttpTransport Error:",

	FILTER_FORM_ELEMENTS: ["file", "submit", "image", "reset", "button"],

	// Bulk functions
	startBulkSend: function() {
		fui.io.internal.bulk.start();
	},

	finishBulkSend: function(handler) {
		fui.io.internal.bulk.finish(handler);
	},

	/**
	 * Modified encodeForm from Dojo
	 */
	encodeForm: function(node) {
		if ( !node || !node.tagName || !node.tagName.toLowerCase() == "form" ) {
			if ( fui.log.isError() ) { fui.log.error("Invalid form node"); }
			return;
		}

		var filter = function(n) {
			if ( n.disabled ) { return false; }
			if ( !n.name ) { return false; }
			if ( !n.type ) { return false; }

			var type = n.type.toLowerCase();
			for ( var i = 0 ; i < fui.io.FILTER_FORM_ELEMENTS.length ; ++i ) {
				if ( type == fui.io.FILTER_FORM_ELEMENTS[i] ) {
					return false;
				}
			}

			return true;
		};

		var values = [];

		for ( var i = 0; i < node.elements.length; ++i ) {
			var elm = node.elements[i];
			if (!elm || elm.tagName.toLowerCase() == "fieldset" || !filter(elm)) { continue; }

			var name = encodeURIComponent(elm.name);
			var type = elm.type.toLowerCase();

			if ( type == "select-multiple" ) {
				for ( var j = 0; j < elm.options.length; ++j ) {
					if ( elm.options[j].selected ) {
						values.push(name + "=" + encodeURIComponent(elm.options[j].value));
					}
				}
			} else if ( (type == "radio") || (type == "checkbox") ) {
				if(elm.checked){
					values.push(name + "=" + encodeURIComponent(elm.value));
				}
			}else{
				values.push(name + "=" + encodeURIComponent(elm.value));
			}
		}

		// now collect input type="image", which doesn't show up in the elements array
		var inputs = node.getElementsByTagName("input");
		for ( var k = 0; k < inputs.length; ++k ) {
			var input = inputs[k];
			if (input.type.toLowerCase() == "image" && input.form == node && filter(input)) {
				var iname = encodeURIComponent(input.name);
				values.push(iname + "=" + encodeURIComponent(input.value));
				values.push(iname + ".x=0");
				values.push(iname + ".y=0");
			}
		}

		return values.join("&") + "&";
	},

	/**
	 * Core send function
	 *
	 * @param url the url to send the requests to
	 * @param requests the requests
	 * @param requestTransport the transport to use for the request
	 * @param responseTransport the transport to use for the response
	 * @param sync async/sync mode
	 */
	send: function(url, requests, requestTransport, responseTransport, sync, bulkHandlers) {
		// Check for bulk
		if ( fui.io.internal.bulk.inBulkMode() ) {
			if ( fui.log.isDebug() ) { fui.log.debug("Deferring send due to bulk mode"); }

			fui.io.internal.bulk.add(url, requests, requestTransport, responseTransport, sync);
			return;
		}

		// Anything to send?
		if ( requests.length === 0 ) {
			if ( fui.log.isDebug() ) { fui.log.debug("Send with no requests rejected"); }
			return;
		}

		if ( fui.log.isDebug() ) { fui.log.debug("Request count: " + requests.length); }

		// Check sync
		if ( !sync ) {
			sync = false;
		}

		// Check request transport
		if ( !requestTransport ) {
			requestTransport = fui.io.DEFAULT_TRANSPORT;
		}

		// Check response transport
		if ( !responseTransport ) {
			responseTransport = requestTransport;
		}

		var transport = fui.transport.get(requestTransport);
		var content = transport.getContent(requests, requestTransport, responseTransport);
		var timeoutSeconds = 0;
		var method = "GET";
		for ( var i = 0 ; i < requests.length ; ++i) {
			timeoutSeconds = requests[i].timeoutSeconds > timeoutSeconds ? requests[i].timeoutSeconds : timeoutSeconds;
			if ( requests[i].ajaxMethod === "POST" ) {
				method = "POST";
			}
		}

		var options = {
			url: url,
			sync: sync,
			data: content,
			timeout: timeoutSeconds,
			transport: responseTransport,
			method: method
		};

		this.internalSend(options, requests, bulkHandlers);
	},

	/**
	 * API function
	 *
	 * @param request the request
	 * @param responseTransport the transport to use for the response
	 * @param sync async/sync mode
	 * @param url the url to send the requests to
	 */
	api: function(request, responseTransport, url) {
		url = url || fui.apiPath;

		// Check for bulk
		if ( fui.io.internal.bulk.inBulkMode() ) {
			if ( fui.log.isDebug() ) { fui.log.debug("Deferring api request due to bulk mode"); }

			fui.io.internal.bulk.add(url, [request], fui.io.DEFAULT_TRANSPORT, responseTransport, request.sync);
			return;
		}

		// Check response transport
		if ( !responseTransport ) {
			responseTransport = fui.io.DEFAULT_TRANSPORT;
		}

		url += '/' + request.actionKey + ((request.method) ? '/' + request.method : '') + 
			 '.' + responseTransport;

		var context = request.getContext();
		var content = fui.combine(context, request.getContent());
		content = fui.io.internal.stringify(content);

		var timeoutSeconds = request.timeoutSeconds > 0 ? request.timeoutSeconds : 0;

		var options = {
			url: url,
			sync: request.sync,
			data: content,
			timeout: timeoutSeconds,
			transport: responseTransport,
			method: request.ajaxMethod
		};

		var formId = request.formId;
		if ( formId && formId.length > 1 ) {
			options.formId = formId;
			options.method = "POST";
		}

		this.internalSend(options, [request], null);
	},

	internalSend: function(options, requests, bulkHandlers) {
		// Send method for IE is always post since it has a URL character limit
		options.method = fui.isIE() ? "POST" : options.method || "GET";

		// Additionally also check the length. Some app servers would not be
		// able to parse a huge/large query String. So, if it is greater than the value
		// specified in vuiConfig.maxAjaxGetLength or 1200 (if not specified) then make it a POST.
		if ( options.method !== "POST") {
			var size = 0;
			for ( var x in options.data ) {
				//protect against undefined value
				size = options.data[x] ? size + (options.data[x]).toString().length : size;
				if ( size > fui.io.MAX_AJAX_GET_LENGTH ) {
					options.method = "POST";
					break;
				}
			}
		}

		if ( fui.log.isDebug() ) {
			fui.log.debug("Sending to url: " + options.url);
			fui.log.debug("Sending method: " + options.method);
			fui.log.debug("Sending sync: " + options.sync);
			fui.log.debug("Sending response transport: " + options.transport);
		}

		var transport = fui.transport.get(options.transport);

		// Success handler
		options.success = function(data) {
			var result = false;

			var responses;
			try {
				// Finish activity
				fui.io.event.internal.finish();

				responses = transport.parse(data);

				fui.io.internal.process(requests, responses);

				fui.io.internal.bulk.callHandlers(bulkHandlers, true);

				result = true;
			} catch (e) {
				if ( fui.log.isError() ) { fui.log.error("An error has occurred: " + e); }
				throw e;
			}

			return result;
		};

		// Error handler
		options.error = function(xhr, error) {
			var authError = xhr.status === 401;

			// Check to see if the response text has actual parsable data that can be handled
			// Only if not authorization error
			if ( !authError && xhr.responseText && (xhr.responseText.indexOf('xapi') > 0) ) {
				if ( options.success(xhr.responseText) ) {
					return;
				}
			}

			var handled = false;
			try {
				// Finish activity
				fui.io.event.internal.finish();

				if ( !authError && fui.log.isError() ) { fui.log.error("An error has occurred: " + xhr.status + " "+ error); }

				// Something really bad so notify all request handlers
				for ( i = 0 ; i < requests.length ; ++i ) {
					var handler = requests[i].handler;
					if ( handler ) {
						var result = authError ? handler.authorization(xhr.responseText) : handler.error(error);
						handled |= result;
					}
				}

				fui.io.internal.bulk.callHandlers(bulkHandlers, false);
			} catch (e) {
				if ( fui.log.isError() ) { fui.log.error("An error has occurred: " + e); }
				throw e;
			}

			if ( !handled ) {
				throw new Error(error);
			}
		};

		// Get any auth headers
		options.headers = fui.auth.getHeaders();

		// Start activity
		fui.io.event.internal.start();

		// Send
		if ( options.formId ) {
			fui.ajaxForm(options);
		} else {
			fui.ajax(options);
		}
	},

	getSingleResponse: function(data) {
		if ( data.indexOf('xapi') > 0 ) {
			try {
				var parsed = fui.transport.json.parse(data);
				if ( parsed.responses && parsed.responses.length > 0 ) {
					data = parsed.responses[0].content;
				}
			} catch ( e ) {
				if ( fui.log.isError() ) { fui.log.error(e); }
			}
		}

		return data;
	},

	getSingleError: function(data) {
		if ( data.indexOf('xapi') > 0 ) {
			try {
				var parsed = fui.transport.json.parse(data);
				if ( parsed.errors && parsed.errors.length > 0 ) {
					data = parsed.errors[0];
				}
			} catch ( e ) {
				if ( fui.log.isError() ) { fui.log.error(e); }
			}
		}

		return data;
	}
};

/**
 * IO Events
 */
fui.provide("fui.io.event");

fui.io.event = {
	// Topic
	IO_TOPIC: "/vui/io/event",

	// Activity events
	START: "start",
	FINISH: "finish",

	/**
	 * Subscribe to IO events
	 *
	 * @param handler the event handler
	 */
	subscribe: function(handler) {
		fui.subscribe(fui.io.event.IO_TOPIC, handler);
	},

	/**
	 * Unsubscribe from IO events
	 *
	 * @param handler the event handler
	 */
	unsubscribe: function(handler) {
		fui.unsubscribe(fui.io.event.IO_TOPIC, handler);
	}
};

fui.io.Event = function() {
	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}
};

fui.extend(fui.io.Event, {
	type: ""
});

/**
 * Set up the private io section and our requirements
 */
fui.provide("fui.io.internal");

fui.io.internal = {
	/**
	 * Stringifies any object values in content.
	 *
	 * @param content the content to stringify
	 */
	stringify: function(content) {
		for ( var key in content ) {
			var value = content[key];
			if ( fui.isArray(value) ) {
				value = value.toString();
				content[key] = value;
			} else if ( fui.isObject(value) ){
				value = fui.json.stringify(value);
				content[key] = value;
			}
		}

		return content;
	},

	/**
	 * Process the server response
	 */
	process: function(requests, responses) {
		if ( fui.log.isDebug() ) { fui.log.debug("Starting process"); }

		// process
		this.processErrors(responses.errors, requests);

		this.processMessages(responses.messages, requests);

		this.processResponses(responses.responses, requests);

		if ( fui.log.isDebug() ) { fui.log.debug("Finished process"); }
	},

	processException: function(e, handler) {
		var throwit = true;
		if ( handler ) {
			throwit = !handler.error(e);
		}

		if ( throwit ) {
			if ( fui.log.isError() ) { fui.log.error("Unhandled exception", e); }
			throw e;
		}
	},

	/**
	 * Process a server response for errors
	 */
	processErrors: function(errors, requests) {
		if ( !errors ) {
			return;
		}

		if ( fui.log.isDebug() ) { fui.log.debug("Processing errors: " + errors.length); }

		for ( var i = 0 ; i < errors.length ; ++i ) {
			var response = errors[i];
			if ( fui.log.isDebug() ) {
				fui.log.debug("Error " + i + ": " + response.message);
				if ( response.rootMessage ) {
					fui.log.debug("Root error " + i + ": " + response.rootMessage);
				}
			}

			var handler = this.getHandler(response, requests);
			try {
				if ( handler ) { handler.error(response.message, response.rootMessage); }
			} catch (e) {
				this.processException(e, handler);
			}
		}
	},

	/**
	 * Process a server response for messages
	 * @param requests list of requests so we can find the right handler
	 */
	processMessages: function(messages, requests) {
		if ( !messages ) {
			return;
		}

		if ( fui.log.isDebug() ) { fui.log.debug("Processing messages: " + messages.length); }

		for ( var i = 0 ; i < messages.length ; ++i ) {
			var response = messages[i];
			if ( fui.log.isDebug() ) { fui.log.debug("Message " + i + ": " + response.message); }

			var handler = this.getHandler(response, requests);
			try {
				if ( handler ) { handler.message(response.message); }
			} catch (e) {
				this.processException(e, handler);
			}
		}
	},

	/**
	 * Process a server response for responses
	 */
	processResponses: function(responses, requests) {
		if ( !responses ) {
			return;
		}

		if ( fui.log.isDebug() ) { fui.log.debug("Processing responses: " + responses.length); }

		for ( var i = 0 ; i < responses.length ; ++i ) {
			var response = responses[i];
			if ( fui.log.isDebug() ) { fui.log.debug("Response " + i + ": " + response.content); }

			var handler = this.getHandler(response, requests);
			try {
				if ( handler ) { handler.response(response.content); }
			} catch (e) {
				this.processException(e, handler);
			}
		}
	},

	/**
	 * Get the handler for the response
	 *
	 * @param response the response
	 * @param requests the requests that contain the handlers
	 */
	getHandler: function(response, requests) {
		// If we're missing a request id default to first handler
		if ( !response.id || (response.id.length === 0) ) {
			return requests[0].handler;
		}

		for ( var i = 0 ; i < requests.length ; ++i ) {
			if ( response.id == requests[i].id ) {
				return requests[i].handler;
			}
		}

		return null;
	}
};

/**
 * Bulk requests
 */
fui.io.internal.Bulk = function() {
	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}
};

fui.extend(fui.io.internal.Bulk, {
	url: "",
	requestTransport: "",
	responseTransport: "",
	sync: false,
	requests: []
});

fui.io.internal.bulk = {
	count: 0,
	bulkRequests: [],
	handlers: [],

	/**
	 * In bulk mode
	 */
	inBulkMode: function() {
		return (this.count > 0);
	},

	/**
	 * Start bulk mode
	 */
	start: function() {
		if ( (this.count === 0) && fui.log.isDebug() ) { fui.log.debug("Starting bulk send"); }

		++this.count;
	},

	/**
	 * Finish bulk mode and send
	 */
	finish: function(handler) {
		if ( handler ) {
			this.handlers.push(handler);
		}

		if ( --this.count > 0 ) {
			return;
		}

		if ( fui.log.isDebug() ) { fui.log.debug("Finishing bulk send"); }

		var bulks = this.bulkRequests;
		var handlers = this.handlers;

		this.bulkRequests = [];
		this.handlers = [];

		var len = bulks.length;
		for ( var i = 0 ; i < len ; ++i ) {
			var bulk = bulks[i];

			fui.io.send(bulk.url, bulk.requests, bulk.requestTransport, bulk.responseTransport,
					bulk.sync, (i == (len - 1)) ? handlers : null);
		}

		if ( len === 0 ) {
			this.callHandlers(handlers, true);
		}
	},

	callHandlers: function(handlers, success) {
		if ( !handlers || (handlers.length === 0) ) {
			return;
		}

		for ( var i = 0 ; i < handlers.length ; ++i ) {
			var handler = handlers[i];
			if ( handler ) {
				handler(success);
			}
		}
	},

	/**
	 * Add a bulk send
	 *
	 * @param url the url to send the request to
	 * @param requests the requests
	 * @param requestTransport the transport to use for the request
	 * @param responseTransport the transport to use for the response
	 * @param sync async/sync mode
	 */
	add: function(url, requests, requestTransport, responseTransport, sync) {
		var bulk;
		for ( var i = 0; i < this.bulkRequests.length; ++i ) {
			var good = false;

			bulk = this.bulkRequests[i];
			if ( bulk.url == url ) {
				good = true;
			}

			if ( good && requestTransport && (bulk.requestTransport != requestTransport) ) {
				good = false;
			}

			if ( good && responseTransport && (bulk.responseTransport != responseTransport) ) {
				good = false;
			}

			if ( good && sync && (bulk.sync != sync) ) {
				good = false;
			}

			if ( good ) {
				for ( var j = 0 ; j < requests.length ; ++j ) {
					bulk.requests.push(requests[j]);
				}

				return;
			}
		}

		bulk = new fui.io.internal.Bulk({
			url: url,
			requests: requests,
			requestTransport: requestTransport,
			responseTransport: responseTransport,
			sync: sync});
		this.bulkRequests.push(bulk);
	}
};

/**
 * Set up the private io event section and our requirements
 */
fui.provide("fui.io.event.internal");

fui.io.event.internal = {
	// Activity level
	activityLevel: 0,

	start: function() {
		if ( ++this.activityLevel == 1 ) {
			fui.publish(fui.io.event.IO_TOPIC, new fui.io.Event({type: fui.io.event.START}));
		}
	},

	finish: function() {
		if ( --this.activityLevel === 0 ) {
			fui.publish(fui.io.event.IO_TOPIC, new fui.io.Event({type: fui.io.event.FINISH}));
		}
	}
};
