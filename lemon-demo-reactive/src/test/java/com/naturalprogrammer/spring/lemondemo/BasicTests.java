package com.naturalprogrammer.spring.lemondemo;

import static com.naturalprogrammer.spring.lemondemo.MyTestUtils.ADMIN_ID;
import static com.naturalprogrammer.spring.lemondemo.MyTestUtils.CLIENT;
import static com.naturalprogrammer.spring.lemondemo.MyTestUtils.TOKENS;
import static com.naturalprogrammer.spring.lemondemo.controllers.MyController.BASE_URI;

import org.junit.Test;

import com.naturalprogrammer.spring.lemon.commons.util.LecUtils;

public class BasicTests extends AbstractTests {

	@Test
	public void testPing() throws Exception {
		
		CLIENT.get()
			.uri(BASE_URI + "/ping")
			.exchange()
			.expectStatus()
				.isNoContent();
	}

	@Test
	public void testGetContextLoggedIn() throws Exception {
		
		CLIENT.get()
			.uri(BASE_URI + "/context")
				.header(LecUtils.TOKEN_REQUEST_HEADER_NAME, TOKENS.get(ADMIN_ID))
				.exchange()
				.expectStatus().isOk()
				.expectHeader().exists(LecUtils.TOKEN_RESPONSE_HEADER_NAME)
				.expectBody()
					.jsonPath("$.context.reCaptchaSiteKey").exists()
					.jsonPath("$.user.id").isEqualTo(ADMIN_ID.toString())
					.jsonPath("$.user.roles[0]").isEqualTo("ADMIN");
	}
	
	@Test
	public void testGetContextWithoutLoggedIn() throws Exception {
		
		CLIENT.get()
		.uri(BASE_URI + "/context")
			.exchange()
			.expectStatus().isOk()
			.expectHeader().doesNotExist(LecUtils.TOKEN_RESPONSE_HEADER_NAME)
			.expectBody()
				.jsonPath("$.context.reCaptchaSiteKey").exists()
				.jsonPath("$.user").doesNotExist();
	}	

}