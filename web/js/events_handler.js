
"use strict";

modulejs.define('events_handler', 
                ["app", "park", "events", "patient/chart", "clinician", "patient/encounter", "soap_note"],
  function (App, Park, Events, PatientChart, Clinician, PatientEncounter, SoapNote) {
    var module = {};
    Events.on("update-messages-inbox", function (evt, messageId) {
      App.currentMessageId = messageId;
      Clinician.viewMessage(messageId);
    });
    Events.on("update-chart-encounters-list", function (evt, encounterId) {
      App.currentEncounterId = encounterId; 
      PatientEncounter.viewEncounter(encounterId);
    });
    Events.on("update-soap-notes-list", function (evt, noteId) {
      App.currentSOAPNoteId = noteId; 
      SoapNote.viewNote(noteId);
    });
    Events.on("update-chief-complaints-list", function (evt, complaintId) {
      App.currentChiefComplaintId = complaintId;
      ChiefComplaints.viewComplaint(complaintId);
    });
    Events.on("idletimer:reset", function () {
      Park.clearWarning();
    });
    Events.on("idletimer:@25", function () {
      Park.showWarning();
    });
    Events.on("idletimer:@30", function () {
      Park.showDialog();
    });
    Events.on("update-patient-search-results", function (evt, id) {
      App.currentPatientId = id; 
      $('#modal-patient-search').modal('hide');
      PatientChart.getChart();
    });
    Events.on("showDashboard", function(){
      Clinician.getDashboard();
    })
    return module;
  });