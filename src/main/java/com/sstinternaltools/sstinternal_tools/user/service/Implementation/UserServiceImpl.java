package com.sstinternaltools.sstinternal_tools.user.service.Implementation;

import com.sstinternaltools.sstinternal_tools.policyChatbot.exception.ResourceNotFoundException;
import com.sstinternaltools.sstinternal_tools.security.exception.UserNotFoundException;
import com.sstinternaltools.sstinternal_tools.security.service.interfaces.JwtService;
import com.sstinternaltools.sstinternal_tools.user.dto.UserResponseDto;
import com.sstinternaltools.sstinternal_tools.user.dto.UserSummaryDto;
import com.sstinternaltools.sstinternal_tools.user.dto.UserUpdateDto;
import com.sstinternaltools.sstinternal_tools.user.entity.User;
import com.sstinternaltools.sstinternal_tools.user.mapper.Implementation.UserMapper;
import com.sstinternaltools.sstinternal_tools.user.repository.UserRepository;
import com.sstinternaltools.sstinternal_tools.user.service.Interface.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, JwtService jwtService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDto getUserByAccessToken(String accessToken) {
        String email = jwtService.extractEmail(accessToken);
        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        return userMapper.toResponseDto(user);
    }

    @Transactional
    @Override
    public UserResponseDto updateUser(UserUpdateDto userUpdateDto, Long userId, Boolean isPut) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found. [Method: updateUser]"));
        if (isPut) {
            if (userUpdateDto.getUsername() == null || userUpdateDto.getUsername().isEmpty()) {
                throw new IllegalArgumentException("User name cannot be blank.");
            }
            user.setUsername(null);
        }
        user = userMapper.fromUpdateDto(userUpdateDto, user);
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    public UserSummaryDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        return userMapper.toSummaryDto(user);
    }

    @Override
    public UserSummaryDto getUserByEmail(String email) {
        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        return userMapper.toSummaryDto(user);
    }

    @Override
    public List<UserSummaryDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserSummaryDto> userSummaryDtos = new ArrayList<>();
        for (User user : users) {
            userSummaryDtos.add(userMapper.toSummaryDto(user));
        }
        return userSummaryDtos;
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found.");
        }
        userRepository.deleteById(id);
    }
}