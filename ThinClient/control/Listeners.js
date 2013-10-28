function listAllTasksListener(start) {
    ApplicationContext.currentUseCase = "all_tasks";

    if (start == undefined) {
        ApplicationContext.currentPage = start = 0;
    }

    var config = [];

    config.webService = 'task';

    config.requestConfig = [];
    config.requestConfig.namespace = ApplicationContext.namespace;
    config.requestConfig.method = 'getAllTasks';
    config.requestConfig.args = [start, ApplicationContext.pageSize];

    config.callbackConfig = [];
    config.callbackConfig.method = taskListerCallback;
    config.callbackConfig.pageTitle = "All Tasks";
    config.callbackConfig.selectedFilter = "all_tasks";

    var webService = new WebService(config);
    webService.invokeMethod(config);
}

function listFinishedTasksListener(start) {
    ApplicationContext.currentUseCase = "finished_tasks";

    if (start == undefined) {
        ApplicationContext.currentPage = start = 0;
    }

    var config = [];

    config.webService = 'task';

    config.requestConfig = [];
    config.requestConfig.namespace = ApplicationContext.namespace;
    config.requestConfig.method = 'getFinishedTasks';
    config.requestConfig.args = [start, ApplicationContext.pageSize];

    config.callbackConfig = [];
    config.callbackConfig.method = taskListerCallback;
    config.callbackConfig.pageTitle = "Finished Tasks";
    config.callbackConfig.selectedFilter = "finished_tasks";

    var webService = new WebService(config);
    webService.invokeMethod(config);
}

function listInProgressTasksListener(start) {
    ApplicationContext.currentUseCase = "in_progress_tasks";

    if (start == undefined) {
        ApplicationContext.currentPage = start = 0;
    }

    var config = [];

    config.webService = 'task';

    config.requestConfig = [];
    config.requestConfig.namespace = ApplicationContext.namespace;
    config.requestConfig.method = 'getInProgressTasks';
    config.requestConfig.args = [start, ApplicationContext.pageSize];

    config.callbackConfig = [];
    config.callbackConfig.method = taskListerCallback;
    config.callbackConfig.pageTitle = "Tasks In Progress";
    config.callbackConfig.selectedFilter = "in_progress_tasks";

    var webService = new WebService(config);
    webService.invokeMethod(config);
}

function newTaskListener() {
    var config = [];

    config.webService = 'task';

    config.requestConfig = [];
    config.requestConfig.namespace = ApplicationContext.namespace;
    config.requestConfig.method = 'addNewTask';
    var domain = document.getElementById('domain_text_box').value;
    var fastMode = document.getElementById('fast_mode_checkbox').checked;
    config.requestConfig.args = [domain, fastMode];

    config.callbackConfig = [];
    config.callbackConfig.method = listAllTasksListener;

    config.callbackConfig.errorCallback = getErrorCallback();

    var webService = new WebService(config);
    webService.invokeMethod(config);
    jQuery.colorbox.close();
}

function deleteTask(id) {
    var config = [];

    config.webService = 'task';

    config.requestConfig = [];
    config.requestConfig.namespace = ApplicationContext.namespace;
    config.requestConfig.method = 'deleteTask';
    config.requestConfig.args = [id];

    config.callbackConfig = [];
    config.callbackConfig.method = listAllTasksListener;

    var webService = new WebService(config);
    webService.invokeMethod(config);
}

function loadLinksOfTask(event, id, start) {
    ApplicationContext.currentUseCase = "all_links";

    if (id != undefined) {
        ApplicationContext.currentTask = id;
    }
    else {
        id = ApplicationContext.currentTask;
    }

    if (start == undefined) {
        ApplicationContext.currentPage = start = 0;
    }

    var config = [];

    config.webService = 'link';

    config.requestConfig = [];
    config.requestConfig.namespace = ApplicationContext.namespace;
    config.requestConfig.method = 'getAllLinks';
    config.requestConfig.args = [id, start, ApplicationContext.pageSize];

    config.callbackConfig = [];
    config.callbackConfig.method = linkListerCallback;
    config.callbackConfig.pageTitle = "All Links";
    config.callbackConfig.selectedFilter = "all_links";

    var webService = new WebService(config);
    webService.invokeMethod(config);
}

