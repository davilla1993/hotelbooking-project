package com.follysitou.hotelbooking.notifications;

import com.follysitou.hotelbooking.dtos.NotificationDto;
import com.follysitou.hotelbooking.entities.Notification;
import com.follysitou.hotelbooking.enums.NotificationType;
import com.follysitou.hotelbooking.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private final JavaMailSender javaMailSender;
    private final NotificationRepository notificationRepository;

    @Override
    @Async
    public void sendEmail(NotificationDto notificationDto) {
        log.info("Sending mail...");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(notificationDto.getRecipient());
        simpleMailMessage.setSubject(notificationDto.getSubject());
        simpleMailMessage.setText(notificationDto.getBody());

        javaMailSender.send(simpleMailMessage);

        // Save to database
        Notification notificationToSave = Notification
                .builder()
                .recipient(notificationDto.getRecipient())
                .subject(notificationDto.getSubject())
                .body(notificationDto.getBody())
                .bookingReference(notificationDto.getBookingReference())
                .type(NotificationType.EMAIL)
                .build();

        notificationRepository.save(notificationToSave);
    }

    @Override
    public void sendSms() {

    }

    @Override
    public void sendWhatsapp() {

    }
}
