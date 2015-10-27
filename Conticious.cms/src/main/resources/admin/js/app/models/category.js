Conticious.Category = DS.Model.extend({
    subcategories: DS.hasMany('subcategory'),
    defaultFields: DS.hasMany('categoryField', {async: true}),
    isPublic: DS.attr('boolean'),

    sortProperties: ['id'],
    sortedSubcategories: Ember.computed.sort('subcategories', 'sortProperties')
});