/* ##--->: js/app/app.js */
var Conticious = Ember.Application.create({
    createCookie: function(name, value, days) {
        var expires = null;

        if (days) {
            var date = new Date();
            date.setTime(date.getTime()+(days*24*60*60*1000));
            expires = "; expires="+date.toGMTString();
        }
        else expires = "";
        document.cookie = name+"="+value+expires+"; path=/";
    },

    readCookie:function (name) {
        var nameEQ = name + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
        }
        return null;
    },

    eraseCookie:function (name) {
        this.createCookie(name, "", -1);
    }
});

/* ##--->: js/app/router.js */
Conticious.Router.map(function() {
    this.resource("categories", {path: "/"}, function() {
        this.resource('category', {path: "/category/:category_id"}, function() {
            this.resource('subcategory', {path: "/subcategory/:subcategory_id"}, function() {
                this.route('fields');
                this.route('preview');
            });
        });
    });
    this.resource("setting");
});

/* ##--->: js/app/store.js */
DS.RESTAdapter.reopen({
    namespace: 'json/admin'
});

Conticious.Store = DS.Store.extend({
    adapter:  "DS.RESTAdapter"
});

/* ##--->: js/app/mixins/dragable_mixin.js */
Conticious.DraggableViewMixin = Ember.Mixin.create({
    classNames: ['draggable'],
    classNameBindings: ['isDragging'],

    attributeBindings: ['draggable'],
    draggable: 'true', // must be the string 'true'
    isDraggable: true,

    dragStart: function(event) {
        this.set('isDragging', true);
    },

    dragEnd: function() {
        this.set('isDragging', false);
    }
});

/* ##--->: js/app/mixins/droppable_mixin.js */
Conticious.DroppableViewMixin = Ember.Mixin.create({
    classNames: ['droppable'],
    classNameBindings: ['isDragOver'],

    dragEnter: function() {
        this.set('isDragOver', true);
    },
    dragLeave: function() {
        this.set('isDragOver', false);
    },

    drop: function(event) {
        console.log("DROP");
        this.set('isDragOver', false);
        this.set('didReceiveDrop', true);
    },

    isDroppable: true

});

/* ##--->: js/app/categories/categories_controller.js */
Conticious.CategoriesController = Ember.ArrayController.extend({
    needs: ['category', 'user', "subcategory"],

    showNewCategoryField: false,
    showUploadField: false,
    newCategoryName: null,
    newId: null,

    actions: {
        showNewCategory: function() {
            this.set('showNewCategoryField', true);
        },

        cancelNewCategory: function() {
            this.set('showNewCategoryField', false);
            this.set('newCategoryName', null);
        },

        saveNewCategory: function() {
            var categoryId = this.get('newCategoryName');

            var category = this.store.createRecord('category', {
                id: categoryId
            });

            category.save();

            this.set('showNewCategoryField', false);
            this.set('newCategoryName', null);
        },

        showFileUpload: function() {
            this.set('showUploadField', true);
        },

        cancelFileUpload: function() {
            this.set('showUploadField', false);
        },

        userChanged: function() {
            console.log('user changed. Refreshing categories!');
            this.set('model', this.store.find('category'));
        },

        doEditSubcategory: function() {
            console.log('editing subcategory: ' + this.get('controllers.subcategory.model.id'));
            if (this.get('controllers.subcategory.model')) {
                this.set('controllers.subcategory.model.isEditing', true);

                Ember.run.schedule("afterRender", function() {
                    $("#editSubcategoryArea").hide();
                    $("#editSubcategoryArea").slideDown();
                });
            }
        },

        finishEditSubcategory: function() {
            console.log('finishing subcategory editing... ' + this.get('newId'));
            if (this.get('controllers.subcategory.model')) {

                var controller = this;
                $("#editSubcategoryArea").slideUp(function() {
                    controller.set('controllers.subcategory.model.deleteFirstStep', false);
                    controller.set('controllers.subcategory.model.isEditing', false);
                    controller.set('newId', '');
                });
            }
        },

        deleteSubcategoryFirstStep: function(subcategory) {
            subcategory.set('deleteFirstStep', true);
        },

        deleteSubcategoryConfirm: function(subcategory) {
            subcategory.set('deleteFirstStep', false);
            console.log('DELETING SUBCATEGORY: ' + subcategory.get('id'));
            this.set('controllers.subcategory.model.isEditing', false);
            subcategory.deleteRecord();
            subcategory.save();

            this.transitionToRoute("categories");
            controller.reloadCategories();
        },

        renameSubcategory: function(subcategory) {
            var self = this;
            console.log(this.get('newId'));

            var newId = this.get('controllers.category.model.id') + "_" + this.get('newId');
            var oldId = subcategory.get('id');

            console.log("Renaming " + oldId + " to " + newId);

            var url = "/json/admin/renameSubcategory/" + oldId + "/" + newId;

            $.ajax({
                type: "POST",
                url: url,
                data: "",
                success: function() {
                    self.transitionToRoute("categories");
                    self.reloadCategories();
                },
                dataType: "json"
            });

            /* This is the old way, which needs to handle fields, etc. This is now implemnted with a single
             * call to the server, as renaming is a lot easier there!

            if (newId && this.get('controllers.category.model.id')) {
                var name = newId;
                newId = this.get('controllers.category.model.id') + "_" + newId;
                var newSubcategory = this.store.createRecord('subcategory', {
                    id: newId,
                    name: name,
                    content: subcategory.get('content'),
                    fields: []

                });

                var fieldsToCopy = [];

                subcategory.get('fields').forEach(function(field) {
                    fieldsToCopy.pushObject(field);
                });

                subcategory.deleteRecord();
                subcategory.save();

                console.log('old subcategory deleted, save new subcategory');

                var controller = this;
                newSubcategory.save().then(function(d) {
                    console.log('saving fields');
                    fieldsToCopy.forEach(function(fieldToCopy) {
                        console.log('fields to save: ' + newSubcategory.get('fields.length'));
                       newSubcategory.get('fields').forEach(function(oldField) {
                           console.log("verifying: " + oldField.get('name') + " agains: " + ieldToCopy.get('name'));
                           if (oldField.get('name') === fieldToCopy.get('name')) {
                               console.log("updating: " + oldField.get('id'));
                               oldField.set('value', fieldToCopy.get('value'));
                               oldField.save();
                           }
                       });
                    });


                });
            } else {
                console.log('newId is not valid: ' + newId);
            }*/

        }
    },

    reloadCategories: function() {
        console.log('reloadCategories');
        console.log(this.get('model'));

        this.store.find('category');

        this.get('model').forEach(function(category) {
            console.log('category: ' + category.get('id'));
            if (category.get('id') === 'uploads') {
                category.reload();
            }
        });

        //this.store.find('category', 'uploads').reload();
    },

    domainHaveUpload: function() {
        return this.get('controllers.user.domain.uploadUrl') !== undefined && this.get('controllers.user.domain.uploadUrl') !== null;
    }.property('controllers.user.domain.uploadUrl'),

    observeID: function(){
        this.send("userChanged");
    }.observes("controllers.user.content.id")
});

