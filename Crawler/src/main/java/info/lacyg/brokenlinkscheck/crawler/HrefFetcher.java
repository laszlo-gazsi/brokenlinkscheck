package info.lacyg.brokenlinkscheck.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class HrefFetcher extends HTMLEditorKit.ParserCallback
{

    private List<String> urls;

    public HrefFetcher()
    {
        this.urls = new ArrayList<String>();
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos)
    {
        if (t == HTML.Tag.A)
        {
            String href = (String) a.getAttribute(HTML.Attribute.HREF);

            //empty
            if (href == null)
            {
                return;
            }

            //# position, check link without it
            if (href.indexOf('#') != -1)
            {
                href = href.substring(0, href.indexOf('#'));
            }

            //mailto
            if (href.toLowerCase().startsWith("mailto:"))
            {
                return;
            }

            // ignore javascript calls in href tags
            if (href.toLowerCase().startsWith("javascript:"))
            {
                return;
            }

            urls.add(href.toLowerCase());
        }
    }

    public List<String> getUrls()
    {
        try
        {
            this.flush();
        }
        catch (BadLocationException e)
        {
        }

        return urls;
    }

}