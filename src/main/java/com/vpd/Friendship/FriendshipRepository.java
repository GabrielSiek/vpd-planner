package com.vpd.Friendship;

import com.vpd.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, String> {

    boolean existsByRequesterAndReceiver(User requester, User receiver);
}
