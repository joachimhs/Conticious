Conticious.Category = DS.Model.extend({
    subcategories: DS.hasMany('subcategory'),
    defaultFields: DS.hasMany('categoryField'),
    isPublic: DS.attr('boolean')
});