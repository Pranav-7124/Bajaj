package com.bajajhealth.java_qualifier;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class JavaQualifierApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JavaQualifierApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Starting the Webhook sequence...");
		RestTemplate restTemplate = new RestTemplate();

		// ==========================================
		// STEP 1: REQUEST THE WEBHOOK AND TOKEN
		// ==========================================
		String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

		Map<String, String> initialPayload = new HashMap<>();
		initialPayload.put("name", "Pranav Kalbhor");
		initialPayload.put("regNo", "ADT23SOCB1599");
		initialPayload.put("email", "pranavkalbhor07@gmail.com");

		System.out.println("Sending Step 1 POST request...");
		ResponseEntity<Map> response1 = restTemplate.postForEntity(generateUrl, initialPayload, Map.class);
		Map<String, Object> responseBody = response1.getBody();

		String accessToken = (String) responseBody.get("accessToken");
		System.out.println("Access Token received: " + accessToken);

		// ==========================================
		// STEP 2: SUBMIT YOUR SQL SOLUTION
		// ==========================================
		String submitUrl = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", accessToken);

		Map<String, String> finalPayload = new HashMap<>();
		finalPayload.put("finalQuery", "SELECT p.AMOUNT AS SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, (EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM e.DOB)) AS AGE, d.DEPARTMENT_NAME FROM PAYMENTS p JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID WHERE EXTRACT(DAY FROM p.PAYMENT_TIME) != 1 ORDER BY p.AMOUNT DESC LIMIT 1");

		HttpEntity<Map<String, String>> entity = new HttpEntity<>(finalPayload, headers);

		System.out.println("Sending Step 2 Final POST request...");
		ResponseEntity<String> finalResponse = restTemplate.postForEntity(submitUrl, entity, String.class);

		System.out.println("Submission Status: " + finalResponse.getStatusCode());
		System.out.println("Response Body: " + finalResponse.getBody());
	}
}