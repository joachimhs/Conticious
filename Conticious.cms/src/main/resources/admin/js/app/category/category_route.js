Conticious.CategoryRoute = Ember.Route.extend({
    model: function(category) {
        return this.store.find('category', category.category_id);
    }
});