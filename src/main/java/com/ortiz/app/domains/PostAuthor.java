package com.ortiz.app.domains;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.ortiz.app.repository.dynamo.converters.UUIDDynamoConverter;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@DynamoDBDocument
public class PostAuthor {
    @DynamoDBTypeConverted(converter = UUIDDynamoConverter.class)
    @DynamoDBAttribute(attributeName = "id")
    private UUID id;
    @DynamoDBAttribute(attributeName = "roles")
    private Set<String> roles;
}
