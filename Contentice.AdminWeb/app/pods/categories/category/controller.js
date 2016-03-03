import Ember from 'ember';

export default Ember.Controller.extend({
    sortProperties: ['id'],
    sortedSubcategories: Ember.computed.sort('model.subcategories', 'sortProperties')
});
