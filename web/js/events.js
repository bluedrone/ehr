
"use strict";

modulejs.define('events', function () {
  var events = {};

  events.trigger = function (event, params=[]) {
    $(document).trigger(event, params);
  };

  events.on = function (event, callback) {
    $(document).on(event, callback);
  };
  return events;
});