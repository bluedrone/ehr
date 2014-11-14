
/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

"use strict";

modulejs.define('patient', ["app", "notifier", "patient/chart", "render_util",
  "util"
], function (App, Notifier, PatientChart, RenderUtil, Util) {

  var module = {}
  
  module.loadInfo = function () {
    PatientChart.loadHeaderInfo();
    $('#section-notification').css("visibility", "visible");
    $('.patient-navbar-btn').css("display", "inline-block");
    $('.check-in-navbar-btn').css("display", "none");
    $('.encounter-navbar-btn').css("display", "none");
    $('#section-notification-text').html("Patient: " + PatientChart.chartFullName);
    PatientChart.show();
  };

  module.loadHistScreenForm = function() {
    var object = App.currentPatient.hist;
    RenderUtil.render('component/patient_medications', {
      patient: App.currentPatient
    }, function (s) {
      $('#patient-medications').html(s);
      $('#modal-medical-history .form-control-unsaved').css({
        display: "none"
      });
      $('.patient-med-editable').blur(function (e) {
        PatientHelper.getCurrentMedicationId(e);
        PatientHelper.updatePatientMedication("medication", $(this).html(), App
          .currentMedicationId);
      });
      $('.patient-dose-editable').blur(function (e) {
        PatientHelper.getCurrentMedicationId(e);
        PatientHelper.updatePatientMedication("dose", $(this).html(), App.currentMedicationId);
      });
      $('.patient-freq-editable').blur(function (e) {
        PatientHelper.getCurrentMedicationId(e);
        PatientHelper.updatePatientMedication("frequency", $(this).html(), App.currentMedicationId);
      });
      $('.medication-delete-control').click(function () {
        module.deleteMedication($(this));
      });
    });
    $('#patient-past-s-m-saved').html(object.pastSM);
    Util.selectCheckboxesFromList(object.famHist, 'patient-fam-hist-');
    $('#patient-fam-hist-notes-saved').html(object.famHistNotes);
    $('#patient-fam-hist-other-saved').html(object.famHistOther);
    $('#patient-allerg-food-saved').html(object.allergFood);
    $('#patient-allerg-drug-saved').html(object.allergDrug);
    $('#patient-allerg-env-saved').html(object.allergEnv);
    $('input[name=patient-vacc][value=' + object.vacc + ']').attr(
      "checked",
      true);
    $('#patient-vacc-notes-saved').html(object.vaccNotes);
    Util.selectCheckboxesFromList(object.subst, 'patient-subst-');
    $('#patient-smoke-pks-day-saved').html(object.smokePksDay);
    $('#patient-years-smoked-saved').html(object.yearsSmoke);
    $('#patient-smoke-years-quit-saved').html(object.smokeYearsQuit);
    $('#patient-etoh-units-week-saved').html(object.etohUnitsWeek);
    $('#patient-current-drugs-saved').html(object.currentDrugs);
    $('#patient-hist-new-medication').click(function () {
      var jsonData = JSON.stringify({
        sessionId: App.clinician.sessionId,
        patientId: App.currentPatient.id
      });
      Ajax.post("patient/addPatientMedication", {
        data: jsonData
      }, function (data) {
        var parsedData = $.parseJSON(data);
        var patientMedicationId = parsedData.patientMedicationId;
        var numMedications = $("#patient-medications").children()
          .length + 2;
        RenderUtil.render('component/patient_medication', {
          ordinal: numMedications,
          id: patientMedicationId
        }, function (s) {
          $("#patient-medications").append(s);
          $('#modal-medical-history .form-control-unsaved').css({
            display: "none"
          });
          $('.patient-med-editable').blur(function (e) {
            PatientHelper.getCurrentMedicationId(e);
            PatientHelper.updatePatientMedication("medication", $(this)
              .html(),
              patientMedicationId);
          });
          $('.patient-dose-editable').blur(function (e) {
            PatientEncounter.getCurrentQuestionId(e);
            PatientHelper.updatePatientMedication("dose", $(this).html(),
              patientMedicationId);
          });
          $('.patient-freq-editable').blur(function (e) {
            PatientEncounter.getCurrentQuestionId(e);
            PatientHelper.updatePatientMedication("frequency", $(this).html(),
              patientMedicationId);
          });
          $('.medication-delete-control').click(function () {
            module.deleteMedication($(this));
          });
        });
      });
    });
    $('#patient-past-s-m-saved').blur(function () {
      updateSavedPatient("pastSM", $(this).html());
    });
    $('input[name=patient-fam-hist-' + ']').click(function () {
      var famHist = $('input[name=patient-fam-hist-' + ']:checked').map(
        function () {
          return this.value;
        }).get().join(',');
      updateSavedPatient("famHist", famHist);
    });
    $('#patient-fam-hist-notes-saved').blur(function () {
      updateSavedPatient("famHistNotes", $(this).html());
    });
    $('#patient-fam-hist-other-saved').blur(function () {
      updateSavedPatient("famHistOther", $(this).html());
    });
    $('#patient-allerg-food-saved').blur(function () {
      updateSavedPatient("allergFood", $(this).html());
    });
    $('#patient-allerg-drug-saved').blur(function () {
      updateSavedPatient("allergDrug", $(this).html());
    });
    $('#patient-allerg-env-saved').blur(function () {
      updateSavedPatient("allergEnv", $(this).html());
    });
    $('input[name=patient-vacc]').click(function () {
      updateSavedPatient("vacc", $(this).val() == 'true');
    });
    $('#patient-vacc-notes-saved').blur(function () {
      updateSavedPatient("vaccNotes", $(this).html());
    });
    $('input[name=patient-subst]').click(function () {
      var subst = $('input[name=patient-subst]:checked').map(function () {
        return this.value;
      }).get().join(',');
      updateSavedPatient("subst", subst);
    });
    $('#patient-smoke-pks-day-saved').blur(function () {
      updateSavedPatient("smokePksDay", $(this).html());
    });
    $('#patient-years-smoked-saved').blur(function () {
      updateSavedPatient("yearsSmoked", $(this).html());
    });
    $('#patient-smoke-years-quit-saved').blur(function () {
      updateSavedPatient("smokeYearsQuit", $(this).html());
    });
    $('#patient-etoh-units-week-saved').blur(function () {
      updateSavedPatient("etohUnitsWeek", $(this).html());
    });
    $('#patient-current-drugs-saved').blur(function () {
      updateSavedPatient("currentDrugs", $(this).html());
    });
    $('#patient-hist-print').click(function () {
      Printer.printPatientForm('print_patient_hist',
        'MEDICAL HISTORY', object);
    });
  }

  module.loadPFSHScreenForm = function() {
   var object = App.currentPatient.pfsh;
    $('#modal-pfsh .form-control-unsaved').css({
      display: "none"
    });
    $('#patient-num-residents-saved').html(object.numResidents);
    $('#patient-job-type-saved').html(object.jobType);
    $('input[name=patient-mother-alive][value=' + object.motherAlive +
      ']').attr("checked",
      true);
    $('#patient-mother-death-reason-saved').html(object.motherDeathReason);
    $('input[name=patient-father-alive][value=' + object.fatherAlive +
      ']').attr("checked",
      true);
    $('#patient-father-death-reason-saved').html(object.fatherDeathReason);
    $('input[name=patient-partner-alive][value=' + object.partnerAlive +
      ']').attr("checked", true);
    $('#patient-partner-death-reason-saved').html(object.partnerDeathReason);
    $('#patient-num-siblings-saved').html(object.numSiblings);
    $('#patient-num-brothers-saved').html(object.numBrothers);
    $('#patient-num-sisters-saved').html(object.numSisters);
    $('#patient-num-children-saved').html(object.numChildren);
    $('#patient-num-sons-saved').html(object.numSons);
    $('#patient-num-daughters-saved').html(object.numDaughters);
    $('#patient-num-residents-saved').blur(function () {
      updateSavedPatient("numResidents", $(this).html());
    });
    $('#patient-job-type-saved').blur(function () {
      updateSavedPatient("jobType", $(this).html());
    });
    $('input[name=patient-mother-alive]').click(function () {
      updateSavedPatient("motherAlive", $(this).val() == 'true');
    });
    $('#patient-mother-death-reason-saved').blur(function () {
      updateSavedPatient("motherDeathReason", $(this).html());
    });
    $('input[name=patient-father-alive]').click(function () {
      updateSavedPatient("fatherAlive", $(this).val() == 'true');
    });
    $('#patient-father-death-reason-saved').blur(function () {
      updateSavedPatient("fatherDeathReason", $(this).html());
    });
    $('input[name=patient-partner-alive]').click(function () {
      updateSavedPatient("partnerAlive", $(this).val() == 'true');
    });
    $('#patient-partner-death-reason-saved').blur(function () {
      updateSavedPatient("partnerDeathReason", $(this).html());
    });
    $('#patient-num-siblings-saved').blur(function () {
      updateSavedPatient("numSiblings", $(this).html());
    });
    $('#patient-num-brothers-saved').blur(function () {
      updateSavedPatient("numBrothers", $(this).html());
    });
    $('#patient-num-sisters-saved').blur(function () {
      updateSavedPatient("numSisters", $(this).html());
    });
    $('#patient-num-children-saved').blur(function () {
      updateSavedPatient("numChildren", $(this).html());
    });
    $('#patient-num-sons-saved').blur(function () {
      updateSavedPatient("numSons", $(this).html());
    });
    $('#patient-num-daughters-saved').blur(function () {
      updateSavedPatient("numDaughters", $(this).html());
    });
    $('#patient-pfsh-print').click(function () {
      Printer.printPatientForm('print_patient_pfsh',
        'SOCIAL & FAMILY HISTORY',
        object);
    });
  }

  function updateSavedPatient(property, value, isDualMode, elementId,
    valueName) {
    updateLocalPatient(property, value);
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      patientId: App.currentPatient.id,
      updateProperty: property,
      updatePropertyValue: value
    });
    Ajax.post("patient/updatePatient", {
      data: jsonData
    }, function (data) {
      if (isDualMode) {
        var unsavedId = elementId.replace('-saved', '');
        $('#' + unsavedId).css({
          display: "none"
        });
        $('#' + elementId).html(valueName ? valueName : value);
        $('#' + elementId).css({
          display: "block"
        });
      }
    });
  }

  function updateLocalPatient(property, value) {
    App.currentPatient[property] = value;
  }

  module.loadCurrentSuppScreen = function() {
    App.patientSupp = [];
    for (var i = 0; i < App.patientEncounters.length; i++) {
      App.patientSupp.push(App.patientEncounters[i].supp);
    }
    if (App.patientSupp.length == 0) {
      return;
    }
    App.patientSuppIndex = 0;
    loadSuppScreenForm();
    $('#patient-supp-print').click(function () {
      Printer.printPatientForm('print_patient_supp',
        'SUPPLEMENTAL QUESTIONS',
        App.patientSupp[App.patientSuppIndex]);
    });
  }

  module.changeSuppScreen = function(adjustment) {
    if ((App.patientSuppIndex == 0 && adjustment == -1) || (App.patientSuppIndex ==
        App.patientSupp.length - 1 && adjustment == 1)) {
      return;
    }
    App.patientSuppIndex += adjustment;
    loadSuppScreenForm();
  }

  function loadSuppScreenForm() {
    var date = dateFormat(App.patientSupp[App.patientSuppIndex].date,
      'mm/dd/yyyy');
    $('#patient-supp-date').html(date);
    $('#num-cups-water').html(App.patientSupp[App.patientSuppIndex].numCupsWater);
    $('#num-cups-coffee').html(App.patientSupp[App.patientSuppIndex].numCupsCoffee);
    $('#num-cups-tea').html(App.patientSupp[App.patientSuppIndex].numCupsTea);
    $('#water-source').html(App.patientSupp[App.patientSuppIndex].waterSource);
    RenderUtil.render('component/encounter_questions', {
      encounter: App.patientEncounters[App.patientSuppIndex]
    }, function (s) {
      $("#questions").html(s); //setEncounterFormMode(id, section, savedState, hasOwnership);
    });
  }

  module.loadCurrentConsultsScreen = function() {
    App.patientConsults = Util.map(App.patientEncounters, function(elem){
      return elem.followUp
    })
    if (App.patientConsults.length == 0) {
      return;
    }
    App.patientConsultsIndex = 0;
    loadConsultsScreenForm();
    $('#patient-follow-up-print').click(function () {
      Printer.printPatientForm('print_patient_follow-up', 'FOLLOW UP',
        App.patientConsults[App.patientConsultsIndex]);
    });
  }

  module.changeConsultsScreen = function(adjustment) {
    if ((App.patientConsultsIndex == 0 && adjustment == -1) || (App.patientConsultsIndex ==
        App.patientConsults.length - 1 && adjustment == 1)) {
      return;
    }
    App.patientConsultsIndex += adjustment;
    loadConsultsScreenForm();
  }

  function loadConsultsScreenForm() {
    var date = dateFormat(App.patientConsults[App.patientConsultsIndex].date,
      'mm/dd/yyyy');
    $('#patient-follow-up-date').html(date);
    $('input[name=follow-up-level][value=' + App.patientConsults[App.patientConsultsIndex]
      .level +
      ']').attr("checked", true);
    $('#follow-up-when').html(App.patientConsults[App.patientConsultsIndex]
      .when);
    $('#follow-up-condition').html(App.patientConsults[App.patientConsultsIndex]
      .condition);
    $('#follow-up-dispense-rx').html(App.patientConsults[App.patientConsultsIndex]
      .dispenseRx);
    $('#follow-up-uss').html(App.patientConsults[App.patientConsultsIndex]
      .USS);
    $('#follow-up-pregnant').html(App.patientConsults[App.patientConsultsIndex]
      .pregnant);
    $('#follow-up-wound-care').html(App.patientConsults[App.patientConsultsIndex]
      .woundCare);
    $('#follow-up-ref-to-specialist').html(App.patientConsults[App.patientConsultsIndex]
      .refToSpecialist);
    $('#follow-up-dental-list').html(App.patientConsults[App.patientConsultsIndex]
      .dentalList);
    $('#follow-up-physiotherapy').html(App.patientConsults[App.patientConsultsIndex]
      .physiotherapy);
    $('#follow-up-blood-labs').html(App.patientConsults[App.patientConsultsIndex]
      .bloodLabs);
    $('#follow-up-other').html(App.patientConsults[App.patientConsultsIndex]
      .other);
    $('#follow-up-pulmonary-fx-test').html(App.patientConsults[App.patientConsultsIndex]
      .pulmonaryFXTest);
    $('#follow-up-vision').html(App.patientConsults[App.patientConsultsIndex]
      .vision);
    $('input[name=follow-up-completed][value=' + App.patientConsults[App.patientConsultsIndex]
      .followUpCompleted +
      ']').attr("checked", true) == 'true';
    var followUpDate = dateFormat(App.patientConsults[App.patientConsultsIndex]
      .followUpDate,
      'mm/dd/yyyy');
    $('#follow-up-date').html(followUpDate);
  }

  module.loadProgressNotesScreen = function() {
    var jsonData = JSON.stringify({
      patientId: App.currentPatientId,
      sessionId: App.clinician.sessionId
    });
    Ajax.post("patient/getProgressNotes", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      App.progressNotes = parsedData.progressNotes;
      if (!App.progressNotes || App.progressNotes.length == 0) {
        return;
      }
      App.progressNotesIndex = 0;
      loadProgressNotesScreenForm();
      $('#btn-progress-notes-print').click(function () {
        Util.printForm('print_progress_note', App.progressNotes[
            App.progressNotesIndex],
          'PROGRESS NOTE');
      });
    });
  }

  module.changeProgressNotesScreen = function(adjustment) {
    if ((App.progressNotesIndex == 0 && adjustment == -1) || (App.progressNotesIndex ==
        App.progressNotes.length - 1 && adjustment == 1)) {
      return;
    }
    App.progressNotesIndex += adjustment;
    loadProgressNotesScreenForm();
  }

  function loadProgressNotesScreenForm() {
    var date = dateFormat(App.progressNotes[App.progressNotesIndex].date,
      'mm/dd/yyyy');
    $('#progress-notes-date').html(date);
    if (App.progressNotes[App.progressNotesIndex].completed == true) {
      $('#progress-notes-subject-saved').html(App.progressNotes[App.progressNotesIndex]
        .subject);
      $('#progress-notes-content-saved').html(App.progressNotes[App.progressNotesIndex]
        .content);
      $('#modal-progress-notes .form-control-unsaved').css({
        display: "none"
      });
      $('#modal-progress-notes .form-control-saved').css({
        display: "block"
      });
    } else {
      $('#progress-notes-subject').val(App.progressNotes[App.progressNotesIndex]
        .subject);
      $('#progress-notes-content').html(App.progressNotes[App.progressNotesIndex]
        .content);
      $('#modal-progress-notes .form-control-saved').css({
        display: "none"
      });
      $('#modal-progress-notes .form-control-unsaved').css({
        display: "block"
      });
    }
  }

  module.newProgressNotesFormDialog = function() {
    if (App.progressNotes[App.progressNotesIndex].completed == true) {
      newProgressNotesForm();
      return;
    }
    var args = {
      modalTitle: "Complete Progress Note Confirmation",
      modalH3: "Ready To Complete The Currently Open Progress Note?",
      modalH4: "In order to start a new progress note the current one needs to be completed.",
      cancelButton: 'Cancel',
      okButton: 'Confirm'
    };
    RenderUtil.render('dialog/confirm', args, function (s) {
      $('#modals-placement').append(s);
      $('#modal-confirm').modal('show');
      $('#app-modal-confirmation-btn').click(function () {
        var jsonData = JSON.stringify({
          sessionId: App.clinician.sessionId,
          progressNoteId: App.progressNotes[App.progressNotesIndex]
            .id
        });
        Ajax.post("patient/closeProgressNote", {
          data: jsonData
        }, function (data) {
          var parsedData = $.parseJSON(data);
          Notifier.notify('Progress Note Completed');
          App.progressNotes[App.progressNotesIndex].completed =
            true;
          newProgressNotesForm();
        });
      });
    });
  }

  module.updateProgressNote = function() {
    App.progressNotes[App.progressNotesIndex].subject = $(
      '#progress-notes-subject').val();
    App.progressNotes[App.progressNotesIndex].content = $(
      '#progress-notes-content').html();
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      progressNote: App.progressNotes[App.progressNotesIndex]
    });
    Ajax.post("patient/updateProgressNote", {
      data: jsonData
    }, function (data) {
      Notifier.notify('Progress Note Saved');
    });
  }

  function newProgressNotesForm() {
    var jsonData = JSON.stringify({
      patientId: App.currentPatientId,
      sessionId: App.clinician.sessionId
    });
    Ajax.post("patient/newProgressNote", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      var newProgressNote = parsedData.progressNote;
      App.progressNotes.unshift(newProgressNote);
      $('#progress-notes-subject').val('');
      $('#progress-notes-content').html('');
      $('#modal-progress-notes .form-control-saved').css({
        display: "none"
      });
      $('#modal-progress-notes .form-control-unsaved').css({
        display: "block"
      });
    });
  }

  module.loadCurrentExamScreen = function() {
    App.patientExam = [];
    for (var i = 0; i < App.patientEncounters.length; i++) {
      App.patientEncounters[i].exam.hb = App.patientEncounters[i].lab.hb;
      App.patientEncounters[i].exam.glucose = App.patientEncounters[i].lab
        .glucose;
      App.patientEncounters[i].exam.urineDip = App.patientEncounters[i].lab
        .urineDip;
      App.patientExam.push(App.patientEncounters[i].exam);
    }
    if (App.patientExam.length == 0) {
      return;
    }
    App.patientExamIndex = 0;
    loadExamScreenForm();
    $('#patient-exam-print').click(function () {
      Printer.printPatientForm('print_patient_exam', 'EXAM', App.patientExam[
        App.patientExamIndex]);
    });
  }

  module.changeExamScreen = function(adjustment) {
    if ((App.patientExamIndex == 0 && adjustment == -1) || (App.patientExamIndex ==
        App.patientExam.length - 1 && adjustment == 1)) {
      return;
    }
    App.patientExamIndex += adjustment;
    loadExamScreenForm();
  }

  function loadExamScreenForm() {
    var date = dateFormat(App.patientExam[App.patientExamIndex].date,
      'mm/dd/yyyy');
    $('#patient-exam-date').html(date);
    $('#hs').html(App.patientExam[App.patientExamIndex].hs);
    $('input[name=heart-rhythm][value=' + App.patientExam[App.patientExamIndex]
      .heartRhythm +
      ']').attr("checked", true);
    $('#lab-hb').html(App.patientExam[App.patientExamIndex].hb);
    $('#lab-glucose').html(App.patientExam[App.patientExamIndex].glucose);
    $('#lab-urine-dip').html(App.patientExam[App.patientExamIndex].urineDip);
    $('#diagnosis').html(App.patientExam[App.patientExamIndex].diagnosis);
    $('#dx-code').html(App.patientExam[App.patientExamIndex].dxCode);
    $('#treatment-plan').html(App.patientExam[App.patientExamIndex].treatmentPlan);
    $('#tx-code').html(App.patientExam[App.patientExamIndex].txCode);
  }

  module.loadCurrentOBGYNScreen = function() {
    App.patientOBGYN = Util.map(App.patientEncounters, function(elem){
      return elem[i].obgyn
    });
    if (App.patientOBGYN.length == 0) {
      return;
    }
    App.patientOBGYNIndex = 0;
    loadOBGYNScreenForm();
    $('#patient-obgyn-print').click(function () {
      Printer.printPatientForm('print_patient_obgyn', 'OBGYN', App.patientOBGYN[
        App.patientOBGYNIndex]);
    });
  }

  module.changeOBGYNScreen = function(adjustment) {
    if ((App.patientOBGYNIndex == 0 && adjustment == -1) || (App.patientOBGYNIndex ==
        App.patientOBGYN.length - 1 && adjustment == 1)) {
      return;
    }
    App.patientOBGYNIndex += adjustment;
    loadOBGYNScreenForm();
  }

  function loadOBGYNScreenForm() {
    var date = dateFormat(App.patientOBGYN[App.patientOBGYNIndex].date,
      'mm/dd/yyyy');
    $('#patient-obgyn-date').html(date);
    $('#obgyn-g').html(App.patientOBGYN[App.patientOBGYNIndex].g);
    $('#obgyn-p').html(App.patientOBGYN[App.patientOBGYNIndex].p);
    $('#obgyn-t').html(App.patientOBGYN[App.patientOBGYNIndex].t);
    $('#obgyn-a').html(App.patientOBGYN[App.patientOBGYNIndex].a);
    $('#obgyn-l').html(App.patientOBGYN[App.patientOBGYNIndex].l);
    $('input[name=pregnant][value=' + App.patientOBGYN[App.patientOBGYNIndex]
      .pregStatus +
      ']').attr("checked", true);
    $('input[name=breastfeeding][value=' + App.patientOBGYN[App.patientOBGYNIndex]
      .breastfeeding + ']').attr("checked",
      true);
    $('#breastfeeding-months').html(App.patientOBGYN[App.patientOBGYNIndex]
      .breastfeedingMonths);
    $('#last-period').html(App.patientOBGYN[App.patientOBGYNIndex].lastPeriod);
    $('#age-first-period').html(App.patientOBGYN[App.patientOBGYNIndex].ageFirstPeriod);
    $('input[name=pap-smear][value=' + App.patientOBGYN[App.patientOBGYNIndex]
      .papSmearStatus +
      ']').attr("checked", true);
    $('input[name=birth-control][value=' + App.patientOBGYN[App.patientOBGYNIndex]
      .birthControlStatus +
      ']').attr("checked", true);
    $('#birth-control-type').html(App.patientOBGYN[App.patientOBGYNIndex]
      .birthControlType);
    Util.selectCheckboxesFromList(App.patientOBGYN[App.patientOBGYNIndex]
      .obgynHistory,
      'obgyn-hist');
    $('#obgyn-hist-other').html(App.patientOBGYN[App.patientOBGYNIndex].obgynHistoryOther);
  }

  return module;
});