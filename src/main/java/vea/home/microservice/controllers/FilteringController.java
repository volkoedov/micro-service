package vea.home.microservice.controllers;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vea.home.microservice.services.SomeBeanDTO;

import java.util.Arrays;
import java.util.List;

@RestController
public class FilteringController {

    @GetMapping("/filtering")
    public MappingJacksonValue retrieveSomeBean() {

        SomeBeanDTO someBeanDTO = new SomeBeanDTO("value1", "value2", "value3");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field1", "field2");
        FilterProvider filters= new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBeanDTO);
        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }

    @GetMapping("/filtering-list")
    public MappingJacksonValue retrieveListOfSomeBean() {

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field2", "field3");
        FilterProvider filters= new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);

        List<SomeBeanDTO> beans = Arrays.asList(new SomeBeanDTO("value1", "value2", "value3"), new SomeBeanDTO("value12", "value22", "value32"));
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(beans);
        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }
}
