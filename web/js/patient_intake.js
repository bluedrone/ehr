var canvas; 
var ctx; 
var flag = false;
var prevX = 0;
var currX = 0;
var prevY = 0;
var currY = 0;
var dot_flag = false;
var strokeColor = "black";
var strokeWidth = 2;
var uploader;


function setIntakeFormModes(hasOwnership) {
  setIntakeFormMode(app_currentEncounter.id, 'basic-info', app_currentEncounter.basicInfoSaved, hasOwnership);
  setIntakeFormMode(app_currentEncounter.id, 'vitals', app_currentEncounter.vitalsSaved, hasOwnership);
  setIntakeFormMode(app_currentEncounter.id, 'family', app_currentEncounter.familySaved, hasOwnership);
  setIntakeFormMode(app_currentEncounter.id, 'cc', app_currentEncounter.ccSaved, hasOwnership);
  setIntakeFormMode(app_currentEncounter.id, 'obgyn', app_currentEncounter.obgynSaved, hasOwnership);
  setIntakeFormMode(app_currentEncounter.id, 'pfsh', app_currentEncounter.pfshSaved, hasOwnership);
  setIntakeFormMode(app_currentEncounter.id, 'supp', app_currentEncounter.suppSaved, hasOwnership);
  setIntakeFormMode(app_currentEncounter.id, 'hist', app_currentEncounter.histSaved, hasOwnership);
  setIntakeFormMode(app_currentEncounter.id, 'exam', app_currentEncounter.examSaved, hasOwnership);
  setIntakeFormMode(app_currentEncounter.id, 'follow-up', app_currentEncounter.followUpSaved, hasOwnership);
}


function updateLockStatusIcon(lockStatus) {
  var id = app_currentEncounter.id;
  var oldLockStatusIcon = app_lockIcons[app_oldLockStatus];
  var lockStatusIcon = app_lockIcons[lockStatus];
  $("#tab-header-intake-icon-"+id).removeClass(oldLockStatusIcon);
  $("#tab-header-intake-icon-"+id).addClass(lockStatusIcon);
}


$('#app-intake-override-record').click(function() { 
  RenderUtil.render('override_patient_intake', {}, function(s) { 
    $('#modals-placement').html(s);
    $('#modal-override-patient-intake').modal('show'); 
    $('#app-overrride-patient-intake-submit').click(function(){ 
      app_oldLockStatus = app_currentEncounter.lockStatus;
      var jsonData = JSON.stringify({ 
        sessionId: clinician.sessionId, 
        encounterId: app_currentEncounter.id
      });
      $.post("patient/overridePatient", {data:jsonData}, function(data) {
        var parsedData = $.parseJSON(data);
        var clinicianId = parsedData.clinicianId;
        var lockStatus = parsedData.lockStatus;
        $('#modal-override-patient-intake').modal('hide'); 
        
        if (lockStatus == INTAKE_OVERRIDDEN) {
          updateLockStatusIcon(lockStatus);
          updateIntakeButtons(lockStatus, clinicianId);
          displayNotification('Patient Intake Record Overridden');
          setIntakeFormModes(true);
        }
        else {
          displayNotification('Unable to Override Patient Intake Record');
        }
      });
    });
  });
});


$('#app-intake-close-record').click(function() { 
  RenderUtil.render('dialog/close_encounter', {}, function(s) { 
    $('#modals-placement').html(s);
    $('#modal-close-encounter').modal('show'); 
    $('#app-intake-close-record-confirmation').click(function(){  
      var jsonData = JSON.stringify({ sessionId: clinician.sessionId, encounterId: app_currentEncounter.id});
      $.post("patient/closeEncounter", {data:jsonData}, function(data) {
        var parsedData = $.parseJSON(data);
        displayNotification('Patient Intake Record Closed');
        var jsonData = JSON.stringify({ sessionId: clinician.sessionId });
        $.post("patient/getPatientIntakeGroups", {data:jsonData}, function(data) {
          var parsedData = $.parseJSON(data);
          app_patientIntakeGroups = parsedData.patientIntakeGroups;
          buildGroupOrderArray();
          $("#app-intake-group-tabs").html('');
          viewPatientIntakeFormGroupWithId();
        });
      });
    });
  });
});


$('#app-intake-release-record').click(function() { 
  var lockStatus = app_currentEncounter.lockStatus;
  if (lockStatus == INTAKE_LOCKED && app_currentEncounter.clinician.id == clinician.id) {
    lockStatus = INTAKE_OWNED;
  } 
  app_oldLockStatus = lockStatus;
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounterId: app_currentEncounter.id
  });
  $.post("patient/releasePatient", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    var clinicianId = parsedData.clinicianId;
    if (parsedData.lockStatus == INTAKE_FREE) {
      updateLockStatusIcon(parsedData.lockStatus);
      updateIntakeButtons(parsedData.lockStatus, clinicianId);
      displayNotification('Patient Intake Record Released');
      setIntakeFormModes(false);
    }
    else {
      displayNotification('Unable to Release Patient Intake Record');
    }
  });
});

$('#app-intake-acquire-record').click(function() { 
  app_oldLockStatus = app_currentEncounter.lockStatus;
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounterId: app_currentEncounter.id
  });
  $.post("patient/acquirePatient", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    var clinicianId = parsedData.clinicianId;
    if (parsedData.lockStatus == INTAKE_LOCKED || parsedData.lockStatus == INTAKE_OVERRIDDEN) {
      var lockStatus = parsedData.lockStatus;
      if (parsedData.lockStatus == INTAKE_LOCKED && clinician.id == clinicianId) {
        lockStatus = INTAKE_OWNED;
      } 
      updateLockStatusIcon(lockStatus);
      updateIntakeButtons(lockStatus, clinicianId);
      displayNotification('Patient Intake Record Acquired');
      setIntakeFormModes(true);
    }
    else {
      displayNotification('Unable to Acquire Patient Intake Record');
    }
  });
});


function viewPatientIntakeFormGroup(e) {
  getCurrentGroupId(e);
  viewPatientIntakeFormGroupWithId();
}
   
function viewPatientIntakeFormGroupWithId() {
  RenderUtil.render('patient_intake_groups', {}, function(s) { 
    $('#patient-intake-screen').html(s);
    app_viewStack('patient-intake-screen', DO_SCROLL);
    $('#section-notification').css("visibility", "visible");
    $('.patient-navbar-btn').css("display", "none");
    $('.check-in-navbar-btn').css("display", "none");
    $('.intake-navbar-btn').css("display", "inline-block");
    
    var group = getPatientIntakeGroup(app_currentGroupId);
    app_currentGroup = group; 
    
    for (i=0;i<group.encounterList.length;i++) {
      var encounter = group.encounterList[i];
      if (i == 0) { app_currentEncounter = encounter; }
      var fullName = util_buildFullName(encounter.patient.cred.firstName, encounter.patient.cred.LastName, encounter.patient.cred.additionalName);
      addPatientIntakeTabContent(encounter, i==0, fullName.trunc(30, false), encounter.lockStatus, i==0);
    }
  });
} 


function addPatientIntakeTabContent(encounter, isActive, patientFullName, lockStatus, isFirst) {
  if (lockStatus == INTAKE_LOCKED && encounter.clinician.id == clinician.id) {
    lockStatus = INTAKE_OWNED;
  } 
  if (isFirst == true) { 
    app_currentEncounter = encounter;
    setIntakeButtons();
  }
  addPatientIntakeTab(encounter,isActive, patientFullName, lockStatus);
  var args = {isActive:isActive, id:encounter.id, isFirst:isFirst};
  RenderUtil.render('patient_intake', args, function(s) { 
    $("#app-intake-group-tabs-content").append(s);
    var hasOwnership = lockStatus == INTAKE_OWNED || (lockStatus == INTAKE_OVERRIDDEN && encounter.clinician.id == clinician.id);
    renderPatientIntakeForm(encounter, hasOwnership); 
  });
}


function addPatientIntakeTab(encounter, isActive, patientFullName, lockStatus) {
  if (lockStatus == INTAKE_LOCKED && app_currentEncounter.clinician != undefined && app_currentEncounter.clinician.id == clinician.id) {
    lockStatus = INTAKE_OWNED;
  } 
  var lockStatusIcon = app_lockIcons[lockStatus];
  var args = {isActive:isActive, id:encounter.id, patientFullName:patientFullName, lockStatus:lockStatus, lockStatusIcon:lockStatusIcon};
  RenderUtil.render('component/intake_group_tab', args, function(s) { 
    $("#app-intake-group-tabs").append(s);
    $('.tab-header-intake').on('show.bs.tab', function(){ onIntakeTabSelect(this); });
  });
}  


function onIntakeTabSelect(that) {
  var encounterId = $(that).attr('data-id'); 
  for (i=0;i<app_currentGroup.encounterList.length;i++) {
    var encounter = app_currentGroup.encounterList[i];
    if (encounter.id == encounterId) {
      app_currentEncounter = encounter;
      console.log('tab id: ' + encounterId + ', encounter.id: ' + encounter.id); 
      setIntakeButtons();
      break;
    }
  }
}

function updateIntakeButtons(lockStatus, clinicianId) {
  if (lockStatus == INTAKE_FREE) {
    $('#app-intake-acquire-record').css("display", "inline-block");
    $('#app-intake-release-record').css("display", "none");
    $('#app-intake-override-record').css("display", "none");
    $('#app-intake-close-record').css("display", "none");
  }
  else if (lockStatus != INTAKE_FREE && clinicianId == clinician.id) {
    $('#app-intake-acquire-record').css("display", "none");
    $('#app-intake-release-record').css("display", "inline-block");
    $('#app-intake-override-record').css("display", "none");
    $('#app-intake-close-record').css("display", "inline-block");
  }
  else if (lockStatus != INTAKE_FREE && clinicianId != clinician.id) {
    $('#app-intake-acquire-record').css("display", "none");
    $('#app-intake-release-record').css("display", "none");
    $('#app-intake-override-record').css("display", "inline-block");
    $('#app-intake-close-record').css("display", "none");
  }
}

