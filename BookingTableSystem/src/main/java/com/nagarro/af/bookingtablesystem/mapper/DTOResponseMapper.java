package com.nagarro.af.bookingtablesystem.mapper;

import com.nagarro.af.bookingtablesystem.controller.response.UserResponse;
import com.nagarro.af.bookingtablesystem.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DTOResponseMapper<D extends UserDTO, R extends UserResponse> {

    protected final ModelMapper modelMapper;

    public DTOResponseMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.addMappings(new PropertyMap<R, D>() {
            @Override
            protected void configure() {
                skip(destination.getId());
                skip(destination.getPassword());
            }
        });
    }

    public abstract R mapDTOToResponse(D dto);

    public List<R> mapDTOListToResponseList(List<D> dtoList) {
        return dtoList
                .stream()
                .map(this::mapDTOToResponse)
                .collect(Collectors.toList());
    }
}
