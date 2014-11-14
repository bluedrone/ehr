
"use strict";

modulejs.define('soap_note', ["table", "app", "printer"], function (Table, App,
  Printer) {
  var module = {};
  App.soapNotes = [];

  module.getNotes = function (patientId) {
    var jsonData = JSON.stringify({
      patientId: patientId,
      sessionId: App.clinician.sessionId
    });
    $.post("patient/getSOAPNotes", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      App.soapNotes = parsedData.soapNotes;
      RenderUtil.render('component/simple_data_table', {
        items: App.soapNotes,
        title: 'SOAP Notes',
        tableName: 'soap-notes-list',
        clickable: true,
        columns: [{
          title: 'Date',
          field: 'date',
          type: 'date'
        }, {
          title: 'Subjective',
          field: 'subjective',
          type: 'soap-note'
        }, {
          title: 'Objective',
          field: 'objective',
          type: 'soap-note'
        }, {
          title: 'Assessment',
          field: 'assessment',
          type: 'soap-note'
        }, {
          title: 'Plan',
          field: 'plan',
          type: 'soap-note'
        }]
      }, function (s) {
        $('#soap-notes-list').html(s);
        $('#soap-notes-list-title').html("SOAP Notes");
        $('#soap-notes-print').addClass("disabled");
        $('.clickable-table-row').click(function (e) {
          $(this).addClass('table-row-highlight').siblings().removeClass(
            'table-row-highlight');
          Table.handleClickableRow(e);
        });
      });
    });
  };

  module.viewNote = function (soapNoteId) {
    soapNote = Util.findById(App.soapNotes, soapNoteId);
    $('#soap-note-subjective').html(soapNote.subjective);
    $('#soap-note-objective').html(soapNote.objective);
    $('#soap-note-assessment').html(soapNote.assessment);
    $('#soap-note-plan').html(soapNote.plan);
    $('#soap-notes-print').removeClass("disabled");
    $('#soap-notes-print').off().on('click', function () {
      Printer.printPatientForm('print_soap_note', 'SOAP NOTE',
        soapNote);
    });
  };
  return module;
});