package info.lacyg.brokenlinkscheck.crawler;

import info.lacyg.brokenlinkscheck.model.Link;
import info.lacyg.brokenlinkscheck.model.Task;

import javax.swing.text.html.parser.ParserDelegator;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class LinkUtils
{
    public static int getHTTPResponse(Link link)
    {
        try
        {
            URL url = new URL(link.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            int response = connection.getResponseCode();
            connection.disconnect();

            return response;
        }
        catch (Exception ex)
        {
            return 0;
        }
    }

    public static List<String> getPage(Link link)
    {
        try
        {
            URL url = new URL(link.getUrl());

            Reader reader = new InputStreamReader((InputStream) url.getContent());
            HrefFetcher hrefFetcher = new HrefFetcher();
            new ParserDelegator().parse(reader, hrefFetcher, true);

            List<String> childURLs = hrefFetcher.getUrls();

            return childURLs;
        }
        catch (Exception ex)
        {
            return Collections.emptyList();
        }
    }

    public static Boolean isInternalLink(Task task, Link link)
    {
        try
        {
            URL parentURL = new URL(task.getName());
            URL childURL = new URL(link.getUrl());

            return parentURL.getHost().equalsIgnoreCase(childURL.getHost());
        }
        catch(MalformedURLException ex)
        {
            return false;
        }
    }
}
