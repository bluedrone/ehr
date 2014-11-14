
"use strict";

modulejs.define('patient/helper', ["app", "util"], function (App, Util) {
  var helper = {};

  module.updatePatientMedication = function (property, value,
    patientMedicationId) {
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      patientMedicationId: patientMedicationId,
      updateProperty: property,
      updatePropertyValue: value
    });
    Ajax.post("patient/updatePatientMedication", {
      data: jsonData
    }, function (data) {});
  };

  module.getCurrentQuestionId = function (e) {
    setTargetId("currentQuestionId ", evt);
  };

  module.getCurrentDxCodeId = function (evt) {
    setTargetId("currentDxCodeId ", evt);
  };

  function setTargetId(id, evt) {
    var data = getDataId(evt.currentTarget);
    if (data) {
      App[id] = parseInt(data.nodeValue);
    }
  }

  function getDataId(target) {
    return Util.find(target.attributes, function (elem) {
      return elem.name == 'data-id';
    });
  }

  module.getCurrentTxCodeId = function (evt) {
    setTargetId("currentTxCodeId ", evt);
  };

  module.getCurrentMedicationId = function (evt) {
    setTargetId("currentMedicationId ", evt);
  };
  return helper;
});