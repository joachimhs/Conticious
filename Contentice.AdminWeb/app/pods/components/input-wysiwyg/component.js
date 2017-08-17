import Ember from 'ember';

export default Ember.Component.extend({
    classNames: ['wysiwyg-editor'],
    btnSize: 'btn-sm',
    height: '600',

    lang: "en",

    willDestroyElement: function() {
        this.$('textarea').destroy();
    },

    didInsertElement: function() {
        var btnSize = this.get('btnSize');
        var height = this.get('height');

        this.$('textarea').summernote({
            lang: this.get('lang'),
            height: height,
            toolbar: [
                ['style', ['style', 'bold', 'italic', 'underline', 'clear']],
                ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['height', ['height']],
                ['insert', ['link']],
                ['table', ['table']],
                ['undoredo', ['undo', 'redo']],
                ['misc', ["fullscreen"]]
            ]
        });

        var content = this.get('content');
        //this.$('textarea').summernote('code', content);
        var placeholder = this.get("placeholder");
        if(placeholder){
            Ember.$('[contentEditable=true]').attr("data-placeholder", placeholder);
        }

        Ember.$('.btn').addClass(btnSize);
    },

    keyUp: function() {
        this.doUpdate();
    },

    click: function() {
        this.doUpdate();
    },

    doUpdate: function() {
        var content = this.$('.note-editable').html();
        this.set('content', content);
    },

    contentObserver: function() {
        console.log('content observer!');

        var hasFocus = Ember.$('.wysiwyg-editor .note-editable').eq(0).is(':focus');

        console.log('hasFocus: ' + hasFocus);

        if (!hasFocus) {
            var content = this.get('content');
            Ember.$('.wysiwyg-editor textarea').summernote('code', content);
        }
    }.observes('content').on('init')
});
