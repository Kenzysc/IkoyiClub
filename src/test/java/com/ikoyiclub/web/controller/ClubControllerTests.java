package com.ikoyiclub.web.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import com.ikoyiclub.web.dto.ClubDto;
import com.ikoyiclub.web.models.UserEntity;
import com.ikoyiclub.web.service.ClubService;
import com.ikoyiclub.web.service.UserService;


@WebMvcTest(controllers = ClubController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ClubControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@InjectMocks
	private ClubController clubController;
	
	@MockBean
	private ClubService clubService;
	
	@MockBean
	private UserService userService;
	
	
	private ClubDto clubDto;
	private ClubDto clubDto2;
	private ClubDto clubDto3;
	private long clubId;
	private List<ClubDto> clubs;
	private String username;
	private UserEntity user;
	private SecurityContext securityContext;
	private Authentication authentication;
	
	
	@BeforeEach
	public void init() {
	    
		clubDto = new ClubDto();
		clubDto3 = new ClubDto();
		clubId = 123L;
		username = "ken";
		user = new UserEntity();
		clubDto2 = ClubDto.builder().title("awesome title").photoUrl("http://image.google.com")
				.content("awesome content").createdBy(user).build();
		clubs = Arrays.asList(clubDto2);
		
	
		clubDto.setId(clubId);
		clubDto.setCreatedBy(user);
		
		securityContext = SecurityContextHolder.createEmptyContext();
		authentication = mock(Authentication.class);
		
		when(authentication.getName()).thenReturn(username);
		securityContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(securityContext);
		
	}
	
	@Test
	public void ClubController_TestCreateClubForm_ReturnsString() throws Exception {
		
		
		ResultActions response = mockMvc.perform(get("/clubs/new"));
				
		
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("clubs-create"))
				.andExpect(model().attributeExists("club"));
	}
	
	@Test
	public void ClubController_TestClubDetail_ReturnsString() throws Exception {
		
		when(clubService.findClubById(clubId)).thenReturn(clubDto);
		when(userService.findByUsername(username)).thenReturn(user);
		
		ResultActions response = mockMvc.perform(get("/clubs/{clubId}", clubId));									
				
		response.andExpect(status().isOk())
				.andExpect(view().name("clubs-detail"))
				.andExpect(model().attribute("club", clubDto))
				.andExpect(model().attribute("user", user));
		
		verify(clubService, times(1)).findClubById(clubId);
		verify(userService, times(1)).findByUsername(username);
		
	}
	
	@Test
	public void ClubController_TestDeleteClub_ReturnsString() throws Exception {

		ResultActions response = mockMvc.perform(get("/clubs/{clubId}/delete", clubId));
				
		
		response.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(redirectedUrl("/clubs"));
		
		verify(clubService, times(1)).delete(clubId);
	}
	
	@Test
	public void ClubController_TestListClubs_ReturnsString() throws Exception {
		
		when(clubService.findAllClubs()).thenReturn(clubs);
		when(userService.findByUsername(username)).thenReturn(user);
		
		ResultActions response = mockMvc.perform(get("/clubs"));
		
		response.andExpect(status().isOk())
				.andExpect(view().name("clubs-list"))
				.andExpect(model().attribute("user", user))
				.andExpect(model().attribute("clubs", clubs));
		
		verify(clubService, times(1)).findAllClubs();
		verify(userService, times(1)).findByUsername(username);
		
	}
	
	@Test
	public void ClubController_TestSearchClub_ReturnsString() throws Exception {
		
		user.setId(1L);
		String query = "Great Club";
		when(clubService.searchClubs(query)).thenReturn(clubs);
		when(userService.findByUsername(username)).thenReturn(user);
		
		ResultActions response = mockMvc.perform(get("/clubs/search")
										.param("query", query));
		
		response.andExpect(status().isOk())
				.andExpect(view().name("clubs-list"))
				.andExpect(model().attribute("clubs", clubs))
				.andExpect(model().attribute("clubs", clubs));
										
		verify(clubService, times(1)).searchClubs(query);
		verify(userService, times(1)).findByUsername(username);
	}
	
	@Test
	public void ClubController_TestSaveClubWithoutError_ReturnString() throws Exception {
		
		ResultActions response = mockMvc.perform(post("/clubs/new")
										.flashAttr("club", clubDto2));
		
		response.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/clubs"));
		
		verify(clubService, times(1)).saveClub(clubDto2);
	}
	
	@Test
	public void ClubController_TestSaveClubWithError_ReturnString() throws Exception {
		
		ResultActions response = mockMvc.perform(post("/clubs/new")
										.flashAttr("club", clubDto));
		
		response.andExpect(status().isOk())
				.andExpect(view().name("clubs-create"));
		
		verify(clubService, never()).saveClub(clubDto);
	}
	
	@Test
	public void ClubController_TestEditClubForm_ReturnsString() throws Exception {
		when(clubService.findClubById(clubId)).thenReturn(clubDto2);
		
		ResultActions response = mockMvc.perform(get("/clubs/{clubId}/edit", clubId));
										
		response.andExpect(status().isOk())
				.andExpect(view().name("clubs-edit"))
				.andExpect(model().attribute("club", clubDto2));
		
		verify(clubService, times(1)).findClubById(clubId);
	}
	
	@Test
	public void ClubController_TestUpdateClubWithoutError_ReturnString() throws Exception {
		
		clubDto2.setId(clubId);
		
		ResultActions response = mockMvc.perform(post("/clubs/{clubId}/edit", clubId)
										.flashAttr("club", clubDto2));
		
		response.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/clubs"));
		
		verify(clubService, times(1)).updateClub(clubDto2);
	}
	
	@Test
	public void ClubController_TestUpdateClubWithError_ReturnString() throws Exception {
		
		ResultActions response = mockMvc.perform(post("/clubs/{clubId}/edit", clubId)
										.flashAttr("club", clubDto));
		
		response.andExpect(status().isOk())
				.andExpect(model().attribute("club", clubDto))
				.andExpect(view().name("clubs-edit"));
		
		verify(clubService, never()).updateClub(clubDto);
	}
	
}








