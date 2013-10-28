package info.lacyg.brokenlinkscheck.core;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class CoreUtilities
{
    public static void openMainPage(String address)
    {
        try
        {
            if (Desktop.isDesktopSupported())
            {
                final URI uri = new URI(address);
                Desktop.getDesktop().browse(uri);
            }
        }
        catch (URISyntaxException ex)
        {
        }
        catch (IOException ex)
        {
        }
    }
}
