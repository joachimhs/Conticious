Conticious.SelectMultipleItemComponent = Ember.Component.extend(Conticious.DraggableViewMixin, Conticious.DroppableViewMixin, {
    classNames: ['list-group-item'],

    actions: {
        deleteItem: function(item) {
            this.sendAction("deleteItem", item);
        }
    },

    dragStart: function(event) {
        this._super();

        var dragData = {
            id: this.get('item.id')
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

        console.log("Dragging over: " + this.get('item.id') + " :: " + event);


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

        console.log("item dropped: " + dragData.id);
        console.log('item dropped on: ' + this.get('item.id'));

        this.sendAction('droppedItem', dragData.id, this.get('item'), droppedTop);
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