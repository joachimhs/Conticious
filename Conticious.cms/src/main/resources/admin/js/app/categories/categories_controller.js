Conticious.CategoriesController = Ember.ArrayController.extend({
    needs: ['category', 'user', "subcategory"],

    showNewCategoryField: false,
    showUploadField: false,
    newCategoryName: null,
    newId: null,

    actions: {
        showNewCategory: function() {
            this.set('showNewCategoryField', true);
        },

        cancelNewCategory: function() {
            this.set('showNewCategoryField', false);
            this.set('newCategoryName', null);
        },

        saveNewCategory: function() {
            var categoryId = this.get('newCategoryName');

            var category = this.store.createRecord('category', {
                id: categoryId
            });

            category.save();

            this.set('showNewCategoryField', false);
            this.set('newCategoryName', null);
        },

        showFileUpload: function() {
            this.set('showUploadField', true);
        },

        cancelFileUpload: function() {
            this.set('showUploadField', false);
        },

        userChanged: function() {
            console.log('user changed. Refreshing categories!');
            this.set('model', this.store.find('category'));
        },

        doEditSubcategory: function() {
            console.log('editing subcategory: ' + this.get('controllers.subcategory.model.id'));
            if (this.get('controllers.subcategory.model')) {
                this.set('controllers.subcategory.model.isEditing', true);

                Ember.run.schedule("afterRender", function() {
                    $("#editSubcategoryArea").hide();
                    $("#editSubcategoryArea").slideDown();
                });
            }
        },

        finishEditSubcategory: function() {
            console.log('finishing subcategory editing... ' + this.get('newId'));
            if (this.get('controllers.subcategory.model')) {

                var controller = this;
                $("#editSubcategoryArea").slideUp(function() {
                    controller.set('controllers.subcategory.model.deleteFirstStep', false);
                    controller.set('controllers.subcategory.model.isEditing', false);
                    controller.set('newId', '');
                });
            }
        },

        deleteSubcategoryFirstStep: function(subcategory) {
            subcategory.set('deleteFirstStep', true);
        },

        deleteSubcategoryConfirm: function(subcategory) {
            subcategory.set('deleteFirstStep', false);
            console.log('DELETING SUBCATEGORY: ' + subcategory.get('id'));
            this.set('controllers.subcategory.model.isEditing', false);
            subcategory.deleteRecord();
            subcategory.save();

            this.transitionToRoute("categories");
            controller.reloadCategories();
        },

        renameSubcategory: function(subcategory) {
            var self = this;
            console.log(this.get('newId'));

            var newId = this.get('controllers.category.model.id') + "_" + this.get('newId');
            var oldId = subcategory.get('id');

            console.log("Renaming " + oldId + " to " + newId);

            var url = "/json/admin/renameSubcategory/" + oldId + "/" + newId;

            $.ajax({
                type: "POST",
                url: url,
                data: "",
                success: function() {
                    self.transitionToRoute("categories");
                    self.reloadCategories();
                },
                dataType: "json"
            });

            /* This is the old way, which needs to handle fields, etc. This is now implemnted with a single
             * call to the server, as renaming is a lot easier there!

            if (newId && this.get('controllers.category.model.id')) {
                var name = newId;
                newId = this.get('controllers.category.model.id') + "_" + newId;
                var newSubcategory = this.store.createRecord('subcategory', {
                    id: newId,
                    name: name,
                    content: subcategory.get('content'),
                    fields: []

                });

                var fieldsToCopy = [];

                subcategory.get('fields').forEach(function(field) {
                    fieldsToCopy.pushObject(field);
                });

                subcategory.deleteRecord();
                subcategory.save();

                console.log('old subcategory deleted, save new subcategory');

                var controller = this;
                newSubcategory.save().then(function(d) {
                    console.log('saving fields');
                    fieldsToCopy.forEach(function(fieldToCopy) {
                        console.log('fields to save: ' + newSubcategory.get('fields.length'));
                       newSubcategory.get('fields').forEach(function(oldField) {
                           console.log("verifying: " + oldField.get('name') + " agains: " + ieldToCopy.get('name'));
                           if (oldField.get('name') === fieldToCopy.get('name')) {
                               console.log("updating: " + oldField.get('id'));
                               oldField.set('value', fieldToCopy.get('value'));
                               oldField.save();
                           }
                       });
                    });


                });
            } else {
                console.log('newId is not valid: ' + newId);
            }*/

        }
    },

    reloadCategories: function() {
        console.log('reloadCategories');
        console.log(this.get('model'));

        this.store.find('category');

        this.get('model').forEach(function(category) {
            console.log('category: ' + category.get('id'));
            if (category.get('id') === 'uploads') {
                category.reload();
            }
        });

        //this.store.find('category', 'uploads').reload();
    },

    domainHaveUpload: function() {
        return this.get('controllers.user.domain.uploadUrl') !== undefined && this.get('controllers.user.domain.uploadUrl') !== null;
    }.property('controllers.user.domain.uploadUrl'),

    observeID: function(){
        this.send("userChanged");
    }.observes("controllers.user.content.id")
});