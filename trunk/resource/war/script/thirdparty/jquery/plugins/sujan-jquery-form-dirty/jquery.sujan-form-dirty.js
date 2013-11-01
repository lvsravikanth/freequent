/*!
 * jQuery Plugin: Dirty Form Detection
 * 
 * Copyright (c) 2012-2013, Sujan Kumar Suppala
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * Author:   mailmrsujan@gmail.cm
 * Version:  1.0
 * Date:     1st Nov 2013
 */
(function($) {
  $.fn.checkDirty = function(options) {
    var settings = $.extend(
          {
            'dirtyClass' : 'dirty',
            'change' : null,
            'fieldSelector' : "select,textarea,input[type='text'],input[type='password'],input[type='checkbox'],input[type='radio'],input[type='hidden'],input[type='file']"
          }, options);

    var getValue = function($field, originalValue) {
      if ($field.hasClass('cd-ignore')
          || $field.hasClass('cdIgnore')
          || $field.attr('data-cd-ignore')
          || $field.attr('name') === undefined) {
        return null;
      }

      if ($field.is(':disabled')) {
        return 'cd-disabled';
      }

      var val;
      var type = $field.attr('type');
      if ($field.is('select')) {
        type = 'select';
      }

      switch (type) {
        case 'checkbox':
        case 'radio':
          val = originalValue ? $field.prop('defaultChecked') : $field.is(':checked');
          break;
        case 'select':
          val = '';
          $field.children('option').each(function(o) {
            var $option = $(this);
			if (originalValue) {
				if ($option.prop('defaultSelected')) {
				  val += $option.val();
				}
			} else {
				if ($option.is(':selected')) {
				  val += $option.val();
				}
			}
            
          });
          break;
		 case 'textarea':
			var val = originalValue ? $field.prop('defaultValue') : $field.val();
			val = String(val.replace(/\r\n/g, "\n").replace(/\r/g, "\n"))
			break;
        default:
          val = originalValue ? $field.prop('defaultValue') : $field.val();
      }

      return val;
    };
	
	var storeOrigValue = function() {
      var $field = $(this);
      $field.data('cd-orig', getValue($field));
    };

    var checkForm = function($form) {
      var isFieldDirty = function($field) {
        return (getValue($field) != getValue($field, true /*originalValue*/));
      };

      var isDirty = false;

      if (!isDirty) {
        $form.find(settings.fieldSelector).each(function() {
          $field = $(this);
          if (isFieldDirty($field)) {
            isDirty = true;
            return false; // break
          }
        });
      }

      markDirty($form, isDirty);
	  return isDirty;
    };
    
    var markDirty = function($form, isDirty) {
      var changed = isDirty != $form.hasClass(settings.dirtyClass);
      $form.toggleClass(settings.dirtyClass, isDirty);     
    };

    var rescan = function() {
      var $form = $(this);
      var newFields = $form.find(settings.fieldSelector).not("[cd-orig]");
      $(newFields).each(storeOrigValue);
      $(newFields).bind('change keyup', checkForm);
    };
  
      if (!$(this).is('form')) {
        return;
      }
      var $form = $(this);

    $form.bind('reset', function() { markDirty($form, false); });
	checkForm($form);
  
	return  checkForm($form);
  };
  	
})(jQuery);
