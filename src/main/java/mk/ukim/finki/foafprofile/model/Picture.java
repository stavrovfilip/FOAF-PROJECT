package mk.ukim.finki.foafprofile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    @Column(length = 10000000)
    private byte[] data;

    public Picture(){}

    public Picture(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

}
