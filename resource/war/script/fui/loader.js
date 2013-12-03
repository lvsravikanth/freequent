/**
 * Set up the loader section and our requirements
 */
fui.provide("fui.loader");

fui.require("fui.string");
fui.require("fui.html");
fui.require("fui.auth");
fui.require("fui.transport");

fui.loader = {
	/**
	 * Returns the current location hash stripping off the leading #.
	 */
	getHash: function() {
		return fui.loader.internal.cleanHash(window.location.hash);
	},

	/**
	 * Sets the hash value.
	 *
	 * @param value the value
	 */
	setHash: function(hash) {
		hash = fui.loader.internal.cleanHash(hash);
		window.location.hash = hash;
	},

	/**
	 * Clears the current location hash
	 */
	clearHash: function() {
		window.location.hash = "#";
	},

	/**
	 * Loads the body from the server using the hash to store the path.
	 *
	 * @param path the path to load
	 * @param params params for the load request
	 * @param callback function to call after load
	 */
	loadBody: function(path, params, callback) {
		path = fui.loader.internal.cleanHash(path);
		if ( fui.auth.isValid() ) {
			if ( !path || (path.length === 0) ) {
				return;
			}

			window.location.hash = path;

			path = fui.apiPath + path;
		} else {
			if ( path && (path.length > 0) && (path !== fui.auth.LOGIN_SCREEN_PATH) ) {
				params = params || {};
				params['fui.loader.path'] = path;
			}

			path = fui.apiPath + fui.auth.LOGIN_SCREEN_PATH ;
		}

		var li = fui.loader.internal;

		path = li.encodePath(path);

		var options = {
			url: path,
			sync: false,
			data: params,
			timeout: null,
			method: "POST",
			success: li.getSuccess(callback),
			error: li.getError(callback)
		};

		fui.publish(fui.loader.event.TOPIC, new fui.loader.Event({type: fui.loader.event.START}));

		fui.ajax(options);
	},

	/**
	 * Adds a cleaner function that is invoked before a successful load is completed. This gives the function an
	 * opportunity to do any clean up between loader transitions.
	 */
	addCleaner: function(cleaner) {
		if ( !cleaner ) {
			return;
		}

		fui.loader.internal.cleaners.push(cleaner);
	}
};

/**
 * Loader Events
 */
fui.provide("fui.loader.event");

fui.loader.event = {
	// Topic
	TOPIC: "/fui/loader/event",

	TOPIC_RESIZE: "/fui/loader/resize",

	// Activity events
	START: "start",
	FINISH: "finish",
	RESIZE: "resize",

	/**
	 * Subscribe to loader events
	 *
	 * @param handler the event handler
	 */
	subscribe: function(handler) {
		fui.subscribe(fui.loader.event.TOPIC, handler);
	},

	/**
	 * Unsubscribe from loader events
	 *
	 * @param handler the event handler
	 */
	unsubscribe: function(handler) {
		fui.unsubscribe(fui.loader.event.TOPIC, handler);
	}
};

fui.loader.Event = function() {
	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}
};

fui.extend(fui.loader.Event, {
	type: ""
});

/**
 * Set up the private loader section and our requirements
 */
fui.provide("fui.loader.internal");

fui.require("fui.io");

fui.loader.internal = {
	LOADER_NODE_ID: 'fui-loader',

	cleaners: [],

	/**
	 * Cleans up a hash path of extraneous #.
	 *
	 * @param path the path to clean
	 */
	cleanHash: function(path) {
		if ( path && fui.string.startsWith(path, '#') ) {
			if ( path.length < 3 ) { // '#' or '#/'
				path = "";
			} else {
				path = path.substring(1);
			}
		}

		return path;
	},

	encodePath: function(path) {
		var major = path.split('?');

		// encode url elements
		var url = major[0];
		var parts = url.split('/');
		for ( var i = parts.length - 1 ; i > -1 ; --i ) {
			parts[i] = encodeURIComponent(parts[i]);
		}

		path = parts.join('/');

		// add query elements
		if ( major.length > 1 ) {
			path += '?' + major[1];
		}

		return path;
	},

	getSuccess: function(callback) {
		return function(data) {
			fui.loader.internal.success(data, callback);
		};
	},

	success: function(data, callback) {
		// Check for error
		var error = fui.io.getSingleError(data);
		if ( error !== data ) {
			fui.loader.internal.error(null, error.message);
			return;
		}

		data = fui.io.getSingleResponse(data);

		// Allow for clean up before pushing out the new page
		for ( var i = 0 ; i < fui.loader.internal.cleaners.length ; ++i ) {
			fui.loader.internal.cleaners[i]();
		}

		setTimeout(function() {
			//fui.html.set(fui.loader.internal.LOADER_NODE_ID, data, true, true, true);
			fui.query("#"+fui.loader.internal.LOADER_NODE_ID).html(data);

			fui.publish(fui.loader.event.TOPIC, new fui.loader.Event({type: fui.loader.event.FINISH}));

			if ( callback ) {
				callback(true);
			}
		}, 0);
	},

	getError: function(callback) {
		return function(xhr, error) {
			fui.loader.internal.error(xhr, error);
			if ( callback ) {
				callback(false);
			}
		};
	},

	error: function(xhr, error) {
		// check for authorization error
		var authError = xhr.status === 401;
		var serverError = xhr.status === 500;

		if ( !authError && fui.log.isError() ) { fui.log.error(error); }

		fui.publish(fui.loader.event.TOPIC, new fui.loader.Event({type: fui.loader.event.FINISH}));

		if ( authError || serverError ) {
			fui.auth.logout(fui.auth.AUTHORIZATION_LOGOUT);
		}
	}
};
