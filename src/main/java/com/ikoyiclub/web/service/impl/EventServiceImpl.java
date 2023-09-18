package com.ikoyiclub.web.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikoyiclub.web.dto.EventDto;
import com.ikoyiclub.web.models.Club;
import com.ikoyiclub.web.models.Event;
import com.ikoyiclub.web.repository.ClubRepository;
import com.ikoyiclub.web.repository.EventRepository;
import com.ikoyiclub.web.service.EventService;

import com.ikoyiclub.web.mapper.*;

@Service
public class EventServiceImpl implements EventService {
	
	private EventRepository eventRepository;
    private ClubRepository clubRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, ClubRepository clubRepository) {
        this.eventRepository = eventRepository;
        this.clubRepository = clubRepository;
    }
	
	@Override
	public void createEvent(Long clubId, EventDto eventDto) {
		 Club club = clubRepository.findById(clubId).get();
	        Event event = EventMapper.mapToEvent(eventDto);
	        event.setClub(club);
	        eventRepository.save(event);
	}

	@Override
    public List<EventDto> findAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(event -> EventMapper.mapToEventDto(event)).collect(Collectors.toList());
    }

    @Override
    public EventDto findByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId).get();
        return EventMapper.mapToEventDto(event);
    }

    @Override
    public void updateEvent(EventDto eventDto) {
        Event event = EventMapper.mapToEvent(eventDto);
        eventRepository.save(event);
    }

    @Override
    public void deleteEvent(long eventId) {
        eventRepository.deleteById(eventId);
    }

}
