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


function setEncounterFormModes(hasOwnership) {
  setEncounterFormMode(app_currentEncounter, 'demo', app_currentEncounter.demoSaved, hasOwnership);
  setEncounterFormMode(app_currentEncounter, 'vitals', app_currentEncounter.vitalsSaved, hasOwnership);
  setEncounterFormMode(app_currentEncounter, 'soap-note', app_currentEncounter.soapNoteSaved, hasOwnership);
  setEncounterFormMode(app_currentEncounter, 'cc', app_currentEncounter.ccSaved, hasOwnership);
  setEncounterFormMode(app_currentEncounter, 'obgyn', app_currentEncounter.obgynSaved, hasOwnership);
  setEncounterFormMode(app_currentEncounter, 'pfsh', app_currentEncounter.pfshSaved, hasOwnership);
  setEncounterFormMode(app_currentEncounter, 'supp', app_currentEncounter.suppSaved, hasOwnership);
  setEncounterFormMode(app_currentEncounter, 'hist', app_currentEncounter.histSaved, hasOwnership);
  setEncounterFormMode(app_currentEncounter, 'exam', app_currentEncounter.examSaved, hasOwnership);
  setEncounterFormMode(app_currentEncounter, 'follow-up', app_currentEncounter.followUpSaved, hasOwnership);
}


function updateLockStatusIcon(lockStatus) {
  var id = app_currentEncounter.id;
  var oldLockStatusIcon = app_lockIcons[app_oldLockStatus];
  var lockStatusIcon = app_lockIcons[lockStatus];
  $("#tab-header-encounter-icon-"+id).removeClass(oldLockStatusIcon);
  $("#tab-header-encounter-icon-"+id).addClass(lockStatusIcon);
}


$('#app-encounter-close-record').click(function() { 
  RenderUtil.render('dialog/close_encounter', {}, function(s) { 
    $('#modals-placement').html(s);
    $('#modal-close-encounter').modal('show'); 
    $('#app-encounter-close-record-confirmation').click(function(){  
      var jsonData = JSON.stringify({ sessionId: clinician.sessionId, encounterId: app_currentEncounter.id});
      $.post("patient/closeEncounter", {data:jsonData}, function(data) {
        var parsedData = $.parseJSON(data);
        displayNotification('Patient Encounter Record Completed');
        var jsonData = JSON.stringify({ sessionId: clinician.sessionId });
        $.post("patient/getPatientEncounterGroups", {data:jsonData}, function(data) {
          var parsedData = $.parseJSON(data);
          app_patientEncounterGroups = parsedData.patientEncounterGroups;
          buildGroupOrderArray();
          $("#app-encounter-group-tabs").html('');
          viewPatientEncounterFormGroupWithId();
        });
      });
    });
  });
});





function printEncounterForm(template, title) { 
  var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
  RenderUtil.render('print/'+template,  {encounter:app_currentEncounter, currentDate:currentDate}, function(obj) {
    var s = obj[0].outerHTML;
    print_openPrintWindow('print.html', s, title);
  });
}

function renderPatientEncounterForm(encounter, hasOwnership) {
  var id = encounter.id;
  var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
  
  renderEncounterFormSection (encounter, 'demo', encounter.demoSaved, hasOwnership);
  renderEncounterFormSection (encounter, 'vitals', encounter.vitalsSaved, hasOwnership);
  renderEncounterFormSection (encounter, 'soap-note', encounter.soapNoteSaved, hasOwnership);
  renderEncounterFormSection (encounter, 'cc', encounter.ccSaved, hasOwnership);
  renderEncounterFormSection (encounter, 'obgyn', encounter.obgynSaved, hasOwnership);
  renderEncounterFormSection (encounter, 'pfsh', encounter.pfshSaved, hasOwnership);
  renderEncounterFormSection (encounter, 'supp', encounter.suppSaved, hasOwnership);
  renderEncounterFormSection (encounter, 'hist', encounter.histSaved, hasOwnership);
  renderEncounterFormSection (encounter, 'exam', encounter.examSaved, hasOwnership);
  renderEncounterFormSection (encounter, 'follow-up', encounter.followUpSaved, hasOwnership);
  
  $(".edit-on-select").focus(function() { $(this).selectContentEditableText(); });
  
  $('#encounter-vitals-save-'+id).click(function() { saveVitalsEncounterForm(encounter); });
  $('#encounter-soap-note-save-'+id).click(function() { saveSOAPNoteEncounterForm(encounter); });
  $('#encounter-cc-save-'+id).click(function() { saveCCEncounterForm(encounter); });
  $('#encounter-obgyn-save-'+id).click(function() { saveOBGYNEncounterForm(encounter); });
  $('#encounter-pfsh-save-'+id).click(function() { savePFSHEncounterForm(encounter); });
  $('#encounter-supp-save-'+id).click(function() { saveSuppEncounterForm(encounter); });
  $('#encounter-hist-save-'+id).click(function() { saveHistEncounterForm(encounter); });
  $('#encounter-exam-save-'+id).click(function() { saveExamEncounterForm(encounter); });
  $('#encounter-follow-up-save-'+id).click(function() { saveFollowUpEncounterForm(encounter); });
  setupPictureUpload(encounter.id, encounter.patient.id);
  $('#encounter-demo-print-'+id).click(function() { printEncounterForm('print_encounter_basic_info', 'PATIENT ENCOUNTER')});
  $('#encounter-vitals-print-'+id).click(function() { printEncounterForm('print_encounter_vitals', 'VITALS')});
  $('#encounter-soap-note-print-'+id).click(function() { printEncounterForm('print_encounter_soap_note', 'SOAP NOTE')});
  $('#encounter-cc-print-'+id).click(function() { printEncounterForm('print_encounter_cc', 'CHIEF COMPLAINT')});
  $('#encounter-obgyn-print-'+id).click(function() { printEncounterForm('print_encounter_obgyn', 'OBGYN')});
  $('#encounter-pfsh-print-'+id).click(function() { printEncounterForm('print_encounter_pfsh', 'SOCIAL & FAMILY HISTORY')});
  $('#encounter-supp-print-'+id).click(function() { printEncounterForm('print_encounter_supp', 'SUPPLEMENTAL QUESTIONS')});
  $('#encounter-hist-print-'+id).click(function() { printEncounterForm('print_encounter_hist', 'MEDICAL HISTORY')});
  $('#encounter-exam-print-'+id).click(function() { printEncounterForm('print_encounter_exam', 'EXAM')});
  $('#encounter-follow-up-print-'+id).click(function() { printEncounterForm('print_encounter_follow-up', 'FOLLOW UP')});
  initEncounterTypeAheads(id);
} 


function initEncounterTypeAheads(id) {
  var icd10 = new Bloodhound({
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
      url: 'app/searchICD10?sessionId='+clinician.sessionId+'&searchText=%QUERY',
      filter: function (data) {
        return $.map(data.icd10List, function (icd10) {
          return { value: icd10.code + ' ' + icd10.description };
        });
      }
    }
  });
  icd10.initialize();
  $('#encounter-dx-code-'+id).typeahead( { hint: true, highlight: true, limit: 10, minLength: 3 },
  { name: 'encounter-dx-code-'+id, displayKey: 'value', source: icd10.ttAdapter(), }); 
  
    var cpt = new Bloodhound({
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
      url: 'app/searchCPT?sessionId='+clinician.sessionId+'&searchText=%QUERY',
      filter: function (data) {
        return $.map(data.cptList, function (cpt) {
          return { value: cpt.code + ' ' + cpt.description };
        });
      }
    }
  });
  cpt.initialize();
  $('#encounter-tx-code-'+id).typeahead( { hint: true, highlight: true, limit: 10, minLength: 3 },
  { name: 'encounter-tx-code-'+id, displayKey: 'value', source: cpt.ttAdapter(), }); 
} 


