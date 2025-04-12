package com.vpd.Friendship;

import java.time.LocalDate;

public record FriendshipDTO(String id,
                            String  requesterEmail,
                            String receiverEmail,
                            FriendshipStatus status,
                            LocalDate invitationDate,
                            LocalDate acceptanceDate) {

    public FriendshipDTO(Friendship friendship) {
        this(
                friendship.getId(),
                friendship.getRequester().getEmail(),
                friendship.getReceiver().getEmail(),
                friendship.getFriendshipStatus(),
                friendship.getInvitationDate(),
                friendship.getAcceptanceDate()
        );
    }

}