function loadBrokenLinksOfTask(event, id, start) {
    ApplicationContext.currentUseCase = "broken_links";

    if (id != undefined) {
        ApplicationContext.currentTask = id;
    }
    else {
        id = ApplicationContext.currentTask;
    }

    if (start == undefined) {
        ApplicationContext.currentPage = start = 0;
    }

    var config = [];

    config.webService = 'link';

    config.requestConfig = [];
    config.requestConfig.namespace = ApplicationContext.namespace;
    config.requestConfig.method = 'getBrokenLinks';
    config.requestConfig.args = [id, start, ApplicationContext.pageSize];

    config.callbackConfig = [];
    config.callbackConfig.method = linkListerCallback;
    config.callbackConfig.pageTitle = "Broken Links";
    config.callbackConfig.selectedFilter = "broken_links";

    var webService = new WebService(config);
    webService.invokeMethod(config);
}

function getParentLinksFor(linkId)
{
    var config = [];

    config.webService = 'link';

    config.requestConfig = [];
    config.requestConfig.namespace = ApplicationContext.namespace;
    config.requestConfig.method = 'getParentLinks';
    config.requestConfig.args = [linkId];

    config.callbackConfig = [];
    config.callbackConfig.method = listParentLinks;

    var webService = new WebService(config);
    webService.invokeMethod(config);
}

function taskListerCallback(config) {
    $('#useCaseTitle').text(config.pageTitle);
    $('#linkFilter').hide();
    $('#taskFilter').show();

    $('#' + config.selectedFilter).prop('checked', 'checked').button('refresh');

    generateTasksList(config);
}

function linkListerCallback(config) {
    $('#useCaseTitle').text(config.pageTitle);
    $('#taskFilter').hide();
    $('#linkFilter').show();

    if (ApplicationContext.fastMode == true) {
        generateLinksListForFastMode(config);
    }
    else {
        generateLinksList(config);
    }
}

function stopServerListener() {
    var dialogConfig = [];
    dialogConfig.title = 'Stopping the server';
    dialogConfig.message = 'Sending the kill signal to the server... ' +
        'Ping it again though to make sure it has stopped.';

    new Dialog(dialogConfig).show();

    var config = [];

    config.webService = 'core';

    config.requestConfig = [];
    config.requestConfig.namespace = ApplicationContext.namespace;
    config.requestConfig.method = 'kill';

    var webService = new WebService(config);
    webService.invokeMethod(config);
}

function pingServerListener() {
    var number = Math.floor(Math.random() * 11);

    var config = [];

    config.webService = 'core';

    config.requestConfig = [];
    config.requestConfig.namespace = ApplicationContext.namespace;
    config.requestConfig.method = 'ping';
    config.requestConfig.args = number;

    config.callbackConfig = [];
    config.callbackConfig.method = pingCallback;
    config.callbackConfig.number = number;

    config.callbackConfig.errorCallback = [];
    config.callbackConfig.errorCallback.method = alertCallback;
    config.callbackConfig.errorCallback.title = 'Ping result';
    config.callbackConfig.errorCallback.message = 'The server is down...';

    var webService = new WebService(config);
    webService.invokeMethod(config);
}

function getErrorCallback() {
    var errorCallback = [];
    errorCallback.method = alertCallback;
    errorCallback.title = 'Error';

    return errorCallback;
}

function pingCallback(config) {
    var message;

    if (config.message == config.number + 1) {
        message = 'The server is running...';
    }
    else {
        message = 'Something is utterly wrong...';
    }

    var dialogConfig = [];
    dialogConfig.title = 'Ping result';
    dialogConfig.message = message;

    new Dialog(dialogConfig).show();
}

function alertCallback(config) {
    new Dialog(config).show();
}

function loadPreviousPage() {
    loadPage(--ApplicationContext.currentPage);
}

function loadNextPage() {
    loadPage(++ApplicationContext.currentPage);
}

function loadPage(pageNumber) {
    if (ApplicationContext.currentUseCase == 'all_tasks') {
        listAllTasksListener(ApplicationContext.currentPage);
    }
    else if (ApplicationContext.currentUseCase == 'finished_tasks') {
        listFinishedTasksListener(ApplicationContext.currentPage);
    }
    else if (ApplicationContext.currentUseCase == 'in_progress_tasks') {
        listInProgressTasksListener(ApplicationContext.currentPage);
    }
    else if (ApplicationContext.currentUseCase == 'all_links') {
        loadLinksOfTask(null, ApplicationContext.currentTask, ApplicationContext.currentPage);
    }
    else if (ApplicationContext.currentUseCase == 'broken_links') {
        loadBrokenLinksOfTask(null, ApplicationContext.currentTask, ApplicationContext.currentPage);
    }
}