function renderEncounterFormSection (encounter, section, savedState, hasOwnership) {
  var id = encounter.id;
  var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
  var notEditable = true; 
  setEncounterFormMode(encounter, section, savedState, hasOwnership);
  
  if (savedState == false) {

    if (section == 'demo') {
    }
    else if (section == 'soap-note') {
    }
    else if (section == 'vitals') {
      $("#encounter-height-"+id+", #encounter-weight-"+id+", #encounter-temp-"+id).keydown(function(e) { util_filterDecimalInput(e); });
    }
    else if (section == 'cc') {
      $("#encounter-hours-since-"+id+", #encounter-days-since-"+id+", #encounter-weeks-since-"+id+", #encounter-months-since-"+id+", #encounter-years-since-"+id).keydown(function(e) { util_filterDecimalInput(e); });
      $("#encounter-pain-x-hour-"+id+", #encounter-pain-x-day-"+id+", #encounter-pain-x-week-"+id+", #encounter-pain-x-month-"+id).keydown(function(e) { util_filterDecimalInput(e); });
      $('#encounter-pain-scale-'+id).slider({value:encounter.cc.painScale}).on('slide', function(ev){
        $('#encounter-pain-scale-value-'+id).html(this.value)
        updateSavedPatientEncounter("painScale", this.value, id);
      });
      $('#encounter-pain-scale-value-'+id).html(encounter.cc.painScale);
    }
    else if (section == 'obgyn' && !encounter.obgyn) {
      $('#obgyn-form, #obgyn-header').hide();
    }
    else if (section == 'pfsh') {
    }
    else if (section == 'supp') {
      RenderUtil.render('component/encounter_questions', {encounter:encounter}, function(s) { $("#encounter-questions-"+id).html(s); setEncounterFormMode(encounter, section, savedState, hasOwnership);});
      $('#encounter-supp-new-question-'+id).click(function() { 
       var jsonData = JSON.stringify({sessionId: clinician.sessionId, encounterId: id});
        $.post("patient/addEncounterQuestion", {data:jsonData}, function(data) {
        var parsedData = $.parseJSON(data);
          var numQuestions = $("#encounter-questions-"+id).children().length + 2;
          RenderUtil.render('component/encounter_question', {ordinal:numQuestions, id: parsedData.encounterQuestionId}, function(s) { $("#encounter-questions-"+id).append(s); setEncounterFormMode(encounter, section, savedState, hasOwnership);});
        });
      });
    }
    else if (section == 'hist') {
      RenderUtil.render('component/encounter_medications', {encounter:encounter}, function(s) { $("#encounter-medications-"+id).html(s); setEncounterFormMode(encounter, section, savedState, hasOwnership);});
      $('#encounter-hist-new-medication-'+id).click(function() { 
       var jsonData = JSON.stringify({sessionId: clinician.sessionId, patientId: encounter.patient.id});
        $.post("patient/addEncounterMedication", {data:jsonData}, function(data) {
        var parsedData = $.parseJSON(data);
          var numMedications = $("#encounter-medications-"+id).children().length + 2;
          RenderUtil.render('component/encounter_medication', {ordinal:numMedications, id: parsedData.encounterMedicationId}, function(s) { $("#encounter-medications-"+id).append(s); setEncounterFormMode(encounter, section, savedState, hasOwnership);});
        });
      });
    }
    else if (section == 'exam') {
    /*
      var canvas = document.getElementById('encounter-exam-image-'+id);
      var context = canvas.getContext('2d');
      var baseImage = new Image();
      baseImage.src = 'images/exam.png';
      baseImage.onload = function() {
        context.drawImage(baseImage, 0, 0, 100, 50);
      }
    */
     initEncounterExamCanvas(id);
    }
    else if (section == 'follow-up') {
      $('#encounter-follow-up-date-'+id).mask("99/99/9999");
    }

  }
  else if (savedState == true) {

    if (section == 'demo') {
      $('#encounter-demo-date-saved-'+id).html(dateFormat(encounter.date, 'mm/dd/yyyy'));
      $('#encounter-demo-mrn-saved-'+id).html(encounter.patient.cred.mrn);
      $("#encounter-demo-photo-"+id).attr("src", app_patientChartHeadshot);
      $('#encounter-demo-first-name-saved-'+id).html(encounter.patient.cred.firstName);
      $('#encounter-demo-middle-name-saved-'+id).html(encounter.patient.cred.middleName);
      $('#encounter-demo-last-name-saved-'+id).html(encounter.patient.cred.lastName);
      $('#encounter-demo-govt-id-saved-'+id).html(encounter.patient.cred.govtId);
      var dob = dateFormat(encounter.patient.demo.dob, 'mm/dd/yyyy')
      $('#encounter-demo-dob-saved-'+id).html(dob);
      $('#encounter-demo-gender-saved-'+id).html(encounter.patient.demo.gender.code);
      $('#encounter-demo-marital-saved-'+id).html(encounter.patient.demo.maritalStatus.name);
      $('#encounter-demo-race-saved-'+id).html(encounter.patient.demo.race.name);
      $('#encounter-demo-ethnicity-saved-'+id).html(encounter.patient.demo.ethnicity.name);
      $('#encounter-demo-school-status-saved-'+id).html(encounter.patient.demo.schoolStatus);
      $('#encounter-demo-employment-status-saved-'+id).html(encounter.patient.demo.employmentStatus);
      $('#encounter-demo-notes-saved-'+id).html(encounter.notes);
      $('#encounter-demo-street-address-saved-'+id).html(encounter.patient.demo.streetAddress1);
      $('#encounter-demo-city-saved-'+id).html(encounter.patient.demo.city);
      $('#encounter-demo-us-state-saved-'+id).html(encounter.patient.demo.usState.name);
      $('#encounter-demo-postal-code-saved-'+id).html(encounter.patient.demo.postalCode);
      $('#encounter-demo-email-saved-'+id).html(encounter.patient.cred.email);
      $('#encounter-demo-primary-phone-saved-'+id).html(encounter.patient.demo.primaryPhone);
      $('#encounter-demo-secondary-phone-saved-'+id).html(encounter.patient.demo.secondaryPhone);
      $('#encounter-demo-occupation-saved-'+id).html(encounter.patient.demo.occupation);
      $('#encounter-demo-employer-saved-'+id).html(encounter.patient.demo.employer);
      $('#encounter-demo-school-name-saved-'+id).html(encounter.patient.demo.schoolName);
      
      $('#encounter-demo-first-name-saved-'+id).blur(function() { updateSavedPatientEncounter("firstName", $(this).html(), id); });
      $('#encounter-demo-middle-name-saved-'+id).blur(function() { updateSavedPatientEncounter("middleName", $(this).html(), id); });
      $('#encounter-demo-last-name-saved-'+id).blur(function() { updateSavedPatientEncounter("lastName", $(this).html(), id); });
      $('#encounter-demo-govt-id-saved-'+id).blur(function() { updateSavedPatientEncounter("govtId", $(this).html(), id); });
      $('#encounter-demo-gender-saved-'+id).blur(function() { updateSavedPatientEncounter("gender", $(this).html(), id, true, 'encounter-demo-gender'); });
      $('#encounter-demo-race-saved-'+id).blur(function() { updateSavedPatientEncounter("race", $(this).html(), id, true, 'encounter-demo-marital'); });
      $('#encounter-demo-marital-saved-'+id).blur(function() { updateSavedPatientEncounter("maritalStatus", $(this).html(), id, true, 'encounter-demo-race'); });
      $('#encounter-demo-ethnicity-saved-'+id).blur(function() { updateSavedPatientEncounter("ethnicity", $(this).html(), id, true, 'encounter-demo-ethnicity'); });
      $('#encounter-demo-school-status-saved-'+id).blur(function() { updateSavedPatientEncounter("schoolStatus", $(this).html(), id, true, 'encounter-demo-school-status'); });
      $('#encounter-demo-employment-status-saved-'+id).blur(function() { updateSavedPatientEncounter("employmentStatus", $(this).html(), id, true, 'encounter-demo-employment-status'); });
      $('#encounter-demo-notes-saved-'+id).blur(function() { updateSavedPatientEncounter("notes", $(this).html(), id); });
      $('#encounter-demo-dob-saved-'+id).blur(function() { updateSavedPatientEncounter("dob", $(this).html(), id); });
      $('#encounter-demo-street-address-saved-'+id).blur(function() { updateSavedPatientEncounter("streetAddress1", $(this).html(), id); });
      $('#encounter-demo-city-saved-'+id).blur(function() { updateSavedPatientEncounter("city", $(this).html(), id); });
      $('#encounter-demo-postal-code-saved-'+id).blur(function() { updateSavedPatientEncounter("postalCode", $(this).html(), id); });
      $('#encounter-demo-email-saved-'+id).blur(function() { updateSavedPatientEncounter("email", $(this).html(), id); });
      $('#encounter-demo-primary-phone-saved-'+id).blur(function() { updateSavedPatientEncounter("primaryPhone", $(this).html(), id); });
      $('#encounter-demo-secondary-phone-saved-'+id).blur(function() { updateSavedPatientEncounter("secondaryPhone", $(this).html(), id); });
      $('#encounter-demo-occupation-saved-'+id).blur(function() { updateSavedPatientEncounter("occupation", $(this).html(), id); });
      $('#encounter-demo-employer-saved-'+id).blur(function() { updateSavedPatientEncounter("employer", $(this).html(), id); });
      $('#encounter-demo-school-name-saved-'+id).blur(function() { updateSavedPatientEncounter("schoolName", $(this).html(), id); });
    }
    else if (section == 'soap-note') {
      $('#encounter-soap-note-subjective-saved-'+id).html(encounter.soapNote.subjective);
      $('#encounter-soap-note-objective-saved-'+id).html(encounter.soapNote.objective);
      $('#encounter-soap-note-assessment-saved-'+id).html(encounter.soapNote.assessment);
      $('#encounter-soap-note-plan-saved-'+id).html(encounter.soapNote.plan);
      $('#encounter-soap-note-subjective-saved-'+id).blur(function() { updateSavedPatientEncounter("subjective", $(this).html(), id); });
      $('#encounter-soap-note-objective-saved-'+id).blur(function() { updateSavedPatientEncounter("objective", $(this).html(), id); });
      $('#encounter-soap-note-assessment-saved-'+id).blur(function() { updateSavedPatientEncounter("assessment", $(this).html(), id); });
      $('#encounter-soap-note-plan-saved-'+id).blur(function() { updateSavedPatientEncounter("plan", $(this).html(), id); });
    }
    else if (section == 'vitals') {
      $('#encounter-height-saved-'+id).html(encounter.vitals.height);
      $('#encounter-weight-saved-'+id).html(encounter.vitals.weight);
      $('#encounter-sys-saved-'+id).html(encounter.vitals.systolic);
      $('#encounter-dia-saved-'+id).html(encounter.vitals.diastolic);
      $('#encounter-hr-saved-'+id).html(encounter.vitals.pulse);
      $('#encounter-rr-saved-'+id).html(encounter.vitals.respiration);
      $('#encounter-temp-saved-'+id).html(encounter.vitals.temperature);
      $('#encounter-height-saved-'+id).blur(function() { updateSavedPatientEncounter("height", $(this).html(), id); });
      $('#encounter-weight-saved-'+id).blur(function() { updateSavedPatientEncounter("weight", $(this).html(), id); });
      $('#encounter-sys-saved-'+id).blur(function() { updateSavedPatientEncounter("systolic", $(this).html(), id); });
      $('#encounter-dia-saved-'+id).blur(function() { updateSavedPatientEncounter("diastolic", $(this).html(), id); });
      $('#encounter-hr-saved-'+id).blur(function() { updateSavedPatientEncounter("pulse", $(this).html(), id); });
      $('#encounter-rr-saved-'+id).blur(function() { updateSavedPatientEncounter("respiration", $(this).html(), id); });
      $('#encounter-temp-saved-'+id).blur(function() { updateSavedPatientEncounter("temp", $(this).html(), id); });
    }
    else if (section == 'cc') {
      $('#encounter-pain-scale-'+id).slider({value:encounter.cc.painScale}).on('slide', function(ev){
        $('#encounter-pain-scale-value-'+id).html(this.value)
        updateSavedPatientEncounter("painScale", this.value, id);
      });
      $('#encounter-pain-scale-value-'+id).html(encounter.cc.painScale);
      $('#encounter-chief-complaint-saved-'+id).html(encounter.cc.description);
      $('#encounter-specific-location-saved-'+id).html(encounter.cc.specificLocation);
      util_selectCheckboxesFromList(encounter.cc.occursWhen, 'encounter-occurs-when-'+id);
      $('#encounter-hours-since-saved-'+id).html(encounter.cc.hoursSince);
      $('#encounter-days-since-saved-'+id).html(encounter.cc.daysSince);
      $('#encounter-weeks-since-saved-'+id).html(encounter.cc.weeksSince);
      $('#encounter-months-since-saved-'+id).html(encounter.cc.monthsSince);
      $('#encounter-years-since-saved-'+id).html(encounter.cc.yearsSince);
      $('#encounter-how-long-other-saved-'+id).html(encounter.cc.howLongOther);
      $('#encounter-pain-scale-saved-'+id).html(encounter.cc.painScale);
      $('#encounter-pain-type-saved-'+id).html(encounter.cc.painType);
      $('#encounter-pain-x-hour-saved-'+id).html(encounter.cc.painXHour);
      $('#encounter-pain-x-day-saved-'+id).html(encounter.cc.painXDay);
      $('#encounter-pain-x-week-saved-'+id).html(encounter.cc.painXWeek);
      $('#encounter-pain-x-month-saved-'+id).html(encounter.cc.painXMonth);
      $('#encounter-pain-duration-saved-'+id).html(encounter.cc.painDuration);
      util_selectCheckboxesFromList(encounter.cc.denies, 'encounter-denies-'+id);
      $('#encounter-denies-other-saved-'+id).html(encounter.cc.deniesOther);
      $('#encounter-chief-complaint-saved-'+id).blur(function() { updateSavedPatientEncounter("ccDescription", $(this).html(), id); });
      $('#encounter-specific-location-saved-'+id).blur(function() { updateSavedPatientEncounter("specificLocation", $(this).html(), id); });
      $('#encounter-hours-since-saved-'+id).blur(function() { updateSavedPatientEncounter("hoursSince", $(this).html(), id); });
      $('#encounter-days-since-saved-'+id).blur(function() { updateSavedPatientEncounter("daysSince", $(this).html(), id); });
      $('#encounter-weeks-since-saved-'+id).blur(function() { updateSavedPatientEncounter("weeksSince", $(this).html(), id); });
      $('#encounter-months-since-saved-'+id).blur(function() { updateSavedPatientEncounter("monthsSince", $(this).html(), id); });
      $('#encounter-years-since-saved-'+id).blur(function() { updateSavedPatientEncounter("yearsSince", $(this).html(), id); });
     /* 
      $('#encounter-pain-type-saved-'+id).click(function() { 
        $(this).css({display: "none"});
        $("#encounter-pain-type-"+id).css({display: "block"});
        $("#encounter-pain-type-"+id).val(encounter.cc.painType);
        $("#encounter-pain-type-"+id).change(function() { updateSavedPatientEncounter("painType", $(this).val(), id); });
      });
      */
      
      $('#encounter-chief-complaint-saved-'+id).blur(function() { updateSavedPatientEncounter("ccDescription", $(this).html(), id); });
      $('#encounter-specific-location-saved-'+id).blur(function() { updateSavedPatientEncounter("specificLocation", $(this).html(), id); });
      $('input[name=encounter-occurs-when-'+id+']').click(function() { 
        var occursWhen = $('input[name=encounter-occurs-when-'+id+']:checked').map(function() {return this.value;}).get().join(',');
        updateSavedPatientEncounter("occursWhen", occursWhen, id); 
      });
      $('#encounter-hours-since-location-saved-'+id).blur(function() { updateSavedPatientEncounter("hoursSince", $(this).html(), id); });
      $('#encounter-days-since-location-saved-'+id).blur(function() { updateSavedPatientEncounter("daysSince", $(this).html(), id); });
      $('#encounter-weeks-since-location-saved-'+id).blur(function() { updateSavedPatientEncounter("weeksSince", $(this).html(), id); });
      $('#encounter-months-since-location-saved-'+id).blur(function() { updateSavedPatientEncounter("monthsSince", $(this).html(), id); });
      $('#encounter-years-since-location-saved-'+id).blur(function() { updateSavedPatientEncounter("yearsSince", $(this).html(), id); });
      $('#encounter-how-long-other-saved-'+id).blur(function() { updateSavedPatientEncounter("howLongOther", $(this).html(), id); });
      $('#encounter-pain-x-hour-saved-'+id).blur(function() { updateSavedPatientEncounter("painXHour", $(this).html(), id); });
      $('#encounter-pain-x-day-saved-'+id).blur(function() { updateSavedPatientEncounter("painXDay", $(this).html(), id); });
      $('#encounter-pain-x-week-saved-'+id).blur(function() { updateSavedPatientEncounter("painXWeek", $(this).html(), id); });
      $('#encounter-pain-x-month-saved-'+id).blur(function() { updateSavedPatientEncounter("painXMonth", $(this).html(), id); });
      $('#encounter-pain-duration-saved-'+id).blur(function() { updateSavedPatientEncounter("painDuration", $(this).html(), id); });
      $('input[name=encounter-denies-'+id+']').click(function() { 
        var denies = $('input[name=encounter-denies-'+id+']:checked').map(function() {return this.value;}).get().join(',');
        updateSavedPatientEncounter("denies", denies, id); 
      });
      $('#encounter-denies-other-saved-'+id).blur(function() { updateSavedPatientEncounter("deniesOther", $(this).html(), id); });
    }
    else if (section == 'obgyn') {
      if (!encounter.obgyn) {
        $('#obgyn-form, #obgyn-header').hide();
      }
      else {
        $('#obgyn-form, #obgyn-header').show();
        $(  '#encounter-obgyn-g-saved-'+id).html(encounter.obgyn.g);
        $('#encounter-obgyn-p-saved-'+id).html(encounter.obgyn.p);
        $('#encounter-obgyn-t-saved-'+id).html(encounter.obgyn.t);
        $('#encounter-obgyn-a-saved-'+id).html(encounter.obgyn.a);
        $('#encounter-obgyn-l-saved-'+id).html(encounter.obgyn.l);
        $('input[name=encounter-pregnant-'+id+'][value='+encounter.obgyn.pregStatus+']').attr("checked", true);
        $('input[name=encounter-breastfeeding-'+id+'][value='+encounter.obgyn.breastfeeding+']').attr("checked", true);
        $('#encounter-breastfeeding-months-saved-'+id).html(encounter.obgyn.breastfeedingMonths);
        $('#encounter-last-period-saved-'+id).html(encounter.obgyn.lastPeriod);
        $('#encounter-age-first-period-saved-'+id).html(encounter.obgyn.ageFirstPeriod);
        $('input[name=encounter-pap-smear-'+id+'][value='+encounter.obgyn.papSmearStatus+']').attr("checked", true);
        $('input[name=encounter-birth-control-'+id+'][value='+encounter.obgyn.birthControlStatus+']').attr("checked", true);
        $('#encounter-birth-control-type-saved-'+id).html(encounter.obgyn.birthControlType);
        util_selectCheckboxesFromList(encounter.obgyn.obgynHistory, 'encounter-obgyn-hist-'+id);
        $('#encounter-obgyn-hist-other-saved-'+id).html(encounter.obgyn.obgynHistoryOther);
        $('#encounter-obgyn-g-saved-'+id).blur(function() { updateSavedPatientEncounter("obgynG", $(this).html(), id); });
        $('#encounter-obgyn-p-saved-'+id).blur(function() { updateSavedPatientEncounter("obgynP", $(this).html(), id); });
        $('#encounter-obgyn-t-saved-'+id).blur(function() { updateSavedPatientEncounter("obgynT", $(this).html(), id); });
        $('#encounter-obgyn-a-saved-'+id).blur(function() { updateSavedPatientEncounter("obgynA", $(this).html(), id); });
        $('#encounter-obgyn-l-saved-'+id).blur(function() { updateSavedPatientEncounter("obgynL", $(this).html(), id); });
        $('input[name=encounter-pregnant-'+id+']').click(function() { updateSavedPatientEncounter("pregStatus", $(this).val(), id); });
        $('input[name=encounter-breastfeeding-'+id+']').click(function() { updateSavedPatientEncounter("breastfeeding", $(this).val(), id); });
        $('#encounter-breastfeeding-months-saved-'+id).blur(function() { updateSavedPatientEncounter("breastfeedingMonths", $(this).html(), id); });
        $('#encounter-last-period-saved-'+id).blur(function() { updateSavedPatientEncounter("lastPeriod", $(this).html(), id); });
        $('#encounter-age-first-period-saved-'+id).blur(function() { updateSavedPatientEncounter("ageFirstPeriod", $(this).html(), id); });
        $('input[name=encounter-pap-smear-'+id+']').click(function() { updateSavedPatientEncounter("papSmearStatus", $(this).val(), id); });
        $('input[name=encounter-birth-control-'+id+']').click(function() { updateSavedPatientEncounter("birthControlStatus", $(this).val(), id); });
        $('#encounter-birth-control-type-saved-'+id).blur(function() { updateSavedPatientEncounter("birthControlType", $(this).html(), id); });
        $('input[name=encounter-obgyn-hist-'+id+']').click(function() { 
          var obgynHist = $('input[name=encounter-obgyn-hist-'+id+']:checked').map(function() {return this.value;}).get().join(',');
          updateSavedPatientEncounter("obgynHistory", obgynHist, id); 
        });
        $('#encounter-obgyn-hist-other-saved-'+id).blur(function() { updateSavedPatientEncounter("obgynHistoryOther", $(this).html(), id); });
      }
    }
    else if (section == 'pfsh') {
      $('#encounter-num-residents-saved-'+id).html(encounter.patient.pfsh.numResidents);
      $('#encounter-job-type-saved-'+id).html(encounter.patient.pfsh.jobType);
      $('input[name=encounter-mother-alive-'+id+'][value='+encounter.patient.pfsh.motherAlive+']').attr("checked", true);
      $('#encounter-mother-death-reason-saved-'+id).html(encounter.patient.pfsh.motherDeathReason);
      $('input[name=encounter-father-alive-'+id+'][value='+encounter.patient.pfsh.fatherAlive+']').attr("checked", true);
      $('#encounter-father-death-reason-saved-'+id).html(encounter.patient.pfsh.fatherDeathReason);
      $('input[name=encounter-partner-alive-'+id+'][value='+encounter.patient.pfsh.partnerAlive+']').attr("checked", true);
      $('#encounter-partner-death-reason-saved-'+id).html(encounter.patient.pfsh.partnerDeathReason);
      $('#encounter-num-siblings-saved-'+id).html(encounter.patient.pfsh.numSiblings);
      $('#encounter-num-brothers-saved-'+id).html(encounter.patient.pfsh.numBrothers);
      $('#encounter-num-sisters-saved-'+id).html(encounter.patient.pfsh.numSisters);
      $('#encounter-num-children-saved-'+id).html(encounter.patient.pfsh.numChildren);
      $('#encounter-num-sons-saved-'+id).html(encounter.patient.pfsh.numSons);
      $('#encounter-num-daughters-saved-'+id).html(encounter.patient.pfsh.numDaughters);
      $('#encounter-num-residents-saved-'+id).blur(function() { updateSavedPatientEncounter("numResidents", $(this).html(), id); });
      $('#encounter-job-type-saved-'+id).blur(function() { updateSavedPatientEncounter("jobType", $(this).html(), id); });
      $('input[name=encounter-mother-alive-'+id+']').click(function() { updateSavedPatientEncounter("motherAlive", $(this).val() == 'true', id); });
      $('#encounter-mother-death-reason-saved-'+id).blur(function() { updateSavedPatientEncounter("motherDeathReason", $(this).html(), id); });
      $('input[name=encounter-father-alive-'+id+']').click(function() { updateSavedPatientEncounter("fatherAlive", $(this).val() == 'true', id); });
      $('#encounter-father-death-reason-saved-'+id).blur(function() { updateSavedPatientEncounter("fatherDeathReason", $(this).html(), id); });
      $('input[name=encounter-partner-alive-'+id+']').click(function() { updateSavedPatientEncounter("partnerAlive", $(this).val() == 'true', id); });
      $('#encounter-partner-death-reason-saved-'+id).blur(function() { updateSavedPatientEncounter("partnerDeathReason", $(this).html(), id); });
      $('#encounter-num-siblings-saved-'+id).blur(function() { updateSavedPatientEncounter("numSiblings", $(this).html(), id); });
      $('#encounter-num-brothers-saved-'+id).blur(function() { updateSavedPatientEncounter("numBrothers", $(this).html(), id); });
      $('#encounter-num-sisters-saved-'+id).blur(function() { updateSavedPatientEncounter("numSisters", $(this).html(), id); });
      $('#encounter-num-children-saved-'+id).blur(function() { updateSavedPatientEncounter("numChildren", $(this).html(), id); });
      $('#encounter-num-sons-saved-'+id).blur(function() { updateSavedPatientEncounter("numSons", $(this).html(), id); });
      $('#encounter-num-daughters-saved-'+id).blur(function() { updateSavedPatientEncounter("numDaughters", $(this).html(), id); });
    }
    else if (section == 'supp') {
      $('#encounter-num-cups-water-saved-'+id).html(encounter.supp.numCupsWater);
      $('#encounter-num-cups-coffee-saved-'+id).html(encounter.supp.numCupsCoffee);
      $('#encounter-num-cups-tea-saved-'+id).html(encounter.supp.numCupsTea);
      $('#encounter-water-source-saved-'+id).html(encounter.supp.waterSource);
      $('#encounter-num-cups-water-saved-'+id).blur(function() { updateSavedPatientEncounter("numCupsWater", $(this).html(), id); });
      $('#encounter-num-cups-coffee-saved-'+id).blur(function() { updateSavedPatientEncounter("numCupsCoffee", $(this).html(), id); });
      $('#encounter-num-cups-tea-saved-'+id).blur(function() { updateSavedPatientEncounter("numCupsTea", $(this).html(), id); });
      
      $('#encounter-water-source-saved-'+id).click(function() { 
        $(this).css({display: "none"});
        $("#encounter-water-source-"+id).css({display: "block"});
        $("#encounter-water-source-"+id).val(encounter.supp.waterSource);
        $("#encounter-water-source-"+id).change(function() { updateSavedPatientEncounter("waterSource", $(this).val(), id); });
      });
      
      RenderUtil.render('component/encounter_questions', {encounter:encounter}, function(s) { 
        $("#encounter-questions-"+id).html(s); 
        setEncounterFormMode(encounter, section, savedState, hasOwnership);
        $('.encounter-question-editable').blur(function(e) { 
          getCurrentQuestionId(e);
          updateEncounterQuestion("question", $(this).html(), app_currentQuestionId); 
        });
        $('.encounter-response-editable').blur(function(e) { 
          getCurrentQuestionId(e);
          updateEncounterQuestion("response", $(this).html(), app_currentQuestionId); 
         });
      });
      
      $('#encounter-supp-new-question-'+id).click(function() { 
        var jsonData = JSON.stringify({sessionId: clinician.sessionId, encounterId: id});
        $.post("patient/addEncounterQuestion", {data:jsonData}, function(data) {
          var parsedData = $.parseJSON(data);
          var encounterQuestionId = parsedData.encounterQuestionId;
          var numQuestions = $("#encounter-questions-"+id).children().length + 2;
          RenderUtil.render('component/encounter_question', {ordinal:numQuestions, id: encounterQuestionId}, function(s) { 
            $("#encounter-questions-"+id).append(s); 
            setEncounterFormMode(encounter, section, savedState, hasOwnership);
            $('.encounter-question-editable').blur(function(e) { 
              getCurrentQuestionId(e);
              updateEncounterQuestion("question", $(this).html(), encounterQuestionId); 
            });
            $('.encounter-response-editable').blur(function(e) { 
              getCurrentQuestionId(e);
              updateEncounterQuestion("response", $(this).html(), encounterQuestionId); 
             });
           });
       });
      });
    }
    else if (section == 'hist') {
      RenderUtil.render('component/encounter_medications', {encounter:encounter}, function(s) { 
        $("#encounter-medications-"+id).html(s); 
        setEncounterFormMode(encounter, section, savedState, hasOwnership);
        $('.encounter-med-editable').blur(function(e) { 
          getCurrentMedicationId(e);
          updateEncounterMedication("medication", $(this).html(), app_currentMedicationId); 
        });
        $('.encounter-dose-editable').blur(function(e) { 
          getCurrentMedicationId(e);
          updateEncounterMedication("dose", $(this).html(), app_currentMedicationId); 
        });
        $('.encounter-freq-editable').blur(function(e) { 
          getCurrentMedicationId(e);
          updateEncounterMedication("frequency", $(this).html(), app_currentMedicationId); 
        });
      });
      
      $('#encounter-past-s-m-saved-'+id).html(encounter.patient.hist.pastSM);
      util_selectCheckboxesFromList(encounter.patient.hist.famHist, 'encounter-fam-hist-'+id);
      $('#encounter-fam-hist-notes-saved-'+id).html(encounter.patient.hist.famHistNotes);
      $('#encounter-fam-hist-other-saved-'+id).html(encounter.patient.hist.famHistOther);
      $('#encounter-allerg-food-saved-'+id).html(encounter.patient.hist.allergFood);
      $('#encounter-allerg-drug-saved-'+id).html(encounter.patient.hist.allergDrug);
      $('#encounter-allerg-env-saved-'+id).html(encounter.patient.hist.allergEnv);
      $('input[name=encounter-vacc-'+id+'][value='+encounter.patient.hist.vacc+']').attr("checked", true);
      $('#encounter-vacc-notes-saved-'+id).html(encounter.patient.hist.vaccNotes);
      util_selectCheckboxesFromList(encounter.patient.hist.subst, 'encounter-subst-'+id);
      $('#encounter-smoke-pks-day-saved-'+id).html(encounter.patient.hist.smokePksDay);
      $('#encounter-years-smoked-saved-'+id).html(encounter.patient.hist.yearsSmoke);
      $('#encounter-smoke-years-quit-saved-'+id).html(encounter.patient.hist.smokeYearsQuit);
      $('#encounter-etoh-units-week-saved-'+id).html(encounter.patient.hist.etohUnitsWeek);
      $('#encounter-current-drugs-saved-'+id).html(encounter.patient.hist.currentDrugs);
      $('#encounter-hist-new-medication-'+id).click(function() { 
        var jsonData = JSON.stringify({sessionId: clinician.sessionId, patientId: encounter.patient.id});
        $.post("patient/addEncounterMedication", {data:jsonData}, function(data) {
          var parsedData = $.parseJSON(data);
          var encounterMedicationId = parsedData.encounterMedicationId;
          var numMedications = $("#encounter-medications-"+id).children().length + 2;
          RenderUtil.render('component/encounter_medication', {ordinal:numMedications, id: encounterMedicationId}, function(s) { 
            $("#encounter-medications-"+id).append(s); 
            setEncounterFormMode(encounter, section, savedState, hasOwnership);
            $('.encounter-med-editable').blur(function(e) { 
              getCurrentMedicationId(e);
              updateEncounterMedication("medication", $(this).html(), encounterMedicationId); 
            });
            $('.encounter-dose-editable').blur(function(e) { 
              getCurrentQuestionId(e);
              updateEncounterMedication("dose", $(this).html(), encounterMedicationId); 
             });
             $('.encounter-freq-editable').blur(function(e) { 
              getCurrentQuestionId(e);
              updateEncounterMedication("frequency", $(this).html(), encounterMedicationId); 
             });
           });
       });
      });
      $('#encounter-past-s-m-saved-'+id).blur(function() { updateSavedPatientEncounter("pastSM", $(this).html(), id); });
      $('input[name=encounter-fam-hist-'+id+']').click(function() { 
        var famHist = $('input[name=encounter-fam-hist-'+id+']:checked').map(function() {return this.value;}).get().join(',');
        updateSavedPatientEncounter("famHist", famHist, id); 
      });
      $('#encounter-fam-hist-notes-saved-'+id).blur(function() { updateSavedPatientEncounter("famHistNotes", $(this).html(), id); });
      $('#encounter-fam-hist-other-saved-'+id).blur(function() { updateSavedPatientEncounter("famHistOther", $(this).html(), id); });
      $('#encounter-allerg-food-saved-'+id).blur(function() { updateSavedPatientEncounter("allergFood", $(this).html(), id); });
      $('#encounter-allerg-drug-saved-'+id).blur(function() { updateSavedPatientEncounter("allergDrug", $(this).html(), id); });
      $('#encounter-allerg-env-saved-'+id).blur(function() { updateSavedPatientEncounter("allergEnv", $(this).html(), id); });
      $('input[name=encounter-vacc-'+id+']').click(function() { updateSavedPatientEncounter("vacc", $(this).val() == 'true', id); });
      $('#encounter-vacc-notes-saved-'+id).blur(function() { updateSavedPatientEncounter("vaccNotes", $(this).html(), id); });
      $('input[name=encounter-subst-'+id+']').click(function() { 
        var subst = $('input[name=encounter-subst-'+id+']:checked').map(function() {return this.value;}).get().join(',');
        updateSavedPatientEncounter("subst", subst, id); 
      });
      $('#encounter-smoke-pks-day-saved-'+id).blur(function() { updateSavedPatientEncounter("smokePksDay", $(this).html(), id); });
      $('#encounter-years-smoked-saved-'+id).blur(function() { updateSavedPatientEncounter("yearsSmoked", $(this).html(), id); });
      $('#encounter-smoke-years-quit-saved-'+id).blur(function() { updateSavedPatientEncounter("smokeYearsQuit", $(this).html(), id); });
      $('#encounter-etoh-units-week-saved-'+id).blur(function() { updateSavedPatientEncounter("etohUnitsWeek", $(this).html(), id); });
      $('#encounter-current-drugs-saved-'+id).blur(function() { updateSavedPatientEncounter("currentDrugs", $(this).html(), id); });
    }
    else if (section == 'exam') {
  /*
      var canvas = document.getElementById('encounter-exam-image-'+id);
      var context = canvas.getContext('2d');
      var baseImage = new Image();
      baseImage.src = 'images/exam.png';
      baseImage.onload = function() {
        context.drawImage(baseImage, 0, 0, 300, 150);
      }
  */
      initEncounterExamCanvas(id);
        
      $('#encounter-hs-saved-'+id).html(encounter.exam.hs);
      $('input[name=encounter-heart-rhythm-'+id+'][value='+encounter.exam.heartRhythm+']').attr("checked", true);
      $('#encounter-lab-hb-saved-'+id).html(encounter.exam.hb);
      $('#encounter-lab-glucose-saved-'+id).html(encounter.lab.glucose);
      $('#encounter-lab-urine-dip-saved-'+id).html(encounter.lab.urineDip);
      $('#encounter-diagnosis-saved-'+id).html(encounter.exam.diagnosis);
      $('#encounter-dx-code-saved-'+id).html(encounter.exam.dxCode);
      $('#encounter-treatment-plan-saved-'+id).html(encounter.exam.treatmentPlan);
      $('#encounter-tx-code-saved-'+id).html(encounter.exam.txCode);
      $('#encounter-provider-name-saved-'+id).html(encounter.exam.providerName);
      $('#encounter-hs-saved-'+id).blur(function() { updateSavedPatientEncounter("hs", $(this).html(), id); });
      $('input[name=encounter-heart-rhythm-'+id+']').click(function() { updateSavedPatientEncounter("heartRhythm", $(this).val(), id); });
      $('#encounter-lab-hb-saved-'+id).blur(function() { updateSavedPatientEncounter("hb", $(this).html(), id); });
      $('#encounter-lab-glucose-saved-'+id).blur(function() { updateSavedPatientEncounter("glucose", $(this).html(), id); });
      $('#encounter-lab-urine-dip-saved-'+id).blur(function() { updateSavedPatientEncounter("urineDip", $(this).html(), id); });
      $('#encounter-diagnosis-saved-'+id).blur(function() { updateSavedPatientEncounter("diagnosis", $(this).html(), id); });
      $('#encounter-dx-code-saved-'+id).blur(function() { updateSavedPatientEncounter("dxCode", $(this).html(), id); });
      $('#encounter-treatment-plan-saved-'+id).blur(function() { updateSavedPatientEncounter("treatmentPlan", $(this).html(), id); });
      $('#encounter-tx-code-saved-'+id).blur(function() { updateSavedPatientEncounter("txCode", $(this).html(), id); });
      $('#encounter-provider-name-saved-'+id).blur(function() { updateSavedPatientEncounter("providerName", $(this).html(), id); });
    }
    else if (section == 'follow-up') {
      $('input[name=encounter-follow-up-level-'+id+'][value='+encounter.followUp.level+']').attr("checked", true);
      $('#encounter-follow-up-when-saved-'+id).html(encounter.followUp.when);
      $('#encounter-follow-up-condition-saved-'+id).html(encounter.followUp.condition);
      $('#encounter-follow-up-dispense-rx-saved-'+id).html(encounter.followUp.dispenseRx);
      $('#encounter-follow-up-uss-saved-'+id).html(encounter.followUp.USS);
      $('#encounter-follow-up-pregnant-saved-'+id).html(encounter.followUp.pregnant);
      $('#encounter-follow-up-wound-care-saved-'+id).html(encounter.followUp.woundCare);
      $('#encounter-follow-up-ref-to-specialist-saved-'+id).html(encounter.followUp.refToSpecialist);
      $('#encounter-follow-up-dental-list-saved-'+id).html(encounter.followUp.dentalList);
      $('#encounter-follow-up-physiotherapy-saved-'+id).html(encounter.followUp.physiotherapy);
      $('#encounter-follow-up-blood-labs-saved-'+id).html(encounter.followUp.bloodLabs);
      $('#encounter-follow-up-other-saved-'+id).html(encounter.followUp.other);
      $('#encounter-follow-up-pulmonary-fx-test-saved-'+id).html(encounter.followUp.pulmonaryFXTest);
      $('#encounter-follow-up-vision-saved-'+id).html(encounter.followUp.vision);
      $('input[name=encounter-follow-up-completed-'+id+'][value='+encounter.followUp.followUpCompleted+']').attr("checked", true) == 'true';
      encounter.followUp.followUpDate = util_processDate('#encounter-follow-up-date-saved-'+id, encounter.followUp.followUpDate);
      $('#encounter-follow-up-notes-saved-'+id).html(encounter.followUp.followUpNotes);
      $('input[name=encounter-follow-up-level-'+id+']').click(function() { updateSavedPatientEncounter("followUpLevel", $(this).val(), id); });
      $('#encounter-follow-up-when-saved-'+id).blur(function() { updateSavedPatientEncounter("followUpWhen", $(this).html(), id); });
      $('#encounter-follow-up-condition-saved-'+id).blur(function() { updateSavedPatientEncounter("followUpCondition", $(this).html(), id); });
      $('#encounter-follow-up-dispense-rx-saved-'+id).blur(function() { updateSavedPatientEncounter("followUpDispenseRx", $(this).html(), id); });
      $('#encounter-follow-up-uss-saved-'+id).blur(function() { updateSavedPatientEncounter("followUpUSS", $(this).html(), id); });
      $('#encounter-follow-up-pregnant-saved-'+id).blur(function() { updateSavedPatientEncounter("followUpPregnant", $(this).html(), id); });
      $('#encounter-follow-up-wound-care-saved-'+id).blur(function() { updateSavedPatientEncounter("followUpWoundCare", $(this).html(), id); });
      $('#encounter-follow-up-ref-to-specialist-saved-'+id).blur(function() { updateSavedPatientEncounter("followUpRefToSpecialist", $(this).html(), id); });
      $('#encounter-follow-up-dental-list-saved-'+id).blur(function() { updateSavedPatientEncounter("followUpDentalList", $(this).html(), id); });
      $('#encounter-follow-up-physiotherapy-saved-'+id).blur(function() { updateSavedPatientEncounter("followUpPhysiotherapy", $(this).html(), id); });
      $('#encounter-follow-up-blood-labs-saved-'+id).blur(function() { updateSavedPatientEncounter("followUpBloodLabs", $(this).html(), id); });
      $('#encounter-follow-up-other-saved-'+id).blur(function() { updateSavedPatientEncounter("followUpOther", $(this).html(), id); });
      $('#encounter-follow-up-pulmonary-fx-test-saved-'+id).blur(function() { updateSavedPatientEncounter("followUpPulmonaryFXTest", $(this).html(), id); });
      $('#encounter-follow-up-vison-saved-'+id).blur(function() { updateSavedPatientEncounter("followUpVision", $(this).html(), id); });
      $('input[name=encounter-follow-up-completed-'+id+']').click(function() { updateSavedPatientEncounter("followUpCompleted", $(this).val() == 'true', id); });
      $('#encounter-follow-up-date-saved-'+id).blur(function() { updateSavedPatientEncounter("followUpDate", $(this).html(), id); });
      $('#encounter-follow-up-notes-saved-'+id).blur(function() { updateSavedPatientEncounter("followUpNotes", $(this).html(), id); });
    } 
   
    $('.dual-mode-saved').off().on('click', function(event) { 
      $(this).css({display: "none"});
      var thisItem = this;
      var savedId = this.id;
      var unsavedId = savedId.replace('-saved','');
      var currentValue = this.innerHTML;
      $('#'+unsavedId).val(currentValue);
      $('#'+unsavedId).css({display: "block"});
      var name = $('#'+unsavedId).attr('name');
      $('#'+unsavedId).change(function() { updateSavedPatientEncounter(name, $(this).val(), id, true, savedId); });
    });
  }
}



