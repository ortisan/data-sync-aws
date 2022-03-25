package com.ortiz.app.repository;

import com.ortiz.app.domains.Post;

import java.util.UUID;

public interface IPostResilienceRepository extends IPostRepository {
    Post fallbackSave(Post post, Throwable exc);

    Post fallbackGetById(UUID id, Throwable exc);
}
