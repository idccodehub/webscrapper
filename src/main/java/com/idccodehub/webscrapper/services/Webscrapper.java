package com.idccodehub.webscrapper.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.idccodehub.webscrapper.utils.SSLUtils;


public class Webscrapper {
	
	public void startScrapping(String url) {
		
		Set<String> links = getAllLinks(url);
		ExecutorService executor = Executors.newFixedThreadPool(10);
		links.stream().forEach(link -> {
				RunnableService worker = new RunnableService(link);
				executor.submit(worker);
			}
		);
		
		executor.shutdown();
		
		while(!executor.isTerminated()) {
			
		}
		
		System.out.println("\n All Threads Brought Data");
		
	}
	
	/**Get all links from the given web site **/
	
	public Set<String> getAllLinks(String url){
		SSLUtils.disableCertificateValidation();
		Document doc = null;
		try {
			Connection con = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
					.referrer("http://www.google.com")
					.ignoreContentType(true)
					.followRedirects(true)
					.timeout(60*1000);
			Connection.Response res = con.execute();
			if(res.statusCode() == 200) {
				doc = con.get();
			}
			} catch (IOException e){
				e.printStackTrace();
		}
		Set<String> urlSet = new HashSet<String>();
		Elements links = doc.select("[href]");
		for(Element link : links){
			urlSet.add(link.attr("abs:href"));
		}
		Set<String> finalUrlSet = checkValidLinks(urlSet, url);
		return finalUrlSet;
	}
	
	public Set<String> checkValidLinks(Set<String> urlSet, String givenUrl) {
		String[] words = {".js", ".css", ".json"};
		
		Set<String> newUrlSet  = urlSet.stream()
				.filter(url -> url.contains(givenUrl))
				.filter(url -> !url.contains(".js"))
				.filter(url -> !url.contains(".css"))
				.filter(url -> !url.contains(".json"))
				.filter(url -> !url.isEmpty())
				.collect(Collectors.toSet());

		return newUrlSet;
	}

}
