package com.follysitou.hotelbooking.controllers;

import com.follysitou.hotelbooking.dtos.LoginRequest;
import com.follysitou.hotelbooking.dtos.RegistrationRequest;
import com.follysitou.hotelbooking.dtos.Response;
import com.follysitou.hotelbooking.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid RegistrationRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(userService.loginUser(request));
    }

}
