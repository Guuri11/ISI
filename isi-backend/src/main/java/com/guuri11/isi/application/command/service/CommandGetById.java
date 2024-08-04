package com.guuri11.isi.application.command.service;


import com.guuri11.isi.application.command.CommandDto;
import com.guuri11.isi.application.exception.NotFoundException;
import com.guuri11.isi.domain.command.CommandMapper;
import com.guuri11.isi.infrastructure.persistance.CommandRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CommandGetById {
    private final CommandRepository repository;
    private final CommandMapper mapper;

    public CommandDto one(final UUID id) {

        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException(id));
    }
}
