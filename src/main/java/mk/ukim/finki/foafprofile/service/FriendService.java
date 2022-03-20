package mk.ukim.finki.foafprofile.service;

import mk.ukim.finki.foafprofile.model.Friend;
import mk.ukim.finki.foafprofile.model.dto.FriendDto;

public interface FriendService {

    Friend saveFriend(FriendDto friendDto);

}
