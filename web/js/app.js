/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

var ENCOUNTER_FREE =  0;
var ENCOUNTER_LOCKED = 1;
var ENCOUNTER_OVERRIDDEN =  2;
var ENCOUNTER_OWNED =  3;
var DO_SCROLL = true;
var DONT_SCROLL = false;
var DO_AUTO_LOGOUT = true;
var DEMO_MODE_ON = true;
var DEMO_MODE_OFF = false;
var DO_AUTO_SERVER_LOGOUT = true;
var PASSWORD_PLACEHOLDER = 'not a password';
var INITIALIZED = false;
var CLINICIAN_STATUS_AUTHORIZED = 1;
var CLINICIAN_STATUS_NOT_FOUND = 0;
var CLINICIAN_STATUS_INVALID_PASSWORD = -1;
var CLINICIAN_STATUS_INACTIVE = -2;
var RETURN_CODE_DUP_USERNAME = -1;
var DEFAULT_TABLE_WIDTH = 772;
var DEMO_USERNAME = 'jdoyle';
var DEMO_PASSWORD = 'Njs2101$';
var app_templates = {};
var app_lockIcons = {0:'icon-unlock', 1:'icon-lock', 2:'icon-bolt', 3:'icon-user-md'};

var DEST_ENCOUNTER = 'encounter';
var app_patientEncounters;
var app_progressNotes;
var app_progressNotesIndex = 0;
var app_patientVitals;
var app_patientVitalsIndex = 0;
var app_patientConsults;
var app_patientConsultsIndex = 0;
var app_patientOBGYN;
var app_patientOBGYNIndex = 0;
var app_patientCC;
var app_patientCCIndex = 0;
var app_patientExam;
var app_patientExamIndex = 0;
var app_patientSupp;
var app_patientSuppIndex = 0;
var app_currentEncounter;
var app_currentSOAPNoteId; 
var app_chiefComplaintId; 
var app_currentEncounterId;
var app_currentScreen = '';
var app_previousScreen = '';
var app_oldEncounter;
var clinician = null;
var encounter = null;
var patients;
var soapNotes;
var chiefComplaints;
var soapNote;
var clinicians;
var patientChartSummary;
var clinicianDashboard;
var patientSearchTypeAheads;
var clinicianMessages;
var clinicianFullName;
var patientVitalSigns;
var app_currentPatientId = null;
var app_currentMessageId;
var app_currentGroup;
var app_currentGroupId;
var app_currentPatientId;
var app_currentQuestionId;
var app_currentMedicationId;
var app_currentGroupIndex;
var app_patientChartFullName;
var app_patientChartDOB;
var app_patientChartGender;
var app_patientChartMRN;
var app_patientChartPrimaryPhone;
var app_patientChartSecondaryPhone;
var app_patientChartHeadshot;
var app_patientEncounterGroups = [];
var app_groupOrderArray = [];
var app_newPatientEncounterGroup = undefined;
var app_oldLockStatus;
var app_currentCalendarView = 'month';

/***********      @JQUERY INIT    *******************/
$(document).ready(function() {
  if (INITIALIZED == false) {
    INITIALIZED = true;
    $(function () { $("[data-toggle='popover']").popover({ trigger: "hover" }); });
    app_viewStack('signin-screen', DO_SCROLL);
    $('.dropdown-menu').find('form').click(function (e) { e.stopPropagation();});
    
    !function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';
    if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";
    fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");
    
    $('#section-notification').css("visibility", "hidden ");
    $('#section-notification-text').html("");
    
    $('#app-check-in-print').click(function(){
      RenderUtil.render('print/print_check-in_list',  {groups:app_patientEncounterGroups}, function(obj) {
        var s = obj[0].outerHTML;
        print_openPrintWindow('print.html', s, 'PATIENT CHECK-IN LIST');
      });
    });
    $('#app-encounter-print-all').click(function(){
      var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
      RenderUtil.render('print/print_encounter_all',  {encounter:app_currentEncounter, currentDate:currentDate}, function(obj) {
        var s = obj[0].outerHTML;
        print_openPrintWindow('print.html', s, 'ENCOUNTER FORM');
      });
    });
    $('#app-check-in-add-group-link').click(function(){ showAddGroupForm(); });
  }
});
/***********      @JQUERY INIT    *******************/


$('#soap-notes-link').click(function() { 
  RenderUtil.render('soap_notes', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-soap-notes').modal('show'); 
    loadPatientInfo();
    getSOAPNotes(app_currentPatientId);
  });
});



$('#about').click(function() { 
  RenderUtil.render('about', {}, function(s) { 
    $('#modals-placement').html(s);
    $('#modal-about').modal('show'); 
  });
});


$('#new-message').click(function() { 
  RenderUtil.render('new_message', {}, function(s) { 
    $('#modals-placement').html(s);
    $('#modal-new-message').modal('show'); 
  });
});

