/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */


var debug = function (log_txt) {
  if (window.console != undefined) {
    console.log(log_txt);
  }
}

function util_processNumber(selector, property) {
  var value = $.trim($(selector).val());
  if (value.length > 0) {property = value;}
  return property;
}


function util_processDate(selector, property) {
  //"Mar 17, 2014 10:01:12 PM";
  var date = $.trim($(selector).val());
  if (date.length > 0) {
    property = dateFormat(date, "EEE MMM dd kk:mm:ss z yyyy"); 
  }
  return property;
}

function util_processDob(selector, property) {
  var date = $.trim($(selector).val());
  if (date.length > 0) {
    property = dateFormat(date, "mm/dd/yyyy"); 
  }
  return property;
}


function util_checkRegexp(s, regexp) {
  return regexp.test(s);
}

function util_checkPassword(s) {
  if (util_checkRegexp(s,/[a-z]/) && util_checkRegexp(s,/[A-Z]/) && util_checkRegexp(s,/\d/) && util_checkRegexp(s,/\W/)) {
    return true;
  }
  return false;
}

function util_buildFullName(first, middle, last) {
  var middleToken = "";
  if (typeof first !== 'undefined' && first.length > 0) {
    if (typeof middle !== 'undefined' && middle.length > 0) {
      middleToken = middle + " ";
    }
    last = (typeof last === 'undefined') ? '' : last;
    return first + " " + middleToken + last;
  }
  else {
      return "";
  }
}


function util_nullCheck(val) {
  if (typeof val !== 'undefined') {
    return val;
  }
  return "";
}

function util_checkSessionResponse(obj) {
  idleTime = 0;
  if (obj != undefined){
  if(obj.authenticated == false){
    //util_logout(DO_AUTO_LOGOUT);
    return false;
  }
  return true;
  }
  else {
   DO_AUTO_LOGOUT = false;
   util_logout(DO_AUTO_LOGOUT, DO_AUTO_SERVER_LOGOUT);
   return false;   
  }
}


function util_checkSession() {
  var jsonData = JSON.stringify({sessionId: user.sessionId});
  $.post("auth/checkSession",{data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    if(parsedData.authenticated == false){util_logout(DO_AUTO_LOGOUT);return false;}
  }); 
}

function util_filterTriageInput(e) {
  var key = e.keyCode || e.which;
  if ((key != 8 && key != 9 && key < 49) || key > 51) { // 1,2,3 
    if (e.preventDefault) {e.preventDefault();}
    e.returnValue = false; 
  }
}

function util_filterDecimalInput(e) {
  var key = e.keyCode || e.which;
  if ((key != 8 && key != 9 && key != 46 && key < 48) || (key > 57 && key != 190)) { // 0-9 .
    if (e.preventDefault) {e.preventDefault();}
    e.returnValue = false; 
  }
}

function util_filterAgeInput(e) {
  var key = e.keyCode || e.which;
  if ((key != 8 && key != 9 && key < 48) || key > 57) { // 0-9
    if (e.preventDefault) {e.preventDefault();}
    e.returnValue = false; 
  }
}

function util_filterGenderInput(e) {
  var key = e.keyCode || e.which;
  if (key != 8 && key != 9 && key != 70 && key != 77 & key != 102 && key != 109) { // [MmFf]
    if (e.preventDefault) {e.preventDefault();}
    e.returnValue = false; 
  }
}

function util_filterBiometricsInput(e) {
  var key = e.keyCode || e.which;
  if (key != 8 && key != 9 && key != 70 && key != 102 & key != 80 && key != 112 && key != 32) { // [PpFf]
    if (e.preventDefault) {e.preventDefault();}
    e.returnValue = false; 
  }
}

String.prototype.trunc = function(n,useWordBoundary){
  var toLong = this.length>n,
  s_ = toLong ? this.substr(0,n-1) : this;
  s_ = useWordBoundary && toLong ? s_.substr(0,s_.lastIndexOf(' ')) : s_;
  return  toLong ? s_ + '&hellip;' : s_;
};

	
function util_selectCheckboxesFromList(list, name) {
  if (list !== undefined) { 
    var a = list.split(","); 
    for (i=0;i<a.length;i++) {
      var s = a[i];
        $("input[name="+name+"][value="+s+"]").attr("checked", true);
    }
  }
}

function util_showError(item, message) {
  if (message == null) {
    message = 'required field';
  }
  $(item).text(message);
  $(item).css({opacity: 0.0, visibility: "visible"}).animate({opacity: 1.0}); 
}


function util_printForm(template, object, title) { 
  var currentDate = dateFormat(new Date(), 'mm/dd/yyyy');
  RenderUtil.render('print/'+template,  {object:object, currentDate:currentDate}, function(obj) {
    var s = obj[0].outerHTML;
    print_openPrintWindow('print.html', s, title);
  });
}

function util_isNumber(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}


var util_substringMatcher = function(strs) {
  return function findMatches(q, cb) {
    var matches, substringRegex;
 
    // an array that will be populated with substring matches
    matches = [];
 
    // regex used to determine if a string contains the substring `q`
    substrRegex = new RegExp(q, 'i');
 
    // iterate through the pool of strings and for any string that
    // contains the substring `q`, add it to the `matches` array
    $.each(strs, function(i, str) {
      if (substrRegex.test(str)) {
        // the typeahead jQuery plugin expects suggestions to a
        // JavaScript object, refer to typeahead docs for more info
        matches.push({ value: str });
      }
    });
 
    cb(matches);
  };
};



function util_stripHtml(s) {
  if (s) {
    s = s.replace(/<(?:.|\n)*?>/gm, '');
  }
  return s;
}



function util_truncate(s, max) {
  if (s && s.length > max) {
    return s.substring(0,max)+'...';
  }
  return s;
};



function createGender(code) {
  var gender = new Gender();
  gender.code = code; 
  if (!code) {
    gender.id = 5;
    gender.name = 'Unreported';
    gender.code = 'U'; 
  }
  else if (code == 'M') {
    gender.id = 1;
    gender.name = 'Male';
  }
  else if (code == 'F') {
    gender.id = 2;
    gender.name = 'Female';
  }
  return gender;
}