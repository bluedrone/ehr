modulejs.define('app/mode',  ['jquery'], function ($) {
	return { 
		isStandalone: function() {
			protocol = document.URL.split(':')[0]
			return (protocol == "file")
		} 
	}
});