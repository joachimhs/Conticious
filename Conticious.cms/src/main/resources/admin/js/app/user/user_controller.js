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
            console.log('Fetching Admin DopplerUser');

            var controller = this;
            this.store.find('user', this.get('uuidAdminToken')).then(function(data) {
                if (data.get('role') === 'admin' || data.get('role') === 'super') {
                    console.log('Creating uuidAdminToken cookie!');
                    Conticious.createCookie("uuidAdminToken", data.get('id'), 30);
                    controller.set('content', data);

                    controller.store.find('setting', 'ConticiousSettings').then(function(setting) {

                        if (setting.get('domains')) {
                            setting.get('domains').forEach(function(domain) {
                                if (domain.get('id') === window.location.hostname) {
                                    controller.set('domain', domain);
                                }
                            });
                        }
                    });


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