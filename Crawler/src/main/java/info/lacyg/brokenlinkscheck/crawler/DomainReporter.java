package info.lacyg.brokenlinkscheck.crawler;

import info.lacyg.brokenlinkscheck.application.Informer;
import info.lacyg.brokenlinkscheck.model.DNSLookup;
import info.lacyg.brokenlinkscheck.model.Link;
import info.lacyg.brokenlinkscheck.validation.URLValidator;

import java.net.URL;

public class DomainReporter extends Thread {

	private String webService = "http://domain-yard.com/ws/wsdomains.php?op=AddDomain&domain=";
	private Link link;
	
	public DomainReporter(Link link) {
		this.link = link;
	}
	
	public void run() {
		URLValidator validator = new URLValidator();
		
		if (validator.hasValidForm(link) && !validator.hasIP(link)) {
			try {
				URL url = new URL(link.getUrl());
				String domainName = url.getHost().toLowerCase();
				domainName = domainName.replace("www.", "");
				
				//check if has any DNS records set
				if (new DNSLookup().hasRecordsSet(domainName)) return;

                Informer.printInformation(" >>> " + domainName);

				URL ws = new URL(webService + domainName);
				ws.getContent();

			} catch (Exception e) {}
		}
	}
	
}
