package com.ikoyiclub.web.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ikoyiclub.web.dto.EventDto;
import com.ikoyiclub.web.mapper.EventMapper;
import com.ikoyiclub.web.models.Club;
import com.ikoyiclub.web.models.Event;
import com.ikoyiclub.web.models.UserEntity;
import com.ikoyiclub.web.repository.ClubRepository;
import com.ikoyiclub.web.repository.EventRepository;
import com.ikoyiclub.web.service.impl.EventServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EventServiceTests {
	
	private Club club1;
	private UserEntity user;
	private EventDto eventDto1;
	private Event event1;
	private Long id;
	
	@InjectMocks 
	private EventServiceImpl eventService;
	
	@Mock
	private EventRepository eventRepository;
	
	@Mock
	private ClubRepository  clubRepository;
	
	@BeforeEach
	public void init() {
		
		user = new UserEntity();
		
		club1 = Club.builder().title("great club").photoUrl("http").content("awesome club")
				  .createdBy(user).events(List.of()).build();
		
		eventDto1 = EventDto.builder().name("great event").club(club1).build();	
		
		event1 = Event.builder().name("great event").club(club1).build();
		
		id = 1L;
				
	}
	
	@Test
	public void EventService_TestCreateEvent_ReturnsVoid() {
		when(clubRepository.findById(id)).thenReturn(Optional.of(club1));
		
		assertAll(() -> eventService.createEvent(id, eventDto1));
	}
	
	@Test
	public void EventService_TestFindAllEvents_ReturnsListOfEventDto() {
		when(eventRepository.findAll()).thenReturn(Arrays.asList(event1));
		
		List<EventDto> returnedEventDtos = eventService.findAllEvents();
		
		Assertions.assertThat(returnedEventDtos).isNotNull();
		
	}
	
	@Test
	public void EventService_TestFindByEventId_ReturnsEventDto() {
		when(eventRepository.findById(id)).thenReturn(Optional.of(event1));
		
		EventDto returnedEventDto = eventService.findByEventId(id);
		
		Assertions.assertThat(returnedEventDto).isEqualTo(eventDto1);
		
	}
	
	@Test
	public void EventService_TestUpdateEvent_ReturnsVoid() {
				
		assertAll(() -> eventService.updateEvent(eventDto1));
		
	}
	
	@Test
	public void EventService_TestDeleteEvent_ReturnsVoid() {	
		
		assertAll(() -> eventService.deleteEvent(id));
		
	}
	
}











