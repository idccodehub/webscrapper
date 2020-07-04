package com.idccodehub.webscrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.idccodehub.webscrapper.services.Webscrapper;
import com.idccodehub.webscrapper.utils.SSLUtils;

@SpringBootApplication
public class WebscrapperApplication {
	
	public static void main(String[] args) {
		SSLUtils.disableCertificateValidation();
		Webscrapper webscrapper = new Webscrapper();
		webscrapper.startScrapping(args[0]);
		SpringApplication.run(WebscrapperApplication.class, args);
		
	}

}
