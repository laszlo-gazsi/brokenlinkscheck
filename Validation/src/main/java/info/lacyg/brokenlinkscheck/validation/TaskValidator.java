package info.lacyg.brokenlinkscheck.validation;

import info.lacyg.brokenlinkscheck.model.Task;
import info.lacyg.brokenlinkscheck.service.GenericHibernateReadService;
import org.hibernate.criterion.Restrictions;

public class TaskValidator
{

    public boolean isUnique(String taskURL)
    {
        GenericHibernateReadService readService = new GenericHibernateReadService(Task.class);
        int rowCount = readService.getRowCount(Restrictions.eq(Task.P_NAME, taskURL));

        return rowCount == 0;
    }

}
