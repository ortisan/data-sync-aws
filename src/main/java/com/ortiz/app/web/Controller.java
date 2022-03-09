package com.ortiz.app.web;

import com.ortiz.app.domains.Post;
import com.ortiz.app.persistence.S3Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class Controller {

    @Autowired
    private S3Repository repository;

    @GetMapping("/posts/{id}")
    public Post getPostById(@PathVariable UUID id) {
        Post post = repository.getById(id);
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found.");
        }
        return post;
    }

    @PostMapping("/posts")
    public Post createPost(@RequestBody Post post) {
        post.setId(UUID.randomUUID());
        repository.save(post);
        return post;
    }

    @PutMapping("/posts/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post post) {
        post.setId(UUID.randomUUID());
        repository.save(post);
        return post;
    }

//    @PatchMapping("/posts/{id}")
//    public Post updatePartially(@PathVariable Long id, @RequestBody Post post) {
//        return updatePost(id, post);
//    }
//
//    @DeleteMapping("/posts/{id}")
//    public void deletePost(@PathVariable Long id) {
//        Post post = posts.get(id);
//        if (post == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found.");
//        }
//        posts.remove(id);
//    }


}
