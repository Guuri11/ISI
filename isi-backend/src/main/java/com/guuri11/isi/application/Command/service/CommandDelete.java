package com.guuri11.isi.application.Command.service;


import com.guuri11.isi.application.Command.CommandDto;
import com.guuri11.isi.application.Command.CommandRequest;
import com.guuri11.isi.application.exception.NotFoundException;
import com.guuri11.isi.domain.Command.Command;
import com.guuri11.isi.domain.Command.CommandMapper;
import com.guuri11.isi.infrastructure.persistance.CommandRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
