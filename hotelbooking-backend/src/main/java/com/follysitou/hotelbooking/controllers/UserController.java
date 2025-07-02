package com.follysitou.hotelbooking.controllers;

import com.follysitou.hotelbooking.dtos.Response;
import com.follysitou.hotelbooking.dtos.UserDto;
import com.follysitou.hotelbooking.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/me")
    public ResponseEntity<Response> getOwnAccountDetails() {
        return ResponseEntity.ok(userService.getOwnAccountDetails());
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateOwnAccount(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateOwnAccount(userDto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteOwnAccount() {
        return ResponseEntity.ok(userService.deleteOwnAccount());
    }

    @GetMapping("/bookings")
    public ResponseEntity<Response> getMyBookingHistory() {
        return ResponseEntity.ok(userService.getMyBookingHistory());
    }
}
