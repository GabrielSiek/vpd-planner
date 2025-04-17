package com.vpd.Friendship;

import com.vpd.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, String> {

    boolean existsByRequesterAndReceiver(User requester, User receiver);
}
