/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

var app_currentReportId;
var app_clinicianActivity;
var app_activityLogs;
var app_groupByPatientsLog;

$('.app-reports-link').click(function(){ 
	viewReports(); 
});

function viewReports() {
  app_viewStack('reports-screen', DO_SCROLL);
  initActivityLogSearchTypeAheads();
  getReportsList();
}

function getReportsList() {
	  var jsonData = JSON.stringify({ sessionId: clinician.sessionId });
	  $.post("reports/getReportsList", {data:jsonData}, function(data) {
	    var reportList = $.parseJSON(data);
	    RenderUtil.render('component/simple_data_table', 
	     {items:reportList, 
	      title:'Reports List', 
	      tableName:'reports-list', 
	      clickable:true,
	      columns:[
           {title:'Available Reports', field:'title', type:'simple'}
         ]}, function(s) {
	      $('#reports-list').html(s);
	      $('.clickable-table-row').click( function(e){ 
	          $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
	          reports_handleClickableRow(e); 
	      });
	    });
	  });
	}

function getActivityLog() {
  var jsonData = JSON.stringify({ sessionId: clinician.sessionId });
  $.post("reports/getActivityLog", {data:jsonData}, function(data) {
	app_activityLogs = $.parseJSON(data);
    RenderUtil.render('component/simple_data_table', 
     {items:app_activityLogs, 
      title:'Activity Logs', 
      tableName:'reports-content', 
      clickable:false,
      columns:[
       {title:'User Name', field:'userName', type:'simple'},
       {title:'Patient Name', field:'patientName', type:'simple'},
       {title:'Time Performed', field:'timePerformed', type:'simple'},
       {title:'Clinician Name', field:'clinicianName', type:'simple'},
       {title:'Field Name', field:'fieldName', type:'simple'},
       {title:'Activity', field:'activity', type:'simple'},
       {title:'Module', field:'module', type:'simple'}
      ]}, function(s) {
      $('#reports-content').html(s);
      $('#reports-view-header').html("Activity Logs");	
      $('#report-date-from').mask("99/99/9999");
      $('#report-date-to').mask("99/99/9999");
    });
  });
}

function getGroupByPatientsLog() {
  var jsonData = JSON.stringify({ sessionId: clinician.sessionId });
  $.post("reports/getGroupByPatientsLog", {data:jsonData}, function(data) {
	app_groupByPatientsLog = $.parseJSON(data);
    RenderUtil.render('component/reports_nested_table', 
     {items:app_groupByPatientsLog, 
      title:'Grouped by Patients Activity Logs', 
      tableName:'reports-content', 
      clickable:false
      }, function(s) {
      $('#reports-content').html(s);
      $('#reports-view-header').html("Grouped by Patients Activity Logs");	
      $('#report-date-from').mask("99/99/9999");
      $('#report-date-to').mask("99/99/9999");
    });
  });
}

$('#export-csv-activity-log-lg, #export-csv-activity-log-sm').click(function() { 
	//exportCsv();
	exportTableToCSV.apply(this, [$('#reports-content>table'), 'ActivityLog.csv']);
});

function exportTableToCSV($table, filename) {
	$rows = $table.find('tr');
	var csvData = "";
	var csv = "";
	for(var i=0;i<$rows.length;i++){
	   var $cells = $($rows[i]).children('th,td'); //header or content cells
	       for(var y=0;y<$cells.length;y++){
	          if(y>0){
	              csv += ",";
	          }
              var txt = ($($cells[y]).text()).toString().trim();
              if(txt.indexOf(',')>=0 || txt.indexOf('\"')>=0 || txt.indexOf('\n')>=0){
                txt = "\"" + txt.replace(/\"/g, "\"\"") + "\"";
              }
              csv += txt;
           }
       csv += '\n';
	}
       csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(csv);
    $(this)
        .attr({
        'download': filename,
        'href': csvData,
        'target': '_blank'
    });
}

function reports_handleClickableRow(e) {
    var id = undefined;
    var tableId = undefined;
    var tableName = undefined;
    var attributes = e.currentTarget.attributes;
    for (i=0;i<attributes.length;i++) {
      if (attributes[i].name == 'name') {
        id = attributes[i].nodeValue; 
      }
      else if (attributes[i].name == 'id') {
        tableId = attributes[i].nodeValue; 
      }
      else if (attributes[i].name == 'data-table-name') {
        tableName = attributes[i].nodeValue; 
      }
    }
    if (id !== undefined) {
      if (tableName == 'reports-list') {
    	app_currentReportId = id; 
        viewReport();
      }
    }
  }

function viewReport() {
  $('#reports-view').css({display: "block"});
  $('#reports-list').css({display: "none"});
  $('#report-filter').css({display: "none"});
  if(app_currentReportId == 25){
	  $('#report-filter').css({display: "block"});
	  getActivityLog();
  }else if(app_currentReportId == 26){
	  $('#report-filter').css({display: "block"});
	  getGroupByPatientsLog();
  }else{
	  $('#reports-view-header').html(app_currentReportId);
  }
}

