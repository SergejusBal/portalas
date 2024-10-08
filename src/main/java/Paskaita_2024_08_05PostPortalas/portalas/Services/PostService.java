package Paskaita_2024_08_05PostPortalas.portalas.Services;


import Paskaita_2024_08_05PostPortalas.portalas.Models.Posts;
import Paskaita_2024_08_05PostPortalas.portalas.Repositories.PostRepository;
import Paskaita_2024_08_05PostPortalas.portalas.Repositories.StripeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private StripeRepository stripeRepository;

    public String createPost(Posts posts,int paymentID, String paymentCode){

        if(posts.getName() == null || posts.getContent() == null || posts.getContacts() == null) return "Invalid data";

        if(!phoneFormatCheck(posts.getContacts())) return "Invalid phone format";

        if(!stripeRepository.ckeckPaymentStatus(paymentID,paymentCode)) return "Payment needed";

        stripeRepository.setPaymentStatusToFalse(paymentID,paymentCode);

        return postRepository.createPost(posts);
    }

    public List<Posts> getPosts(int limit, int offset){
        return postRepository.getPosts(limit, offset);
    }

    private boolean phoneFormatCheck(String phoneNumber){

        String phoneNumberPattern = "\\+370\\d{8}";

        Pattern pattern = Pattern.compile(phoneNumberPattern);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }



}