function setIntakeButtons() {
  if (app_currentEncounter.clinician == undefined) {
    $('#app-intake-acquire-record').css("display", "inline-block");
    $('#app-intake-release-record').css("display", "none");
    $('#app-intake-override-record').css("display", "none");
    $('#app-intake-close-record').css("display", "none");
  }
  else if (app_currentEncounter.clinician != undefined && app_currentEncounter.clinician.id == clinician.id) {
    $('#app-intake-acquire-record').css("display", "none");
    $('#app-intake-release-record').css("display", "inline-block");
    $('#app-intake-override-record').css("display", "none");
    $('#app-intake-close-record').css("display", "inline-block");
  }
  else if (app_currentEncounter.clinician != undefined && app_currentEncounter.clinician.id != clinician.id) {
    $('#app-intake-acquire-record').css("display", "none");
    $('#app-intake-release-record').css("display", "none");
    $('#app-intake-override-record').css("display", "inline-block");
    $('#app-intake-close-record').css("display", "none");
  }
}


function printIntakeForm(template, title) { 
  var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
  RenderUtil.render('print/'+template,  {encounter:app_currentEncounter, currentDate:currentDate}, function(obj) {
    var s = obj[0].outerHTML;
    print_openPrintWindow('print.html', s, title);
  });
}

function renderPatientIntakeForm(encounter, hasOwnership) {
  var id = encounter.id;
  var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
  
  renderIntakeFormSection (encounter, 'basic-info', encounter.basicInfoSaved, hasOwnership);
  renderIntakeFormSection (encounter, 'vitals', encounter.vitalsSaved, hasOwnership);
  renderIntakeFormSection (encounter, 'family', encounter.familySaved, hasOwnership);
  renderIntakeFormSection (encounter, 'cc', encounter.ccSaved, hasOwnership);
  renderIntakeFormSection (encounter, 'obgyn', encounter.obgynSaved, hasOwnership);
  renderIntakeFormSection (encounter, 'pfsh', encounter.pfshSaved, hasOwnership);
  renderIntakeFormSection (encounter, 'supp', encounter.suppSaved, hasOwnership);
  renderIntakeFormSection (encounter, 'hist', encounter.histSaved, hasOwnership);
  renderIntakeFormSection (encounter, 'exam', encounter.examSaved, hasOwnership);
  renderIntakeFormSection (encounter, 'follow-up', encounter.followUpSaved, hasOwnership);
  
  $(".edit-on-select").focus(function() { $(this).selectContentEditableText(); });
  
  $('#intake-basic-info-save-'+id).click(function() { saveBasicInfoIntakeForm(encounter); });
  $('#intake-vitals-save-'+id).click(function() { saveVitalsIntakeForm(encounter); });
  $('#intake-family-save-'+id).click(function() { saveFamilyIntakeForm(encounter); });
  $('#intake-cc-save-'+id).click(function() { saveCCIntakeForm(encounter); });
  $('#intake-obgyn-save-'+id).click(function() { saveOBGYNIntakeForm(encounter); });
  $('#intake-pfsh-save-'+id).click(function() { savePFSHIntakeForm(encounter); });
  $('#intake-supp-save-'+id).click(function() { saveSuppIntakeForm(encounter); });
  $('#intake-hist-save-'+id).click(function() { saveHistIntakeForm(encounter); });
  $('#intake-exam-save-'+id).click(function() { saveExamIntakeForm(encounter); });
  $('#intake-follow-up-save-'+id).click(function() { saveFollowUpIntakeForm(encounter); });
  setupPictureUpload(encounter.id, encounter.patient.id);
  $('#intake-basic-info-print-'+id).click(function() { printIntakeForm('print_intake_basic_info', 'PATIENT INTAKE')});
  $('#intake-vitals-print-'+id).click(function() { printIntakeForm('print_intake_vitals', 'VITALS')});
  $('#intake-family-print-'+id).click(function() { printIntakeForm('print_intake_family', 'FAMILY')});
  $('#intake-cc-print-'+id).click(function() { printIntakeForm('print_intake_cc', 'CHIEF COMPLAINT')});
  $('#intake-obgyn-print-'+id).click(function() { printIntakeForm('print_intake_obgyn', 'OBGYN')});
  $('#intake-pfsh-print-'+id).click(function() { printIntakeForm('print_intake_pfsh', 'SOCIAL & FAMILY HISTORY')});
  $('#intake-supp-print-'+id).click(function() { printIntakeForm('print_intake_supp', 'SUPPLEMENTAL QUESTIONS')});
  $('#intake-hist-print-'+id).click(function() { printIntakeForm('print_intake_hist', 'MEDICAL HISTORY')});
  $('#intake-exam-print-'+id).click(function() { printIntakeForm('print_intake_exam', 'EXAM')});
  $('#intake-follow-up-print-'+id).click(function() { printIntakeForm('print_intake_follow-up', 'FOLLOW UP')});
} 