function saveVitalsEncounterForm(encounter) {
  var id = encounter.id;  
  encounter.vitals.height = util_processNumber("#encounter-height-"+id, encounter.vitals.height);
  encounter.vitals.weight = util_processNumber("#encounter-weight-"+id, encounter.vitals.weight);
  encounter.vitals.systolic = util_processNumber("#encounter-sys-"+id, encounter.vitals.systolic);
  encounter.vitals.diastolic = util_processNumber("#encounter-dia-"+id, encounter.vitals.diastolic);
  encounter.vitals.pulse = util_processNumber("#encounter-hr-"+id, encounter.vitals.pulse);
  encounter.vitals.respiration = util_processNumber("#encounter-rr-"+id, encounter.vitals.respiration);
  encounter.vitals.temperature = util_processNumber("#encounter-temp-"+id, encounter.vitals.temperature);
  encounter.vitalsSaved = true;
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createVitals", {data:jsonData}, function(data) {
    renderEncounterFormSection (encounter, 'vitals', true, true);
  });
}


function saveCCEncounterForm(encounter) {
  var id = encounter.id;  
  encounter.cc.description = $.trim($("#encounter-chief-complaint-"+id).val());
  encounter.cc.occursWhen = $('input[name="encounter-occurs-when-'+id+'"]:checked').map(function() {return this.value;}).get().join(',');
  encounter.cc.occursOther = $.trim($("#encounter-occurs-other-"+id).val());
  encounter.cc.specificLocation = $.trim($("#encounter-specific-location-"+id).val());
  encounter.cc.painType = $.trim($("#encounter-pain-type-"+id).val());
  encounter.cc.denies = $('input[name="encounter-denies-'+id+'"]:checked').map(function() {return this.value;}).get().join(',');
  encounter.cc.deniesOther = $.trim($("#encounter-denies-other-"+id).val());
  encounter.cc.howLongOther = $.trim($("#encounter-how-long-other-"+id).val());
  encounter.cc.painDuration = $.trim($("#encounter-pain-duration-"+id).val());
  encounter.cc.hoursSince = util_processNumber("#encounter-hours-since-"+id, encounter.cc.hoursSince);
  encounter.cc.daysSince = util_processNumber("#encounter-days-since-"+id, encounter.cc.daysSince);
  encounter.cc.weeksSince = util_processNumber("#encounter-weeks-since-"+id, encounter.cc.weeksSince);
  encounter.cc.monthsSince = util_processNumber("#encounter-months-since-"+id, encounter.cc.monthsSince);
  encounter.cc.yearsSince = util_processNumber("#encounter-years-since-"+id, encounter.cc.yearsSince);
  encounter.cc.painScale = util_processNumber("#encounter-pain-scale-"+id, encounter.cc.painScale);
  encounter.cc.painXHour = util_processNumber("#encounter-pain-x-hour-"+id, encounter.cc.painXHour);
  encounter.cc.painXDay = util_processNumber("#encounter-pain-x-day-"+id, encounter.cc.painXDay);
  encounter.cc.painXWeek = util_processNumber("#encounter-pain-x-week-"+id, encounter.cc.painXWeek);
  encounter.cc.painXMonth = util_processNumber("#encounter-pain-x-month-"+id, encounter.cc.painXMonth);
  encounter.ccSaved = true;
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createCC", {data:jsonData}, function(data) {
    renderEncounterFormSection (encounter, 'cc', true, true);
  });
}


