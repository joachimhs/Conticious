import Ember from "ember";

export default Ember.Component.extend({
    content: null,
    sortKeys: ['id:asc'],

    optionValuePath: null,
    optionLabelPath: null,

    sortedContent: Ember.computed.sort('content', function(a, b) {
        if (typeof a.get === 'function' && typeof b.get === 'function') {
            return a.get('id').localeCompare(b.get('id'));
        } else {
            return a > b;
        }
    }),
    selectedValue: null,

    init(attrs) {
        this._super(...arguments);
        this.populateSelect();
    },

    contentObserver: function() {
        this.populateSelect();
    }.observes('content.length', 'optionValuePath', 'optionLabelPath').on('init'),

    populateSelect: function() {
        var content = this.get('content');

        if (!content) {
            content = Ember.A();
            this.set('content', content);
        }

        var optionLabelPath = this.get('optionLabelPath');
        if (optionLabelPath && optionLabelPath.indexOf('content.') > -1) {
            optionLabelPath = optionLabelPath.substring(8);
        }
        var optionValuePath = this.get('optionValuePath');
        if (optionValuePath && optionValuePath.indexOf('content.') > -1) {
            optionValuePath = optionValuePath.substring(8);
        }

        content.forEach(function(item) {
            if (optionLabelPath && typeof item.get === 'function' && item.get(optionLabelPath)) {
                item.set('label', item.get(optionLabelPath));
            }
            if (optionValuePath && typeof item.get === 'function' && item.get(optionValuePath)) {
                item.set('value', item.get(optionValuePath));
            }

        });

        this.set('content', content);
    },

    actions: {
        change() {
            console.log('CHANGE');
            const changeAction = this.get('action');
            const selectedEl = this.$('select')[0];
            const selectedIndex = selectedEl.selectedIndex;
            const content = this.get('sortedContent');
            var selectedValue = content[selectedIndex];


            console.log('selectedIndex: ' + selectedIndex);
            console.log('selectedValue: ' + selectedValue);

            if (typeof content.objectAt === 'function') {
                if (typeof content.objectAt(selectedIndex).get === 'function') {
                    selectedValue = content.objectAt(selectedIndex).get('id');
                } else {
                    selectedValue = content.objectAt(selectedIndex);
                }

            }

            if (!selectedValue  && typeof content.objectAt === 'function') {
                selectedValue = content.objectAt(selectedIndex).get('value');
            }

            this.set('selectedValue', selectedValue);
            if (changeAction) {
                changeAction(selectedValue);
            }
        }
    }
});
