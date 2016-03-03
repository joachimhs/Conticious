import Ember from 'ember';

export default Ember.Controller.extend({
    categoryController: Ember.inject.controller('categories/category'),
    categoriesController: Ember.inject.controller('categories'),
    subcategoryController: Ember.inject.controller('categories/category/subcategory'),

    sortColumn: ['name:asc'],
    sortedFields: Ember.computed.sort('subcategoryController.model.fields', 'sortColumn'),

    actions: {
        addSubcategoryToRelation: function(addToSubcategoryId, addToSubcategoryFieldId) {
            var self = this;

            var subcategoryId = this.get('controllers.subcategory.model.id');
            var url = "/json/admin/addSubcategoryToRelation/" + subcategoryId + "/" + addToSubcategoryId + "_" + addToSubcategoryFieldId;

            console.log("POSTING to: " + url);

            $.ajax({
                type: "POST",
                url: url,
                data: "",
                success: function() {
                    //self.transitionToRoute("categories");
                    //self.reloadCategories();
                    console.log("POSTED");
                    if (self.get('categoriesController')) {
                        self.get('categoriesController').reloadCategories();
                    }
                },
                dataType: "json"
            });
        }
    },

    enrichedFields: function() {
        var fields = this.get('sortedFields');
        var self = this;

        if (fields) {
            fields.forEach(function (field) {
                if (field.get('isToMany')) {
                    self.store.query('subcategory', {category: field.get('relation.id')}).then(function(data) {
                        field.set('allRelations', data);
                    });

                }

                if (field.get('isToOne')) {
                    self.store.query('subcategory', {category: field.get('relation.id')}).then(function(data) {
                        field.set('allRelations', data);
                    });
                }
            });
        }

        return fields;
    }.property('sortedFields.@each.id')
});
