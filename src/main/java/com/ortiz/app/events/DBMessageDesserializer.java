package com.ortiz.app.events;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.ortiz.app.CustomException;

import java.io.IOException;

public class DBMessageDesserializer extends StdDeserializer<DBMessage> {

    public DBMessageDesserializer() {
        this(null);
    }

    protected DBMessageDesserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public DBMessage deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        try {
            String value = p.getText();
            var mapper = new ObjectMapper();
            mapper.registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule()); // new module, NOT JSR310Module

            return mapper.readValue(value, DBMessage.class);
        } catch (Exception exc) {
            throw new CustomException("Error to deserialize message.", exc);
        }
    }
}
