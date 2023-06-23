package com.example.teamtwelvebackend.activity.common.repository;

import com.example.teamtwelvebackend.activity.common.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

}
