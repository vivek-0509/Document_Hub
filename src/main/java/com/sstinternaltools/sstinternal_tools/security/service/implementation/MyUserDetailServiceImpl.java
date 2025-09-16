package com.sstinternaltools.sstinternal_tools.security.service.implementation;

import com.sstinternaltools.sstinternal_tools.security.entity.UserPrincipal;
import com.sstinternaltools.sstinternal_tools.security.exception.UserNotFoundException;
import com.sstinternaltools.sstinternal_tools.security.service.interfaces.MyUserDetailService;
import com.sstinternaltools.sstinternal_tools.user.entity.User;
import com.sstinternaltools.sstinternal_tools.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailServiceImpl implements MyUserDetailService {

    private final UserRepository userJpaRepository;

    public MyUserDetailServiceImpl(UserRepository userJpaRepository){
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userJpaRepository.findByEmailWithRoles(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));
        return new UserPrincipal(user);
    }
}
