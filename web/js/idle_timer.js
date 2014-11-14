
"use strict";

modulejs.define('idle_timer', ["events", "app"], function (Events, App) {
  var ONE_SECOND = 1000;
  var ONE_MINUTE = 60000;

  var timer = {};
  
  App.idleInterval;
  App.idleTime = 0;

  timer.reset = function () {
    Events.trigger("idletimer:reset");
    App.idleTime = 0;
  };
  timer.increment = function() {
    App.idleTime++;
    if (App.idleTime == 25) {
      Events.trigger("idletimer:@25");
    } else if (App.idleTime == 30) {
      Events.trigger("idletimer:@30");
    }
  }

  timer.clear = function () {
    if (App.idleInterval) {
      clearInterval(App.idleInterval);
    }
  };

  timer.run = function () {
    App.idleTime = 0;
    this.clear();
    App.idleInterval = setInterval(this.increment, ONE_MINUTE);
  };
  return timer;
});