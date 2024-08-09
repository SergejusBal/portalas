package Paskaita_2024_08_05PostPortalas.portalas.Controllers;

import Paskaita_2024_08_05PostPortalas.portalas.Services.StripeService;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500","http://localhost:7778/"})
@RequestMapping("/api/stripe")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, Object> data) {
        try {
            Long amount = Long.parseLong(data.get("amount").toString());
            String currency = data.get("currency").toString();
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(amount, currency);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("clientSecret", paymentIntent.getClientSecret());

            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody Map<String, Object> data) {
        try {
            Long amount = Long.parseLong(data.get("amount").toString());
            String currency = data.get("currency").toString();

            Session session = stripeService.createCheckoutSession(amount, currency);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("id", session.getId());
            responseData.put("paymentCode", session.getMetadata().get("uuid"));
            responseData.put("paymentID", session.getMetadata().get("id"));

            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/payment/{uuid}/{uuidSecret}/{id}")
    public RedirectView redirect(@PathVariable String uuid, @PathVariable String uuidSecret,@PathVariable int id) {
        stripeService.createPaymentStatus(id,uuid,uuidSecret);
        return new RedirectView("http://127.0.0.1:5500/");
    }
    @GetMapping("payment/{uuid}/{id}")
    public RedirectView redirect(@PathVariable String uuid,@PathVariable int id) {
        stripeService.createPaymentStatus(id,uuid,null);
        return new RedirectView("http://127.0.0.1:5500/");
    }

}