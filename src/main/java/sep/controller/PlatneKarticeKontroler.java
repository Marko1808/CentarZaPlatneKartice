package sep.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	
	private static final Logger logger = LoggerFactory.getLogger(PlatneKarticeKontroler.class);

	@CrossOrigin
	@RequestMapping(value = "/usmeriNaBanku", method = RequestMethod.POST)
	public URLDTO usmeriNaBanku(@RequestBody CardDTO card) {
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		Boolean autentifikovan = false;
		
		try {
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<CardDTO> entity = new HttpEntity<>(card, headers);
			autentifikovan = client.postForObject("https://localhost:1235/payment/autentifikacijaUsera", entity, Boolean.class);
			
		} catch (Exception e) {
			System.out.println("\n\t\tNe može da pošalje zahtev banci.\n");
			
		}
		
		if(autentifikovan==true) {
			RestTemplate client1 = new RestTemplate();
			HttpHeaders headers1 = new HttpHeaders();
			
			try {
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<CardDTO> entity = new HttpEntity<>(card, headers);
				URLDTO url = new URLDTO();
				url = client.postForObject("https://localhost:1235/payment/izvrsiPlacanje", entity, URLDTO.class);
				
				logger.info("\n\t\tPreusmeravanje na URL (uspešno):\n" + url + "\n");
				return url;
			} catch (Exception e) {
				URLDTO url = new URLDTO();
				url.setUrl("https://localhost:9081/greska.html");
				logger.info("\n\t\tGreška prilikom pokušaja preusmeravanja na banku.\n");
				return url;
			}
		}else{
			URLDTO url = new URLDTO();
			url.setUrl("https://localhost:9081/greska.html");
			logger.info("\n\t\tGreška prilikom pokušaja preusmeravanja na banku.\n");
			return url;
		}
	}

}
