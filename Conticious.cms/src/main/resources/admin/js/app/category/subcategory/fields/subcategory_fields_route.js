Conticious.SubcategoryFieldsRoute = Ember.Route.extend({
    actions: {
        saveSubcategoryField: function(field) {
            field.save();
        },

        revertSubcategoryField: function(field) {
            field.rollback();
        }
    },

    model: function() {
        var subcategory = this.modelFor('subcategory');
        return subcategory;
    }
});