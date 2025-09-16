package com.sstinternaltools.sstinternal_tools.user.controller.Interface;

import com.sstinternaltools.sstinternal_tools.user.dto.UserResponseDto;
import com.sstinternaltools.sstinternal_tools.user.dto.UserSummaryDto;
import com.sstinternaltools.sstinternal_tools.user.dto.UserUpdateDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserController {
    ResponseEntity<UserResponseDto> getUserByAccessToken(String accessToken);
    ResponseEntity<UserResponseDto> updateUser(UserUpdateDto userUpdateDto, Long id);
    ResponseEntity<UserResponseDto> updateUserPartially(UserUpdateDto userUpdateDto, Long id);
    ResponseEntity<UserSummaryDto> getUserById(Long id);
    ResponseEntity<UserSummaryDto> getUserByEmail(String email);
    ResponseEntity<List<UserSummaryDto>> getAllUsers();
    ResponseEntity<String> deleteUser(Long id);
}