$('.app-park-link').click(function(){ 
  RenderUtil.render('park', {}, function(s) { 
    $('#modals-placement').html(s);
    $('#app-parked-full-name').html(clinicianFullName);
    $('#modal-park').modal('show'); 
    park();
    $('.app-exit').click(function(){ logout(); });
    $('#app-unpark-submit').click(function(){ unpark(); });
  });
});

$('#app-close-chart').click(function(){ 
  app_currentPatientId = null;
  $('#section-notification').css("visibility", "hidden");
  $('#section-notification-text').html("");
  viewDashboard();
});

$('#app-change-patient').click(function(){ 
  patientSearchDialog();
});

$('.patient-button-group').click(function(){ 
  if (app_currentPatientId != null) {
    if (app_currentScreen != 'patient-chart-screen') {
      viewPatientChart();
    }
    return;
  }
  patientSearchDialog();
  initPatientSearchTypeAheads();
});


function initPatientSearchTypeAheads() {
  var jsonData = JSON.stringify({ clinicianId: clinician.id, sessionId: clinician.sessionId });
  $.post("app/getPatientSearchTypeAheads", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    patientSearchTypeAheads = parsedData.patientSearchTypeAheads;
    
    $('#patient-search-first-name').typeahead(
      { hint: true, highlight: true, minLength: 1 },
      { name: 'firstNames', displayKey: 'value', source: util_substringMatcher(patientSearchTypeAheads.firstNames) }); 
    $('#patient-search-middle-name').typeahead(
      { hint: true, highlight: true, minLength: 1 },
      { name: 'middleNames', displayKey: 'value', source: util_substringMatcher(patientSearchTypeAheads.middleNames) }); 
    $('#patient-search-last-name').typeahead(
      { hint: true, highlight: true, minLength: 1 },
      { name: 'lastNames', displayKey: 'value', source: util_substringMatcher(patientSearchTypeAheads.lastNames) }); 
    $('#patient-search-city').typeahead(
      { hint: true, highlight: true, minLength: 1 },
      { name: 'cities', displayKey: 'value', source: util_substringMatcher(patientSearchTypeAheads.cities) }); 
    
    });
}

function patientSearch() {
  var dob = util_processDob("#patient-search-dob", dob);
  var jsonData = JSON.stringify({ 
    id: clinician.id, 
    firstNameFilter: $.trim($("#patient-search-first-name").val()),
    middleNameFilter: $.trim($("#patient-search-middle-name").val()),
    lastNameFilter: $.trim($("#patient-search-last-name").val()),
    cityFilter: $.trim($("#patient-search-city").val()),
    genderFilter: $.trim($("#patient-search-gender").val()),
    dobFilter: dob,
    sessionId: clinician.sessionId 
  });debug("patient json data: "+jsonData);
  $.post("app/patientSearch", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    patients = parsedData.patients;
    RenderUtil.render('component/simple_data_table', 
     {items:patients, 
      title:'Patients', 
      tableName:'patient-search-results', 
      clickable:true, 
      columns:[
        {title:'Full Name', field:'cred.firstName', type:'double-person'},
        {title:'Date of Birth', field:'demo.dob', type:'double-date'},
        {title:'Gender', field:'demo.gender.name', type:'triple'},
        {title:'City', field:'demo.city', type:'double'}
      ]}, function(s) {
      $('#patient-search-results').html(s);
      $('#patient-search-results-title').html("Patient Search");
      $('.clickable-table-row').click( function(e){ 
        $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        handleClickableRow(e); 
      });
    });
  });
}

function clearPatientSearchForm() {
  $('#patient-search-first-name').val('');
  $('#patient-search-middle-name').val('');
  $('#patient-search-last-name').val('');
  $('#patient-search-city').val('');
  $('#patient-search-gender').val('');
  $('#patient-search-dob').val('');
}

function patientSearchDialog() {
  RenderUtil.render('patient_search', {}, function(s) {
    clearPatientSearchForm();
    $('#modals-placement').html(s);
    $('#modal-patient-search').modal('show'); 
    $('#btn-patient-search-ok').prop('disabled', true);
    $('#btn-patient-search-ok').addClass('disabled');
    $('.clickable-table-row').removeClass('table-row-highlight');
    $('#btn-patient-search-search').click(function(){ patientSearch(); });
    $('#btn-patient-search-clear').click(function(){ clearPatientSearchForm(); });
    $('#btn-patient-search-ok').click(function(){ getPatientChart(); });
    $('#patient-search-dob').mask("99/99/9999");
    getRecentPatients();
  
    $('#btn-patient-search-new-patient').click(function() { 
      RenderUtil.render('new_patient', {}, function(s) {
      $('#modals-placement').append(s);
      $('#modal-new-patient').modal('show');
      $('#new-patient-save, #new-patient-cancel').click(function(){ $('#modal-new-patient').modal('hide'); });
      $('#modal-new-patient').on('hidden.bs.modal', function () { debug("new-patient modal hidden"); });
      $('#modal-new-patient').on('hide.bs.modal', function () { debug("new-patient hide called"); });
    });
  });
  $('#modal-patient-search').on('hidden.bs.modal', function () { debug("patient-search modal hidden"); });
  $('#modal-patient-search').on('hide.bs.modal', function () { debug("patient-search hide called"); });
 });
}


