
"use strict";

modulejs.define('standalone/patients/filter', ['jquery', 'underscore'],
  function ($, _) {
    var filterBy = ["firstName", "middleName", "lastName", "gender", "city",
      "dob"
    ];

    var elementVal = function (elem, filter) {
      switch (filter) {
      case "firstName":
      case "lastName":
      case "middleName":
        return elem["cred"][filter];
      case "gender":
        return elem["demo"]["gender"]["code"];
      case "dob":
      case "city":
        return elem["demo"][filter];
      }
    };
    return {
      process: function (data, conditions) {
        var result = $.grep(data, function (elem, ndx) {
          var matches = [true];
          var filter, conditions_filter, dob, test, conditionsVal, match;
          for (var ndx in filterBy) {
            filter = filterBy[ndx];
            conditions_filter = filter + "Filter";
            if (conditions.hasOwnProperty(conditions_filter) &&
              conditions[conditions_filter]) {
              if (filter == "dob") {
                dob = _(elementVal(elem, filter).split(' ')).initial(2)
                  .join('-').replace(/,/,
                    '');
                dob = moment(dob, "MMM-DD-YYYY").toDate().getTime();
                test = moment(conditions[conditions_filter],
                  "MM/DD/YYYY").toDate().getTime();
                match = dob == test;
                matches.push(match);
              } else {
                conditionsVal = conditions[conditions_filter];
                matches.push(elementVal(elem, filter) == conditionsVal);
              }
            }
          }
          matches = _.uniq(matches);
          return matches.length == 1 && _.first(matches) == true;
        });
        return result;
      }
    };
  });