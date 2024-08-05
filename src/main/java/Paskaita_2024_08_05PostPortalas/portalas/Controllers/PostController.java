package Paskaita_2024_08_05PostPortalas.portalas.Controllers;


import Paskaita_2024_08_05PostPortalas.portalas.Models.Posts;
import Paskaita_2024_08_05PostPortalas.portalas.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500"})
@RestController
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }


    @PostMapping("/posts")
    public ResponseEntity<String> createpost(@RequestBody Posts posts) {

        String response = postService.createPost(posts);
        HttpStatus status = checkHttpStatus(response);

        if(status == HttpStatus.OK) return new ResponseEntity<>(response, status);
        else return new ResponseEntity<>(response, status);
    }


    @GetMapping("/posts")
    public ResponseEntity<List<Posts>> getPosts() {

        List<Posts> posts = postService.getPosts();

        if(posts.isEmpty()) return new ResponseEntity<>(posts, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(posts, HttpStatus.OK);
    }



    private HttpStatus checkHttpStatus(String response){

        switch (response){
            case "Database connection failed":
                return HttpStatus.INTERNAL_SERVER_ERROR;
            case "Invalid data", "Invalid phone format":
                return HttpStatus.BAD_REQUEST;
            default:
                return HttpStatus.OK;
        }

    }




}
