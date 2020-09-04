var apiclient = (function(){

    return {
        capturarDatos: function(info, callback) {
            var promise = $.post({
        		url: "/calculadora",
        		data: JSON.stringify(info),
        		contentType: "application/json"
            });
        	promise.then(function(data){
        		callback(null, JSON.parse(data));
        	}, function(error) {
        		callback(error, null);
        	});
        }
    }
})();