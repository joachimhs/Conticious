Conticious.DraggableViewMixin = Ember.Mixin.create({
    classNames: ['draggable'],
    classNameBindings: ['isDragging'],

    attributeBindings: ['draggable'],
    draggable: 'true', // must be the string 'true'
    isDraggable: true,

    dragStart: function(event) {
        this.set('isDragging', true);
    },

    dragEnd: function() {
        this.set('isDragging', false);
    }
});