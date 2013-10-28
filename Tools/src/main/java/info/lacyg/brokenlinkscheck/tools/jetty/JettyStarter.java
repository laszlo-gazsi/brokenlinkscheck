package info.lacyg.brokenlinkscheck.tools.jetty;

import info.lacyg.brokenlinkscheck.ws.CoreTCService;
import info.lacyg.brokenlinkscheck.ws.LinkTCService;
import info.lacyg.brokenlinkscheck.ws.TaskTCService;
import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.xml.ws.Endpoint;

public class JettyStarter
{
    private static final Logger LOGGER = Log.getLogger(JettyStarter.class);

    private Server server;

    /**
     * Publishes the webservices and initializes a server for serving static resources
     * @param port Port number to start the server on
     */

    public void start(int port)
    {
        this.server = new Server(port);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setResourceBase("./ThinClient/");

        JettyHttpServer jettyServer = new JettyHttpServer(this.server, true);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, new ContextHandlerCollection()});
        this.server.setHandler(handlers);

        // web services
        Endpoint.create(new CoreTCService()).publish(jettyServer.createContext("/core"));
        Endpoint.create(new TaskTCService()).publish(jettyServer.createContext("/task"));
        Endpoint.create(new LinkTCService()).publish(jettyServer.createContext("/link"));

        try
        {
            this.server.start();
            LOGGER.info("Server started...");
        }
        catch (Exception ex)
        {
            LOGGER.warn(ex);
        }
    }

    public void stop()
    {
        try
        {
            this.server.stop();
            this.server.join();
        }
        catch (Exception ex)
        {
            LOGGER.warn(ex);
        }
    }
}