function saveSOAPNoteEncounterForm(encounter) {
  var id = encounter.id;  
  encounter.soapNote.subjective = $.trim($("#encounter-soap-note-subjective-"+id).val()); 
  encounter.soapNote.objective = $.trim($("#encounter-soap-note-objective-"+id).val()); 
  encounter.soapNote.assessment = $.trim($("#encounter-soap-note-assessment-"+id).val()); 
  encounter.soapNote.plan = $.trim($("#encounter-soap-note-plan-"+id).val()); 
  encounter.soapNoteSaved = true;
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter,
    soapNoteSaved: true
  });
  $.post("patient/createSOAPNote", {data:jsonData}, function(data) {
    renderEncounterFormSection (encounter, 'soap-note', true, true);
  });
}


function saveOBGYNEncounterForm(encounter) {
  var id = encounter.id;  
  encounter.obgyn.g = $.trim($("#encounter-obgyn-g-"+id).val());
  encounter.obgyn.p = $.trim($("#encounter-obgyn-p-"+id).val());
  encounter.obgyn.t = $.trim($("#encounter-obgyn-t-"+id).val());
  encounter.obgyn.a = $.trim($("#encounter-obgyn-a-"+id).val());
  encounter.obgyn.l = $.trim($("#encounter-obgyn-l-"+id).val());
  encounter.obgyn.pregStatus = $('input[name=encounter-pregnant-'+id+']:checked').val();
  encounter.obgyn.breastfeeding = $('input[name=encounter-breastfeeding-'+id+']:checked').val();
  encounter.obgyn.breastfeedingMonths = $.trim($("#encounter-breastfeeding-months-"+id).val());
  encounter.obgyn.lastPeriod = $.trim($("#encounter-last-period-"+id).val());
  encounter.obgyn.ageFirstPeriod = $.trim($("#encounter-age-first-period-"+id).val());
  encounter.obgyn.papSmearStatus = $('input[name=encounter-pap-smear-'+id+']:checked').val();
  encounter.obgyn.birthControlStatus = $('input[name=encounter-birth-control-'+id+']:checked').val();
  encounter.obgyn.birthControlType = $.trim($("#encounter-birth-control-type-"+id).val());
  encounter.obgyn.history = $('input[name="encounter-obgyn-hist-'+id+'"]:checked').map(function() {return this.value;}).get().join(',');
  encounter.obgyn.historyOther = $.trim($("#encounter-obgyn-hist-other-"+id).val());
  encounter.obgynSaved = true;
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createOBGYN", {data:jsonData}, function(data) {
    renderEncounterFormSection (encounter, 'obgyn', true, true);
  });
}


