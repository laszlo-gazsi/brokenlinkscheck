function WebService(config)
{
	this.config = config;

	this.invokeMethod = invokeMethod;
	function invokeMethod(method, args, callback)
	{
		var soapRequestBuilder = new RequestBuilder();
		var soapRequest = soapRequestBuilder.buildRequest(config.requestConfig);
		
		ajaxRequest(soapRequest, config.callbackConfig);
	}

	this.ajaxRequest = ajaxRequest;
	function ajaxRequest(soapRequest, callbackConfig)
	{
		$.ajax(
		{
			type : 'post',
			url : '/' + config.webService + '/',
			contentType : 'text/xml',
			data : $.trim(soapRequest),
			dataType : 'xml',
			processData : false,
			
			success : function(response)
			{
				var xml = $(response);
				callbackConfig.message = xml.find('return').text();
				callbackConfig.method(callbackConfig);
			},
			error : function(response)
			{
				if (!jQuery.isEmptyObject(callbackConfig) && !jQuery.isEmptyObject(callbackConfig.errorCallback))
				{
                    if (jQuery.isEmptyObject(callbackConfig.errorCallback.message))
                    {
                        var xmlDoc = response.responseXML.documentElement;
                        callbackConfig.errorCallback.message = xmlDoc.getElementsByTagName('message')[0].textContent;
                    }

                    callbackConfig.errorCallback.method(callbackConfig.errorCallback);
                }
				else
				{
					console.log("ERROR", arguments);
				}
			}
		});
	}
}
