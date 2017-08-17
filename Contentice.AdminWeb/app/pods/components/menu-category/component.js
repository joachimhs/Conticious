import Ember from 'ember';

export default Ember.Component.extend({
  selectedSortColumn: 'id',
  selectedFilterString: null,
  selectedFilterColumn: 'id',
  sortAndFilterShowing: false,
  numSubcategoriesShown: 0,
  showNewSubcategoryArea: false,
  newSubcategoryName: null,
  isExpanded: false,
  sortColumn: ['sortValue:asc'],


  actions: {
    renameSubcategory: function (category, subcategory, newId) {
      this.sendAction("renameSubcategory", category, subcategory, newId);
    },

    copySubcategory: function (category, subcategory, newId) {
      this.sendAction("copySubcategory", category, subcategory, newId);
    },

    deleteSubcategory: function (category, subcategory) {
      this.sendAction('deleteSubcategory', category, subcategory);
    },

    selectSubcategory: function (category, subcategory) {
      console.log('menu-category selectSubcategory: ' + category + " :: " + subcategory);

      this.sendAction('selectSubcategory', category, subcategory);
    },

    selectCategory: function (category) {
      this.sendAction("selectCategory", category);
    },

    toggleSortAndFilter: function () {
      if (this.get('sortAndFilterShowing')) {
        var view = this;
        $("#sortAndFilterArea").slideUp(function () {
          view.set('sortAndFilterShowing', false);
        });
      } else {
        this.set('sortAndFilterShowing', true);
        Ember.run.schedule("afterRender", function () {
          $("#sortAndFilterArea").hide();
          $("#sortAndFilterArea").slideDown();
        });
      }

      console.log('toggleSortAndFilter: ' + this.get('sortAndFilterShowing'));
    },

    openNewSubcategory: function () {
      if (!this.get('showNewSubcategoryArea')) {
        this.set('showNewSubcategoryArea', true);
        Ember.run.schedule("afterRender", function () {
          $("#showNewSubcategoryArea").hide();
          $("#showNewSubcategoryArea").slideDown();
        });
      }
    },

    cancelNewSubcategory: function () {
      this.hideAddSubcategory();
    },

    addNewSubcategory: function () {
      var category = this.get('category');
      var newSubcategoryName = this.get('newSubcategoryName');

      if (newSubcategoryName) {
        this.sendAction('createSubcateogry', category, newSubcategoryName);
      }

      this.hideAddSubcategory();
    }
  },

  hideAddSubcategory: function () {
    var view = this;
    $("#showNewSubcategoryArea").slideUp(function () {
      view.set('showNewSubcategoryArea', false);
      view.set('newSubcategoryName', null);
    });
  },

  isSelected: function () {
    return this.get('category.id') === this.get('selectedCategory.id');
  }.property('selectedCategory.id', 'category.id'),

  /*isSelectedObserver: function() {
   var view = this;
   var isSelected = this.get('isSelected');

   console.log("isSelectedObserver: " + isSelected + " id: " + view.get('category.id'));
   if (isSelected) {
   console.log(view.get('elementId') + " .categoryList");
   view.set('isExpanded', true);
   $("#" + view.get('elementId') + " .categoryList").css({hidden: true});
   Ember.run.schedule("afterRender", function() {
   $("#" + view.get('elementId') + " .categoryList").hide().slideDown(function() {
   console.log('setting isExpanded to true');

   }, 600);
   });

   } else {
   $("#" + view.get('elementId') + " .categoryList").slideUp(function() {
   view.set('isExpanded', false);
   }).hide();
   }
   }.observes('isSelected'),*/

  subcategoryId: function () {
    return this.get('elementId') + "_subcategoryArea";
  }.property('elementId'),

  selectedSortColumnObserver: function () {
    console.log('New Sort Columnn: ' + this.get('selectedSortColumn'));

    Ember.run.debounce(this, this.sortOrFilter, 200);
  }.observes('selectedSortColumn'),

  selectedFilterStringObserver: function () {
    console.log('New Filter String: ' + this.get('selectedFilterString'));

    Ember.run.debounce(this, this.sortOrFilter, 200);
  }.observes('selectedFilterString'),

  selectedFilterColumnObserver: function () {
    console.log('New Filter Column: ' + this.get('selectedFilterColumn'));

    Ember.run.debounce(this, this.sortOrFilter, 200);
  }.observes('selectedFilterColumn'),

  subcategpriesObserver: function () {
    Ember.run.debounce(this, this.sortOrFilter, 200);
  }.observes('category.id', 'subcategories.length', 'subcategories.@each.id').on('init'),

  pulldownFields: function () {
    var defaultFields = this.get('category.defaultFields');
    var fields = Ember.A();

    fields.pushObject(Ember.Object.create({
      id: 'id',
      name: 'id'
    }));

    if (defaultFields) {
      defaultFields.forEach(function (df) {
        fields.pushObject(Ember.Object.create({
          id: df.get('id'),
          name: df.get('name')
        }));
      })
    }

    return fields;
  }.property('category.defaultFields.@each.id'),

  sortOrFilter: function () {
    console.log('Sorting subcategories for: ' + this.get('category.id'));
    var columnToSortBy = this.get('selectedSortColumn');
    var columnToFilterBy = this.get('selectedFilterColumn');

    console.log("sortOrFilter columnToFilterBy: '" + columnToFilterBy + "'");

    if (columnToFilterBy.indexOf(this.get('category.id') > 0)) {
      columnToFilterBy = columnToFilterBy.substr(this.get('category.id.length') + 1);
    }

    if (!columnToFilterBy) {
      columnToFilterBy = 'id';
    }

    var columnFilter = this.get('selectedFilterString');

    console.log("sortOrFilter column: '" + columnToFilterBy + "'");

    var subcategories = [];
    var sortProp = 'id';

    if (columnToSortBy && subcategories) {
      sortProp = "sortValue";
    }

    for (var sindex = 0; sindex < this.get('subcategories.length'); sindex++) {

      var subcat = this.get('subcategories').objectAt(sindex);
      var showColumn = true;

      for (var index = 0; index < subcat.get('fields.length'); index++) {
        var field = subcat.get('fields').objectAt(index);
        if (field.get('name') === columnToSortBy) {
          subcat.set('sortValue', field.get('value'));
        } else if (columnToSortBy === 'id') {
          subcat.set('sortValue', field.get('id'));
        }

        if (columnToFilterBy === 'id' && columnFilter) {
          showColumn = field.get('id') && field.get('id').indexOf(columnFilter) > -1;
        } else if (columnToFilterBy && columnFilter && field.get('name') === columnToFilterBy) {
          showColumn = field.get('value') && field.get('value').indexOf(columnFilter) > -1;
        } else if (columnToFilterBy === null && columnFilter) {
          showColumn = subcat.get('id').indexOf(columnFilter) > -1;
        }
      }

      if (!columnToSortBy) {
        subcat.set('sortValue', subcat.get('id'));
      }

      if (showColumn) {
        subcategories.push(subcat);
      }
    }

    this.set('numSubcategoriesShown', subcategories.get('length'));
    this.set('filteredSubcategories', subcategories);

  //sortedSubcategories: Ember.computed.sort('filteredSubcategories', 'sortColumn'),
    var sortedSubcategories = subcategories.sort(function(a,b) {
      return a.get('sortValue') - b.get('sortValue');
    });

    this.set('sortedSubcategories', sortedSubcategories);
  }
});
