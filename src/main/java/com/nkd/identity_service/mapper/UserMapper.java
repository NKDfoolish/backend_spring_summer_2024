package com.nkd.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.nkd.identity_service.dto.request.UserCreationRequest;
import com.nkd.identity_service.dto.request.UserUpdateRequest;
import com.nkd.identity_service.dto.response.UserResponse;
import com.nkd.identity_service.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
