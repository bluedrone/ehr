modulejs.define('loader', ["jquery"], function ($) {
	var json = function(url) {
	  return "json/" + url + ".json"
	}
    var path = function(url) {
    	  if (url.match(/\.html$/)) {
    	    return url
    	  } else {
    		  return json(url)
    	  }
     }
	return {
			load: function(url) {
				_path = path(url)
				return $.ajax( 	
					_path,
					{
						dataType : "text",
						error: function(xhr, textStatus, err) {
							console.log("Error retrieving " + _path + " Status: " + err)
						}
					}
				)			
			}
	}
});