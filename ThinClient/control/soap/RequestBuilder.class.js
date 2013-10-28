/*
 * A helper class for building SOAP requests
 */

function RequestBuilder()
{	
	this.XML_HEADER = '<?xml version="1.0" encoding="utf-8"?>';
	
	this.ENVELOPE_OPEN_TAG = '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="{namespace}">';
	
	this.ENVELOPE_CLOSE_TAG = '</soapenv:Envelope>';
	
	this.HEADER_TAG = '<soapenv:Header/>';
	
	this.BODY_OPEN_TAG = '<soapenv:Body>';
	
	this.BODY_CLOSE_TAG = '</soapenv:Body>';
	
	this.METHOD_OPEN_TAG = '<ws:{method}>';
	
	this.METHOD_CLOSE_TAG = '</ws:{method}>';
	
	this.ARGUMENT_TAG = '<arg{count}>{value}</arg{count}>';

	this.buildRequest = buildRequest;
	function buildRequest(config)
	{
		var request;

		if (jQuery.isEmptyObject(config.namespace) || jQuery.isEmptyObject(config.method))
		{
			console.log("ERROR: namespace and web method must be provided!");
		}
		else
		{

			request = this.XML_HEADER;
			request += this.ENVELOPE_OPEN_TAG.replace("{namespace}", config.namespace);
			request += this.HEADER_TAG;
			request += this.BODY_OPEN_TAG;

			request += this.METHOD_OPEN_TAG.replace('{method}', config.method);

			// add arguments if there are any
			if (config.args !== undefined)
			{
				if (jQuery.isArray(config.args))
				{
					for (var i = 0; i < config.args.length; i++)
					{
						var argument_tag = this.ARGUMENT_TAG.replace('{count}', i);
						argument_tag = argument_tag.replace('{count}', i);

						request += argument_tag.replace('{value}', config.args[i]);
					}
				}
				else
				{
					var argument_tag = this.ARGUMENT_TAG.replace('{count}', 0);
					argument_tag = argument_tag.replace('{count}', 0);
					
					request += argument_tag.replace('{value}', config.args);
				}
			}

			request += this.METHOD_CLOSE_TAG.replace('{method}', config.method);

			request += this.BODY_CLOSE_TAG;
			request += this.ENVELOPE_CLOSE_TAG;
		}

		return request;
	}
}