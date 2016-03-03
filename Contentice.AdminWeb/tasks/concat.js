module.exports = {
    options: {
        process: function(src,filepath){
            return '/* ##--->: '+filepath+' */'+'\n'+src+'\n';
        }
    },

	app: {
        src: [
            'js/app/*.js',
            'js/app/mixins/*.js',
            'js/app/**/*.js'],
        dest: 'dist/<%= pkg.name %>.js'
	},

    lib: {
        src: [
            'js/scripts/jquery-1.10.2.min.js',
            'js/scripts/summernote.min.js',
            'js/scripts/jquery.ui.widget.js',
            'js/scripts/jquery.iframe-transport.js',
            'js/scripts/jquery.fileupload.js',
            'js/scripts/bootstrap-markdown.js',
            'js/scripts/bootstrap.min.js',
            'js/scripts/ember-template-compiler-1.13.10.js',
            'js/scripts/ember-1.13.7.js',
            'js/scripts/ember-data.prod-1.13.14.js'
        ],
        dest: 'dist/<%= pkg.name %>-lib.js'
    },

    tests: {
        src: [
            'tests/unit/**/*.js'
        ],
        dest: 'tests/dist/<%= pkg.name %>-tests.js'
    }
};