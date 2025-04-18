package com.vpd.Friendship;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.ApiResponse.ApiResponseHelper;
import com.vpd.Friendship.DTO.*;
import com.vpd.User.User;
import com.vpd.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Service
public class FriendShipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    public ApiResponse<FriendshipDTO> getFriendship(String id, User user) {
        try {
            Optional<User> optionalUser = userRepository.findById(user.getId());

            if(optionalUser.isEmpty())
                return ApiResponseHelper.notFound("User not found");

            user = optionalUser.get();

            ApiResponse<FriendshipDTO> verification = verifyFriendship(id);

            if (verification.getData() == null)
                return verification;

            FriendshipDTO friendshipDTO = verification.getData();

            if (verifyAuthorization(friendshipDTO, user).equals(HttpStatus.FORBIDDEN))
                return ApiResponseHelper.unauthorized();

            return ApiResponseHelper.ok("Friendship found successfully", friendshipDTO);
        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

        public ApiResponse<List<FriendDTO>> getAllFriendships(User user) {
            try {

                Optional<User> optionalUser = userRepository.findById(user.getId());

                if(optionalUser.isEmpty())
                    return ApiResponseHelper.notFound("User not found");

                user = optionalUser.get();

                List<FriendDTO> friends = Stream.concat(
                                user.getSentFriendships().stream()
                                        .filter(friendship -> friendship.getFriendshipStatus() == FriendshipStatus.ACCEPTED)
                                        .map(friendship -> new FriendDTO(
                                                friendship.getId(),
                                                friendship.getReceiver().getEmail(),
                                                friendship.getReceiver().getUsername(),
                                                friendship.getAcceptanceDate()
                                        )),
                                user.getReceivedFriendships().stream()
                                        .filter(friendship -> friendship.getFriendshipStatus() == FriendshipStatus.ACCEPTED)
                                        .map(friendship -> new FriendDTO(
                                                friendship.getId(),
                                                friendship.getRequester().getEmail(),
                                                friendship.getRequester().getUsername(),
                                                friendship.getAcceptanceDate()
                                        ))
                        ).sorted(Comparator.comparing(FriendDTO::name))
                        .toList();

                return ApiResponseHelper.ok("Friends List", friends);

            } catch (Exception exception) {
                return ApiResponseHelper.internalError(exception);
            }
        }

    public ApiResponse<List<InviteDTO>> getAllInvites(User user) {
        try {

            Optional<User> optionalUser = userRepository.findById(user.getId());

            if(optionalUser.isEmpty())
                return ApiResponseHelper.notFound("User not found");

            user = optionalUser.get();

            List<InviteDTO> invites = user.getReceivedFriendships().stream()
                    .filter(friendship -> friendship.getFriendshipStatus() == FriendshipStatus.PENDING)
                    .map(friendship -> new InviteDTO(
                            friendship.getId(),
                            friendship.getRequester().getEmail(),
                            friendship.getRequester().getUsername(),
                            friendship.getFriendshipStatus(),
                            friendship.getInvitationDate()
                        ))
                    .sorted(Comparator.comparing(InviteDTO::name))
                    .toList();

            return ApiResponseHelper.ok("Invites List", invites);

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    @Transactional
    public ApiResponse<FriendshipDTO> sendInvite(FriendshipInviteDTO friendshipInviteDTO, User user) {
        try {

            Optional<User> optionalRequester = userRepository.findById(user.getId());
            if(optionalRequester.isEmpty())
                return ApiResponseHelper.notFound("Requester not found with email provided");

            User requester = optionalRequester.get();

            Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(friendshipInviteDTO.receiverEmail()));
            if (optionalUser.isEmpty())
                return ApiResponseHelper.notFound("Receiver not found with email provided");

            User receiver = optionalUser.get();

            boolean alreadyExists = friendshipRepository.existsByRequesterAndReceiver(requester, receiver)
                    || friendshipRepository.existsByRequesterAndReceiver(receiver, requester);

            if (alreadyExists)
                return ApiResponseHelper.badRequest("Friendship already exists");

            Friendship friendship = new Friendship();
            friendship.setRequester(requester);
            friendship.setReceiver(receiver);
            friendship.setFriendshipStatus(FriendshipStatus.PENDING);
            friendship.setInvitationDate(LocalDate.now());

            addFrienshipInvite(requester, receiver, friendship);
            friendshipRepository.save(friendship);

            FriendshipDTO friendshipDTO = objectToDTO(friendship, requester.getEmail(), receiver.getEmail());
            return ApiResponseHelper.ok("Invite sent successfully", friendshipDTO);

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    @Transactional
    public ApiResponse<FriendshipDTO> responseInvite(String id, FriendshipResponseDTO friendshipResponseDTO, User user) {
        try {

            Optional<User> optionalUser = userRepository.findById(user.getId());

            if(optionalUser.isEmpty())
                return ApiResponseHelper.notFound("User not found");

            user = optionalUser.get();

            ApiResponse<FriendshipDTO> verification = verifyFriendship(id);

            if (verification.getData() == null)
                return verification;

            FriendshipDTO friendshipDTO = verification.getData();

            if (verifyAuthorization(friendshipDTO, user).equals(HttpStatus.FORBIDDEN))
                return ApiResponseHelper.unauthorized();

            Optional<Friendship> optionalFriendship = friendshipRepository.findById(id);
            if (optionalFriendship.isEmpty())
                return ApiResponseHelper.notFound("Friendship not found");

            Friendship friendship = optionalFriendship.get();
            friendship.setFriendshipStatus(friendshipResponseDTO.friendshipStatus());

            if(friendship.getFriendshipStatus() == FriendshipStatus.ACCEPTED) {
                LocalDate localDate = LocalDate.now();
                friendship.setAcceptanceDate(localDate);
                friendshipRepository.save(friendship);
            }
            else if(friendship.getFriendshipStatus() == FriendshipStatus.REJECTED)
                friendshipRepository.delete(friendship);

            FriendshipDTO response = objectToDTO(friendship);
            return ApiResponseHelper.ok("Response sent", response);

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    @Transactional
    public  ApiResponse<?> deleteFriendship(String id, User user) {
        try {

            Optional<User> optionalUser = userRepository.findById(user.getId());

            if(optionalUser.isEmpty())
                return ApiResponseHelper.notFound("User not found");

            user = optionalUser.get();

            ApiResponse<FriendshipDTO> verification = verifyFriendship(id);

            if (verification.getData() == null)
                return verification;

            FriendshipDTO friendshipDTO = verification.getData();

            if (verifyAuthorization(friendshipDTO, user).equals(HttpStatus.FORBIDDEN))
                return ApiResponseHelper.unauthorized();

            Optional<Friendship> optionalFriendship = friendshipRepository.findById(id);
            if (optionalFriendship.isEmpty())
                return ApiResponseHelper.notFound("Friendship not found");

            Friendship friendship = optionalFriendship.get();
            friendshipRepository.delete(friendship);

            return ApiResponseHelper.ok("Friendship deleted", friendship.getId());

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    private void addFrienshipInvite(User requester, User receiver, Friendship friendship) {

        requester.getSentFriendships().add(friendship);
        receiver.getReceivedFriendships().add(friendship);

        userRepository.save(requester);
        userRepository.save(receiver);
    }

    private FriendshipDTO objectToDTO(Friendship friendship, String requester, String receiver) {
        return new FriendshipDTO(
                friendship.getId(),
                requester,
                receiver,
                friendship.getFriendshipStatus(),
                friendship.getInvitationDate(),
                friendship.getAcceptanceDate()
        );
    }

    private FriendshipDTO objectToDTO(Friendship friendship) {
        return new FriendshipDTO(
                friendship.getId(),
                friendship.getRequester().getEmail(),
                friendship.getReceiver().getEmail(),
                friendship.getFriendshipStatus(),
                friendship.getInvitationDate(),
                friendship.getAcceptanceDate()
        );
    }

    private ApiResponse<FriendshipDTO> verifyFriendship(String id) {
        Optional<Friendship> optionalFriendship = friendshipRepository.findById(id);

        if (optionalFriendship.isEmpty())
            return ApiResponseHelper.notFound("Friendship not found");

        Friendship friendship = optionalFriendship.get();
        FriendshipDTO friendshipDTO = objectToDTO(friendship);

        return ApiResponseHelper.ok("Friendship found", friendshipDTO);
    }

    private HttpStatus verifyAuthorization(FriendshipDTO friendship, User user) {
        if (!Objects.equals(friendship.requesterEmail(), user.getEmail()) &&
                !Objects.equals(friendship.receiverEmail(), user.getEmail()))
            return HttpStatus.FORBIDDEN;

        return HttpStatus.OK;
    }


}
