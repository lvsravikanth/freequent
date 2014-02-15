/**
 * Set up the type section and our requirements
 */
fui.provide("fui.ui.type");

fui.ui.type = {
    /**
	 * Constant used to identify DEMO Type.
	 */
	DEMO: 'demo',

    MANAGEUSERS: 'manageusers',
    MANAGEITEMS: 'manageitems',
    MANAGEGROUPS: 'managegroups',


	USER: 'user',
	ITEM: "item",
    GROUP: "group",
	ORDER: "order",
    CATEGORY: "category",
    UNIT: "unit",

	getActionKey: function (type) {
		if (type === fui.ui.type.USER) {
			return fui.ui.manageusers.ACTION_KEY;
		} else if (type === fui.ui.type.ITEM) {
			return fui.ui.manageitems.ACTION_KEY;
		} else if (type === fui.ui.type.GROUP) {
            return fui.ui.managegroups.ACTION_KEY;
        } else if (type === fui.ui.type.ORDER) {
			return fui.ui.manageorders.ACTION_KEY;
		}  else if (type === fui.ui.type.CATEGORY) {
			return fui.ui.managecategories.ACTION_KEY;
		}  else if (type === fui.ui.type.UNIT) {
			return fui.ui.manageunits.ACTION_KEY;
		}

		return null;
	}
};