package org.springframework.samples.petclinic.API.resources;

import java.io.UnsupportedEncodingException; 
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.API.model.Organization.Organization;
import org.springframework.samples.petclinic.API.model.Organizations.Organizations;
import org.springframework.web.client.RestTemplate;

public class PetfinderResource {

	private static final String PETFINDER_API_KEY = "EORSI4EeIBk7izNDQOKxDYRtAggUKjN0JpR8tGPlFfrcBdFcs8";
	private static final String SECRET = "fn2zmavJpNwmCNZeOBlzm1imqBAFRwGH5CKIR3fV";
	private static final Logger log = Logger.getLogger(PetfinderResource.class.getName());
	
	public static String getToken() {
		
		ResponseEntity<String> response = null;
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String body = "{\r\n" + 
				"  \"client_id\": \"EORSI4EeIBk7izNDQOKxDYRtAggUKjN0JpR8tGPlFfrcBdFcs8\",\r\n" + 
				"  \"client_secret\": \"fn2zmavJpNwmCNZeOBlzm1imqBAFRwGH5CKIR3fV\",\r\n" + 
				"  \"grant_type\": \"client_credentials\"\r\n" + 
				"}";
		
		HttpEntity<String> request = new HttpEntity<String>(body, headers);
		
		String access_token_url = "https://api.petfinder.com/v2/oauth2/token";
		
		response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
		
		//System.out.println(response.getBody());
		
		String responseChungo = response.getBody();
		String[] s = responseChungo.split(",");
		String[] s2 = s[2].trim().split(":");
		String token = s2[1].trim().substring(1, s2[1].trim().length()-2);
		System.out.println(token);
		
		return token;
	}
	
	public Organizations getOrganizations() throws UnsupportedEncodingException {
		ClientResource cr = new ClientResource("https://api.petfinder.com/v2/organizations");
		String token = getToken();
		ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_OAUTH_BEARER);
		challengeResponse.setRawValue(token);
		cr.setChallengeResponse(challengeResponse);
		Organizations res = cr.get(Organizations.class);
		log.log(Level.FINE, "Organizations search done successfully");
		return res;
	}
	
	public Organization getOrganizationsById(String id) throws UnsupportedEncodingException {
		ClientResource cr = new ClientResource("https://api.petfinder.com/v2/organizations/".concat(id));
		String token = getToken();
		ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_OAUTH_BEARER);
		challengeResponse.setRawValue(token);
		cr.setChallengeResponse(challengeResponse);
		Organization res = cr.get(Organization.class);
		log.log(Level.FINE, "Organization search done successfully");
		return res;
	}
	
}
