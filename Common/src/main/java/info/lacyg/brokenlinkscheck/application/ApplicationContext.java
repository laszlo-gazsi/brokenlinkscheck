package info.lacyg.brokenlinkscheck.application;

import info.lacyg.brokenlinkscheck.database.IDatabaseReadService;
import info.lacyg.brokenlinkscheck.database.IDatabaseWriteService;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext
{
    private final static Map<Class, IDatabaseReadService> databaseReadServices;

    private static IDatabaseWriteService databaseWriteService;

    public static String TASK_MUTEX = "get me!";

    static
    {
        databaseReadServices = new HashMap<Class, IDatabaseReadService>();
    }

    public static IDatabaseReadService getDatabaseReadService(Class clazz)
    {
        return databaseReadServices.get(clazz);
    }

    public static void setDatabaseReadService(Class clazz, IDatabaseReadService databaseReadService)
    {
        databaseReadServices.put(clazz, databaseReadService);
    }

    public static IDatabaseWriteService getDatabaseWriteService()
    {
        return databaseWriteService;
    }

    public static void setDatabaseWriteService(IDatabaseWriteService writeService)
    {
        if (databaseWriteService == null)
        {
            databaseWriteService = writeService;
        }
    }
}
