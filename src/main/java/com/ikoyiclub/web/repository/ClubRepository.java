package com.ikoyiclub.web.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ikoyiclub.web.models.Club;

public interface ClubRepository extends JpaRepository<Club, Long> {
	Optional<Club> findByTitle(String url);
	
	@Query("SELECT c from Club c WHERE c.title LIKE CONCAT('%', :query, '%')")
	List<Club> searchClub(String query);
}
