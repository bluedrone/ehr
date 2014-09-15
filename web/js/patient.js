/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

function getChiefComplaints(patientId) {
  var jsonData = JSON.stringify({ patientId: patientId, sessionId: clinician.sessionId });
  $.post("patient/getChiefComplaints", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    chiefComplaints = parsedData.chiefComplaints;
    RenderUtil.render('component/simple_data_table', 
     {items:chiefComplaints, 
      title:'Chief Complaints', 
      tableName:'chief-complaints-list', 
      clickable:true, 
      columns:[
        {title:'Date', field:'date', type:'date'},
        {title:'Description', field:'description', type:'strip-html'}
      ]}, function(s) {
      $('#chief-complaint-list').html(s);
      $('#chief-complaint-list-title').html("Chief Complaints");
      $('#chief-complaint-print').addClass("disabled");
      $('.clickable-table-row').click( function(e){ 
        $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        handleClickableRow(e); 
      });
    });
  });
}



function viewChiefComplaint(chiefComplaintId) {
  for (i=0;i<chiefComplaints.length;i++) {
    if (chiefComplaints[i].id == chiefComplaintId) {
      chiefComplaint = chiefComplaints[i]; 
      break;
    }
  }
  
  var date = dateFormat(chiefComplaint.date, 'mm/dd/yyyy')
  $('#patient-cc-date').html(date);
  $('#pain-scale-value').html(chiefComplaint.painScale);
  $('#chief-complaint').html(chiefComplaint.description);
  $('#specific-location').html(chiefComplaint.specificLocation);
  util_selectCheckboxesFromList(chiefComplaint.occursWhen, 'occurs-when');
  $('#hours-since').html(chiefComplaint.hoursSince);
  $('#days-since').html(chiefComplaint.daysSince);
  $('#weeks-since').html(chiefComplaint.weeksSince);
  $('#months-since').html(chiefComplaint.monthsSince);
  $('#years-since').html(chiefComplaint.yearsSince);
  $('#how-long-other').html(chiefComplaint.howLongOther);
  $('#pain-scale').html(chiefComplaint.painScale);
  $('#pain-type').html(chiefComplaint.painType);
  $('#pain-x-hour').html(chiefComplaint.painXHour);
  $('#pain-x-day').html(chiefComplaint.painXDay);
  $('#pain-x-week').html(chiefComplaint.painXWeek);
  $('#pain-x-month').html(chiefComplaint.painXMonth);
  $('#pain-duration').html(chiefComplaint.painDuration);
  util_selectCheckboxesFromList(chiefComplaint.denies, 'denies');
  $('#denies-other').html(chiefComplaint.deniesOther);
  
  $('#chief-complaint-print').removeClass("disabled");
  $('#chief-complaint-print').off().on('click', function () { printPatientForm('print_patient_cc', 'CHIEF COMPLAINT', chiefComplaint)});
}


function getSOAPNotes(patientId) {
  var jsonData = JSON.stringify({ patientId: patientId, sessionId: clinician.sessionId });
  $.post("patient/getSOAPNotes", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    soapNotes = parsedData.soapNotes;
    RenderUtil.render('component/simple_data_table', 
     {items:soapNotes, 
      title:'SOAP Notes', 
      tableName:'soap-notes-list', 
      clickable:true, 
      columns:[
        {title:'Date', field:'date', type:'date'},
        {title:'Subjective', field:'subjective', type:'soap-note'},
        {title:'Objective', field:'objective', type:'soap-note'},
        {title:'Assessment', field:'assessment', type:'soap-note'},
        {title:'Plan', field:'plan', type:'soap-note'}
      ]}, function(s) {
      $('#soap-notes-list').html(s);
      $('#soap-notes-list-title').html("SOAP Notes");
      $('#soap-notes-print').addClass("disabled");
      $('.clickable-table-row').click( function(e){ 
        $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        handleClickableRow(e); 
      });
    });
  });
}



function viewSOAPNote(soapNoteId) {
  for (i=0;i<soapNotes.length;i++) {
    if (soapNotes[i].id == soapNoteId) {
      soapNote = soapNotes[i]; 
      break;
    }
  }
  $('#soap-note-subjective').html(soapNote.subjective);
  $('#soap-note-objective').html(soapNote.objective);
  $('#soap-note-assessment').html(soapNote.assessment);
  $('#soap-note-plan').html(soapNote.plan);
  $('#soap-notes-print').removeClass("disabled");
  $('#soap-notes-print').off().on('click', function () { printPatientForm('print_soap_note', 'SOAP NOTE', soapNote)});
}



