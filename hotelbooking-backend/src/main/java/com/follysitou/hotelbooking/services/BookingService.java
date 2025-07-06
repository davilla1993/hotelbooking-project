package com.follysitou.hotelbooking.services;

import com.follysitou.hotelbooking.dtos.BookingDto;
import com.follysitou.hotelbooking.dtos.Response;

public interface BookingService {

    Response getAllBookings();

    Response createBooking(BookingDto bookingDto);

    Response updateBooking(BookingDto bookingDto);

    Response findBookingByReferenceNo(String bookingReference);

}
