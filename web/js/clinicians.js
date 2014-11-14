
"use strict";

modulejs.define('clinicians', ["app", "util"], function (App, Util) {
  var module = {};

  module.getList = function () {};
  module.getPatients = function (clinicianId) {};
  module.find = function (id) {
    return Util.find(App.clinicians, function (clinician) {
      clinician.id = id;
    });
  };
  module.updateClinician = function(id, property, value) {
    var clinician = module.find(id);
    if (clinician) {
      clinicians[property] = value;
    }
  }
  return module;
});