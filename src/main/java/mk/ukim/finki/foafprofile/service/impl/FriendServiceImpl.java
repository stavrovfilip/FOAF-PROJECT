package mk.ukim.finki.foafprofile.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.foafprofile.model.Friend;
import mk.ukim.finki.foafprofile.model.dto.FriendDto;
import mk.ukim.finki.foafprofile.repository.FriendRepository;
import mk.ukim.finki.foafprofile.service.FriendService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;

    @Override
    public Friend saveFriend(FriendDto friendDto) {
        Friend friend = new Friend();
        if (friendDto.getFriendEmail() != null) {
            friend.setEmail(friendDto.getFriendEmail());
        }
        if (friendDto.getFriendFirstName() != null) {
            friend.setFirstName(friendDto.getFriendFirstName());
        }
        if (friendDto.getFriendLastName() != null) {
            friend.setLastName(friendDto.getFriendLastName());
        }
        if (friendDto.getFriendFoafUri() != null) {
            friend.setFoafProfileUri(friendDto.getFriendFoafUri());
        }
        return this.friendRepository.save(friend);

    }

}
