package Paskaita_2024_08_05PostPortalas.portalas.Services;

import Paskaita_2024_08_05PostPortalas.portalas.Repositories.PostRepository;
import Paskaita_2024_08_05PostPortalas.portalas.Repositories.StripeRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class StripeService{

    @Value("${stripe.api.key}")
    private String stripeApiKey;
    @Autowired
    private StripeRepository stripeRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public PaymentIntent createPaymentIntent(Long amount, String currency) throws StripeException {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency(currency)
                        .build();

        return PaymentIntent.create(params);
    }

    public Session createCheckoutSession(Long amount, String currency) throws StripeException {
        UUID uuid = generateUID();
        UUID uuidSecret = generateUID();
        int payment_id = createPayment(uuid,uuidSecret);

        String successUrl = "http://localhost:8080/api/stripe/payment/" + uuid + "/" + uuidSecret + "/" + payment_id;
        String cancelUrl = "http://localhost:8080/api/stripe/payment/" + uuid + "/" + payment_id;

        Map<String, String> metadata = new HashMap<>();
        metadata.put("uuid", uuid.toString());
        metadata.put("id", payment_id + "");
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(currency)
                                .setUnitAmount(amount)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("One time post")
                                        .build())
                                .build())
                        .build())
                .putAllMetadata(metadata)
                .build();

        return Session.create(params);
    }

    public void createPaymentStatus(int id, String uuid, String uuidSecret){
        if (uuidSecret == null) stripeRepository.setPaymentStatusToFalse(id,uuid);
        else stripeRepository.setPaymentStatusToTrue(id,uuid,uuidSecret);
    }

    private int createPayment(UUID uuid,UUID uuidSecret){
        return stripeRepository.createPayment(uuid, uuidSecret);
    }


    private UUID generateUID(){
        return UUID.randomUUID();
    }

}