/* ##--->: js/app/categories/categories_route.js */
Conticious.CategoriesRoute = Ember.Route.extend({
    model: function() {
        return this.store.find('category');
    }
});

/* ##--->: js/app/categories/categories_view.js */
Conticious.CategoriesView = Ember.View.extend({
    isSelected: function() {
        console.log(this.get('model.id'));
        console.log(this.get('controller.model.id'));
        console.log(this.get('controller'));
        console.log(this.get('context'));
        console.log(this);

        return this.get('model.id') === this.get('controller.model.id');
    }.property('controller.model')
});

/* ##--->: js/app/category/category_controller.js */
Conticious.CategoryController = Ember.ObjectController.extend({

});

/* ##--->: js/app/category/category_index_controller.js */
Conticious.CategoryIndexController = Ember.Controller.extend({
    needs: ['category', 'categories'],
    showNewFieldArea: false,
    showNewSubcategoryArea: false,

    showNewField: function() {
        return this.get('showNewFieldArea') || this.get('showNewSubcategoryArea');
    }.property('showNewFieldArea', 'showNewSubcategoryArea'),

    actions: {
        openNewField: function() {
            this.set('showNewFieldArea', true);
        },

        cancelNewField: function() {
            this.set('showNewFieldArea', false);
            this.set('newFieldName', null);
            this.set('newFieldType', null);
            this.set('newFieldRequired', false);
        },

        openNewSubcategory: function() {
            this.set('showNewSubcategoryArea', true);
        },

        cancelNewSubcategory: function() {
            this.set('showNewSubcategoryArea', false);
            this.set('newSubcategoryName', null);
        },

        saveCategory: function(category) {
            this.doSaveCategory(category);
        },

        saveCategoryField: function(categoryField) {
            categoryField.save();
        },

        revertCategoryField: function(categoryField) {
            categoryField.rollback();
        },

        deleteCategoryField: function(categoryField) {
            categoryField.deleteRecord();
            categoryField.save();
        }
    },

    doSaveCategory: function(category) {
        category.save();
        var defaultFields = category.get('defaultFields');
        if (defaultFields) {
            defaultFields.forEach(function(field) {
                if (field.get('isDirty') || field.get('isNew')) {
                    field.save();
                }
            });
        }

        var subcategories = category.get('subategories');
        if (subcategories) {
            subcategories.forEach(function(subcategory) {
                if (subcategory.get('isDirty') || subcategory.get('isNew')) {
                    subcategory.save();
                }
            });
        }
    },

    newFieldIsAssociation: function() {
        return this.get('newFieldType') && (this.get('newFieldType') === "toOne" || this.get('newFieldType') === "toMany");
    }.property('newFieldType'),

    init: function() {
        this._super();

        var fieldTypes = [];
        fieldTypes.pushObject('textarea');
        fieldTypes.pushObject('textfield');
        fieldTypes.pushObject('boolean');
        fieldTypes.pushObject('array');
        fieldTypes.pushObject('toOne');
        fieldTypes.pushObject('toMany');

        this.set('fieldTypes', fieldTypes);
    }
});

