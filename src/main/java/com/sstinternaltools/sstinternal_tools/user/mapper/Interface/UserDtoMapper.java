package com.sstinternaltools.sstinternal_tools.user.mapper.Interface;

public interface UserDtoMapper <User, UserUpdateDto, UserResponseDto, UserSummaryDto> {
    User fromUpdateDto(UserUpdateDto updateDto, User user);
    UserResponseDto toResponseDto(User user);
    UserSummaryDto toSummaryDto(User user);
}