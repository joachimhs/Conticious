import Ember from 'ember';

export default Ember.Component.extend({
  actions: {
    selectSubcategory: function(category, subcategory) {
      console.log('menu-subcategory selectSubcategory: ' + category + " :: " + subcategory);
      /*subcategory.get('fields').forEach(function(field) {
        field.reload();
      });*/
      this.sendAction('selectSubcategory', category, subcategory);
    }
  },

  isSelected: function() {
    return this.get('subcategory.id') === this.get('selectedSubcategory.id');
  }.property('subcategory.id', 'selectedSubcategory.id'),

  isEditing: function() {
    return this.get('isSelected') && this.get('subcategory.isEditing');
  }.property('isSelected', 'subcategory.isEditing')
});
