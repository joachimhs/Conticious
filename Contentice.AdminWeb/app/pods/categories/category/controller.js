import Ember from 'ember';

export default Ember.Controller.extend({
    sortProperties: ['id'],
    sortedSubcategories: Ember.computed.sort('model.subcategories', 'sortProperties'),

    reloadSubcategories: function() {
        console.log('reloadSubcategories:' + this.get('model.category.id'));

        this.set('model.subcategories', this.store.query('subcategory', {category: this.get('model.category.id')}));
    }
});
