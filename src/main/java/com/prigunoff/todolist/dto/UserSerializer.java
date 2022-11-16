package com.prigunoff.todolist.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.prigunoff.todolist.model.User;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {
    public UserSerializer(Class<User> t) {
        super(t);
    }
    public UserSerializer() {
        this(null);
    }


    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", user.getId());
        jsonGenerator.writeStringField("firstName", user.getFirstName());
        jsonGenerator.writeStringField("email", user.getEmail());
        jsonGenerator.writeEndObject();
    }
}
