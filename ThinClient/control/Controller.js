$(document).ready(function() 
{

	addListeners();

	$("#taskFilter").buttonset();
	$("#linkFilter").buttonset();
	
	// list tasks
	listAllTasksListener();

});

function addListeners() 
{
	$('#stop_server').bind("click", stopServerListener);
	$('#ping_server').bind("click", pingServerListener);

	$('#list_all_tasks').bind("click", listAllTasksListener);
	$('#all_tasks').bind("click", listAllTasksListener);
	$('#finished_tasks').bind("click", listFinishedTasksListener);
	$('#in_progress_tasks').bind("click", listInProgressTasksListener);
    $("#new_task").colorbox({inline:true, width:"30%"});
    $('#new_task_submit').bind("click", newTaskListener);

    $('#all_links').bind("click", loadLinksOfTask);
    $('#broken_links').bind("click", loadBrokenLinksOfTask);

	$('#previous').bind("click", loadPreviousPage);
    $('#next').bind("click", loadNextPage);
}
