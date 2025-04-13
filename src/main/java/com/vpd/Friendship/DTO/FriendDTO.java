package com.vpd.Friendship.DTO;

import java.time.LocalDate;

public record FriendDTO(String id,
                        String email,
                        String name,
                        LocalDate date) {

}
