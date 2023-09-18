package com.ikoyiclub.web.service;

import java.util.List;

import com.ikoyiclub.web.dto.ClubDto;
import com.ikoyiclub.web.models.Club;

public interface ClubService {
	
	List<ClubDto> findAllClubs();

	Club saveClub(ClubDto clubDto);

	void updateClub(ClubDto clubDto);

	ClubDto findClubById(Long clubId);

	void delete(Long clubId);

	List<ClubDto> searchClubs(String query);
}