function renderIntakeFormSection (encounter, section, savedState, hasOwnership) {
  var id = encounter.id;
  var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
  var notEditable = true; 
  setIntakeFormMode(id, section, savedState, hasOwnership);
  
  if (savedState == false) {

    if (section == 'basic-info') {
      $("#intake-basic-info-photo-"+id).attr("src","files/patients/"+encounter.patient.id+"/"+encounter.patient.demo.profileImagePath);
      $('#intake-nombre-'+id).val(encounter.patient.cred.firstName);
      $('#intake-apellido-'+id).val(encounter.patient.cred.lasstName);
      $('#intake-apellido-segundo-'+id).val(encounter.patient.cred.additionalName);
      $('#intake-todays-date-'+id).val(currentDate);
      $('#intake-dob-'+id).mask("99/99/9999");
      $("#intake-gender-"+id).keydown(function(e) { util_filterGenderInput(e); });
      $("#intake-age-years-"+id+", #intake-age-months-"+id).keydown(function(e) { util_filterAgeInput(e); });
    }
    else if (section == 'family') {
      $('#intake-mother-dob-'+id).mask("99/99/9999");
    }
    else if (section == 'vitals') {
      $("#intake-height-"+id+", #intake-weight-"+id+", #intake-temp-"+id+", #intake-arm-"+id).keydown(function(e) { util_filterDecimalInput(e); });
    }
    else if (section == 'cc') {
      $("#intake-hours-since-"+id+", #intake-days-since-"+id+", #intake-weeks-since-"+id+", #intake-months-since-"+id+", #intake-years-since-"+id).keydown(function(e) { util_filterDecimalInput(e); });
      $("#intake-pain-x-hour-"+id+", #intake-pain-x-day-"+id+", #intake-pain-x-week-"+id+", #intake-pain-x-month-"+id).keydown(function(e) { util_filterDecimalInput(e); });
      $('#intake-pain-scale-'+id).slider({value:encounter.cc.painScale}).on('slide', function(ev){
        $('#intake-pain-scale-value-'+id).html(this.value)
        updateSavedPatientIntake("painScale", this.value, id);
      });
      $('#intake-pain-scale-value-'+id).html(encounter.cc.painScale);
    }
    else if (section == 'obgyn') {
    }
    else if (section == 'pfsh') {
    }
    else if (section == 'supp') {
      RenderUtil.render('component/intake_questions', {encounter:encounter}, function(s) { $("#intake-questions-"+id).html(s); setIntakeFormMode(id, section, savedState, hasOwnership);});
      $('#intake-supp-new-question-'+id).click(function() { 
       var jsonData = JSON.stringify({sessionId: clinician.sessionId, encounterId: id});
        $.post("patient/addIntakeQuestion", {data:jsonData}, function(data) {
        var parsedData = $.parseJSON(data);
          var numQuestions = $("#intake-questions-"+id).children().length + 2;
          RenderUtil.render('component/intake_question', {ordinal:numQuestions, id: parsedData.intakeQuestionId}, function(s) { $("#intake-questions-"+id).append(s); setIntakeFormMode(id, section, savedState, hasOwnership);});
        });
      });
    }
    else if (section == 'hist') {
      RenderUtil.render('component/intake_medications', {encounter:encounter}, function(s) { $("#intake-medications-"+id).html(s); setIntakeFormMode(id, section, savedState, hasOwnership);});
      $('#intake-hist-new-medication-'+id).click(function() { 
       var jsonData = JSON.stringify({sessionId: clinician.sessionId, patientId: encounter.patient.id});
        $.post("patient/addIntakeMedication", {data:jsonData}, function(data) {
        var parsedData = $.parseJSON(data);
          var numMedications = $("#intake-medications-"+id).children().length + 2;
          RenderUtil.render('component/intake_medication', {ordinal:numMedications, id: parsedData.intakeMedicationId}, function(s) { $("#intake-medications-"+id).append(s); setIntakeFormMode(id, section, savedState, hasOwnership);});
        });
      });
    }
    else if (section == 'exam') {
    /*
      var canvas = document.getElementById('intake-exam-image-'+id);
      var context = canvas.getContext('2d');
      var baseImage = new Image();
      baseImage.src = 'images/exam.png';
      baseImage.onload = function() {
        context.drawImage(baseImage, 0, 0, 100, 50);
      }
    */
     initIntakeExamCanvas(id);
    }
    else if (section == 'follow-up') {
      $('#intake-follow-up-date-'+id).mask("99/99/9999");
    }

  }
  else if (savedState == true) {

    if (section == 'basic-info') {
      $("#intake-basic-info-photo-"+id).attr("src","files/patients/"+encounter.patient.id+"/"+encounter.patient.demo.profileImagePath);
      $('#intake-nombre-'+id).val(encounter.patient.cred.firstName);
      $('#intake-apellido-'+id).val(encounter.patient.cred.lasstName);
      $('#intake-apellido-segundo-'+id).val(encounter.patient.cred.additionalName);
      $('#intake-todays-date-saved-'+id).html(currentDate);
      $('#intake-consult-location-saved-'+id).html(encounter.consultLocation);
      $('#intake-govt-id-saved-'+id).html(encounter.patient.cred.govtId);
      var dob = dateFormat(encounter.patient.demo.dob, 'mm/dd/yyyy')
      $('#intake-dob-saved-'+id).html(dob);
      $('#intake-gender-saved-'+id).html(encounter.patient.demo.gender.code);
      $('#intake-age-years-saved-'+id).html(encounter.ageInYears);
      $('#intake-age-months-saved-'+id).html(encounter.ageInMonths);
      $('#intake-phone-saved-'+id).html(encounter.patient.demo.primaryPhone);
      $('#intake-community-saved-'+id).html(encounter.community);
      $('#intake-notes-saved-'+id).html(encounter.notes);
      $('#intake-nombre-saved-'+id).blur(function() { updateSavedPatientIntake("firstName", $(this).html(), id); });
      $('#intake-apellido-saved-'+id).blur(function() { updateSavedPatientIntake("lastName", $(this).html(), id); });
      $('#intake-apellido-segundo-saved-'+id).blur(function() { updateSavedPatientIntake("additionalName", $(this).html(), id); });
      $('#intake-consult-location-saved-'+id).blur(function() { updateSavedPatientIntake("consultLocation", $(this).html(), id); });
      $('#intake-govt-id-saved-'+id).blur(function() { updateSavedPatientIntake("govtId", $(this).html(), id); });
      $('#intake-gender-saved-'+id).blur(function() { updateSavedPatientIntake("gender", $(this).html(), id); });
      $('#intake-phone-saved-'+id).blur(function() { updateSavedPatientIntake("phone", $(this).html(), id); });
      $('#intake-community-saved-'+id).blur(function() { updateSavedPatientIntake("community", $(this).html(), id); });
      $('#intake-notes-saved-'+id).blur(function() { updateSavedPatientIntake("notes", $(this).html(), id); });
      $('#intake-age-years-saved-'+id).blur(function() { updateSavedPatientIntake("ageInYears", $(this).html(), id); });
      $('#intake-age-months-saved-'+id).blur(function() { updateSavedPatientIntake("ageInMonths", $(this).html(), id); });
      $('#intake-dob-saved-'+id).blur(function() { updateSavedPatientIntake("dob", $(this).html(), id); });
    }
    else if (section == 'family') {
      $('#intake-mother-name-saved-'+id).html(encounter.patient.pfsh.motherName);
      var motherDob = dateFormat(encounter.patient.pfsh.motherDob, 'mm/dd/yyyy')
      $('#intake-mother-dob-saved-'+id).html(motherDob);
      $('#intake-caretaker-name-saved-'+id).html(encounter.patient.pfsh.caretakerName);
      $('#intake-patient-relationship-saved-'+id).html(encounter.patient.pfsh.patientRelationship);
      $('#intake-mother-name-saved-'+id).blur(function() { updateSavedPatientIntake("motherName", $(this).html(), id); });
      $('#intake-mother-dob-saved-'+id).blur(function() { updateSavedPatientIntake("motherDob", $(this).html(), id); });
      $('#intake-caretaker-name-saved-'+id).blur(function() { updateSavedPatientIntake("caretakerName", $(this).html(), id); });
      $('#intake-patient-relationship-saved-'+id).blur(function() { updateSavedPatientIntake("patientRelationship", $(this).html(), id); });
    }
    else if (section == 'vitals') {
      $('#intake-height-saved-'+id).html(encounter.vitals.height);
      $('#intake-weight-saved-'+id).html(encounter.vitals.weight);
      $('#intake-sys-saved-'+id).html(encounter.vitals.systolic);
      $('#intake-dia-saved-'+id).html(encounter.vitals.diastolic);
      $('#intake-hr-saved-'+id).html(encounter.vitals.pulse);
      $('#intake-rr-saved-'+id).html(encounter.vitals.respiration);
      $('#intake-temp-saved-'+id).html(encounter.vitals.temperature);
      $('#intake-arm-saved-'+id).html(encounter.vitals.arm);
      $('#intake-height-saved-'+id).blur(function() { updateSavedPatientIntake("height", $(this).html(), id); });
      $('#intake-weight-saved-'+id).blur(function() { updateSavedPatientIntake("weight", $(this).html(), id); });
      $('#intake-sys-saved-'+id).blur(function() { updateSavedPatientIntake("systolic", $(this).html(), id); });
      $('#intake-dia-saved-'+id).blur(function() { updateSavedPatientIntake("diastolic", $(this).html(), id); });
      $('#intake-hr-saved-'+id).blur(function() { updateSavedPatientIntake("pulse", $(this).html(), id); });
      $('#intake-rr-saved-'+id).blur(function() { updateSavedPatientIntake("respiration", $(this).html(), id); });
      $('#intake-temp-saved-'+id).blur(function() { updateSavedPatientIntake("temp", $(this).html(), id); });
      $('#intake-arm-saved-'+id).blur(function() { updateSavedPatientIntake("arm", $(this).html(), id); });
    }
    else if (section == 'cc') {
      $('#intake-pain-scale-'+id).slider({value:encounter.cc.painScale}).on('slide', function(ev){
        $('#intake-pain-scale-value-'+id).html(this.value)
        updateSavedPatientIntake("painScale", this.value, id);
      });
      $('#intake-pain-scale-value-'+id).html(encounter.cc.painScale);
      $('#intake-chief-complaint-saved-'+id).html(encounter.cc.description);
      $('#intake-specific-location-saved-'+id).html(encounter.cc.specificLocation);
      util_selectCheckboxesFromList(encounter.cc.occursWhen, 'intake-occurs-when-'+id);
      $('#intake-hours-since-saved-'+id).html(encounter.cc.hoursSince);
      $('#intake-days-since-saved-'+id).html(encounter.cc.daysSince);
      $('#intake-weeks-since-saved-'+id).html(encounter.cc.weeksSince);
      $('#intake-months-since-saved-'+id).html(encounter.cc.monthsSince);
      $('#intake-years-since-saved-'+id).html(encounter.cc.yearsSince);
      $('#intake-how-long-other-saved-'+id).html(encounter.cc.howLongOther);
      $('#intake-pain-scale-saved-'+id).html(encounter.cc.painScale);
      $('#intake-pain-type-saved-'+id).html(encounter.cc.painType);
      $('#intake-pain-x-hour-saved-'+id).html(encounter.cc.painXHour);
      $('#intake-pain-x-day-saved-'+id).html(encounter.cc.painXDay);
      $('#intake-pain-x-week-saved-'+id).html(encounter.cc.painXWeek);
      $('#intake-pain-x-month-saved-'+id).html(encounter.cc.painXMonth);
      $('#intake-pain-duration-saved-'+id).html(encounter.cc.painDuration);
      util_selectCheckboxesFromList(encounter.cc.denies, 'intake-denies-'+id);
      $('#intake-denies-other-saved-'+id).html(encounter.cc.deniesOther);
      $('#intake-chief-complaint-saved-'+id).blur(function() { updateSavedPatientIntake("ccDescription", $(this).html(), id); });
      $('#intake-specific-location-saved-'+id).blur(function() { updateSavedPatientIntake("specificLocation", $(this).html(), id); });
      $('#intake-hours-since-saved-'+id).blur(function() { updateSavedPatientIntake("hoursSince", $(this).html(), id); });
      $('#intake-days-since-saved-'+id).blur(function() { updateSavedPatientIntake("daysSince", $(this).html(), id); });
      $('#intake-weeks-since-saved-'+id).blur(function() { updateSavedPatientIntake("weeksSince", $(this).html(), id); });
      $('#intake-months-since-saved-'+id).blur(function() { updateSavedPatientIntake("monthsSince", $(this).html(), id); });
      $('#intake-years-since-saved-'+id).blur(function() { updateSavedPatientIntake("yearsSince", $(this).html(), id); });
      $('#intake-pain-type-saved-'+id).click(function() { 
        $(this).css({display: "none"});
        $("#intake-pain-type-"+id).css({display: "block"});
        $("#intake-pain-type-"+id).val(encounter.cc.painType);
        $("#intake-pain-type-"+id).change(function() { updateSavedPatientIntake("painType", $(this).val(), id); });
      });
      $('#intake-chief-complaint-saved-'+id).blur(function() { updateSavedPatientIntake("ccDescription", $(this).html(), id); });
      $('#intake-specific-location-saved-'+id).blur(function() { updateSavedPatientIntake("specificLocation", $(this).html(), id); });
      $('input[name=intake-occurs-when-'+id+']').click(function() { 
        var occursWhen = $('input[name=intake-occurs-when-'+id+']:checked').map(function() {return this.value;}).get().join(',');
        updateSavedPatientIntake("occursWhen", occursWhen, id); 
      });
      $('#intake-hours-since-location-saved-'+id).blur(function() { updateSavedPatientIntake("hoursSince", $(this).html(), id); });
      $('#intake-days-since-location-saved-'+id).blur(function() { updateSavedPatientIntake("daysSince", $(this).html(), id); });
      $('#intake-weeks-since-location-saved-'+id).blur(function() { updateSavedPatientIntake("weeksSince", $(this).html(), id); });
      $('#intake-months-since-location-saved-'+id).blur(function() { updateSavedPatientIntake("monthsSince", $(this).html(), id); });
      $('#intake-years-since-location-saved-'+id).blur(function() { updateSavedPatientIntake("yearsSince", $(this).html(), id); });
      $('#intake-how-long-other-saved-'+id).blur(function() { updateSavedPatientIntake("howLongOther", $(this).html(), id); });
      $('#intake-pain-x-hour-saved-'+id).blur(function() { updateSavedPatientIntake("painXHour", $(this).html(), id); });
      $('#intake-pain-x-day-saved-'+id).blur(function() { updateSavedPatientIntake("painXDay", $(this).html(), id); });
      $('#intake-pain-x-week-saved-'+id).blur(function() { updateSavedPatientIntake("painXWeek", $(this).html(), id); });
      $('#intake-pain-x-month-saved-'+id).blur(function() { updateSavedPatientIntake("painXMonth", $(this).html(), id); });
      $('#intake-pain-duration-saved-'+id).blur(function() { updateSavedPatientIntake("painDuration", $(this).html(), id); });
      $('input[name=intake-denies-'+id+']').click(function() { 
        var denies = $('input[name=intake-denies-'+id+']:checked').map(function() {return this.value;}).get().join(',');
        updateSavedPatientIntake("denies", denies, id); 
      });
      $('#intake-denies-other-saved-'+id).blur(function() { updateSavedPatientIntake("deniesOther", $(this).html(), id); });
    }
    else if (section == 'obgyn') {
      $('#intake-obgyn-g-saved-'+id).html(encounter.obgyn.g);
      $('#intake-obgyn-p-saved-'+id).html(encounter.obgyn.p);
      $('#intake-obgyn-t-saved-'+id).html(encounter.obgyn.t);
      $('#intake-obgyn-a-saved-'+id).html(encounter.obgyn.a);
      $('#intake-obgyn-l-saved-'+id).html(encounter.obgyn.l);
      $('input[name=intake-pregnant-'+id+'][value='+encounter.obgyn.pregStatus+']').attr("checked", true);
      $('input[name=intake-breastfeeding-'+id+'][value='+encounter.obgyn.breastfeeding+']').attr("checked", true);
      $('#intake-breastfeeding-months-saved-'+id).html(encounter.obgyn.breastfeedingMonths);
      $('#intake-last-period-saved-'+id).html(encounter.obgyn.lastPeriod);
      $('#intake-age-first-period-saved-'+id).html(encounter.obgyn.ageFirstPeriod);
      $('input[name=intake-pap-smear-'+id+'][value='+encounter.obgyn.papSmearStatus+']').attr("checked", true);
      $('input[name=intake-birth-control-'+id+'][value='+encounter.obgyn.birthControlStatus+']').attr("checked", true);
      $('#intake-birth-control-type-saved-'+id).html(encounter.obgyn.birthControlType);
      util_selectCheckboxesFromList(encounter.obgyn.obgynHistory, 'intake-obgyn-hist-'+id);
      $('#intake-obgyn-hist-other-saved-'+id).html(encounter.obgyn.obgynHistoryOther);
      $('#intake-obgyn-g-saved-'+id).blur(function() { updateSavedPatientIntake("obgynG", $(this).html(), id); });
      $('#intake-obgyn-p-saved-'+id).blur(function() { updateSavedPatientIntake("obgynP", $(this).html(), id); });
      $('#intake-obgyn-t-saved-'+id).blur(function() { updateSavedPatientIntake("obgynT", $(this).html(), id); });
      $('#intake-obgyn-a-saved-'+id).blur(function() { updateSavedPatientIntake("obgynA", $(this).html(), id); });
      $('#intake-obgyn-l-saved-'+id).blur(function() { updateSavedPatientIntake("obgynL", $(this).html(), id); });
      $('input[name=intake-pregnant-'+id+']').click(function() { updateSavedPatientIntake("pregStatus", $(this).val(), id); });
      $('input[name=intake-breastfeeding-'+id+']').click(function() { updateSavedPatientIntake("breastfeeding", $(this).val(), id); });
      $('#intake-breastfeeding-months-saved-'+id).blur(function() { updateSavedPatientIntake("breastfeedingMonths", $(this).html(), id); });
      $('#intake-last-period-saved-'+id).blur(function() { updateSavedPatientIntake("lastPeriod", $(this).html(), id); });
      $('#intake-age-first-period-saved-'+id).blur(function() { updateSavedPatientIntake("ageFirstPeriod", $(this).html(), id); });
      $('input[name=intake-pap-smear-'+id+']').click(function() { updateSavedPatientIntake("papSmearStatus", $(this).val(), id); });
      $('input[name=intake-birth-control-'+id+']').click(function() { updateSavedPatientIntake("birthControlStatus", $(this).val(), id); });
      $('#intake-birth-control-type-saved-'+id).blur(function() { updateSavedPatientIntake("birthControlType", $(this).html(), id); });
      $('input[name=intake-obgyn-hist-'+id+']').click(function() { 
        var obgynHist = $('input[name=intake-obgyn-hist-'+id+']:checked').map(function() {return this.value;}).get().join(',');
        updateSavedPatientIntake("obgynHistory", obgynHist, id); 
      });
      $('#intake-obgyn-hist-other-saved-'+id).blur(function() { updateSavedPatientIntake("obgynHistoryOther", $(this).html(), id); });
    }
    else if (section == 'pfsh') {
      $('#intake-num-residents-saved-'+id).html(encounter.patient.pfsh.numResidents);
      $('#intake-job-type-saved-'+id).html(encounter.patient.pfsh.jobType);
      $('input[name=intake-mother-alive-'+id+'][value='+encounter.patient.pfsh.motherAlive+']').attr("checked", true);
      $('#intake-mother-death-reason-saved-'+id).html(encounter.patient.pfsh.motherDeathReason);
      $('input[name=intake-father-alive-'+id+'][value='+encounter.patient.pfsh.fatherAlive+']').attr("checked", true);
      $('#intake-father-death-reason-saved-'+id).html(encounter.patient.pfsh.fatherDeathReason);
      $('input[name=intake-partner-alive-'+id+'][value='+encounter.patient.pfsh.partnerAlive+']').attr("checked", true);
      $('#intake-partner-death-reason-saved-'+id).html(encounter.patient.pfsh.partnerDeathReason);
      $('#intake-num-siblings-saved-'+id).html(encounter.patient.pfsh.numSiblings);
      $('#intake-num-brothers-saved-'+id).html(encounter.patient.pfsh.numBrothers);
      $('#intake-num-sisters-saved-'+id).html(encounter.patient.pfsh.numSisters);
      $('#intake-num-children-saved-'+id).html(encounter.patient.pfsh.numChildren);
      $('#intake-num-sons-saved-'+id).html(encounter.patient.pfsh.numSons);
      $('#intake-num-daughters-saved-'+id).html(encounter.patient.pfsh.numDaughters);
      $('#intake-num-residents-saved-'+id).blur(function() { updateSavedPatientIntake("numResidents", $(this).html(), id); });
      $('#intake-job-type-saved-'+id).blur(function() { updateSavedPatientIntake("jobType", $(this).html(), id); });
      $('input[name=intake-mother-alive-'+id+']').click(function() { updateSavedPatientIntake("motherAlive", $(this).val() == 'true', id); });
      $('#intake-mother-death-reason-saved-'+id).blur(function() { updateSavedPatientIntake("motherDeathReason", $(this).html(), id); });
      $('input[name=intake-father-alive-'+id+']').click(function() { updateSavedPatientIntake("fatherAlive", $(this).val() == 'true', id); });
      $('#intake-father-death-reason-saved-'+id).blur(function() { updateSavedPatientIntake("fatherDeathReason", $(this).html(), id); });
      $('input[name=intake-partner-alive-'+id+']').click(function() { updateSavedPatientIntake("partnerAlive", $(this).val() == 'true', id); });
      $('#intake-partner-death-reason-saved-'+id).blur(function() { updateSavedPatientIntake("partnerDeathReason", $(this).html(), id); });
      $('#intake-num-siblings-saved-'+id).blur(function() { updateSavedPatientIntake("numSiblings", $(this).html(), id); });
      $('#intake-num-brothers-saved-'+id).blur(function() { updateSavedPatientIntake("numBrothers", $(this).html(), id); });
      $('#intake-num-sisters-saved-'+id).blur(function() { updateSavedPatientIntake("numSisters", $(this).html(), id); });
      $('#intake-num-children-saved-'+id).blur(function() { updateSavedPatientIntake("numChildren", $(this).html(), id); });
      $('#intake-num-sons-saved-'+id).blur(function() { updateSavedPatientIntake("numSons", $(this).html(), id); });
      $('#intake-num-daughters-saved-'+id).blur(function() { updateSavedPatientIntake("numDaughters", $(this).html(), id); });
    }
    else if (section == 'supp') {
      $('#intake-num-cups-water-saved-'+id).html(encounter.supp.numCupsWater);
      $('#intake-num-cups-coffee-saved-'+id).html(encounter.supp.numCupsCoffee);
      $('#intake-num-cups-tea-saved-'+id).html(encounter.supp.numCupsTea);
      $('#intake-water-source-saved-'+id).html(encounter.supp.waterSource);
      $('#intake-num-cups-water-saved-'+id).blur(function() { updateSavedPatientIntake("numCupsWater", $(this).html(), id); });
      $('#intake-num-cups-coffee-saved-'+id).blur(function() { updateSavedPatientIntake("numCupsCoffee", $(this).html(), id); });
      $('#intake-num-cups-tea-saved-'+id).blur(function() { updateSavedPatientIntake("numCupsTea", $(this).html(), id); });
      
      $('#intake-water-source-saved-'+id).click(function() { 
        $(this).css({display: "none"});
        $("#intake-water-source-"+id).css({display: "block"});
        $("#intake-water-source-"+id).val(encounter.supp.waterSource);
        $("#intake-water-source-"+id).change(function() { updateSavedPatientIntake("waterSource", $(this).val(), id); });
      });
      
      RenderUtil.render('component/intake_questions', {encounter:encounter}, function(s) { 
        $("#intake-questions-"+id).html(s); 
        setIntakeFormMode(id, section, savedState, hasOwnership);
        $('.intake-question-editable').blur(function(e) { 
          getCurrentQuestionId(e);
          updateIntakeQuestion("question", $(this).html(), app_currentQuestionId); 
        });
        $('.intake-response-editable').blur(function(e) { 
          getCurrentQuestionId(e);
          updateIntakeQuestion("response", $(this).html(), app_currentQuestionId); 
         });
      });
      
      $('#intake-supp-new-question-'+id).click(function() { 
        var jsonData = JSON.stringify({sessionId: clinician.sessionId, encounterId: id});
        $.post("patient/addIntakeQuestion", {data:jsonData}, function(data) {
          var parsedData = $.parseJSON(data);
          var intakeQuestionId = parsedData.intakeQuestionId;
          var numQuestions = $("#intake-questions-"+id).children().length + 2;
          RenderUtil.render('component/intake_question', {ordinal:numQuestions, id: intakeQuestionId}, function(s) { 
            $("#intake-questions-"+id).append(s); 
            setIntakeFormMode(id, section, savedState, hasOwnership);
            $('.intake-question-editable').blur(function(e) { 
              getCurrentQuestionId(e);
              updateIntakeQuestion("question", $(this).html(), intakeQuestionId); 
            });
            $('.intake-response-editable').blur(function(e) { 
              getCurrentQuestionId(e);
              updateIntakeQuestion("response", $(this).html(), intakeQuestionId); 
             });
           });
       });
      });
    }
    else if (section == 'hist') {
      RenderUtil.render('component/intake_medications', {encounter:encounter}, function(s) { 
        $("#intake-medications-"+id).html(s); 
        setIntakeFormMode(id, section, savedState, hasOwnership);
        $('.intake-med-editable').blur(function(e) { 
          getCurrentMedicationId(e);
          updateIntakeMedication("medication", $(this).html(), app_currentMedicationId); 
        });
        $('.intake-dose-editable').blur(function(e) { 
          getCurrentMedicationId(e);
          updateIntakeMedication("dose", $(this).html(), app_currentMedicationId); 
        });
        $('.intake-freq-editable').blur(function(e) { 
          getCurrentMedicationId(e);
          updateIntakeMedication("frequency", $(this).html(), app_currentMedicationId); 
        });
      });
      
      $('#intake-past-s-m-saved-'+id).html(encounter.patient.hist.pastSM);
      util_selectCheckboxesFromList(encounter.patient.hist.famHist, 'intake-fam-hist-'+id);
      $('#intake-fam-hist-notes-saved-'+id).html(encounter.patient.hist.famHistNotes);
      $('#intake-fam-hist-other-saved-'+id).html(encounter.patient.hist.famHistOther);
      $('#intake-allerg-food-saved-'+id).html(encounter.patient.hist.allergFood);
      $('#intake-allerg-drug-saved-'+id).html(encounter.patient.hist.allergDrug);
      $('#intake-allerg-env-saved-'+id).html(encounter.patient.hist.allergEnv);
      $('input[name=intake-vacc-'+id+'][value='+encounter.patient.hist.vacc+']').attr("checked", true);
      $('#intake-vacc-notes-saved-'+id).html(encounter.patient.hist.vaccNotes);
      util_selectCheckboxesFromList(encounter.patient.hist.subst, 'intake-subst-'+id);
      $('#intake-smoke-pks-day-saved-'+id).html(encounter.patient.hist.smokePksDay);
      $('#intake-years-smoked-saved-'+id).html(encounter.patient.hist.yearsSmoke);
      $('#intake-smoke-years-quit-saved-'+id).html(encounter.patient.hist.smokeYearsQuit);
      $('#intake-etoh-units-week-saved-'+id).html(encounter.patient.hist.etohUnitsWeek);
      $('#intake-current-drugs-saved-'+id).html(encounter.patient.hist.currentDrugs);
      $('#intake-hist-new-medication-'+id).click(function() { 
        var jsonData = JSON.stringify({sessionId: clinician.sessionId, patientId: encounter.patient.id});
        $.post("patient/addIntakeMedication", {data:jsonData}, function(data) {
          var parsedData = $.parseJSON(data);
          var intakeMedicationId = parsedData.intakeMedicationId;
          var numMedications = $("#intake-medications-"+id).children().length + 2;
          RenderUtil.render('component/intake_medication', {ordinal:numMedications, id: intakeMedicationId}, function(s) { 
            $("#intake-medications-"+id).append(s); 
            setIntakeFormMode(id, section, savedState, hasOwnership);
            $('.intake-med-editable').blur(function(e) { 
              getCurrentMedicationId(e);
              updateIntakeMedication("medication", $(this).html(), intakeMedicationId); 
            });
            $('.intake-dose-editable').blur(function(e) { 
              getCurrentQuestionId(e);
              updateIntakeMedication("dose", $(this).html(), intakeMedicationId); 
             });
             $('.intake-freq-editable').blur(function(e) { 
              getCurrentQuestionId(e);
              updateIntakeMedication("frequency", $(this).html(), intakeMedicationId); 
             });
           });
       });
      });
      $('#intake-past-s-m-saved-'+id).blur(function() { updateSavedPatientIntake("pastSM", $(this).html(), id); });
      $('input[name=intake-fam-hist-'+id+']').click(function() { 
        var famHist = $('input[name=intake-fam-hist-'+id+']:checked').map(function() {return this.value;}).get().join(',');
        updateSavedPatientIntake("famHist", famHist, id); 
      });
      $('#intake-fam-hist-notes-saved-'+id).blur(function() { updateSavedPatientIntake("famHistNotes", $(this).html(), id); });
      $('#intake-fam-hist-other-saved-'+id).blur(function() { updateSavedPatientIntake("famHistOther", $(this).html(), id); });
      $('#intake-allerg-food-saved-'+id).blur(function() { updateSavedPatientIntake("allergFood", $(this).html(), id); });
      $('#intake-allerg-drug-saved-'+id).blur(function() { updateSavedPatientIntake("allergDrug", $(this).html(), id); });
      $('#intake-allerg-env-saved-'+id).blur(function() { updateSavedPatientIntake("allergEnv", $(this).html(), id); });
      $('input[name=intake-vacc-'+id+']').click(function() { updateSavedPatientIntake("vacc", $(this).val() == 'true', id); });
      $('#intake-vacc-notes-saved-'+id).blur(function() { updateSavedPatientIntake("vaccNotes", $(this).html(), id); });
      $('input[name=intake-subst-'+id+']').click(function() { 
        var subst = $('input[name=intake-subst-'+id+']:checked').map(function() {return this.value;}).get().join(',');
        updateSavedPatientIntake("subst", subst, id); 
      });
      $('#intake-smoke-pks-day-saved-'+id).blur(function() { updateSavedPatientIntake("smokePksDay", $(this).html(), id); });
      $('#intake-years-smoked-saved-'+id).blur(function() { updateSavedPatientIntake("yearsSmoked", $(this).html(), id); });
      $('#intake-smoke-years-quit-saved-'+id).blur(function() { updateSavedPatientIntake("smokeYearsQuit", $(this).html(), id); });
      $('#intake-etoh-units-week-saved-'+id).blur(function() { updateSavedPatientIntake("etohUnitsWeek", $(this).html(), id); });
      $('#intake-current-drugs-saved-'+id).blur(function() { updateSavedPatientIntake("currentDrugs", $(this).html(), id); });
    }
    else if (section == 'exam') {
  /*
      var canvas = document.getElementById('intake-exam-image-'+id);
      var context = canvas.getContext('2d');
      var baseImage = new Image();
      baseImage.src = 'images/exam.png';
      baseImage.onload = function() {
        context.drawImage(baseImage, 0, 0, 300, 150);
      }
  */
      initIntakeExamCanvas(id);
        
      $('#intake-hs-saved-'+id).html(encounter.exam.hs);
      $('input[name=intake-heart-rhythm-'+id+'][value='+encounter.exam.heartRhythm+']').attr("checked", true);
      $('#intake-lab-hb-saved-'+id).html(encounter.exam.hb);
      $('#intake-lab-glucose-saved-'+id).html(encounter.lab.glucose);
      $('#intake-lab-urine-dip-saved-'+id).html(encounter.lab.urineDip);
      $('#intake-diagnosis-saved-'+id).html(encounter.exam.diagnosis);
      $('#intake-dx-code-saved-'+id).html(encounter.exam.dxCode);
      $('#intake-treatment-plan-saved-'+id).html(encounter.exam.treatmentPlan);
      $('#intake-tx-code-saved-'+id).html(encounter.exam.txCode);
      $('#intake-provider-name-saved-'+id).html(encounter.exam.providerName);
      $('#intake-hs-saved-'+id).blur(function() { updateSavedPatientIntake("hs", $(this).html(), id); });
      $('input[name=intake-heart-rhythm-'+id+']').click(function() { updateSavedPatientIntake("heartRhythm", $(this).val(), id); });
      $('#intake-lab-hb-saved-'+id).blur(function() { updateSavedPatientIntake("hb", $(this).html(), id); });
      $('#intake-lab-glucose-saved-'+id).blur(function() { updateSavedPatientIntake("glucose", $(this).html(), id); });
      $('#intake-lab-urine-dip-saved-'+id).blur(function() { updateSavedPatientIntake("urineDip", $(this).html(), id); });
      $('#intake-diagnosis-saved-'+id).blur(function() { updateSavedPatientIntake("diagnosis", $(this).html(), id); });
      $('#intake-dx-code-saved-'+id).blur(function() { updateSavedPatientIntake("dxCode", $(this).html(), id); });
      $('#intake-treatment-plan-saved-'+id).blur(function() { updateSavedPatientIntake("treatmentPlan", $(this).html(), id); });
      $('#intake-tx-code-saved-'+id).blur(function() { updateSavedPatientIntake("txCode", $(this).html(), id); });
      $('#intake-provider-name-saved-'+id).blur(function() { updateSavedPatientIntake("providerName", $(this).html(), id); });
    }
    else if (section == 'follow-up') {
      $('input[name=intake-follow-up-level-'+id+'][value='+encounter.followUp.level+']').attr("checked", true);
      $('#intake-follow-up-when-saved-'+id).html(encounter.followUp.when);
      $('#intake-follow-up-condition-saved-'+id).html(encounter.followUp.condition);
      $('#intake-follow-up-dispense-rx-saved-'+id).html(encounter.followUp.dispenseRx);
      $('#intake-follow-up-uss-saved-'+id).html(encounter.followUp.USS);
      $('#intake-follow-up-pregnant-saved-'+id).html(encounter.followUp.pregnant);
      $('#intake-follow-up-wound-care-saved-'+id).html(encounter.followUp.woundCare);
      $('#intake-follow-up-ref-to-specialist-saved-'+id).html(encounter.followUp.refToSpecialist);
      $('#intake-follow-up-dental-list-saved-'+id).html(encounter.followUp.dentalList);
      $('#intake-follow-up-physiotherapy-saved-'+id).html(encounter.followUp.physiotherapy);
      $('#intake-follow-up-blood-labs-saved-'+id).html(encounter.followUp.bloodLabs);
      $('#intake-follow-up-other-saved-'+id).html(encounter.followUp.other);
      $('#intake-follow-up-pulmonary-fx-test-saved-'+id).html(encounter.followUp.pulmonaryFXTest);
      $('#intake-follow-up-vision-saved-'+id).html(encounter.followUp.vision);
      $('input[name=intake-follow-up-completed-'+id+'][value='+encounter.followUp.followUpCompleted+']').attr("checked", true) == 'true';
      encounter.followUp.followUpDate = util_processDate('#intake-follow-up-date-saved-'+id, encounter.followUp.followUpDate);
      $('#intake-follow-up-notes-saved-'+id).html(encounter.followUp.followUpNotes);
      $('input[name=intake-follow-up-level-'+id+']').click(function() { updateSavedPatientIntake("followUpLevel", $(this).val(), id); });
      $('#intake-follow-up-when-saved-'+id).blur(function() { updateSavedPatientIntake("followUpWhen", $(this).html(), id); });
      $('#intake-follow-up-condition-saved-'+id).blur(function() { updateSavedPatientIntake("followUpCondition", $(this).html(), id); });
      $('#intake-follow-up-dispense-rx-saved-'+id).blur(function() { updateSavedPatientIntake("followUpDispenseRx", $(this).html(), id); });
      $('#intake-follow-up-uss-saved-'+id).blur(function() { updateSavedPatientIntake("followUpUSS", $(this).html(), id); });
      $('#intake-follow-up-pregnant-saved-'+id).blur(function() { updateSavedPatientIntake("followUpPregnant", $(this).html(), id); });
      $('#intake-follow-up-wound-care-saved-'+id).blur(function() { updateSavedPatientIntake("followUpWoundCare", $(this).html(), id); });
      $('#intake-follow-up-ref-to-specialist-saved-'+id).blur(function() { updateSavedPatientIntake("followUpRefToSpecialist", $(this).html(), id); });
      $('#intake-follow-up-dental-list-saved-'+id).blur(function() { updateSavedPatientIntake("followUpDentalList", $(this).html(), id); });
      $('#intake-follow-up-physiotherapy-saved-'+id).blur(function() { updateSavedPatientIntake("followUpPhysiotherapy", $(this).html(), id); });
      $('#intake-follow-up-blood-labs-saved-'+id).blur(function() { updateSavedPatientIntake("followUpBloodLabs", $(this).html(), id); });
      $('#intake-follow-up-other-saved-'+id).blur(function() { updateSavedPatientIntake("followUpOther", $(this).html(), id); });
      $('#intake-follow-up-pulmonary-fx-test-saved-'+id).blur(function() { updateSavedPatientIntake("followUpPulmonaryFXTest", $(this).html(), id); });
      $('#intake-follow-up-vison-saved-'+id).blur(function() { updateSavedPatientIntake("followUpVision", $(this).html(), id); });
      $('input[name=intake-follow-up-completed-'+id+']').click(function() { updateSavedPatientIntake("followUpCompleted", $(this).val() == 'true', id); });
      $('#intake-follow-up-date-saved-'+id).blur(function() { updateSavedPatientIntake("followUpDate", $(this).html(), id); });
      $('#intake-follow-up-notes-saved-'+id).blur(function() { updateSavedPatientIntake("followUpNotes", $(this).html(), id); });
    } 
  }
}

