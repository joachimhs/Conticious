import Ember from 'ember';

export default Ember.Controller.extend({
  needs: ['application'],

  init: function() {
    console.log('UserController init');
    var controller = this;

    var cookie = this.readCookie("uuidAdminToken");
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
          controller.createCookie("uuidAdminToken", data.get('id'), 30);
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
          controller.get('controllers.application').eraseCookie("uuidAdminToken");
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
  }.property('content.role'),

  isSuperLoggedIn: function() {
    var isAdmin = false;
    console.log('roleObserver: ' + this.get('content.role'));

    if (this.get('content.role') === 'super') {
      isAdmin = true;
    }

    return isAdmin;
  }.property('content.role'),

  createCookie: function(name, value, days) {
    var expires = null;

    if (days) {
      var date = new Date();
      date.setTime(date.getTime()+(days*24*60*60*1000));
      expires = "; expires="+date.toGMTString();
    }
    else expires = "";
    document.cookie = name+"="+value+expires+"; path=/";
  },

  readCookie:function (name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
      var c = ca[i];
      while (c.charAt(0) == ' ') c = c.substring(1, c.length);
      if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
  },

  eraseCookie:function (name) {
    this.createCookie(name, "", -1);
  }
});
