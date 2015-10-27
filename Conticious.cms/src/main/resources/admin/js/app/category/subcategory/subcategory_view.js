Conticious.SubcategoryView = Ember.View.extend({
    modelObserver: function() {
        console.log('SubcategoryView ModelObserver');
        //this.rerender();
    }.observes('controller.model.id').on('init')
});