/* ##--->: tests/unit/app_test.js */
var appController;

Conticious.rootElement = "#ember-testing";
Conticious.setupForTesting();
Conticious.injectTestHelpers();

module('controller:application test', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
    setup: function() {


        Ember.run(function() {

            appController = Conticious.__container__.lookup("controller:application");
            console.log('setup: appController: ' + appController);
        });
    },

    teardown: function() {
        //Ember.run(Conticious, 'destroy');
        Conticious.reset();
    }
});


// Replace this with your real tests.
test('it exists', function() {
    expect(1);

    visit('/');
    andThen(function() {
        appController = Conticious.__container__.lookup("controller:application");
        console.log("appController: " + appController);
        ok(appController, "Expecting non-null appController");
    });
    console.log('test: appController: ' + appController);
    //ok(appController, "Expecting non-null appController");
});
