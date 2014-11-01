modulejs.define('app/standalone/responder', ['loader'], function (Loader) {
 return {
	 respond: function(url) {
		return Loader.load(url)
	 }
 }	
});