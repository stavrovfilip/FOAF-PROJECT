package mk.ukim.finki.foafprofile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendDto {
    private String friendFirstName;
    private String friendLastName;
    private String friendEmail;
    private String friendFoafUri;
}
