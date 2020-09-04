var app = (function () {

	
	var numeros = function (error, info){
		if (error != null){
			alert("Verificar datos");
			return;	
		}
		//$("#avg").text(info.media);
		//$("#stdv").text(info.desviacion);
	}
	
	var enviar = function (){
		var strings = $("#captura").val();
		var listaNums= strings.split(",");
		apiclient.capturarDatos(listaNums, numeros);
	}
	

    return {
		capturarDatos : function(){
			enviar();
		}
    }
})();