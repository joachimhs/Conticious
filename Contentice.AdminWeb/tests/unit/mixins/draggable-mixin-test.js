import Ember from 'ember';
import DraggableMixinMixin from '../../../mixins/draggable-mixin';
import { module, test } from 'qunit';

module('Unit | Mixin | draggable mixin');

// Replace this with your real tests.
test('it works', function(assert) {
  var DraggableMixinObject = Ember.Object.extend(DraggableMixinMixin);
  var subject = DraggableMixinObject.create();
  assert.ok(subject);
});
