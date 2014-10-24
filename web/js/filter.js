modulejs.define('app/standalone/filter', ['jquery', 'underscore'], function($, _) {
 filterForUrl = function(url) {
	 path = url.split('/')[0]
	 switch(path) {
	 	case "app":
	 		return ["clinicianId", "id"]
	 	case "patient":
	 		return ["patientId", "encounterId"]
	 	default: 
	 	 return []    
	 }
 }
return {
	 process: function(url, json_string, request_data) {
		 var filterBy = filterForUrl(url)
		 var filters = request_data
		 var needsToBeFiltered = !(filters == null || _.intersection(_.keys(filters), filterBy).length == 0) 
		 var json = JSON.parse(json_string)
		 var result = json 
		 if (needsToBeFiltered) {
			 filtered = $.grep(json, function(elem, ndx){
				 matches = []
				 for(var ndx in filterBy) {
					 filter = filterBy[ndx]
					 if (filters.hasOwnProperty(filter)) {
						  matches.push(elem[filter] == filters[filter])
					 }
				 }
				 matches = _.uniq(matches)
				 return matches.length == 1 && _.first(matches) == true
			 })		
			if (filtered[0]) {
				result = filtered[0]
			} 	 
		 }
		 return result		 
	 }
 }	
});