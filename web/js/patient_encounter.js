
/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

"use strict";

modulejs.define('patient/encounter', ["render_util", "app", "notifier",
  "patient/encounter_form"
], function (RenderUtil, App, Notifier,
  PatientEncounterForm) {

  var module = {}
  
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
    var modes = [
      [App.currentEncounter, 'demo', App.currentEncounter.demoSaved,
        hasOwnership
      ],
      [App.currentEncounter, 'vitals', App.currentEncounter.vitalsSaved,
        hasOwnership
      ],
      [App.currentEncounter, 'soap-note', App.currentEncounter.soapNoteSaved,
        hasOwnership
      ],
      [App.currentEncounter, 'cc', App.currentEncounter.ccSaved,
        hasOwnership
      ],
      [App.currentEncounter, 'obgyn', App.currentEncounter.obgynSaved,
        hasOwnership
      ],
      [App.currentEncounter, 'pfsh', App.currentEncounter.pfshSaved,
        hasOwnership
      ],
      [App.currentEncounter, 'supp', App.currentEncounter.suppSaved,
        hasOwnership
      ],
      [App.currentEncounter, 'hist', App.currentEncounter.histSaved,
        hasOwnership
      ],
      [App.currentEncounter,
        'exam', App.currentEncounter.examSaved, hasOwnership
      ],
      [App.currentEncounter,
        'follow-up', App.currentEncounter.followUpSaved, hasOwnership
      ]
    ];
    for (mode in mode) {
      setEncounterFrom.apply(mode);
    }
  }
  $('#app-encounter-close-record').click(function () {
    RenderUtil.render('dialog/close_encounter', {}, function (s) {
      $('#modals-placement').html(s);
      $('#modal-close-encounter').modal('show');
      $('#app-encounter-close-record-confirmation').click(function () {
        var jsonData = JSON.stringify({
          sessionId: App.clinician.sessionId,
          encounterId: App.currentEncounter.id
        });
        Ajax.post("patient/closeEncounter", {
          data: jsonData
        }, function (data) {
          var parsedData = $.parseJSON(data);
          Notifier.notify(
            'Patient Encounter Record Completed');
          var jsonData = JSON.stringify({
            sessionId: App.clinician.sessionId
          });
          Ajax.post("patient/getPatientEncounterGroups", {
              data: jsonData
            },
            function (data) {
              var parsedData = $.parseJSON(data);
              App.patientEncounterGroups = parsedData.patientEncounterGroups;
              buildGroupOrderArray();
              $("#app-encounter-group-tabs").html('');
              viewEncounterFormGroupWithId();
            });
        });
      });
    });
  });

  function renderCPTModifiers(id, element) {
    RenderUtil.render('component/basic_select_options', {
      options: App.cptModifiers,
      collection: 'app_cptModifiers'
    }, function (s) {
      var id = App.currentEncounter.id;
      $(element).html(s);
    });
  }
  function updateLockStatusIcon(lockStatus) {
    var id = App.currentEncounter.id;
    var oldLockStatusIcon = App.lockIcons[App.oldLockStatus];
    var lockStatusIcon = App.lockIcons[lockStatus];
    $("#tab-header-encounter-icon-"+id).removeClass(oldLockStatusIcon);
    $("#tab-header-encounter-icon-"+id).addClass(lockStatusIcon);
  }
  
  function initDxTypeAheads(id) {
      var clinician = App.clinician;
      var dxCode = new Bloodhound({
          datumTokenizer: function(icd9List) {
              return Bloodhound.tokenizers.whitespace(icd9List.value);
          },
          queryTokenizer: Bloodhound.tokenizers.whitespace,
          limit: 100,
          remote: {
              url: 'app/searchICD9?sessionId='+clinician.sessionId+'&searchText=%QUERY',
              filter: function(response) {
                  return response.icd9List;
              }
          }
      });
      dxCode.initialize();
      $('.icd9-typeahead').typeahead( { hint: true, highlight: true, limit: 10, minLength: 3 },
          {
              name: 'icd9List',
              displayKey: function(icd9List) {
                  return icd9List.code + " " + icd9List.codeText;
              },
              source: dxCode.ttAdapter()
          }).on('typeahead:selected', function($e, datum) {
              var str = $(this).attr("id");
              var res = parseInt(str.replace(/.+-.+-/i,''));
              var jsonData = JSON.stringify({
                  sessionId: clinician.sessionId,
                  dxCodeId: res,
                  updateProperty: "icd_9",
                  icd9Id: datum.id
              });
              $.post("patient/updateDxCode", {data:jsonData}, function(data) {

              });
          })
  }
  function onTypeaheadSelect($e, datum) {
    //console.log('selected');
    //console.log(datum);
  }

  module.initTxTypeAheads = function(id) {
      var clinician = App.clinician;
        var cpt = new Bloodhound({
            datumTokenizer: function(cptList) {
                return Bloodhound.tokenizers.whitespace(cptList.value);
            },
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            limit: 100,
            remote: {
                url: 'app/searchCPT?sessionId='+clinician.sessionId+'&searchText=%QUERY',
                filter: function(response) {
                    return response.cptList;
                }
            }
        });
        cpt.initialize();
        $('.cpt-typeahead').typeahead( { hint: true, highlight: true, minLength: 3 },
            {
                name: 'cptList',
                displayKey: function(cptList) {
                    return cptList.code + " " + cptList.description;
                },
                source: cpt.ttAdapter()
            }).on('typeahead:selected', function($e, datum) {
                var str = $(this).attr("id");
                var res = parseInt(str.replace(/.+-.+-/i,''));
                var jsonData = JSON.stringify({
                    sessionId: clinician.sessionId,
                    txCodeId: res,
                    updateProperty: "cpt",
                    cptId: datum.id
                });
                $.post("patient/updateTxCode", {data:jsonData}, function(data) {

                });
            });
    }

    function getPatientEncounter(encounterId) {}
  
  module.updateSavedPatientEncounter = function (property, value,
    encounterId, isDualMode,
    elementId, valueName) {
    var encounter = App.currentEncounter;
    if (encounter == undefined) {
      //NOT IMPLEMENTED
      encounter = getPatientEncounter(encounterId);
    }
    updateLocalPatientEncounter(property, value, encounter.patient.id);
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      patientId: encounter.patient.id,
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
  };

  module.saveVitalsEncounterForm = function (encounter) {
    var id = encounter.id;
    encounter.vitals.height = Util.processNumber("#encounter-height-" +
      id,
      encounter.vitals.height);
    encounter.vitals.weight = Util.processNumber("#encounter-weight-" +
      id, encounter.vitals.weight);
    encounter.vitals.systolic = Util.processNumber("#encounter-sys-" + id,
      encounter.vitals.systolic);
    encounter.vitals.diastolic = Util.processNumber("#encounter-dia-" +
      id,
      encounter.vitals.diastolic);
    encounter.vitals.pulse = Util.processNumber("#encounter-hr-" + id,
      encounter.vitals.pulse);
    encounter.vitals.respiration = Util.processNumber("#encounter-rr-" +
      id,
      encounter.vitals.respiration);
    encounter.vitals.temperature = Util.processNumber("#encounter-temp-" +
      id,
      encounter.vitals.temperature);
    encounter.vitalsSaved = true;
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      encounter: encounter
    });
    Ajax.post("patient/createVitals", {
      data: jsonData
    }, function (data) {
      PatientEncounterForm.renderFormSection(encounter, 'vitals',
        true, true);
    });
  };

  module.saveCCEncounterForm = function (encounter) {
    var id = encounter.id;
    encounter.cc.description = $.trim($("#encounter-chief-complaint-" +
      id).val());
    encounter.cc.occursWhen = $('input[name="encounter-occurs-when-' + id +
      '"]:checked').map(function () {
      return this.value;
    }).get().join(',');
    encounter.cc.occursOther = $.trim($("#encounter-occurs-other-" + id).val());
    encounter.cc.specificLocation = $.trim($(
      "#encounter-specific-location-" + id).val());
    encounter.cc.painType = $.trim($("#encounter-pain-type-" + id).val());
    encounter.cc.denies = $('input[name="encounter-denies-' + id +
      '"]:checked').map(function () {
      return this.value;
    }).get().join(',');
    encounter.cc.deniesOther = $.trim($("#encounter-denies-other-" + id).val());
    encounter.cc.howLongOther = $.trim($("#encounter-how-long-other-" +
      id).val());
    encounter.cc.painDuration = $.trim($("#encounter-pain-duration-" + id)
      .val());
    encounter.cc.hoursSince = Util.processNumber(
      "#encounter-hours-since-" + id, encounter.cc.hoursSince);
    encounter.cc.daysSince = Util.processNumber("#encounter-days-since-" +
      id,
      encounter.cc.daysSince);
    encounter.cc.weeksSince = Util.processNumber(
      "#encounter-weeks-since-" + id,
      encounter.cc.weeksSince);
    encounter.cc.monthsSince = Util.processNumber(
      "#encounter-months-since-" + id,
      encounter.cc.monthsSince);
    encounter.cc.yearsSince = Util.processNumber(
      "#encounter-years-since-" + id,
      encounter.cc.yearsSince);
    encounter.cc.painScale = Util.processNumber("#encounter-pain-scale-" +
      id,
      encounter.cc.painScale);
    encounter.cc.painXHour = Util.processNumber("#encounter-pain-x-hour-" +
      id,
      encounter.cc.painXHour);
    encounter.cc.painXDay = Util.processNumber("#encounter-pain-x-day-" +
      id, encounter.cc.painXDay);
    encounter.cc.painXWeek = Util.processNumber("#encounter-pain-x-week-" +
      id,
      encounter.cc.painXWeek);
    encounter.cc.painXMonth = Util.processNumber(
      "#encounter-pain-x-month-" +
      id, encounter.cc.painXMonth);
    encounter.ccSaved = true;
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      encounter: encounter
    });
    Ajax.post("patient/createCC", {
      data: jsonData
    }, function (data) {
      PatientEncounterForm.renderFormSection(encounter, 'cc', true,
        true);
    });
  };

  module.saveSOAPNoteEncounterForm = function (encounter) {
    var id = encounter.id;
    encounter.soapNote.subjective = $.trim($(
      "#encounter-soap-note-subjective-" +
      id).val());
    encounter.soapNote.objective = $.trim($(
      "#encounter-soap-note-objective-" + id).val());
    encounter.soapNote.assessment = $.trim($(
      "#encounter-soap-note-assessment-" +
      id).val());
    encounter.soapNote.plan = $.trim($("#encounter-soap-note-plan-" + id)
      .val());
    encounter.soapNoteSaved = true;
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      encounter: encounter,
      soapNoteSaved: true
    });
    Ajax.post("patient/createSOAPNote", {
      data: jsonData
    }, function (data) {
      PatientEncounterForm.renderFormSection(encounter, 'soap-note',
        true, true);
    });
  };

  module.saveOBGYNEncounterForm = function (encounter) {
    var id = encounter.id;
    encounter.obgyn.g = $.trim($("#encounter-obgyn-g-" + id).val());
    encounter.obgyn.p = $.trim($("#encounter-obgyn-p-" + id).val());
    encounter.obgyn.t = $.trim($("#encounter-obgyn-t-" + id).val());
    encounter.obgyn.a = $.trim($("#encounter-obgyn-a-" + id).val());
    encounter.obgyn.l = $.trim($("#encounter-obgyn-l-" + id).val());
    encounter.obgyn.pregStatus = $('input[name=encounter-pregnant-' + id +
      ']:checked').val();
    encounter.obgyn.breastfeeding = $(
      'input[name=encounter-breastfeeding-' + id +
      ']:checked').val();
    encounter.obgyn.breastfeedingMonths = $.trim($(
      "#encounter-breastfeeding-months-" +
      id).val());
    encounter.obgyn.lastPeriod = $.trim($("#encounter-last-period-" + id)
      .val());
    encounter.obgyn.ageFirstPeriod = $.trim($(
      "#encounter-age-first-period-" + id).val());
    encounter.obgyn.papSmearStatus = $('input[name=encounter-pap-smear-' +
      id +
      ']:checked').val();
    encounter.obgyn.birthControlStatus = $(
      'input[name=encounter-birth-control-' +
      id + ']:checked').val();
    encounter.obgyn.birthControlType = $.trim($(
      "#encounter-birth-control-type-" + id).val());
    encounter.obgyn.history = $('input[name="encounter-obgyn-hist-' + id +
      '"]:checked').map(function () {
      return this.value;
    }).get().join(',');
    encounter.obgyn.historyOther = $.trim($(
      "#encounter-obgyn-hist-other-" + id).val());
    encounter.obgynSaved = true;
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      encounter: encounter
    });
    Ajax.post("patient/createOBGYN", {
      data: jsonData
    }, function (data) {
      PatientEncounterForm.renderFormSection(encounter, 'obgyn', true,
        true);
    });
  };

  module.savePFSHEncounterForm = function (encounter) {
    var id = encounter.id;
    encounter.patient.pfsh.jobType = $.trim($("#encounter-job-type-" + id)
      .val());
    encounter.patient.pfsh.motherAlive = $(
      'input[name=encounter-mother-alive-' +
      id + ']:checked').val() == 'true';
    encounter.patient.pfsh.motherDeathReason = $.trim($(
      "#encounter-mother-death-reason-" +
      id).val());
    encounter.patient.pfsh.fatherAlive = $(
      'input[name=encounter-father-alive-' +
      id + ']:checked').val() == 'true';
    encounter.patient.pfsh.fatherDeathReason = $.trim($(
      "#encounter-mother-death-reason-" +
      id).val());
    encounter.patient.pfsh.partnerAlive = $(
      'input[name=encounter-partner-alive-' +
      id + ']:checked').val() == 'true';
    encounter.patient.pfsh.partnerDeathReason = $.trim($(
      "#encounter-partner-death-reason-" +
      id).val());
    encounter.patient.pfsh.numResidents = Util.processNumber(
      "#encounter-num-residents-" +
      id, encounter.patient.pfsh.numResidents);
    encounter.patient.pfsh.numSiblings = Util.processNumber(
      "#encounter-num-siblings-" +
      id, encounter.patient.pfsh.numSiblings);
    encounter.patient.pfsh.numBrothers = Util.processNumber(
      "#encounter-num-brothers-" + id,
      encounter.patient.pfsh.numBrothers);
    encounter.patient.pfsh.numSisters = Util.processNumber(
      "#encounter-num-sisters-" + id, encounter.patient.pfsh.numSisters
    );
    encounter.patient.pfsh.numChildren = Util.processNumber(
      "#encounter-num-children-" +
      id, encounter.patient.pfsh.numChildren);
    encounter.patient.pfsh.numSons = Util.processNumber(
      "#encounter-num-sons-" +
      id, encounter.patient.pfsh.numSons);
    encounter.patient.pfsh.numDaughters = Util.processNumber(
      "#encounter-num-daughters-" +
      id, encounter.patient.pfsh.numDaughters);
    encounter.pfshSaved = true;
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      encounter: encounter
    });
    Ajax.post("patient/createPFSH", {
      data: jsonData
    }, function (data) {
      PatientEncounterForm.renderFormSection(encounter, 'pfsh', true,
        true);
    });
  };

  module.saveSuppEncounterForm = function (encounter) {
    var id = encounter.id;
    encounter.supp.waterSource = $.trim($("#encounter-water-source-" + id)
      .val());
    encounter.supp.numCupsCoffee = Util.processNumber(
      "#encounter-num-cups-coffee" +
      id, encounter.supp.numCupsCoffee);
    encounter.supp.numCupsTea = Util.processNumber(
      "#encounter-num-cups-tea-" +
      id, encounter.supp.numCupsTea);
    encounter.supp.numCupsWater = Util.processNumber(
      "#encounter-num-cups-water-" +
      id, encounter.supp.numCupsWater);
    for (i = 0; i < encounter.supp.encounterQuestionList.length; i++) {
      var questionId = encounter.supp.encounterQuestionList[i].id;
      encounter.supp.encounterQuestionList[i].question = $(
        "#encounter-question-" +
        questionId).val();
      encounter.supp.encounterQuestionList[i].response = $(
        "#encounter-response-" +
        questionId).val();
    }
    encounter.suppSaved = true;
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      encounter: encounter
    });
    Ajax.post("patient/createSupp", {
      data: jsonData
    }, function (data) {
      PatientEncounterForm.renderFormSection(encounter, 'supp', true,
        true);
    });
  };

  module.saveHistEncounterForm = function (encounter) {
    var id = encounter.id;
    for (i = 0; i < encounter.patient.hist.patientMedicationList.length; i++) {
      var medicationId = encounter.patient.hist.patientMedicationList[i].id;
      encounter.patient.hist.patientMedicationList[i].medication = $(
        "#patient-med-" +
        medicationId).val();
      encounter.patient.hist.patientMedicationList[i].dose = $(
        "#patient-dose-" +
        medicationId).val();
      encounter.patient.hist.patientMedicationList[i].frequency = $(
        "#patient-freq-" +
        medicationId).val();
    }
    encounter.patient.hist.pastSM = $.trim($("#encounter-past-s-m-" + id)
      .val());
    encounter.patient.hist.famHist = $('input[name="encounter-fam-hist-' +
      id +
      '"]:checked').map(function () {
      return this.value;
    }).get().join(',');
    encounter.patient.hist.famHistOther = $.trim($(
      "#encounter-fam-hist-other-" +
      id).val());
    encounter.patient.hist.famHistNotes = $.trim($(
      "#encounter-fam-hist-notes-" + id).val());
    encounter.patient.hist.allergFood = $.trim($(
      "#encounter-allerg-food-" + id).val());
    encounter.patient.hist.allergDrug = $.trim($(
      "#encounter-allerg-drug-" + id).val());
    encounter.patient.hist.allergEnv = $.trim($("#encounter-allerg-env-" +
      id).val());
    encounter.patient.hist.vacc = $('input[name=encounter-vacc-' + id +
      ']:checked').val() == 'true';
    encounter.patient.hist.vaccNotes = $.trim($("#encounter-vacc-notes-" +
      id).val());
    encounter.patient.hist.subst = $('input[name="encounter-subst-' + id +
      '"]:checked').map(function () {
      return this.value;
    }).get().join(',');
    encounter.patient.hist.currentDrugs = $.trim($(
      "#encounter-current-drugs-" +
      id).val());
    encounter.patient.hist.smokePksDay = Util.processNumber(
      "#encounter-smoke-pks-day-" +
      id, encounter.patient.hist.smokePksDay);
    encounter.patient.hist.yearsSmoked = Util.processNumber(
      "#encounter-years-smoked-" + id, encounter.patient.hist.yearsSmoked
    );
    encounter.patient.hist.smokeYearsQuit = Util.processNumber(
      "#encounter-smoke-years-quit-" +
      id, encounter.patient.hist.smokeYearsQuit);
    encounter.patient.hist.etohUnitsWeek = Util.processNumber(
      "#encounter-etoh-units-week-" +
      id, encounter.patient.hist.etohUnitsWeek);
    encounter.histSaved = true;
    jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      encounter: encounter
    });
    Ajax.post("patient/createHist", {
      data: jsonData
    }, function (data) {
      PatientEncounterForm.renderFormSection(encounter, 'hist', true,
        true);
    });
  };

  module.saveExamEncounterForm = function (encounter) {
    var id = encounter.id;
    for (i = 0; i < encounter.dxCodes.length; i++) {
      var dxCodeId = encounter.dxCodes[i].id;
      encounter.dxCodes[i].icd9 = $("#encounter-icd9-" + dxCodeId).val();
    }
    for (i = 0; i < encounter.txCodes.length; i++) {
      var txCodeId = encounter.txCodes[i].id;
      encounter.txCodes[i].cpt = $("#encounter-cpt-" + txCodeId).val();
      encounter.txCodes[i].cptModifier = $("#encounter-cpt-modifier-" +
        txCodeId).val();
    }
    encounter.exam.hs = $.trim($("#encounter-hs-" + id).val());
    encounter.exam.heartRhythm = $('input[name=encounter-heart-rhythm-' +
      id + ']:checked').val();
    encounter.exam.hb = $.trim($("#encounter-lab-hb-" + id).val());
    encounter.exam.glucose = $.trim($("#encounter-lab-glucose-" + id).val());
    encounter.exam.urineDIP = $.trim($("#encounter-lab-urine-dip-" + id).val());
    encounter.exam.diagnosis = $.trim($("#encounter-diagnosis-" + id).val());
    encounter.exam.treatmentPlan = $.trim($("#encounter-treatment-plan-" +
      id).val());
    encounter.examSaved = true;
    jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      encounter: encounter
    });
    Ajax.post("patient/createExam", {
      data: jsonData
    }, function (data) {
      PatientEncounterForm.renderFormSection(encounter, 'exam', true,
        true);
    });
  };

  module.saveFollowUpEncounterForm = function (encounter) {
    var id = encounter.id;
    encounter.followUp.level = $('input[name=encounter-follow-up-level-' +
      id +
      ']:checked').val();
    encounter.followUp.when = $.trim($("#encounter-follow-up-when-" + id)
      .val());
    encounter.followUp.condition = $.trim($(
      "#encounter-follow-up-condition-" +
      id).val());
    encounter.followUp.dispenseRx = $.trim($(
      "#encounter-follow-up-dispense-rx-" +
      id).val());
    encounter.followUp.USS = $.trim($("#encounter-follow-up-uss-" + id).val());
    encounter.followUp.pregnant = $.trim($(
      "#encounter-follow-up-pregnant-" +
      id).val());
    encounter.followUp.woundCare = $.trim($(
      "#encounter-follow-up-wound-care-" +
      id).val());
    encounter.followUp.refToSpecialist = $.trim($(
      "#encounter-follow-up-ref-to-specialist-" +
      id).val());
    encounter.followUp.dentalList = $.trim($(
      "#encounter-follow-up-dental-list-" +
      id).val());
    encounter.followUp.physiotherapy = $.trim($(
      "#encounter-follow-up-physiotherapy-" + id).val());
    encounter.followUp.bloodLabs = $.trim($(
      "#encounter-follow-up-blood-labs-" +
      id).val());
    encounter.followUp.other = $.trim($("#encounter-follow-up-other-" +
      id).val());
    encounter.followUp.pulmonaryFXTest = $.trim($(
      "#encounter-follow-up-pulmonary-fx-test-" +
      id).val());
    encounter.followUp.vision = $.trim($("#encounter-follow-up-vision-" +
      id).val());
    encounter.followUp.completed = $(
        'input[name=encounter-follow-up-completed--' + id + ']:checked').val() ==
      'true';
    encounter.followUp.notes = $.trim($("#encounter-follow-up-notes-" +
      id).val());
    encounter.followUp.followUpDate = Util.processDate(
      '#encounter-follow-up-date-saved-' +
      id, encounter.followUp.followUpDate);
    encounter.followUpSaved = true;
    jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      encounter: encounter
    });
    Ajax.post("patient/createFollowUp", {
      data: jsonData
    }, function (data) {
      PatientEncounterForm.renderFormSection(encounter, 'follow-up',
        true, true);
    });
  };

  function updateLocalPatientEncounter(property, value, patientId) {
    App.currentEncounter[property] = value;
  }

  function updateDxCode(icd9, dxCodeId) {
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      dxCodeId: dxCodeId,
      icd9: icd9
    });
    Ajax.post("patient/updateDxCode", {
      data: jsonData
    }, function (data) {});
  }

  function updateTxCode(cpt, cptModifier, txCodeId) {
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      txCodeId: txCodeId,
      cpt: cpt,
      cptModifier: cptModifier
    });
    Ajax.post("patient/updateTxCode", {
      data: jsonData
    }, function (data) {});
  }

  function updateEncounterQuestion(property, value, encounterQuestionId) {
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      encounterQuestionId: encounterQuestionId,
      updateProperty: property,
      updatePropertyValue: value
    });
    Ajax.post("patient/updateEncounterQuestion", {
      data: jsonData
    }, function (data) {});
  }

  function initEncounterExamCanvas(id) {
    canvas = document.getElementById('encounter-exam-image-overlay-' + id);
    ctx = canvas.getContext("2d");
    w = canvas.width;
    h = canvas.height;
    canvas.addEventListener("mousemove", function (e) {
      findEncounterExamCanvasXY('move', e);
    }, false);
    canvas.addEventListener("mousedown", function (e) {
      findEncounterExamCanvasXY('down', e);
    }, false);
    canvas.addEventListener("mouseup", function (e) {
      findEncounterExamCanvasXY('up', e);
    }, false);
    canvas.addEventListener("mouseout", function (e) {
      findEncounterExamCanvasXY('out', e);
    }, false);
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
      $('#encounter-' + section + '-panel-' + id +
        ' .form-control-unsaved').css({
        display: "none"
      });
      $('#encounter-' + section + '-panel-' + id + ' .form-control-saved')
        .css({
          display: "block"
        });
      $('#encounter-' + section + '-panel-' + id + ' input:checkbox').attr(
        "disabled",
        "disabled");
      $('#encounter-' + section + '-panel-' + id + ' input:checkbox').attr(
        "readonly",
        "readonly");
      $('#encounter-' + section + '-panel-' + id + ' input:radio').attr(
        "disabled",
        "disabled");
      $('#encounter-' + section + '-panel-' + id + ' input:radio').attr(
        "readonly",
        "readonly");
      $('#encounter-' + section + '-save-' + id).css({
        display: "none"
      });
      $('#encounter-' + section + '-saved-' + id).css({
        display: "none"
      });
      $('#encounter-' + section + '-clear-' + id).css({
        display: "none"
      });
      $('#encounter-' + section + '-panel-' + id + ' .form-control-saved')
        .removeAttr('contenteditable');
      $('#encounter-' + section + '-panel-' + id + ' .form-control-saved')
        .css({
          border: "none"
        });
      $('#encounter-' + section + '-panel-' + id + ' .form-control-saved')
        .removeClass('form-control-saved').addClass('form-control-closed');
      $('#encounter-demo-photo-upload-control-' + id).css({
        display: "none"
      });
      $('#encounter-pain-scale-value-' + id).css({
        display: "inline"
      });
      $(".slider-track").css({
        display: "none"
      });
      $('.dual-mode-unsaved').css({
        display: "none"
      });
      $('.dual-mode-saved').css({
        display: "inline"
      });
      $('.dual-mode-saved').off("click");
      return;
    }
    $('#encounter-' + section + '-panel-' + id + ' .form-control-unsaved')
      .css({
        display: (isSaved == true ?
          "none" : "block")
      });
    $('#encounter-' + section + '-panel-' + id + ' .form-control-saved').css({
      display: (isSaved == true ? "block" : "none")
    });
    $('#encounter-' + section + '-save-' + id).css({
      display: (hasOwnership ==
        true && isSaved == false ? "inline-block" : "none")
    });
    $('#encounter-' + section + '-saved-' + id).css({
      display: (hasOwnership ==
        true && isSaved == true ? "inline-block" : "none")
    });
    $('#encounter-' + section + '-clear-' + id).css({
      display: (hasOwnership ==
        true && isSaved == false ? "inline-block" : "none")
    });
    $('#encounter-' + section + '-panel-' + id + ' .form-control-unsaved')
      .prop("readonly", !hasOwnership);
    if (hasOwnership == false) {
      $('#encounter-' + section + '-panel-' + id + ' .form-control-saved')
        .removeAttr('contenteditable').blur();
      $('#encounter-demo-photo-upload-control-' + id).css({
        display: "none"
      });
    } else {
      $('#encounter-demo-photo-upload-control-' + id).css({
        display: "block"
      });
      $('#encounter-' + section + '-panel-' + id + ' .form-control-saved')
        .prop('contenteditable',
          true);
      $('#encounter-' + section + '-panel-' + id +
        ' .form-control-readonly').removeAttr('contenteditable').blur();
    }
  }

  function setupPictureUpload(encounterId, patientId) {
    $('#encounter-demo-photo-upload-' + encounterId).click(function () {
      $('#encounter-demo-photo-upload-progress-' + encounterId +
        ' .progress-bar').css('width',
        '0');
    });
    uploader = new qq.FileUploader({
      element: document.getElementById('encounter-demo-photo-upload-' +
        encounterId),
      action: 'patient/uploadProfileImage?patientId=' + patientId +
        '&sessionId=' + App.clinician.sessionId,
      debug: true,
      sizeLimit: 1048576,
      allowedExtensions: ['jpg', 'jpeg', 'png', 'gif'],
      onProgress: function (id,
        fileName, loaded, total) {
        var progress = parseInt(loaded / total * 100, 10);

        $('#encounter-demo-photo-upload-progress-' + encounterId +
          ' .progress-bar').css('width',
          progress + '%');
      },
      onComplete: function (id, fileName, responseJSON) {
        $('#encounter-demo-photo-upload-progress-' + encounterId +
          ' .progress-bar').css('width',
          '100%');
        var response = parsedData = $.parseJSON(responseJSON);
        var path = response.filename;
        var patientId = response.patientId;
        $("#encounter-demo-photo-" + encounterId).attr("src", App.patientChart
          .chartHeadshot);
      }
    });
  }

  function getPatientEncounters() {
    var jsonData = JSON.stringify({
      patientId: App.currentPatientId,
      sessionId: App.clinician.sessionId
    });
    Ajax.post("patient/getPatientEncounters", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      App.patientEncounters = parsedData.patientEncounters;
    });
  }

  module.viewEncounter = function(encounterId) {
    var jsonData = JSON.stringify({
      encounterId: encounterId,
      sessionId: App.clinician.sessionId
    });
    Ajax.post("patient/getEncounter", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      var currentEncounter = App.currentEncounter = parsedData.encounter;
      var args = {
        isActive: true,
        id: currentEncounter.id
      };
      RenderUtil.render('encounter', args, function (s) {
        $('#modals-placement').html(s);
        $('#modal-encounter').modal('show');
          $('#modal-encounter').on('shown.bs.modal', function() {
              Util.debug("encounter form showing");
              $( ".cpt-modifier" ).on("change", function() {
                  var str = $(this).attr("id");
                  var res = parseInt(str.replace(/.+-.+-/i,''));
                  var jsonData = JSON.stringify({
                      sessionId: App.clinician.sessionId,
                      txCodeId: res,
                      updateProperty: "cptModifier",
                      cptModifierId: parseInt($(this).val())
                  });
                  $.post("patient/updateTxCode", {data:jsonData}, function(data) {

                  });
              });
          });
        $('#app-encounter-close-record').css({
          display: (currentEncounter.completed ?
            "none" : "inline-block")
        });
        setupCloseRecordButton();
        PatientEncounterForm.renderForm(currentEncounter,
          true);
        $('#app-encounter-print-all').click(function () {
          var currentDate = dateFormat(new Date(),
            'mm/dd/yyyy');
          RenderUtil.render('print/Printer.encounter_all', {
            encounter: currentEncounter,
            currentDate: currentDate
          }, function (obj) {
            var s = obj[0].outerHTML;
            Printer.openPrintWindow('print.html', s,
              'ENCOUNTER FORM');
          });
        });
      });
    });
  }

  function getCurrentEncounterFromEncounters() {
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      patientId: App.currentPatientId
    });
    Ajax.post("patient/getCurrentPatientEncounter", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      var oldEncounter = parsedData.encounter;
      return oldEncounter;
    });
  }

  module.newEncounterFormDialog = function() {
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId,
      patientId: App.currentPatientId
    });
    Ajax.post("patient/getCurrentPatientEncounter", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      var oldEncounter = App.oldEncounter = parsedData.encounter;
      if (oldEncounter == undefined || oldEncounter.completed ==
        true) {
        getEncountersListing();
        newEncounterForm();
        return;
      }
      var args = {
        modalTitle: "Complete Encounter Confirmation",
        modalH3: "Ready To Complete The Currently Open Encounter?",
        modalH4: "In order to start a new encounter the current one needs to be completed.",
        cancelButton: 'Cancel',
        okButton: 'Confirm'
      };
      RenderUtil.render('dialog/confirm', args, function (s) {
        $('#modals-placement').append(s);
        $('#modal-confirm').modal('show');
        $('#app-modal-confirmation-btn').click(function () {
          var jsonData = JSON.stringify({
            sessionId: App.clinician.sessionId,
            encounterId: App.oldEncounter.id
          });
          Ajax.post("patient/closeEncounter", {
            data: jsonData
          }, function (data) {
            var parsedData = $.parseJSON(data);
            Notifier.notify(
              'Patient Encounter Record Completed');
            App.oldEncounter.completed = true;
            $('#modal-encounter').modal('hide');
            getEncountersListing();
            newEncounterForm();
          });
        });
      });
    });
  }

  function newEncounterForm() {
    var jsonData = JSON.stringify({
      patientId: App.currentPatientId,
      sessionId: App.clinician.sessionId
    });
    Ajax.post("patient/newEncounter", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      App.currentEncounter = parsedData.encounter;
      var args = {
        isActive: true,
        id: App.currentEncounter.id
      };
      RenderUtil.render('encounter', args, function (s) {
        $('#modals-placement').html(s);
        $('#modal-encounter').modal('show');
        $('#app-encounter-close-record').css({
          display: "inline-block"
        });
        setupCloseRecordButton();
        PatientEncounterForm.renderForm(App.currentEncounter,
          true);
        $('#app-encounter-print-all').click(function () {
          var currentDate = dateFormat(new Date(),
            'mm/dd/yyyy');
          RenderUtil.render('print/Printer.encounter_all', {
            encounter: App.currentEncounter,
            currentDate: currentDate
          }, function (obj) {
            var s = obj[0].outerHTML;
            Printer.openPrintWindow('print.html', s,
              'ENCOUNTER FORM');
          });
        });
      });
    });
  }

  function setupCloseRecordButton() {
    $('#app-encounter-close-record').click(function () {
      var args = {
        modalTitle: "Complete Encounter Confirmation",
        modalH3: "Ready To Complete The Encounter?",
        modalH4: "Once completed, the encounter is locked.",
        cancelButton: 'Cancel',
        okButton: 'Confirm'
      };
      RenderUtil.render('dialog/confirm', args, function (s) {
        $('#modals-placement').append(s);
        $('#modal-confirm').modal('show');
        $('#app-modal-confirmation-btn').click(function () {
          var jsonData = JSON.stringify({
            sessionId: App.clinician.sessionId,
            encounterId: App.currentEncounter.id
          });
          Ajax.post("patient/closeEncounter", {
            data: jsonData
          }, function (data) {
            var parsedData = $.parseJSON(data);
            Notifier.notify(
              'Patient Encounter Record Completed');
            App.currentEncounter.completed = true;
            $('#modal-encounter').modal('hide');
            App.oldEncounter.completed = true;
            getEncountersListing();
          });
        });
      });
    });
  }

  function getEncountersListing() {
    var jsonData = JSON.stringify({
      patientId: App.currentPatientId,
      sessionId: App.clinician.sessionId
    });
    Ajax.post("patient/getPatientEncounters", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      var encounters = App.encounters = parsedData.patientEncounters;
      RenderUtil.render('component/simple_data_table', {
        items: encounters,
        title: 'Encounter History',
        tableName: 'chart-encounters-list',
        clickable: true,
        columns: [{
          title: 'Date',
          field: 'date',
          type: 'date'
        }, {
          title: 'Clinician',
          field: 'clinician.firstName',
          type: 'double-person'
        }, {
          title: 'Completed',
          field: 'completed',
          type: 'simple'
        }, {
          title: 'Notes',
          field: 'notes',
          type: 'simple'
        }]
      }, function (s) {
        $('#chart-encounters-list').html(s);
        $('#chart-encounters-list-title').html(
          "Encounter History");
        $('#encounter-view-button').click(function () {
          viewEncounter(App.currentEncounter.id);
        });
        $('.clickable-table-row').click(function (e) {
          $(this).addClass('table-row-highlight').siblings().removeClass(
            'table-row-highlight');
          Table.handleClickableRow(e);
        });
      });
    });
  }
  return module;
});