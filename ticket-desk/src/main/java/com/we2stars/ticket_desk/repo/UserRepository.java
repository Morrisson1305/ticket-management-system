package com.we2stars.ticket_desk.repo;

import com.we2stars.ticket_desk.model.AppUser;
import com.we2stars.ticket_desk.model.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    List<AppUser> findByType(UserType type);
    Optional<AppUser> findByScore(int score);
    Optional<AppUser> findByEmail(String email);


}
