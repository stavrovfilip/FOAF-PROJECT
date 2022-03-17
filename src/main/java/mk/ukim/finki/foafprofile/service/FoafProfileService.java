package mk.ukim.finki.foafprofile.service;

import mk.ukim.finki.foafprofile.model.FoafProfile;
import mk.ukim.finki.foafprofile.model.FoafProfileInfo;

public interface FoafProfileService {
    //TO DO
    public FoafProfile createFoafProfile(FoafProfileInfo foafProfileInfo);

    public FoafProfile updateFoafProfile(String foafProfileuri, FoafProfileInfo foafProfileInfo);

    public FoafProfile getFoafProfileByUri(String uri);
}
