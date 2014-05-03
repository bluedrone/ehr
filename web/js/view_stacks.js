/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

function app_viewStack(screen, doScroll) {
  app_previousScreen = app_currentScreen;
  switch (screen) {
    case 'signin-screen':
      $('#app-page-name').html('');
      $('#signin-screen').css({display: "block"});
      $('#dashboard-screen').css({display: "none"});
      $('#patient-chart-screen').css({display: "none"});
      $('#schedule-screen').css({display: "none"});
      $('#messages-screen').css({display: "none"});
      $('#letters-screen').css({display: "none"});
      $('#patient-intake-screen').css({display: "none"});
      $('#patient-continuation-screen').css({display: "none"});
      $('#user-admin-screen').css({display: "none"});
      $('#app-dropdown-logout').css({display: "none"});
      $('#app-dropdown-settings').css({display: "none"});
      $('#app-dropdown-signin').css({display: "block"});
      $('#main-navigation').css({display: "none"});
    break;
    case 'dashboard-screen':
      $('#app-page-name').html('Dashboard');
      $('#signin-screen').css({display: "none"});
      $('#dashboard-screen').css({display: "block"});
      $('#patient-chart-screen').css({display: "none"});
      $('#schedule-screen').css({display: "none"});
      $('#messages-screen').css({display: "none"});
      $('#letters-screen').css({display: "none"});
      $('#patient-intake-screen').css({display: "none"});
      $('#patient-continuation-screen').css({display: "none"});
      $('#user-admin-screen').css({display: "none"});
      $('#app-dropdown-settings').css({display: "block"});
      $('#app-dropdown-logout').css({display: "block"});
      $('#app-dropdown-signin').css({display: "none"});
      $('#main-navigation').css({display: "block"});
      getClinicianDashboard();
    break;
    case 'patient-chart-screen':
      $('#app-page-name').html('Patient Chart');
      $('#signin-screen').css({display: "none"});
      $('#dashboard-screen').css({display: "none"});
      $('#patient-chart-screen').css({display: "block"});
      $('#schedule-screen').css({display: "none"});
      $('#messages-screen').css({display: "none"});
      $('#letters-screen').css({display: "none"});
      $('#patient-intake-screen').css({display: "none"});
      $('#patient-continuation-screen').css({display: "none"});
      $('#user-admin-screen').css({display: "none"});
      $('#app-dropdown-settings').css({display: "block"});
      $('#app-dropdown-logout').css({display: "block"});
      $('#app-dropdown-signin').css({display: "none"});
      $('#main-navigation').css({display: "block"});
    break;
    case 'schedule-screen':
      $('#app-page-name').html('Schedule');
      $('#signin-screen').css({display: "none"});
      $('#dashboard-screen').css({display: "none"});
      $('#patient-chart-screen').css({display: "none"});
      $('#schedule-screen').css({display: "block"});
      $('#messages-screen').css({display: "none"});
      $('#letters-screen').css({display: "none"});
      $('#patient-intake-screen').css({display: "none"});
      $('#patient-continuation-screen').css({display: "none"});
      $('#user-admin-screen').css({display: "none"});
      $('#app-dropdown-settings').css({display: "block"});
      $('#app-dropdown-logout').css({display: "block"});
      $('#app-dropdown-signin').css({display: "none"});
      $('#main-navigation').css({display: "block"});
    break;        
    case 'messages-screen':
      $('#app-page-name').html('Messages');
      $('#signin-screen').css({display: "none"});
      $('#dashboard-screen').css({display: "none"});
      $('#patient-chart-screen').css({display: "none"});
      $('#schedule-screen').css({display: "none"});
      $('#messages-screen').css({display: "block"});
      $('#letters-screen').css({display: "none"});
      $('#patient-intake-screen').css({display: "none"});
      $('#patient-continuation-screen').css({display: "none"});
      $('#user-admin-screen').css({display: "none"});
      $('#app-dropdown-settings').css({display: "block"});
      $('#app-dropdown-logout').css({display: "block"});
      $('#app-dropdown-signin').css({display: "none"});
      $('#main-navigation').css({display: "block"});
      $('#messages-view').css({display: "none"});
      $('#messages-inbox').css({display: "block"});
    break;
    case 'letters-screen':
      $('#app-page-name').html('Letters');
      $('#signin-screen').css({display: "none"});
      $('#dashboard-screen').css({display: "none"});
      $('#patient-chart-screen').css({display: "none"});
      $('#schedule-screen').css({display: "none"});
      $('#messages-screen').css({display: "none"});
      $('#letters-screen').css({display: "block"});
      $('#patient-intake-screen').css({display: "none"});
      $('#patient-continuation-screen').css({display: "none"});
      $('#user-admin-screen').css({display: "none"});
      $('#app-dropdown-settings').css({display: "block"});
      $('#app-dropdown-logout').css({display: "block"});
      $('#app-dropdown-signin').css({display: "none"});
      $('#main-navigation').css({display: "block"});
    break;
    case 'patient-intake-screen':
      $('#app-page-name').html('Patient Intake');
      $('#signin-screen').css({display: "none"});
      $('#dashboard-screen').css({display: "none"});
      $('#patient-chart-screen').css({display: "none"});
      $('#schedule-screen').css({display: "none"});
      $('#messages-screen').css({display: "none"});
      $('#letters-screen').css({display: "none"});
      $('#patient-intake-screen').css({display: "block"});
      $('#patient-continuation-screen').css({display: "none"});
      $('#user-admin-screen').css({display: "none"});
      $('#app-dropdown-settings').css({display: "block"});
      $('#app-dropdown-logout').css({display: "block"});
      $('#app-dropdown-signin').css({display: "none"});
      $('#main-navigation').css({display: "block"});
    break;
    case 'patient-continuation-screen':
      $('#app-page-name').html('Patient Intake');
      $('#signin-screen').css({display: "none"});
      $('#dashboard-screen').css({display: "none"});
      $('#patient-chart-screen').css({display: "none"});
      $('#schedule-screen').css({display: "none"});
      $('#messages-screen').css({display: "none"});
      $('#letters-screen').css({display: "none"});
      $('#patient-intake-screen').css({display: "none"});
      $('#patient-continuation-screen').css({display: "block"});
      $('#user-admin-screen').css({display: "none"});
      $('#app-dropdown-settings').css({display: "block"});
      $('#app-dropdown-logout').css({display: "block"});
      $('#app-dropdown-signin').css({display: "none"});
      $('#main-navigation').css({display: "block"});
    break;
    case 'user-admin-screen':
      $('#app-page-name').html('');
      $('#signin-screen').css({display: "none"});
      $('#dashboard-screen').css({display: "none"});
      $('#patient-chart-screen').css({display: "none"});
      $('#schedule-screen').css({display: "none"});
      $('#messages-screen').css({display: "none"});
      $('#letters-screen').css({display: "none"});
      $('#patient-intake-screen').css({display: "none"});
      $('#patient-continuation-screen').css({display: "none"});
      $('#user-admin-screen').css({display: "block"});
      $('#app-dropdown-settings').css({display: "block"});
      $('#app-dropdown-logout').css({display: "block"});
      $('#app-dropdown-signin').css({display: "none"});
      $('#main-navigation').css({display: "block"});
    break;
  }
  if (doScroll) {scroll(0,0);}
  app_currentScreen = screen;
}
