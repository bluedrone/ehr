
"use strict";

modulejs.define('chief_complaints', ["underscore", "table", "util", "app",
  "printer"
], function (_, Table, Util, App, Printer) {

  var module = {}
   
  module.getComplaints = function (patientId) {
    var jsonData = JSON.stringify({
      patientId: patientId,
      sessionId: App.clinician.sessionId
    });
    Ajax.post("patient/getChiefComplaints", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      var chiefComplaints = App.chiefComplaints = parsedData.chiefComplaints;
      RenderUtil.render('component/simple_data_table', {
        items: chiefComplaints,
        title: 'Chief Complaints',
        tableName: 'chief-complaints-list',
        clickable: true,
        columns: [{
          title: 'Date',
          field: 'date',
          type: 'date'
        }, {
          title: 'Description',
          field: 'description',
          type: 'strip-html'
        }]
      }, function (s) {
        $('#chief-complaint-list').html(s);
        $('#chief-complaint-list-title').html("Chief Complaints");
        $('#chief-complaint-print').addClass("disabled");
        $('.clickable-table-row').click(function (e) {
          $(this).addClass('table-row-highlight').siblings().removeClass(
            'table-row-highlight');
          Table.handleClickableRow(e);
        });
      });
    });
  };

  module.viewComplaint = function (chiefComplaintId) {
    var chiefComplaint = Util.find(module.chiefComplaints, function (complaint) {
      complaint.id = chiefComplaintId;
    });
    var date = dateFormat(chiefComplaint.date, 'mm/dd/yyyy');
    $('#patient-cc-date').html(date);
    $('#pain-scale-value').html(chiefComplaint.painScale);
    $('#chief-complaint').html(chiefComplaint.description);
    $('#specific-location').html(chiefComplaint.specificLocation);
    Util.CheckboxesFromList(chiefComplaint.occursWhen, 'occurs-when');
    $('#hours-since').html(chiefComplaint.hoursSince);
    $('#days-since').html(chiefComplaint.daysSince);
    $('#weeks-since').html(chiefComplaint.weeksSince);
    $('#months-since').html(chiefComplaint.monthsSince);
    $('#years-since').html(chiefComplaint.yearsSince);
    $('#how-long-other').html(chiefComplaint.howLongOther);
    $('#pain-scale').html(chiefComplaint.painScale);
    $('#pain-type').html(chiefComplaint.painType);
    $('#pain-x-hour').html(chiefComplaint.painXHour);
    $('#pain-x-day').html(chiefComplaint.painXDay);
    $('#pain-x-week').html(chiefComplaint.painXWeek);
    $('#pain-x-month').html(chiefComplaint.painXMonth);
    $('#pain-duration').html(chiefComplaint.painDuration);
    Util.selectCheckboxesFromList(chiefComplaint.denies, 'denies');
    $('#denies-other').html(chiefComplaint.deniesOther);
    $('#chief-complaint-print').removeClass("disabled");
    $('#chief-complaint-print').off().on('click', function () {
      Printer.printPatientForm('print_patient_cc', 'CHIEF COMPLAINT',
        chiefComplaint);
    });
  };
  return module;
});