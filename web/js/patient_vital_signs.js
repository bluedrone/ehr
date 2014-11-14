
"use strict";

modulejs.define('patient/vital_signs', ["charting", "render_util", "app"],
  function (Charting, RenderUtil, App) {

    var module = {}
    
    module.chartMap;
    module.loadScreen(patientId); {
      var jsonData = JSON.stringify({
        patientId: patientId,
        sessionId: App.clinician.sessionId
      });
      Ajax.post("patient/getPatientVitalSigns", {
        data: jsonData
      }, function (data) {
        var parsedData = $.parseJSON(data);
        var columns = [{
          title: 'Date',
          field: 'date',
          type: 'date'
        }, {
          title: 'Weight',
          field: 'weight',
          type: 'simple'
        }, {
          title: 'BMI',
          field: 'bmi',
          type: 'simple'
        }, {
          title: 'OFC',
          field: 'ofc',
          type: 'simple'
        }, {
          title: 'Temp',
          field: 'temperature',
          type: 'simple'
        }, {
          title: 'Pulse',
          field: 'pulse',
          type: 'simple'
        }, {
          title: 'Resp',
          field: 'respiration',
          type: 'simple'
        }, {
          title: 'Syst',
          field: 'systolic',
          type: 'simple'
        }, {
          title: 'Dia',
          field: 'diastolic',
          type: 'simple'
        }, {
          title: 'Ox',
          field: 'oximetry',
          type: 'simple'
        }];
        patientVitalSigns = parsedData.vitalSigns;
        RenderUtil.render('component/portal_data_table', {
          items: patientVitalSigns,
          title: 'Vital Signs',
          clickable: false,
          columns: columns
        }, function (s) {
          $('#patient_health_issue_detail_table').html(s);
          $('#patient-vitals-print').off().on('click', function () {
            printPatientTable('print_patient_vitals',
              'VITAL SIGNS',
              patientVitalSigns, columns);
          });
          var cellIndexMap = {};
          for (i = 0; i < columns.length; i++) {
            cellIndexMap[i] = columns[i].field;
          }
          patientVitalSigns.reverse();
          var chartMap = module.chartMap = {};
          for (i = 0; i < patientVitalSigns.length; i++) {
            var obj = patientVitalSigns[i];
            for (var property in obj) {
              if (obj.hasOwnProperty(property)) {
                if (property in chartMap == false) {
                  var values = [];
                  chartMap[property] = values;
                }
                if (property == 'date') {
                  obj[property] = dateFormat(obj[property],
                    'mm/dd/yyyy');
                }
                chartMap[property].push(obj[property]);
              }
            }
          }
          var labels = chartMap['date'];
          var data = chartMap['weight'];
          renderLineChart(labels, data);
          $(
            "#patient_health_issue_detail_table th.highlightable:nth-child(2)"
          ).addClass('highlighted').siblings().removeClass(
            'highlighted');
          $(
            "#patient_health_issue_detail_table td.highlightable:nth-child(2)"
          ).addClass('highlighted').siblings().removeClass(
            'highlighted');
          $('#patient_health_issue_detail_table th.highlightable').on(
            'click',
            function (e) {
              var cellIndex = this.cellIndex;
              if (cellIndex > 0) {
                var jqCellIndex = cellIndex + 1;
                $(this).addClass('highlighted').siblings().removeClass(
                  'highlighted');
                $(
                    "#patient_health_issue_detail_table td.highlightable:nth-child(" +
                    jqCellIndex + ")").addClass('highlighted').siblings()
                  .removeClass('highlighted');
                var labels = chartMap['date'];
                var data = chartMap[cellIndexMap[cellIndex]];
                Charting.renderLineChart(labels, data);
              }
            });
        });
      });
    }
    return module;
  });