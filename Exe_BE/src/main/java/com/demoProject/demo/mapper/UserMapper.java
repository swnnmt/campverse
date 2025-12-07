package com.demoProject.demo.mapper;


import com.demoProject.demo.model.dto.request.RegistrationRequest;
import com.demoProject.demo.model.dto.response.RegistrationResponse;
import com.demoProject.demo.model.dto.response.UserResponse;
import com.demoProject.demo.model.entity.User;
import com.demoProject.demo.model.entity.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    UserInfo toUserInfo(RegistrationRequest registrationRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", constant = "false")
    @Mapping(target = "loginCount", constant = "0")
    @Mapping(target = "locked", constant = "false")
    @Mapping(target = "userInfo", source = "registrationRequest")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "refreshTokens", ignore = true)
    User toUser(RegistrationRequest registrationRequest);

    RegistrationResponse toRegistrationResponse(UserInfo userInfo);

    UserResponse toUserResponse(UserInfo userInfo);
}
