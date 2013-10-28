package info.lacyg.brokenlinkscheck.database;

import info.lacyg.brokenlinkscheck.model.AbstractPersistentObject;

public interface IDatabaseWriteService<T extends AbstractPersistentObject>
{
    void create(T ... objects);

    void update(T ... objects);

    void delete(T ... objects);

    void executeSqlQuery(String sql);
}
