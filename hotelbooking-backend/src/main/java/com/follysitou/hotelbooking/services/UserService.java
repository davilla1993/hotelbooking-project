package com.follysitou.hotelbooking.services;

import com.follysitou.hotelbooking.dtos.LoginRequest;
import com.follysitou.hotelbooking.dtos.RegistrationRequest;
import com.follysitou.hotelbooking.dtos.Response;
import com.follysitou.hotelbooking.dtos.UserDto;
import com.follysitou.hotelbooking.entities.User;

public interface UserService {

    Response registerUser(RegistrationRequest registrationRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    Response getOwnAccountDetails();

    User getCurrentLoggedInUser();

    Response updateOwnAccount(UserDto userDto);

    Response getMyBookingHistory();

    Response deleteOwnAccount();
}