function savePFSHEncounterForm(encounter) {
  var id = encounter.id;  
  encounter.patient.pfsh.jobType = $.trim($("#encounter-job-type-"+id).val());
  encounter.patient.pfsh.motherAlive = $('input[name=encounter-mother-alive-'+id+']:checked').val() == 'true';
  encounter.patient.pfsh.motherDeathReason = $.trim($("#encounter-mother-death-reason-"+id).val());
  encounter.patient.pfsh.fatherAlive = $('input[name=encounter-father-alive-'+id+']:checked').val() == 'true';
  encounter.patient.pfsh.fatherDeathReason = $.trim($("#encounter-mother-death-reason-"+id).val());
  encounter.patient.pfsh.partnerAlive = $('input[name=encounter-partner-alive-'+id+']:checked').val() == 'true';
  encounter.patient.pfsh.partnerDeathReason = $.trim($("#encounter-partner-death-reason-"+id).val());
  encounter.patient.pfsh.numResidents = util_processNumber("#encounter-num-residents-"+id, encounter.patient.pfsh.numResidents);
  encounter.patient.pfsh.numSiblings = util_processNumber("#encounter-num-siblings-"+id, encounter.patient.pfsh.numSiblings);
  encounter.patient.pfsh.numBrothers = util_processNumber("#encounter-num-brothers-"+id, encounter.patient.pfsh.numBrothers);
  encounter.patient.pfsh.numSisters = util_processNumber("#encounter-num-sisters-"+id, encounter.patient.pfsh.numSisters);
  encounter.patient.pfsh.numChildren = util_processNumber("#encounter-num-children-"+id, encounter.patient.pfsh.numChildren);
  encounter.patient.pfsh.numSons = util_processNumber("#encounter-num-sons-"+id, encounter.patient.pfsh.numSons);
  encounter.patient.pfsh.numDaughters = util_processNumber("#encounter-num-daughters-"+id, encounter.patient.pfsh.numDaughters);
  encounter.pfshSaved = true;
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createPFSH", {data:jsonData}, function(data) {
    renderEncounterFormSection (encounter, 'pfsh', true, true);
  });
}


