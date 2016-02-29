function loadPatientConnectedHealthScreen() {
  var patientId = app_currentPatientId;
  debug('IN loadPatientConnectedHealthScreen(patientId)');
  var jsonData = JSON.stringify({ patientId: patientId, sessionId: clinician.sessionId });
  $.post("app/getIOTData", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    app_patientConnectedHealth = parsedData.deviceData;
    
    if (app_patientConnectedHealth.devicesRead == true) {
      return;
    }
    
    var columns = [
      {title:'Date/Time', field:'date', type:'date-time'}, 
      {title:'BP', field:'bp', type:'simple'},
      {title:'Pulse', field:'pulse', type:'simple'},
      {title:'Glucose', field:'glucose', type:'simple'},
      {title:'Weightscale', field:'weightscale', type:'simple'},
      {title:'Activity', field:'activity', type:'simple'},
      {title:'Physician Notes', field:'phynotes', type:'simple'}
    ];
    RenderUtil.render('component/portal_data_table', 
    {items:app_patientConnectedHealth, 
    title:'Connected Health', 
    clickable:false, 
    columns:columns
    },
    function(s) { 
      $('#connected_health_detail_table').html(s);
      $('#patient-connected-health-print').off().on('click', function () { printPatientTable('print_patient_connected_health', 'CONNECTED HEALTH', app_patientConnectedHealth, columns)});
   
      var cellIndexMap = {};
      for (i=0;i<columns.length;i++) {
        cellIndexMap[i] = columns[i].field;
      }
      app_patientConnectedHealth.reverse(); 
      app_chartMap = {};
      for (i=0;i<app_patientConnectedHealth.length;i++) {
        var obj = app_patientConnectedHealth[i];
        for (var property in obj) {
          if (obj.hasOwnProperty(property)) {
            if (property in app_chartMap == false){
              var values = [];
              app_chartMap[property] = values; 
            }
            if (property == 'date') {
              obj[property] = dateFormat(obj[property], 'mm/dd/yyyy');
            }
            app_chartMap[property].push(obj[property]);
          }
        }
      }
      var labels = app_chartMap['date'];
      var data = app_chartMap['weight'];
      //renderLineChart(labels, data);
    
      $("#patient_connected_health_detail_table th.highlightable:nth-child(2)").addClass('highlighted').siblings().removeClass('highlighted');
      $("#patient_connected_health_detail_table td.highlightable:nth-child(2)").addClass('highlighted').siblings().removeClass('highlighted');
    
      $('#patient_connected_health_detail_table th.highlightable').on('click', function(e){ 
        var cellIndex = this.cellIndex;	
        if (cellIndex > 0) {
          var jqCellIndex = cellIndex+1;
          $(this).addClass('highlighted').siblings().removeClass('highlighted');
          $("#patient_conneted_health_detail_table td.highlightable:nth-child("+jqCellIndex+")").addClass('highlighted').siblings().removeClass('highlighted');
          var labels = app_chartMap['date'];
          var data = app_chartMap[cellIndexMap[cellIndex]];
          renderLineChart(labels, data);
        }
      });
    });
  });
}
