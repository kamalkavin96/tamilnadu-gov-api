package com.kamalkavin96.tamilnadu_gov_api.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: %s",
                resourceName, fieldName, fieldValue));
    }
}
