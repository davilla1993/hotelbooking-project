package com.follysitou.hotelbooking;

import com.follysitou.hotelbooking.dtos.NotificationDto;
import com.follysitou.hotelbooking.notifications.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class HotelbookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelbookingApplication.class, args);
	}
}
