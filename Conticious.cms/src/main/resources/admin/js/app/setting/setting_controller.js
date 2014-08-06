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
            console.log('port: ');

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