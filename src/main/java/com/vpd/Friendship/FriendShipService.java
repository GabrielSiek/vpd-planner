package com.vpd.Friendship;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.User.User;
import com.vpd.User.UserRepository;
import com.vpd.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendShipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    public ApiResponse<?> sendInvite(FriendshipInviteDTO friendshipInviteDTO, User requester) {

        try {
            Friendship friendship = new Friendship();

            Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(friendshipInviteDTO.receiverEmail()));
            if(optionalUser.isEmpty())
                return new ApiResponse<>(false, "User not found with email provided", null, HttpStatus.NOT_FOUND.value());

            User receiver = optionalUser.get();

            friendship.setRequester(requester);
            friendship.setReceiver(receiver);
            friendship.setFriendshipStatus(FriendshipStatus.PENDING);

            //requester add invite
            //receiver add invite
            addFrienshipInvite(requester, receiver, friendship);

            friendshipRepository.save(friendship);

            return new ApiResponse<>(true, "Invite sent succesfully", null, HttpStatus.OK.value());

        }catch (Exception exception) {
            return new ApiResponse<>(false, "Interal error: " + exception.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private void addFrienshipInvite(User requester, User receiver, Friendship friendship) {

        List<Friendship> requesterList = requester.getSentFriendships();
        List<Friendship> receiverList = receiver.getReceivedFriendships();

        requesterList.add(friendship);
        receiverList.add(friendship);

        requester.setSentFriendships(requesterList);
        receiver.setReceivedFriendships(receiverList);

        userRepository.save(requester);
        userRepository.save(receiver);
    }
}