function getRecentPatients() {
  var jsonData = JSON.stringify({ clinicianId: clinician.id, sessionId: clinician.sessionId });
  $.post("app/getRecentPatients", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    patients = parsedData.patients;
    RenderUtil.render('component/simple_data_table', 
     {items:patients, 
      title:'Recent Patients', 
      tableName:'patient-search-results', 
      clickable:true, 
      columns:[
        {title:'Full Name', field:'cred.firstName', type:'double-person'},
        {title:'Date of Birth', field:'demo.dob', type:'double-date'},
        {title:'Gender', field:'demo.gender.name', type:'triple'},
        {title:'City', field:'demo.city', type:'double'}
      ]}, function(s) {
      $('#patient-search-results').html(s);
      $('#patient-search-results-title').html("Recent Patients");
      $('.clickable-table-row').click( function(e){ 
        $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        handleClickableRow(e); 
      });
    });
  });
}



function getClinicianDashboard() {
  var jsonData = JSON.stringify({ id: clinician.id, sessionId: clinician.sessionId });
  $.post("app/getClinicianDashboard", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    clinicianDashboard = parsedData.dashboard;
      
    RenderUtil.render('component/simple_data_table', 
     {items:clinicianDashboard.messages, 
      title:'Messages', 
      clickable:true, 
      columns:[
        {title:'Date', field:'date', type:'date'}, 
        {title:'From', field:'patient.cred.firstName', type:'triple-person'}, 
        {title:'Subject', field:'subject', type:'simple'}
      ]}, function(s) {
        $('#clinician-dashboard-messages').html(s);
        $('.clickable-table-row').click( function(e){ 
          $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        });
      });
      
      RenderUtil.render('component/simple_data_table', 
       {items:clinicianDashboard.progressNotes, 
        title:'Progress Notes', 
        clickable:true, 
        columns:[
          {title:'Date', field:'date', type:'date'}, 
          {title:'Patient', field:'patient.cred.firstName', type:'triple-person'}, 
          {title:'Subject', field:'subject', type:'simple'}
        ]}, function(s) {
        $('#clinician-dashboard-progress-notes').html(s);
        $('.clickable-table-row').click( function(e){ 
          $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        });
      });
     
      RenderUtil.render('component/simple_data_table', 
       {items:clinicianDashboard.toDoNotes, 
        title:'To Do', 
        clickable:true, 
        columns:[
          {title:'Date', field:'date', type:'date'}, 
          {title:'Patient', field:'patient.cred.firstName', type:'triple-person'}, 
          {title:'Subject', field:'subject', type:'simple'}
        ]}, function(s) {
        $('#clinician-dashboard-to-do-notes').html(s);
        $('.clickable-table-row').click( function(e){ 
          $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        });
      });
      
      RenderUtil.render('component/simple_data_table', 
       {items:clinicianDashboard.labReview, 
        title:'Lab Review', 
        clickable:true, 
        columns:[
          {title:'Date', field:'date', type:'date'}, 
          {title:'Patient', field:'patient.cred.firstName', type:'triple-person'}, 
          {title:'Lab', field:'name', type:'simple'},
          {title:'Value', field:'value', type:'numeric'}
        ]}, function(s) {
        $('#clinician-dashboard-lab-review').html(s);
        $('.clickable-table-row').click( function(e){ 
          $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        });
      });
      
      RenderUtil.render('component/simple_data_table', 
       {items:clinicianDashboard.clinicianSchedule, 
        title:'Schedule', 
        clickable:true, 
        columns:[
          {title:'Time', field:'date', type:'date'}, 
          {title:'Length', field:'length', type:'numeric'},
          {title:'Age', field:'age', type:'numeric'},
          {title:'Gender', field:'patient.demo.gender.name', type:'quad'},
          {title:'Patient', field:'patient.cred.firstName', type:'triple-person'}, 
          {title:'Reason', field:'reason', type:'simple'},
          {title:'Comments', field:'comments', type:'simple'},
          {title:'Status', field:'status', type:'simple'},
          {title:'Patient Location', field:'patientLocation', type:'simple'},
          {title:'Room', field:'room', type:'simple'},
          {title:'Checked In', field:'checkedIn', type:'simple'},
          {title:'Wait Time', field:'waitTime', type:'numeric'},
          {title:'Progress Note Status', field:'progressNoteStatus', type:'simple'}
        ]}, function(s) {
        $('#clinician-dashboard-schedule').html(s);
        $('.clickable-table-row').click( function(e){ 
          $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        });
      });
    });
  }

