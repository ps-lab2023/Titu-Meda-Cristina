package com.nagarro.af.bookingtablesystem.controller;

import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping(path = "/restaurants")
public interface RestaurantController {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Save a new restaurant.",
            response = RestaurantDTO.class,
            notes = "Return the created restaurant.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Restaurant successfully created!"),
            @ApiResponse(code = 400, message = "Bad request!")
    })
    ResponseEntity<RestaurantDTO> save(@Valid @RequestBody RestaurantDTO restaurantDTO);

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Find restaurant by id.",
            response = RestaurantDTO.class,
            notes = "Return the restaurant with the given id if found.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Restaurant found!"),
            @ApiResponse(code = 404, message = "Restaurant not found!")
    })
    ResponseEntity<RestaurantDTO> findById(@PathVariable UUID id);

    @GetMapping(
            path = "/name/{name}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Find restaurant by name.",
            response = RestaurantDTO.class,
            notes = "Return the restaurant with the given name if found.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Restaurant found!"),
            @ApiResponse(code = 404, message = "Restaurant not found!")
    })
    ResponseEntity<RestaurantDTO> findByName(@PathVariable String name);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Find all restaurants.",
            notes = "Return all the saved restaurants.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Restaurants returned!")
    })
    ResponseEntity<List<RestaurantDTO>> findAll();

    @GetMapping(
            path = "/country/{country}/city/{city}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Find all restaurants by country and city.",
            notes = "Return all the saved filtered restaurants.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Restaurants returned!")
    })
    ResponseEntity<List<RestaurantDTO>> findAllByCountryAndCity(@PathVariable String country, @PathVariable String city);


    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Delete an existing restaurant by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Restaurant successfully deleted!"),
            @ApiResponse(code = 404, message = "Restaurant not found!")
    })
    ResponseEntity<Void> delete(@PathVariable UUID id);

    @PutMapping(path = "/{restaurant_id}/manager/{manager_id}")
    @ApiOperation(value = "Assign a manager to a restaurant.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Manager successfully assigned!"),
            @ApiResponse(code = 404, message = "Restaurant or manager not found!")
    })
    ResponseEntity<Void> assignRestaurantManager(@PathVariable("restaurant_id") UUID restaurantId,
                                                 @PathVariable("manager_id") UUID managerId);

    @PostMapping(
            path = "/{restaurant_id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ApiOperation(value = "Add a menu to a restaurant.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Menu successfully uploaded!"),
            @ApiResponse(code = 404, message = "Restaurant not found!")
    })
    ResponseEntity<Void> uploadMenu(@PathVariable("restaurant_id") UUID restaurantId,
                                    @RequestPart(name = "menu", required = false) MultipartFile menu);

}
