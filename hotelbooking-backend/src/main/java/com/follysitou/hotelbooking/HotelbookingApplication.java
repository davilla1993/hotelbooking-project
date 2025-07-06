package com.follysitou.hotelbooking;

import com.follysitou.hotelbooking.dtos.NotificationDto;
import com.follysitou.hotelbooking.notifications.NotificationService;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class HotelbookingApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		System.setProperty("STRIPE_PUBLIC_KEY", dotenv.get("STRIPE_PUBLIC_KEY"));
		System.setProperty("STRIPE_SECRET_KEY", dotenv.get("STRIPE_SECRET_KEY"));

		SpringApplication.run(HotelbookingApplication.class, args);
	}
}
