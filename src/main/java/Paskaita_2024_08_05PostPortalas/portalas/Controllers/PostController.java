package Paskaita_2024_08_05PostPortalas.portalas.Controllers;


import Paskaita_2024_08_05PostPortalas.portalas.Models.Posts;
import Paskaita_2024_08_05PostPortalas.portalas.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500","http://localhost:7778/"})
@RestController
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<String> createpost(@RequestBody Posts posts, @RequestHeader("PaymentCode") String paymentCode, @RequestHeader("PaymentID") int paymentID) {

        String response = postService.createPost(posts,paymentID,paymentCode);
        HttpStatus status = checkHttpStatus(response);

        return new ResponseEntity<>(response, status);

    }


    @GetMapping("/posts")
    public ResponseEntity<List<Posts>> getPosts(@RequestParam int limit, int offset) {

        List<Posts> posts = postService.getPosts(limit, offset);

        if(posts.isEmpty()) return new ResponseEntity<>(posts, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    private HttpStatus checkHttpStatus(String response){

        switch (response){
            case "Database connection failed":
                return HttpStatus.INTERNAL_SERVER_ERROR;
            case "Invalid data", "Invalid phone format":
                return HttpStatus.BAD_REQUEST;
            case "Payment needed":
                return HttpStatus.PAYMENT_REQUIRED;
            case "Post was successfully added":
                return HttpStatus.OK;
            default:
                return HttpStatus.NOT_IMPLEMENTED;
        }

    }




}
