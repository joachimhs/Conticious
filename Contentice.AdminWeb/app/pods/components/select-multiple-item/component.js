import Ember from 'ember';
import DraggableMixin from '../../../mixins/draggable-mixin'
import DroppableMixin from '../../../mixins/droppable-mixin'

export default Ember.Component.extend(DraggableMixin, DroppableMixin, {
    classNames: ['list-group-item'],

    actions: {
        deleteItem: function(item) {
            this.sendAction("deleteItem", item);
        }
    },

    dragStart: function(event) {
        this._super();

        var dragData = {
            id: this.get('item')
        };

        event.dataTransfer.setData(
            'application/json',                     // first argument is data type
            JSON.stringify( dragData ) // can only transfer strings
        );
    },

    dragOver: function(event) {
        var elementId = this.get('elementId');
        var y = event.originalEvent.pageY - Ember.$("#" + elementId).offset().top;
        var elemHeight = Ember.$("#" + elementId).height();

        if (y <= (elemHeight / 2)) {
            this.set('isDragOverTop', true);
            this.set('isDragOverBottom', false);
        } else if (y > (elemHeight / 2)) {
            this.set('isDragOverTop', false);
            this.set('isDragOverBottom', true);
        }

        event.preventDefault();

        console.log("Dragging over: " + this.get('isDragOver') + " item: " + this.get('item') + " :: " + event + " top: " + this.get('isDragOverTop') + " bottom: " + this.get('isDragOverBottom'));
    },

    drop: function(event) {
        this._super();

        var elementId = this.get('elementId');
        var y = event.originalEvent.pageY - Ember.$("#" + elementId).offset().top;
        var elemHeight = Ember.$("#" + elementId).height();
        var droppedTop = true;

        if  (y > (elemHeight / 2)) {
            droppedTop = false;
        }

        console.log("y: " + y + " elemHeight: " + elemHeight);

        var dragData = JSON.parse( event.dataTransfer.getData('application/json') );

        console.log("item dropped: " + dragData);
        console.log('item dropped on: ' + this.get('item'));

        this.sendAction('droppedItem', dragData, this.get('item'), droppedTop);
    },

    isDragOverObserver: function() {
        if (this.get('isDragging')) {
            //No styling if this is the item being dragged!
            return;
        }
        var elementId = this.get('elementId');
        if (this.get('isDragOver') && this.get('isDragOverTop')) {
            Ember.$("#" + elementId).addClass('draggingOverTop');
            Ember.$("#" + elementId).removeClass('draggingOverBottom');
        } else if (this.get('isDragOver') && this.get('isDragOverBottom')) {
            Ember.$("#" + elementId).addClass('draggingOverBottom');
            Ember.$("#" + elementId).removeClass('draggingOverTop');
        } else {
            Ember.$("#" + elementId).removeClass('draggingOverTop');
            Ember.$("#" + elementId).removeClass('draggingOverBottom');
        }
    }.observes('isDragging', 'isDragOver', 'isDragOverTop', 'isDragOverBottom')
});
