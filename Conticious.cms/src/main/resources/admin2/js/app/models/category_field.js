Conticious.CategoryField = DS.Model.extend({
    name: DS.attr('string'),
    type: DS.attr('string'),
    required: DS.attr('boolean'),
    relation: DS.attr('string'),

    isTextfield: function() {
        return this.get('type') === 'textfield';
    }.property('type'),

    isTextarea: function() {
        return this.get('type') === 'textarea';
    }.property('type'),

    isBoolean: function() {
        return this.get('type') === 'boolean';
    }.property('type'),

    isToOne: function() {
        return this.get('type') === 'toOne';
    }.property('type'),

    isToMany: function() {
        return this.get('type') === 'toMany';
    }.property('type')
});