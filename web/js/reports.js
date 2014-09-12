/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

var app_currentReportId;

$('.app-reports-link').click(function(){ viewReports(); });

function viewReports() {
  app_viewStack('reports-screen', DO_SCROLL);
  getReportsList();
  //getActivityLog();
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
	  if(app_currentReportId == 25){
		  getActivityLog();
	  }else{
		  $('#reports-view-header').html(app_currentReportId);
		  $('#reports-content').html("<pre>"+"Hello world of reports!"+"</pre>");
	  }
}

$('#report-view-button').click(function(){ viewReport(); });
$('#report-close-button').click(function(){ viewReports(); });


