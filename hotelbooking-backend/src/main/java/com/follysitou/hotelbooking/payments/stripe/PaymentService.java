package com.follysitou.hotelbooking.payments.stripe;

import com.follysitou.hotelbooking.dtos.NotificationDto;
import com.follysitou.hotelbooking.dtos.Response;
import com.follysitou.hotelbooking.entities.Booking;
import com.follysitou.hotelbooking.entities.PaymentEntity;
import com.follysitou.hotelbooking.enums.NotificationType;
import com.follysitou.hotelbooking.enums.PaymentGateway;
import com.follysitou.hotelbooking.enums.PaymentStatus;
import com.follysitou.hotelbooking.exceptions.NotFoundException;
import com.follysitou.hotelbooking.notifications.NotificationService;
import com.follysitou.hotelbooking.repositories.BookingRepository;
import com.follysitou.hotelbooking.repositories.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationService emailService;

    @Value("${stripe.api.secret.key}")
    private String secretKey;

    //We are initializing a payment, this will return a unique transactionId called secret which
    // will be used to process,complete the payment in the frontend
    public Response initializePayment(PaymentRequest paymentRequest){

        log.info("Inside createPaymentIntent()");
        Stripe.apiKey = secretKey;

        String bookingReference = paymentRequest.getBookingReference();

        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new NotFoundException("Booking Not Found"));

        if (booking.getPaymentStatus() == PaymentStatus.COMPLETED){
            throw new NotFoundException("Payment Already Made For This Booking");
        }

        if (booking.getTotalPrice().compareTo(paymentRequest.getAmount()) != 0){
            throw new NotFoundException("Payment Amount Does Not Tally. Please Contact Out Customer Support Agent");
        }

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(paymentRequest.getAmount().multiply(BigDecimal.valueOf(100)).longValue()) // converting to cent
                    .setCurrency("usd")
                    .putMetadata("bookingReference", bookingReference)
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);
            String  uniqueTransactionId = intent.getClientSecret();

            return Response.builder()
                    .status(200)
                    .message("success")
                    .transactionId(uniqueTransactionId)
                    .build();

        }catch (Exception e){
            throw new RuntimeException("Error Creating payment unique transaction id");
        }
    }

    public void updatePaymentBooking(PaymentRequest paymentRequest){
        log.info("inside updatePaymentBooking()");

        String bookingReference = paymentRequest.getBookingReference();

        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new NotFoundException("Booking Not Found"));

        PaymentEntity payment = new PaymentEntity();
        payment.setPaymentGateway(PaymentGateway.STRIPE);
        payment.setAmount(paymentRequest.getAmount());
        payment.setTransactionId(paymentRequest.getTransactionId());
        payment.setPaymentStatus(paymentRequest.isSuccess() ? PaymentStatus.COMPLETED : PaymentStatus.FAILED);
        payment.setPaymentDate(LocalDate.from(LocalDateTime.now()));
        payment.setBookingReference(bookingReference);
        payment.setUser(booking.getUser());

        if (!paymentRequest.isSuccess()){
            payment.setFailureReason(paymentRequest.getFailureReason());
        }

        paymentRepository.save(payment); //save to our payment table

        //create and send notification via email
        NotificationDto notificationDTO = NotificationDto.builder()
                .recipient(booking.getUser().getEmail())
                .type(NotificationType.EMAIL)
                .bookingReference(bookingReference)
                .build();

        if (paymentRequest.isSuccess()) {

            booking.setPaymentStatus(PaymentStatus.COMPLETED);
            bookingRepository.save(booking); //Update Booking Status To SUCCESFUL

            notificationDTO.setSubject("BOOKING PAYMENT SUCCESSFUL");
            notificationDTO.setBody("Congratulations!! Your payment for booking with reference: " + bookingReference + "is successful");
            emailService.sendEmail(notificationDTO);

        }else{

            booking.setPaymentStatus(PaymentStatus.FAILED);
            bookingRepository.save(booking); //UPDATE THE BOOKING

            notificationDTO.setSubject("BOOKING PAYMENT FAILED");
            notificationDTO.setBody("Your Payment for booking with reference: " + bookingReference + " failed with reason: " + paymentRequest.getFailureReason());
            emailService.sendEmail(notificationDTO);

        }
    }
}
