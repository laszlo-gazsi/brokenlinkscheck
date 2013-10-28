package info.lacyg.brokenlinkscheck.service;

import info.lacyg.brokenlinkscheck.database.IDatabaseWriteService;
import info.lacyg.brokenlinkscheck.model.AbstractPersistentObject;
import org.hibernate.Session;
import org.hibernate.exception.GenericJDBCException;

public class GenericHibernateWriteService<T extends AbstractPersistentObject> extends HibernateService implements IDatabaseWriteService<T>
{
    public void create(T... objects)
    {
        synchronized (MUTEX)
        {
            Object obj = new Object();
            try
            {
                Session session = getSessionFactory().getCurrentSession();
                session.beginTransaction();
                for (Object object : objects)
                {
                    obj = object;
                    session.saveOrUpdate(object);
                }
                session.getTransaction().commit();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public void update(T... objects)
    {
        synchronized (MUTEX)
        {
            Session session = getSessionFactory().getCurrentSession();
            session.beginTransaction();
            for (Object object : objects)
            {
                session.saveOrUpdate(object);
            }
            session.getTransaction().commit();
        }
    }

    public void delete(T... objects)
    {
        synchronized (MUTEX)
        {
            Session session = getSessionFactory().getCurrentSession();
            session.beginTransaction();
            for (Object object : objects)
            {
                session.delete(object);
            }
            session.getTransaction().commit();
        }
    }

    public void executeSqlQuery(String sql)
    {
        synchronized (MUTEX)
        {
            Session session = getSessionFactory().getCurrentSession();

            session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
        }
    }

}
