Conticious.SettingRoute = Ember.Route.extend({
    model: function() {
        return Ember.RSVP.hash({
            "settings": this.store.find('setting', 'ConticiousSettings'),
            "postProcessors": this.store.find('postProcessor')
        });
    }
});