
/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

var JSLIBS = [ "js/lib/bowser.min.js", "js/lib/moment.min.js", "js/lib/jquery-1.10.2.js",
  "js/lib/modulejs-1.5.0.min.js", "js/lib/jquery-ui.custom.min.js",
  "js/lib/fullcalendar.min.js", "js/lib/jquery.tmpl.js",
  "js/lib/underscore-min.js",
  "js/lib/backbone-min.js", "js/lib/bootstrap.min.js",
  "js/lib/date.format.js",
  "js/lib/bootstrap-datepicker.js", "js/lib/bootstrap-slider.js",
  "js/lib/jquery.maskedinput.min.js", "js/lib/fileuploader.custom.js",
  "js/lib/typeahead.bundle.min.js", "js/lib/Chart.min.js"
];
var JS = ["js/admin.js", "js/ajax.js", "js/app.js",
  "js/app_jquery_functions.js",
  "js/calendar.js", "js/charting.js", "js/chief_complaints.js",
  "js/clinician.js", "js/clinicians.js", "js/dto.js", "js/events.js",
  "js/events_handler.js", "js/filter.js", "js/head.js", "js/helper.js",
  "js/idle_timer.js", "js/jquery.js", "js/loader.js", "js/messages.js",
  "js/mode.js", "js/notifier.js", "js/park.js", "js/patient.js",
  "js/patient_chart.js", "js/patient_encounter.js",
  "js/patient_encounter_form.js", "js/patient_vital_signs.js",
  "js/patients_filter.js", "js/printer.js", "js/render_util.js",
  "js/reports.js", "js/reports_filter.js",
  "js/responder.js", "js/session.js", "js/sinon.js", "js/soap_note.js",
  "js/standalone.js", "js/table.js", "js/twitter_wjs.js", "js/underscore.js",
  "js/util.js",
  "js/view_stack.js"
];
var init = function(){
  app = modulejs.require('app');
  // test for Grade A browser support.
  if (!bowser.a) {
     document.location = 'browser_upgrade.html';
  }
  app.init();
} 
head.load.apply(this, JSLIBS.concat(JS).concat(init))
