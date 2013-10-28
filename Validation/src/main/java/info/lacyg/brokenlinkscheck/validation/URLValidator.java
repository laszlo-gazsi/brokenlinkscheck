package info.lacyg.brokenlinkscheck.validation;

import info.lacyg.brokenlinkscheck.model.Link;

import java.net.HttpURLConnection;
import java.net.URL;

public class URLValidator {
	
	private String[] allowedDomains = {"com", "net", "info", "org", "mobi", "asia", "name",
			"biz", "us", "tv", "in", "eu", "me", "cc", "be", "co"};
	
	public Boolean isTextType(Link link) {
		try {
			URL url = new URL(link.getUrl());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			if ((connection.getContentType()!=null ) &&
					!connection.getContentType().toLowerCase().startsWith("text/") ) {
				connection.disconnect();
				return false;
			}
			connection.disconnect();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public Boolean hasValidForm(Link link) {
		URL url;
		try {
			url = new URL(link.getUrl());
			
			// no www.
			String host = url.getHost().toLowerCase();
			host = host.replace("www.", "");
			
			String[] hostComponents = host.split("\\.");
			if (hostComponents.length != 2) return false;
			
			//.com .hu .ro .info ...
			Boolean ok = false;
			for (int i = 0; i < allowedDomains.length; i++) {
				if (allowedDomains[i].equals(hostComponents[1])) {
					ok = true;
					break;
				}
			}
			if (!ok) return false;
			
			//letters, numbers or hyphen
			for (int i = 0; i < hostComponents[0].length(); i++) {
				Character c = hostComponents[0].toCharArray()[i];
				if (!Character.isLetterOrDigit(c) && c != '-') {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public Boolean hasIP(Link link) {
		try {
			URL url = new URL(link.getUrl());
			java.net.InetAddress.getByName(url.getHost());
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	
	
}
