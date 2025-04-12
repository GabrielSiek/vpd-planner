package com.vpd.Friendship;

import java.time.LocalDate;

public record FriendDTO(String id,
                        String email,
                        String name,
                        LocalDate date) {

}