function saveBasicInfoIntakeForm(encounter) {
  var id = encounter.id;
  encounter.consultLocation = $.trim($("#intake-consult-location-"+id).val());
  encounter.patient.cred.govtId = $.trim($("#intake-govt-id-"+id).val());
  encounter.patient.demo.dob = util_processDate("#intake-dob-"+id, encounter.patient.demo.dob); //"Mar 17, 2014 10:01:12 PM";
  encounter.patient.cred.firstName = $.trim($("#intake-nombre-"+id).val());
  encounter.patient.cred.lastName = $.trim($("#intake-apellido-"+id).val());
  encounter.patient.cred.additionalName = $.trim($("#intake-apellido-segundo-"+id).val());
  encounter.patient.demo.gender = createGender($.trim($("#intake-gender-"+id).val().toUpperCase()));
  encounter.ageInMonths = util_processNumber("#intake-age-months-"+id, encounter.ageInMonths);
  encounter.ageInYears = util_processNumber("#intake-age-years-"+id, encounter.ageInYears);
  encounter.patient.demo.primaryPhone = $.trim($("#intake-phone-"+id).val());
  encounter.community = $.trim($("#intake-community-"+id).val());
  encounter.notes = $.trim($("#intake-notes-"+id).val());
  encounter.basicInfoSaved = true;
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createBasicInfo", {data:jsonData}, function(data) {
    renderIntakeFormSection (encounter, 'basic-info', true, true);
  });
}

