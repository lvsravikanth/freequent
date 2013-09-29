/**
 * Set up the content section and our requirements
 */
fui.provide("fui.ui.content");

fui.require("fui.creator");

fui.ui.content = {
    /**
	 * Methods
	 */
	CREATE: "create",
	DELETE: "delete",
	EDIT: "edit",

	listType: {
		CONTAINERS: 'CONTAINERS',
		ITEMS: 'ITEMS',
		ALL: 'ALL'
	}
};

fui.provide("fui.ui.content.event");

fui.ui.content.event = {
	// Topic
	TOPIC: "/fui/ui/content/event",

	// Events
	CREATE: "create",
	UPDATE: "update",
	DELETE: "delete",
	LOCK:   "lock",
	UNLOCK: "unlock",
	MOVE:   "move",

	/**
	 * Subscribe to content events
	 *
	 * @param handler the event handler
	 */
	subscribe: function(handler) {
		fui.subscribe(fui.ui.content.event.TOPIC, handler);
	},

	/**
	 * Unsubscribe from content events
	 *
	 * @param handler the event handler
	 */
	unsubscribe: function(handler) {
		fui.unsubscribe(fui.ui.content.event.TOPIC, handler);
	}
};

fui.ui.content.Event = function() {
	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}
};

fui.extend(fui.ui.content.Event, {
	type: "",
	objects: []
});

/**
 * Set up the private content section and our requirements
 */
fui.provide("fui.ui.content.internal");

fui.require("fui.string");
fui.require("fui.io");

fui.ui.content.internal = {
	/**
	 * Handles the request
	 */
	handler: function(handler, method) {
		var isFunc = handler && fui.isFunction(handler);

		return function(response) {
			// any content that is json-able?
			if ( (response.length === 0) ||
				 !fui.string.startsWith(response, "/*{") ||
				 !fui.string.endsWith(response, "}*/") ) {
				if ( isFunc ) { handler(response, 0, method); }
				return;
			}

			var c = fui.creator;

			var result = fui.secure_eval(response);
			var total = result.total ? result.total : 0;
			var type;

			// a list of items?
			if ( result.items ) {
				// check for the filtered flag
				var filtered = result.filtered === true;

				var items = [];
				for ( var i = 0 ; i < result.items.length ; ++i ) {
					type = result.items[i].asObjectType || result.items[i].type;
					var o = c.createByType(type, result.items[i]);
					items.push( o ? o : result.items[i]);
				}

				if ( isFunc ) { handler(items, total, method, filtered); }
			} else if ( result.item ) {
				// a single item
				type = result.item.asObjectType || result.item.type;
				var item = c.createByType(type, result.item);
				if ( isFunc ) { handler(item, total, method); }
			} else {
				// unknown!
				if ( isFunc ) { handler(result, total, method); }
			}

			// Select event type
			var eventType;
			switch ( method ) {
				case fui.ui.content.CREATE:
					eventType = fui.ui.content.event.CREATE;
					break;

				case fui.ui.content.DELETE:
					eventType = fui.ui.content.event.DELETE;
					break;

                case fui.ui.content.EDIT:
					eventType = fui.ui.content.event.UPDATE;
					break;

				default:
					eventType = method;
					break;
			}

			// Set up event objects
			var eventObjects = [];
			if ( result.item ) {
				eventObjects = [result.item];
			} else if ( result.items ) {
				eventObjects = result.items;
			}

			var contentEvent = new fui.ui.content.Event({
				type: eventType,
				objects: eventObjects
			});

			// Publish it
			fui.publish(fui.ui.content.event.TOPIC, contentEvent);
			if (opener && !opener.closed && fui.exists("vui", opener) ) {
				opener.fui.publish(fui.ui.content.event.TOPIC, contentEvent);
			}
		};
	},

	/**
	 * Builds a request
	 */
	buildRequest: function(thisObj, method, requestData) {
		requestData.actionKey = thisObj.ACTION_KEY;
		requestData.method = method;

		requestData.handler = this.handler(requestData.handler, method);

		return fui.request.build(requestData);
	},

	/**
	 * Send an API request
	 */
	sendAPI: function(thisObj, method, requestData) {
		var request = this.buildRequest(thisObj, method, requestData);
		fui.io.api(request);
	}
};
