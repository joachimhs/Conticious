import Ember from 'ember';

export default Ember.Controller.extend({
    categoriesController: Ember.inject.controller('categories'),
    categoryController: Ember.inject.controller('categories/category'),
    showNewFieldArea: false,
    showNewSubcategoryArea: false,
    newFieldName: 'ttt',
    newFieldType: null,
    newFieldRequired: null,

    showNewField: function() {
        return this.get('showNewFieldArea') || this.get('showNewSubcategoryArea');
    }.property('showNewFieldArea', 'showNewSubcategoryArea'),

    actions: {
        addNewField: function() {
            var category = this.get('model.category');
            var newFieldName = this.get('newFieldName');
            var newFieldType = this.get('newFieldType');
            var newFieldRequired = this.get('newFieldRequired');

            if (newFieldName) {
                var newField = this.store.createRecord('categoryField', {
                    id: category.get('id') + "_" + newFieldName,
                    name: newFieldName,
                    type: newFieldType,
                    required: newFieldRequired
                });

                //category.get('defaultFields').pushObject(newField);

                newField.save().then(function(data) {
                    console.log('new field saved. saving category');
                    category.get('defaultFields').pushObject(newField);
                    category.save();
                });
            }

            this.set('controller.newFieldName', null);
            this.set('controller.newFieldType', null);
            this.set('controller.showNewFieldArea', false);
        },

        addNewSubcategory: function() {
            var category = this.get('model.category');
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
        },

        openNewField: function() {
            this.set('showNewFieldArea', true);
        },

        cancelNewField: function() {
            this.set('showNewFieldArea', false);
            this.set('newFieldName', null);
            this.set('newFieldType', null);
            this.set('newFieldRequired', false);
        },

        openNewSubcategory: function() {
            this.set('showNewSubcategoryArea', true);
        },

        cancelNewSubcategory: function() {
            this.set('showNewSubcategoryArea', false);
            this.set('newSubcategoryName', null);
        },

        saveCategory: function(category) {
            this.doSaveCategory(category);
        },

        saveCategoryField: function(categoryField) {
            categoryField.save();
        },

        revertCategoryField: function(categoryField) {
            categoryField.rollback();
        },

        deleteCategoryField: function(categoryField) {
            categoryField.deleteRecord();
            categoryField.save();
        }
    },

    doSaveCategory: function(category) {
        category.save();
        var defaultFields = category.get('defaultFields');
        if (defaultFields) {
            defaultFields.forEach(function(field) {
                if (field.get('hasDirtyAttributes') || field.get('isNew')) {
                    field.save();
                }
            });
        }

        var subcategories = category.get('subategories');
        if (subcategories) {
            subcategories.forEach(function(subcategory) {
                if (subcategory.get('hasDirtyAttributes') || subcategory.get('isNew')) {
                    subcategory.save();
                }
            });
        }
    },

    newFieldIsAssociation: function() {
        return this.get('newFieldType') && (this.get('newFieldType') === "toOne" || this.get('newFieldType') === "toMany");
    }.property('newFieldType'),

    init: function() {
        this._super();

        var fieldTypes = [];
        fieldTypes.pushObject('textarea');
        fieldTypes.pushObject('textfield');
        fieldTypes.pushObject('boolean');
        fieldTypes.pushObject('array');
        fieldTypes.pushObject('toOne');
        fieldTypes.pushObject('toMany');

        this.set('fieldTypes', fieldTypes);
    }
});
