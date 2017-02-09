import Ember from 'ember';

export default Ember.Component.extend({
    sortProperties: ['id:asc'],
    sortedItems: Ember.computed.sort('items', 'sortProperties'),
    selectedValue: null,

    actions: {
        addItem: function() {
            var selectedValue = this.get('selectedValue');
            var addedItems = this.get('addedItems');

            console.log('selectedValue: ' + selectedValue);
            console.log('addedItems: ' + addedItems);


            if (!selectedValue && this.get('sortedItems.length')) {
                selectedValue = this.get('sortedItems').objectAt(0);
            }

            if (typeof selectedValue.get === 'function' && selectedValue.get('id')) {
                selectedValue = selectedValue.get('id');
            }
            console.log('selectedValue: ' + selectedValue);

            if (!addedItems) {
                addedItems = [];
                this.set('addedItems', addedItems);
            }

            addedItems.pushObject(selectedValue);

            this.get('model').send('becomeDirty');

            console.log('addedItems: ' + addedItems);
        },

        deleteItem: function(item) {
            console.log('Select Multiple Component deleteItem: ' + item);
            if (item) {
                this.get('addedItems').removeObject(item);
                this.get('model').send('becomeDirty');
            }
        },

        droppedItem: function(draggedId, droppedItem, droppedTop) {
            if (draggedId === droppedItem) {
                return;
            }

            var draggedIndex = null;
            var draggedItem = null;
            var droppedIndex = null;

            for (var index = 0; index < this.get('addedItems.length'); index++) {
                var droppedId = this.get('addedItems').objectAt(index);
                console.log('droppedId: ' + droppedId + " draggedId: " + draggedId.id);
                if (droppedId === draggedId.id) {
                    draggedIndex = index;
                    draggedItem = this.get('addedItems').objectAt(index);
                }

                if (droppedId === droppedItem) {
                    droppedIndex = index;
                }
            }

            if (draggedIndex > -1 && droppedIndex > -1) {
                if (!droppedTop) {
                    droppedIndex++;
                }

                if (droppedIndex > this.get('addedItems.length')) {
                    console.log('dragging to bottom');
                    droppedIndex = this.get('addedItems.length') - 1;
                }

                if (draggedIndex === (droppedIndex - 1)) {
                    //nothing
                } else {
                    console.log('draggedIndex: ' + draggedIndex);
                    console.log('droppedIndex: ' + droppedIndex);
                    console.log('draggedItem: ' + draggedItem);
                    this.get('addedItems').removeAt(draggedIndex);
                    this.get('addedItems').insertAt(droppedIndex-1, draggedItem);
                    this.get('model').send('becomeDirty');
                }
            }


        }
    }
});
