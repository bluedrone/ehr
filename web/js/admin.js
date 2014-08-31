/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */


$('#admin').click(function() { 
  app_viewStack('user-admin-screen', DO_SCROLL);
  getCliniciansList();
});


$('#user-admin-new-user-lg, #user-admin-new-user-sm').click(function() { 
  RenderUtil.render('user_form', {formMode:'add'}, function(s) { 
    $('#modals-placement').html(s);
    $('#modal-admin-user-form').modal('show'); 
    $('#modal-admin-user-form .form-control-unsaved').css({display: "block"});
    $('#modal-admin-user-form .form-control-saved').css({display: "none"});
    $('#admin-user-form-title').html("Add User");
    userForm_clearForm();
    $('#admin-user-form-save').click(function() { saveNewUser() });
  });
});

function getCliniciansList() {
  var jsonData = JSON.stringify({ sessionId: clinician.sessionId });
  $.post("app/getClinicians", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    clinicians = parsedData.clinicians;
    RenderUtil.render('component/user_admin_data_table', 
     {items:clinicians, 
      title:'Clinicians', 
      tableName:'user-admin-clinicians-list', 
      clickable:true
      }, function(s) {
      $('#user-admin-clinicians-list').html(s);
      $('#user-admin-clinicians-list-title').html("Clinicians");
      $('.clickable-table-row').dblclick( function(e){ handleDoubleClickedRow(e); });
      $('.user-admin-user-details').click( function(e){ handleUserDetails(e); });
      $('.user-admin-change-user-status').click( function(e){ handleChangeUserStatus(e); });
      $('.user-admin-delete-user').click( function(e){ handleDeleteUser(e); });
    });
  });
}

function findClinicianFromList(id) {
  for (i=0;i<clinicians.length;i++) {
    if (clinicians[i].id == id) {
      return clinicians[i];
    }
  }
}

function updateClinicianInList(id, property, value) {
  for (i=0;i<clinicians.length;i++) {
    if (clinicians[i].id == id) {
      clinicians[i][property] = value;
    }
  }
}


function handleUserDetails(e) {
  var id = e.currentTarget.name;
  var clinicianToChange = findClinicianFromList(id);
  RenderUtil.render('user_form', {formMode:'edit'}, function(s) { 
    $('#modals-placement').html(s);
    $('#modal-admin-user-form').modal('show'); 
    $('#modal-admin-user-form .form-control-unsaved').css({display: "none"});
    $('#modal-admin-user-form .form-control-saved').css({display: "block"});
    $('#admin-user-form-title').html("View/Edit User");
    
    $('#user-form-first-name-saved').html(clinicianToChange.firstName);
    $('#user-form-middle-name-saved').html(clinicianToChange.middleName);
    $('#user-form-last-name-saved').html(clinicianToChange.lastName);
    $('#user-form-username-saved').html(clinicianToChange.username);
    $('#user-form-group-name-saved').html(clinicianToChange.groupName);
    $('#user-form-practice-name-saved').html(clinicianToChange.practiceName);
    $('#user-form-role-saved').html(clinicianToChange.role.name);
    $('#user-form-credential-saved').html(clinicianToChange.credential.name);
    $('#user-form-primary-phone-saved').html(clinicianToChange.primaryPhone);
    $('#user-form-secondary-phone-saved').html(clinicianToChange.secondaryPhone);
    $('#user-form-email-saved').html(clinicianToChange.email);
    $('#user-form-pager-saved').html(clinicianToChange.pager);
    $('#user-form-password-saved').html("******");
      
    $(".edit-on-select").focus(function() { $(this).selectContentEditableText(); });
    $('#user-form-first-name-saved').blur(function() { updateSavedUser("firstName", $(this).html(), id); });
    $('#user-form-middle-name-saved').blur(function() { updateSavedUser("middleName", $(this).html(), id); });
    $('#user-form-last-name-saved').blur(function() { updateSavedUser("lastName", $(this).html(), id); });
    $('#user-form-username-saved').blur(function() { updateSavedUser("username", $(this).html(), id); });
    $('#user-form-group-name-saved').blur(function() { updateSavedUser("groupName", $(this).html(), id); });
    $('#user-form-practice-name-saved').blur(function() { updateSavedUser("practiceName", $(this).html(), id); });
    $('#user-form-primary-phone-saved').blur(function() { updateSavedUser("primaryPhone", $(this).html(), id); });
    $('#user-form-secondary-phone-saved').blur(function() { updateSavedUser("secondaryPhone", $(this).html(), id); });
    $('#user-form-pager-saved').blur(function() { updateSavedUser("pager", $(this).html(), id); });
    $('#user-form-email-saved').blur(function() { updateSavedUser("email", $(this).html(), id); });
    $('#user-form-password-saved').blur(function() { 
      var isValid = true;    	
      if($.trim($("#user-form-password").val()).length < 1) { util_showError('#user-form-password-validation'); isValid = false; }
      if ($.trim($("#user-form-password").val()).length > 0) {
        if($.trim($("#user-form-password").val()).length < 6) { 
          util_showError('#user-form-password-validation', 'must be at least 6 chararcters long'); 
          isValid = false; 
        }
        if(util_checkPassword($.trim($("#user-form-password").val())) == false) { 
          util_showError('#user-form-password-validation', 'must contain a lowercase, uppercase, digit, and special character'); 
          isValid = false; 
        }
      }
      if (isValid == true) {
        updateSavedUser("password", $(this).html(), id); 
      }
    });
    $('#user-form-role-saved').click(function() { 
      $(this).css({display: "none"});
      $('#user-form-role').css({display: "block"});
      $('#user-form-role').val(clinicianToChange.roleId);
      $('#user-form-role').change(function() { updateSavedUser("roleId", $(this).val(), id); });
    });
    $('#user-form-credential-saved').click(function() { 
      $(this).css({display: "none"});
      $('#user-form-credential').css({display: "block"});
      $('#user-form-credential').val(clinicianToChange.credentialId);
      $('#user-form-credential').change(function() { updateSavedUser("credentialId", $(this).val(), id); });
    });
  });
}
  

