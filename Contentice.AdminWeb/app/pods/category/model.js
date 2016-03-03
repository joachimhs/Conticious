import DS from 'ember-data';

export default DS.Model.extend({
  numberOfSubcategories: DS.attr('number'),
  defaultFields: DS.hasMany('categoryField', {async: true}),
  isPublic: DS.attr('boolean'),

  fieldSortProperties: ['name:asc'],
  sortedFields: Ember.computed.sort('defaultFields', 'fieldSortProperties'),
});
