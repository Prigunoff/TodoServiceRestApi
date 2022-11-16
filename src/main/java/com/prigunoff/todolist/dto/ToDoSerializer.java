package com.prigunoff.todolist.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.prigunoff.todolist.model.ToDo;


import java.io.IOException;

public class ToDoSerializer extends StdSerializer<ToDo> {

    public ToDoSerializer() {
        this(null);
    }

    public ToDoSerializer(Class<ToDo> t) {
        super(t);
    }


    @Override
    public void serialize(ToDo toDo, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeNumberField("id", toDo.getId());
        jsonGenerator.writeStringField("title", toDo.getTitle());
        jsonGenerator.writeNumberField("owner_id", toDo.getOwner().getId());
        jsonGenerator.writeStringField("owner_name", toDo.getOwner().getFirstName());
        jsonGenerator.writeEndObject();
    }
}
