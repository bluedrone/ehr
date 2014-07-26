/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

function app_loadCalendar() {
  request = $.ajax({
    type: "GET",
    contentType: "application/json",
    data:"{}",
    url: "app/getAppointmentsByClinician?sessionId="+clinician.sessionId,
    dataType: "json",
    success: function(data) {
      $('#app-calendar').fullCalendar('destroy');
      var calendar =  $('#app-calendar').fullCalendar({
        defaultView: app_currentCalendarView,
        header: {
          left: 'prev,next today',
          center: 'title',
          right: 'month,agendaWeek,agendaDay'
        },
        windowResize: true,
        selectable: false,
        selectHelper: true,
        select: function(start, end) {
          //newApptForm(start, end)
        },
        eventDrop: function(event, jsEvent, ui, view) {
          moveAppt(event, jsEvent, ui, view);
        },
        eventResize: function(event, jsEvent, ui, view) {
          resizeAppt(event, jsEvent, ui, view);
        },
        lazyFetching: true,
        editable: false,
        eventRender: function(event, element) {
          startDate = event.start.format('h:mm');
          endDate = event.end.format('h:mm');
          eventTitle = event.title; 
          element.find('.fc-event-time').html(startDate + ' - ' + endDate);
          element.find('.fc-event-title').html(eventTitle);
        },
        eventMouseover: function(calEvent, jsEvent) {
          var tooltip = 
          '<div class="tooltipevetn" style="min-width:110px;max-width:400px;min-height:20px;background:#C0C0C0;color: black;'+
          'font-size: small; font-weight: bold;position:absolute;z-index:10001;">' + calEvent.desc + '</div>';
          $("body").append(tooltip);
          $(this).mouseover(function(e) {
            $(this).css('z-index', 10000);
            $('.tooltipevetn').fadeIn('500');
            $('.tooltipevetn').fadeTo('10', 1.9);
          }).mousemove(function(e) {
            $('.tooltipevetn').css('top', e.pageY + 10);
            $('.tooltipevetn').css('left', e.pageX + 20);
          });
        },
        eventMouseout: function(calEvent, jsEvent) {
          $(this).css('z-index', 8);
          $('.tooltipevetn').remove();
        },
        eventClick: function(calEvent, jsEvent, view) {
          //alert('Event: ' + calEvent.title);
          //alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
          //alert('View: ' + view.name);
          // change the border color just for fun
          //$(this).css('border-color', 'red');
          editApptForm(calEvent);
        },
        events: $.map(data, function(item, i) {
          var event = new Object();
          if (item != null){
            event.id = item.id;
            event.className = item.className;
            event.start = item.start;
            event.end = item.end;
            event.desc = item.desc;
            event.title = item.title;
            event.allDay = item.allDay;
            return event;
          }
        }),
        allDayDefault: false
      });
      $('.fc-button-agendaDay').click(function() { app_currentCalendarView = 'agendaDay'});
      $('.fc-button-agendaWeek').click(function() { app_currentCalendarView = 'agendaWeek'});
      $('.fc-button-month').click(function() { app_currentCalendarView = 'month'});
    },
    error: function(XMLHttpRequest, textStatus, errorThrown) {
      debug('There was an error while fetching data for calendar.');
    }
  });
}




function newApptForm(start, end) {
  var offset = new Date().getTimezoneOffset();
  start.add('m', offset);
  end.add('m', offset);
  var title = 'New Appointment';
  RenderUtil.render('dialog/event', {title:title,deleteButton:null,submitButtonLabel:'Add'}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-event').modal('show'); 
    
    if (app_currentCalendarView == 'month') {
      var startTimeString = dateFormat(start, 'mm/dd/yyyy') + ' 9:00 AM';
      var jsonData = JSON.stringify({ sessionId: clinician.sessionId, startTime: startTimeString, apptLengthInMinutes: 30 });
      $.post("app/suggestApptSlot", {data:jsonData}, function(data) {
        var parsedData = $.parseJSON(data);
        $('#app-appt-start').val(dateFormat(parsedData.newApptStartTime, 'h:MM TT'));
        $('#app-appt-end').val(dateFormat(parsedData.newApptEndTime, 'h:MM TT'));
      });
    }
    else {
      $('#app-appt-start').val(dateFormat(start, 'h:MM TT'));
      $('#app-appt-end').val(dateFormat(end, 'h:MM TT'));
    }
    getClinicians();
    $('#app-appt-clinician').on('change',function(){
      selectedClinician = $('#app-appt-clinician').val();
      getClinicianPatients();
    });
    // note: sending start date as end date since we want to stay on the same day.
    end = moment(start);
    $('#app-appt-submit').off().on("click", function (e) { handleNewAppt(e, start, end, offset); });
  });
}



