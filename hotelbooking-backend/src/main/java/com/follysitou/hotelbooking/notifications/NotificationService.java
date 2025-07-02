package com.follysitou.hotelbooking.notifications;

import com.follysitou.hotelbooking.dtos.NotificationDto;

public interface NotificationService {

    void sendEmail(NotificationDto notificationDto);

    void sendSms();

    void sendWhatsapp();
}
