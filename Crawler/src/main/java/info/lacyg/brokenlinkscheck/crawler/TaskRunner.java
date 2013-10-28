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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskRunner
{
    public static final int PARALLEL_LINKS_NUMBER = 1;

    private final Task task;

    public TaskRunner(Task task)
    {
        this.task = task;
    }

    private void closeTask(Task task, IDatabaseWriteService writeService)
    {
        task.setFinished(true);
        writeService.update(task);
    }

    private void updateHttpResponse(Link link, IDatabaseWriteService<Link> writeService)
    {
        int httpResponse = LinkUtils.getHTTPResponse(link);
        link.setHttpResponse(httpResponse);

        writeService.update(link);

        // check whether we've found some diamonds...
        new DomainReporter(link).start();
    }

    private Link getLink(Link parentLink, String childURL, Task task, IDatabaseReadService<Link> linkReadService)
    {
        Link resultLink;

        try
        {
            // get absolute URL for the child link
            URL parentURL = new URL(parentLink.getUrl());
            URL url = new URL(parentURL, childURL);

            // retrieve Link from the DB if it was already added
            List<Link> linkList = linkReadService.getAll(null, null,
                    Restrictions.eq(Link.P_URL, url.toExternalForm()),
                    Restrictions.eq(Link.P_TASK_ID, task.getID()));

            if (linkList.size() > 0)
            {
                resultLink = linkList.get(0);
            } else
            {
                resultLink = new Link(url.toExternalForm());
                resultLink.setTaskID(task.getID());
            }

        }
        catch (MalformedURLException e)
        {
            resultLink = new Link(childURL);
            resultLink.setTaskID(task.getID());
        }

        return resultLink;
    }

    private void parseInternalLink(Link link, Task task, IDatabaseReadService<Link> readService, IDatabaseWriteService writeService)
    {
        // get child links
        List<String> childLinks = LinkUtils.getPage(link);

        LinkRelation[] linkRelations = new LinkRelation[childLinks.size()];
        int childLinkCounter = 0;

        for (String childURL : childLinks)
        {
            Link childLink = getLink(link, childURL, task, readService);
            writeService.create(childLink);

            linkRelations[childLinkCounter] = new LinkRelation(link.getID(), childLink.getID());
            childLinkCounter++;
        }

        // increase performance by saving all LinkRelations in bulk
        writeService.create(linkRelations);
    }

    private void parseInternalLinksFastMode(Link link, Task task, IDatabaseReadService<Link> readService, IDatabaseWriteService writeService)
    {
        List<String> childLinks = LinkUtils.getPage(link);

        Set<Link> links = new HashSet<Link>(childLinks.size());

        for (String childURL : childLinks)
        {
            Link childLink = getLink(link, childURL, task, readService);
            if (!links.contains(childLink) && childLink.getID() == 0)
            {
                links.add(childLink);
            }
        }

        writeService.create(links.toArray(new Link[links.size()]));
    }

    public void run()
    {
        final IDatabaseReadService<Link> linkReadService = ApplicationContext.getDatabaseReadService(Link.class);
        final IDatabaseWriteService writeService = ApplicationContext.getDatabaseWriteService();

        List<Link> links = linkReadService.getAll(null, PARALLEL_LINKS_NUMBER,
                Restrictions.eq(Link.P_TASK_ID, task.getID()),
                Restrictions.isNull(Link.P_HTTP_RESPONSE));
        if (links.size() == 0)
        {
            closeTask(task, writeService);
        } else
        {
            for (Link link : links)
            {
                Informer.printInformation(task.getID() + " - " + link.getUrl());

                // set http response
                updateHttpResponse(link, writeService);

                if (LinkUtils.isInternalLink(task, link))
                {
                    if (task.isFastMode())
                    {
                        parseInternalLinksFastMode(link, task, linkReadService, writeService);
                    } else
                    {
                        parseInternalLink(link, task, linkReadService, writeService);
                    }
                }
            }
        }
    }
}
