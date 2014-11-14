
/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

"use strict";

modulejs.define('app', 
                [ "notifier", "events", "util", "render_util", "table"], 
                function (Notifier, Event, Util, RenderUtil, Table) {
  var ENCOUNTER_FREE = 0;
  var ENCOUNTER_LOCKED = 1;
  var ENCOUNTER_OVERRIDDEN = 2;
  var ENCOUNTER_OWNED = 3;
  var DO_SCROLL = true;
  var DONT_SCROLL = false;
  var DO_AUTO_LOGOUT = true;
  var DEMO_MODE_ON = true;
  var DEMO_MODE_OFF = false;
  var DO_AUTO_SERVER_LOGOUT = true;
  var PASSWORD_PLACEHOLDER = 'not a password';
  var INITIALIZED = false;
  var RETURN_CODE_DUP_USERNAME = -1;
  var DEFAULT_TABLE_WIDTH = 772;
  var DEMO_USERNAME = 'jdoyle';
  var DEMO_PASSWORD = 'Njs2101$';
  var DEST_ENCOUNTER = 'encounter';
  
  var app = {}
  
  app.chiefComplaints;
  app.clinician = null;
  app.clinician;
  app.clinicianDashboard;
  app.clinicianFullName;
  app.clinicianMessages;
  app.clinicians;
  app.cptModifiers;
  app.currentCalendarView = 'month';
  app.currentCalendarView;
  app.currentChiefComplaintId;
  app.currentDxCodeId;
  app.currentEncounter;
  app.currentEncounterId;
  app.currentGroup;
  app.currentGroupId;
  app.currentGroupIndex;
  app.currentMedicationId;
  app.currentMessageId;
  app.currentPatient;
  app.currentPatientId = null;
  app.currentPatientId;
  app.currentQuestionId;
  app.currentSOAPNoteId;
  app.currentScreen = '';
  app.currentTxCodeId;
  app.encounter = null;
  app.groupOrderArray = [];
  app.groupOrderArray;
  app.idleInterval;
  app.idleTime = 0;
  app.newPatientEncounterGroup = undefined;
  app.newPatientEncounterGroup;
  app.oldEncounter;
  app.oldLockStatus;
  app.parkWarningDisplayed;
  app.patientCC;
  app.patientCCIndex = 0;
  app.patientConsultsIndex = 0;
  app.patientConsultsIndex;
  app.patientEncounterGroups = [];
  app.patientEncounters = [];
  app.patientExam;
  app.patientExamIndex = 0;
  app.patientOBGYN;
  app.patientOBGYNIndex = 0;
  app.patientSearchTypeAheads;
  app.patientSupp;
  app.patientSuppIndex = 0;
  app.patientVitalSigns;
  app.patientVitals;
  app.patientVitalsIndex = 0;
  app.patients;
  app.previousScreen = '';
  app.progressNotes;
  app.progressNotesIndex = 0;
  app.soapNote;
  app.soapNotes;
  app.usStates;

  var IdleTimer, SOAPNote, ChiefComplaints, PatientChart, ViewStack, PatientEncounter, PatientVitalSigns, Park, Calendar, Clinician, Patient, TwitterWjs, Util;
  
  app.init = function () {
    
    IdleTimer = modulejs.require("idle_timer");
    ViewStack = modulejs.require("view_stack");
    Clinician = modulejs.require("clinician"); 
    Patient = modulejs.require("patient");
    TwitterWjs = modulejs.require("twitter_wjs");
    Calendar = modulejs.require("calendar");
    ChiefComplaints = modulejs.require("chief_complaints");
    Park = modulejs.require("park");
    SOAPNote = modulejs.require("soap_note");
    PatientVitalSigns = modulejs.require("patient/vital_signs");
    PatientEncounter = modulejs.require("patient/encounter");
    PatientChart = modulejs.require("patient/chart");
    modulejs.require("events_handler");
    modulejs.require("reports");
    
    /***********      @JQUERY INIT    *******************/
    var jqueryInit = function () {
      $('#cloak').css({
        visibility: 'visible'
      });
      if (INITIALIZED == false) {
        getStaticLists();
        INITIALIZED = true;
        $(function () {
          $("[data-toggle='popover']").popover({
            trigger: "hover"
          });
        });
        viewSigninScreen();
        $('.dropdown-menu').find('form').click(function (e) {
          e.stopPropagation();
        });
        TwitterWjs.init();
        $('#section-notification').css("visibility", "hidden");
        $('#section-notification-text').html("");
        $('#app-check-in-print').click(function () {
          RenderUtil.render('print/print_check-in_list', {
            groups: app.patientEncounterGroups
          }, function (obj) {
            var s = obj[0].outerHTML;
            Printer.openPrintWindow('print.html', s,
              'PATIENT CHECK-IN LIST');
          });
        });
        $('#app-encounter-print-all').click(function () {
          var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
          RenderUtil.render('print/print_encounter_all', {
            encounter: app.currentEncounter,
            currentDate: currentDate
          }, function (obj) {
            var s = obj[0].outerHTML;
            Printer.openPrintWindow('print.html', s,
              'ENCOUNTER FORM');
          });
        });
        $(document).mousemove(function () {
          timerReset();
        });
        window.onbeforeunload = confirmBeforeUnload;
      }
    };
    $(document).ready(function () {
      var standalone = modulejs.require("standalone");
      standalone.ready(jqueryInit);
    }); /***********      @JQUERY INIT    *******************/
  };

  function confirmBeforeUnload() {
    if (app.clinician && app.clinician != null) {
      return "Please log out first in order to save your data.";
    }
  }

  function getStaticLists() {
      Ajax.post("app/getStaticLists", {}, function (data) {
        var parsedData = $.parseJSON(data);
        app.usStates = parsedData.usStates;
        app.cptModifiers = parsedData.cptModifiers;
      });
    }
    /***********      @LINKS    *******************/
  $('#soap-notes-link').click(function () {
    RenderUtil.render('soap_notes', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-soap-notes').modal('show');
      loadPatientInfo();
      getSOAPNotes(app.currentPatientId);
    });
  });
  $('.app-check-in-link').click(function () {
    viewPatientCheckInList();
  });
  $('.app-park-link').click(function () {
    Park.showDialog();
  });
  $('#patient-chart-summary-link').click(function () {
    RenderUtil.render('chart_summary', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-chart-summary').modal('show');
      getPatientChartSummary();
      loadPatientInfo();
    });
  });
  $('#health-maintenance-summary-link').click(function () {
    RenderUtil.render('health_maintenance_summary', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-health-maintenance-summary').modal('show');
      loadPatientInfo();
    });
  });
  $('#rx-medications-link').click(function () {
    RenderUtil.render('rx_medications', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-rx-medications').modal('show');
      loadPatientInfo();
    });
  });
  $('#obgyn-link').click(function () {
    RenderUtil.render('obgyn', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-obgyn').modal('show');
      loadPatientInfo();
      loadCurrentOBGYNScreen();
      $('#patient-obgyn-next-btn').click(function () {
        changeOBGYNScreen(1);
      });
      $('#patient-obgyn-prev-btn').click(function () {
        changeOBGYNScreen(-1);
      });
    });
  });
  $('#medical-history-link').click(function () {
    RenderUtil.render('medical_history', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-medical-history').modal('show');
      loadPatientInfo();
      loadHistScreenForm();
    });
  });
  $('#pfsh-link').click(function () {
    RenderUtil.render('pfsh', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-pfsh').modal('show');
      loadPatientInfo();
      loadPFSHScreenForm();
    });
  });
  $('#images-all-chart-sections-link').click(function () {
    RenderUtil.render('images_all_chart_sections', {}, function (s) {
      $('#modals-placement').html(s);
      $('#images-all-chart-sections').modal('show');
      loadPatientInfo();
    });
  });
  $('#x-ray-link').click(function () {
    RenderUtil.render('x_ray', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-x-ray').modal('show');
      loadPatientInfo();
    });
  });
  $('#ekg-link').click(function () {
    RenderUtil.render('ekg', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-ekg').modal('show');
      loadPatientInfo();
    });
  });
  $('#supp-link').click(function () {
    RenderUtil.render('supp', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-supp').modal('show');
      loadPatientInfo();
      loadCurrentSuppScreen();
      $('#patient-supp-next-btn').click(function () {
        changeSuppScreen(1);
      });
      $('#patient-supp-prev-btn').click(function () {
        changeSuppScreen(-1);
      });
    });
  });
  $('#exam-link').click(function () {
    RenderUtil.render('exam', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-exam').modal('show');
      loadPatientInfo();
      loadCurrentExamScreen();
      $('#patient-exam-next-btn').click(function () {
        changeExamScreen(1);
      });
      $('#patient-exam-prev-btn').click(function () {
        changeExamScreen(-1);
      });
    });
  });
  $('#attending-notes-link').click(function () {
    RenderUtil.render('attending_notes', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-attending-notes').modal('show');
      loadPatientInfo();
    });
  });
  $('#consults-link').click(function () {
    RenderUtil.render('consults', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-consults').modal('show');
      loadPatientInfo();
      loadCurrentConsultsScreen();
      $('#patient-follow-up-next-btn').click(function () {
        changeConsultsScreen(1);
      });
      $('#patient-follow-up-prev-btn').click(function () {
        changeConsultsScreen(-1);
      });
    });
  });
  $('#chief-complaints-link').click(function () {
    RenderUtil.render('chief_complaint', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-chief-complaint').modal('show');
      loadPatientInfo();
      getChiefComplaints(app.currentPatientId);
    });
  });
  $('#misc-docs-link').click(function () {
    RenderUtil.render('misc_info_documents', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-misc-info-documents').modal('show');
      loadPatientInfo();
    });
  });
  $('#laboratory-data-link').click(function () {
    RenderUtil.render('laboratory_data', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-laboratory-data').modal('show');
      loadPatientInfo();
    });
  });
  $('#flow-charts-link').click(function () {
    RenderUtil.render('flow_chart_selection', {}, function (s) {
      $('#modals-placement').html(s);
      $('#flow-chart-selection').modal('show');
      loadPatientInfo();
      $('#flow-chart-btn').click(function () {
        RenderUtil.render('flow_chart_for_anemia', {}, function (
          s) {
          $('#modals-placement').append(s);
          $('#modal-flow-chart-for-anemia').modal('show');
        });
      });
    });
  });
  $('#app-progress-notes-link').click(function () {
    RenderUtil.render('progress_notes', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-progress-notes').modal('show');
      loadPatientInfo();
      loadProgressNotesScreen();
      $('#progress-notes-next-btn').click(function () {
        changeProgressNotesScreen(1);
      });
      $('#progress-notes-prev-btn').click(function () {
        changeProgressNotesScreen(-1);
      });
      $('#btn-progress-notes-new').click(function () {
        newProgressNotesFormDialog();
      });
      $('#btn-progress-notes-save').click(function () {
        updateProgressNote();
      });
      $('#btn-progress-notes-close').click(function () {
        $('#modal-progress-notes').modal('hide');
      });
    });
  });
  $('#vital-signs-link').click(function () {
    RenderUtil.render('vital_signs', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-vital-signs').modal('show');
      loadPatientInfo();
      loadPatientVitalsScreen(app.currentPatientId);
    });
  });
  $('#app-problem-list-link').click(function () {
    RenderUtil.render('problem_list', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-problem-list').modal('show');
      loadPatientInfo();
      $('.newer-problem').click(function () {
        RenderUtil.render('new_problem', {}, function (s) {
          $('#modals-placement').append(s);
          $('#modal-new-problem').modal('show');
        });
      });
    });
  });
  $('.app-dashboard-link').click(function () {
    viewDashboard();
  });
  $('.app-messages-link').click(function () {
    viewMessages();
  });
  $('#encounter-link').click(function () {
    viewPatientEncounters();
  });
  $('.app-letters-link').click(function () {
    viewLetters();
  });
  $('.app-schedule-link').click(function () {
    viewSchedule();
  }); 
  
  /***********     END @LINKS    *******************/ 
  
  /***********      @DEFERRED METHODS    *******************/

  function loadCurrentExamScreen() {
    Patient.loadCurrentExamScreen();
  }
  
  function changeExamScreen(adjustment) {
    Patient.changeExamScreen(adjustment);
  } 
  
  function loadCurrentSuppScreen() {
    Patient.loadCurrentSuppScreen() ;
  }
  
  function changeSuppScreen(adjustment) {
    Patient.changeSuppScreen(adjustment) ;
  }
  
  function loadCurrentConsultsScreen() {
    Patient.loadCurrentConsultsScreen(); 
  }
  
  function changeConsultsScreen(adjustment) {
    Patient.changeConsultsScreen(adjustment) ;
  }
  
  function loadPFSHScreenForm() {
    Patient.loadPFSHScreenForm();
  }
  
  function loadHistScreenForm() {
    Patient.loadHistScreenForm(); 
  }
  
  function loadCurrentOBGYNScreen() {
    Patient.loadCurrentOBGYNScreen();
  }
  
  function changeOBGYNScreen(adjustment) {
    Patient.changeOBGYNScreen(adjustment);
  }
  
  function loadProgressNotesScreen() {
    Patient.loadProgressNotesScreen();
  }
  
  function changeProgressNotesScreen(adjustment) {
    Patient.changeProgressNotesScreen(adjustment);
  }
  
  function getPatientChartSummary() {
    PatientChart.getSummary();
  }

  function getPatientChart() {
    PatientChart.getChart();
  }

  function loadPatientInfo() {
    Patient.loadInfo();
  }

  function newProgressNotesFormDialog() {
    Patient.newProgressNotesFormDialog() ;
  }
  
  function updateProgressNote() {
    Patient.updateProgressNote();
  }
  
  function loadPatientVitalsScreen(patientId) {
    PatientVitalSigns.loadScreen(patientId);
  }
  
  function newEncounterFormDialog() {
    PatientEncounter.newEncounterFormDialog();
  }

  function getPatientEncountersListing() {
    PatientEncounter.getEncountersListing();
  }

  function getClinicianMessages() {
    Clinician.getMessages();
  }

  function getChiefComplaints(patientId) {
    ChiefComplaints.getComplaints(patientId);
  }

  function handleClickableRow(e) {
    Table.handleClickableRow(e);
  }

  function viewSOAPNote(id) {
    SOAPNote.viewNote(id);
  }

  function viewClinicianMessage() {
    Clinician.viewMessage();
  }

  function viewPatientEncounter(encounterId) {
    PatientEncounter.viewEncounter(encounterId);
  }

  function getSOAPNotes(patientId) {
      SOAPNote.getNotes(patientId);
    }
    /***********    END @DEFERRED METHODS *******************/
  
  $('#about').click(function () {
    RenderUtil.render('about', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-about').modal('show');
    });
  });
  $('#new-message').click(function () {
    RenderUtil.render('new_message', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-new-message').modal('show');
    });
  });
  $('#app-close-chart').click(function () {
    app.currentPatientId = null;
    $('#section-notification').css("visibility", "hidden");
    $('#section-notification-text').html("");
    viewDashboard();
  });
  $('#app-change-patient').click(function () {
    patientSearchDialog();
  });
  $('.patient-button-group').click(function () {
    if (app.currentPatientId != null) {
      if (app.currentScreen != 'patient-chart-screen') {
        app.viewPatientChartScreen();
      }
      return;
    }
    patientSearchDialog();
    initPatientSearchTypeAheads();
  });

  function initPatientSearchTypeAheads() {
    var clinician = app.clinician;
    var jsonData = JSON.stringify({
      clinicianId: clinician.id,
      sessionId: clinician.sessionId
    });
    Ajax.post("app/getPatientSearchTypeAheads", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      var patientSearchTypeAheads = app.patientSearchTypeAheads =
        parsedData.patientSearchTypeAheads;
      $('#patient-search-first-name').typeahead({
        hint: true,
        highlight: true,
        minLength: 1
      }, {
        name: 'firstNames',
        displayKey: 'value',
        source: Util.substringMatcher(patientSearchTypeAheads.firstNames)
      });
      $('#patient-search-middle-name').typeahead({
        hint: true,
        highlight: true,
        minLength: 1
      }, {
        name: 'middleNames',
        displayKey: 'value',
        source: Util.substringMatcher(patientSearchTypeAheads.middleNames)
      });
      $('#patient-search-last-name').typeahead({
        hint: true,
        highlight: true,
        minLength: 1
      }, {
        name: 'lastNames',
        displayKey: 'value',
        source: Util.substringMatcher(patientSearchTypeAheads.lastNames)
      });
      $('#patient-search-city').typeahead({
        hint: true,
        highlight: true,
        minLength: 1
      }, {
        name: 'cities',
        displayKey: 'value',
        source: Util.substringMatcher(patientSearchTypeAheads.cities)
      });
    });
  }

  function patientSearch() {
    var dob = Util.processDob("#patient-search-dob", dob);
    var clinician = app.clinician;
    var jsonData = JSON.stringify({
      id: clinician.id,
      firstNameFilter: $.trim($("#patient-search-first-name").val()),
      middleNameFilter: $.trim($("#patient-search-middle-name").val()),
      lastNameFilter: $.trim($("#patient-search-last-name").val()),
      cityFilter: $.trim($("#patient-search-city").val()),
      genderFilter: $.trim($("#patient-search-gender").val()),
      dobFilter: dob,
      sessionId: clinician.sessionId
    });
    Util.debug("patient json data: " + jsonData);
    Ajax.post("app/patientSearch", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      var patients = app.patients = parsedData.patients;
      RenderUtil.render('component/simple_data_table', {
        items: patients,
        title: 'Patients',
        tableName: 'patient-search-results',
        clickable: true,
        columns: [{
          title: 'Full Name',
          field: 'cred.firstName',
          type: 'double-person'
        }, {
          title: 'Date of Birth',
          field: 'demo.dob',
          type: 'double-date'
        }, {
          title: 'Gender',
          field: 'demo.gender.name',
          type: 'triple'
        }, {
          title: 'City',
          field: 'demo.city',
          type: 'double'
        }]
      }, function (s) {
        $('#patient-search-results').html(s);
        $('#patient-search-results-title').html("Patient Search");
        $('.clickable-table-row').click(function (e) {
          $(this).addClass('table-row-highlight').siblings().removeClass(
            'table-row-highlight');
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
    RenderUtil.render('patient_search', {}, function (s) {
      clearPatientSearchForm();
      $('#modals-placement').html(s);
      $('#modal-patient-search').modal('show');
      $('#btn-patient-search-ok').prop('disabled', true);
      $('#btn-patient-search-ok').addClass('disabled');
      $('.clickable-table-row').removeClass('table-row-highlight');
      $('#btn-patient-search-search').click(function () {
        patientSearch();
      });
      $('#btn-patient-search-clear').click(function () {
        clearPatientSearchForm();
      });
      $('#btn-patient-search-ok').click(function () {
        getPatientChart();
      });
      $('#patient-search-dob').mask("99/99/9999");
      getRecentPatients();
    });
  }

  function getRecentPatients() {
    var clinician = app.clinician;
    var jsonData = JSON.stringify({
      clinicianId: clinician.id,
      sessionId: clinician.sessionId
    });
    Ajax.post("app/getRecentPatients", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      var patients = app.patients = parsedData.patients;
      RenderUtil.render('component/simple_data_table', {
          items: patients,
          title: 'Recent Patients',
          tableName: 'patient-search-results',
          clickable: true,
          columns: [{
            title: 'Full Name',
            field: 'cred.firstName',
            type: 'double-person'
          }, {
            title: 'Date of Birth',
            field: 'demo.dob',
            type: 'double-date'
          }, {
            title: 'Gender',
            field: 'demo.gender.name',
            type: 'triple'
          }, {
            title: 'City',
            field: 'demo.city',
            type: 'double'
          }]
        },
        function (s) {
          $('#patient-search-results').html(s);
          $('#patient-search-results-title').html("Recent Patients");
          $('.clickable-table-row').click(function (e) {
            $(this).addClass('table-row-highlight').siblings().removeClass(
              'table-row-highlight');
            handleClickableRow(e);
          });
        });
    });
  }
  $('#app-signin-submit').click(function () {
    login(DEMO_MODE_OFF);
  });
  $('#app-signin-encounter-submit').click(function () {
    login(DEMO_MODE_OFF, DEST_ENCOUNTER);
  });
  $('#get-started-btn').click(function () {
    login(DEMO_MODE_ON);
  });
  $('.app-exit').click(function () {
   app.logout();
  });
  $('#message-view-button').click(function () {
    viewClinicianMessage();
  });
  $('#message-close-button').click(function () {
    viewMessages();
  });

  function viewPatientEncounters() {
    viewPatientEncountersScreen();
    $('#app-new-encounter-btn').click(function () {
      newEncounterFormDialog();
    });
    getPatientEncountersListing();
  }

  function viewPatientEncountersScreen() {
    ViewStack.show('patient-encounters-screen', DO_SCROLL);
  }

  function viewMessages() {
    viewMessagesScreen();
    $('#messages-inbox-header').html('Inbox');
    Clinician.getMessages();
  }

  function viewMessagesScreen() {
    ViewStack.show('messages-screen', DO_SCROLL);
  }

  function viewLetters() {
    ViewStack.show('letters-screen', DO_SCROLL);
  }

  function viewScheduleScreen() {
    ViewStack.show('schedule-screen', DO_SCROLL);
  }

  function viewSchedule() {
    viewScheduleScreen();
    Calendar.load();
  }

  function timerReset() {
    IdleTimer.reset();
  }

  function viewPatientCheckInList() {}

  function viewAdminScreen() {
    ViewStack('user-admin-screen', DO_SCROLL);
  }

  function viewSigninScreen() {
    ViewStack.show('signin-screen', DO_SCROLL);
  }

  function viewDashboard() {
    ViewStack.show('dashboard-screen', DO_SCROLL);
  }

  app.viewReportsScreen = function() {
    ViewStack.show('reports-screen', DO_SCROLL);
  }

  app.viewPatientChartScreen = function() {
    ViewStack.show('patient-chart-screen', DO_SCROLL);
  }

  function login(demoMode, destination) {
    var username;
    var password;
    if (demoMode == false) {
      if ($.trim($("#app-signin-username").val()).length < 1 || $.trim($(
          "#app-signin-password").val()).length <
        1) {
        return;
      }
      username = $.trim($("#app-signin-username").val());
      password = $.trim($("#app-signin-password").val());
    } else {
      username = DEMO_USERNAME;
      password = DEMO_PASSWORD;
    }
    var jsonData = JSON.stringify({
      username: username,
      password: password
    });
    Ajax.post("app/login", {
      data: jsonData
    }, function (data) {
      $('#app-login-error').css({
        display: 'none'
      });
      var parsedData = $.parseJSON(data);
      var clinician = Clinician.load(parsedData);
      if (clinician.authStatus == Clinician.STATUS_AUTHORIZED) {
        var clinicianFullName = app.clinicianFullName = Util.buildFullName(
          clinician.firstName,
          clinician.middleName, clinician.lastName);
        var notificationText = clinicianFullName + ' logged in.';
        if (destination == DEST_ENCOUNTER) {
          viewPatientCheckInList();
        } else {
          viewDashboard();
        }
        IdleTimer.run();
      } else {
        if (clinician.authStatus == Clinician.STATUS_NOT_FOUND) {
          notificationText = 'Clinician not found in system';
        } else if (clinician.authStatus == Clinician.STATUS_INVALID_PASSWORD) {
          notificationText = 'Invalid password';
        } else if (clinician.authStatus == Clinician.STATUS_INACTIVE) {
          notificationText = 'Clinician is inactive';
        }
      }
      Notifier.notify(notificationText);
    });
  }

  function resetNavButtons() {
    $('a.nav-selected').addClass('nav');
    $('a.nav').removeClass('nav-selected');
  }
  $('a.nav').click(function () {
    resetNavButtons();
    if ($(this).hasClass('toggle-selectable')) {
      $(this).removeClass('nav');
      $(this).addClass('nav-selected');
    }
  });

  app.logout = function () {
    if (app.clinician == null) {
      return;
    }
    var jsonData = JSON.stringify({
      sessionId: app.clinician.sessionId
    });
    Ajax.post("app/logout", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      viewSigninScreen();
      $('#section-notification').css("visibility", "hidden");
      $('#section-notification-text').html("");
      var notificationText = app.clinicianFullName + ' logged out.';
      IdleTimer.clear();
      Notifier.notify(notificationText);
      app.currentPatientId = null;
      app.clinician = null;
    });
  };
  return app;
});