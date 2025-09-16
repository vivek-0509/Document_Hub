package com.sstinternaltools.sstinternal_tools.user.controller.Implementation;

import com.sstinternaltools.sstinternal_tools.user.controller.Interface.UserController;
import com.sstinternaltools.sstinternal_tools.user.dto.UserResponseDto;
import com.sstinternaltools.sstinternal_tools.user.dto.UserSummaryDto;
import com.sstinternaltools.sstinternal_tools.user.dto.UserUpdateDto;
import com.sstinternaltools.sstinternal_tools.user.service.Interface.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserControllerImpl implements UserController {
    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping("/whoAmI")
    public ResponseEntity<UserResponseDto> getUserByAccessToken(@Valid @CookieValue String accessToken) {
        UserResponseDto userResponseDto = userService.getUserByAccessToken(accessToken);
        return ResponseEntity.ok(userResponseDto);
    }

    @Override
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto, @PathVariable Long id){
        UserResponseDto userResponseDto = userService.updateUser(userUpdateDto, id, true);
        return ResponseEntity.ok(userResponseDto);
    }

    @Override
    @PatchMapping("/update/{id}")
    public ResponseEntity<UserResponseDto> updateUserPartially(@Valid @RequestBody UserUpdateDto userUpdateDto, @PathVariable Long id){
        UserResponseDto userResponseDto = userService.updateUser(userUpdateDto, id, false);
        return ResponseEntity.ok(userResponseDto);
    }

    @Override
    @GetMapping("/fetchById/{id}")
    public ResponseEntity<UserSummaryDto> getUserById(@Valid @PathVariable Long id) {
        UserSummaryDto userSummaryDto = userService.getUserById(id);
        return ResponseEntity.ok(userSummaryDto);
    }

    @Override
    @GetMapping("/fetchByEmail/{email}")
    public ResponseEntity<UserSummaryDto> getUserByEmail(@Valid @PathVariable String email) {
        UserSummaryDto userSummaryDto = userService.getUserByEmail(email);
        return ResponseEntity.ok(userSummaryDto);
    }

    @Override
    @GetMapping("/fetchAll")
    public ResponseEntity<List<UserSummaryDto>> getAllUsers() {
        List<UserSummaryDto> userSummaryDtos = userService.getAllUsers();
        return ResponseEntity.ok(userSummaryDtos);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@Valid @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("✅ User deleted successfully");
    }
}