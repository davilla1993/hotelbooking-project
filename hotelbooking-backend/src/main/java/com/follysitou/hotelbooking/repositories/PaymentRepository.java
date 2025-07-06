package com.follysitou.hotelbooking.repositories;

import com.follysitou.hotelbooking.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