function deleteApptConfirm(e, id) {
  var args = {
    modalTitle:"Delete Appointment", 
    modalH3:"Delete Appointment?",
    modalH4:"",
    cancelButton: 'Cancel',
    okButton: 'Confirm'
  };
  RenderUtil.render('dialog/confirm', args, function(s) { 
    $('#modals-placement').append(s);
    $('#modal-confirm').modal('show'); 
    $('#app-modal-confirmation-btn').click(function(){  
      var jsonData = JSON.stringify({ sessionId: clinician.sessionId, id:id });
      $.post("app/deleteAppt", {data:jsonData}, function(data) {
        $('#modal-confirm').modal('hide'); 
        $('#modal-event').modal('hide');
        displayNotification('Appointment Deleted.');
        app_loadCalendar();
      }); 
    });
  });
}



function editApptForm(calEvent) {
  var start = calEvent.start;
  var end = calEvent.end;
  var id = calEvent.id;
  var offset = new Date().getTimezoneOffset();
  start.add('m', offset);
  end.add('m', offset);
  var title = 'Appointment Details';
  RenderUtil.render('dialog/event', {title:title, deleteButton:null,submitButtonLabel:null}, function(s) {
    $('#modals-placement').html(s);
    $('#modal-event').modal('show'); 
    $('#app-appt-start').val(dateFormat(start, 'h:MM TT'));
    $('#app-appt-end').val(dateFormat(end, 'h:MM TT'));
    $('#app-appt-desc').val(calEvent.desc);
    
    var jsonData = JSON.stringify({ 
      sessionId: clinician.sessionId,
      id: calEvent.id
    });
  
    $.post("app/getAppointment", {data:jsonData}, function(data) {
      var parsedData = $.parseJSON(data);
      var appt = parsedData.appointment;
      var patientFullName = util_buildFullName(appt.patient.cred.firstName, appt.patient.cred.middleName, appt.patient.cred.lastName);
      $('#app-appt-patient-text').val(patientFullName);
      $('#app-appt-patient-text').show();
      $('#app-appt-patient').hide();
      var clinicianFullName = util_buildFullName(appt.clinician.firstName, appt.clinician.middleName, appt.clinician.lastName);
      $('#app-appt-clinician-text').val(clinicianFullName);
      $('#app-appt-clinician-text').show();
      $('#app-appt-clinician').hide();
    });
    
    $('#app-appt-submit').off().on("click", function (e) { handleUpdateAppt(e, start, end, id); });
    $('#app-appt-delete').off().on("click", function (e) { deleteApptConfirm(e, id); });
  });
}




function moveAppt(event, jsEvent, ui, view) {
  var start = event.start;
  var end = event.end;
  var offset = new Date().getTimezoneOffset();
  start.add('m', offset);
  end.add('m', offset);
  var startTimeString = dateFormat(start, 'mm/dd/yyyy') + " " + dateFormat(start, 'h:MM TT');
  var endTimeString = dateFormat(end, 'mm/dd/yyyy') + " " + dateFormat(end, 'h:MM TT');
  
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId,
    startTime: startTimeString,
    endTime: endTimeString,
    id: event.id
  });
  
  $.post("app/changeApptTime", {data:jsonData}, function(data) {
    displayNotification('Appointment moved.');
    var parsedData = $.parseJSON(data);
    app_loadCalendar();
  });
}



function resizeAppt(event, jsEvent, ui, view) {
  var start = event.start;
  var end = event.end;
  var offset = new Date().getTimezoneOffset();
  start.add('m', offset);
  end.add('m', offset);
  var startTimeString = dateFormat(start, 'mm/dd/yyyy') + " " + dateFormat(start, 'h:MM TT');
  var endTimeString = dateFormat(end, 'mm/dd/yyyy') + " " + dateFormat(end, 'h:MM TT');
  
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId,
    startTime: startTimeString,
    endTime: endTimeString,
    id: event.id
  });
  
  $.post("app/changeApptTime", {data:jsonData}, function(data) {
    displayNotification('Appointment length changed.');
    var parsedData = $.parseJSON(data);
    app_loadCalendar();
  });
}



