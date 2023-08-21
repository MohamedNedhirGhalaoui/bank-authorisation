package com.bnm.dev.web;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bnm.dev.model.Authorisation;
import com.bnm.dev.model.User;
import com.bnm.dev.service.AuthorisationService;
import com.bnm.dev.service.UserService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;

@Controller
public class HomeController {
	private AuthorisationService authService;
	
	Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	public HomeController(AuthorisationService authService) {
		super();
		this.authService = authService;
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/")
	public String home(Model model) {
        List<Authorisation> auths = authService.getAuthorisationsInProgress("IN_PROGRESS");
		
		model.addAttribute("authorisations", auths);
		
		return "index";
	}
	
	@GetMapping("/update")
	public ResponseEntity updateAuthoriation(@RequestParam Long id, @RequestParam Long type) {
		String status = "";
		if(type == 1)
			status = "AUTHORISED";
		else if(type == 2)
			status = "REJECTED";
		
		Authorisation auth = authService.loadAuthorisationById(id);
		
		auth.setStatus(status);
		authService.save(auth);
		
		String json = "{\n"
				+ "\"transactionId\":\""+auth.getTransactionID()+"\",\n"
				+ "\"notificationType\":\" ACCOUNT\",\n"
				+ "\"status\":\""+auth.getStatus()+"\",\n"
				+ "\"rejectionReasonMessage\":\"\" \n"
				+ "}";
		
		
		
		return ResponseEntity.ok(json);
	}
	@GetMapping(value="/api/test1", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity test1() {
		String payload = "{\n"
				+ " \"transactionId\":\"cb2ee12e-a23f-4bbc-a0aa-575d158e7c3f\",\n"
				+ " \"operationType\":\"ACCOUNT\",\n"
				+ " \"taxPayerIban\":\"MR13000010010001234567890134\",\n"
				+ " \"taxPayerLegalName\":\"ADIAS SA\",\n"
				+ " \"nif\":\"01234567\",\n"
				+ " \"taxPayerPhoneNumber\":\"+22222171190\",\n"
				+ " \"operationDate\":\"2022-12-13T19:20:45.274\"\n"
				+ "}\n"
				+ "";
		return ResponseEntity.status(HttpStatus.OK).body(payload);
	}
	@PostMapping(value="/api/stt", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity endPoint(@RequestBody String payload) throws Exception {
		logger.info(payload);
		//System.out.println(payload);
		JsonObject jsonObject = new JsonParser().parse(payload).getAsJsonObject();
		
		String type = jsonObject.get("notificationType").getAsString();
		String transactionId = jsonObject.get("transactionId").getAsString();
		String urlGetAccount = "http://localhost:8080/api/test1"; //+transactionId;
		String urlPayment = "/api/v1/notification/ACCOUNT/"+transactionId;
		
		if(type.equals("ACCOUNT")) {
			String accountJson = getJsonFromUrl(urlGetAccount);
			jsonObject = new JsonParser().parse(accountJson).getAsJsonObject();
			String taxPayerIBAN = jsonObject.get("taxPayerIban").getAsString();
			String taxPayerLegalName = jsonObject.get("taxPayerLegalName").getAsString();
			String taxPayerNIF = jsonObject.get("nif").getAsString();
			String taxPayerPhoneNumber = jsonObject.get("taxPayerPhoneNumber").getAsString();
			
			Authorisation auth = new Authorisation();
			auth.setIBAN(taxPayerIBAN);
			auth.setLegalName(taxPayerLegalName);
			auth.setStatus(Authorisation.IN_PROGRESS);
			
			authService.save(auth);
		} else if(type.equals("PAYMENT")) {
			String paymentJson = getJsonFromUrl(urlPayment);
			jsonObject = new JsonParser().parse(paymentJson).getAsJsonObject();
		    Authorisation auth = authService.loadAuthByTransactionID(transactionId);
		    if(auth == null || !auth.getStatus().equals(Authorisation.AUTHORIZED))
		    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		    Integer amount = jsonObject.get("Amount").getAsInt();
			
		}
		return ResponseEntity.status(HttpStatus.OK).body(payload);
	}
	
	public String getJsonFromUrl(String url) {
		RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result =
                restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return result.getBody();
	}
	
	public String updateSTT(String content, String url) {
		RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        
        HttpEntity<String> entity = new HttpEntity<>(content, headers);
        
        ResponseEntity<String> result =
                restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return result.getBody();
	}
	
}
