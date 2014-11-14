
/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

"use strict";

modulejs.define('patient/encounter_form', 
                ["printer","render_util", "util"], 
                function (Printer, RenderUtil, Util) {
  
  var module = {};
  
  var PatientEncounter;
  
  function patient_encounter() {
    if (!PatientEncounter) {
      PatientEncounter = modulejs.require("patient/encounter");
    }
    return PatientEncounter;
  }
  
  function renderPatientEncounterForm(encounter, hasOwnership) {
    var id = encounter.id;
    var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
    renderEncounterFormSection(encounter, 'demo', encounter.demoSaved,
      hasOwnership);
    renderEncounterFormSection(encounter, 'vitals', encounter.vitalsSaved,
      hasOwnership);
    renderEncounterFormSection(encounter, 'soap-note', encounter.soapNoteSaved,
      hasOwnership);
    renderEncounterFormSection(encounter, 'cc', encounter.ccSaved,
      hasOwnership);
    renderEncounterFormSection(encounter, 'obgyn', encounter.obgynSaved,
      hasOwnership);
    renderEncounterFormSection(encounter, 'pfsh', encounter.patient.pfsh.saved,
      hasOwnership);
    renderEncounterFormSection(encounter, 'supp', encounter.suppSaved,
      hasOwnership);
    renderEncounterFormSection(encounter, 'hist', encounter.patient.hist.saved,
      hasOwnership);
    renderEncounterFormSection(encounter, 'exam', encounter.examSaved,
      hasOwnership);
    renderEncounterFormSection(encounter, 'follow-up', encounter.followUpSaved,
      hasOwnership);
    $(".edit-on-select").focus(function () {
      $(this).selectContentEditableText();
    });
    $('#encounter-vitals-save-' + id).click(function () {
      patient_encounter().saveVitalsEncounterForm(encounter);
    });
    $('#encounter-soap-note-save-' + id).click(function () {
      patient_encounter().saveSOAPNoteEncounterForm(encounter);
    });
    $('#encounter-cc-save-' + id).click(function () {
      patient_encounter().saveCCEncounterForm(encounter);
    });
    $('#encounter-obgyn-save-' + id).click(function () {
      patient_encounter().saveOBGYNEncounterForm(encounter);
    });
    $('#encounter-pfsh-save-' + id).click(function () {
      patient_encounter().savePFSHEncounterForm(encounter);
    });
    $('#encounter-supp-save-' + id).click(function () {
      patient_encounter().saveSuppEncounterForm(encounter);
    });
    $('#encounter-hist-save-' + id).click(function () {
      patient_encounter().saveHistEncounterForm(encounter);
    });
    $('#encounter-exam-save-' + id).click(function () {
      patient_encounter().saveExamEncounterForm(encounter);
    });
    $('#encounter-follow-up-save-' + id).click(function () {
      patient_encounter().saveFollowUpEncounterForm(encounter);
    });
    setupPictureUpload(encounter.id, encounter.patient.id);
    $('#encounter-demo-print-' + id).click(function () {
      Printer.printEncounterForm('print_encounter_demographics',
        'DEMOGRAPHICS');
    });
    $('#encounter-vitals-print-' + id).click(function () {
      Printer.printEncounterForm('print_encounter_vitals', 'VITALS');
    });
    $('#encounter-soap-note-print-' + id).click(function () {
      Printer.printEncounterForm('print_encounter_soap_note',
        'SOAP NOTE');
    });
    $('#encounter-cc-print-' + id).click(function () {
      Printer.printEncounterForm('print_encounter_cc',
        'CHIEF COMPLAINT');
    });
    $('#encounter-obgyn-print-' + id).click(function () {
      Printer.printEncounterForm('print_encounter_obgyn', 'OBGYN');
    });
    $('#encounter-pfsh-print-' + id).click(function () {
      Printer.printEncounterForm('print_encounter_pfsh',
        'SOCIAL & FAMILY HISTORY');
    });
    $('#encounter-supp-print-' + id).click(function () {
      Printer.printEncounterForm('print_encounter_supp',
        'SUPPLEMENTAL QUESTIONS');
    });
    $('#encounter-hist-print-' + id).click(function () {
      Printer.printEncounterForm('print_encounter_hist',
        'MEDICAL HISTORY');
    });
    $('#encounter-exam-print-' + id).click(function () {
      Printer.printEncounterForm('print_encounter_exam', 'EXAM');
    });
    $('#encounter-follow-up-print-' + id).click(function () {
      Printer.printEncounterForm('print_encounter_follow-up',
        'FOLLOW UP');
    });
    RenderUtil.render('component/basic_select_options', {
      options: App.usStates,
      collection: 'App.usStates'
    }, function (s) {
      var id = App.currentEncounter.id;
      $('#encounter-demo-us-state-' + id).html(s);
    });
    renderCPTModifiers(id, '.cpt-modifier');
  }

  function renderEncounterFormSection(encounter, section, savedState,
    hasOwnership) {
    var id = encounter.id;
    var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
    var notEditable = true;
    setEncounterFormMode(encounter, section, savedState, hasOwnership);
    var clinician = App.clinician;
    if (section == 'demo') {
      if (savedState == false) {
        $('#encounter-demo-govt-id-' + id).mask("999-99-9999");
        $('#encounter-demo-dob-' + id).mask("99/99/9999");
        $('#encounter-demo-postal-code-' + id).mask("99999");
      } else if (savedState == true) {
        $('#encounter-demo-date-saved-' + id).html(dateFormat(encounter.date,
          'mm/dd/yyyy'));
        $('#encounter-demo-mrn-saved-' + id).html(encounter.patient.cred.mrn);
        $("#encounter-demo-photo-" + id).attr("src", App.patientChartHeadshot);
        $('#encounter-demo-first-name-saved-' + id).html(encounter.patient
          .cred.firstName);
        $('#encounter-demo-middle-name-saved-' + id).html(encounter.patient
          .cred.middleName);
        $('#encounter-demo-last-name-saved-' + id).html(encounter.patient
          .cred.lastName);
        $('#encounter-demo-govt-id-saved-' + id).html(encounter.patient.cred
          .govtId);
        var dob = dateFormat(encounter.patient.demo.dob, 'mm/dd/yyyy');
        $('#encounter-demo-dob-saved-' + id).html(dob);
        $('#encounter-demo-gender-saved-' + id).html(encounter.patient.demo
          .gender.code);
        $('#encounter-demo-marital-saved-' + id).html(encounter.patient.demo
          .maritalStatus.name);
        $('#encounter-demo-race-saved-' + id).html(encounter.patient.demo
          .race.name);
        $('#encounter-demo-ethnicity-saved-' + id).html(encounter.patient
          .demo.ethnicity.name);
        $('#encounter-demo-school-status-saved-' + id).html(encounter.patient
          .demo.schoolStatus);
        $('#encounter-demo-employment-status-saved-' + id).html(encounter
          .patient.demo.employmentStatus);
        $('#encounter-demo-notes-saved-' + id).html(encounter.notes);
        $('#encounter-demo-street-address-saved-' + id).html(encounter.patient
          .demo.streetAddress1);
        $('#encounter-demo-city-saved-' + id).html(encounter.patient.demo
          .city);
        $('#encounter-demo-us-state-saved-' + id).html(encounter.patient.demo
          .usState.name);
        $('#encounter-demo-postal-code-saved-' + id).html(encounter.patient
          .demo.postalCode);
        $('#encounter-demo-email-saved-' + id).html(encounter.patient.cred
          .email);
        $('#encounter-demo-primary-phone-saved-' + id).html(encounter.patient
          .demo.primaryPhone);
        $('#encounter-demo-secondary-phone-saved-' + id).html(encounter.patient
          .demo.secondaryPhone);
        $('#encounter-demo-occupation-saved-' + id).html(encounter.patient
          .demo.occupation);
        $('#encounter-demo-employer-saved-' + id).html(encounter.patient.demo
          .employer);
        $('#encounter-demo-school-name-saved-' + id).html(encounter.patient
          .demo.schoolName);
        $('#encounter-demo-first-name-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("firstName", $(
              this).html(),
            id);
        });
        $('#encounter-demo-middle-name-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("middleName",
            $(this).html(),
            id);
        });
        $('#encounter-demo-last-name-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("lastName", $(
              this).html(),
            id);
        });
        $('#encounter-demo-govt-id-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("govtId", $(
              this).val(),
            id);
        });
        $('#encounter-demo-notes-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("notes", $(
              this).html(),
            id);
        });
        $('#encounter-demo-dob-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("dob", $(this)
            .html(),
            id);
        });
        $('#encounter-demo-street-address-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "streetAddress1", $(this).html(),
            id);
        });
        $('#encounter-demo-city-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("city", $(this)
            .html(),
            id);
        });
        $('#encounter-demo-postal-code-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("postalCode",
            $(this).html(),
            id);
        });
        $('#encounter-demo-email-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("email", $(
              this).html(),
            id);
        });
        $('#encounter-demo-primary-phone-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("primaryPhone",
            $(this).html(),
            id);
        });
        $('#encounter-demo-secondary-phone-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "secondaryPhone", $(this).html(),
            id);
        });
        $('#encounter-demo-occupation-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("occupation",
            $(this).html(),
            id);
        });
        $('#encounter-demo-employer-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("employer", $(
              this).html(),
            id);
        });
        $('#encounter-demo-school-name-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("schoolName",
            $(this).html(),
            id);
        });
      }
    } else if (section == 'soap-note') {
      if (savedState == false) {} else if (savedState == true) {
        $('#encounter-soap-note-subjective-saved-' + id).html(encounter.soapNote
          .subjective);
        $('#encounter-soap-note-objective-saved-' + id).html(encounter.soapNote
          .objective);
        $('#encounter-soap-note-assessment-saved-' + id).html(encounter.soapNote
          .assessment);
        $('#encounter-soap-note-plan-saved-' + id).html(encounter.soapNote
          .plan);
        $('#encounter-soap-note-subjective-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("subjective",
            $(this).html(),
            id);
        });
        $('#encounter-soap-note-objective-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("objective", $(
              this).html(),
            id);
        });
        $('#encounter-soap-note-assessment-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("assessment",
            $(this).html(),
            id);
        });
        $('#encounter-soap-note-plan-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("plan", $(this)
            .html(),
            id);
        });
      }
    } else if (section == 'vitals') {
      if (savedState == false) {
        $("#encounter-height-" + id + ", #encounter-weight-" + id +
          ", #encounter-temp-" + id).keydown(function (e) {
          Util.filterDecimalInput(e);
        });
      } else if (savedState == true) {
        $('#encounter-height-saved-' + id).html(encounter.vitals.height);
        $('#encounter-weight-saved-' + id).html(encounter.vitals.weight);
        $('#encounter-sys-saved-' + id).html(encounter.vitals.systolic);
        $('#encounter-dia-saved-' + id).html(encounter.vitals.diastolic);
        $('#encounter-hr-saved-' + id).html(encounter.vitals.pulse);
        $('#encounter-rr-saved-' + id).html(encounter.vitals.respiration);
        $('#encounter-temp-saved-' + id).html(encounter.vitals.temperature);
        $('#encounter-height-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("height", $(
              this).html(),
            id);
        });
        $('#encounter-weight-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("weight", $(
              this).html(),
            id);
        });
        $('#encounter-sys-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("systolic", $(
              this).html(),
            id);
        });
        $('#encounter-dia-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("diastolic", $(
              this).html(),
            id);
        });
        $('#encounter-hr-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("pulse", $(
              this).html(),
            id);
        });
        $('#encounter-rr-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("respiration",
            $(this).html(),
            id);
        });
        $('#encounter-temp-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("temp", $(this)
            .html(),
            id);
        });
      }
    } else if (section == 'cc') {
      if (savedState == false) {
        $("#encounter-hours-since-" + id + ", #encounter-days-since-" +
          id +
          ", #encounter-weeks-since-" + id +
          ", #encounter-months-since-" + id +
          ", #encounter-years-since-" + id).keydown(function (e) {
          Util.filterDecimalInput(e);
        });
        $("#encounter-pain-x-hour-" + id + ", #encounter-pain-x-day-" +
          id +
          ", #encounter-pain-x-week-" + id +
          ", #encounter-pain-x-month-" + id).keydown(function (e) {
          Util.filterDecimalInput(e);
        });
        $('#encounter-pain-scale-' + id).slider({
          value: encounter.cc.painScale
        }).on('slide',
          function (ev) {
            $('#encounter-pain-scale-value-' + id).html(this.value);
            patient_encounter().updateSavedPatientEncounter("painScale",
              this.value,
              id);
          });
        $('#encounter-pain-scale-value-' + id).html(encounter.cc.painScale);
      } else if (savedState == true) {
        $('#encounter-pain-scale-' + id).slider({
          value: encounter.cc.painScale
        }).on('slide',
          function (ev) {
            $('#encounter-pain-scale-value-' + id).html(this.value);
            patient_encounter().updateSavedPatientEncounter("painScale",
              this.value,
              id);
          });
        $('#encounter-pain-scale-value-' + id).html(encounter.cc.painScale);
        $('#encounter-chief-complaint-saved-' + id).html(encounter.cc.description);
        $('#encounter-specific-location-saved-' + id).html(encounter.cc.specificLocation);
        Util.selectCheckboxesFromList(encounter.cc.occursWhen,
          'encounter-occurs-when-' + id);
        $('#encounter-hours-since-saved-' + id).html(encounter.cc.hoursSince);
        $('#encounter-days-since-saved-' + id).html(encounter.cc.daysSince);
        $('#encounter-weeks-since-saved-' + id).html(encounter.cc.weeksSince);
        $('#encounter-months-since-saved-' + id).html(encounter.cc.monthsSince);
        $('#encounter-years-since-saved-' + id).html(encounter.cc.yearsSince);
        $('#encounter-how-long-other-saved-' + id).html(encounter.cc.howLongOther);
        $('#encounter-pain-scale-saved-' + id).html(encounter.cc.painScale);
        $('#encounter-pain-type-saved-' + id).html(encounter.cc.painType);
        $('#encounter-pain-x-hour-saved-' + id).html(encounter.cc.painXHour);
        $('#encounter-pain-x-day-saved-' + id).html(encounter.cc.painXDay);
        $('#encounter-pain-x-week-saved-' + id).html(encounter.cc.painXWeek);
        $('#encounter-pain-x-month-saved-' + id).html(encounter.cc.painXMonth);
        $('#encounter-pain-duration-saved-' + id).html(encounter.cc.painDuration);
        Util.selectCheckboxesFromList(encounter.cc.denies,
          'encounter-denies-' + id);
        $('#encounter-denies-other-saved-' + id).html(encounter.cc.deniesOther);
        $('#encounter-chief-complaint-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "ccDescription", $(this).html(),
            id);
        });
        $('#encounter-specific-location-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "specificLocation", $(this).html(),
            id);
        });
        $('#encounter-hours-since-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("hoursSince",
            $(this).html(),
            id);
        });
        $('#encounter-days-since-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("daysSince", $(
              this).html(),
            id);
        });
        $('#encounter-weeks-since-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("weeksSince",
            $(this).html(),
            id);
        });
        $('#encounter-months-since-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("monthsSince",
            $(this).html(),
            id);
        });
        $('#encounter-years-since-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("yearsSince",
            $(this).html(),
            id);
        });
        $('#encounter-chief-complaint-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "ccDescription", $(this).html(),
            id);
        });
        $('#encounter-specific-location-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "specificLocation", $(this).html(),
            id);
        });
        $('input[name=encounter-occurs-when-' + id + ']').click(function () {
          var occursWhen = $('input[name=encounter-occurs-when-' + id +
            ']:checked').map(function () {
            return this.value;
          }).get().join(',');
          patient_encounter().updateSavedPatientEncounter("occursWhen",
            occursWhen,
            id);
        });
        $('#encounter-hours-since-location-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("hoursSince",
            $(this).html(),
            id);
        });
        $('#encounter-days-since-location-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("daysSince", $(
              this).html(),
            id);
        });
        $('#encounter-weeks-since-location-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("weeksSince",
            $(this).html(),
            id);
        });
        $('#encounter-months-since-location-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("monthsSince",
            $(this).html(),
            id);
        });
        $('#encounter-years-since-location-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("yearsSince",
            $(this).html(),
            id);
        });
        $('#encounter-how-long-other-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("howLongOther",
            $(this).html(),
            id);
        });
        $('#encounter-pain-x-hour-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("painXHour", $(
              this).html(),
            id);
        });
        $('#encounter-pain-x-day-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("painXDay", $(
              this).html(),
            id);
        });
        $('#encounter-pain-x-week-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("painXWeek", $(
              this).html(),
            id);
        });
        $('#encounter-pain-x-month-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("painXMonth",
            $(this).html(),
            id);
        });
        $('#encounter-pain-duration-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("painDuration",
            $(this).html(),
            id);
        });
        $('input[name=encounter-denies-' + id + ']').click(function () {
          var denies = $('input[name=encounter-denies-' + id +
            ']:checked').map(function () {
            return this.value;
          }).get().join(',');
          patient_encounter().updateSavedPatientEncounter("denies",
            denies, id);
        });
        $('#encounter-denies-other-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("deniesOther",
            $(this).html(),
            id);
        });
      }
    } else if (section == 'obgyn' && !encounter.obgyn) {
      if (savedState == false) {
        $('#obgyn-form, #obgyn-header').hide();
      } else if (savedState == true) {
        if (!encounter.obgyn) {
          $('#obgyn-form, #obgyn-header').hide();
        } else {
          $('#obgyn-form, #obgyn-header').show();
          $('#encounter-obgyn-g-saved-' + id).html(encounter.obgyn.g);
          $('#encounter-obgyn-p-saved-' + id).html(encounter.obgyn.p);
          $('#encounter-obgyn-t-saved-' + id).html(encounter.obgyn.t);
          $('#encounter-obgyn-a-saved-' + id).html(encounter.obgyn.a);
          $('#encounter-obgyn-l-saved-' + id).html(encounter.obgyn.l);
          $('input[name=encounter-pregnant-' + id + '][value=' +
            encounter.obgyn.pregStatus +
            ']').attr("checked", true);
          $('input[name=encounter-breastfeeding-' + id + '][value=' +
            encounter.obgyn.breastfeeding +
            ']').attr("checked", true);
          $('#encounter-breastfeeding-months-saved-' + id).html(encounter
            .obgyn.breastfeedingMonths);
          $('#encounter-last-period-saved-' + id).html(encounter.obgyn.lastPeriod);
          $('#encounter-age-first-period-saved-' + id).html(encounter.obgyn
            .ageFirstPeriod);
          $('input[name=encounter-pap-smear-' + id + '][value=' +
            encounter.obgyn.papSmearStatus +
            ']').attr("checked", true);
          $('input[name=encounter-birth-control-' + id + '][value=' +
            encounter.obgyn.birthControlStatus +
            ']').attr("checked", true);
          $('#encounter-birth-control-type-saved-' + id).html(encounter.obgyn
            .birthControlType);
          Util.selectCheckboxesFromList(encounter.obgyn.obgynHistory,
            'encounter-obgyn-hist-' + id);
          $('#encounter-obgyn-hist-other-saved-' + id).html(encounter.obgyn
            .obgynHistoryOther);
          $('#encounter-obgyn-g-saved-' + id).blur(function () {
            patient_encounter().updateSavedPatientEncounter("obgynG", $(
                this).html(),
              id);
          });
          $('#encounter-obgyn-p-saved-' + id).blur(function () {
            patient_encounter().updateSavedPatientEncounter("obgynP", $(
                this).html(),
              id);
          });
          $('#encounter-obgyn-t-saved-' + id).blur(function () {
            patient_encounter().updateSavedPatientEncounter("obgynT", $(
                this).html(),
              id);
          });
          $('#encounter-obgyn-a-saved-' + id).blur(function () {
            patient_encounter().updateSavedPatientEncounter("obgynA", $(
                this).html(),
              id);
          });
          $('#encounter-obgyn-l-saved-' + id).blur(function () {
            patient_encounter().updateSavedPatientEncounter("obgynL", $(
                this).html(),
              id);
          });
          $('input[name=encounter-pregnant-' + id + ']').click(function () {
            patient_encounter().updateSavedPatientEncounter("pregStatus",
              $(this).val(),
              id);
          });
          $('input[name=encounter-breastfeeding-' + id + ']').click(
            function () {
              patient_encounter().updateSavedPatientEncounter(
                "breastfeeding", $(this).val(),
                id);
            });
          $('#encounter-breastfeeding-months-saved-' + id).blur(function () {
            patient_encounter().updateSavedPatientEncounter(
              "breastfeedingMonths",
              $(this).html(), id);
          });
          $('#encounter-last-period-saved-' + id).blur(function () {
            patient_encounter().updateSavedPatientEncounter("lastPeriod",
              $(this).html(),
              id);
          });
          $('#encounter-age-first-period-saved-' + id).blur(function () {
            patient_encounter().updateSavedPatientEncounter(
              "ageFirstPeriod", $(this).html(),
              id);
          });
          $('input[name=encounter-pap-smear-' + id + ']').click(function () {
            patient_encounter().updateSavedPatientEncounter(
              "papSmearStatus", $(this).val(),
              id);
          });
          $('input[name=encounter-birth-control-' + id + ']').click(
            function () {
              patient_encounter().updateSavedPatientEncounter(
                "birthControlStatus",
                $(this).val(), id);
            });
          $('#encounter-birth-control-type-saved-' + id).blur(function () {
            patient_encounter().updateSavedPatientEncounter(
              "birthControlType", $(this).html(),
              id);
          });
          $('input[name=encounter-obgyn-hist-' + id + ']').click(function () {
            var obgynHist = $('input[name=encounter-obgyn-hist-' + id +
              ']:checked').map(function () {
              return this.value;
            }).get().join(',');
            patient_encounter().updateSavedPatientEncounter(
              "obgynHistory",
              obgynHist, id);
          });
          $('#encounter-obgyn-hist-other-saved-' + id).blur(function () {
            patient_encounter().updateSavedPatientEncounter(
              "obgynHistoryOther", $(this).html(),
              id);
          });
        }
      }
    } else if (section == 'pfsh') {
      if (savedState == false) {} else if (savedState == true) {
        $('#encounter-num-residents-saved-' + id).html(encounter.patient.pfsh
          .numResidents);
        $('#encounter-job-type-saved-' + id).html(encounter.patient.pfsh.jobType);
        $('input[name=encounter-mother-alive-' + id + '][value=' +
          encounter.patient.pfsh.motherAlive +
          ']').attr("checked", true);
        $('#encounter-mother-death-reason-saved-' + id).html(encounter.patient
          .pfsh.motherDeathReason);
        $('input[name=encounter-father-alive-' + id + '][value=' +
          encounter.patient.pfsh.fatherAlive + ']').attr("checked",
          true);
        $('#encounter-father-death-reason-saved-' + id).html(encounter.patient
          .pfsh.fatherDeathReason);
        $('input[name=encounter-partner-alive-' + id + '][value=' +
          encounter.patient.pfsh.partnerAlive +
          ']').attr("checked", true);
        $('#encounter-partner-death-reason-saved-' + id).html(encounter.patient
          .pfsh.partnerDeathReason);
        $('#encounter-num-siblings-saved-' + id).html(encounter.patient.pfsh
          .numSiblings);
        $('#encounter-num-brothers-saved-' + id).html(encounter.patient.pfsh
          .numBrothers);
        $('#encounter-num-sisters-saved-' + id).html(encounter.patient.pfsh
          .numSisters);
        $('#encounter-num-children-saved-' + id).html(encounter.patient.pfsh
          .numChildren);
        $('#encounter-num-sons-saved-' + id).html(encounter.patient.pfsh.numSons);
        $('#encounter-num-daughters-saved-' + id).html(encounter.patient.pfsh
          .numDaughters);
        $('#encounter-num-residents-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("numResidents",
            $(this).html(),
            id);
        });
        $('#encounter-job-type-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("jobType", $(
              this).html(),
            id);
        });
        $('input[name=encounter-mother-alive-' + id + ']').click(function () {
          patient_encounter().updateSavedPatientEncounter("motherAlive",
            $(this).val() ==
            'true', id);
        });
        $('#encounter-mother-death-reason-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "motherDeathReason", $(this).html(),
            id);
        });
        $('input[name=encounter-father-alive-' + id + ']').click(function () {
          patient_encounter().updateSavedPatientEncounter("fatherAlive",
            $(this).val() ==
            'true', id);
        });
        $('#encounter-father-death-reason-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "fatherDeathReason", $(this).html(),
            id);
        });
        $('input[name=encounter-partner-alive-' + id + ']').click(
          function () {
            patient_encounter().updateSavedPatientEncounter("partnerAlive",
              $(this).val() ==
              'true', id);
          });
        $('#encounter-partner-death-reason-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "partnerDeathReason", $(this).html(),
            id);
        });
        $('#encounter-num-siblings-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("numSiblings",
            $(this).html(),
            id);
        });
        $('#encounter-num-brothers-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("numBrothers",
            $(this).html(),
            id);
        });
        $('#encounter-num-sisters-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("numSisters",
            $(this).html(),
            id);
        });
        $('#encounter-num-children-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("numChildren",
            $(this).html(),
            id);
        });
        $('#encounter-num-sons-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("numSons", $(
              this).html(),
            id);
        });
        $('#encounter-num-daughters-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("numDaughters",
            $(this).html(),
            id);
        });
      }
    } else if (section == 'supp') {
      if (savedState == false) {
        RenderUtil.render('component/encounter_questions', {
          encounter: encounter
        }, function (s) {
          $("#encounter-questions-" + id).html(s);
          setEncounterFormMode(encounter, section, savedState,
            hasOwnership);
        });
        $('#encounter-supp-new-question-' + id).click(function () {
          var jsonData = JSON.stringify({
            sessionId: clinician.sessionId,
            encounterId: id
          });
          $.post("patient/addEncounterQuestion", {
            data: jsonData
          }, function (data) {
            var parsedData = $.parseJSON(data);
            var numQuestions = $("#encounter-questions-" + id).children()
              .length +
              2;
            RenderUtil.render('component/encounter_question', {
              ordinal: numQuestions,
              id: parsedData.encounterQuestionId
            }, function (s) {
              $("#encounter-questions-" + id).append(s);
              setEncounterFormMode(encounter, section,
                savedState, hasOwnership);
            });
          });
        });
      } else if (savedState == true) {
        $('#encounter-num-cups-water-saved-' + id).html(encounter.supp.numCupsWater);
        $('#encounter-num-cups-coffee-saved-' + id).html(encounter.supp.numCupsCoffee);
        $('#encounter-num-cups-tea-saved-' + id).html(encounter.supp.numCupsTea);
        $('#encounter-water-source-saved-' + id).html(encounter.supp.waterSource);
        $('#encounter-num-cups-water-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("numCupsWater",
            $(this).html(),
            id);
        });
        $('#encounter-num-cups-coffee-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "numCupsCoffee", $(this).html(),
            id);
        });
        $('#encounter-num-cups-tea-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("numCupsTea",
            $(this).html(),
            id);
        });
        $('#encounter-water-source-saved-' + id).click(function () {
          $(this).css({
            display: "none"
          });
          $("#encounter-water-source-" + id).css({
            display: "block"
          });
          $("#encounter-water-source-" + id).val(encounter.supp.waterSource);
          $("#encounter-water-source-" + id).change(function () {
            patient_encounter().updateSavedPatientEncounter(
              "waterSource", $(this).val(),
              id);
          });
        });
        RenderUtil.render('component/encounter_questions', {
          encounter: encounter
        }, function (s) {
          $("#encounter-questions-" + id).html(s);
          setEncounterFormMode(encounter, section, savedState,
            hasOwnership);
          $('.encounter-question-editable').blur(function (e) {
            patient_encounter().getCurrentQuestionId(e);
            patient_encounter().updateEncounterQuestion("question",
              $(this).html(),
              App.currentQuestionId);
          });
          $('.encounter-response-editable').blur(function (e) {
            patient_encounter().getCurrentQuestionId(e);
            patient_encounter().updateEncounterQuestion("response",
              $(this).html(),
              App.currentQuestionId);
          });
        });
        $('#encounter-supp-new-question-' + id).click(function () {
          var jsonData = JSON.stringify({
            sessionId: clinician.sessionId,
            encounterId: id
          });
          $.post("patient/addEncounterQuestion", {
            data: jsonData
          }, function (data) {
            var parsedData = $.parseJSON(data);
            var encounterQuestionId = parsedData.encounterQuestionId;
            var numQuestions = $("#encounter-questions-" + id).children()
              .length + 2;
            RenderUtil.render('component/encounter_question', {
              ordinal: numQuestions,
              id: encounterQuestionId
            }, function (s) {
              $("#encounter-questions-" + id).append(s);
              setEncounterFormMode(encounter, section,
                savedState, hasOwnership);
              $('.encounter-question-editable').blur(function (
                e) {
                patient_encounter().getCurrentQuestionId(e);
                patient_encounter().updateEncounterQuestion(
                  "question", $(this).html(),
                  encounterQuestionId);
              });
              $('.encounter-response-editable').blur(function (
                e) {
                patient_encounter().getCurrentQuestionId(e);
                patient_encounter().updateEncounterQuestion(
                  "response", $(this).html(),
                  encounterQuestionId);
              });
            });
          });
        });
      }
    } else if (section == 'hist') {
      if (savedState == false) {
        RenderUtil.render('component/patient_medications', {
          encounter: encounter
        }, function (s) {
          $("#patient-medications-" + id).html(s);
          setEncounterFormMode(encounter, section, savedState,
            hasOwnership);
        });
        $('#encounter-hist-new-medication-' + id).click(function () {
          var jsonData = JSON.stringify({
            sessionId: clinician.sessionId,
            patientId: encounter.patient.id
          });
          $.post("patient/addPatientMedication", {
            data: jsonData
          }, function (data) {
            var parsedData = $.parseJSON(data);
            var numMedications = $("#patient-medications-" + id).children()
              .length +
              2;
            RenderUtil.render('component/patient_medication', {
              ordinal: numMedications,
              id: parsedData.patientMedicationId
            }, function (s) {
              $("#patient-medications-" + id).append(s);
              setEncounterFormMode(encounter, section,
                savedState, hasOwnership);
            });
          });
        });
        $('.medication-delete-control').click(function () {
          patient_encounter().deleteMedication($(this));
        });
      } else if (savedState == true) {
        RenderUtil.render('component/patient_medications', {
          patient: encounter.patient
        }, function (s) {
          $("#patient-medications-" + id).html(s);
          setEncounterFormMode(encounter, section, savedState,
            hasOwnership);
          $('.patient-med-editable').blur(function (e) {
            patient_encounter().getCurrentMedicationId(e);
            patient_encounter().updatePatientMedication("medication",
              $(this).html(),
              App.currentMedicationId);
          });
          $('.patient-dose-editable').blur(function (e) {
            patient_encounter().getCurrentMedicationId(e);
            patient_encounter().updatePatientMedication("dose", $(
                this).html(),
              App.currentMedicationId);
          });
          $('.patient-freq-editable').blur(function (e) {
            patient_encounter().getCurrentMedicationId(e);
            patient_encounter().updatePatientMedication("frequency",
              $(this).html(),
              App.currentMedicationId);
          });
          $('.medication-delete-control').click(function () {
            patient_encounter().deleteMedication($(this));
          });
        });
        $('#encounter-past-s-m-saved-' + id).html(encounter.patient.hist.pastSM);
        Util.selectCheckboxesFromList(encounter.patient.hist.famHist,
          'encounter-fam-hist-' + id);
        $('#encounter-fam-hist-notes-saved-' + id).html(encounter.patient
          .hist.famHistNotes);
        $('#encounter-fam-hist-other-saved-' + id).html(encounter.patient
          .hist.famHistOther);
        $('#encounter-allerg-food-saved-' + id).html(encounter.patient.hist
          .allergFood);
        $('#encounter-allerg-drug-saved-' + id).html(encounter.patient.hist
          .allergDrug);
        $('#encounter-allerg-env-saved-' + id).html(encounter.patient.hist
          .allergEnv);
        $('input[name=encounter-vacc-' + id + '][value=' + encounter.patient
          .hist.vacc +
          ']').attr("checked", true);
        $('#encounter-vacc-notes-saved-' + id).html(encounter.patient.hist
          .vaccNotes);
        Util.selectCheckboxesFromList(encounter.patient.hist.subst,
          'encounter-subst-' + id);
        $('#encounter-smoke-pks-day-saved-' + id).html(encounter.patient.hist
          .smokePksDay);
        $('#encounter-years-smoked-saved-' + id).html(encounter.patient.hist
          .yearsSmoke);
        $('#encounter-smoke-years-quit-saved-' + id).html(encounter.patient
          .hist.smokeYearsQuit);
        $('#encounter-etoh-units-week-saved-' + id).html(encounter.patient
          .hist.etohUnitsWeek);
        $('#encounter-current-drugs-saved-' + id).html(encounter.patient.hist
          .currentDrugs);
        $('#encounter-hist-new-medication-' + id).click(function () {
          var jsonData = JSON.stringify({
            sessionId: clinician.sessionId,
            patientId: encounter.patient.id
          });
          $.post("patient/addPatientMedication", {
            data: jsonData
          }, function (data) {
            var parsedData = $.parseJSON(data);
            var patientMedicationId = parsedData.patientMedicationId;
            var numMedications = $("#patient-medications-" + id).children()
              .length + 2;
            RenderUtil.render('component/patient_medication', {
              ordinal: numMedications,
              id: patientMedicationId
            }, function (s) {
              $("#patient-medications-" + id).append(s);
              setEncounterFormMode(encounter, section,
                savedState, hasOwnership);
              $('.patient-med-editable').blur(function (e) {
                patient_encounter().getCurrentMedicationId(e);
                patient_encounter().updatePatientMedication(
                  "medication", $(this).html(),
                  patientMedicationId);
              });
              $('.patient-dose-editable').blur(function (e) {
                patient_encounter().getCurrentQuestionId(e);
                patient_encounter().updatePatientMedication(
                  "dose", $(this).html(),
                  patientMedicationId);
              });
              $('.patient-freq-editable').blur(function (e) {
                patient_encounter().getCurrentQuestionId(e);
                patient_encounter().updatePatientMedication(
                  "frequency", $(this).html(),
                  patientMedicationId);
              });
            });
          });
        });
        $('#encounter-past-s-m-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("pastSM", $(
              this).html(),
            id);
        });
        $('input[name=encounter-fam-hist-' + id + ']').click(function () {
          var famHist = $('input[name=encounter-fam-hist-' + id +
            ']:checked').map(function () {
            return this.value;
          }).get().join(',');
          patient_encounter().updateSavedPatientEncounter("famHist",
            famHist, id);
        });
        $('#encounter-fam-hist-notes-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("famHistNotes",
            $(this).html(),
            id);
        });
        $('#encounter-fam-hist-other-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("famHistOther",
            $(this).html(),
            id);
        });
        $('#encounter-allerg-food-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("allergFood",
            $(this).html(),
            id);
        });
        $('#encounter-allerg-drug-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("allergDrug",
            $(this).html(),
            id);
        });
        $('#encounter-allerg-env-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("allergEnv", $(
              this).html(),
            id);
        });
        $('input[name=encounter-vacc-' + id + ']').click(function () {
          patient_encounter().updateSavedPatientEncounter("vacc", $(this)
            .val() ==
            'true', id);
        });
        $('#encounter-vacc-notes-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("vaccNotes", $(
              this).html(),
            id);
        });
        $('input[name=encounter-subst-' + id + ']').click(function () {
          var subst = $('input[name=encounter-subst-' + id +
            ']:checked').map(function () {
            return this.value;
          }).get().join(',');
          patient_encounter().updateSavedPatientEncounter("subst", subst,
            id);
        });
        $('#encounter-smoke-pks-day-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("smokePksDay",
            $(this).html(),
            id);
        });
        $('#encounter-years-smoked-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("yearsSmoked",
            $(this).html(),
            id);
        });
        $('#encounter-smoke-years-quit-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "smokeYearsQuit", $(this).html(),
            id);
        });
        $('#encounter-etoh-units-week-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "etohUnitsWeek", $(this).html(),
            id);
        });
        $('#encounter-current-drugs-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("currentDrugs",
            $(this).html(),
            id);
        });
      }
    } else if (section == 'exam') {
      if (savedState == false) {
        RenderUtil.render('component/dx_codes', {
            encounter: encounter
          },
          function (s) {
            $("#encounter-dx-codes-" + id).html(s);
            setEncounterFormMode(encounter, section, savedState,
              hasOwnership);
            patient_encounter().initDxTypeAheads(id);
            $('.dx-code-delete-control').click(function () {
              patient_encounter().deleteDxCode($(this));
            });
          });
        $('#encounter-new-dx-code-' + id).click(function () {
          var jsonData = JSON.stringify({
            sessionId: clinician.sessionId,
            encounterId: id
          });
          $.post("patient/addDxCode", {
            data: jsonData
          }, function (data) {
            var parsedData = $.parseJSON(data);
            RenderUtil.render('component/dx_code', {
                id: parsedData.dxCodeId
              },
              function (s) {
                $("#encounter-dx-codes-" + id).append(s);
                setEncounterFormMode(encounter, section,
                  savedState, hasOwnership);
                $('.dx-code-delete-control').click(function () {
                  patient_encounter().deleteDxCode($(this));
                });
              });
          });
        });
        RenderUtil.render('component/tx_codes', {
            encounter: encounter
          },
          function (s) {
            $("#encounter-tx-codes-" + id).html(s);
            setEncounterFormMode(encounter, section, savedState,
              hasOwnership);
            patient_encounter().initTxTypeAheads(id);
            $('.tx-code-delete-control').click(function () {
              patient_encounter().deleteTxCode($(this));
            });
          });
        $('#encounter-new-tx-code-' + id).click(function () {
          var jsonData = JSON.stringify({
            sessionId: clinician.sessionId,
            encounterId: id
          });
          $.post("patient/addTxCode", {
            data: jsonData
          }, function (data) {
            var parsedData = $.parseJSON(data);
            RenderUtil.render('component/tx_code', {
                id: parsedData.txCodeId
              },
              function (s) {
                $("#encounter-tx-codes-" + id).append(s);
                setEncounterFormMode(encounter, section,
                  savedState, hasOwnership);
                renderCPTModifiers(id, '.cpt-modifier-new');
                $('.tx-code-delete-control').click(function () {
                  patient_encounter().deleteTxCode($(this));
                });
              });
          });
        });
      } else if (savedState == true) {
        RenderUtil.render('component/dx_codes', {
            encounter: encounter
          },
          function (s) {
            $("#encounter-dx_codes-" + id).html(s);
            setEncounterFormMode(encounter, section, savedState,
              hasOwnership);
            $('.encounter-dx-code-icd9-editable').blur(function (e) {
              patient_encounter().patient_encounter().getCurrentDxCodeId(
                e);
              patient_encounter().updateDxCode("icd9", $(this).html(),
                App.currentDxCodeId);
            });
            patient_encounter().initDxTypeAheads(id);
            $('.dx-code-delete-control').click(function () {
              patient_encounter().deleteDxCode($(this));
            });
          });
        $('#encounter-new-dx-code-' + id).click(function () {
          var jsonData = JSON.stringify({
            sessionId: clinician.sessionId,
            encounterId: id
          });
          $.post("patient/addDxCode", {
            data: jsonData
          }, function (data) {
            var parsedData = $.parseJSON(data);
            var dxCodeId = parsedData.dxCodeId;
            RenderUtil.render('component/dx_code', {
              id: dxCodeId
            }, function (s) {
              $("#encounter-dx-codes-" + id).append(s);
              setEncounterFormMode(encounter, section,
                savedState, hasOwnership);
              $('.dx-code-delete-control').click(function () {
                patient_encounter().deleteDxCode($(this));
              });
              $('.encounter-dx-code-icd9-editable').blur(
                function (e) {
                  patient_encounter().patient_encounter().getCurrentDxCodeId(
                    e);
                  patient_encounter().updateDxCode("icd9", $(
                    this).html(), dxCodeId);
                });
            });
          });
        });
        RenderUtil.render('component/tx_codes', {
            encounter: encounter
          },
          function (s) {
            $("#encounter-tx_codes-" + id).html(s);
            setEncounterFormMode(encounter, section, savedState,
              hasOwnership);
            $('.encounter-tx-code-cpt-editable').blur(function (e) {
              patient_encounter().getCurrentTxCodeId(e);
              patient_encounter().updateTxCode("cpt", $(this).html(),
                App.currentTxCodeId);
            });
            $('.encounter-tx-code-cpt-modifier-editable').blur(function (
              e) {
              patient_encounter().getCurrentTxCodeId(e);
              patient_encounter().updateTxCode("cptModifier", $(this).html(),
                App.currentTxCodeId);
            });
            patient_encounter().initTxTypeAheads(id);
            $('.tx-code-delete-control').click(function () {
              patient_encounter().deleteTxCode($(this));
            });
          });
        $('#encounter-new-tx-code-' + id).click(function () {
          var jsonData = JSON.stringify({
            sessionId: clinician.sessionId,
            encounterId: id
          });
          $.post("patient/addTxCode", {
            data: jsonData
          }, function (data) {
            var parsedData = $.parseJSON(data);
            var txCodeId = parsedData.txCodeId;
            RenderUtil.render('component/tx_code', {
              id: txCodeId
            }, function (s) {
              $("#encounter-tx-codes-" + id).append(s);
              setEncounterFormMode(encounter, section,
                savedState, hasOwnership);
              $('.tx-code-delete-control').click(function () {
                patient_encounter().deleteTxCode($(this));
              });
              $('.encounter-tx-code-cpt-editable').blur(
                function (e) {
                  patient_encounter().getCurrentTxCodeId(e);
                  patient_encounter().updateTxCode("cpt", $(
                    this).html(), App.currentTxCodeId);
                });
              $('.encounter-tx-code-cpt-modifier-editable').blur(
                function (e) {
                  patient_encounter().getCurrentTxCodeId(e);
                  patient_encounter().updateTxCode(
                    "cptModifier", $(this).html(),
                    App.currentTxCodeId);
                });
            });
            renderCPTModifiers(id, '.cpt-modifier-new');
          });
        });
        $('#encounter-hs-saved-' + id).html(encounter.exam.hs);
        $('input[name=encounter-heart-rhythm-' + id + '][value=' +
          encounter.exam.heartRhythm +
          ']').attr("checked", true);
        $('#encounter-lab-hb-saved-' + id).html(encounter.exam.hb);
        $('#encounter-lab-glucose-saved-' + id).html(encounter.lab.glucose);
        $('#encounter-lab-urine-dip-saved-' + id).html(encounter.lab.urineDip);
        $('#encounter-diagnosis-saved-' + id).html(encounter.exam.diagnosis);
        $('#encounter-dx-code-saved-' + id).html(encounter.exam.dxCode);
        $('#encounter-treatment-plan-saved-' + id).html(encounter.exam.treatmentPlan);
        $('#encounter-tx-code-saved-' + id).html(encounter.exam.txCode);
        $('#encounter-hs-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("hs", $(this).html(),
            id);
        });
        $('input[name=encounter-heart-rhythm-' + id + ']').click(function () {
          patient_encounter().updateSavedPatientEncounter("heartRhythm",
            $(this).val(),
            id);
        });
        $('#encounter-lab-hb-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("hb", $(this).html(),
            id);
        });
        $('#encounter-lab-glucose-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("glucose", $(
              this).html(),
            id);
        });
        $('#encounter-lab-urine-dip-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("urineDip", $(
              this).html(),
            id);
        });
        $('#encounter-diagnosis-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("diagnosis", $(
              this).html(),
            id);
        });
        $('#encounter-dx-code-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("dxCode", $(
              this).html(),
            id);
        });
        $('#encounter-treatment-plan-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "treatmentPlan", $(this).html(),
            id);
        });
        $('#encounter-tx-code-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("txCode", $(
              this).html(),
            id);
        });
      }
    } else if (section == 'follow-up') {
      if (savedState == false) {
        $('#encounter-follow-up-date-' + id).mask("99/99/9999");
      } else if (savedState == true) {
        $('input[name=encounter-follow-up-level-' + id + '][value=' +
          encounter.followUp.level +
          ']').attr("checked", true);
        $('#encounter-follow-up-when-saved-' + id).html(encounter.followUp
          .when);
        $('#encounter-follow-up-condition-saved-' + id).html(encounter.followUp
          .condition);
        $('#encounter-follow-up-dispense-rx-saved-' + id).html(encounter.followUp
          .dispenseRx);
        $('#encounter-follow-up-uss-saved-' + id).html(encounter.followUp
          .USS);
        $('#encounter-follow-up-pregnant-saved-' + id).html(encounter.followUp
          .pregnant);
        $('#encounter-follow-up-wound-care-saved-' + id).html(encounter.followUp
          .woundCare);
        $('#encounter-follow-up-ref-to-specialist-saved-' + id).html(
          encounter.followUp.refToSpecialist);
        $('#encounter-follow-up-dental-list-saved-' + id).html(encounter.followUp
          .dentalList);
        $('#encounter-follow-up-physiotherapy-saved-' + id).html(
          encounter.followUp.physiotherapy);
        $('#encounter-follow-up-blood-labs-saved-' + id).html(encounter.followUp
          .bloodLabs);
        $('#encounter-follow-up-other-saved-' + id).html(encounter.followUp
          .other);
        $('#encounter-follow-up-pulmonary-fx-test-saved-' + id).html(
          encounter.followUp.pulmonaryFXTest);
        $('#encounter-follow-up-vision-saved-' + id).html(encounter.followUp
          .vision);
        $('input[name=encounter-follow-up-completed-' + id + '][value=' +
          encounter.followUp.followUpCompleted + ']').attr("checked",
          true) == 'true';
        encounter.followUp.followUpDate = Util.processDate(
          '#encounter-follow-up-date-saved-' +
          id, encounter.followUp.followUpDate);
        $('#encounter-follow-up-notes-saved-' + id).html(encounter.followUp
          .followUpNotes);
        $('input[name=encounter-follow-up-level-' + id + ']').click(
          function () {
            patient_encounter().updateSavedPatientEncounter(
              "followUpLevel", $(this).val(),
              id);
          });
        $('#encounter-follow-up-when-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("followUpWhen",
            $(this).html(),
            id);
        });
        $('#encounter-follow-up-condition-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "followUpCondition", $(this).html(),
            id);
        });
        $('#encounter-follow-up-dispense-rx-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "followUpDispenseRx", $(this).html(),
            id);
        });
        $('#encounter-follow-up-uss-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("followUpUSS",
            $(this).html(),
            id);
        });
        $('#encounter-follow-up-pregnant-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "followUpPregnant", $(this).html(),
            id);
        });
        $('#encounter-follow-up-wound-care-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "followUpWoundCare", $(this).html(),
            id);
        });
        $('#encounter-follow-up-ref-to-specialist-saved-' + id).blur(
          function () {
            patient_encounter().updateSavedPatientEncounter(
              "followUpRefToSpecialist",
              $(this).html(), id);
          });
        $('#encounter-follow-up-dental-list-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "followUpDentalList", $(this).html(),
            id);
        });
        $('#encounter-follow-up-physiotherapy-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "followUpPhysiotherapy",
            $(this).html(), id);
        });
        $('#encounter-follow-up-blood-labs-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "followUpBloodLabs", $(this).html(),
            id);
        });
        $('#encounter-follow-up-other-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "followUpOther", $(this).html(),
            id);
        });
        $('#encounter-follow-up-pulmonary-fx-test-saved-' + id).blur(
          function () {
            patient_encounter().updateSavedPatientEncounter(
              "followUpPulmonaryFXTest",
              $(this).html(), id);
          });
        $('#encounter-follow-up-vison-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "followUpVision", $(this).html(),
            id);
        });
        $('input[name=encounter-follow-up-completed-' + id + ']').click(
          function () {
            patient_encounter().updateSavedPatientEncounter(
              "followUpCompleted", $(this).val() ==
              'true', id);
          });
        $('#encounter-follow-up-date-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter("followUpDate",
            $(this).html(),
            id);
        });
        $('#encounter-follow-up-notes-saved-' + id).blur(function () {
          patient_encounter().updateSavedPatientEncounter(
            "followUpNotes", $(this).html(),
            id);
        });
      }
    }
    $('.dual-mode-saved').off().on('click', function (event) {
      $(this).css({
        display: "none"
      });
      var thisItem = this;
      var savedId = this.id;
      var unsavedId = savedId.replace('-saved', '');
      var currentValue = this.innerHTML;
      $('#' + unsavedId).val(currentValue);
      $('#' + unsavedId).css({
        display: "block"
      });
      var name = $('#' + unsavedId).attr('name');
      var unsavedItem = $('#' + unsavedId);
      if (name) {
        $('#' + unsavedId).change(function () {
          patient_encounter().updateSavedPatientEncounter(name, $(
              this).val(), id,
            true, savedId, this.selectedOptions[0].label);
        });
      } else {
        $('#encounter-demo-govt-id-' + id).mask("999-99-9999");
        $('#encounter-demo-dob-' + id).mask("99/99/9999");
        $('#encounter-demo-postal-code-' + id).mask("99999");
      }
    });
  }
  return module;
});