function printPatientForm(template, title, object) { 
  var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
  RenderUtil.render('print/'+template,  {object:object, currentDate:currentDate}, function(obj) {
    var s = obj[0].outerHTML;
    print_openPrintWindow('print.html', s, title);
  });
}


function printPatientTable(template, title, items, columns) { 
  var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
  RenderUtil.render('print/'+template,  {items:items, currentDate:currentDate, columns:columns}, function(obj) {
    var s = obj[0].outerHTML;
    print_openPrintWindow('print.html', s, title);
  });
}


function loadHistScreenForm() {
  object = app_currentPatient.hist; 
  
  RenderUtil.render('component/patient_medications', {patient: app_currentPatient}, function(s) { 
    $('#patient-medications').html(s); 
    $('#modal-medical-history .form-control-unsaved').css({display: "none"});
    $('.patient-med-editable').blur(function(e) { 
      getCurrentMedicationId(e);
      updatePatientMedication("medication", $(this).html(), app_currentMedicationId); 
    });
    $('.patient-dose-editable').blur(function(e) { 
      getCurrentMedicationId(e);
      updatePatientMedication("dose", $(this).html(), app_currentMedicationId); 
    });
    $('.patient-freq-editable').blur(function(e) { 
      getCurrentMedicationId(e);
      updatePatientMedication("frequency", $(this).html(), app_currentMedicationId); 
    });
    $('.medication-delete-control').click(function() { deleteMedication($(this)); });
  });
      
  $('#patient-past-s-m-saved').html(object.pastSM);
  util_selectCheckboxesFromList(object.famHist, 'patient-fam-hist-');
  $('#patient-fam-hist-notes-saved').html(object.famHistNotes);
  $('#patient-fam-hist-other-saved').html(object.famHistOther);
  $('#patient-allerg-food-saved').html(object.allergFood);
  $('#patient-allerg-drug-saved').html(object.allergDrug);
  $('#patient-allerg-env-saved').html(object.allergEnv);
  $('input[name=patient-vacc][value='+object.vacc+']').attr("checked", true);
  $('#patient-vacc-notes-saved').html(object.vaccNotes);
  util_selectCheckboxesFromList(object.subst, 'patient-subst-');
  $('#patient-smoke-pks-day-saved').html(object.smokePksDay);
  $('#patient-years-smoked-saved').html(object.yearsSmoke);
  $('#patient-smoke-years-quit-saved').html(object.smokeYearsQuit);
  $('#patient-etoh-units-week-saved').html(object.etohUnitsWeek);
  $('#patient-current-drugs-saved').html(object.currentDrugs);
  
  $('#patient-hist-new-medication').click(function() { 
    var jsonData = JSON.stringify({sessionId: clinician.sessionId, patientId:  app_currentPatient.id});
    $.post("patient/addPatientMedication", {data:jsonData}, function(data) {
      var parsedData = $.parseJSON(data);
      var patientMedicationId = parsedData.patientMedicationId;
      var numMedications = $("#patient-medications").children().length + 2;
      RenderUtil.render('component/patient_medication', {ordinal:numMedications, id: patientMedicationId}, function(s) { 
        $("#patient-medications").append(s); 
        $('#modal-medical-history .form-control-unsaved').css({display: "none"});
        $('.patient-med-editable').blur(function(e) { 
          getCurrentMedicationId(e);
          updatePatientMedication("medication", $(this).html(), patientMedicationId); 
        });
        $('.patient-dose-editable').blur(function(e) { 
          getCurrentQuestionId(e);
          updatePatientMedication("dose", $(this).html(), patientMedicationId); 
        });
        $('.patient-freq-editable').blur(function(e) { 
          getCurrentQuestionId(e);
          updatePatientMedication("frequency", $(this).html(), patientMedicationId); 
        });
        $('.medication-delete-control').click(function() { deleteMedication($(this)); });
      });
    });
  });
  
  $('#patient-past-s-m-saved').blur(function() { updateSavedPatient("pastSM", $(this).html()); });
    $('input[name=patient-fam-hist-'+']').click(function() { 
      var famHist = $('input[name=patient-fam-hist-'+']:checked').map(function() {return this.value;}).get().join(',');
      updateSavedPatient("famHist", famHist); 
    });
    $('#patient-fam-hist-notes-saved').blur(function() { updateSavedPatient("famHistNotes", $(this).html()); });
    $('#patient-fam-hist-other-saved').blur(function() { updateSavedPatient("famHistOther", $(this).html()); });
    $('#patient-allerg-food-saved').blur(function() { updateSavedPatient("allergFood", $(this).html()); });
    $('#patient-allerg-drug-saved').blur(function() { updateSavedPatient("allergDrug", $(this).html()); });
    $('#patient-allerg-env-saved').blur(function() { updateSavedPatient("allergEnv", $(this).html()); });
    $('input[name=patient-vacc]').click(function() { updateSavedPatient("vacc", $(this).val() == 'true'); });
    $('#patient-vacc-notes-saved').blur(function() { updateSavedPatient("vaccNotes", $(this).html()); });
    $('input[name=patient-subst]').click(function() { 
      var subst = $('input[name=patient-subst]:checked').map(function() {return this.value;}).get().join(',');
      updateSavedPatient("subst", subst); 
    });
    $('#patient-smoke-pks-day-saved').blur(function() { updateSavedPatient("smokePksDay", $(this).html()); });
    $('#patient-years-smoked-saved').blur(function() { updateSavedPatient("yearsSmoked", $(this).html()); });
    $('#patient-smoke-years-quit-saved').blur(function() { updateSavedPatient("smokeYearsQuit", $(this).html()); });
    $('#patient-etoh-units-week-saved').blur(function() { updateSavedPatient("etohUnitsWeek", $(this).html()); });
    $('#patient-current-drugs-saved').blur(function() { updateSavedPatient("currentDrugs", $(this).html()); });
    $('#patient-hist-print').click(function() { printPatientForm('print_patient_hist', 'MEDICAL HISTORY', object)});
}


