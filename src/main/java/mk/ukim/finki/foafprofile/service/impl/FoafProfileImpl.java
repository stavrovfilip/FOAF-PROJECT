package mk.ukim.finki.foafprofile.service.impl;

import mk.ukim.finki.foafprofile.model.FoafProfile;
import mk.ukim.finki.foafprofile.model.FoafProfileInfo;
import mk.ukim.finki.foafprofile.model.Friend;
import mk.ukim.finki.foafprofile.service.FoafProfileService;
import org.springframework.stereotype.Service;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.VCARD;

import java.util.List;

@Service
public class FoafProfileImpl implements FoafProfileService {
    @Override
    public FoafProfile createFoafProfile(FoafProfileInfo foafProfileInfo) {

        Model model = ModelFactory.createDefaultModel();

        Resource foafProfile = model.createResource(foafProfileInfo.getUri(), FOAF.Person)
                .addProperty(FOAF.firstName, foafProfileInfo.getFirstName())
                .addProperty(FOAF.lastName, foafProfileInfo.getLastName())
                .addProperty(FOAF.mbox_sha1sum, foafProfileInfo.getEmail());

        //uri, title, firstName, lastName, nickName,
        //                email, homepage, phoneNumber, picture, workHomepage, workDescription, schoolHomepage, myFriends

        if(foafProfileInfo.getTitle() != null){
            foafProfile.addProperty(FOAF.title, foafProfileInfo.getTitle());
        }
        if(foafProfileInfo.getNickName() != null){
            foafProfile.addProperty(FOAF.nick, foafProfileInfo.getNickName());
        }
        if(foafProfileInfo.getPhoneNumber() != null){
            foafProfile.addProperty(FOAF.phone, foafProfileInfo.getPhoneNumber());
        }
        if(foafProfileInfo.getHomepage() != null){
            foafProfile.addProperty(FOAF.homepage, foafProfileInfo.getHomepage());
        }
        if(foafProfileInfo.getPicture() != null){
            foafProfile.addProperty(FOAF.img, (RDFNode) foafProfileInfo.getPicture());
        }
        if(foafProfileInfo.getWorkHomepage() != null){
            foafProfile.addProperty(FOAF.workplaceHomepage, foafProfileInfo.getWorkHomepage());
        }
        if(foafProfileInfo.getWorkDescription() != null){
            foafProfile.addProperty(FOAF.workInfoHomepage, foafProfileInfo.getWorkDescription());
        }
        if(foafProfileInfo.getSchoolHomepage() != null){
            foafProfile.addProperty(FOAF.schoolHomepage, foafProfileInfo.getSchoolHomepage());
        }

        List<Friend> myFriends = foafProfileInfo.getMyFriends();
        if(!myFriends.isEmpty()){
            myFriends.forEach(myFriend -> {
                foafProfile.addProperty(FOAF.knows, model.createResource(myFriend.getFoafProfileUri(), FOAF.Person)
                        .addProperty(FOAF.firstName, myFriend.getFirstName())
                        .addProperty(FOAF.lastName, myFriend.getLastName())
                        .addProperty(FOAF.mbox_sha1sum, myFriend.getEmail()));
            });
        }
        /*
        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = false;
        String fileName = RandomStringUtils.random(length, useLetters, useNumbers);
        FileWriter out = new FileWriter(localPath + fileName);
        try {
            model.write(out, "RDF/XML");
        } finally {
            try {
                out.close();
            } catch (IOException closeException) {
            }
        }
        final File sendFile = new File(localPath + fileName);
         */
        return null;
    }

    @Override
    public FoafProfile updateFoafProfile(FoafProfileInfo foafProfileInfo) {
        return null;
    }

    @Override
    public FoafProfile getFoafProfileByUri(String uri) {
        return null;
    }
}
