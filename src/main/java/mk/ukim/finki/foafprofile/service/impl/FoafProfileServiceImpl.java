package mk.ukim.finki.foafprofile.service.impl;

import mk.ukim.finki.foafprofile.model.FoafProfile;
import mk.ukim.finki.foafprofile.model.FoafProfileInfo;
import mk.ukim.finki.foafprofile.model.Friend;
import mk.ukim.finki.foafprofile.model.User;
import mk.ukim.finki.foafprofile.repository.FoafProfileRepository;
import mk.ukim.finki.foafprofile.repository.UserRepository;
import mk.ukim.finki.foafprofile.service.FoafProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;

import java.lang.String;

import java.io.*;
import java.util.List;

@Service
public class FoafProfileServiceImpl implements FoafProfileService {
    @Value("${foafprofile.file.path}")
    private String filePath;

    private final FoafProfileRepository foafProfileRepository;
    private final UserRepository userRepository;

    public FoafProfileServiceImpl(FoafProfileRepository foafProfileRepository, UserRepository userRepository) {
        this.foafProfileRepository = foafProfileRepository;
        this.userRepository = userRepository;
    }

    /**
     * Method for creating new FOAF profile
     *
     * @param foafProfileInfo
     * @param username
     * @return FoafProfile
     */
    @Override
    public FoafProfile createFoafProfile(FoafProfileInfo foafProfileInfo, String username) {
        User user = userRepository.findByUsername(username);
        Model model = ModelFactory.createDefaultModel();
        Resource foafProfile = this.createFoafProfileResource(model, foafProfileInfo);

        String fileName = foafProfileInfo.getUri();
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
        foafProfileRepository.save(newFoafProfile);
        user.setMyProfile(newFoafProfile);
        userRepository.save(user);
        return newFoafProfile;
    }

    /**
     * Method for updating already existing FOAF profile
     *
     * @param foafProfileuri
     * @param foafProfileInfo
     * @param username
     * @return FoafProfile
     */
    @Override
    public FoafProfile updateFoafProfile(String foafProfileuri, FoafProfileInfo foafProfileInfo, String username) {
        User user = userRepository.findByUsername(username);
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
        foafProfileRepository.save(foafProfileForUpdate);
        user.setMyProfile(foafProfileForUpdate);
        userRepository.save(user);
        return foafProfileForUpdate;
    }

    /**
     * Method for deleting already existing FOAF profile from database and file system
     *
     * @param uri
     * @param username
     */
    @Override
    public void deleteFoafProfile(String uri, String username) {
        User user = this.userRepository.findByUsername(username);
        user.setMyProfile(null);
        this.userRepository.save(user);
        this.foafProfileRepository.deleteById(uri);
        File fileToBeDeleted = new File(filePath + uri + ".rdf");
        fileToBeDeleted.delete();
    }

    /**
     * Method for getting FOAF profile associated with provided uri
     *
     * @param uri
     * @return FoafProfile
     */
    @Override
    public FoafProfile getFoafProfileByUri(String uri) {
        return foafProfileRepository.getById(uri);
    }

    /**
     * Method for returning all FOAF profiles created on this platform
     *
     * @return List<FoafProfile>
     */
    @Override
    public List<FoafProfile> findAll() {
        return this.foafProfileRepository.findAll();
    }

    /**
     * Method from converting FOAF profile from rdf to provided type
     *
     * @param uri
     * @param format
     * @return string - file path name
     */
    @Override
    public String convertFromRDF(String uri, String format) {
        FoafProfile foafProfile = this.foafProfileRepository.getById(uri);
        Model model = ModelFactory.createDefaultModel();
        model.read(foafProfile.getProfileFile(), "RDF/XML");
        String[] filePathNameList = foafProfile.getProfileFile().split("\\.");
        String type = null;
        if (format.equals("TURTLE")) {
            type = ".ttl";
        } else if (format.equals("N-TRIPLES")) {
            type = ".n3";
        } else if (format.equals("JSON-LD")) {
            type = ".jsonld";
        }
        String filePathName = filePathNameList[0] + type;
        FileWriter out = null;
        try {
            out = new FileWriter(filePathName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.write(out, format);
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePathName))) {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePathName;
    }

