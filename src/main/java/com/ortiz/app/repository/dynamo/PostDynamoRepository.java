package com.ortiz.app.repository.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.ortiz.app.domains.Author;
import com.ortiz.app.domains.Post;
import com.ortiz.app.domains.PostAuthor;
import com.ortiz.app.domains.RelationshipAuthorPost;
import com.ortiz.app.repository.IPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository("postDynamoRepository")
public class PostDynamoRepository implements IPostRepository {

    @Autowired
    private AmazonDynamoDB dynamoDBClient;

    @Override
    public Post save(Post post) {
        post.setId(UUID.randomUUID());

        Set<Author> authorsExamples = post.getAuthors().stream()
                .filter(author -> author.getId() != null).map(author -> {
                            Author authorExample = new Author();
                            authorExample.setId(author.getId());
                            return authorExample;
                        }
                ).collect(Collectors.toSet());

        if (!authorsExamples.isEmpty()) {
            DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient, DynamoDBMapperConfig.builder()
                    .withConversionSchema(ConversionSchemas.V2).build());
            Map<String, List<Object>> authorsLoaded = mapper.batchLoad(authorsExamples);
            if (authorsLoaded.isEmpty()) {
                throw new RuntimeException("Authors not found");
            }
        }

        List<RelationshipAuthorPost> relationshipAuthorPosts = new ArrayList<>();

        for (PostAuthor author : post.getAuthors()) {
            for (String role : author.getRoles()) {
                RelationshipAuthorPost relationshipAuthorPost = new RelationshipAuthorPost();
                relationshipAuthorPost.setPostId(post.getId());
                relationshipAuthorPost.setAuthorId(author.getId());
                relationshipAuthorPost.setRole(role);
                relationshipAuthorPosts.add(relationshipAuthorPost);
            }
        }

        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient, DynamoDBMapperConfig.builder()
                .withConversionSchema(ConversionSchemas.V2).build());

        TransactionWriteRequest transactionWriteRequest = new TransactionWriteRequest();
        transactionWriteRequest.addPut(post);
        for (RelationshipAuthorPost relationship : relationshipAuthorPosts) {
            transactionWriteRequest.addPut(relationship);
        }

        mapper.transactionWrite(transactionWriteRequest);

        return post;
    }

    @Override
    public Post getById(UUID id) {

        RelationshipAuthorPost relationshipExample = new RelationshipAuthorPost();
        relationshipExample.setPostId(id);

        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);

        // Load post
        Post post = mapper.load(Post.class, id);

        // Search for relationships
        DynamoDBQueryExpression<RelationshipAuthorPost> queryExpression = new DynamoDBQueryExpression<RelationshipAuthorPost>()
                .withHashKeyValues(relationshipExample);

        List<RelationshipAuthorPost> relationships = mapper.query(RelationshipAuthorPost.class, queryExpression);

        Map<UUID, Set<String>> authorRolesByPostId = relationships.stream()
                .collect(Collectors.groupingBy(RelationshipAuthorPost::getAuthorId,
                        Collectors.mapping(RelationshipAuthorPost::getRole, Collectors.toSet())));
        post.getAuthors().stream().forEach(author -> author.setRoles(authorRolesByPostId.get(author.getId())));
        return post;
    }

}
