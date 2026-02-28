package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByPhone(String phone);
    
    Optional<User> findByUserCode(String userCode);
    
    boolean existsByUsername(String username);
    
    boolean existsByPhone(String phone);
    
    boolean existsByUserCode(String userCode);
    
    /**
     * 查询当前最大的用户编码
     * @return 最大的用户编码（如：0005）
     */
    @Query("SELECT MAX(u.userCode) FROM User u")
    String findMaxUserCode();
}