function saveVitalsIntakeForm(encounter) {
  var id = encounter.id;  
  encounter.vitals.height = util_processNumber("#intake-height-"+id, encounter.vitals.height);
  encounter.vitals.weight = util_processNumber("#intake-weight-"+id, encounter.vitals.weight);
  encounter.vitals.systolic = util_processNumber("#intake-sys-"+id, encounter.vitals.systolic);
  encounter.vitals.diastolic = util_processNumber("#intake-dia-"+id, encounter.vitals.diastolic);
  encounter.vitals.pulse = util_processNumber("#intake-hr-"+id, encounter.vitals.pulse);
  encounter.vitals.respiration = util_processNumber("#intake-rr-"+id, encounter.vitals.respiration);
  encounter.vitals.temperature = util_processNumber("#intake-temp-"+id, encounter.vitals.temperature);
  encounter.vitals.arm = util_processNumber("#intake-arm-"+id, encounter.vitals.arm);
  encounter.vitalsSaved = true;
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createVitals", {data:jsonData}, function(data) {
    renderIntakeFormSection (encounter, 'vitals', true, true);
  });
}


function saveCCIntakeForm(encounter) {
  var id = encounter.id;  
  encounter.cc.description = $.trim($("#intake-chief-complaint-"+id).val());
  encounter.cc.occursWhen = $('input[name="intake-occurs-when-'+id+'"]:checked').map(function() {return this.value;}).get().join(',');
  encounter.cc.occursOther = $.trim($("#intake-occurs-other-"+id).val());
  encounter.cc.specificLocation = $.trim($("#intake-specific-location-"+id).val());
  encounter.cc.painType = $.trim($("#intake-pain-type-"+id).val());
  encounter.cc.denies = $('input[name="intake-denies-'+id+'"]:checked').map(function() {return this.value;}).get().join(',');
  encounter.cc.deniesOther = $.trim($("#intake-denies-other-"+id).val());
  encounter.cc.howLongOther = $.trim($("#intake-how-long-other-"+id).val());
  encounter.cc.painDuration = $.trim($("#intake-pain-duration-"+id).val());
  encounter.cc.hoursSince = util_processNumber("#intake-hours-since-"+id, encounter.cc.hoursSince);
  encounter.cc.daysSince = util_processNumber("#intake-days-since-"+id, encounter.cc.daysSince);
  encounter.cc.weeksSince = util_processNumber("#intake-weeks-since-"+id, encounter.cc.weeksSince);
  encounter.cc.monthsSince = util_processNumber("#intake-months-since-"+id, encounter.cc.monthsSince);
  encounter.cc.yearsSince = util_processNumber("#intake-years-since-"+id, encounter.cc.yearsSince);
  encounter.cc.painScale = util_processNumber("#intake-pain-scale-"+id, encounter.cc.painScale);
  encounter.cc.painXHour = util_processNumber("#intake-pain-x-hour-"+id, encounter.cc.painXHour);
  encounter.cc.painXDay = util_processNumber("#intake-pain-x-day-"+id, encounter.cc.painXDay);
  encounter.cc.painXWeek = util_processNumber("#intake-pain-x-week-"+id, encounter.cc.painXWeek);
  encounter.cc.painXMonth = util_processNumber("#intake-pain-x-month-"+id, encounter.cc.painXMonth);
  encounter.ccSaved = true;
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createCC", {data:jsonData}, function(data) {
    renderIntakeFormSection (encounter, 'cc', true, true);
  });
}