$('#patient-chart-summary-link').click(function(){ 
  RenderUtil.render('chart_summary', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-chart-summary').modal('show'); 
    getPatientChartSummary();
    loadPatientInfo();
  });
});

$('#health-maintenance-summary-link').click(function(){ 
  RenderUtil.render('health_maintenance_summary', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-health-maintenance-summary').modal('show'); 
    loadPatientInfo();
  });
});


$('#rx-medications-link').click(function(){ 
    RenderUtil.render('rx_medications', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-rx-medications').modal('show'); 
    loadPatientInfo();
  });
});

$('#obgyn-link').click(function(){ 
    RenderUtil.render('obgyn', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-obgyn').modal('show'); 
    loadPatientInfo();
    loadCurrentOBGYNScreen();        
    $('#patient-obgyn-next-btn').click(function(){changeOBGYNScreen(1)}); 
    $('#patient-obgyn-prev-btn').click(function(){changeOBGYNScreen(-1)}); 
  });
});


$('#medical-history-link').click(function(){ 
    RenderUtil.render('past_medical_history', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-medical-history').modal('show'); 
    loadPatientInfo();
    loadHistScreenForm();
  });
});

$('#pfsh-link').click(function(){ 
    RenderUtil.render('pfsh', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-pfsh').modal('show'); 
    loadPatientInfo();
    loadPFSHScreenForm();
  });
});

$('#images-all-chart-sections-link').click(function(){ 
    RenderUtil.render('images_all_chart_sections', {}, function(s) {
    $('#modals-placement').html(s);
    $('#images-all-chart-sections').modal('show'); 
    loadPatientInfo();
  });
});

$('#x-ray-link').click(function(){ 
    RenderUtil.render('x_ray', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-x-ray').modal('show'); 
    loadPatientInfo();
  });
});

$('#ekg-link').click(function(){ 
    RenderUtil.render('ekg', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-ekg').modal('show'); 
    loadPatientInfo();
  });
});


$('#supp-link').click(function(){ 
    RenderUtil.render('supp', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-supp').modal('show'); 
    loadPatientInfo();
    loadCurrentSuppScreen();
    $('#patient-supp-next-btn').click(function(){changeSuppScreen(1)}); 
    $('#patient-supp-prev-btn').click(function(){changeSuppScreen(-1)});
  });
});

$('#exam-link').click(function(){ 
    RenderUtil.render('exam', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-exam').modal('show'); 
    loadPatientInfo();
    loadCurrentExamScreen();        
    $('#patient-exam-next-btn').click(function(){changeExamScreen(1)}); 
    $('#patient-exam-prev-btn').click(function(){changeExamScreen(-1)}); 
  });
});

$('#attending-notes-link').click(function(){ 
    RenderUtil.render('attending_notes', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-attending-notes').modal('show'); 
    loadPatientInfo();
  });
});

$('#consults-link').click(function(){ 
    RenderUtil.render('consults', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-consults').modal('show'); 
    loadPatientInfo();
    loadCurrentConsultsScreen();        
    $('#patient-follow-up-next-btn').click(function(){changeConsultsScreen(1)}); 
    $('#patient-follow-up-prev-btn').click(function(){changeConsultsScreen(-1)}); 
  });
});

$('#chief-complaints-link').click(function(){ 
    RenderUtil.render('chief_complaint', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-chief-complaint').modal('show'); 
    loadPatientInfo();
    loadCurrentChiefComplaintScreen();        
    $('#patient-cc-next-btn').click(function(){changeChiefComplaintScreen(1)}); 
    $('#patient-cc-prev-btn').click(function(){changeChiefComplaintScreen(-1)}); 
  });
});

$('#misc-docs-link').click(function(){ 
    RenderUtil.render('misc_info_documents', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-misc-info-documents').modal('show'); 
    loadPatientInfo();
  });
});

$('#laboratory-data-link').click(function(){ 
    RenderUtil.render('laboratory_data', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-laboratory-data').modal('show'); 
    loadPatientInfo();
  });
});

$('#flow-charts-link').click(function(){ 
  RenderUtil.render('flow_chart_selection', {}, function(s) {
    $('#modals-placement').html(s);
    $('#flow-chart-selection').modal('show'); 
    loadPatientInfo();
    $('#flow-chart-btn').click(function(){ 
      RenderUtil.render('flow_chart_for_anemia', {}, function(s) {
      $('#modals-placement').append(s);
      $('#modal-flow-chart-for-anemia').modal('show'); 
    });
  });
 });
});


