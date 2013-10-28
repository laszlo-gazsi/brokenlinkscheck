package info.lacyg.brokenlinkscheck.validation;

import java.net.URL;

public class IPValidator
{
    public static Boolean hasAssociatedIP(URL url)
    {
        boolean result = true;
        try
        {
            java.net.InetAddress.getByName(url.getHost());

        } catch (Exception expectedException)
        {
            result = false;
        }

        return result;
    }
}
