package com.prosper.sbk.twitterwordcount.resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prosper.sbk.twitterwordcount.dto.TweetDto;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TwitterWordCountResourceTest {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objMapper = new ObjectMapper();


	@Test
	public void testExecuteTwitterSamplingCommand() throws Exception {
		TweetDto dto = new TweetDto();
		dto.setContent("Kubernetes is an open-source system for automating deployment, scaling, and management of containerized applications. Kubernetes is open source giving you the freedom to take advantage of on-premises, hybrid, or public cloud infrastructure, letting you effortlessly move workloads to where it matters to you");

		mockMvc.perform(
				post("/twitter/tweet")
				.content(objMapper.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				)
		.andDo(print())
		.andExpect(status().isOk());
	}
}