$('#app-progress-notes-link').click(function() { 
  RenderUtil.render('progress_notes', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-progress-notes').modal('show'); 
    loadPatientInfo();
    loadProgressNotesScreen();        
    $('#progress-notes-next-btn').click(function(){changeProgressNotesScreen(1)}); 
    $('#progress-notes-prev-btn').click(function(){changeProgressNotesScreen(-1)}); 
    $('#btn-progress-notes-new').click(function(){ newProgressNotesFormDialog(); });
    $('#btn-progress-notes-save').click(function(){ updateProgressNote(); });
    $('#btn-progress-notes-close').click( function(){ 
      $('#modal-progress-notes').modal('hide'); 
    });
  });
});


$('#vital-signs-link').click(function(){ 
  RenderUtil.render('vital_signs', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-vital-signs').modal('show'); 
    loadPatientInfo();
    loadPatientVitalsScreen(app_currentPatientId);
  });
});


$('#app-problem-list-link').click(function(){ 
  RenderUtil.render('problem_list', {}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-problem-list').modal('show'); 
    loadPatientInfo();
    $('.newer-problem').click(function(){ 
      RenderUtil.render('new_problem', {}, function(s) {
      $('#modals-placement').append(s);
      $('#modal-new-problem').modal('show'); 
    });
  });
 });
});


$('#app-signin-submit').click(function(){ login(DEMO_MODE_OFF); });
$('#app-signin-encounter-submit').click(function(){ login(DEMO_MODE_OFF, DEST_ENCOUNTER); });
$('#get-started-btn').click(function(){ login(DEMO_MODE_ON); });
$('.app-exit').click(function(){ logout(); });

$('.app-dashboard-link').click(function(){ viewDashboard(); });
$('.app-messages-link').click(function(){ viewMessages(); });
$('#encounter-link').click(function(){ viewPatientEncounters(); });
$('.app-letters-link').click(function(){ viewLetters(); });
$('.app-schedule-link').click(function(){ viewSchedule(); });
$('#message-view-button').click(function(){ viewClinicianMessage(); });
$('#message-close-button').click(function(){ viewMessages(); });

function viewPatientEncounters() {
  app_viewStack('patient-encounters-screen', DO_SCROLL);
  $('#app-new-encounter-btn').click(function(){ newEncounterFormDialog(); });
  getPatientEncountersListing();
}

function viewMessages() {
  app_viewStack('messages-screen', DO_SCROLL);
  $('#messages-inbox-header').html('Inbox');
  getClinicianMessages();
}

function viewLetters() {
  app_viewStack('letters-screen', DO_SCROLL);
}

function viewSchedule() {
  app_viewStack('schedule-screen', DO_SCROLL);
  app_loadCalendar();
}


function getClinicianMessages() {
  var jsonData = JSON.stringify({ id: clinician.id, sessionId: clinician.sessionId });
  $.post("app/getClinicianMessages", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    clinicianMessages = parsedData.patientMessages;
    RenderUtil.render('component/simple_data_table', 
     {items:clinicianMessages, 
      title:'Messages', 
      tableName:'messages-inbox', 
      clickable:true, 
      columns:[
        {title:'Date', field:'date', type:'date'},
        {title:'From', field:'patient.cred.firstName', type:'triple-person'},
        {title:'Subject', field:'subject', type:'simple'}
      ]}, function (s) {
      $('#messages-inbox').html(s);
      $('.clickable-table-row').click( function(e){ 
        $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        handleClickableRow(e); 
      });
    });
  });
}

