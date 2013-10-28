package info.lacyg.brokenlinkscheck.ws;

import info.lacyg.brokenlinkscheck.application.ApplicationContext;
import info.lacyg.brokenlinkscheck.database.IDatabaseReadService;
import info.lacyg.brokenlinkscheck.database.IDatabaseWriteService;
import info.lacyg.brokenlinkscheck.model.Link;
import info.lacyg.brokenlinkscheck.model.LinkRelation;
import info.lacyg.brokenlinkscheck.model.Task;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService
public class TaskTCService
{

    public static final String YES = "yes";

    public static final String NO = "no";

    public static final String FINISHED_CLASS = "finished";

    public static final String IN_PROGRESS_CLASS = "in_progress";

    public static final String DATA_SEPARATOR = "|";

    public static final String ROW_SEPARATOR = "*";

    @WebMethod
    public void addNewTask(String domain, boolean fastMode) throws Exception
    {
        IDatabaseReadService<Task> readService = ApplicationContext.getDatabaseReadService(Task.class);
        IDatabaseWriteService writeService = ApplicationContext.getDatabaseWriteService();

        int rowCount = readService.getRowCount(Restrictions.eq(Task.P_NAME, domain));

        if (rowCount != 0)
        {
            throw new Exception("A task is already created for this website!");
        }

        // create new Task
        Task newTask = new Task();
        newTask.setName(domain);
        newTask.setFastMode(fastMode);
        writeService.create(newTask);

        // insert first link for the task
        Link link = new Link();
        link.setUrl(domain);
        link.setTaskID(newTask.getID());
        writeService.create(link);
    }

    @WebMethod
    public void deleteTask(int taskId)
    {
        IDatabaseReadService<Task> taskReadService = ApplicationContext.getDatabaseReadService(Task.class);
        IDatabaseReadService<LinkRelation> linkRelationReadService = ApplicationContext.getDatabaseReadService(LinkRelation.class);
        IDatabaseReadService<Link> linkReadService = ApplicationContext.getDatabaseReadService(Link.class);
        IDatabaseWriteService writeService = ApplicationContext.getDatabaseWriteService();

        Integer activeTaskId = getActiveTaskId();

        if (taskId == activeTaskId)
        {
            synchronized (ApplicationContext.TASK_MUTEX)
            {
                deleteTaskAndLinks(taskId);
            }
        }
        else
        {
            deleteTaskAndLinks(taskId);
        }
    }

    @WebMethod
    public String getAllTasks(int startPage, int pageSize)
    {
        IDatabaseReadService<Task> readService = ApplicationContext.getDatabaseReadService(Task.class);

        int count = readService.getRowCount();
        List<Task> tasks = readService.getAll(startPage * pageSize, pageSize);

        return generateResponse(startPage, pageSize, count, tasks);
    }

    @WebMethod
    public String getFinishedTasks(int startPage, int pageSize)
    {
        IDatabaseReadService<Task> readService = ApplicationContext.getDatabaseReadService(Task.class);

        SimpleExpression finishedCriterion = Restrictions.eq(Task.P_FINISHED, true);
        int count = readService.getRowCount(finishedCriterion);
        List<Task> tasks = readService.getAll(startPage * pageSize, pageSize, finishedCriterion);

        return generateResponse(startPage, pageSize, count, tasks);
    }

    @WebMethod
    public String getInProgressTasks(int startPage, int pageSize)
    {
        IDatabaseReadService<Task> readService = ApplicationContext.getDatabaseReadService(Task.class);

        SimpleExpression unfinishedCriterion = Restrictions.eq(Task.P_FINISHED, false);
        int count = readService.getRowCount(unfinishedCriterion);
        List<Task> tasks = readService.getAll(startPage * pageSize, pageSize, unfinishedCriterion);

        return generateResponse(startPage, pageSize, count, tasks);
    }

    private String generateResponse(int start, int limit, int count, List<Task> tasks)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getPaginationInfo(start, limit, count));

        for (Task task : tasks)
        {
            stringBuilder.append(generateTaskRow(task));
        }

        return stringBuilder.toString();
    }

    private String getPaginationInfo(int start, int limit, int count)
    {
        String result = "";

        result += (start > 0) ? YES : NO;
        result += DATA_SEPARATOR;
        result += (count > (start + 1) * limit) ? YES : NO;
        result += ROW_SEPARATOR;

        return result;
    }

    private String generateTaskRow(Task task)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(task.getID());
        stringBuilder.append(DATA_SEPARATOR);
        stringBuilder.append(task.getName());
        stringBuilder.append(DATA_SEPARATOR);
        stringBuilder.append((task.isFinished() ? FINISHED_CLASS : IN_PROGRESS_CLASS));
        stringBuilder.append(DATA_SEPARATOR);
        stringBuilder.append(task.isFastMode());
        stringBuilder.append(ROW_SEPARATOR);

        return stringBuilder.toString();
    }

    private Integer getActiveTaskId()
    {
        final IDatabaseReadService<Task> taskReadService = ApplicationContext.getDatabaseReadService(Task.class);
        List<Task> all = taskReadService.getAll(null, 1, Restrictions.eq(Task.P_FINISHED, false));

        if (all.size() > 0)
        {
            return all.get(0).getID();
        }

        return null;
    }

    private void deleteTaskAndLinks(int taskId)
    {
        IDatabaseReadService<Task> taskReadService = ApplicationContext.getDatabaseReadService(Task.class);
        IDatabaseReadService<LinkRelation> linkRelationReadService = ApplicationContext.getDatabaseReadService(LinkRelation.class);
        IDatabaseReadService<Link> linkReadService = ApplicationContext.getDatabaseReadService(Link.class);
        IDatabaseWriteService writeService = ApplicationContext.getDatabaseWriteService();

        Task task = taskReadService.getById(taskId);
        if (!task.isFastMode())
        {
            List<Link> links = linkReadService.getAll(null, null, Restrictions.eq(Link.P_TASK_ID, taskId));

            List<Integer> linkIDs = new ArrayList();
            for (Link link : links)
            {
                linkIDs.add(link.getID());
            }

            List<LinkRelation> linkRelations = linkRelationReadService.getAll(null, null, Restrictions.in(LinkRelation.P_CHILD_ID, linkIDs), Restrictions.in(LinkRelation.P_PARENT_ID, linkIDs));
            writeService.delete(linkRelations.toArray(new LinkRelation[linkRelations.size()]));
        }

        writeService.executeSqlQuery("DELETE FROM Links WHERE " + Link.P_TASK_ID + " = " + taskId);
        writeService.delete(task);
    }
}
