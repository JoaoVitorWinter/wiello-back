package com.wiello_back.controller;

import com.wiello_back.WielloUserRepository;
import com.wiello_back.entity.WielloUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class WielloUserController {
    private WielloUserRepository wielloUserRepository;
}
