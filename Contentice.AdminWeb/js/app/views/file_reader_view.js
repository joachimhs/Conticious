Conticious.FileReaderView = Ember.View.extend({
    tagName: 'input',
    attributeBindings: ['type', 'id', 'name'],
    type: 'file',

    didInsertElement: function() {
        var view = this;
        Ember.run.schedule("afterRender", function() {
            var uploadButton = $("#uploadFileButton");
            var uploadUrl = view.get('controller.controllers.user.domain.uploadUrl');
            $('#' + view.get('elementId')).fileupload({
                url: uploadUrl,
                dataType: 'json',
                autoUpload: true,
                add: function (e, data) {
                    console.log('add...');
                    $('#progressText').html('Uploading: ' + data.files[0].name);
                    $("#uploadFileButton").off('click').on('click', function () {
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
                    view.get('controller').reloadCategories();

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