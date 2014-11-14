
"use strict";

var loadPatientChartHeaderInfo;

modulejs.define('patient/chart', 
                ["view_stack", "app", "util", "render_util"], 
                function (ViewStack, App, Util, RenderUtil) {

  var module = {}
  
  
  module.chartFullName;
  module.chartDOB;
  module.chartGender;
  module.chartMRN;
  module.chartPrimaryPhone;
  module.chartSecondaryPhone;
  module.chartHeadshot;
  module.chartUSState;
  module.chartCity;
  module.chartPostalCode;
  module.chartEmail;
  module.chartAddress;
  module.chartLastApptDate;

  module.loadHeaderInfo = function() {
    $('.patient-chart-full-name').html(module.chartFullName);
    $('.patient-chart-dob').html(module.chartDOB);
    $('.patient-chart-gender').html(module.chartGender);
    $('.patient-chart-mrn').html(module.chartMRN);
    $('.patient-chart-primary-phone').html(module.chartPrimaryPhone);
    $('.patient-chart-secondary-phone').html(module.chartSecondaryPhone);  
    $('.patient-chart-headshot').attr('src', module.chartHeadshot);
    $('.patient-chart-us-state').html(module.chartUSState);
    $('.patient-chart-postal-code').html(module.chartPostalCode);
    $('.patient-chart-email').html(module.chartEmail);
    $('.patient-chart-address').html(module.chartAddress);
    $('.patient-chart-last-appt').html(module.chartLastApptDate);
    $('.patient-chart-city').html(module.chartCity);
  }

  loadPatientChartHeaderInfo = module.loadHeaderInfo;

  module.getSummary = function () {
    var jsonData = JSON.stringify({
      id: App.currentPatientId,
      clinicianId: App.clinician.id,
      sessionId: App.clinician.sessionId
    });
    Ajax.post("app/getPatientChartSummary", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      var patientChartSummary = App.patientChartSummary = parsedData.patientChartSummary;
      RenderUtil.render('component/simple_data_table', {
        items: patientChartSummary.patientEncounters,
        title: 'Visits',
        clickable: true,
        columns: [{
          title: 'Date',
          field: 'date',
          type: 'date'
        }, {
          title: 'Type',
          field: 'encounterType.name',
          type: 'double'
        }]
      }, function (s) {
        $('#patient-chart-summary-visits').html(s);
        $('.clickable-table-row').click(function (e) {
          $(this).addClass('table-row-highlight').siblings().removeClass(
            'table-row-highlight');
        });
      });
      RenderUtil.render('component/simple_data_table', {
        items: patientChartSummary.patientVitalSigns,
        title: 'Vital Signs',
        clickable: true,
        columns: [{
          title: 'Height',
          field: 'height',
          type: 'numeric'
        }, {
          title: 'Weight',
          field: 'weight',
          type: 'numeric'
        }, {
          title: 'BMI',
          field: 'bmi',
          type: 'numeric'
        }, {
          title: 'OFC',
          field: 'ofc',
          type: 'numeric'
        }, {
          title: 'Temp',
          field: 'temperature',
          type: 'numeric'
        }, {
          title: 'Pulse',
          field: 'pulse',
          type: 'numeric'
        }, {
          title: 'Resp',
          field: 'respiration',
          type: 'numeric'
        }, {
          title: 'Syst',
          field: 'systolic',
          type: 'numeric'
        }, {
          title: 'Dia',
          field: 'diastolic',
          type: 'numeric'
        }, {
          title: 'Ox',
          field: 'oximetry',
          type: 'numeric'
        }]
      }, function (s) {
        $('#patient-chart-summary-vital-signs').html(s);
        $('.clickable-table-row').click(function (e) {
          $(this).addClass('table-row-highlight').siblings().removeClass(
            'table-row-highlight');
        });
      });
      RenderUtil.render('component/simple_data_table', {
          items: patientChartSummary.patientHealthIssues,
          title: 'Health Maintenance',
          clickable: true,
          columns: [{
            title: 'Health Issue',
            field: 'healthIssue.name',
            type: 'double'
          }, {
            title: 'Date',
            field: 'date',
            type: 'date'
          }]
        },
        function (s) {
          $('#patient-chart-summary-hm').html(s);
          $('.clickable-table-row').click(function (e) {
            $(this).addClass('table-row-highlight').siblings().removeClass(
              'table-row-highlight');
          });
        });
      RenderUtil.render('component/simple_data_table', {
        items: patientChartSummary.patientAllergens,
        title: 'Allergens',
        clickable: true,
        columns: [{
          title: 'Allergen',
          field: 'allergen.name',
          type: 'double'
        }, {
          title: 'Reaction',
          field: 'comment',
          type: 'simple'
        }]
      }, function (s) {
        $('#patient-chart-summary-allergens').html(s);
        $('.clickable-table-row').click(function (e) {
          $(this).addClass('table-row-highlight').siblings().removeClass(
            'table-row-highlight');
        });
      });
      RenderUtil.render('component/simple_data_table', {
        items: patientChartSummary.patientMedications,
        title: 'Medication',
        clickable: true,
        columns: [{
          title: 'Medication',
          field: 'medication.name',
          type: 'double'
        }, {
          title: 'Date',
          field: 'date',
          type: 'date'
        }, {
          title: 'Unit',
          field: 'unit',
          type: 'simple'
        }, {
          title: 'Instructions',
          field: 'instructions',
          type: 'simple'
        }]
      }, function (s) {
        $('#patient-chart-summary-medication').html(s);
        $('.clickable-table-row').click(function (e) {
          $(this).addClass('table-row-highlight').siblings().removeClass(
            'table-row-highlight');
        });
      });
      RenderUtil.render('component/simple_data_table', {
        items: patientChartSummary.patientMedicalProcedures,
        title: 'Procedures',
        clickable: true,
        columns: [{
          title: 'Procedure',
          field: 'medicalProcedure.name',
          type: 'double'
        }, {
          title: 'Due Date',
          field: 'date',
          type: 'date'
        }, {
          title: 'Status',
          field: 'status.name',
          type: 'double'
        }, {
          title: 'Last Done',
          field: 'date',
          type: 'date'
        }]
      }, function (s) {
        $('#patient-chart-summary-procedures').html(s);
        $('.clickable-table-row').click(function (e) {
          $(this).addClass('table-row-highlight').siblings().removeClass(
            'table-row-highlight');
        });
      });
    });
  };

  function renderHeader() {
    RenderUtil.render('patient_chart_header', {}, function (s) {
      $('#patient_chart_header_template').html(s);
      module.loadHeaderInfo();
    });
  }

  module.getChart = function () {
    var clinician = App.clinician;
    var jsonData = JSON.stringify({
      id: App.currentPatientId,
      sessionId: clinician.sessionId
    });
    Ajax.post("app/getPatientChart", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      parsedData.fullName = Util.buildFullName(parsedData.firstName,
        parsedData.middleName,
        parsedData.lastName);
      App.currentPatient = parsedData.patient;
      load(parsedData);
      module.loadHeaderInfo();
      $('#section-notification').css("visibility", "visible");
      $('.patient-navbar-btn').css("display", "inline-block");
      $('.check-in-navbar-btn').css("display", "none");
      $('.encounter-navbar-btn').css("display", "none");
      $('#section-notification-text').html("Patient: " + module.chartFullName);
      App.viewPatientChartScreen();
      renderHeader();
    });
  };

  module.show = function() {
      App.viewPatientChartScreen();
  }
  function load(data) {
    module.chartFullName = data.fullName;
    module.chartDOB = dateFormat(data.dob, 'mm/dd/yyyy');
    module.chartGender = data.gender;
    module.chartAddress = data.streetAddress1;
    module.chartCity = data.city;
    module.chartUSState = data.usState;
    module.chartPostalCode = data.postalCode;
    module.chartMRN = data.mrn;
    module.chartEmail = data.email;
    module.chartPrimaryPhone = data.primaryPhone;
    module.chartSecondaryPhone = data.secondaryPhone;
    module.chartLastApptDate = data.lastApptDate;
    module.chartHeadshot = 'patient/getPatientProfileImage?sessionId=' +
      data.sessionId +
      "&patientId=" + data.id + "&profileImagePath=" + data.profileImagePath;
  }
  return module;
});