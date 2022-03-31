package mk.ukim.finki.foafprofile.service;

import mk.ukim.finki.foafprofile.model.FoafProfile;
import mk.ukim.finki.foafprofile.model.FoafProfileInfo;
import java.util.List;

public interface FoafProfileService {

    public FoafProfile createFoafProfile(FoafProfileInfo foafProfileInfo, String username);

    public FoafProfile updateFoafProfile(String foafProfileuri, FoafProfileInfo foafProfileInfo, String username);

    public void deleteFoafProfile(String uri, String username);

    public FoafProfile getFoafProfileByUri(String uri);

    public List<FoafProfile> findAll();

    public String convertFromRDF(String uri, String format);

}