function initActivityLogSearchTypeAheads() {
  var jsonData = JSON.stringify({ sessionId: clinician.sessionId });
  $.post("reports/getActivityLogSearchTypeAheads", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    var activityLogClinicianSearchTypeAheads = parsedData.activityLogClinicianSearchTypeAheads;
    var activityLogPatientSearchTypeAheads = parsedData.activityLogPatientSearchTypeAheads;
    app_clinicianActivity = parsedData.clinicianActivityList.clinicianActivity;
    $('#reports-clinician-search-full-name').typeahead(
      { hint: true, highlight: true, minLength: 1 },
      { name: 'clinicianFullNames', displayKey: 'value', source: util_substringMatcher(activityLogClinicianSearchTypeAheads.clinicianFullNames) }); 
    $('#reports-patient-search-full-name').typeahead(
      { hint: true, highlight: true, minLength: 1 },
      { name: 'patientFullNames', displayKey: 'value', source: util_substringMatcher(activityLogPatientSearchTypeAheads.patientFullNames) });
    renderClinicianActivity();      
  });  	  
}

function renderClinicianActivity(){
	RenderUtil.render('component/basic_select_options', {options:app_clinicianActivity, collection:'app_clinicianActivity'}, function(s) {
	  $('#reports-activity-log-activity').html(s);
	});
}

function filterActivityLog(){
  var jsonData = JSON.stringify({ 
	dateFrom: $.trim($("#report-date-from").val()),
	dateTo: $.trim($("#report-date-to").val()),
	clinicianName: $.trim($("#reports-clinician-search-full-name").val()),
	activityId: $.trim($("#reports-activity-log-activity").val()),
    patientName: $.trim($("#reports-patient-search-full-name").val()),
    sessionId: clinician.sessionId 
  });debug("ActivityLog filter json data: "+jsonData);
  $.post("reports/filterActivityLog", {data:jsonData}, function(data) {
  var activityLogs = $.parseJSON(data);
    RenderUtil.render('component/simple_data_table', 
	 {items:activityLogs, 
	  title:'Activity Logs', 
	  tableName:'reports-content', 
	  clickable:false,
	  columns:[
	   {title:'User Name', field:'userName', type:'simple'},
	   {title:'Patient Name', field:'patientName', type:'simple'},
	   {title:'Time Performed', field:'timePerformed', type:'simple'},
	   {title:'Clinician Name', field:'clinicianName', type:'simple'},
	   {title:'Field Name', field:'fieldName', type:'simple'},
	   {title:'Activity', field:'activity', type:'simple'},
	   {title:'Module', field:'module', type:'simple'}
	  ]}, function(s) {
	  $('#reports-content').html(s);
	  $('#reports-view-header').html("Activity Logs");	      
	});
  });
}

function filterGroupByPatientsActivityLog(){
  var jsonData = JSON.stringify({ 
	dateFrom: $.trim($("#report-date-from").val()),
	dateTo: $.trim($("#report-date-to").val()),
	clinicianName: $.trim($("#reports-clinician-search-full-name").val()),
	activityId: $.trim($("#reports-activity-log-activity").val()),
    patientName: $.trim($("#reports-patient-search-full-name").val()),
    sessionId: clinician.sessionId 
  });debug("Grouped by Patients Activity Logs json data: "+jsonData);
  $.post("reports/filterGroupByPatientsActivityLog", {data:jsonData}, function(data) {
	    var parsedData = $.parseJSON(data);
	    RenderUtil.render('component/reports_nested_table', 
	     {items:parsedData, 
	      title:'Grouped by Patients Activity Logs', 
	      tableName:'reports-content', 
	      clickable:false
	      }, function(s) {
	      $('#reports-content').html(s);
	      $('#reports-view-header').html("Grouped by Patients Activity Logs");	      
	    });
	});
}

function clearActivityLogFilter() {
  $('#reports-clinician-search-full-name').val('');
  $('#reports-activity-log-activity').val('0');
  $('#reports-patient-search-full-name').val('');
  $('#report-date-from').val('');
  $('#report-date-to').val('');
}

$('#report-view-button').click(function(){ viewReport(); });
$('#report-close-button').click(function(){ viewReports(); });
$('#btn-reports-activity-log-filter').click(function(){ 
	if(app_currentReportId == 25){
		filterActivityLog(); 
	}else if(app_currentReportId == 26){
		filterGroupByPatientsActivityLog();
	}
});

$('#btn-reports-activity-log-clear').click(function(){ clearActivityLogFilter(); });

$('#print-report-button').click(function(){
	if(app_currentReportId == 26){
		window.print();
	   /* RenderUtil.render('print/grouped_by_patients_activity_logs',  {groupedByPatientsActivityLogs:app_groupByPatientsLog}, function(obj) {
	      var s = obj[0].outerHTML;
	      print_openPrintWindow('print.html', s, 'GROUPED BY PATIENTS ACTIVITY LOGS');
	    });*/
	}
  });