function viewClinicianMessage() {
  $('#messages-view').css({display: "block"});
  $('#messages-inbox').css({display: "none"});
  var jsonData = JSON.stringify({ id: app_currentMessageId, sessionId: clinician.sessionId });
  $.post("app/getClinicianMessage", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    var content = parsedData.content;
    var patient = parsedData.patient;
    if (patient) { 
      var patientFullName = util_buildFullName(patient.cred.firstName, patient.cred.middleName, patient.cred.lastName);
      $('#messages-inbox-header').html("Message from: " + patientFullName);
    }
    
    $('#message-content').html("<pre>"+content+"</pre>");
  });
}

  function handleDoubleClickedRow(e) {
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
    }
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
      if (tableName == 'patient-search-results') {
        app_currentPatientId = id; 
        $('#modal-patient-search').modal('hide'); 
        getPatientChart();
      }
      else if (tableName == 'messages-inbox') {
        app_currentMessageId = id; 
        viewClinicianMessage();
      }
      else if (tableName == 'chart-encounters-list') {
        app_currentEncounterId = id; 
        viewPatientEncounter(id);
      }
      else if (tableName == 'soap-notes-list') {
        app_currentSOAPNoteId = id; 
        viewSOAPNote(id);
      }
      else if (tableName == 'chief-complaints-list') {
        app_currentChiefComplaintId = id; 
        viewChiefComplaint(id);
      }
    }
  }
  
  function getPatientChart() {
  getPatientEncounters();
    var jsonData = JSON.stringify({ id: app_currentPatientId, sessionId: clinician.sessionId });
    $.post("app/getPatientChart", {data:jsonData}, function(data) {
      var parsedData = $.parseJSON(data);
      var fullName = util_buildFullName(parsedData.firstName, parsedData.middleName, parsedData.lastName);
      app_patientChartFullName = fullName;
      app_patientChartDOB = dateFormat(parsedData.dob, 'mm/dd/yyyy');
      app_patientChartGender = parsedData.gender;
      app_patientChartMRN = parsedData.mrn;
      app_patientChartPrimaryPhone = parsedData.primaryPhone;
      app_patientChartSecondaryPhone = parsedData.secondaryPhone;
      app_patientChartHeadshot = 'patient/getPatientProfileImage?sessionId=' + parsedData.sessionId + "&patientId=" + parsedData.id  + "&profileImagePath=" + parsedData.profileImagePath;
      $('.patient-chart-full-name').html(app_patientChartFullName);
      $('.patient-chart-dob').html(app_patientChartDOB);
      $('.patient-chart-gender').html(app_patientChartGender);
      $('.patient-chart-mrn').html(app_patientChartMRN);
      $('.patient-chart-primary-phone').html(app_patientChartPrimaryPhone);
      $('.patient-chart-secondary-phone').html(app_patientChartSecondaryPhone);
      $('.patient-chart-headshot').attr('src', app_patientChartHeadshot);
      $('#section-notification').css("visibility", "visible");
      $('.patient-navbar-btn').css("display", "inline-block");
      $('.check-in-navbar-btn').css("display", "none");
      $('.encounter-navbar-btn').css("display", "none");
      $('#section-notification-text').html("Patient: " + app_patientChartFullName);
      viewPatientChart();
    });
  }
  
  
  function getPatientChartSummary() {
    var jsonData = JSON.stringify({ id: app_currentPatientId, clinicianId: clinician.id, sessionId: clinician.sessionId });
    $.post("app/getPatientChartSummary", {data:jsonData}, function(data) {
      var parsedData = $.parseJSON(data);
      patientChartSummary = parsedData.patientChartSummary;
      
      RenderUtil.render('component/simple_data_table', 
       {items:patientChartSummary.patientEncounters, 
        title:'Visits', 
        clickable:true, 
        columns:[
          {title:'Date', field:'date', type:'date'},
          {title:'Type', field:'encounterType.name', type:'double'}
        ]}, function(s) {
        $('#patient-chart-summary-visits').html(s);
        $('.clickable-table-row').click( function(e){ 
          $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        });
      });
      
      RenderUtil.render('component/simple_data_table', 
       {items:patientChartSummary.patientVitalSigns, 
        title:'Vital Signs', 
        clickable:true, 
        columns:[
          {title:'Height', field:'height', type:'numeric'},
          {title:'Weight', field:'weight', type:'numeric'},
          {title:'BMI', field:'bmi', type:'numeric'},
          {title:'OFC', field:'ofc', type:'numeric'},
          {title:'Temp', field:'temperature', type:'numeric'},
          {title:'Pulse', field:'pulse', type:'numeric'},
          {title:'Resp', field:'respiration', type:'numeric'},
          {title:'Syst', field:'systolic', type:'numeric'},
          {title:'Dia', field:'diastolic', type:'numeric'},
          {title:'Ox', field:'oximetry', type:'numeric'}
        ]}, function(s) {
        $('#patient-chart-summary-vital-signs').html(s);
        $('.clickable-table-row').click( function(e){ 
          $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        });
      });
      
      RenderUtil.render('component/simple_data_table', 
       {items:patientChartSummary.patientHealthIssues, 
        title:'Health Maintenance', 
        clickable:true, 
        columns:[
         {title:'Health Issue', field:'healthIssue.name', type:'double'}, 
         {title:'Date', field:'date', type:'date'}
        ]}, function(s) {
        $('#patient-chart-summary-hm').html(s);
        $('.clickable-table-row').click( function(e){ 
          $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        });
      });
     
      RenderUtil.render('component/simple_data_table', 
       {items:patientChartSummary.patientAllergens, 
        title:'Allergens', 
        clickable:true, 
        columns:[
          {title:'Allergen', field:'allergen.name', type:'double'}, 
          {title:'Reaction', field:'comment', type:'simple'}
        ]}, function(s) {
        $('#patient-chart-summary-allergens').html(s);
        $('.clickable-table-row').click( function(e){ 
          $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        });
      });
      
      RenderUtil.render('component/simple_data_table', 
       {items:patientChartSummary.patientMedications, 
        title:'Medication', 
        clickable:true, 
        columns:[
          {title:'Medication', field:'medication.name', type:'double'}, 
          {title:'Date', field:'date', type:'date'},
          {title:'Unit', field:'unit', type:'simple'},
          {title:'Instructions', field:'instructions', type:'simple'}
        ]}, function(s) {
        $('#patient-chart-summary-medication').html(s);
        $('.clickable-table-row').click( function(e){ 
          $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        });
      });
      
      RenderUtil.render('component/simple_data_table', 
       {items:patientChartSummary.patientMedicalProcedures, 
        title:'Procedures', 
        clickable:true, 
        columns:[
          {title:'Procedure', field:'medicalProcedure.name', type:'double'}, 
          {title:'Due Date', field:'date', type:'date'},
          {title:'Status', field:'status.name', type:'double'},
          {title:'Last Done', field:'date', type:'date'}
        ]}, function(s) {
        $('#patient-chart-summary-procedures').html(s);
        $('.clickable-table-row').click( function(e){ 
          $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        });
      });
    });
  }
  
 

  function getColumnValue(column, item) {
    var value = '';
    
    if (item === undefined) {
      return value; 
    }
    var columnFields = column.field.split('.'); 
    
    if (column.type == 'simple' || column.type == 'numeric') {
      value = item[column.field];
    }
    else if (column.type == 'html') {
      value = util_stripHtml(item[column.field]);
    }
    else if (column.type == 'soap-note') {
      value = util_stripHtml(item[column.field]);
      value =  util_truncate(value, 50);
    }
    else if (column.type == 'strip-html') {
      value = util_stripHtml(item[column.field]);
      value =  util_truncate(value, 50);
    }
    else if (column.type == 'date') {
      value = dateFormat(item[column.field], 'mm/dd/yyyy')
    }
    else if (column.type == 'date-time') {
      value = dateFormat(item[column.field], 'mm/dd/yyyy hh:mm')
    }
    else if (column.type == 'double') {
      var field0 = columnFields[0];
      var field1 = columnFields[1];
      if (item[field0] === undefined) {
        return value; 
      }
      value = item[field0][field1];
    }  
    else if (column.type == 'double-person') {

      var field0 = columnFields[0];
      var field1 = columnFields[1];
      if (item[field0] === undefined) {
        return value; 
      }
      value = util_buildFullName(item[field0]['firstName'], item[field0]['middleName'], item[field0]['lastName'])
    }
    else if (column.type == 'double-date') {
      var field0 = columnFields[0];
      var field1 = columnFields[1];
      if (item[field0] === undefined) {
        return value; 
      }  
      value = dateFormat(item[field0][field1], 'mm/dd/yyyy')
    }
    else if (column.type == 'triple-person') {
      var field0 = columnFields[0];
      var field1 = columnFields[1];
      var field2 = columnFields[2];
      if (item[field0] === undefined || item[field0][field1] === undefined) {
        return value; 
      }  
      value = util_buildFullName(item[field0][field1]['firstName'], item[field0][field1]['middleName'], item[field0][field1]['lastName'])
    }
    else if (column.type == 'triple') {
      var field0 = columnFields[0];
      var field1 = columnFields[1];
      var field2 = columnFields[2];
      if (item[field0] === undefined || item[field0][field1] === undefined) {
        return value; 
      }  
      value = item[field0][field1][field2];
    }
    else if (column.type == 'triple-date') {
      var field0 = columnFields[0];
      var field1 = columnFields[1];
      var field2 = columnFields[2];
      if (item[field0] === undefined || item[field0][field1] === undefined) {
        return value; 
      }  
      value = dateFormat(item[field0][field1][field2], 'mm/dd/yyyy')
    }
    else if (column.type == 'quad') {
      var field0 = columnFields[0];
      var field1 = columnFields[1];
      var field2 = columnFields[2];
      var field3 = columnFields[3];
      if (item[field0] === undefined || item[field0][field1] === undefined || item[field0][field1][field2] === undefined) {
        return value; 
      }  
      value = item[field0][field1][field2][field3];
    }
    return value;
  }

