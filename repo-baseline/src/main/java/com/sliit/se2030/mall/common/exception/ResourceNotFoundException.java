package com.sliit.se2030.mall.common.exception;

/**
 * Thrown when a lookup by id (or similar) finds nothing.
 * e.g. productRepository.findById(id) returns empty.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
