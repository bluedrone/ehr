/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
function showScreen(page, screen) {
  $('#app-page-name').html(page);
  $(['#signin-screen','#dashboard-screen','#patient-chart-screen','#schedule-screen',
  '#messages-screen','#letters-screen','#reports-screen','#reports-list','#reports-view','#patient-encounters-screen',
  '#user-admin-screen','#app-dropdown-logout','#app-dropdown-settings','#app-dropdown-signin',
  '#main-navigation','#messages-view','#messages-inbox'].join(',')).css({display: "none"});
  $(screen.join(',')).css({display: "block"});
}

function app_viewStack(screen, doScroll) {
  app_previousScreen = app_currentScreen;
  switch (screen) {
    case 'signin-screen':
      showScreen('', ['#signin-screen','#app-dropdown-signin']);
    break;
    case 'dashboard-screen':
      showScreen('Dashboard', ['#dashboard-screen','#app-dropdown-settings','#app-dropdown-logout','#main-navigation']);
      getClinicianDashboard();
    break;
    case 'patient-chart-screen':
      showScreen('Patient Chart', ['#patient-chart-screen','#app-dropdown-settings','#app-dropdown-logout','#main-navigation']);
    break;
    case 'schedule-screen':
      showScreen('Schedule', ['#schedule-screen','#app-dropdown-settings','#app-dropdown-logout','#main-navigation']);
    break;        
    case 'messages-screen':
      showScreen('Messages', ['#messages-screen','#app-dropdown-settings','#app-dropdown-logout','#main-navigation','#messages-inbox']);
    break;
    case 'letters-screen':
      showScreen('Letters', ['#letters-screen','#app-dropdown-settings','#app-dropdown-logout','#main-navigation']);
    break;
    case 'reports-screen':
      showScreen('Reports', ['#reports-screen','#reports-list','#app-dropdown-settings','#app-dropdown-logout','#main-navigation']);
    break;
    case 'patient-encounters-screen':
      showScreen('Patient Encounter', ['#patient-encounters-screen','#app-dropdown-settings','#app-dropdown-logout','#main-navigation']);
    break;
    case 'user-admin-screen':
      showScreen('', ['#user-admin-screen','#app-dropdown-settings','#app-dropdown-logout','#main-navigation']);
    break;
  }
  if (doScroll) {scroll(0,0);}
  app_currentScreen = screen;
}
