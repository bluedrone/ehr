
"use strict";

modulejs.define('notifier', function () {
  var notifier = {};

  notifier.clear = function () {
    $('#wdm-notification-text').html('');
  };

  notifier.notify = function (text, sticky) {
    $('#wdm-notification-text').html(text);
    if (sticky) {
      $('#wdm-notification').fadeIn(400);
    } else {
      $('#wdm-notification').fadeIn(400).delay(3000).fadeOut(400);
    }
  };
  return notifier;
});