function viewPatientChart() {
  app_viewStack('patient-chart-screen', DO_SCROLL);
}

function viewDashboard() {
  app_viewStack('dashboard-screen', DO_SCROLL);
}

function unpark() {
  if($.trim($("#app-unpark-username").val()).length < 1 || $.trim($("#app-unpark-password").val()).length < 1) { 
    return;
  }
  var username = $.trim($("#app-unpark-username").val());
  var password = $.trim($("#app-unpark-password").val());
  
  $('#app-unpark-running').css({display: "block"});
  var jsonData = JSON.stringify({ username: username, password: password, sessionId: clinician.sessionId});
  $.post("app/unpark", {data:jsonData},
    function(data) {
      $('#app-login-error').css({display:'none'});
      var parsedData = $.parseJSON(data);
      
      $('#app-login-running').css({display: "none"});
        
      if (clinician.authStatus == CLINICIAN_STATUS_AUTHORIZED) {
        notificationText = clinicianFullName + ' unparked.';
        $('#modal-park').modal('hide'); 
        displayNotification(notificationText);
      }  
      else  {
        if (clinician.authStatus == CLINICIAN_STATUS_NOT_FOUND) {
          notificationText = 'Clinician not found in system';
        }
        else if (clinician.authStatus == CLINICIAN_STATUS_INVALID_PASSWORD) {
          notificationText = 'Invalid password';
        }
        else if (clinician.authStatus == CLINICIAN_STATUS_INACTIVE) {
          notificationText = 'Clinician is inactive';
        }
        $("#app-unpark-notification").html(notificationText);
      }
    }
  ); 
}

