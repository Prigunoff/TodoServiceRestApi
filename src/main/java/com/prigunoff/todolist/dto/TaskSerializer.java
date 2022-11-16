package com.prigunoff.todolist.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.prigunoff.todolist.model.Task;

import java.io.IOException;

    public class TaskSerializer extends StdSerializer<Task> {

        public TaskSerializer() {
            this(null);
        }

        public TaskSerializer(Class<Task> t) {
            super(t);
        }


        @Override
        public void serialize(Task task, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();

            jsonGenerator.writeNumberField("id", task.getId());
            jsonGenerator.writeStringField("firstName", task.getName());
            jsonGenerator.writeNumberField("todo_id", task.getTodo().getId());
            jsonGenerator.writeStringField("state", task.getPriority().name());

            jsonGenerator.writeEndObject();

        }



    }
