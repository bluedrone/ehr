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
	          handleClickableRow(e); 
	      });
	    });
	  });
	}

function getActivityLog() {
  var jsonData = JSON.stringify({ sessionId: clinician.sessionId });
  $.post("reports/getActivityLog", {data:jsonData}, function(data) {
    var activityLogs = $.parseJSON(data);
    RenderUtil.render('component/user_admin_activity_log_table', 
     {items:activityLogs, 
      title:'Activity Logs', 
      tableName:'user-admin-activityLogs-list', 
      clickable:false
      }, function(s) {
      $('#user-admin-activityLogs-list').html(s);
      $('#user-admin-activity-log-list-title').html("Activity Logs");	      
    });
  });
}

$('#export-csv-activity-log-lg, #export-csv-activity-log-sm').click(function() { 
	//exportCsv();
    exportTableToCSV.apply(this, [$('#user-admin-activityLogs-list>table'), 'ActivityLog.csv']);
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

function handleClickableRow(e) {
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
	  $('#reports-view-header').html(app_currentReportId);
	  $('#reports-content').html("<pre>"+"Hello world of reports!"+"</pre>");
}

$('#report-view-button').click(function(){ viewReport(); });
$('#report-close-button').click(function(){ viewReports(); });


