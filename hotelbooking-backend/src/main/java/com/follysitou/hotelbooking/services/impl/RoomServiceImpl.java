package com.follysitou.hotelbooking.services.impl;

import com.follysitou.hotelbooking.dtos.Response;
import com.follysitou.hotelbooking.dtos.RoomDto;
import com.follysitou.hotelbooking.entities.Room;
import com.follysitou.hotelbooking.enums.RoomType;
import com.follysitou.hotelbooking.exceptions.InvalidBookingStateAndDateException;
import com.follysitou.hotelbooking.exceptions.NotFoundException;
import com.follysitou.hotelbooking.repositories.RoomRepository;
import com.follysitou.hotelbooking.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    // private final static String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/uploads/";

    private final static String IMAGE_DIRECTORY_FRONTEND = "D:/Fullstack-projects/hotelbooking-project/hotelbooking-frontend/public/rooms/";


    @Override
    public Response addRoom(RoomDto roomDto, MultipartFile imageFile) {

        Room roomToSave = modelMapper.map(roomDto, Room.class);
        if(imageFile != null) {
       //     String imagePath = saveImage(imageFile);
            String imagePath = saveImageToFrontend(imageFile);
            roomToSave.setImageUrl(imagePath);
        }
        roomRepository.save(roomToSave);

        return Response.builder()
                .status(200)
                .message("Room created successfully")
                .build();
    }

    @Override
    public Response updateRoom(RoomDto roomDto, MultipartFile imageFile) {
        Room existingRoom = roomRepository.findById(roomDto.getId())
                .orElseThrow(() -> new NotFoundException("Room does not exists"));

        if(imageFile != null && !imageFile.isEmpty()) {
          //  String imagePath = saveImage(imageFile);
            String imagePath = saveImageToFrontend(imageFile);
            existingRoom.setImageUrl(imagePath);
        }

        if(roomDto.getRoomNumber() != null && roomDto.getRoomNumber() >= 0) {
            existingRoom.setRoomNumber(roomDto.getRoomNumber());
        }

        if(roomDto.getPricePerNight() != null && roomDto.getPricePerNight().compareTo(BigDecimal.ZERO) >= 0) {
            existingRoom.setPricePerNight(roomDto.getPricePerNight());
        }

        if(roomDto.getCapacity() != null && roomDto.getCapacity() > 0) {
            existingRoom.setCapacity(roomDto.getCapacity());
        }

        if(roomDto.getType() != null ) {
            existingRoom.setType(roomDto.getType());
        }

        if(roomDto.getDescription() != null) {
            existingRoom.setDescription(roomDto.getDescription());
        }

        roomRepository.save(existingRoom);

        return Response.builder()
                .status(200)
                .message("Room updated successfully")
                .build();
    }

    @Override
    public Response getAllRooms() {
        List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<RoomDto> roomDtoList = modelMapper.map(roomList, new TypeToken<List<RoomDto>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .rooms(roomDtoList)
                .build();
    }

    @Override
    public Response getRoomById(Long id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        RoomDto roomDto = modelMapper.map(room, RoomDto.class);

        return Response.builder()
                .status(200)
                .message("success")
                .room(roomDto)
                .build();
    }

    @Override
    public Response deleteRoom(Long id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        roomRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Room deleted successfully")
                .build();
    }

    @Override
    public Response getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType) {

        // Validation: Ensure check In Date is not before today
        if(checkInDate.isBefore(LocalDate.now())) {
            throw new InvalidBookingStateAndDateException("Check in date cannot be before today");
        }

        // Validation: Ensure check Out Date is not before check In Date
        if(checkOutDate.isBefore(checkInDate)) {
            throw new InvalidBookingStateAndDateException("Check Out date must be before check in date");
        }

        // Validation: Ensure check In Date is not same as check Out Date
        if(checkInDate.isEqual(checkOutDate)) {
            throw new InvalidBookingStateAndDateException("Check In date cannot be equal to check Out date");
        }

        List<Room> roomList = roomRepository.findAvailableRooms(checkInDate, checkOutDate, roomType);
        List<RoomDto> roomDtoList = modelMapper.map(roomList, new TypeToken<List<RoomDto>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .rooms(roomDtoList)
                .build();
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        return Arrays.asList(RoomType.values());
    }

    @Override
    public Response searchRoom(String input) {

        List<Room> roomList = roomRepository.searchRooms(input);
        List<RoomDto> roomDtoList = modelMapper.map(roomList, new TypeToken<List<RoomDto>>(){}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .rooms(roomDtoList)
                .build();
    }

    /*private String saveImage(MultipartFile imageFile) {
        if(!Objects.requireNonNull(imageFile.getContentType()).startsWith("image/")){
            throw new IllegalArgumentException("Only image file is allowed");
        }
        // create directory if it don't exist
        File directory = new File(IMAGE_DIRECTORY);

        if(!directory.exists()) {
            directory.mkdir();
        }

        // Generate unique file name for the image
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        String imagePath = IMAGE_DIRECTORY + uniqueFileName;

        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
            return imagePath;
    }*/

    private String saveImageToFrontend(MultipartFile imageFile) {
        if(!Objects.requireNonNull(imageFile.getContentType()).startsWith("image/")){
            throw new IllegalArgumentException("Only image file is allowed");
        }
        // create directory if it don't exist
        File directory = new File(IMAGE_DIRECTORY_FRONTEND);

        if(!directory.exists()) {
            directory.mkdir();
        }

        // Generate unique file name for the image
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        String imagePath = IMAGE_DIRECTORY_FRONTEND + uniqueFileName;

        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return "/rooms/"+uniqueFileName;
    }
}
