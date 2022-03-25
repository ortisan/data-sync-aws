package com.ortiz.app.domains;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.ortiz.app.repository.dynamo.converters.UUIDDynamoConverter;
import lombok.Data;

import java.util.UUID;

@Data
@DynamoDBTable(tableName = "Author")
public class Author {
    @DynamoDBTypeConverted(converter = UUIDDynamoConverter.class)
    @DynamoDBHashKey(attributeName = "id")
    private UUID id;
    @DynamoDBAttribute(attributeName = "first_name")
    private String firstName;
    @DynamoDBAttribute(attributeName = "last_name")
    private String lastName;
}
