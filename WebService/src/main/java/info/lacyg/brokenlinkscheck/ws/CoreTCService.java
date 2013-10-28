package info.lacyg.brokenlinkscheck.ws;

import info.lacyg.brokenlinkscheck.application.ApplicationContext;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class CoreTCService
{

    /**
     * A simple ping function
     *
     * @param number an integer value
     * @return sending an increased value
     */
    @WebMethod
    public int ping(int number)
    {
        return number + 1;
    }

    /**
     * Stops the server and the engine
     */
    @WebMethod
    public void kill()
    {
        synchronized (ApplicationContext.TASK_MUTEX)
        {
            System.exit(0);
        }
    }

}
