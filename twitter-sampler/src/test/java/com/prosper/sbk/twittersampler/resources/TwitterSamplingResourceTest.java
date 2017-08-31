package com.prosper.sbk.twittersampler.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import com.prosper.sbk.twittersampler.dto.TwitterSamplingCommandDto;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TwitterSamplingResourceTest {

	@Autowired
    private MockMvc mockMvc;

	private ObjectMapper objMapper = new ObjectMapper();
	
	
	@Test
	public void testExecuteTwitterSamplingCommand() throws Exception {
		Thread.currentThread().sleep(3000);
		
		TwitterSamplingCommandDto dto = new TwitterSamplingCommandDto();
		dto.setCommand(TwitterSamplingCommandDto.Command.START);
		
		mockMvc.perform(
			post("/tweets/samplings")
				.content(objMapper.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
		)
		.andDo(print())
		.andExpect(status().isOk());
		
		
		Thread.currentThread().sleep(5000);
		
		
		dto.setCommand(TwitterSamplingCommandDto.Command.STOP);
		mockMvc.perform(
				post("/tweets/samplings")
					.content(objMapper.writeValueAsString(dto))
					.contentType(MediaType.APPLICATION_JSON_VALUE)
			)
			.andDo(print())
			.andExpect(status().isOk());
			
		
		Thread.currentThread().sleep(1000);
	}
}
