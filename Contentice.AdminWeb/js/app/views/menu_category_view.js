Conticious.MenuCategoryView = Ember.View.extend({
    templateName: 'menu-category',
    selectedSortColumn: null,
    selectedFilterString: null,
    selectedFilterColumn: null,
    sortAndFilterShowing: false,
    numSubcategoriesShown: 0,
    showNewSubcategoryArea: false,
    newSubcategoryName: null,
    isExpanded: false,

    actions: {
        toggleSortAndFilter: function() {
            if (this.get('sortAndFilterShowing')) {
                var view = this;
                $("#sortAndFilterArea").slideUp(function() {
                    view.set('sortAndFilterShowing', false);
                });
            } else {
                this.set('sortAndFilterShowing', true);
                Ember.run.schedule("afterRender", function() {
                    $("#sortAndFilterArea").hide();
                    $("#sortAndFilterArea").slideDown();
                });
            }

            console.log('toggleSortAndFilter: ' + this.get('sortAndFilterShowing'));
        },

        openNewSubcategory: function() {
            if (!this.get('showNewSubcategoryArea')) {
                this.set('showNewSubcategoryArea', true);
                Ember.run.schedule("afterRender", function() {
                    $("#showNewSubcategoryArea").hide();
                    $("#showNewSubcategoryArea").slideDown();
                });
            }
        },

        cancelNewSubcategory: function() {
            this.hideAddSubcategory();
        },

        addNewSubcategory: function() {
            var category = this.get('category');
            var newSubcategoryName = this.get('newSubcategoryName');

            if (newSubcategoryName) {
                var newSubcategory = this.get('controller').store.createRecord('subcategory', {
                    id: category.get('id') + "_" + newSubcategoryName,
                    name: newSubcategoryName
                });

                category.get('subcategories').pushObject(newSubcategory);

                newSubcategory.save().then(function(data) {
                    console.log('newSubcategory saved. Saving category');
                    category.save().then(function(data) {
                        category.reload();
                        newSubcategory.reload();
                    });
                });

            }

            this.hideAddSubcategory();
        }
    },

    hideAddSubcategory: function() {
        var view = this;
        $("#showNewSubcategoryArea").slideUp(function() {
            view.set('showNewSubcategoryArea', false);
            view.set('newSubcategoryName', null);
        });
    },

    isSelected: function() {
        return this.get('category.id') === this.get('controller.controllers.category.model.id') && this.get('controller.controllers.category.model.isLoaded') === true;
    }.property('controller.controllers.category.model.id', 'controller.controllers.category.model.isLoaded'),

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

    subcategoryId: function() {
        return this.get('elementId') + "_subcategoryArea";
    }.property('elementId'),

    selectedSortColumnObserver: function() {
        console.log('New Sort Columnn: ' + this.get('selectedSortColumn'));
        this.sortOrFilter();
    }.observes('selectedSortColumn'),

    selectedFilterStringObserver: function() {
        console.log('New Filter String: ' + this.get('selectedFilterString'));
        this.sortOrFilter();
    }.observes('selectedFilterString'),

    selectedFilterColumnObserver: function() {
        console.log('New Filter Column: ' + this.get('selectedFilterColumn'));
        this.sortOrFilter();
    }.observes('selectedFilterColumn'),

    /*subcategoriesObserver: function() {
        this.set('sortedSubcategories', this.get('category.subcategories'));
    }.observes('category.subcategories').on('init'),*/


    sortOrFilter: function() {
        console.log('Sorting subcategories');
        var columnToSortBy = this.get('selectedSortColumn');
        var columnToFilterBy = this.get('selectedFilterColumn');
        var columnFilter = this.get('selectedFilterString');

        var subcategories = [];
        var sortProp = 'id';

        if (columnToSortBy && subcategories) {
            sortProp = "sortValue";
        }

        this.get('category.subcategories').forEach(function(subcat) {
            var showColumn = true;

            subcat.get('fields').forEach(function(field) {
                if (field.get('name') === columnToSortBy) {
                    subcat.set('sortValue', field.get('value'));
                }

                if (columnToFilterBy && columnFilter && field.get('name') === columnToFilterBy) {
                    showColumn = field.get('value') && field.get('value').indexOf(columnFilter) > -1;
                    console.log('    Filtering away: ' + subcat.get('id'));
                } else if (columnToFilterBy === null && columnFilter) {
                    showColumn = subcat.get('id').indexOf(columnFilter) > -1;
                }
            });

            if (!columnToSortBy) {
                subcat.set('sortValue', subcat.get('id'));
            }

            if (showColumn) {
                subcategories.push(subcat);
            }
        });

        console.log('sortProp: ' + sortProp);
        console.log(sortProp);

        var sortedResult = Em.ArrayProxy.createWithMixins(
            Ember.SortableMixin,
            { content:subcategories, sortProperties: ['sortValue'] }
        );

        console.log(sortedResult.getEach('id'));

        this.set('numSubcategoriesShown', subcategories.get('length'));

        this.set('sortedSubcategories', sortedResult);
    },

    sortedSubcategoriesObserver: function() {

    }.observes('sortedSubcategories.@each.id')
});