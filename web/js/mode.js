modulejs.define('app/mode',  ['jquery'], function ($) {
	return { 
		isStandalone: function() {
			return $('body').data('mode') == 'standalone'
		} 
	}
});