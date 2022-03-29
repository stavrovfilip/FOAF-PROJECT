package mk.ukim.finki.foafprofile.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.foafprofile.model.Picture;
import mk.ukim.finki.foafprofile.model.exceptions.InvalidImageIdException;
import mk.ukim.finki.foafprofile.model.exceptions.InvalidImageNameException;
import mk.ukim.finki.foafprofile.repository.PictureRepository;
import mk.ukim.finki.foafprofile.service.PictureService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Base64;

@AllArgsConstructor
@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;

    /**
     * Storing new picture in database
     *
     * @param multipartFile
     * @return Picture
     * @throws IOException
     */
    @Override
    public Picture store(MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile).getOriginalFilename());
        Picture picture = new Picture(fileName, multipartFile.getContentType(), multipartFile.getBytes());

        return this.pictureRepository.save(picture);
    }

    /**
     * Finding picture associated with provided id
     *
     * @param id
     * @return Picture
     */
    @Override
    public Picture findPictureById(Long id) {
        return this.pictureRepository.findById(id).orElseThrow(() -> new InvalidImageIdException());
    }

    /**
     * Finding picture associated with provided name
     *
     * @param name
     * @return
     */
    @Override
    public Picture findPictureByName(String name) {
        return this.pictureRepository.findPictureByName(name).orElseThrow(() -> new InvalidImageNameException());
    }

    /**
     * Listing all pictures stored in database
     *
     * @return List<Picture>
     */
    @Override
    public List<Picture> findAll() {
        return this.pictureRepository.findAll();
    }

    /**
     * Deleting picture from database
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        this.pictureRepository.deleteById(id);
    }
}
