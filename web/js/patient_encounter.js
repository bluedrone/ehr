/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

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


function renderCPTModifiers(id) {
  RenderUtil.render('component/basic_select_options', {options:app_cptModifiers, collection:'app_cptModifiers'}, function(s) {
    var id = app_currentEncounter.id;
    $('#encounter-cpt-modifier-'+id).html(s);
  });
}


function initEncounterTypeAheads(id) {
  var dxCode = new Bloodhound({
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
      url: 'app/searchICD9?sessionId='+clinician.sessionId+'&searchText=%QUERY',
      filter: function (data) {
        return $.map(data.icd9List, function (dxCode) {
          return { value: dxCode.code + ' ' + dxCode.codeText };
        });
      }
    }
  });
  dxCode.initialize();
  //$('#encounter-dx-code-'+id).typeahead( { hint: true, highlight: true, limit: 10, minLength: 3 },
  $('.icd9-typeahead').typeahead( { hint: true, highlight: true, limit: 10, minLength: 3 },
  { name: 'encounter-dx-code-'+id, displayKey: 'value', source: dxCode.ttAdapter(), }); 
  
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



function updateSavedPatientEncounter(property, value, encounterId, isDualMode, elementId, valueName) {
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
      $('#'+elementId).html(valueName ? valueName : value);
      $('#'+elementId).css({display: "block"});
    }
  }); 
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
  for (i=0;i<encounter.patient.hist.patientMedicationList.length;i++) { 
    var medicationId = encounter.patient.hist.patientMedicationList[i].id;
    encounter.patient.hist.patientMedicationList[i].medication = $("#patient-med-"+medicationId).val();
    encounter.patient.hist.patientMedicationList[i].dose = $("#patient-dose-"+medicationId).val();
    encounter.patient.hist.patientMedicationList[i].frequency = $("#patient-freq-"+medicationId).val();
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
  for (i=0;i<encounter.dxCodes.length;i++) { 
    var dxCodeId = encounter.dxCodes[i].id;
    encounter.dxCodes[i].icd9 = $("#encounter-icd9-"+dxCodeId).val();
  }
  for (i=0;i<encounter.txCodes.length;i++) { 
    var txCodeId = encounter.txCodes[i].id;
    encounter.txCodes[i].cpt = $("#encounter-cpt-"+txCodeId).val();
    encounter.txCodes[i].cptModifier = $("#encounter-cpt-modifier-"+txCodeId).val();
  }
  encounter.exam.hs = $.trim($("#encounter-hs-"+id).val());
  encounter.exam.heartRhythm = $('input[name=encounter-heart-rhythm-'+id+']:checked').val();
  encounter.exam.hb = $.trim($("#encounter-lab-hb-"+id).val());
  encounter.exam.glucose = $.trim($("#encounter-lab-glucose-"+id).val());
  encounter.exam.urineDIP = $.trim($("#encounter-lab-urine-dip-"+id).val());
  encounter.exam.diagnosis = $.trim($("#encounter-diagnosis-"+id).val());
  encounter.exam.treatmentPlan = $.trim($("#encounter-treatment-plan-"+id).val());
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


function updateDxCode(icd9, dxCodeId) {
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    dxCodeId: dxCodeId,
    icd9:icd9
  });
  $.post("patient/updateDxCode", {data:jsonData}, function(data) {
  }); 
}


function updateTxCode(cpt, cptModifier, txCodeId) {
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    txCodeId: txCodeId,
    cpt:cpt,
    cptModifier:cptModifier
  });
  $.post("patient/updateTxCode", {data:jsonData}, function(data) {
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

function updatePatientMedication(property, value, patientMedicationId) {
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    patientMedicationId: patientMedicationId,
    updateProperty:property,
    updatePropertyValue:value
  });
  $.post("patient/updatePatientMedication", {data:jsonData}, function(data) {
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


function getCurrentDxCodeId(e) {
  var attributes = e.currentTarget.attributes;
  for (i=0;i<attributes.length;i++) {
    if (attributes[i].name == 'data-id') {
      app_currentDxCodeId = parseInt(attributes[i].nodeValue); 
    }
  }
} 

function getCurrentTxCodeId(e) {
  var attributes = e.currentTarget.attributes;
  for (i=0;i<attributes.length;i++) {
    if (attributes[i].name == 'data-id') {
      app_currentTxCodeId = parseInt(attributes[i].nodeValue); 
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


function deleteMedication(element) {
  var id = element.attr('name');
  var jsonData = JSON.stringify({ sessionId: clinician.sessionId, patientMedicationId:id});
  $.post("patient/deletePatientMedication", {data:jsonData}, function(data) { 
    $('#patient-medication-'+id).remove();
  });  
}

    