package com.nagarro.af.bookingtablesystem.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface EntityDTOMapper<E, D> {
    D mapEntityToDTO(E entity);

    E mapDTOtoEntity(D dto);

    default List<D> mapEntityListToDTOList(List<E> entityList) {
        return entityList
                .stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    default List<E> mapDTOListToEntityList(List<D> dtoList) {
        return dtoList
                .stream()
                .map(this::mapDTOtoEntity)
                .collect(Collectors.toList());
    }
}
