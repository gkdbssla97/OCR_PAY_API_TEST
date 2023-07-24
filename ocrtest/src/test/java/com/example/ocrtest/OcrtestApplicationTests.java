package com.example.ocrtest;

import com.example.ocrtest.domain.Member;
import com.example.ocrtest.domain.Pay;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@Transactional
class OcrtestApplicationTests {

	@Autowired MemberRepository memberRepository;
	@Autowired EntityManager em;
	@Autowired MockMvc mvc;
	@Autowired
	ObjectMapper objectMapper;

	@Test
	void contextLoads() throws Exception {
		Member member1 = new Member("member1");
		memberRepository.save(member1);

		Pay pay = new Pay();

		MockHttpServletResponse naver_response = mvc.perform(get("/ocr/V2"))
				.andDo(print())
				.andReturn().getResponse();
		String s = naver_response.getContentAsString();
		JSONObject jsonObject = new JSONObject(s);
		JSONArray images = jsonObject.getJSONArray("images");

		String validDate = images.getJSONObject(0).getJSONObject("creditCard").getJSONObject("result").getJSONObject("validThru").getString("text");
		String cardNumber = images.getJSONObject(0).getJSONObject("creditCard").getJSONObject("result").getJSONObject("number").getString("text");
		System.out.println("cardNumber: " + cardNumber);
		System.out.println("validDate: " + validDate);

		System.out.println("start----toss----");
		MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
		info.add("cardNumber", cardNumber);
		info.add("validYear", validDate.split("/")[1]);
		info.add("validMonth", validDate.split("/")[0]);
		MockHttpServletResponse toss_response = mvc.perform(post("/ocr/v1/payments/credit-card")
						.params(info))
				.andDo(print())
				.andReturn().getResponse();
		String toss = toss_response.getContentAsString();
		System.out.println(toss);
		System.out.println("end----toss----");

	}
}
