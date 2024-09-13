package com.emazon.user_v1.infrastructure.input.rest;

import com.emazon.user_v1.application.dto.UserRequest;
import com.emazon.user_v1.application.handler.IUserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import static com.emazon.user_v1.infrastructure.input.rest.utils.PathDefinition.SIGNUP_WAREHOUSE_WORKER;
import static com.emazon.user_v1.infrastructure.input.rest.utils.PathDefinition.USER;

@RestController
@RequestMapping(USER)
@RequiredArgsConstructor
@Validated
public class UserRestController {

    private final IUserHandler userHandler;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created", content = @Content),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad user request", content = @Content)
    })
    @Operation(summary = "Add a new user")
    @PostMapping(SIGNUP_WAREHOUSE_WORKER)
    public ResponseEntity<String> saveWarehouseWorker(
            @RequestBody @Valid UserRequest userRequest
    ) {
        userHandler.saveWarehouseWorker(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