/* ##--->: js/app/category/category_index_route.js */
Conticious.CategoryIndexRoute = Ember.Route.extend({
    actions: {

        addNewField: function() {
            var category = this.modelFor('category');
            var newFieldName = this.get('controller.newFieldName');
            var newFieldType = this.get('controller.newFieldType');
            var newFieldRequired = this.get('controller.newFieldRequired');

            if (newFieldName) {
                var newField = this.store.createRecord('categoryField', {
                    id: category.get('id') + "_" + newFieldName,
                    name: newFieldName,
                    type: newFieldType,
                    required: newFieldRequired
                });

                category.get('defaultFields').pushObject(newField);

                newField.save().then(function(data) {
                    console.log('new field saved. saving category');
                    category.save();
                });
            }

            this.set('controller.newFieldName', null);
            this.set('controller.newFieldType', null);
            this.set('controller.showNewFieldArea', false);
        },

        addNewSubcategory: function() {
            var category = this.modelFor('category');
            var newSubcategoryName = this.get('controller.newSubcategoryName');

            if (newSubcategoryName) {
                var newSubcategory = this.store.createRecord('subcategory', {
                    id: category.get('id') + "_" + newSubcategoryName,
                    name: newSubcategoryName
                });

                category.get('subcategories').pushObject(newSubcategory);

                newSubcategory.save().then(function(data) {
                    console.log('newSubcategory saved. Saving category');
                    category.save().then(function(data) {
                        category.reload();
                        newSubcategory.reload();
                    });
                });

            }

            this.set('controller.newSubcategoryName', null);
            this.set('controller.showNewSubcategoryArea', false);
        }
    }
});

/* ##--->: js/app/category/category_route.js */
Conticious.CategoryRoute = Ember.Route.extend({
    model: function(category) {
        return this.store.find('category', category.category_id);
    }
});

/* ##--->: js/app/category/subcategory/fields/subcategory_fields_controller.js */
Conticious.SubcategoryFieldsController = Ember.ObjectController.extend({
    needs: ['category', 'subcategory', 'categories'],

    actions: {
        addSubcategoryToRelation: function(addToSubcategoryId, addToSubcategoryFieldId) {
            var self = this;

            var subcategoryId = this.get('controllers.subcategory.model.id');
            var url = "/json/admin/addSubcategoryToRelation/" + subcategoryId + "/" + addToSubcategoryId + "_" + addToSubcategoryFieldId;

            console.log("POSTING to: " + url);

            $.ajax({
                type: "POST",
                url: url,
                data: "",
                success: function() {
                    //self.transitionToRoute("categories");
                    //self.reloadCategories();
                    console.log("POSTED");
                    if (self.get('controllers.categories')) {
                        self.get('controllers.categories').reloadCategories();
                    }
                },
                dataType: "json"
            });
        }
    }
});

/* ##--->: js/app/category/subcategory/fields/subcategory_fields_route.js */
Conticious.SubcategoryFieldsRoute = Ember.Route.extend({
    actions: {
        saveSubcategoryField: function(field) {
            field.save();
        },

        revertSubcategoryField: function(field) {
            field.rollback();
        }
    },

    model: function() {
        var self = this;
        var subcategory = this.modelFor('subcategory');

        var cfs = Conticious.__container__.lookup('store:main').all('categoryField');
        var toManys = cfs.filter(function(cf) { if (cf.get('type') === 'toMany') { return true; } });

        var subcategoryType = subcategory.get('id').split("_");
        if (subcategoryType.length > 0) {
            subcategoryType = subcategoryType[0];
        }

        var relations = [];

        toManys.forEach(function(cf) {
            if (cf.get('relation') === subcategoryType) {
                var catId = null;
                var catField = null;

                var idParts = cf.get('id').split("_");
                if (idParts.length >= 2) {
                    catId = idParts[0];
                    catField = idParts[1];
                }

                if (catId && catField) {
                    console.log('Related to category: ' + catId + " through field: " + catField);

                    var relatedCategory = self.store.getById("category", catId);

                    console.log("category: " + relatedCategory.get('id'));
                    console.log(relatedCategory);

                    var relatedSubcategories = relatedCategory.get('subcategories');

                    console.log("subcategories: " + relatedSubcategories);

                    relations.pushObject(Ember.Object.create({
                        relatedToCategory: catId,
                        throughField: catField,
                        relatedSubcategories: relatedSubcategories
                    }));
                }
            }
        });

        subcategory.set('relatedCategoryFields', relations);

        return subcategory;
    }
});

