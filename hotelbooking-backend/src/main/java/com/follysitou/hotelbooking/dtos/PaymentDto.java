package com.follysitou.hotelbooking.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.follysitou.hotelbooking.entities.User;
import com.follysitou.hotelbooking.enums.PaymentGateway;
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
public class PaymentDto {

    private Long id;

    private BookingDto booking;

    private String transactionId;

    private BigDecimal amount;

    private PaymentGateway paymentMethod;

    private LocalDate paymentDate;

    private PaymentStatus status;

    private String bookingReference;

    private String failureReason;

    private String approvalLink; // paypal payment approval
}