    /**
     * Method for generating FOAF profile with jena model
     *
     * @param model
     * @param foafProfileInfo
     * @return resource - new created FOAF model in jena
     */
    private Resource createFoafProfileResource(Model model, FoafProfileInfo foafProfileInfo) {

        String totalUri = "https://localhost:8080/profiles/" + foafProfileInfo.getUri();

        Resource foafProfile = model.createResource(totalUri, FOAF.Person)
                .addProperty(FOAF.firstName, foafProfileInfo.getFirstName())
                .addProperty(FOAF.lastName, foafProfileInfo.getLastName())
                .addProperty(FOAF.mbox_sha1sum, foafProfileInfo.getEmail());

        if (foafProfileInfo.getTitle() != null && !foafProfileInfo.getTitle().isEmpty()) {
            foafProfile.addProperty(FOAF.title, foafProfileInfo.getTitle());
        }
        if (foafProfileInfo.getNickName() != null && !foafProfileInfo.getNickName().isEmpty()) {
            foafProfile.addProperty(FOAF.nick, foafProfileInfo.getNickName());
        }
        if (foafProfileInfo.getPhoneNumber() != null && !foafProfileInfo.getPhoneNumber().isEmpty()) {
            foafProfile.addProperty(FOAF.phone, foafProfileInfo.getPhoneNumber());
        }
        if (foafProfileInfo.getHomepage() != null && !foafProfileInfo.getHomepage().isEmpty()) {
            foafProfile.addProperty(FOAF.homepage, foafProfileInfo.getHomepage());
        }
        if (foafProfileInfo.getPicture() != null && !foafProfileInfo.getPicture().getName().isEmpty()) {
            foafProfile.addProperty(FOAF.img, foafProfileInfo.getPicture().getName());
        }
        if (foafProfileInfo.getWorkHomepage() != null && !foafProfileInfo.getWorkHomepage().isEmpty()) {
            foafProfile.addProperty(FOAF.workplaceHomepage, foafProfileInfo.getWorkHomepage());
        }
        if (foafProfileInfo.getWorkDescription() != null && !foafProfileInfo.getWorkDescription().isEmpty()) {
            foafProfile.addProperty(FOAF.workInfoHomepage, foafProfileInfo.getWorkDescription());
        }
        if (foafProfileInfo.getSchoolHomepage() != null && !foafProfileInfo.getSchoolHomepage().isEmpty()) {
            foafProfile.addProperty(FOAF.schoolHomepage, foafProfileInfo.getSchoolHomepage());
        }

        List<Friend> myFriends = foafProfileInfo.getMyFriends();
        if (!myFriends.isEmpty()) {
            myFriends.forEach(myFriend -> {
                if (myFriend.getFoafProfileUri() != null || !myFriend.getFoafProfileUri().isEmpty()) {
                    foafProfile.addProperty(FOAF.knows, model.createResource(myFriend.getFoafProfileUri(), FOAF.Person)
                            .addProperty(FOAF.firstName, myFriend.getFirstName())
                            .addProperty(FOAF.lastName, myFriend.getLastName())
                            .addProperty(FOAF.mbox_sha1sum, myFriend.getEmail()));
                } else if (myFriend.getEmail() != null || !myFriend.getEmail().isEmpty()) {
                    String newUri = "https://localhost:8080/profiles/" + myFriend.getFirstName() + myFriend.getLastName();
                    foafProfile.addProperty(FOAF.knows, model.createResource(newUri, FOAF.Person)
                            .addProperty(FOAF.firstName, myFriend.getFirstName())
                            .addProperty(FOAF.lastName, myFriend.getLastName())
                            .addProperty(FOAF.mbox_sha1sum, myFriend.getEmail()));
                }
            });

        }
        return foafProfile;

    }
}