function saveFamilyIntakeForm(encounter) {
  var id = encounter.id;  
  encounter.patient.pfsh.motherName = $.trim($("#intake-mother-name-"+id).val()); 
  util_processDate("#intake-mother-dob-"+id, encounter.patient.pfsh.motherDob);
  encounter.patient.pfsh.caretakerName = $.trim($("#intake-caretaker-name-"+id).val()); 
  encounter.patient.pfsh.patientRelationship = $.trim($("#intake-patient-relationship-"+id).val()); 
  encounter.familySaved = true;
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter,
    familySaved: true
  });
  $.post("patient/createFamily", {data:jsonData}, function(data) {
    renderIntakeFormSection (encounter, 'family', true, true);
  });
}


function saveOBGYNIntakeForm(encounter) {
  var id = encounter.id;  
  encounter.obgyn.g = $.trim($("#intake-obgyn-g-"+id).val());
  encounter.obgyn.p = $.trim($("#intake-obgyn-p-"+id).val());
  encounter.obgyn.t = $.trim($("#intake-obgyn-t-"+id).val());
  encounter.obgyn.a = $.trim($("#intake-obgyn-a-"+id).val());
  encounter.obgyn.l = $.trim($("#intake-obgyn-l-"+id).val());
  encounter.obgyn.pregStatus = $('input[name=intake-pregnant-'+id+']:checked').val();
  encounter.obgyn.breastfeeding = $('input[name=intake-breastfeeding-'+id+']:checked').val();
  encounter.obgyn.breastfeedingMonths = $.trim($("#intake-breastfeeding-months-"+id).val());
  encounter.obgyn.lastPeriod = $.trim($("#intake-last-period-"+id).val());
  encounter.obgyn.ageFirstPeriod = $.trim($("#intake-age-first-period-"+id).val());
  encounter.obgyn.papSmearStatus = $('input[name=intake-pap-smear-'+id+']:checked').val();
  encounter.obgyn.birthControlStatus = $('input[name=intake-birth-control-'+id+']:checked').val();
  encounter.obgyn.birthControlType = $.trim($("#intake-birth-control-type-"+id).val());
  encounter.obgyn.history = $('input[name="intake-obgyn-hist-'+id+'"]:checked').map(function() {return this.value;}).get().join(',');
  encounter.obgyn.historyOther = $.trim($("#intake-obgyn-hist-other-"+id).val());
  encounter.obgynSaved = true;
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createOBGYN", {data:jsonData}, function(data) {
    renderIntakeFormSection (encounter, 'obgyn', true, true);
  });
}