function loadPFSHScreenForm() {
  object = app_currentPatient.pfsh; 
  $('#modal-pfsh .form-control-unsaved').css({display: "none"});
  $('#patient-num-residents-saved').html(object.numResidents);
  $('#patient-job-type-saved').html(object.jobType);
  $('input[name=patient-mother-alive][value='+object.motherAlive+']').attr("checked", true);
  $('#patient-mother-death-reason-saved').html(object.motherDeathReason);
  $('input[name=patient-father-alive][value='+object.fatherAlive+']').attr("checked", true);
  $('#patient-father-death-reason-saved').html(object.fatherDeathReason);
  $('input[name=patient-partner-alive][value='+object.partnerAlive+']').attr("checked", true);
  $('#patient-partner-death-reason-saved').html(object.partnerDeathReason);
  $('#patient-num-siblings-saved').html(object.numSiblings);
  $('#patient-num-brothers-saved').html(object.numBrothers);
  $('#patient-num-sisters-saved').html(object.numSisters);
  $('#patient-num-children-saved').html(object.numChildren);
  $('#patient-num-sons-saved').html(object.numSons);
  $('#patient-num-daughters-saved').html(object.numDaughters);
  $('#patient-num-residents-saved').blur(function() { updateSavedPatient("numResidents", $(this).html()); });
  $('#patient-job-type-saved').blur(function() { updateSavedPatient("jobType", $(this).html()); });
  $('input[name=patient-mother-alive]').click(function() { updateSavedPatient("motherAlive", $(this).val() == 'true'); });
  $('#patient-mother-death-reason-saved').blur(function() { updateSavedPatient("motherDeathReason", $(this).html()); });
  $('input[name=patient-father-alive]').click(function() { updateSavedPatient("fatherAlive", $(this).val() == 'true'); });
  $('#patient-father-death-reason-saved').blur(function() { updateSavedPatient("fatherDeathReason", $(this).html()); });
  $('input[name=patient-partner-alive]').click(function() { updateSavedPatient("partnerAlive", $(this).val() == 'true'); });
  $('#patient-partner-death-reason-saved').blur(function() { updateSavedPatient("partnerDeathReason", $(this).html()); });
  $('#patient-num-siblings-saved').blur(function() { updateSavedPatient("numSiblings", $(this).html()); });
  $('#patient-num-brothers-saved').blur(function() { updateSavedPatient("numBrothers", $(this).html()); });
  $('#patient-num-sisters-saved').blur(function() { updateSavedPatient("numSisters", $(this).html()); });
  $('#patient-num-children-saved').blur(function() { updateSavedPatient("numChildren", $(this).html()); });
  $('#patient-num-sons-saved').blur(function() { updateSavedPatient("numSons", $(this).html()); });
  $('#patient-num-daughters-saved').blur(function() { updateSavedPatient("numDaughters", $(this).html()); });
  $('#patient-pfsh-print').click(function() { printPatientForm('print_patient_pfsh', 'SOCIAL & FAMILY HISTORY', object)});
}



