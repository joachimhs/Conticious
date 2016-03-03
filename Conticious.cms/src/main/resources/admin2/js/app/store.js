DS.RESTAdapter.reopen({
    namespace: 'json/admin'
});

Conticious.Store = DS.Store.extend({
    adapter:  "DS.RESTAdapter"
});