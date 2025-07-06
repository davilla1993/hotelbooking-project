package com.follysitou.hotelbooking.services;


import com.follysitou.hotelbooking.dtos.Response;
import com.follysitou.hotelbooking.dtos.RoomDto;
import com.follysitou.hotelbooking.enums.RoomType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {

    Response addRoom(RoomDto roomDto, MultipartFile imageFile);

    Response updateRoom(RoomDto roomDto, MultipartFile imageFile);

    Response getAllRooms();

    Response getRoomById(Long id);

    Response deleteRoom(Long id);

    Response getAvailableRooms(LocalDate checkDate, LocalDate checkOutDate, RoomType roomType);

    List<RoomType> getAllRoomTypes();

    Response searchRoom(String input);
}
