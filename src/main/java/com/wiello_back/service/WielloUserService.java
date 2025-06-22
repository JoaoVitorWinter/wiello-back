package com.wiello_back.service;

import com.wiello_back.component.security.JwtHelper;
import com.wiello_back.controller.WielloUser.UserDTO;
import com.wiello_back.entity.WielloUser;
import com.wiello_back.exception.UserAlreadyExistsException;
import com.wiello_back.repository.WielloUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class WielloUserService {
    private WielloUserRepository wielloUserRepository;
    private JwtHelper jwtHelper;
    private AuthenticationProvider authenticationProvider;

    public String login(String username, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        authentication = authenticationProvider.authenticate(authentication);

        if (authentication.isAuthenticated()) {
            WielloUser wielloUser = (WielloUser) authentication.getPrincipal();
            return jwtHelper.createToken(wielloUser);
        }
        throw new BadCredentialsException("Invalid login credentials!");
    }

    public void createUser(UserDTO userDTO) {

        if (wielloUserRepository.existsByUsername(userDTO.username())) {
            throw new UserAlreadyExistsException();
        }
        WielloUser wielloUser = new WielloUser();
        BeanUtils.copyProperties(userDTO, wielloUser);
        wielloUserRepository.save(wielloUser);
    }
}
