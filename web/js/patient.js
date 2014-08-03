/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */


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
  $('#soap-notes-print').click(function() { printPatientForm('print_soap_note', 'SOAP NOTE', soapNote)});
}



function printPatientForm(template, title, object) { 
  var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
  RenderUtil.render('print/'+template,  {object:object, currentDate:currentDate}, function(obj) {
    var s = obj[0].outerHTML;
    print_openPrintWindow('print.html', s, title);
  });
}


function loadHistScreenForm() {
  app_currentEncounter = app_patientEncounters[0];
  var id = app_patientEncounters[0].id; 
  object = app_patientEncounters[0].patient.hist; 
  $('#modal-medical-history .form-control-unsaved').css({display: "none"});
      RenderUtil.render('component/encounter_medications', {encounter: app_currentEncounter}, function(s) { 
        $("#patient-medications-").html(s); 
        $('.patient-med-editable').blur(function(e) { 
          getCurrentMedicationId(e);
          updateEncounterMedication("medication", $(this).html(), app_currentMedicationId); 
        });
        $('.patient-dose-editable').blur(function(e) { 
          getCurrentMedicationId(e);
          updateEncounterMedication("dose", $(this).html(), app_currentMedicationId); 
        });
        $('.patient-freq-editable').blur(function(e) { 
          getCurrentMedicationId(e);
          updateEncounterMedication("frequency", $(this).html(), app_currentMedicationId); 
        });
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
        var jsonData = JSON.stringify({sessionId: clinician.sessionId, patientId:  app_currentEncounter.patient.id});
        $.post("patient/addEncounterMedication", {data:jsonData}, function(data) {
          var parsedData = $.parseJSON(data);
          var encounterMedicationId = parsedData.encounterMedicationId;
          var numMedications = $("#patient-medications").children().length + 2;
          RenderUtil.render('component/encounter_medication', {ordinal:numMedications, id: encounterMedicationId}, function(s) { 
            $("#patient-medications").append(s); 
            setEncounterFormMode(id, section, savedState, hasOwnership);
            $('.patient-med-editable').blur(function(e) { 
              getCurrentMedicationId(e);
              updateEncounterMedication("medication", $(this).html(), encounterMedicationId); 
            });
            $('.patient-dose-editable').blur(function(e) { 
              getCurrentQuestionId(e);
              updateEncounterMedication("dose", $(this).html(), encounterMedicationId); 
             });
             $('.patient-freq-editable').blur(function(e) { 
              getCurrentQuestionId(e);
              updateEncounterMedication("frequency", $(this).html(), encounterMedicationId); 
             });
           });
       });
      });
      $('#patient-past-s-m-saved').blur(function() { updateSavedPatientEncounter("pastSM", $(this).html(), id); });
      $('input[name=patient-fam-hist-'+']').click(function() { 
        var famHist = $('input[name=patient-fam-hist-'+']:checked').map(function() {return this.value;}).get().join(',');
        updateSavedPatientEncounter("famHist", famHist, id); 
      });
      $('#patient-fam-hist-notes-saved').blur(function() { updateSavedPatientEncounter("famHistNotes", $(this).html(), id); });
      $('#patient-fam-hist-other-saved').blur(function() { updateSavedPatientEncounter("famHistOther", $(this).html(), id); });
      $('#patient-allerg-food-saved').blur(function() { updateSavedPatientEncounter("allergFood", $(this).html(), id); });
      $('#patient-allerg-drug-saved').blur(function() { updateSavedPatientEncounter("allergDrug", $(this).html(), id); });
      $('#patient-allerg-env-saved').blur(function() { updateSavedPatientEncounter("allergEnv", $(this).html(), id); });
      $('input[name=patient-vacc]').click(function() { updateSavedPatientEncounter("vacc", $(this).val() == 'true', id); });
      $('#patient-vacc-notes-saved').blur(function() { updateSavedPatientEncounter("vaccNotes", $(this).html(), id); });
      $('input[name=patient-subst]').click(function() { 
        var subst = $('input[name=patient-subst]:checked').map(function() {return this.value;}).get().join(',');
        updateSavedPatientEncounter("subst", subst, id); 
      });
      $('#patient-smoke-pks-day-saved').blur(function() { updateSavedPatientEncounter("smokePksDay", $(this).html(), id); });
      $('#patient-years-smoked-saved').blur(function() { updateSavedPatientEncounter("yearsSmoked", $(this).html(), id); });
      $('#patient-smoke-years-quit-saved').blur(function() { updateSavedPatientEncounter("smokeYearsQuit", $(this).html(), id); });
      $('#patient-etoh-units-week-saved').blur(function() { updateSavedPatientEncounter("etohUnitsWeek", $(this).html(), id); });
      $('#patient-current-drugs-saved').blur(function() { updateSavedPatientEncounter("currentDrugs", $(this).html(), id); });
  $('#patient-hist-print').click(function() { printPatientForm('print_patient_hist', 'SOCIAL & FAMILY HISTORY', object)});
}