/* ##--->: js/app/category/subcategory/preview/subcategory_preview_controller.js */
Conticious.SubcategoryPreviewController = Ember.ObjectController.extend({
    actions: {
        doSaveSubcategory: function(subcategory) {
            console.log('doSaveSubcategory: ' + subcategory.get('id'));
            if (subcategory.get('isDirty')) {
                subcategory.save();
            }
        }
    }
});

/* ##--->: js/app/category/subcategory/preview/subcategory_preview_route.js */
Conticious.SubcategoryPreviewRoute = Ember.Route.extend({
    model: function() {
        var subcategory = this.modelFor('subcategory');
        return subcategory;
    }
});


/* ##--->: js/app/category/subcategory/subcategory_index_controller.js */
Conticious.SubcategoryIndexController = Ember.ObjectController.extend({
    actions: {
        doSaveSubcategory: function(subcategory) {
            console.log('doSaveSubcategory: ' + subcategory.get('id'));
            if (subcategory.get('isDirty')) {
                subcategory.save();
            }
        }
    }
});

/* ##--->: js/app/category/subcategory/subcategory_route.js */
Conticious.SubcategoryRoute = Ember.Route.extend({
    model: function(subcategory) {
        return this.store.find('subcategory', subcategory.subcategory_id);
    }
});


/* ##--->: js/app/category/subcategory/subcategory_view.js */
Conticious.SubcategoryView = Ember.View.extend({
    modelObserver: function() {
        console.log('SubcategoryView ModelObserver');
        //this.rerender();
    }.observes('controller.model.id').on('init')
});

/* ##--->: js/app/category/subcategory/subcategroy_index_route.js */
Conticious.SubcategoryIndexRoute = Ember.Route.extend({
    model: function() {
        var subcategory = this.modelFor('subcategory');
        return subcategory;
    }
});

/* ##--->: js/app/category/subcategory_controller.js */
Conticious.SubcategoryController = Ember.ObjectController.extend({

});

/* ##--->: js/app/components/input_wysiwyg/wysiwyg.js */
Conticious.InputWysiwygComponent = Ember.Component.extend({
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
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['height', ['height']],
                ['insert', ['link']],
                ['table', ['table']],
                ['undoredo', ['undo', 'redo']]
            ]
        });

        var content = this.get('content');
        this.$('textarea').code(content);
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
            Ember.$('.wysiwyg-editor textarea').code(content);
        }
    }.observes('content').on('init')
});

/* ##--->: js/app/components/log_in/login_component.js */
Conticious.LogInComponent = Ember.Component.extend({
    actions: {
        loginButtonAction: function() {
            console.log('attempting to log in with username: ' + this.get('username') + ' and password: ' + this.get('password'));

            var data = {
                username: this.get('username'),
                password: this.get('password')
            };

            var component = this;

            $.ajax({
                type: "POST",
                url: "/json/admin/auth",
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function(res, status, xhr) {
                    console.log("success: " + JSON.stringify(res));
                    if (res.uuidAdminToken) {
                        component.set('uuidAdminToken', res.uuidAdminToken);
                    }
                }, error: function(res, status, err) {
                    console.log("error: " + JSON.stringify(res));
                    console.log("error: " + status + " error: " + err);
                }
            });
        }
    }
});

/* ##--->: js/app/components/pop_over/pop_over_component.js */
Conticious.PopOverComponent = Ember.Component.extend({
    classNames: ['glyphicon','glyphicon-question-sign','pull-right', 'popover-dismiss'],
    attributeBindings: ['dataToggle:data-toggle', 'title', 'dataContent:data-content'],
    tagName: 'span',

    didInsertElement: function() {
        var elementId = this.get('elementId');
        $("#" + elementId).popover({ trigger: 'hover' });
    }
});

