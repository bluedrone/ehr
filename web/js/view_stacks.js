/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

var hide_element_list_cache = $(['#signin-screen','#dashboard-screen','#patient-chart-screen','#schedule-screen',
  '#messages-screen','#letters-screen','#reports-screen','#reports-list','#reports-view','#patient-encounters-screen',
  '#user-admin-screen','#app-dropdown-logout','#app-dropdown-settings','#app-dropdown-signin',
  '#main-navigation','#messages-view','#messages-inbox'].join(','));
  
var signin_cache = $(['#signin-screen','#app-dropdown-signin'].join(','));
var dashboard_cache = $(['#dashboard-screen','#app-dropdown-settings','#app-dropdown-logout','#main-navigation'].join(','));
var patient_chart_cache = $(['#patient-chart-screen','#app-dropdown-settings','#app-dropdown-logout','#main-navigation'].join(','));
var schedule_cache = $(['#schedule-screen','#app-dropdown-settings','#app-dropdown-logout','#main-navigation'].join(','));
var messages_cache = $(['#messages-screen','#app-dropdown-settings','#app-dropdown-logout','#main-navigation','#messages-inbox'].join(','));
var letters_cache = $(['#letters-screen','#app-dropdown-settings','#app-dropdown-logout','#main-navigation'].join(','));
var reports_cache = $(['#reports-screen','#reports-list','#app-dropdown-settings','#app-dropdown-logout','#main-navigation'].join(','));
var patient_encounters_cache = $(['#patient-encounters-screen','#app-dropdown-settings','#app-dropdown-logout','#main-navigation'].join(','));
var user_admin_cache = $(['#user-admin-screen','#app-dropdown-settings','#app-dropdown-logout','#main-navigation'].join(','));

function showScreen(screenName, screen) {
  $('#app-screen-name').html(screenName);
  hide_element_list_cache.css({display: "none"});
  screen.css({display: "block"});
}

function app_viewStack(screen, doScroll) {
  app_previousScreen = app_currentScreen;
  switch (screen) {
    case 'signin-screen':
      showScreen('', signin_cache);
    break;
    case 'dashboard-screen':
      showScreen('Dashboard', dashboard_cache);
      getClinicianDashboard();
    break;
    case 'patient-chart-screen':
      showScreen('Patient Chart', patient_chart_cache);
    break;
    case 'schedule-screen':
      showScreen('Schedule', schedule_cache);
    break;        
    case 'messages-screen':
      showScreen('Messages', messages_cache);
    break;
    case 'letters-screen':
      showScreen('Letters', letters_cache);
    break;
    case 'reports-screen':
      showScreen('Reports', reports_cache);
    break;
    case 'patient-encounters-screen':
      showScreen('Patient Encounter', patient_encounters_cache);
    break;
    case 'user-admin-screen':
      showScreen('', user_admin_cache);
    break;
  }
  if (doScroll) {scroll(0,0);}
  app_currentScreen = screen;
}
