/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

"use strict";

modulejs.define('util', ["underscore"], function (_) {
  var module = {};

  module.findById = function (collection, id) {
    return _.find(collection, function (elem) {
      return elem.id == id;
    });
  };
  
  module.map = function(list, transformer) {_.map(list, transformer)}
  
  module.debug = function (log_txt) {
    if (window.console != undefined) {
      console.log(log_txt);
    }
  };

  module.processNumber = function (selector, property) {
    var value = $.trim($(selector).val());
    if (value.length > 0) {
      property = value;
    }
    return property;
  };

  module.processDate = function (selector, property) {
    // "Mar 17, 2014
    // 10:01:12 PM";
    var date = $.trim($(selector).val());
    if (date.length > 0) {
      property = dateFormat(date, "EEE MMM dd kk:mm:ss z yyyy");
    }
    return property;
  };

  module.processDob = function (selector, property) {
    var date = $.trim($(selector).val());
    if (date.length > 0) {
      property = dateFormat(date, "mm/dd/yyyy");
    }
    return property;
  };

  module.checkRegexp = function (s, regexp) {
    return regexp.test(s);
  };

  module.checkPassword = function (s) {
    if (module.checkRegexp(s, /[a-z]/) && module.checkRegexp(s, /[A-Z]/) &&
      module.checkRegexp(s, /\d/) && module.checkRegexp(s, /\W/)) {
      return true;
    }
    return false;
  };

  module.buildFullName = function (first, middle, last) {
    var middleToken = "";
    if (typeof first !== 'undefined' && first.length > 0) {
      if (typeof middle !== 'undefined' && middle.length > 0) {
        middleToken = middle + " ";
      }
      last = (typeof last === 'undefined') ? '' : last;
      return first + " " + middleToken + last;
    } else {
      return "";
    }
  };

  module.nullCheck = function (val) {
    if (typeof val !== 'undefined') {
      return val;
    }
    return "";
  };

  module.filterTriageInput = function (e) {
    var key = e.keyCode || e.which;
    if ((key != 8 && key != 9 && key < 49) || key > 51) {
      // 1,2,3
      if (e.preventDefault) {
        e.preventDefault();
      }
      e.returnValue = false;
    }
  };

  module.filterDecimalInput = function (e) {
    var key = e.keyCode || e.which;
    if ((key != 8 && key != 9 && key != 46 && key < 48) || (key > 57 &&
        key !=
        190)) {
      // 0-9
      // .
      if (e.preventDefault) {
        e.preventDefault();
      }
      e.returnValue = false;
    }
  };

  module.filterAgeInput = function (e) {
    var key = e.keyCode || e.which;
    if ((key != 8 && key != 9 && key < 48) || key > 57) {
      // 0-9
      if (e.preventDefault) {
        e.preventDefault();
      }
      e.returnValue = false;
    }
  };

  module.filterGenderInput = function (e) {
    var key = e.keyCode || e.which;
    if (key != 8 && key != 9 && key != 70 && key != 77 & key != 102 &&
      key !=
      109) {
      // [MmFf]
      if (e.preventDefault) {
        e.preventDefault();
      }
      e.returnValue = false;
    }
  };

  module.filterBiometricsInput = function (e) {
    var key = e.keyCode || e.which;
    if (key != 8 && key != 9 && key != 70 && key != 102 & key != 80 &&
      key !=
      112 && key != 32) {
      // [PpFf]
      if (e.preventDefault) {
        e.preventDefault();
      }
      e.returnValue = false;
    }
  };

  String.prototype.trunc = function (n, useWordBoundary) {
    var toLong = this.length > n,
      s_ = toLong ? this.substr(0, n - 1) : this;
    s_ = useWordBoundary && toLong ? s_.substr(0, s_.lastIndexOf(' ')) :
      s_;
    return toLong ? s_ + '&hellip;' : s_;
  };

  module.selectCheckboxesFromList = function (list, name) {
    if (list !== undefined) {
      var a = list.split(",");
      for (i = 0; i < a.length; i++) {
        var s = a[i];
        $("input[name=" + name + "][value=" + s + "]").attr("checked",
          true);
      }
    }
  };

  module.showError = function (item, message) {
    if (message == null) {
      message = 'required field';
    }
    $(item).text(message);
    $(item).css({
      opacity: 0.0,
      visibility: "visible"
    }).animate({
      opacity: 1.0
    });
  };

  module.printForm = function (template, object, title) {
    var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
    RenderUtil.render('print/' + template, {
      object: object,
      currentDate: currentDate
    }, function (obj) {
      var s = obj[0].outerHTML;
      print_openPrintWindow('print.html', s, title);
    });
  };

  module.isNumber = function (n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
  };

  module.substringMatcher = function (strs) {
    return function findMatches(q, cb) {
      var substrRegex;
      var matches, substringRegex; // an array that will be populated with
      // substring matches
      matches = []; // regex used to determine if a string contains the
      // substring `q`
      substrRegex = new RegExp(q, 'i'); // iterate through the pool of
      // strings and for any string that
      // contains the substring `q`, add it to the `matches` array
      $.each(strs, function (i, str) {
        if (substrRegex.test(str)) {
          // the typeahead jQuery plugin expects
          // suggestions to a
          // JavaScript object, refer to typeahead docs for more info
          matches.push({
            value: str
          });
        }
      });
      cb(matches);
    };
  };

  module.stripHtml = function (s) {
    if (s) {
      s = s.replace(/<(?:.|\n)*?>/gm, '');
    }
    return s;
  };

  module.truncate = function (s, max) {
    if (s && s.length > max) {
      return s.substring(0, max) + '...';
    }
    return s;
  };
  return module;
});