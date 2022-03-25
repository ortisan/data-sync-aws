package com.ortiz.app.web;

import com.ortiz.app.domains.Post;
import com.ortiz.app.repository.IPostRepository;
import com.ortiz.app.repository.IPostResilienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class Controller {

    @Autowired
    @Qualifier("postFacadeRepository")
    private IPostRepository postFacadeRepository;

    @GetMapping("/posts/{id}")
    public Post getById(@PathVariable UUID id) {
        return postFacadeRepository.getById(id);
    }

    @PostMapping("/posts")
    public Post post(@RequestBody Post post) throws InterruptedException {
        Post postPersisted = postFacadeRepository.save(post);
        return postPersisted;
    }
}
