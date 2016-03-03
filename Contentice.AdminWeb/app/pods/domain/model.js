import DS from 'ember-data';

export default DS.Model.extend({
  domainName: DS.attr('string'),
  webappName: DS.attr('string'),
  active: DS.attr('boolean'),
  minified: DS.attr('boolean'),
  uploadUrl: DS.attr('string'),
  uploadPath: DS.attr('string'),
  createCategory: DS.attr('string'),
  postProcessor: DS.belongsTo('post-processor')
});
