package com.project.spring.repositories;

import com.project.spring.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser,Long> {
    @Query("SELECT u FROM AppUser u WHERE u.email = :email")
    AppUser getUserByUsername(@Param("email") String email);

}
