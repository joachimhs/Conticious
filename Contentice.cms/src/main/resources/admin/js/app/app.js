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
    templates: ['application', 'index', 'header']
});

Contentice.Router.map(function() {
    this.resource("index", {path: "/"});
});

DS.RESTAdapter.reopen({
    namespace: 'json/admin'
});

Contentice.Store = DS.Store.extend({
    adapter:  "DS.RESTAdapter"
});

Contentice.IndexRoute = Ember.Route.extend({
    model: function() {
        return this.store.find('category');
    }
});

Contentice.Category = DS.Model.extend({
    subcategories: DS.hasMany('subcategory')
});


Contentice.Subcategory = DS.Model.extend({

});



