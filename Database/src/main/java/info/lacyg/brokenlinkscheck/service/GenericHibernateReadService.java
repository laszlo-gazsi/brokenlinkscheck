package info.lacyg.brokenlinkscheck.service;

import info.lacyg.brokenlinkscheck.database.IDatabaseReadService;
import info.lacyg.brokenlinkscheck.model.AbstractPersistentObject;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;

import java.util.List;

public class GenericHibernateReadService<T extends AbstractPersistentObject> extends HibernateService implements IDatabaseReadService<T>
{

    private Class<T> type;

    public GenericHibernateReadService(Class<T> type)
    {
        this.type = type;
    }

    public T getById(int id)
    {
        synchronized (MUTEX)
        {
            Session session = getSessionFactory().getCurrentSession();
            session.beginTransaction();
            T result = (T) session.get(getType(), id);
            session.getTransaction().commit();
            return result;
        }
    }

    public List<T> getAll(Integer start, Integer limit, Criterion... criterion)
    {
        synchronized (MUTEX)
        {
            Session session = getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Criteria criteria = session.createCriteria(getType());

            for (Criterion criterionItem : criterion)
            {
                criteria.add(criterionItem);
            }

            if (start != null)
            {
                criteria.setFirstResult(start);
            }
            if (limit != null)
            {
                criteria.setMaxResults(limit);
            }

            List<T> result = criteria.list();
            session.getTransaction().commit();

            return result;
        }
    }

    public int getRowCount(Criterion... criterion)
    {
        synchronized (MUTEX)
        {
            Session session = getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Criteria criteria = session.createCriteria(getType());

            for (Criterion criterionItem : criterion)
            {
                criteria.add(criterionItem);
            }

            final int result = (int) criteria.setProjection(Projections.rowCount()).uniqueResult();

            session.getTransaction().commit();

            return result;
        }
    }

    protected Class<T> getType()
    {
        return this.type;
    }
}
