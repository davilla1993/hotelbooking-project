package com.follysitou.hotelbooking.services.impl;

import com.follysitou.hotelbooking.dtos.BookingDto;
import com.follysitou.hotelbooking.dtos.NotificationDto;
import com.follysitou.hotelbooking.dtos.Response;
import com.follysitou.hotelbooking.entities.Booking;
import com.follysitou.hotelbooking.entities.Room;
import com.follysitou.hotelbooking.entities.User;
import com.follysitou.hotelbooking.enums.BookingStatus;
import com.follysitou.hotelbooking.enums.PaymentStatus;
import com.follysitou.hotelbooking.exceptions.InvalidBookingStateAndDateException;
import com.follysitou.hotelbooking.exceptions.NotFoundException;
import com.follysitou.hotelbooking.notifications.NotificationService;
import com.follysitou.hotelbooking.repositories.BookingRepository;
import com.follysitou.hotelbooking.repositories.RoomRepository;
import com.follysitou.hotelbooking.services.BookingCodeGenerator;
import com.follysitou.hotelbooking.services.BookingService;
import com.follysitou.hotelbooking.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final RoomRepository roomRepository;

    private final NotificationService notificationService;

    private final ModelMapper modelMapper;

    private final UserService userService;

    private final BookingCodeGenerator bookingCodeGenerator;

    @Value("${booking.payment.link}")
    private String paymentLink;

    @Override
    public Response getAllBookings() {
        List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<BookingDto> bookingDtoList = modelMapper.map(bookingList, new TypeToken<List<BookingDto>>() {}.getType());

        for(BookingDto bookingDto: bookingDtoList) {
            bookingDto.setUser(null);
            bookingDto.setRoom(null);
        }
        return Response.builder()
                .status(200)
                .message("success")
                .bookings(bookingDtoList)
                .build();
    }

    @Override
    public Response createBooking(BookingDto bookingDto) {

        User currentUser = userService.getCurrentLoggedInUser();

        Room room = roomRepository.findById(bookingDto.getRoomId())
                .orElseThrow(() -> new NotFoundException("Room not found"));

        // VALIDATION: Ensure check in date is not before today
        if(bookingDto.getCheckInDate().isBefore(LocalDate.now())) {
            throw new InvalidBookingStateAndDateException("Check in date cannot be before today");
        }
        // VALIDATION: Checkout date should not be before check in date
        if(bookingDto.getCheckOutDate().isBefore(bookingDto.getCheckInDate())) {
            throw new InvalidBookingStateAndDateException("Check Out date cannot be before check in date");
        }
        // VALIDATION: Check in date cannot be same as checkout date
        if(bookingDto.getCheckInDate().isEqual(bookingDto.getCheckOutDate())) {
            throw new InvalidBookingStateAndDateException("Check In date cannot be equal to check Out date");
        }

        // VALID ROOM AVAILABILITY
        boolean isAvailable = bookingRepository.isRoomAvailable(room.getId(), bookingDto.getCheckInDate(),
                                        bookingDto.getCheckOutDate());
        if(!isAvailable) {
            throw new InvalidBookingStateAndDateException("Room is not available to be booked");
        }
        BigDecimal totalPrice = calculateTotalPrice(room, bookingDto);
        String bookingReference = bookingCodeGenerator.generateBookingReference();

        // Create And Save to database
        Booking booking = new Booking();
        booking.setUser(currentUser);
        booking.setRoom(room);
        booking.setCheckInDate(bookingDto.getCheckInDate());
        booking.setCheckOutDate(bookingDto.getCheckOutDate());
        booking.setTotalPrice(totalPrice);
        booking.setBookingReference(bookingReference);
        booking.setBookingStatus(BookingStatus.BOOKED);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setCreatedAt(LocalDate.now());

        bookingRepository.save(booking);

        String paymentLink = this.paymentLink + bookingReference + "/" + totalPrice;
        log.info("BOOKING SUCCESSFULLY PAYMENT LINK IS {} ", paymentLink);

        // Send email to user via mail
        NotificationDto notificationDto = NotificationDto.builder()
                .recipient(currentUser.getEmail())
                .subject("BOOKING CONFIRMATION")
                .body(String.format("Your booking has been created successfully. \n Please, proceed with your payment using " +
                        "payment link below \n%s", paymentLink))
                .bookingReference(bookingReference)
                .build();

        notificationService.sendEmail(notificationDto);

        return Response.builder()
                .status(200)
                .message("Booking is successful")
                .booking(bookingDto)
                .build();

    }

    @Override
    public Response findBookingByReferenceNo(String bookingReference) {
        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);

        return Response.builder()
                .status(200)
                .message("success")
                .booking(bookingDto)
                .build();
    }

    @Override
    public Response updateBooking(BookingDto bookingDto) {
        if(bookingDto.getId() == null) throw new NotFoundException("Booking Id is required");

        Booking existingBooking = bookingRepository.findById(bookingDto.getId())
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if(bookingDto.getBookingStatus() != null) {
            existingBooking.setBookingStatus(bookingDto.getBookingStatus());
        }

        if(bookingDto.getPaymentStatus() != null) {
            existingBooking.setPaymentStatus(bookingDto.getPaymentStatus());
        }
        bookingRepository.save(existingBooking);

        return Response.builder()
                .status(200)
                .message("Booking updated successfully")
                .build();
    }

    private BigDecimal calculateTotalPrice(Room room, BookingDto bookingDto) {
        BigDecimal pricePerNight = room.getPricePerNight();
        long days = ChronoUnit.DAYS.between(bookingDto.getCheckInDate(), bookingDto.getCheckOutDate());

        return pricePerNight.multiply(BigDecimal.valueOf(days));
    }
}
