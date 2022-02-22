package vea.home.microservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vea.home.microservice.services.Name;
import vea.home.microservice.services.PersonV1;
import vea.home.microservice.services.PersonV2;

@RestController
public class PersonVersioningController {

    @GetMapping("/v1/person")
    public PersonV1 getPersonV1(){
        return new PersonV1("Eugen Volkoedov");
    }

    @GetMapping("/v2/person")
    public PersonV2 getPersonV2(){
        return new PersonV2(new Name("Eugen", "Volkoedov"));
    }

}
