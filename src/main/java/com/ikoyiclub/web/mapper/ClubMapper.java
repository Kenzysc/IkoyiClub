package com.ikoyiclub.web.mapper;

import java.util.stream.Collectors;

import com.ikoyiclub.web.dto.ClubDto;
import com.ikoyiclub.web.models.Club;

import com.ikoyiclub.web.mapper.*;

public class ClubMapper {
    public static Club mapToClub(ClubDto clubDto) {
        Club club = Club.builder()
                .id(clubDto.getId())
                .title(clubDto.getTitle())
                .photoUrl(clubDto.getPhotoUrl())
                .content(clubDto.getContent())
                .createdOn(clubDto.getCreatedOn())
                .updatedOn(clubDto.getUpdatedOn())
                .createdBy(clubDto.getCreatedBy())
                .subscribers(clubDto.getSubscribers())
                .build();
        return  club;
    }
    
    public static ClubDto mapToClubDto(Club club) {
        ClubDto clubDto = ClubDto.builder()
                .id(club.getId())
                .title(club.getTitle())
                .photoUrl(club.getPhotoUrl())
                .content(club.getContent())
                .createdOn(club.getCreatedOn())
                .updatedOn(club.getUpdatedOn())
                .createdBy(club.getCreatedBy())
                .events(club.getEvents().stream().map((event) -> EventMapper.mapToEventDto(event)).collect(Collectors.toList()))
                .subscribers(club.getSubscribers())
                .build();
        return clubDto;
    }
}
