import Ember from 'ember';

export default Ember.Component.extend({
    tagName: 'input',
    attributeBindings: ['type', 'name', 'dataUrl:data-url'],
    type: 'file',

    didInsertElement: function() {
        var view = this;
        Ember.run.schedule("afterRender", function() {
            var uploadButton = $("#uploadFileButton");

            var uploadUrl = view.get('uploadUrl');
            $('#' + view.get('elementId')).fileupload({
                url: uploadUrl,
                dataType: 'json',
                add: function (e, data) {
                    $('#progressText').html('File to upload: ' + data.files[0].name);
                    uploadButton.click(function () {
                        console.log('data.submit...');
                        data.submit();
                    });
                },
                done: function (e, data) {
                    console.log('done');
                    console.log(data);
                    console.log(data.result.filename);

                    //view.set('controller.user.photo', data.result.filename);

                    $('#progress .bar').css(
                        'width',
                        '0%'
                    );

                    console.log('Reloading Uploads');
                    $('#progressText').html('Done: ' + data.files[0].name);
                    view.sendAction('reloadCategories');

                },
                progressall: function (e, data) {
                    console.log('progressall');
                    console.log(data);
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#progress .bar').css(
                        'width',
                        progress + '%'
                    );
                }
            }).prop('disabled', !$.support.fileInput)
                .parent().addClass($.support.fileInput ? undefined : 'disabled');
        });
    }
});
