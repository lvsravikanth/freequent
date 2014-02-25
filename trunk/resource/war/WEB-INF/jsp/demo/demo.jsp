<div>
<h1>Demo Jsp</h1>

<input type="button" value="Test simpleJson" onClick="javascript: fui.ui.demo.testSimpleJson();">
<button id="testexception" onclick="javascript: fui.ui.demo.testException();">Test Exception</button>
<button id="testauthorization" onclick="javascript: fui.ui.demo.testauthorization();">Test Authorization</button>
<button id="testtemplate">Test Template</button>
<div id="template-container">Click 'Test Template' button to see the template</div>
<div id="dialog" title="Dialog Title">
	I'm a dialog
	<hr/>
	Text: <input type="text" name="name" id="name"/>
	Select2:
	<select id="groupData">
		<option>Group1</option>
		<option>Group2</option>
		<option>Group3</option>
	</select>

</div>
<button id="opener">Click to Open The Dialog</button>
<button id="viewAndPrint">Click to View and Print</button>
<div id="viewAndPrintDialog" title="View">
</div>
<div id="testmask">
	click mask to mask me.
	<div style="heigh:100px">what's up?</div>
	click unmask to unmask me.
</div>
<button id="maskbutton">Mask</button>
<button id="unmaskbutton">UnMask</button>
<hr/>
<form action="#" id="validatortestform">
	input required: <input type="text" id="userid" name="userid"/>

</form><button id="validatetestbtn">Validate</button>
</div>
<script type="text/javascript">
    fui.ready(function() {
        fui.query('#groupData').select2({dropdownCssClass:"ui-dialog"});
        fui.query('#testexception').button();
        fui.query('#testauthorization').button();
        fui.query('#testtemplate').button()
        .click(function( event ) {
            var handler= function(data) {
                fui.html.set('template-container', data);
            };
            fui.ui.demo.testtemplate(handler);
      });

		fui.query("#maskbutton").button()
		.click(function(){
			fui.query("#testmask").mask("mask label");
		});
		fui.query("#unmaskbutton").button()
		.click(function(){
			fui.query("#testmask").unmask();
		});
	fui.query('#opener').button()
		.click(function() {
			//show the dialog
	  		fui.query( "#dialog" ).dialog( "open" );
		});
	//initialize
		fui.query( "#dialog" ).dialog(
			{
				autoOpen: false,
				modal: true,
				height: 'auto',
				width: 650,
				close: function( event, ui ) {alert('in close');},
				buttons: {
				"Create an account": function() {
				  alert ('in create an account');
				},
				Cancel: function() {
				  fui.query( this ).dialog( "close" );
				}
			  }
			}
		);

		var validator = fui.query('#validatortestform').validate(
			{
				rules: {
						userid: "required"
						}
			});

		fui.query('#validatetestbtn').button()
		.click(function(){
			fui.query('#validatortestform').valid();
		});

		//
		//initialize
		fui.query( "#viewAndPrintDialog" ).dialog(
			{
				autoOpen: false,
				modal: true,
				height: 'auto',
				width: 650,
				close: function( event, ui ) {alert('in close');},
				buttons: {
				"Create an account": function() {
				  alert ('in create an account');
				},
				Cancel: function() {
				  fui.query( this ).dialog( "close" );
				  fui.query( this ).empty();
				  //fui.query( this ).dialog( "destroy" );
				}
			  }
			}
		);
		fui.query('#viewAndPrint').button()
		.click(function() {
			//show the dialog
	  		fui.query( "#viewAndPrintDialog" ).dialog( "open" );
			//fui.query( "#viewAndPrintDialog" ).append('<iframe src="/freequent/common/test.pdf" id="printme" height="100%" width="100%"></iframe>');
			fui.query( "#viewAndPrintDialog" ).append('<iframe src="/freequent/report/getreport?reportname=demo" id="objAdobePrint" name="objAdobePrint" height="95%" width="100%" frameborder=0></iframe>');
			
			//fui.query( "#viewAndPrintDialog" ).append('<object id="printme" data="/freequent/common/test.pdf#navpanes=1&amp;toolbar=1&amp;statusbar=0" type="application/pdf" width="100%" height="100%"></object>');
			//fui.query("#printme").attr('src', "/freequent/manageusers");
			fui.query("#printme").focus();
			document.getElementById("printme").contentWindow.print();
		});
	}
            );
</script>