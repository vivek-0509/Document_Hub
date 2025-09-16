package com.sstinternaltools.sstinternal_tools.security.service.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface MyUserDetailService {
    UserDetails loadUserByEmail(String email);
}