function updateSavedPatient(property, value, isDualMode, elementId, valueName) {
  updateLocalPatient(property, value);
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    patientId: app_currentPatient.id,
    updateProperty:property,
    updatePropertyValue:value
  });
  $.post("patient/updatePatient", {data:jsonData}, function(data) {
    if (isDualMode) {
      var unsavedId = elementId.replace('-saved','');
      $('#'+unsavedId).css({display: "none"});
      $('#'+elementId).html(valueName ? valueName : value);
      $('#'+elementId).css({display: "block"});
    }
  }); 
}


function updateLocalPatient(property, value) {
  app_currentPatient[property] = value;  
}

function loadCurrentSuppScreen() {
  app_patientSupp = []; 	
  for (i=0;i<app_patientEncounters.length;i++) {
    app_patientSupp.push(app_patientEncounters[i].supp); 
  }
  if (app_patientSupp.length == 0) {
    return;
  }
  app_patientSuppIndex = 0;
  loadSuppScreenForm();
  $('#patient-supp-print').click(function() { printPatientForm('print_patient_supp', 'SUPPLEMENTAL QUESTIONS', app_patientSupp[app_patientSuppIndex])});
}


function changeSuppScreen(adjustment) {
  if ((app_patientSuppIndex == 0 && adjustment == -1) || (app_patientSuppIndex == app_patientSupp.length-1 && adjustment == 1)) {
    return;
  }
  app_patientSuppIndex += adjustment;
  loadSuppScreenForm();
}

function loadSuppScreenForm() {
  var date = dateFormat(app_patientSupp[app_patientSuppIndex].date, 'mm/dd/yyyy')
  $('#patient-supp-date').html(date);
  $('#num-cups-water').html(app_patientSupp[app_patientSuppIndex].numCupsWater);
  $('#num-cups-coffee').html(app_patientSupp[app_patientSuppIndex].numCupsCoffee);
  $('#num-cups-tea').html(app_patientSupp[app_patientSuppIndex].numCupsTea);
  $('#water-source').html(app_patientSupp[app_patientSuppIndex].waterSource);
      
  RenderUtil.render('component/encounter_questions', {encounter:app_patientEncounters[app_patientSuppIndex]}, function(s) { 
    $("#questions").html(s); 
    //setEncounterFormMode(id, section, savedState, hasOwnership);
  });
}


function loadCurrentConsultsScreen() {
  app_patientConsults = []; 	
  for (i=0;i<app_patientEncounters.length;i++) {
    app_patientConsults.push(app_patientEncounters[i].followUp); 
  }
  if (app_patientConsults.length == 0) {
    return;
  }
  app_patientConsultsIndex = 0;
  loadConsultsScreenForm();
  $('#patient-follow-up-print').click(function() { printPatientForm('print_patient_follow-up', 'FOLLOW UP', app_patientConsults[app_patientConsultsIndex])});
}


function changeConsultsScreen(adjustment) {
  if ((app_patientConsultsIndex == 0 && adjustment == -1) || (app_patientConsultsIndex == app_patientConsults.length-1 && adjustment == 1)) {
    return;
  }
  app_patientConsultsIndex += adjustment;
  loadConsultsScreenForm();
}

