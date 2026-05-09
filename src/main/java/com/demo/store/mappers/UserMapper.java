package com.demo.store.mappers;

import com.demo.store.dtos.RegisterUserRequest;
import com.demo.store.dtos.UpdateUserRequest;
import com.demo.store.dtos.UserDto;
import com.demo.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// used as interface as map strut automatically implements this class at run time!
@Mapper (componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto (User user);
    User toEntity (RegisterUserRequest registerUserRequest);
    void updateUser(UpdateUserRequest updateUserRequest,  @MappingTarget User user);
}
