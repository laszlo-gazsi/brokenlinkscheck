package info.lacyg.brokenlinkscheck.database;

import info.lacyg.brokenlinkscheck.model.AbstractPersistentObject;
import org.hibernate.criterion.Criterion;

import java.util.List;

public interface IDatabaseReadService<T extends AbstractPersistentObject>
{
    T getById(int id);

    List<T> getAll(Integer start, Integer limit, Criterion... criterion);

    int getRowCount(Criterion... criterion);
}
