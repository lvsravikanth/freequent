/**
 * Set up the string section and our requirements
 */
fui.provide("fui.string");

fui.string = {
	/**
	 * define default maximum number of character columns before a wrap
	 */
	// quite generous, for conservative wrapping
	MAX_SOFT_WRAP_COLUMNS: 18,

	/**
	 * define SOFT_WRAP_HYPHEN default (what character to insert) as a static property of the class fui.string
	 */
	// use soft-hyphen for IE and Opera which are known to implement it correctly;
	SOFT_WRAP_HYPHEN: (navigator.userAgent.toLowerCase().indexOf("applewebkit") > -1 || document.all) ? "&shy;" : "<wbr/>",

	// Allowed data types into which string value can be parsed
	DATA_TYPE_KEY_STR: "STR",
	DATA_TYPE_KEY_INT: "INT",
	DATA_TYPE_KEY_DEC: "DEC",
	DATA_TYPE_KEY_DATE: "DATE",

	softWrap: function(str, _maxcolumns, _strSoftHyphen) {
		// optional 3rd argument allows passing in a strSoftHyphen string, other
		// otherwise the class property is used
		var strSoftHyphen = _strSoftHyphen || this.SOFT_WRAP_HYPHEN;
		var wrapstr = "";
		var charCount = 0;
		var maxcolumns = _maxcolumns || this.MAX_SOFT_WRAP_COLUMNS;
		var wordspaceRe = new RegExp('^\\w{1,' + maxcolumns + '}\\s+');
		var punctuationRe = new RegExp('^[!\\._\\-\\\\,=\\*]{1,' + maxcolumns + '}');
		while(str.length) {
			var endidx = 1;
			// shortcut if there's less remaining characters than the maxcols
			if(str.length < maxcolumns) {
				wrapstr += str;
				break;
			}
			// look ahead for space characters
			var spaceMatches = str.match(wordspaceRe);
			if(spaceMatches && spaceMatches[0]) {
				endidx = spaceMatches[0].length;
				wrapstr += str.substring(0, endidx);
				str = str.substring(endidx);
				charCount = 0; // reset
				continue;
			} else {
				// handle markup
				if(str.charAt(0) == "<" && str.indexOf(">") > -1) {
					endidx = str.indexOf(">");
					charCount++; // count as one character
				} else if( str.charAt(0) == "&" && str.match(/^&\w+;/) ) { // handle entities
					endidx = (str.indexOf(";") > -1) ? str.indexOf(";") +1 : str.length;
					charCount++; // count as one character
				} else {
					var puncMatches = str.match(punctuationRe);
					if(puncMatches && puncMatches[0]) {
						// handle punctuation
						endidx = puncMatches[0].length;
						charCount += endidx; // count as one character
					} else {
						charCount++; // default case is just one character
					}
				}
			}
			wrapstr += str.substring(0, endidx);

			if(charCount >= maxcolumns) {
				wrapstr += strSoftHyphen;
				charCount = 0;
			}
			str = str.substring(endidx);
		}

		return wrapstr;
	},

    toBoolean: function(str) {
		if ( !str ) {
			return false;
		}
		
		var val = str.toString();
        return (val.length && val.match(/true|1|on/i)) ? true: false;
    },

	/**
	 * "text" can contain parameters of the type %{type} or {0}, {1} etc;
	 * In the first case "%{type}", data is expected to be an object containing the key
	 * "type". In this case, %{type} is replaced with the value of the key "type" in data.
	 * In the second case, when text contains parameters of the type {0}, {1} etc;, data is
	 * expected to be an array and the first token {0} is replaced with data[0],
	 * second token {1} is replaced with data[1] etc;
	 *
	 * @param text in one of the formats mentioned above
	 * @param data either containing an object or an array
	 */
	replace: function(text, data) {
		var result = text;
		var re;
		if (fui.query.isArray(data)) {
			for (var i in data) {
				re = new RegExp("\\{" + i + "\\}", "g");
				result = result.replace(re, data[i]);
			}
		} else {
			for (var key in data) {
				re = new RegExp("\\%\\{" + key + "\\}", "g");
				result = result.replace(re, data[key]);
			}
		}
		return result;
	},

	/**
	 * Replaces line breaks with a specified string.
	 * 
	 * @param value The text in which to convert.
	 * @param replaceStr The converted text.
	 */
	replaceEscapedBreaks: function(value, replaceStr) {
		return (value ? value.replace(/[\n\r]+/g, replaceStr) : value);
	},

	startsWith: function(str, start) {
		return str.indexOf(start) === 0;
	},
	
	endsWith: function(str, end) {
		return str.lastIndexOf(end) == (str.length - end.length);
	},

	/**
	 * Utility function to transform the value to the given type of object.
	 *
	 * @param dataType, the datatype into which the value has to be parsed
	 * @param valueStr, the string form of the object to be parsed
	 * @param locale, optional parameter to specify the locale. If not provided casts value based on default locale
	 */
	castToType: function(dataType, valueStr, locale) {
		var v = fui.string;

		switch(dataType) {
			case v.DATA_TYPE_KEY_DATE:
				var dateSelector = locale ? {selector: "date", locale: locale} : {selector: "date"};

				// If the entered value is date, return time
				var date = '';//vuit.date.locale.parse(valueStr, dateSelector); //todo
				if(date) {
					return date;
				}

				break;
			case v.DATA_TYPE_KEY_INT:
			case v.DATA_TYPE_KEY_DEC:
				var numberSelector = locale ? {selector: "decimal", locale: locale} : {selector: "decimal"};

				// If the entered value is number, return number
				var numberObj = '';//vuit.number.parse(valueStr, numberSelector); //todo
				if(!isNaN(numberObj)) {
					return numberObj;
				}

				// Try to evaluate to number
				try {
					return eval(valueStr);
				} catch(ex) {
					// The valueStr is not an expression proceed for further processing
				}
		}

		return valueStr;
	}
};