function handleUpdateAppt(e, start, end, id) {
  var isValid = true;
  handleNewAppt_clearErrors();
  
  var apptStartValid = util_checkRegexp($.trim($("#app-appt-start").val()), /^(0?[1-9]|1[012])(:[0-5]\d) [APap][mM]$/);
  if (apptStartValid == false) {
    showError('#app-appt-start-validation', 'invalid time format');
    isValid = false;
  }
  
  var apptEndValid = util_checkRegexp($.trim($("#app-appt-end").val()), /^(0?[1-9]|1[012])(:[0-5]\d) [APap][mM]$/);
  if (apptEndValid == false) {
    showError('#app-appt-end-validation', 'invalid time format');
    isValid = false;
  }

  if (isValid == false) {
    return;
  }
  
  var startTimeString = dateFormat(start, 'mm/dd/yyyy') + " " + $('#app-appt-start').val();
  var endTimeString = dateFormat(end, 'mm/dd/yyyy') + " " + $('#app-appt-end').val();
  var startDate = new Date(startTimeString);
  var endDate = new Date(endTimeString);
  var startTimestamp = startDate.getTime();
  var endTimestamp = endDate.getTime();
 
  if (endTimestamp < startTimestamp) {
    isValid = false;
    showError('#app-appt-end-validation', 'invalid time range');
  }
  else if (endTimestamp - startTimestamp < 900000) {
    isValid = false;
    showError('#app-appt-end-validation', 'Appointment must be at least 15 minutes long.');
  }
  
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId,
    id: id,
    startTime: startTimeString,
    endTime: endTimeString,
    desc: $('#app-appt-desc').val() 
  });
  
  $.post("app/updateAppt", {data:jsonData}, function(data) {
    handleNewAppt_clearForm();
    displayNotification('Appointment updated.');
    var parsedData = $.parseJSON(data);
    $('#modal-event').modal('hide');
    app_loadCalendar();
  });
 
}


function handleNewAppt(e, start, end) {
  var isValid = true;
  handleNewAppt_clearErrors();
  
  var apptStartValid = util_checkRegexp($.trim($("#app-appt-start").val()), /^(0?[1-9]|1[012])(:[0-5]\d) [APap][mM]$/);
  if (apptStartValid == false) {
    showError('#app-appt-start-validation', 'invalid time format');
    isValid = false;
  }
  
  var apptEndValid = util_checkRegexp($.trim($("#app-appt-end").val()), /^(0?[1-9]|1[012])(:[0-5]\d) [APap][mM]$/);
  if (apptEndValid == false) {
    showError('#app-appt-end-validation', 'invalid time format');
    isValid = false;
  }
  
  if($("#app-appt-clinician").val().length < 1) { 
    showError('#app-appt-clinician-validation');
    isValid = false;
  }
  if(!$("#app-appt-patient").val() || $("#app-appt-patient").val().length < 1) { 
    showError('#app-appt-patient-validation');
    isValid = false;
  }

  
  var startTimeString = dateFormat(start, 'mm/dd/yyyy') + " " + $('#app-appt-start').val();
  var endTimeString = dateFormat(end, 'mm/dd/yyyy') + " " + $('#app-appt-end').val();
  var startDate = new Date(startTimeString);
  var endDate = new Date(endTimeString);
  var startTimestamp = startDate.getTime();
  var endTimestamp = endDate.getTime();
 
  if (endTimestamp < startTimestamp) {
    isValid = false;
    showError('#app-appt-end-validation', 'invalid time range');
  }
  else if (endTimestamp - startTimestamp < 900000) {
    isValid = false;
    showError('#app-appt-end-validation', 'Appointment must be at least 15 minutes long.');
  }
  
  if (isValid == false) {
    return;
  }
  
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId,
    startTime: startTimeString,
    endTime: endTimeString,
    clinician: $('#app-appt-clinician').val(), 
    patient: $('#app-appt-patient').val(),
    desc: $('#app-appt-desc').val() 
  });
  
  $.post("app/newAppt", {data:jsonData}, function(data) {
    handleNewAppt_clearForm();
    displayNotification('New appointment created.');
    var parsedData = $.parseJSON(data);
    $('#modal-event').modal('hide');
    app_loadCalendar();
  });
 
}

function handleNewAppt_clearForm() {
  $('#app-appt-start').val('');
  $('#app-appt-end').val('');
  $('#app-appt-clinician').val('');
  $('#app-appt-patient').val('');
  $('#app-appt-desc').val('');
  handleNewAppt_clearErrors();
}

function handleNewAppt_clearErrors() {
  $('#app-appt-start-validation').css({visibility: "hidden"});
  $('#app-appt-end-validation').css({visibility: "hidden"});
  $('#app-appt-clinician-validation').css({visibility: "hidden"});
  $('#app-appt-patient-validation').css({visibility: "hidden"});
  $('#app-appt-desc-validation').css({visibility: "hidden"});
}