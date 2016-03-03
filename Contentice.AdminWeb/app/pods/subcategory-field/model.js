import DS from 'ember-data';

export default DS.Model.extend({
    name: DS.attr('string'),
    type: DS.attr('string'),
    required: DS.attr('boolean'),
    value: DS.attr('string'),
    relation: DS.belongsTo('category'),
    addedRelations: DS.attr('raw'),//('subcategory'),

    addedRelationsObserver: function() {
        console.log('-----');
        console.log(this.get('addedRelations.length'));
        console.log('-----');
    }.observes('addedRelations.length').on('init'),

    isTextfield: function() {
        return this.get('type') === 'textfield';
    }.property('type'),

    isTextarea: function() {
        return this.get('type') === 'textarea';
    }.property('type'),

    isBoolean: function() {
        return this.get('type') === 'boolean';
    }.property('type'),

    isArray: function() {
        return this.get('type') === "array";
    }.property('type'),

    isToOne: function() {
        return this.get('type') === 'toOne';
    }.property('type'),

    isToMany: function() {
        return this.get('type') === 'toMany';
    }.property('type')
});
