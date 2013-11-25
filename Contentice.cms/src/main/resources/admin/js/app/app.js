Ember.Application.reopen({
    templates: [],

    init: function() {
        this._super();

        this.loadTemplates();
    },

    loadTemplates: function() {
        var app = this,
            templates = this.get('templates');

        app.deferReadiness();

        var promises = templates.map(function(name) {
            return Ember.$.get('/admin/templates/'+name+'.hbs').then(function(data) {
                Ember.TEMPLATES[name] = Ember.Handlebars.compile(data);
            });
        });

        Ember.RSVP.all(promises).then(function() {
            app.advanceReadiness();
        });
    }
});

var Contentice = Ember.Application.create({
    templates: ['application', 'categories', 'header', 'category', 'category/index', 'subcategory', 'subcategory/index', 'subcategory/fields', 'subcategory/preview']
});

Contentice.Router.map(function() {
    this.resource("categories", {path: "/"}, function() {
        this.resource('category', {path: "/category/:category_id"}, function() {
            this.resource('subcategory', {path: "/subcategory/:subcategory_id"}, function() {
                this.route('fields');
                this.route('preview');
            });
        });
    });
});

DS.RESTAdapter.reopen({
    namespace: 'json/admin'
});

Contentice.Store = DS.Store.extend({
    adapter:  "DS.RESTAdapter"
});

Contentice.CategoriesRoute = Ember.Route.extend({
    model: function() {
        return this.store.find('category');
    }
});

Contentice.CategoryRoute = Ember.Route.extend({
    model: function(category) {
        return this.store.find('category', category.category_id);
    }
});

Contentice.CategoryIndexRoute = Ember.Route.extend({
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

                category.save();
                newField.save();
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

                category.save();
                newSubcategory.save();
            }

            this.set('controller.newSubcategoryName', null);
            this.set('controller.showNewSubcategoryArea', false);
        }
    }
});

Contentice.SubcategoryIndexRoute = Ember.Route.extend({

});

Contentice.SubcategoryIndexRoute = Ember.Route.extend({
    model: function() {
        var subcategory = this.modelFor('subcategory');
        return subcategory;
    }
});

Contentice.SubcategoryFieldsRoute = Ember.Route.extend({
    model: function() {
        var subcategory = this.modelFor('subcategory');
        return subcategory;
    }
});

Contentice.SubcategoryPreviewRoute = Ember.Route.extend({
    model: function() {
        var subcategory = this.modelFor('subcategory');
        return subcategory;
    }
});

Contentice.CategoriesController = Ember.ArrayController.extend({
    showNewCategoryField: false,
    newCategoryName: null,

    actions: {
        showNewCategory: function() {
            this.set('showNewCategoryField', true)
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
        }
    }
});

Contentice.CategoryIndexController = Ember.Controller.extend({
    needs: 'category',
    showNewFieldArea: false,
    showNewSubcategoryArea: false,

    showNewField: function() {
        return this.get('showNewFieldArea') || this.get('showNewSubcategoryArea');
    }.property('showNewFieldArea', 'showNewSubcategoryArea'),

    actions: {
        openNewField: function() {
            this.set('showNewFieldArea', true);
        },

        cancelNewField: function() {
            this.set('showNewFieldArea', false);
            this.set('newFieldName', null);
            this.set('newFieldType', null)
            this.set('newFieldRequired', false)
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
                if (field.get('isDirty') || field.get('isNew')) {
                    field.save();
                }
            })
        }

        var subcategories = category.get('subategories');
        if (subcategories) {
            subcategories.forEach(function(subcategory) {
                if (subcategory.get('isDirty') || subcategory.get('isNew')) {
                    subcategory.save();
                }
            })
        }
    },

    init: function() {
        this._super();

        var fieldTypes = [];
        fieldTypes.pushObject('textarea');
        fieldTypes.pushObject('textfield');
        fieldTypes.pushObject('boolean');
        fieldTypes.pushObject('array');

        this.set('fieldTypes', fieldTypes);
    }
});

Contentice.SubcategoryController = Ember.ObjectController.extend({
    actions: {
        doSaveSubcategory: function(subcategory) {
            console.log('doSaveSubcategory: ' + subcategory.get('id'));
            if (subcategory.get('isDirty')) {
                subcategory.save();
            }
        }
    }
});

Contentice.SubcategoryRoute = Ember.Route.extend({
    model: function(subcategory) {
        return this.store.find('subcategory', subcategory.subcategory_id);
    }
});

Contentice.Category = DS.Model.extend({
    subcategories: DS.hasMany('subcategory'),
    defaultFields: DS.hasMany('categoryField')
});

Contentice.CategoryField = DS.Model.extend({
    name: DS.attr('string'),
    type: DS.attr('string'),
    required: DS.attr('boolean'),

    isTextfield: function() {
        return this.get('type') === 'textfield'
    }.property('type'),

    isTextarea: function() {
        return this.get('type') === 'textarea'
    }.property('type'),

    isBoolean: function() {
        return this.get('type') === 'boolean'
    }.property('type')
});

Contentice.Subcategory = DS.Model.extend({
    name: DS.attr('string'),
    content: DS.attr('string')
});



