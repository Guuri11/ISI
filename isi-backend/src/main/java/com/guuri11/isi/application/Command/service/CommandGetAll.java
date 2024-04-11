package com.guuri11.isi.application.Command.service;


import com.guuri11.isi.application.Command.CommandDto;
import com.guuri11.isi.domain.Command.CommandMapper;
import com.guuri11.isi.infrastructure.persistance.CommandRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CommandGetAll {
    private final CommandRepository repository;
    private final CommandMapper mapper;

    public Collection<CommandDto> all() {

        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

}