function loadConsultsScreenForm() {
   var date = dateFormat(app_patientConsults[app_patientConsultsIndex].date, 'mm/dd/yyyy')
  $('#patient-follow-up-date').html(date);
  $('input[name=follow-up-level][value='+app_patientConsults[app_patientConsultsIndex].level+']').attr("checked", true);
  $('#follow-up-when').html(app_patientConsults[app_patientConsultsIndex].when);
  $('#follow-up-condition').html(app_patientConsults[app_patientConsultsIndex].condition);
  $('#follow-up-dispense-rx').html(app_patientConsults[app_patientConsultsIndex].dispenseRx);
  $('#follow-up-uss').html(app_patientConsults[app_patientConsultsIndex].USS);
  $('#follow-up-pregnant').html(app_patientConsults[app_patientConsultsIndex].pregnant);
  $('#follow-up-wound-care').html(app_patientConsults[app_patientConsultsIndex].woundCare);
  $('#follow-up-ref-to-specialist').html(app_patientConsults[app_patientConsultsIndex].refToSpecialist);
  $('#follow-up-dental-list').html(app_patientConsults[app_patientConsultsIndex].dentalList);
  $('#follow-up-physiotherapy').html(app_patientConsults[app_patientConsultsIndex].physiotherapy);
  $('#follow-up-blood-labs').html(app_patientConsults[app_patientConsultsIndex].bloodLabs);
  $('#follow-up-other').html(app_patientConsults[app_patientConsultsIndex].other);
  $('#follow-up-pulmonary-fx-test').html(app_patientConsults[app_patientConsultsIndex].pulmonaryFXTest);
  $('#follow-up-vision').html(app_patientConsults[app_patientConsultsIndex].vision);
  $('input[name=follow-up-completed][value='+app_patientConsults[app_patientConsultsIndex].followUpCompleted+']').attr("checked", true) == 'true';
  var followUpDate = dateFormat(app_patientConsults[app_patientConsultsIndex].followUpDate, 'mm/dd/yyyy')
  $('#follow-up-date').html(followUpDate);
}


function loadProgressNotesScreen() {
  var jsonData = JSON.stringify({ patientId: app_currentPatientId, sessionId: clinician.sessionId });
  $.post("patient/getProgressNotes", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    app_progressNotes = parsedData.progressNotes;
    if (!app_progressNotes || app_progressNotes.length == 0) {
      return;
    }
    app_progressNotesIndex = 0;
    loadProgressNotesScreenForm();
    $('#btn-progress-notes-print').click(function() { util_printForm('print_progress_note', app_progressNotes[app_progressNotesIndex], 'PROGRESS NOTE')});
  });
}

function changeProgressNotesScreen(adjustment) {
  if ((app_progressNotesIndex == 0 && adjustment == -1) || (app_progressNotesIndex == app_progressNotes.length-1 && adjustment == 1)) {
    return;
  }
  app_progressNotesIndex += adjustment;
  loadProgressNotesScreenForm();
}

function loadProgressNotesScreenForm() {
  var date = dateFormat(app_progressNotes[app_progressNotesIndex].date, 'mm/dd/yyyy')
  $('#progress-notes-date').html(date);
  
  if (app_progressNotes[app_progressNotesIndex].completed == true) {
    $('#progress-notes-subject-saved').html(app_progressNotes[app_progressNotesIndex].subject);
    $('#progress-notes-content-saved').html(app_progressNotes[app_progressNotesIndex].content);
    $('#modal-progress-notes .form-control-unsaved').css({display: "none"});
    $('#modal-progress-notes .form-control-saved').css({display: "block"});
  }
  else {
    $('#progress-notes-subject').val(app_progressNotes[app_progressNotesIndex].subject);
    $('#progress-notes-content').html(app_progressNotes[app_progressNotesIndex].content);
    $('#modal-progress-notes .form-control-saved').css({display: "none"});
    $('#modal-progress-notes .form-control-unsaved').css({display: "block"});
  }
}

