package com.follysitou.hotelbooking.payments.stripe;

import com.follysitou.hotelbooking.dtos.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<Response> initializePayment(@RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(paymentService.initializePayment(paymentRequest));
    }

    @PutMapping("/update")
    public void updatePaymentBooking(@RequestBody PaymentRequest paymentRequest) {
        paymentService.updatePaymentBooking(paymentRequest);
    }
}
