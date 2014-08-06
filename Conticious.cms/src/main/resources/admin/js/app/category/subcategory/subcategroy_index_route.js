Conticious.SubcategoryIndexRoute = Ember.Route.extend({
    model: function() {
        var subcategory = this.modelFor('subcategory');
        return subcategory;
    }
});