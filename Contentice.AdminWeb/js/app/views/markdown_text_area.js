Conticious.MarkdownTextArea = Ember.TextArea.extend({
    didInsertElement: function() {
        var elementId = this.get('elementId');
        //$("#" + elementId).markdown({autofocus:false,savable:false});
    }
});