package com.example.gadgetstore.mappers;

import org.mapstruct.Mapper;
import com.example.gadgetstore.entities.User;
import com.example.gadgetstore.dtos.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto user);
}
