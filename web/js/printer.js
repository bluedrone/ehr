"use strict";

 modulejs.define('printer',["render_util", "app"], function(RenderUtil, App){
  
  var module = {}
  
  module.openPrintWindow = function(url, s, pageTitle){
    url = encodeURI(url);
    var docWindow = module.openNewWindow(url, '', 
       'directories=no,location=no,menubar=yes,scrollbars=yes,toolbar=no,status=no,resizable=yes', 
       'print');
    
    docWindow.onload = function(){
      $(docWindow.document).attr('title', pageTitle);
      $(docWindow.document).find("#container").html(s);
    };
  };
  
  module.openNewWindow = function(url, name, features, windowSize){
    if (features == ""){
      features = 
         "toolbar=yes,directories=yes,location=1,status=yes,menubar=yes,scrollbars=yes,resizable=yes";
    }
    var height = 250;
    var width = 350;
    var screenHeight = 480;
    var screenWidth = 640;
    if (windowSize == 'print'){
      height = 700;
      width = 900;
    }
    if (window.screen != null){
      screenHeight = window.screen.height;
      screenWidth = window.screen.width;
    }
    if (screenWidth > 640){
      width = width + (screenWidth - 640) *.50;
    }
    if (screenHeight > 480){
      height = height + (screenHeight - 480) *.50;
    }
    features +=",width=" + width +",height=" + height;
    var docWindow = window.open(url, name, features);
    docWindow.focus();
    return docWindow;
  };
  
  module.printPatientForm = function(template, title, object){
    var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
    RenderUtil.render('print/' + template, {object: object, currentDate: currentDate}
       , function(obj){
      var s = obj[0].outerHTML;
      module.openPrintWindow('print.html', s, title);
    });
  };
  
  module.printPatientTable = function(template, title, items, columns){
    var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
    RenderUtil.render('print/' + template, {items: items, currentDate: currentDate, 
       columns: columns}, function(obj){
      var s = obj[0].outerHTML;
      module.openPrintWindow('print.html', s, title);
    });
  };
  module.printEncounterForm = function(template, title) {
    var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
    RenderUtil.render('print/' + template, {encounter: App.currentEncounter, 
       currentDate: currentDate}, function(obj){
      var s = obj[0].outerHTML;
      module.openPrintWindow('print.html', s, title);
    });
  }
  return module;
});