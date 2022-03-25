package com.ortiz.app.repository.dynamo.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.util.UUID;

public class UUIDDynamoConverter implements DynamoDBTypeConverter<String, UUID> {
    @Override
    public String convert(UUID uuid) {
        return uuid.toString();
    }
    @Override
    public UUID unconvert(String uuid) {
        return UUID.fromString(uuid);
    }
}
