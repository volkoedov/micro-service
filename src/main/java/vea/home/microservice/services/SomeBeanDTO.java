package vea.home.microservice.services;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record SomeBeanDTO(String field1, String field2, @JsonIgnore String field3) {
}
