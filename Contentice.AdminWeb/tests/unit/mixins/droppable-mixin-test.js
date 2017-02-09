import Ember from 'ember';
import DroppableMixinMixin from '../../../mixins/droppable-mixin';
import { module, test } from 'qunit';

module('Unit | Mixin | droppable mixin');

// Replace this with your real tests.
test('it works', function(assert) {
  var DroppableMixinObject = Ember.Object.extend(DroppableMixinMixin);
  var subject = DroppableMixinObject.create();
  assert.ok(subject);
});