/* ##--->: js/app/components/select_multiple/select_multiple_component.js */
Conticious.SelectMultipleComponent = Ember.Component.extend({

    sortProperties: ['id'],
    sortedItems: Ember.computed.sort('items', 'sortProperties'),

    actions: {
        addItem: function() {
            var selectedValue = this.get('selectedValue');
            var addedItems = this.get('addedItems');

            console.log('selectedValue: ' + selectedValue);
            console.log('addedItems: ' + addedItems);

            if (!addedItems) {
                addedItems = [];
                this.set('addedItems', addedItems);
            }

            addedItems.pushObject(selectedValue);

            this.get('model').send('becomeDirty');

            console.log('addedItems: ' + addedItems);
        },

        deleteItem: function(item) {
            console.log('Select Multiple Component deleteItem: ' + item);
            if (item) {
                this.get('addedItems').removeObject(item);
                this.get('model').send('becomeDirty');
            }
        },

        droppedItem: function(draggedId, droppedItem, droppedTop) {
            if (draggedId === droppedItem.get('id')) {
                return;
            }

            var draggedIndex = null;
            var draggedItem = null;
            var droppedIndex = null;

            for (var index = 0; index < this.get('addedItems.length'); index++) {
                if (this.get('addedItems').objectAt(index).get('id') === draggedId) {
                    draggedIndex = index;
                    draggedItem = this.get('addedItems').objectAt(index);
                }

                if (this.get('addedItems').objectAt(index).get('id') === droppedItem.get('id')) {
                    droppedIndex = index;
                }
            }

            if (draggedIndex > -1 && droppedIndex > -1) {
                if (!droppedTop) {
                    droppedIndex++;
                }

                if (draggedIndex === (droppedIndex - 1)) {
                    //nothing
                } else {
                    this.get('addedItems').removeAt(draggedIndex);
                    this.get('addedItems').insertAt(droppedIndex, draggedItem);
                    this.get('model').send('becomeDirty');
                }
            }


        }
    }
});

/* ##--->: js/app/components/select_multiple/select_multiple_item_component.js */
Conticious.SelectMultipleItemComponent = Ember.Component.extend(Conticious.DraggableViewMixin, Conticious.DroppableViewMixin, {
    classNames: ['list-group-item'],

    actions: {
        deleteItem: function(item) {
            this.sendAction("deleteItem", item);
        }
    },

    dragStart: function(event) {
        this._super();

        var dragData = {
            id: this.get('item.id')
        };

        event.dataTransfer.setData(
            'application/json',                     // first argument is data type
            JSON.stringify( dragData ) // can only transfer strings
        );
    },

    dragOver: function(event) {
        var elementId = this.get('elementId');
        var y = event.originalEvent.pageY - Ember.$("#" + elementId).offset().top;
        var elemHeight = Ember.$("#" + elementId).height();

        if (y <= (elemHeight / 2)) {
            this.set('isDragOverTop', true);
            this.set('isDragOverBottom', false);
        } else if (y > (elemHeight / 2)) {
            this.set('isDragOverTop', false);
            this.set('isDragOverBottom', true);
        }

        event.preventDefault();

        console.log("Dragging over: " + this.get('item.id') + " :: " + event);


    },

    drop: function(event) {
        this._super();

        var elementId = this.get('elementId');
        var y = event.originalEvent.pageY - Ember.$("#" + elementId).offset().top;
        var elemHeight = Ember.$("#" + elementId).height();
        var droppedTop = true;

        if  (y > (elemHeight / 2)) {
            droppedTop = false;
        }

        console.log("y: " + y + " elemHeight: " + elemHeight);



        var dragData = JSON.parse( event.dataTransfer.getData('application/json') );

        console.log("item dropped: " + dragData.id);
        console.log('item dropped on: ' + this.get('item.id'));

        this.sendAction('droppedItem', dragData.id, this.get('item'), droppedTop);
    },

    isDragOverObserver: function() {
        if (this.get('isDragging')) {
            //No styling if this is the item being dragged!
            return;
        }
        var elementId = this.get('elementId');
        if (this.get('isDragOver') && this.get('isDragOverTop')) {
            Ember.$("#" + elementId).addClass('draggingOverTop');
            Ember.$("#" + elementId).removeClass('draggingOverBottom');
        } else if (this.get('isDragOver') && this.get('isDragOverBottom')) {
            Ember.$("#" + elementId).addClass('draggingOverBottom');
            Ember.$("#" + elementId).removeClass('draggingOverTop');
        } else {
            Ember.$("#" + elementId).removeClass('draggingOverTop');
            Ember.$("#" + elementId).removeClass('draggingOverBottom');
        }
    }.observes('isDragging', 'isDragOver', 'isDragOverTop', 'isDragOverBottom')
});

