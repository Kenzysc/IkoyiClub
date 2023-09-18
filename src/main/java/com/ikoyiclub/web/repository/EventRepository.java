package com.ikoyiclub.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ikoyiclub.web.models.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

}