function loadPFSHScreenForm() {
  app_currentEncounter = app_patientEncounters[0];
  var id = app_patientEncounters[0].id; 
  object = app_patientEncounters[0].patient.pfsh; 
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
  $('#patient-num-residents-saved').blur(function() { updateSavedPatientEncounter("numResidents", $(this).html(), id); });
  $('#patient-job-type-saved').blur(function() { updateSavedPatientEncounter("jobType", $(this).html(), id); });
  $('input[name=patient-mother-alive]').click(function() { updateSavedPatientEncounter("motherAlive", $(this).val() == 'true', id); });
  $('#patient-mother-death-reason-saved').blur(function() { updateSavedPatientEncounter("motherDeathReason", $(this).html(), id); });
  $('input[name=patient-father-alive]').click(function() { updateSavedPatientEncounter("fatherAlive", $(this).val() == 'true', id); });
  $('#patient-father-death-reason-saved').blur(function() { updateSavedPatientEncounter("fatherDeathReason", $(this).html(), id); });
  $('input[name=patient-partner-alive]').click(function() { updateSavedPatientEncounter("partnerAlive", $(this).val() == 'true', id); });
  $('#patient-partner-death-reason-saved').blur(function() { updateSavedPatientEncounter("partnerDeathReason", $(this).html(), id); });
  $('#patient-num-siblings-saved').blur(function() { updateSavedPatientEncounter("numSiblings", $(this).html(), id); });
  $('#patient-num-brothers-saved').blur(function() { updateSavedPatientEncounter("numBrothers", $(this).html(), id); });
  $('#patient-num-sisters-saved').blur(function() { updateSavedPatientEncounter("numSisters", $(this).html(), id); });
  $('#patient-num-children-saved').blur(function() { updateSavedPatientEncounter("numChildren", $(this).html(), id); });
  $('#patient-num-sons-saved').blur(function() { updateSavedPatientEncounter("numSons", $(this).html(), id); });
  $('#patient-num-daughters-saved').blur(function() { updateSavedPatientEncounter("numDaughters", $(this).html(), id); });
  $('#patient-pfsh-print').click(function() { printPatientForm('print_patient_pfsh', 'SOCIAL & FAMILY HISTORY', object)});
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


function loadCurrentChiefComplaintScreen() {
  app_patientCC = []; 	
  for (i=0;i<app_patientEncounters.length;i++) {
    app_patientCC.push(app_patientEncounters[i].cc); 
  }
  if (app_patientCC.length == 0) {
    return;
  }
  app_patientCCIndex = 0;
  loadChiefComplaintScreenForm();
  $('#patient-cc-print').click(function() { printPatientForm('print_patient_cc', 'CHIEF COMPLAINT', app_patientCC[app_patientCCIndex])});
}


function changeChiefComplaintScreen(adjustment) {
  if ((app_patientCCIndex == 0 && adjustment == -1) || (app_patientCCIndex == app_patientCC.length-1 && adjustment == 1)) {
    return;
  }
  app_patientCCIndex += adjustment;
  loadChiefComplaintScreenForm();
}

function loadChiefComplaintScreenForm() {
  var date = dateFormat(app_patientCC[app_patientCCIndex].date, 'mm/dd/yyyy')
  $('#patient-cc-date').html(date);
  $('#pain-scale-value').html(app_patientCC[app_patientCCIndex].painScale);
  $('#chief-complaint').html(app_patientCC[app_patientCCIndex].description);
  $('#specific-location').html(app_patientCC[app_patientCCIndex].specificLocation);
  util_selectCheckboxesFromList(app_patientCC[app_patientCCIndex].occursWhen, 'occurs-when');
  $('#hours-since').html(app_patientCC[app_patientCCIndex].hoursSince);
  $('#days-since').html(app_patientCC[app_patientCCIndex].daysSince);
  $('#weeks-since').html(app_patientCC[app_patientCCIndex].weeksSince);
  $('#months-since').html(app_patientCC[app_patientCCIndex].monthsSince);
  $('#years-since').html(app_patientCC[app_patientCCIndex].yearsSince);
  $('#how-long-other').html(app_patientCC[app_patientCCIndex].howLongOther);
  $('#pain-scale').html(app_patientCC[app_patientCCIndex].painScale);
  $('#pain-type').html(app_patientCC[app_patientCCIndex].painType);
  $('#pain-x-hour').html(app_patientCC[app_patientCCIndex].painXHour);
  $('#pain-x-day').html(app_patientCC[app_patientCCIndex].painXDay);
  $('#pain-x-week').html(app_patientCC[app_patientCCIndex].painXWeek);
  $('#pain-x-month').html(app_patientCC[app_patientCCIndex].painXMonth);
  $('#pain-duration').html(app_patientCC[app_patientCCIndex].painDuration);
  util_selectCheckboxesFromList(app_patientCC[app_patientCCIndex].denies, 'denies');
  $('#denies-other').html(app_patientCC[app_patientCCIndex].deniesOther)
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
    $('#app-modal-confirmation-btn').click(function(){  
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
  $('#provider-name').html(app_patientExam[app_patientExamIndex].providerName);
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