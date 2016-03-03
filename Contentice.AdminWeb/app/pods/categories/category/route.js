import Ember from 'ember';

export default Ember.Route.extend({

    model: function(params) {
        console.log('MODEL:::');
        console.log(params);

        return Ember.RSVP.hash({
            category: this.store.find('category', params.category_id),
            subcategories: this.store.query('subcategory', {category: params.category_id})
        });

    }
});