function login(demoMode, destination) {
  var username;
  var password;
  
  if (demoMode == false) {
    if($.trim($("#app-signin-username").val()).length < 1 || $.trim($("#app-signin-password").val()).length < 1) { 
      return;
    }
    username = $.trim($("#app-signin-username").val());
    password = $.trim($("#app-signin-password").val());
  }
  else {
    username = DEMO_USERNAME;
    password = DEMO_PASSWORD;
  }
   
  var jsonData = JSON.stringify({ username: username, password: password});
  $.post("app/login", {data:jsonData},
    function(data) {
      $('#app-login-error').css({display:'none'});
      var parsedData = $.parseJSON(data);
      clinician = new Clinician();
      clinician.id = parsedData.id;
      clinician.firstName = parsedData.firstName;
      clinician.middleName = parsedData.middleName;
      clinician.lastName = parsedData.lastName;
      clinician.streetAddress1 = parsedData.streetAddress1;
      clinician.streetAddress2 = parsedData.streetAddress2;
      clinician.city = parsedData.city;
      clinician.state = parsedData.state;
      clinician.zip = parsedData.zip;
      clinician.primaryPhone = parsedData.primaryPhone;
      clinician.secondaryPhone = parsedData.secondaryPhone;
      clinician.email = parsedData.email;
      clinician.authStatus = parsedData.authStatus;
      clinician.patientId = parsedData.patientId;
      clinician.previousLoginTime = parsedData.previousLoginTime;
      clinician.sessionId = parsedData.sessionId;
        
      if (clinician.authStatus == CLINICIAN_STATUS_AUTHORIZED) {
        clinicianFullName = util_buildFullName(clinician.firstName, clinician.middleName, clinician.lastName);
        notificationText = clinicianFullName + ' logged in.';
        
        if (destination == DEST_ENCOUNTER) {
          viewPatientCheckInList();
        }
        else {
          app_viewStack('dashboard-screen', DO_SCROLL); 
        }
      }  
      else  {
        if (clinician.authStatus == CLINICIAN_STATUS_NOT_FOUND) {
          notificationText = 'Clinician not found in system';
        }
        else if (clinician.authStatus == CLINICIAN_STATUS_INVALID_PASSWORD) {
          notificationText = 'Invalid password';
        }
        else if (clinician.authStatus == CLINICIAN_STATUS_INACTIVE) {
          notificationText = 'Clinician is inactive';
        }
      }
      displayNotification(notificationText);
    }
  );  
}


function park() {
  var jsonData = JSON.stringify({ sessionId: clinician.sessionId });
  $.post("app/park", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
  });
}


function logout() {
  if (clinician == null) {
    return;
  }
  var jsonData = JSON.stringify({ sessionId: clinician.sessionId });
  $.post("app/logout", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    app_viewStack('signin-screen', DO_SCROLL);
    $('#section-notification').css("visibility", "hidden");
    $('#section-notification-text').html("");
    notificationText = clinicianFullName + ' logged out.';
    displayNotification(notificationText);
    app_currentPatientId = null;
    clinician = null;
  });
}

function loadPatientInfo() {
  $('.patient-chart-full-name').html(app_patientChartFullName);
  $('.patient-chart-dob').html(app_patientChartDOB);
  $('.patient-chart-gender').html(app_patientChartGender);
  $('.patient-chart-mrn').html(app_patientChartMRN);
  $('.patient-chart-primary-phone').html(app_patientChartPrimaryPhone);
  $('.patient-chart-secondary-phone').html(app_patientChartSecondaryPhone);
  $('.patient-chart-headshot').attr('src', app_patientChartHeadshot);
}

function resetNavButtons() {
  $('a.nav-selected').addClass('nav');
  $('a.nav').removeClass('nav-selected');
}

$('a.nav').click(function() { 
  resetNavButtons();
  if ($(this).hasClass('toggle-selectable') ) {
    $(this).removeClass('nav');
    $(this).addClass('nav-selected');
  }
});

function displayNotification(text) {
  $('#wdm-notification-text').html(text);
  $('#wdm-notification').fadeIn(400).delay(3000).fadeOut(400); 
}

$('.app-check-in-link').click(function(){ 
  viewPatientCheckInList();
});


function getDataTableName(item) {
  var value = '';
  if(item.medicalTest) {
    value = item.medicalTest.name;
  }
  else if(item.healthTrendReport) {
    value = item.healthTrendReport.name;
  }
  return value;
}

