
"use strict";

var Ajax = (function (ajax) {

  ajax.post = function (path, data, callback) {
    return $.post(path, data, callback);
  };

  ajax.get = function (path, callback) {
    return $.get(path, callback);
  };
  return ajax;
})({});