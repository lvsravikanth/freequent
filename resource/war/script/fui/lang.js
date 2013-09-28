fui.provide("fui.lang");

// Some additional lang utils
fui.lang = {
	isEmptyObject: function(obj) {
		var empty = true;

		var test = {};
		for ( x in obj ) {
			if ( typeof test[x] == "undefined" ) {
				empty = false;
				break;
			}
		}

		return empty;
	},

	combine: function(/*Object*/ leftObj, /*Object*/ rightObj, /* Object */kwArgs) {
		// summary:
		//		Adds properties and methods of props to obj.
		//    based on dojo.lang.mixin, which says:
		//    This addition is "prototype extension safe", so that instances of objects will not
		//		pass along prototype defaults.

		// leftObj: object to merge into
		// rightObj: object to merge from
		// options: e.g. {
		//  precedent: 'left'|'right',
		//  combineAs: 'intersection'|'union' (only merge existing keys, or combine keys)
		//  clone: true|false (return copy or leftObj)
		// }
    
		// NB: this is a *shallow* merge. Any nested object/array properties will be copied by reference

		// mixin args to default options
		var options = fui.combine({
			precedent: 'right',  // or left
			combineAs: 'union',  // or intersection
			clone: false
		}, kwArgs || {});

		var targetObj = (options.clone) ? {} : leftObj;

		// if we don't have a target or a right object there is nothing to do
		if ( !targetObj || !rightObj ) {
			return targetObj;
		}

		var referenceObj = {}; // template object, we add each key as we go to avoid doing the same work twice
    
		var isRightPrecedent = (options.precedent.toLowerCase() == "right");
		var isIntersection = (options.combineAs.toLowerCase() == "intersection");
    
		function conditionalCopy(key) {
			if(typeof leftObj[key] != "undefined") {
				// key exists in both objects
				targetObj[key] = (isRightPrecedent) ? rightObj[key] : leftObj[key];
				// dont visit this key again
				referenceObj[key] = 1;
			} else if(!isIntersection) {
				// key exists only on the right
				targetObj[key] = rightObj[key];
				referenceObj[key] = 1;
			}
		}

		for (var x in rightObj) {
			// the "tobj" condition avoid copying properties in "props"
			// inherited from Object.prototype.  For example, if obj has a custom
			// toString() method, don't overwrite it with the toString() method
			// that props inherited from Object.protoype
			if (typeof referenceObj[x] == "undefined") {
				conditionalCopy(x);
			}
		}

		// Do cloning only work
		if (options.clone) {
			// IE doesn't recognize custom toStrings in for..in
			if ( fui.isIE() ) {
				var toStringVal = (isRightPrecedent) ? leftObj.toString || rightObj.toString : rightObj.toString || isleftObj.toString;
				if (typeof toStringVal == "function") {
					targetObj.toString = toStringVal;
				}
			}

			// pick up any stragglers from leftObj
			if(!isIntersection) {
				for (var y in leftObj) {
					if (typeof referenceObj[x] == "undefined") {
						targetObj[y] = leftObj[y];
					}
				}
			}
		}

		return targetObj;
		// Object
	}
};
