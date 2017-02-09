import Ember from 'ember';

export default Ember.Route.extend({
    model() {
        return Ember.RSVP.hash({
            "settings": this.store.find('setting', 'ConticiousSettings'),
            "postProcessors": this.store.findAll('postProcessor')
        });
    }
});
