package mk.ukim.finki.foafprofile.service.impl;

import mk.ukim.finki.foafprofile.model.FoafProfile;
import mk.ukim.finki.foafprofile.model.FoafProfileInfo;
import mk.ukim.finki.foafprofile.model.Friend;
import mk.ukim.finki.foafprofile.repository.FoafProfileRepository;
import mk.ukim.finki.foafprofile.service.FoafProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;

import java.lang.String;

import java.io.*;
import java.util.List;

@Service
public class FoafProfileImpl implements FoafProfileService {
    @Value("${foafprofile.file.path}")
    private String filePath;

    private final FoafProfileRepository foafProfileRepository;

    public FoafProfileImpl(FoafProfileRepository foafProfileRepository) {
        this.foafProfileRepository = foafProfileRepository;
    }

    @Override
    public FoafProfile createFoafProfile(FoafProfileInfo foafProfileInfo) {
        Model model = ModelFactory.createDefaultModel();
        Resource foafProfile = this.createFoafProfileResource(model, foafProfileInfo);

        //za ovoj del ne sum sigurna
        String fileName = foafProfileInfo.getFirstName() + foafProfileInfo.getLastName();
        String filePathName = filePath + fileName + ".rdf";
        FileWriter out = null;
        try {
            out = new FileWriter(filePathName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.write(out, "RDF/XML");
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File sendFile = new File(filePathName);

        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePathName))) {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String wholeFileInString = contentBuilder.toString();

        FoafProfile newFoafProfile = new FoafProfile(foafProfileInfo.getUri(), wholeFileInString, filePathName, foafProfileInfo);

        return foafProfileRepository.save(newFoafProfile);
    }

    @Override
    public FoafProfile updateFoafProfile(String foafProfileuri, FoafProfileInfo foafProfileInfo) {
        Model model = ModelFactory.createDefaultModel();
        Resource foafProfile = this.createFoafProfileResource(model, foafProfileInfo);
        FoafProfile foafProfileForUpdate = this.getFoafProfileByUri(foafProfileuri);
        String fileName = foafProfileInfo.getFirstName() + foafProfileInfo.getLastName();
        String filePathName = filePath + fileName;
        FileWriter out = null;
        try {
            out = new FileWriter(filePathName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.write(out, "RDF/XML");
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File sendFile = new File(filePathName);

        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePathName))) {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String wholeFileInString = contentBuilder.toString();
        foafProfileForUpdate.setProfile(wholeFileInString);
        foafProfileForUpdate.setProfileFile(filePathName);
        foafProfileForUpdate.setFoafProfileInfo(foafProfileInfo);

        return foafProfileRepository.save(foafProfileForUpdate);
    }

    @Override
    public FoafProfile getFoafProfileByUri(String uri) {
        return foafProfileRepository.getById(uri);
    }

    private Resource createFoafProfileResource(Model model, FoafProfileInfo foafProfileInfo) {

        String totalUri = "https://localhost:8080/profiles/" + foafProfileInfo.getUri();

        Resource foafProfile = model.createResource(totalUri, FOAF.Person)
                .addProperty(FOAF.firstName, foafProfileInfo.getFirstName())
                .addProperty(FOAF.lastName, foafProfileInfo.getLastName())
                .addProperty(FOAF.mbox_sha1sum, foafProfileInfo.getEmail());

        //uri, title, firstName, lastName, nickName,
        //                email, homepage, phoneNumber, picture, workHomepage, workDescription, schoolHomepage, myFriends

        if (foafProfileInfo.getTitle() != null) {
            foafProfile.addProperty(FOAF.title, foafProfileInfo.getTitle());
        }
        if (foafProfileInfo.getNickName() != null) {
            foafProfile.addProperty(FOAF.nick, foafProfileInfo.getNickName());
        }
        if (foafProfileInfo.getPhoneNumber() != null) {
            foafProfile.addProperty(FOAF.phone, foafProfileInfo.getPhoneNumber());
        }
        if (foafProfileInfo.getHomepage() != null) {
            foafProfile.addProperty(FOAF.homepage, foafProfileInfo.getHomepage());
        }
        if (foafProfileInfo.getPicture() != null) {
            foafProfile.addProperty(FOAF.img, foafProfileInfo.getPicture().getName());
        }
        if (foafProfileInfo.getWorkHomepage() != null) {
            foafProfile.addProperty(FOAF.workplaceHomepage, foafProfileInfo.getWorkHomepage());
        }
        if (foafProfileInfo.getWorkDescription() != null) {
            foafProfile.addProperty(FOAF.workInfoHomepage, foafProfileInfo.getWorkDescription());
        }
        if (foafProfileInfo.getSchoolHomepage() != null) {
            foafProfile.addProperty(FOAF.schoolHomepage, foafProfileInfo.getSchoolHomepage());
        }

        List<Friend> myFriends = foafProfileInfo.getMyFriends();
        if (!myFriends.isEmpty()) {
            myFriends.forEach(myFriend -> {
                foafProfile.addProperty(FOAF.knows, model.createResource(myFriend.getFoafProfileUri(), FOAF.Person)
                        .addProperty(FOAF.firstName, myFriend.getFirstName())
                        .addProperty(FOAF.lastName, myFriend.getLastName())
                        .addProperty(FOAF.mbox_sha1sum, myFriend.getEmail()));
            });
        }
        return foafProfile;

    }
}
