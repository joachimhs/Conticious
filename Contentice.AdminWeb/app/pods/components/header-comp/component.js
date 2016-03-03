import Ember from 'ember';

export default Ember.Component.extend({

  actions: {
    logOut: function() {
      /*Conticious.eraseCookie('uuidAdminToken');
      this.set('controllers.user.uuidAdminToken', null);
      Conticious.reset();*/
    }
  }
});