function saveSuppEncounterForm(encounter) {
  var id = encounter.id;  
  encounter.supp.waterSource = $.trim($("#encounter-water-source-"+id).val());
  encounter.supp.numCupsCoffee = util_processNumber("#encounter-num-cups-coffee"+id, encounter.supp.numCupsCoffee);
  encounter.supp.numCupsTea = util_processNumber("#encounter-num-cups-tea-"+id, encounter.supp.numCupsTea);
  encounter.supp.numCupsWater = util_processNumber("#encounter-num-cups-water-"+id, encounter.supp.numCupsWater);
  
  for (i=0;i<encounter.supp.encounterQuestionList.length;i++) { 
    var questionId = encounter.supp.encounterQuestionList[i].id;
    encounter.supp.encounterQuestionList[i].question = $("#encounter-question-"+questionId).val();
    encounter.supp.encounterQuestionList[i].response = $("#encounter-response-"+questionId).val();
  }
  encounter.suppSaved = true;
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createSupp", {data:jsonData}, function(data) {
    renderEncounterFormSection (encounter, 'supp', true, true);
  });
}


function saveHistEncounterForm(encounter) {
  var id = encounter.id;  
  for (i=0;i<encounter.patient.hist.encounterMedicationList.length;i++) { 
    var medicationId = encounter.patient.hist.encounterMedicationList[i].id;
    encounter.patient.hist.encounterMedicationList[i].medication = $("#encounter-med-"+medicationId).val();
    encounter.patient.hist.encounterMedicationList[i].dose = $("#encounter-dose-"+medicationId).val();
    encounter.patient.hist.encounterMedicationList[i].frequency = $("#encounter-freq-"+medicationId).val();
  }
  encounter.patient.hist.pastSM = $.trim($("#encounter-past-s-m-"+id).val());
  encounter.patient.hist.famHist = $('input[name="encounter-fam-hist-'+id+'"]:checked').map(function() {return this.value;}).get().join(',');
  encounter.patient.hist.famHistOther = $.trim($("#encounter-fam-hist-other-"+id).val());
  encounter.patient.hist.famHistNotes = $.trim($("#encounter-fam-hist-notes-"+id).val());
  encounter.patient.hist.allergFood = $.trim($("#encounter-allerg-food-"+id).val());
  encounter.patient.hist.allergDrug = $.trim($("#encounter-allerg-drug-"+id).val());
  encounter.patient.hist.allergEnv = $.trim($("#encounter-allerg-env-"+id).val());
  encounter.patient.hist.vacc = $('input[name=encounter-vacc-'+id+']:checked').val() == 'true';
  encounter.patient.hist.vaccNotes = $.trim($("#encounter-vacc-notes-"+id).val());
  encounter.patient.hist.subst = $('input[name="encounter-subst-'+id+'"]:checked').map(function() {return this.value;}).get().join(',');
  encounter.patient.hist.currentDrugs = $.trim($("#encounter-current-drugs-"+id).val());
  encounter.patient.hist.smokePksDay = util_processNumber("#encounter-smoke-pks-day-"+id, encounter.patient.hist.smokePksDay);
  encounter.patient.hist.yearsSmoked = util_processNumber("#encounter-years-smoked-"+id, encounter.patient.hist.yearsSmoked);
  encounter.patient.hist.smokeYearsQuit = util_processNumber("#encounter-smoke-years-quit-"+id, encounter.patient.hist.smokeYearsQuit);
  encounter.patient.hist.etohUnitsWeek = util_processNumber("#encounter-etoh-units-week-"+id, encounter.patient.hist.etohUnitsWeek);
  encounter.histSaved = true;
  jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createHist", {data:jsonData}, function(data) {
    renderEncounterFormSection (encounter, 'hist', true, true);
  });
}


