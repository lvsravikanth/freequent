var freequent = {

};

freequent.util ={

};

freequent.util.errorinfoutil = {

	ERRORS_ID : 'frq-error-msg',
	INFOS_ID : 'frq-info-msg',
	WARNINGS_ID : 'frq-warning-msg',
	errors : [],
	warnings : [],
	infos : [],

	/**
	 * shows errors on the page 
	 *
	 * @param errorObj the errorObj is of type String or Object
	 */
	showerrors : function( errorObj ) {
		id = this.ERRORS_ID
		this.clearerrors();
		if(typeof errorObj == 'string' || errorObj instanceof String) {
			this.adderror(errorObj);
		} else {
			for (var i=0; i < errorObj.messages.length ; i++) {
				this.adderror(errorObj.messages[i].msg);
			}
		}
		if (errors && errors.length > 0 ) {
			$(id).remove();
			this.createNode(id, errors);
			$("#"+ id +"-wrap").addClass("ui-state-error");
			$("#"+ id +"-wrap  > .ui-icon").addClass("ui-icon-alert");
		}
	},

	/**
	 * shows warnings messssages on the page
	 *
	 * @param warningObj the warningObj is of type String or Object
	 */
	showwarnings: function( warningObj ) {
		id = this.WARNINGS_ID;
		this.clearwarnings();
		if(warningObj) {
			var msgJsonArray = warningObj.messages;
			for (var i=0; i < msgJsonArray.length ; i++) {
				this.addwarning(msgJsonArray[i]);
			}
		}
		if ( warnings && warnings.length > 0) {
			$(id).remove();
			this.createNode(id, warnings);
			$("#"+ id +"-wrap").addClass(".ui-state-highlight");
			$("#"+ id +"-wrap  > .ui-icon").addClass("ui-icon-info");
		}
	},

	/**
	 * shows info messssages on the page
	 *
	 * @param infoObj the infoObj is of type String or Object
	 */
	showinfos: function( infoObj ) {
		id = this.INFOS_ID;
		this.clearinfos();
		if(infoObj) {
			var msgJsonArray = infoObj.messages;
			for (var i=0; i < msgJsonArray.length ; i++) {
				this.addinfo(msgJsonArray[i]);
			}
		}
		if (infos && infos.length > 0) {
			$(id).remove();
			this.createNode(id, infos);
			$("#"+ id +"-wrap").addClass("ui-state-error");
			$("#"+ id +"-wrap  > .ui-icon").addClass("ui-icon-info");
		}
	},

	/**
	 * Add errors  
	 *
	 * @param error Add error messages into errors arrays.
	 */
	adderror: function(error) {
		errors.push(error);
	},

	/**
	 * Add warnings
	 *
	 * @param warning Add warning messages into warning arrays.
	 */
	addwarning: function(warning) {
		warnings.push(warning);
	},

	/**
	 * Add infos
	 *
	 * @param infos Add inof messages into infos arrays.
	 */
	addinfo: function(info) {
		infos.push(info);
	},

	/**
	 * Clear errors clear all existing error messages
	 *
	 */
	clearerrors: function() {
		errors = [];
	},

	/**
	 * Clear warnings clear all existing warning messages
	 *
	 */
	clearwarnings: function() {
		warning = [];
	},

	/**
	 * Clear infos clear all existing info messages
	 *
	 */
	clearinfos: function() {
		infos = [];
	},

	/**
	 * Clear errors clear all existing error messages
	 *
	 */
	clearall: function() {
		errors = [];
		warnings = [];
		infos = [];
	},

	/**
	 * Create messages
	 *
	 * @param id
	 * @param msg
	 */
	createNode : function(id, msg) {
		$('<div class="ui-widget"><div id=\"'+ id +'-wrap" class="ui-corner-all" style="padding: 0 .3em ;"></div></div>').appendTo('#' + id);
			$('<span class="ui-icon" style="float: left; margin-right: .3em; background-position: 3px -140px;"></span>').appendTo("#"+ id +"-wrap");
			for (var j=0; j < msg.length ; j++) {
				$('<li style="list-style-position: inside; margin-left: 22px;"></li>').appendTo("#"+ id +"-wrap");
				var list = $("#"+ id +"-wrap > li");
				$(list[j]).text(msg[j]);
			}
	}
};