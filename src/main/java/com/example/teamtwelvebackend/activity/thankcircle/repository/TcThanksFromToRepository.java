package com.example.teamtwelvebackend.activity.thankcircle.repository;

import com.example.teamtwelvebackend.activity.thankcircle.domain.ThankCircleFromTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TcThanksFromToRepository extends JpaRepository<ThankCircleFromTo, Long> {
    Optional<ThankCircleFromTo> findByRoomNameAndNumber(String roomName, Integer number);
}
