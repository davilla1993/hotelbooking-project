package com.follysitou.hotelbooking.controllers;

import com.follysitou.hotelbooking.dtos.BookingDto;
import com.follysitou.hotelbooking.dtos.Response;
import com.follysitou.hotelbooking.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    public ResponseEntity<Response> createBooking(@RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok(bookingService.createBooking(bookingDto));
    }

    @GetMapping("/{reference}")
    public ResponseEntity<Response> findBookingByReferenceNo(@PathVariable String reference) {
        return ResponseEntity.ok(bookingService.findBookingByReferenceNo(reference));
    }

    @PutMapping("/{update}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateBooking(@RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok(bookingService.updateBooking(bookingDto));
    }
}
