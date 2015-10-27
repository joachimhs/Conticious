function config(name) {
    return require('./tasks/' + name + '.js');
}

module.exports = function (grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        concat: config('concat'),
        jshint: config('jshint'),
        emberhandlebars: config('emberhandlebars'),
        uglify: config('uglify'),
        server: config('server'),
        watch: {
            files: ['templates/**/*.hbs', 'js/app/**/*.js', 'tests/unit/**/*.js'],
            tasks: ['emberTemplates', 'concat']
        }
    });

    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-ember-template-compiler');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-watch');

    grunt.registerTask('dist', ['jshint', 'emberhandlebars', 'concat', 'uglify']);
    grunt.registerTask('default', ['watch']);
};
