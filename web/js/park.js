
"use strict";

modulejs.define('park', ["notifier", "idle_timer", "render_util", "clinician",
  "app"
], function (Notifier, IdleTimer, RenderUtil, Clinician, App) {
  var module = {};

  module.showWarning = function () {
    Notifier.notify(
      'Your session will soon be automatically parked if still idle',
      true);
    App.parkWarningDisplayed = true;
  };

  module.clearWarning = function () {
    if (App.parkWarningDisplayed) {
      Notifier.clear();
      App.parkWarningDisplayed = false;
    }  
  };

  module.showDialog = function () {
    RenderUtil.render('park', {}, function (s) {
      $('#modals-placement').html(s);
      $('#app-parked-full-name').html(App.clinicianFullName);
      $('#modal-park').modal('show');
      park();
      $('.app-exit').click(function () {
        App.logout();
      });
      $('#app-unpark-submit').click(function () {
        unpark();
      });
    });
  };

  function park() {
    var jsonData = JSON.stringify({
      sessionId: App.clinician.sessionId
    });
    Ajax.post("app/park", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      IdleTimer.clear();
    });
  }

  function unpark() {
    if ($.trim($("#app-unpark-username").val()).length < 1 || $.trim($(
        "#app-unpark-password").val()).length <
      1) {
      return;
    }
    var username = $.trim($("#app-unpark-username").val());
    var password = $.trim($("#app-unpark-password").val());
    $('#app-unpark-running').css({
      display: "block"
    });
    var clinician = App.clinician
    var jsonData = JSON.stringify({
      username: username,
      password: password,
      sessionId: clinician.sessionId
    });
    Ajax.post("app/unpark", {
      data: jsonData
    }, function (data) {
      $('#app-login-error').css({
        display: 'none'
      });
      var parsedData = $.parseJSON(data);
      $('#app-login-running').css({
        display: "none"
      });
      if (clinician.authStatus == Clinician.STATUS_AUTHORIZED) {
        notificationText = App.clinicianFullName + ' unparked.';
        $('#modal-park').modal('hide');
        Notifier.notify(notificationText);
        IdleTimer.run();
      } else {
        if (clinician.authStatus == Clinician.STATUS_NOT_FOUND) {
          notificationText = 'Clinician not found in system';
        } else if (clinician.authStatus == Clinician.STATUS_INVALID_PASSWORD) {
          notificationText = 'Invalid password';
        } else if (clinician.authStatus == Clinician.STATUS_INACTIVE) {
          notificationText = 'Clinician is inactive';
        }
        $("#app-unpark-notification").html(notificationText);
      }
    });
  }
  return module;
});