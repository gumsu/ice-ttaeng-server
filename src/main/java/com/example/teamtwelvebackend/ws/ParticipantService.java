package com.example.teamtwelvebackend.ws;

import com.example.teamtwelvebackend.activity.speedgame.controller.ws.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ParticipantService {

    final SimpUserRegistry simpUserRegistry;

    /**
     * destination 에 참가한 참가자 목록을 조회합니다.
     * 주최자는 제외됩니다.
     *
     * @param destination 조회할 topic 경로
     * @return Participant list
     */
    public List<Participant> getParticipant(String destination) {
        Set<SimpSubscription> subscriptions = simpUserRegistry.findSubscriptions(subscription -> subscription.getDestination().equals(destination));
        return subscriptions.stream()
                .filter(subscription -> subscription.getSession().getUser().getPrincipal() instanceof Participant)
                .map(user -> (Participant) user.getSession().getUser().getPrincipal())
                .toList();
    }
}
