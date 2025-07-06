package com.follysitou.hotelbooking.controllers;

import com.follysitou.hotelbooking.dtos.Response;
import com.follysitou.hotelbooking.dtos.RoomDto;
import com.follysitou.hotelbooking.enums.RoomType;
import com.follysitou.hotelbooking.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addRoom(
            @RequestParam Integer roomNumber,
            @RequestParam RoomType roomType,
            @RequestParam BigDecimal pricePerNight,
            @RequestParam Integer capacity,
            @RequestParam String description,
            @RequestParam MultipartFile imageFile
    ) {

        RoomDto roomDto = RoomDto.builder()
                .roomNumber(roomNumber)
                .type(roomType)
                .pricePerNight(pricePerNight)
                .capacity(capacity)
                .description(description)
                .build();

        return ResponseEntity.ok(roomService.addRoom(roomDto, imageFile));
    }


    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateRoom(
            @RequestParam (value = "id", required = true) Long id,
            @RequestParam (value = "roomNumber", required = false) Integer roomNumber,
            @RequestParam (value = "roomType", required = false) RoomType roomType,
            @RequestParam (value = "pricePerNight", required = false) BigDecimal pricePerNight,
            @RequestParam (value = "capacity", required = false) Integer capacity,
            @RequestParam (value = "description", required = false) String description,
            @RequestParam (value = "imageFile", required = false) MultipartFile imageFile
    ) {

        RoomDto roomDto = RoomDto.builder()
                .id(id)
                .roomNumber(roomNumber)
                .type(roomType)
                .pricePerNight(pricePerNight)
                .capacity(capacity)
                .description(description)
                .build();

        return ResponseEntity.ok(roomService.updateRoom(roomDto, imageFile));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/available")
    public ResponseEntity<Response> availableRooms(
            @RequestParam LocalDate checkInDate,
            @RequestParam LocalDate checkOutDate,
            @RequestParam (required = false) RoomType roomType
            ) {

        return ResponseEntity.ok(roomService.getAvailableRooms(checkInDate, checkOutDate, roomType));
    }

    @GetMapping("/types")
    public ResponseEntity<List<RoomType>> getAllRoomTypes() {
        return ResponseEntity.ok(roomService.getAllRoomTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getRoomById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteRoom(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roomService.deleteRoom(id));
    }
}
