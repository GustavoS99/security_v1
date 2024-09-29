package com.emazon.user_v1.infrastructure.input.rest;

import com.emazon.user_v1.application.dto.LoginRequest;
import com.emazon.user_v1.application.dto.LoginResponse;
import com.emazon.user_v1.application.dto.UserRequest;
import com.emazon.user_v1.application.handler.IUserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.emazon.user_v1.infrastructure.input.rest.util.PathDefinition.*;

@RestController
@RequestMapping(USER)
@RequiredArgsConstructor
@Validated
public class UserRestController {

    private final IUserHandler userHandler;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created", content = @Content),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad user request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @Operation(summary = "Add a new warehouse worker")
    @PostMapping(SIGNUP_WAREHOUSE_WORKER)
    public ResponseEntity<Void> saveWarehouseWorker(
            @RequestBody @Valid UserRequest userRequest
    ) {
        userHandler.saveWarehouseWorker(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged in", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @Operation(summary = "Login user")
    @PostMapping(LOGIN)
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(userHandler.authenticate(loginRequest));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad user request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content)
    })
    @Operation(summary = "Add a new customer")
    @PostMapping(SIGNUP)
    public ResponseEntity<Void> saveCustomer(
            @RequestBody @Valid UserRequest userRequest
    ) {
        userHandler.saveCustomer(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
