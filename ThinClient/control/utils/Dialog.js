function Dialog(config)
{
	this.config = config;

	this.show = show;
	function show()
	{
		$("#dialog_text").text(config.message);
		
		$("#dialog").dialog(
		{
			autoOpen : true,
			width : 450,
			title : config.title || null,
			buttons : [
			{
				text : config.closeText || 'Ok',
				click : function()
				{
					$(this).dialog("close");
				}
			}]
		});
	}

}