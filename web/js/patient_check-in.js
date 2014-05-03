function initPatientIntakeFunctions() {
    
  $('.patient-triage-control').each(function() { 
    var val = parseInt($(this).html());
    if (val == 1) { $(this).css("color", "#D9534F"); $(this).parent().css("background-color", "#f2dede");}
    else if (val == 2) { $(this).css("color", "#F0AD4E"); $(this).parent().css("background-color", "#fcf8e3"); }
    else if (val == 3) { $(this).css("color", "#5CB85C"); $(this).parent().css("background-color", "#dff0d8"); }
  });
      
  $('.toggle-badge').click(function() { 
    if ($(this).children("i").hasClass('icon-hidden') ) {
      $(this).children("i").removeClass('icon-hidden');
    }
    else {
      $(this).children("i").addClass('icon-hidden');
    }
  });
    
  $('.patient-missing-control').click(function() { 
    if ($(this).has(".patient-missing-checkbox").length) {
      $(this).html("<span class='patient-missing-badge badge badge-danger'><i class='icon-question'></i></span>");
    }
    else {
      $(this).html("<input class='patient-missing-checkbox' type='checkbox' name='missing' style='toggle-missing'>");
    }
  });
    
  $('.patient-triage-control').blur(function() { 
    var val = parseInt($(this).html());
    if (val == 1) { $(this).css("color", "#D9534F"); $(this).parent().css("background-color", "#f2dede");}
    else if (val == 2) { $(this).css("color", "#F0AD4E"); $(this).parent().css("background-color", "#fcf8e3"); }
    else if (val == 3) { $(this).css("color", "#5CB85C"); $(this).parent().css("background-color", "#dff0d8"); }
  });
    
  $('.patient-delete-control').click(function() { 
    deletePatient($(this).closest("tr"));
  });
    
  $(".edit-on-select").focus(function() {
    $(this).selectContentEditableText();
  });
  
  $(".check-in-biometrics").blur(function() {
    getPatientIntakeId($(this).closest("tr"));
    var input = $(this).html().toUpperCase();
    var bioFinger = input.indexOf("F") != -1;
    var bioPhoto = input.indexOf("P") != -1;
    updatePatientBiometrics(bioFinger, bioPhoto, app_currentPatientIntakeId)
  });
  
  $(".check-in-biometrics").keydown(function(e) {
    util_filterBiometricsInput(e);
  });
  
  $("#patient-intake-biometrics").keydown(function(e) {
    util_filterBiometricsInput(e);
  });
  
  $(".check-in-nombre").blur(function() {
    getPatientIntakeId($(this).closest("tr"));
    updatePatient("nombre", $(this).html(), app_currentPatientIntakeId);
  });
  
  $(".check-in-apellido").blur(function() {
    getPatientIntakeId($(this).closest("tr"));
    updatePatient("apellido", $(this).html(), app_currentPatientIntakeId);
  });
  
  $(".check-in-apellido-segundo").blur(function() {
    getPatientIntakeId($(this).closest("tr"));
    updatePatient("apellidoSegundo", $(this).html(), app_currentPatientIntakeId);
  });
  
  $(".check-in-age").blur(function() {
    getPatientIntakeId($(this).closest("tr"));
    updatePatient("age", $(this).html(), app_currentPatientIntakeId);
  });
  
  $(".check-in-age").keydown(function(e) {
    util_filterAgeInput(e);
  });
  
  $("#patient-intake-age").keydown(function(e) {
    util_filterAgeInput(e);
  });
  
  $(".check-in-gender").blur(function() {
    getPatientIntakeId($(this).closest("tr"));
    updatePatient("gender", $(this).html().toUpperCase(), app_currentPatientIntakeId);
  });
  
  $(".check-in-gender").keydown(function(e) {
    util_filterGenderInput(e);
  });
  
  $("#patient-intake-gender").keydown(function(e) {
    util_filterGenderInput(e);
  });
  
  $(".check-in-triage").blur(function() {
    getPatientIntakeId($(this).closest("tr"));
    updatePatient("triage", $(this).html(), app_currentPatientIntakeId);
  });
    
  $(".check-in-triage").keydown(function(e) {
    util_filterTriageInput(e);
  });
  
  $("#patient-intake-triage").keydown(function(e) {
    util_filterTriageInput(e);
  });
  
  $(".check-in-notes").blur(function() {
    getPatientIntakeId($(this).closest("tr"));
    updatePatient("notes", $(this).html(), app_currentPatientIntakeId);
  });
  
  $(".check-in-check-in").click(function() {
    getPatientIntakeId($(this).closest("tr"));
    updatePatient("checkIn", $(this).children("i").hasClass('icon-hidden') == true, app_currentPatientIntakeId);
  });
  
  $(".check-in-intake").click(function() {
    getPatientIntakeId($(this).closest("tr"));
    updatePatient("intake", $(this).children("i").hasClass('icon-hidden') == true, app_currentPatientIntakeId);
  });
  
  $(".check-in-provider").click(function() {
    getPatientIntakeId($(this).closest("tr"));
    updatePatient("provider", $(this).children("i").hasClass('icon-hidden'), app_currentPatientIntakeId);
  });
  
  $(".check-in-missing").click(function() {
    getPatientIntakeId($(this).closest("tr"));
    updatePatient("missing", $(this).has(".patient-missing-checkbox").length == 0, app_currentPatientIntakeId);
  });
    
  $(".check-in-completed").click(function() {
    getPatientIntakeId($(this).closest("tr"));
    updatePatient("completed", $(this).children("i").hasClass('icon-hidden'), app_currentPatientIntakeId);
  });
} 