function savePFSHIntakeForm(encounter) {
  var id = encounter.id;  
  encounter.patient.pfsh.jobType = $.trim($("#intake-job-type-"+id).val());
  encounter.patient.pfsh.motherAlive = $('input[name=intake-mother-alive-'+id+']:checked').val() == 'true';
  encounter.patient.pfsh.motherDeathReason = $.trim($("#intake-mother-death-reason-"+id).val());
  encounter.patient.pfsh.fatherAlive = $('input[name=intake-father-alive-'+id+']:checked').val() == 'true';
  encounter.patient.pfsh.fatherDeathReason = $.trim($("#intake-mother-death-reason-"+id).val());
  encounter.patient.pfsh.partnerAlive = $('input[name=intake-partner-alive-'+id+']:checked').val() == 'true';
  encounter.patient.pfsh.partnerDeathReason = $.trim($("#intake-partner-death-reason-"+id).val());
  encounter.patient.pfsh.numResidents = util_processNumber("#intake-num-residents-"+id, encounter.patient.pfsh.numResidents);
  encounter.patient.pfsh.numSiblings = util_processNumber("#intake-num-siblings-"+id, encounter.patient.pfsh.numSiblings);
  encounter.patient.pfsh.numBrothers = util_processNumber("#intake-num-brothers-"+id, encounter.patient.pfsh.numBrothers);
  encounter.patient.pfsh.numSisters = util_processNumber("#intake-num-sisters-"+id, encounter.patient.pfsh.numSisters);
  encounter.patient.pfsh.numChildren = util_processNumber("#intake-num-children-"+id, encounter.patient.pfsh.numChildren);
  encounter.patient.pfsh.numSons = util_processNumber("#intake-num-sons-"+id, encounter.patient.pfsh.numSons);
  encounter.patient.pfsh.numDaughters = util_processNumber("#intake-num-daughters-"+id, encounter.patient.pfsh.numDaughters);
  encounter.pfshSaved = true;
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createPFSH", {data:jsonData}, function(data) {
    renderIntakeFormSection (encounter, 'pfsh', true, true);
  });
}


function saveSuppIntakeForm(encounter) {
  var id = encounter.id;  
  encounter.supp.waterSource = $.trim($("#intake-water-source-"+id).val());
  encounter.supp.numCupsCoffee = util_processNumber("#intake-num-cups-coffee"+id, encounter.supp.numCupsCoffee);
  encounter.supp.numCupsTea = util_processNumber("#intake-num-cups-tea-"+id, encounter.supp.numCupsTea);
  encounter.supp.numCupsWater = util_processNumber("#intake-num-cups-water-"+id, encounter.supp.numCupsWater);
  
  for (i=0;i<encounter.supp.intakeQuestionList.length;i++) { 
    var questionId = encounter.supp.intakeQuestionList[i].id;
    encounter.supp.intakeQuestionList[i].question = $("#intake-question-"+questionId).val();
    encounter.supp.intakeQuestionList[i].response = $("#intake-response-"+questionId).val();
  }
  encounter.suppSaved = true;
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createSupp", {data:jsonData}, function(data) {
    renderIntakeFormSection (encounter, 'supp', true, true);
  });
}


function saveHistIntakeForm(encounter) {
  var id = encounter.id;  
  for (i=0;i<encounter.patient.hist.intakeMedicationList.length;i++) { 
    var medicationId = encounter.patient.hist.intakeMedicationList[i].id;
    encounter.patient.hist.intakeMedicationList[i].medication = $("#intake-med-"+medicationId).val();
    encounter.patient.hist.intakeMedicationList[i].dose = $("#intake-dose-"+medicationId).val();
    encounter.patient.hist.intakeMedicationList[i].frequency = $("#intake-freq-"+medicationId).val();
  }
  encounter.patient.hist.pastSM = $.trim($("#intake-past-s-m-"+id).val());
  encounter.patient.hist.famHist = $('input[name="intake-fam-hist-'+id+'"]:checked').map(function() {return this.value;}).get().join(',');
  encounter.patient.hist.famHistOther = $.trim($("#intake-fam-hist-other-"+id).val());
  encounter.patient.hist.famHistNotes = $.trim($("#intake-fam-hist-notes-"+id).val());
  encounter.patient.hist.allergFood = $.trim($("#intake-allerg-food-"+id).val());
  encounter.patient.hist.allergDrug = $.trim($("#intake-allerg-drug-"+id).val());
  encounter.patient.hist.allergEnv = $.trim($("#intake-allerg-env-"+id).val());
  encounter.patient.hist.vacc = $('input[name=intake-vacc-'+id+']:checked').val() == 'true';
  encounter.patient.hist.vaccNotes = $.trim($("#intake-vacc-notes-"+id).val());
  encounter.patient.hist.subst = $('input[name="intake-subst-'+id+'"]:checked').map(function() {return this.value;}).get().join(',');
  encounter.patient.hist.currentDrugs = $.trim($("#intake-current-drugs-"+id).val());
  encounter.patient.hist.smokePksDay = util_processNumber("#intake-smoke-pks-day-"+id, encounter.patient.hist.smokePksDay);
  encounter.patient.hist.yearsSmoked = util_processNumber("#intake-years-smoked-"+id, encounter.patient.hist.yearsSmoked);
  encounter.patient.hist.smokeYearsQuit = util_processNumber("#intake-smoke-years-quit-"+id, encounter.patient.hist.smokeYearsQuit);
  encounter.patient.hist.etohUnitsWeek = util_processNumber("#intake-etoh-units-week-"+id, encounter.patient.hist.etohUnitsWeek);
  encounter.histSaved = true;
  jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createHist", {data:jsonData}, function(data) {
    renderIntakeFormSection (encounter, 'hist', true, true);
  });
}


