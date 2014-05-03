/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

var RenderUtil = Backbone.Model.extend({}, {

  templates: {},

  // Get and Render template by name from hash of preloaded templates
  // and fetch from server if not yet loaded into cache.    
  render: function (templateName, templateValues, callback) {
    var that = this;
    if (templateName in this.templates == false) {
      debug('Fetching template: ' + templateName);
      $.get('template/' + templateName + '.html', function(data) {
       that.templates[templateName] = data; 
       that.render(templateName, templateValues, callback);
      });
    }
    else {
      debug('Loading preloaded template: ' + templateName);
      var s = $.tmpl(this.templates[templateName], templateValues);
      callback(s);
    }
  }

});
