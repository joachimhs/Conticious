import DS from 'ember-data';

export default DS.Model.extend({
  domains: DS.hasMany('domain')
});
