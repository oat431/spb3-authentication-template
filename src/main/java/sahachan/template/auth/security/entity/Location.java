package sahachan.template.auth.security.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    Long id;

    String subDistrict;
    String district;
    String province;

    @OneToOne(mappedBy = "location",cascade = CascadeType.ALL)
    User user;

    public String showLocation(){
        return "ตำบล"+ subDistrict + " อำเภอ" + district + " จังหวัด" + province;
    }
}
