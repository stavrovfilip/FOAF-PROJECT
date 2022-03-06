package mk.ukim.finki.foafprofile.service;

import mk.ukim.finki.foafprofile.model.FoafProfile;
import mk.ukim.finki.foafprofile.model.FoafProfileInfo;

public interface FoafProfileService {
    //TO DO
    public FoafProfile createFoafProfile(FoafProfileInfo foafProfileInfo);

    public FoafProfile updateFoafProfile(FoafProfileInfo foafProfileInfo);
}
