package com.sstinternaltools.sstinternal_tools.user.repository;

import com.sstinternaltools.sstinternal_tools.user.entity.Role;
import com.sstinternaltools.sstinternal_tools.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sstinternaltools.sstinternal_tools.user.entity.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findAllByUser_Id(Long userId);
    Optional<UserRole> findByUserAndRole(User user, Role role);
}
