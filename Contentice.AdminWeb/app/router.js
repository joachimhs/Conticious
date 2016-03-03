import Ember from 'ember';
import config from './config/environment';

var Router = Ember.Router.extend({
  location: config.locationType
});

Router.map(function() {
  this.route('categories', function() {
    this.route('category', {path: '/category/:category_id'}, function() {
      this.route('subcategory',{path: '/subcategory/:subcategory_id'}, function() {
        this.route('fields');
        this.route('preview');
      });
    });
  });
  this.route('setting');
});

export default Router;
