package com.example.springbootweb.mapper;

import com.example.springbootweb.dto.request.UserCreationRequest;
import com.example.springbootweb.dto.request.UserUpdateRequest;
import com.example.springbootweb.dto.respone.UserResponse;
import com.example.springbootweb.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "role", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
    User toUser(UserCreationRequest userRequest);
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "username", ignore = true)
//    @Mapping(target = "password", ignore = true)
//    @Mapping(target = "role", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);


}
