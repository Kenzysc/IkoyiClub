package com.ikoyiclub.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ikoyiclub.web.dto.ClubDto;
import com.ikoyiclub.web.models.UserEntity;
import com.ikoyiclub.web.security.SecurityUtil;
import com.ikoyiclub.web.service.ClubService;
import com.ikoyiclub.web.service.UserService;
import com.ikoyiclub.web.service.impl.ClubServiceImpl;

@Controller
public class ClubController {
	private ClubService clubService;
	private UserService userService;

	@Autowired
	public ClubController(ClubService clubService, UserService userService) {
		this.clubService = clubService;
		this.userService = userService;
	}
	
	@GetMapping("/")
	public String home(Model model) {
		return "redirect:/clubs";
	}
	
	@GetMapping("/clubs")
    public String listClubs(Model model) {
        UserEntity user = new UserEntity();
        List<ClubDto> clubs = clubService.findAllClubs();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        model.addAttribute("user", user);
        model.addAttribute("clubs", clubs);
        return "clubs-list";
    }

    @GetMapping("/clubs/{clubId}")
    public String clubDetail(@PathVariable("clubId") long clubId, Model model) {
    	String subStatus = null;
    	
        UserEntity user = new UserEntity();
        ClubDto clubDto = clubService.findClubById(clubId);
        String username = SecurityUtil.getSessionUser();
        
        if(username != null) {
            user = userService.findByUsername(username);
            model.addAttribute("user", user);
            
            
            if(clubDto.getSubscribers().contains(user)) {
            	subStatus = "Unsubscribe";
            }
            else {
            	
				subStatus = "Subscribe";
			}
        }
        
        model.addAttribute("user", user);
        model.addAttribute("club", clubDto);
        model.addAttribute("substatus", subStatus);

        
        return "clubs-detail";
    }

    @GetMapping("/clubs/new")
    public String createClubForm(Model model) {
        ClubDto club = new ClubDto();
        model.addAttribute("club", club);
        return "clubs-create";
    }

    @GetMapping("/clubs/{clubId}/delete")
    public String deleteClub(@PathVariable("clubId")Long clubId) {
        clubService.delete(clubId);
        return "redirect:/clubs";
    }

    @GetMapping("/clubs/search")
    public String searchClub(@RequestParam(value = "query") String query, Model model) {
    	UserEntity user = new UserEntity();
    	List<ClubDto> clubs = clubService.searchClubs(query);
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        model.addAttribute("clubs", clubs);
        model.addAttribute("user", user);
        return "clubs-list";
    }

    @PostMapping("/clubs/new")
    public String saveClub(@Valid @ModelAttribute("club") ClubDto clubDto, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("club", clubDto);
            return "clubs-create";
        }
        
        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUsername(username);
        }
        ArrayList<UserEntity> subArrayList = new ArrayList<>();
        subArrayList.add(user);
        clubDto.setSubscribers(subArrayList);
        
        clubService.saveClub(clubDto);
        return "redirect:/clubs";
    }

    @GetMapping("/clubs/{clubId}/edit")
    public String editClubForm(@PathVariable("clubId") Long clubId, Model model) {
        ClubDto club = clubService.findClubById(clubId);
        model.addAttribute("club", club);
        return "clubs-edit";
    }
    
    @PostMapping("/clubs/{clubId}/edit")
    public String updateClub(@PathVariable("clubId") Long clubId,
                             @Valid @ModelAttribute("club") ClubDto club,
                             BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("club", club);
            return "clubs-edit";
        }
        club.setId(clubId);
        clubService.updateClub(club);
        return "redirect:/clubs";
    }
    
    @GetMapping("/clubs/{clubId}/subscription")
    public String subscribe(@PathVariable("clubId") Long clubId) {
    	
    	 ClubDto clubDto = clubService.findClubById(clubId);
    	 UserEntity user = new UserEntity();
         String username = SecurityUtil.getSessionUser();
         if(username != null) {
             user = userService.findByUsername(username);
         }
           
         // check subscription status and toggle it
         List<UserEntity> clubSubscribers = clubDto.getSubscribers();
         if (clubSubscribers.contains(user)) {
        	 clubSubscribers.remove(user);
        	 clubDto.setSubscribers(clubSubscribers);
        	 clubDto.setId(clubId);
        	 clubService.updateClub(clubDto);
        	 
         } else {
        	 clubSubscribers.add(user);
        	 clubDto.setSubscribers(clubSubscribers);
        	 clubDto.setId(clubId);
        	 clubService.updateClub(clubDto);
        	 
         }
         
    	return "redirect:/clubs/{clubId}";
    	
    }
	
}