/* ##--->: js/app/header/header_controller.js */
Conticious.HeaderController = Ember.Controller.extend({
    needs: ['user'],

    actions: {
        logOut: function() {
            Conticious.eraseCookie('uuidAdminToken');
            this.set('controllers.user.uuidAdminToken', null);
            Conticious.reset();
        }
    }
});

/* ##--->: js/app/models/category.js */
Conticious.Category = DS.Model.extend({
    subcategories: DS.hasMany('subcategory'),
    defaultFields: DS.hasMany('categoryField'),
    isPublic: DS.attr('boolean'),

    sortProperties: ['id'],
    sortedSubcategories: Ember.computed.sort('subcategories', 'sortProperties')
});

/* ##--->: js/app/models/category_field.js */
Conticious.CategoryField = DS.Model.extend({
    name: DS.attr('string'),
    type: DS.attr('string'),
    required: DS.attr('boolean'),
    relation: DS.attr('string'),

    isTextfield: function() {
        return this.get('type') === 'textfield';
    }.property('type'),

    isTextarea: function() {
        return this.get('type') === 'textarea';
    }.property('type'),

    isBoolean: function() {
        return this.get('type') === 'boolean';
    }.property('type'),

    isToOne: function() {
        return this.get('type') === 'toOne';
    }.property('type'),

    isToMany: function() {
        return this.get('type') === 'toMany';
    }.property('type')
});

/* ##--->: js/app/models/domain.js */
Conticious.Domain = DS.Model.extend({
    domainName: DS.attr('string'),
    webappName: DS.attr('string'),
    active: DS.attr('boolean'),
    minified: DS.attr('boolean'),
    uploadUrl: DS.attr('string'),
    uploadPath: DS.attr('string'),
    createCategory: DS.attr('string')
});

/* ##--->: js/app/models/setting.js */
Conticious.Setting = DS.Model.extend({
    domains: DS.hasMany('domain')
});

/* ##--->: js/app/models/subcategory.js */
Conticious.Subcategory = DS.Model.extend({
    name: DS.attr('string'),
    content: DS.attr('string'),
    fields: DS.hasMany('subcategoryField')
});

/* ##--->: js/app/models/subcategory_field.js */
Conticious.SubcategoryField = DS.Model.extend({
    name: DS.attr('string'),
    type: DS.attr('string'),
    required: DS.attr('boolean'),
    value: DS.attr('string'),
    relation: DS.belongsTo('category'),
    relations: DS.hasMany('subcategory', {async: true}),

    isTextfield: function() {
        return this.get('type') === 'textfield';
    }.property('type'),

    isTextarea: function() {
        return this.get('type') === 'textarea';
    }.property('type'),

    isBoolean: function() {
        return this.get('type') === 'boolean';
    }.property('type'),

    isArray: function() {
        return this.get('type') === "array";
    }.property('type'),

    isToOne: function() {
        return this.get('type') === 'toOne';
    }.property('type'),

    isToMany: function() {
        return this.get('type') === 'toMany';
    }.property('type')
});

/* ##--->: js/app/models/user.js */
Conticious.User = DS.Model.extend({
    username: DS.attr('string'),
    role: DS.attr('string')
});


/* ##--->: js/app/setting/setting_controller.js */
Conticious.SettingController = Ember.ObjectController.extend({
    needs: ['user'],

    actions: {
        addDomain: function() {
            var domains = this.get('domains');

            var newDomain = this.store.createRecord('domain', {

            });

            domains.pushObject(newDomain);
        },

        deleteDomain: function(domain) {
            domain.deleteRecord();
            this.get('model.domains').removeObject(domain);
        },

        saveChanges: function() {
            this.doSaveSettings();
        },

        userChanged: function() {
            console.log('user changed. Refreshing settings!');
            this.get('model').reload();
        },

        generateStatic: function(domain) {
            var full = location.protocol+'//'+location.hostname+(location.port ? ':'+location.port: '');

            var payload = {};
            payload.hostname = window.location.hostname;
            payload.port = location.port ? location.port: '';
            payload.domain = domain.get('domainName');

            console.log('hostname: '  + window.location.hostname);
            console.log('port: ');

            $.ajax("/json/admin/spg/" + domain.get('domainName'), {
                data: JSON.stringify(payload),
                contentType: 'application/json',
                type: 'POST',
                success: function () {
                    console.log('SUCCESS');
                }
            });
        }
    },

    doSaveSettings: function() {
        var domains = [];

        this.get('model.domains').forEach(function(domain) {
            domains.pushObject(domain);
        });


        var jsonRequest = {};
        jsonRequest.domains = domains;

        var controller = this;

        $.ajax("/json/admin/settings", {
            data: JSON.stringify(jsonRequest),
            contentType: 'application/json',
            type: 'POST',
            success: function () {
                console.log('SUCCESS');
                console.log(controller.get('model'));
                controller.get('model').reload();
            }
        });
    },

    observeID: function(){
        this.send("userChanged");
    }.observes("controllers.user.content.id")
});

