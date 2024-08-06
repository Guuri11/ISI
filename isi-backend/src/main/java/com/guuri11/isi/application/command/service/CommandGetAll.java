package com.guuri11.isi.application.command.service;


import com.guuri11.isi.application.command.CommandDto;
import com.guuri11.isi.domain.command.Command;
import com.guuri11.isi.domain.command.CommandMapper;
import com.guuri11.isi.infrastructure.persistance.CommandRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;

@Service
@AllArgsConstructor
public class CommandGetAll {
    private final CommandRepository repository;
    private final CommandMapper mapper;

    public Collection<CommandDto> all() {

        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(Command::getCreatedAt))
                .map(mapper::toDto)
                .toList();
    }

}
