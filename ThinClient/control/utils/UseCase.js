function generateTasksList(config) {
    var tasks = config.message.split('*');

    setPrevNextButtons(tasks[0]);

    var list = '<ul id="tasksList">';
    for (var i = 1; i < tasks.length - 1; i++) {
        var task = tasks[i].split('|');
        var taskName = ((task[3] == 'true') ? "[F] " : "") + task[1];
        list += '<li class="task_item ' + task[2] + '">';
        list += '<a class="taskName" href="Javascript:void(0);" onClick="ApplicationContext.fastMode = ' + task[3] + '; loadLinksOfTask(null, ' + task[0] + ');">' + taskName + '</a>';
        list += '<a class="deleteButton" href="Javascript:void(0);" onClick="deleteTask(' + task[0] + ')">Delete</a>';
        list += '</li>';
    }
    list += '</ul>';

    $("#useCaseContent").html(list);
}

function generateLinksListForFastMode(config) {
    var links = config.message.split('*');

    setPrevNextButtons(links[0]);

    var list = '<ul id="tasksList">';
    for (var i = 1; i < links.length - 1; i++) {
        var link = links[i].split('|');
        var url = 'http://lopakodo.net/?' + encodeURI(link[1]);
        list += '<li class="task_item ' + link[2] + '">';
        list += '<a class="taskName" target="_blank" href="' + url + '">' + link[1] + '</a>';
        list += '<span class="httpResponse">' + link[3] + '</span>';
        list += '</li>';
    }
    list += '</ul>';

    $("#useCaseContent").html(list);
}

function generateLinksList(config) {
    var links = config.message.split('*');

    setPrevNextButtons(links[0]);

    var list = '<ul id="tasksList">';
    for (var i = 1; i < links.length - 1; i++) {
        var link = links[i].split('|');
        list += '<li class="task_item ' + link[2] + '">';
        list += '<a class="taskName" href="JavaScript:void(0)" onClick="getParentLinksFor(' + link[0] + ');">' + link[1] + '</a>';
        list += '<span class="httpResponse">' + link[3] + '</span>';
        list += '</li>';
    }
    list += '</ul>';

    $("#useCaseContent").html(list);
}

function listParentLinks(config) {
    var links = config.message.split('*');

    var list = '<ul id="tasksList" style="margin-top: 30px; width: 98%; margin-left: 10px;">';
    for (var i = 0; i < links.length - 1; i++) {
        var link = links[i].split('|');
        var url = 'http://lopakodo.net/?' + encodeURI(link[1]);
        list += '<li class="task_item ' + link[2] + '">';
        list += '<a class="taskName" target="_blank" href="' + url + '">' + link[1] + '</a>';
        list += '</li>';
    }
    list += '</ul>';

    jQuery.colorbox(
        {
            title: '<b>Parent links</b>',
            width: '70%',
            height: '80%',
            html: list
        }
    );
}

// show or hide the next and previous buttons on the UI
function setPrevNextButtons(buttonSettings) {

    var controls = buttonSettings.split("|");

    if (controls[0] == "no") {
        $("#previous").hide();
    }
    else {
        $("#previous").show();
    }

    if (controls[1] == "no") {
        $("#next").hide();
    }
    else {
        $("#next").show();
    }
}

