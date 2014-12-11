Conticious.MenuSubcategoryView = Ember.View.extend({
    templateName: 'menu-subcategory',

    isSelected: function() {
        return this.get('subcategory.id') === this.get('controller.controllers.subcategory.model.id');
    }.property('controller.controllers.subcategory.model.id'),

    isEditing: function() {
        return this.get('isSelected') && this.get('subcategory.isEditing');
    }.property('isSelected', 'subcategory.isEditing')

    /*,

     willDestroyElement: function() {
     console.log('MenuSubcategoryView willDestroyElement');
     var clone = this.$().clone();
     this.$().parent().append(clone);
     clone.slideUp();
     }*/
});