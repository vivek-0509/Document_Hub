package com.sstinternaltools.sstinternal_tools.security.service.implementation;

import com.sstinternaltools.sstinternal_tools.security.exception.RoleAssignmentException;
import com.sstinternaltools.sstinternal_tools.security.service.interfaces.CustomLogicService;
import com.sstinternaltools.sstinternal_tools.security.service.interfaces.ExcelEmailChecker;
import com.sstinternaltools.sstinternal_tools.user.entity.Role;
import com.sstinternaltools.sstinternal_tools.user.entity.User;
import com.sstinternaltools.sstinternal_tools.user.entity.UserRole;
import com.sstinternaltools.sstinternal_tools.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CustomLogicServiceImpl implements CustomLogicService {

    private final ExcelEmailChecker excelEmailChecker;
    private final UserRepository userRepository;
    private final String excelFilePath;

    public CustomLogicServiceImpl(ExcelEmailChecker excelEmailChecker, UserRepository userRepository, @Value("${EXCEL_FILE_PATH}") String excelFilePath) {
        this.excelEmailChecker = excelEmailChecker;
        this.userRepository = userRepository;
        this.excelFilePath = excelFilePath;
    }

    public List<UserRole> assignRoles(String email) {
        List<UserRole> roles = new ArrayList<>();

        try {
            String domain = email.split("@")[1];
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

            if (domain.equals("scaler.com") || email.equals("arnav.24bcs10063@sst.scaler.com") || email.equals("srinidhi.24bcs10037@sst.scaler.com") || email.equals("vivek.24bcs10338@sst.scaler.com") || email.equals("amrinder.24bcs10596@sst.scaler.com")) { // Need to change this after getting a priveleged email.
                if (excelEmailChecker.isEmailInExcel(email, excelFilePath)) {
                    UserRole adminRole = new UserRole();
                    adminRole.setRole(Role.valueOf("admin".toUpperCase()));// Assign admin role if found in the Excel sheet
                    adminRole.setUser(user);
                    roles.add(adminRole);
                    UserRole studentRole = new UserRole();
                    studentRole.setRole(Role.valueOf("student".toUpperCase()));// Assign student role
                    studentRole.setUser(user);
                    roles.add(studentRole);
                }
            }
            else if (domain.equals("sst.scaler.com")) {
                UserRole studentRole = new UserRole();
                studentRole.setRole(Role.valueOf("student".toUpperCase()));// Assign student role
                studentRole.setUser(user);
                roles.add(studentRole);
            }

        } catch (Exception e) {
            throw new RoleAssignmentException("Role assignment failed.");
        }
        return roles;
    }
}
