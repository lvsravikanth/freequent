/**
 * Set up the auth section and our requirements
 */
fui.provide("fui.auth");

fui.require("fui.request");
fui.require("fui.io");

fui.auth = {
	/**
	 * The action key
	 */
	ACTION_KEY: "auth",

	/**
	 * Methods
	 */
	LOGIN: "login",
	LOGOUT: "logout",
	LOGINSCREEN: "loginscreen",

	/**
	 * Logout types
	 */
	LOGOUT_TYPE: 'logout.type',
	USER_LOGOUT: 'user',
	AUTHORIZATION_LOGOUT: 'authorization',

	/**
	 * Login screen
	 */
	LOGIN_SCREEN_PATH:"/auth/login.json",

	/**
	 * Log the user into the application.
	 *
	 * @param user the user
	 * @param pass the password
	 * @param requestData the request data
	 * @param skipThemeUpdate skip theme updating
	 */
	login : function(user, pass, requestData, skipThemeUpdate) {
		if ( fui.log.isDebug() ) { fui.log.debug("Logging in"); }

		requestData = requestData || {};

		requestData.ajaxMethod = 'POST';

		requestData.actionKey = fui.auth.ACTION_KEY;
		requestData.method = fui.auth.LOGIN;

		requestData.content = requestData.content || {};
		requestData.content.user = user;
		requestData.content.password= pass;

		if ( skipThemeUpdate ) {
			requestData.content.skipThemeUpdate = true;
		}

		var handler = requestData.handler;
		requestData.handler = function(data) {
			data = fui.secure_eval(data);
			fui.auth.internal.TOKEN = data.token;

			// Update theme as needed
			if ( !skipThemeUpdate ) {
				//fui.theme.update(data);
			}

			if ( handler ) {
				handler(data);
			}

			// Publish event
			fui.publish(fui.auth.event.TOPIC, new fui.auth.Event({type: fui.auth.event.LOGIN}));
		};

		var request = fui.request.build(requestData);
		fui.io.api(request);
	},

	/**
	 * Log out of the application.
	 *
	 * @param type the logout type
	 * @param requestData the request data
	 */
	logout: function(type, requestData) {
		if ( fui.log.isDebug() ) { fui.log.debug("Logging out"); }

		requestData = requestData || {};

		requestData.actionKey = fui.auth.ACTION_KEY;
		requestData.method = fui.auth.LOGOUT;

		var handler = requestData.handler;
		requestData.handler = function(data) {
			// Publish event
			fui.publish(fui.auth.event.TOPIC, new fui.auth.Event({type: fui.auth.event.LOGOUT}));

			fui.auth.invalidate();

			if ( handler ) {
				handler(data);
			}

			fui.loader.clearHash();

			var params = {};
			params[fui.auth.LOGOUT_TYPE] = type || fui.auth.USER_LOGOUT;

			fui.loader.loadBody(fui.auth.LOGIN_SCREEN_PATH, params);
		};

		var request = fui.request.build(requestData);
		fui.io.api(request);
	},

	/**
	 * Is the current user valid.
	 */
	isValid : function() {
		return ( null !== fui.auth.internal.TOKEN );
	},

	/**
	 * Invalidate the user authentication.
	 */
	invalidate : function() {
		fui.auth.internal.TOKEN = null;

		// Publish event
		fui.publish(fui.auth.event.TOPIC, new fui.auth.Event({type: fui.auth.event.INVALIDATE}));
	},

	/**
	 * Get the authentication token.
	 */
	get : function() {
		return fui.auth.internal.TOKEN;
	},

	getHeaders : function() {
		// TODO return OAUTH headers
		// return { foo: 'bar', top: 'hat' };
		return null;
	}
};

/**
 * Auth Events
 */
fui.provide("fui.auth.event");

fui.auth.event = {
	// Topic
	TOPIC: "/vui/auth/event",

	// Activity events
	LOGIN: "LOGIN",
	LOGOUT: "LOGOUT",
	INVALIDATE: "INVALIDATE",

	/**
	 * Subscribe to auth events.
	 *
	 * @param handler the event handler
	 */
	subscribe: function(handler) {
		fui.subscribe(fui.auth.event.TOPIC, handler);
	},

	/**
	 * Unsubscribe from auth events.
	 *
	 * @param handler the event handler
	 */
	unsubscribe: function(handler) {
		fui.unsubscribe(fui.auth.event.TOPIC, handler);
	}
};

fui.auth.Event = function() {
	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}
};

fui.extend(fui.auth.Event, {
	type: ""
});

/**
 * Set up the private auth section and our requirements
 */
fui.provide("fui.auth.internal");

fui.auth.internal = {
	TOKEN : null
};
