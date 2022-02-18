package vea.home.microservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vea.home.microservice.services.SomeBeanDTO;

import java.util.Arrays;
import java.util.List;

@RestController
public class FilteringController {

    @GetMapping("/filtering")
    public SomeBeanDTO retrieveSomeBean() {
        return new SomeBeanDTO("value1", "value2", "value3");
    }

    @GetMapping("/filtering-list")
    public List<SomeBeanDTO> retrieveListOfSomeBean() {
        return Arrays.asList(new SomeBeanDTO("value1", "value2", "value3"),new SomeBeanDTO("value12", "value22", "value32"));
    }
}
