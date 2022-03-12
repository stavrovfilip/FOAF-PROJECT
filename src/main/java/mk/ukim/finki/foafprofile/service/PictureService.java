package mk.ukim.finki.foafprofile.service;

import mk.ukim.finki.foafprofile.model.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PictureService {
    Picture store(MultipartFile multipartFile) throws IOException;
    Picture findPictureById(Long id);
    Picture findPictureByName(String name);
    List<Picture> findAll();
    void delete(Long id);
}
