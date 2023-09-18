package com.ikoyiclub.web.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ikoyiclub.web.dto.ClubDto;
import com.ikoyiclub.web.models.Club;
import com.ikoyiclub.web.models.UserEntity;
import com.ikoyiclub.web.repository.ClubRepository;
import com.ikoyiclub.web.repository.UserRepository;
import com.ikoyiclub.web.security.SecurityUtil;
import com.ikoyiclub.web.service.ClubService;

import static com.ikoyiclub.web.mapper.ClubMapper.mapToClubDto;
import static com.ikoyiclub.web.mapper.ClubMapper.mapToClub;


@Service
public class ClubServiceImpl implements ClubService {
	
	private ClubRepository clubRepository;
	private UserRepository userRepository;
	
	@Autowired
	public ClubServiceImpl(ClubRepository clubRepository, UserRepository userRepository) {
		super();
		this.clubRepository = clubRepository;
		this.userRepository = userRepository;
	}


	@Override
	public List<ClubDto> findAllClubs() {
		List<Club> clubs = clubRepository.findAll();
		return clubs.stream().map((club) -> mapToClubDto(club)).collect(Collectors.toList());
	}

	@Override
	public Club saveClub(ClubDto clubDto) {
		String username = SecurityUtil.getSessionUser();
		UserEntity user = userRepository.findByUsername(username);
		 Club club = mapToClub(clubDto);
		 club.setCreatedBy(user);
	     return clubRepository.save(club);
	}
	
	@Override
    public void updateClub(ClubDto clubDto) {
		String username = SecurityUtil.getSessionUser();
		UserEntity user = userRepository.findByUsername(username);
        Club club = mapToClub(clubDto);
        club.setCreatedBy(user);
        clubRepository.save(club);
    }
	
	@Override
    public ClubDto findClubById(Long clubId) {
        Club club = clubRepository.findById(clubId).get();
        return mapToClubDto(club);
    }
	
	@Override
    public void delete(Long clubId) {
        clubRepository.deleteById(clubId);
    }
	
	@Override
	public List<ClubDto> searchClubs(String query) {
		List<Club> clubs = clubRepository.searchClub(query);
		return clubs.stream().map(club -> mapToClubDto(club)).collect(Collectors.toList());
	}

}





