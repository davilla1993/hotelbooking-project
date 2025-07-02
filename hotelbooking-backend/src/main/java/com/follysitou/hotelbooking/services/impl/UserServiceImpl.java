package com.follysitou.hotelbooking.services.impl;

import com.follysitou.hotelbooking.dtos.*;
import com.follysitou.hotelbooking.entities.Booking;
import com.follysitou.hotelbooking.entities.User;
import com.follysitou.hotelbooking.enums.UserRole;
import com.follysitou.hotelbooking.exceptions.InvalidCredentialException;
import com.follysitou.hotelbooking.exceptions.NotFoundException;
import com.follysitou.hotelbooking.repositories.BookingRepository;
import com.follysitou.hotelbooking.repositories.UserRepository;
import com.follysitou.hotelbooking.security.JwtUtils;
import com.follysitou.hotelbooking.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;
    private final BookingRepository bookingRepository;

    @Value("${jwt.access-token.expiration-minutes}")
    private String tokenExpirationTime;

    @Override
    public Response registerUser(RegistrationRequest registrationRequest) {

        UserRole role = UserRole.CUSTOMER;

        if(registrationRequest.getRole() != null) {
            role = registrationRequest.getRole();
        }

        User userToSave = User.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .phoneNumber(registrationRequest.getPhoneNumber())
                .role(role)
                .active(true)
                .build();

        userRepository.save(userToSave);

        return Response.builder()
                .status(200)
                .message("User registered successfully")
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not found"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialException("Password does not match");
        }

        String token = jwtUtils.generateToken(user.getEmail());

        return Response.builder()
                .status(200)
                .message("User logged in successfully")
                .role(user.getRole())
                .token(token)
                .active(user.isActive())
                .expirationTime(tokenExpirationTime + "min")
                .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<UserDto> userDtoList = modelMapper.map(users, new TypeToken<List<UserDto>>(){}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .users(userDtoList)
                .build();
    }

    @Override
    public Response getOwnAccountDetails() {

        log.info("INSIDE getOwnAccountDetails()");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email not found"));

        UserDto userDto = modelMapper.map(user, UserDto.class);

        return Response.builder()
                .status(200)
                .message("success")
                .user(userDto)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {

        log.info("INSIDE getCurrentLoggedInUser()");

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

       return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

    }

    @Override
    public Response updateOwnAccount(UserDto userDto) {

        log.info("INSIDE updateOwnAccount()");
        User existingUser = getCurrentLoggedInUser();

        if(userDto.getEmail() != null) existingUser.setEmail(userDto.getEmail());
        if(userDto.getFirstName() != null) existingUser.setFirstName(userDto.getFirstName());
        if(userDto.getLastName() != null) existingUser.setLastName(userDto.getLastName());
        if(userDto.getPhoneNumber() != null) existingUser.setPhoneNumber(userDto.getPhoneNumber());

        if(userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        userRepository.save(existingUser);

        return Response.builder()
                .status(200)
                .message("User updated successfully")
                .build();
    }


    @Override
    public Response getMyBookingHistory() {

        log.info("INSIDE getMyBookingHistory()");
        User currentUser = getCurrentLoggedInUser();

        List<Booking> bookingList = bookingRepository.findByUserId(currentUser.getId());

        List<BookingDto> bookingDtoList = modelMapper
                .map(bookingList, new TypeToken<List<BookingDto>>(){}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .bookings(bookingDtoList)
                .build();
    }

    @Override
    public Response deleteOwnAccount() {

        log.info("INSIDE deleteOwnAccount()");

        User existingUser = getCurrentLoggedInUser();
        userRepository.delete(existingUser);

        return Response.builder()
                .status(200)
                .message("User deleted successfully")
                .build();
    }
}
