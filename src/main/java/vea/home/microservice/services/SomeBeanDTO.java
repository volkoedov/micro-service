package vea.home.microservice.services;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("SomeBeanFilter")
public record SomeBeanDTO(String field1, String field2, String field3) {
}
