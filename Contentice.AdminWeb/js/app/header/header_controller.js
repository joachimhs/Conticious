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