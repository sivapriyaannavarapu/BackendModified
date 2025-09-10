package com.application.service;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IntArrayConverter implements AttributeConverter<int[], Object> {

    @Override
    public Object convertToDatabaseColumn(int[] attribute) {
        // You can return the array directly, JPA/Hibernate will handle the conversion for PostgreSQL
        return attribute;
    }

    @Override
    public int[] convertToEntityAttribute(Object dbData) {
        // You can cast the object back to an int[]
        if (dbData instanceof int[]) {
            return (int[]) dbData;
        }
        return null;
    }
}