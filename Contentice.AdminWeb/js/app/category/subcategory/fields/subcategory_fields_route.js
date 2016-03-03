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
        var self = this;
        var subcategory = this.modelFor('subcategory');

        var cfs = Conticious.__container__.lookup('store:main').all('categoryField');
        var toManys = cfs.filter(function(cf) { if (cf.get('type') === 'toMany') { return true; } });

        var subcategoryType = subcategory.get('id').split("_");
        if (subcategoryType.length > 0) {
            subcategoryType = subcategoryType[0];
        }

        var relations = [];

        toManys.forEach(function(cf) {
            if (cf.get('relation') === subcategoryType) {
                var catId = null;
                var catField = null;

                var idParts = cf.get('id').split("_");
                if (idParts.length >= 2) {
                    catId = idParts[0];
                    catField = idParts[1];
                }

                if (catId && catField) {
                    console.log('Related to category: ' + catId + " through field: " + catField);

                    var relatedCategory = self.store.peekRecord("category", catId);

                    console.log("category: " + relatedCategory.get('id'));
                    console.log(relatedCategory);

                    var relatedSubcategories = relatedCategory.get('subcategories');

                    console.log("subcategories: " + relatedSubcategories);

                    relations.pushObject(Ember.Object.create({
                        relatedToCategory: catId,
                        throughField: catField,
                        relatedSubcategories: relatedSubcategories
                    }));
                }
            }
        });

        subcategory.set('relatedCategoryFields', relations);

        return subcategory;
    }
});