function saveExamEncounterForm(encounter) {
  var id = encounter.id;  
  encounter.exam.hs = $.trim($("#encounter-hs-"+id).val());
  encounter.exam.heartRhythm = $('input[name=encounter-heart-rhythm-'+id+']:checked').val();
  encounter.exam.hb = $.trim($("#encounter-lab-hb-"+id).val());
  encounter.exam.glucose = $.trim($("#encounter-lab-glucose-"+id).val());
  encounter.exam.urineDIP = $.trim($("#encounter-lab-urine-dip-"+id).val());
  encounter.exam.diagnosis = $.trim($("#encounter-diagnosis-"+id).val());
  encounter.exam.dxCode = $.trim($("#encounter-dx-code-"+id).val());
  encounter.exam.treatmentPlan = $.trim($("#encounter-treatment-plan-"+id).val());
  encounter.exam.txCode = $.trim($("#encounter-tx-code-"+id).val());
  encounter.exam.providerName = $.trim($("#encounter-provider-name-"+id).val());
  encounter.examSaved = true;
  jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createExam", {data:jsonData}, function(data) {
    renderEncounterFormSection (encounter, 'exam', true, true);
  });
}

function saveFollowUpEncounterForm(encounter) {
  var id = encounter.id;  
  
  encounter.followUp.level = $('input[name=encounter-follow-up-level-'+id+']:checked').val();
  encounter.followUp.when = $.trim($("#encounter-follow-up-when-"+id).val());
  encounter.followUp.condition = $.trim($("#encounter-follow-up-condition-"+id).val());
  encounter.followUp.dispenseRx = $.trim($("#encounter-follow-up-dispense-rx-"+id).val());
  encounter.followUp.USS = $.trim($("#encounter-follow-up-uss-"+id).val());
  encounter.followUp.pregnant = $.trim($("#encounter-follow-up-pregnant-"+id).val());
  encounter.followUp.woundCare = $.trim($("#encounter-follow-up-wound-care-"+id).val());
  encounter.followUp.refToSpecialist = $.trim($("#encounter-follow-up-ref-to-specialist-"+id).val());
  encounter.followUp.dentalList = $.trim($("#encounter-follow-up-dental-list-"+id).val());
  encounter.followUp.physiotherapy = $.trim($("#encounter-follow-up-physiotherapy-"+id).val());
  encounter.followUp.bloodLabs = $.trim($("#encounter-follow-up-blood-labs-"+id).val());
  encounter.followUp.other = $.trim($("#encounter-follow-up-other-"+id).val());
  encounter.followUp.pulmonaryFXTest = $.trim($("#encounter-follow-up-pulmonary-fx-test-"+id).val());
  encounter.followUp.vision = $.trim($("#encounter-follow-up-vision-"+id).val());
  encounter.followUp.completed = $('input[name=encounter-follow-up-completed--'+id+']:checked').val() == 'true';
  encounter.followUp.notes = $.trim($("#encounter-follow-up-notes-"+id).val());
  encounter.followUp.followUpDate = util_processDate('#encounter-follow-up-date-saved-'+id, encounter.followUp.followUpDate);
  encounter.followUpSaved = true;
  jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounter: encounter
  });
  $.post("patient/createFollowUp", {data:jsonData}, function(data) {
    renderEncounterFormSection (encounter, 'follow-up', true, true);
  });
}


