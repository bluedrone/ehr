/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */


function renderPatientEncounterForm(encounter, hasOwnership) {
  var id = encounter.id;
  var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
  
  renderEncounterFormSection (encounter, 'demo', encounter.demoSaved, hasOwnership);
  renderEncounterFormSection (encounter, 'vitals', encounter.vitalsSaved, hasOwnership);
  renderEncounterFormSection (encounter, 'soap-note', encounter.soapNoteSaved, hasOwnership);
  renderEncounterFormSection (encounter, 'cc', encounter.ccSaved, hasOwnership);
  renderEncounterFormSection (encounter, 'obgyn', encounter.obgynSaved, hasOwnership);
  renderEncounterFormSection (encounter, 'pfsh', encounter.patient.pfsh.saved, hasOwnership);
  renderEncounterFormSection (encounter, 'supp', encounter.suppSaved, hasOwnership);
  renderEncounterFormSection (encounter, 'hist', encounter.patient.hist.saved, hasOwnership);
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
  $('#encounter-demo-print-'+id).click(function() { printEncounterForm('print_encounter_demographics', 'DEMOGRAPHICS')});
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
  RenderUtil.render('component/basic_select_options', {options:app_usStates, collection:'app_usStates'}, function(s) {
    var id = app_currentEncounter.id;
    $('#encounter-demo-us-state-'+id).html(s);
  });
  renderCPTModifiers(id);
} 



