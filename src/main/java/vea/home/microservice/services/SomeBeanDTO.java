package vea.home.microservice.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"field3"})
public record SomeBeanDTO(String field1, String field2, String field3) {
}
