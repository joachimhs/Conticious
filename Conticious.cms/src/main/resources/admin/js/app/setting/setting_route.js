Conticious.SettingRoute = Ember.Route.extend({
    model: function() {
        return this.store.find('setting', 'ConticiousSettings');
    }
});