package com.ikoyiclub.web.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ikoyiclub.web.dto.ClubDto;
import com.ikoyiclub.web.mapper.ClubMapper;
import com.ikoyiclub.web.models.Club;
import com.ikoyiclub.web.models.UserEntity;
import com.ikoyiclub.web.repository.ClubRepository;
import com.ikoyiclub.web.repository.UserRepository;
import com.ikoyiclub.web.security.SecurityUtil;
import com.ikoyiclub.web.service.impl.ClubServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ClubServiceTests {
	
	@InjectMocks
	private  ClubServiceImpl clubService;
	
	@Mock
	private ClubRepository clubRepository;
	
	@Mock
	private UserRepository userRepository;
	
	private List<Club> clubs;
	private Club club;
	private Club club2;
	private Club club3;
	private UserEntity user;
	private String username;
	private ClubDto clubDto;
	private ClubDto clubDto2;
	private SecurityContext securityContext;
	private Authentication authentication;
	private Long clubId;
	
	@BeforeEach
	public void init() {
		clubId = 1L;
		
		user = new UserEntity();
		username = "ken";
		
		club = new Club();
//		club3 = new Club();
//		club3.setTitle("great club");
//		club3.setContent("awe content");
//		club3.setPhotoUrl("http");
//		club3.setCreatedBy(user);
		club2 = Club.builder().title("great club").photoUrl("http").content("awesome club")
							  .createdBy(user).events(List.of()).build();
		clubs = Arrays.asList(club2);
		
		clubDto = ClubDto.builder().title("great club").photoUrl("http").content("awesome club")
				  .createdBy(user).events(List.of()).build();
		clubDto2 = new ClubDto();
		clubDto2.setTitle("great c");
		clubDto2.setContent("awe content");
		clubDto2.setPhotoUrl("http");
		clubDto2.setCreatedBy(user);
		
		club3 = ClubMapper.mapToClub(clubDto2);
		
		securityContext = SecurityContextHolder.createEmptyContext();
		authentication = mock(Authentication.class);
		
		securityContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(securityContext);
	}
	
	@Test
	public void ClubService_TestFindAllClubs_ReturnsClubDtoList() {
		when(clubRepository.findAll()).thenReturn(clubs);
		
		List<ClubDto> serviceReturn = clubService.findAllClubs();
		
		Assertions.assertThat(clubs.size()).isEqualTo(serviceReturn.size());
		
		verify(clubRepository, times(1)).findAll();
	}
	
	@Test
	public void ClubService_TestSaveClub_ReturnsClub() {
		when(SecurityUtil.getSessionUser()).thenReturn(username);
		when((userRepository.findByUsername(username))).thenReturn(user);
		when(clubRepository.save(club3)).thenReturn(club2);
		
		Club savedClub = clubService.saveClub(clubDto2);
		
		Assertions.assertThat(savedClub).isNotNull();
		
		verify(clubRepository, times(1)).save(club3);
		verify(userRepository, times(1)).findByUsername(username);
	}
	
	@Test
	public void ClubService_TestUpdateClub_ReturnsVoid() {
		when(SecurityUtil.getSessionUser()).thenReturn(username);
		when((userRepository.findByUsername(username))).thenReturn(user);
		
		 assertAll(() -> clubService.updateClub(clubDto2));
		
		verify(userRepository, times(1)).findByUsername(username);
	}
	
	@Test
	public void ClubService_TestFindClubById_ReturnsClubDto() {
		when(clubRepository.findById(club2.getId())).thenReturn(Optional.of(club2));
		
		ClubDto returnedClub = clubService.findClubById(club2.getId());
		
		Assertions.assertThat(returnedClub).isNotNull();
				
	}
	
	@Test
	public void ClubService_TestDelete_ReturnsVoid() {
		assertAll(() -> clubRepository.deleteById(clubId));
	}
	
	@Test
	public void ClubService_TestSearchClubs_ReturnsClubDtoList() {
		when(clubRepository.searchClub("ken")).thenReturn(clubs);
		
		List<ClubDto> returnedClubs = clubService.searchClubs("ken");
		
		Assertions.assertThat(returnedClubs).isNotNull();
	}

}







