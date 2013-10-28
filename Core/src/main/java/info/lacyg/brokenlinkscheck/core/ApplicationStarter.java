package info.lacyg.brokenlinkscheck.core;

import info.lacyg.brokenlinkscheck.application.ApplicationContext;
import info.lacyg.brokenlinkscheck.crawler.Engine;
import info.lacyg.brokenlinkscheck.model.Link;
import info.lacyg.brokenlinkscheck.model.LinkRelation;
import info.lacyg.brokenlinkscheck.model.Task;
import info.lacyg.brokenlinkscheck.service.GenericHibernateReadService;
import info.lacyg.brokenlinkscheck.service.GenericHibernateWriteService;
import info.lacyg.brokenlinkscheck.tools.jetty.JettyStarter;

public class ApplicationStarter
{
    public static void main(String[] args)
    {
        int port = 5224;

        ApplicationContext.setDatabaseReadService(Task.class, new GenericHibernateReadService(Task.class));
        ApplicationContext.setDatabaseReadService(Link.class, new GenericHibernateReadService(Link.class));
        ApplicationContext.setDatabaseReadService(LinkRelation.class, new GenericHibernateReadService(LinkRelation.class));

        ApplicationContext.setDatabaseWriteService(new GenericHibernateWriteService());

        // web services
        JettyStarter jettyStarter = new JettyStarter();
        jettyStarter.start(port);

        // start the crawl engine
        new Engine().start();

        // open browser, for fake 'desktop app' experience
        CoreUtilities.openMainPage("http://localhost:" + port);
    }
}
