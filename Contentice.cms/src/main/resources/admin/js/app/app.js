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
    templates: ['application', 'categories', 'header', 'category', 'category/index', 'category/subcategory']
});

Contentice.Router.map(function() {
    this.resource("categories", {path: "/"}, function() {
        this.resource('category', {path: "/category/:category_id"}, function() {
            this.route('subcategory', {path: "/subcategory/:subcategory_id"});
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

Contentice.CategoryIndexController = Ember.Controller.extend({
    needs: 'category',

    init: function() {
        this._super();

        var fieldTypes = [];
        fieldTypes.pushObject('textarea');
        fieldTypes.pushObject('textfield');
        fieldTypes.pushObject('boolean');

        this.set('fieldTypes', fieldTypes);
    }
});

Contentice.SubcategoriesCategoryRoute = Ember.Route.extend({
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

});



