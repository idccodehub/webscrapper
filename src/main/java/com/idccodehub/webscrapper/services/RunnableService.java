package com.idccodehub.webscrapper.services;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.idccodehub.webscrapper.utils.SSLUtils;

public class RunnableService implements Runnable {
	
	private String url;
	private Document doc;
	
	public RunnableService(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		
		String pageTitle = null;
		try {
			
			SSLUtils sslUtils = new SSLUtils();
			sslUtils.disableCertificateValidation();			
			Connection con = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:25.0) Gecko/20100101 Firefox/41.0")
					.referrer("http://www.google.com")
					.ignoreContentType(true)
					.followRedirects(true)
					.timeout(180*1000);

			Connection.Response res = con.execute();
			
			if(res.statusCode() == 200) {
				doc = con.get();
			}
			
			pageTitle = getTitle(doc); 
			System.out.println(pageTitle);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String getTitle(Document doc) {		
		String title = doc.getElementsByTag("title").text();
		return title;
		
	}

}
