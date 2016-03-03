import Ember from "ember";

export default Ember.Helper.helper(function([leftSide, rightSide]) {
  if (leftSide === null || leftSide === undefined || rightSide === null || rightSide === undefined) {
    return false;
  } else if (typeof leftSide.get === 'function' && typeof rightSide.get === 'function') {
    return leftSide.get('id') === rightSide.get('id');
  } else if (typeof leftSide.get === 'function' && typeof rightSide.get !== 'function') {
    return leftSide.get('id') === rightSide;
  } else {
    return leftSide === rightSide;
  }
});