function newProgressNotesFormDialog() {
  if (app_progressNotes[app_progressNotesIndex].completed == true) {
    newProgressNotesForm();
    return;
  }	  
  var args = {
    modalTitle:"Complete Progress Note Confirmation", 
    modalH3:"Ready To Complete The Currently Open Progress Note?", 
    modalH4:"In order to start a new progress note the current one needs to be completed.",
    cancelButton: 'Cancel',
    okButton: 'Confirm'
  };
  RenderUtil.render('dialog/confirm', args, function(s) { 
    $('#modals-placement').append(s);
    $('#modal-confirm').modal('show'); 
    $('#app-modal-confirmation-btn').click(function() {  
      var jsonData = JSON.stringify({ sessionId: clinician.sessionId, progressNoteId: app_progressNotes[app_progressNotesIndex].id});
      $.post("patient/closeProgressNote", {data:jsonData}, function(data) {
        var parsedData = $.parseJSON(data);
        displayNotification('Progress Note Completed');
        app_progressNotes[app_progressNotesIndex].completed = true;
        newProgressNotesForm();
      });
    });
  });
}

function updateProgressNote() {
  app_progressNotes[app_progressNotesIndex].subject = $('#progress-notes-subject').val();
  app_progressNotes[app_progressNotesIndex].content = $('#progress-notes-content').html();
  var jsonData = JSON.stringify({ sessionId: clinician.sessionId, progressNote: app_progressNotes[app_progressNotesIndex]});
  $.post("patient/updateProgressNote", {data:jsonData}, function(data) {
    displayNotification('Progress Note Saved');
  });
}

function newProgressNotesForm() {
  var jsonData = JSON.stringify({ patientId: app_currentPatientId, sessionId: clinician.sessionId });
  $.post("patient/newProgressNote", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    var newProgressNote = parsedData.progressNote;
    app_progressNotes.unshift(newProgressNote);
    $('#progress-notes-subject').val('');
    $('#progress-notes-content').html('');
    $('#modal-progress-notes .form-control-saved').css({display: "none"});
    $('#modal-progress-notes .form-control-unsaved').css({display: "block"});
  });
}
    
function loadCurrentExamScreen() {
  app_patientExam = []; 	
  for (i=0;i<app_patientEncounters.length;i++) {
    app_patientEncounters[i].exam.hb =  app_patientEncounters[i].lab.hb;
    app_patientEncounters[i].exam.glucose =  app_patientEncounters[i].lab.glucose;
    app_patientEncounters[i].exam.urineDip =  app_patientEncounters[i].lab.urineDip;
    app_patientExam.push(app_patientEncounters[i].exam); 
  }
  if (app_patientExam.length == 0) {
    return;
  }
  app_patientExamIndex = 0;
  loadExamScreenForm();
  $('#patient-exam-print').click(function() { printPatientForm('print_patient_exam', 'EXAM', app_patientExam[app_patientExamIndex])});
}


function changeExamScreen(adjustment) {
  if ((app_patientExamIndex == 0 && adjustment == -1) || (app_patientExamIndex == app_patientExam.length-1 && adjustment == 1)) {
    return;
  }
  app_patientExamIndex += adjustment;
  loadExamScreenForm();
}

function loadExamScreenForm() {
  var date = dateFormat(app_patientExam[app_patientExamIndex].date, 'mm/dd/yyyy')
  $('#patient-exam-date').html(date);
  $('#hs').html(app_patientExam[app_patientExamIndex].hs);
  $('input[name=heart-rhythm][value='+app_patientExam[app_patientExamIndex].heartRhythm+']').attr("checked", true);
  $('#lab-hb').html(app_patientExam[app_patientExamIndex].hb);
  $('#lab-glucose').html(app_patientExam[app_patientExamIndex].glucose);
  $('#lab-urine-dip').html(app_patientExam[app_patientExamIndex].urineDip);
  $('#diagnosis').html(app_patientExam[app_patientExamIndex].diagnosis);
  $('#dx-code').html(app_patientExam[app_patientExamIndex].dxCode);
  $('#treatment-plan').html(app_patientExam[app_patientExamIndex].treatmentPlan);
  $('#tx-code').html(app_patientExam[app_patientExamIndex].txCode);
}

    
function loadCurrentOBGYNScreen() {
  app_patientOBGYN = []; 	
  for (i=0;i<app_patientEncounters.length;i++) {
    app_patientOBGYN.push(app_patientEncounters[i].obgyn); 
  }
  if (app_patientOBGYN.length == 0) {
    return;
  }
  app_patientOBGYNIndex = 0;
  loadOBGYNScreenForm();
  $('#patient-obgyn-print').click(function() { printPatientForm('print_patient_obgyn', 'OBGYN', app_patientOBGYN[app_patientOBGYNIndex])});
}


