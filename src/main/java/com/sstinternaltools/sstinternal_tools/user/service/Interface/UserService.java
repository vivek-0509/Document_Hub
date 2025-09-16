package com.sstinternaltools.sstinternal_tools.user.service.Interface;

import com.sstinternaltools.sstinternal_tools.user.dto.UserResponseDto;
import com.sstinternaltools.sstinternal_tools.user.dto.UserSummaryDto;
import com.sstinternaltools.sstinternal_tools.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    UserResponseDto getUserByAccessToken(String accessToken);
    UserResponseDto updateUser(UserUpdateDto userUpdateDto, Long id, Boolean isPut);
    UserSummaryDto getUserById(Long id);
    UserSummaryDto getUserByEmail(String email);
    List<UserSummaryDto> getAllUsers();
    void deleteUser(Long id);
}