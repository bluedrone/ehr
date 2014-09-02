/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

$('.app-reports-link').click(function(){ viewReports(); });

function viewReports() {
  app_viewStack('reports-screen', DO_SCROLL);
  getReportsList();
  getActivityLog();
}

function getReportsList() {
	  var jsonData = JSON.stringify({ sessionId: clinician.sessionId });
	  $.post("reports/getReportsList", {data:jsonData}, function(data) {
	    var reportList = $.parseJSON(data);
	    RenderUtil.render('component/user_reports_list_table', 
	     {items:reportList, 
	      title:'Reports List', 
	      tableName:'user-reports-list', 
	      clickable:false
	      }, function(s) {
	      $('#user-reports-list').html(s);
	      $('#user-reports-list-title').html("Reports List");	      
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


