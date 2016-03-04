import Ember from 'ember';

export default Ember.Component.extend({
  actions: {
    renameSubcategory: function(category, subcategory) {
      this.sendAction('renameSubcategory', category, subcategory, this.get('newId'));
    },

    selectSubcategory: function(category, subcategory) {
      console.log('menu-subcategory selectSubcategory: ' + category + " :: " + subcategory);
      /*subcategory.get('fields').forEach(function(field) {
        field.reload();
      });*/
      this.sendAction('selectSubcategory', category, subcategory);
    },

    doEditSubcategory: function() {
      console.log('editing subcategory: ' + this.get('controllers.subcategory.model.id'));
      if (this.get('subcategory')) {
        this.set('subcategory.isEditing', true);

        Ember.run.schedule("afterRender", function() {
          $("#editSubcategoryArea").hide();
          $("#editSubcategoryArea").slideDown();
        });
      }
    },

    deleteSubcategoryFirstStep: function(subcategory) {
      subcategory.set('deleteFirstStep', true);
    },

    deleteSubcategoryConfirm: function(category, subcategory) {
      this.sendAction('deleteSubcategory', category, subcategory);
    }
  },

  isSelected: function() {
    return this.get('subcategory.id') === this.get('selectedSubcategory.id');
  }.property('subcategory.id', 'selectedSubcategory.id'),

  isEditing: function() {
    return this.get('isSelected') && this.get('subcategory.isEditing');
  }.property('isSelected', 'subcategory.isEditing')
});
