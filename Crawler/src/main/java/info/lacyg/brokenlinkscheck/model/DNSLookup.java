package info.lacyg.brokenlinkscheck.model;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class DNSLookup {

    private static final String RECORD_SOA = "SOA";

    private static final String RECORD_A = "A";

    private static final String RECORD_NS = "NS";

    private static final String RECORD_MX = "MX";

    private static final String RECORD_CNAME = "CNAME";
    
    private static Hashtable env = new Hashtable();
    
    public DNSLookup() {
    	env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
    }
    
    public Boolean hasRecordsSet(String hostName) {
    	
    	hostName = hostName.replace("www.", "");
    	
    	if (lookup(hostName, RECORD_SOA)) return true;
    	if (lookup(hostName, RECORD_A)) return true;
    	if (lookup(hostName, RECORD_NS)) return true;
    	if (lookup(hostName, RECORD_MX)) return true;
    	if (lookup(hostName, RECORD_CNAME)) return true;
    	return false;
    }

    public static Boolean lookup(String hostName, String record) {
        try {
            DirContext ictx = new InitialDirContext(env);
            Attributes attrs = ictx.getAttributes(hostName, new String[] { record });
            Attribute attr = attrs.get(record);

            NamingEnumeration attrEnum = attr.getAll();
            if (attrEnum.hasMoreElements()) {
            	ictx.close();
                return true;
            }
            
            ictx.close();
        } catch (NamingException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
}

