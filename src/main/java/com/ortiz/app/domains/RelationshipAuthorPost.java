package com.ortiz.app.domains;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.ortiz.app.repository.dynamo.converters.UUIDDynamoConverter;
import lombok.Data;

import java.util.UUID;

@Data
@DynamoDBTable(tableName = "RelationshipAuthorPost")
public class RelationshipAuthorPost {
    @DynamoDBTypeConverted(converter = UUIDDynamoConverter.class)
    @DynamoDBHashKey(attributeName = "postId")
    private UUID postId;
    @DynamoDBTypeConverted(converter = UUIDDynamoConverter.class)
    @DynamoDBAttribute(attributeName = "authorId")
    private UUID authorId;
    @DynamoDBAttribute(attributeName = "role")
    private String role;
}
