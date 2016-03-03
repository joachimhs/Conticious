Conticious.SubcategoryFieldsController = Ember.ObjectController.extend({
    needs: ['category', 'subcategory', 'categories'],

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
                    if (self.get('controllers.categories')) {
                        self.get('controllers.categories').reloadCategories();
                    }
                },
                dataType: "json"
            });
        }
    }
});