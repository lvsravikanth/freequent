/**
 * Set up the locale section and our requirements
 */
fui.provide("fui.locale");

fui.locale = {
	/**
	 * Returns the current locale.
	 */
	get: function() {
		return fui.locale.internal.locale;
	},
	
	/**
	 * Sets the current locale.
	 * 
	 * @param locale the new locale
	 */
	set: function(locale) {
		// Normalize the locale
		if ( locale ) {
			locale.replace(/-/g, '_');
		}
		
		var l = fui.locale;
		
		// Set internal
		l.internal.locale = locale; 

		// Publish the event 
		fui.publish(l.event.LOCALE_TOPIC, new l.Event({locale: locale}));
	}
};

/**
 * Locale Events
 */
fui.provide("fui.locale.event");

fui.locale.event = {
	// Topic
	LOCALE_TOPIC: "/vui/locale/event",

	// Events
	CHANGE: "change",

	/**
	 * Subscribe to locale events
	 *
	 * @param handler the event handler
	 */
	subscribe: function(handler) {
		fui.subscribe(fui.locale.event.LOCALE_TOPIC, handler);
	},

	/**
	 * Unsubscribe from locale events
	 *
	 * @param handler the event handler
	 */
	unsubscribe: function(handler) {
		fui.unsubscribe(fui.locale.event.LOCALE_TOPIC, handler);
	}
};

fui.locale.Event = function() {
	if ( (arguments.length == 1) && (typeof arguments[0] == "object") ) {
		fui.combine(this, arguments[0]);
	}
};

fui.extend(fui.locale.Event, {
	locale: ""
});

/**
 * Set up the locale internal section and our requirements
 */
fui.provide("fui.locale.internal");

fui.locale.internal = {
	locale: ""
};
