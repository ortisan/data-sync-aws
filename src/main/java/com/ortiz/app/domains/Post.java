package com.ortiz.app.domains;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ortiz.app.repository.dynamo.converters.UUIDDynamoConverter;
import com.ortiz.app.repository.dynamo.converters.LocalDateConverter;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Data
@DynamoDBTable(tableName = "Post")
public class Post {
    @JsonProperty("id")
    @DynamoDBTypeConverted(converter = UUIDDynamoConverter.class)
    @DynamoDBHashKey(attributeName = "id")
    private UUID id;
    @JsonProperty("title")
    @DynamoDBAttribute(attributeName = "title")
    private String name;
    @JsonProperty("publish_date")
    @DynamoDBTypeConverted(converter = LocalDateConverter.class)
    @DynamoDBAttribute(attributeName = "publish_date")
    private LocalDate publishDate;
    @DynamoDBAttribute(attributeName = "authors")
    private List<PostAuthor> authors;
}