/* ##--->: js/app/setting/setting_route.js */
Conticious.SettingRoute = Ember.Route.extend({
    model: function() {
        return this.store.find('setting', 'ConticiousSettings');
    }
});

/* ##--->: js/app/user/user_controller.js */
Conticious.UserController = Ember.Controller.extend({
    init: function() {
        console.log('UserController init');
        var controller = this;

        var cookie = Conticious.readCookie("uuidAdminToken");
        if (cookie) {
            this.set('uuidAdminToken', cookie);
        }
    },

    uuidObserver: function() {
        console.log('uuidAdminTokenObserver: ' + this.get('uuidAdminToken'));
        if (this.get('uuidAdminToken')) {
            console.log('Fetching Admin DopplerUser');

            var controller = this;
            this.store.find('user', this.get('uuidAdminToken')).then(function(data) {
                if (data.get('role') === 'admin' || data.get('role') === 'super') {
                    console.log('Creating uuidAdminToken cookie!');
                    Conticious.createCookie("uuidAdminToken", data.get('id'), 30);
                    controller.set('content', data);

                    controller.store.find('setting', 'ConticiousSettings').then(function(setting) {

                        if (setting.get('domains')) {
                            setting.get('domains').forEach(function(domain) {
                                if (domain.get('id') === window.location.hostname) {
                                    controller.set('domain', domain);
                                }
                            });
                        }
                    });


                } else {
                    console.log('UUID Token is no longer valid, erasing cookie!');
                    Conticious.eraseCookie("uuidAdminToken");
                }
            });
        }
    }.observes('uuidAdminToken').on('init'),

    isAdminLoggedIn: function() {
        var isAdmin = false;
        console.log('roleObserver: ' + this.get('content.role'));

        if (this.get('content.role') === 'admin' || this.get('content.role') === 'super') {
            isAdmin = true;
        }

        return isAdmin;
    }.property('content.role'),

    isSuperLoggedIn: function() {
        var isAdmin = false;
        console.log('roleObserver: ' + this.get('content.role'));

        if (this.get('content.role') === 'super') {
            isAdmin = true;
        }

        return isAdmin;
    }.property('content.role')
});

