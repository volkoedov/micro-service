package vea.home.microservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vea.home.microservice.services.SomeBeanDTO;

@RestController
public class FilteringController {

    @GetMapping("/filtering")
    public SomeBeanDTO retrieveSomeBean() {
        return new SomeBeanDTO("value1", "value2", "value3");
    }
}
