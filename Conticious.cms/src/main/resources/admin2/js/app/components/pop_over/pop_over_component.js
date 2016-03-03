Conticious.PopOverComponent = Ember.Component.extend({
    classNames: ['glyphicon','glyphicon-question-sign','pull-right', 'popover-dismiss'],
    attributeBindings: ['dataToggle:data-toggle', 'title', 'dataContent:data-content'],
    tagName: 'span',

    didInsertElement: function() {
        var elementId = this.get('elementId');
        $("#" + elementId).popover({ trigger: 'hover' });
    }
});