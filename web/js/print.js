function print_openPrintWindow(url, s, pageTitle) {
  url = encodeURI(url);
  var docWindow = 
  print_openNewWindow(url, '', 'directories=no,location=no,menubar=yes,scrollbars=yes,toolbar=no,status=no,resizable=yes', 'print');
  docWindow.onload = function() {
    $(docWindow.document).attr('title', pageTitle);
    $(docWindow.document).find("#container").html(s);
  }
}


function print_openNewWindow(url, name, features, windowSize) {
  if (features == "") {
    features = "toolbar=yes,directories=yes,location=1,status=yes,menubar=yes,scrollbars=yes,resizable=yes";
  }

  var height=250;
  var width=350;
  var screenHeight = 480;
  var screenWidth = 640;

  if(windowSize == 'print') {
    height = 700;
    width = 900;
  }

  if (window.screen != null) {
    screenHeight = window.screen.height;
    screenWidth = window.screen.width;
  }

  if (screenWidth > 640) {
    width = width + (screenWidth - 640)*.50;
  }

  if(screenHeight > 480) {
    height = height + (screenHeight - 480)*.50;
  }

  features += ",width=" + width + ",height=" + height;

  var docWindow = window.open (url, name, features);
  docWindow.focus();
  return docWindow;
}
