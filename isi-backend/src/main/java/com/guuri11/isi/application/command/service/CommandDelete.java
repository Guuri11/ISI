package com.guuri11.isi.application.command.service;


import com.guuri11.isi.application.exception.NotFoundException;
import com.guuri11.isi.domain.command.Command;
import com.guuri11.isi.infrastructure.persistance.CommandRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CommandDelete {
    private final CommandRepository repository;

    public void delete(final UUID id) {

        final Command entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        repository.delete(entity);
    }
}
