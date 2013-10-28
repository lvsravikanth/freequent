/**
 * Date utility functions
 */
fui.provide("fui.date");

fui.date = {
	/** number of milliseconds in a minute */
	MS_IN_MIN: 60000,

	/** we need to map incoming Java-formatted date strings to the jquery style used by jQuery. This is a simple
	 * mapping. A more robust one can be found in third party libraries like datejs.
	 */
	formatMap: [ { old: "yy", changed: "y" }, { old: "yyy", changed: "yy" }, { old: "yyyy", changed: "yy" }, { old:"MMMMM", changed:"mm" }, { old:"MMMM", changed:"mm" },{ old:"MMM", changed:"mm" },
		{ old:"MM", changed:"mm" }, { old:"M", changed:"m" }, { old:"EEEEEE", changed:"DD" }, { old:"EEEEE", changed:"DD" }, { old:"EEEE", changed:"DD" },
		{ old:"EEE", changed:"D" }, { old:"dd", changed:"dd" }, { old:"d", changed:"d" }, { old:"nn", changed:"m" }, { old:"jj", changed:"d" }],

	/**
	 * Determines if the specified date is in daylight savings time,
	 * according to the browser's time zone.
	 *
	 * @param date A date, or null to use the current date.
	 */
	isDaylightTime: function(date) {
		if (!date) {
			date = new Date();
		}
		var nowOffset = this.getTimezoneOffsetMs(date);
		return (nowOffset !== this.getStandardTimezoneOffset());
	},

	/**
	 * Determines if the browser's time zone uses daylight savings time.
	 */
	useDaylightTime: function() {
		return (this.getStandardTimezoneOffset() !== this.getDaylightTimezoneOffset());
	},

	/**
	 * Returns the date's time zone offset for the browser's time zone,
	 * in milliseconds.  This is comparable to the Java value.
	 *
	 * @param date A date, or null to use the current date.
	 */
	getTimezoneOffsetMs: function(date) {
		if (!date) {
			date = new Date();
		}
		return date.getTimezoneOffset() * -1 * this.MS_IN_MIN;
	},

	/**
	 * Returns the standard time zone offset for the browser's time zone,
	 * in milliseconds.  This is comparable to the Java value.
	 */
	getStandardTimezoneOffset: function() {
		var date = new Date();
		date.setMonth(0);  // January
		return this.getTimezoneOffsetMs(date);
	},

	/**
	 * Returns the daylight savings time zone offset for the browser's time
	 * zone, in milliseconds.  This is comparable to the Java value.
	 */
	getDaylightTimezoneOffset: function() {
		var date = new Date();
		date.setMonth(6);  // July
		return this.getTimezoneOffsetMs(date);
	},

	/**
	 * Converts Java Date Formats (e.g., mm/dd/yyyy) into
	 * the Unix/PHP format used by ExtJS (e.g., n/j/Y)
	 * @param localeFormat
	 */
	convertJavaDateFormat: function(localeFormat) {
		if (!localeFormat) {
			return;
		}
		var pattern = localeFormat;
		//pattern = pattern.replace(/d/g, 'j');
		//pattern = pattern.replace(/M/g, 'n');
		var formatMap = fui.date.formatMap;
		for ( var i=0; i < formatMap.length; i++ ) {
			if ( pattern.indexOf(formatMap[i].old) != -1) {
				pattern = pattern.replace(formatMap[i].old,formatMap[i].changed);
			}
		}

		return pattern;
	}
};