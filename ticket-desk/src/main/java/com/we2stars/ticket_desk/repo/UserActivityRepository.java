package com.we2stars.ticket_desk.repo;

import com.we2stars.ticket_desk.model.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    List<UserActivity> findByUsername( String username);
}
