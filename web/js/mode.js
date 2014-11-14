
"use strict";

modulejs.define('mode', ['jquery'], function ($) {
  return {
    isStandalone: function () {
      var protocol = document.URL.split(':')[0];
      return (protocol == "file");
    }
  };
});