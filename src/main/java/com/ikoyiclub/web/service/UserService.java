package com.ikoyiclub.web.service;

import com.ikoyiclub.web.dto.RegistrationDto;
import com.ikoyiclub.web.models.UserEntity;

public interface UserService {
	void saveUser(RegistrationDto registrationDto);
	UserEntity findByEmail(String email);
	UserEntity findByUsername(String username);
}
