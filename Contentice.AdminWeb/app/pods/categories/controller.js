import Ember from 'ember';

export default Ember.Controller.extend({
  userController: Ember.inject.controller('user'),
  categoryController: Ember.inject.controller('categories/category'),
  subcategoryController: Ember.inject.controller('categories/category/subcategory'),

  categorySortProperties: ['id:asc'],
  sortedCategories: Ember.computed.sort('model', 'categorySortProperties'),

  showNewCategoryField: false,
  showUploadField: false,
  newCategoryName: null,
  newId: null,

  actions: {
    selectCategory: function(category) {


      var self = this;

      console.log('category.id: ' + category.id);
      console.log('categorryController ' + this.get('categoryController'));
      console.log('categorryController category ' + this.get('categoryController.model.category'));
      console.log('categorryController category.id ' + this.get('categoryController.model.category.id'));
      if (category.id === this.get('categoryController.model.category.id')) {
        console.log('DE-SELECTING:' + category.id);
        this.set('categoryController.model.category', null);
        self.transitionToRoute("categories");
      } else {
        console.log('SELECTING:' + category.id);
        self.transitionToRoute("categories.category", category.id);
      }



      /*category.reload().then(function(data) {
        console.log('TRANSITION');

      }, function() {
        console.log("ERROR");
      });*/
      //{{#link-to "category" category
    },

    selectSubcategory: function(category, subcategory) {
      console.log('SELECTING SUBCATEGORY:' + category.id + " :: " + subcategory.id);

      var self = this;

      self.transitionToRoute("categories.category.subcategory", category.id, subcategory.id);
    },

    showNewCategory: function() {
      this.set('showNewCategoryField', true);
    },

    cancelNewCategory: function() {
      this.set('showNewCategoryField', false);
      this.set('newCategoryName', null);
    },

    saveNewCategory: function() {
      var categoryId = this.get('newCategoryName');

      var category = this.store.createRecord('category', {
        id: categoryId
      });

      category.save();

      this.set('showNewCategoryField', false);
      this.set('newCategoryName', null);
    },

    showFileUpload: function() {
      this.set('showUploadField', true);
    },

    cancelFileUpload: function() {
      this.set('showUploadField', false);
    },

    userChanged: function() {
      console.log('user changed. Refreshing categorill!');
      this.set('model', this.store.findAll('category'));
    },

    finishEditSubcategory: function() {
      console.log('finishing subcategory editing... ' + this.get('newId'));
      if (this.get('subcategoryController.model')) {

        var controller = this;obse
        $("#editSubcategoryArea").slideUp(function() {
          controller.set('subcategoryController.model.deleteFirstStep', false);
          controller.set('subcategoryController.model.isEditing', false);
          controller.set('newId', '');
        });
      }
    },

    deleteSubcategoryFirstStep: function(subcategory) {
      subcategory.set('deleteFirstStep', true);
    },

    deleteSubcategoryConfirm: function(subcategory) {
      subcategory.set('deleteFirstStep', false);
      console.log('DELETING SUBCATEGORY: ' + subcategory.get('id'));
      this.set('subcategoryController.model.isEditing', false);
      subcategory.deleteRecord();
      subcategory.save();

      this.transitionToRoute("categories");
      controller.reloadCategories();
    },

    renameSubcategory: function(category, subcategory, newId) {
      var self = this;

      var newId = category.get('id') + "_" + newId;
      var oldId = subcategory.get('id');

      console.log("Renaming " + oldId + " to " + newId);

      var url = "/json/admin/renameSubcategory/" + oldId + "/" + newId;

      $.ajax({
        type: "POST",
        url: url,
        data: "",
        success: function() {
          subcategory.set('isEditing', false);
          self.transitionToRoute("categories.category", category.get('id'));
          self.reloadSubcategories();
        },
        dataType: "json"
      });
    },

    deleteSubcategory: function(category, subcategory) {
      subcategory.destroyRecord().then(function() {
        console.log('reloading subcategories');
        this.reloadSubcategories();
      });

      this.transitionToRoute("categories.category", category.get('id'));
    },

    createSubcateogry(category, newSubcategoryName) {
      var self = this;
      console.log('createSubcateogry: ' + category + ' name: ' + newSubcategoryName);
      var newSubcategory = this.store.createRecord('subcategory', {
        id: category.get('id') + "_" + newSubcategoryName,
        name: newSubcategoryName
      });

      newSubcategory.save().then(function(data) {
        console.log('newSubcategory saved. Saving category');
        category.save().then(function(data) {
          category.reload();
          newSubcategory.reload();

          self.get('categoryController').set('model.subcategories', self.store.query('subcategory', {category: self.get('categoryController.model.category.id')}));
        });
      });
    }
  },

  reloadCategories: function() {
    console.log('reloadCategories');

    this.store.findAll('category', { reload: true });
  },

  reloadSubcategories: function() {
    console.log('reloadSubcategories');
    this.get('categoryController').reloadSubcategories();
    //this.store.query('subcategory', {category: categoryId})
  },

  subcategories: function() {
    console.log('SUBCATEGORIES: ');
    console.log(this.get('categoryController'));

    return this.get('categoryController.sortedSubcategories');
  }.property('categoryController.sortedSubcategories'),

  domainHaveUpload: function() {
    return this.get('userController.domain.uploadUrl') !== undefined && this.get('userController.domain.uploadUrl') !== null;
  }.property('userController.domain.uploadUrl'),

  observeID: function(){
    this.send("userChanged");
  }.observes("userController.content.id")
});