function saveExamIntakeForm(encounter) {
  var id = encounter.id;  
  encounter.exam.hs = $.trim($("#intake-hs-"+id).val());
  encounter.exam.heartRhythm = $('input[name=intake-heart-rhythm-'+id+']:checked').val();
  encounter.exam.hb = $.trim($("#intake-lab-hb-"+id).val());
  encounter.exam.glucose = $.trim($("#intake-lab-glucose-"+id).val());
  encounter.exam.urineDIP = $.trim($("#intake-lab-urine-dip-"+id).val());
  encounter.exam.diagnosis = $.trim($("#intake-diagnosis-"+id).val());
  encounter.exam.dxCode = $.trim($("#intake-dx-code-"+id).val());
  encounter.exam.treatmentPlan = $.trim($("#intake-treatment-plan-"+id).val());
  encounter.exam.txCode = $.trim($("#intake-tx-code-"+id).val());
  encounter.exam.providerName = $.trim($("#intake-provider-name-"+id).val());
  encounter.examSaved = true;
  jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createExam", {data:jsonData}, function(data) {
    renderIntakeFormSection (encounter, 'exam', true, true);
  });
}

function saveFollowUpIntakeForm(encounter) {
  var id = encounter.id;  
  
  encounter.followUp.level = $('input[name=intake-follow-up-level-'+id+']:checked').val();
  encounter.followUp.when = $.trim($("#intake-follow-up-when-"+id).val());
  encounter.followUp.condition = $.trim($("#intake-follow-up-condition-"+id).val());
  encounter.followUp.dispenseRx = $.trim($("#intake-follow-up-dispense-rx-"+id).val());
  encounter.followUp.USS = $.trim($("#intake-follow-up-uss-"+id).val());
  encounter.followUp.pregnant = $.trim($("#intake-follow-up-pregnant-"+id).val());
  encounter.followUp.woundCare = $.trim($("#intake-follow-up-wound-care-"+id).val());
  encounter.followUp.refToSpecialist = $.trim($("#intake-follow-up-ref-to-specialist-"+id).val());
  encounter.followUp.dentalList = $.trim($("#intake-follow-up-dental-list-"+id).val());
  encounter.followUp.physiotherapy = $.trim($("#intake-follow-up-physiotherapy-"+id).val());
  encounter.followUp.bloodLabs = $.trim($("#intake-follow-up-blood-labs-"+id).val());
  encounter.followUp.other = $.trim($("#intake-follow-up-other-"+id).val());
  encounter.followUp.pulmonaryFXTest = $.trim($("#intake-follow-up-pulmonary-fx-test-"+id).val());
  encounter.followUp.vision = $.trim($("#intake-follow-up-vision-"+id).val());
  encounter.followUp.completed = $('input[name=intake-follow-up-completed--'+id+']:checked').val() == 'true';
  encounter.followUp.notes = $.trim($("#intake-follow-up-notes-"+id).val());
  encounter.followUp.followUpDate = util_processDate('#intake-follow-up-date-saved-'+id, encounter.followUp.followUpDate);
  encounter.followUpSaved = true;
  jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createFollowUp", {data:jsonData}, function(data) {
    renderIntakeFormSection (encounter, 'follow-up', true, true);
  });
}


function getPatientEncounter(id) {
  var group = getPatientIntakeGroup(app_currentGroupId);
  for (i=0;i<group.encounterList.length;i++) {
    if (group.encounterList[i].id == id) {
      return group.encounterList[i];
    }
  }
}

function updateLocalPatientIntake(property, value, patientId) {
  app_currentEncounter[property] = value;  
}

function updateSavedPatientIntake(property, value, encounterId) {
  var encounter = app_currentEncounter;
  if (encounter == undefined) {
    encounter = getPatientEncounter(encounterId);
  }
  updateLocalPatientIntake(property, value, encounter.patient.id);
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    patientId: encounter.patient.id,
    updateProperty:property,
    updatePropertyValue:value
  });
  $.post("patient/updatePatient", {data:jsonData}, function(data) {
    if (property == "painType") {
      $("#intake-pain-type-"+encounterId).css({display: "none"});
      $("#intake-pain-type-saved-"+encounterId).html(value);
      $("#intake-pain-type-saved-"+encounterId).css({display: "block"});
    }
    else if (property == "waterSource") {
      $("#intake-water-source-"+encounterId).css({display: "none"});
      $("#intake-water-source-saved-"+encounterId).html(value);
      $("#intake-water-source-saved-"+encounterId).css({display: "block"});
    }
  }); 
}

function updateIntakeQuestion(property, value, intakeQuestionId) {
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    intakeQuestionId: intakeQuestionId,
    updateProperty:property,
    updatePropertyValue:value
  });
  $.post("patient/updateIntakeQuestion", {data:jsonData}, function(data) {
  }); 
}

function updateIntakeMedication(property, value, intakeMedicationId) {
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    intakeMedicationId: intakeMedicationId,
    updateProperty:property,
    updatePropertyValue:value
  });
  $.post("patient/updateIntakeMedication", {data:jsonData}, function(data) {
  }); 
}


function getCurrentQuestionId(e) {
  var attributes = e.currentTarget.attributes;
  for (i=0;i<attributes.length;i++) {
    if (attributes[i].name == 'data-id') {
      app_currentQuestionId = parseInt(attributes[i].nodeValue); 
    }
  }
} 

function getCurrentMedicationId(e) {
  var attributes = e.currentTarget.attributes;
  for (i=0;i<attributes.length;i++) {
    if (attributes[i].name == 'data-id') {
      app_currentMedicationId = parseInt(attributes[i].nodeValue); 
    }
  }
} 

function initIntakeExamCanvas(id) {
  canvas = document.getElementById('intake-exam-image-overlay-'+id);
  ctx = canvas.getContext("2d");
  w = canvas.width;
  h = canvas.height;
  canvas.addEventListener("mousemove", function (e) { findIntakeExamCanvasXY('move', e) }, false);
  canvas.addEventListener("mousedown", function (e) { findIntakeExamCanvasXY('down', e) }, false);
  canvas.addEventListener("mouseup", function (e) { findIntakeExamCanvasXY('up', e) }, false);
  canvas.addEventListener("mouseout", function (e) { findIntakeExamCanvasXY('out', e) }, false);
}


function findIntakeExamCanvasXY(res, e) {
  if (res == 'down') {
    prevX = currX;
    prevY = currY;
    currX = e.clientX - canvas.offsetLeft;
    currY = e.clientY - canvas.offsetTop;

    flag = true;
    dot_flag = true;
    if (dot_flag) {
      ctx.beginPath();
      ctx.fillStyle = strokeColor;
      ctx.fillRect(currX, currY, 2, 2);
      ctx.closePath();
      dot_flag = false;
    }
  }
  if (res == 'up' || res == "out") {
    flag = false;
  }
  if (res == 'move') {
    if (flag) {
      prevX = currX;
      prevY = currY;
      currX = e.clientX - canvas.offsetLeft;
      currY = e.clientY - canvas.offsetTop;
      drawIntakeExamCanvas();
    }
  }
}


function drawIntakeExamCanvas() {
  ctx.beginPath();
  ctx.moveTo(prevX, prevY);
  ctx.lineTo(currX, currY);
  ctx.strokeStyle = strokeColor;
  ctx.lineWidth = strokeWidth;
  ctx.stroke();
  ctx.closePath();
}

function setIntakeFormMode(id, section, isSaved, hasOwnership) {
  $('#intake-'+section+'-panel-'+id+' .form-control-unsaved').css({display: (isSaved == true ? "none" : "block")});
  $('#intake-'+section+'-panel-'+id+' .form-control-saved').css({display: (isSaved == true ? "block" : "none")});
  $('#intake-'+section+'-save-'+id).css({display: (hasOwnership == true && isSaved == false ? "inline-block" : "none")});
  $('#intake-'+section+'-saved-'+id).css({display: (hasOwnership == true && isSaved == true ? "inline-block" : "none")});
  $('#intake-'+section+'-clear-'+id).css({display: (hasOwnership == true && isSaved == false ? "inline-block" : "none")});
  $('#intake-'+section+'-panel-'+id+' .form-control-unsaved').prop("readonly",!hasOwnership);
  if (hasOwnership == false) {
    $('#intake-'+section+'-panel-'+id+' .form-control-saved').removeAttr('contenteditable').blur();
    $('#intake-basic-info-photo-upload-control-'+id).css({display:"none"});
  }
  else {
    $('#intake-basic-info-photo-upload-control-'+id).css({display:"block"});
    $('#intake-'+section+'-panel-'+id+' .form-control-saved').prop('contenteditable', true);
    $('#intake-'+section+'-panel-'+id+' .form-control-readonly').removeAttr('contenteditable').blur();
  }
}

function  setupPictureUpload(encounterId, patientId) {
  $('#intake-basic-info-photo-upload-'+encounterId).click(function(){ 
    $('#intake-basic-info-photo-upload-progress-'+encounterId+' .progress-bar').css('width', '0');
  });
  uploader = new qq.FileUploader({
   element: document.getElementById('intake-basic-info-photo-upload-'+encounterId),
   action: 'patient/uploadProfileImage?patientId='+patientId+'&sessionId=' + clinician.sessionId,
   debug: true,
   sizeLimit: 1048576,   
   allowedExtensions: ['jpg', 'jpeg', 'png', 'gif'],
   onProgress: function(id, fileName, loaded, total) {
      var progress = parseInt(loaded / total * 100, 10);
      $('#intake-basic-info-photo-upload-progress-'+encounterId+' .progress-bar').css('width', progress + '%');
   },
   onComplete: function(id, fileName, responseJSON){
     $('#intake-basic-info-photo-upload-progress-'+encounterId+' .progress-bar').css('width', '100%');
   var response = parsedData = $.parseJSON(responseJSON);
   var path = response.filename;
   var patientId = response.patientId;
     $("#intake-basic-info-photo-"+encounterId).attr("src","files/patients/"+patientId+"/"+path);
   }
  }); 
}

    