function updatePatient(property, value, patientId) {
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    patientId: patientId,
    updateProperty:property,
    updatePropertyValue:value
  });
  $.post("patient/updatePatient", {data:jsonData}, function(data) {
    viewPatientCheckInList(DONT_SCROLL);
  }); 
}

function updatePatientBiometrics(bioFinger, bioPhoto, patientId) {
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    patientId: patientId,
    bioFinger:bioFinger,
    bioPhoto:bioPhoto
  });
  $.post("patient/updatePatientBiometrics", {data:jsonData}, function(data) {
    viewPatientCheckInList(DONT_SCROLL);
  }); 
}

function deletePatient(tr) {
  getPatientIntakeId(tr);
  var jsonData = JSON.stringify({ sessionId: clinician.sessionId, patientId:app_currentPatientIntakeId});
  $.post("patient/deletePatient", {data:jsonData}, function(data) {
    viewPatientCheckInList(DO_SCROLL);
  });  
}

function getPatientIntakeGroups(scroll) {
  var jsonData = JSON.stringify({ sessionId: clinician.sessionId });
  $.post("patient/getPatientIntakeGroups", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    app_patientIntakeGroups = parsedData.patientIntakeGroups;
    buildGroupOrderArray();
    RenderUtil.render('check-in_list', {groups:app_patientIntakeGroups}, function(s) { 
      $('#patient-intake-screen').html(s);
      initPatientIntakeFunctions(); 
      app_viewStack('patient-intake-screen', scroll);
      
      $('.intake-forms-link').click(function(e){ viewPatientIntakeFormGroup(e); });
      
      $('.intake-group-down').click(function(e) { 
         getCurrentGroupId(e);
         getCurrentGroupIndex(e);
         if (app_currentGroupIndex == app_groupOrderArray.length-1) {
           return; 
         }
         var swapGroupId = app_groupOrderArray[app_currentGroupIndex+1];
         var jsonData = JSON.stringify({ 
           sessionId: clinician.sessionId, 
           patientIntakeGroupId: app_currentGroupId, 
           swapGroupId: swapGroupId
         });
         $.post("patient/swapSortOrder", {data:jsonData}, function(data) {
           viewPatientCheckInList(DO_SCROLL);
         }); 
      });
      
      $('.intake-group-up').click(function(e) { 
         getCurrentGroupId(e);
         getCurrentGroupIndex(e);
         if (app_currentGroupIndex == 0) {
           return; 
         }
         var swapGroupId = app_groupOrderArray[app_currentGroupIndex-1];
         var jsonData = JSON.stringify({ 
           sessionId: clinician.sessionId, 
           patientIntakeGroupId: app_currentGroupId, 
           swapGroupId: swapGroupId
         });
         $.post("patient/swapSortOrder", {data:jsonData}, function(data) {
           viewPatientCheckInList(DO_SCROLL);
         }); 
      });
      
      $('.intake-add-patient-to-group-link').click(function(e) { 
        RenderUtil.render('add_patient_intake_to_group', {}, function(s) { 
         $('#modals-placement').html(s);
         initPatientIntakeFunctions(); 
         $('#modal-add-patient-intake-to-group').modal('show'); 
         clearNewPatientIntakeForm();
         getCurrentGroupId(e);
         
         $("#btn-patient-intake-cancel-new-patient").click(function() {
           initPatientIntakeFunctions(); 
         });
         
         $("#btn-patient-intake-save-new-patient").click(function() {
           var biometrics = $("#patient-intake-biometrics").val().toUpperCase();
           encounter = new Encounter();
           encounter.patient = new Patient();
           encounter.patient.cred = new Credentials();
           encounter.patient.demo = new Demographics();
           encounter.patient.cred.firstName = $.trim($("#patient-intake-nombre").val());
           encounter.patient.cred.lastName = $.trim($("#patient-intake-apellido").val());
           encounter.patient.cred.additionalName = $.trim($("#patient-intake-apellido-segundo").val());
           encounter.patient.demo.bioFinger = biometrics.indexOf("F") != -1;
           encounter.patient.demo.bioPhoto = biometrics.indexOf("P") != -1; 
           encounter.patient.demo.gender = createGender($.trim($("#patient-intake-gender").val().toUpperCase()));
           encounter.patient.demo.age = $.trim($("#patient-intake-age").val());
           encounter.triage = $.trim($("#patient-intake-triage").val()); 
           encounter.notes = $.trim($("#patient-intake-notes").val());
           encounter.checkIn = !$("#patient-intake-check-in").children("i").hasClass('icon-hidden');
           encounter.intake =  !$("#patient-intake-intake").children("i").hasClass('icon-hidden');
           encounter.provider =  !$("#patient-intake-provider").children("i").hasClass('icon-hidden');
           encounter.missing = !$("#patient-intake-missing").has(".patient-missing-checkbox").length;
           encounter.completed =  !$("#patient-intake-completed").children("i").hasClass('icon-hidden');
           encounter.patientIntakeGroupId = app_currentGroupId;
           encounter.lockStatus = INTAKE_FREE;
           
           var jsonData = JSON.stringify({ 
             sessionId: clinician.sessionId, 
             encounter: encounter
           });
           
           $.post("patient/createPatientAndEncounter", {data:jsonData}, function(data) {
             viewPatientCheckInList(DO_SCROLL);
           }); 
         });
        });
      });
   });
 });
}