function handleDeleteUser(e) {
  var id = e.currentTarget.name;
  var clinicianToChange = findClinicianFromList(id);
  var clinicianFullName = util_buildFullName(clinicianToChange.firstName, clinicianToChange.middleName, clinicianToChange.lastName);
  var args = {
    modalTitle:"Delete Clinician", 
    modalH3:"Delete Clinician "+clinicianFullName+"?",
    modalH4:"Once deleted, the clinician will remain in the system but not be visible to the application.",
    cancelButton: 'Cancel',
    okButton: 'Confirm'
  };
  RenderUtil.render('dialog/confirm', args, function(s) { 
    $('#modals-placement').html(s);
    $('#modal-confirm').modal('show'); 
    $('#app-modal-confirmation-btn').click(function(){  
      var jsonData = JSON.stringify({ sessionId: clinician.sessionId, clinicianId: id });
      $.post("admin/purgeClinician", {data:jsonData}, function(data) {
        $('#modal-confirm').modal('hide'); 
        displayNotification('Clinician '+clinicianFullName+' deleted.');
        getCliniciansList();
      }); 
    });
  });
}


function handleChangeUserStatus(e) {
  var id = e.currentTarget.name;
  var clinicianToChange = findClinicianFromList(id);
  var clinicianFullName = util_buildFullName(clinicianToChange.firstName, clinicianToChange.middleName, clinicianToChange.lastName);
  var activationMode = clinicianToChange.active ? "deactivate"  : "activate";
  
  RenderUtil.render('dialog/change_user_status', {activationMode:activationMode, clinicianFullName: clinicianFullName}, function(s) { 
    $('#modals-placement').html(s);
    $('#modal-admin-change-user-status').modal('show'); 
    $('#app-modal-confirmation-btn').click(function(){  
      var method = activationMode == "deactivate" ? "deactivateClinician"  : "activateClinician";
      var jsonData = JSON.stringify({ sessionId: clinician.sessionId, clinicianId: id });
      $.post("admin/"+method, {data:jsonData}, function(data) {
        $('#modal-admin-change-user-status').modal('hide'); 
        displayNotification('Clinician '+clinicianFullName+' '+activationMode+'d.');
        getCliniciansList();
      }); 
    });
  });
}
    


