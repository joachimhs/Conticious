Conticious.CategoryIndexRoute = Ember.Route.extend({
    actions: {

        addNewField: function() {
            var category = this.modelFor('category');
            var newFieldName = this.get('controller.newFieldName');
            var newFieldType = this.get('controller.newFieldType');
            var newFieldRequired = this.get('controller.newFieldRequired');

            if (newFieldName) {
                var newField = this.store.createRecord('categoryField', {
                    id: category.get('id') + "_" + newFieldName,
                    name: newFieldName,
                    type: newFieldType,
                    required: newFieldRequired
                });

                category.get('defaultFields').pushObject(newField);

                newField.save().then(function(data) {
                    console.log('new field saved. saving category');
                    category.save();
                });
            }

            this.set('controller.newFieldName', null);
            this.set('controller.newFieldType', null);
            this.set('controller.showNewFieldArea', false);
        },

        addNewSubcategory: function() {
            var category = this.modelFor('category');
            var newSubcategoryName = this.get('controller.newSubcategoryName');

            if (newSubcategoryName) {
                var newSubcategory = this.store.createRecord('subcategory', {
                    id: category.get('id') + "_" + newSubcategoryName,
                    name: newSubcategoryName
                });

                category.get('subcategories').pushObject(newSubcategory);

                newSubcategory.save().then(function(data) {
                    console.log('newSubcategory saved. Saving category');
                    category.save().then(function(data) {
                        category.reload();
                        newSubcategory.reload();
                    });
                });

            }

            this.set('controller.newSubcategoryName', null);
            this.set('controller.showNewSubcategoryArea', false);
        }
    }
});