function viewPatientCheckInList(scroll) {
  $('#section-notification').css("visibility", "visible");
  $('.patient-navbar-btn').css("display", "none");
  $('.check-in-navbar-btn').css("display", "inline-block");
  $('.intake-navbar-btn').css("display", "none");
  $('#section-notification-text').html("");
  getPatientIntakeGroups(scroll);
}

function showAddGroupForm() {
  
  app_newPatientIntakeGroup = new PatientIntakeGroup();
  
  RenderUtil.render('new_patient_intake_group', {}, function(s) { 
    $('#modals-placement').html(s);
    $('#modal-intake-group').modal('show'); 
 
    initPatientIntakeFunctions(); 
    clearNewPatientIntakeForm();
    
    $('#app-check-in-add-patient-link').click(function() { 
       var biometrics = $("#patient-intake-biometrics").val().toUpperCase();
       encounter = new Encounter();
       encounter.patient = new Patient();
       encounter.patient.cred = new Credentials();
       encounter.patient.demo = new Demographics();
       encounter.patient.cred.firstName = $.trim($("#patient-intake-nombre").val());
       encounter.patient.cred.lastName = $.trim($("#patient-intake-apellido").val());
       encounter.patient.cred.additionalName = $.trim($("#patient-intake-apellido-segundo").val());
       encounter.patient.demo.bioFinger = biometrics.indexOf("F") != -1;
       encounter.patient.demo.bioPhoto = biometrics.indexOf("P") != -1; 
       encounter.patient.demo.gender = createGender($.trim($("#patient-intake-gender").val().toUpperCase()));
       encounter.patient.demo.age = $.trim($("#patient-intake-age").val());
       encounter.triage = $.trim($("#patient-intake-triage").val()); 
       encounter.notes = $.trim($("#patient-intake-notes").val());
       encounter.checkIn = !$("#patient-intake-check-in").children("i").hasClass('icon-hidden');
       encounter.intake =  !$("#patient-intake-intake").children("i").hasClass('icon-hidden');
       encounter.provider =  !$("#patient-intake-provider").children("i").hasClass('icon-hidden');
       encounter.missing = !$("#patient-intake-missing").has(".patient-missing-checkbox").length;
       encounter.completed =  !$("#patient-intake-completed").children("i").hasClass('icon-hidden');
       encounter.patientIntakeGroupId = app_currentGroupId;
       encounter.lockStatus = INTAKE_FREE;
       app_newPatientIntakeGroup.encounterList.push(encounter);
      
      var args = {
        sessionId: clinician.sessionId, 
        encounter: encounter
      };
      RenderUtil.render('component/patient_intake_row', args, function(s) { 
        $("#new-patient-group-table").append(s);        
        clearNewPatientIntakeForm();
      });
    });
        
    $("#btn-patient-intake-save-new-group").click(function() {
      app_newPatientIntakeGroup.name = $.trim($("#intake-new-group-name").val());
      var jsonData = JSON.stringify({ sessionId: clinician.sessionId, newPatientIntakeGroup: app_newPatientIntakeGroup });
      $.post("patient/createPatientIntakeGroup", {data:jsonData}, function(data) {
        viewPatientCheckInList(DO_SCROLL);
      }); 
    });
    
    
    $("#btn-patient-intake-cancel-new-group").click(function() {
      initPatientIntakeFunctions(); 
    });
    
  });
}