function changeOBGYNScreen(adjustment) {
  if ((app_patientOBGYNIndex == 0 && adjustment == -1) || (app_patientOBGYNIndex == app_patientOBGYN.length-1 && adjustment == 1)) {
    return;
  }
  app_patientOBGYNIndex += adjustment;
  loadOBGYNScreenForm();
}

function loadOBGYNScreenForm() {
  var date = dateFormat(app_patientOBGYN[app_patientOBGYNIndex].date, 'mm/dd/yyyy')
  $('#patient-obgyn-date').html(date);
  $('#obgyn-g').html(app_patientOBGYN[app_patientOBGYNIndex].g);
  $('#obgyn-p').html(app_patientOBGYN[app_patientOBGYNIndex].p);
  $('#obgyn-t').html(app_patientOBGYN[app_patientOBGYNIndex].t);
  $('#obgyn-a').html(app_patientOBGYN[app_patientOBGYNIndex].a);
  $('#obgyn-l').html(app_patientOBGYN[app_patientOBGYNIndex].l);
  $('input[name=pregnant][value='+app_patientOBGYN[app_patientOBGYNIndex].pregStatus+']').attr("checked", true);
  $('input[name=breastfeeding][value='+app_patientOBGYN[app_patientOBGYNIndex].breastfeeding+']').attr("checked", true);
  $('#breastfeeding-months').html(app_patientOBGYN[app_patientOBGYNIndex].breastfeedingMonths);
  $('#last-period').html(app_patientOBGYN[app_patientOBGYNIndex].lastPeriod);
  $('#age-first-period').html(app_patientOBGYN[app_patientOBGYNIndex].ageFirstPeriod);
  $('input[name=pap-smear][value='+app_patientOBGYN[app_patientOBGYNIndex].papSmearStatus+']').attr("checked", true);
  $('input[name=birth-control][value='+app_patientOBGYN[app_patientOBGYNIndex].birthControlStatus+']').attr("checked", true);
  $('#birth-control-type').html(app_patientOBGYN[app_patientOBGYNIndex].birthControlType);
  util_selectCheckboxesFromList(app_patientOBGYN[app_patientOBGYNIndex].obgynHistory, 'obgyn-hist');
  $('#obgyn-hist-other').html(app_patientOBGYN[app_patientOBGYNIndex].obgynHistoryOther);
}



function getPatientEncountersListing() {
  var jsonData = JSON.stringify({ patientId: app_currentPatientId, sessionId: clinician.sessionId });
  $.post("patient/getPatientEncounters", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    var encounters = parsedData.patientEncounters;
    RenderUtil.render('component/simple_data_table', 
     {items:encounters, 
      title:'Encounter History', 
      tableName:'chart-encounters-list', 
      clickable:true, 
      columns:[
        {title:'Date', field:'date', type:'date'},
        {title:'Clinician', field:'clinician.firstName', type:'double-person'},
        {title:'Completed', field:'completed', type:'simple'},
        {title:'Notes', field:'notes', type:'simple'}
      ]}, function(s) {
      $('#chart-encounters-list').html(s);
      $('#chart-encounters-list-title').html("Encounter History");
      $('#encounter-view-button').click(function(){ viewPatientEncounter(app_currentEncounterId); });
      $('.clickable-table-row').click( function(e){ 
        $(this).addClass('table-row-highlight').siblings().removeClass('table-row-highlight');
        handleClickableRow(e); 
      });
    });
  });
}


function getPatientEncounters() {
  var jsonData = JSON.stringify({ patientId: app_currentPatientId, sessionId: clinician.sessionId });
  $.post("patient/getPatientEncounters", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    app_patientEncounters = parsedData.patientEncounters;
  });
}


