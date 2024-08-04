package com.guuri11.isi.infrastructure.web;

import com.guuri11.isi.application.command.CommandDto;
import com.guuri11.isi.application.command.CommandRequest;
import com.guuri11.isi.application.command.service.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/commands")
@RequiredArgsConstructor
public class CommandController extends BaseController {
    private final CommandGetAll getAll;
    private final CommandGetById getById;
    private final CommandCreate create;
    private final CommandDelete delete;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Collection<CommandDto> all() {

        this.logger.info("Request: GET /commands");
        return getAll.all();
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public CommandDto one(@PathVariable("id") final UUID id) {

        this.logger.info("Request: GET /commands/{}", id);
        return getById.one(id);
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    CommandDto create(@RequestBody final CommandRequest command) {

        this.logger.info("Request: POST /commands : {}", command);

        return create.create(command, MessageType.USER);
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<?> delete(@PathVariable("id") final UUID id) {

        this.logger.info("Request: DELETE /commands/{}", id);
        delete.delete(id);
        return ResponseEntity.noContent()
                .build();
    }
}
