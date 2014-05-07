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
    templates: ['application', 'categories', 'header', 'category', 'category/index', 'subcategory', 'subcategory/index', 'subcategory/fields', 'subcategory/preview', 'menu-category', 'menu-subcategory', 'setting', 'components/log-in'],

    createCookie: function(name, value, days) {
        if (days) {
            var date = new Date();
            date.setTime(date.getTime()+(days*24*60*60*1000));
            var expires = "; expires="+date.toGMTString();
        }
        else var expires = "";
        document.cookie = name+"="+value+expires+"; path=/";
    },

    readCookie:function (name) {
        var nameEQ = name + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
        }
        return null;
    },

    eraseCookie:function (name) {
        this.createCookie(name, "", -1);
    }
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

Conticious.HeaderController = Ember.Controller.extend({
    needs: ['user'],

    actions: {
        logOut: function() {
            Conticious.eraseCookie('uuidAdminToken');
            this.set('controllers.user.uuidAdminToken', null);
            Conticious.reset();
        }
    }
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

Conticious.UserController = Ember.Controller.extend({
    init: function() {
        console.log('UserController init');
        var controller = this;

        var cookie = Conticious.readCookie("uuidAdminToken");
        if (cookie) {
            this.set('uuidAdminToken', cookie);
        }
    },

    uuidObserver: function() {
        console.log('uuidAdminTokenObserver: ' + this.get('uuidAdminToken'));
        if (this.get('uuidAdminToken')) {
            console.log('Fetching Admin User');

            var controller = this;
            this.store.find('user', this.get('uuidAdminToken')).then(function(data) {
                if (data.get('role') === 'admin' || data.get('role') === 'super') {
                    console.log('Creating uuidAdminToken cookie!');
                    Conticious.createCookie("uuidAdminToken", data.get('id'), 30);
                    controller.set('content', data);
                } else {
                    console.log('UUID Token is no longer valid, erasing cookie!');
                    Conticious.eraseCookie("uuidAdminToken");
                }
            });
        }
    }.observes('uuidAdminToken').on('init'),

    isAdminLoggedIn: function() {
        var isAdmin = false;
        console.log('roleObserver: ' + this.get('content.role'));

        if (this.get('content.role') === 'admin' || this.get('content.role') === 'super') {
            isAdmin = true;
        }

        return isAdmin;
    }.property('content.role')
});

Conticious.LogInComponent = Ember.Component.extend({
    actions: {
        loginButtonAction: function() {
            console.log('attempting to log in with username: ' + this.get('username') + ' and password: ' + this.get('password'))

            var data = {
                username: this.get('username'),
                password: this.get('password')
            };

            var component = this;

            $.ajax({
                type: "POST",
                url: "/json/admin/auth",
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function(res, status, xhr) {
                    console.log("success: " + JSON.stringify(res));
                    if (res.uuidAdminToken) {
                        component.set('uuidAdminToken', res.uuidAdminToken);
                    }
                }, error: function(res, status, err) {
                    console.log("error: " + JSON.stringify(res));
                    console.log("error: " + status + " error: " + err);
                }
            });
        }
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
    needs: ['user'],

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
        },

        userChanged: function() {
            console.log('user changed. Refreshing settings!');
            this.get('model').reload();
        },

        generateStatic: function(domain) {
            var full = location.protocol+'//'+location.hostname+(location.port ? ':'+location.port: '');

            var payload = {};
            payload.hostname = window.location.hostname;
            payload.port = location.port ? location.port: '';
            payload.domain = domain.get('domainName');

            console.log('hostname: '  + window.location.hostname);
            console.log('port: ')

            $.ajax("/json/admin/spg/" + domain.get('domainName'), {
                data: JSON.stringify(payload),
                contentType: 'application/json',
                type: 'POST',
                success: function () {
                    console.log('SUCCESS');
                }
            });
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
                console.log(controller.get('model'));
                controller.get('model').reload();
            }
        });
    },

    observeID: function(){
        this.send("userChanged");
    }.observes("controllers.user.content.id")
});

Conticious.CategoriesController = Ember.ArrayController.extend({
    needs: ['category', 'user'],

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
        },

        userChanged: function() {
            console.log('user changed. Refreshing categories!');
            this.set('model', this.store.find('category'));
        }
    },

    observeID: function(){
        this.send("userChanged");
    }.observes("controllers.user.content.id")
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
    active: DS.attr('boolean'),
    minified: DS.attr('boolean')
});

Conticious.User = DS.Model.extend({
    username: DS.attr('string'),
    role: DS.attr('string')
});



