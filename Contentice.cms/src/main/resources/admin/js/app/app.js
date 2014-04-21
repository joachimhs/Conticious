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

var Conticious = Ember.Application.create({
    templates: ['application', 'categories', 'header', 'category', 'category/index', 'subcategory', 'subcategory/index', 'subcategory/fields', 'subcategory/preview', 'menu-category', 'menu-subcategory', 'setting']
});

Conticious.Router.map(function() {
    this.resource("categories", {path: "/"}, function() {
        this.resource('category', {path: "/category/:category_id"}, function() {
            this.resource('subcategory', {path: "/subcategory/:subcategory_id"}, function() {
                this.route('fields');
                this.route('preview');
            });
        });
    });
    this.resource("setting");
});

DS.RESTAdapter.reopen({
    namespace: 'json/admin'
});

Conticious.Store = DS.Store.extend({
    adapter:  "DS.RESTAdapter"
});

Conticious.CategoriesRoute = Ember.Route.extend({
    model: function() {
        return this.store.find('category');
    }
});

Conticious.CategoryRoute = Ember.Route.extend({
    model: function(category) {
        return this.store.find('category', category.category_id);
    }
});

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
                    console.log('newSubcategory saved. Saving category')
                    category.save();
                    /*.then(function(data) {
                        category.reload();
                        newSubcategory.reload();
                    });*/
                });

            }

            this.set('controller.newSubcategoryName', null);
            this.set('controller.showNewSubcategoryArea', false);
        }
    }
});

Conticious.SubcategoryIndexRoute = Ember.Route.extend({

});

Conticious.SubcategoryIndexRoute = Ember.Route.extend({
    model: function() {
        var subcategory = this.modelFor('subcategory');
        return subcategory;
    }
});

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

Conticious.SubcategoryPreviewRoute = Ember.Route.extend({
    model: function() {
        var subcategory = this.modelFor('subcategory');
        return subcategory;
    }
});

Conticious.SettingRoute = Ember.Route.extend({
    model: function() {
        return this.store.find('setting', 'ConticiousSettings');
    }
});

Conticious.SettingController = Ember.ObjectController.extend({
    actions: {
        addDomain: function() {
            var domains = this.get('domains');

            var newDomain = this.store.createRecord('domain', {

            });

            domains.pushObject(newDomain);
        },

        deleteDomain: function(domain) {
            domain.deleteRecord();
            this.get('model.domains').removeObject(domain);
        },

        saveChanges: function() {
            this.doSaveSettings();
        }
    },

    doSaveSettings: function() {
        var domains = [];

        this.get('model.domains').forEach(function(domain) {
            domains.pushObject(domain);
        });


        var jsonRequest = {};
        jsonRequest.domains = domains;

        var controller = this;
        $.ajax("/json/admin/settings", {
            data: JSON.stringify(jsonRequest),
            contentType: 'application/json',
            type: 'POST',
            success: function () {
                console.log('SUCCESS');
                controller.get('model').reload();
            }
        });
    }
});

Conticious.CategoriesController = Ember.ArrayController.extend({
    needs: ['category'],

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

Conticious.MenuCategoryView = Ember.View.extend({
    templateName: 'menu-category',

    isSelected: function() {
        return this.get('category.id') === this.get('controller.controllers.category.model.id');
    }.property('controller.controllers.category.model.id')
});

Conticious.MenuSubcategoryView = Ember.View.extend({
    templateName: 'menu-subcategory'

});

Conticious.CategoriesView = Ember.View.extend({
    isSelected: function() {
        console.log(this.get('model.id'));
        console.log(this.get('controller.model.id'));
        console.log(this.get('controller'));
        console.log(this.get('context'));
        console.log(this);

        return this.get('model.id') === this.get('controller.model.id');
    }.property('controller.model')
});

Conticious.CategoryController = Ember.ObjectController.extend({

});

Conticious.CategoryIndexController = Ember.Controller.extend({
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
        },

        saveCategory: function(category) {
            if (category.get('isDirty')) {
                category.save();
            }
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

Conticious.SubcategoryController = Ember.ObjectController.extend({

});

Conticious.SubcategoryIndexController = Ember.ObjectController.extend({
    actions: {
        doSaveSubcategory: function(subcategory) {
            console.log('doSaveSubcategory: ' + subcategory.get('id'));
            if (subcategory.get('isDirty')) {
                subcategory.save();
            }
        }
    }
});

Conticious.SubcategoryRoute = Ember.Route.extend({
    model: function(subcategory) {
        return this.store.find('subcategory', subcategory.subcategory_id);
    }
});

Conticious.Category = DS.Model.extend({
    subcategories: DS.hasMany('subcategory'),
    defaultFields: DS.hasMany('categoryField'),
    isPublic: DS.attr('boolean')
});

Conticious.CategoryField = DS.Model.extend({
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

Conticious.Subcategory = DS.Model.extend({
    name: DS.attr('string'),
    content: DS.attr('string'),
    fields: DS.hasMany('subcategoryField')
});

Conticious.SubcategoryField = DS.Model.extend({
    name: DS.attr('string'),
    type: DS.attr('string'),
    required: DS.attr('boolean'),
    value: DS.attr('string'),

    isTextfield: function() {
        return this.get('type') === 'textfield';
    }.property('type'),

    isTextarea: function() {
        return this.get('type') === 'textarea';
    }.property('type'),

    isBoolean: function() {
        return this.get('type') === 'boolean';
    }.property('type'),

    isArray: function() {
        return this.get('type') === "array";
    }.property('type')
});

Conticious.Setting = DS.Model.extend({
    domains: DS.hasMany('domain')
});

Conticious.Domain = DS.Model.extend({
    domainName: DS.attr('string'),
    webappName: DS.attr('string'),
    active: DS.attr('boolean')
});



