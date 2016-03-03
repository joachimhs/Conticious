Conticious.DroppableViewMixin = Ember.Mixin.create({
    classNames: ['droppable'],
    classNameBindings: ['isDragOver'],

    dragEnter: function() {
        this.set('isDragOver', true);
    },
    dragLeave: function() {
        this.set('isDragOver', false);
    },

    drop: function(event) {
        console.log("DROP");
        this.set('isDragOver', false);
        this.set('didReceiveDrop', true);
    },

    isDroppable: true

});