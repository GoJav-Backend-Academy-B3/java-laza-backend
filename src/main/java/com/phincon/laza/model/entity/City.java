package com.phincon.laza.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "cities")
@Entity
public class City {
//     "city_id": "3",
//             "province_id": "21",
//             "province": "Nanggroe Aceh Darussalam (NAD)",
//             "type": "Kabupaten",
//             "city_name": "Aceh Besar",
//             "postal_code": "23951"

    @Id
    private String cityId;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province provinces;
    private String type;
    private String cityName;
    private String postalCode;
}
