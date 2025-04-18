package com.vpd.Friendship.DTO;

import com.vpd.Friendship.FriendshipStatus;

import java.time.LocalDate;

public record InviteDTO(String id,
                        String email,
                        String name,
                        FriendshipStatus status,
                        LocalDate invitationDate) {
}
