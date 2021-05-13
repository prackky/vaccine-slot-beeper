package tech.knowshipp.vaccinebooking.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import tech.knowshipp.vaccinebooking.dtos.CowinResponse;

@Service
public class ApiService {
	
	private static final Logger log = LoggerFactory.getLogger(ApiService.class);
	
	@Autowired
	RestTemplate rest;
	
	@Value("${covid.appointment.search.url}")
	String finderUrl;
	
	private HttpHeaders headers;

	
	@PostConstruct
	public void setHeaders() {
		headers = new HttpHeaders();
		headers.add("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:79.0) Gecko/20100101 Firefox/79.0");
		headers.add("Accept", "application/json, text/plain, */*");
		headers.add("Accept-Language", "en-US,en;q=0.5");
		headers.add("Origin", "https://www.cowin.gov.in");
		headers.add("Connection", "keep-alive");
		headers.add("Referer", "https://www.cowin.gov.in/home");
		headers.add("TE", "Trailers");
		
	}		
	
	public CowinResponse findSlots(String pin, String date) {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl(finderUrl).queryParam("pincode", pin).queryParam("date", date).build();
		HttpEntity<Object> en = new HttpEntity<Object>(headers);
		ResponseEntity<CowinResponse> response = rest.exchange(uri.toUriString(), HttpMethod.GET, en, CowinResponse.class);
		return response.getBody();
	}
	
}
