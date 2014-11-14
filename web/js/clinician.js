
"use strict";

modulejs.define('clinician', ["dto", "messages", "table", "app", "util", "render_util"], 
                function (Dto, Messages, Table, App, Util, RenderUtil) {

  var module = {}
  
  module.STATUS_AUTHORIZED = 1;
  module.STATUS_NOT_FOUND = 0;
  module.STATUS_INVALID_PASSWORD = -1;
  module.STATUS_INACTIVE = -2;

  module.viewMessage = function () {
    var messageId = App.currentMessageId;
    var clinician = App.clinician;
    var jsonData = JSON.stringify({
      id: messageId,
      sessionId: clinician.sessionId
    });
    Messages.reset();
    Ajax.post("app/getClinicianMessage", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      var content = parsedData.content;
      var patient = parsedData.patient;
      if (patient) {
        var patientFullName = Util.buildFullName(patient.cred.firstName,
          patient.cred.middleName, patient.cred.lastName);
        Messages.header("Message from: " + patientFullName);
      }
      Messages.content("<pre>" + content + "</pre>");
    });
  };

  module.load = function (data) {
    var clinician = new Dto.clinician();
    clinician.id = data.id;
    clinician.firstName = data.firstName;
    clinician.middleName = data.middleName;
    clinician.lastName = data.lastName;
    clinician.streetAddress1 = data.streetAddress1;
    clinician.streetAddress2 = data.streetAddress2;
    clinician.city = data.city;
    clinician.state = data.state;
    clinician.zip = data.zip;
    clinician.primaryPhone = data.primaryPhone;
    clinician.secondaryPhone = data.secondaryPhone;
    clinician.email = data.email;
    clinician.authStatus = data.authStatus;
    clinician.patientId = data.patientId;
    clinician.previousLoginTime = data.previousLoginTime;
    clinician.sessionId = data.sessionId;
    App.clinician = clinician;
    return clinician;
  };

  module.getMessages = function () {
    var clinician = App.clinician;
    var jsonData = JSON.stringify({
      id: clinician.id,
      sessionId: clinician.sessionId
    });
    Ajax.post("app/getClinicianMessages", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      var clinicianMessages = App.clinicianMessages = parsedData.patientMessages;
      RenderUtil.render('component/simple_data_table', {
        items: clinicianMessages,
        title: 'Messages',
        tableName: 'messages-inbox',
        clickable: true,
        columns: [{
          title: 'Date',
          field: 'date',
          type: 'date'
        }, {
          title: 'From',
          field: 'patient.cred.firstName',
          type: 'triple-person'
        }, {
          title: 'Subject',
          field: 'subject',
          type: 'simple'
        }]
      }, function (s) {
        $('#messages-inbox').html(s);
        $('.clickable-table-row').click(function (e) {
          $(this).addClass('table-row-highlight').siblings().removeClass(
            'table-row-highlight');
          Table.handleClickableRow(e);
        });
      });
    });
  };

  module.getDashboard = function () {
    var clinician = App.clinician;
    var jsonData = JSON.stringify({
      id: clinician.id,
      sessionId: clinician.sessionId
    });
    Ajax.post("app/getClinicianDashboard", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      var clinicianDashboard = App.clinicianDashboard = parsedData.dashboard;
      RenderUtil.render('component/simple_data_table', {
        items: clinicianDashboard.messages,
        title: 'Messages',
        clickable: true,
        columns: [{
          title: 'Date',
          field: 'date',
          type: 'date'
        }, {
          title: 'From',
          field: 'patient.cred.firstName',
          type: 'triple-person'
        }, {
          title: 'Subject',
          field: 'subject',
          type: 'simple'
        }]
      }, function (s) {
        $('#clinician-dashboard-messages').html(s);
        $('.clickable-table-row').click(function (e) {
          $(this).addClass('table-row-highlight').siblings().removeClass(
            'table-row-highlight');
        });
      });
      RenderUtil.render('component/simple_data_table', {
        items: clinicianDashboard.progressNotes,
        title: 'Progress Notes',
        clickable: true,
        columns: [{
          title: 'Date',
          field: 'date',
          type: 'date'
        }, {
          title: 'Patient',
          field: 'patient.cred.firstName',
          type: 'triple-person'
        }, {
          title: 'Subject',
          field: 'subject',
          type: 'simple'
        }]
      }, function (s) {
        $('#clinician-dashboard-progress-notes').html(s);
        $('.clickable-table-row').click(function (e) {
          $(this).addClass('table-row-highlight').siblings().removeClass(
            'table-row-highlight');
        });
      });
      RenderUtil.render('component/simple_data_table', {
        items: clinicianDashboard.toDoNotes,
        title: 'To Do',
        clickable: true,
        columns: [{
          title: 'Date',
          field: 'date',
          type: 'date'
        }, {
          title: 'Patient',
          field: 'patient.cred.firstName',
          type: 'triple-person'
        }, {
          title: 'Subject',
          field: 'subject',
          type: 'simple'
        }]
      }, function (s) {
        $('#clinician-dashboard-to-do-notes').html(s);
        $('.clickable-table-row').click(function (e) {
          $(this).addClass('table-row-highlight').siblings().removeClass(
            'table-row-highlight');
        });
      });
      RenderUtil.render('component/simple_data_table', {
        items: clinicianDashboard.labReview,
        title: 'Lab Review',
        clickable: true,
        columns: [{
          title: 'Date',
          field: 'date',
          type: 'date'
        }, {
          title: 'Patient',
          field: 'patient.cred.firstName',
          type: 'triple-person'
        }, {
          title: 'Lab',
          field: 'name',
          type: 'simple'
        }, {
          title: 'Value',
          field: 'value',
          type: 'numeric'
        }]
      }, function (s) {
        $('#clinician-dashboard-lab-review').html(s);
        $('.clickable-table-row').click(function (e) {
          $(this).addClass('table-row-highlight').siblings().removeClass(
            'table-row-highlight');
        });
      });
      RenderUtil.render('component/simple_data_table', {
        items: clinicianDashboard.clinicianSchedule,
        title: 'Schedule',
        clickable: true,
        columns: [{
          title: 'Time',
          field: 'date',
          type: 'date'
        }, {
          title: 'Length',
          field: 'length',
          type: 'numeric'
        }, {
          title: 'Age',
          field: 'age',
          type: 'numeric'
        }, {
          title: 'Gender',
          field: 'patient.demo.gender.name',
          type: 'quad'
        }, {
          title: 'Patient',
          field: 'patient.cred.firstName',
          type: 'triple-person'
        }, {
          title: 'Reason',
          field: 'reason',
          type: 'simple'
        }, {
          title: 'Comments',
          field: 'comments',
          type: 'simple'
        }, {
          title: 'Status',
          field: 'status',
          type: 'simple'
        }, {
          title: 'Patient Location',
          field: 'patientLocation',
          type: 'simple'
        }, {
          title: 'Room',
          field: 'room',
          type: 'simple'
        }, {
          title: 'Checked In',
          field: 'checkedIn',
          type: 'simple'
        }, {
          title: 'Wait Time',
          field: 'waitTime',
          type: 'numeric'
        }, {
          title: 'Progress Note Status',
          field: 'progressNoteStatus',
          type: 'simple'
        }]
      }, function (s) {
        $('#clinician-dashboard-schedule').html(s);
        $('.clickable-table-row').click(function (e) {
          $(this).addClass('table-row-highlight').siblings().removeClass(
            'table-row-highlight');
        });
      });
    });
  };
  return module;
});