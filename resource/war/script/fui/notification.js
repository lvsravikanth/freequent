/**
 * Set up the notification section and our requirements
 */
fui.provide("fui.notification");

fui.notification = {
	/**
	 * Notification types.
	 */
	MESSAGE :"fui-notification-message",
	WARNING :"fui-notification-warning",
	ERROR :"fui-notification-error",

	/**
	 * Padding.
	 */
	VERTICAL_PADDING: 5,
	HORIZONTAL_PADDING: 10,

	/**
	 * Show a normal message.
	 *
	 * @param msg the message to show
	 * @param autoClose the autoclose time; if not set, it be set to 5000
	 *     milliseconds
	 */
	message : function(msg, autoClose) {
		autoClose = autoClose || 5000;  // if not set, default to 5 seconds
		return fui.notification.show(fui.notification.MESSAGE, msg, autoClose);
	},

	/**
	 * Show a warning message.
	 *
	 * @param msg the message to show
	 * @param autoClose the autoclose time; can be null
	 */
	warning : function(msg, autoClose) {
		return fui.notification.show(fui.notification.WARNING, msg, autoClose);
	},

	/**
	 * Show an error message.
	 *
	 * @param msg the message to show
	 * @param autoClose the autoclose time; can be null
	 */
	error : function(msg, autoClose) {
		return fui.notification.show(fui.notification.ERROR, msg, autoClose);
	},

	/**
	 * Show a notification using the given class and message.
	 *
	 * @param cls the notification class
	 * @param msg the message to show
	 * @param autoClose the autoclose time; can be null
	 */
	show: function(cls, msg, autoClose) {
        var config = {text: msg, layout: 'top'};
        if (autoClose) {
            config.timeout = autoClose;
        }
        if (fui.notification.MESSAGE === cls) {
            config.type = 'success';
        } else if (fui.notification.WARNING === cls) {
            config.type = 'warning';
        } else if (fui.notification.ERROR === cls) {
            config.type = 'error';
        }

        noty(config);
	}
};

fui.notification.internal = {
	windows: [],

	clear: function(win) {
		fui.query.noty.closeAll();
	}
};
