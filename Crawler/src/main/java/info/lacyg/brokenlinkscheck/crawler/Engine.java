package info.lacyg.brokenlinkscheck.crawler;

import info.lacyg.brokenlinkscheck.application.ApplicationContext;
import info.lacyg.brokenlinkscheck.application.Informer;
import info.lacyg.brokenlinkscheck.database.IDatabaseReadService;
import info.lacyg.brokenlinkscheck.database.IDatabaseWriteService;
import info.lacyg.brokenlinkscheck.model.Link;
import info.lacyg.brokenlinkscheck.model.LinkRelation;
import info.lacyg.brokenlinkscheck.model.Task;
import org.hibernate.criterion.Restrictions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Engine extends Thread
{
    public static final int PARALLEL_TASKS_NUMBER = 1;

    @Override
    public void run()
    {
        final IDatabaseReadService<Task> taskReadService = ApplicationContext.getDatabaseReadService(Task.class);

        List<Task> unfinishedTasks;

        while (true)
        {
            try
            {
                synchronized (ApplicationContext.TASK_MUTEX)
                {
                    unfinishedTasks = taskReadService.getAll(null, PARALLEL_TASKS_NUMBER, Restrictions.eq(Task.P_FINISHED, false));

                    TaskRunner[] taskRunners = new TaskRunner[unfinishedTasks.size()];
                    for (int i = 0; i < unfinishedTasks.size(); i++)
                    {
                        taskRunners[i] = new TaskRunner(unfinishedTasks.get(i));
                        taskRunners[i].run();
                    }
                }

                if (unfinishedTasks.size() == 0)
                {
                    sleep(10000);
                }
                else
                {
                    sleep(500);
                }
            }
            catch (Exception ex)
            {
            }
        }

    }
}
