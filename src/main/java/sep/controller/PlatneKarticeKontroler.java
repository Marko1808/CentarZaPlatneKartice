package sep.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import sep.dto.CardDTO;
import sep.dto.URLDTO;

@RestController
@RequestMapping(value = "/platneKartice")
public class PlatneKarticeKontroler {

	@CrossOrigin
	@RequestMapping(value = "/usmeriNaBanku", method = RequestMethod.POST)
	public URLDTO usmeriNaBanku(@RequestBody CardDTO card) {
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		Boolean autentifikovan = false;
		try {
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<CardDTO> entity = new HttpEntity<>(card, headers);
			autentifikovan = client.postForObject("http://localhost:1235/payment/autentifikacijaUsera", entity, Boolean.class);
			
		} catch (Exception e) {
			System.out.println("Ne moze da posalje");
			
		}
		if(autentifikovan==true) {
			RestTemplate client1 = new RestTemplate();
			HttpHeaders headers1 = new HttpHeaders();
			try {
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<CardDTO> entity = new HttpEntity<>(card, headers);
				URLDTO url = new URLDTO();
				url = client.postForObject("http://localhost:1235/payment/izvrsiPlacanje", entity, URLDTO.class);
				return url;
			} catch (Exception e) {
				URLDTO url = new URLDTO();
				url.setUrl("http://localhost:1234/greska.html");
				return url;
			}
		}else {
			URLDTO url = new URLDTO();
			url.setUrl("http://localhost:1234/greska.html");
			return url;
		}

	}

}
