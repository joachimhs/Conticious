import Ember from 'ember';

export default Ember.Component.extend({
  actions: {
    loginButtonAction: function() {
      console.log('attempting to log in with username: ' + this.get('username') + ' and password: ' + this.get('password'));

      var data = {
        username: this.get('username'),
        password: this.get('password')
      };

      var component = this;

      $.ajax({
        type: "POST",
        url: "/json/admin/auth",
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(res, status, xhr) {
          console.log("success: " + JSON.stringify(res));
          if (res.uuidAdminToken) {
            component.set('uuidAdminToken', res.uuidAdminToken);
          }
        }, error: function(res, status, err) {
          console.log("error: " + JSON.stringify(res));
          console.log("error: " + status + " error: " + err);
        }
      });
    }
  }
});
