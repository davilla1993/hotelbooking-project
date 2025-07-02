package com.follysitou.hotelbooking.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.follysitou.hotelbooking.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    //generic
    private int status;
    private String message;

    //for login
    private String token;
    private UserRole role;
    private Boolean active;
    private String expirationTime;

    //user data
    private UserDto user;
    private List<UserDto> users;

    // booking data
    private BookingDto booking;
    private List<BookingDto> bookings;

    //room data
    private RoomDto room;
    private List<RoomDto> rooms;

    //room payments
    private String transactionId;
    private PaymentDto payment;
    private List<PaymentDto> payments;

    //room Notification
    private NotificationDto notification;
    private List<NotificationDto> notifications;

    private final LocalDateTime timestamp = LocalDateTime.now();

}
