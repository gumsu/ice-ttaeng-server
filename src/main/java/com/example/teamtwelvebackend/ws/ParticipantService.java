package com.example.teamtwelvebackend.ws;

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

    /**
     * destination 구독자 전체 목록을 조회합니다.
     * 주최자도 포함됩니다.
     *
     * @param destination 조회할 topic 경로
     * @return 구독자 전체 list
     */
    public List<ActivityParticipant> getAll(String destination) {
        return simpUserRegistry.findSubscriptions(subscription -> subscription.getDestination().equals(destination)).stream()
                .map(user -> (ActivityParticipant) user.getSession().getUser().getPrincipal())
                .toList();
    }

    /**
     * 참가자 한 명만 조회
     * @param destination 조회할 topic 경로
     * @param userId 참가자 고유 아이디
     * @return Participant
     */
    public Participant getParticipantOne(String destination, String userId) {
        Set<SimpSubscription> subscriptions = simpUserRegistry.findSubscriptions(subscription -> subscription.getDestination().equals(destination));
        return subscriptions.stream()
            .filter(subscription -> subscription.getSession().getUser().getName().equals(userId))
            .findFirst()
            .map(user -> (Participant) user.getSession().getUser().getPrincipal())
            .orElseThrow(() -> new RuntimeException("존재하지 않는 참가자입니다."));
    }
}