/* ##--->: js/app/views/file_reader_view.js */
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
                add: function (e, data) {
                    $('#progressText').html('Uploading: ' + data.files[0].name);
                    uploadButton.click(function () {
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

/* ##--->: js/app/views/markdown_text_area.js */
Conticious.MarkdownTextArea = Ember.TextArea.extend({
    didInsertElement: function() {
        var elementId = this.get('elementId');
        //$("#" + elementId).markdown({autofocus:false,savable:false});
    }
});

/* ##--->: js/app/views/menu_category_view.js */
Conticious.MenuCategoryView = Ember.View.extend({
    templateName: 'menu-category',
    selectedSortColumn: null,
    selectedFilterString: null,
    selectedFilterColumn: null,
    sortAndFilterShowing: false,
    numSubcategoriesShown: 0,
    showNewSubcategoryArea: false,
    newSubcategoryName: null,
    isExpanded: false,

    actions: {
        toggleSortAndFilter: function() {


            if (this.get('sortAndFilterShowing')) {
                var view = this;
                $("#sortAndFilterArea").slideUp(function() {
                    view.set('sortAndFilterShowing', false);
                });
            } else {
                this.set('sortAndFilterShowing', true);
                Ember.run.schedule("afterRender", function() {
                    $("#sortAndFilterArea").hide();
                    $("#sortAndFilterArea").slideDown();
                });
            }

            console.log('toggleSortAndFilter: ' + this.get('sortAndFilterShowing'));
        },

        openNewSubcategory: function() {
            if (!this.get('showNewSubcategoryArea')) {
                this.set('showNewSubcategoryArea', true);
                Ember.run.schedule("afterRender", function() {
                    $("#showNewSubcategoryArea").hide();
                    $("#showNewSubcategoryArea").slideDown();
                });
            }
        },

        cancelNewSubcategory: function() {
            this.hideAddSubcategory();
        },

        addNewSubcategory: function() {
            var category = this.get('category');
            var newSubcategoryName = this.get('newSubcategoryName');

            if (newSubcategoryName) {
                var newSubcategory = this.get('controller').store.createRecord('subcategory', {
                    id: category.get('id') + "_" + newSubcategoryName,
                    name: newSubcategoryName
                });

                category.get('subcategories').pushObject(newSubcategory);

                newSubcategory.save().then(function(data) {
                    console.log('newSubcategory saved. Saving category');
                    category.save().then(function(data) {
                        category.reload();
                        newSubcategory.reload();
                    });
                });

            }

            this.hideAddSubcategory();
        }
    },

    hideAddSubcategory: function() {
        var view = this;
        $("#showNewSubcategoryArea").slideUp(function() {
            view.set('showNewSubcategoryArea', false);
            view.set('newSubcategoryName', null);
        });
    },

    isSelected: function() {
        return this.get('category.id') === this.get('controller.controllers.category.model.id');
    }.property('controller.controllers.category.model.id'),

    /*isSelectedObserver: function() {
        var view = this;
        var isSelected = this.get('isSelected');

        console.log("isSelectedObserver: " + isSelected + " id: " + view.get('category.id'));
        if (isSelected) {
            console.log(view.get('elementId') + " .categoryList");
            view.set('isExpanded', true);
            $("#" + view.get('elementId') + " .categoryList").css({hidden: true});
            Ember.run.schedule("afterRender", function() {
                $("#" + view.get('elementId') + " .categoryList").hide().slideDown(function() {
                    console.log('setting isExpanded to true');

                }, 600);
            });

        } else {
            $("#" + view.get('elementId') + " .categoryList").slideUp(function() {
                view.set('isExpanded', false);
            }).hide();
        }
    }.observes('isSelected'),*/

    subcategoryId: function() {
        return this.get('elementId') + "_subcategoryArea";
    }.property('elementId'),

    selectedSortColumnObserver: function() {
        console.log('New Sort Columnn: ' + this.get('selectedSortColumn'));
        this.sortOrFilter();
    }.observes('selectedSortColumn'),

    selectedFilterStringObserver: function() {
        console.log('New Filter String: ' + this.get('selectedFilterString'));
        this.sortOrFilter();
    }.observes('selectedFilterString'),

    selectedFilterColumnObserver: function() {
        console.log('New Filter Column: ' + this.get('selectedFilterColumn'));
        this.sortOrFilter();
    }.observes('selectedFilterColumn'),

    subcategoriesObserver: function() {
        this.set('sortedSubcategories', this.get('category.subcategories'));
    }.observes('category.subcategories').on('init'),

    sortOrFilter: function() {
        console.log('Sorting subcategories');
        var columnToSortBy = this.get('selectedSortColumn');
        var columnToFilterBy = this.get('selectedFilterColumn');
        var columnFilter = this.get('selectedFilterString');

        var subcategories = [];
        var sortProp = 'id';

        if (columnToSortBy && subcategories) {
            sortProp = "sortValue";
        }

        this.get('category.subcategories').forEach(function(subcat) {
            var showColumn = true;

            subcat.get('fields').forEach(function(field) {
                if (field.get('name') === columnToSortBy) {
                    subcat.set('sortValue', field.get('value'));
                }

                if (columnToFilterBy && columnFilter && field.get('name') === columnToFilterBy) {
                    showColumn = field.get('value') && field.get('value').indexOf(columnFilter) > -1;
                    console.log('    Filtering away: ' + subcat.get('id'));
                } else if (columnToFilterBy === null && columnFilter) {
                    showColumn = subcat.get('id').indexOf(columnFilter) > -1;
                }
            });

            if (!columnToSortBy) {
                subcat.set('sortValue', subcat.get('id'));
            }

            if (showColumn) {
                subcategories.push(subcat);
            }
        });

        console.log('sortProp: ' + sortProp);
        console.log(sortProp);

        var sortedResult = Em.ArrayProxy.createWithMixins(
            Ember.SortableMixin,
            { content:subcategories, sortProperties: ['sortValue'] }
        );

        console.log(sortedResult.getEach('id'));

        this.set('numSubcategoriesShown', subcategories.get('length'));

        this.set('sortedSubcategories', sortedResult);
    },

    sortedSubcategoriesObserver: function() {

    }.observes('sortedSubcategories.@each.id')
});

/* ##--->: js/app/views/menu_subcategory_view.js */
Conticious.MenuSubcategoryView = Ember.View.extend({
    templateName: 'menu-subcategory',

    isSelected: function() {
        return this.get('subcategory.id') === this.get('controller.controllers.subcategory.model.id');
    }.property('controller.controllers.subcategory.model.id'),

    isEditing: function() {
        return this.get('isSelected') && this.get('subcategory.isEditing');
    }.property('isSelected', 'subcategory.isEditing')

    /*,

     willDestroyElement: function() {
     console.log('MenuSubcategoryView willDestroyElement');
     var clone = this.$().clone();
     this.$().parent().append(clone);
     clone.slideUp();
     }*/
});
