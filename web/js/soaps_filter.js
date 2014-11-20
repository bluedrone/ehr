
"use strict";

modulejs.define('standalone/soaps/filter', function ($, _) {
    return {
      process: function (json_string, conditions) {
        var json = JSON.parse(json_string)
        if (conditions.patientId == 1)  {
          return json[1];
        }else {
          json[0].patientId=conditions.patientId;
          return json[0];
        }
      }
    };
});