function clearNewPatientIntakeForm() {
  $("#patient-intake-biometrics").val('');
  $("#patient-intake-nombre").val('');
  $("#patient-intake-apellido").val('');
  $("#patient-intake-apellido-segundo").val('');
  $("#patient-intake-gender").val('');
  $("#patient-intake-age").val('');
  $("#patient-intake-triage").val('');
  $("#patient-intake-notes").val('');
  $("#patient-intake-check-in").children("i").addClass('icon-hidden');
  $("#patient-intake-intake").children("i").addClass('icon-hidden');
  $("#patient-intake-provider").children("i").addClass('icon-hidden');
  $("#patient-intake-completed").children("i").addClass('icon-hidden');
  $("#patient-intake-missing").html("<input class='patient-missing-checkbox' type='checkbox' name='missing' style='toggle-missing'>");
}  

function getPatientIntakeId(tr) {
  var attributes = tr[0].attributes;
  for (i=0;i<attributes.length;i++) {
    if (attributes[i].name == 'data-patient-intake-id') {
      app_currentPatientIntakeId = parseInt(attributes[i].nodeValue); 
    }
  }
}

function getCurrentGroupId(e) {
  var attributes = e.currentTarget.attributes;
  for (i=0;i<attributes.length;i++) {
    if (attributes[i].name == 'data-group-id') {
      app_currentGroupId = parseInt(attributes[i].nodeValue); 
    }
  }
}  

function getCurrentGroupIndex(e) {
  var attributes = e.currentTarget.attributes;
  for (i=0;i<attributes.length;i++) {
    if (attributes[i].name == 'data-group-index') {
      app_currentGroupIndex = parseInt(attributes[i].nodeValue); 
    }
  }
}  

function buildGroupOrderArray() {
  app_groupOrderArray = [];
  for (i=0;i<app_patientIntakeGroups.length;i++) {
    app_groupOrderArray.push(parseInt(app_patientIntakeGroups[i].id));
  }
}

function getPatientIntakeGroup(id) {
  for (i=0;i<app_patientIntakeGroups.length;i++) {
    if (app_patientIntakeGroups[i].id == id) {
      return app_patientIntakeGroups[i];
    }
  }
}

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




    