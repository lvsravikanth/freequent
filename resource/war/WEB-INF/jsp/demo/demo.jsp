<div>
<h1>Demo Jsp</h1>

<input type="button" value="Test simpleJson" onClick="javascript: fui.ui.demo.testSimpleJson();">
<button id="testexception" onclick="javascript: fui.ui.demo.testException();">Test Exception</button>
<button id="testauthorization" onclick="javascript: fui.ui.demo.testauthorization();">Test Authorization</button>
<button id="testtemplate">Test Template</button>
<div id="template-container">Click 'Test Template' button to see the template</div>
	<div id="dialog" title="Dialog Title">I'm a dialog</div>
</div>
<div id="testmask">
	click mask to mask me.
	<div style="heigh:100px">what's up?</div>
	click unmask to unmask me.
</div>
<button id="maskbutton">Mask</button>
<button id="unmaskbutton">UnMask</button>

<script type="text/javascript">
    fui.ready(function() {
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
	}
            );
</script>