package com.ikoyiclub.web.service;

import java.util.List;

import com.ikoyiclub.web.dto.EventDto;

public interface EventService {
	void createEvent(Long clubId, EventDto eventDto);
    List<EventDto> findAllEvents();
    EventDto findByEventId(Long eventId);
    void updateEvent(EventDto eventDto);
    void deleteEvent(long eventId);
}
