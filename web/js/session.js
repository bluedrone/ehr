
"use strict";

modulejs.define('session', ["app"], function (App) {

  var module = {}

  module.checkSessionResponse = function (obj) {
    App.idleTimer.idleTime = 0;
    if (obj != undefined) {
      if (obj.authenticated == false) {
        return false;
      }
      return true;
    } else {
      App.config.disable_auto_logout();
      module.logout(App.config.do_auto_logout(), App.config.do_auto_server_logout());
      return false;
    }
  };

  module.logout = function () {};

  module.checkSession = function () {
    var jsonData = JSON.stringify({
      sessionId: user.sessionId
    });
    Ajax.post("auth/checkSession", {
      data: jsonData
    }, function (data) {
      var parsedData = $.parseJSON(data);
      if (parsedData.authenticated == false) {
        module.logout(App.config.do_auto_logout());
        return false;
      }
    });
  };
  return module;
});