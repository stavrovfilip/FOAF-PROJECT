package mk.ukim.finki.foafprofile.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.foafprofile.model.FoafProfileInfo;
import mk.ukim.finki.foafprofile.model.Friend;
import mk.ukim.finki.foafprofile.model.Picture;
import mk.ukim.finki.foafprofile.model.exceptions.EmailAlreadyUsedExeption;
import mk.ukim.finki.foafprofile.model.exceptions.FoafProfileInfoNotFoundException;
import mk.ukim.finki.foafprofile.model.exceptions.MissingPersonalInfoException;
import mk.ukim.finki.foafprofile.repository.FoafProfileInfoRepository;
import mk.ukim.finki.foafprofile.service.FoafProfileInfoService;
import mk.ukim.finki.foafprofile.service.FoafProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class FoafProfileInfoServiceImpl implements FoafProfileInfoService {

    private final FoafProfileInfoRepository foafProfileInfoRepository;
    private final FoafProfileService foafProfileService;

    @Override
    public FoafProfileInfo saveProfile(String title, String firstName, String lastName, String nickName, String email, String homepage, String phoneNumber, Picture picture, String workHomepage, String workDescription, String schoolHomepage, List<Friend> myFriends) {

        if (firstName.isEmpty() || firstName == null || lastName.isEmpty()
                || lastName == null || email.isEmpty() || email == null) {
            throw new MissingPersonalInfoException();
        }

        if (this.foafProfileInfoRepository.findByEmail(email) != null) {
            throw new EmailAlreadyUsedExeption();
        }
        String name = firstName + lastName;
        if (this.foafProfileInfoRepository.findByFirstNameAndLastName(firstName, lastName) != null) {
            Random random = new Random();
            int randomNumber = random.nextInt(100);
            name += randomNumber;

        }
        // TO DO: stavete go vo app.properites
        String uri = name;

        FoafProfileInfo newProfile = new FoafProfileInfo(uri, title, firstName, lastName, nickName,
                email, homepage, phoneNumber, picture, workHomepage, workDescription, schoolHomepage, myFriends);

        foafProfileService.createFoafProfile(newProfile);
        return this.foafProfileInfoRepository.save(newProfile);

    }

    @Override
    public FoafProfileInfo updateProfile(Long id, String title, String lastName, String nickName, String homepage,
                                         String phoneNumber, Picture picture, String workHomepage,
                                         String workDescription, String schoolHomepage, List<Friend> myFriends) {

        FoafProfileInfo foafProfileInfo = this.foafProfileInfoRepository.findById(id).orElseThrow(FoafProfileInfoNotFoundException::new);
        foafProfileInfo.setTitle(title);
        foafProfileInfo.setLastName(lastName);
        foafProfileInfo.setNickName(nickName);
        foafProfileInfo.setHomepage(homepage);
        foafProfileInfo.setPhoneNumber(phoneNumber);
        foafProfileInfo.setPicture(picture);
        foafProfileInfo.setWorkHomepage(workHomepage);
        foafProfileInfo.setWorkDescription(workDescription);
        foafProfileInfo.setSchoolHomepage(schoolHomepage);
        foafProfileInfo.setMyFriends(myFriends);

        this.foafProfileService.updateFoafProfile(foafProfileInfo.getUri(), foafProfileInfo);
        return this.foafProfileInfoRepository.save(foafProfileInfo);
    }

    @Override
    public Optional<FoafProfileInfo> findById(Long id) {
        return this.foafProfileInfoRepository.findById(id);
    }

    @Override
    public FoafProfileInfo findByURI(String uri) {
        return this.foafProfileInfoRepository.findByUri(uri);
    }

    @Override
    public FoafProfileInfo findByEmail(String email) {
        return this.foafProfileInfoRepository.findByEmail(email);
    }

}
