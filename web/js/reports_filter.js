modulejs.define('app/standalone/reports/filter', ['jquery', 'underscore'], function($, _) {
	var filterBy = ["dateFrom", "dateTo", "clinicianName", "patientName", "activityName"]
	var elementVal = function(elem, filter){
		switch(filter) {
		case "dateFrom":
		case "dateTo":
			return elem["timePerformed"]
		case "clinicianName":
		case "patientName":
			return elem[filter]
		case "activityName":
			return elem["activity"]
		
		}
	}
	return {
	 process: function(data, conditions) {
		 result = $.grep(data, function(elem, ndx){
			 matches = [true]
			 for(var ndx in filterBy) {
				 filter = filterBy[ndx]
				 if (conditions.hasOwnProperty(filter) && conditions[filter]) {
					  if (filter == "dateFrom") {
					   matches.push(moment(elementVal(elem,filter)) >= moment(conditions[filter]))	  
					  }else if (filter == "dateTo") {
						  matches.push(moment(elementVal(elem,filter)) <= moment(conditions[filter]))	    
					  } else if (filter == "clinicianName" || filter == "patientName" || (conditions["activityId"] != 0 && filter == "activityName")) {
						conditionsVal = conditions[filter].replace(/\+/g, ' ')
						 matches.push(elementVal(elem,filter) == conditionsVal)
					  }
				 }
			 }
			 matches = _.uniq(matches)
			 return matches.length == 1 && _.first(matches) == true

		 })	 
		 return result
	 }
}
});