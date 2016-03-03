import DS from 'ember-data';

export default DS.Model.extend({
    name: DS.attr('string'),
    content: DS.attr('string'),
    fields: DS.hasMany('subcategory-field', {async: true}),

    fieldSortProperties: ['name:asc'],
    sortedFields: Ember.computed.sort('fields', 'fieldSortProperties')
});
