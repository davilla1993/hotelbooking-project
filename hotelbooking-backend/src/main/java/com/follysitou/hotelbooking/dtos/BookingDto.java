package com.follysitou.hotelbooking.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.follysitou.hotelbooking.entities.Room;
import com.follysitou.hotelbooking.entities.User;
import com.follysitou.hotelbooking.enums.BookingStatus;
import com.follysitou.hotelbooking.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDto {

    private Long id;

    private UserDto user;

    private RoomDto room;

    private Long roomId;

    private PaymentStatus paymentStatus;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private BigDecimal totalPrice;

    private String bookingReference;

    private BookingStatus bookingStatus;

    private LocalDate createdAt;


}
