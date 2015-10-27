Ember.TEMPLATES['application'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var buffer = '', stack1, helper, options, helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression;


  data.buffer.push("<div class=\"headerArea\">\n    ");
  data.buffer.push(escapeExpression((helper = helpers.render || (depth0 && depth0.render),options={hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data},helper ? helper.call(depth0, "header", options) : helperMissing.call(depth0, "render", "header", options))));
  data.buffer.push("\n</div>\n\n");
  stack1 = helpers._triageMustache.call(depth0, "outlet", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  return buffer;
  
});Ember.TEMPLATES['categories'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var stack1, escapeExpression=this.escapeExpression, self=this, helperMissing=helpers.helperMissing;

function program1(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n    <div class=\"wrap\" style=\"margin-top: 25px;\">\n        <div class=\"row\" style=\"height: 100%;\">\n            <div class=\"col-sm-12 col-md-4\">\n                ");
  stack1 = helpers['if'].call(depth0, "showNewCategoryField", {hash:{},hashTypes:{},hashContexts:{},inverse:self.program(4, program4, data),fn:self.program(2, program2, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n                ");
  stack1 = helpers['if'].call(depth0, "domainHaveUpload", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(6, program6, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n                <div style=\"margin-bottom: 10px;\">\n\n                </div>\n\n                ");
  stack1 = helpers.each.call(depth0, "category", "in", "controller", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(11, program11, data),contexts:[depth0,depth0,depth0],types:["ID","ID","ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n            </div>\n            <div class=\"col-sm-12 col-md-8\" style=\"height: 100%\">\n                ");
  stack1 = helpers._triageMustache.call(depth0, "outlet", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n            </div>\n        </div>\n    </div>\n");
  return buffer;
  }
function program2(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                    <table class=\"table\">\n                        <tr>\n                            <th colspan=\"2\" class=\"tableHeader\">Add new Category</th>\n                        </tr>\n                        <tr>\n                            <td colspan=\"2\">");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.TextField", {hash:{
    'valueBinding': ("newCategoryName"),
    'classNames': ("form-control"),
    'placeholder': ("Category Name")
  },hashTypes:{'valueBinding': "STRING",'classNames': "STRING",'placeholder': "STRING"},hashContexts:{'valueBinding': depth0,'classNames': depth0,'placeholder': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("</td>\n                        </tr>\n                        <tr>\n                            <td><button class=\"btn btn-primary form-control\" style=\"margin-right: 10px;\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "saveNewCategory", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Add Category</button></td>\n                            <td><button class=\"btn btn-default form-control\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "cancelNewCategory", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Cancel</button></td>\n                        </tr>\n                    </table>\n                ");
  return buffer;
  }

function program4(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                    <button class=\"btn btn-default form-control\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "showNewCategory", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Add Category</button>\n                ");
  return buffer;
  }

function program6(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n                    ");
  stack1 = helpers['if'].call(depth0, "showUploadField", {hash:{},hashTypes:{},hashContexts:{},inverse:self.program(9, program9, data),fn:self.program(7, program7, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n                ");
  return buffer;
  }
function program7(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                        <table class=\"table\">\n                            <tr>\n                                <th colspan=\"2\" class=\"tableHeader\">Upload File</th>\n                            </tr>\n                            <tr>\n                                <td colspan=\"2\">\n                                    ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Conticious.FileReaderView", {hash:{
    'name': ("photo")
  },hashTypes:{'name': "STRING"},hashContexts:{'name': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n                                    <div id=\"progress\">\n                                        <div id=\"progressText\" style=\"float: left;\"></div>\n                                        <div class=\"bar\" style=\"width: 0%;\"></div>\n\n                                    </div>\n                                </td>\n                            </tr>\n                            <tr>\n                                <td><button class=\"btn btn-primary form-control\" id=\"uploadFileButton\" style=\"margin-right: 10px;\">Upload</button></td>\n                                <td><button class=\"btn btn-default form-control\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "cancelFileUpload", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Cancel</button></td>\n                            </tr>\n                        </table>\n                    ");
  return buffer;
  }

function program9(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                        <button class=\"btn btn-default form-control\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "showFileUpload", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Upload File</button>\n                    ");
  return buffer;
  }

function program11(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                    ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Conticious.MenuCategoryView", {hash:{
    'category': ("category")
  },hashTypes:{'category': "ID"},hashContexts:{'category': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n                ");
  return buffer;
  }

function program13(depth0,data) {
  
  var buffer = '', helper, options;
  data.buffer.push("\n    <div class=\"wrap\" style=\"margin-top: 25px;\">\n        ");
  data.buffer.push(escapeExpression((helper = helpers['log-in'] || (depth0 && depth0['log-in']),options={hash:{
    'uuidAdminToken': ("controllers.user.uuidAdminToken")
  },hashTypes:{'uuidAdminToken': "ID"},hashContexts:{'uuidAdminToken': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "log-in", options))));
  data.buffer.push("\n    </div>\n");
  return buffer;
  }

  stack1 = helpers['if'].call(depth0, "controllers.user.isAdminLoggedIn", {hash:{},hashTypes:{},hashContexts:{},inverse:self.program(13, program13, data),fn:self.program(1, program1, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  else { data.buffer.push(''); }
  
});Ember.TEMPLATES['category'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var stack1;


  stack1 = helpers._triageMustache.call(depth0, "outlet", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  else { data.buffer.push(''); }
  
});Ember.TEMPLATES['category/index'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var buffer = '', stack1, escapeExpression=this.escapeExpression, self=this;

function program1(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n    ");
  stack1 = helpers['if'].call(depth0, "showNewFieldArea", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(2, program2, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n    ");
  stack1 = helpers['if'].call(depth0, "showNewSubcategoryArea", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(5, program5, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n");
  return buffer;
  }
function program2(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n        <table class=\"table\">\n        <table class=\"table\">\n            <tr>\n                <th colspan=\"5\" class=\"tableHeader\">Add new Field</th>\n            </tr>\n            <tr>\n                <td>");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.TextField", {hash:{
    'valueBinding': ("newFieldName"),
    'classNames': ("form-control"),
    'placeholder': ("Field Name")
  },hashTypes:{'valueBinding': "STRING",'classNames': "STRING",'placeholder': "STRING"},hashContexts:{'valueBinding': depth0,'classNames': depth0,'placeholder': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("</td>\n                <td>");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Select", {hash:{
    'contentBinding': ("fieldTypes"),
    'valueBinding': ("newFieldType"),
    'prompt': ("Select a field type"),
    'classNames': ("form-control")
  },hashTypes:{'contentBinding': "STRING",'valueBinding': "STRING",'prompt': "STRING",'classNames': "STRING"},hashContexts:{'contentBinding': depth0,'valueBinding': depth0,'prompt': depth0,'classNames': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("</td>\n                <td>\n                    ");
  stack1 = helpers['if'].call(depth0, "newFieldIsAssociation", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(3, program3, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n                </td>\n                <td><label class=\"checkbox-inline\">");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Checkbox", {hash:{
    'checkedBinding': ("newFieldRequired")
  },hashTypes:{'checkedBinding': "STRING"},hashContexts:{'checkedBinding': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("Required</label></td>\n                <td style=\"text-align: right;\">\n                    <button class=\"btn btn-primary\" style=\"margin-right: 10px;\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "addNewField", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Add Field</button>\n                    <button class=\"btn btn-default\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "cancelNewField", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Cancel</button>\n                </td>\n            </tr>\n        </table>\n    ");
  return buffer;
  }
function program3(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                        ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Select", {hash:{
    'contentBinding': ("controller.controllers.categories.model"),
    'value': ("field.relation"),
    'optionValuePath': ("content.id"),
    'optionLabelPath': ("content.id"),
    'prompt': ("Select Category"),
    'classNames': ("form-control")
  },hashTypes:{'contentBinding': "STRING",'value': "ID",'optionValuePath': "STRING",'optionLabelPath': "STRING",'prompt': "STRING",'classNames': "STRING"},hashContexts:{'contentBinding': depth0,'value': depth0,'optionValuePath': depth0,'optionLabelPath': depth0,'prompt': depth0,'classNames': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n                    ");
  return buffer;
  }

function program5(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n        <table class=\"table\">\n            <tr>\n                <th colspan=\"2\" class=\"tableHeader\">Add new Subcategory</th>\n            </tr>\n            <tr>\n                <td>");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.TextField", {hash:{
    'valueBinding': ("newSubcategoryName"),
    'classNames': ("form-control"),
    'placeholder': ("Subcategory Name")
  },hashTypes:{'valueBinding': "STRING",'classNames': "STRING",'placeholder': "STRING"},hashContexts:{'valueBinding': depth0,'classNames': depth0,'placeholder': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("</td>\n                <td style=\"text-align: right;\">\n                    <button class=\"btn btn-primary\" style=\"margin-right: 10px;\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "addNewSubcategory", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Add Subcategory</button>\n                    <button class=\"btn btn-default\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "cancelNewSubcategory", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Cancel</button>\n                </td>\n            </tr>\n        </table>\n    ");
  return buffer;
  }

function program7(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n    <div class=\"actionButtons\">\n        <button class=\"btn btn-default\" style=\"margin-right: 10px;\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "openNewField", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Add New Field</button>\n        <button class=\"btn btn-default\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "openNewSubcategory", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Add New Subcategory</button>\n    </div>\n");
  return buffer;
  }

function program9(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n        <tr>\n            <td>");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.TextField", {hash:{
    'valueBinding': ("field.name"),
    'classNames': ("form-control")
  },hashTypes:{'valueBinding': "STRING",'classNames': "STRING"},hashContexts:{'valueBinding': depth0,'classNames': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("</td>\n            <td>\n                ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Select", {hash:{
    'contentBinding': ("controller.fieldTypes"),
    'valueBinding': ("field.type"),
    'prompt': ("Select a field type"),
    'classNames': ("form-control")
  },hashTypes:{'contentBinding': "STRING",'valueBinding': "STRING",'prompt': "STRING",'classNames': "STRING"},hashContexts:{'contentBinding': depth0,'valueBinding': depth0,'prompt': depth0,'classNames': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n            </td>\n\n\n            <td>\n                ");
  stack1 = helpers['if'].call(depth0, "field.isToOne", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(10, program10, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n                ");
  stack1 = helpers['if'].call(depth0, "field.isToMany", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(10, program10, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n            </td>\n\n            <td><label class=\"checkbox-inline\">");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Checkbox", {hash:{
    'checkedBinding': ("field.required")
  },hashTypes:{'checkedBinding': "STRING"},hashContexts:{'checkedBinding': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("Required</label></td>\n            <td>\n                ");
  stack1 = helpers['if'].call(depth0, "field.isDirty", {hash:{},hashTypes:{},hashContexts:{},inverse:self.program(14, program14, data),fn:self.program(12, program12, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n            </td>\n        </tr>\n    ");
  return buffer;
  }
function program10(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                    ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Select", {hash:{
    'contentBinding': ("controller.controllers.categories.model"),
    'value': ("field.relation"),
    'optionValuePath': ("content.id"),
    'optionLabelPath': ("content.id"),
    'prompt': ("Select Category"),
    'classNames': ("form-control")
  },hashTypes:{'contentBinding': "STRING",'value': "ID",'optionValuePath': "STRING",'optionLabelPath': "STRING",'prompt': "STRING",'classNames': "STRING"},hashContexts:{'contentBinding': depth0,'value': depth0,'optionValuePath': depth0,'optionLabelPath': depth0,'prompt': depth0,'classNames': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n                ");
  return buffer;
  }

function program12(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                    <button class=\"btn btn-primary\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "saveCategoryField", "field", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(" style=\"width: 75px;\">Save</button>\n                    <button class=\"btn btn-default\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "revertCategoryField", "field", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(" style=\"width: 75px;\">Revert</button>\n                ");
  return buffer;
  }

function program14(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                    <button class=\"btn btn-danger\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "deleteCategoryField", "field", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(" style=\"width: 155px;\">Delete</button>\n                ");
  return buffer;
  }

function program16(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n    <button class=\"btn btn-primary\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "saveCategory", "controllers.category", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(">Save Category Settings</button>\n");
  return buffer;
  }

  stack1 = helpers['if'].call(depth0, "showNewField", {hash:{},hashTypes:{},hashContexts:{},inverse:self.program(7, program7, data),fn:self.program(1, program1, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n<div style=\"padding-top: 15px;\">\n\n</div>\n\n<table class=\"table\">\n    <tr>\n        <th>Field Name</th>\n        <th>Field Type</th>\n        <th>Relationship</th>\n        <th>Required</th>\n        <th>Field Action</th>\n    </tr>\n    ");
  stack1 = helpers.each.call(depth0, "field", "in", "controllers.category.defaultFields", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(9, program9, data),contexts:[depth0,depth0,depth0],types:["ID","ID","ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n</table>\n\n<h1>Category Settings</h1>\n\n");
  stack1 = helpers._triageMustache.call(depth0, "controllers.category.public", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push(" -\n<table class=\"table\">\n    <tr>\n        <th>Settings Name</th>\n        <th>Settings Value</th>\n    </tr>\n    <tr>\n        <td>Public</td>\n        <td>");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Checkbox", {hash:{
    'checkedBinding': ("controllers.category.isPublic")
  },hashTypes:{'checkedBinding': "STRING"},hashContexts:{'checkedBinding': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("</td>\n    </tr>\n</table>\n");
  stack1 = helpers['if'].call(depth0, "controllers.category.isDirty", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(16, program16, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  return buffer;
  
});Ember.TEMPLATES['components/input-wysiwyg'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var helper, options, helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression;


  data.buffer.push(escapeExpression((helper = helpers.textarea || (depth0 && depth0.textarea),options={hash:{
    'classNames': ("wysiwyg-textarea form-control"),
    'value': ("content")
  },hashTypes:{'classNames': "STRING",'value': "ID"},hashContexts:{'classNames': depth0,'value': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "textarea", options))));
  
});Ember.TEMPLATES['components/log-in'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var buffer = '', helper, options, helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression;


  data.buffer.push("<h1>Log in To Administer your account!</h1>\n\n<form role=\"form\">\n    <div class=\"form-group\">\n        <label for=\"inputUsername\">Username address</label>\n        ");
  data.buffer.push(escapeExpression((helper = helpers.input || (depth0 && depth0.input),options={hash:{
    'type': ("username"),
    'id': ("inputUsername"),
    'placeholder': ("Enter Username"),
    'value': ("username"),
    'class': ("form-control")
  },hashTypes:{'type': "STRING",'id': "STRING",'placeholder': "STRING",'value': "ID",'class': "STRING"},hashContexts:{'type': depth0,'id': depth0,'placeholder': depth0,'value': depth0,'class': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "input", options))));
  data.buffer.push("\n    </div>\n    <div class=\"form-group\">\n        <label for=\"inputPassword\">Password</label>\n        ");
  data.buffer.push(escapeExpression((helper = helpers.input || (depth0 && depth0.input),options={hash:{
    'type': ("password"),
    'id': ("inputPassword"),
    'placeholder': ("Enter Password"),
    'value': ("password"),
    'class': ("form-control")
  },hashTypes:{'type': "STRING",'id': "STRING",'placeholder': "STRING",'value': "ID",'class': "STRING"},hashContexts:{'type': depth0,'id': depth0,'placeholder': depth0,'value': depth0,'class': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "input", options))));
  data.buffer.push("\n    </div>\n    <button class=\"btn btn-default\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "loginButtonAction", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Submit</button>\n</form>");
  return buffer;
  
});Ember.TEMPLATES['components/pop-over'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var buffer = '';


  return buffer;
  
});Ember.TEMPLATES['components/select-multiple'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var buffer = '', stack1, helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression, self=this;

function program1(depth0,data) {
  
  var buffer = '', helper, options;
  data.buffer.push("\n        ");
  data.buffer.push(escapeExpression((helper = helpers['select-multipleItem'] || (depth0 && depth0['select-multipleItem']),options={hash:{
    'item': ("item"),
    'droppedItem': ("droppedItem"),
    'deleteItem': ("deleteItem")
  },hashTypes:{'item': "ID",'droppedItem': "STRING",'deleteItem': "STRING"},hashContexts:{'item': depth0,'droppedItem': depth0,'deleteItem': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "select-multipleItem", options))));
  data.buffer.push("\n    ");
  return buffer;
  }

  data.buffer.push("<div>\n    ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Select", {hash:{
    'contentBinding': ("sortedItems"),
    'valueBinding': ("selectedValue"),
    'optionValuePath': ("content"),
    'optionLabelPath': ("content.id"),
    'prompt': ("Add Item")
  },hashTypes:{'contentBinding': "STRING",'valueBinding': "STRING",'optionValuePath': "STRING",'optionLabelPath': "STRING",'prompt': "STRING"},hashContexts:{'contentBinding': depth0,'valueBinding': depth0,'optionValuePath': depth0,'optionLabelPath': depth0,'prompt': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n    <button class=\"btn btn-primary btn-xs pull-right\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "addItem", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Add</button>\n</div>\n\n<div class=\"list-group\">\n    ");
  stack1 = helpers.each.call(depth0, "item", "in", "addedItems", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(1, program1, data),contexts:[depth0,depth0,depth0],types:["ID","ID","ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n</div>\n");
  return buffer;
  
});Ember.TEMPLATES['components/select-multipleItem'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var buffer = '', stack1, escapeExpression=this.escapeExpression;


  stack1 = helpers._triageMustache.call(depth0, "item.id", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n<button class=\"btn btn-danger btn-xs pull-right\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "deleteItem", "item", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(">Delete</button>");
  return buffer;
  
});Ember.TEMPLATES['header'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var buffer = '', stack1, helper, options, self=this, helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression;

function program1(depth0,data) {
  
  
  data.buffer.push("\n            <img src=\"/admin/images/logo.png\" class=\"headerLogo\" />\n        ");
  }

function program3(depth0,data) {
  
  var buffer = '', stack1, helper, options;
  data.buffer.push("\n            ");
  stack1 = (helper = helpers['link-to'] || (depth0 && depth0['link-to']),options={hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(4, program4, data),contexts:[depth0],types:["STRING"],data:data},helper ? helper.call(depth0, "setting", options) : helperMissing.call(depth0, "link-to", "setting", options));
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n        ");
  return buffer;
  }
function program4(depth0,data) {
  
  
  data.buffer.push("\n                <button class=\"btn btn-default pull-right\">Settings</button>\n            ");
  }

function program6(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n            <button class=\"btn btn-default pull-right\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "logOut", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Log Out</button>\n        ");
  return buffer;
  }

  data.buffer.push("<div id=\"headerArea\">\n    <div class=\"wrap\">\n        ");
  stack1 = (helper = helpers['link-to'] || (depth0 && depth0['link-to']),options={hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(1, program1, data),contexts:[depth0],types:["STRING"],data:data},helper ? helper.call(depth0, "categories", options) : helperMissing.call(depth0, "link-to", "categories", options));
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n        ");
  stack1 = helpers['if'].call(depth0, "controllers.user.isSuperLoggedIn", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(3, program3, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n        ");
  stack1 = helpers['if'].call(depth0, "controllers.user.isAdminLoggedIn", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(6, program6, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n    </div>\n</div>\n");
  return buffer;
  
});Ember.TEMPLATES['menu-category'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var buffer = '', stack1, self=this, escapeExpression=this.escapeExpression, helperMissing=helpers.helperMissing;

function program1(depth0,data) {
  
  
  data.buffer.push("\n            &#x25BC;\n        ");
  }

function program3(depth0,data) {
  
  
  data.buffer.push("\n            &#x25B6;\n        ");
  }

function program5(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n                    ");
  stack1 = helpers['if'].call(depth0, "category.isLoaded", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(6, program6, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n                ");
  return buffer;
  }
function program6(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n                        ");
  stack1 = helpers._triageMustache.call(depth0, "view.sortedSubcategories.length", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push(" / ");
  stack1 = helpers._triageMustache.call(depth0, "category.subcategories.length", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n                    ");
  return buffer;
  }

function program8(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n        <div class=\"list-group-item text-right menuFilterItem\">\n            <span class=\"glyphicon glyphicon-filter pointer\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "toggleSortAndFilter", {hash:{
    'target': ("view")
  },hashTypes:{'target': "STRING"},hashContexts:{'target': depth0},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push("></span>&nbsp;\n            <span class=\"glyphicon glyphicon-plus pointer\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "openNewSubcategory", {hash:{
    'target': ("view")
  },hashTypes:{'target': "STRING"},hashContexts:{'target': depth0},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push("></span>&nbsp;\n        </div>\n\n        ");
  stack1 = helpers['if'].call(depth0, "view.showNewSubcategoryArea", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(9, program9, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n        ");
  stack1 = helpers['if'].call(depth0, "view.sortAndFilterShowing", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(11, program11, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n    ");
  return buffer;
  }
function program9(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n            <div id=\"showNewSubcategoryArea\" class=\"list-group-item text-left menuFilterItem\">\n                <h4 class=\"text-center\">Add new Subcategory</h4>\n                ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.TextField", {hash:{
    'valueBinding': ("view.newSubcategoryName"),
    'classNames': ("form-control"),
    'placeholder': ("Subcategory Name")
  },hashTypes:{'valueBinding': "STRING",'classNames': "STRING",'placeholder': "STRING"},hashContexts:{'valueBinding': depth0,'classNames': depth0,'placeholder': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n                <div style=\"margin-top: 5px;\">\n                    <button class=\"btn btn-primary\" style=\"margin-right: 10px;\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "addNewSubcategory", {hash:{
    'target': ("view")
  },hashTypes:{'target': "STRING"},hashContexts:{'target': depth0},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Add Subcategory</button>\n                    <button class=\"btn btn-default\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "cancelNewSubcategory", {hash:{
    'target': ("view")
  },hashTypes:{'target': "STRING"},hashContexts:{'target': depth0},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Cancel</button>\n                </div>\n            </div>\n    ");
  return buffer;
  }

function program11(depth0,data) {
  
  var buffer = '', helper, options;
  data.buffer.push("\n            <div id=\"sortAndFilterArea\" class=\"list-group-item menuFilterItem\">\n                <h4 class=\"text-center\">Sort and Filter</h4>\n                ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Select", {hash:{
    'contentBinding': ("category.defaultFields"),
    'valueBinding': ("view.selectedSortColumn"),
    'optionValuePath': ("content.name"),
    'optionLabelPath': ("content.name"),
    'prompt': ("Sort by ID"),
    'class': ("form-control smallBottomMargin")
  },hashTypes:{'contentBinding': "STRING",'valueBinding': "STRING",'optionValuePath': "STRING",'optionLabelPath': "STRING",'prompt': "STRING",'class': "STRING"},hashContexts:{'contentBinding': depth0,'valueBinding': depth0,'optionValuePath': depth0,'optionLabelPath': depth0,'prompt': depth0,'class': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n\n                ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Select", {hash:{
    'contentBinding': ("category.defaultFields"),
    'valueBinding': ("view.selectedFilterColumn"),
    'optionValuePath': ("content.name"),
    'optionLabelPath': ("content.name"),
    'prompt': ("Filter by ID"),
    'class': ("form-control smallBottomMargin")
  },hashTypes:{'contentBinding': "STRING",'valueBinding': "STRING",'optionValuePath': "STRING",'optionLabelPath': "STRING",'prompt': "STRING",'class': "STRING"},hashContexts:{'contentBinding': depth0,'valueBinding': depth0,'optionValuePath': depth0,'optionLabelPath': depth0,'prompt': depth0,'class': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n\n                ");
  data.buffer.push(escapeExpression((helper = helpers.input || (depth0 && depth0.input),options={hash:{
    'type': ("text"),
    'value': ("view.selectedFilterString"),
    'class': ("form-control smallBottomMargin")
  },hashTypes:{'type': "STRING",'value': "ID",'class': "STRING"},hashContexts:{'type': depth0,'value': depth0,'class': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "input", options))));
  data.buffer.push("\n            </div>\n        ");
  return buffer;
  }

function program13(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n    <div class=\"list-group categoryList\">\n        ");
  stack1 = helpers.each.call(depth0, "subcategory", "in", "view.sortedSubcategories", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(14, program14, data),contexts:[depth0,depth0,depth0],types:["ID","ID","ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n    </div>\n");
  return buffer;
  }
function program14(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n            ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Conticious.MenuSubcategoryView", {hash:{
    'subcategory': ("subcategory")
  },hashTypes:{'subcategory': "ID"},hashContexts:{'subcategory': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n        ");
  return buffer;
  }

  data.buffer.push("<div class=\"list-group\">\n\n    <div class=\"list-group-item categoryItem\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "selectCategory", "category", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(">\n        ");
  stack1 = helpers['if'].call(depth0, "view.isSelected", {hash:{},hashTypes:{},hashContexts:{},inverse:self.program(3, program3, data),fn:self.program(1, program1, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n        ");
  stack1 = helpers._triageMustache.call(depth0, "category.id", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n        <span class=\"pull-right\">\n            <span class=\"badge menuBadge\">\n                ");
  stack1 = helpers['if'].call(depth0, "view.isSelected", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(5, program5, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n            </span>\n        </span>\n    </div>\n\n    ");
  stack1 = helpers['if'].call(depth0, "view.isSelected", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(8, program8, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n</div>\n\n");
  stack1 = helpers['if'].call(depth0, "view.isSelected", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(13, program13, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n");
  return buffer;
  
});Ember.TEMPLATES['menu-subcategory'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var buffer = '', stack1, helper, options, escapeExpression=this.escapeExpression, self=this, helperMissing=helpers.helperMissing;

function program1(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n    <span style=\"margin-left: 18px;\"></span> ");
  stack1 = helpers._triageMustache.call(depth0, "subcategory.name", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n    ");
  stack1 = helpers['if'].call(depth0, "view.isSelected", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(2, program2, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n");
  return buffer;
  }
function program2(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n        <span class=\"glyphicon glyphicon-edit pull-right tinyMarginTop blackText\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "doEditSubcategory", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push("></span>\n    ");
  return buffer;
  }

function program4(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n    <div id=\"editSubcategoryArea\" class=\"list-group-item text-left subcategoryEditArea\">\n        <h4 class=\"text-center\">Edit Subcategory</h4>\n\n        ");
  stack1 = helpers['if'].call(depth0, "subcategory.deleteFirstStep", {hash:{},hashTypes:{},hashContexts:{},inverse:self.program(7, program7, data),fn:self.program(5, program5, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n\n\n    </div>\n");
  return buffer;
  }
function program5(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n            <table class=\"table\">\n                <tr>\n                    <td><button class=\"btn btn-danger form-control\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "deleteSubcategoryConfirm", "subcategory", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(">Confirm Delete Subcategory</button></td>\n                    <td><button class=\"btn btn-default\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "finishEditSubcategory", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Cancel</button></td>\n                </tr>\n            </table>\n        ");
  return buffer;
  }

function program7(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n            <table class=\"table\">\n                <tr>\n                    <td colspan=\"3\">\n                        ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.TextField", {hash:{
    'valueBinding': ("newId"),
    'classNames': ("form-control"),
    'placeholder': ("New Subcategory ID")
  },hashTypes:{'valueBinding': "STRING",'classNames': "STRING",'placeholder': "STRING"},hashContexts:{'valueBinding': depth0,'classNames': depth0,'placeholder': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n                    </td>\n                </tr>\n                <tr>\n                    <td><button class=\"btn btn-primary\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "renameSubcategory", "subcategory", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(">Save</button></td>\n                    <td><button class=\"btn btn-default\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "finishEditSubcategory", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Cancel</button></td>\n                    <td><button class=\"btn btn-danger\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "deleteSubcategoryFirstStep", "subcategory", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(">Delete Subcategory</button></td>\n                </tr>\n            </table>\n        ");
  return buffer;
  }

  stack1 = (helper = helpers['link-to'] || (depth0 && depth0['link-to']),options={hash:{
    'classNames': ("list-group-item subcategoryItem")
  },hashTypes:{'classNames': "STRING"},hashContexts:{'classNames': depth0},inverse:self.noop,fn:self.program(1, program1, data),contexts:[depth0,depth0,depth0],types:["STRING","ID","ID"],data:data},helper ? helper.call(depth0, "subcategory", "category", "subcategory", options) : helperMissing.call(depth0, "link-to", "subcategory", "category", "subcategory", options));
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n");
  stack1 = helpers['if'].call(depth0, "view.isEditing", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(4, program4, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n\n");
  return buffer;
  
});Ember.TEMPLATES['setting'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var stack1, escapeExpression=this.escapeExpression, self=this, helperMissing=helpers.helperMissing;

function program1(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n    <div class=\"wrap\" style=\"margin-top: 25px;\">\n\n        <div class=\"text-right\">\n            <button class=\"btn btn-primary smallMarginBottom\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "addDomain", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Add Domain</button>\n        </div>\n\n        <div class=\"row\">\n            ");
  stack1 = helpers.each.call(depth0, "domain", "in", "controller.settings.domains", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(2, program2, data),contexts:[depth0,depth0,depth0],types:["ID","ID","ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n        </div>\n\n        <div>\n            <button class=\"btn btn-primary\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "saveChanges", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["STRING"],data:data})));
  data.buffer.push(">Save Changes</button>\n        </div>\n    </div>\n");
  return buffer;
  }
function program2(depth0,data) {
  
  var buffer = '', stack1, helper, options;
  data.buffer.push("\n                <div class=\"col-md-6\">\n                    <h3 class=\"text-center\">");
  stack1 = helpers._triageMustache.call(depth0, "domain.id", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("</h3>\n                    <table class=\"table table-bordered\">\n                        <tr>\n                            <td colspan=\"2\">\n                                <h5 class=\"text-center\">Domain Settings</h5>\n                            </td>\n                        </tr>\n                        <tr>\n                            <td>Domain Name: ");
  data.buffer.push(escapeExpression((helper = helpers['pop-over'] || (depth0 && depth0['pop-over']),options={hash:{
    'dataToggle': ("popover"),
    'title': ("Application Domain Name"),
    'dataContent': ("This is the domain name the deployed application have. The domain name will be mapped to the correct web applicatio name, defined below.")
  },hashTypes:{'dataToggle': "STRING",'title': "STRING",'dataContent': "STRING"},hashContexts:{'dataToggle': depth0,'title': depth0,'dataContent': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "pop-over", options))));
  data.buffer.push("</td>\n                            <td>");
  data.buffer.push(escapeExpression((helper = helpers.input || (depth0 && depth0.input),options={hash:{
    'type': ("text"),
    'value': ("domain.domainName"),
    'class': ("form-control"),
    'placeholder': ("The Conticious domain name")
  },hashTypes:{'type': "STRING",'value': "ID",'class': "STRING",'placeholder': "STRING"},hashContexts:{'type': depth0,'value': depth0,'class': depth0,'placeholder': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "input", options))));
  data.buffer.push("</td>\n                        </tr>\n                        <tr>\n                            <td>Webapp Name: ");
  data.buffer.push(escapeExpression((helper = helpers['pop-over'] || (depth0 && depth0['pop-over']),options={hash:{
    'dataToggle': ("popover"),
    'title': ("Web App Name"),
    'dataContent': ("This variable defines the name of the webapp, as well as the directory into which the web-application and its documents is expected to be found.")
  },hashTypes:{'dataToggle': "STRING",'title': "STRING",'dataContent': "STRING"},hashContexts:{'dataToggle': depth0,'title': depth0,'dataContent': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "pop-over", options))));
  data.buffer.push("</td>\n                            <td>\n                                ");
  data.buffer.push(escapeExpression((helper = helpers.input || (depth0 && depth0.input),options={hash:{
    'type': ("text"),
    'value': ("domain.webappName"),
    'class': ("form-control"),
    'placeholder': ("The hostname of the live site")
  },hashTypes:{'type': "STRING",'value': "ID",'class': "STRING",'placeholder': "STRING"},hashContexts:{'type': depth0,'value': depth0,'class': depth0,'placeholder': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "input", options))));
  data.buffer.push("\n                            </td>\n                        </tr>\n                        <tr>\n                            <td>Active: ");
  data.buffer.push(escapeExpression((helper = helpers['pop-over'] || (depth0 && depth0['pop-over']),options={hash:{
    'dataToggle': ("popover"),
    'title': ("Is your application active="),
    'dataContent': ("When this checkbox is enabled, Conticious will host your web application.")
  },hashTypes:{'dataToggle': "STRING",'title': "STRING",'dataContent': "STRING"},hashContexts:{'dataToggle': depth0,'title': depth0,'dataContent': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "pop-over", options))));
  data.buffer.push("</td>\n                            <td>");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Checkbox", {hash:{
    'checkedBinding': ("domain.active")
  },hashTypes:{'checkedBinding': "STRING"},hashContexts:{'checkedBinding': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("</td>\n                        </tr>\n                        <tr>\n                            <td>Minified: ");
  data.buffer.push(escapeExpression((helper = helpers['pop-over'] || (depth0 && depth0['pop-over']),options={hash:{
    'dataToggle': ("popover"),
    'title': ("Minify the JavaScript?"),
    'dataContent': ("Conticious will, by default, concatenate any scripts into a single file. Enabling this checkbox will also minify the scripts using JSMin.")
  },hashTypes:{'dataToggle': "STRING",'title': "STRING",'dataContent': "STRING"},hashContexts:{'dataToggle': depth0,'title': depth0,'dataContent': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "pop-over", options))));
  data.buffer.push("</td>\n                            <td>");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Checkbox", {hash:{
    'checkedBinding': ("domain.minified")
  },hashTypes:{'checkedBinding': "STRING"},hashContexts:{'checkedBinding': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("</td>\n                        </tr>\n                        <tr>\n                            <td colspan=\"2\">\n                                <h5 class=\"text-center\">File Upload Settings</h5>\n                            </td>\n                        </tr>\n                        <tr>\n                            <td>UploadURL: ");
  data.buffer.push(escapeExpression((helper = helpers['pop-over'] || (depth0 && depth0['pop-over']),options={hash:{
    'dataToggle': ("popover"),
    'title': ("Post Uploads to URL"),
    'dataContent': ("If you want to enable file uploading for your web application, specify the URL for your plugin here. If you would like to use the default upload handler, specify /json/admin/fileUpload.")
  },hashTypes:{'dataToggle': "STRING",'title': "STRING",'dataContent': "STRING"},hashContexts:{'dataToggle': depth0,'title': depth0,'dataContent': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "pop-over", options))));
  data.buffer.push("</td>\n                            <td>");
  data.buffer.push(escapeExpression((helper = helpers.input || (depth0 && depth0.input),options={hash:{
    'type': ("text"),
    'value': ("domain.uploadUrl"),
    'class': ("form-control"),
    'placeholder': ("URL to call when uploading files")
  },hashTypes:{'type': "STRING",'value': "ID",'class': "STRING",'placeholder': "STRING"},hashContexts:{'type': depth0,'value': depth0,'class': depth0,'placeholder': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "input", options))));
  data.buffer.push("</td>\n                        </tr>\n                        <tr>\n                            <td>UploadPath: ");
  data.buffer.push(escapeExpression((helper = helpers['pop-over'] || (depth0 && depth0['pop-over']),options={hash:{
    'dataToggle': ("popover"),
    'title': ("Upload files to"),
    'dataContent': ("Specify which directory inside your webapp you would like to uplaod your files to. If you specify 'uploads' here, the file will be uploaded to a directory named uploads inside your webapplications directory.")
  },hashTypes:{'dataToggle': "STRING",'title': "STRING",'dataContent': "STRING"},hashContexts:{'dataToggle': depth0,'title': depth0,'dataContent': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "pop-over", options))));
  data.buffer.push("</td>\n                            <td>");
  data.buffer.push(escapeExpression((helper = helpers.input || (depth0 && depth0.input),options={hash:{
    'type': ("text"),
    'value': ("domain.uploadPath"),
    'class': ("form-control"),
    'placeholder': ("Path to the uploaded files on the server")
  },hashTypes:{'type': "STRING",'value': "ID",'class': "STRING",'placeholder': "STRING"},hashContexts:{'type': depth0,'value': depth0,'class': depth0,'placeholder': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "input", options))));
  data.buffer.push("</td>\n                        </tr>\n                        <tr>\n                            <td>Add to Category: ");
  data.buffer.push(escapeExpression((helper = helpers['pop-over'] || (depth0 && depth0['pop-over']),options={hash:{
    'dataToggle': ("popover"),
    'title': ("Create a subcategory for"),
    'dataContent': ("Specify a Category that you would like to have a subcategory creted for each file uploaded. Conticious will automatically create this category if it does not exist when new files are uploaded. For each file uploaded, Conticious will create a subcategory with an ID equal to the name of the uploaded file.")
  },hashTypes:{'dataToggle': "STRING",'title': "STRING",'dataContent': "STRING"},hashContexts:{'dataToggle': depth0,'title': depth0,'dataContent': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "pop-over", options))));
  data.buffer.push("</td>\n                            <td>");
  data.buffer.push(escapeExpression((helper = helpers.input || (depth0 && depth0.input),options={hash:{
    'type': ("text"),
    'value': ("domain.createCategory"),
    'class': ("form-control"),
    'placeholder': ("Add href to uploaded file as a subcategory to this category")
  },hashTypes:{'type': "STRING",'value': "ID",'class': "STRING",'placeholder': "STRING"},hashContexts:{'type': depth0,'value': depth0,'class': depth0,'placeholder': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "input", options))));
  data.buffer.push("</td>\n                        </tr>\n                        <tr>\n                            <td colspan=\"2\">\n                                <h5 class=\"text-center\">Post Processor</h5>\n                            </td>\n                        </tr>\n                        <tr>\n                            <td>Post Processor: ");
  data.buffer.push(escapeExpression((helper = helpers['pop-over'] || (depth0 && depth0['pop-over']),options={hash:{
    'dataToggle': ("popover"),
    'title': ("Choose a Post Processor"),
    'dataContent': ("If your webapplication requires additional postprocessing to be performed on the HTTP responses before being sent to the server, you can choose which Post Processor that you would like to handle the request.")
  },hashTypes:{'dataToggle': "STRING",'title': "STRING",'dataContent': "STRING"},hashContexts:{'dataToggle': depth0,'title': depth0,'dataContent': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "pop-over", options))));
  data.buffer.push("</td>\n                            <td>\n                                ");
  stack1 = helpers['if'].call(depth0, "postProcessors", {hash:{},hashTypes:{},hashContexts:{},inverse:self.program(6, program6, data),fn:self.program(3, program3, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n                            </td>\n                        </tr>\n                        <tr>\n                            <td colspan=\"2\">\n                                <h5 class=\"text-center\">Actions</h5>\n                            </td>\n                        </tr>\n                        <tr>\n                            <td colspan=\"2\" class=\"text-center\">\n                                <button class=\"btn btn-primary\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "saveChanges", "domain", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(">Save Changes to All Domains</button>\n                            </td>\n                        </tr>\n                        <tr>\n                            <td colspan=\"2\" class=\"text-center\">\n                                <button class=\"btn btn-default\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "generateStatic", "domain", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(">Generate Static SEO Version</button>\n                            </td>\n                        </tr>\n                        <tr>\n                            <td colspan=\"2\" class=\"text-center\">\n                                <button class=\"btn btn-danger\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "deleteDomain", "domain", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(">Delete Domain</button>\n                            </td>\n                        </tr>\n                    </table>\n                </div>\n            ");
  return buffer;
  }
function program3(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n                                    ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Select", {hash:{
    'contentBinding': ("postProcessors"),
    'valueBinding': ("domain.postProcessor"),
    'optionValuePath': ("content"),
    'optionLabelPath': ("content.name"),
    'prompt': ("Select Post Processor")
  },hashTypes:{'contentBinding': "STRING",'valueBinding': "STRING",'optionValuePath': "STRING",'optionLabelPath': "STRING",'prompt': "STRING"},hashContexts:{'contentBinding': depth0,'valueBinding': depth0,'optionValuePath': depth0,'optionLabelPath': depth0,'prompt': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n                                    ");
  stack1 = helpers['if'].call(depth0, "domain.postProcessor", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(4, program4, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n                                ");
  return buffer;
  }
function program4(depth0,data) {
  
  
  data.buffer.push("\n                                        <div style=\"max-width: 300px;\">All HTTP Responses will be sent through this post processor before exiting the server. This is true for both data and files.</div>\n                                    ");
  }

function program6(depth0,data) {
  
  
  data.buffer.push("\n                                        No Post Processors registered\n                                ");
  }

function program8(depth0,data) {
  
  var buffer = '', helper, options;
  data.buffer.push("\n    <div class=\"wrap\" style=\"margin-top: 25px;\">\n        ");
  data.buffer.push(escapeExpression((helper = helpers['log-in'] || (depth0 && depth0['log-in']),options={hash:{
    'uuidAdminToken': ("controllers.user.uuidAdminToken")
  },hashTypes:{'uuidAdminToken': "ID"},hashContexts:{'uuidAdminToken': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "log-in", options))));
  data.buffer.push("\n    </div>\n");
  return buffer;
  }

  stack1 = helpers['if'].call(depth0, "controllers.user.isAdminLoggedIn", {hash:{},hashTypes:{},hashContexts:{},inverse:self.program(8, program8, data),fn:self.program(1, program1, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  else { data.buffer.push(''); }
  
});Ember.TEMPLATES['subcategory'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var buffer = '', stack1, helper, options, self=this, helperMissing=helpers.helperMissing;

function program1(depth0,data) {
  
  
  data.buffer.push("<a href=\"#\">Editable Content</a>");
  }

function program3(depth0,data) {
  
  
  data.buffer.push("<a href=\"#\">HTML/Markdown</a>");
  }

function program5(depth0,data) {
  
  
  data.buffer.push("<a href=\"#\">Fields</a>");
  }

  data.buffer.push("<div class=\"subcategoryinput\">\n    <ul class=\"nav nav-tabs\">\n        ");
  stack1 = (helper = helpers['link-to'] || (depth0 && depth0['link-to']),options={hash:{
    'tagName': ("li")
  },hashTypes:{'tagName': "STRING"},hashContexts:{'tagName': depth0},inverse:self.noop,fn:self.program(1, program1, data),contexts:[depth0],types:["STRING"],data:data},helper ? helper.call(depth0, "subcategory.index", options) : helperMissing.call(depth0, "link-to", "subcategory.index", options));
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n        ");
  stack1 = (helper = helpers['link-to'] || (depth0 && depth0['link-to']),options={hash:{
    'tagName': ("li")
  },hashTypes:{'tagName': "STRING"},hashContexts:{'tagName': depth0},inverse:self.noop,fn:self.program(3, program3, data),contexts:[depth0],types:["STRING"],data:data},helper ? helper.call(depth0, "subcategory.preview", options) : helperMissing.call(depth0, "link-to", "subcategory.preview", options));
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n        ");
  stack1 = (helper = helpers['link-to'] || (depth0 && depth0['link-to']),options={hash:{
    'tagName': ("li")
  },hashTypes:{'tagName': "STRING"},hashContexts:{'tagName': depth0},inverse:self.noop,fn:self.program(5, program5, data),contexts:[depth0],types:["STRING"],data:data},helper ? helper.call(depth0, "subcategory.fields", options) : helperMissing.call(depth0, "link-to", "subcategory.fields", options));
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n    </ul>\n    ");
  stack1 = helpers._triageMustache.call(depth0, "outlet", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n</div>");
  return buffer;
  
});Ember.TEMPLATES['subcategory/fields'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var buffer = '', stack1, escapeExpression=this.escapeExpression, helperMissing=helpers.helperMissing, self=this;

function program1(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n        <tr>\n            <td>");
  stack1 = helpers._triageMustache.call(depth0, "field.name", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("</td>\n            <td>\n                ");
  stack1 = helpers['if'].call(depth0, "field.isTextfield", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(2, program2, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n                ");
  stack1 = helpers['if'].call(depth0, "field.isTextarea", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(4, program4, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n                ");
  stack1 = helpers['if'].call(depth0, "field.isBoolean", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(6, program6, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n                ");
  stack1 = helpers['if'].call(depth0, "field.isArray", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(8, program8, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n                ");
  stack1 = helpers['if'].call(depth0, "field.isToOne", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(10, program10, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n                ");
  stack1 = helpers['if'].call(depth0, "field.isToMany", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(12, program12, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n            </td>\n            <td>\n                ");
  stack1 = helpers['if'].call(depth0, "field.isDirty", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(14, program14, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n                ");
  stack1 = helpers['if'].call(depth0, "field.relations.isDirty", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(14, program14, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n            </td>\n        </tr>\n    ");
  return buffer;
  }
function program2(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                    ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.TextField", {hash:{
    'valueBinding': ("field.value")
  },hashTypes:{'valueBinding': "STRING"},hashContexts:{'valueBinding': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n                ");
  return buffer;
  }

function program4(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                    ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.TextArea", {hash:{
    'valueBinding': ("field.value")
  },hashTypes:{'valueBinding': "STRING"},hashContexts:{'valueBinding': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push(" <br />\n                ");
  return buffer;
  }

function program6(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                    ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Checkbox", {hash:{
    'checkedBinding': ("field.value")
  },hashTypes:{'checkedBinding': "STRING"},hashContexts:{'checkedBinding': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push(" <br />\n                ");
  return buffer;
  }

function program8(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                    ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.TextField", {hash:{
    'valueBinding': ("field.value")
  },hashTypes:{'valueBinding': "STRING"},hashContexts:{'valueBinding': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push(" <br />\n                ");
  return buffer;
  }

function program10(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                    ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Select", {hash:{
    'contentBinding': ("field.relation.sortedSubcategories"),
    'valueBinding': ("field.value"),
    'optionValuePath': ("content.id"),
    'optionLabelPath': ("content.id"),
    'prompt': ("Select Subcategory"),
    'classNames': ("form-control")
  },hashTypes:{'contentBinding': "STRING",'valueBinding': "STRING",'optionValuePath': "STRING",'optionLabelPath': "STRING",'prompt': "STRING",'classNames': "STRING"},hashContexts:{'contentBinding': depth0,'valueBinding': depth0,'optionValuePath': depth0,'optionLabelPath': depth0,'prompt': depth0,'classNames': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n                ");
  return buffer;
  }

function program12(depth0,data) {
  
  var buffer = '', helper, options;
  data.buffer.push("\n                    ");
  data.buffer.push(escapeExpression((helper = helpers['select-multiple'] || (depth0 && depth0['select-multiple']),options={hash:{
    'items': ("field.relation.subcategories"),
    'addedItems': ("field.relations"),
    'model': ("field")
  },hashTypes:{'items': "ID",'addedItems': "ID",'model': "ID"},hashContexts:{'items': depth0,'addedItems': depth0,'model': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "select-multiple", options))));
  data.buffer.push("\n                ");
  return buffer;
  }

function program14(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                    <button class=\"btn btn-primary\" style=\"width: 75px;\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "saveSubcategoryField", "field", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(">Save</button>\n                    <button class=\"btn btn-default\" style=\"width: 75px;\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "revertSubcategoryField", "field", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(">Revert</button>\n                ");
  return buffer;
  }

function program16(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n    <h1>Add this subcategory to a relationship</h1>\n\n    <table class=\"table subcategoryFields\">\n        <thead>\n        <tr>\n            <th>Related to Category</th>\n            <th>Through Field</th>\n            <th>Add to Subcategory</th>\n            <th>Actions</th>\n        </tr>\n        </thead>\n        <tbody>\n        ");
  stack1 = helpers.each.call(depth0, "relation", "in", "relatedCategoryFields", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(17, program17, data),contexts:[depth0,depth0,depth0],types:["ID","ID","ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n        </tbody>\n    </table>\n");
  return buffer;
  }
function program17(depth0,data) {
  
  var buffer = '', stack1;
  data.buffer.push("\n            <tr>\n                <td>");
  stack1 = helpers._triageMustache.call(depth0, "relation.relatedToCategory", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("</td>\n                <td>");
  stack1 = helpers._triageMustache.call(depth0, "relation.throughField", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("</td>\n                <td>\n                    ");
  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Ember.Select", {hash:{
    'classNames': ("input-medium form-control"),
    'valueBinding': ("relation.selectedRelationId"),
    'contentBinding': ("relation.relatedSubcategories"),
    'optionLabelPath': ("content.name"),
    'optionValuePath': ("content.id")
  },hashTypes:{'classNames': "STRING",'valueBinding': "STRING",'contentBinding': "STRING",'optionLabelPath': "STRING",'optionValuePath': "STRING"},hashContexts:{'classNames': depth0,'valueBinding': depth0,'contentBinding': depth0,'optionLabelPath': depth0,'optionValuePath': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n                </td>\n                <td>\n                    ");
  stack1 = helpers['if'].call(depth0, "relation.selectedRelationId", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(18, program18, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n                </td>\n            </tr>\n        ");
  return buffer;
  }
function program18(depth0,data) {
  
  var buffer = '';
  data.buffer.push("\n                        <button class=\"btn btn-primary\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "addSubcategoryToRelation", "relation.selectedRelationId", "relation.throughField", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0,depth0],types:["STRING","ID","ID"],data:data})));
  data.buffer.push(">Add to field</button>\n                    ");
  return buffer;
  }

  data.buffer.push("<table class=\"table subcategoryFields\">\n    <thead>\n    <tr>\n        <th>Name</th>\n        <th>Value</th>\n        <th>Actions</th>\n    </tr>\n    </thead>\n    <tbody>\n    ");
  stack1 = helpers.each.call(depth0, "field", "in", "fields", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(1, program1, data),contexts:[depth0,depth0,depth0],types:["ID","ID","ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n    </tbody>\n</table>\n\n");
  stack1 = helpers['if'].call(depth0, "relatedCategoryFields", {hash:{},hashTypes:{},hashContexts:{},inverse:self.noop,fn:self.program(16, program16, data),contexts:[depth0],types:["ID"],data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  return buffer;
  
});Ember.TEMPLATES['subcategory/index'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var buffer = '', helper, options, helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression;


  data.buffer.push(escapeExpression((helper = helpers['input-wysiwyg'] || (depth0 && depth0['input-wysiwyg']),options={hash:{
    'content': ("content.content")
  },hashTypes:{'content': "ID"},hashContexts:{'content': depth0},contexts:[],types:[],data:data},helper ? helper.call(depth0, options) : helperMissing.call(depth0, "input-wysiwyg", options))));
  data.buffer.push("\n\n<div style=\"width: 100%; margin-top: 20px;\">\n    <button class=\"btn btn-primary\" style=\"width: 49%; margin-right: 5%;\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "doSaveSubcategory", "", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(">Save Subcategory</button>\n    <button class=\"btn btn-default\" style=\"width: 44%;\">Reset</button>\n</div>");
  return buffer;
  
});Ember.TEMPLATES['subcategory/preview'] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data
/**/) {
this.compilerInfo = [4,'>= 1.0.0'];
helpers = this.merge(helpers, Ember.Handlebars.helpers); data = data || {};
  var buffer = '', escapeExpression=this.escapeExpression;


  data.buffer.push(escapeExpression(helpers.view.call(depth0, "Conticious.MarkdownTextArea", {hash:{
    'valueBinding': ("content.content")
  },hashTypes:{'valueBinding': "STRING"},hashContexts:{'valueBinding': depth0},contexts:[depth0],types:["ID"],data:data})));
  data.buffer.push("\n\n<div style=\"width: 100%; margin-top: 20px;\">\n    <button class=\"btn btn-primary\" style=\"width: 49%; margin-right: 5%;\" ");
  data.buffer.push(escapeExpression(helpers.action.call(depth0, "doSaveSubcategory", "", {hash:{},hashTypes:{},hashContexts:{},contexts:[depth0,depth0],types:["STRING","ID"],data:data})));
  data.buffer.push(">Save Subcategory</button>\n    <button class=\"btn btn-default\" style=\"width: 44%;\">Reset</button>\n</div>");
  return buffer;
  
});