function updateLocalPatientEncounter(property, value, patientId) {
  app_currentEncounter[property] = value;  
}

function updateSavedPatientEncounter(property, value, encounterId, isDualMode, elementId) {
  var encounter = app_currentEncounter;
  if (encounter == undefined) {
    encounter = getPatientEncounter(encounterId);
  }
  updateLocalPatientEncounter(property, value, encounter.patient.id);
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    patientId: encounter.patient.id,
    updateProperty:property,
    updatePropertyValue:value
  });
  $.post("patient/updatePatient", {data:jsonData}, function(data) {
    if (isDualMode) {
      var unsavedId = elementId.replace('-saved','');
      $('#'+unsavedId).css({display: "none"});
      $('#'+elementId).html(value);
      $('#'+elementId).css({display: "block"});
    }
  }); 
}

function updateEncounterQuestion(property, value, encounterQuestionId) {
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounterQuestionId: encounterQuestionId,
    updateProperty:property,
    updatePropertyValue:value
  });
  $.post("patient/updateEncounterQuestion", {data:jsonData}, function(data) {
  }); 
}

function updateEncounterMedication(property, value, encounterMedicationId) {
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    encounterMedicationId: encounterMedicationId,
    updateProperty:property,
    updatePropertyValue:value
  });
  $.post("patient/updateEncounterMedication", {data:jsonData}, function(data) {
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

function initEncounterExamCanvas(id) {
  canvas = document.getElementById('encounter-exam-image-overlay-'+id);
  ctx = canvas.getContext("2d");
  w = canvas.width;
  h = canvas.height;
  canvas.addEventListener("mousemove", function (e) { findEncounterExamCanvasXY('move', e) }, false);
  canvas.addEventListener("mousedown", function (e) { findEncounterExamCanvasXY('down', e) }, false);
  canvas.addEventListener("mouseup", function (e) { findEncounterExamCanvasXY('up', e) }, false);
  canvas.addEventListener("mouseout", function (e) { findEncounterExamCanvasXY('out', e) }, false);
}


function findEncounterExamCanvasXY(res, e) {
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
      drawEncounterExamCanvas();
    }
  }
}


function drawEncounterExamCanvas() {
  ctx.beginPath();
  ctx.moveTo(prevX, prevY);
  ctx.lineTo(currX, currY);
  ctx.strokeStyle = strokeColor;
  ctx.lineWidth = strokeWidth;
  ctx.stroke();
  ctx.closePath();
}

function setEncounterFormMode(encounter, section, isSaved, hasOwnership) {
  var id = encounter.id;
  
  if (encounter.completed == true) {
    $('#encounter-'+section+'-panel-'+id+' .form-control-unsaved').css({display: "none"});
    $('#encounter-'+section+'-panel-'+id+' .form-control-saved').css({display: "block"});
    $('#encounter-'+section+'-panel-'+id+' input:checkbox').attr("disabled", "disabled");
    $('#encounter-'+section+'-panel-'+id+' input:checkbox').attr("readonly", "readonly");
    $('#encounter-'+section+'-panel-'+id+' input:radio').attr("disabled", "disabled");
    $('#encounter-'+section+'-panel-'+id+' input:radio').attr("readonly", "readonly");
    $('#encounter-'+section+'-save-'+id).css({display: "none"});
    $('#encounter-'+section+'-saved-'+id).css({display: "none"});
    $('#encounter-'+section+'-clear-'+id).css({display: "none"});
    $('#encounter-'+section+'-panel-'+id+' .form-control-saved').removeAttr('contenteditable');
    $('#encounter-'+section+'-panel-'+id+' .form-control-saved').css({border:"none"});
    $('#encounter-'+section+'-panel-'+id+' .form-control-saved').removeClass('form-control-saved').addClass('form-control-closed');
    $('#encounter-demo-photo-upload-control-'+id).css({display:"none"});
    $('#encounter-pain-scale-value-'+id).css({display: "inline"});
    $(".slider-track").css({display: "none"});
    
    $('.dual-mode-unsaved').css({display: "none"});
    $('.dual-mode-saved').css({display: "inline"});
    $('.dual-mode-saved').off( "click");
    
    //$("#encounter-pain-type-"+id).css({display: "none"});
    //$("#encounter-pain-type-saved-"+id).css({display: "inline"});
    //$("#encounter-pain-type-saved-"+id).off( "click");
    
    return;
  }
  
  $('#encounter-'+section+'-panel-'+id+' .form-control-unsaved').css({display: (isSaved == true ? "none" : "block")});
  $('#encounter-'+section+'-panel-'+id+' .form-control-saved').css({display: (isSaved == true ? "block" : "none")});
  $('#encounter-'+section+'-save-'+id).css({display: (hasOwnership == true && isSaved == false ? "inline-block" : "none")});
  $('#encounter-'+section+'-saved-'+id).css({display: (hasOwnership == true && isSaved == true ? "inline-block" : "none")});
  $('#encounter-'+section+'-clear-'+id).css({display: (hasOwnership == true && isSaved == false ? "inline-block" : "none")});
  $('#encounter-'+section+'-panel-'+id+' .form-control-unsaved').prop("readonly",!hasOwnership);
  if (hasOwnership == false) {
    $('#encounter-'+section+'-panel-'+id+' .form-control-saved').removeAttr('contenteditable').blur();
    $('#encounter-demo-photo-upload-control-'+id).css({display:"none"});
  }
  else {
    $('#encounter-demo-photo-upload-control-'+id).css({display:"block"});
    $('#encounter-'+section+'-panel-'+id+' .form-control-saved').prop('contenteditable', true);
    $('#encounter-'+section+'-panel-'+id+' .form-control-readonly').removeAttr('contenteditable').blur();
  }
}

function  setupPictureUpload(encounterId, patientId) {
  $('#encounter-demo-photo-upload-'+encounterId).click(function(){ 
    $('#encounter-demo-photo-upload-progress-'+encounterId+' .progress-bar').css('width', '0');
  });
  uploader = new qq.FileUploader({
   element: document.getElementById('encounter-demo-photo-upload-'+encounterId),
   action: 'patient/uploadProfileImage?patientId='+patientId+'&sessionId=' + clinician.sessionId,
   debug: true,
   sizeLimit: 1048576,   
   allowedExtensions: ['jpg', 'jpeg', 'png', 'gif'],
   onProgress: function(id, fileName, loaded, total) {
      var progress = parseInt(loaded / total * 100, 10);
      $('#encounter-demo-photo-upload-progress-'+encounterId+' .progress-bar').css('width', progress + '%');
   },
   onComplete: function(id, fileName, responseJSON){
     $('#encounter-demo-photo-upload-progress-'+encounterId+' .progress-bar').css('width', '100%');
   var response = parsedData = $.parseJSON(responseJSON);
   var path = response.filename;
   var patientId = response.patientId;
     $("#encounter-demo-photo-"+encounterId).attr("src", app_patientChartHeadshot);
   }
  }); 
}

    