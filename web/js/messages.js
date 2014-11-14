
"use strict";

modulejs.define('messages', function () {
  var module = {};

  module.reset = function () {
    $('#messages-view').css({
      display: "block"
    });
    $('#messages-inbox').css({
      display: "none"
    });
  };

  module.header = function (header) {
    $('#messages-inbox-header').html(header);
  };

  module.content = function (content) {
    $('#message-content').html(content);
  };
  return module;
});