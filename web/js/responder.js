
"use strict";

modulejs.define('standalone/responder', ['loader'], function (Loader) {
  return {
    respond: function (url) {
      return Loader.load(url);
    }
  };
});