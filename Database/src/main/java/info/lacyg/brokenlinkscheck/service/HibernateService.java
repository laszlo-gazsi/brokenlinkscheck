package info.lacyg.brokenlinkscheck.service;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateService
{
    private SessionFactory sessionFactory;

    protected final static String MUTEX = "mutex";

    public HibernateService()
    {
        sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
    }

    protected SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }
}
