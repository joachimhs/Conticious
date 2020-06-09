import Ember from 'ember';

export default Ember.Controller.extend({
  actions: {
    doSaveSubcategory: function(subcategory) {
      console.log('doSaveSubcategory: ' + subcategory.get('id'));
      if (subcategory.get('hasDirtyAttributes')) {
        subcategory.save();
      }
    }
  }
});
