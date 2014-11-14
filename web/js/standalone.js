
"use strict";

modulejs.define('standalone', ['mode', 'head', 'standalone/responder',
  'standalone/filter', 'jquery', 'underscore', "patient/chart"
], function (mode, head,
  responder, filter, $, _, PatientChart) {
  return {
    ready: function (onReady) {
      if (mode.isStandalone()) {
        $('#wdm-pm').click(function () {
          $(this).attr('href', '../../pm/web/app.html');
        });
        var server, recentPatients, groupByPatient;
        var activityLogs = [];
        var appUrlRegex = /^app\/.*/;
        var patientUrlRegex = /^patient\/.*/;
        var reportsUrlRegex = /^reports\/.*/;
        var templateUrlRegex = /^template\/.*/;
        var htmlUrlRegex = /^html\/.*/;
        var originalParseJson = $.parseJSON;
        var headShotPlaceHolder = "images/headshot-placeholder.jpg";

        $.parseJSON = function (data) {
          if ($.isPlainObject(data)) {
            return data;
          } else {
            return originalParseJson(data);
          }
        };
        $.ajaxSetup({
          beforeSend: function (jqXHR, settings) {
            settings.url = settings.url.replace(/\?.*/, '');
            if (settings.url.match(templateUrlRegex)) {
              settings.url = settings.url.replace(/^template/,
                'html');
            } else if (settings.url.match(htmlUrlRegex)) {
              settings.url = settings.url.replace(/^html/,
                'template');
            }
          }
        });

        var fakeUrl = function (url) {
          return url.match(appUrlRegex) || url.match(reportsUrlRegex) ||
            url.match(patientUrlRegex) ||
            url.match(htmlUrlRegex);
        };

        var expectsJson = function (url) {
          switch (url) {
          case url.match('app/getAppointmentsByClinician'):
            return true;
          default:
            return false;
          }
        };

        var callback = function () {
          server = sinon.fakeServer.create();
          server.xhr.useFilters = true;
          server.autoRespond = true;
          var contentType = {
            "Content-Type": "application/json"
          };
          var statusCode = 200;
          server.xhr.addFilter(function (method, url) {
            // when true response not faked
              console.log(url)
            return !fakeUrl(url);
          });
          var originalProcess = server.processRequest;

          server.processRequest = function (request) {
            var url = request.url;
            var method = request.method;
            var processData = undefined;
            if (fakeUrl(url)) {
              if (url.match(htmlUrlRegex)) {
                contentType = {
                  "Content-Type": "text/html"
                };

                processData = function (data) {
                  return data;
                };
              } else {

                processData = function (data) {
                  var postData, activity;
                  if (url.match('app/getAppointmentsByClinician')) {
                    // non existing value will cause array to
                    // be returned
                    postData = {
                      id: -1
                    };
                  } else if (url.match(
                      'app/getPatientSearchTypeAheads')) {
                    postData = {
                      id: 0
                    };
                  } else {
                    // gets post request data param
                    postData = decodeURIComponent(request.requestBody)
                      .split('=')[1];
                    if (postData) {
                      postData = JSON.parse(postData);
                    }
                  }
                  if (url == 'reports/filterActivityLog' || url ==
                    'reports/filterGroupByPatientsActivityLog') {
                    activity = $(
                      '#reports-activity-log-activity option:selected'
                    ).text();
                    postData.activityName = activity;
                    return filter.reports().process(activityLogs,
                      postData);
                  } else if (url == 'app/patientSearch') {
                    return filter.patients().process(recentPatients
                      .patients,
                      postData);
                  } else {
                    return filter.process(url, data, postData);
                  }
                };
              }

              var success = function (data) {
                var processedData = processData(data);
                if (url == 'app/getRecentPatients') {
                  recentPatients = processedData;
                }
                if (url == 'app/patientSearch') {
                  var patients = processedData;
                  processedData = _.extend({}, recentPatients, {
                    patients: patients
                  });
                }
                if (url == 'reports/getActivityLog' || url ==
                  'reports/getGroupByPatientsLog') {
                  activityLogs = processedData[0].activityLog ||
                    processedData;
                  groupByPatient = processedData[0].patient;
                }
                if (url ==
                  'reports/filterGroupByPatientsActivityLog') {
                  activityLog = processedData;
                  processedData = [{
                    patient: groupByPatient,
                    activityLog: activityLog
                  }];
                }
                 var string;
                if (url.match(htmlUrlRegex)) {
                  string = processedData;
                } else {
                  if (!expectsJson(url)) {
                    string = JSON.stringify(processedData);
                  }
                }
                server.responses = [{
                  method: method,
                  url: url,
                  response: [statusCode,
                    contentType, string
                  ]
                }];
                originalProcess.call(server, request);
                if (url.match('app/getPatientChart')) {
                  var headShot = "files/patients/" + processedData.id +
                    '/' +
                    processedData.profileImagePath;
                  var tried = false;
                  PatientChart.chartHeadshot = headShot;
                  console.log(PatientChart.chartHeadshot)
                  $('.patient-chart-headshot').attr('src', PatientChart
                    .chartHeadshot);
                  $('.patient-chart-headshot').error(function () {
                    if (!tried) {
                      $('.patient-chart-headshot').attr('src',
                        headShotPlaceHolder);
                      $('.patient-chart-headshot').load();
                      tried = true;
                    }
                  });
                }
              };
              responder.respond(url).then(success);
            }
          };
          onReady();
        };
        head.load('js/lib/sinon-server.js', function () {
          if (head.browser.ie) {
            head.load("js/lib/sinon-ie.js", callback);
          } else {
            callback();
          }
        });
      } else {
        onReady();
      }
    }
  };
});