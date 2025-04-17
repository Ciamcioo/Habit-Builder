package io.github.ciamcioo.habit_builder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.ciamcioo.habit_builder.exception.ProcessingException;
import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.json.JsonMergePatch;

import java.io.StringReader;

@Component
class MergePatchHelper {

    private final ObjectMapper objectMapper;

    @Autowired
    MergePatchHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    <T> T mergePatch(JsonMergePatch patch, T targetEntity, Class<T> entityClass) {
        validateUpdatedEntity(targetEntity, entityClass);
        String targetEntityAsString = convertTargetToString(targetEntity);

        try(JsonReader jsonReader = Json.createReader(new StringReader(targetEntityAsString))) {
            JsonValue targetJsonValue = jsonReader.readValue();
            JsonValue updatedTargetJsonValue = patch.apply(targetJsonValue);
            String updateTargetEntityAsString = convertTargetToString(updatedTargetJsonValue);
            return objectMapper.readValue(updateTargetEntityAsString, entityClass);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid patch object for update!");
        }
    }

    private <T> void validateUpdatedEntity(T entity, Class<T> entityClass) {
        if (entity == null || entityClass == null) {
            throw new IllegalArgumentException("Entity or entity class cannot be null value!");
        }

        if (!entityClass.isInstance(entity)) {
            throw new IllegalArgumentException("Target class should represent the class of entity object");
        }
    }

    private <T> String convertTargetToString(T target) throws ProcessingException {
        try {
            return objectMapper.writeValueAsString(target);
        } catch (JsonProcessingException e) {
            throw new ProcessingException("Target cannot be processed to String!");
        }
    }

}
