var freequent = {

};

freequent.util ={

};

freequent.util.errorinfoutil = {

	errors : [],
	warnings : [],
	infos : [],

	showerrors : function(id, errorObj) {
		this.clearerrors();
		if(errorObj) {
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

	showwarnings: function(id, warningObj) {
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

	showinfos: function(infoObj) {
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

	adderror: function(error) {
		errors.push(error);
	},

	addwarning: function(warning) {
		warnings.push(warning);
	},

	addinfo: function(info) {
		infos.push(info);
	},

	clearerrors: function() {
		errors = [];
	},

	clearwarnings: function() {
		warning = [];
	},

	clearinfos: function() {
		infos = [];
	},

	clearall: function() {

	},

	createNode : function(id ,errors) {
		$('<div class="ui-widget"><div id=\"'+ id +'-wrap" class="ui-corner-all" style="padding: 0 .3em ;"></div></div>').appendTo('#' + id);
			$('<span class="ui-icon" style="float: left; margin-right: .3em; background-position: 3px -140px;"></span>').appendTo("#"+ id +"-wrap");
			for (var j=0; j < errors.length ; j++) {
				$('<li style="list-style-position: inside; margin-left: 22px;"></li>').appendTo("#"+ id +"-wrap");
				var list = $("#"+ id +"-wrap > li");
				$(list[j]).text(errors[j]);
			}
	}
};