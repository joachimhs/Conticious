Conticious.Subcategory = DS.Model.extend({
    name: DS.attr('string'),
    content: DS.attr('string'),
    fields: DS.hasMany('subcategoryField', {async: true})
});