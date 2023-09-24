package com.ikoyiclub.web.controllers;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;

import com.ikoyiclub.web.controller.ClubController;
import com.ikoyiclub.web.dto.ClubDto;
import com.ikoyiclub.web.service.ClubService;

@WebMvcTest(controllers = ClubController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ClubControllerTests {

	@Autowired
	private MockMvc mockMvc;	
	
	@MockBean
	private ClubService clubService;
	
	private Model model;
	private ClubDto clubDto;
	
	
	@BeforeEach
	public void init() {
		
	}
	
	@Test
	public void ClubController_CreateClubForm_ReturnsString() throws Exception {
		doNothing().when(model).addAttribute("clubs", clubDto);
		
		ResultActions response = mockMvc.perform(get("/clubs/new")
				.contentType(MediaType.ALL));
		
		response.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
}
