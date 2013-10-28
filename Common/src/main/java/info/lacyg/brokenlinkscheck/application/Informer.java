package info.lacyg.brokenlinkscheck.application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Informer
{

    public static void printInformation(String message)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        System.out.println("[" + dateFormat.format(date) + "] " + message);
    }

}