function saveNewUser() {
  var phoneRegexObj = /^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/;
  var emailRegexObj = /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i;

  var isValid = true;
  userForm_clearErrors();

  if($("#user-form-first-name").val().length < 1) {util_showError('#user-form-first-name-validation'); isValid = false; }
  if($("#user-form-last-name").val().length < 1) {util_showError('#user-form-last-name-validation'); isValid = false; }
  if($("#user-form-username").val().length < 1) {util_showError('#user-form-username-validation'); isValid = false; }
  if($("#user-form-role").val().length < 1) {util_showError('#user-form-role-validation'); isValid = false; }
  if($("#user-form-primary-phone").val().length < 1) {util_showError('#user-form-primary-phone-validation'); isValid = false; }

  if ($.trim($("#user-form-password").val()) != PASSWORD_PLACEHOLDER) {  
    if($.trim($("#user-form-password").val()).length < 1) { util_showError('#user-form-password-validation'); isValid = false; }
    if ($.trim($("#user-form-password").val()).length > 0) {
      if($.trim($("#user-form-password").val()).length < 6 || $.trim($("#user-form-password-confirm").val()).length < 6) { 
        util_showError('#user-form-password-validation', 'must be at least 6 chararcters long'); 
        isValid = false; 
      }
      if(util_checkPassword($.trim($("#user-form-password").val())) == false) { 
        util_showError('#user-form-password-validation', 'must contain a lowercase, uppercase, digit, and special character'); 
        isValid = false; 
      }
      if($.trim($("#user-form-password").val()) != $.trim($("#user-form-password-confirm").val())) { 
        util_showError('#user-form-password-validation', 'passwords must match'); 
        isValid = false; 
      }
    }
  }
  
  if($.trim($("#user-form-email").val()).length < 1) { util_showError('#user-form-email-validation'); isValid = false; }
  var emailValid = util_checkRegexp($.trim($("#user-form-email").val()), emailRegexObj);
  if(emailValid == false) { util_showError('#user-form-email-validation', 'invalid email address'); isValid = false; }
  if($.trim($("#user-form-email").val()) != $.trim($("#user-form-email-confirm").val())) { 
    util_showError('#user-form-email-validation', 'email addresses must match'); 
    isValid = false; 
  }

  if (isValid == false) {
   return;
  }
  
  var jsonData = JSON.stringify({ 
    firstName: $('#user-form-first-name').val(), 
    middleName: $('#user-form-middle-name').val(), 
    lastName: $('#user-form-last-name').val(), 
    username: $('#user-form-username').val(), 
    groupName: $('#user-form-group-name').val(), 
    practiceName: $('#user-form-practice-name').val(), 
    roleId: $('#user-form-role').val(), 
    credentialId: $('#user-form-credential').val(), 
    primaryPhone: $('#user-form-primary-phone').val(), 
    secondaryPhone: $('#user-form-secondary-phone').val(), 
    email: $('#user-form-email').val(),
    password: $('#user-form-password').val(),
    pager: $('#user-form-pager').val(),
    sessionId: clinician.sessionId
  });
  $.post("admin/saveNewClinician", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    if (parsedData.returnCode == RETURN_CODE_DUP_USERNAME) {
      var args = {
        modalTitle:"Username Already In Use", 
        modalH3:"Username Already In Use", 
        modalH4:"Please try again with a different username.",
        cancelButton: null,
        okButton: 'OK'
      };
      RenderUtil.render('dialog/confirm', args, function(s) { 
        $('#modals-placement').append(s);
        $('#modal-confirm').modal('show'); 
        $('#app-modal-confirmation-btn').click(function(){  $('#modal-confirm').modal('hide');});
      });
      return;
    }
    $('#modal-admin-user-form').modal('hide');
    var clinicianFullName = util_buildFullName(parsedData.firstName, parsedData.middleName, parsedData.lastName);
    displayNotification('New user '+ clinicianFullName + ' created.');
    getCliniciansList();
  });
}


function updateSavedUser(property, value, id) {
  var jsonData = JSON.stringify({ 
    sessionId: clinician.sessionId, 
    updateProperty:property,
    updatePropertyValue:value,
    clinicianId: id
  });
  $.post("admin/updateClinician", {data:jsonData}, function(data) {
    var parsedData = $.parseJSON(data);
    if (parsedData.returnCode == RETURN_CODE_DUP_USERNAME) {
      var args = {
        modalTitle:"Username Already In Use", 
        modalH3:"Username Already In Use", 
        modalH4:"Please try again with a different username.",
        cancelButton: null,
        okButton: 'OK'
      };
      RenderUtil.render('dialog/confirm', args, function(s) { 
        $('#modals-placement').append(s);
        $('#modal-confirm').modal('show'); 
        $('#app-modal-confirmation-btn').click(function(){  $('#modal-confirm').modal('hide');});
      });
      return;
    }
    else {
      if (property == "roleId") {
        $("#user-form-role").css({display: "none"});
        $("#user-form-role-saved").html(createRole(value).name);
        $("#user-form-role-saved").css({display: "block"});
        updateClinicianInList(id, property, createRole(value).id);
      }
      else if (property == "credentialId") {
        $("#user-form-credential").css({display: "none"});
        $("#user-form-credential-saved").html(createCredential(value).name);
        $("#user-form-credential-saved").css({display: "block"});
        updateClinicianInList(id, property, createCredential(value).id);
      }
      else {
        updateClinicianInList(id, property, value);
      }
      getCliniciansList();
    }
  }); 
}

function userForm_clearForm() {
  $('#user-form-first-name').val('');
  $('#user-form-middle-name').val('');
  $('#user-form-last-name').val('');
  $('#user-form-username').val('');
  $('#user-form-group-name').val('');
  $('#user-form-practice-name').val('');
  $('#user-form-role').val('');
  $('#user-form-primary-phone').val('');
  $('#user-form-secondary-phone').val('');
  $('#user-form-credential').val('');
  $('#user-form-pager').val('');
  $('#user-form-email').val('');
  $('#user-form-email-confirm').val('');
  $('#user-form-password').val('');
  $('#user-form-password-confirm').val('');
  userForm_clearErrors();
}

function userForm_clearErrors() {
  $('#user-form-first-name-validation').css({visibility: "hidden"});
  $('#user-form-last-name-validation').css({visibility: "hidden"});
  $('#user-form-username-validation').css({visibility: "hidden"});
  $('#user-form-role-validation').css({visibility: "hidden"});
  $('#user-form-credential-validation').css({visibility: "hidden"});
  $('#user-form-primary-phone-validation').css({visibility: "hidden"});
  $('#user-form-secondary-phone-validation').css({visibility: "hidden"});
  $('#user-form-pager-validation').css({visibility: "hidden"});
  $('#user-form-email-validation').css({visibility: "hidden"});
  $('#user-form-password-validation').css({visibility: "hidden"});
}