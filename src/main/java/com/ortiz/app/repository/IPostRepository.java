package com.ortiz.app.repository;

import com.ortiz.app.domains.Post;

import java.util.UUID;

public interface IPostRepository {
    Post save(Post post);

    Post getById(UUID id);
}
