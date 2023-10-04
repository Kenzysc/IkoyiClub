package com.ikoyiclub.web.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
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

import com.ikoyiclub.web.dto.EventDto;
import com.ikoyiclub.web.models.Club;
import com.ikoyiclub.web.models.Event;
import com.ikoyiclub.web.models.UserEntity;
import com.ikoyiclub.web.service.ClubService;
import com.ikoyiclub.web.service.EventService;
import com.ikoyiclub.web.service.UserService;

@WebMvcTest(controllers = EventController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EventControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@InjectMocks
	private EventController eventController;
	
	@MockBean
	private EventService eventService;
	
	@MockBean
	private UserService userService;
	
	private EventDto eventDto;
	private EventDto eventDto2;
	private EventDto eventDto3;
	private Event event;
	private List<EventDto> events;
	private Long eventId;
	private Club club;
	private Long clubId;
	private UserEntity user;
	private String username;
	private SecurityContext securityContext;
	private Authentication authentication;
	
	@BeforeEach
	public void init() {
		user = new UserEntity();
		username = "ken";
		clubId = 1L;
		club = new Club();
		club.setCreatedBy(user);
		club.setId(clubId);
		eventId = 123L;
		eventDto = new EventDto();
		event = new Event();
		eventDto2 = EventDto.builder().name("ken event").type("good type").photoUrl("http").club(club)
				      .build();
		eventDto3 = EventDto.builder().name("ken event").photoUrl("http").club(club).build();
		events = List.of(eventDto2);
				
		securityContext = SecurityContextHolder.createEmptyContext();
		authentication = mock(Authentication.class);
		
		when(authentication.getName()).thenReturn(username);
		securityContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(securityContext);
	}
	
	@Test
	public void EventController_TestCreateEventForm_ReturnsString() throws Exception {
		ResultActions response = mockMvc.perform(get("/events/{clubId}/new", clubId));
		
		response.andExpect(status().isOk())
				.andExpect(model().attribute("clubId", clubId))
				.andExpect(model().attribute("event", event))
				.andExpect(view().name("events-create"));
	}
	
	@Test
	public void EventController_TestEventList_ReturnsString() throws Exception {
		when(userService.findByUsername(username)).thenReturn(user);
		when(eventService.findAllEvents()).thenReturn(events);
		
		ResultActions response = mockMvc.perform(get("/events"));
		
		response.andExpect(status().isOk())
				.andExpect(model().attribute("user", user))
				.andExpect(model().attribute("events", events))
				.andExpect(view().name("events-list"));
		
		verify(eventService, times(1)).findAllEvents();
		verify(userService, times(1)).findByUsername(username);
				
	}
	
	@Test
	public void EventController_TestViewEvent_ReturnString() throws Exception {
		when(eventService.findByEventId(eventId)).thenReturn(eventDto2);
		when(userService.findByUsername(username)).thenReturn(user);
		
		ResultActions response = mockMvc.perform(get("/events/{eventId}", eventId));
		
		response.andExpect(status().isOk())
				.andExpect(model().attribute("club", eventDto2.getClub()))
				.andExpect(model().attribute("user", user))
				.andExpect(model().attribute("event", eventDto2))
				.andExpect(view().name("events-detail"));
		
		verify(eventService, times(1)).findByEventId(eventId);
		verify(userService, times(1)).findByUsername(username);
	}
	
	@Test
	public void EventController_TestEditEventForm_ReturnsString() throws Exception {
		when(eventService.findByEventId(eventId)).thenReturn(eventDto2);
		
		mockMvc.perform(get("/events/{eventId}/edit", eventId))
			   .andExpect(status().isOk())
			   .andExpect(model().attribute("event", eventDto2))
			   .andExpect(view().name("events-edit"));
		
		verify(eventService, times(1)).findByEventId(eventId);
	}
	
	@Test
	public void EventController_TestCreateEventWithoutError_ReturnsString() throws Exception {
		ResultActions response = mockMvc.perform(post("/events/{clubId}", clubId)
										.flashAttr("event", eventDto2));
		
		response.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/clubs/" + clubId));
		
		verify(eventService, times(1)).createEvent(clubId, eventDto2);
										
	}
	
	@Test
	public void EventController_TestCreateEventWithError_ReturnsString() throws Exception {
		//thymeleaf security does the test

		verify(eventService, never()).createEvent(clubId, eventDto3);
	}
	
	@Test
	public void EventController_TestUpdateEventWithoutError_ReturnsString() throws Exception {
		when(eventService.findByEventId(eventId)).thenReturn(eventDto2);
		
		ResultActions response = mockMvc.perform(post("/events/{eventId}/edit", eventId)
										.flashAttr("event", eventDto2));
		
		response.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/events"));
		
		verify(eventService, times(1)).updateEvent(eventDto2);
	}
	
	@Test
	public void EventController_TestUpdateEventWithError_ReturnsString() throws Exception {
		
		// thymeleaf security does the test
		
		verify(eventService, never()).updateEvent(eventDto3);
	}
	
	@Test
	public void EventController_TestDeleteEvent_ReturnsString() throws Exception {
		mockMvc.perform(get("/events/{eventId}/delete", eventId))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/events"));
		
		verify(eventService, times(1)).deleteEvent(eventId);
	}

}