function viewPatientEncounter(encounterId) {
  var jsonData = JSON.stringify({ encounterId: encounterId, sessionId: clinician.sessionId });
  $.post("patient/getEncounter", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    app_currentEncounter = parsedData.encounter;
    var args = {isActive:true, id:app_currentEncounter.id};
    RenderUtil.render('encounter', args, function(s) { 
      $('#modals-placement').html(s);
      $('#modal-encounter').modal('show'); 
      $('#app-encounter-close-record').css({display: (app_currentEncounter.completed ? "none" : "inline-block")}); 
      setupCloseRecordButton();
      renderPatientEncounterForm(app_currentEncounter, true); 
      $('#app-encounter-print-all').click(function(){
        var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
        RenderUtil.render('print/print_encounter_all',  {encounter:app_currentEncounter, currentDate:currentDate}, function(obj) {
          var s = obj[0].outerHTML;
          print_openPrintWindow('print.html', s, 'ENCOUNTER FORM');
        });
      });
    });
  });
}


function getCurrentEncounterFromEncounters() {
  var jsonData = JSON.stringify({ sessionId: clinician.sessionId, patientId: app_currentPatientId});
  $.post("patient/getCurrentPatientEncounter", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    var oldEncounter = parsedData.encounter;
    return oldEncounter;
  });
}


function newEncounterFormDialog() {
  var jsonData = JSON.stringify({ sessionId: clinician.sessionId, patientId: app_currentPatientId});
  $.post("patient/getCurrentPatientEncounter", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    app_oldEncounter = parsedData.encounter;
	
    if (app_oldEncounter == undefined || app_oldEncounter.completed == true) {
      getPatientEncountersListing();
      newEncounterForm();
      return;
    }	  
    
    var args = {
      modalTitle:"Complete Encounter Confirmation", 
      modalH3:"Ready To Complete The Currently Open Encounter?", 
      modalH4:"In order to start a new encounter the current one needs to be completed.",
      cancelButton: 'Cancel',
      okButton: 'Confirm'
    };
    RenderUtil.render('dialog/confirm', args, function(s) { 
      $('#modals-placement').append(s);
      $('#modal-confirm').modal('show'); 
      $('#app-modal-confirmation-btn').click(function(){  
        var jsonData = JSON.stringify({ sessionId: clinician.sessionId, encounterId: app_oldEncounter.id});
        $.post("patient/closeEncounter", {data:jsonData}, function(data) {
          var parsedData = $.parseJSON(data);
          displayNotification('Patient Encounter Record Completed');
          app_oldEncounter.completed = true;
          $('#modal-encounter').modal('hide'); 
          getPatientEncountersListing();
          newEncounterForm();
        });
      });
    });
  });
}



function newEncounterForm() {
  var jsonData = JSON.stringify({ patientId: app_currentPatientId, sessionId: clinician.sessionId });
  $.post("patient/newEncounter", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    app_currentEncounter = parsedData.encounter;
    var args = {isActive:true, id:app_currentEncounter.id};
    RenderUtil.render('encounter', args, function(s) { 
      $('#modals-placement').html(s);
      $('#modal-encounter').modal('show'); 
      $('#app-encounter-close-record').css({display: "inline-block"});
      setupCloseRecordButton();
      renderPatientEncounterForm(app_currentEncounter, true); 
      $('#app-encounter-print-all').click(function(){
        var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
        RenderUtil.render('print/print_encounter_all',  {encounter:app_currentEncounter, currentDate:currentDate}, function(obj) {
          var s = obj[0].outerHTML;
          print_openPrintWindow('print.html', s, 'ENCOUNTER FORM');
        });
      });
    });
  });
}


function setupCloseRecordButton() {
$('#app-encounter-close-record').click(function() { 
  var args = {
    modalTitle:"Complete Encounter Confirmation", 
    modalH3:"Ready To Complete The Encounter?", 
    modalH4:"Once completed, the encounter is locked.",
    cancelButton: 'Cancel',
    okButton: 'Confirm'
  };
  RenderUtil.render('dialog/confirm', args, function(s) { 
    $('#modals-placement').append(s);
    $('#modal-confirm').modal('show'); 
    $('#app-modal-confirmation-btn').click(function(){  
      var jsonData = JSON.stringify({ sessionId: clinician.sessionId, encounterId: app_currentEncounter.id});
      $.post("patient/closeEncounter", {data:jsonData}, function(data) {
        var parsedData = $.parseJSON(data);
        displayNotification('Patient Encounter Record Completed');
        app_currentEncounter.completed = true;
        $('#modal-encounter').modal('hide'); 
        app_oldEncounter.completed = true;
        getPatientEncountersListing();
      });
    });
  });
});
}