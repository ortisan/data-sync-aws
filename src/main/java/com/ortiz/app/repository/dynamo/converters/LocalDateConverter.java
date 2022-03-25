package com.ortiz.app.repository.dynamo.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class LocalDateConverter implements DynamoDBTypeConverter<String, LocalDate> {

    @Override
    public String convert(LocalDate object) {
        return object.format(ISO_LOCAL_DATE);
    }

    @Override
    public LocalDate unconvert(String object) {
        return LocalDate.parse(object, ISO_LOCAL_DATE);
    }
}