function renderEncounterFormSection (encounter, section, savedState, hasOwnership) {
  var id = encounter.id;
  var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
  var notEditable = true; 
  setEncounterFormMode(encounter, section, savedState, hasOwnership);

  if (section == 'demo') {
    if (savedState == false) {
      $('#encounter-demo-govt-id-'+id).mask("999-99-9999");
      $('#encounter-demo-dob-'+id).mask("99/99/9999");
      $('#encounter-demo-postal-code-'+id).mask("99999");
    }
    else if (savedState == true) {
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
      $('#encounter-demo-govt-id-'+id).blur(function() { updateSavedPatientEncounter("govtId", $(this).val(), id); });
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
  }
  else if (section == 'soap-note') {
    if (savedState == false) {
    }
    else if (savedState == true) {
      $('#encounter-soap-note-subjective-saved-'+id).html(encounter.soapNote.subjective);
      $('#encounter-soap-note-objective-saved-'+id).html(encounter.soapNote.objective);
      $('#encounter-soap-note-assessment-saved-'+id).html(encounter.soapNote.assessment);
      $('#encounter-soap-note-plan-saved-'+id).html(encounter.soapNote.plan);
      $('#encounter-soap-note-subjective-saved-'+id).blur(function() { updateSavedPatientEncounter("subjective", $(this).html(), id); });
      $('#encounter-soap-note-objective-saved-'+id).blur(function() { updateSavedPatientEncounter("objective", $(this).html(), id); });
      $('#encounter-soap-note-assessment-saved-'+id).blur(function() { updateSavedPatientEncounter("assessment", $(this).html(), id); });
      $('#encounter-soap-note-plan-saved-'+id).blur(function() { updateSavedPatientEncounter("plan", $(this).html(), id); });
    }
  }
  else if (section == 'vitals') {
    if (savedState == false) {
      $("#encounter-height-"+id+", #encounter-weight-"+id+", #encounter-temp-"+id).keydown(function(e) { util_filterDecimalInput(e); });
    }
    else if (savedState == true) {
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
  }
  else if (section == 'cc') {
    if (savedState == false) {
      $("#encounter-hours-since-"+id+", #encounter-days-since-"+id+", #encounter-weeks-since-"+id+", #encounter-months-since-"+id+", #encounter-years-since-"+id).keydown(function(e) { util_filterDecimalInput(e); });
      $("#encounter-pain-x-hour-"+id+", #encounter-pain-x-day-"+id+", #encounter-pain-x-week-"+id+", #encounter-pain-x-month-"+id).keydown(function(e) { util_filterDecimalInput(e); });
      $('#encounter-pain-scale-'+id).slider({value:encounter.cc.painScale}).on('slide', function(ev){
        $('#encounter-pain-scale-value-'+id).html(this.value)
        updateSavedPatientEncounter("painScale", this.value, id);
      });
      $('#encounter-pain-scale-value-'+id).html(encounter.cc.painScale);
    }
    else if (savedState == true) {
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
  }
  else if (section == 'obgyn' && !encounter.obgyn) {
    if (savedState == false) {
      $('#obgyn-form, #obgyn-header').hide();
    }
    else if (savedState == true) {
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
  }
  else if (section == 'pfsh') {
    if (savedState == false) {
    }
    else if (savedState == true) {
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
  } 
  else if (section == 'supp') {
    if (savedState == false) {
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
    else if (savedState == true) {
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
  }
  else if (section == 'hist') {
    if (savedState == false) {
      RenderUtil.render('component/patient_medications', {encounter:encounter}, function(s) { $("#patient-medications-"+id).html(s); setEncounterFormMode(encounter, section, savedState, hasOwnership);});
      $('#encounter-hist-new-medication-'+id).click(function() { 
       var jsonData = JSON.stringify({sessionId: clinician.sessionId, patientId: encounter.patient.id});
        $.post("patient/addPatientMedication", {data:jsonData}, function(data) {
        var parsedData = $.parseJSON(data);
          var numMedications = $("#patient-medications-"+id).children().length + 2;
          RenderUtil.render('component/patient_medication', {ordinal:numMedications, id: parsedData.patientMedicationId}, function(s) { $("#patient-medications-"+id).append(s); setEncounterFormMode(encounter, section, savedState, hasOwnership);});
        });
      });
      $('.medication-delete-control').click(function() { deleteMedication($(this)); });
    }
    else if (savedState == true) {
      RenderUtil.render('component/patient_medications', {patient:encounter.patient}, function(s) { 
        $("#patient-medications-"+id).html(s); 
        setEncounterFormMode(encounter, section, savedState, hasOwnership);
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
        $.post("patient/addPatientMedication", {data:jsonData}, function(data) {
          var parsedData = $.parseJSON(data);
          var patientMedicationId = parsedData.patientMedicationId;
          var numMedications = $("#patient-medications-"+id).children().length + 2;
          RenderUtil.render('component/patient_medication', {ordinal:numMedications, id: patientMedicationId}, function(s) { 
            $("#patient-medications-"+id).append(s); 
            setEncounterFormMode(encounter, section, savedState, hasOwnership);
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
    
    if (savedState == false) {
     RenderUtil.render('component/dx_codes', {encounter:encounter}, function(s) { $("#encounter-dx-codes-"+id).html(s); setEncounterFormMode(encounter, section, savedState, hasOwnership);});
      $('#encounter-exam-new-dx-code-'+id).click(function() { 
       var jsonData = JSON.stringify({sessionId: clinician.sessionId, encounterId: id});
        $.post("patient/addDxCode", {data:jsonData}, function(data) {
        var parsedData = $.parseJSON(data);
          var numQuestions = $("#encounter-dx-codes-"+id).children().length + 2;
          RenderUtil.render('component/dx_code', {id: parsedData.dxCodeId}, function(s) { $("#encounter-dx_codes-"+id).append(s); setEncounterFormMode(encounter, section, savedState, hasOwnership);});
        });
      });
      RenderUtil.render('component/tx_codes', {encounter:encounter}, function(s) { $("#encounter-tx-codes-"+id).html(s); setEncounterFormMode(encounter, section, savedState, hasOwnership);});
      $('#encounter-exam-new-tx-code-'+id).click(function() { 
       var jsonData = JSON.stringify({sessionId: clinician.sessionId, encounterId: id});
        $.post("patient/addTxCode", {data:jsonData}, function(data) {
        var parsedData = $.parseJSON(data);
          var numQuestions = $("#encounter-tx-codes-"+id).children().length + 2;
          RenderUtil.render('component/tx_code', {id: parsedData.txCodeId}, function(s) { $("#encounter-tx_codes-"+id).append(s); setEncounterFormMode(encounter, section, savedState, hasOwnership);});
        });
      });
    }
    else if (savedState == true) {      
      RenderUtil.render('component/dx_codes', {encounter:encounter}, function(s) { 
        $("#encounter-dx_codes-"+id).html(s); 
        setEncounterFormMode(encounter, section, savedState, hasOwnership);
        $('.encounter-dx-code-icd9-editable').blur(function(e) { 
          getCurrentDxCodeId(e);
          updateDxCode("icd9", $(this).html(), app_currentDxCodeId); 
        });
      });
      
      $('#encounter-supp-new-dx-code-'+id).click(function() { 
        var jsonData = JSON.stringify({sessionId: clinician.sessionId, encounterId: id});
        $.post("patient/addDxCode", {data:jsonData}, function(data) {
          var parsedData = $.parseJSON(data);
          var dxCodeId = parsedData.dxCodeId;
          RenderUtil.render('component/dx_code', {id: dxCodeId}, function(s) { 
            $("#encounter-dx-codes-"+id).append(s); 
            setEncounterFormMode(encounter, section, savedState, hasOwnership);
            $('.encounter-dx-code-icd9-editable').blur(function(e) { 
              getCurrentDxCodeId(e);
              updateDxCode("icd9", $(this).html(), dxCodeId); 
            });
           });
       });
      });
      
      RenderUtil.render('component/tx_codes', {encounter:encounter}, function(s) { 
        $("#encounter-tx_codes-"+id).html(s); 
        setEncounterFormMode(encounter, section, savedState, hasOwnership);
        $('.encounter-tx-code-cpt-editable').blur(function(e) { 
          getCurrentTxCodeId(e);
          updateTxCode("cpt", $(this).html(), app_currentTxCodeId); 
        });
        $('.encounter-tx-code-cpt-modifier-editable').blur(function(e) { 
          getCurrentTxCodeId(e);
          updateTxCode("cptModifier", $(this).html(), app_currentTxCodeId); 
        });
      });
      
      $('#encounter-supp-new-tx-code-'+id).click(function() { 
        var jsonData = JSON.stringify({sessionId: clinician.sessionId, encounterId: id});
        $.post("patient/addTxCode", {data:jsonData}, function(data) {
          var parsedData = $.parseJSON(data);
          var txCodeId = parsedData.txCodeId;
          RenderUtil.render('component/tx_code', {id: txCodeId}, function(s) { 
            $("#encounter-tx-codes-"+id).append(s); 
            setEncounterFormMode(encounter, section, savedState, hasOwnership);
            $('.encounter-tx-code-cpt-editable').blur(function(e) { 
              getCurrentTxCodeId(e);
              updateTxCode("cpt", $(this).html(), app_currentTxCodeId); 
            });
            $('.encounter-tx-code-cpt-modifier-editable').blur(function(e) { 
              getCurrentTxCodeId(e);
              updateTxCode("cptModifier", $(this).html(), app_currentTxCodeId); 
            });
           });
       });
      }); 
      
      $('#encounter-hs-saved-'+id).html(encounter.exam.hs);
      $('input[name=encounter-heart-rhythm-'+id+'][value='+encounter.exam.heartRhythm+']').attr("checked", true);
      $('#encounter-lab-hb-saved-'+id).html(encounter.exam.hb);
      $('#encounter-lab-glucose-saved-'+id).html(encounter.lab.glucose);
      $('#encounter-lab-urine-dip-saved-'+id).html(encounter.lab.urineDip);
      $('#encounter-diagnosis-saved-'+id).html(encounter.exam.diagnosis);
      $('#encounter-dx-code-saved-'+id).html(encounter.exam.dxCode);
      $('#encounter-treatment-plan-saved-'+id).html(encounter.exam.treatmentPlan);
      $('#encounter-tx-code-saved-'+id).html(encounter.exam.txCode);
      $('#encounter-hs-saved-'+id).blur(function() { updateSavedPatientEncounter("hs", $(this).html(), id); });
      $('input[name=encounter-heart-rhythm-'+id+']').click(function() { updateSavedPatientEncounter("heartRhythm", $(this).val(), id); });
      $('#encounter-lab-hb-saved-'+id).blur(function() { updateSavedPatientEncounter("hb", $(this).html(), id); });
      $('#encounter-lab-glucose-saved-'+id).blur(function() { updateSavedPatientEncounter("glucose", $(this).html(), id); });
      $('#encounter-lab-urine-dip-saved-'+id).blur(function() { updateSavedPatientEncounter("urineDip", $(this).html(), id); });
      $('#encounter-diagnosis-saved-'+id).blur(function() { updateSavedPatientEncounter("diagnosis", $(this).html(), id); });
      $('#encounter-dx-code-saved-'+id).blur(function() { updateSavedPatientEncounter("dxCode", $(this).html(), id); });
      $('#encounter-treatment-plan-saved-'+id).blur(function() { updateSavedPatientEncounter("treatmentPlan", $(this).html(), id); });
      $('#encounter-tx-code-saved-'+id).blur(function() { updateSavedPatientEncounter("txCode", $(this).html(), id); });
    }
  }
  else if (section == 'follow-up') {
    if (savedState == false) {
      $('#encounter-follow-up-date-'+id).mask("99/99/9999");
    }
    else if (savedState == true) {
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
    var unsavedItem = $('#'+unsavedId);
    if (name) { 
      $('#'+unsavedId).change(function() { 
        updateSavedPatientEncounter(name, $(this).val(), id, true, savedId, this.selectedOptions[0].label); 
      });
    }
    else {
      $('#encounter-demo-govt-id-'+id).mask("999-99-9999");
      $('#encounter-demo-dob-'+id).mask("99/99/9999");
      $('#encounter-demo-postal-code-'+id).mask("99999");
    }
  });
}