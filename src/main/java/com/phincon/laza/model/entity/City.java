package com.phincon.laza.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Id
    private String cityId;

    @ManyToOne
    @JoinColumn(name = "province_id")
    @JsonManagedReference
    @JsonIgnore
    private Province provinces;

    private String type;
    private String cityName;
    private String postalCode;

    @OneToMany(mappedBy = "city")
    @JsonBackReference
    private List<Address> addresses;
}
