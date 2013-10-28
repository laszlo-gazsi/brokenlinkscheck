package info.lacyg.brokenlinkscheck.model.link;

import info.lacyg.brokenlinkscheck.application.ApplicationContext;

public class LinksHibernateServiceTest
{

    ApplicationContext applicationContext;

    //@Before
    public void setUp() throws Exception
    {
        applicationContext = new ApplicationContext();
        //applicationContext.setDatabaseReadService(new GenericHibernateReadService());
        //applicationContext.setDatabaseWriteService(new GenericHibernateWriteService());
    }

    /*@Test
    public void testWriteLink()
    {
        IDatabaseWriteService databaseService = applicationContext.getDatabaseWriteService();
        IDatabaseReadService databaseReadService = applicationContext.getDatabaseReadService();

        Link link = new Link();
        link.setUrl("http://example.com/secondPage/");

        Link parentLink = new Link("http://example.com");
        databaseService.create(parentLink);

        Link childLink = new Link("http://ex2.com");
        databaseService.create(childLink);

        link.getParentLinks().add(parentLink);
        link.getChildLinks().add(childLink);

        databaseService.create(link);

        // re-read the link and check if everything was properly saved
        Link newLink = databaseReadService.getById(Link.class, link.getId());
        Assert.assertEquals(link.getUrl(), newLink.getUrl());
        Assert.assertEquals(link.getChildLinks().size(), newLink.getChildLinks().size());
        Assert.assertEquals(link.getParentLinks().size(), newLink.getParentLinks().size());
    }*/
}
